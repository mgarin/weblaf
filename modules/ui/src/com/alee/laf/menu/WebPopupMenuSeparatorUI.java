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

package com.alee.laf.menu;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.api.jdk.Consumer;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * Custom UI for {@link javax.swing.JPopupMenu.Separator} component.
 *
 * @param <C> component type
 * @author Mikle Garin
 */
public class WebPopupMenuSeparatorUI<C extends JPopupMenu.Separator> extends WPopupMenuSeparatorUI<C>
        implements ShapeSupport, MarginSupport, PaddingSupport
{
    /**
     * Component painter.
     */
    @DefaultPainter ( PopupMenuSeparatorPainter.class )
    protected IPopupMenuSeparatorPainter painter;

    /**
     * Returns an instance of the {@link WebPopupMenuSeparatorUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebPopupMenuSeparatorUI}
     */
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebPopupMenuSeparatorUI ();
    }

    @Override
    public void installUI ( @NotNull final JComponent c )
    {
        // Installing UI
        super.installUI ( c );

        // Applying skin
        StyleManager.installSkin ( separator );
    }

    @Override
    public void uninstallUI ( @NotNull final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( separator );

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    @NotNull
    @Override
    public Shape getShape ()
    {
        return PainterSupport.getShape ( separator, painter );
    }

    @Override
    public boolean isShapeDetectionEnabled ()
    {
        return PainterSupport.isShapeDetectionEnabled ( separator, painter );
    }

    @Override
    public void setShapeDetectionEnabled ( final boolean enabled )
    {
        PainterSupport.setShapeDetectionEnabled ( separator, painter, enabled );
    }

    @Nullable
    @Override
    public Insets getMargin ()
    {
        return PainterSupport.getMargin ( separator );
    }

    @Override
    public void setMargin ( @Nullable final Insets margin )
    {
        PainterSupport.setMargin ( separator, margin );
    }

    @Nullable
    @Override
    public Insets getPadding ()
    {
        return PainterSupport.getPadding ( separator );
    }

    @Override
    public void setPadding ( @Nullable final Insets padding )
    {
        PainterSupport.setPadding ( separator, padding );
    }

    /**
     * Returns separator painter.
     *
     * @return separator painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getPainter ( painter );
    }

    /**
     * Sets separator painter.
     * Pass null to remove separator painter.
     *
     * @param painter new separator painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( separator, this, new Consumer<IPopupMenuSeparatorPainter> ()
        {
            @Override
            public void accept ( final IPopupMenuSeparatorPainter newPainter )
            {
                WebPopupMenuSeparatorUI.this.painter = newPainter;
            }
        }, this.painter, painter, IPopupMenuSeparatorPainter.class, AdaptivePopupMenuSeparatorPainter.class );
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

    @Override
    public Dimension getMaximumSize ( final JComponent c )
    {
        final Dimension ps = getPreferredSize ( c );
        if ( separator.getOrientation () == SwingConstants.VERTICAL )
        {
            ps.height = Short.MAX_VALUE;
        }
        else
        {
            ps.width = Short.MAX_VALUE;
        }
        return ps;
    }
}