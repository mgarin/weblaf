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

import com.alee.laf.WebLookAndFeel;
import com.alee.utils.ReflectUtils;

import javax.swing.*;

/**
 * User: mgarin Date: 28.08.11 Time: 22:01
 */

public class WebOptionPane extends JOptionPane
{
    public WebOptionPane ()
    {
        super ();
    }

    public WebOptionPane ( Object message )
    {
        super ( message );
    }

    public WebOptionPane ( Object message, int messageType )
    {
        super ( message, messageType );
    }

    public WebOptionPane ( Object message, int messageType, int optionType )
    {
        super ( message, messageType, optionType );
    }

    public WebOptionPane ( Object message, int messageType, int optionType, Icon icon )
    {
        super ( message, messageType, optionType, icon );
    }

    public WebOptionPane ( Object message, int messageType, int optionType, Icon icon, Object[] options )
    {
        super ( message, messageType, optionType, icon, options );
    }

    public WebOptionPane ( Object message, int messageType, int optionType, Icon icon, Object[] options, Object initialValue )
    {
        super ( message, messageType, optionType, icon, options, initialValue );
    }

    public WebOptionPaneUI getWebUI ()
    {
        return ( WebOptionPaneUI ) getUI ();
    }

    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebOptionPaneUI ) )
        {
            try
            {
                setUI ( ( WebOptionPaneUI ) ReflectUtils.createInstance ( WebLookAndFeel.optionPaneUI ) );
            }
            catch ( Throwable e )
            {
                e.printStackTrace ();
                setUI ( new WebOptionPaneUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }
}
