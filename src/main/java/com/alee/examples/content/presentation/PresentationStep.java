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

package com.alee.examples.content.presentation;

/**
 * User: mgarin Date: 21.12.12 Time: 16:14
 */

public class PresentationStep
{
    private String shortName;
    private int duration;
    private Runnable onStart;
    private Runnable onEnd;

    public PresentationStep ( String shortName, int duration )
    {
        this ( shortName, duration, null, null );
    }

    public PresentationStep ( String shortName, int duration, Runnable onStart, Runnable onEnd )
    {
        super ();
        setShortName ( shortName );
        setDuration ( duration );
        setOnStart ( onStart );
        setOnEnd ( onEnd );
    }

    public String getShortName ()
    {
        return shortName;
    }

    public void setShortName ( String shortName )
    {
        this.shortName = shortName;
    }

    public int getDuration ()
    {
        return duration;
    }

    public void setDuration ( int duration )
    {
        this.duration = duration;
    }

    public Runnable getOnStart ()
    {
        return onStart;
    }

    public void setOnStart ( Runnable onStart )
    {
        this.onStart = onStart;
    }

    public Runnable getOnEnd ()
    {
        return onEnd;
    }

    public void setOnEnd ( Runnable onEnd )
    {
        this.onEnd = onEnd;
    }
}
