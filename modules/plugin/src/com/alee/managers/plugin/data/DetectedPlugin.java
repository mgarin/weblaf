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

import com.alee.managers.plugin.Plugin;
import com.alee.utils.SystemUtils;

import javax.swing.*;
import java.io.File;

/**
 * Initial information gathered about existing {@link Plugin}.
 *
 * @param <P> {@link Plugin} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-PluginManager">How to use PluginManager</a>
 * @see com.alee.managers.plugin.PluginManager
 */
public class DetectedPlugin<P extends Plugin>
{
    /**
     * Path to plugin folder.
     */
    private final String pluginFolder;

    /**
     * Plugin file name.
     */
    private final String pluginFileName;

    /**
     * Plugin class.
     */
    private final PluginInformation information;

    /**
     * Plugin logo.
     */
    private final ImageIcon logo;

    /**
     * Plugin load status.
     */
    private PluginStatus status;

    /**
     * Possible load failure cause.
     */
    private String failureCause;

    /**
     * Plugin load exception.
     */
    private Throwable exception;

    /**
     * Custom load exception message.
     */
    private String exceptionMessage;

    /**
     * Loaded plugin.
     */
    private P plugin;

    /**
     * Constructs new information object about existing plugin.
     *
     * @param pluginFolder   path to plugin file folder
     * @param pluginFileName plugin file name
     * @param information    plugin information
     * @param logo           plugin logo
     */
    public DetectedPlugin ( final String pluginFolder, final String pluginFileName, final PluginInformation information,
                            final ImageIcon logo )
    {
        super ();
        this.pluginFolder = pluginFolder;
        this.pluginFileName = pluginFileName;
        this.information = information;
        this.logo = logo;
        this.status = PluginStatus.detected;
    }

    /**
     * Returns plugin folder path.
     *
     * @return plugin folder path
     */
    public String getPluginFolder ()
    {
        return pluginFolder;
    }

    /**
     * Returns plugin file name.
     *
     * @return plugin file name
     */
    public String getPluginFileName ()
    {
        return pluginFileName;
    }

    /**
     * Returns plugin file.
     *
     * @return plugin file
     */
    public File getFile ()
    {
        return new File ( pluginFolder, pluginFileName );
    }

    /**
     * Returns plugin information.
     *
     * @return plugin information
     */
    public PluginInformation getInformation ()
    {
        return information;
    }

    /**
     * Returns plugin logo.
     *
     * @return plugin logo
     */
    public ImageIcon getLogo ()
    {
        return !SystemUtils.isHeadlessEnvironment () ? logo : null;
    }

    /**
     * Returns plugin status.
     *
     * @return plugin status
     */
    public PluginStatus getStatus ()
    {
        return status;
    }

    /**
     * Sets plugin status.
     *
     * @param status new plugin status
     */
    public void setStatus ( final PluginStatus status )
    {
        this.status = status;
    }

    /**
     * Returns load failure cause.
     *
     * @return load failure cause
     */
    public String getFailureCause ()
    {
        return failureCause;
    }

    /**
     * Sets load failure cause.
     *
     * @param failureCause new load failure cause
     */
    public void setFailureCause ( final String failureCause )
    {
        this.failureCause = failureCause;
    }

    /**
     * Returns load exception.
     *
     * @return load exception
     */
    public Throwable getException ()
    {
        return exception;
    }

    /**
     * Sets load exception.
     *
     * @param exception new load exception
     */
    public void setException ( final Throwable exception )
    {
        this.exception = exception;
    }

    /**
     * Returns load exception message.
     *
     * @return load exception message
     */
    public String getExceptionMessage ()
    {
        return exceptionMessage;
    }

    /**
     * Sets load exception message.
     *
     * @param exceptionMessage new load exception message
     */
    public void setExceptionMessage ( final String exceptionMessage )
    {
        this.exceptionMessage = exceptionMessage;
    }

    /**
     * Returns load failure HTML description.
     *
     * @return load failure HTML description
     */
    public String getFailureHtmlText ()
    {
        return "<html><b>" + failureCause + "</b>" + ( exceptionMessage != null || exception != null ?
                "<br>" + ( exceptionMessage != null ? exceptionMessage : exception.toString () ) : "" ) + "</html>";
    }

    /**
     * Returns loaded plugin instance.
     *
     * @return loaded plugin instance
     */
    public P getPlugin ()
    {
        return plugin;
    }

    /**
     * Sets loaded plugin instance.
     *
     * @param plugin new loaded plugin instance
     */
    public void setPlugin ( final P plugin )
    {
        this.plugin = plugin;
    }

    @Override
    public String toString ()
    {
        return information + ", Status: " + status + ( status == PluginStatus.failed ? ", Cause: " + failureCause : "" );
    }
}