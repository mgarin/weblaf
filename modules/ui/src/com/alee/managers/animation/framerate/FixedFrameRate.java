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

package com.alee.managers.animation.framerate;

import com.alee.managers.animation.AnimationException;

/**
 * {@link FrameRate} implementation providing fixed frame rate.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-AnimationManager">How to use AnimationManager</a>
 * @see com.alee.managers.animation.AnimationManager
 */
public final class FixedFrameRate implements FrameRate
{
    /**
     * Frame rate.
     */
    private final double frameRate;

    /**
     * Constructs new {@link FixedFrameRate}.
     *
     * @param frameRate frame rate
     */
    public FixedFrameRate ( final double frameRate )
    {
        super ();
        if ( frameRate == 0 )
        {
            throw new AnimationException ( "Frame rate should never be zero" );
        }
        else if ( Double.isInfinite ( frameRate ) || Double.isNaN ( frameRate ) )
        {
            throw new AnimationException ( "Incorrect frame rate specified: " + frameRate );
        }
        this.frameRate = frameRate;
    }

    @Override
    public double value ()
    {
        return frameRate;
    }

    @Override
    public boolean isFixed ()
    {
        return true;
    }

    @Override
    public String toString ()
    {
        return "Fixed frame rate [ value = " + value () + " ]";
    }
}