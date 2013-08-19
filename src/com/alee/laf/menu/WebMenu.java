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
import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.LanguageMethods;
import com.alee.managers.language.updaters.LanguageUpdater;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.FontMethods;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 15.08.11 Time: 19:47
 */

public class WebMenu extends JMenu implements LanguageMethods, FontMethods<WebMenu>
{
    private Point customMenuLocation = null;

    public WebMenu ()
    {
        super ();
    }

    public WebMenu ( String s )
    {
        super ( s );
    }

    public WebMenu ( Action a )
    {
        super ( a );
    }

    public WebMenu ( String s, boolean b )
    {
        super ( s, b );
    }

    public WebMenu ( String s, Icon icon )
    {
        super ( s );
        setIcon ( icon );
    }

    public void setMenuLocation ( int x, int y )
    {
        customMenuLocation = new Point ( x, y );
        if ( getPopupMenu ().isVisible () )
        {
            getPopupMenu ().setLocation ( x, y );
        }
    }

    public Point getCustomMenuLocation ()
    {
        return customMenuLocation;
    }

    public void setPopupMenuVisible ( boolean b )
    {
        boolean isVisible = isPopupMenuVisible ();
        if ( b != isVisible && ( isEnabled () || !b ) )
        {
            if ( b && isShowing () )
            {
                Point p = getCustomMenuLocation ();
                if ( p == null )
                {
                    p = getPopupMenuOrigin ();
                }
                getPopupMenu ().show ( this, p.x, p.y );
                //                getPopupMenu ().show ( this, WebMenu.this.getWidth (), 0 );
            }
            else
            {
                getPopupMenu ().setVisible ( false );
            }
        }
    }

    public WebMenuUI getWebUI ()
    {
        return ( WebMenuUI ) getUI ();
    }

    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebMenuUI ) )
        {
            try
            {
                setUI ( ( WebMenuUI ) ReflectUtils.createInstance ( WebLookAndFeel.menuUI ) );
            }
            catch ( Throwable e )
            {
                e.printStackTrace ();
                setUI ( new WebMenuUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
        if ( getPopupMenu () != null )
        {
            getPopupMenu ().updateUI ();
        }
    }

    /**
     * Language methods
     */

    /**
     * {@inheritDoc}
     */
    public void setLanguage ( String key, Object... data )
    {
        LanguageManager.registerComponent ( this, key, data );
    }

    /**
     * {@inheritDoc}
     */
    public void updateLanguage ( Object... data )
    {
        LanguageManager.updateComponent ( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public void updateLanguage ( String key, Object... data )
    {
        LanguageManager.updateComponent ( this, key, data );
    }

    /**
     * {@inheritDoc}
     */
    public void removeLanguage ()
    {
        LanguageManager.unregisterComponent ( this );
    }

    /**
     * {@inheritDoc}
     */
    public boolean isLanguageSet ()
    {
        return LanguageManager.isRegisteredComponent ( this );
    }

    /**
     * {@inheritDoc}
     */
    public void setLanguageUpdater ( LanguageUpdater updater )
    {
        LanguageManager.registerLanguageUpdater ( this, updater );
    }

    /**
     * {@inheritDoc}
     */
    public void removeLanguageUpdater ()
    {
        LanguageManager.unregisterLanguageUpdater ( this );
    }

    /**
     * Font methods
     */

    /**
     * {@inheritDoc}
     */
    public WebMenu setPlainFont ()
    {
        return SwingUtils.setPlainFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public WebMenu setPlainFont ( boolean apply )
    {
        return SwingUtils.setPlainFont ( this, apply );
    }

    /**
     * {@inheritDoc}
     */
    public boolean isPlainFont ()
    {
        return SwingUtils.isPlainFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public WebMenu setBoldFont ()
    {
        return SwingUtils.setBoldFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public WebMenu setBoldFont ( boolean apply )
    {
        return SwingUtils.setBoldFont ( this, apply );
    }

    /**
     * {@inheritDoc}
     */
    public boolean isBoldFont ()
    {
        return SwingUtils.isBoldFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public WebMenu setItalicFont ()
    {
        return SwingUtils.setItalicFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public WebMenu setItalicFont ( boolean apply )
    {
        return SwingUtils.setItalicFont ( this, apply );
    }

    /**
     * {@inheritDoc}
     */
    public boolean isItalicFont ()
    {
        return SwingUtils.isItalicFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public WebMenu setFontStyle ( boolean bold, boolean italic )
    {
        return SwingUtils.setFontStyle ( this, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    public WebMenu setFontStyle ( int style )
    {
        return SwingUtils.setFontStyle ( this, style );
    }

    /**
     * {@inheritDoc}
     */
    public WebMenu setFontSize ( int fontSize )
    {
        return SwingUtils.setFontSize ( this, fontSize );
    }

    /**
     * {@inheritDoc}
     */
    public WebMenu changeFontSize ( int change )
    {
        return SwingUtils.changeFontSize ( this, change );
    }

    /**
     * {@inheritDoc}
     */
    public int getFontSize ()
    {
        return SwingUtils.getFontSize ( this );
    }

    /**
     * {@inheritDoc}
     */
    public WebMenu setFontSizeAndStyle ( int fontSize, boolean bold, boolean italic )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    public WebMenu setFontSizeAndStyle ( int fontSize, int style )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, style );
    }

    /**
     * {@inheritDoc}
     */
    public WebMenu setFontName ( String fontName )
    {
        return SwingUtils.setFontName ( this, fontName );
    }

    /**
     * {@inheritDoc}
     */
    public String getFontName ()
    {
        return SwingUtils.getFontName ( this );
    }
}