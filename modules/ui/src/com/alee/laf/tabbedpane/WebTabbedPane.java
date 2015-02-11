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
import com.alee.managers.language.LanguageContainerMethods;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.LanguageMethods;
import com.alee.managers.language.updaters.LanguageUpdater;
import com.alee.managers.log.Log;
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

public class WebTabbedPane extends JTabbedPane
        implements ShapeProvider, LanguageMethods, SettingsMethods, FontMethods<WebTabbedPane>, LanguageContainerMethods
{
    public WebTabbedPane ()
    {
        super ();
    }

    public WebTabbedPane ( final int tabPlacement )
    {
        super ( tabPlacement );
    }

    public WebTabbedPane ( final int tabPlacement, final int tabLayoutPolicy )
    {
        super ( tabPlacement, tabLayoutPolicy );
    }

    public WebTabbedPane ( final TabbedPaneStyle style )
    {
        super ();
        setTabbedPaneStyle ( style );
    }

    public WebTabbedPane ( final int tabPlacement, final TabbedPaneStyle style )
    {
        super ( tabPlacement );
        setTabbedPaneStyle ( style );
    }

    public WebTabbedPane ( final int tabPlacement, final int tabLayoutPolicy, final TabbedPaneStyle style )
    {
        super ( tabPlacement, tabLayoutPolicy );
        setTabbedPaneStyle ( style );
    }

    /**
     * Returns tab index for the specified location or -1 if there is no tab there.
     *
     * @param point location
     * @return tab index for the specified location or -1 if there is no tab there
     */
    public int getTabAt ( final Point point )
    {
        return getTabAt ( point.x, point.y );
    }

    /**
     * Returns tab index for the specified location or -1 if there is no tab there.
     *
     * @param x location X
     * @param y location Y
     * @return tab index for the specified location or -1 if there is no tab there
     */
    public int getTabAt ( final int x, final int y )
    {
        return indexAtLocation ( x, y );
    }

    /**
     * Returns tab bounds for the specified location or null if there is no tab there.
     *
     * @param point location
     * @return tab bounds for the specified location or null if there is no tab there
     */
    public Rectangle getBoundsAt ( final Point point )
    {
        return getBoundsAt ( point.x, point.y );
    }

    /**
     * Returns tab bounds for the specified location or null if there is no tab there.
     *
     * @param x location X
     * @param y location Y
     * @return tab bounds for the specified location or null if there is no tab there
     */
    public Rectangle getBoundsAt ( final int x, final int y )
    {
        final int index = getTabAt ( x, y );
        return index != -1 ? getBoundsAt ( index ) : null;
    }

    public int getRound ()
    {
        return getWebUI ().getRound ();
    }

    public void setRound ( final int round )
    {
        getWebUI ().setRound ( round );
    }

    public int getShadeWidth ()
    {
        return getWebUI ().getShadeWidth ();
    }

    public void setShadeWidth ( final int shadeWidth )
    {
        getWebUI ().setShadeWidth ( shadeWidth );
    }

    public boolean isRotateTabInsets ()
    {
        return getWebUI ().isRotateTabInsets ();
    }

    public void setRotateTabInsets ( final boolean rotateTabInsets )
    {
        getWebUI ().setRotateTabInsets ( rotateTabInsets );
    }

    public Insets getContentInsets ()
    {
        return getWebUI ().getContentInsets ();
    }

    public void setContentInsets ( final Insets contentInsets )
    {
        getWebUI ().setContentInsets ( contentInsets );
    }

    public Insets getTabInsets ()
    {
        return getWebUI ().getTabInsets ();
    }

    public void setTabInsets ( final Insets tabInsets )
    {
        getWebUI ().setTabInsets ( tabInsets );
    }

    public Color getSelectedTopBg ()
    {
        return getWebUI ().getSelectedTopBg ();
    }

    public void setSelectedTopBg ( final Color selectedTopBg )
    {
        getWebUI ().setSelectedTopBg ( selectedTopBg );
    }

    public Color getSelectedBottomBg ()
    {
        return getWebUI ().getSelectedBottomBg ();
    }

    public void setSelectedBottomBg ( final Color selectedBottomBg )
    {
        getWebUI ().setSelectedBottomBg ( selectedBottomBg );
    }

    public Color getTopBg ()
    {
        return getWebUI ().getTopBg ();
    }

    public void setTopBg ( final Color topBg )
    {
        getWebUI ().setTopBg ( topBg );
    }

    public Color getBottomBg ()
    {
        return getWebUI ().getBottomBg ();
    }

    public void setBottomBg ( final Color bottomBg )
    {
        getWebUI ().setBottomBg ( bottomBg );
    }

    public void setSelectedForegroundAt ( final int tabIndex, final Color foreground )
    {
        getWebUI ().setSelectedForegroundAt ( tabIndex, foreground );
    }

    public Color getSelectedForegroundAt ( final int tabIndex )
    {
        return getWebUI ().getSelectedForegroundAt ( tabIndex );
    }

    public void setBackgroundPainterAt ( final int tabIndex, final Painter painter )
    {
        getWebUI ().setBackgroundPainterAt ( tabIndex, painter );
    }

    public Painter getBackgroundPainterAt ( final int tabIndex )
    {
        return getWebUI ().getBackgroundPainterAt ( tabIndex );
    }

    public TabbedPaneStyle getTabbedPaneStyle ()
    {
        return getWebUI ().getTabbedPaneStyle ();
    }

    public void setTabbedPaneStyle ( final TabbedPaneStyle tabbedPaneStyle )
    {
        getWebUI ().setTabbedPaneStyle ( tabbedPaneStyle );
    }

    public Painter getPainter ()
    {
        return getWebUI ().getPainter ();
    }

    public void setPainter ( final Painter painter )
    {
        getWebUI ().setPainter ( painter );
    }

    public int getTabRunIndent ()
    {
        return getWebUI ().getTabRunIndent ();
    }

    public void setTabRunIndent ( final int tabRunIndent )
    {
        getWebUI ().setTabRunIndent ( tabRunIndent );
    }

    public int getTabOverlay ()
    {
        return getWebUI ().getTabOverlay ();
    }

    public void setTabOverlay ( final int tabOverlay )
    {
        getWebUI ().setTabOverlay ( tabOverlay );
    }

    public TabStretchType getTabStretchType ()
    {
        return getWebUI ().getTabStretchType ();
    }

    public void setTabStretchType ( final TabStretchType tabStretchType )
    {
        getWebUI ().setTabStretchType ( tabStretchType );
    }

    public Color getTabBorderColor ()
    {
        return getWebUI ().getTabBorderColor ();
    }

    public void setTabBorderColor ( final Color tabBorderColor )
    {
        getWebUI ().setTabBorderColor ( tabBorderColor );
    }

    public Color getContentBorderColor ()
    {
        return getWebUI ().getContentBorderColor ();
    }

    public void setContentBorderColor ( final Color contentBorderColor )
    {
        getWebUI ().setContentBorderColor ( contentBorderColor );
    }

    public boolean isPaintBorderOnlyOnSelectedTab ()
    {
        return getWebUI ().isPaintBorderOnlyOnSelectedTab ();
    }

    public void setPaintBorderOnlyOnSelectedTab ( final boolean paintBorderOnlyOnSelectedTab )
    {
        getWebUI ().setPaintBorderOnlyOnSelectedTab ( paintBorderOnlyOnSelectedTab );
    }

    public boolean isForceUseSelectedTabBgColors ()
    {
        return getWebUI ().isForceUseSelectedTabBgColors ();
    }

    public void setForceUseSelectedTabBgColors ( final boolean forceUseSelectedTabBgColors )
    {
        getWebUI ().setForceUseSelectedTabBgColors ( forceUseSelectedTabBgColors );
    }

    public Color getBackgroundColor ()
    {
        return getWebUI ().getBackgroundColor ();
    }

    public void setBackgroundColor ( final Color backgroundColor )
    {
        getWebUI ().setBackgroundColor ( backgroundColor );
    }

    public boolean isPaintOnlyTopBorder ()
    {
        return getWebUI ().isPaintOnlyTopBorder ();
    }

    public void setPaintOnlyTopBorder ( final boolean paintOnlyTopBorder )
    {
        getWebUI ().setPaintOnlyTopBorder ( paintOnlyTopBorder );
    }

    @Override
    public Shape provideShape ()
    {
        return getWebUI ().provideShape ();
    }

    public WebTabbedPaneUI getWebUI ()
    {
        return ( WebTabbedPaneUI ) getUI ();
    }

    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebTabbedPaneUI ) )
        {
            try
            {
                setUI ( ( WebTabbedPaneUI ) ReflectUtils.createInstance ( WebLookAndFeel.tabbedPaneUI ) );
            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebTabbedPaneUI () );
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
    public WebTabbedPane setPlainFont ()
    {
        return SwingUtils.setPlainFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTabbedPane setPlainFont ( final boolean apply )
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
    public WebTabbedPane setBoldFont ()
    {
        return SwingUtils.setBoldFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTabbedPane setBoldFont ( final boolean apply )
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
    public WebTabbedPane setItalicFont ()
    {
        return SwingUtils.setItalicFont ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTabbedPane setItalicFont ( final boolean apply )
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
    public WebTabbedPane setFontStyle ( final boolean bold, final boolean italic )
    {
        return SwingUtils.setFontStyle ( this, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTabbedPane setFontStyle ( final int style )
    {
        return SwingUtils.setFontStyle ( this, style );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTabbedPane setFontSize ( final int fontSize )
    {
        return SwingUtils.setFontSize ( this, fontSize );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTabbedPane changeFontSize ( final int change )
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
    public WebTabbedPane setFontSizeAndStyle ( final int fontSize, final boolean bold, final boolean italic )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTabbedPane setFontSizeAndStyle ( final int fontSize, final int style )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, style );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebTabbedPane setFontName ( final String fontName )
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
     * Language container methods
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLanguageContainerKey ( final String key )
    {
        LanguageManager.registerLanguageContainer ( this, key );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeLanguageContainerKey ()
    {
        LanguageManager.unregisterLanguageContainer ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLanguageContainerKey ()
    {
        return LanguageManager.getLanguageContainerKey ( this );
    }
}
