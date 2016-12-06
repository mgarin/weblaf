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

import com.alee.global.GlobalConstants;
import com.alee.managers.log.Log;
import com.alee.managers.plugin.data.*;
import com.alee.utils.*;
import com.alee.utils.compare.Filter;
import com.alee.utils.sort.GraphDataProvider;

import javax.swing.*;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Base class for any plugin manager you might want to create.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-PluginManager">How to use PluginManager</a>
 * @see com.alee.managers.plugin.Plugin
 */

public abstract class PluginManager<T extends Plugin>
{
    /**
     * Global class loader for all plugin managers implementations.
     *
     * @see #getGlobalClassLoader()
     */
    protected static PluginClassLoader globalClassLoader;

    /**
     * Local class loader for this specific plugin manager implementation.
     *
     * @see #getLocalClassLoader()
     */
    protected PluginClassLoader localClassLoader;

    /**
     * Plugins listeners.
     */
    protected List<PluginsListener<T>> listeners = new ArrayList<PluginsListener<T>> ( 1 );

    /**
     * Plugin checks lock object.
     */
    protected final Object checkLock = new Object ();

    /**
     * Related plugin managers list.
     * These managers are used to check dependencies load state and some other information later on.
     */
    protected final List<PluginManager> relatedManagers = new ArrayList<PluginManager> ();

    /**
     * Detected plugins list.
     * All plugins with available descriptions will get into this list.
     */
    protected List<DetectedPlugin<T>> detectedPlugins;

    /**
     * Detected plugins cached by plugin file path.
     */
    protected Map<String, DetectedPlugin<T>> detectedPluginsByPath = new HashMap<String, DetectedPlugin<T>> ();

    /**
     * Recently detected plugins list.
     * Contains plugins detected while last plugins check.
     */
    protected List<DetectedPlugin<T>> recentlyDetected;

    /**
     * Special filter to filter out unwanted plugins before their initialization.
     * It is up to developer to specify this filter and its conditions.
     */
    protected Filter<DetectedPlugin<T>> pluginFilter = null;

    /**
     * Whether should allow loading multiply plugins with the same ID or not.
     * In case this is set to false only the newest version of the same plugin will be loaded if more than one provided.
     */
    protected boolean allowSimilarPlugins = false;

    /**
     * Whether plugin manager logging is enabled or not.
     */
    protected boolean loggingEnabled = true;

    /**
     * Loaded and running plugins list.
     * This might be less than list of detected plugins in the end due to lots of different reasons.
     * Only those plugins which are actually loaded successfully are getting added here.
     */
    protected List<T> availablePlugins;

    /**
     * Map of plugins cached by their IDs.
     */
    protected Map<String, T> availablePluginsById = new HashMap<String, T> ();

    /**
     * Map of plugins cached by their classes.
     */
    protected Map<Class<? extends Plugin>, T> availablePluginsByClass = new HashMap<Class<? extends Plugin>, T> ();

    /**
     * Recently initialized plugins list.
     * Contains plugins initialized while last plugins check.
     */
    protected List<T> recentlyInitialized;

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
     * By default "*.jar" and "*.plugin" files are accepted.
     */
    protected FileFilter fileFilter;

