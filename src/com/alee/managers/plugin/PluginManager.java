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

import com.alee.extended.log.Log;
import com.alee.laf.GlobalConstants;
import com.alee.managers.plugin.data.*;
import com.alee.utils.XmlUtils;
import com.alee.utils.ZipUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Base for any plugin manager you might want to create.
 *
 * @author Mikle Garin
 */

public abstract class PluginManager<T extends Plugin>
{
    /**
     * Plugin checks lock object.
     */
    protected final Object checkLock = new Object ();

    /**
     * Detected plugins list.
     * All plugins with available descriptions will get into this list.
     */
    protected List<DetectedPlugin<T>> detectedPlugins;

    /**
     * Loaded and running plugins list.
     * This might be less than list of detected plugins in the end due to lots of different reasons.
     * Only those plugins which are actually loaded successfully are getting added here.
     */
    protected List<T> availablePlugins;

    /**
     * Amount of successfully loaded plugins.
     */
    protected int loadedPluginsAmount = 0;

    /**
     * Amount of plugins which have failed to load.
     * There might be a lot of reasons why they failed to load - exception, broken JAR, missing libraries etc.
     * Simply check the log or retrieve failure cause from DetectedPlugin to understand what happened.
     */
    protected int failedPluginsAmount = 0;

    /**
     * Plugins directory path.
     * It is either absolute path or relative to working directory path.
     */
    protected String pluginsDirectoryPath;

    /**
     * Whether plugins directory subfolders should be checked recursively or not.
     */
    protected boolean checkRecursively;

    /**
     * Plugin directory files filter.
     * By defauly "*.jar" and "*.plugin" files are accepted.
     */
    protected FileFilter fileFilter;

    /**
     * Constructs new plugin manager.
     */
    public PluginManager ()
    {
        this ( null );
    }

    /**
     * Constructs new plugin manager.
     *
     * @param pluginsDirectoryPath plugins directory path
     */
    public PluginManager ( final String pluginsDirectoryPath )
    {
        this ( pluginsDirectoryPath, true );
    }

    /**
     * Constructs new plugin manager.
     *
     * @param pluginsDirectoryPath plugins directory path
     * @param checkRecursively     whether plugins directory subfolders should be checked recursively or not
     */
    public PluginManager ( final String pluginsDirectoryPath, final boolean checkRecursively )
    {
        super ();

        // User settings
        this.pluginsDirectoryPath = pluginsDirectoryPath;
        this.checkRecursively = checkRecursively;

        // Default file filter
        this.fileFilter = new FileFilter ()
        {
            @Override
            public boolean accept ( final File file )
            {
                final String name = file.getName ().toLowerCase ();
                return name.endsWith ( ".jar" ) || name.endsWith ( ".plugin" );
            }
        };

        // Runtime variables
        detectedPlugins = new ArrayList<DetectedPlugin<T>> ();
        availablePlugins = new ArrayList<T> ( detectedPlugins.size () );
    }

    /**
     * Returns name of the plugin descriptor file.
     * This file should contain serialized PluginInformation.
     *
     * @return name of the plugin descriptor file
     */
    protected abstract String getPluginDescriptor ();

    /**
     * Returns accepted by this manager plugin type.
     *
     * @return accepted by this manager plugin type
     */
    protected abstract String getAcceptedPluginType ();

    /**
     * Performs plugins search within the specified plugins directory.
     * This call might be performed as many times as you like.
     * It will simply ignore plugins detected before and will process newly found plugins appropriately.
     */
    public void checkPlugins ()
    {
        checkPlugins ( pluginsDirectoryPath );
    }

    /**
     * Performs plugins search within the specified plugins directory.
     * This call might be performed as many times as you like.
     * It will simply ignore plugins detected before and will process newly found plugins appropriately.
     *
     * @param pluginsDirectoryPath plugins directory path
     */
    public void checkPlugins ( final String pluginsDirectoryPath )
    {
        checkPlugins ( pluginsDirectoryPath, checkRecursively );
    }

