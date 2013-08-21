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

package com.alee.laf.scroll;

import com.alee.laf.WebLookAndFeel;
import com.alee.utils.ReflectUtils;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 28.06.11 Time: 1:00
 */

public class WebScrollBar extends JScrollBar
{
    public WebScrollBar ()
    {
        super ();
    }

    public WebScrollBar ( int orientation )
    {
        super ( orientation );
    }

    public WebScrollBar ( int orientation, int value, int extent, int min, int max )
    {
        super ( orientation, value, extent, min, max );
    }

    public boolean isDrawBorder ()
    {
        return getWebUI ().isDrawBorder ();
    }

    public void setDrawBorder ( boolean drawBorder )
    {
        getWebUI ().setDrawBorder ( drawBorder );
    }

    public int getRound ()
    {
        return getWebUI ().getRound ();
    }

    public void setRound ( int rounding )
    {
        getWebUI ().setRound ( rounding );
    }

    public Color getScrollBg ()
    {
        return getWebUI ().getScrollBg ();
    }

    public void setScrollBg ( Color scrollBg )
    {
        getWebUI ().setScrollBg ( scrollBg );
    }

    public Color getScrollBorder ()
    {
        return getWebUI ().getScrollBorder ();
    }

    public void setScrollBorder ( Color scrollBorder )
    {
        getWebUI ().setScrollBorder ( scrollBorder );
    }

    public Color getScrollBarBorder ()
    {
        return getWebUI ().getScrollBarBorder ();
    }

    public void setScrollBarBorder ( Color scrollBarBorder )
    {
        getWebUI ().setScrollBarBorder ( scrollBarBorder );
    }

    public Color getScrollGradientLeft ()
    {
        return getWebUI ().getScrollGradientLeft ();
    }

    public void setScrollGradientLeft ( Color scrollGradientLeft )
    {
        getWebUI ().setScrollGradientLeft ( scrollGradientLeft );
    }

    public Color getScrollGradientRight ()
    {
        return getWebUI ().getScrollGradientRight ();
    }

    public void setScrollGradientRight ( Color scrollGradientRight )
    {
        getWebUI ().setScrollGradientRight ( scrollGradientRight );
    }

    public Color getScrollSelGradientLeft ()
    {
        return getWebUI ().getScrollSelGradientLeft ();
    }

    public void setScrollSelGradientLeft ( Color scrollSelGradientLeft )
    {
        getWebUI ().setScrollSelGradientLeft ( scrollSelGradientLeft );
    }

    public Color getScrollSelGradientRight ()
    {
        return getWebUI ().getScrollSelGradientRight ();
    }

    public void setScrollSelGradientRight ( Color scrollSelGradientRight )
    {
        getWebUI ().setScrollSelGradientRight ( scrollSelGradientRight );
    }

    public WebScrollBarUI getWebUI ()
    {
        return ( WebScrollBarUI ) getUI ();
    }

    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebScrollBarUI ) )
        {
            try
            {
                setUI ( ( WebScrollBarUI ) ReflectUtils.createInstance ( WebLookAndFeel.scrollBarUI ) );
            }
            catch ( Throwable e )
            {
                e.printStackTrace ();
                setUI ( new WebScrollBarUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }
}
