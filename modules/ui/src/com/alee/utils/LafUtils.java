/*
 * This file is part of WebLookAndFeel library.
 *
 * WebLookAndFeel library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WebLookAndFeel library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with WebLookAndFeel library.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.alee.utils;

import com.alee.laf.LookAndFeelException;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.rootpane.WebRootPaneUI;
import com.alee.managers.style.*;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.RootPaneUI;
import java.awt.*;

/**
 * This class provides a set of L&F utilities for core WebLaF components.
 *
 * @author Mikle Garin
 */
public final class LafUtils
{
    /**
     * Private constructor to avoid instantiation.
     */
    private LafUtils ()
    {
        throw new UtilityException ( "Utility classes are not meant to be instantiated" );
    }

    /**
     * Returns whether {@link Window} in which specified {@link Component} located is decorated by LaF or not.
     *
     * @param component {@link Component} used to determine {@link Window} decoration state
     * @return true if {@link Window} in which specified {@link Component} located is decorated by LaF, false otherwise
     */
    public static boolean isInDecoratedWindow ( final Component component )
    {
        final JRootPane rootPane = CoreSwingUtils.getRootPane ( component );
        if ( rootPane != null )
        {
            final RootPaneUI ui = rootPane.getUI ();
            if ( ui instanceof WebRootPaneUI )
            {
                return ( ( WebRootPaneUI ) ui ).isDecorated ();
            }
        }
        return false;
    }

    /**
     * Returns whether or not specified {@link JComponent} uses {@link ComponentUI}.
     *
     * @param component {@link JComponent} to check {@link ComponentUI} usage in
     * @return {@code true} if specified {@link JComponent} uses {@link ComponentUI}, {@code false} otherwise
     */
    public static boolean hasUI ( final JComponent component )
    {
        return ReflectUtils.hasMethod ( component, "getUI" );
    }

    /**
     * Returns {@link ComponentUI} or {@code null} if UI cannot be retrieved.
     *
     * @param component {@link JComponent} to retrieve UI from
     * @param <U>       {@link ComponentUI} class type
     * @return {@link ComponentUI} or {@code null} if UI cannot be retrieved
     */
    public static <U extends ComponentUI> U getUI ( final JComponent component )
    {
        try
        {
            return ReflectUtils.callMethod ( component, "getUI" );
        }
        catch ( final Exception e )
        {
            throw new StyleException ( "Unable to retrieve component UI: " + component, e );
        }
    }

    /**
     * Setup provided {@link ComponentUI} into specified {@link JComponent}.
     *
     * @param component {@link JComponent} to setup {@link ComponentUI} for
     * @param ui        {@link ComponentUI}
     */
    public static void setUI ( final JComponent component, final ComponentUI ui )
    {
        try
        {
            ReflectUtils.callMethod ( component, "setUI", ui );
        }
        catch ( final Exception e )
        {
            throw new StyleException ( "Unable to setup component UI: " + component, e );
        }
    }

    /**
     * Returns whether or not specified {@link JComponent} uses WebLaF {@link ComponentUI}.
     *
     * @param component {@link JComponent} to check WebLaF {@link ComponentUI} usage in
     * @return {@code true} if specified {@link JComponent} uses WebLaF {@link ComponentUI}, {@code false} otherwise
     */
    public static boolean hasWebLafUI ( final JComponent component )
    {
        final boolean webUI;
        if ( StyleManager.isSupported ( component ) )
        {
            // Checking that currently installed UI is compatible with base UI class for this component
            // For instance base UI class for JButton is WButtonUI, so WebButtonUI would be compatible and MetalButtonUI won't be
            final ComponentUI ui = getUI ( component );
            final ComponentDescriptor descriptor = StyleManager.getDescriptor ( component );
            webUI = ui != null && descriptor.getBaseUIClass ().isAssignableFrom ( ui.getClass () );
        }
        else
        {
            // This might be the case when LaF is not installed and another LaF is used across all common J-components
            // Also it could be the case for complex components like WebScrollPane, look at issue #458 for more details on that case
            webUI = false;
        }
        return webUI;
    }

