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

import com.alee.managers.plugin.data.DetectedPlugin;

import java.util.List;

/**
 * Special listener for PluginManager events.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-PluginManager">How to use PluginManager</a>
 * @see com.alee.managers.plugin.PluginManager
 */

public interface PluginsListener<T extends Plugin>
{
    /**
     * Called when plugins check starts.
     *
     * @param directory checked plugins directory path
     * @param recursive whether plugins directory subfolders are checked recursively or not
     */
    public void pluginsCheckStarted ( String directory, boolean recursive );

    /**
     * Called when plugins check finishes.
     *
     * @param directory checked plugins directory path
     * @param recursive whether plugins directory subfolders are checked recursively or not
     */
    public void pluginsCheckEnded ( String directory, boolean recursive );

    /**
     * Called when new portion of plugins have been detected.
     * Be aware that this list does not provide all detected plugins - complete detected plugins list can be retrieved from PluginManager.
     *
     * @param plugins recently detected plugins
     */
    public void pluginsDetected ( List<DetectedPlugin<T>> plugins );

    /**
     * Called when new portion of plugins have been successfully initialized.
     * Be aware that this list does not provide all initialized plugins - complete plugins list can be retrieved from PluginManager.
     * This also can be called without pre-call of plugin check and detection methods if plugins are registered programmatically.
     *
     * @param plugins recently initialized plugins
     */
    public void pluginsInitialized ( List<T> plugins );
}