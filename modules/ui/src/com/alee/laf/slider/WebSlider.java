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

package com.alee.laf.slider;

import com.alee.extended.painter.Painter;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.settings.DefaultValue;
import com.alee.managers.settings.SettingsManager;
import com.alee.managers.settings.SettingsMethods;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SizeUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.FontMethods;
import com.alee.utils.swing.SizeMethods;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 28.06.11 Time: 1:08
 */

public class WebSlider extends JSlider implements SettingsMethods, FontMethods<WebSlider>, SizeMethods<WebSlider>
{
    public WebSlider ()
    {
        super ();
    }

    public WebSlider ( final int orientation )
    {
        super ( orientation );
    }

    public WebSlider ( final int min, final int max )
    {
        super ( min, max );
    }

    public WebSlider ( final int min, final int max, final int value )
    {
        super ( min, max, value );
    }

    public WebSlider ( final int orientation, final int min, final int max, final int value )
    {
        super ( orientation, min, max, value );
    }

    public WebSlider ( final BoundedRangeModel brm )
    {
        super ( brm );
    }

    public boolean isAnimated ()
    {
        return getWebUI ().isAnimated ();
    }

    public void setAnimated ( final boolean animated )
    {
        getWebUI ().setAnimated ( animated );
    }

    public boolean isRolloverDarkBorderOnly ()
    {
        return getWebUI ().isRolloverDarkBorderOnly ();
    }

    public void setRolloverDarkBorderOnly ( final boolean rolloverDarkBorderOnly )
    {
        getWebUI ().setRolloverDarkBorderOnly ( rolloverDarkBorderOnly );
    }

    public Color getTrackBgTop ()
    {
        return getWebUI ().getTrackBgTop ();
    }

    public void setTrackBgTop ( final Color trackBgTop )
    {
        getWebUI ().setTrackBgTop ( trackBgTop );
    }

    public Color getTrackBgBottom ()
    {
        return getWebUI ().getTrackBgBottom ();
    }

    public void setTrackBgBottom ( final Color trackBgBottom )
    {
        getWebUI ().setTrackBgBottom ( trackBgBottom );
    }

    public int getTrackHeight ()
    {
        return getWebUI ().getTrackHeight ();
    }

    public void setTrackHeight ( final int trackHeight )
    {
        getWebUI ().setTrackHeight ( trackHeight );
    }

    public int getTrackRound ()
    {
        return getWebUI ().getTrackRound ();
    }

    public void setTrackRound ( final int trackRound )
    {
        getWebUI ().setTrackRound ( trackRound );
    }

    public int getTrackShadeWidth ()
    {
        return getWebUI ().getTrackShadeWidth ();
    }

    public void setTrackShadeWidth ( final int trackShadeWidth )
    {
        getWebUI ().setTrackShadeWidth ( trackShadeWidth );
    }

    public boolean isDrawProgress ()
    {
        return getWebUI ().isDrawProgress ();
    }

    public void setDrawProgress ( final boolean drawProgress )
    {
        getWebUI ().setDrawProgress ( drawProgress );
    }

    public int getProgressRound ()
    {
        return getWebUI ().getProgressRound ();
    }

    public void setProgressRound ( final int progressRound )
    {
        getWebUI ().setProgressRound ( progressRound );
    }

    public int getProgressShadeWidth ()
    {
        return getWebUI ().getProgressShadeWidth ();
    }

    public void setProgressShadeWidth ( final int progressShadeWidth )
    {
        getWebUI ().setProgressShadeWidth ( progressShadeWidth );
    }

    public boolean isDrawThumb ()
    {
        return getWebUI ().isDrawThumb ();
    }

    public void setDrawThumb ( final boolean drawThumb )
    {
        getWebUI ().setDrawThumb ( drawThumb );
    }

    public Color getThumbBgTop ()
    {
        return getWebUI ().getThumbBgTop ();
    }

    public void setThumbBgTop ( final Color thumbBgTop )
    {
        getWebUI ().setThumbBgTop ( thumbBgTop );
    }

    public Color getThumbBgBottom ()
    {
        return getWebUI ().getThumbBgBottom ();
    }

    public void setThumbBgBottom ( final Color thumbBgBottom )
    {
        getWebUI ().setThumbBgBottom ( thumbBgBottom );
    }

    public int getThumbWidth ()
    {
        return getWebUI ().getThumbWidth ();
    }

    public void setThumbWidth ( final int thumbWidth )
    {
        getWebUI ().setThumbWidth ( thumbWidth );
    }

    public int getThumbHeight ()
    {
        return getWebUI ().getThumbHeight ();
    }

    public void setThumbHeight ( final int thumbHeight )
    {
        getWebUI ().setThumbHeight ( thumbHeight );
    }

    public int getThumbRound ()
    {
        return getWebUI ().getThumbRound ();
    }

    public void setThumbRound ( final int thumbRound )
    {
        getWebUI ().setThumbRound ( thumbRound );
    }

    public int getThumbShadeWidth ()
    {
        return getWebUI ().getThumbShadeWidth ();
    }