    /**
     * Class loader type used by this manager to load plugins.
     * Be aware that different types might have a heavy impact on classes availability across your application.
     */
    protected ClassLoaderType classLoaderType = ClassLoaderType.context;

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
     * @param recursively          whether plugins directory subfolders should be checked recursively or not
     */
    public PluginManager ( final String pluginsDirectoryPath, final boolean recursively )
    {
        super ();

        // User settings
        this.pluginsDirectoryPath = pluginsDirectoryPath;
        this.checkRecursively = recursively;

        // Default file filter
        this.fileFilter = new FileFilter ()
        {
            @Override
            public boolean accept ( final File file )
            {
                final String name = file.getName ().toLowerCase ( Locale.ROOT );
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
    protected String getPluginDescriptorFile ()
    {
        return "plugin.xml";
    }

    /**
     * Returns name of the plugin logo file.
     * Logo should be placed near the plugin descriptor file.
     *
     * @return name of the plugin logo file
     */
    protected String getPluginLogoFile ()
    {
        return "logo.png";
    }

    /**
     * Returns accepted by this manager plugin type.
     * In case {@code null} is returned this manager accepts any plugin type.
     *
     * @return accepted by this manager plugin type
     */
    protected String getAcceptedPluginType ()
    {
        return null;
    }

    /**
     * Adds plugin manager into related managers list.
     * These managers are used to check dependencies load state and some other information later on.
     *
     * @param manager plugin manager to add into related managers list
     */
    public void addRelatedManager ( final PluginManager manager )
    {
        if ( !relatedManagers.contains ( manager ) )
        {
            relatedManagers.add ( manager );
        }
        else
        {
            Log.get ().warn ( ReflectUtils.getClassName ( manager.getClass () ) + " is already added into related managers list" );
        }
    }

    /**
     * Removes plugin manager from related managers list.
     * These managers are used to check dependencies load state and some other information later on.
     *
     * @param manager plugin manager to add into related managers list
     */
    public void removeRelatedManager ( final PluginManager manager )
    {
        relatedManagers.remove ( manager );
    }

    /**
     * Registers programmatically loaded plugin within this PluginManager.
     * This call will add the specified plugin into available plugins list.
     * It will also create a custom DetectedPlugin data based on provided information.
     *
     * @param plugin plugin to register
     */
    public void registerPlugin ( final T plugin )
    {
        registerPlugin ( plugin, plugin.getPluginInformation (), !SystemUtils.isHeadlessEnvironment () ? plugin.getPluginLogo () : null );
    }

    /**
     * Registers programmatically loaded plugin within this PluginManager.
     * This call will add the specified plugin into available plugins list.
     * It will also create a custom DetectedPlugin data based on provided information.
     *
     * @param plugin      plugin to register
     * @param information about this plugin
     * @param logo        plugin logo
     */
    public void registerPlugin ( final T plugin, final PluginInformation information, final ImageIcon logo )
    {
        final String prefix = "[" + information + "] ";
        Log.info ( this, prefix + "Initializing pre-loaded plugin..." );

        // Creating base detected plugin information
        final DetectedPlugin<T> detectedPlugin = new DetectedPlugin<T> ( null, null, information, logo );
        detectedPlugin.setStatus ( PluginStatus.loaded );
        detectedPlugin.setPlugin ( plugin );
        plugin.setPluginManager ( PluginManager.this );
        plugin.setDetectedPlugin ( detectedPlugin );

        // Saving plugin
        detectedPlugins.add ( detectedPlugin );
        availablePlugins.add ( plugin );
        availablePluginsById.put ( plugin.getId (), plugin );
        availablePluginsByClass.put ( plugin.getClass (), plugin );

        Log.info ( this, prefix + "Pre-loaded plugin initialized" );

        // Informing
        firePluginsInitialized ( Arrays.asList ( plugin ) );
    }

    /**
     * Downloads plugin from the specified URL and tries to load it.
     * In case the file is not a plugin it will simply be ignored.
     * Plugins added this way will also be filtered and checked for other means.
     *
     * @param pluginFileURL plugin file URL
     */
    public void scanPlugin ( final URL pluginFileURL )
    {
        try
        {
            final String url = pluginFileURL.toURI ().toASCIIString ();
            final File tmpFile = File.createTempFile ( "plugin", ".jar" );
            final File downloadedPlugin = FileUtils.downloadFile ( url, tmpFile );
            scanPlugin ( downloadedPlugin );
        }
        catch ( final URISyntaxException e )
        {
            Log.error ( this, "Unable to parse plugin URL", e );
        }
        catch ( final IOException e )
        {
            Log.error ( this, "Unable to create local file to download plugin", e );
        }
    }

    /**
     * Tries to load plugin from the specified file.
     * In case the file is not a plugin it will simply be ignored.
     * Plugins added this way will also be filtered and checked for other means.
     *
     * @param pluginFile plugin file path
     */
    public void scanPlugin ( final String pluginFile )
    {
        scanPlugin ( new File ( pluginFile ) );
    }

    /**
     * Tries to load plugin from the specified file.
     * In case the file is not a plugin it will simply be ignored.
     * Plugins added this way will also be filtered and checked for other means.
     *
     * @param pluginFile plugin file
     */
    public void scanPlugin ( final File pluginFile )
    {
        synchronized ( checkLock )
        {
            Log.info ( this, "Scanning plugin file: " + FileUtils.canonicalPath ( pluginFile ) );

            // Resetting recently detected plugins list
            recentlyDetected = new ArrayList<DetectedPlugin<T>> ();

            // Collecting plugins information
            if ( collectPluginInformation ( pluginFile ) )
            {
                // Initializing detected plugins
                initializeDetectedPlugins ();
            }
        }
    }

    /**
     * Performs plugins search within the specified plugins directory.
     * This call might be performed as many times as you like.
     * It will simply ignore plugins detected before and will process newly found plugins appropriately.
     */
    public void scanPluginsDirectory ()
    {
        scanPluginsDirectory ( pluginsDirectoryPath, checkRecursively );
    }

    /**
     * Performs plugins search within the specified plugins directory.
     * This call might be performed as many times as you like.
     * It will simply ignore plugins detected before and will process newly found plugins appropriately.
     *
     * @param recursively whether plugins directory subfolders should be checked recursively or not
     */
    public void scanPluginsDirectory ( final boolean recursively )
    {
        scanPluginsDirectory ( pluginsDirectoryPath, recursively );
    }

    /**
     * Performs plugins search within the specified plugins directory.
     * This call might be performed as many times as you like.
     * It will simply ignore plugins detected before and will process newly found plugins appropriately.
     *
     * @param pluginsDirectoryPath plugins directory path
     */
    public void scanPluginsDirectory ( final String pluginsDirectoryPath )
    {
        scanPluginsDirectory ( pluginsDirectoryPath, checkRecursively );
    }

    /**
     * Performs plugins search within the specified plugins directory.
     * This call might be performed as many times as you like.
     * It will simply ignore plugins detected before and will process newly found plugins appropriately.
     *
     * @param pluginsDirectoryPath plugins directory path
     * @param recursively          whether plugins directory subfolders should be checked recursively or not
     */
    public void scanPluginsDirectory ( final String pluginsDirectoryPath, final boolean recursively )
    {
        synchronized ( checkLock )
        {
            // Ignore check if check path is not specified
            if ( pluginsDirectoryPath == null )
            {
                return;
            }

            // Informing about plugins check start
            firePluginsCheckStarted ( pluginsDirectoryPath, recursively );

            // Resetting recently detected plugins list
            recentlyDetected = new ArrayList<DetectedPlugin<T>> ();

            // Collecting plugins information
            if ( collectPluginsInformation ( pluginsDirectoryPath, recursively ) )
            {
                // Initializing detected plugins
                initializeDetectedPlugins ();
            }

            // Informing about plugins check end
            firePluginsCheckEnded ( pluginsDirectoryPath, recursively );
        }
    }

    /**
     * Collects information about available plugins.
     *
     * @param dir         plugins directory path
     * @param recursively whether plugins directory subfolders should be checked recursively or not
     * @return true if operation succeeded, false otherwise
     */
    protected boolean collectPluginsInformation ( final String dir, final boolean recursively )
    {
        if ( dir != null )
        {
            collectPluginsInformationImpl ( new File ( dir ), recursively );
            sortRecentlyDetectedPluginsByDependencies ();
            return true;
        }
        else
        {
            Log.warn ( this, "Plugins directory is not yet specified" );
            return false;
        }
    }

    /**
     * Collects information about available plugins.
     *
     * @param dir         plugins directory
     * @param recursively whether plugins directory subfolders should be checked recursively or not
     */
    protected void collectPluginsInformationImpl ( final File dir, final boolean recursively )
    {
        Log.info ( this, "Scanning plugins directory" + ( recursively ? " recursively" : "" ) + ": " + pluginsDirectoryPath );

        // Checking all files
        final File[] files = dir.listFiles ( getFileFilter () );
        if ( files != null )
        {
            for ( final File file : files )
            {
                collectPluginInformation ( file );
            }
        }

        // Checking sub-directories recursively
        if ( recursively )
        {
            final File[] subfolders = dir.listFiles ( GlobalConstants.DIRECTORIES_FILTER );
            if ( subfolders != null )
            {
                for ( final File subfolder : subfolders )
                {
                    collectPluginsInformationImpl ( subfolder, recursively );
                }
            }
        }
    }

    /**
     * Tries to collect plugin information from the specified file.
     * This call will simply be ignored if this is not a plugin file or if something goes wrong.
     *
     * @param file plugin file to process
     * @return true if operation succeeded, false otherwise
     */
    protected boolean collectPluginInformation ( final File file )
    {
        final DetectedPlugin<T> plugin = getPluginInformation ( file );
        if ( plugin != null )
        {
            recentlyDetected.add ( plugin );
            Log.info ( this, "Plugin detected: " + plugin );
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Tries to sort recently detected plugins list by known plugin dependencies.
     * This sorting will have effect only if dependencies are pointing at plugins of the same type.
     * <p>
     * In case you setup dependencies on other type of plugin you will have to manually check whether those are loaded or not.
     * That can be done by setting plugin filter into this manager and checking dependencies there.
     */
    protected void sortRecentlyDetectedPluginsByDependencies ()
    {
        if ( recentlyDetected.size () > 1 )
        {
            try
            {
                Log.info ( this, "Sorting detected plugins according to known dependencies" );

                // Collecting plugins that doesn't have any dependencies or their dependencies are loaded
                // Also mapping dependencies for quick access later
                final int s = recentlyDetected.size ();
                final List<DetectedPlugin<T>> root = new ArrayList<DetectedPlugin<T>> ( s );
                final Map<String, List<DetectedPlugin<T>>> references = new HashMap<String, List<DetectedPlugin<T>>> ();
                for ( final DetectedPlugin<T> plugin : recentlyDetected )
                {
                    final List<PluginDependency> dependencies = plugin.getInformation ().getDependencies ();
                    if ( dependencies != null )
                    {
                        // Iterating through detected plugin dependencies
                        boolean dependenciesMet = true;
                        for ( final PluginDependency dependency : dependencies )
                        {
                            // Checking whether or not each dependency is met
                            boolean met = false;
                            for ( final T availablePlugin : availablePlugins )
                            {
                                // todo This is a bad optional workaround, it should actually take part in sorting
                                if ( dependency.isOptional () || dependency.accept ( availablePlugin ) )
                                {
                                    met = true;
                                    break;
                                }
                            }
                            if ( !met )
                            {
                                dependenciesMet = false;
                            }

                            // Mapping dependency references
                            final String id = dependency.getPluginId ();
                            List<DetectedPlugin<T>> ref = references.get ( id );
                            if ( ref == null )
                            {
                                ref = new ArrayList<DetectedPlugin<T>> ( 1 );
                                references.put ( id, ref );
                            }
                            ref.add ( plugin );
                        }

                        // Adding plugin into root plugins list if its dependencies are met
                        if ( dependenciesMet )
                        {
                            root.add ( plugin );
                        }
                    }
                    else
                    {
                        // Adding plugin into root plugins list as it doesn't have any dependencies
                        root.add ( plugin );
                    }
                }

                // Creating graph provider for further topological sorting
                final GraphDataProvider<DetectedPlugin<T>> graphDataProvider = new GraphDataProvider<DetectedPlugin<T>> ()
                {
                    @Override
                    public List<DetectedPlugin<T>> getRoots ()
                    {
                        return root;
                    }

                    @Override
                    public List<DetectedPlugin<T>> getChildren ( final DetectedPlugin<T> data )
                    {
                        final List<DetectedPlugin<T>> children = references.get ( data.getInformation ().getId () );
                        return children != null ? children : Collections.EMPTY_LIST;
                    }
                };

                // Performing topological sorting
                // Saving result as new recently detected plugins list
                final List<DetectedPlugin<T>> sorted = SortUtils.doTopologicalSort ( graphDataProvider );

                // Adding plugins which didn't get into graph into the end
                // There might be such plugin for example in case it has some side dependencies
                // It might still be properly initialized, we just don't know anything about its dependencies
                // Such plugins should be initialized after anything else to increase their chances
                for ( final DetectedPlugin<T> plugin : recentlyDetected )
                {
                    if ( !sorted.contains ( plugin ) )
                    {
                        sorted.add ( plugin );
                    }
                }

                recentlyDetected = sorted;
            }
            catch ( final Throwable e )
            {
                Log.warn ( this, "Unable to perform proper dependencies sorting", e );
            }
        }
    }

    /**
     * Returns plugin information from the specified plugin file.
     * Returns null in case plugin file cannot be read or if it is incorrect.
     *
     * @param file plugin file to process
     * @return plugin information from the specified plugin file or null
     */
    protected DetectedPlugin<T> getPluginInformation ( final File file )
    {
        try
        {
            final String pluginDescriptor = getPluginDescriptorFile ();
            final String pluginLogo = getPluginLogoFile ();
            final ZipFile zipFile = new ZipFile ( file );
            final Enumeration entries = zipFile.entries ();
            while ( entries.hasMoreElements () )
            {
                final ZipEntry entry = ( ZipEntry ) entries.nextElement ();
                if ( entry.getName ().endsWith ( pluginDescriptor ) )
                {
                    // Reading plugin information
                    final InputStream inputStream = zipFile.getInputStream ( entry );
                    final PluginInformation info = XmlUtils.fromXML ( inputStream );
                    inputStream.close ();

                    // Reading plugin icon
                    final ImageIcon logo;
                    if ( !SystemUtils.isHeadlessEnvironment () )
                    {
                        final ZipEntry logoEntry = new ZipEntry ( ZipUtils.getZipEntryFileLocation ( entry ) + pluginLogo );
                        final InputStream logoInputStream = zipFile.getInputStream ( logoEntry );
                        if ( logoInputStream != null )
                        {
                            logo = ImageUtils.loadImage ( logoInputStream );
                            logoInputStream.close ();
                        }
                        else
                        {
                            logo = null;
                        }
                    }
                    else
                    {
                        logo = null;
                    }

                    // Checking whether we have already detected this plugin or not
                    if ( !wasDetected ( file.getParent (), file.getName () ) )
                    {
                        // Cache and return new plugin information
                        // This cache map is filled here since it has different usage cases
                        final DetectedPlugin<T> plugin = new DetectedPlugin<T> ( file.getParent (), file.getName (), info, logo );
                        detectedPluginsByPath.put ( FileUtils.canonicalPath ( file ), plugin );
                        return plugin;
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
        return null;
    }

    /**
     * Returns plugin information from the specified plugin file.
     *
     * @param file plugin file to process
     * @return plugin information from the specified plugin file
     */
    public DetectedPlugin<T> getDetectedPlugin ( final File file )
    {
        if ( file == null )
        {
            return null;
        }
        final String path = FileUtils.canonicalPath ( file );
        if ( detectedPluginsByPath.containsKey ( path ) )
        {
            // Cached plugin information
            return detectedPluginsByPath.get ( path );
        }
        else
        {
            // Loading plugin information
            return getPluginInformation ( file );
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
            if ( plugin.getPluginFileName () != null && plugin.getPluginFolder ().equals ( pluginFolder ) &&
                    plugin.getPluginFileName ().equals ( pluginFile ) )
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Initializes earlier detected plugins.
     * Also informs listeners about appropriate events.
     */
    protected void initializeDetectedPlugins ()
    {
        if ( !recentlyDetected.isEmpty () )
        {
            // Informing about newly detected plugins
            firePluginsDetected ( recentlyDetected );

            Log.info ( this, "Initializing plugins..." );

            // Initializing plugins
            initializeDetectedPluginsImpl ();

            // Sorting plugins according to their initialization strategies
            applyInitializationStrategy ();

            // Properly sorting recently initialized plugins
            Collections.sort ( recentlyInitialized, new Comparator<T> ()
            {
                @Override
                public int compare ( final T o1, final T o2 )
                {
                    final Integer i1 = availablePlugins.indexOf ( o1 );
                    final Integer i2 = availablePlugins.indexOf ( o2 );
                    return i1.compareTo ( i2 );
                }
            } );

            // Informing about new plugins initialization
            firePluginsInitialized ( recentlyInitialized );

            Log.info ( this, "Plugins initialization finished" );
        }
        else
        {
            Log.info ( this, "No new plugins found" );
        }
    }

    /**
     * Initializes earlier detected plugins.
     */
    protected void initializeDetectedPluginsImpl ()
    {
        // Map to store plugin libraries
        final Map<String, Map<PluginLibrary, PluginInformation>> pluginLibraries =
                new HashMap<String, Map<PluginLibrary, PluginInformation>> ();

        // List to collect newly initialized plugins
        // This is required to properly inform about newly loaded plugins later
        recentlyInitialized = new ArrayList<T> ();

        // Adding recently detected into the end of the detected plugins list
        detectedPlugins.addAll ( recentlyDetected );

        // Initializing detected plugins
        final String acceptedPluginType = getAcceptedPluginType ();
        for ( final DetectedPlugin<T> dp : detectedPlugins )
        {
            // Skip plugins we have already tried to initialize
            if ( dp.getStatus () != PluginStatus.detected )
            {
                continue;
            }

            final File pluginFile = dp.getFile ();
            final PluginInformation info = dp.getInformation ();
            final String prefix = "[" + FileUtils.getRelativePath ( pluginFile, new File ( pluginsDirectoryPath ) ) + "] [" + info + "] ";
            try
            {
                // Starting to load plugin now
                Log.info ( this, prefix + "Initializing plugin..." );
                dp.setStatus ( PluginStatus.loading );

                // Checking plugin type as we don't want (for example) to load server plugins on client side
                if ( acceptedPluginType != null && ( info.getType () == null || !info.getType ().equals ( acceptedPluginType ) ) )
                {
                    Log.error ( this, prefix + "Plugin of type \"" + info.getType () + "\" cannot be loaded, " +
                            "required plugin type is \"" + acceptedPluginType + "\"" );
                    dp.setStatus ( PluginStatus.failed );
                    dp.setFailureCause ( "Wrong type" );
                    dp.setExceptionMessage ( "Detected plugin type: " + info.getType () + "\", " +
                            "required plugin type: \"" + acceptedPluginType + "\"" );
                    continue;
                }

                // Checking that this is latest plugin version of all available
                // Usually there shouldn't be different versions of the same plugin but everyone make mistakes
                if ( isDeprecatedVersion ( dp ) )
                {
                    Log.warn ( this, prefix + "This plugin is deprecated, newer version loaded instead" );
                    dp.setStatus ( PluginStatus.failed );
                    dp.setFailureCause ( "Deprecated" );
                    dp.setExceptionMessage ( "This plugin is deprecated, newer version loaded instead" );
                    continue;
                }

                // Checking that this plugin version is not yet loaded
                // This might occur in case the same plugin appears more than once in different files
                if ( isSameVersionAlreadyLoaded ( dp, detectedPlugins ) )
                {
                    Log.warn ( this, prefix + "Plugin is duplicate, it will be loaded from another file" );
                    dp.setStatus ( PluginStatus.failed );
                    dp.setFailureCause ( "Duplicate" );
                    dp.setExceptionMessage ( "This plugin is duplicate, it will be loaded from another file" );
                    continue;
                }

                // Checking that plugin filter accepts this plugin
                if ( getPluginFilter () != null && !getPluginFilter ().accept ( dp ) )
                {
                    Log.info ( this, prefix + "Plugin was not accepted by plugin filter" );
                    dp.setStatus ( PluginStatus.failed );
                    dp.setFailureCause ( "Filtered" );
                    dp.setExceptionMessage ( "Plugin was not accepted by plugin filter" );
                    continue;
                }

                // Checking plugin dependencies
                final List<PluginDependency> dependencies = dp.getInformation ().getDependencies ();
                if ( dependencies != null )
                {
                    for ( final PluginDependency dependency : dependencies )
                    {
                        // Checking whether or not dependency is mandatory and whether or not it is available
                        final String did = dependency.getPluginId ();
                        if ( !dependency.isOptional () && !isPluginAvailable ( did ) )
                        {
                            // If it is mandatory and not available - check related managers for that dependency
                            boolean available = false;
                            for ( final PluginManager relatedManager : relatedManagers )
                            {
                                if ( relatedManager.isPluginAvailable ( did ) )
                                {
                                    available = true;
                                    break;
                                }
                            }
                            if ( !available )
                            {
                                Log.error ( this, prefix + "Mandatory plugin dependency was not found: " + did );
                                dp.setStatus ( PluginStatus.failed );
                                dp.setFailureCause ( "Incomplete" );
                                dp.setExceptionMessage ( "Mandatory plugin dependency was not found: " + did );
                                break;
                            }
                        }
                    }
                    if ( dp.getStatus () == PluginStatus.failed )
                    {
                        continue;
                    }
                }

                // Collecting plugin and its libraries JAR paths
                final List<URL> jarPaths = new ArrayList<URL> ( 1 + info.getLibrariesCount () );
                jarPaths.add ( pluginFile.toURI ().toURL () );
                if ( info.getLibraries () != null )
                {
                    for ( final PluginLibrary library : info.getLibraries () )
                    {
                        final File file = new File ( dp.getPluginFolder (), library.getFile () );
                        if ( file.exists () )
                        {
                            // Adding library URI to path
                            jarPaths.add ( file.toURI ().toURL () );

                            // Saving library information for further checks
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
                            Log.error ( this, prefix + "Plugin library was not found: " + file.getAbsolutePath () );
                            dp.setStatus ( PluginStatus.failed );
                            dp.setFailureCause ( "Incomplete" );
                            dp.setExceptionMessage ( "Plugin library was not found: " + file.getAbsolutePath () );
                            break;
                        }
                    }
                    if ( dp.getStatus () == PluginStatus.failed )
                    {
                        continue;
                    }
                }

                try
                {
                    // Choosing class loader
                    final ClassLoader cl;
                    switch ( classLoaderType )
                    {
                        case system:
                        {
                            cl = ClassLoader.getSystemClassLoader ();
                            break;
                        }
                        case context:
                        default:
                        {
                            final ClassLoader ccl = Thread.currentThread ().getContextClassLoader ();
                            cl = ccl != null ? ccl : getClass ().getClassLoader ();
                            break;
                        }
                        case global:
                        {
                            cl = getGlobalClassLoader ();
                            break;
                        }
                        case local:
                        {
                            cl = getLocalClassLoader ();
                            break;
                        }
                        case separate:
                        {
                            cl = createPluginClassLoader ( new URL[ 0 ] );
                            break;
                        }
                    }

                    // Obtaining {@link URLClassLoader}
                    final URLClassLoader classLoader;
                    if ( cl instanceof URLClassLoader )
                    {
                        // Use current class loader
                        classLoader = ( URLClassLoader ) cl;
                    }
                    else
                    {
                        // Create new class loader
                        classLoader = new PluginClassLoader ( jarPaths.toArray ( new URL[ jarPaths.size () ] ), cl );
                    }

                    // Adding all plugin paths
                    for ( final URL url : jarPaths )
                    {
                        ReflectUtils.callMethodSafely ( classLoader, "addURL", url );
                    }

                    // Loading plugin
                    final Class<?> pluginClass = classLoader.loadClass ( info.getMainClass () );
                    final T plugin = ReflectUtils.createInstance ( pluginClass );
                    plugin.setPluginManager ( PluginManager.this );
                    plugin.setDetectedPlugin ( dp );

                    // Saving initialized plugin
                    availablePlugins.add ( plugin );
                    availablePluginsById.put ( plugin.getId (), plugin );
                    availablePluginsByClass.put ( plugin.getClass (), plugin );
                    recentlyInitialized.add ( plugin );

                    // Updating detected plugin status
                    Log.info ( this, prefix + "Plugin initialized" );
                    dp.setStatus ( PluginStatus.loaded );
                    dp.setPlugin ( plugin );
                }
                catch ( final Throwable e )
                {
                    // Something happened while performing plugin class load
                    Log.error ( this, prefix + "Unable to initialize plugin", e );
                    dp.setStatus ( PluginStatus.failed );
                    dp.setFailureCause ( "Internal exception" );
                    dp.setException ( e );
                }
            }
            catch ( final Throwable e )
            {
                // Something happened while checking plugin information
                Log.error ( this, prefix + "Unable to initialize plugin data", e );
                dp.setStatus ( PluginStatus.failed );
                dp.setFailureCause ( "Data exception" );
                dp.setException ( e );
            }
        }

        // Checking for same/similar libraries used within plugins
        boolean sameLibrariesInPlugins = false;
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
                sameLibrariesInPlugins = true;
                break;
            }
        }
        if ( sameLibrariesInPlugins )
        {
            Log.warn ( this, "Make sure that the same library usage within different plugins was actually your intent" );
        }
    }

    /**
     * Returns whether the list of detected plugins contain a newer version of the specified plugin or not.
     *
     * @param plugin plugin to compare with other detected plugins
     * @return true if the list of detected plugins contain a newer version of the specified plugin, false otherwise
     */
    public boolean isDeprecatedVersion ( final DetectedPlugin<T> plugin )
    {
        return isDeprecatedVersion ( plugin, detectedPlugins );
    }

    /**
     * Returns whether the list of detected plugins contain a newer version of the specified plugin or not.
     *
     * @param plugin          plugin to compare with other detected plugins
     * @param detectedPlugins list of detected plugins
     * @return true if the list of detected plugins contain a newer version of the specified plugin, false otherwise
     */
    public boolean isDeprecatedVersion ( final DetectedPlugin<T> plugin, final List<DetectedPlugin<T>> detectedPlugins )
    {
        final PluginInformation pluginInfo = plugin.getInformation ();
        for ( final DetectedPlugin detectedPlugin : detectedPlugins )
        {
            if ( detectedPlugin != plugin )
            {
                final PluginInformation detectedPluginInfo = detectedPlugin.getInformation ();
                if ( detectedPluginInfo.getId ().equals ( pluginInfo.getId () ) &&
                        detectedPluginInfo.getVersion () != null && pluginInfo.getVersion () != null &&
                        detectedPluginInfo.getVersion ().isNewerThan ( pluginInfo.getVersion () ) )
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns whether the list of detected plugins contain the same version of the specified plugin or not.
     *
     * @param plugin          plugin to compare with other detected plugins
     * @param detectedPlugins list of detected plugins
     * @return true if the list of detected plugins contain the same version of the specified plugin, false otherwise
     */
    protected boolean isSameVersionAlreadyLoaded ( final DetectedPlugin<T> plugin, final List<DetectedPlugin<T>> detectedPlugins )
    {
        final PluginInformation pluginInfo = plugin.getInformation ();
        for ( final DetectedPlugin detectedPlugin : detectedPlugins )
        {
            if ( detectedPlugin != plugin )
            {
                final PluginInformation detectedPluginInfo = detectedPlugin.getInformation ();
                if ( detectedPluginInfo.getId ().equals ( pluginInfo.getId () ) &&
                        ( detectedPluginInfo.getVersion () == null && pluginInfo.getVersion () == null ||
                                detectedPluginInfo.getVersion ().isSame ( pluginInfo.getVersion () ) ) &&
                        detectedPlugin.getStatus () == PluginStatus.loaded )
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

        // todo Take plugin dependencies into account with top priority here

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

        // Sorting plugins in appropriate order
        // This order is not used by PluginManager itself due to possible unstructured plugin loading
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
                if ( !plugin.getId ().equals ( id ) )
                {
                    final int oldIndex = sortedMiddle.indexOf ( plugin );
                    for ( int index = 0; index < sortedMiddle.size (); index++ )
                    {
                        if ( sortedMiddle.get ( index ).getId ().equals ( id ) )
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
        return CollectionUtils.copy ( detectedPlugins );
    }

    /**
     * Returns list of available loaded plugins.
     *
     * @return list of available loaded plugins
     */
    public List<T> getAvailablePlugins ()
    {
        return CollectionUtils.copy ( availablePlugins );
    }

    /**
     * Returns available plugin instance by its ID.
     *
     * @param pluginId plugin ID
     * @return available plugin instance by its ID
     */
    public <P extends T> P getPlugin ( final String pluginId )
    {
        return ( P ) availablePluginsById.get ( pluginId );
    }

    /**
     * Returns whether plugin is available or not.
     *
     * @param pluginId plugin ID
     * @return true if plugin is available, false otherwise
     */
    public boolean isPluginAvailable ( final String pluginId )
    {
        return getPlugin ( pluginId ) != null;
    }

    /**
     * Returns available plugin instance by its class.
     *
     * @param pluginClass plugin class
     * @return available plugin instance by its class
     */
    public <P extends T> P getPlugin ( final Class<P> pluginClass )
    {
        return ( P ) availablePluginsByClass.get ( pluginClass );
    }

    /**
     * Returns amount of detected plugins.
     *
     * @return amount of detected loaded plugins
     */
    public int getDetectedPluginsAmount ()
    {
        return getDetectedPlugins ().size ();
    }

    /**
     * Returns amount of successfully loaded plugins.
     *
     * @return amount of successfully loaded plugins
     */
    public int getLoadedPluginsAmount ()
    {
        return getAvailablePlugins ().size ();
    }

    /**
     * Returns amount of plugins which have failed to load.
     * There might be a lot of reasons why they failed to load - exception, broken JAR, missing libraries etc.
     * Simply check the log or retrieve failure cause from DetectedPlugin to understand what happened.
     *
     * @return amount of plugins which have failed to load
     */
    public int getFailedPluginsAmount ()
    {
        return getDetectedPlugins ().size () - getAvailablePlugins ().size ();
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
     * Note that setting this filter will not have any effect on plugins which are already initialized.
     *
     * @param filter plugins directory file filter
     */
    public void setFileFilter ( final FileFilter filter )
    {
        this.fileFilter = filter;
    }

    /**
     * Returns class loader type used by this manager to load plugins.
     *
     * @return class loader type used by this manager to load plugins
     */
    public ClassLoaderType getClassLoaderType ()
    {
        return classLoaderType;
    }

    /**
     * Sets class loader type used by this manager to load plugins.
     *
     * @param classLoaderType class loader type used by this manager to load plugins
     */
    public void setClassLoaderType ( final ClassLoaderType classLoaderType )
    {
        this.classLoaderType = classLoaderType;
    }

    /**
     * Returns special filter that filters out unwanted plugins before their initialization.
     *
     * @return special filter that filters out unwanted plugins before their initialization
     */
    public Filter<DetectedPlugin<T>> getPluginFilter ()
    {
        return pluginFilter;
    }

    /**
     * Sets special filter that filters out unwanted plugins before their initialization.
     *
     * @param pluginFilter special filter that filters out unwanted plugins before their initialization
     */
    public void setPluginFilter ( final Filter<DetectedPlugin<T>> pluginFilter )
    {
        this.pluginFilter = pluginFilter;
    }

    /**
     * Returns whether should allow loading multiply plugins with the same ID or not.
     *
     * @return true if should allow loading multiply plugins with the same ID, false otherwise
     */
    public boolean isAllowSimilarPlugins ()
    {
        return allowSimilarPlugins;
    }

    /**
     * Sets whether should allow loading multiply plugins with the same ID or not.
     *
     * @param allow whether should allow loading multiply plugins with the same ID or not
     */
    public void setAllowSimilarPlugins ( final boolean allow )
    {
        this.allowSimilarPlugins = allow;
    }

    /**
     * Returns whether plugin manager logging is enabled or not.
     *
     * @return true if plugin manager logging is enabled, false otherwise
     */
    public boolean isLoggingEnabled ()
    {
        return loggingEnabled;
    }

    /**
     * Sets whether plugin manager logging is enabled or not.
     *
     * @param loggingEnabled whether plugin manager logging is enabled or not
     */
    public void setLoggingEnabled ( final boolean loggingEnabled )
    {
        this.loggingEnabled = loggingEnabled;
        Log.setLoggingEnabled ( this, loggingEnabled );
    }

    /**
     * Adds plugins listener.
     *
     * @param listener new plugins listener
     */
    public void addPluginsListener ( final PluginsListener<T> listener )
    {
        listeners.add ( listener );
    }

    /**
     * Adds plugins scan start event action.
     *
     * @param runnable action to perform
     * @return added plugins listener
     */
    public PluginsAdapter<T> onPluginsScanStart ( final DirectoryRunnable runnable )
    {
        final PluginsAdapter<T> listener = new PluginsAdapter<T> ()
        {
            @Override
            public void pluginsCheckStarted ( final String directory, final boolean recursive )
            {
                runnable.run ( directory, recursive );
            }
        };
        addPluginsListener ( listener );
        return listener;
    }

    /**
     * Adds plugins scan end event action.
     *
     * @param runnable action to perform
     * @return added plugins listener
     */
    public PluginsAdapter<T> onPluginsScanEnd ( final DirectoryRunnable runnable )
    {
        final PluginsAdapter<T> listener = new PluginsAdapter<T> ()
        {
            @Override
            public void pluginsCheckEnded ( final String directory, final boolean recursive )
            {
                runnable.run ( directory, recursive );
            }
        };
        addPluginsListener ( listener );
        return listener;
    }

    /**
     * Adds plugins detection event action.
     *
     * @param runnable action to perform
     * @return added plugins listener
     */
    public PluginsAdapter<T> onPluginsDetection ( final DetectedPluginsRunnable<T> runnable )
    {
        final PluginsAdapter<T> listener = new PluginsAdapter<T> ()
        {
            @Override
            public void pluginsDetected ( final List<DetectedPlugin<T>> detectedPlugins )
            {
                runnable.run ( detectedPlugins );
            }
        };
        addPluginsListener ( listener );
        return listener;
    }

    /**
     * Adds plugins initialization end event action.
     *
     * @param runnable action to perform
     * @return added plugins listener
     */
    public PluginsAdapter<T> onPluginsInitialization ( final PluginsRunnable<T> runnable )
    {
        final PluginsAdapter<T> listener = new PluginsAdapter<T> ()
        {
            @Override
            public void pluginsInitialized ( final List<T> plugins )
            {
                runnable.run ( plugins );
            }
        };
        addPluginsListener ( listener );
        return listener;
    }

    /**
     * Removes plugins listener.
     *
     * @param listener plugins listener to remove
     */
    public void removePluginsListener ( final PluginsListener<T> listener )
    {
        listeners.remove ( listener );
    }

    /**
     * Informs about plugins check operation start.
     *
     * @param directory checked plugins directory path
     * @param recursive whether plugins directory subfolders are checked recursively or not
     */
    public void firePluginsCheckStarted ( final String directory, final boolean recursive )
    {
        for ( final PluginsListener<T> listener : CollectionUtils.copy ( listeners ) )
        {
            listener.pluginsCheckStarted ( directory, recursive );
        }
    }

    /**
     * Informs about plugins check operation end.
     *
     * @param directory checked plugins directory path
     * @param recursive whether plugins directory subfolders are checked recursively or not
     */
    public void firePluginsCheckEnded ( final String directory, final boolean recursive )
    {
        for ( final PluginsListener<T> listener : CollectionUtils.copy ( listeners ) )
        {
            listener.pluginsCheckEnded ( directory, recursive );
        }
    }

    /**
     * Informs about newly detected plugins.
     *
     * @param plugins newly detected plugins list
     */
    public void firePluginsDetected ( final List<DetectedPlugin<T>> plugins )
    {
        for ( final PluginsListener<T> listener : CollectionUtils.copy ( listeners ) )
        {
            listener.pluginsDetected ( CollectionUtils.copy ( plugins ) );
        }
    }

    /**
     * Informs about newly initialized plugins.
     *
     * @param plugins newly initialized plugins list
     */
    public void firePluginsInitialized ( final List<T> plugins )
    {
        for ( final PluginsListener<T> listener : CollectionUtils.copy ( listeners ) )
        {
            listener.pluginsInitialized ( CollectionUtils.copy ( plugins ) );
        }
    }

    /**
     * Returns local class loader for this specific plugin manager implementation.
     *
     * @return local class loader for this specific plugin manager implementation
     */
    protected PluginClassLoader getLocalClassLoader ()
    {
        if ( localClassLoader == null )
        {
            synchronized ( this )
            {
                if ( localClassLoader == null )
                {
                    localClassLoader = createPluginClassLoader ( new URL[ 0 ] );
                }
            }
        }
        return localClassLoader;
    }

    /**
     * Returns global class loader for all plugin managers implementations.
     *
     * @return global class loader for all plugin managers implementations
     */
    protected PluginClassLoader getGlobalClassLoader ()
    {
        synchronized ( PluginManager.class )
        {
            if ( globalClassLoader == null )
            {
                globalClassLoader = createPluginClassLoader ( new URL[ 0 ] );
            }
        }
        return globalClassLoader;
    }

    /**
     * Returns new plugin class loader for this specific plugin manager implementation.
     *
     * @param classpath class loader classpath
     * @return new plugin class loader for this specific plugin manager implementation
     */
    protected PluginClassLoader createPluginClassLoader ( final URL[] classpath )
    {
        return new PluginClassLoader ( classpath, PluginManager.class.getClassLoader () );
    }
}