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

package com.alee.laf.label;

import com.alee.extended.painter.Painter;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.LanguageMethods;
import com.alee.managers.language.updaters.LanguageUpdater;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SizeUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.FontMethods;
import com.alee.utils.swing.SizeMethods;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 26.07.11 Time: 13:16
 */

public class WebLabel extends JLabel implements LanguageMethods, FontMethods<WebLabel>, SizeMethods<WebLabel>
{
    public WebLabel ()
    {
        super ();
    }

    public WebLabel ( final Insets margin )
    {
        super ();
        setMargin ( margin );
    }

    public WebLabel ( final Icon image )
    {
        super ( image );
    }

    public WebLabel ( final Icon image, final Insets margin )
    {
        super ( image );
        setMargin ( margin );
    }

    public WebLabel ( final int horizontalAlignment )
    {
        super ();
        setHorizontalAlignment ( horizontalAlignment );
    }

    public WebLabel ( final int horizontalAlignment, final Insets margin )
    {
        super ();
        setHorizontalAlignment ( horizontalAlignment );
        setMargin ( margin );
    }

    public WebLabel ( final Icon image, final int horizontalAlignment )
    {
        super ( image, horizontalAlignment );
    }

    public WebLabel ( final Icon image, final int horizontalAlignment, final Insets margin )
    {
        super ( image, horizontalAlignment );
        setMargin ( margin );
    }

    public WebLabel ( final String text )
    {
        super ( text );
    }

    public WebLabel ( final String text, final Insets margin )
    {
        super ( text );
        setMargin ( margin );
    }

    public WebLabel ( final String text, final int horizontalAlignment )
    {
        super ( text, horizontalAlignment );
    }

    public WebLabel ( final String text, final int horizontalAlignment, final Insets margin )
    {
        super ( text, horizontalAlignment );
        setMargin ( margin );
    }

    public WebLabel ( final String text, final Icon icon )
    {
        super ( text, icon, LEADING );
    }

    public WebLabel ( final String text, final Icon icon, final Insets margin )
    {
        super ( text, icon, LEADING );
        setMargin ( margin );
    }

    public WebLabel ( final String text, final Icon icon, final int horizontalAlignment )
    {
        super ( text, icon, horizontalAlignment );
    }

    public WebLabel ( final String text, final Icon icon, final int horizontalAlignment, final Insets margin )
    {
        super ( text, icon, horizontalAlignment );
        setMargin ( margin );
    }

    public Insets getMargin ()
    {
        return getWebUI ().getMargin ();
    }

    public void setMargin ( final Insets margin )
    {
        getWebUI ().setMargin ( margin );
    }

    public WebLabel setMargin ( final int top, final int left, final int bottom, final int right )
    {
        setMargin ( new Insets ( top, left, bottom, right ) );
        return this;
    }

    public WebLabel setMargin ( final int spacing )
    {
        return setMargin ( spacing, spacing, spacing, spacing );
    }

    public Painter getPainter ()
    {
        return getWebUI ().getPainter ();
    }

    public WebLabel setPainter ( final Painter painter )
    {
        getWebUI ().setPainter ( painter );
        return this;
    }

    public boolean isDrawShade ()
    {
        return getWebUI ().isDrawShade ();
    }

    public WebLabel setDrawShade ( final boolean drawShade )
    {
        getWebUI ().setDrawShade ( drawShade );
        return this;
    }

    public Color getShadeColor ()
    {
        return getWebUI ().getShadeColor ();
    }

    public WebLabel setShadeColor ( final Color shadeColor )
    {
        getWebUI ().setShadeColor ( shadeColor );
        return this;
    }

    public Float getTransparency ()
    {
        return getWebUI ().getTransparency ();
    }

    public WebLabel setTransparency ( final Float transparency )
    {
        getWebUI ().setTransparency ( transparency );
        return this;
    }

