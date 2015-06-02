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

package com.alee.laf.optionpane;

import com.alee.extended.painter.Painter;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.log.Log;
import com.alee.utils.ReflectUtils;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.laf.Styleable;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 28.08.11 Time: 22:01
 */

public class WebOptionPane extends JOptionPane implements Styleable, ShapeProvider
{
    public WebOptionPane ()
    {
        super ();
    }

    public WebOptionPane ( final Object message )
    {
        super ( message );
    }

    public WebOptionPane ( final Object message, final int messageType )
    {
        super ( message, messageType );
    }

    public WebOptionPane ( final Object message, final int messageType, final int optionType )
    {
        super ( message, messageType, optionType );
    }

    public WebOptionPane ( final Object message, final int messageType, final int optionType, final Icon icon )
    {
        super ( message, messageType, optionType, icon );
    }

    public WebOptionPane ( final Object message, final int messageType, final int optionType, final Icon icon, final Object[] options )
    {
        super ( message, messageType, optionType, icon, options );
    }

    public WebOptionPane ( final Object message, final int messageType, final int optionType, final Icon icon, final Object[] options,
                           final Object initialValue )
    {
        super ( message, messageType, optionType, icon, options, initialValue );
    }

    /**
     * Returns text field painter.
     *
     * @return text field painter
     */
    public Painter getPainter ()
    {
        return getWebUI ().getPainter ();
    }

    /**
     * Sets text field painter.
     * Pass null to remove text field painter.
     *
     * @param painter new text field painter
     * @return this text field
     */
    public WebOptionPane setPainter ( final Painter painter )
    {
        getWebUI ().setPainter ( painter );
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStyleId ()
    {
        return getWebUI ().getStyleId ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStyleId ( final String id )
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
    private WebOptionPaneUI getWebUI ()
    {
        return ( WebOptionPaneUI ) getUI ();
    }

    /**
     * Installs a Web-UI into this component.
     */
    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebOptionPaneUI ) )
        {
            try
            {
                setUI ( ( WebOptionPaneUI ) ReflectUtils.createInstance ( WebLookAndFeel.optionPaneUI ) );
            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebOptionPaneUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }
}