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

import com.alee.managers.log.Log;

import javax.swing.*;
import java.io.Serializable;

/**
 * This abstract class represents core component settings tracking functionality.
 * Extend and register it in SettingsManager or ComponentSettingsManager to provide additional components support.
 * <p>
 * SettingsProcessor is also defended from recursive settings load/save which might occur if component sends additional data change events
 * when new data is loaded into it (doesn't matter from SettingsProcessor or some other source).
 * <p>
 * To register new SettingsProcessor use {@code registerSettingsProcessor(Class, Class)} method from SettingsManager or
 * ComponentSettingsManager class (they both do the same).
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see com.alee.managers.settings.SettingsManager
 * @see com.alee.managers.settings.SettingsManager#registerSettingsProcessor(Class, Class)
 * @see com.alee.managers.settings.ComponentSettingsManager
 * @see com.alee.managers.settings.ComponentSettingsManager#registerSettingsProcessor(Class, Class)
 */
public abstract class SettingsProcessor<C extends JComponent, V extends Serializable>
{
    /**
     * Whether this settings processor is currently loading settings or not.
     */
    protected boolean loading = false;

    /**
     * Whether this settings processor is currently saving settings or not.
     */
    protected boolean saving = false;

    /**
     * Settings processor data.
     */
    protected SettingsProcessorData data;

    /**
     * Constructs SettingsProcessor using the specified data.
     *
     * @param component            component which settings are being managed
     * @param group                component settings group
     * @param key                  component settings key
     * @param defaultValue         component default value
     * @param loadInitialSettings  whether to load initial available settings into the component or not
     * @param applySettingsChanges whether to apply settings changes to the component or not
     */
    public SettingsProcessor ( final JComponent component, final String group, final String key, final Object defaultValue,
                               final boolean loadInitialSettings, final boolean applySettingsChanges )
    {
        this ( new SettingsProcessorData ( component, group, key, defaultValue, loadInitialSettings, applySettingsChanges ) );
    }

    /**
     * Constructs SettingsProcessor using the specified SettingsProcessorData.
     *
     * @param data SettingsProcessorData
     */
    public SettingsProcessor ( final SettingsProcessorData data )
    {
        super ();

        // SettingsProcessor data
        this.data = data;

        // Performing initial settings load
        if ( data.isLoadInitialSettings () )
        {
            try
            {
                load ();
            }
            catch ( final Throwable e )
            {
                if ( SettingsManager.isLoggingEnabled () )
                {
                    Log.error ( this, "Unable to load initial component settings for group \"" + data.getGroup () +
                            "\" and key \"" + data.getKey () + "\" due to unexpected exception:", e );
                }
            }
        }

        // Initializing specific processor settings
        try
        {
            doInit ( getComponent () );
        }
        catch ( final Throwable e )
        {
            if ( SettingsManager.isLoggingEnabled () )
            {
                Log.error ( this, "Unable to initialize specific processor settings for component with group \"" +
                        data.getGroup () + "\" and key \"" + data.getKey () + "\" due to unexpected exception:", e );
            }
        }

        // Apply settings changes to the component
        if ( data.isApplySettingsChanges () )
        {
            SettingsManager.addSettingsListener ( getGroup (), getKey (), new SettingsListener ()
            {
                @Override
                public void settingsChanged ( final String group, final String key, final Object newValue )
                {
                    load ();
                }
            } );
        }
    }

    /**
     * Returns SettingsProcessorData.
     *
     * @return SettingsProcessorData
     */
    public SettingsProcessorData getData ()
    {
        return data;
    }

    /**
     * Returns managed component.
     *
     * @return managed component
     */
    public C getComponent ()
    {
        return ( C ) data.getComponent ();
    }

    /**
     * Returns component settings group.
     *
     * @return component settings group
     */
    public String getGroup ()
    {
        return data.getGroup ();
    }

    /**
     * Returns component settings key.
     *
     * @return component settings key
     */
    public String getKey ()
    {
        return data.getKey ();
    }

    /**
     * Returns component default value.
     *
     * @return component default value
     */
    public V getDefaultValue ()
    {
        return ( V ) data.getDefaultValue ();
    }

    /**
     * Loads saved settings into the component.
     */
    public final void load ()
    {
        // Ignore load if its save or load already running
        if ( loading || saving )
        {
            return;
        }

        // Load settings
        loading = true;
        doLoad ( getComponent () );
        loading = false;
    }

    /**
     * Saves settings taken from the component.
     * This method might be called from the component listeners to provide auto-save functionality.
     */
    public final void save ()
    {
        save ( true );
    }

    /**
     * Saves settings taken from the component.
     * This method might be called from the component listeners to provide auto-save functionality.
     *
     * @param onChange whether this save is called from component change listeners
     */
    public final void save ( final boolean onChange )
    {
        // Ignore this call if save-on-change is disabled
        if ( onChange && !SettingsManager.isSaveOnChange () )
        {
            return;
        }

        // Ignore save if its save or load already running
        if ( loading || saving )
        {
            return;
        }

        // Save settings
        saving = true;
        doSave ( getComponent () );
        saving = false;
    }

    /**
     * Destroys this SettingsProcessor.
     */
    public final void destroy ()
    {
        doDestroy ( getComponent () );
        this.data = null;
    }

    /**
     * Loads and returns saved component settings.
     *
     * @return loaded settings
     */
    protected V loadValue ()
    {
        return SettingsManager.get ( getGroup (), getKey (), getDefaultValue () );
    }

    /**
     * Saves component settings.
     *
     * @param value new component settings
     */
    protected void saveValue ( final V value )
    {
        SettingsManager.set ( getGroup (), getKey (), value );
    }

    /**
     * Called when a new component is registered in ComponentSettingsManager.
     *
     * @param component registered component
     */
    protected abstract void doInit ( C component );

    /**
     * Called when component is unregistered from ComponentSettingsManager.
     *
     * @param component unregistered component
     */
    protected abstract void doDestroy ( C component );

    /**
     * Called on component settings load.
     * It doesn't matter if it is invoked by SettingsProcessor or some other source.
     *
     * @param component component to load settings into
     */
    protected abstract void doLoad ( C component );

    /**
     * Called on component settings save.
     * It doesn't matter if it is invoked by SettingsProcessor or some other source.
     *
     * @param component component to save settings from
     */
    protected abstract void doSave ( C component );
}