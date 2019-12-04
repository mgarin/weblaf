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

package com.alee.extended.memorybar;

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
 * Custom UI for {@link WebMemoryBar} component.
 *
 * @param <C> {@link WebMemoryBar} type
 * @author Mikle Garin
 */
public class WebMemoryBarUI<C extends WebMemoryBar> extends WMemoryBarUI<C> implements ShapeSupport, MarginSupport, PaddingSupport
{
    /**
     * Component painter.
     */
    @Nullable
    @DefaultPainter ( MemoryBarPainter.class )
    protected IMemoryBarPainter painter;

    /**
     * Returns an instance of the {@link WebMemoryBarUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebMemoryBarUI}
     */
    @NotNull
    public static ComponentUI createUI ( @NotNull final JComponent c )
    {
        return new WebMemoryBarUI ();
    }

    @Override
    public void installUI ( @NotNull final JComponent c )
    {
        // Installing UI
        super.installUI ( c );

        // Applying skin
        StyleManager.installSkin ( memoryBar );
    }

    @Override
    public void uninstallUI ( @NotNull final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( memoryBar );

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    @NotNull
    @Override
    public Shape getShape ()
    {
        return PainterSupport.getShape ( memoryBar, painter );
    }

    @Override
    public boolean isShapeDetectionEnabled ()
    {
        return PainterSupport.isShapeDetectionEnabled ( memoryBar, painter );
    }

    @Override
    public void setShapeDetectionEnabled ( final boolean enabled )
    {
        PainterSupport.setShapeDetectionEnabled ( memoryBar, painter, enabled );
    }

    @Nullable
    @Override
    public Insets getMargin ()
    {
        return PainterSupport.getMargin ( memoryBar );
    }

    @Override
    public void setMargin ( @Nullable final Insets margin )
    {
        PainterSupport.setMargin ( memoryBar, margin );
    }

    @Nullable
    @Override
    public Insets getPadding ()
    {
        return PainterSupport.getPadding ( memoryBar );
    }

    @Override
    public void setPadding ( @Nullable final Insets padding )
    {
        PainterSupport.setPadding ( memoryBar, padding );
    }

    /**
     * Returns memory bar painter.
     *
     * @return memory bar painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getPainter ( painter );
    }

    /**
     * Sets memory bar painter.
     * Pass null to remove memory bar painter.
     *
     * @param painter new memory bar painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( memoryBar, this, new Consumer<IMemoryBarPainter> ()
        {
            @Override
            public void accept ( final IMemoryBarPainter newPainter )
            {
                WebMemoryBarUI.this.painter = newPainter;
            }
        }, this.painter, painter, IMemoryBarPainter.class, AdaptiveMemoryBarPainter.class );
    }

    @Override
    public boolean contains ( @NotNull final JComponent c, final int x, final int y )
    {
        return PainterSupport.contains ( c, this, painter, x, y );
    }

    @Override
    public int getBaseline ( @NotNull final JComponent c, final int width, final int height )
    {
        return PainterSupport.getBaseline ( c, this, painter, width, height );
    }

    @Override
    public Component.BaselineResizeBehavior getBaselineResizeBehavior ( @NotNull final JComponent c )
    {
        return PainterSupport.getBaselineResizeBehavior ( c, this, painter );
    }

    @Override
    public void paint ( @NotNull final Graphics g, @NotNull final JComponent c )
    {
        if ( painter != null )
        {
            painter.paint ( ( Graphics2D ) g, c, this, new Bounds ( c ) );
        }
    }

    @Override
    public Dimension getPreferredSize ( @NotNull final JComponent c )
    {
        return PainterSupport.getPreferredSize ( c, painter );
    }
}