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

package com.alee.managers.animation.easing;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Bezier easing implementation.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-AnimationManager">How to use AnimationManager</a>
 * @see com.alee.managers.animation.AnimationManager
 */
@XStreamAlias ( "Bezier" )
public final class Bezier extends AbstractEasing
{
    /**
     * First curve control X coordinate.
     */
    @XStreamAsAttribute
    private final double x1;

    /**
     * First curve control Y coordinate.
     */
    @XStreamAsAttribute
    private final double y1;

    /**
     * Second curve control X coordinate.
     */
    @XStreamAsAttribute
    private final double x2;

    /**
     * Second curve control Y coordinate.
     */
    @XStreamAsAttribute
    private final double y2;

    /**
     * Constructs new bezier easing algorithm.
     *
     * @param x1 first curve control X coordinate, should be in [0..1] range
     * @param y1 first curve control Y coordinate, could be anything but keeping it in [-1..2] range is recommended
     * @param x2 second curve control X coordinate, should be in [0..1] range
     * @param y2 second curve control Y coordinate, could be anything but keeping it in [-1..2] range is recommended
     */
    public Bezier ( final double x1, final double y1, final double x2, final double y2 )
    {
        super ();
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override
    public String getTitle ()
    {
        return "Bezier";
    }

    @Override
    protected double calculateImpl ( final double start, final double distance, final double current, final double total )
    {
        // Scaling time using curve X coordinate
        final double xprogress = 1 - current / total;
        final double time = 3 * Math.pow ( 1 - xprogress, 2 ) * xprogress * x1 +
                3 * ( 1 - xprogress ) * xprogress * xprogress * x2 + xprogress * xprogress * xprogress;

        // Retrieving Y coordinate according to acquired progress
        final double yprogress = 1 - time;
        final double animation = 3 * Math.pow ( 1 - yprogress, 2 ) * yprogress * y1 +
                3 * ( 1 - yprogress ) * yprogress * yprogress * y2 + yprogress * yprogress * yprogress;

        return start + distance * animation;
    }
}