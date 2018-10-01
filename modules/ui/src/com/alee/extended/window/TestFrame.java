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

package com.alee.extended.window;

import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.version.VersionManager;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SystemUtils;

import javax.swing.*;
import java.awt.*;

/**
 * This class provides a quick way to open frame with the specified content and some other settings.
 * It suits best for writing UI test applications.
 *
 * @author Mikle Garin
 */
public class TestFrame extends JFrame
{
    /**
     * Main components container.
     */
    protected final JPanel container;

    /**
     * Displays and returns test frame with the specified content and settings.
     *
     * @param component component to display
     * @return displayed test frame
     */
    public static TestFrame show ( final Component component )
    {
        return new TestFrame ( component ).displayFrame ();
    }

    /**
     * Constructs test frame with the specified content and settings.
     *
     * @param component component to display
     */
    public TestFrame ( final Component component )
    {
        this ( new BorderLayout (), component );
    }

    /**
     * Displays and returns test frame with the specified content and settings.
     *
     * @param component component to display
     * @param margin    container margin
     * @return displayed test frame
     */
    public static TestFrame show ( final Component component, final int margin )
    {
        return new TestFrame ( component, margin ).displayFrame ();
    }

    /**
     * Constructs test frame with the specified content and settings.
     *
     * @param component component to display
     * @param margin    container margin
     */
    public TestFrame ( final Component component, final int margin )
    {
        this ( component, margin, margin, margin, margin );
    }

    /**
     * Displays and returns test frame with the specified content and settings.
     *
     * @param component component to display
     * @param top       container top margin
     * @param left      container left margin
     * @param bottom    container bottom margin
     * @param right     container right margin
     * @return displayed test frame
     */
    public static TestFrame show ( final Component component, final int top, final int left, final int bottom, final int right )
    {
        return new TestFrame ( component, top, left, bottom, right ).displayFrame ();
    }

    /**
     * Constructs test frame with the specified content and settings.
     *
     * @param component component to display
     * @param top       container top margin
     * @param left      container left margin
     * @param bottom    container bottom margin
     * @param right     container right margin
     */
    public TestFrame ( final Component component, final int top, final int left, final int bottom, final int right )
    {
        this ( component, new Insets ( top, left, bottom, right ) );
    }

    /**
     * Displays and returns test frame with the specified content and settings.
     *
     * @param component component to display
     * @param margin    container margin
     * @return displayed test frame
     */
    public static TestFrame show ( final Component component, final Insets margin )
    {
        return new TestFrame ( component, margin ).displayFrame ();
    }

    /**
     * Constructs test frame with the specified content and settings.
     *
     * @param component component to display
     * @param margin    container margin
     */
    public TestFrame ( final Component component, final Insets margin )
    {
        this ( new BorderLayout (), component, BorderLayout.CENTER, margin );
    }

    /**
     * Displays and returns test frame with the specified content and settings.
     *
     * @param layout    container layout
     * @param component component to display
     * @return displayed test frame
     */
    public static TestFrame show ( final LayoutManager layout, final Component component )
    {
        return new TestFrame ( layout, component ).displayFrame ();
    }

    /**
     * Constructs test frame with the specified content and settings.
     *
     * @param layout    container layout
     * @param component component to display
     */
    public TestFrame ( final LayoutManager layout, final Component component )
    {
        this ( layout, component, null, null );
    }

    /**
     * Displays and returns test frame with the specified content and settings.
     *
     * @param layout    container layout
     * @param component component to display
     * @param margin    container margin
     * @return displayed test frame
     */
    public static TestFrame show ( final LayoutManager layout, final Component component, final int margin )
    {
        return new TestFrame ( layout, component, margin ).displayFrame ();
    }

    /**
     * Constructs test frame with the specified content and settings.
     *
     * @param layout    container layout
     * @param component component to display
     * @param margin    container margin
     */
    public TestFrame ( final LayoutManager layout, final Component component, final int margin )
    {
        this ( layout, component, new Insets ( margin, margin, margin, margin ) );
    }

    /**
     * Displays and returns test frame with the specified content and settings.
     *
     * @param layout    container layout
     * @param component component to display
     * @param top       container top margin
     * @param left      container left margin
     * @param bottom    container bottom margin
     * @param right     container right margin
     * @return displayed test frame
     */
    public static TestFrame show ( final LayoutManager layout, final Component component,
                                   final int top, final int left, final int bottom, final int right )
    {
        return new TestFrame ( layout, component, top, left, bottom, right ).displayFrame ();
    }

