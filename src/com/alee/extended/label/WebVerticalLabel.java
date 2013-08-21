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

package com.alee.extended.label;

import com.alee.laf.WebLookAndFeel;
import com.alee.laf.label.WebLabel;
import com.alee.utils.ReflectUtils;

import javax.swing.*;

/**
 * User: mgarin Date: 11.10.11 Time: 14:09
 */

public class WebVerticalLabel extends WebLabel
{
    public WebVerticalLabel ()
    {
        this ( false );
    }

    public WebVerticalLabel ( boolean clockwise )
    {
        super ();
        setClockwise ( clockwise );
    }

    public WebVerticalLabel ( Icon image )
    {
        this ( image, false );
    }

    public WebVerticalLabel ( Icon image, boolean clockwise )
    {
        super ( image );
        setClockwise ( clockwise );
    }

    public WebVerticalLabel ( Icon image, int horizontalAlignment )
    {
        this ( image, horizontalAlignment, false );
    }

    public WebVerticalLabel ( Icon image, int horizontalAlignment, boolean clockwise )
    {
        super ( image, horizontalAlignment );
        setClockwise ( clockwise );
    }

    public WebVerticalLabel ( String text )
    {
        this ( text, false );
    }

    public WebVerticalLabel ( String text, boolean clockwise )
    {
        super ( text );
        setClockwise ( clockwise );
    }

    public WebVerticalLabel ( String text, int horizontalAlignment )
    {
        this ( text, horizontalAlignment, false );
    }

    public WebVerticalLabel ( String text, int horizontalAlignment, boolean clockwise )
    {
        super ( text, horizontalAlignment );
        setClockwise ( clockwise );
    }

    public WebVerticalLabel ( String text, Icon icon, int horizontalAlignment )
    {
        this ( text, icon, horizontalAlignment, false );
    }

    public WebVerticalLabel ( String text, Icon icon, int horizontalAlignment, boolean clockwise )
    {
        super ( text, icon, horizontalAlignment );
        setClockwise ( clockwise );
    }

    public boolean isClockwise ()
    {
        return getWebUI ().isClockwise ();
    }

    public void setClockwise ( boolean clockwise )
    {
        getWebUI ().setClockwise ( clockwise );
    }

    @Override
    public WebVerticalLabelUI getWebUI ()
    {
        return ( WebVerticalLabelUI ) getUI ();
    }

    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebVerticalLabelUI ) )
        {
            try
            {
                setUI ( ( WebVerticalLabelUI ) ReflectUtils.createInstance ( WebLookAndFeel.verticalLabelUI ) );

            }
            catch ( Throwable e )
            {
                e.printStackTrace ();
                setUI ( new WebVerticalLabelUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }
}
