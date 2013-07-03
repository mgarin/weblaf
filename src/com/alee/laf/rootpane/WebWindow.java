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

import com.alee.managers.focus.FocusManager;
import com.alee.managers.focus.FocusTracker;
import com.alee.managers.language.LanguageContainer;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.settings.DefaultValue;
import com.alee.managers.settings.SettingsManager;
import com.alee.managers.settings.SettingsMethods;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


/**
 * User: mgarin Date: 26.06.12 Time: 16:48
 */

public class WebWindow extends JWindow implements FocusTracker, LanguageContainer, SettingsMethods
{
    private boolean closeOnFocusLoss = false;

    protected boolean focused;

    public WebWindow ()
    {
        super ();
        initialize ();
    }

    public WebWindow ( GraphicsConfiguration gc )
    {
        super ( gc );
        initialize ();
    }

    public WebWindow ( Frame owner )
    {
        super ( owner );
        initialize ();
    }

    public WebWindow ( Component owner )
    {
        super ( SwingUtils.getWindowAncestor ( owner ) );
        initialize ();
    }

    public WebWindow ( Window owner )
    {
        super ( owner );
        initialize ();
    }

    public WebWindow ( Window owner, GraphicsConfiguration gc )
    {
        super ( owner, gc );
        initialize ();
    }

    private void initialize ()
    {
        setFocusable ( true );
        SwingUtils.setOrientation ( this );
        addComponentListener ( new ComponentAdapter ()
        {
            public void componentShown ( ComponentEvent e )
            {
                updateFocusTracker ();
            }

            public void componentHidden ( ComponentEvent e )
            {
                updateFocusTracker ();
            }
        } );
    }

    /**
     * Should this window close on focus loss or not
     */

    public boolean isCloseOnFocusLoss ()
    {
        return closeOnFocusLoss;
    }

    public void setCloseOnFocusLoss ( boolean closeOnFocusLoss )
    {
        this.closeOnFocusLoss = closeOnFocusLoss;
        updateFocusTracker ();
    }

    /**
     * Focus tracker implemented methods
     */

    public boolean isTrackingEnabled ()
    {
        return isShowing ();
    }

    public Component getTrackedComponent ()
    {
        return this;
    }

    public boolean isUniteWithChilds ()
    {
        return true;
    }

    public boolean isListenGlobalChange ()
    {
        return false;
    }

    public void focusChanged ( boolean focused )
    {
        this.focused = focused;
        if ( WebWindow.this.isShowing () && !focused && closeOnFocusLoss )
        {
            setVisible ( false );
        }
    }

    private void updateFocusTracker ()
    {
        if ( isShowing () && closeOnFocusLoss )
        {
            FocusManager.registerFocusTracker ( this );
        }
        else
        {
            FocusManager.unregisterFocusTracker ( this );
        }
    }

    /**
     * Additional methods
     */

    public void setWindowOpaque ( boolean opaque )
    {
        SwingUtils.setWindowOpaque ( this, opaque );
    }

    public boolean isWindowOpaque ()
    {
        return SwingUtils.isWindowOpaque ( this );
    }

    public void setWindowOpacity ( float opacity )
    {
        SwingUtils.setWindowOpacity ( this, opacity );
    }

    public float getWindowOpacity ()
    {
        return SwingUtils.getWindowOpacity ( this );
    }

    public void packAndCenter ()
    {
        SwingUtils.packAndCenter ( this );
    }

    public void packAndCenter ( boolean animate )
    {
        SwingUtils.packAndCenter ( this, animate );
    }

    public void center ()
    {
        setLocationRelativeTo ( null );
    }

    public void center ( Component relativeTo )
    {
        setLocationRelativeTo ( relativeTo );
    }

    public void center ( int width, int height )
    {
        setSize ( width, height );
        center ();
    }

    public void packToWidth ( int width )
    {
        setSize ( width, getPreferredSize ().height );
    }

    public void packToHeight ( int height )
    {
        setSize ( getPreferredSize ().width, height );
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
}