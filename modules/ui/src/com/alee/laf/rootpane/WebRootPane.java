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

package com.alee.laf.rootpane;

import com.alee.extended.painter.Painter;
import com.alee.laf.StyleId;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.log.Log;
import com.alee.utils.ReflectUtils;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.laf.Styleable;

import javax.swing.*;
import java.awt.*;

/**
 * This JRootPane extension class provides a direct access to WebRootPaneUI methods.
 *
 * @author Mikle Garin
 */

public class WebRootPane extends JRootPane implements Styleable, ShapeProvider
{
    /**
     * Constructs new root pane.
     */
    public WebRootPane ()
    {
        super ();
    }

    /**
     * Constructs new root pane with the specified style ID.
     *
     * @param id style ID
     */
    public WebRootPane ( final StyleId id )
    {
        super ();
        setStyleId ( id );
    }

    /**
     * Returns root pane painter.
     *
     * @return root pane painter
     */
    public Painter getPainter ()
    {
        return getWebUI ().getPainter ();
    }

    /**
     * Sets root pane painter.
     * Pass null to remove root pane painter.
     *
     * @param painter new root pane painter
     * @return this root pane
     */
    public WebRootPane setPainter ( final Painter painter )
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
     * {@inheritDoc}
     */
    @Override
    public Shape provideShape ()
    {
        return getWebUI ().provideShape ();
    }

    /**
     * Returns Web-UI applied to this class.
     *
     * @return Web-UI applied to this class
     */
    private WebRootPaneUI getWebUI ()
    {
        return ( WebRootPaneUI ) getUI ();
    }

    /**
     * Installs a Web-UI into this component.
     */
    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebRootPaneUI ) )
        {
            try
            {
                setUI ( ( WebRootPaneUI ) ReflectUtils.createInstance ( WebLookAndFeel.rootPaneUI ) );
            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebRootPaneUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }
}