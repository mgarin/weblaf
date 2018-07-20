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

package com.alee.extended.split;

import com.alee.api.jdk.Consumer;
import com.alee.api.jdk.Objects;
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
 * Custom UI for {@link WebMultiSplitPane} component.
 *
 * @param <C> component type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebMultiSplitPane">How to use WebMultiSplitPane</a>
 * @see WebMultiSplitPane
 */
public class WebMultiSplitPaneUI<C extends WebMultiSplitPane> extends WMultiSplitPaneUI<C>
        implements ShapeSupport, MarginSupport, PaddingSupport, PropertyChangeListener
{
    /**
     * Component painter.
     */
    @DefaultPainter ( MultiSplitPanePainter.class )
    protected IMultiSplitPanePainter painter;

    /**
     * Returns an instance of the {@link WebMultiSplitPaneUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebMultiSplitPaneUI}
     */
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebMultiSplitPaneUI ();
    }

    @Override
    public void installUI ( final JComponent c )
    {
        // Installing UI
        super.installUI ( c );

        // Applying skin
        StyleManager.installSkin ( multisplitpane );
    }

    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( multisplitpane );

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    @Override
    protected void installListeners ()
    {
        // Installing default listeners
        super.installListeners ();

        // Instaling custom listeners
        multisplitpane.addPropertyChangeListener ( this );
    }

    @Override
    protected void uninstallListeners ()
    {
        // Uninstaling custom listeners
        multisplitpane.removePropertyChangeListener ( this );

        // Uninstalling default listeners
        super.uninstallListeners ();
    }

    @Override
    public void propertyChange ( final PropertyChangeEvent evt )
    {
        final String property = evt.getPropertyName ();
        if ( Objects.equals ( property, WebMultiSplitPane.DIVIDER_SIZE_PROPERTY ) )
        {
            // Updating split pane layout
            multisplitpane.revalidate ();
            multisplitpane.repaint ();
        }
        else if ( Objects.equals ( property, WebMultiSplitPane.ONE_TOUCH_EXPANDABLE_PROPERTY ) )
        {
            // Collapsing expanded view upon changes
            if ( !multisplitpane.isOneTouchExpandable () && multisplitpane.isAnyViewExpanded () )
            {
                multisplitpane.collapseExpandedView ();
            }
        }
    }

    @Override
    public Shape getShape ()
    {
        return PainterSupport.getShape ( multisplitpane, painter );
    }

    @Override
    public boolean isShapeDetectionEnabled ()
    {
        return PainterSupport.isShapeDetectionEnabled ( multisplitpane, painter );
    }

    @Override
    public void setShapeDetectionEnabled ( final boolean enabled )
    {
        PainterSupport.setShapeDetectionEnabled ( multisplitpane, painter, enabled );
    }

    @Override
    public Insets getMargin ()
    {
        return PainterSupport.getMargin ( multisplitpane );
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        PainterSupport.setMargin ( multisplitpane, margin );
    }

    @Override
    public Insets getPadding ()
    {
        return PainterSupport.getPadding ( multisplitpane );
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        PainterSupport.setPadding ( multisplitpane, padding );
    }

    /**
     * Returns multi split pane painter.
     *
     * @return multi split pane painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getPainter ( painter );
    }

    /**
     * Sets multi split pane painter.
     * Pass null to remove multi split pane painter.
     *
     * @param painter new multi split pane painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( multisplitpane, new Consumer<IMultiSplitPanePainter> ()
        {
            @Override
            public void accept ( final IMultiSplitPanePainter newPainter )
            {
                WebMultiSplitPaneUI.this.painter = newPainter;
            }
        }, this.painter, painter, IMultiSplitPanePainter.class, AdaptiveMultiSplitPanePainter.class );
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
        return PainterSupport.getPreferredSize ( c, painter );
    }
}