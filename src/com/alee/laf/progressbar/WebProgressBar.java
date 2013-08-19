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

package com.alee.laf.progressbar;

import com.alee.laf.WebLookAndFeel;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.LanguageMethods;
import com.alee.managers.language.updaters.LanguageUpdater;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.swing.FontMethods;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 28.06.11 Time: 1:10
 */

public class WebProgressBar extends JProgressBar implements ShapeProvider, LanguageMethods, FontMethods<WebProgressBar>
{
    public WebProgressBar ()
    {
        super ();
        reinstallUI ();
    }

    public WebProgressBar ( int orient )
    {
        super ( orient );
        reinstallUI ();
    }

    public WebProgressBar ( int min, int max )
    {
        super ( min, max );
        reinstallUI ();
    }

    public WebProgressBar ( int orient, int min, int max )
    {
        super ( orient, min, max );
        reinstallUI ();
    }

    public WebProgressBar ( BoundedRangeModel newModel )
    {
        super ( newModel );
        reinstallUI ();
    }

    private void reinstallUI ()
    {
        // Fix for JProgressBar default constructors
        getUI ().installUI ( this );
    }

    public int getRound ()
    {
        return getWebUI ().getRound ();
    }

    public void setRound ( int round )
    {
        getWebUI ().setRound ( round );
    }

    public int getInnerRound ()
    {
        return getWebUI ().getInnerRound ();
    }

    public void setInnerRound ( int innerRound )
    {
        getWebUI ().setInnerRound ( innerRound );
    }

    public int getShadeWidth ()
    {
        return getWebUI ().getShadeWidth ();
    }

    public void setShadeWidth ( int shadeWidth )
    {
        getWebUI ().setShadeWidth ( shadeWidth );
    }

    public boolean isPaintIndeterminateBorder ()
    {
        return getWebUI ().isPaintIndeterminateBorder ();
    }

    public void setPaintIndeterminateBorder ( boolean paintIndeterminateBorder )
    {
        getWebUI ().setPaintIndeterminateBorder ( paintIndeterminateBorder );
    }

    public int getPreferredWidth ()
    {
        return getWebUI ().getPreferredWidth ();
    }

    public void setPreferredWidth ( int preferredWidth )
    {
        getWebUI ().setPreferredWidth ( preferredWidth );
    }

    public Color getBgTop ()
    {
        return getWebUI ().getBgTop ();
    }

    public void setBgTop ( Color bgTop )
    {
        getWebUI ().setBgTop ( bgTop );
    }

    public Color getBgBottom ()
    {
        return getWebUI ().getBgBottom ();
    }

    public void setBgBottom ( Color bgBottom )
    {
        getWebUI ().setBgBottom ( bgBottom );
    }

    public Color getProgressTopColor ()
    {
        return getWebUI ().getProgressTopColor ();
    }

    public void setProgressTopColor ( Color progressTopColor )
    {
        getWebUI ().setProgressTopColor ( progressTopColor );
    }

    public Color getProgressBottomColor ()
    {
        return getWebUI ().getProgressBottomColor ();
    }

    public void setProgressBottomColor ( Color progressBottomColor )
    {
        getWebUI ().setProgressBottomColor ( progressBottomColor );
    }

    public Color getIndeterminateBorder ()
    {
        return getWebUI ().getIndeterminateBorder ();
    }

    public void setIndeterminateBorder ( Color indeterminateBorder )
    {
        getWebUI ().setIndeterminateBorder ( indeterminateBorder );
    }

    public Color getHighlightWhite ()
    {
        return getWebUI ().getHighlightWhite ();
    }

    public void setHighlightWhite ( Color highlightWhite )
    {
        getWebUI ().setHighlightWhite ( highlightWhite );
    }

    public Color getHighlightDarkWhite ()
    {
        return getWebUI ().getHighlightDarkWhite ();
    }

    public void setHighlightDarkWhite ( Color highlightDarkWhite )
    {
        getWebUI ().setHighlightDarkWhite ( highlightDarkWhite );
    }

    public Shape provideShape ()
    {
        return getWebUI ().provideShape ();
    }

    public WebProgressBarUI getWebUI ()
    {
        return ( WebProgressBarUI ) getUI ();
    }

    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebProgressBarUI ) )
        {
            try
            {
                setUI ( ( WebProgressBarUI ) ReflectUtils.createInstance ( WebLookAndFeel.progressBarUI ) );
            }
            catch ( Throwable e )
            {
                e.printStackTrace ();
                setUI ( new WebProgressBarUI () );
            }
        }
        else
        {
            setUI ( getUI () );
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
    public WebProgressBar setPlainFont ()
    {
        return SwingUtils.setPlainFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public WebProgressBar setPlainFont ( boolean apply )
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
    public WebProgressBar setBoldFont ()
    {
        return SwingUtils.setBoldFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public WebProgressBar setBoldFont ( boolean apply )
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
    public WebProgressBar setItalicFont ()
    {
        return SwingUtils.setItalicFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public WebProgressBar setItalicFont ( boolean apply )
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
    public WebProgressBar setFontStyle ( boolean bold, boolean italic )
    {
        return SwingUtils.setFontStyle ( this, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    public WebProgressBar setFontStyle ( int style )
    {
        return SwingUtils.setFontStyle ( this, style );
    }

    /**
     * {@inheritDoc}
     */
    public WebProgressBar setFontSize ( int fontSize )
    {
        return SwingUtils.setFontSize ( this, fontSize );
    }

    /**
     * {@inheritDoc}
     */
    public WebProgressBar changeFontSize ( int change )
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
    public WebProgressBar setFontSizeAndStyle ( int fontSize, boolean bold, boolean italic )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    public WebProgressBar setFontSizeAndStyle ( int fontSize, int style )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, style );
    }

    /**
     * {@inheritDoc}
     */
    public WebProgressBar setFontName ( String fontName )
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