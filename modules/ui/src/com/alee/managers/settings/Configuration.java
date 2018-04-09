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

import com.alee.api.jdk.SerializableSupplier;

import javax.swing.*;
import java.io.Serializable;

/**
 * Configuration provided for each {@link SettingsProcessor}.
 * It can be extended to provide additional settings for custom {@link SettingsProcessor} implementations.
 *
 * @param <V> {@link Serializable} value type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see SettingsProcessor
 * @see UISettingsManager
 * @see SettingsManager
 */
public class Configuration<V extends Serializable> implements Serializable
{
    /**
     * Settings group.
     */
    protected final String group;

    /**
     * Settings key.
     */
    protected final String key;

    /**
     * {@link SerializableSupplier} for default value.
     */
    protected final SerializableSupplier<V> defaultValue;

    /**
     * Whether to load initial available settings into the {@link JComponent} or not.
     */
    protected final boolean loadInitialSettings;

    /**
     * Whether to apply settings changes to the {@link JComponent} or not.
     */
    protected final boolean applySettingsChanges;

    /**
     * Constructs new {@link Configuration}.
     *
     * @param key settings key
     */
    public Configuration ( final String key )
    {
        this ( SettingsManager.getDefaultSettingsGroup (), key, ( SerializableSupplier<V> ) null, true, false );
    }

    /**
     * Constructs new {@link Configuration}.
     *
     * @param key                  settings key
     * @param loadInitialSettings  whether to load initial available settings into the {@link JComponent} or not
     * @param applySettingsChanges whether to apply settings changes to the {@link JComponent} or not
     */
    public Configuration ( final String key, final boolean loadInitialSettings, final boolean applySettingsChanges )
    {
        this ( SettingsManager.getDefaultSettingsGroup (), key, ( SerializableSupplier<V> ) null, true, false );
    }

    /**
     * Constructs new {@link Configuration}.
     *
     * @param key          settings key
     * @param defaultValue default value
     */
    public Configuration ( final String key, final V defaultValue )
    {
        this ( SettingsManager.getDefaultSettingsGroup (), key, new StaticDefaultValue<V> ( defaultValue ), true, false );
    }

    /**
     * Constructs new {@link Configuration}.
     *
     * @param key          settings key
     * @param defaultValue {@link SerializableSupplier} for default value
     */
    public Configuration ( final String key, final SerializableSupplier<V> defaultValue )
    {
        this ( SettingsManager.getDefaultSettingsGroup (), key, defaultValue, true, false );
    }

    /**
     * Constructs new {@link Configuration}.
     *
     * @param key                  settings key
     * @param defaultValue         default value
     * @param loadInitialSettings  whether to load initial available settings into the {@link JComponent} or not
     * @param applySettingsChanges whether to apply settings changes to the {@link JComponent} or not
     */
    public Configuration ( final String key, final V defaultValue,
                           final boolean loadInitialSettings, final boolean applySettingsChanges )
    {
        this ( SettingsManager.getDefaultSettingsGroup (), key, new StaticDefaultValue<V> ( defaultValue ),
                loadInitialSettings, applySettingsChanges );
    }

    /**
     * Constructs new {@link Configuration}.
     *
     * @param key                  settings key
     * @param defaultValue         {@link SerializableSupplier} for default value
     * @param loadInitialSettings  whether to load initial available settings into the {@link JComponent} or not
     * @param applySettingsChanges whether to apply settings changes to the {@link JComponent} or not
     */
    public Configuration ( final String key, final SerializableSupplier<V> defaultValue,
                           final boolean loadInitialSettings, final boolean applySettingsChanges )
    {
        this ( SettingsManager.getDefaultSettingsGroup (), key, defaultValue,
                loadInitialSettings, applySettingsChanges );
    }

    /**
     * Constructs new {@link Configuration}.
     *
     * @param group settings group
     * @param key   settings key
     */
    public Configuration ( final String group, final String key )
    {
        this ( group, key, ( SerializableSupplier<V> ) null, true, false );
    }

