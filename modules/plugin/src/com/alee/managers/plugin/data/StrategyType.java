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
 * {@link com.alee.managers.plugin.Plugin} initialization strategy type.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-PluginManager">How to use PluginManager</a>
 * @see com.alee.managers.plugin.PluginManager
 * @see com.alee.managers.plugin.Plugin
 */
public enum StrategyType
{
    /**
     * Plugin doesn't define any specific strategy.
     */
    any,

    /**
     * Plugin must be initialized strictly before some other plugin.
     */
    before,

    /**
     * Plugin must be initialized strictly after some other plugin.
     */
    after;

    /**
     * Returns plugin strategy type icon.
     *
     * @return plugin strategy type icon
     */
    public ImageIcon getIcon ()
    {
        return EnumLazyIconProvider.getIcon ( this, "icons/strategy/" );
    }

    /**
     * Returns plugin strategy type description.
     *
     * @return plugin strategy type description
     */
    public String getDescription ()
    {
        switch ( this )
        {
            case any:
                return "Plugin doesn't define any specific strategy";

            case before:
                return "Plugin must be initialized strictly before some other plugin";

            case after:
                return "Plugin must be initialized strictly after some other plugin";

            default:
                return null;
        }
    }
}