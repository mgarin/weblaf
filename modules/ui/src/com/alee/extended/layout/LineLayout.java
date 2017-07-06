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

package com.alee.extended.layout;

import java.awt.*;

/**
 * Custom {@link AbstractLineLayout} with customizable {@link #orientation}.
 *
 * @author Mikle Garin
 */

public class LineLayout extends AbstractLineLayout
{
    /**
     * Layout orientation.
     * Can either be {@link #HORIZONTAL} or {@link #VERTICAL}.
     */
    protected int orientation;

    /**
     * Constructs new {@link LineLayout}.
     *
     * @param orientation either {@link #HORIZONTAL} or {@link #VERTICAL} orientation
     */
    public LineLayout ( final int orientation )
    {
        super ();
        this.orientation = orientation;
    }

    /**
     * Constructs new {@link LineLayout}.
     *
     * @param orientation either {@link #HORIZONTAL} or {@link #VERTICAL} orientation
     * @param spacing     spacing between layout elements
     */
    public LineLayout ( final int orientation, final int spacing )
    {
        super ( spacing );
        this.orientation = orientation;
    }

    /**
     * Constructs new {@link LineLayout}.
     *
     * @param orientation  either {@link #HORIZONTAL} or {@link #VERTICAL} orientation
     * @param spacing      spacing between layout elements
     * @param partsSpacing spacing between {@link #START}, {@link #MIDDLE} and {@link #END} layout parts
     */
    public LineLayout ( final int orientation, final int spacing, final int partsSpacing )
    {
        super ( spacing, partsSpacing );
        this.orientation = orientation;
    }

    @Override
    public int getOrientation ( final Container container )
    {
        return orientation;
    }

    /**
     * Sets layout orientation.
     *
     * @param orientation either {@link #HORIZONTAL} or {@link #VERTICAL} orientation
     */
    public void setOrientation ( final int orientation )
    {
        this.orientation = orientation;
    }
}