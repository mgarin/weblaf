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

package com.alee.laf.desktoppane;

import com.alee.api.annotations.NotNull;
import com.alee.laf.WebUI;
import com.alee.utils.LafUtils;

import javax.swing.*;
import javax.swing.plaf.DesktopIconUI;
import java.awt.*;

/**
 * Pluggable look and feel interface for any component based on {@link javax.swing.JInternalFrame.JDesktopIcon}.
 *
 * @param <C> component type
 * @author Mikle Garin
 */
public abstract class WDesktopIconUI<C extends JInternalFrame.JDesktopIcon> extends DesktopIconUI implements WebUI<C>
{
    /**
     * {@link DesktopIconInputListener} for the {@link javax.swing.JInternalFrame.JDesktopIcon}.
     */
    protected DesktopIconInputListener<C> inputListener;

    /**
     * Runtime variables.
     */
    protected C desktopIcon;
    protected JInternalFrame internalFrame;
    protected JComponent iconPane;

    @NotNull
    @Override
    public String getPropertyPrefix ()
    {
        return "Desktop.";
    }

    @Override
    public void installUI ( @NotNull final JComponent c )
    {
        // Saving component reference
        desktopIcon = ( C ) c;
        internalFrame = desktopIcon.getInternalFrame ();

        // Installing default component settings
        installDefaults ();

        // Installing default components
        installComponents ();

        // Installing default component listeners
        installListeners ();
    }

    @Override
    public void uninstallUI ( @NotNull final JComponent c )
    {
        // Uninstalling default component listeners
        uninstallListeners ();

        // Uninstalling default components
        uninstallComponents ();

        // Uninstalling default component settings
        uninstallDefaults ();

        // Removing component reference
        internalFrame = null;
        desktopIcon = null;
    }

    /**
     * Installs default component settings.
     */
    protected void installDefaults ()
    {
        LafUtils.installDefaults ( desktopIcon, getPropertyPrefix () );

        // Update icon layout if frame is already iconified
        if ( internalFrame.isIcon () && internalFrame.getParent () == null )
        {
            final JDesktopPane desktop = desktopIcon.getDesktopPane ();
            if ( desktop != null )
            {
                final DesktopManager desktopManager = desktop.getDesktopManager ();
                if ( desktopManager instanceof DefaultDesktopManager )
                {
                    desktopManager.iconifyFrame ( internalFrame );
                }
            }
        }

        // Synchronizing icon layer with frame layer
        JLayeredPane.putLayer ( desktopIcon, JLayeredPane.getLayer ( internalFrame ) );
    }

    /**
     * Uninstalls default component settings.
     */
    protected void uninstallDefaults ()
    {
        // Force future UI to relayout icon
        if ( internalFrame.isIcon () )
        {
            final JDesktopPane desktop = desktopIcon.getDesktopPane ();
            if ( desktop != null )
            {
                final DesktopManager desktopManager = desktop.getDesktopManager ();
                if ( desktopManager instanceof DefaultDesktopManager )
                {
                    // This will cause DefaultDesktopManager to layout the icon
                    internalFrame.putClientProperty ( WebDesktopManager.HAS_BEEN_ICONIFIED_PROPERTY, null );

                    // Move aside to allow fresh layout of all icons
                    desktopIcon.setLocation ( Integer.MIN_VALUE, 0 );
                }
            }
        }

        LafUtils.uninstallDefaults ( desktopIcon );
    }

    /**
     * Installs default components.
     */
    protected void installComponents ()
    {
        iconPane = new WebInternalFrameTitlePane ( desktopIcon, internalFrame );
        desktopIcon.setLayout ( new BorderLayout () );
        desktopIcon.add ( iconPane, BorderLayout.CENTER );

        // Instaling custom listeners
        if ( iconPane instanceof WebInternalFrameTitlePane )
        {
            ( ( WebInternalFrameTitlePane ) iconPane ).install ();
        }
    }

    /**
     * Uninstalls default components.
     */
    protected void uninstallComponents ()
    {
        // Uninstaling custom listeners
        if ( iconPane instanceof WebInternalFrameTitlePane )
        {
            ( ( WebInternalFrameTitlePane ) iconPane ).uninstall ();
        }

        desktopIcon.remove ( iconPane );
        desktopIcon.setLayout ( null );
        iconPane = null;
    }

    /**
     * Installs default component listeners.
     */
    protected void installListeners ()
    {
        inputListener = createDesktopIconInputListener ();
        inputListener.install ( desktopIcon );
    }

    /**
     * Uninstalls default component listeners.
     */
    protected void uninstallListeners ()
    {
        inputListener.uninstall ( desktopIcon );
        inputListener = null;
    }

    /**
     * Returns {@link WDesktopIconInputListener} for the {@link AbstractButton}.
     *
     * @return {@link WDesktopIconInputListener} for the {@link AbstractButton}
     */
    protected DesktopIconInputListener<C> createDesktopIconInputListener ()
    {
        return new WDesktopIconInputListener<C, WDesktopIconUI<C>> ();
    }

    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        return iconPane.getPreferredSize ();
    }

    @Override
    public Dimension getMinimumSize ( final JComponent c )
    {
        return iconPane.getMinimumSize ();
    }

    @Override
    public Dimension getMaximumSize ( final JComponent c )
    {
        return iconPane.getMaximumSize ();
    }
}