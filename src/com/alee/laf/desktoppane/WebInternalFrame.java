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

import com.alee.laf.WebLookAndFeel;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.LanguageMethods;
import com.alee.managers.language.updaters.LanguageUpdater;
import com.alee.utils.ReflectUtils;

import javax.swing.*;
import java.beans.PropertyVetoException;

/**
 * User: mgarin Date: 24.08.11 Time: 18:11
 */

public class WebInternalFrame extends JInternalFrame implements LanguageMethods
{
    public WebInternalFrame ()
    {
        super ();
    }

    public WebInternalFrame ( String title )
    {
        super ( title );
    }

    public WebInternalFrame ( String title, boolean resizable )
    {
        super ( title, resizable );
    }

    public WebInternalFrame ( String title, boolean resizable, boolean closable )
    {
        super ( title, resizable, closable );
    }

    public WebInternalFrame ( String title, boolean resizable, boolean closable, boolean maximizable )
    {
        super ( title, resizable, closable, maximizable );
    }

    public WebInternalFrame ( String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable )
    {
        super ( title, resizable, closable, maximizable, iconifiable );
    }

    public WebInternalFrameUI getWebUI ()
    {
        return ( WebInternalFrameUI ) getUI ();
    }

    @Override
    public void setIcon ( boolean b )
    {
        try
        {
            super.setIcon ( b );
        }
        catch ( PropertyVetoException e )
        {
            e.printStackTrace ();
        }
    }

    public void close ()
    {
        try
        {
            setClosed ( true );
        }
        catch ( PropertyVetoException e )
        {
            e.printStackTrace ();
        }
    }

    public void open ()
    {
        try
        {
            setClosed ( false );
            setVisible ( true );
        }
        catch ( PropertyVetoException e )
        {
            e.printStackTrace ();
        }
    }

    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebInternalFrameUI ) )
        {
            try
            {
                setUI ( ( WebInternalFrameUI ) ReflectUtils.createInstance ( WebLookAndFeel.internalFrameUI, this ) );
            }
            catch ( Throwable e )
            {
                e.printStackTrace ();
                setUI ( new WebInternalFrameUI ( this ) );
            }
        }
        else
        {
            setUI ( getUI () );
        }
        invalidate ();
    }

    /**
     * Language methods
     */

    @Override
    public void setLanguage ( String key, Object... data )
    {
        LanguageManager.registerComponent ( this, key, data );
    }

    @Override
    public void updateLanguage ( Object... data )
    {
        LanguageManager.updateComponent ( this, data );
    }

    @Override
    public void updateLanguage ( String key, Object... data )
    {
        LanguageManager.updateComponent ( this, key, data );
    }

    @Override
    public void removeLanguage ()
    {
        LanguageManager.unregisterComponent ( this );
    }

    @Override
    public boolean isLanguageSet ()
    {
        return LanguageManager.isRegisteredComponent ( this );
    }

    @Override
    public void setLanguageUpdater ( LanguageUpdater updater )
    {
        LanguageManager.registerLanguageUpdater ( this, updater );
    }

    @Override
    public void removeLanguageUpdater ()
    {
        LanguageManager.unregisterLanguageUpdater ( this );
    }
}