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

import com.alee.api.data.Orientation;

import java.awt.*;
import java.io.Serializable;

/**
 * @author Mikle Garin
 */

public final class ResizeData implements Cloneable, Serializable
{
    /**
     * Resizable area bounds.
     */
    private final Rectangle bounds;

    /**
     * Resize orientation.
     */
    private final Orientation orientation;

    /**
     * Left resizable element ID.
     */
    private final String leftId;

    /**
     * Right resizable element ID.
     */
    private final String rightId;

    /**
     * Constructs new resizable area data.
     *
     * @param bounds      resizable area bounds
     * @param orientation resize orientation
     * @param leftId      left resizable element ID
     * @param rightId     right resizable element ID
     */
    public ResizeData ( final Rectangle bounds, final Orientation orientation, final String leftId, final String rightId )
    {
        super ();
        this.bounds = bounds;
        this.orientation = orientation;
        this.leftId = leftId;
        this.rightId = rightId;
    }

    /**
     * Returns resizable area bounds.
     *
     * @return resizable area bounds
     */
    public Rectangle getBounds ()
    {
        return bounds;
    }

    /**
     * Returns resize orientation.
     *
     * @return resize orientation
     */
    public Orientation getOrientation ()
    {
        return orientation;
    }

    /**
     * Returns left resizable element ID.
     *
     * @return left resizable element ID
     */
    public String getLeftId ()
    {
        return leftId;
    }

    /**
     * Returns right resizable element ID.
     *
     * @return right resizable element ID
     */
    public String getRightId ()
    {
        return rightId;
    }

    @Override
    public ResizeData clone ()
    {
        return new ResizeData ( new Rectangle ( bounds ), orientation, leftId, rightId );
    }
}