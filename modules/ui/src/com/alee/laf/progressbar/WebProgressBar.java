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
import com.alee.utils.SizeUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.swing.FontMethods;
import com.alee.utils.swing.SizeMethods;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 28.06.11 Time: 1:10
 */

public class WebProgressBar extends JProgressBar
        implements ShapeProvider, LanguageMethods, FontMethods<WebProgressBar>, SizeMethods<WebProgressBar>
{
    public WebProgressBar ()
    {
        super ();
        reinstallUI ();
    }

    public WebProgressBar ( final int orient )
    {
        super ( orient );
        reinstallUI ();
    }

    public WebProgressBar ( final int min, final int max )
    {
        super ( min, max );
        reinstallUI ();
    }

    public WebProgressBar ( final int orient, final int min, final int max )
    {
        super ( orient, min, max );
        reinstallUI ();
    }

    public WebProgressBar ( final BoundedRangeModel newModel )
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

    public void setRound ( final int round )
    {
        getWebUI ().setRound ( round );
    }

    public int getInnerRound ()
    {
        return getWebUI ().getInnerRound ();
    }

    public void setInnerRound ( final int innerRound )
    {
        getWebUI ().setInnerRound ( innerRound );
    }

    public int getShadeWidth ()
    {
        return getWebUI ().getShadeWidth ();
    }

    public void setShadeWidth ( final int shadeWidth )
    {
        getWebUI ().setShadeWidth ( shadeWidth );
    }

    public boolean isPaintIndeterminateBorder ()
    {
        return getWebUI ().isPaintIndeterminateBorder ();
    }

    public void setPaintIndeterminateBorder ( final boolean paintIndeterminateBorder )
    {
        getWebUI ().setPaintIndeterminateBorder ( paintIndeterminateBorder );
    }

    public int getPreferredProgressWidth ()
    {
        return getWebUI ().getPreferredProgressWidth ();
    }

    public void setPreferredProgressWidth ( final int preferredWidth )
    {
        getWebUI ().setPreferredProgressWidth ( preferredWidth );
    }

    public Color getBgTop ()
    {
        return getWebUI ().getBgTop ();
    }

    public void setBgTop ( final Color bgTop )
    {
        getWebUI ().setBgTop ( bgTop );
    }

    public Color getBgBottom ()
    {
        return getWebUI ().getBgBottom ();
    }

    public void setBgBottom ( final Color bgBottom )
    {
        getWebUI ().setBgBottom ( bgBottom );
    }

    public Color getProgressTopColor ()
    {
        return getWebUI ().getProgressTopColor ();
    }

    public void setProgressTopColor ( final Color progressTopColor )
    {
        getWebUI ().setProgressTopColor ( progressTopColor );
    }

    public Color getProgressBottomColor ()
    {
        return getWebUI ().getProgressBottomColor ();
    }

    public void setProgressBottomColor ( final Color progressBottomColor )
    {
        getWebUI ().setProgressBottomColor ( progressBottomColor );
    }

    public Color getIndeterminateBorder ()
    {
        return getWebUI ().getIndeterminateBorder ();
    }

    public void setIndeterminateBorder ( final Color indeterminateBorder )
    {
        getWebUI ().setIndeterminateBorder ( indeterminateBorder );
    }

    public Color getHighlightWhite ()
    {
        return getWebUI ().getHighlightWhite ();
    }

    public void setHighlightWhite ( final Color highlightWhite )
    {
        getWebUI ().setHighlightWhite ( highlightWhite );
    }

    public Color getHighlightDarkWhite ()
    {
        return getWebUI ().getHighlightDarkWhite ();
    }

    public void setHighlightDarkWhite ( final Color highlightDarkWhite )
    {
        getWebUI ().setHighlightDarkWhite ( highlightDarkWhite );
    }

    @Override
    public Shape provideShape ()
    {
        return getWebUI ().provideShape ();
    }

    public WebProgressBarUI getWebUI ()
    {
        return ( WebProgressBarUI ) getUI ();
    }

    @Override
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
     * Font methods
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public WebProgressBar setPlainFont ()
    {
        return SwingUtils.setPlainFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebProgressBar setPlainFont ( final boolean apply )
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
    public WebProgressBar setBoldFont ()
    {
        return SwingUtils.setBoldFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebProgressBar setBoldFont ( final boolean apply )
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
    public WebProgressBar setItalicFont ()
    {
        return SwingUtils.setItalicFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebProgressBar setItalicFont ( final boolean apply )
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
    public WebProgressBar setFontStyle ( final boolean bold, final boolean italic )
    {
        return SwingUtils.setFontStyle ( this, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebProgressBar setFontStyle ( final int style )
    {
        return SwingUtils.setFontStyle ( this, style );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebProgressBar setFontSize ( final int fontSize )
    {
        return SwingUtils.setFontSize ( this, fontSize );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebProgressBar changeFontSize ( final int change )
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
    public WebProgressBar setFontSizeAndStyle ( final int fontSize, final boolean bold, final boolean italic )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebProgressBar setFontSizeAndStyle ( final int fontSize, final int style )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, style );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebProgressBar setFontName ( final String fontName )
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

    /**
     * Size methods.
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPreferredWidth ()
    {
        return SizeUtils.getPreferredWidth ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebProgressBar setPreferredWidth ( final int preferredWidth )
    {
        return SizeUtils.setPreferredWidth ( this, preferredWidth );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPreferredHeight ()
    {
        return SizeUtils.getPreferredHeight ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebProgressBar setPreferredHeight ( final int preferredHeight )
    {
        return SizeUtils.setPreferredHeight ( this, preferredHeight );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMinimumWidth ()
    {
        return SizeUtils.getMinimumWidth ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebProgressBar setMinimumWidth ( final int minimumWidth )
    {
        return SizeUtils.setMinimumWidth ( this, minimumWidth );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMinimumHeight ()
    {
        return SizeUtils.getMinimumHeight ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebProgressBar setMinimumHeight ( final int minimumHeight )
    {
        return SizeUtils.setMinimumHeight ( this, minimumHeight );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getPreferredSize ()
    {
        return SizeUtils.getPreferredSize ( this, super.getPreferredSize () );
    }
}