    /**
     * Constructs test frame with the specified content and settings.
     *
     * @param layout    container layout
     * @param component component to display
     * @param top       container top margin
     * @param left      container left margin
     * @param bottom    container bottom margin
     * @param right     container right margin
     */
    public TestFrame ( final LayoutManager layout, final Component component,
                       final int top, final int left, final int bottom, final int right )
    {
        this ( layout, component, new Insets ( top, left, bottom, right ) );
    }

    /**
     * Displays and returns test frame with the specified content and settings.
     *
     * @param layout    container layout
     * @param component component to display
     * @param margin    container margin
     * @return displayed test frame
     */
    public static TestFrame show ( final LayoutManager layout, final Component component, final Insets margin )
    {
        return new TestFrame ( layout, component, margin ).displayFrame ();
    }

    /**
     * Constructs test frame with the specified content and settings.
     *
     * @param layout    container layout
     * @param component component to display
     * @param margin    container margin
     */
    public TestFrame ( final LayoutManager layout, final Component component, final Insets margin )
    {
        this ( layout, component, null, margin );
    }

    /**
     * Displays and returns test frame with the specified content and settings.
     *
     * @param layout      container layout
     * @param component   component to display
     * @param constraints component layout constraints
     * @param margin      container margin
     * @return displayed test frame
     */
    public static TestFrame show ( final LayoutManager layout, final Component component, final Object constraints, final Insets margin )
    {
        return new TestFrame ( layout, component, constraints, margin ).displayFrame ();
    }

    /**
     * Constructs test frame with the specified content and settings.
     *
     * @param layout      container layout
     * @param component   component to display
     * @param constraints component layout constraints
     * @param margin      container margin
     */
    public TestFrame ( final LayoutManager layout, final Component component, final Object constraints, final Insets margin )
    {
        super ( getFrameTitle ( component ) );
        setLayout ( new BorderLayout () );

        container = new JPanel ( layout );
        if ( margin != null )
        {
            container.setBorder ( BorderFactory.createEmptyBorder ( margin.top, margin.left, margin.bottom, margin.right ) );
        }
        if ( constraints != null )
        {
            container.add ( component, constraints );
        }
        else
        {
            container.add ( component );
        }
        add ( container, BorderLayout.CENTER );

        configureFrame ();
    }

    /**
     * Displays and returns test frame with the specified content and settings.
     *
     * @param horizontal whether should place components horizontally or not
     * @param components components to display
     * @return displayed test frame
     */
    public static TestFrame show ( final boolean horizontal, final Component... components )
    {
        return new TestFrame ( horizontal, components ).displayFrame ();
    }

    /**
     * Constructs test frame with the specified content and settings.
     *
     * @param horizontal whether should place components horizontally or not
     * @param components components to display
     */
    public TestFrame ( final boolean horizontal, final Component... components )
    {
        this ( 0, horizontal, components );
    }

    /**
     * Displays and returns test frame with the specified content and settings.
     *
     * @param gap        gap between components
     * @param horizontal whether should place components horizontally or not
     * @param components components to display
     * @return displayed test frame
     */
    public static TestFrame show ( final int gap, final boolean horizontal, final Component... components )
    {
        return new TestFrame ( gap, horizontal, components ).displayFrame ();
    }

    /**
     * Constructs test frame with the specified content and settings.
     *
     * @param gap        gap between components
     * @param horizontal whether should place components horizontally or not
     * @param components components to display
     */
    public TestFrame ( final int gap, final boolean horizontal, final Component... components )
    {
        this ( horizontal ? new HorizontalFlowLayout ( gap, false ) :
                new VerticalFlowLayout ( VerticalFlowLayout.TOP, 0, gap, true, false ), components );
    }

    /**
     * Displays and returns test frame with the specified content and settings.
     *
     * @param layout     container layout
     * @param components components to display
     * @return displayed test frame
     */
    public static TestFrame show ( final LayoutManager layout, final Component... components )
    {
        return new TestFrame ( layout, components ).displayFrame ();
    }

    /**
     * Constructs test frame with the specified content and settings.
     *
     * @param layout     container layout
     * @param components components to display
     */
    public TestFrame ( final LayoutManager layout, final Component... components )
    {
        this ( layout, null, components );
    }

    /**
     * Displays and returns test frame with the specified content and settings.
     *
     * @param horizontal whether should place components horizontally or not
     * @param margin     container margin
     * @param components components to display
     * @return displayed test frame
     */
    public static TestFrame show ( final boolean horizontal, final int margin, final Component... components )
    {
        return new TestFrame ( horizontal, margin, components ).displayFrame ();
    }

