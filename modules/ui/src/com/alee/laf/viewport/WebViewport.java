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

package com.alee.laf.viewport;

import com.alee.extended.painter.Painter;
import com.alee.managers.style.StyleId;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.log.Log;
import com.alee.utils.ReflectUtils;
import com.alee.utils.laf.Styleable;

import javax.swing.*;

/**
 * This JViewport extension class provides a direct access to WebViewportUI methods.
 *
 * @author Mikle Garin
 */

public class WebViewport extends JViewport implements Styleable
{
    /**
     * Constructs new viewport component.
     */
    public WebViewport ()
    {
        super ();
    }

    /**
     * Constructs new viewport component.
     *
     * @param id style ID
     */
    public WebViewport ( final StyleId id )
    {
        super ();
        setStyleId ( id );
    }

    /**
     * Returns viewport painter.
     *
     * @return viewport painter
     */
    public Painter getPainter ()
    {
        return getWebUI ().getPainter ();
    }

    /**
     * Sets viewport painter.
     * Pass null to remove viewport painter.
     *
     * @param painter new viewport painter
     * @return this viewport
     */
    public WebViewport setPainter ( final Painter painter )
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
     * Installs a Web-UI into this component.
     */
    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebViewportUI ) )
        {
            try
            {
                setUI ( ( WebViewportUI ) ReflectUtils.createInstance ( WebLookAndFeel.viewportUI ) );
            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebViewportUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }

    /**
     * Returns Web-UI applied to this class.
     *
     * @return Web-UI applied to this class
     */
    private WebViewportUI getWebUI ()
    {
        return ( WebViewportUI ) getUI ();
    }
}