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

import com.alee.api.Mergeable;
import com.alee.utils.MergeUtils;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import java.io.Serializable;

/**
 * Custom shape corners round.
 *
 * @author Mikle Garin
 */

@XStreamConverter ( RoundConverter.class )
public final class Round implements Serializable, Cloneable, Mergeable<Round>
{
    /**
     * Top left corner round.
     */
    public int topLeft;

    /**
     * Top right corner round.
     */
    public int topRight;

    /**
     * Bottom right corner round.
     */
    public int bottomRight;

    /**
     * Bottom left corner round.
     */
    public int bottomLeft;

    /**
     * Constructs zero corners round data.
     */
    public Round ()
    {
        super ();
        this.topLeft = 0;
        this.topRight = 0;
        this.bottomRight = 0;
        this.bottomLeft = 0;
    }

    /**
     * Constructs corners round data with the specified values.
     *
     * @param topLeft     top left corner round
     * @param topRight    top right corner round
     * @param bottomRight bottom right corner round
     * @param bottomLeft  bottom left corner round
     */
    public Round ( final int topLeft, final int topRight, final int bottomRight, final int bottomLeft )
    {
        super ();
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomRight = bottomRight;
        this.bottomLeft = bottomLeft;
    }

    @Override
    public String toString ()
    {
        return RoundConverter.roundToString ( this );
    }

    @Override
    public Round merge ( final Round merged )
    {
        if ( merged.topLeft != -1 )
        {
            topLeft = merged.topLeft;
        }
        if ( merged.topRight != -1 )
        {
            topRight = merged.topRight;
        }
        if ( merged.bottomRight != -1 )
        {
            bottomRight = merged.bottomRight;
        }
        if ( merged.bottomLeft != -1 )
        {
            bottomLeft = merged.bottomLeft;
        }
        return this;
    }

    @Override
    public Round clone ()
    {
        return MergeUtils.cloneByFieldsSafely ( this );
    }
}