    /**
     * Constructs test frame with the specified content and settings.
     *
     * @param horizontal whether should place components horizontally or not
     * @param margin     container margin
     * @param components components to display
     */
    public TestFrame ( final boolean horizontal, final int margin, final Component... components )
    {
        this ( horizontal, margin, margin, margin, margin, components );
    }

    /**
     * Displays and returns test frame with the specified content and settings.
     *
     * @param horizontal whether should place components horizontally or not
     * @param top        top container margin
     * @param left       left container margin
     * @param bottom     bottom container margin
     * @param right      right container margin
     * @param components components to display
     * @return displayed test frame
     */
    public static TestFrame show ( final boolean horizontal, final int top, final int left, final int bottom, final int right,
                                   final Component... components )
    {
        return new TestFrame ( horizontal, top, left, bottom, right, components ).displayFrame ();
    }

    /**
     * Constructs test frame with the specified content and settings.
     *
     * @param horizontal whether should place components horizontally or not
     * @param top        top container margin
     * @param left       left container margin
     * @param bottom     bottom container margin
     * @param right      right container margin
     * @param components components to display
     */
    public TestFrame ( final boolean horizontal, final int top, final int left, final int bottom, final int right,
                       final Component... components )
    {
        this ( horizontal, new Insets ( top, left, bottom, right ), components );
    }

    /**
     * Displays and returns test frame with the specified content and settings.
     *
     * @param horizontal whether should place components horizontally or not
     * @param margin     container margin
     * @param components components to display
     * @return displayed test frame
     */
    public static TestFrame show ( final boolean horizontal, final Insets margin, final Component... components )
    {
        return new TestFrame ( horizontal, margin, components ).displayFrame ();
    }

    /**
     * Constructs test frame with the specified content and settings.
     *
     * @param horizontal whether should place components horizontally or not
     * @param margin     container margin
     * @param components components to display
     */
    public TestFrame ( final boolean horizontal, final Insets margin, final Component... components )
    {
        this ( 0, horizontal, margin, components );
    }

    /**
     * Displays and returns test frame with the specified content and settings.
     *
     * @param gap        gap between components
     * @param horizontal whether should place components horizontally or not
     * @param margin     container margin
     * @param components components to display
     * @return displayed test frame
     */
    public static TestFrame show ( final int gap, final boolean horizontal, final int margin, final Component... components )
    {
        return new TestFrame ( gap, horizontal, margin, components ).displayFrame ();
    }

    /**
     * Constructs test frame with the specified content and settings.
     *
     * @param gap        gap between components
     * @param horizontal whether should place components horizontally or not
     * @param margin     container margin
     * @param components components to display
     */
    public TestFrame ( final int gap, final boolean horizontal, final int margin, final Component... components )
    {
        this ( gap, horizontal, margin, margin, margin, margin, components );
    }

    /**
     * Displays and returns test frame with the specified content and settings.
     *
     * @param gap        gap between components
     * @param horizontal whether should place components horizontally or not
     * @param top        top container margin
     * @param left       left container margin
     * @param bottom     bottom container margin
     * @param right      right container margin
     * @param components components to display
     * @return displayed test frame
     */
    public static TestFrame show ( final int gap, final boolean horizontal, final int top, final int left, final int bottom,
                                   final int right, final Component... components )
    {
        return new TestFrame ( gap, horizontal, top, left, bottom, right, components ).displayFrame ();
    }

    /**
     * Constructs test frame with the specified content and settings.
     *
     * @param gap        gap between components
     * @param horizontal whether should place components horizontally or not
     * @param top        top container margin
     * @param left       left container margin
     * @param bottom     bottom container margin
     * @param right      right container margin
     * @param components components to display
     */
    public TestFrame ( final int gap, final boolean horizontal, final int top, final int left, final int bottom, final int right,
                       final Component... components )
    {
        this ( gap, horizontal, new Insets ( top, left, bottom, right ), components );
    }

    /**
     * Displays and returns test frame with the specified content and settings.
     *
     * @param gap        gap between components
     * @param horizontal whether should place components horizontally or not
     * @param margin     container margin
     * @param components components to display
     * @return displayed test frame
     */
    public static TestFrame show ( final int gap, final boolean horizontal, final Insets margin, final Component... components )
    {
        return new TestFrame ( gap, horizontal, margin, components ).displayFrame ();
    }

