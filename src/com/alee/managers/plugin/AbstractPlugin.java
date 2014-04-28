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

import com.alee.managers.plugin.data.InitializationStrategy;
import com.alee.managers.plugin.data.PluginInformation;
import com.alee.utils.XmlUtils;

/**
 * @author Mikle Garin
 */

public abstract class AbstractPlugin extends Plugin
{
    /**
     * Cached plugin information.
     */
    private PluginInformation pluginInformation;

    /**
     * Cached plugin initialization strategy.
     */
    private InitializationStrategy initializationStrategy;

    /**
     * {@inheritDoc}
     */
    @Override
    public PluginInformation getPluginInformation ()
    {
        if ( pluginInformation == null )
        {
            pluginInformation = loadPluginInformation ( getClass (), "resources/" + getPluginDescriptor () );
        }
        return pluginInformation;
    }

    /**
     * Loads plugin information from the specified location near class.
     *
     * @return plugin information from the specified location near class
     */
    protected PluginInformation loadPluginInformation ( final Class nearClass, final String path )
    {
        return XmlUtils.fromXML ( nearClass.getResource ( path ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InitializationStrategy getInitializationStrategy ()
    {
        if ( initializationStrategy == null )
        {
            initializationStrategy = createInitializationStrategy ();
        }
        return initializationStrategy;
    }

    /**
     * Creates and returns plugin initialization strategy.
     *
     * @return plugin initialization strategy
     */
    protected InitializationStrategy createInitializationStrategy ()
    {
        return InitializationStrategy.any ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void disabled ()
    {
        // Do nothing by default
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void enabled ()
    {
        // Do nothing by default
    }
}