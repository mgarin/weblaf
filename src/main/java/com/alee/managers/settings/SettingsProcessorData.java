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

import java.awt.*;

/**
 * Base data class for any SettingsProcessor.
 *
 * @author Mikle Garin
 * @see SettingsProcessor
 * @see com.alee.managers.settings.ComponentSettingsManager
 * @see com.alee.managers.settings.SettingsManager
 * @since 1.4
 */

public class SettingsProcessorData
{
    /**
     * Component which settings are being managed.
     */
    private Component component;

    /**
     * Component settings group.
     */
    private String group;

    /**
     * Component settings key.
     */
    private String key;

    /**
     * Component default value.
     */
    private Object defaultValue;

    /**
     * Whether to load initial available settings into the component or not.
     */
    private boolean loadInitialSettings;

    /**
     * Whether to apply settings changes to the component or not.
     */
    private boolean applySettingsChanges;

    /**
     * Constructs empty SettingsProcessorData.
     */
    public SettingsProcessorData ()
    {
        super ();
    }

    /**
     * Constructs SettingsProcessorData with the specified data.
     *
     * @param component            component which settings are being managed
     * @param group                component settings group
     * @param key                  component settings key
     * @param defaultValue         component default value
     * @param loadInitialSettings  whether to load initial available settings into the component or not
     * @param applySettingsChanges whether to apply settings changes to the component or not
     */
    public SettingsProcessorData ( Component component, String group, String key, Object defaultValue, boolean loadInitialSettings,
                                   boolean applySettingsChanges )
    {
        super ();
        setComponent ( component );
        setGroup ( group );
        setKey ( key );
        setDefaultValue ( defaultValue );
        setLoadInitialSettings ( loadInitialSettings );
        setApplySettingsChanges ( applySettingsChanges );
    }

    /**
     * Returns component which settings are being managed.
     *
     * @return component which settings are being managed
     */
    public Component getComponent ()
    {
        return component;
    }

    /**
     * Sets component which settings are being managed.
     *
     * @param component component which settings are being managed
     */
    public void setComponent ( Component component )
    {
        this.component = component;
    }

    /**
     * Returns component settings group.
     *
     * @return component settings group
     */
    public String getGroup ()
    {
        return group;
    }

    /**
     * Sets component settings group.
     *
     * @param group component settings group
     */
    public void setGroup ( String group )
    {
        this.group = group;
    }

    /**
     * Returns component settings key.
     *
     * @return component settings key
     */
    public String getKey ()
    {
        return key;
    }

    /**
     * Sets component settings key.
     *
     * @param key component settings key
     */
    public void setKey ( String key )
    {
        this.key = key;
    }

    /**
     * Returns component default value.
     *
     * @return component default value
     */
    public Object getDefaultValue ()
    {
        return defaultValue;
    }

    /**
     * Sets component default value.
     *
     * @param defaultValue component default value
     */
    public void setDefaultValue ( Object defaultValue )
    {
        this.defaultValue = defaultValue;
    }

    /**
     * Returns whether initial available settings should be loaded into the component or not.
     *
     * @return true if initial available settings should be loaded into the component, false otherwise
     */
    public boolean isLoadInitialSettings ()
    {
        return loadInitialSettings;
    }

    /**
     * Sets whether to load initial available settings into the component or not.
     *
     * @param loadInitialSettings whether to load initial available settings into the component or not
     */
    public void setLoadInitialSettings ( boolean loadInitialSettings )
    {
        this.loadInitialSettings = loadInitialSettings;
    }

    /**
     * Returns whether settings changes should be applied to the component or not.
     *
     * @return true if settings changes should be applied to the component, false otherwise
     */
    public boolean isApplySettingsChanges ()
    {
        return applySettingsChanges;
    }

    /**
     * Sets whether to apply settings changes to the component or not.
     *
     * @param applySettingsChanges whether to apply settings changes to the component or not
     */
    public void setApplySettingsChanges ( boolean applySettingsChanges )
    {
        this.applySettingsChanges = applySettingsChanges;
    }
}