    public void setThumbShadeWidth ( final int thumbShadeWidth )
    {
        getWebUI ().setThumbShadeWidth ( thumbShadeWidth );
    }

    public boolean isAngledThumb ()
    {
        return getWebUI ().isAngledThumb ();
    }

    public void setAngledThumb ( final boolean angledThumb )
    {
        getWebUI ().setAngledThumb ( angledThumb );
    }

    public boolean isSharpThumbAngle ()
    {
        return getWebUI ().isSharpThumbAngle ();
    }

    public void setSharpThumbAngle ( final boolean sharpThumbAngle )
    {
        getWebUI ().setSharpThumbAngle ( sharpThumbAngle );
    }

    public int getThumbAngleLength ()
    {
        return getWebUI ().getThumbAngleLength ();
    }

    public void setThumbAngleLength ( final int thumbAngleLength )
    {
        getWebUI ().setThumbAngleLength ( thumbAngleLength );
    }

    public Insets getMargin ()
    {
        return getWebUI ().getMargin ();
    }

    public void setMargin ( final Insets margin )
    {
        getWebUI ().setMargin ( margin );
    }

    public WebSlider setMargin ( final int top, final int left, final int bottom, final int right )
    {
        setMargin ( new Insets ( top, left, bottom, right ) );
        return this;
    }

    public WebSlider setMargin ( final int spacing )
    {
        return setMargin ( spacing, spacing, spacing, spacing );
    }

    public Painter getPainter ()
    {
        return getWebUI ().getPainter ();
    }

    public WebSlider setPainter ( final Painter painter )
    {
        getWebUI ().setPainter ( painter );
        return this;
    }

    public WebSliderUI getWebUI ()
    {
        return ( WebSliderUI ) getUI ();
    }

    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebSliderUI ) )
        {
            try
            {
                setUI ( ( WebSliderUI ) ReflectUtils.createInstance ( WebLookAndFeel.sliderUI, this ) );
            }
            catch ( final Throwable e )
            {
                e.printStackTrace ();
                setUI ( new WebSliderUI ( this ) );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }

    /**
     * Settings methods
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( final String key )
    {
        SettingsManager.registerComponent ( this, key );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends DefaultValue> void registerSettings ( final String key, final Class<T> defaultValueClass )
    {
        SettingsManager.registerComponent ( this, key, defaultValueClass );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( final String key, final Object defaultValue )
    {
        SettingsManager.registerComponent ( this, key, defaultValue );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( final String group, final String key )
    {
        SettingsManager.registerComponent ( this, group, key );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends DefaultValue> void registerSettings ( final String group, final String key, final Class<T> defaultValueClass )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValueClass );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( final String group, final String key, final Object defaultValue )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValue );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( final String key, final boolean loadInitialSettings, final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, key, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends DefaultValue> void registerSettings ( final String key, final Class<T> defaultValueClass,
                                                            final boolean loadInitialSettings, final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, key, defaultValueClass, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( final String key, final Object defaultValue, final boolean loadInitialSettings,
                                   final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, key, defaultValue, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends DefaultValue> void registerSettings ( final String group, final String key, final Class<T> defaultValueClass,
                                                            final boolean loadInitialSettings, final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValueClass, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( final String group, final String key, final Object defaultValue, final boolean loadInitialSettings,
                                   final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValue, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( final SettingsProcessor settingsProcessor )
    {
        SettingsManager.registerComponent ( this, settingsProcessor );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unregisterSettings ()
    {
        SettingsManager.unregisterComponent ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadSettings ()
    {
        SettingsManager.loadComponentSettings ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveSettings ()
    {
        SettingsManager.saveComponentSettings ( this );
    }

    /**
     * Font methods
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public WebSlider setPlainFont ()
    {
        return SwingUtils.setPlainFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebSlider setPlainFont ( final boolean apply )
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
    public WebSlider setBoldFont ()
    {
        return SwingUtils.setBoldFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebSlider setBoldFont ( final boolean apply )
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
    public WebSlider setItalicFont ()
    {
        return SwingUtils.setItalicFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebSlider setItalicFont ( final boolean apply )
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
    public WebSlider setFontStyle ( final boolean bold, final boolean italic )
    {
        return SwingUtils.setFontStyle ( this, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebSlider setFontStyle ( final int style )
    {
        return SwingUtils.setFontStyle ( this, style );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebSlider setFontSize ( final int fontSize )
    {
        return SwingUtils.setFontSize ( this, fontSize );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebSlider changeFontSize ( final int change )
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
    public WebSlider setFontSizeAndStyle ( final int fontSize, final boolean bold, final boolean italic )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebSlider setFontSizeAndStyle ( final int fontSize, final int style )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, style );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebSlider setFontName ( final String fontName )
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
    public WebSlider setPreferredWidth ( final int preferredWidth )
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
    public WebSlider setPreferredHeight ( final int preferredHeight )
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
    public WebSlider setMinimumWidth ( final int minimumWidth )
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
    public WebSlider setMinimumHeight ( final int minimumHeight )
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