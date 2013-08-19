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

package com.alee.laf.combobox;

import com.alee.laf.WebLookAndFeel;
import com.alee.managers.settings.DefaultValue;
import com.alee.managers.settings.SettingsManager;
import com.alee.managers.settings.SettingsMethods;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.swing.FontMethods;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

/**
 * User: mgarin Date: 28.06.11 Time: 1:11
 */

public class WebComboBox extends JComboBox implements ShapeProvider, SettingsMethods, FontMethods<WebComboBox>
{
    public WebComboBox ()
    {
        super ();
    }

    public WebComboBox ( Vector<?> items )
    {
        super ( items );
    }

    public WebComboBox ( Object[] items )
    {
        super ( items );
    }

    public WebComboBox ( ComboBoxModel aModel )
    {
        super ( aModel );
    }

    public void setEditorColumns ( int columns )
    {
        getWebUI ().setEditorColumns ( columns );
    }

    public ImageIcon getExpandIcon ()
    {
        return getWebUI ().getExpandIcon ();
    }

    public void setExpandIcon ( ImageIcon expandIcon )
    {
        getWebUI ().setExpandIcon ( expandIcon );
    }

    public ImageIcon getCollapseIcon ()
    {
        return getWebUI ().getCollapseIcon ();
    }

    public void setCollapseIcon ( ImageIcon collapseIcon )
    {
        getWebUI ().setCollapseIcon ( collapseIcon );
    }

    public int getIconSpacing ()
    {
        return getWebUI ().getIconSpacing ();
    }

    public void setIconSpacing ( int iconSpacing )
    {
        getWebUI ().setIconSpacing ( iconSpacing );
    }

    public boolean isDrawBorder ()
    {
        return getWebUI ().isDrawBorder ();
    }

    public void setDrawBorder ( boolean drawBorder )
    {
        getWebUI ().setDrawBorder ( drawBorder );
    }

    public boolean isDrawFocus ()
    {
        return getWebUI ().isDrawFocus ();
    }

    public void setDrawFocus ( boolean drawFocus )
    {
        getWebUI ().setDrawFocus ( drawFocus );
    }

    public int getRound ()
    {
        return getWebUI ().getRound ();
    }

    public void setRound ( int round )
    {
        getWebUI ().setRound ( round );
    }

    public int getShadeWidth ()
    {
        return getWebUI ().getShadeWidth ();
    }

    public void setShadeWidth ( int shadeWidth )
    {
        getWebUI ().setShadeWidth ( shadeWidth );
    }

    public boolean isMouseWheelScrollingEnabled ()
    {
        return getWebUI ().isMouseWheelScrollingEnabled ();
    }

    public void setMouseWheelScrollingEnabled ( boolean enabled )
    {
        getWebUI ().setMouseWheelScrollingEnabled ( enabled );
    }

    public Shape provideShape ()
    {
        return getWebUI ().provideShape ();
    }

    public WebComboBoxCellRenderer getWebRenderer ()
    {
        return ( WebComboBoxCellRenderer ) getRenderer ();
    }

    public WebComboBoxUI getWebUI ()
    {
        return ( WebComboBoxUI ) getUI ();
    }

    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebComboBoxUI ) )
        {
            try
            {
                setUI ( ( WebComboBoxUI ) ReflectUtils.createInstance ( WebLookAndFeel.comboBoxUI ) );
            }
            catch ( Throwable e )
            {
                e.printStackTrace ();
                setUI ( new WebComboBoxUI () );
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
    public WebComboBox setPlainFont ()
    {
        return SwingUtils.setPlainFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public WebComboBox setPlainFont ( boolean apply )
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
    public WebComboBox setBoldFont ()
    {
        return SwingUtils.setBoldFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public WebComboBox setBoldFont ( boolean apply )
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
    public WebComboBox setItalicFont ()
    {
        return SwingUtils.setItalicFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    public WebComboBox setItalicFont ( boolean apply )
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
    public WebComboBox setFontStyle ( boolean bold, boolean italic )
    {
        return SwingUtils.setFontStyle ( this, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    public WebComboBox setFontStyle ( int style )
    {
        return SwingUtils.setFontStyle ( this, style );
    }

    /**
     * {@inheritDoc}
     */
    public WebComboBox setFontSize ( int fontSize )
    {
        return SwingUtils.setFontSize ( this, fontSize );
    }

    /**
     * {@inheritDoc}
     */
    public WebComboBox changeFontSize ( int change )
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
    public WebComboBox setFontSizeAndStyle ( int fontSize, boolean bold, boolean italic )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    public WebComboBox setFontSizeAndStyle ( int fontSize, int style )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, style );
    }

    /**
     * {@inheritDoc}
     */
    public WebComboBox setFontName ( String fontName )
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