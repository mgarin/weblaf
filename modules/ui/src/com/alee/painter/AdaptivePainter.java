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

package com.alee.painter;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.managers.style.Bounds;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * Special painter made to adapts any kind of painters to fit custom painters within the specific UIs.
 * To use it properly you should extend this class and implement UI painter interface methods.
 * In general cases those methods might have no effect since general-type painters do not know anything about component specifics.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Mikle Garin
 */
public abstract class AdaptivePainter<C extends JComponent, U extends ComponentUI> implements SpecificPainter<C, U>
{
    /**
     * Adapted {@link Painter}.
     */
    @NotNull
    private final Painter painter;

    /**
     * Constructs new {@link AdaptivePainter} to adapt specified {@link Painter}.
     *
     * @param painter {@link Painter} to adapt
     */
    public AdaptivePainter ( @NotNull final Painter painter )
    {
        this.painter = painter;
    }

    /**
     * Returns adapted {@link Painter}.
     *
     * @return adapted {@link Painter}
     */
    @NotNull
    public Painter getPainter ()
    {
        return painter;
    }

    @Override
    public void install ( @NotNull final C c, @NotNull final U ui )
    {
        painter.install ( c, ui );
    }

    @Override
    public void uninstall ( @NotNull final C c, @NotNull final U ui )
    {
        painter.uninstall ( c, ui );
    }

    @Override
    public boolean isInstalled ()
    {
        return painter.isInstalled ();
    }

    @Nullable
    @Override
    public Boolean isOpaque ()
    {
        return painter.isOpaque ();
    }

    @Override
    public int getBaseline ( @NotNull final C c, @NotNull final U ui, @NotNull final Bounds bounds )
    {
        return painter.getBaseline ( c, ui, bounds );
    }

    @Override
    public Component.BaselineResizeBehavior getBaselineResizeBehavior ( @NotNull final C c, @NotNull final U ui )
    {
        return painter.getBaselineResizeBehavior ( c, ui );
    }

    @Override
    public boolean contains ( @NotNull final C c, @NotNull final U ui, @NotNull final Bounds bounds, final int x, final int y )
    {
        return painter.contains ( c, ui, bounds, x, y );
    }

    @Override
    public void paint ( @NotNull final Graphics2D g2d, @NotNull final C c, @NotNull final U ui, @NotNull final Bounds bounds )
    {
        painter.paint ( g2d, c, ui, bounds );
    }

    @NotNull
    @Override
    public Dimension getPreferredSize ()
    {
        return painter.getPreferredSize ();
    }
}