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

import com.alee.utils.MergeUtils;
import com.alee.utils.TextUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.awt.*;
import java.io.Serializable;

/**
 * @author Mikle Garin
 */

@XStreamAlias ("NinePatchInterval")
public final class NinePatchInterval implements Serializable, Cloneable
{
    public static final String ID_PREFIX = "NPI";

    @XStreamAsAttribute
    private transient String id;

    @XStreamAsAttribute
    private boolean pixel;

    @XStreamAsAttribute
    private int start;

    @XStreamAsAttribute
    private int end;

    public NinePatchInterval ()
    {
        this ( 0 );
    }

    public NinePatchInterval ( final int start )
    {
        this ( start, start );
    }

    public NinePatchInterval ( final int start, final boolean pixel )
    {
        this ( start, start, pixel );
    }

    public NinePatchInterval ( final int start, final int end )
    {
        this ( start, end, true );
    }

    public NinePatchInterval ( final int start, final int end, final boolean pixel )
    {
        super ();
        setId ();
        setPixel ( pixel );
        setStart ( start );
        setEnd ( end );
    }

    public NinePatchInterval ( final String id )
    {
        super ();
        setId ( id );
    }

    public String getId ()
    {
        return id;
    }

    public void setId ( final String id )
    {
        this.id = id;
    }

    public void setId ()
    {
        this.id = TextUtils.generateId ( ID_PREFIX );
    }

    public boolean isPixel ()
    {
        return pixel;
    }

    public void setPixel ( final boolean pixel )
    {
        this.pixel = pixel;
    }

    public int getStart ()
    {
        return start;
    }

    public void setStart ( final int start )
    {
        this.start = start;
    }

    public int getEnd ()
    {
        return end;
    }

    public void setEnd ( final int end )
    {
        this.end = end;
    }

    public boolean intersects ( final NinePatchInterval npi )
    {
        return new Rectangle ( getStart (), 0, getEnd () - getStart (), 1 )
                .intersects ( new Rectangle ( npi.getStart (), 0, npi.getEnd () - npi.getStart (), 1 ) );
    }

    public int getLength ()
    {
        return getEnd () - getStart ();
    }

    public boolean isSame ( final NinePatchInterval ninePatchInterval )
    {
        return ninePatchInterval != null && this.getId ().equals ( ninePatchInterval.getId () );
    }

    public boolean equals ( final Object obj )
    {
        if ( obj != null && obj instanceof NinePatchInterval )
        {
            final NinePatchInterval npi = ( NinePatchInterval ) obj;
            return isPixel () == npi.isPixel () && getStart () == npi.getStart () &&
                    getEnd () == npi.getEnd ();
        }
        else
        {
            return false;
        }
    }

    @Override
    public NinePatchInterval clone ()
    {
        return MergeUtils.cloneByFieldsSafely ( this, getId () );
    }
}