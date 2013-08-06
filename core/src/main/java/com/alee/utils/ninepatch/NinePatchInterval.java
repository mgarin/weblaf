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

import com.alee.utils.TextUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.awt.*;
import java.io.Serializable;

/**
 * User: mgarin Date: 13.12.11 Time: 15:12
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

    public NinePatchInterval ( int start )
    {
        this ( start, start );
    }

    public NinePatchInterval ( int start, boolean pixel )
    {
        this ( start, start, pixel );
    }

    public NinePatchInterval ( int start, int end )
    {
        this ( start, end, true );
    }

    public NinePatchInterval ( int start, int end, boolean pixel )
    {
        super ();
        setId ();
        setPixel ( pixel );
        setStart ( start );
        setEnd ( end );
    }

    public String getId ()
    {
        return id;
    }

    public void setId ( String id )
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

    public void setPixel ( boolean pixel )
    {
        this.pixel = pixel;
    }

    public int getStart ()
    {
        return start;
    }

    public void setStart ( int start )
    {
        this.start = start;
    }

    public int getEnd ()
    {
        return end;
    }

    public void setEnd ( int end )
    {
        this.end = end;
    }

    public boolean intersects ( NinePatchInterval npi )
    {
        return new Rectangle ( getStart (), 0, getEnd () - getStart (), 1 )
                .intersects ( new Rectangle ( npi.getStart (), 0, npi.getEnd () - npi.getStart (), 1 ) );
    }

    public NinePatchInterval clone ()
    {
        NinePatchInterval npi = new NinePatchInterval ();
        npi.setId ( getId () );
        npi.setPixel ( isPixel () );
        npi.setStart ( getStart () );
        npi.setEnd ( getEnd () );
        return npi;
    }

    public boolean isSame ( NinePatchInterval ninePatchInterval )
    {
        return ninePatchInterval != null && this.getId ().equals ( ninePatchInterval.getId () );
    }

    public boolean equals ( Object obj )
    {
        if ( obj != null && obj instanceof NinePatchInterval )
        {
            NinePatchInterval npi = ( NinePatchInterval ) obj;
            return isPixel () == npi.isPixel () && getStart () == npi.getStart () &&
                    getEnd () == npi.getEnd ();
        }
        else
        {
            return false;
        }
    }
}
