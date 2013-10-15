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

    public WebSlider ( int orientation )
    {
        super ( orientation );
    }

    public WebSlider ( int min, int max )
    {
        super ( min, max );
    }

    public WebSlider ( int min, int max, int value )
    {
        super ( min, max, value );
    }

    public WebSlider ( int orientation, int min, int max, int value )
    {
        super ( orientation, min, max, value );
    }

    public WebSlider ( BoundedRangeModel brm )
    {
        super ( brm );
    }

    public boolean isAnimated ()
    {
        return getWebUI ().isAnimated ();
    }

    public void setAnimated ( boolean animated )
    {
        getWebUI ().setAnimated ( animated );
    }

    public boolean isRolloverDarkBorderOnly ()
    {
        return getWebUI ().isRolloverDarkBorderOnly ();
    }

    public void setRolloverDarkBorderOnly ( boolean rolloverDarkBorderOnly )
    {
        getWebUI ().setRolloverDarkBorderOnly ( rolloverDarkBorderOnly );
    }

    public Color getTrackBgTop ()
    {
        return getWebUI ().getTrackBgTop ();
    }

    public void setTrackBgTop ( Color trackBgTop )
    {
        getWebUI ().setTrackBgTop ( trackBgTop );
    }

    public Color getTrackBgBottom ()
    {
        return getWebUI ().getTrackBgBottom ();
    }

    public void setTrackBgBottom ( Color trackBgBottom )
    {
        getWebUI ().setTrackBgBottom ( trackBgBottom );
    }

    public int getTrackHeight ()
    {
        return getWebUI ().getTrackHeight ();
    }

    public void setTrackHeight ( int trackHeight )
    {
        getWebUI ().setTrackHeight ( trackHeight );
    }

    public int getTrackRound ()
    {
        return getWebUI ().getTrackRound ();
    }

    public void setTrackRound ( int trackRound )
    {
        getWebUI ().setTrackRound ( trackRound );
    }

    public int getTrackShadeWidth ()
    {
        return getWebUI ().getTrackShadeWidth ();
    }

    public void setTrackShadeWidth ( int trackShadeWidth )
    {
        getWebUI ().setTrackShadeWidth ( trackShadeWidth );
    }

    public boolean isDrawProgress ()
    {
        return getWebUI ().isDrawProgress ();
    }

    public void setDrawProgress ( boolean drawProgress )
    {
        getWebUI ().setDrawProgress ( drawProgress );
    }

    public int getProgressRound ()
    {
        return getWebUI ().getProgressRound ();
    }

    public void setProgressRound ( int progressRound )
    {
        getWebUI ().setProgressRound ( progressRound );
    }

    public int getProgressShadeWidth ()
    {
        return getWebUI ().getProgressShadeWidth ();
    }

    public void setProgressShadeWidth ( int progressShadeWidth )
    {
        getWebUI ().setProgressShadeWidth ( progressShadeWidth );
    }

    public boolean isDrawThumb ()
    {
        return getWebUI ().isDrawThumb ();
    }

    public void setDrawThumb ( boolean drawThumb )
    {
        getWebUI ().setDrawThumb ( drawThumb );
    }

    public Color getThumbBgTop ()
    {
        return getWebUI ().getThumbBgTop ();
    }

    public void setThumbBgTop ( Color thumbBgTop )
    {
        getWebUI ().setThumbBgTop ( thumbBgTop );
    }

    public Color getThumbBgBottom ()
    {
        return getWebUI ().getThumbBgBottom ();
    }

    public void setThumbBgBottom ( Color thumbBgBottom )
    {
        getWebUI ().setThumbBgBottom ( thumbBgBottom );
    }

    public int getThumbWidth ()
    {
        return getWebUI ().getThumbWidth ();
    }

    public void setThumbWidth ( int thumbWidth )
    {
        getWebUI ().setThumbWidth ( thumbWidth );
    }

    public int getThumbHeight ()
    {
        return getWebUI ().getThumbHeight ();
    }

    public void setThumbHeight ( int thumbHeight )
    {
        getWebUI ().setThumbHeight ( thumbHeight );
    }

    public int getThumbRound ()
    {
        return getWebUI ().getThumbRound ();
    }

    public void setThumbRound ( int thumbRound )
    {
        getWebUI ().setThumbRound ( thumbRound );
    }

    public int getThumbShadeWidth ()
    {
        return getWebUI ().getThumbShadeWidth ();
    }

    public void setThumbShadeWidth ( int thumbShadeWidth )
    {
        getWebUI ().setThumbShadeWidth ( thumbShadeWidth );
    }

    public boolean isAngledThumb ()
    {
        return getWebUI ().isAngledThumb ();
    }

    public void setAngledThumb ( boolean angledThumb )
    {
        getWebUI ().setAngledThumb ( angledThumb );
    }

    public boolean isSharpThumbAngle ()
    {
        return getWebUI ().isSharpThumbAngle ();
    }

    public void setSharpThumbAngle ( boolean sharpThumbAngle )
    {
        getWebUI ().setSharpThumbAngle ( sharpThumbAngle );
    }

    public int getThumbAngleLength ()
    {
        return getWebUI ().getThumbAngleLength ();
    }

    public void setThumbAngleLength ( int thumbAngleLength )
    {
        getWebUI ().setThumbAngleLength ( thumbAngleLength );
    }

    public Insets getMargin ()
    {
        return getWebUI ().getMargin ();
    }

    public void setMargin ( Insets margin )
    {
        getWebUI ().setMargin ( margin );
    }

    public WebSlider setMargin ( int top, int left, int bottom, int right )
    {
        setMargin ( new Insets ( top, left, bottom, right ) );
        return this;
    }

    public WebSlider setMargin ( int spacing )
    {
        return setMargin ( spacing, spacing, spacing, spacing );
    }

    public Painter getPainter ()
    {
        return getWebUI ().getPainter ();
    }

    public WebSlider setPainter ( Painter painter )
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
            catch ( Throwable e )
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
    public void registerSettings ( String key )
    {
        SettingsManager.registerComponent ( this, key );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends DefaultValue> void registerSettings ( String key, Class<T> defaultValueClass )
    {
        SettingsManager.registerComponent ( this, key, defaultValueClass );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( String key, Object defaultValue )
    {
        SettingsManager.registerComponent ( this, key, defaultValue );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( String group, String key )
    {
        SettingsManager.registerComponent ( this, group, key );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends DefaultValue> void registerSettings ( String group, String key, Class<T> defaultValueClass )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValueClass );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( String group, String key, Object defaultValue )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValue );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( String key, boolean loadInitialSettings, boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, key, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends DefaultValue> void registerSettings ( String key, Class<T> defaultValueClass, boolean loadInitialSettings,
                                                            boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, key, defaultValueClass, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( String key, Object defaultValue, boolean loadInitialSettings, boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, key, defaultValue, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends DefaultValue> void registerSettings ( String group, String key, Class<T> defaultValueClass,
                                                            boolean loadInitialSettings, boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValueClass, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( String group, String key, Object defaultValue, boolean loadInitialSettings,
                                   boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValue, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( SettingsProcessor settingsProcessor )
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
    public WebSlider setPlainFont ( boolean apply )
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
    public WebSlider setBoldFont ( boolean apply )
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
    public WebSlider setItalicFont ( boolean apply )
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
    public WebSlider setFontStyle ( boolean bold, boolean italic )
    {
        return SwingUtils.setFontStyle ( this, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebSlider setFontStyle ( int style )
    {
        return SwingUtils.setFontStyle ( this, style );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebSlider setFontSize ( int fontSize )
    {
        return SwingUtils.setFontSize ( this, fontSize );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebSlider changeFontSize ( int change )
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
    public WebSlider setFontSizeAndStyle ( int fontSize, boolean bold, boolean italic )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebSlider setFontSizeAndStyle ( int fontSize, int style )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, style );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebSlider setFontName ( String fontName )
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
    public WebSlider setPreferredWidth ( int preferredWidth )
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
    public WebSlider setPreferredHeight ( int preferredHeight )
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
    public WebSlider setMinimumWidth ( int minimumWidth )
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
    public WebSlider setMinimumHeight ( int minimumHeight )
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