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

import com.alee.utils.MergeUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import java.io.Serializable;

/**
 * @author Mikle Garin
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

    public Tooltip ( final String text )
    {
        super ();
        setText ( text );
    }

    public Tooltip ( final Integer delay, final String text )
    {
        super ();
        setDelay ( delay );
        setText ( text );
    }

    public Tooltip ( final TooltipWay way, final String text )
    {
        super ();
        setWay ( way );
        setText ( text );
    }

    public Tooltip ( final TooltipWay way, final Integer delay, final String text )
    {
        super ();
        setWay ( way );
        setDelay ( delay );
        setText ( text );
    }

    public Tooltip ( final TooltipType type, final String text )
    {
        super ();
        setType ( type );
        setText ( text );
    }

    public Tooltip ( final TooltipType type, final TooltipWay way, final Integer delay, final String text )
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

    public void setType ( final TooltipType type )
    {
        this.type = type;
    }

    public TooltipWay getWay ()
    {
        return way;
    }

    public void setWay ( final TooltipWay way )
    {
        this.way = way;
    }

    public Integer getDelay ()
    {
        return delay;
    }

    public void setDelay ( final Integer delay )
    {
        this.delay = delay;
    }

    public String getText ()
    {
        return text;
    }

    public void setText ( final String text )
    {
        this.text = text;
    }

    @Override
    public Tooltip clone ()
    {
        return MergeUtils.cloneByFieldsSafely ( this );
    }

    @Override
    public String toString ()
    {
        return text + ( type != null ? " (" + type + ")" : "" ) +
                ( delay != null ? " (" + delay + "ms delay)" : "" ) +
                ( way != null ? " (" + way + ")" : "" );
    }
}