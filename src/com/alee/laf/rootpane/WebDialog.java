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

package com.alee.laf.rootpane;

import com.alee.extended.panel.WebButtonGroup;
import com.alee.managers.language.LanguageContainerMethods;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.LanguageMethods;
import com.alee.managers.language.updaters.LanguageUpdater;
import com.alee.managers.settings.DefaultValue;
import com.alee.managers.settings.SettingsManager;
import com.alee.managers.settings.SettingsMethods;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.utils.SwingUtils;
import com.alee.utils.WindowUtils;
import com.alee.utils.swing.WindowMethods;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 11.12.12 Time: 14:51
 */

public class WebDialog extends JDialog implements LanguageMethods, LanguageContainerMethods, SettingsMethods, WindowMethods<WebDialog>
{
    public WebDialog ()
    {
        super ();
    }

    public WebDialog ( Frame owner )
    {
        super ( owner );
    }

    public WebDialog ( Frame owner, boolean modal )
    {
        super ( owner, modal );
    }

    public WebDialog ( Frame owner, String title )
    {
        super ( owner, title );
    }

    public WebDialog ( Frame owner, String title, boolean modal )
    {
        super ( owner, title, modal );
    }

    public WebDialog ( Frame owner, String title, boolean modal, GraphicsConfiguration gc )
    {
        super ( owner, title, modal, gc );
    }

    public WebDialog ( Dialog owner )
    {
        super ( owner );
    }

    public WebDialog ( Dialog owner, boolean modal )
    {
        super ( owner, modal );
    }

    public WebDialog ( Dialog owner, String title )
    {
        super ( owner, title );
    }

    public WebDialog ( Dialog owner, String title, boolean modal )
    {
        super ( owner, title, modal );
    }

    public WebDialog ( Dialog owner, String title, boolean modal, GraphicsConfiguration gc )
    {
        super ( owner, title, modal, gc );
    }

    public WebDialog ( Component owner )
    {
        super ( SwingUtils.getWindowAncestor ( owner ) );
    }

    public WebDialog ( Window owner )
    {
        super ( owner );
    }

    public WebDialog ( Window owner, ModalityType modalityType )
    {
        super ( owner, modalityType );
    }

    public WebDialog ( Window owner, String title )
    {
        super ( owner, title );
    }

    public WebDialog ( Window owner, String title, ModalityType modalityType )
    {
        super ( owner, title, modalityType );
    }

    public WebDialog ( Window owner, String title, ModalityType modalityType, GraphicsConfiguration gc )
    {
        super ( owner, title, modalityType, gc );
    }

    public Color getTopBg ()
    {
        return getWebRootPaneUI ().getTopBg ();
    }

    public void setTopBg ( Color topBg )
    {
        getWebRootPaneUI ().setTopBg ( topBg );
    }

    public Color getMiddleBg ()
    {
        return getWebRootPaneUI ().getMiddleBg ();
    }

    public void setMiddleBg ( Color middleBg )
    {
        getWebRootPaneUI ().setMiddleBg ( middleBg );
    }

    public int getShadeWidth ()
    {
        return getWebRootPaneUI ().getShadeWidth ();
    }

    public void setShadeWidth ( int shadeWidth )
    {
        getWebRootPaneUI ().setShadeWidth ( shadeWidth );
    }

    public int getInactiveShadeWidth ()
    {
        return getWebRootPaneUI ().getInactiveShadeWidth ();
    }

    public void setInactiveShadeWidth ( int inactiveShadeWidth )
    {
        getWebRootPaneUI ().setInactiveShadeWidth ( inactiveShadeWidth );
    }

    public int getRound ()
    {
        return getWebRootPaneUI ().getRound ();
    }

    public void setRound ( int round )
    {
        getWebRootPaneUI ().setRound ( round );
    }

    public boolean isDrawWatermark ()
    {
        return getWebRootPaneUI ().isDrawWatermark ();
    }

    public void setDrawWatermark ( boolean drawWatermark )
    {
        getWebRootPaneUI ().setDrawWatermark ( drawWatermark );
    }

    public ImageIcon getWatermark ()
    {
        return getWebRootPaneUI ().getWatermark ();
    }

    public void setWatermark ( ImageIcon watermark )
    {
        getWebRootPaneUI ().setWatermark ( watermark );
    }

    public JComponent getTitleComponent ()
    {
        return getWebRootPaneUI ().getTitleComponent ();
    }

    public void setTitleComponent ( JComponent titleComponent )
    {
        getWebRootPaneUI ().setTitleComponent ( titleComponent );
    }

