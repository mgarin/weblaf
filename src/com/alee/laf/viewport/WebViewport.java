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

import com.alee.laf.WebLookAndFeel;
import com.alee.utils.ReflectUtils;

import javax.swing.*;

/**
 * User: mgarin Date: 01.11.11 Time: 13:26
 */

public class WebViewport extends JViewport
{
    public WebViewport ()
    {
        super ();
    }

    public WebViewportUI getWebUI ()
    {
        return ( WebViewportUI ) getUI ();
    }

    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebViewportUI ) )
        {
            try
            {
                setUI ( ( WebViewportUI ) ReflectUtils.createInstance ( WebLookAndFeel.viewportUI ) );
            }
            catch ( Throwable e )
            {
                e.printStackTrace ();
                setUI ( new WebViewportUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }
}
