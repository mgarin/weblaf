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

import com.alee.managers.focus.DefaultFocusTracker;
import com.alee.managers.focus.FocusManager;
import com.alee.managers.language.LanguageContainerMethods;
import com.alee.managers.language.LanguageManager;
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
 * This JWindow extenstion class provides some additional methods and options to manipulate window behavior.
 *
 * @author Mikle Garin
 */

public class WebWindow extends JWindow implements LanguageContainerMethods, SettingsMethods, WindowMethods<WebWindow>
{
    /**
     * Whether should close window on focus loss or not.
     */
    protected boolean closeOnFocusLoss = false;

    /**
     * Creates a window with no specified owner. This window will not be focusable.
     * <p/>
     * This constructor sets the component's locale property to the value returned by <code>JComponent.getDefaultLocale</code>.
     */
    public WebWindow ()
    {
        super ();
        initialize ();
    }

    /**
     * Creates a window with the specified <code>GraphicsConfiguration</code> of a screen device. This window will not be focusable.
     * <p/>
     * This constructor sets the component's locale property to the value returned by <code>JComponent.getDefaultLocale</code>.
     *
     * @param gc the <code>GraphicsConfiguration</code> that is used to construct the new window with; if gc is <code>null</code>,
     *           the system default <code>GraphicsConfiguration</code> is assumed
     */
    public WebWindow ( GraphicsConfiguration gc )
    {
        super ( gc );
        initialize ();
    }

    /**
     * Creates a window with the specified owner frame.
     * If <code>owner</code> is <code>null</code>, the shared owner will be used and this window will not be focusable.
     * Also, this window will not be focusable unless its owner is showing on the screen.
     * <p/>
     * This constructor sets the component's locale property to the value returned by <code>JComponent.getDefaultLocale</code>.
     *
     * @param owner the frame from which the window is displayed
     */
    public WebWindow ( Frame owner )
    {
        super ( owner );
        initialize ();
    }

    /**
     * Creates a window with the owner window from the specified component.
     * This window will not be focusable unless its owner is showing on the screen.
     * If <code>owner</code> is <code>null</code>, the shared owner will be used and this window will not be focusable.
     * <p/>
     * This constructor sets the component's locale property to the value returned by <code>JComponent.getDefaultLocale</code>.
     *
     * @param owner the componnt from which parent window this window is displayed
     */
    public WebWindow ( Component owner )
    {
        super ( SwingUtils.getWindowAncestor ( owner ) );
        initialize ();
    }

    /**
     * Creates a window with the specified owner window. This window will not be focusable unless its owner is showing on the screen.
     * If <code>owner</code> is <code>null</code>, the shared owner will be used and this window will not be focusable.
     * <p/>
     * This constructor sets the component's locale property to the value returned by <code>JComponent.getDefaultLocale</code>.
     *
     * @param owner the window from which the window is displayed
     */
    public WebWindow ( Window owner )
    {
        super ( owner );
        initialize ();
    }

    /**
     * Creates a window with the specified owner window and <code>GraphicsConfiguration</code> of a screen device.
     * If <code>owner</code> is <code>null</code>, the shared owner will be used and this window will not be focusable.
     * <p/>
     * This constructor sets the component's locale property to the value returned by <code>JComponent.getDefaultLocale</code>.
     *
     * @param owner the window from which the window is displayed
     * @param gc    the <code>GraphicsConfiguration</code> that is used to construct the new window with; if gc is <code>null</code>,
     *              the system default <code>GraphicsConfiguration</code> is assumed, unless <code>owner</code> is also null, in which
     *              case the <code>GraphicsConfiguration</code> from the shared owner frame will be used
     */
    public WebWindow ( Window owner, GraphicsConfiguration gc )
    {
        super ( owner, gc );
        initialize ();
    }

    /**
     * Additional initializtion of WebWindow settings.
     */
    protected void initialize ()
    {
        setFocusable ( true );
        SwingUtils.setOrientation ( this );
        FocusManager.addFocusTracker ( this, new DefaultFocusTracker ( true )
        {
            @Override
            public boolean isTrackingEnabled ()
            {
                return closeOnFocusLoss;
            }

            @Override
            public void focusChanged ( boolean focused )
            {
                if ( closeOnFocusLoss && WebWindow.this.isShowing () && !focused )
                {
                    setVisible ( false );
                }
            }
        } );
    }

    /**
     * Returns whether should close window on focus loss or not.
     *
     * @return true if should close window on focus loss, false otherwise
     */
    public boolean isCloseOnFocusLoss ()
    {
        return closeOnFocusLoss;
    }

    /**
     * Sets whether should close window on focus loss or not.
     *
     * @param closeOnFocusLoss whether should close window on focus loss or not
     */
    public void setCloseOnFocusLoss ( boolean closeOnFocusLoss )
    {
        this.closeOnFocusLoss = closeOnFocusLoss;
    }

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
     * {@inheritDoc}
     */
    @Override
    public WebWindow setWindowOpaque ( boolean opaque )
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
    public WebWindow setWindowOpacity ( float opacity )
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
    public WebWindow center ()
    {
        return WindowUtils.center ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebWindow center ( Component relativeTo )
    {
        return WindowUtils.center ( this, relativeTo );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebWindow center ( int width, int height )
    {
        return WindowUtils.center ( this, width, height );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebWindow center ( Component relativeTo, int width, int height )
    {
        return WindowUtils.center ( this, relativeTo, width, height );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebWindow packToWidth ( int width )
    {
        return WindowUtils.packToWidth ( this, width );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebWindow packToHeight ( int height )
    {
        return WindowUtils.packToHeight ( this, height );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebWindow packAndCenter ()
    {
        return WindowUtils.packAndCenter ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebWindow packAndCenter ( boolean animate )
    {
        return WindowUtils.packAndCenter ( this, animate );
    }
}