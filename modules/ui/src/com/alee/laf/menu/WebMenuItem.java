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

import com.alee.extended.painter.Painter;
import com.alee.laf.StyleId;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.hotkey.HotkeyData;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.LanguageMethods;
import com.alee.managers.language.updaters.LanguageUpdater;
import com.alee.managers.log.Log;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.laf.Styleable;
import com.alee.utils.swing.FontMethods;

import javax.swing.*;
import java.awt.*;

/**
 * This JMenuItem extension class provides a direct access to WebMenuItemUI methods.
 *
 * @author Mikle Garin
 */

public class WebMenuItem extends JMenuItem implements Styleable, ShapeProvider, LanguageMethods, FontMethods<WebMenuItem>
{
    /**
     * Constructs new menu item.
     */
    public WebMenuItem ()
    {
        super ();
    }

    /**
     * Constructs new menu item using the specified settings.
     *
     * @param icon menu item icon
     */
    public WebMenuItem ( final Icon icon )
    {
        super ( icon );
    }

    /**
     * Constructs new menu item using the specified settings.
     *
     * @param text menu item text
     */
    public WebMenuItem ( final String text )
    {
        super ( text );
    }

    /**
     * Constructs new menu item using the specified settings.
     *
     * @param text        menu item text
     * @param accelerator menu item accelerator
     */
    public WebMenuItem ( final String text, final KeyStroke accelerator )
    {
        super ( text );
        setAccelerator ( accelerator );
    }

    /**
     * Constructs new menu item using the specified settings.
     *
     * @param text        menu item text
     * @param accelerator menu item accelerator
     */
    public WebMenuItem ( final String text, final HotkeyData accelerator )
    {
        super ( text );
        setAccelerator ( accelerator );
    }

    /**
     * Constructs new menu item using the specified settings.
     *
     * @param a menu item action
     */
    public WebMenuItem ( final Action a )
    {
        super ( a );
    }

    /**
     * Constructs new menu item using the specified settings.
     *
     * @param text menu item text
     * @param icon menu item icon
     */
    public WebMenuItem ( final String text, final Icon icon )
    {
        super ( text, icon );
    }

    /**
     * Constructs new menu item using the specified settings.
     *
     * @param text     menu item text
     * @param mnemonic menu item mnemonic
     */
    public WebMenuItem ( final String text, final int mnemonic )
    {
        super ( text, mnemonic );
    }

    /**
     * Constructs new menu item using the specified settings.
     *
     * @param text        menu item text
     * @param icon        menu item icon
     * @param accelerator menu item accelerator
     */
    public WebMenuItem ( final String text, final Icon icon, final KeyStroke accelerator )
    {
        super ( text, icon );
        setAccelerator ( accelerator );
    }

    /**
     * Constructs new menu item using the specified settings.
     *
     * @param text        menu item text
     * @param icon        menu item icon
     * @param accelerator menu item accelerator
     */
    public WebMenuItem ( final String text, final Icon icon, final HotkeyData accelerator )
    {
        super ( text, icon );
        setAccelerator ( accelerator );
    }

    /**
     * Constructs new menu item.
     *
     * @param id style ID
     */
    public WebMenuItem ( final StyleId id )
    {
        super ();
    }

    /**
     * Constructs new menu item using the specified settings.
     *
     * @param id   style ID
     * @param icon menu item icon
     */
    public WebMenuItem ( final StyleId id, final Icon icon )
    {
        super ( icon );
    }

    /**
     * Constructs new menu item using the specified settings.
     *
     * @param id   style ID
     * @param text menu item text
     */
    public WebMenuItem ( final StyleId id, final String text )
    {
        super ( text );
    }

    /**
     * Constructs new menu item using the specified settings.
     *
     * @param id          style ID
     * @param text        menu item text
     * @param accelerator menu item accelerator
     */
    public WebMenuItem ( final StyleId id, final String text, final KeyStroke accelerator )
    {
        super ( text );
        setAccelerator ( accelerator );
    }

    /**
     * Constructs new menu item using the specified settings.
     *
     * @param id          style ID
     * @param text        menu item text
     * @param accelerator menu item accelerator
     */
    public WebMenuItem ( final StyleId id, final String text, final HotkeyData accelerator )
    {
        super ( text );
        setAccelerator ( accelerator );
    }

    /**
     * Constructs new menu item using the specified settings.
     *
     * @param id style ID
     * @param a  menu item action
     */
    public WebMenuItem ( final StyleId id, final Action a )
    {
        super ( a );
    }

    /**
     * Constructs new menu item using the specified settings.
     *
     * @param id   style ID
     * @param text menu item text
     * @param icon menu item icon
     */
    public WebMenuItem ( final StyleId id, final String text, final Icon icon )
    {
        super ( text, icon );
    }

