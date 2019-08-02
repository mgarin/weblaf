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

import com.alee.api.merge.Overwriting;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import java.io.Serializable;

/**
 * Shape corners round data.
 *
 * @author Mikle Garin
 */
@XStreamConverter ( RoundConverter.class )
public final class Round implements Overwriting, Cloneable, Serializable
{
    /**
     * Top left corner round.
     */
    public final int topLeft;

    /**
     * Top right corner round.
     */
    public final int topRight;

    /**
     * Bottom right corner round.
     */
    public final int bottomRight;

    /**
     * Bottom left corner round.
     */
    public final int bottomLeft;

    /**
     * Constructs zero corners round settings.
     */
    public Round ()
    {
        this ( 0, 0, 0, 0 );
    }

    /**
     * Constructs corners round settings with the specified values.
     *
     * @param topLeft     top left corner round
     * @param topRight    top right corner round
     * @param bottomRight bottom right corner round
     * @param bottomLeft  bottom left corner round
     */
    public Round ( final int topLeft, final int topRight, final int bottomRight, final int bottomLeft )
    {
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomRight = bottomRight;
        this.bottomLeft = bottomLeft;
    }

    /**
     * Always returns {@code true} to avoid any merge operations between {@link Round} instances.
     *
     * @return always {@code true} to avoid any merge operations between {@link Round} instances
     */
    @Override
    public boolean isOverwrite ()
    {
        return true;
    }

    @Override
    public String toString ()
    {
        return RoundConverter.roundToString ( this );
    }
}