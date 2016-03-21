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

import com.alee.laf.rootpane.WebDialog;
import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.ProprietaryUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.DataRunnable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToolBarUI;
import java.awt.*;
import java.awt.event.WindowListener;

/**
 * @author Mikle Garin
 * @author Alexandr Zernov
 */

public class WebToolBarUI extends BasicToolBarUI implements Styleable, ShapeProvider, MarginSupport, PaddingSupport
{
    /**
     * Component painter.
     */
    @DefaultPainter ( ToolBarPainter.class )
    protected IToolBarPainter painter;

    /**
     * Runtime variables.
     */
    protected Insets margin = null;
    protected Insets padding = null;

    /**
     * Returns an instance of the WebButtonUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebButtonUI
     */
    @SuppressWarnings ("UnusedParameters")
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebToolBarUI ();
    }

    /**
     * Installs UI in the specified component.
     *
     * @param c component for this UI
     */
    @Override
    public void installUI ( final JComponent c )
    {
        // Installing UI
        super.installUI ( c );

        // Applying skin
        StyleManager.installSkin ( toolBar );
    }

    /**
     * Uninstalls UI from the specified component.
     *
     * @param c component with this UI
     */
    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( toolBar );

        // Uninstalling UI
        super.uninstallUI ( c );

        // Swing doesn't cleanup this value in some versions
        // So we will give a hand here and simply nullify it
        toolBar = null;
    }

    @Override
    public StyleId getStyleId ()
    {
        return StyleManager.getStyleId ( toolBar );
    }

    @Override
    public StyleId setStyleId ( final StyleId id )
    {
        return StyleManager.setStyleId ( toolBar, id );
    }

    @Override
    public Shape provideShape ()
    {
        return PainterSupport.getShape ( toolBar, painter );
    }

    @Override
    public Insets getMargin ()
    {
        return margin;
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        this.margin = margin;
        PainterSupport.updateBorder ( getPainter () );
    }

    @Override
    public Insets getPadding ()
    {
        return padding;
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        this.padding = padding;
        PainterSupport.updateBorder ( getPainter () );
    }

    /**
     * Returns toolbar painter.
     *
     * @return toolbar painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getAdaptedPainter ( painter );
    }

    /**
     * Sets toolbar painter.
     * Pass null to remove toolbar painter.
     *
     * @param painter new toolbar painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( toolBar, new DataRunnable<IToolBarPainter> ()
        {
            @Override
            public void run ( final IToolBarPainter newPainter )
            {
                WebToolBarUI.this.painter = newPainter;
            }
        }, this.painter, painter, IToolBarPainter.class, AdaptiveToolBarPainter.class );
    }

    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( painter != null )
        {
            painter.paint ( ( Graphics2D ) g, Bounds.component.of ( c ), c, this );
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
        final Window window = SwingUtils.getWindowAncestor ( toolbar );
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