    public WebButtonGroup getWindowButtons ()
    {
        return getWebRootPaneUI ().getWindowButtons ();
    }

    public WebResizeCorner getResizeCorner ()
    {
        return getWebRootPaneUI ().getResizeCorner ();
    }

    public boolean isShowResizeCorner ()
    {
        return getWebRootPaneUI ().isShowResizeCorner ();
    }

    public void setShowResizeCorner ( boolean showResizeCorner )
    {
        getWebRootPaneUI ().setShowResizeCorner ( showResizeCorner );
    }

    public boolean isShowTitleComponent ()
    {
        return getWebRootPaneUI ().isShowTitleComponent ();
    }

    public void setShowTitleComponent ( boolean showTitleComponent )
    {
        getWebRootPaneUI ().setShowTitleComponent ( showTitleComponent );
    }

    public boolean isShowWindowButtons ()
    {
        return getWebRootPaneUI ().isShowWindowButtons ();
    }

    public void setShowWindowButtons ( boolean showWindowButtons )
    {
        getWebRootPaneUI ().setShowWindowButtons ( showWindowButtons );
    }

    public boolean isShowMinimizeButton ()
    {
        return getWebRootPaneUI ().isShowMinimizeButton ();
    }

    public void setShowMinimizeButton ( boolean showMinimizeButton )
    {
        getWebRootPaneUI ().setShowMinimizeButton ( showMinimizeButton );
    }

    public boolean isShowMaximizeButton ()
    {
        return getWebRootPaneUI ().isShowMaximizeButton ();
    }

    public void setShowMaximizeButton ( boolean showMaximizeButton )
    {
        getWebRootPaneUI ().setShowMaximizeButton ( showMaximizeButton );
    }

    public boolean isShowCloseButton ()
    {
        return getWebRootPaneUI ().isShowCloseButton ();
    }

    public void setShowCloseButton ( boolean showCloseButton )
    {
        getWebRootPaneUI ().setShowCloseButton ( showCloseButton );
    }

    public boolean isGroupButtons ()
    {
        return getWebRootPaneUI ().isGroupButtons ();
    }

    public void setGroupButtons ( boolean groupButtons )
    {
        getWebRootPaneUI ().setGroupButtons ( groupButtons );
    }

    public boolean isAttachButtons ()
    {
        return getWebRootPaneUI ().isAttachButtons ();
    }

    public void setAttachButtons ( boolean attachButtons )
    {
        getWebRootPaneUI ().setAttachButtons ( attachButtons );
    }

    public boolean isShowMenuBar ()
    {
        return getWebRootPaneUI ().isShowMenuBar ();
    }

    public void setShowMenuBar ( boolean showMenuBar )
    {
        getWebRootPaneUI ().setShowMenuBar ( showMenuBar );
    }

    public WebRootPaneUI getWebRootPaneUI ()
    {
        return ( WebRootPaneUI ) super.getRootPane ().getUI ();
    }

    /**
     * Language methods
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLanguage ( String key, Object... data )
    {
        LanguageManager.registerComponent ( this, key, data );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateLanguage ( Object... data )
    {
        LanguageManager.updateComponent ( this, data );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateLanguage ( String key, Object... data )
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
    public void setLanguageUpdater ( LanguageUpdater updater )
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
     * Language container methods
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLanguageContainerKey ( String key )
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
     * Window methods.
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public WebDialog setWindowOpaque ( boolean opaque )
    {
        return WindowUtils.setWindowOpaque ( this, opaque );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isWindowOpaque ()
    {
        return WindowUtils.isWindowOpaque ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebDialog setWindowOpacity ( float opacity )
    {
        return WindowUtils.setWindowOpacity ( this, opacity );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getWindowOpacity ()
    {
        return WindowUtils.getWindowOpacity ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebDialog center ()
    {
        return WindowUtils.center ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebDialog center ( Component relativeTo )
    {
        return WindowUtils.center ( this, relativeTo );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebDialog center ( int width, int height )
    {
        return WindowUtils.center ( this, width, height );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebDialog center ( Component relativeTo, int width, int height )
    {
        return WindowUtils.center ( this, relativeTo, width, height );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebDialog packToWidth ( int width )
    {
        return WindowUtils.packToWidth ( this, width );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebDialog packToHeight ( int height )
    {
        return WindowUtils.packToHeight ( this, height );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebDialog packAndCenter ()
    {
        return WindowUtils.packAndCenter ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebDialog packAndCenter ( boolean animate )
    {
        return WindowUtils.packAndCenter ( this, animate );
    }
}