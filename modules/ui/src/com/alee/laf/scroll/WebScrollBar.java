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
import com.alee.managers.log.Log;
import com.alee.managers.style.StyleId;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SizeUtils;
import com.alee.managers.style.Styleable;
import com.alee.utils.swing.SizeMethods;

import javax.swing.*;
import java.awt.*;

/**
 * This JScrollBar extension class provides a direct access to WebScrollBarUI methods.
 *
 * @author Mikle Garin
 */

public class WebScrollBar extends JScrollBar implements Styleable, SizeMethods<WebScrollBar>
{
    /**
     * Constructs new scroll bar.
     */
    public WebScrollBar ()
    {
        super ();
    }

    /**
     * Constructs new scroll bar with the specified orientation.
     *
     * @param orientation scroll bar orientation
     */
    public WebScrollBar ( final int orientation )
    {
        super ( orientation );
    }

    /**
     * Constructs new scroll bar with the specified orientation and values.
     *
     * @param orientation scroll bar orientation
     * @param value       scroll bar value
     * @param extent      scroll bar extent
     * @param min         scroll bar minimum value
     * @param max         scroll bar maximum value
     */
    public WebScrollBar ( final int orientation, final int value, final int extent, final int min, final int max )
    {
        super ( orientation, value, extent, min, max );
    }

    /**
     * Constructs new scroll bar.
     *
     * @param id style ID
     */
    public WebScrollBar ( final StyleId id )
    {
        super ();
        setStyleId ( id );
    }

    /**
     * Constructs new scroll bar with the specified orientation.
     *
     * @param id          style ID
     * @param orientation scroll bar orientation
     */
    public WebScrollBar ( final StyleId id, final int orientation )
    {
        super ( orientation );
        setStyleId ( id );
    }

    /**
     * Constructs new scroll bar with the specified orientation and values.
     *
     * @param id          style ID
     * @param orientation scroll bar orientation
     * @param value       scroll bar value
     * @param extent      scroll bar extent
     * @param min         scroll bar minimum value
     * @param max         scroll bar maximum value
     */
    public WebScrollBar ( final StyleId id, final int orientation, final int value, final int extent, final int min, final int max )
    {
        super ( orientation, value, extent, min, max );
        setStyleId ( id );
    }

    /**
     * Returns whether scroll bar arrow buttons should be displayed or not.
     *
     * @return true if scroll bar arrow buttons should be displayed, false otherwise
     */
    public boolean isPaintButtons ()
    {
        return getWebUI ().isPaintButtons ();
    }

    /**
     * Sets whether scroll bar arrow buttons should be displayed or not.
     *
     * @param paintButtons whether scroll bar arrow buttons should be displayed or not
     * @return scroll bar
     */
    public WebScrollBar setPaintButtons ( final boolean paintButtons )
    {
        getWebUI ().setPaintButtons ( paintButtons );
        return this;
    }

    /**
     * Returns whether scroll bar track should be displayed or not.
     *
     * @return true if scroll bar track should be displayed, false otherwise
     */
    public boolean isPaintTrack ()
    {
        return getWebUI ().isPaintTrack ();
    }

    /**
     * Sets whether scroll bar track should be displayed or not.
     *
     * @param paintTrack whether scroll bar track should be displayed or not
     * @return scroll bar
     */
    public WebScrollBar setPaintTrack ( final boolean paintTrack )
    {
        getWebUI ().setPaintTrack ( paintTrack );
        return this;
    }

    @Override
    public StyleId getStyleId ()
    {
        return getWebUI ().getStyleId ();
    }

    @Override
    public StyleId setStyleId ( final StyleId id )
    {
        return getWebUI ().setStyleId ( id );
    }

    /**
     * Returns Web-UI applied to this class.
     *
     * @return Web-UI applied to this class
     */
    private WebScrollBarUI getWebUI ()
    {
        return ( WebScrollBarUI ) getUI ();
    }

    /**
     * Installs a Web-UI into this component.
     */
    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebScrollBarUI ) )
        {
            try
            {
                setUI ( ( WebScrollBarUI ) ReflectUtils.createInstance ( WebLookAndFeel.scrollBarUI ) );
            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebScrollBarUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }

    @Override
    public int getPreferredWidth ()
    {
        return SizeUtils.getPreferredWidth ( this );
    }

    @Override
    public WebScrollBar setPreferredWidth ( final int preferredWidth )
    {
        return SizeUtils.setPreferredWidth ( this, preferredWidth );
    }

    @Override
    public int getPreferredHeight ()
    {
        return SizeUtils.getPreferredHeight ( this );
    }

    @Override
    public WebScrollBar setPreferredHeight ( final int preferredHeight )
    {
        return SizeUtils.setPreferredHeight ( this, preferredHeight );
    }

    @Override
    public int getMinimumWidth ()
    {
        return SizeUtils.getMinimumWidth ( this );
    }

    @Override
    public WebScrollBar setMinimumWidth ( final int minimumWidth )
    {
        return SizeUtils.setMinimumWidth ( this, minimumWidth );
    }

    @Override
    public int getMinimumHeight ()
    {
        return SizeUtils.getMinimumHeight ( this );
    }

    @Override
    public WebScrollBar setMinimumHeight ( final int minimumHeight )
    {
        return SizeUtils.setMinimumHeight ( this, minimumHeight );
    }

    @Override
    public int getMaximumWidth ()
    {
        return SizeUtils.getMaximumWidth ( this );
    }

    @Override
    public WebScrollBar setMaximumWidth ( final int maximumWidth )
    {
        return SizeUtils.setMaximumWidth ( this, maximumWidth );
    }

    @Override
    public int getMaximumHeight ()
    {
        return SizeUtils.getMaximumHeight ( this );
    }

    @Override
    public WebScrollBar setMaximumHeight ( final int maximumHeight )
    {
        return SizeUtils.setMaximumHeight ( this, maximumHeight );
    }

    @Override
    public Dimension getPreferredSize ()
    {
        return SizeUtils.getPreferredSize ( this, super.getPreferredSize () );
    }

    @Override
    public WebScrollBar setPreferredSize ( final int width, final int height )
    {
        return SizeUtils.setPreferredSize ( this, width, height );
    }
}