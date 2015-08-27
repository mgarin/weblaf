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
import com.alee.managers.style.StyleId;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.log.Log;
import com.alee.utils.ReflectUtils;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.laf.Styleable;

import javax.swing.*;
import java.awt.*;

/**
 * This JOptionPane extension class provides a direct access to WebOptionPaneUI methods.
 *
 * @author Mikle Garin
 */

public class WebOptionPane extends JOptionPane implements Styleable, ShapeProvider
{
    /**
     * Constructs new option pane.
     */
    public WebOptionPane ()
    {
        super ();
    }

    /**
     * Constructs new option pane.
     *
     * @param message message
     */
    public WebOptionPane ( final Object message )
    {
        super ( message );
    }

    /**
     * Constructs new option pane.
     *
     * @param message     message
     * @param messageType message type
     */
    public WebOptionPane ( final Object message, final int messageType )
    {
        super ( message, messageType );
    }

    /**
     * Constructs new option pane.
     *
     * @param message     message
     * @param messageType message type
     * @param optionType  option pane type
     */
    public WebOptionPane ( final Object message, final int messageType, final int optionType )
    {
        super ( message, messageType, optionType );
    }

    /**
     * Constructs new option pane.
     *
     * @param message     message
     * @param messageType message type
     * @param optionType  option pane type
     * @param icon        option pane icon
     */
    public WebOptionPane ( final Object message, final int messageType, final int optionType, final Icon icon )
    {
        super ( message, messageType, optionType, icon );
    }

    /**
     * Constructs new option pane.
     *
     * @param message     message
     * @param messageType message type
     * @param optionType  option pane type
     * @param icon        option pane icon
     * @param options     available options
     */
    public WebOptionPane ( final Object message, final int messageType, final int optionType, final Icon icon, final Object[] options )
    {
        super ( message, messageType, optionType, icon, options );
    }

    /**
     * Constructs new option pane.
     *
     * @param message      message
     * @param messageType  message type
     * @param optionType   option pane type
     * @param icon         option pane icon
     * @param options      available options
     * @param initialValue initial value
     */
    public WebOptionPane ( final Object message, final int messageType, final int optionType, final Icon icon, final Object[] options,
                           final Object initialValue )
    {
        super ( message, messageType, optionType, icon, options, initialValue );
    }

    /**
     * Constructs new option pane.
     *
     * @param id style ID
     */
    public WebOptionPane ( final StyleId id )
    {
        super ();
        setStyleId ( id );
    }

    /**
     * Constructs new option pane.
     *
     * @param id      style ID
     * @param message message
     */
    public WebOptionPane ( final StyleId id, final Object message )
    {
        super ( message );
        setStyleId ( id );
    }

    /**
     * Constructs new option pane.
     *
     * @param id          style ID
     * @param message     message
     * @param messageType message type
     */
    public WebOptionPane ( final StyleId id, final Object message, final int messageType )
    {
        super ( message, messageType );
        setStyleId ( id );
    }

    /**
     * Constructs new option pane.
     *
     * @param id          style ID
     * @param message     message
     * @param messageType message type
     * @param optionType  option pane type
     */
    public WebOptionPane ( final StyleId id, final Object message, final int messageType, final int optionType )
    {
        super ( message, messageType, optionType );
        setStyleId ( id );
    }

    /**
     * Constructs new option pane.
     *
     * @param id          style ID
     * @param message     message
     * @param messageType message type
     * @param optionType  option pane type
     * @param icon        option pane icon
     */
    public WebOptionPane ( final StyleId id, final Object message, final int messageType, final int optionType, final Icon icon )
    {
        super ( message, messageType, optionType, icon );
        setStyleId ( id );
    }

    /**
     * Constructs new option pane.
     *
     * @param id          style ID
     * @param message     message
     * @param messageType message type
     * @param optionType  option pane type
     * @param icon        option pane icon
     * @param options     available options
     */
    public WebOptionPane ( final StyleId id, final Object message, final int messageType, final int optionType, final Icon icon,
                           final Object[] options )
    {
        super ( message, messageType, optionType, icon, options );
        setStyleId ( id );
    }

    /**
     * Constructs new option pane.
     *
     * @param id           style ID
     * @param message      message
     * @param messageType  message type
     * @param optionType   option pane type
     * @param icon         option pane icon
     * @param options      available options
     * @param initialValue initial value
     */
    public WebOptionPane ( final StyleId id, final Object message, final int messageType, final int optionType, final Icon icon,
                           final Object[] options, final Object initialValue )
    {
        super ( message, messageType, optionType, icon, options, initialValue );
        setStyleId ( id );
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