    public WebLabelUI getWebUI ()
    {
        return ( WebLabelUI ) getUI ();
    }

    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebLabelUI ) )
        {
            try
            {
                setUI ( ( WebLabelUI ) ReflectUtils.createInstance ( WebLookAndFeel.labelUI ) );
            }
            catch ( Throwable e )
            {
                e.printStackTrace ();
                setUI ( new WebLabelUI () );
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
    public WebLabel setPlainFont ()
    {
        return SwingUtils.setPlainFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebLabel setPlainFont ( final boolean apply )
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
    public WebLabel setBoldFont ()
    {
        return SwingUtils.setBoldFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebLabel setBoldFont ( final boolean apply )
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
    public WebLabel setItalicFont ()
    {
        return SwingUtils.setItalicFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebLabel setItalicFont ( final boolean apply )
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
    public WebLabel setFontStyle ( final boolean bold, final boolean italic )
    {
        return SwingUtils.setFontStyle ( this, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebLabel setFontStyle ( final int style )
    {
        return SwingUtils.setFontStyle ( this, style );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebLabel setFontSize ( final int fontSize )
    {
        return SwingUtils.setFontSize ( this, fontSize );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebLabel changeFontSize ( final int change )
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
    public WebLabel setFontSizeAndStyle ( final int fontSize, final boolean bold, final boolean italic )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebLabel setFontSizeAndStyle ( final int fontSize, final int style )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, style );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebLabel setFontName ( final String fontName )
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
    public WebLabel setPreferredWidth ( final int preferredWidth )
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
    public WebLabel setPreferredHeight ( final int preferredHeight )
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
    public WebLabel setMinimumWidth ( final int minimumWidth )
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
    public WebLabel setMinimumHeight ( final int minimumHeight )
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

    /**
     * Creates and returns new label with the specified preferences.
     *
     * @param key label language key
     * @return created label
     */
    public static WebLabel createTranslatedLabel ( final String key )
    {
        final WebLabel label = new WebLabel ();
        label.setLanguage ( key );
        return label;
    }

    /**
     * Creates and returns new label with the specified preferences.
     *
     * @param key  label language key
     * @param data label language data
     * @return created label
     */
    public static WebLabel createTranslatedLabel ( final String key, final Object... data )
    {
        final WebLabel label = new WebLabel ();
        label.setLanguage ( key, data );
        return label;
    }

    /**
     * Creates and returns new label with the specified preferences.
     *
     * @param halign horizontal alignment
     * @param key    label language key
     * @return created label
     */
    public static WebLabel createTranslatedLabel ( final int halign, final String key )
    {
        final WebLabel label = new WebLabel ( halign );
        label.setLanguage ( key );
        return label;
    }

    /**
     * Creates and returns new label with the specified preferences.
     *
     * @param halign horizontal alignment
     * @param key    label language key
     * @param data   label language data
     * @return created label
     */
    public static WebLabel createTranslatedLabel ( final int halign, final String key, final Object... data )
    {
        final WebLabel label = new WebLabel ( halign );
        label.setLanguage ( key, data );
        return label;
    }

    /**
     * Creates and returns new label with the specified preferences.
     *
     * @param icon label icon
     * @param key  label language key
     * @return created label
     */
    public static WebLabel createTranslatedLabel ( final Icon icon, final String key )
    {
        final WebLabel label = new WebLabel ( icon );
        label.setLanguage ( key );
        return label;
    }

    /**
     * Creates and returns new label with the specified preferences.
     *
     * @param icon label icon
     * @param key  label language key
     * @param data label language data
     * @return created label
     */
    public static WebLabel createTranslatedLabel ( final Icon icon, final String key, final Object... data )
    {
        final WebLabel label = new WebLabel ( icon );
        label.setLanguage ( key, data );
        return label;
    }

    /**
     * Creates and returns new label with the specified preferences.
     *
     * @param halign horizontal alignment
     * @param icon   label icon
     * @param key    label language key
     * @return created label
     */
    public static WebLabel createTranslatedLabel ( final int halign, final Icon icon, final String key )
    {
        final WebLabel label = new WebLabel ( icon, halign );
        label.setLanguage ( key );
        return label;
    }

    /**
     * Creates and returns new label with the specified preferences.
     *
     * @param halign horizontal alignment
     * @param icon   label icon
     * @param key    label language key
     * @param data   label language data
     * @return created label
     */
    public static WebLabel createTranslatedLabel ( final int halign, final Icon icon, final String key, final Object... data )
    {
        final WebLabel label = new WebLabel ( icon, halign );
        label.setLanguage ( key, data );
        return label;
    }
}