    /**
     * Constructs new {@link Configuration}.
     *
     * @param group                settings group
     * @param key                  settings key
     * @param loadInitialSettings  whether to load initial available settings into the {@link JComponent} or not
     * @param applySettingsChanges whether to apply settings changes to the {@link JComponent} or not
     */
    public Configuration ( final String group, final String key,
                           final boolean loadInitialSettings, final boolean applySettingsChanges )
    {
        this ( group, key, ( SerializableSupplier<V> ) null, loadInitialSettings, applySettingsChanges );
    }

    /**
     * Constructs new {@link Configuration}.
     *
     * @param group        settings group
     * @param key          settings key
     * @param defaultValue default value
     */
    public Configuration ( final String group, final String key, final V defaultValue )
    {
        this ( group, key, new StaticDefaultValue<V> ( defaultValue ), true, false );
    }

    /**
     * Constructs new {@link Configuration}.
     *
     * @param group        settings group
     * @param key          settings key
     * @param defaultValue {@link SerializableSupplier} for default value
     */
    public Configuration ( final String group, final String key, final SerializableSupplier<V> defaultValue )
    {
        this ( group, key, defaultValue, true, false );
    }

    /**
     * Constructs new {@link Configuration}.
     *
     * @param group                settings group
     * @param key                  settings key
     * @param defaultValue         default value
     * @param loadInitialSettings  whether to load initial available settings into the {@link JComponent} or not
     * @param applySettingsChanges whether to apply settings changes to the {@link JComponent} or not
     */
    public Configuration ( final String group, final String key, final V defaultValue,
                           final boolean loadInitialSettings, final boolean applySettingsChanges )
    {
        this ( group, key, defaultValue != null ? new StaticDefaultValue<V> ( defaultValue ) : null,
                loadInitialSettings, applySettingsChanges );
    }

    /**
     * Constructs new {@link Configuration}.
     *
     * @param group                settings group
     * @param key                  settings key
     * @param defaultValue         {@link SerializableSupplier} for default value
     * @param loadInitialSettings  whether to load initial available settings into the {@link JComponent} or not
     * @param applySettingsChanges whether to apply settings changes to the {@link JComponent} or not
     */
    public Configuration ( final String group, final String key, final SerializableSupplier<V> defaultValue,
                           final boolean loadInitialSettings, final boolean applySettingsChanges )
    {
        super ();
        this.group = group;
        this.key = key;
        this.defaultValue = defaultValue;
        this.loadInitialSettings = loadInitialSettings;
        this.applySettingsChanges = applySettingsChanges;
    }

    /**
     * Returns settings group.
     *
     * @return settings group
     */
    public String group ()
    {
        return group;
    }

    /**
     * Returns settings key.
     *
     * @return settings key
     */
    public String key ()
    {
        return key;
    }

    /**
     * Returns default value.
     *
     * @return default value
     */
    public V defaultValue ()
    {
        return defaultValue != null ? defaultValue.get () : null;
    }

    /**
     * Returns whether initial available settings should be loaded into the {@link JComponent} or not.
     *
     * @return true if initial available settings should be loaded into the {@link JComponent}, false otherwise
     */
    public boolean isLoadInitialSettings ()
    {
        return loadInitialSettings;
    }

    /**
     * Returns whether settings changes should be applied to the {@link JComponent} or not.
     *
     * @return {@code true} if settings changes should be applied to the {@link JComponent}, {@code false} otherwise
     */
    public boolean isApplySettingsChanges ()
    {
        return applySettingsChanges;
    }

    @Override
    public String toString ()
    {
        final String name = getClass ().getSimpleName ();
        final String settings = "[ " + group + " -> " + key + " ]";
        return name + settings;
    }

    /**
     * Simple {@link SerializableSupplier} for holding static default values.
     *
     * @param <V> {@link Serializable} value type
     */
    protected static class StaticDefaultValue<V extends Serializable> implements SerializableSupplier<V>
    {
        /**
         * Static value.
         */
        private final V value;

        /**
         * Csontructs new {@link StaticDefaultValue}.
         *
         * @param value static value
         */
        public StaticDefaultValue ( final V value )
        {
            super ();
            this.value = value;
        }

        @Override
        public V get ()
        {
            return value;
        }
    }
}