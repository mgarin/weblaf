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
 * Special listener adapter for PluginManager events.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-PluginManager">How to use PluginManager</a>
 * @see com.alee.managers.plugin.PluginManager
 */

public abstract class PluginsAdapter<T extends Plugin> implements PluginsListener<T>
{
    @Override
    public void pluginsCheckStarted ( final String directory, final boolean recursive )
    {
        // Do nothing by default
    }

    @Override
    public void pluginsCheckEnded ( final String directory, final boolean recursive )
    {
        // Do nothing by default
    }

    @Override
    public void pluginsDetected ( final List<DetectedPlugin<T>> plugins )
    {
        // Do nothing by default
    }

    @Override
    public void pluginsInitialized ( final List<T> plugins )
    {
        // Do nothing by default
    }
}