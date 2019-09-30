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

package com.alee.extended.dock.data;

import com.alee.api.annotations.NotNull;
import com.alee.api.data.Orientation;

import java.awt.*;
import java.io.Serializable;

/**
 * Sample data object used for {@link com.alee.extended.dock.WebDockablePane} resize operations.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDockablePane">How to use WebDockablePane</a>
 * @see com.alee.extended.dock.WebDockablePane
 */
public final class ResizeData implements Cloneable, Serializable
{
    /**
     * Resizable area bounds.
     */
    @NotNull
    private final Rectangle bounds;

    /**
     * Resize orientation.
     */
    @NotNull
    private final Orientation orientation;

    /**
     * Left resizable element identifier.
     */
    @NotNull
    private final String leftElementId;

    /**
     * Right resizable element identifier.
     */
    @NotNull
    private final String rightElementId;

    /**
     * Constructs new resizable area data.
     *
     * @param bounds         resizable area bounds
     * @param orientation    resize orientation
     * @param leftElementId  left resizable element identifier
     * @param rightElementId right resizable element identifier
     */
    public ResizeData ( @NotNull final Rectangle bounds, @NotNull final Orientation orientation,
                        @NotNull final String leftElementId, @NotNull final String rightElementId )
    {
        this.bounds = bounds;
        this.orientation = orientation;
        this.leftElementId = leftElementId;
        this.rightElementId = rightElementId;
    }

    /**
     * Returns resizable area bounds.
     *
     * @return resizable area bounds
     */
    @NotNull
    public Rectangle bounds ()
    {
        return bounds;
    }

    /**
     * Returns resize orientation.
     *
     * @return resize orientation
     */
    @NotNull
    public Orientation orientation ()
    {
        return orientation;
    }

    /**
     * Returns left resizable element identifier.
     *
     * @return left resizable element identifier
     */
    @NotNull
    public String leftElementId ()
    {
        return leftElementId;
    }

    /**
     * Returns right resizable element identifier.
     *
     * @return right resizable element identifier
     */
    @NotNull
    public String rightElementId ()
    {
        return rightElementId;
    }
}