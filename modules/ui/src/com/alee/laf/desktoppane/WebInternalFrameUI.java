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
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Consumer;
import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Custom UI for {@link JInternalFrame} component.
 *
 * @param <C> component type
 * @author Mikle Garin
 */
public class WebInternalFrameUI<C extends JInternalFrame> extends WInternalFrameUI<C> implements ShapeSupport, MarginSupport, PaddingSupport
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
        return new WebInternalFrameUI ();
    }

    @Override
    public void installUI ( @NotNull final JComponent c )
    {
        super.installUI ( c );

        // Applying skin
        StyleManager.installSkin ( internalFrame );

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
        internalFrame.addPropertyChangeListener ( JInternalFrame.ROOT_PANE_PROPERTY, rootPaneTracker );
    }

    @Override
    public void uninstallUI ( @NotNull final JComponent c )
    {
        // Uninstalling listeners
        internalFrame.removePropertyChangeListener ( JInternalFrame.ROOT_PANE_PROPERTY, rootPaneTracker );

        // Uninstalling title pane
        if ( northPane instanceof WebInternalFrameTitlePane )
        {
            ( ( WebInternalFrameTitlePane ) northPane ).uninstall ();
        }

        // Uninstalling applied skin
        StyleManager.uninstallSkin ( internalFrame );

        super.uninstallUI ( c );
    }

    /**
     * Performs root pane style update.
     */
    protected void updateRootPaneStyle ()
    {
        StyleId.internalframeRootpane.at ( internalFrame ).set ( internalFrame.getRootPane () );
    }

    @NotNull
    @Override
    public Shape getShape ()
    {
        return PainterSupport.getShape ( internalFrame, painter );
    }

    @Override
    public boolean isShapeDetectionEnabled ()
    {
        return PainterSupport.isShapeDetectionEnabled ( internalFrame, painter );
    }

    @Override
    public void setShapeDetectionEnabled ( final boolean enabled )
    {
        PainterSupport.setShapeDetectionEnabled ( internalFrame, painter, enabled );
    }

    @Nullable
    @Override
    public Insets getMargin ()
    {
        return PainterSupport.getMargin ( internalFrame );
    }

    @Override
    public void setMargin ( @Nullable final Insets margin )
    {
        PainterSupport.setMargin ( internalFrame, margin );
    }

    @Nullable
    @Override
    public Insets getPadding ()
    {
        return PainterSupport.getPadding ( internalFrame );
    }

    @Override
    public void setPadding ( @Nullable final Insets padding )
    {
        PainterSupport.setPadding ( internalFrame, padding );
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
        PainterSupport.setPainter ( internalFrame, this, new Consumer<IInternalFramePainter> ()
        {
            @Override
            public void accept ( final IInternalFramePainter newPainter )
            {
                WebInternalFrameUI.this.painter = newPainter;
            }
        }, this.painter, painter, IInternalFramePainter.class, AdaptiveInternalFramePainter.class );
    }

    @Override
    protected LayoutManager createLayoutManager ()
    {
        return new InternalFrameLayout ();
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
    public Dimension getMinimumSize ( final JComponent c )
    {
        return null;
    }

    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        return null;
    }
}