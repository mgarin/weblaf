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

package com.alee.laf.tabbedpane;

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

/**
 * Custom UI for {@link JTabbedPane} component.
 *
 * @param <C> component type
 * @author Mikle Garin
 * @author Alexandr Zernov
 */
public class WebTabbedPaneUI<C extends JTabbedPane> extends WTabbedPaneUI<C> implements ShapeSupport, MarginSupport, PaddingSupport
{
    /**
     * Component painter.
     */
    @DefaultPainter ( TabbedPanePainter.class )
    protected ITabbedPanePainter painter;

    /**
     * Returns an instance of the {@link WebTabbedPaneUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebTabbedPaneUI}
     */
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebTabbedPaneUI ();
    }

    @Override
    public void installUI ( @NotNull final JComponent c )
    {
        // Installing UI
        super.installUI ( c );

        // Applying skin
        StyleManager.installSkin ( tabbedPane );
    }

    @Override
    public void uninstallUI ( @NotNull final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( tabbedPane );

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    @NotNull
    @Override
    public Shape getShape ()
    {
        return PainterSupport.getShape ( tabbedPane, painter );
    }

    @Override
    public boolean isShapeDetectionEnabled ()
    {
        return PainterSupport.isShapeDetectionEnabled ( tabbedPane, painter );
    }

    @Override
    public void setShapeDetectionEnabled ( final boolean enabled )
    {
        PainterSupport.setShapeDetectionEnabled ( tabbedPane, painter, enabled );
    }

    @Nullable
    @Override
    public Insets getMargin ()
    {
        return PainterSupport.getMargin ( tabbedPane );
    }

    @Override
    public void setMargin ( @Nullable final Insets margin )
    {
        PainterSupport.setMargin ( tabbedPane, margin );
    }

    @Nullable
    @Override
    public Insets getPadding ()
    {
        return PainterSupport.getPadding ( tabbedPane );
    }

    @Override
    public void setPadding ( @Nullable final Insets padding )
    {
        PainterSupport.setPadding ( tabbedPane, padding );
    }

    /**
     * Returns tabbed pane painter.
     *
     * @return tabbed pane painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getPainter ( painter );
    }

    /**
     * Sets tabbed pane painter.
     * Pass null to remove tabbed pane painter.
     *
     * @param painter new tabbed pane painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( tabbedPane, this, new Consumer<ITabbedPanePainter> ()
        {
            @Override
            public void accept ( final ITabbedPanePainter newPainter )
            {
                WebTabbedPaneUI.this.painter = newPainter;
            }
        }, this.painter, painter, ITabbedPanePainter.class, AdaptiveTabbedPanePainter.class );
    }

    @Override
    public boolean contains ( final JComponent c, final int x, final int y )
    {
        return PainterSupport.contains ( c, this, painter, x, y );
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
}