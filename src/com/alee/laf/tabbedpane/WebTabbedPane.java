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

package com.alee.laf.tabbedpane;

import com.alee.extended.painter.Painter;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.language.LanguageContainer;
import com.alee.managers.language.LanguageManager;
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

/**
 * User: mgarin Date: 28.06.11 Time: 0:52
 */

public class WebTabbedPane extends JTabbedPane implements ShapeProvider, SettingsMethods, FontMethods<WebTabbedPane>, LanguageContainer
{
    public WebTabbedPane ()
    {
        super ();
    }

    public WebTabbedPane ( int tabPlacement )
    {
        super ( tabPlacement );
    }

    public WebTabbedPane ( int tabPlacement, int tabLayoutPolicy )
    {
        super ( tabPlacement, tabLayoutPolicy );
    }

    public WebTabbedPane ( TabbedPaneStyle style )
    {
        super ();
        setTabbedPaneStyle ( style );
    }

    public WebTabbedPane ( int tabPlacement, TabbedPaneStyle style )
    {
        super ( tabPlacement );
        setTabbedPaneStyle ( style );
    }

    public WebTabbedPane ( int tabPlacement, int tabLayoutPolicy, TabbedPaneStyle style )
    {
        super ( tabPlacement, tabLayoutPolicy );
        setTabbedPaneStyle ( style );
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

    public Insets getContentInsets ()
    {
        return getWebUI ().getContentInsets ();
    }

    public void setContentInsets ( Insets contentInsets )
    {
        getWebUI ().setContentInsets ( contentInsets );
    }

    public Insets getTabInsets ()
    {
        return getWebUI ().getTabInsets ();
    }

    public void setTabInsets ( Insets tabInsets )
    {
        getWebUI ().setTabInsets ( tabInsets );
    }

    public Color getSelectedTopBg ()
    {
        return getWebUI ().getSelectedTopBg ();
    }

    public void setSelectedTopBg ( Color selectedTopBg )
    {
        getWebUI ().setSelectedTopBg ( selectedTopBg );
    }

    public Color getSelectedBottomBg ()
    {
        return getWebUI ().getSelectedBottomBg ();
    }

    public void setSelectedBottomBg ( Color selectedBottomBg )
    {
        getWebUI ().setSelectedBottomBg ( selectedBottomBg );
    }

    public Color getTopBg ()
    {
        return getWebUI ().getTopBg ();
    }

    public void setTopBg ( Color topBg )
    {
        getWebUI ().setTopBg ( topBg );
    }

    public Color getBottomBg ()
    {
        return getWebUI ().getBottomBg ();
    }

    public void setBottomBg ( Color bottomBg )
    {
        getWebUI ().setBottomBg ( bottomBg );
    }

    public void setSelectedForegroundAt ( int tabIndex, Color foreground )
    {
        getWebUI ().setSelectedForegroundAt ( tabIndex, foreground );
    }

    public Color getSelectedForegroundAt ( int tabIndex )
    {
        return getWebUI ().getSelectedForegroundAt ( tabIndex );
    }

    public void setBackgroundPainterAt ( int tabIndex, Painter painter )
    {
        getWebUI ().setBackgroundPainterAt ( tabIndex, painter );
    }

    public Painter getBackgroundPainterAt ( int tabIndex )
    {
        return getWebUI ().getBackgroundPainterAt ( tabIndex );
    }

    public TabbedPaneStyle getTabbedPaneStyle ()
    {
        return getWebUI ().getTabbedPaneStyle ();
    }

    public void setTabbedPaneStyle ( TabbedPaneStyle tabbedPaneStyle )
    {
        getWebUI ().setTabbedPaneStyle ( tabbedPaneStyle );
    }

    public Painter getPainter ()
    {
        return getWebUI ().getPainter ();
    }

    public void setPainter ( Painter painter )
    {
        getWebUI ().setPainter ( painter );
    }

    public int getTabRunIndent ()
    {
        return getWebUI ().getTabRunIndent ();
    }

    public void setTabRunIndent ( int tabRunIndent )
    {
        getWebUI ().setTabRunIndent ( tabRunIndent );
    }

    public int getTabOverlay ()
    {
        return getWebUI ().getTabOverlay ();
    }

    public void setTabOverlay ( int tabOverlay )
    {
        getWebUI ().setTabOverlay ( tabOverlay );
    }

    public TabStretchType getTabStretchType ()
    {
        return getWebUI ().getTabStretchType ();
    }

    public void setTabStretchType ( TabStretchType tabStretchType )
    {
        getWebUI ().setTabStretchType ( tabStretchType );
    }

    public Shape provideShape ()
    {
        return getWebUI ().provideShape ();
    }

    public WebTabbedPaneUI getWebUI ()
    {
        return ( WebTabbedPaneUI ) getUI ();
    }

    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebTabbedPaneUI ) )
        {
            try
            {
                setUI ( ( WebTabbedPaneUI ) ReflectUtils.createInstance ( WebLookAndFeel.tabbedPaneUI ) );
            }
            catch ( Throwable e )
            {
                e.printStackTrace ();
                setUI ( new WebTabbedPaneUI () );
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
    public WebTabbedPane setPlainFont ()
    {
        return SwingUtils.setPlainFont ( this );
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
    public WebTabbedPane setBoldFont ()
    {
        return SwingUtils.setBoldFont ( this );
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
    public WebTabbedPane setItalicFont ()
    {
        return SwingUtils.setItalicFont ( this );
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
    public WebTabbedPane setFontStyle ( boolean bold, boolean italic )
    {
        return SwingUtils.setFontStyle ( this, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    public WebTabbedPane setFontStyle ( int style )
    {
        return SwingUtils.setFontStyle ( this, style );
    }

    /**
     * {@inheritDoc}
     */
    public WebTabbedPane setFontSize ( int fontSize )
    {
        return SwingUtils.setFontSize ( this, fontSize );
    }

    /**
     * {@inheritDoc}
     */
    public WebTabbedPane changeFontSize ( int change )
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
    public WebTabbedPane setFontSizeAndStyle ( int fontSize, boolean bold, boolean italic )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    public WebTabbedPane setFontSizeAndStyle ( int fontSize, int style )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, style );
    }

    /**
     * {@inheritDoc}
     */
    public WebTabbedPane setFontName ( String fontName )
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

    /**
     * Language container methods
     */

    /**
     * {@inheritDoc}
     */
    public void setLanguageContainerKey ( String key )
    {
        LanguageManager.registerLanguageContainer ( this, key );
    }

    /**
     * {@inheritDoc}
     */
    public void removeLanguageContainerKey ()
    {
        LanguageManager.unregisterLanguageContainer ( this );
    }

    /**
     * {@inheritDoc}
     */
    public String getLanguageContainerKey ()
    {
        return LanguageManager.getLanguageContainerKey ( this );
    }
}
