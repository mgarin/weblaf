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

package com.alee.painter.decoration.shape;

import com.alee.api.annotations.NotNull;
import com.alee.api.data.Orientation;
import com.alee.painter.decoration.WebDecoration;
import com.alee.utils.MathUtils;

import javax.swing.*;
import java.awt.*;

/**
 * {@link WebShape} extension providing progress bar shape.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> shape type
 * @author Mikle Garin
 */
public abstract class AbstractProgressShape<C extends JComponent, D extends WebDecoration<C, D>, I extends AbstractProgressShape<C, D, I>>
        extends WebShape<C, D, I>
{
    @Override
    public boolean isVisible ( @NotNull final ShapeType type, @NotNull final Rectangle bounds, @NotNull final C c, @NotNull final D d )
    {
        return super.isVisible ( type, adjustBounds ( bounds, c, d ), c, d );
    }

    @NotNull
    @Override
    public Shape getShape ( @NotNull final ShapeType type, @NotNull final Rectangle bounds, @NotNull final C c, @NotNull final D d )
    {
        return super.getShape ( type, adjustBounds ( bounds, c, d ), c, d );
    }

    @NotNull
    @Override
    public Object[] getShapeSettings ( @NotNull final Rectangle bounds, @NotNull final C c, @NotNull final D d )
    {
        return super.getShapeSettings ( adjustBounds ( bounds, c, d ), c, d );
    }

    @NotNull
    @Override
    public StretchInfo getStretchInfo ( @NotNull final Rectangle bounds, @NotNull final C c, @NotNull final D d )
    {
        return super.getStretchInfo ( adjustBounds ( bounds, c, d ), c, d );
    }

    /**
     * Returns {@link Rectangle} bounds adjusted to match progress value.
     *
     * @param bounds painting bounds
     * @param c      painted component
     * @param d      painted decoration state
     * @return {@link Rectangle} bounds adjusted to match progress value
     */
    @NotNull
    protected Rectangle adjustBounds ( @NotNull final Rectangle bounds, @NotNull final C c, @NotNull final D d )
    {
        final Rectangle result;
        if ( isIndeterminate ( c, d ) )
        {
            result = bounds;
        }
        else
        {
            final Orientation orientation = getOrientation ( c, d );
            final double progress = getProgress ( c, d );
            final boolean ltr = c.getComponentOrientation ().isLeftToRight ();
            if ( orientation.isHorizontal () )
            {
                final int barLength = MathUtils.roundToInt ( bounds.width * progress );
                result = new Rectangle (
                        ltr ? bounds.x : bounds.x + bounds.width - barLength,
                        bounds.y,
                        barLength,
                        bounds.height
                );
            }
            else
            {
                final int barLength = MathUtils.roundToInt ( bounds.height * progress );
                result = new Rectangle (
                        bounds.x,
                        ltr ? bounds.y + bounds.height - barLength : bounds.y,
                        bounds.width,
                        barLength
                );
            }
        }
        return result;
    }

    /**
     * Returns progress bar orientation.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return progress bar orientation
     */
    @NotNull
    protected abstract Orientation getOrientation ( @NotNull C c, @NotNull D d );

    /**
     * Returns whether or not indeterminate progress is being displayed.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return whether or not indeterminate progress is being displayed
     */
    protected abstract boolean isIndeterminate ( @NotNull C c, @NotNull D d );

    /**
     * Returns progress value between {@code 0.0d} and {@code 1.0d}.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return progress value between {@code 0.0d} and {@code 1.0d}
     */
    protected abstract double getProgress ( @NotNull C c, @NotNull D d );
}