    /**
     * Constructs test frame with the specified content and settings.
     *
     * @param gap        gap between components
     * @param horizontal whether should place components horizontally or not
     * @param margin     container margin
     * @param components components to display
     */
    public TestFrame ( final int gap, final boolean horizontal, final Insets margin, final Component... components )
    {
        this ( horizontal ? new HorizontalFlowLayout ( gap, false ) :
                new VerticalFlowLayout ( VerticalFlowLayout.TOP, 0, gap, true, false ), margin, components );
    }

    /**
     * Displays and returns test frame with the specified content and settings.
     *
     * @param layout     container layout
     * @param margin     container margin
     * @param components components to display
     * @return displayed test frame
     */
    public static TestFrame show ( final LayoutManager layout, final int margin, final Component... components )
    {
        return new TestFrame ( layout, margin, components ).displayFrame ();
    }

    /**
     * Constructs test frame with the specified content and settings.
     *
     * @param layout     container layout
     * @param margin     container margin
     * @param components components to display
     */
    public TestFrame ( final LayoutManager layout, final int margin, final Component... components )
    {
        this ( layout, margin, margin, margin, margin, components );
    }

    /**
     * Displays and returns test frame with the specified content and settings.
     *
     * @param layout     container layout
     * @param top        top container margin
     * @param left       left container margin
     * @param bottom     bottom container margin
     * @param right      right container margin
     * @param components components to display
     * @return displayed test frame
     */
    public static TestFrame show ( final LayoutManager layout, final int top, final int left, final int bottom, final int right,
                                   final Component... components )
    {
        return new TestFrame ( layout, top, left, bottom, right, components ).displayFrame ();
    }

    /**
     * Constructs test frame with the specified content and settings.
     *
     * @param layout     container layout
     * @param top        top container margin
     * @param left       left container margin
     * @param bottom     bottom container margin
     * @param right      right container margin
     * @param components components to display
     */
    public TestFrame ( final LayoutManager layout, final int top, final int left, final int bottom, final int right,
                       final Component... components )
    {
        this ( layout, new Insets ( top, left, bottom, right ), components );
    }

    /**
     * Displays and returns test frame with the specified content and settings.
     *
     * @param layout     container layout
     * @param margin     container margin
     * @param components components to display
     * @return displayed test frame
     */
    public static TestFrame show ( final LayoutManager layout, final Insets margin, final Component... components )
    {
        return new TestFrame ( layout, margin, components ).displayFrame ();
    }

    /**
     * Constructs test frame with the specified content and settings.
     *
     * @param layout     container layout
     * @param margin     container margin
     * @param components components to display
     */
    public TestFrame ( final LayoutManager layout, final Insets margin, final Component... components )
    {
        super ( getFrameTitle ( null ) );
        setLayout ( new BorderLayout () );

        container = new JPanel ( layout );
        if ( margin != null )
        {
            container.setBorder ( BorderFactory.createEmptyBorder ( margin.top, margin.left, margin.bottom, margin.right ) );
        }
        for ( final Component component : components )
        {
            container.add ( component );
        }
        add ( container, BorderLayout.CENTER );

        configureFrame ();
    }

    /**
     * Configures test frame.
     */
    public void configureFrame ()
    {
        setIconImages ( WebLookAndFeel.getImages () );
        setDefaultCloseOperation ( JFrame.DISPOSE_ON_CLOSE );
        setResizable ( true );
    }

    /**
     * Sets test frame background.
     *
     * @param c background color
     */
    public void setContentBackground ( final Color c )
    {
        if ( container != null )
        {
            container.setBackground ( c );
        }
    }

    /**
     * Displays and returns test frame.
     *
     * @return test frame
     */
    public TestFrame displayFrame ()
    {
        pack ();
        setLocationRelativeTo ( null );
        setVisible ( true );
        return this;
    }

    /**
     * Returns test frame title.
     *
     * @param component component to process
     * @return test frame title
     */
    public static String getFrameTitle ( final Component component )
    {
        // Tested class name
        final String className = ( component != null ? ReflectUtils.getClassName ( component.getClass () ) : "TestFrame" ) + " ";

        // WebLaF version
        String libVersion = "";
        try
        {
            libVersion = "[ " + VersionManager.getLibraryVersion ().toString () + " ] ";
        }
        catch ( final Exception ignored )
        {
            // Cannot load version now
        }

        // Undelying OS name and version
        final String osVersion = "[ " + SystemUtils.getOsName () + " " + SystemUtils.getOsArch () + " ] ";

        // JRE version
        final String jreVersion = "[ JRE " + SystemUtils.getJavaVersionString () + " " + SystemUtils.getJreArch () + "-bit ]";

        return className + libVersion + osVersion + jreVersion;
    }
}