    /**
     * Returns X and Y shift for the specified text and font metrics.
     * It will return values you need to add to the point relative to which you want to center text.
     *
     * @param metrics font metrics
     * @param text    text
     * @return X and Y shift for the specified text and font metrics
     */
    public static Point getTextCenterShift ( final FontMetrics metrics, final String text )
    {
        return new Point ( getTextCenterShiftX ( metrics, text ), getTextCenterShiftY ( metrics ) );
    }

    /**
     * Returns X shift for the specified text and font metrics.
     * It will return value you need to add to the point X coordinate relative to which you want to horizontally center text.
     *
     * @param metrics font metrics
     * @param text    text
     * @return X shift for the specified text and font metrics
     */
    public static int getTextCenterShiftX ( final FontMetrics metrics, final String text )
    {
        return -metrics.stringWidth ( text ) / 2;
    }

    /**
     * Returns Y shift for the specified font metrics.
     * It will return value you need to add to the point Y coordinate relative to which you want to vertically center text.
     *
     * @param metrics font metrics
     * @return Y shift for the specified font metrics
     */
    public static int getTextCenterShiftY ( final FontMetrics metrics )
    {
        return ( metrics.getAscent () - metrics.getLeading () - metrics.getDescent () ) / 2;
    }

    /**
     * Returns {@link Shape} of the specified {@link JComponent}. If it is not possible to determine actual {@link Shape}
     * then {@link Rectangle} representing {@link JComponent} bounds within its coordinates is returned.
     *
     * @param component {@link JComponent} to process
     * @return {@link Shape} of the specified {@link JComponent}
     */
    public static Shape getShape ( final JComponent component )
    {
        if ( component instanceof ShapeSupport )
        {
            final ShapeSupport shapeSupport = ( ShapeSupport ) component;
            return shapeSupport.getShape ();
        }
        else
        {
            final ComponentUI ui = getUI ( component );
            if ( ui != null && ui instanceof ShapeSupport )
            {
                final ShapeSupport shapeSupport = ( ShapeSupport ) ui;
                return shapeSupport.getShape ();
            }
        }
        return BoundsType.margin.bounds ( component );
    }

    /**
     * Installs specified LaF as current application's LaF.
     *
     * @param clazz LaF class
     * @throws LookAndFeelException when unable to install specified LaF
     */
    public static void setupLookAndFeel ( final Class<? extends LookAndFeel> clazz ) throws LookAndFeelException
    {
        setupLookAndFeel ( clazz.getCanonicalName () );
    }

    /**
     * Installs specified LaF as current application's LaF.
     *
     * @param className LaF canonical class name
     * @throws LookAndFeelException when unable to install specified LaF
     */
    public static void setupLookAndFeel ( final String className ) throws LookAndFeelException
    {
        try
        {
            UIManager.setLookAndFeel ( className );
        }
        catch ( final Exception e )
        {
            throw new RuntimeException ( "Unable to initialize LaF for class name: " + className, e );
        }
    }

    /**
     * Installs default settings into the specified {@code component}.
     *
     * @param component component to install default settings into
     * @param prefix    component type prefix
     */
    public static void installDefaults ( final JComponent component, final String prefix )
    {
        if ( SwingUtils.isUIResource ( component.getFont () ) )
        {
            component.setFont ( UIManager.getFont ( prefix + WebLookAndFeel.FONT_PROPERTY ) );
        }
        if ( SwingUtils.isUIResource ( component.getBackground () ) )
        {
            component.setBackground ( UIManager.getColor ( prefix + WebLookAndFeel.BACKGROUND_PROPERTY ) );
        }
        if ( SwingUtils.isUIResource ( component.getForeground () ) )
        {
            component.setForeground ( UIManager.getColor ( prefix + WebLookAndFeel.FOREGROUND_PROPERTY ) );
        }
    }

    /**
     * Uninstalls default settings from the specified {@code component}.
     *
     * @param component component to uninstall default settings from
     */
    public static void uninstallDefaults ( final JComponent component )
    {
        if ( SwingUtils.isUIResource ( component.getForeground () ) )
        {
            component.setForeground ( null );
        }
        if ( SwingUtils.isUIResource ( component.getBackground () ) )
        {
            component.setBackground ( null );
        }
        if ( SwingUtils.isUIResource ( component.getFont () ) )
        {
            component.setFont ( null );
        }
        LookAndFeel.uninstallBorder ( component );
    }
}