    /**
     * Constructs new menu item using the specified settings.
     *
     * @param id       style ID
     * @param text     menu item text
     * @param mnemonic menu item mnemonic
     */
    public WebMenuItem ( final StyleId id, final String text, final int mnemonic )
    {
        super ( text, mnemonic );
    }

    /**
     * Constructs new menu item using the specified settings.
     *
     * @param id          style ID
     * @param text        menu item text
     * @param icon        menu item icon
     * @param accelerator menu item accelerator
     */
    public WebMenuItem ( final StyleId id, final String text, final Icon icon, final KeyStroke accelerator )
    {
        super ( text, icon );
        setAccelerator ( accelerator );
    }

    /**
     * Constructs new menu item using the specified settings.
     *
     * @param id          style ID
     * @param text        menu item text
     * @param icon        menu item icon
     * @param accelerator menu item accelerator
     */
    public WebMenuItem ( final StyleId id, final String text, final Icon icon, final HotkeyData accelerator )
    {
        super ( text, icon );
        setAccelerator ( accelerator );
    }

    /**
     * Sets the key combination which invokes the menu item's action listeners without navigating the menu hierarchy.
     *
     * @param hotkey hotkey data
     */
    public void setAccelerator ( final HotkeyData hotkey )
    {
        SwingUtils.setAccelerator ( this, hotkey );
    }

    /**
     * Returns menu item painter.
     *
     * @return menu item painter
     */
    public Painter getPainter ()
    {
        return getWebUI ().getPainter ();
    }

    /**
     * Sets menu item painter.
     * Pass null to remove menu item painter.
     *
     * @param painter new menu item painter
     * @return this menu item
     */
    public WebMenuItem setPainter ( final Painter painter )
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
    private WebMenuItemUI getWebUI ()
    {
        return ( WebMenuItemUI ) getUI ();
    }

    /**
     * Installs a Web-UI into this component.
     */
    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebMenuItemUI ) )
        {
            try
            {
                setUI ( ( WebMenuItemUI ) ReflectUtils.createInstance ( WebLookAndFeel.menuItemUI ) );
            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebMenuItemUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLanguage ( final String key, final Object... data )
    {
        LanguageManager.registerComponent ( this, key, data );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateLanguage ( final Object... data )
    {
        LanguageManager.updateComponent ( this, data );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateLanguage ( final String key, final Object... data )
    {
        LanguageManager.updateComponent ( this, key, data );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeLanguage ()
    {
        LanguageManager.unregisterComponent ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLanguageSet ()
    {
        return LanguageManager.isRegisteredComponent ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLanguageUpdater ( final LanguageUpdater updater )
    {
        LanguageManager.registerLanguageUpdater ( this, updater );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeLanguageUpdater ()
    {
        LanguageManager.unregisterLanguageUpdater ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebMenuItem setPlainFont ()
    {
        return SwingUtils.setPlainFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebMenuItem setPlainFont ( final boolean apply )
    {
        return SwingUtils.setPlainFont ( this, apply );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPlainFont ()
    {
        return SwingUtils.isPlainFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebMenuItem setBoldFont ()
    {
        return SwingUtils.setBoldFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebMenuItem setBoldFont ( final boolean apply )
    {
        return SwingUtils.setBoldFont ( this, apply );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBoldFont ()
    {
        return SwingUtils.isBoldFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebMenuItem setItalicFont ()
    {
        return SwingUtils.setItalicFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebMenuItem setItalicFont ( final boolean apply )
    {
        return SwingUtils.setItalicFont ( this, apply );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isItalicFont ()
    {
        return SwingUtils.isItalicFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebMenuItem setFontStyle ( final boolean bold, final boolean italic )
    {
        return SwingUtils.setFontStyle ( this, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebMenuItem setFontStyle ( final int style )
    {
        return SwingUtils.setFontStyle ( this, style );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebMenuItem setFontSize ( final int fontSize )
    {
        return SwingUtils.setFontSize ( this, fontSize );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebMenuItem changeFontSize ( final int change )
    {
        return SwingUtils.changeFontSize ( this, change );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getFontSize ()
    {
        return SwingUtils.getFontSize ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebMenuItem setFontSizeAndStyle ( final int fontSize, final boolean bold, final boolean italic )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebMenuItem setFontSizeAndStyle ( final int fontSize, final int style )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, style );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebMenuItem setFontName ( final String fontName )
    {
        return SwingUtils.setFontName ( this, fontName );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFontName ()
    {
        return SwingUtils.getFontName ( this );
    }
}