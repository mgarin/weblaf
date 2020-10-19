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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.managers.plugin.Plugin;
import com.alee.managers.plugin.PluginManager;
import com.alee.utils.SystemUtils;

import javax.swing.*;
import java.io.File;

/**
 * Initial information gathered about existing {@link Plugin}.
 *
 * @param <P> {@link Plugin} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-PluginManager">How to use PluginManager</a>
 * @see PluginManager
 */
public class DetectedPlugin<P extends Plugin>
{
    /**
     * Path to {@link Plugin} folder.
     * Will be {@code null} if the {@link Plugin} was not loaded via {@link PluginManager}.
     */
    @Nullable
    private final String pluginFolder;

    /**
     * {@link Plugin} file name.
     * Will be {@code null} if the {@link Plugin} was not loaded via {@link PluginManager}.
     */
    @Nullable
    private final String pluginFileName;

    /**
     * {@link PluginInformation}.
     */
    @NotNull
    private final PluginInformation information;

    /**
     * {@link Plugin} logo.
     * Will be {@code null} if not specified.
     */
    @Nullable
    private final Icon logo;

    /**
     * Plugin load status.
     */
    @NotNull
    private PluginStatus status;

    /**
     * {@link Plugin} initialization failure cause.
     * Will be {@code null} if {@link Plugin} wasn't initialized yet or initialized successfully.
     */
    @Nullable
    private String failureCause;

    /**
     * {@link Throwable} that occurred during {@link Plugin} initialization.
     * Will be {@code null} if {@link Plugin} wasn't initialized yet or initialized successfully.
     */
    @Nullable
    private Throwable exception;

    /**
     * Message of the exception that occurred during plug{@link Plugin}tialization.
     * Will be {@code null} if {@link Plugin} wasn't initialized yet or initialized successfully.
     */
    @Nullable
    private String exceptionMessage;

    /**
     * Initialized {@link Plugin} instance.
     */
    @Nullable
    private P plugin;

    /**
     * Constructs new {@link DetectedPlugin}.
     *
     * @param pluginFolder   path to {@link Plugin} folder
     * @param pluginFileName {@link Plugin} file name
     * @param information    {@link PluginInformation}
     * @param logo           {@link Plugin} logo
     */
    public DetectedPlugin ( @Nullable final String pluginFolder, @Nullable final String pluginFileName,
                            @NotNull final PluginInformation information, @Nullable final Icon logo )
    {
        this.pluginFolder = pluginFolder;
        this.pluginFileName = pluginFileName;
        this.information = information;
        this.logo = logo;
        this.status = PluginStatus.detected;
    }

    /**
     * Returns path to {@link Plugin} folder.
     * Will be {@code null} if the {@link Plugin} was not loaded via {@link PluginManager}.
     *
     * @return path to {@link Plugin} folder
     */
    @Nullable
    public String getPluginFolder ()
    {
        return pluginFolder;
    }

    /**
     * Returns {@link Plugin} file name.
     * Will be {@code null} if the {@link Plugin} was not loaded via {@link PluginManager}.
     *
     * @return {@link Plugin} file name
     */
    @Nullable
    public String getPluginFileName ()
    {
        return pluginFileName;
    }

    /**
     * Returns {@link Plugin} {@link File}.
     * Will be {@code null} if the {@link Plugin} was not loaded via {@link PluginManager}.
     *
     * @return {@link Plugin} {@link File}.
     */
    @Nullable
    public File getFile ()
    {
        final String pluginFolder = getPluginFolder ();
        final String pluginFileName = getPluginFileName ();
        return pluginFolder != null && pluginFileName != null
                ? new File ( pluginFolder, pluginFileName )
                : null;
    }

    /**
     * Returns {@link PluginInformation}.
     *
     * @return {@link PluginInformation}
     */
    @NotNull
    public PluginInformation getInformation ()
    {
        return information;
    }

    /**
     * Returns {@link Plugin} logo {@link Icon}.
     * Will be {@code null} if not specified or application is running in headless mode.
     *
     * @return {@link Plugin} logo {@link Icon}
     */
    @Nullable
    public Icon getLogo ()
    {
        return !SystemUtils.isHeadlessEnvironment () ? logo : null;
    }

    /**
     * Returns {@link PluginStatus}.
     *
     * @return {@link PluginStatus}
     */
    @NotNull
    public PluginStatus getStatus ()
    {
        return status;
    }

    /**
     * Sets {@link PluginStatus}.
     *
     * @param status new {@link PluginStatus}
     */
    public void setStatus ( @NotNull final PluginStatus status )
    {
        this.status = status;
    }

    /**
     * Returns {@link Plugin} initialization failure cause.
     * Will be {@code null} if {@link Plugin} wasn't initialized yet or initialized successfully.
     *
     * @return {@link Plugin} initialization failure cause
     */
    @Nullable
    public String getFailureCause ()
    {
        return failureCause;
    }

    /**
     * Sets {@link Plugin} initialization failure cause.
     *
     * @param failureCause new {@link Plugin} initialization failure cause
     */
    public void setFailureCause ( @Nullable final String failureCause )
    {
        this.failureCause = failureCause;
    }

    /**
     * Returns {@link Throwable} that occurred during {@link Plugin} initialization.
     * Will be {@code null} if {@link Plugin} wasn't initialized yet or initialized successfully.
     *
     * @return {@link Throwable} that occurred during {@link Plugin} initialization
     */
    @Nullable
    public Throwable getException ()
    {
        return exception;
    }

    /**
     * Sets {@link Throwable} that occurred during {@link Plugin} initialization.
     *
     * @param exception new {@link Throwable} that occurred during {@link Plugin} initialization
     */
    public void setException ( @Nullable final Throwable exception )
    {
        this.exception = exception;
    }

    /**
     * Returns message of the exception that occurred during {@link Plugin} initialization.
     * Will be {@code null} if {@link Plugin} wasn't initialized yet or initialized successfully.
     *
     * @return message of the exception that occurred during {@link Plugin} initialization
     */
    @Nullable
    public String getExceptionMessage ()
    {
        return exceptionMessage;
    }

    /**
     * Sets message of the exception that occurred during {@link Plugin} initialization.
     *
     * @param exceptionMessage new message of the exception that occurred during {@link Plugin} initialization
     */
    public void setExceptionMessage ( @Nullable final String exceptionMessage )
    {
        this.exceptionMessage = exceptionMessage;
    }

    /**
     * Returns initialized {@link Plugin} instance.
     *
     * @return initialized {@link Plugin} instance
     */
    @Nullable
    public P getPlugin ()
    {
        return plugin;
    }

    /**
     * Sets initialized {@link Plugin} instance.
     *
     * @param plugin new initialized {@link Plugin} instance
     */
    public void setPlugin ( @Nullable final P plugin )
    {
        this.plugin = plugin;
    }

    @NotNull
    @Override
    public String toString ()
    {
        final PluginStatus status = getStatus ();
        final String text;
        if ( status == PluginStatus.failed )
        {
            text = String.format (
                    "%s, Status: %s, Cause: %s",
                    getInformation (),
                    status,
                    getFailureCause ()
            );
        }
        else
        {
            text = String.format (
                    "%s, Status: %s",
                    getInformation (),
                    status
            );
        }
        return text;
    }
}