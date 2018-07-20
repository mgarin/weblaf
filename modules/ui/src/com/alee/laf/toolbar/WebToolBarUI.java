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

import com.alee.api.jdk.Consumer;
import com.alee.api.jdk.Objects;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.window.WebDialog;
import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.ProprietaryUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToolBarUI;
import java.awt.*;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Custom UI for {@link JToolBar} component.
 *
 * @author Mikle Garin
 * @author Alexandr Zernov
 */
public class WebToolBarUI extends BasicToolBarUI implements ShapeSupport, MarginSupport, PaddingSupport
{
    /**
     * todo 1. Restore toolbar element focus upon floating mode enter/exit
     */

    /**
     * Component painter.
     */
    @DefaultPainter ( ToolBarPainter.class )
    protected IToolBarPainter painter;

    /**
     * Preserved old layout.
     */
    protected transient LayoutManager oldLayout;

    /**
     * Returns an instance of the {@link WebToolBarUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebToolBarUI}
     */
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebToolBarUI ();
    }

    @Override
    public void installUI ( final JComponent c )
    {
        // Installing UI
        super.installUI ( c );

        // Installing custom layout
        installLayout ();

        // Applying skin
        StyleManager.installSkin ( toolBar );
    }

    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( toolBar );

        // Uninstalling custom layout
        uninstallLayout ();

        // Uninstalling UI
        super.uninstallUI ( c );

        // Swing doesn't cleanup this value so we do instead
        toolBar = null;
    }

    /**
     * Overridden to skip unnecessary operations.
     */
    @Override
    protected PropertyChangeListener createPropertyListener ()
    {
        return new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
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
    protected void propertyChanged ( final String property, final Object oldValue, final Object newValue )
    {
        if ( Objects.equals ( property, WebLookAndFeel.ORIENTATION_PROPERTY ) )
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

    /**
     * Installs layout update listeners.
     */
    protected void installLayout ()
    {
        // Saving old layout
        oldLayout = toolBar.getLayout ();

        // Updating initial layout
        toolBar.setLayout ( createLayout () );
    }

    /**
     * Uninstalls layout update listeners.
     */
    protected void uninstallLayout ()
    {
        // Restoring old layout
        toolBar.setLayout ( oldLayout );

        // Workaround for DefaultToolBarLayout
        if ( oldLayout instanceof PropertyChangeListener )
        {
            toolBar.addPropertyChangeListener ( ( PropertyChangeListener ) oldLayout );
        }

        // Cleaning up old layout reference
        oldLayout = null;
    }

    /**
     * Returns custom {@link LayoutManager} for the toolbar.
     *
     * @return custom {@link LayoutManager} for the toolbar
     */
    protected LayoutManager createLayout ()
    {
        return new ToolbarLayout ();
    }

    @Override
    public Shape getShape ()
    {
        return PainterSupport.getShape ( toolBar, painter );
    }

    @Override
    public boolean isShapeDetectionEnabled ()
    {
        return PainterSupport.isShapeDetectionEnabled ( toolBar, painter );
    }

    @Override
    public void setShapeDetectionEnabled ( final boolean enabled )
    {
        PainterSupport.setShapeDetectionEnabled ( toolBar, painter, enabled );
    }

    @Override
    public Insets getMargin ()
    {
        return PainterSupport.getMargin ( toolBar );
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        PainterSupport.setMargin ( toolBar, margin );
    }

    @Override
    public Insets getPadding ()
    {
        return PainterSupport.getPadding ( toolBar );
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        PainterSupport.setPadding ( toolBar, padding );
    }

    /**
     * Returns toolbar painter.
     *
     * @return toolbar painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getPainter ( painter );
    }

    /**
     * Sets toolbar painter.
     * Pass null to remove toolbar painter.
     *
     * @param painter new toolbar painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( toolBar, new Consumer<IToolBarPainter> ()
        {
            @Override
            public void accept ( final IToolBarPainter newPainter )
            {
                WebToolBarUI.this.painter = newPainter;
            }
        }, this.painter, painter, IToolBarPainter.class, AdaptiveToolBarPainter.class );
    }

    @Override
    public boolean contains ( final JComponent c, final int x, final int y )
    {
        return PainterSupport.contains ( c, this, painter, x, y );
    }

    @Override
    public int getBaseline ( final JComponent c, final int width, final int height )
    {
        return PainterSupport.getBaseline ( c, this, painter, width, height );
    }

    @Override
    public Component.BaselineResizeBehavior getBaselineResizeBehavior ( final JComponent c )
    {
        return PainterSupport.getBaselineResizeBehavior ( c, this, painter );
    }

    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( painter != null )
        {
            painter.paint ( ( Graphics2D ) g, c, this, new Bounds ( c ) );
        }
    }

    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        // return PainterSupport.getPreferredSize ( c, painter );
        return null;
    }

    @Override
    protected RootPaneContainer createFloatingWindow ( final JToolBar toolbar )
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
        final WindowListener wl = createFrameListener ();
        dialog.addWindowListener ( wl );
        return dialog;
    }

    @Override
    protected DragWindow createDragWindow ( final JToolBar toolbar )
    {
        final DragWindow dragWindow = super.createDragWindow ( toolbar );
        ProprietaryUtils.setWindowOpacity ( dragWindow, 0.5f );
        return dragWindow;
    }

    @Override
    protected void installRolloverBorders ( final JComponent c )
    {
        // Do not touch any elements here as it will break WebLaF borders
    }

    @Override
    protected void installNonRolloverBorders ( final JComponent c )
    {
        // Do not touch any elements here as it will break WebLaF borders
    }

    @Override
    protected void installNormalBorders ( final JComponent c )
    {
        // Do not touch any elements here as it will break WebLaF borders
    }

    @Override
    protected void setBorderToRollover ( final Component c )
    {
        // Do not touch any elements here as it will break WebLaF borders
    }

    @Override
    protected void setBorderToNonRollover ( final Component c )
    {
        // Do not touch any elements here as it will break WebLaF borders
    }

    @Override
    protected void setBorderToNormal ( final Component c )
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
        public ToolBarDialog ( final Frame owner, final String title )
        {
            super ( owner, title, false );
        }

        /**
         * Constructs new dialog for dragged toolbar.
         *
         * @param owner owner dialog
         * @param title dialog title
         */
        public ToolBarDialog ( final Dialog owner, final String title )
        {
            super ( owner, title, false );
        }

        @Override
        protected JRootPane createRootPane ()
        {
            // Override createRootPane() to automatically resize the frame when contents change
            final JRootPane rootPane = new JRootPane ()
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