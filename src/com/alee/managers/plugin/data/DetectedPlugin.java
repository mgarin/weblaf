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

import javax.swing.*;

/**
 * Initial information gathered about existing plugin.
 *
 * @author Mikle Garin
 */

public class DetectedPlugin<T extends Plugin>
{
    /**
     * Path to plugin file folder.
     */
    private final String pluginFolder;

    /**
     * Plugin file name.
     */
    private final String pluginFile;

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
     * Possible failure cause.
     */
    private String failureCause;

    /**
     * Plugin load exception.
     */
    private Throwable exception;

    /**
     * Custom exception message.
     */
    private String exceptionMessage;

    /**
     * Loaded plugin.
     */
    private T plugin;

    /**
     * Constructs new information object about existing plugin.
     *
     * @param pluginFolder path to plugin file folder
     * @param pluginFile   plugin file name
     * @param information  plugin information
     * @param logo         plugin logo
     */
    public DetectedPlugin ( final String pluginFolder, final String pluginFile, final PluginInformation information, final ImageIcon logo )
    {
        super ();
        this.pluginFolder = pluginFolder;
        this.pluginFile = pluginFile;
        this.information = information;
        this.logo = logo;
        this.status = PluginStatus.detected;
    }

    public String getPluginFolder ()
    {
        return pluginFolder;
    }

    public String getPluginFile ()
    {
        return pluginFile;
    }

    public PluginInformation getInformation ()
    {
        return information;
    }

    public ImageIcon getLogo ()
    {
        return logo;
    }

    public PluginStatus getStatus ()
    {
        return status;
    }

    public void setStatus ( final PluginStatus status )
    {
        this.status = status;
    }

    public String getFailureCause ()
    {
        return failureCause;
    }

    public void setFailureCause ( final String failureCause )
    {
        this.failureCause = failureCause;
    }

    public Throwable getException ()
    {
        return exception;
    }

    public void setException ( final Throwable exception )
    {
        this.exception = exception;
    }

    public String getExceptionMessage ()
    {
        return exceptionMessage;
    }

    public void setExceptionMessage ( final String exceptionMessage )
    {
        this.exceptionMessage = exceptionMessage;
    }

    public String getFailureText ()
    {
        return "<html><b>" + failureCause + "</b>" + ( exceptionMessage != null || exception != null ?
                "<br>" + ( exceptionMessage != null ? exceptionMessage : exception.toString () ) : "" ) + "</html>";
    }

    public T getPlugin ()
    {
        return plugin;
    }

    public void setPlugin ( final T plugin )
    {
        this.plugin = plugin;
    }
}