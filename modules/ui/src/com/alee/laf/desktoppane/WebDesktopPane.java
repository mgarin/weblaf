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

package com.alee.laf.desktoppane;

import com.alee.extended.painter.Painter;
import com.alee.laf.StyleId;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.log.Log;
import com.alee.utils.ReflectUtils;
import com.alee.utils.laf.Styleable;

import javax.swing.*;

/**
 * User: mgarin Date: 24.08.11 Time: 17:46
 */

public class WebDesktopPane extends JDesktopPane implements Styleable
{
    /**
     * Constructs new desktop pane component.
     */
    public WebDesktopPane ()
    {
        super ();
    }

    /**
     * Returns desktop pane painter.
     *
     * @return desktop pane painter
     */
    public Painter getPainter ()
    {
        return getWebUI ().getPainter ();
    }

    /**
     * Sets desktop pane painter.
     * Pass null to remove desktop pane painter.
     *
     * @param painter new desktop pane painter
     * @return this desktop pane
     */
    public WebDesktopPane setPainter ( final Painter painter )
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
        if ( getUI () == null || !( getUI () instanceof WebDesktopPaneUI ) )
        {
            try
            {
                setUI ( ( WebDesktopPaneUI ) ReflectUtils.createInstance ( WebLookAndFeel.desktopPaneUI ) );
            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebDesktopPaneUI () );
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
    private WebDesktopPaneUI getWebUI ()
    {
        return ( WebDesktopPaneUI ) getUI ();
    }
}
