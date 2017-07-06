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

import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.swing.DataRunnable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Custom UI for {@link JInternalFrame} component.
 *
 * @author Mikle Garin
 */

public class WebInternalFrameUI extends BasicInternalFrameUI implements ShapeSupport, MarginSupport, PaddingSupport
{
    /**
     * Component painter.
     */
    @DefaultPainter ( InternalFramePainter.class )
    protected IInternalFramePainter painter;

    /**
     * Listeners.
     */
    protected transient PropertyChangeListener rootPaneTracker;

    /**
     * Returns an instance of the {@link WebInternalFrameUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebInternalFrameUI}
     */
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebInternalFrameUI ( ( JInternalFrame ) c );
    }

    /**
     * Constructs new internal frame UI.
     *
     * @param b internal frame to which this UI will be applied
     */
    public WebInternalFrameUI ( final JInternalFrame b )
    {
        super ( b );
    }

    @Override
    public void installUI ( final JComponent c )
    {
        super.installUI ( c );

        // Applying skin
        StyleManager.installSkin ( frame );

        // Installing title pane
        if ( northPane instanceof WebInternalFrameTitlePane )
        {
            ( ( WebInternalFrameTitlePane ) northPane ).install ();
        }

        // Root pane style updates
        updateRootPaneStyle ();
        rootPaneTracker = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                updateRootPaneStyle ();
            }
        };
        frame.addPropertyChangeListener ( JInternalFrame.ROOT_PANE_PROPERTY, rootPaneTracker );
    }

    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling listeners
        frame.removePropertyChangeListener ( JInternalFrame.ROOT_PANE_PROPERTY, rootPaneTracker );

        // Uninstalling title pane
        if ( northPane instanceof WebInternalFrameTitlePane )
        {
            ( ( WebInternalFrameTitlePane ) northPane ).uninstall ();
        }

        // Uninstalling applied skin
        StyleManager.uninstallSkin ( frame );

        super.uninstallUI ( c );
    }

    /**
     * Performs root pane style update.
     */
    protected void updateRootPaneStyle ()
    {
        StyleId.internalframeRootpane.at ( frame ).set ( frame.getRootPane () );
    }

    @Override
    public Shape getShape ()
    {
        return PainterSupport.getShape ( frame, painter );
    }

    @Override
    public Insets getMargin ()
    {
        return PainterSupport.getMargin ( frame );
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        PainterSupport.setMargin ( frame, margin );
    }

    @Override
    public Insets getPadding ()
    {
        return PainterSupport.getPadding ( frame );
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        PainterSupport.setPadding ( frame, padding );
    }

    /**
     * Returns internal frame painter.
     *
     * @return internal frame painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getPainter ( painter );
    }

    /**
     * Sets internal frame painter.
     * Pass null to remove internal frame painter.
     *
     * @param painter new internal frame painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( frame, new DataRunnable<IInternalFramePainter> ()
        {
            @Override
            public void run ( final IInternalFramePainter newPainter )
            {
                WebInternalFrameUI.this.painter = newPainter;
            }
        }, this.painter, painter, IInternalFramePainter.class, AdaptiveInternalFramePainter.class );
    }

    @Override
    protected JComponent createNorthPane ( final JInternalFrame frame )
    {
        return new WebInternalFrameTitlePane ( frame, frame );
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
    public Dimension getMinimumSize ( final JComponent c )
    {
        return getPreferredSize ( c );
    }

    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        return PainterSupport.getPreferredSize ( c, super.getPreferredSize ( c ), painter );
    }
}