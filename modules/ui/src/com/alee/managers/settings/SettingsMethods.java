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

import com.alee.utils.swing.extensions.MethodExtension;

import javax.swing.*;
import java.io.Serializable;

/**
 * This interface provides a set of methods for {@link JComponent}s supported by {@link UISettingsManager}.
 *
 * @param <C> {@link JComponent} type
 * @param <V> {@link Serializable} data type
 * @param <K> {@link Configuration} type
 * @author Mikle Garin
 * @see UISettingsManager
 * @see SettingsManager
 * @see MethodExtension
 */
public interface SettingsMethods extends MethodExtension
{
    /**
     * Registers {@link JComponent} for settings auto-save.
     *
     * Registered {@link JComponent} will be:
     * - tracked for settings changes (depends on {@link SettingsProcessor}) to save them when requested
     * - automatically updated with any loaded settings for that key if requested
     * - automatically updated with any changes made in its settings if requested
     *
     * @param configuration {@link Configuration}
     */
    public void registerSettings ( Configuration configuration );

    /**
     * Registers {@link JComponent} for settings auto-save.
     *
     * Registered {@link JComponent} will be:
     * - tracked for settings changes (depends on {@link SettingsProcessor}) to save them when requested
     * - automatically updated with any loaded settings for that key if requested
     * - automatically updated with any changes made in its settings if requested
     *
     * @param processor {@link SettingsProcessor}
     */
    public void registerSettings ( SettingsProcessor processor );

    /**
     * Unregisters {@link JComponent} from settings auto-save.
     */
    public void unregisterSettings ();

    /**
     * Loads previously saved settings for the specified {@link JComponent} if it is registered.
     */
    public void loadSettings ();

    /**
     * Saves settings for the specified {@link JComponent} if it is registered.
     */
    public void saveSettings ();
}