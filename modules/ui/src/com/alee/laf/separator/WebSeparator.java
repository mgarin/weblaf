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

package com.alee.laf.separator;

import com.alee.laf.WebLookAndFeel;
import com.alee.managers.log.Log;
import com.alee.utils.ReflectUtils;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 21.09.2010 Time: 15:37:04
 */

public class WebSeparator extends JSeparator
{
    public WebSeparator ()
    {
        super ();
    }

    public WebSeparator ( final int orientation )
    {
        super ( orientation );
    }

    public WebSeparator ( final boolean reversedColors )
    {
        super ();
        setReversedColors ( reversedColors );
    }

    public WebSeparator ( final int orientation, final boolean reversedColors )
    {
        super ( orientation );
        setReversedColors ( reversedColors );
    }

    public WebSeparator ( final boolean drawSideLines, final int orientation )
    {
        super ( orientation );
        setDrawSideLines ( drawSideLines );
    }

    public WebSeparator ( final boolean drawSideLines, final int orientation, final boolean reversedColors )
    {
        super ( orientation );
        setDrawSideLines ( drawSideLines );
        setReversedColors ( reversedColors );
    }

    public WebSeparator ( final boolean drawLeadingLine, final boolean drawTrailingLine )
    {
        super ();
        setDrawLeadingLine ( drawLeadingLine );
        setDrawTrailingLine ( drawTrailingLine );
    }

    public WebSeparator ( final boolean drawLeadingLine, final boolean drawTrailingLine, final int orientation )
    {
        super ( orientation );
        setDrawLeadingLine ( drawLeadingLine );
        setDrawTrailingLine ( drawTrailingLine );
    }

    public WebSeparator ( final boolean drawLeadingLine, final boolean drawTrailingLine, final int orientation,
                          final boolean reversedColors )
    {
        super ( orientation );
        setDrawLeadingLine ( drawLeadingLine );
        setDrawTrailingLine ( drawTrailingLine );
        setReversedColors ( reversedColors );
    }

    public Color getSeparatorColor ()
    {
        return getWebUI ().getSeparatorColor ();
    }

    public void setSeparatorColor ( final Color separatorColor )
    {
        getWebUI ().setSeparatorColor ( separatorColor );
    }

    public Color getSeparatorUpperColor ()
    {
        return getWebUI ().getSeparatorUpperColor ();
    }

    public void setSeparatorUpperColor ( final Color separatorUpperColor )
    {
        getWebUI ().setSeparatorUpperColor ( separatorUpperColor );
    }

    public Color getSeparatorLightColor ()
    {
        return getWebUI ().getSeparatorLightColor ();
    }

    public void setSeparatorLightColor ( final Color separatorLightColor )
    {
        getWebUI ().setSeparatorLightColor ( separatorLightColor );
    }

    public Color getSeparatorLightUpperColor ()
    {
        return getWebUI ().getSeparatorLightUpperColor ();
    }

    public void setSeparatorLightUpperColor ( final Color separatorLightUpperColor )
    {
        getWebUI ().setSeparatorLightUpperColor ( separatorLightUpperColor );
    }

    public boolean isReversedColors ()
    {
        return getWebUI ().isReversedColors ();
    }

    public void setReversedColors ( final boolean reversedColors )
    {
        getWebUI ().setReversedColors ( reversedColors );
    }

    public void setDrawSideLines ( final boolean drawSideLines )
    {
        setDrawLeadingLine ( drawSideLines );
        setDrawTrailingLine ( drawSideLines );
    }

    public boolean isDrawLeadingLine ()
    {
        return getWebUI ().isDrawLeadingLine ();
    }

    public void setDrawLeadingLine ( final boolean drawLeadingLine )
    {
        getWebUI ().setDrawLeadingLine ( drawLeadingLine );
    }

    public boolean isDrawTrailingLine ()
    {
        return getWebUI ().isDrawTrailingLine ();
    }

    public void setDrawTrailingLine ( final boolean drawTrailingLine )
    {
        getWebUI ().setDrawTrailingLine ( drawTrailingLine );
    }

    public Insets getMargin ()
    {
        return getWebUI ().getMargin ();
    }

    public void setMargin ( final Insets margin )
    {
        getWebUI ().setMargin ( margin );
    }

    public WebSeparator setMargin ( final int top, final int left, final int bottom, final int right )
    {
        setMargin ( new Insets ( top, left, bottom, right ) );
        return this;
    }

    public WebSeparator setMargin ( final int spacing )
    {
        return setMargin ( spacing, spacing, spacing, spacing );
    }

    public WebSeparatorUI getWebUI ()
    {
        return ( WebSeparatorUI ) getUI ();
    }

    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebSeparatorUI ) )
        {
            try
            {
                setUI ( ( WebSeparatorUI ) ReflectUtils.createInstance ( WebLookAndFeel.separatorUI ) );
            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebSeparatorUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }

    public static WebSeparator createHorizontal ()
    {
        return new WebSeparator ( WebSeparator.HORIZONTAL );
    }

    public static WebSeparator createVertical ()
    {
        return new WebSeparator ( WebSeparator.VERTICAL );
    }
}