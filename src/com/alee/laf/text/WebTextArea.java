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

package com.alee.laf.text;

import com.alee.extended.painter.Painter;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.LanguageMethods;
import com.alee.managers.language.updaters.LanguageUpdater;
import com.alee.managers.settings.DefaultValue;
import com.alee.managers.settings.SettingsManager;
import com.alee.managers.settings.SettingsMethods;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.FontMethods;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;

/**
 * User: mgarin Date: 22.08.11 Time: 12:40
 */

public class WebTextArea extends JTextArea implements LanguageMethods, SettingsMethods, FontMethods<WebTextArea>
{
    public WebTextArea ()
    {
        super ();
    }

    public WebTextArea ( String text )
    {
        super ( text );
    }

    public WebTextArea ( int rows, int columns )
    {
        super ( rows, columns );
    }

    public WebTextArea ( String text, int rows, int columns )
    {
        super ( text, rows, columns );
    }

    public WebTextArea ( Document doc )
    {
        super ( doc );
    }

    public WebTextArea ( Document doc, String text, int rows, int columns )
    {
        super ( doc, text, rows, columns );
    }

    /**
     * Additional component methods
     */

    public void clear ()
    {
        setText ( "" );
    }

    /**
     * UI methods
     */

    public void setMargin ( int top, int left, int bottom, int right )
    {
        setMargin ( new Insets ( top, left, bottom, right ) );
    }

    public void setMargin ( int spacing )
    {
        setMargin ( spacing, spacing, spacing, spacing );
    }

    public Painter getPainter ()
    {
        return getWebUI ().getPainter ();
    }

    public void setPainter ( Painter painter )
    {
        getWebUI ().setPainter ( painter );
    }

    public WebTextAreaUI getWebUI ()
    {
        return ( WebTextAreaUI ) getUI ();
    }

    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebTextAreaUI ) )
        {
            try
            {
                setUI ( ( WebTextAreaUI ) ReflectUtils.createInstance ( WebLookAndFeel.textAreaUI ) );
            }
            catch ( Throwable e )
            {
                e.printStackTrace ();
                setUI ( new WebTextAreaUI () );
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
    public WebTextArea setPlainFont ()
    {
        return SwingUtils.setPlainFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public WebTextArea setPlainFont ( boolean apply )
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
    public WebTextArea setBoldFont ()
    {
        return SwingUtils.setBoldFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public WebTextArea setBoldFont ( boolean apply )
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
    public WebTextArea setItalicFont ()
    {
        return SwingUtils.setItalicFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public WebTextArea setItalicFont ( boolean apply )
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
    public WebTextArea setFontStyle ( boolean bold, boolean italic )
    {
        return SwingUtils.setFontStyle ( this, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    public WebTextArea setFontStyle ( int style )
    {
        return SwingUtils.setFontStyle ( this, style );
    }

    /**
     * {@inheritDoc}
     */
    public WebTextArea setFontSize ( int fontSize )
    {
        return SwingUtils.setFontSize ( this, fontSize );
    }

    /**
     * {@inheritDoc}
     */
    public WebTextArea changeFontSize ( int change )
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
    public WebTextArea setFontSizeAndStyle ( int fontSize, boolean bold, boolean italic )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    public WebTextArea setFontSizeAndStyle ( int fontSize, int style )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, style );
    }

    /**
     * {@inheritDoc}
     */
    public WebTextArea setFontName ( String fontName )
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