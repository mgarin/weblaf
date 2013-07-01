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
import com.alee.utils.laf.ShapeProvider;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 29.04.11 Time: 15:37
 */

public class WebScrollPane extends JScrollPane implements ShapeProvider
{
    private int preferredWidth = -1;
    private int minimumWidth = -1;
    private int preferredHeight = -1;
    private int minimumHeight = -1;

    public WebScrollPane ( Component view )
    {
        this ( view, true );
    }

    public WebScrollPane ( Component view, boolean drawBorder )
    {
        this ( view, drawBorder, WebScrollBarStyle.drawBorder );
    }

    public WebScrollPane ( Component view, boolean drawBorder, boolean drawInnerBorder )
    {
        super ( view );
        setDrawBorder ( drawBorder );
        getWebHorizontalScrollBar ().setDrawBorder ( drawInnerBorder );
        getWebVerticalScrollBar ().setDrawBorder ( drawInnerBorder );
        if ( !drawInnerBorder )
        {
            setCorner ( JScrollPane.LOWER_RIGHT_CORNER, null );
        }
    }

    public WebScrollBar createVerticalScrollBar ()
    {
        return new WebScrollBar ( WebScrollBar.VERTICAL );
    }

    public WebScrollBar createHorizontalScrollBar ()
    {
        return new WebScrollBar ( WebScrollBar.HORIZONTAL );
    }

    /**
     * Additional Web-component methods
     */

    public WebScrollBar getWebVerticalScrollBar ()
    {
        return ( WebScrollBar ) super.getVerticalScrollBar ();
    }

    public WebScrollBar getWebHorizontalScrollBar ()
    {
        return ( WebScrollBar ) super.getHorizontalScrollBar ();
    }

    public int getPreferredWidth ()
    {
        return preferredWidth;
    }

    public WebScrollPane setPreferredWidth ( int preferredWidth )
    {
        this.preferredWidth = preferredWidth;
        return this;
    }

    public int getMinimumWidth ()
    {
        return minimumWidth;
    }

    public WebScrollPane setMinimumWidth ( int minimumWidth )
    {
        this.minimumWidth = minimumWidth;
        return this;
    }

    public int getPreferredHeight ()
    {
        return preferredHeight;
    }

    public WebScrollPane setPreferredHeight ( int preferredHeight )
    {
        this.preferredHeight = preferredHeight;
        return this;
    }

    public int getMinimumHeight ()
    {
        return minimumHeight;
    }

    public WebScrollPane setMinimumHeight ( int minimumHeight )
    {
        this.minimumHeight = minimumHeight;
        return this;
    }

    /**
     * UI methods
     */

    public boolean isDrawBorder ()
    {
        return getWebUI ().isDrawBorder ();
    }

    public WebScrollPane setDrawBorder ( boolean drawBorder )
    {
        getWebUI ().setDrawBorder ( drawBorder );
        return this;
    }

    public int getRound ()
    {
        return getWebUI ().getRound ();
    }

    public WebScrollPane setRound ( int round )
    {
        getWebUI ().setRound ( round );
        return this;
    }

    public int getShadeWidth ()
    {
        return getWebUI ().getShadeWidth ();
    }

    public WebScrollPane setShadeWidth ( int shadeWidth )
    {
        getWebUI ().setShadeWidth ( shadeWidth );
        return this;
    }

    public Insets getMargin ()
    {
        return getWebUI ().getMargin ();
    }

    public WebScrollPane setMargin ( Insets margin )
    {
        getWebUI ().setMargin ( margin );
        return this;
    }

    public WebScrollPane setMargin ( int top, int left, int bottom, int right )
    {
        return  setMargin ( new Insets ( top, left, bottom, right ) );
    }

    public WebScrollPane setMargin ( int spacing )
    {
        return setMargin ( spacing, spacing, spacing, spacing );
    }

    public boolean isDrawFocus ()
    {
        return getWebUI ().isDrawFocus ();
    }

    public WebScrollPane setDrawFocus ( boolean drawFocus )
    {
        getWebUI ().setDrawFocus ( drawFocus );
        return this;
    }

    public boolean isDrawBackground ()
    {
        return getWebUI ().isDrawBackground ();
    }

    public WebScrollPane setDrawBackground ( boolean drawBackground )
    {
        getWebUI ().setDrawBackground ( drawBackground );
        return this;
    }

    public Color getBorderColor ()
    {
        return getWebUI ().getBorderColor ();
    }

    public WebScrollPane setBorderColor ( Color borderColor )
    {
        getWebUI ().setBorderColor ( borderColor );
        return this;
    }

    public Color getDarkBorder ()
    {
        return getWebUI ().getDarkBorder ();
    }

    public WebScrollPane setDarkBorder ( Color darkBorder )
    {
        getWebUI ().setDarkBorder ( darkBorder );
        return this;
    }

    public Shape provideShape ()
    {
        return getWebUI ().provideShape ();
    }

    public WebScrollPaneUI getWebUI ()
    {
        return ( WebScrollPaneUI ) getUI ();
    }

    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebScrollPaneUI ) )
        {
            try
            {
                setUI ( ( WebScrollPaneUI ) ReflectUtils.createInstance ( WebLookAndFeel.scrollPaneUI ) );
            }
            catch ( Throwable e )
            {
                e.printStackTrace ();
                setUI ( new WebScrollPaneUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }

    //    public void setOpaque ( boolean isOpaque )
    //    {
    //        super.setOpaque ( isOpaque );
    //        if ( getViewport () != null )
    //        {
    //            getViewport ().setOpaque ( isOpaque );
    //        }
    //    }

    public Dimension getPreferredSize ()
    {
        Dimension ps = super.getPreferredSize ();
        if ( preferredWidth != -1 )
        {
            ps.width = preferredWidth;
        }
        else if ( minimumWidth != -1 )
        {
            ps.width = Math.max ( minimumWidth, ps.width );
        }
        if ( preferredHeight != -1 )
        {
            ps.height = preferredHeight;
        }
        else if ( minimumHeight != -1 )
        {
            ps.height = Math.max ( minimumHeight, ps.height );
        }
        return ps;
    }
}