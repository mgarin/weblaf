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

package com.alee.laf.scroll;

import com.alee.api.annotations.NotNull;
import com.alee.painter.PaintParameters;

import java.awt.*;

/**
 * {@link PaintParameters} for {@link IScrollBarPainter}.
 *
 * @author Mikle Garin
 */
public class ScrollBarPaintParameters implements PaintParameters
{
    /**
     * Whether or not {@link javax.swing.JScrollBar} thumb is being dragged.
     * This value is updated by {@link WebScrollBarUI} when drag event starts or ends.
     */
    public final boolean dragged;

    /**
     * {@link javax.swing.JScrollBar} track bounds.
     * This value is updated by {@link WebScrollBarUI} on each paint call to ensure that proper bounds presented.
     */
    @NotNull
    public final Rectangle trackBounds;

    /**
     * {@link javax.swing.JScrollBar} thumb bounds.
     * This value is updated by {@link WebScrollBarUI} on each paint call to ensure that proper bounds presented.
     */
    @NotNull
    public final Rectangle thumbBounds;

    /**
     * Constructs new {@link ScrollBarPaintParameters}.
     *
     * @param dragged     whether or not {@link javax.swing.JScrollBar} thumb is being dragged
     * @param trackBounds {@link javax.swing.JScrollBar} track bounds
     * @param thumbBounds {@link javax.swing.JScrollBar} thumb bounds
     */
    public ScrollBarPaintParameters ( final boolean dragged, @NotNull final Rectangle trackBounds, @NotNull final Rectangle thumbBounds )
    {
        this.dragged = dragged;
        this.trackBounds = trackBounds;
        this.thumbBounds = thumbBounds;
    }
}