    /**
     * Performs plugins search within the specified plugins directory.
     * This call might be performed as many times as you like.
     * It will simply ignore plugins detected before and will process newly found plugins appropriately.
     *
     * @param pluginsDirectoryPath plugins directory path
     * @param checkRecursively     whether plugins directory subfolders should be checked recursively or not
     */
    public void checkPlugins ( final String pluginsDirectoryPath, final boolean checkRecursively )
    {
        synchronized ( checkLock )
        {
            // Collecting plugins information
            if ( collectPluginsInformation ( pluginsDirectoryPath, checkRecursively ) )
            {
                // Initializing plugins
                initializePlugins ();

                // Sorting plugins according to their initialization strategies
                applyInitializationStrategy ();
            }
        }
    }

    /**
     * Collects information about available plugins.
     *
     * @return true if operation succeeded, false otherwise
     */
    protected boolean collectPluginsInformation ( final String pluginsDirectoryPath, final boolean checkRecursively )
    {
        if ( pluginsDirectoryPath != null )
        {
            Log.info ( this, "Collecting plugins information..." );
            return collectPluginsInformationImpl ( new File ( pluginsDirectoryPath ), checkRecursively );
        }
        else
        {
            Log.error ( this, "Plugins directory is not yet specified" );
            return false;
        }
    }

    /**
     * Collects information about available plugins.
     *
     * @param dir plugins directory
     * @return true if operation succeeded, false otherwise
     */
    protected boolean collectPluginsInformationImpl ( final File dir, final boolean checkRecursively )
    {
        // Checking all files
        final File[] files = dir.listFiles ( fileFilter );
        if ( files != null )
        {
            for ( final File file : files )
            {
                collectPluginInformation ( file );
            }
        }

        // Checking sub-directories recursively
        if ( checkRecursively )
        {
            final File[] subfolders = dir.listFiles ( GlobalConstants.DIRECTORIES_FILTER );
            if ( subfolders != null )
            {
                for ( final File subfolder : subfolders )
                {
                    collectPluginsInformationImpl ( subfolder, checkRecursively );
                }
            }
        }

        return true;
    }

    /**
     * Tries to collect plugin information from the specified file.
     * This call will simply be ignored if this is not a plugin file or if something goes wrong.
     *
     * @param file plugin file to process
     */
    protected void collectPluginInformation ( final File file )
    {
        try
        {
            final ZipFile zipFile = new ZipFile ( file );
            final Enumeration entries = zipFile.entries ();
            while ( entries.hasMoreElements () )
            {
                final ZipEntry entry = ( ZipEntry ) entries.nextElement ();
                if ( entry.getName ().endsWith ( getPluginDescriptor () ) )
                {
                    // Reading plugin information
                    final InputStream inputStream = zipFile.getInputStream ( entry );
                    final PluginInformation info = XmlUtils.fromXML ( inputStream );
                    inputStream.close ();

                    // Reading plugin icon
                    final ZipEntry logoEntry = new ZipEntry ( ZipUtils.getZipEntryFileLocation ( entry ) + "logo.png" );
                    final InputStream logoInputStream = zipFile.getInputStream ( logoEntry );
                    final ImageIcon logo = new ImageIcon ( ImageIO.read ( logoInputStream ) );
                    logoInputStream.close ();

                    // Checking whether we have already detected this plugin or not
                    if ( !wasDetected ( file.getParent (), file.getName () ) )
                    {
                        final DetectedPlugin<T> plugin = new DetectedPlugin<T> ( file.getParent (), file.getName (), info, logo );
                        detectedPlugins.add ( plugin );
                        Log.info ( this, "Plugin detected: " + info );
                    }

                    break;
                }
            }
            zipFile.close ();
        }
        catch ( final IOException e )
        {
            Log.error ( this, e );
        }
    }

