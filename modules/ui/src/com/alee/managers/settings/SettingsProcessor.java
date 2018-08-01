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

import com.alee.laf.WebLookAndFeel;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.Serializable;

/**
 * Abstract class that tracks {@link JComponent} to load and save its settings on demand.
 * Implementations of this class are instantiated for each {@link JComponent} registered within {@link UISettingsManager}.
 * Extend this class and register it in {@link UISettingsManager} to enable automated settings support additional {@link JComponent} types.
 *
 * {@link SettingsProcessor} is defended from recursive settings load and save which might occur if {@link JComponent} sends additional
 * settings change events while new settings are being loaded into it (either from {@link SettingsProcessor} itself or some other source).
 *
 * To register new {@link SettingsProcessor} use {@link UISettingsManager#registerSettingsProcessor(Class, Class)} method.
 * This will enable automated use of that {@link SettingsProcessor} for specified {@link JComponent} type.
 * There are also additional options to register {@link SettingsProcessor} for specific {@link JComponent} in {@link UISettingsManager}.
 *
 * @param <C> {@link JComponent} type
 * @param <V> {@link Serializable} value type
 * @param <K> {@link Configuration} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see UISettingsManager
 * @see SettingsManager
 */
public abstract class SettingsProcessor<C extends JComponent, V extends Serializable, K extends Configuration<V>>
        implements Serializable
{
    /**
     * todo 1. Separate initialization into install & destruction into uninstall methods
     * todo 2. Enclose {@link #loadSettings()} and {@link #saveSettings(Serializable)} and pass into abstract methods for convenient use
     */

    /**
     * {@link JComponent} which settings are being managed.
     */
    protected C component;

    /**
     * {@link Configuration} for this {@link SettingsProcessor}.
     */
    protected K configuration;

    /**
     * Whether this settings processor is currently loading settings or not.
     */
    protected transient boolean loading = false;

    /**
     * Whether this settings processor is currently saving settings or not.
     */
    protected transient boolean saving = false;

    /**
     * Constructs new {@link SettingsProcessor} for the specified {@link JComponent} and {@link Configuration}.
     *
     * @param component     {@link JComponent} which settings are being managed
     * @param configuration {@link Configuration}
     */
    public SettingsProcessor ( final C component, final K configuration )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Component and configuration
        this.component = component;
        this.configuration = configuration;

        // Performing initial settings load
        loadInitialSettings ();

        // Initializing processor settings
        initialize ();
    }

    /**
     * Loads initial settings if provided {@link Configuration} dictates so.
     */
    protected void loadInitialSettings ()
    {
        if ( configuration.isLoadInitialSettings () )
        {
            load ();
        }
    }

    /**
     * Initializes {@link SettingsProcessor} settings.
     */
    protected void initialize ()
    {
        try
        {
            register ( component () );
        }
        catch ( final Exception e )
        {
            final String msg = "Unable to initialize specific processor settings for component " +
                    "with group '%s' and key '%s' due to unexpected exception";
            final String fmsg = String.format ( msg, configuration.group (), configuration.key () );
            LoggerFactory.getLogger ( SettingsProcessor.class ).error ( fmsg, e );
        }
    }

    /**
     * Called when {@link JComponent} is registered in {@link UISettingsManager} and this {@link SettingsProcessor} is attached to it.
     *
     * @param component {@link JComponent} to register this {@link SettingsProcessor} for
     */
    protected abstract void register ( C component );

    /**
     * Destroys this SettingsProcessor.
     */
    public final void destroy ()
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Unegistering specific processor settings
        try
        {
            unregister ( component () );
        }
        catch ( final Exception e )
        {
            final String msg = "Unable to unregister specific processor settings for component " +
                    "with group '%s' and key '%s' due to unexpected exception";
            final String fmsg = String.format ( msg, configuration.group (), configuration.key () );
            LoggerFactory.getLogger ( SettingsProcessor.class ).error ( fmsg, e );
        }

        // Component and configuration
        this.configuration = null;
        this.component = null;
    }

    /**
     * Called when {@link JComponent} is unregistered from {@link UISettingsManager} and this {@link SettingsProcessor} is detached from it.
     *
     * @param component {@link JComponent} to unregister this {@link SettingsProcessor} for
     */
    protected abstract void unregister ( C component );

    /**
     * Returns {@link JComponent} which settings are being managed.
     *
     * @return {@link JComponent} which settings are being managed
     */
    public C component ()
    {
        return component;
    }

    /**
     * Returns {@link Configuration} for this {@link SettingsProcessor}.
     *
     * @return {@link Configuration} for this {@link SettingsProcessor}
     */
    public K configuration ()
    {
        return configuration;
    }

    /**
     * Returns default value for {@link JComponent} which settings are being managed.
     * This method can return default value provided within settings registration, default {@link SettingsProcessor} value or {@code null}.
     *
     * @return default value for {@link JComponent} which settings are being managed
     */
    public final V defaultValue ()
    {
        V defaultValue = configuration.defaultValue ();
        if ( defaultValue == null )
        {
            defaultValue = createDefaultValue ();
        }
        return defaultValue;
    }

    /**
     * Returns default value for {@link JComponent} provided by {@link SettingsProcessor} implementation.
     *
     * @return default value for {@link JComponent} provided by {@link SettingsProcessor} implementation
     */
    protected V createDefaultValue ()
    {
        return null;
    }

    /**
     * Loads saved settings for the {@link JComponent}.
     * Must always be performed on Swing Event Dispatch Thread.
     */
    public final void load ()
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Ignore load if its save or load already running
        if ( loading || saving )
        {
            return;
        }

        // Load settings
        try
        {
            loading = true;
            loadSettings ( component () );
        }
        catch ( final Exception e )
        {
            final String msg = "Unable to load component settings for group '%s' and key '%s' due to unexpected exception";
            final String fmsg = String.format ( msg, configuration.group (), configuration.key () );
            LoggerFactory.getLogger ( SettingsProcessor.class ).error ( fmsg, e );
        }
        finally
        {
            loading = false;
        }
    }

    /**
     * Loads previously stored settings into the specified {@link JComponent}.
     * To load actual previously stored settings call {@link #loadSettings()} method.
     *
     * @param component {@link JComponent} to load value for
     * @see #loadSettings()
     */
    protected abstract void loadSettings ( C component );

    /**
     * Loads and returns saved {@link JComponent} settings.
     *
     * @return loaded {@link JComponent} settings
     */
    protected V loadSettings ()
    {
        final K configuration = configuration ();
        return SettingsManager.get ( configuration.group (), configuration.key (), defaultValue () );
    }

    /**
     * Saves settings taken from {@link JComponent}.
     * This method might be called from the {@link JComponent} listeners to provide auto-save functionality.
     */
    public final void save ()
    {
        save ( true );
    }

    /**
     * Saves settings taken from {@link JComponent}.
     * This method might be called from the {@link JComponent} listeners to provide auto-save functionality.
     *
     * @param onChange whether this save is called from {@link JComponent} change listeners
     */
    public final void save ( final boolean onChange )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

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
        try
        {
            saving = true;
            saveSettings ( component () );
        }
        catch ( final Exception e )
        {
            final String msg = "Unable to save component settings for group '%s' and key '%s' due to unexpected exception";
            final String fmsg = String.format ( msg, configuration.group (), configuration.key () );
            LoggerFactory.getLogger ( SettingsProcessor.class ).error ( fmsg, e );
        }
        finally
        {
            saving = false;
        }
    }

    /**
     * Saves current settings of the specified {@link JComponent}.
     * To save actual retrieved settings call {@link #saveSettings(java.io.Serializable)} method.
     *
     * @param component {@link JComponent} to save settings for
     * @see #saveSettings(java.io.Serializable)
     */
    protected abstract void saveSettings ( C component );

    /**
     * Saves {@link JComponent} settings.
     *
     * @param value new {@link JComponent} settings
     */
    protected void saveSettings ( final V value )
    {
        final K configuration = configuration ();
        SettingsManager.set ( configuration.group (), configuration.key (), value );
    }
}