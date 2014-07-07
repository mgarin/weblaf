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
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.LanguageMethods;
import com.alee.managers.language.updaters.LanguageUpdater;
import com.alee.managers.log.Log;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.FontMethods;

import javax.swing.*;
import java.awt.*;

/**
 * This JMenu extension class provides a direct access to WebMenuUI methods.
 *
 * @author Mikle Garin
 */

public class WebMenu extends JMenu implements LanguageMethods, FontMethods<WebMenu>
{
    /**
     * Constructs new menu.
     */
    public WebMenu ()
    {
        super ();
    }

    /**
     * Constructs new menu using the specified settings.
     *
     * @param icon menu icon
     */
    public WebMenu ( final Icon icon )
    {
        super ();
        setIcon ( icon );
    }

    /**
     * Constructs new menu using the specified settings.
     *
     * @param s menu text
     */
    public WebMenu ( final String s )
    {
        super ( s );
    }

    /**
     * Constructs new menu using the specified settings.
     *
     * @param a menu action
     */
    public WebMenu ( final Action a )
    {
        super ( a );
    }

    /**
     * Constructs new menu using the specified settings.
     *
     * @param s    menu text
     * @param icon menu item icon
     */
    public WebMenu ( final String s, final Icon icon )
    {
        super ( s );
        setIcon ( icon );
    }

    /**
     * Returns top-level menu border rounding.
     *
     * @return top-level menu border rounding
     */
    public int getRound ()
    {
        return getWebUI ().getRound ();
    }

    /**
     * Sets top-level menu border rounding.
     *
     * @param round new top-level menu border rounding
     */
    public void setRound ( final int round )
    {
        getWebUI ().setRound ( round );
    }

    /**
     * Returns top-level menu shade width.
     *
     * @return top-level menu shade width
     */
    public int getShadeWidth ()
    {
        return getWebUI ().getShadeWidth ();
    }

    /**
     * Sets top-level menu shade width.
     *
     * @param shadeWidth new top-level menu shade width
     */
    public void setShadeWidth ( final int shadeWidth )
    {
        getWebUI ().setShadeWidth ( shadeWidth );
    }

    /**
     * Returns menu item margin.
     *
     * @return menu item margin
     */
    @Override
    public Insets getMargin ()
    {
        return getWebUI ().getMargin ();
    }

    /**
     * Sets menu item margin.
     *
     * @param margin new menu item margin
     */
    @Override
    public void setMargin ( final Insets margin )
    {
        getWebUI ().setMargin ( margin );
    }

    /**
     * Returns spacing between menu item content and its left/right borders.
     *
     * @return spacing between menu item content and its left/right borders
     */
    public int getSideSpacing ()
    {
        return getWebUI ().getSideSpacing ();
    }

    /**
     * Sets spacing between menu item content and its left/right borders
     *
     * @param sideSpacing spacing between menu item content and its left/right borders
     */
    public void setSideSpacing ( final int sideSpacing )
    {
        getWebUI ().setSideSpacing ( sideSpacing );
    }

    /**
     * Returns disabled menu item foreground.
     *
     * @return disabled menu item foreground
     */
    public Color getDisabledFg ()
    {
        return getWebUI ().getDisabledFg ();
    }

    /**
     * Sets disabled menu item foreground.
     *
     * @param foreground new disabled menu item foreground
     */
    public void setDisabledFg ( final Color foreground )
    {
        getWebUI ().setDisabledFg ( foreground );
    }

    /**
     * Returns top background color for selected item.
     *
     * @return top background color for selected item
     */
    public Color getSelectedTopBg ()
    {
        return getWebUI ().getSelectedTopBg ();
    }

    /**
     * Sets top background color for selected item.
     *
     * @param background new top background color for selected item
     */
    public void setSelectedTopBg ( final Color background )
    {
        getWebUI ().setSelectedTopBg ( background );
    }

    /**
     * Returns bottom background color for selected item.
     *
     * @return bottom background color for selected item
     */
    public Color getSelectedBottomBg ()
    {
        return getWebUI ().getSelectedBottomBg ();
    }

    /**
     * Sets bottom background color for selected item.
     *
     * @param background new bottom background color for selected item
     */
    public void setSelectedBottomBg ( final Color background )
    {
        getWebUI ().setSelectedBottomBg ( background );
    }

    /**
     * Returns gap between menu icon/text and submenu arrow.
     *
     * @return gap between menu icon/text and submenu arrow
     */
    public int getArrowGap ()
    {
        return getWebUI ().getArrowGap ();
    }

    /**
     * Sets gap between menu icon/text and submenu arrow.
     *
     * @param gap new gap between menu icon/text and submenu arrow
     */
    public void setArrowGap ( final int gap )
    {
        getWebUI ().setArrowGap ( gap );
    }

    /**
     * Returns whether should align all item texts to a single vertical line within single popup menu or not.
     *
     * @return true if should align all item texts to a single vertical line within single popup menu, false otherwise
     */
    public boolean isAlignTextToMenuIcons ()
    {
        return getWebUI ().isAlignTextToMenuIcons ();
    }

    /**
     * Sets whether should align all item texts to a single vertical line within single popup menu or not.
     *
     * @param align whether should align all item texts to a single vertical line within single popup menu or not
     */
    public void setAlignTextToMenuIcons ( final boolean align )
    {
        getWebUI ().setAlignTextToMenuIcons ( align );
    }

    /**
     * Returns icon alignment.
     *
     * @return icon alignment
     */
    public int getIconAlignment ()
    {
        return getWebUI ().getIconAlignment ();
    }

    /**
     * Sets icon alignment
     *
     * @param alignment new icon alignment
     */
    public void setIconAlignment ( final int alignment )
    {
        getWebUI ().setIconAlignment ( alignment );
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
     *
     * @param painter new menu item painter
     */
    public void setPainter ( final Painter painter )
    {
        getWebUI ().setPainter ( painter );
    }

    /**
     * Returns Web-UI applied to this class.
     *
     * @return Web-UI applied to this class
     */
    public WebMenuUI getWebUI ()
    {
        return ( WebMenuUI ) getUI ();
    }

    /**
     * Installs a Web-UI into this component.
     */
    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebMenuUI ) )
        {
            try
            {
                setUI ( ( WebMenuUI ) ReflectUtils.createInstance ( WebLookAndFeel.menuUI ) );
            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
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
    public WebMenu setPlainFont ()
    {
        return SwingUtils.setPlainFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebMenu setPlainFont ( final boolean apply )
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
    public WebMenu setBoldFont ()
    {
        return SwingUtils.setBoldFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebMenu setBoldFont ( final boolean apply )
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
    public WebMenu setItalicFont ()
    {
        return SwingUtils.setItalicFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebMenu setItalicFont ( final boolean apply )
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
    public WebMenu setFontStyle ( final boolean bold, final boolean italic )
    {
        return SwingUtils.setFontStyle ( this, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebMenu setFontStyle ( final int style )
    {
        return SwingUtils.setFontStyle ( this, style );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebMenu setFontSize ( final int fontSize )
    {
        return SwingUtils.setFontSize ( this, fontSize );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebMenu changeFontSize ( final int change )
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
    public WebMenu setFontSizeAndStyle ( final int fontSize, final boolean bold, final boolean italic )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebMenu setFontSizeAndStyle ( final int fontSize, final int style )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, style );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebMenu setFontName ( final String fontName )
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