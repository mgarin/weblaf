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

import com.alee.extended.painter.Painter;
import com.alee.managers.style.StyleId;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.log.Log;
import com.alee.utils.ReflectUtils;
import com.alee.utils.laf.Styleable;

import javax.swing.*;

/**
 * This JSeparator extension class provides a direct access to WebSeparatorUI methods.
 *
 * @author Mikle Garin
 */

public class WebSeparator extends JSeparator implements Styleable
{
    /**
     * Constructs new separator.
     */
    public WebSeparator ()
    {
        super ();
    }

    /**
     * Constructs new separator with the specified orientation.
     *
     * @param orientation component orientation
     */
    public WebSeparator ( final int orientation )
    {
        super ( orientation );
    }

    /**
     * Constructs new separator.
     *
     * @param id style ID
     */
    public WebSeparator ( final StyleId id )
    {
        super ();
    }

    /**
     * Constructs new separator with the specified orientation.
     *
     * @param orientation component orientation
     * @param id          style ID
     */
    public WebSeparator ( final StyleId id, final int orientation )
    {
        super ( orientation );
    }

    /**
     * Returns separator painter.
     *
     * @return separator painter
     */
    public Painter getPainter ()
    {
        return getWebUI ().getPainter ();
    }

    /**
     * Sets separator painter.
     * Pass null to remove separator painter.
     *
     * @param painter new separator painter
     * @return this separator
     */
    public WebSeparator setPainter ( final Painter painter )
    {
        getWebUI ().setPainter ( painter );
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StyleId getStyleId ()
    {
        return getWebUI ().getStyleId ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStyleId ( final StyleId id )
    {
        getWebUI ().setStyleId ( id );
    }

    /**
     * Returns Web-UI applied to this class.
     *
     * @return Web-UI applied to this class
     */
    private WebSeparatorUI getWebUI ()
    {
        return ( WebSeparatorUI ) getUI ();
    }

    /**
     * Installs a Web-UI into this component.
     */
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

    /**
     * Returns newly created horizontal separator.
     *
     * @return newly created horizontal separator
     */
    public static WebSeparator createHorizontal ()
    {
        return new WebSeparator ( WebSeparator.HORIZONTAL );
    }

    /**
     * Returns newly created vertical separator.
     *
     * @return newly created vertical separator
     */
    public static WebSeparator createVertical ()
    {
        return new WebSeparator ( WebSeparator.VERTICAL );
    }
}