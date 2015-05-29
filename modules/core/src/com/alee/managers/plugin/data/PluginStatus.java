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

package com.alee.managers.plugin.data;

import com.alee.utils.swing.EnumLazyIconProvider;

import javax.swing.*;

/**
 * Available plugin statuses.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-PluginManager">How to use PluginManager</a>
 * @see com.alee.managers.plugin.PluginManager
 */

public enum PluginStatus
{
    /**
     * Plugin was detected and waiting for further actions.
     */
    detected,

    /**
     * Plugin is being loaded.
     */
    loading,

    /**
     * Plugin successfully loaded.
     */
    loaded,

    /**
     * Plugin failed to load due to plugin class initialization exception.
     */
    failed;

    /**
     * Plugin status icons folder.
     */
    private static final String iconsFolder = "icons/status/";

    /**
     * Returns plugin status icon.
     *
     * @return plugin status icon
     */
    public ImageIcon getIcon ()
    {
        return EnumLazyIconProvider.getIcon ( this, iconsFolder );
    }

    /**
     * Returns plugin status text.
     *
     * @return plugin status text
     */
    public String getText ()
    {
        switch ( this )
        {
            case loading:
                return "Loading...";
            case loaded:
                return "Loaded successfully";
            case failed:
                return "Failed to load";
            default:
                return null;
        }
    }
}