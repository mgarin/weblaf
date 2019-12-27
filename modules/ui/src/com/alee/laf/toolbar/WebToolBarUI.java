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

package com.alee.laf.toolbar;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.window.WebDialog;
import com.alee.managers.style.StyleManager;
import com.alee.painter.PainterSupport;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.ProprietaryUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToolBarUI;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Custom UI for {@link JToolBar} component.
 *
 * @author Mikle Garin
 * @author Alexandr Zernov
 */
public class WebToolBarUI extends BasicToolBarUI
{
    /**
     * todo 1. Restore toolbar element focus upon floating mode enter/exit
     */

    /**
     * Returns an instance of the {@link WebToolBarUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebToolBarUI}
     */
    @NotNull
    public static ComponentUI createUI ( @NotNull final JComponent c )
    {
        return new WebToolBarUI ();
    }

    @Override
    public void installUI ( @NotNull final JComponent c )
    {
        // Installing UI
        super.installUI ( c );

        // Applying skin
        StyleManager.installSkin ( toolBar );
    }

    @Override
    public void uninstallUI ( @NotNull final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( toolBar );

        // Uninstalling UI
        super.uninstallUI ( c );

        // Swing doesn't cleanup this value so we do instead
        toolBar = null;
    }

    /**
     * Overridden to skip unnecessary operations.
     */
    @Nullable
    @Override
    protected PropertyChangeListener createPropertyListener ()
    {
        return new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( @NotNull final PropertyChangeEvent evt )
            {
                propertyChanged ( evt.getPropertyName (), evt.getOldValue (), evt.getNewValue () );
            }
        };
    }

    /**
     * Informs about {@link #toolBar} property changes.
     *
     * @param property modified property
     * @param oldValue old property value
     * @param newValue new property value
     */
    protected void propertyChanged ( @NotNull final String property, @Nullable final Object oldValue, @Nullable final Object newValue )
    {
        if ( Objects.equals ( property, WebLookAndFeel.ORIENTATION_PROPERTY ) && newValue != null )
        {
            /**
             * Search for {@link JSeparator} components and change their
             * orientation to match the toolbar and flip it's orientation.
             */
            final int orientation = ( Integer ) newValue;
            for ( final Component component : toolBar.getComponents () )
            {
                if ( component instanceof JToolBar.Separator )
                {
                    final JToolBar.Separator separator = ( JToolBar.Separator ) component;

                    // Flipping separator orientation
                    separator.setOrientation ( orientation == JToolBar.HORIZONTAL ? JSeparator.VERTICAL : JSeparator.HORIZONTAL );

                    // Flipping separator size
                    final Dimension size = separator.getSeparatorSize ();
                    if ( size != null && size.width != size.height )
                    {
                        final Dimension newSize = new Dimension ( size.height, size.width );
                        separator.setSeparatorSize ( newSize );
                    }
                }
            }
        }
    }

    @Override
    public boolean contains ( @NotNull final JComponent c, final int x, final int y )
    {
        return PainterSupport.contains ( c, this, x, y );
    }

    @Override
    public int getBaseline ( @NotNull final JComponent c, final int width, final int height )
    {
        return PainterSupport.getBaseline ( c, this, width, height );
    }

    @NotNull
    @Override
    public Component.BaselineResizeBehavior getBaselineResizeBehavior ( @NotNull final JComponent c )
    {
        return PainterSupport.getBaselineResizeBehavior ( c, this );
    }

    @Override
    public void paint ( @NotNull final Graphics g, @NotNull final JComponent c )
    {
        PainterSupport.paint ( g, c, this );
    }

    @Nullable
    @Override
    public Dimension getPreferredSize ( @NotNull final JComponent c )
    {
        return null;
    }

    @Override
    protected RootPaneContainer createFloatingWindow ( @NotNull final JToolBar toolbar )
    {
        final JDialog dialog;
        final Window window = CoreSwingUtils.getWindowAncestor ( toolbar );
        if ( window instanceof Frame )
        {
            dialog = new ToolBarDialog ( ( Frame ) window, toolbar.getName () );
        }
        else if ( window instanceof Dialog )
        {
            dialog = new ToolBarDialog ( ( Dialog ) window, toolbar.getName () );
        }
        else
        {
            dialog = new ToolBarDialog ( ( Frame ) null, toolbar.getName () );
        }

        dialog.getRootPane ().setName ( "ToolBar.FloatingWindow" );
        dialog.setTitle ( toolbar.getName () );
        dialog.setResizable ( false );
        dialog.addWindowListener ( createFrameListener () );
        return dialog;
    }

    @Override
    protected DragWindow createDragWindow ( @NotNull final JToolBar toolbar )
    {
        final DragWindow dragWindow = super.createDragWindow ( toolbar );
        ProprietaryUtils.setWindowOpacity ( dragWindow, 0.5f );
        return dragWindow;
    }

    @Override
    protected void installRolloverBorders ( @NotNull final JComponent c )
    {
        // Do not touch any elements here as it will break WebLaF borders
    }

    @Override
    protected void installNonRolloverBorders ( @NotNull final JComponent c )
    {
        // Do not touch any elements here as it will break WebLaF borders
    }

    @Override
    protected void installNormalBorders ( @NotNull final JComponent c )
    {
        // Do not touch any elements here as it will break WebLaF borders
    }

    @Override
    protected void setBorderToRollover ( @NotNull final Component c )
    {
        // Do not touch any elements here as it will break WebLaF borders
    }

    @Override
    protected void setBorderToNonRollover ( @NotNull final Component c )
    {
        // Do not touch any elements here as it will break WebLaF borders
    }

    @Override
    protected void setBorderToNormal ( @NotNull final Component c )
    {
        // Do not touch any elements here as it will break WebLaF borders
    }

    /**
     * Custom dialog for dragged toolbar.
     */
    protected class ToolBarDialog extends WebDialog
    {
        /**
         * Constructs new dialog for dragged toolbar.
         *
         * @param owner owner frame
         * @param title dialog title
         */
        public ToolBarDialog ( @Nullable final Frame owner, @Nullable final String title )
        {
            super ( owner, title, false );
        }

        /**
         * Constructs new dialog for dragged toolbar.
         *
         * @param owner owner dialog
         * @param title dialog title
         */
        public ToolBarDialog ( @Nullable final Dialog owner, @Nullable final String title )
        {
            super ( owner, title, false );
        }

        @NotNull
        @Override
        protected JRootPane createRootPane ()
        {
            // Override createRootPane() to automatically resize the frame when contents change
            final WebDialogRootPane rootPane = new WebDialogRootPane ()
            {
                private boolean packing = false;

                @Override
                public void validate ()
                {
                    super.validate ();
                    if ( !packing )
                    {
                        packing = true;
                        pack ();
                        packing = false;
                    }
                }
            };
            rootPane.setOpaque ( true );
            return rootPane;
        }
    }
}