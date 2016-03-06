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

import com.alee.managers.log.Log;
import com.alee.managers.plugin.data.*;

import javax.swing.*;
import java.util.List;

/**
 * Base class for any plugin.
 * You still might want to use {@code AbstractPlugin} instead as it has some basic plugin methods.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-PluginManager">How to use PluginManager</a>
 * @see com.alee.managers.plugin.PluginManager
 */

public abstract class Plugin<T extends Plugin<T>>
{
    /**
     * Plugin manager which loaded this plugin.
     */
    protected PluginManager<T> pluginManager;

    /**
     * Detected plugin information.
     */
    protected DetectedPlugin<T> detectedPlugin;

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
    public PluginManager<T> getPluginManager ()
    {
        return pluginManager;
    }

    /**
     * Sets plugin manager which loaded this plugin.
     *
     * @param pluginManager plugin manager which loaded this plugin
     */
    protected void setPluginManager ( final PluginManager<T> pluginManager )
    {
        this.pluginManager = pluginManager;
    }

    /**
     * Returns additional information about this plugin.
     *
     * @return additional information about this plugin
     */
    public DetectedPlugin<T> getDetectedPlugin ()
    {
        return detectedPlugin;
    }

    /**
     * Sets additional information about this plugin.
     *
     * @param detectedPlugin additional information about this plugin
     */
    protected void setDetectedPlugin ( final DetectedPlugin<T> detectedPlugin )
    {
        this.detectedPlugin = detectedPlugin;
    }

    /**
     * Returns information about this plugin.
     *
     * @return information about this plugin
     */
    public PluginInformation getPluginInformation ()
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
        return detectedPlugin != null ? detectedPlugin.getLogo () : null;
    }

    /**
     * Returns plugin ID.
     *
     * @return plugin ID
     */
    public String getId ()
    {
        return getPluginInformation ().getId ();
    }

    /**
     * Returns plugin type.
     *
     * @return plugin type
     */
    public String getType ()
    {
        return getPluginInformation ().getType ();
    }

    /**
     * Returns plugin types.
     *
     * @return plugin types
     */
    public String getTypes ()
    {
        return getPluginInformation ().getTypes ();
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
        return getPluginInformation ().isDisableable ();
    }

    /**
     * Returns plugin main class.
     *
     * @return plugin main class
     */
    public String getMainClass ()
    {
        return getPluginInformation ().getMainClass ();
    }

    /**
     * Returns plugin title.
     *
     * @return plugin title
     */
    public String getTitle ()
    {
        return getPluginInformation ().getTitle ();
    }

    /**
     * Returns plugin description.
     *
     * @return plugin description
     */
    public String getDescription ()
    {
        return getPluginInformation ().getDescription ();
    }

    /**
     * Returns plugin version information.
     *
     * @return plugin version information
     */
    public PluginVersion getVersion ()
    {
        return getPluginInformation ().getVersion ();
    }

    /**
     * Returns plugin libraries list.
     *
     * @return plugin libraries list
     */
    public List<PluginLibrary> getLibraries ()
    {
        return getPluginInformation ().getLibraries ();
    }

    /**
     * Returns plugins required to run this one.
     *
     * @return plugins required to run this one
     */
    public List<PluginDependency> getDependencies ()
    {
        return getPluginInformation ().getDependencies ();
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
                Log.warn ( this, "Plugin [ " + getPluginInformation () + " ] is already disabled" );
            }
        }
        else
        {
            Log.error ( this, "Plugin [ " + getPluginInformation () + " ] cannot be disabled" );
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
                Log.warn ( this, "Plugin [ " + getPluginInformation () + " ] is already enabled" );
            }
        }
        else
        {
            Log.error ( this, "Plugin [ " + getPluginInformation () + " ] cannot be re-enabled" );
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