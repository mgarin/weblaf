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

package com.alee.utils.ninepatch;

import com.alee.api.Identifiable;
import com.alee.api.annotations.Nullable;
import com.alee.api.clone.Clone;
import com.alee.utils.TextUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.awt.*;
import java.io.Serializable;

/**
 * Data class representing various {@link NinePatchIcon} patch intervals.
 * Interval represents a stretched area whenever {@link #pixel} is set to {@code false}.
 * Whenever {@link #pixel} is set to {@code true} interval represents a non-stretchable area.
 *
 * @author Mikle Garin
 */
@XStreamAlias ( "NinePatchInterval" )
public final class NinePatchInterval implements Identifiable, Cloneable, Serializable
{
    /**
     * Unique runtime interval identifier.
     */
    private final transient String id;

    /**
     * Whether this interval represents pixel or stretched area.
     */
    @XStreamAsAttribute
    private boolean pixel;

    /**
     * Interval start.
     */
    @XStreamAsAttribute
    private int start;

    /**
     * Interval end.
     */
    @XStreamAsAttribute
    private int end;

    /**
     * Constructs new {@link NinePatchInterval}.
     */
    public NinePatchInterval ()
    {
        this ( 0, 0, true );
    }

    /**
     * Constructs new {@link NinePatchInterval}.
     *
     * @param start interval start
     */
    public NinePatchInterval ( final int start )
    {
        this ( start, start, true );
    }

    /**
     * Constructs new {@link NinePatchInterval}.
     *
     * @param start interval start
     * @param pixel whether this interval represents pixel or stretched area
     */
    public NinePatchInterval ( final int start, final boolean pixel )
    {
        this ( start, start, pixel );
    }

    /**
     * Constructs new {@link NinePatchInterval}.
     *
     * @param start interval start
     * @param end   interval end
     */
    public NinePatchInterval ( final int start, final int end )
    {
        this ( start, end, true );
    }

    /**
     * Constructs new {@link NinePatchInterval}.
     *
     * @param start interval start
     * @param end   interval end
     * @param pixel whether this interval represents pixel or stretched area
     */
    public NinePatchInterval ( final int start, final int end, final boolean pixel )
    {
        this.id = TextUtils.generateId ( "NPINT" );
        this.start = start;
        this.end = end;
        this.pixel = pixel;
    }

    /**
     * Returns unique runtime interval identifier.
     *
     * @return unique runtime interval identifier
     */
    @Nullable
    @Override
    public String getId ()
    {
        return id;
    }

    /**
     * Returns whether this interval represents pixel or stretched area.
     *
     * @return {@code true} if this interval represents pixel area, {@code false} if it represents stretched area
     */
    public boolean isPixel ()
    {
        return pixel;
    }

    /**
     * Sets whether this interval represents pixel or stretched area
     *
     * @param pixel whether this interval represents pixel or stretched area
     */
    public void setPixel ( final boolean pixel )
    {
        this.pixel = pixel;
    }

    /**
     * Returns interval start.
     *
     * @return interval start
     */
    public int getStart ()
    {
        return start;
    }

    /**
     * Sets interval start.
     *
     * @param start interval start
     */
    public void setStart ( final int start )
    {
        this.start = start;
    }

    /**
     * Returns interval end.
     *
     * @return interval end
     */
    public int getEnd ()
    {
        return end;
    }

    /**
     * Sets interval end.
     *
     * @param end interval end
     */
    public void setEnd ( final int end )
    {
        this.end = end;
    }

    /**
     * Returns whether or not this interval intersects with the specified one.
     *
     * @param npi another {@link NinePatchInterval}
     * @return {@code true} if this interval intersects with the specified one, {@code false} otherwise
     */
    public boolean intersects ( final NinePatchInterval npi )
    {
        return new Rectangle ( getStart (), 0, getEnd () - getStart (), 1 )
                .intersects ( new Rectangle ( npi.getStart (), 0, npi.getEnd () - npi.getStart (), 1 ) );
    }

    /**
     * Returns interval lendth.
     *
     * @return interval lendth
     */
    public int getLength ()
    {
        return getEnd () - getStart ();
    }

    @Override
    public NinePatchInterval clone ()
    {
        return Clone.deep ().clone ( this );
    }

    @Override
    public boolean equals ( final Object object )
    {
        final boolean equals;
        if ( object instanceof NinePatchInterval )
        {
            final NinePatchInterval npi = ( NinePatchInterval ) object;
            equals = isPixel () == npi.isPixel () && getStart () == npi.getStart () && getEnd () == npi.getEnd ();
        }
        else
        {
            equals = false;
        }
        return equals;
    }
}