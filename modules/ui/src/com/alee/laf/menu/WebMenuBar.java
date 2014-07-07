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

package com.alee.laf.menu;

import com.alee.laf.WebLookAndFeel;
import com.alee.managers.log.Log;
import com.alee.utils.ReflectUtils;
import com.alee.utils.laf.ShapeProvider;

import javax.swing.*;
import java.awt.*;

/**
 * @author Mikle Garin
 */

public class WebMenuBar extends JMenuBar implements ShapeProvider
{
    public WebMenuBar ()
    {
        super ();
    }

    public WebMenuBar ( final MenuBarStyle menuBarStyle )
    {
        super ();
        setMenuBarStyle ( menuBarStyle );
    }

    public boolean isUndecorated ()
    {
        return getWebUI ().isUndecorated ();
    }

    public void setUndecorated ( final boolean undecorated )
    {
        getWebUI ().setUndecorated ( undecorated );
    }

    public MenuBarStyle getMenuBarStyle ()
    {
        return getWebUI ().getMenuBarStyle ();
    }

    public void setMenuBarStyle ( final MenuBarStyle menuBarStyle )
    {
        getWebUI ().setMenuBarStyle ( menuBarStyle );
    }

    public Color getBorderColor ()
    {
        return getWebUI ().getBorderColor ();
    }

    public void setBorderColor ( final Color borderColor )
    {
        getWebUI ().setBorderColor ( borderColor );
    }

    public int getRound ()
    {
        return getWebUI ().getRound ();
    }

    public void setRound ( final int round )
    {
        getWebUI ().setRound ( round );
    }

    public int getShadeWidth ()
    {
        return getWebUI ().getShadeWidth ();
    }

    public void setShadeWidth ( final int shadeWidth )
    {
        getWebUI ().setShadeWidth ( shadeWidth );
    }

    @Override
    public Shape provideShape ()
    {
        return getWebUI ().provideShape ();
    }

    public WebMenuBarUI getWebUI ()
    {
        return ( WebMenuBarUI ) getUI ();
    }

    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebMenuBarUI ) )
        {
            try
            {
                setUI ( ( WebMenuBarUI ) ReflectUtils.createInstance ( WebLookAndFeel.menuBarUI ) );
            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebMenuBarUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }
}