    /**
     * Returns whether this plugin file was already detected before or not.
     *
     * @param pluginFolder plugin directory
     * @param pluginFile   plugin file
     * @return true if this plugin file was already detected before, false otherwise
     */
    protected boolean wasDetected ( final String pluginFolder, final String pluginFile )
    {
        for ( final DetectedPlugin<T> plugin : detectedPlugins )
        {
            if ( plugin.getPluginFolder ().equals ( pluginFolder ) && plugin.getPluginFile ().equals ( pluginFile ) )
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Initializes detected earlier plugins.
     */
    protected void initializePlugins ()
    {
        Log.info ( this, "Initializing plugins..." );

        // Map to store plugin libraries
        final Map<String, Map<PluginLibrary, PluginInformation>> pluginLibraries =
                new HashMap<String, Map<PluginLibrary, PluginInformation>> ();

        // Initializing detected plugins
        for ( final DetectedPlugin<T> detectedPlugin : detectedPlugins )
        {
            // Skip plugins we have already tried to initialize
            if ( detectedPlugin.getStatus () != PluginStatus.detected )
            {
                continue;
            }

            final PluginInformation info = detectedPlugin.getInformation ();
            final String prefix = "[" + info.toString () + "] ";
            try
            {
                // Checking plugin type as we don't want (for example) to load server plugins on client side
                if ( !info.getType ().equals ( getAcceptedPluginType () ) )
                {
                    Log.warn ( this, prefix + "Plugin of type \"" + info.getType () + "\" cannot be loaded, " +
                            "required plugin type is \"" + getAcceptedPluginType () + "\"" );
                    detectedPlugin.setStatus ( PluginStatus.failed );
                    detectedPlugin.setFailureCause ( "Inappropriate plugin type" );
                    detectedPlugin.setExceptionMessage ( "Detected plugin type: " + info.getType () + "\", " +
                            "required plugin type: \"" + getAcceptedPluginType () + "\"" );
                    failedPluginsAmount++;
                    continue;
                }

                // Checking that it is latest plugin version of all available
                // Usually there shouldn't be different versions of the same plugin but everyone make mistakes
                if ( isDeprecated ( detectedPlugin, detectedPlugins ) )
                {
                    Log.warn ( this, prefix + "Plugin is deprecated, newer version of plugin will be loaded instead" );
                    detectedPlugin.setStatus ( PluginStatus.failed );
                    detectedPlugin.setFailureCause ( "Deprecated plugin" );
                    detectedPlugin.setExceptionMessage ( "This plugin is deprecated, newer version loaded instead" );
                    failedPluginsAmount++;
                    continue;
                }

                // Now loading the plugin
                Log.info ( this, prefix + "Initializing plugin..." );
                detectedPlugin.setStatus ( PluginStatus.loading );

                // Collecting plugin and its libraries JAR paths
                final List<URL> jarPaths = new ArrayList<URL> ( 1 + info.getLibrariesCount () );
                jarPaths.add ( new File ( detectedPlugin.getPluginFolder (), detectedPlugin.getPluginFile () ).toURI ().toURL () );
                if ( info.getLibraries () != null )
                {
                    for ( final PluginLibrary library : info.getLibraries () )
                    {
                        final File file = new File ( detectedPlugin.getPluginFolder (), library.getFile () );
                        if ( file.exists () )
                        {
                            // Adding library URI to path
                            jarPaths.add ( file.toURI ().toURL () );

                            // Saving library information for futher checks
                            Map<PluginLibrary, PluginInformation> libraries = pluginLibraries.get ( library.getId () );
                            if ( libraries == null )
                            {
                                libraries = new HashMap<PluginLibrary, PluginInformation> ( 1 );
                                pluginLibraries.put ( library.getId (), libraries );
                            }
                            libraries.put ( library, info );
                        }
                        else
                        {
                            Log.warn ( this, prefix + "Unable to locate library: " + library.getFile () );
                        }
                    }
                }

                try
                {
                    // Loading the plugin
                    final URLClassLoader classLoader = URLClassLoader.newInstance ( jarPaths.toArray ( new URL[ jarPaths.size () ] ) );
                    final T plugin = ( T ) classLoader.loadClass ( info.getMainClass () ).newInstance ();
                    availablePlugins.add ( plugin );
                    Log.info ( this, prefix + "Plugin initialized" );

                    detectedPlugin.setStatus ( PluginStatus.loaded );
                    detectedPlugin.setPlugin ( plugin );
                    loadedPluginsAmount++;
                }
                catch ( final Throwable e )
                {
                    Log.error ( this, prefix + "Unable to initialize plugin", e );
                    detectedPlugin.setStatus ( PluginStatus.failed );
                    detectedPlugin.setFailureCause ( "Internal plugin exception" );
                    detectedPlugin.setException ( e );
                    failedPluginsAmount++;
                }
            }
            catch ( final Throwable e )
            {
                Log.error ( this, prefix + "Unable to initialize plugin data", e );
                detectedPlugin.setStatus ( PluginStatus.failed );
                detectedPlugin.setFailureCause ( "Plugin data initialization exception" );
                detectedPlugin.setException ( e );
                failedPluginsAmount++;
            }
        }

        // Checking for same/similar libraries used within plugins
        boolean warned = false;
        for ( final Map.Entry<String, Map<PluginLibrary, PluginInformation>> libraries : pluginLibraries.entrySet () )
        {
            final Map<PluginLibrary, PluginInformation> sameLibraries = libraries.getValue ();
            if ( sameLibraries.size () > 1 )
            {
                final String title = sameLibraries.keySet ().iterator ().next ().getTitle ();
                final StringBuilder sb = new StringBuilder ( "Library [ " ).append ( title ).append ( " ] was found in plugins: " );
                for ( final Map.Entry<PluginLibrary, PluginInformation> library : sameLibraries.entrySet () )
                {
                    final PluginInformation plugin = library.getValue ();
                    final String libraryVersion = library.getKey ().getVersion ();
                    sb.append ( "[ " ).append ( plugin.toString () ).append ( ", version " ).append ( libraryVersion ).append ( " ] " );
                }
                Log.warn ( this, sb.toString () );
                warned = true;
            }
        }
        if ( warned )
        {
            Log.warn ( this, "Make sure the same library usafe within different plugins was actually your intent" );
        }
    }

    /**
     * Returns whether the list of detected plugins contain a newer version of the specified plugin or not.
     *
     * @param plugin          plugin to compare with other detected plugins
     * @param detectedPlugins list of detected plugins
     * @return true if the list of detected plugins contain a newer version of the specified plugin, false otherwise
     */
    protected boolean isDeprecated ( final DetectedPlugin<T> plugin, final List<DetectedPlugin<T>> detectedPlugins )
    {
        final PluginInformation pluginInfo = plugin.getInformation ();
        for ( final DetectedPlugin detectedPlugin : detectedPlugins )
        {
            if ( detectedPlugin != plugin )
            {
                final PluginInformation detectedPluginInfo = detectedPlugin.getInformation ();
                if ( detectedPluginInfo.getId ().equals ( pluginInfo.getId () ) &&
                        detectedPluginInfo.getVersion ().newerThan ( pluginInfo.getVersion () ) )
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Sorting plugins according to their initialization strategies.
     */
    protected void applyInitializationStrategy ()
    {
        // Skip if no available plugins
        if ( availablePlugins.size () == 0 )
        {
            return;
        }

        // Splitting plugins by initial groups
        final List<T> beforeAll = new ArrayList<T> ( availablePlugins.size () );
        final List<T> middle = new ArrayList<T> ( availablePlugins.size () );
        final List<T> afterAll = new ArrayList<T> ( availablePlugins.size () );
        for ( final T plugin : availablePlugins )
        {
            final InitializationStrategy strategy = plugin.getInitializationStrategy ();
            if ( strategy.getId ().equals ( InitializationStrategy.ALL_ID ) )
            {
                switch ( strategy.getType () )
                {
                    case before:
                    {
                        beforeAll.add ( plugin );
                        break;
                    }
                    case any:
                    {
                        middle.add ( plugin );
                        break;
                    }
                    case after:
                    {
                        afterAll.add ( plugin );
                        break;
                    }
                }
            }
            else
            {
                middle.add ( plugin );
            }
        }

        if ( middle.size () == 0 )
        {
            // Combining all plugins into single list
            availablePlugins.clear ();
            availablePlugins.addAll ( beforeAll );
            availablePlugins.addAll ( afterAll );
        }
        else
        {
            // Sorting middle plugins properly
            final List<T> sortedMiddle = new ArrayList<T> ( middle );
            for ( final T plugin : middle )
            {
                final InitializationStrategy strategy = plugin.getInitializationStrategy ();
                final String id = strategy.getId ();
                if ( !plugin.getPluginInformation ().getId ().equals ( id ) )
                {
                    final int oldIndex = sortedMiddle.indexOf ( plugin );
                    for ( int index = 0; index < sortedMiddle.size (); index++ )
                    {
                        if ( sortedMiddle.get ( index ).getPluginInformation ().getId ().equals ( id ) )
                        {
                            switch ( strategy.getType () )
                            {
                                case before:
                                {
                                    sortedMiddle.remove ( oldIndex );
                                    if ( oldIndex < index )
                                    {
                                        sortedMiddle.add ( index - 1, plugin );
                                    }
                                    else
                                    {
                                        sortedMiddle.add ( index, plugin );
                                    }
                                    break;
                                }
                                case after:
                                {
                                    sortedMiddle.remove ( oldIndex );
                                    if ( oldIndex < index )
                                    {
                                        sortedMiddle.add ( index, plugin );
                                    }
                                    else
                                    {
                                        sortedMiddle.add ( index + 1, plugin );
                                    }
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }
            }

            // Combining all plugins into single list
            availablePlugins.clear ();
            availablePlugins.addAll ( beforeAll );
            availablePlugins.addAll ( sortedMiddle );
            availablePlugins.addAll ( afterAll );
        }
    }

    /**
     * Returns list of detected plugins.
     *
     * @return list of detected plugins
     */
    public List<DetectedPlugin<T>> getDetectedPlugins ()
    {
        return detectedPlugins;
    }

    /**
     * Returns list of available loaded plugins.
     *
     * @return list of available loaded plugins
     */
    public List<T> getAvailablePlugins ()
    {
        return availablePlugins;
    }

    /**
     * Returns amount of successfully loaded plugins.
     *
     * @return amount of successfully loaded plugins
     */
    public int getLoadedPluginsAmount ()
    {
        return loadedPluginsAmount;
    }

    /**
     * Returns amount of plugins which have failed to load.
     *
     * @return amount of plugins which have failed to load
     */
    public int getFailedPluginsAmount ()
    {
        return failedPluginsAmount;
    }

    /**
     * Returns plugins directory path.
     *
     * @return plugins directory path
     */
    public String getPluginsDirectoryPath ()
    {
        return pluginsDirectoryPath;
    }

    /**
     * Sets plugins directory path.
     *
     * @param path new plugins directory path
     */
    public void setPluginsDirectoryPath ( final String path )
    {
        this.pluginsDirectoryPath = path;
    }

    /**
     * Returns whether plugins directory subfolders should be checked recursively or not.
     *
     * @return true if plugins directory subfolders should be checked recursively, false otherwise
     */
    public boolean isCheckRecursively ()
    {
        return checkRecursively;
    }

    /**
     * Sets whether plugins directory subfolders should be checked recursively or not.
     *
     * @param checkRecursively whether plugins directory subfolders should be checked recursively or not
     */
    public void setCheckRecursively ( final boolean checkRecursively )
    {
        this.checkRecursively = checkRecursively;
    }

    /**
     * Returns plugins directory file filter.
     *
     * @return plugins directory file filter
     */
    public FileFilter getFileFilter ()
    {
        return fileFilter;
    }

    /**
     * Sets plugins directory file filter.
     *
     * @param fileFilter plugins directory file filter
     */
    public void setFileFilter ( final FileFilter fileFilter )
    {
        this.fileFilter = fileFilter;
    }
}