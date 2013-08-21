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

package com.alee.managers.language.data;

import com.alee.managers.tooltip.TooltipWay;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import java.io.Serializable;

/**
 * User: mgarin Date: 27.04.12 Time: 16:15
 */

@XStreamAlias ("tooltip")
@XStreamConverter (TooltipConverter.class)
public final class Tooltip implements Serializable, Cloneable
{
    private TooltipType type;
    private TooltipWay way;
    private Integer delay;
    private String text;

    public Tooltip ()
    {
        super ();
    }

    public Tooltip ( String text )
    {
        super ();
        setText ( text );
    }

    public Tooltip ( Integer delay, String text )
    {
        super ();
        setDelay ( delay );
        setText ( text );
    }

    public Tooltip ( TooltipWay way, String text )
    {
        super ();
        setWay ( way );
        setText ( text );
    }

    public Tooltip ( TooltipWay way, Integer delay, String text )
    {
        super ();
        setWay ( way );
        setDelay ( delay );
        setText ( text );
    }

    public Tooltip ( TooltipType type, String text )
    {
        super ();
        setType ( type );
        setText ( text );
    }

    public Tooltip ( TooltipType type, TooltipWay way, Integer delay, String text )
    {
        super ();
        setType ( type );
        setWay ( way );
        setDelay ( delay );
        setText ( text );
    }

    public TooltipType getType ()
    {
        return type;
    }

    public void setType ( TooltipType type )
    {
        this.type = type;
    }

    public TooltipWay getWay ()
    {
        return way;
    }

    public void setWay ( TooltipWay way )
    {
        this.way = way;
    }

    public Integer getDelay ()
    {
        return delay;
    }

    public void setDelay ( Integer delay )
    {
        this.delay = delay;
    }

    public String getText ()
    {
        return text;
    }

    public void setText ( String text )
    {
        this.text = text;
    }

    @Override
    public Tooltip clone ()
    {
        return new Tooltip ( type, way, delay, text );
    }

    public String toString ()
    {
        return text + ( type != null ? " (" + type + ")" : "" ) +
                ( delay != null ? " (" + delay + "ms delay)" : "" ) +
                ( way != null ? " (" + way + ")" : "" );
    }
}