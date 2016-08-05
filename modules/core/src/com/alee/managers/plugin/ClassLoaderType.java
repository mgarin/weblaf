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

package com.alee.managers.plugin;

/**
 * @author Mikle Garin
 */

public enum ClassLoaderType
{
    /**
     * todo 1. Add `hierarchical` type - it should use child class loader for each separate plugin based on parent plugin class loader
     */

    /**
     * Class loader acquired from {@link java.lang.ClassLoader#getSystemClassLoader()}
     */
    system,

    /**
     * Class loader used to load {@link com.alee.managers.plugin.PluginManager} class.
     * Commonly it is assumed to be {@link java.net.URLClassLoader} but in case it is something else a child
     * {@link com.alee.managers.plugin.PluginClassLoader} will be created to load classes properly.
     */
    manager,

    /**
     * Child {@link com.alee.managers.plugin.PluginClassLoader} for {@link #manager} class loader.
     * Same instance will be used to load all plugins within all plugin managers using this class loader type.
     */
    global,

    /**
     * Child {@link com.alee.managers.plugin.PluginClassLoader} for {@link #manager} class loader.
     * Same instance will be used to load all plugins within single plugin manager, but different managers will use different instances.
     */
    local,

    /**
     * Child {@link com.alee.managers.plugin.PluginClassLoader} for {@link #manager} class loader.
     * Separate instances will be used to load each plugin of each manager using this class loader type.
     */
    separate
}