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

package com.alee.managers.settings;

import com.alee.utils.swing.SwingMethods;

/**
 * This interface provides a set of methods that should be added into components which are supported by SettingsManager.
 * Basically all these methods are already implemented in SettingsManager but it is much easier to call them directly from component.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see com.alee.managers.settings.SettingsManager
 * @see com.alee.utils.swing.SwingMethods
 */

public interface SettingsMethods extends SwingMethods
{
    /**
     * Registers component for settings auto-save.
     * <p>
     * Also registered component will be:
     * - listened for settings changes to save them when requested
     * - automatically updated with any loaded settings for that key if requested
     * - automatically updated with any changes made in its settings if requested
     *
     * @param key component settings key
     */
    public void registerSettings ( String key );

    /**
     * Registers component for settings auto-save.
     * <p>
     * Also registered component will be:
     * - listened for settings changes to save them when requested
     * - automatically updated with any loaded settings for that key if requested
     * - automatically updated with any changes made in its settings if requested
     *
     * @param key               component settings key
     * @param defaultValueClass component default value class
     * @param <T>               default value type
     * @see DefaultValue
     */
    public <T extends DefaultValue> void registerSettings ( String key, Class<T> defaultValueClass );

    /**
     * Registers component for settings auto-save.
     * <p>
     * Also registered component will be:
     * - listened for settings changes to save them when requested
     * - automatically updated with any loaded settings for that key if requested
     * - automatically updated with any changes made in its settings if requested
     *
     * @param key          component settings key
     * @param defaultValue component default value
     */
    public void registerSettings ( String key, Object defaultValue );

    /**
     * Registers component for settings auto-save.
     * <p>
     * Also registered component will be:
     * - listened for settings changes to save them when requested
     * - automatically updated with any loaded settings for that key if requested
     * - automatically updated with any changes made in its settings if requested
     *
     * @param group             component settings group
     * @param key               component settings key
     * @param defaultValueClass component default value class
     * @param <T>               default value type
     * @see DefaultValue
     */
    public <T extends DefaultValue> void registerSettings ( String group, String key, Class<T> defaultValueClass );

    /**
     * Registers component for settings auto-save.
     * <p>
     * Also registered component will be:
     * - listened for settings changes to save them when requested
     * - automatically updated with any loaded settings for that key if requested
     * - automatically updated with any changes made in its settings if requested
     *
     * @param group        component settings group
     * @param key          component settings key
     * @param defaultValue component default value
     */
    public void registerSettings ( String group, String key, Object defaultValue );

    /**
     * Registers component for settings auto-save.
     * <p>
     * Also registered component will be:
     * - listened for settings changes to save them when requested
     * - automatically updated with any loaded settings for that key if requested
     * - automatically updated with any changes made in its settings if requested
     *
     * @param group component settings group
     * @param key   component settings key
     */
    public void registerSettings ( String group, String key );

    /**
     * Registers component for settings auto-save.
     * <p>
     * Also registered component will be:
     * - listened for settings changes to save them when requested
     * - automatically updated with any loaded settings for that key if requested
     * - automatically updated with any changes made in its settings if requested
     *
     * @param key                  component settings key
     * @param loadInitialSettings  whether to load initial available settings into the component or not
     * @param applySettingsChanges whether to apply settings changes to the component or not
     */
    public void registerSettings ( String key, boolean loadInitialSettings, boolean applySettingsChanges );

    /**
     * Registers component for settings auto-save.
     * <p>
     * Also registered component will be:
     * - listened for settings changes to save them when requested
     * - automatically updated with any loaded settings for that key if requested
     * - automatically updated with any changes made in its settings if requested
     *
     * @param key                  component settings key
     * @param defaultValueClass    component default value class
     * @param loadInitialSettings  whether to load initial available settings into the component or not
     * @param applySettingsChanges whether to apply settings changes to the component or not
     * @param <T>                  default value type
     * @see DefaultValue
     */
    public <T extends DefaultValue> void registerSettings ( String key, Class<T> defaultValueClass, boolean loadInitialSettings,
                                                            boolean applySettingsChanges );

    /**
     * Registers component for settings auto-save.
     * <p>
     * Also registered component will be:
     * - listened for settings changes to save them when requested
     * - automatically updated with any loaded settings for that key if requested
     * - automatically updated with any changes made in its settings if requested
     *
     * @param key                  component settings key
     * @param defaultValue         component default value
     * @param loadInitialSettings  whether to load initial available settings into the component or not
     * @param applySettingsChanges whether to apply settings changes to the component or not
     */
    public void registerSettings ( String key, Object defaultValue, boolean loadInitialSettings, boolean applySettingsChanges );

    /**
     * Registers component for settings auto-save.
     * <p>
     * Also registered component will be:
     * - listened for settings changes to save them when requested
     * - automatically updated with any loaded settings for that key if requested
     * - automatically updated with any changes made in its settings if requested
     *
     * @param group                component settings group
     * @param key                  component settings key
     * @param defaultValueClass    component default value class
     * @param loadInitialSettings  whether to load initial available settings into the component or not
     * @param applySettingsChanges whether to apply settings changes to the component or not
     * @param <T>                  default value type
     * @see DefaultValue
     */
    public <T extends DefaultValue> void registerSettings ( String group, String key, Class<T> defaultValueClass,
                                                            boolean loadInitialSettings, boolean applySettingsChanges );

    /**
     * Registers component for settings auto-save.
     * <p>
     * Also registered component will be:
     * - listened for settings changes to save them when requested
     * - automatically updated with any loaded settings for that key if requested
     * - automatically updated with any changes made in its settings if requested
     *
     * @param group                component settings group
     * @param key                  component settings key
     * @param defaultValue         component default value
     * @param loadInitialSettings  whether to load initial available settings into the component or not
     * @param applySettingsChanges whether to apply settings changes to the component or not
     */
    public void registerSettings ( String group, String key, Object defaultValue, boolean loadInitialSettings,
                                   boolean applySettingsChanges );

    /**
     * Registers component for settings auto-save.
     * <p>
     * Also registered component will be:
     * - listened for settings changes to save them when requested
     * - automatically updated with any loaded settings for that key if requested
     * - automatically updated with any changes made in its settings if requested
     *
     * @param settingsProcessor component settings processor
     */
    public void registerSettings ( SettingsProcessor settingsProcessor );

    /**
     * Unregisters component from settings auto-save.
     */
    public void unregisterSettings ();

    /**
     * Loads saved settings into the component if it is registered.
     */
    public void loadSettings ();

    /**
     * Saves component settings.
     */
    public void saveSettings ();
}