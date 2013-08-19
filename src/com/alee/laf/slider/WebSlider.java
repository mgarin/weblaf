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

import com.alee.laf.WebLookAndFeel;
import com.alee.managers.settings.DefaultValue;
import com.alee.managers.settings.SettingsManager;
import com.alee.managers.settings.SettingsMethods;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.FontMethods;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 28.06.11 Time: 1:08
 */

public class WebSlider extends JSlider implements SettingsMethods, FontMethods<WebSlider>
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

    public WebSliderUI getWebUI ()
    {
        return ( WebSliderUI ) getUI ();
    }

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
    public void registerSettings ( String key )
    {
        SettingsManager.registerComponent ( this, key );
    }

    /**
     * {@inheritDoc}
     */
    public <T extends DefaultValue> void registerSettings ( String key, Class<T> defaultValueClass )
    {
        SettingsManager.registerComponent ( this, key, defaultValueClass );
    }

    /**
     * {@inheritDoc}
     */
    public void registerSettings ( String key, Object defaultValue )
    {
        SettingsManager.registerComponent ( this, key, defaultValue );
    }

    /**
     * {@inheritDoc}
     */
    public void registerSettings ( String group, String key )
    {
        SettingsManager.registerComponent ( this, group, key );
    }

    /**
     * {@inheritDoc}
     */
    public <T extends DefaultValue> void registerSettings ( String group, String key, Class<T> defaultValueClass )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValueClass );
    }

    /**
     * {@inheritDoc}
     */
    public void registerSettings ( String group, String key, Object defaultValue )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValue );
    }

    /**
     * {@inheritDoc}
     */
    public void registerSettings ( String key, boolean loadInitialSettings, boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, key, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    public <T extends DefaultValue> void registerSettings ( String key, Class<T> defaultValueClass, boolean loadInitialSettings,
                                                            boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, key, defaultValueClass, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    public void registerSettings ( String key, Object defaultValue, boolean loadInitialSettings, boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, key, defaultValue, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    public <T extends DefaultValue> void registerSettings ( String group, String key, Class<T> defaultValueClass,
                                                            boolean loadInitialSettings, boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValueClass, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    public void registerSettings ( String group, String key, Object defaultValue, boolean loadInitialSettings,
                                   boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValue, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    public void registerSettings ( SettingsProcessor settingsProcessor )
    {
        SettingsManager.registerComponent ( this, settingsProcessor );
    }

    /**
     * {@inheritDoc}
     */
    public void unregisterSettings ()
    {
        SettingsManager.unregisterComponent ( this );
    }

    /**
     * {@inheritDoc}
     */
    public void loadSettings ()
    {
        SettingsManager.loadComponentSettings ( this );
    }

    /**
     * {@inheritDoc}
     */
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
    public WebSlider setPlainFont ()
    {
        return SwingUtils.setPlainFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public WebSlider setPlainFont ( boolean apply )
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
    public WebSlider setBoldFont ()
    {
        return SwingUtils.setBoldFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public WebSlider setBoldFont ( boolean apply )
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
    public WebSlider setItalicFont ()
    {
        return SwingUtils.setItalicFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public WebSlider setItalicFont ( boolean apply )
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
    public WebSlider setFontStyle ( boolean bold, boolean italic )
    {
        return SwingUtils.setFontStyle ( this, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    public WebSlider setFontStyle ( int style )
    {
        return SwingUtils.setFontStyle ( this, style );
    }

    /**
     * {@inheritDoc}
     */
    public WebSlider setFontSize ( int fontSize )
    {
        return SwingUtils.setFontSize ( this, fontSize );
    }

    /**
     * {@inheritDoc}
     */
    public WebSlider changeFontSize ( int change )
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
    public WebSlider setFontSizeAndStyle ( int fontSize, boolean bold, boolean italic )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    public WebSlider setFontSizeAndStyle ( int fontSize, int style )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, style );
    }

    /**
     * {@inheritDoc}
     */
    public WebSlider setFontName ( String fontName )
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