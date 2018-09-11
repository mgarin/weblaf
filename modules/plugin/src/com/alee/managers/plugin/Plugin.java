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

import com.alee.managers.plugin.data.*;
import com.alee.utils.SystemUtils;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.List;

/**
 * Base class for any plugin.
 *
 * @param <P> {@link Plugin} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-PluginManager">How to use PluginManager</a>
 * @see PluginManager
 */
public abstract class Plugin<P extends Plugin<P>>
{
    /**
     * Plugin manager which loaded this plugin.
     */
    protected PluginManager<P> pluginManager;

    /**
     * Detected plugin information.
     */
    protected DetectedPlugin<P> detectedPlugin;

    /**
     * Cached plugin initialization strategy.
     */
    protected InitializationStrategy initializationStrategy;

    /**
     * Whether this plugin is enabled or not.
     */
    protected boolean enabled = true;

    /**
     * Returns plugin manager which loaded this plugin.
     *
     * @return plugin manager which loaded this plugin
     */
    public PluginManager<P> getPluginManager ()
    {
        return pluginManager;
    }

    /**
     * Sets plugin manager which loaded this plugin.
     *
     * @param pluginManager plugin manager which loaded this plugin
     */
    protected void setPluginManager ( final PluginManager<P> pluginManager )
    {
        this.pluginManager = pluginManager;
    }

    /**
     * Returns additional information about this plugin.
     *
     * @return additional information about this plugin
     */
    public DetectedPlugin<P> getDetectedPlugin ()
    {
        return detectedPlugin;
    }

    /**
     * Sets additional information about this plugin.
     *
     * @param detectedPlugin additional information about this plugin
     */
    protected void setDetectedPlugin ( final DetectedPlugin<P> detectedPlugin )
    {
        this.detectedPlugin = detectedPlugin;
    }

    /**
     * Returns information about this plugin.
     *
     * @return information about this plugin
     */
    public PluginInformation getInformation ()
    {
        return detectedPlugin != null ? detectedPlugin.getInformation () : null;
    }

    /**
     * Returns plugin logo.
     *
     * @return plugin logo
     */
    public ImageIcon getPluginLogo ()
    {
        return !SystemUtils.isHeadlessEnvironment () && detectedPlugin != null ? detectedPlugin.getLogo () : null;
    }

    /**
     * Returns plugin ID.
     *
     * @return plugin ID
     */
    public String getId ()
    {
        return getInformation ().getId ();
    }

    /**
     * Returns plugin type.
     *
     * @return plugin type
     */
    public String getType ()
    {
        return getInformation ().getType ();
    }

    /**
     * Returns plugin types.
     *
     * @return plugin types
     */
    public String getTypes ()
    {
        return getInformation ().getTypes ();
    }

    /**
     * Says whether plugin can be disabled in runtime or not.
     * In some cases you don't want plugin to be disabled in runtime, for example if it is vital for system runtime.
     * Note that if plugin is disableable but cannot be hot-loaded you won't be able to enable it after disabling.
     *
     * @return true if plugin can be disabled in runtime, false otherwise
     */
    public boolean isDisableable ()
    {
        return getInformation ().isDisableable ();
    }

    /**
     * Returns plugin main class.
     *
     * @return plugin main class
     */
    public String getMainClass ()
    {
        return getInformation ().getMainClass ();
    }

    /**
     * Returns plugin title.
     *
     * @return plugin title
     */
    public String getTitle ()
    {
        return getInformation ().getTitle ();
    }

    /**
     * Returns plugin description.
     *
     * @return plugin description
     */
    public String getDescription ()
    {
        return getInformation ().getDescription ();
    }

    /**
     * Returns plugin version information.
     *
     * @return plugin version information
     */
    public PluginVersion getVersion ()
    {
        return getInformation ().getVersion ();
    }

    /**
     * Returns plugin libraries list.
     *
     * @return plugin libraries list
     */
    public List<PluginLibrary> getLibraries ()
    {
        return getInformation ().getLibraries ();
    }

    /**
     * Returns plugins required to run this one.
     *
     * @return plugins required to run this one
     */
    public List<PluginDependency> getDependencies ()
    {
        return getInformation ().getDependencies ();
    }

    /**
     * Returns plugin initialization strategy.
     * It will determine order in which plugins are sorted for further usage.
     *
     * @return plugin initialization strategy
     */
    public final InitializationStrategy getInitializationStrategy ()
    {
        if ( initializationStrategy == null )
        {
            initializationStrategy = createInitializationStrategy ();
        }
        return initializationStrategy;
    }

    /**
     * Creates and returns plugin initialization strategy.
     * It will determine order in which plugins are sorted for further usage.
     *
     * @return plugin initialization strategy
     */
    protected InitializationStrategy createInitializationStrategy ()
    {
        return InitializationStrategy.any ();
    }

    /**
     * Disables plugin runtime actions.
     * It is up to developer to do the actual work, but base method still checks whether action is available or not.
     */
    public final void disable ()
    {
        if ( isDisableable () )
        {
            if ( enabled )
            {
                disabled ();
                enabled = false;
            }
            else
            {
                final String msg = "Plugin [ %s ] is already disabled";
                LoggerFactory.getLogger ( Plugin.class ).warn ( String.format ( msg, getInformation () ) );
            }
        }
        else
        {
            final String msg = "Plugin [ %s ] cannot be disabled";
            LoggerFactory.getLogger ( Plugin.class ).error ( String.format ( msg, getInformation () ) );
        }
    }

    /**
     * This method called when plugin is disabled.
     * Plugin should stop all its services and wait till re-enable or application exit.
     */
    protected void disabled ()
    {
        // Do nothing by default
    }

    /**
     * Enables plugin runtime actions.
     * It is up to developer to the actual work, but base method still checks whether action is available or not.
     */
    public final void enable ()
    {
        if ( isDisableable () )
        {
            if ( !enabled )
            {
                enabled ();
                enabled = true;
            }
            else
            {
                final String msg = "Plugin [ %s ] is already enabled";
                LoggerFactory.getLogger ( Plugin.class ).warn ( String.format ( msg, getInformation () ) );
            }
        }
        else
        {
            final String msg = "Plugin [ %s ] cannot be re-enabled";
            LoggerFactory.getLogger ( Plugin.class ).error ( String.format ( msg, getInformation () ) );
        }
    }

    /**
     * This method called when plugin is re-enabled.
     * Plugin should restart all its services.
     */
    protected void enabled ()
    {
        // Do nothing by default
    }

    /**
     * Returns whether this plugin is enabled or not.
     *
     * @return true if this plugin is enabled, false otherwise
     */
    public boolean isEnabled ()
    {
        return enabled;
    }

    /**
     * Sets whether this plugin should be enabled or not.
     *
     * @param enabled whether this plugin should be enabled or not
     */
    public void setEnabled ( final boolean enabled )
    {
        if ( enabled )
        {
            enable ();
        }
        else
        {
            disable ();
        }
    }
}