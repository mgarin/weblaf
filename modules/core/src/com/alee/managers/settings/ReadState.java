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

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * This enumeration represents {@link SettingsGroup} read states.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see SettingsManager
 * @see SettingsGroupState
 * @see SettingsGroup
 */
@XStreamAlias ( "ReadState" )
public enum ReadState
{
    /**
     * Did not attempt to load {@link SettingsGroup} yet.
     */
    none,

    /**
     * New {@link SettingsGroup} has been created.
     */
    created,

    /**
     * {@link SettingsGroup} was read successfully.
     */
    ok,

    /**
     * {@link SettingsGroup} was successfully restored from backup.
     */
    restored,

    /**
     * Failed to load {@link SettingsGroup}.
     */
    failed
}