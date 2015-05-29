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
import com.alee.managers.log.Log;
import com.alee.utils.ReflectUtils;

import javax.swing.*;
import java.beans.PropertyVetoException;

/**
 * This JInternalFrame extension class provides a direct access to WebInternalFrameUI methods.
 * There is also a set of additional methods to simplify some operations with internal frame.
 * <p/>
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application L&amp;F as this component will use Web-UI in any case.
 *
 * @author Mikle Garin
 */

public class WebInternalFrame extends JInternalFrame implements LanguageMethods
{
    public WebInternalFrame ()
    {
        super ();
    }

    public WebInternalFrame ( final String title )
    {
        super ( title );
    }

    public WebInternalFrame ( final String title, final boolean resizable )
    {
        super ( title, resizable );
    }

    public WebInternalFrame ( final String title, final boolean resizable, final boolean closable )
    {
        super ( title, resizable, closable );
    }

    public WebInternalFrame ( final String title, final boolean resizable, final boolean closable, final boolean maximizable )
    {
        super ( title, resizable, closable, maximizable );
    }

    public WebInternalFrame ( final String title, final boolean resizable, final boolean closable, final boolean maximizable,
                              final boolean iconifiable )
    {
        super ( title, resizable, closable, maximizable, iconifiable );
    }

    public WebInternalFrameUI getWebUI ()
    {
        return ( WebInternalFrameUI ) getUI ();
    }

    @Override
    public void setIcon ( final boolean b )
    {
        try
        {
            super.setIcon ( b );
        }
        catch ( final PropertyVetoException e )
        {
            Log.error ( this, e );
        }
    }

    public void close ()
    {
        try
        {
            setClosed ( true );
        }
        catch ( final PropertyVetoException e )
        {
            Log.error ( this, e );
        }
    }

    public void open ()
    {
        try
        {
            setClosed ( false );
            setVisible ( true );
        }
        catch ( final PropertyVetoException e )
        {
            Log.error ( this, e );
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
            catch ( final Throwable e )
            {
                Log.error ( this, e );
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
    public void setLanguage ( final String key, final Object... data )
    {
        LanguageManager.registerComponent ( this, key, data );
    }

    @Override
    public void updateLanguage ( final Object... data )
    {
        LanguageManager.updateComponent ( this, data );
    }

    @Override
    public void updateLanguage ( final String key, final Object... data )
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
    public void setLanguageUpdater ( final LanguageUpdater updater )
    {
        LanguageManager.registerLanguageUpdater ( this, updater );
    }

    @Override
    public void removeLanguageUpdater ()
    {
        LanguageManager.unregisterLanguageUpdater ( this );
    }
}