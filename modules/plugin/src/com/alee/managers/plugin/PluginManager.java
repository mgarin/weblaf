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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
import com.alee.managers.plugin.data.*;
import com.alee.utils.*;
import com.alee.utils.collection.ImmutableList;
import com.alee.utils.compare.Filter;
import com.alee.utils.sort.TopologicalGraphProvider;
import com.alee.utils.sort.TopologicalSorter;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * {@link PluginManager} for any {@link Plugin} of specific type.
 *
 * @param <P> {@link Plugin} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-PluginManager">How to use PluginManager</a>
 * @see Plugin
 */
public abstract class PluginManager<P extends Plugin>
{
    /**
     * todo 1. Replace {@link #applyInitializationStrategy()} this with a custom comparator for listener events
     */

    /**
     * {@link FileFilter} used to filter out directories only.
     */
    @NotNull
    protected static final FileFilter DIRECTORIES_FILTER = new FileFilter ()
    {
        @Override
        public boolean accept ( @NotNull final File file )
        {
            return file.isDirectory ();
        }
    };

    /**
     * Plugin checks lock object.
     */
    @NotNull
    protected final Object checkLock;

    /**
     * Global class loader for all plugin managers implementations.
     *
     * @see #getGlobalClassLoader()
     */
    @Nullable
    protected static PluginClassLoader globalClassLoader;

    /**
     * Local class loader for this specific plugin manager implementation.
     *
     * @see #getLocalClassLoader()
     */
    @Nullable
    protected PluginClassLoader localClassLoader;

    /**
     * Plugins listeners.
     */
    @NotNull
    protected final List<PluginsListener<P>> listeners;

    /**
     * Related plugin managers list.
     * These managers are used to check dependencies load state and some other information later on.
     */
    @NotNull
    protected final List<PluginManager> parentManagers;

    /**
     * Detected plugins list.
     * All plugins with available descriptions will get into this list.
     */
    @NotNull
    protected final List<DetectedPlugin<P>> detectedPlugins;

    /**
     * Detected plugins cached by plugin file path.
     */
    @NotNull
    protected final Map<String, DetectedPlugin<P>> detectedPluginsByPath;

    /**
     * Recently detected plugins list.
     * Contains plugins detected while last plugins check.
     */
    @NotNull
    protected final List<DetectedPlugin<P>> recentlyDetected;

    /**
     * Recently initialized plugins list.
     * Contains plugins initialized while last plugins check.
     */
    @NotNull
    protected final List<P> recentlyInitialized;

    /**
     * Loaded and running plugins list.
     * This might be less than list of detected plugins in the end due to lots of different reasons.
     * Only those plugins which are actually loaded successfully are getting added here.
     */
    @NotNull
    protected final List<P> availablePlugins;

    /**
     * Map of plugins cached by their IDs.
     */
    @NotNull
    protected final Map<String, P> availablePluginsById;

    /**
     * Map of plugins cached by their classes.
     */
    @NotNull
    protected final Map<Class<? extends Plugin>, P> availablePluginsByClass;

    /**
     * Special filter to filter out unwanted plugins before their initialization.
     * It is up to developer to specify this filter and its conditions.
     */
    @Nullable
    protected Filter<DetectedPlugin<P>> pluginFilter;

    /**
     * Whether should allow loading multiple plugins with the same ID or not.
     * In case this is set to false only the newest version of the same plugin will be loaded if more than one provided.
     */
    protected boolean allowSimilarPlugins;

    /**
     * Plugins directory path.
     * It is either absolute path or relative to working directory path.
     */
    @Nullable
    protected String pluginsDirectoryPath;

    /**
     * Whether plugins directory subfolders should be checked recursively or not.
     */
    protected boolean checkRecursively;

    /**
     * Plugin directory files filter.
     * By default "*.jar" and "*.plugin" files are accepted.
     */
    @Nullable
    protected FileFilter fileFilter;

    /**
     * Class loader type used by this manager to load plugins.
     * Be aware that different types might have a heavy impact on classes availability across your application.
     */
    @NotNull
    protected ClassLoaderType classLoaderType;

    /**
     * Whether or not {@link PluginManager} should provide a fallback {@link ClassLoader} in cases when classpath of the
     * {@link ClassLoader} retrieved according to the specified {@link ClassLoaderType} cannot be modified in runtime.
     */
    protected boolean provideClassLoaderFallback;

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
    public PluginManager ( @Nullable final String pluginsDirectoryPath )
    {
        this ( pluginsDirectoryPath, true );
    }

    /**
     * Constructs new plugin manager.
     *
     * @param pluginsDirectoryPath plugins directory path
     * @param recursively          whether plugins directory subfolders should be checked recursively or not
     */
    public PluginManager ( @Nullable final String pluginsDirectoryPath, final boolean recursively )
    {
        // Various runtime variables
        this.checkLock = new Object ();
        this.listeners = new ArrayList<PluginsListener<P>> ( 1 );
        this.parentManagers = new ArrayList<PluginManager> ();

        // Default settings
        this.pluginFilter = null;
        this.allowSimilarPlugins = false;
        this.classLoaderType = ClassLoaderType.context;
        this.provideClassLoaderFallback = false;

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
        this.detectedPlugins = new ArrayList<DetectedPlugin<P>> ();
        this.detectedPluginsByPath = new HashMap<String, DetectedPlugin<P>> ();
        this.recentlyDetected = new ArrayList<DetectedPlugin<P>> ();
        this.recentlyInitialized = new ArrayList<P> ();
        this.availablePlugins = new ArrayList<P> ();
        this.availablePluginsById = new HashMap<String, P> ();
        this.availablePluginsByClass = new HashMap<Class<? extends Plugin>, P> ();

        // Plugin manager classes aliases
        XmlUtils.processAnnotations ( PluginInformation.class );
        XmlUtils.processAnnotations ( PluginVersion.class );
        XmlUtils.processAnnotations ( PluginDependency.class );
        XmlUtils.processAnnotations ( PluginLibrary.class );
    }

    /**
     * Returns plugins directory path.
     *
     * @return plugins directory path
     */
    @Nullable
    public String getPluginsDirectoryPath ()
    {
        return pluginsDirectoryPath;
    }

    /**
     * Sets plugins directory path.
     *
     * @param path new plugins directory path
     */
    public void setPluginsDirectoryPath ( @Nullable final String path )
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
    @Nullable
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
    public void setFileFilter ( @Nullable final FileFilter filter )
    {
        this.fileFilter = filter;
    }

    /**
     * Returns class loader type used by this manager to load plugins.
     *
     * @return class loader type used by this manager to load plugins
     */
    @NotNull
    public ClassLoaderType getClassLoaderType ()
    {
        return classLoaderType;
    }

    /**
     * Sets class loader type used by this manager to load plugins.
     *
     * @param classLoaderType class loader type used by this manager to load plugins
     */
    public void setClassLoaderType ( @NotNull final ClassLoaderType classLoaderType )
    {
        this.classLoaderType = classLoaderType;
    }

    /**
     * Returns whether or not {@link PluginManager} should provide a fallback {@link ClassLoader} in cases when classpath of the
     * {@link ClassLoader} retrieved according to the specified {@link ClassLoaderType} cannot be modified in runtime.
     *
     * @return {@code true} if {@link PluginManager} should provide a fallback {@link ClassLoader} when needed, {@code false} otherwise
     */
    public boolean isProvideClassLoaderFallback ()
    {
        return provideClassLoaderFallback;
    }

    /**
     * Sets whether or not {@link PluginManager} should provide a fallback {@link ClassLoader} in cases when classpath of the
     * {@link ClassLoader} retrieved according to the specified {@link ClassLoaderType} cannot be modified in runtime.
     *
     * @param provideClassLoaderFallback whether or not {@link PluginManager} should provide a fallback {@link ClassLoader} when needed
     */
    public void setProvideClassLoaderFallback ( final boolean provideClassLoaderFallback )
    {
        this.provideClassLoaderFallback = provideClassLoaderFallback;
    }

    /**
     * Returns special filter that filters out unwanted plugins before their initialization.
     *
     * @return special filter that filters out unwanted plugins before their initialization
     */
    @Nullable
    public Filter<DetectedPlugin<P>> getPluginFilter ()
    {
        return pluginFilter;
    }

    /**
     * Sets special filter that filters out unwanted plugins before their initialization.
     *
     * @param pluginFilter special filter that filters out unwanted plugins before their initialization
     */
    public void setPluginFilter ( @Nullable final Filter<DetectedPlugin<P>> pluginFilter )
    {
        this.pluginFilter = pluginFilter;
    }

    /**
     * Returns whether should allow loading multiple plugins with the same ID or not.
     *
     * @return true if should allow loading multiple plugins with the same ID, false otherwise
     */
    public boolean isAllowSimilarPlugins ()
    {
        return allowSimilarPlugins;
    }

    /**
     * Sets whether should allow loading multiple plugins with the same ID or not.
     *
     * @param allow whether should allow loading multiple plugins with the same ID or not
     */
    public void setAllowSimilarPlugins ( final boolean allow )
    {
        this.allowSimilarPlugins = allow;
    }

    /**
     * Returns name of the plugin descriptor file.
     * This file should contain serialized PluginInformation.
     *
     * @return name of the plugin descriptor file
     */
    @NotNull
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
    @Nullable
    protected String getPluginLogoFile ()
    {
        return "logo.png";
    }

    /**
     * Returns plugin type accepted by this {@link PluginManager}.
     * In case {@code null} is returned this manager accepts any plugin type.
     *
     * @return plugin type accepted by this {@link PluginManager}
     */
    @Nullable
    protected String getAcceptedPluginType ()
    {
        return null;
    }

    /**
     * Returns {@link List} of parent {@link PluginManager}s.
     *
     * @return {@link List} of parent {@link PluginManager}s
     */
    @NotNull
    public List<PluginManager> getParentManagers ()
    {
        synchronized ( parentManagers )
        {
            return CollectionUtils.copy ( parentManagers );
        }
    }

    /**
     * Returns whether or not specified {@link PluginManager} is added as a parent manager for this one.
     * This will also ask all parent {@link PluginManager}s whether they have specified one as a parent.
     * This method is defended against circular parent references, but it is not recommended to have such.
     *
     * @param manager {@link PluginManager} to check
     * @return {@code true} if specified {@link PluginManager} is added as a parent manager for this one, {@code false} otherwise
     */
    public boolean isParentManager ( @NotNull final PluginManager manager )
    {
        return isParentManager ( manager, new HashSet<PluginManager> ( 3 ) );
    }

    /**
     * Returns whether or not specified {@link PluginManager} is added as a parent manager for this one.
     * This will also ask all parent {@link PluginManager}s whether they have specified one as a parent.
     * This method is defended against circular parent references, but it is not recommended to have such.
     *
     * @param manager {@link PluginManager} to check
     * @param checked {@link Set} of checked {@link PluginManager}s
     * @return {@code true} if specified {@link PluginManager} is added as a parent manager for this one, {@code false} otherwise
     */
    protected boolean isParentManager ( @NotNull final PluginManager manager, @NotNull final Set<PluginManager> checked )
    {
        synchronized ( parentManagers )
        {
            // Saving this manager into checked set
            checked.add ( this );

            // Checking parent managers
            boolean parent = false;
            for ( final PluginManager parentManager : parentManagers )
            {
                // If one of the parent managers is the specified one we return true
                // If one of the parent managers was not checked yet and has the specified one we also return true
                if ( parentManager == manager || !checked.contains ( parentManager ) && parentManager.isParentManager ( manager, checked ) )
                {
                    parent = true;
                    break;
                }
            }
            return parent;
        }
    }

    /**
     * Adds parent {@link PluginManager} reference.
     * Parent {@link PluginManager}s are used to check dependencies load state and some other information.
     *
     * @param manager {@link PluginManager} to add into parent managers {@link List}
     */
    public void addParentManager ( @NotNull final PluginManager manager )
    {
        synchronized ( parentManagers )
        {
            if ( !parentManagers.contains ( manager ) )
            {
                if ( manager.isParentManager ( PluginManager.this ) )
                {
                    LoggerFactory.getLogger ( PluginManager.class ).error ( String.format (
                            "%s already have %s as parent",
                            ReflectUtils.getClassName ( manager.getClass () ),
                            ReflectUtils.getClassName ( PluginManager.this.getClass () )
                    ) );
                }
                parentManagers.add ( manager );
            }
            else
            {
                LoggerFactory.getLogger ( PluginManager.class ).error ( String.format (
                        "%s was already added as a parent",
                        ReflectUtils.getClassName ( manager.getClass () )
                ) );
            }
        }
    }

    /**
     * Removes parent {@link PluginManager} reference.
     * These {@link PluginManager}s are used to check dependencies load state and some other information later on.
     *
     * @param manager {@link PluginManager} to remove from parent managers {@link List}
     */
    public void removeParentManager ( @NotNull final PluginManager manager )
    {
        synchronized ( parentManagers )
        {
            if ( parentManagers.contains ( manager ) )
            {
                parentManagers.remove ( manager );
            }
            else
            {
                LoggerFactory.getLogger ( PluginManager.class ).error ( String.format (
                        "%s was not added as a parent",
                        ReflectUtils.getClassName ( manager.getClass () )
                ) );
            }
        }
    }

    /**
     * Registers specified {@link Plugin} within this {@link PluginManager}.
     * It will also create {@link DetectedPlugin} data based on provided information.
     *
     * @param plugin plugin to register
     */
    public void registerPlugin ( @NotNull final P plugin )
    {
        registerPlugin (
                plugin,
                plugin.getInformation (),
                !SystemUtils.isHeadlessEnvironment () ? plugin.getLogo () : null
        );
    }

    /**
     * Registers specified {@link Plugin} within this {@link PluginManager}.
     * It will also create {@link DetectedPlugin} data based on provided information.
     *
     * @param plugin      plugin to register
     * @param information about this plugin
     * @param logo        plugin logo
     */
    public void registerPlugin ( @NotNull final P plugin, @NotNull final PluginInformation information, @Nullable final Icon logo )
    {
        synchronized ( checkLock )
        {
            final String logPrefix = "[" + information + "] ";
            LoggerFactory.getLogger ( PluginManager.class ).info ( logPrefix + "Initializing pre-loaded plugin" );

            // Creating base detected plugin information
            final DetectedPlugin<P> detectedPlugin = new DetectedPlugin<P> ( null, null, information, logo );
            detectedPlugin.setStatus ( PluginStatus.loaded );
            detectedPlugin.setPlugin ( plugin );
            plugin.setPluginManager ( PluginManager.this );
            plugin.setDetectedPlugin ( detectedPlugin );

            // Saving plugin
            detectedPlugins.add ( detectedPlugin );
            availablePlugins.add ( plugin );
            availablePluginsById.put ( plugin.getId (), plugin );
            availablePluginsByClass.put ( plugin.getClass (), plugin );

            // Adding single initialized plugin
            recentlyInitialized.clear ();
            recentlyInitialized.add ( plugin );

            // Sorting plugins according to their initialization strategies
            // todo Probably should optimize this and only sort upon retrieval
            applyInitializationStrategy ();

            LoggerFactory.getLogger ( PluginManager.class ).info ( logPrefix + "Pre-loaded plugin initialized" );

            // Informing everyone about plugin registration
            firePluginsInitialized ( recentlyInitialized );
        }
    }

    /**
     * Downloads plugin from the specified URL and tries to load it.
     * In case the file is not a plugin it will simply be ignored.
     * Plugins added this way will also be filtered and checked for other means.
     *
     * @param pluginFileURL plugin file URL
     */
    public void scanPlugin ( @NotNull final URL pluginFileURL )
    {
        try
        {
            final String url = pluginFileURL.toURI ().toASCIIString ();
            final File tmpFile = File.createTempFile ( "plugin", ".jar" );
            final File downloadedPlugin = FileUtils.downloadFile ( url, tmpFile );
            scanPlugin ( downloadedPlugin );
        }
        catch ( final Exception e )
        {
            LoggerFactory.getLogger ( PluginManager.class ).error ( "Unable to download plugin from URL: " + pluginFileURL, e );
        }
    }

    /**
     * Tries to load plugin from the specified file.
     * In case the file is not a plugin it will simply be ignored.
     * Plugins added this way will also be filtered and checked for other means.
     *
     * @param pluginFile plugin file path
     */
    public void scanPlugin ( @NotNull final String pluginFile )
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
    public void scanPlugin ( @NotNull final File pluginFile )
    {
        synchronized ( checkLock )
        {
            final String scanPath = FileUtils.canonicalPath ( pluginFile );
            final String msg = "Scanning plugin file: %s";
            LoggerFactory.getLogger ( PluginManager.class ).info ( String.format ( msg, scanPath ) );

            // Resetting recently detected plugins list
            recentlyDetected.clear ();

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
        scanPluginsDirectory ( getPluginsDirectoryPath (), isCheckRecursively () );
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
        scanPluginsDirectory ( getPluginsDirectoryPath (), recursively );
    }

    /**
     * Performs plugins search within the specified plugins directory.
     * This call might be performed as many times as you like.
     * It will simply ignore plugins detected before and will process newly found plugins appropriately.
     *
     * @param pluginsDirectoryPath plugins directory path
     */
    public void scanPluginsDirectory ( @Nullable final String pluginsDirectoryPath )
    {
        scanPluginsDirectory ( pluginsDirectoryPath, isCheckRecursively () );
    }

    /**
     * Performs plugins search within the specified plugins directory.
     * This call might be performed as many times as you like.
     * It will simply ignore plugins detected before and will process newly found plugins appropriately.
     *
     * @param pluginsDirectoryPath plugins directory path
     * @param recursively          whether plugins directory subfolders should be checked recursively or not
     */
    public void scanPluginsDirectory ( @Nullable final String pluginsDirectoryPath, final boolean recursively )
    {
        synchronized ( checkLock )
        {
            // Ignore check if check path is not specified
            if ( pluginsDirectoryPath != null )
            {
                // Informing about plugins check start
                firePluginsCheckStarted ( pluginsDirectoryPath, recursively );

                // Resetting recently detected plugins list
                recentlyDetected.clear ();

                // Collecting plugins information
                collectPluginsInformation ( pluginsDirectoryPath, recursively );

                // Initializing detected plugins
                initializeDetectedPlugins ();

                // Informing about plugins check end
                firePluginsCheckEnded ( pluginsDirectoryPath, recursively );
            }
        }
    }

    /**
     * Collects information about available plugins.
     *
     * @param dir         plugins directory path
     * @param recursively whether plugins directory subfolders should be checked recursively or not
     */
    protected void collectPluginsInformation ( @NotNull final String dir, final boolean recursively )
    {
        collectPluginsInformationImpl ( new File ( dir ), recursively );
        sortRecentlyDetectedPluginsByDependencies ();
    }

    /**
     * Collects information about available plugins.
     *
     * @param dir         plugins directory
     * @param recursively whether plugins directory subfolders should be checked recursively or not
     */
    protected void collectPluginsInformationImpl ( @NotNull final File dir, final boolean recursively )
    {
        LoggerFactory.getLogger ( PluginManager.class ).info ( String.format (
                "Scanning plugins directory (%s): %s",
                recursively ? "recursive" : "flat",
                getPluginsDirectoryPath ()
        ) );

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
            // todo Once moved to NIO this filter should be changed, right now it might not accept drives and custom native stuff
            // todo DirectoriesFilter usage was removed due to FileSystemView usage and related calls
            final File[] subfolders = dir.listFiles ( DIRECTORIES_FILTER );
            if ( subfolders != null )
            {
                for ( final File subfolder : subfolders )
                {
                    collectPluginsInformationImpl ( subfolder, true );
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
    protected boolean collectPluginInformation ( @NotNull final File file )
    {
        final boolean result;
        final DetectedPlugin<P> plugin = detectPlugin ( file );
        if ( plugin != null )
        {
            recentlyDetected.add ( plugin );

            final String msg = "Plugin detected: %s";
            LoggerFactory.getLogger ( PluginManager.class ).info ( String.format ( msg, plugin ) );

            result = true;
        }
        else
        {
            result = false;
        }
        return result;
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
                LoggerFactory.getLogger ( PluginManager.class ).info ( "Sorting detected plugins according to known dependencies" );

                // Collecting plugins that doesn't have any dependencies or their dependencies are loaded
                // Also mapping dependencies for quick access later
                final int s = recentlyDetected.size ();
                final List<DetectedPlugin<P>> root = new ArrayList<DetectedPlugin<P>> ( s );
                final Map<String, List<DetectedPlugin<P>>> references = new HashMap<String, List<DetectedPlugin<P>>> ();
                for ( final DetectedPlugin<P> plugin : recentlyDetected )
                {
                    final List<PluginDependency> dependencies = plugin.getInformation ().getDependencies ();
                    if ( CollectionUtils.notEmpty ( dependencies ) )
                    {
                        // List of resulting dependencies
                        // There could be less resulting dependenies due to missing optional ones
                        // It will also not contain any dependencies that are already loaded prior to this operation
                        final List<String> dependencyIds = new ArrayList<String> ( dependencies.size () );

                        // List of missing dependencies
                        final List<PluginDependency> missingDependencies = new ArrayList<PluginDependency> ( 1 );

                        // Iterating through detected plugin dependencies
                        boolean allDependenciesLoaded = true;
                        for ( final PluginDependency dependency : dependencies )
                        {
                            // Checking that dependency is not yet loaded
                            if ( !isAvailable ( dependency ) )
                            {
                                // Checking whether dependency is in detected list
                                if ( isDetected ( dependency ) )
                                {
                                    // Dependency is not loaded when its not in available list
                                    allDependenciesLoaded = false;

                                    // Saving resulting dependency ID
                                    dependencyIds.add ( dependency.getPluginId () );
                                }
                                else if ( !dependency.isOptional () )
                                {
                                    // Plugin cannot be loaded due to missing required dependency
                                    missingDependencies.add ( dependency );
                                }
                            }
                        }

                        // Continue only if plugin can be loaded
                        if ( CollectionUtils.isEmpty ( missingDependencies ) )
                        {
                            // Mapping all dependencies of this plugin
                            if ( CollectionUtils.notEmpty ( dependencyIds ) )
                            {
                                for ( final String dependencyId : dependencyIds )
                                {
                                    List<DetectedPlugin<P>> dependent = references.get ( dependencyId );
                                    if ( dependent == null )
                                    {
                                        dependent = new ArrayList<DetectedPlugin<P>> ( 1 );
                                        references.put ( dependencyId, dependent );
                                    }
                                    dependent.add ( plugin );
                                }
                            }

                            // Adding plugin into root plugins list if its dependencies are met
                            if ( allDependenciesLoaded )
                            {
                                root.add ( plugin );
                            }
                        }
                    }
                    else
                    {
                        // Adding plugin into root plugins list as it doesn't have any dependencies
                        root.add ( plugin );
                    }
                }

                // Creating graph provider for further topological sorting
                final TopologicalGraphProvider<DetectedPlugin<P>> graph = new TopologicalGraphProvider<DetectedPlugin<P>> ()
                {
                    @Override
                    public List<DetectedPlugin<P>> getRoots ()
                    {
                        return root;
                    }

                    @Override
                    public List<DetectedPlugin<P>> getChildren ( final DetectedPlugin<P> parent )
                    {
                        final List<DetectedPlugin<P>> children = references.get ( parent.getInformation ().getId () );
                        return children != null ? children : Collections.EMPTY_LIST;
                    }
                };

                // Performing topological sorting
                // Saving result as new recently detected plugins list
                final List<DetectedPlugin<P>> sorted = new TopologicalSorter<DetectedPlugin<P>> ( graph ).list ();

                // Adding plugins which didn't get into graph into the end
                // There might be plugins with some side dependencies and they might still be properly initialized
                // Plugins that have missing dependencies will also be added into this list and taken care of later
                for ( final DetectedPlugin<P> plugin : recentlyDetected )
                {
                    if ( !sorted.contains ( plugin ) )
                    {
                        sorted.add ( plugin );
                    }
                }

                // Replacing detected list with it's sorted version
                recentlyDetected.clear ();
                recentlyDetected.addAll ( sorted );
            }
            catch ( final Exception e )
            {
                LoggerFactory.getLogger ( PluginManager.class ).error ( "Unable to perform proper dependencies sorting", e );
            }
        }
    }

    /**
     * Returns whether or not {@link PluginDependency} is available in this or any parent {@link PluginManager}.
     *
     * @param dependency {@link PluginDependency} to look for
     * @return {@code true} if {@link PluginDependency} is available in this or any parent {@link PluginManager}, {@code false} otherwise
     */
    protected boolean isAvailable ( @NotNull final PluginDependency dependency )
    {
        boolean dependencyLoaded = false;

        // Checking plugins available in this manager
        for ( final P available : availablePlugins )
        {
            if ( dependency.accept ( available.getInformation () ) )
            {
                dependencyLoaded = true;
                break;
            }
        }

        // Checking plugins available in parent managers
        synchronized ( parentManagers )
        {
            for ( final PluginManager parentManager : parentManagers )
            {
                if ( parentManager.isAvailable ( dependency ) )
                {
                    dependencyLoaded = true;
                    break;
                }
            }
        }

        return dependencyLoaded;
    }

    /**
     * Returns whether or not {@link PluginDependency} is available in {@link #recentlyDetected} list.
     *
     * @param dependency {@link PluginDependency} to look for
     * @return {@code true} if {@link PluginDependency} is available in {@link #recentlyDetected} list, {@code false} otherwise
     */
    protected boolean isDetected ( @NotNull final PluginDependency dependency )
    {
        boolean dependencyDetected = false;
        for ( final DetectedPlugin<P> detected : recentlyDetected )
        {
            if ( dependency.accept ( detected.getInformation () ) )
            {
                dependencyDetected = true;
                break;
            }
        }
        return dependencyDetected;
    }

    /**
     * Returns {@link DetectedPlugin} for the specified {@link File} if available, {@code null} otherwise.
     *
     * @param file {@link File} to find {@link DetectedPlugin} for
     * @return {@link DetectedPlugin} for the specified {@link File} if available, {@code null} otherwise
     */
    @Nullable
    public DetectedPlugin<P> getDetectedPlugin ( @Nullable final File file )
    {
        final DetectedPlugin<P> detectedPlugin;
        synchronized ( checkLock )
        {
            if ( file != null )
            {
                final String path = FileUtils.canonicalPath ( file );
                if ( detectedPluginsByPath.containsKey ( path ) )
                {
                    // Cached plugin information
                    detectedPlugin = detectedPluginsByPath.get ( path );
                }
                else
                {
                    // Loading plugin information
                    detectedPlugin = detectPlugin ( file );
                }
            }
            else
            {
                detectedPlugin = null;
            }
        }
        return detectedPlugin;
    }

    /**
     * Returns {@link DetectedPlugin} for the specified {@link File} if available, {@code null} otherwise.
     * Returns null in case plugin file cannot be read or if it is incorrect.
     *
     * @param file plugin file to process
     * @return {@link DetectedPlugin} for the specified {@link File} if available, {@code null} otherwise
     */
    @Nullable
    protected DetectedPlugin<P> detectPlugin ( @NotNull final File file )
    {
        DetectedPlugin<P> detectedPlugin = null;
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
                    final Icon logo;
                    if ( !SystemUtils.isHeadlessEnvironment () )
                    {
                        final ZipEntry logoEntry = new ZipEntry ( ZipUtils.getFileLocation ( entry ) + pluginLogo );
                        final InputStream logoInputStream = zipFile.getInputStream ( logoEntry );
                        if ( logoInputStream != null )
                        {
                            // todo This will force logo to always be static
                            logo = ImageUtils.toImageIcon ( ImageUtils.loadBufferedImage ( logoInputStream ) );
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
                        final DetectedPlugin<P> plugin = new DetectedPlugin<P> ( file.getParent (), file.getName (), info, logo );
                        detectedPluginsByPath.put ( FileUtils.canonicalPath ( file ), plugin );
                        detectedPlugin = plugin;
                        break;
                    }

                    break;
                }
            }
            zipFile.close ();
        }
        catch ( final IOException e )
        {
            LoggerFactory.getLogger ( PluginManager.class ).error ( "Unable to read plugin information", e );
        }
        return detectedPlugin;
    }

    /**
     * Returns whether or not specified plugin file was already detected before.
     *
     * @param pluginFolder plugin directory
     * @param pluginFile   plugin file
     * @return {@code true} if specified plugin file was already detected before, {@code false} otherwise
     */
    protected boolean wasDetected ( @Nullable final String pluginFolder, @Nullable final String pluginFile )
    {
        boolean detected = false;
        for ( final DetectedPlugin<P> plugin : detectedPlugins )
        {
            if ( Objects.equals ( plugin.getPluginFolder (), pluginFolder ) &&
                    Objects.equals ( plugin.getPluginFileName (), pluginFile ) )
            {
                detected = true;
                break;
            }
        }
        return detected;
    }

    /**
     * Initializes earlier detected plugins.
     * Also informs listeners about appropriate events.
     */
    protected void initializeDetectedPlugins ()
    {
        if ( !recentlyDetected.isEmpty () )
        {
            LoggerFactory.getLogger ( PluginManager.class ).info ( "Initializing plugins" );

            // Informing about newly detected plugins
            firePluginsDetected ( recentlyDetected );

            // Initializing plugins
            initializeDetectedPluginsImpl ();

            // Sorting plugins according to their initialization strategies
            applyInitializationStrategy ();

            // Properly sorting recently initialized plugins
            Collections.sort ( recentlyInitialized, new Comparator<P> ()
            {
                @Override
                public int compare ( final P o1, final P o2 )
                {
                    final Integer i1 = availablePlugins.indexOf ( o1 );
                    final Integer i2 = availablePlugins.indexOf ( o2 );
                    return i1.compareTo ( i2 );
                }
            } );

            LoggerFactory.getLogger ( PluginManager.class ).info ( "Plugins initialization finished" );

            // Informing about new plugins initialization
            firePluginsInitialized ( recentlyInitialized );
        }
        else
        {
            LoggerFactory.getLogger ( PluginManager.class ).info ( "No new plugins found" );
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

        // Clearing recently initialized plugins list
        // This is required to properly inform about newly loaded plugins later
        recentlyInitialized.clear ();

        // Adding recently detected into the end of the detected plugins list
        detectedPlugins.addAll ( recentlyDetected );

        // Initializing detected plugins
        final String acceptedPluginType = getAcceptedPluginType ();
        for ( final DetectedPlugin<P> detectedPlugin : detectedPlugins )
        {
            // Skip plugins we have already tried to initialize
            if ( detectedPlugin.getStatus () != PluginStatus.detected )
            {
                continue;
            }

            // Check that plugin location is available, meaning that it's not a preloaded plugin
            final File pluginFile = detectedPlugin.getFile ();
            final String pluginsDirectoryPath = getPluginsDirectoryPath ();
            if ( pluginFile == null || pluginsDirectoryPath == null )
            {
                throw new PluginException ( String.format (
                        "Preloaded plugin should be properly registered separately: %s",
                        detectedPlugin.getInformation ().toString ()
                ) );
            }

            // Construct log prefix
            final PluginInformation info = detectedPlugin.getInformation ();
            final String logPrefix = String.format (
                    "[%s] [%s] ",
                    FileUtils.getRelativePath ( pluginFile, new File ( pluginsDirectoryPath ) ),
                    info
            );

            try
            {
                // Starting to load plugin now
                LoggerFactory.getLogger ( PluginManager.class ).info ( logPrefix + "Initializing plugin" );
                detectedPlugin.setStatus ( PluginStatus.loading );

                // Checking plugin type as we don't want (for example) to load server plugins on client side
                if ( acceptedPluginType != null && ( info.getType () == null || !info.getType ().equals ( acceptedPluginType ) ) )
                {
                    pluginLoadFailed (
                            detectedPlugin,
                            "Wrong type",
                            null,
                            logPrefix,
                            String.format (
                                    "Plugin of type '%s' cannot be loaded, required type is: %s",
                                    info.getType (),
                                    acceptedPluginType
                            )
                    );
                    continue;
                }

                // Checking that this is latest plugin version of all available
                // Usually there shouldn't be different versions of the same plugin but everyone make mistakes
                if ( isDeprecatedVersion ( detectedPlugin ) )
                {
                    pluginLoadFailed (
                            detectedPlugin,
                            "Deprecated",
                            null,
                            logPrefix,
                            "Plugin is deprecated, newer version loaded instead"
                    );
                    continue;
                }

                // Checking that this plugin version is not yet loaded
                // This might occur in case the same plugin appears more than once in different files
                if ( isSameVersionAlreadyLoaded ( detectedPlugin, detectedPlugins ) )
                {
                    pluginLoadFailed (
                            detectedPlugin,
                            "Duplicate",
                            null,
                            logPrefix,
                            "Plugin is a duplicate, it will be loaded from another source"
                    );
                    continue;
                }

                // Checking that plugin filter accepts this plugin
                final Filter<DetectedPlugin<P>> pluginFilter = getPluginFilter ();
                if ( pluginFilter != null && !pluginFilter.accept ( detectedPlugin ) )
                {
                    pluginLoadFailed (
                            detectedPlugin,
                            "Filtered",
                            null,
                            logPrefix,
                            "Plugin was not accepted by plugin filter"
                    );
                    continue;
                }

                // Checking plugin dependencies
                final List<PluginDependency> dependencies = detectedPlugin.getInformation ().getDependencies ();
                if ( dependencies != null )
                {
                    for ( final PluginDependency dependency : dependencies )
                    {
                        // Checking whether or not dependency is mandatory and whether or not it is available
                        final String dependencyId = dependency.getPluginId ();
                        if ( !dependency.isOptional () && !isPluginAvailable ( dependencyId ) )
                        {
                            // If it is mandatory and not available - check related managers for that dependency
                            boolean available = false;
                            for ( final PluginManager relatedManager : parentManagers )
                            {
                                if ( relatedManager.isPluginAvailable ( dependencyId ) )
                                {
                                    available = true;
                                    break;
                                }
                            }
                            if ( !available )
                            {
                                pluginLoadFailed (
                                        detectedPlugin,
                                        "Incomplete",
                                        null,
                                        logPrefix,
                                        String.format (
                                                "Mandatory plugin dependency is missing: %s",
                                                dependencyId
                                        )
                                );
                                break;
                            }
                        }
                    }
                    if ( detectedPlugin.getStatus () == PluginStatus.failed )
                    {
                        continue;
                    }
                }

                // Collecting plugin and its libraries JAR paths
                final List<URL> pluginClassPath = new ArrayList<URL> ( 1 + info.getLibrariesCount () );
                pluginClassPath.add ( pluginFile.toURI ().toURL () );
                if ( info.getLibraries () != null )
                {
                    for ( final PluginLibrary library : info.getLibraries () )
                    {
                        final File file = new File ( detectedPlugin.getPluginFolder (), library.getFile () );
                        if ( file.exists () )
                        {
                            // Adding library URI to path
                            pluginClassPath.add ( file.toURI ().toURL () );

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
                            pluginLoadFailed (
                                    detectedPlugin,
                                    "Incomplete",
                                    null,
                                    logPrefix,
                                    String.format (
                                            "Plugin library was not found: %s",
                                            file.getAbsolutePath ()
                                    )
                            );
                            break;
                        }
                    }
                    if ( detectedPlugin.getStatus () == PluginStatus.failed )
                    {
                        continue;
                    }
                }

                try
                {
                    // Configuring class loader for our plugin
                    final ClassLoader classLoader = configureClassLoaderForPlugin ( detectedPlugin, pluginClassPath );

                    // Loading plugin class
                    final Class<?> pluginClass = classLoader.loadClass ( info.getMainClass () );
                    final P plugin = ReflectUtils.createInstance ( pluginClass );
                    plugin.setPluginManager ( PluginManager.this );
                    plugin.setDetectedPlugin ( detectedPlugin );

                    // Saving initialized plugin
                    availablePlugins.add ( plugin );
                    availablePluginsById.put ( plugin.getId (), plugin );
                    availablePluginsByClass.put ( plugin.getClass (), plugin );
                    recentlyInitialized.add ( plugin );

                    // Updating detected plugin status
                    LoggerFactory.getLogger ( PluginManager.class ).info ( logPrefix + "Plugin initialized" );
                    detectedPlugin.setStatus ( PluginStatus.loaded );
                    detectedPlugin.setPlugin ( plugin );
                }
                catch ( final Exception e )
                {
                    // Something happened while performing plugin class load
                    pluginLoadFailed (
                            detectedPlugin,
                            "Plugin initialization exception",
                            e,
                            logPrefix,
                            "Unable to initialize plugin"
                    );
                }
            }
            catch ( final Exception e )
            {
                // Something happened while checking plugin information
                pluginLoadFailed (
                        detectedPlugin,
                        "Plugin data exception",
                        e,
                        logPrefix,
                        "Unable to initialize plugin data"
                );
            }
        }

        // Checking for same/similar libraries used within plugins
        // todo There should be a flag for libraries to specify when duplicates usage is intended
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
                LoggerFactory.getLogger ( PluginManager.class ).info ( sb.toString () );
                sameLibrariesInPlugins = true;
                break;
            }
        }
        if ( sameLibrariesInPlugins )
        {
            final String msg = "Make sure that the same library usage within different plugins was actually your intent";
            LoggerFactory.getLogger ( PluginManager.class ).info ( msg );
        }
    }

    /**
     * Logs {@link Plugin} loading error and updates {@link DetectedPlugin} information.
     *
     * @param detectedPlugin {@link DetectedPlugin}
     * @param failureCause   {@link Plugin} initialization failure cause
     * @param exception      {@link Throwable} that occurred during {@link Plugin} initialization
     * @param logPrefix      prefix for logged messages only
     * @param message        message to log and save in {@link DetectedPlugin}
     */
    private void pluginLoadFailed ( @NotNull final DetectedPlugin<P> detectedPlugin, @NotNull final String failureCause,
                                    @Nullable final Exception exception, @NotNull final String logPrefix, @NotNull final String message )
    {
        LoggerFactory.getLogger ( PluginManager.class ).error ( logPrefix + message );

        detectedPlugin.setStatus ( PluginStatus.failed );
        detectedPlugin.setFailureCause ( failureCause );
        detectedPlugin.setException ( exception );
        detectedPlugin.setExceptionMessage ( message );
    }

    /**
     * Returns {@link ClassLoader} configured for the specified {@link DetectedPlugin}.
     *
     * @param detectedPlugin  {@link DetectedPlugin}
     * @param pluginClassPath {@link List} of plugin class path {@link URL}s
     * @return {@link ClassLoader} configured for the specified {@link DetectedPlugin}
     */
    @NotNull
    protected ClassLoader configureClassLoaderForPlugin ( @NotNull final DetectedPlugin<P> detectedPlugin,
                                                          @NotNull final List<URL> pluginClassPath )
    {
        // Choosing class loader based on configured type
        ClassLoader classLoader;
        switch ( getClassLoaderType () )
        {
            case system:
            {
                classLoader = ClassLoader.getSystemClassLoader ();
                break;
            }
            case context:
            default:
            {
                final ClassLoader ccl = Thread.currentThread ().getContextClassLoader ();
                classLoader = ccl != null ? ccl : getClass ().getClassLoader ();
                break;
            }
            case global:
            {
                classLoader = getGlobalClassLoader ();
                break;
            }
            case local:
            {
                classLoader = getLocalClassLoader ();
                break;
            }
            case separate:
            {
                classLoader = createPluginClassLoader ( new URL[ 0 ] );
                break;
            }
        }

        try
        {
            // Adding plugin classpath into class loader via utility
            ReflectUtils.addToClasspath ( classLoader, pluginClassPath );
        }
        catch ( final Exception e )
        {
            // We might want to handle exception differently if fallback mechanism is enabled
            if ( isProvideClassLoaderFallback () )
            {
                // Using plugin class loader with a custom class path
                classLoader = new PluginClassLoader (
                        pluginClassPath.toArray ( new URL[ pluginClassPath.size () ] ),
                        classLoader
                );
            }
            else
            {
                throw new PluginException ( String.format (
                        "Unable to load detected plugin: %s",
                        detectedPlugin.getInformation ().toString ()
                ), e );
            }
        }
        return classLoader;
    }

    /**
     * Returns whether the list of detected plugins contain a newer version of the specified plugin or not.
     *
     * @param plugin plugin to compare with other detected plugins
     * @return true if the list of detected plugins contain a newer version of the specified plugin, false otherwise
     */
    public boolean isDeprecatedVersion ( @NotNull final DetectedPlugin<P> plugin )
    {
        synchronized ( checkLock )
        {
            return isDeprecatedVersion ( plugin, detectedPlugins );
        }
    }

    /**
     * Returns whether or not the {@link List} contains a newer version of the specified {@link DetectedPlugin}.
     *
     * @param plugin          {@link DetectedPlugin} to compare others from {@link List} with
     * @param detectedPlugins {@link List} of {@link DetectedPlugin} to look for newer version in
     * @return {@code true} if the {@link List} contains a newer version of the specified {@link DetectedPlugin}, {@code false} otherwise
     */
    public boolean isDeprecatedVersion ( @NotNull final DetectedPlugin<P> plugin, @NotNull final List<DetectedPlugin<P>> detectedPlugins )
    {
        boolean isDeprecatedVersion = false;
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
                    isDeprecatedVersion = true;
                    break;
                }
            }
        }
        return isDeprecatedVersion;
    }

    /**
     * Returns whether or not the {@link List} contains the same version of the specified {@link DetectedPlugin}.
     *
     * @param plugin          {@link DetectedPlugin} to compare others from {@link List} with
     * @param detectedPlugins {@link List} of {@link DetectedPlugin} to look for the same version in
     * @return {@code true} if the {@link List} contains the same version of the specified {@link DetectedPlugin}, {@code false} otherwise
     */
    protected boolean isSameVersionAlreadyLoaded ( @NotNull final DetectedPlugin<P> plugin,
                                                   @NotNull final List<DetectedPlugin<P>> detectedPlugins )
    {
        boolean isSameVersionAlreadyLoaded = false;
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
                    isSameVersionAlreadyLoaded = true;
                    break;
                }
            }
        }
        return isSameVersionAlreadyLoaded;
    }

    /**
     * Sorting plugins according to their initialization strategies.
     * todo Take plugin dependencies into account with top priority here
     */
    protected void applyInitializationStrategy ()
    {
        if ( availablePlugins.size () > 1 )
        {
            // Splitting plugins by initial groups
            final List<P> beforeAll = new ArrayList<P> ( availablePlugins.size () );
            final List<P> middle = new ArrayList<P> ( availablePlugins.size () );
            final List<P> afterAll = new ArrayList<P> ( availablePlugins.size () );
            for ( final P plugin : availablePlugins )
            {
                final InitializationStrategy strategy = plugin.getInitializationStrategy ();
                if ( strategy.getPluginId ().equals ( InitializationStrategy.ALL_ID ) )
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
                final List<P> sortedMiddle = new ArrayList<P> ( middle );
                for ( final P plugin : middle )
                {
                    final InitializationStrategy strategy = plugin.getInitializationStrategy ();
                    final String id = strategy.getPluginId ();
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
    }

    /**
     * Returns list of detected plugins.
     *
     * @return list of detected plugins
     */
    public List<DetectedPlugin<P>> getDetectedPlugins ()
    {
        synchronized ( checkLock )
        {
            return new ImmutableList<DetectedPlugin<P>> ( detectedPlugins );
        }
    }

    /**
     * Returns list of available loaded plugins.
     *
     * @return list of available loaded plugins
     */
    public List<P> getAvailablePlugins ()
    {
        synchronized ( checkLock )
        {
            return new ImmutableList<P> ( availablePlugins );
        }
    }

    /**
     * Returns whether or not instance of the {@link Plugin} with the specified identifier is available.
     *
     * @param pluginId {@link Plugin} identifier
     * @return {@code true} if instance of the {@link Plugin} with the specified identifier is available, {@code false} otherwise
     */
    public boolean isPluginAvailable ( @NotNull final String pluginId )
    {
        return getPlugin ( pluginId ) != null;
    }

    /**
     * Returns {@link Plugin} instance by its identifier or {@code null} if it's not available.
     *
     * @param pluginId {@link Plugin} identifier
     * @param <T>      {@link Plugin} type
     * @return {@link Plugin} instance by its identifier or {@code null} if it's not available
     */
    @Nullable
    public <T extends P> T getPlugin ( @NotNull final String pluginId )
    {
        synchronized ( checkLock )
        {
            return ( T ) availablePluginsById.get ( pluginId );
        }
    }

    /**
     * Returns {@link Plugin} instance by its {@link Class} or {@code null} if it's not available.
     * Returns available plugin instance by its class.
     *
     * @param pluginClass {@link Plugin} {@link Class}
     * @param <T>         {@link Plugin} type
     * @return {@link Plugin} instance by its {@link Class} or {@code null} if it's not available
     */
    @Nullable
    public <T extends P> T getPlugin ( @NotNull final Class<T> pluginClass )
    {
        synchronized ( checkLock )
        {
            return ( T ) availablePluginsByClass.get ( pluginClass );
        }
    }

    /**
     * Returns amount of detected plugins.
     *
     * @return amount of detected loaded plugins
     */
    public int getDetectedPluginsAmount ()
    {
        synchronized ( checkLock )
        {
            return detectedPlugins.size ();
        }
    }

    /**
     * Returns amount of successfully loaded plugins.
     *
     * @return amount of successfully loaded plugins
     */
    public int getLoadedPluginsAmount ()
    {
        synchronized ( checkLock )
        {
            return availablePlugins.size ();
        }
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
        synchronized ( checkLock )
        {
            return detectedPlugins.size () - availablePlugins.size ();
        }
    }

    /**
     * Registers specified {@link PluginsRunnable} for {@link PluginsListener#pluginsCheckStarted(String, boolean)} event.
     *
     * @param runnable {@link PluginsRunnable} to register for event
     * @return created {@link PluginsListener}
     */
    @NotNull
    public PluginsListener<P> onPluginsScanStart ( @NotNull final DirectoryRunnable runnable )
    {
        final PluginsAdapter<P> listener = new PluginsAdapter<P> ()
        {
            @Override
            public void pluginsCheckStarted ( @NotNull final String directory, final boolean recursive )
            {
                runnable.run ( directory, recursive );
            }
        };
        addPluginsListener ( listener );
        return listener;
    }

    /**
     * Registers specified {@link PluginsRunnable} for {@link PluginsListener#pluginsCheckEnded(String, boolean)} event.
     *
     * @param runnable {@link PluginsRunnable} to register for event
     * @return created {@link PluginsListener}
     */
    @NotNull
    public PluginsListener<P> onPluginsScanEnd ( @NotNull final DirectoryRunnable runnable )
    {
        final PluginsAdapter<P> listener = new PluginsAdapter<P> ()
        {
            @Override
            public void pluginsCheckEnded ( @NotNull final String directory, final boolean recursive )
            {
                runnable.run ( directory, recursive );
            }
        };
        addPluginsListener ( listener );
        return listener;
    }

    /**
     * Registers specified {@link PluginsRunnable} for {@link PluginsListener#pluginsDetected(List)} event.
     *
     * @param runnable {@link PluginsRunnable} to register for event
     * @return created {@link PluginsListener}
     */
    @NotNull
    public PluginsListener<P> onPluginsDetection ( @NotNull final DetectedPluginsRunnable<P> runnable )
    {
        final PluginsAdapter<P> listener = new PluginsAdapter<P> ()
        {
            @Override
            public void pluginsDetected ( @NotNull final List<DetectedPlugin<P>> detectedPlugins )
            {
                runnable.run ( detectedPlugins );
            }
        };
        addPluginsListener ( listener );
        return listener;
    }

    /**
     * Registers specified {@link PluginsRunnable} for {@link PluginsListener#pluginsInitialized(List)} event.
     *
     * @param runnable {@link PluginsRunnable} to register for event
     * @return created {@link PluginsListener}
     */
    @NotNull
    public PluginsListener<P> onPluginsInitialization ( @NotNull final PluginsRunnable<P> runnable )
    {
        final PluginsAdapter<P> listener = new PluginsAdapter<P> ()
        {
            @Override
            public void pluginsInitialized ( @NotNull final List<P> plugins )
            {
                runnable.run ( plugins );
            }
        };
        addPluginsListener ( listener );
        return listener;
    }

    /**
     * Adds {@link PluginsListener}.
     *
     * @param listener {@link PluginsListener} to add
     */
    public void addPluginsListener ( @NotNull final PluginsListener<P> listener )
    {
        synchronized ( listeners )
        {
            listeners.add ( listener );
        }
    }

    /**
     * Removes {@link PluginsListener}.
     *
     * @param listener {@link PluginsListener} to remove
     */
    public void removePluginsListener ( @NotNull final PluginsListener<P> listener )
    {
        synchronized ( listeners )
        {
            listeners.remove ( listener );
        }
    }

    /**
     * Informs all added {@link PluginsListener}s about plugins check operation start.
     *
     * @param directory checked plugins directory path
     * @param recursive whether plugins directory subfolders are checked recursively or not
     */
    public void firePluginsCheckStarted ( @NotNull final String directory, final boolean recursive )
    {
        synchronized ( listeners )
        {
            for ( final PluginsListener<P> listener : CollectionUtils.copy ( listeners ) )
            {
                listener.pluginsCheckStarted ( directory, recursive );
            }
        }
    }

    /**
     * Informs all added {@link PluginsListener}s about plugins check operation end.
     *
     * @param directory checked plugins directory path
     * @param recursive whether plugins directory subfolders are checked recursively or not
     */
    public void firePluginsCheckEnded ( @NotNull final String directory, final boolean recursive )
    {
        synchronized ( listeners )
        {
            for ( final PluginsListener<P> listener : CollectionUtils.copy ( listeners ) )
            {
                listener.pluginsCheckEnded ( directory, recursive );
            }
        }
    }

    /**
     * Informs all added {@link PluginsListener}s about newly detected plugins.
     *
     * @param plugins newly detected plugins list
     */
    public void firePluginsDetected ( @NotNull final List<DetectedPlugin<P>> plugins )
    {
        synchronized ( listeners )
        {
            final ImmutableList<DetectedPlugin<P>> immutable = new ImmutableList<DetectedPlugin<P>> ( plugins );
            for ( final PluginsListener<P> listener : CollectionUtils.copy ( listeners ) )
            {
                listener.pluginsDetected ( immutable );
            }
        }
    }

    /**
     * Informs all added {@link PluginsListener}s about newly initialized plugins.
     *
     * @param plugins newly initialized plugins list
     */
    public void firePluginsInitialized ( @NotNull final List<P> plugins )
    {
        synchronized ( listeners )
        {
            final ImmutableList<P> immutable = new ImmutableList<P> ( plugins );
            for ( final PluginsListener<P> listener : CollectionUtils.copy ( listeners ) )
            {
                listener.pluginsInitialized ( immutable );
            }
        }
    }

    /**
     * Returns local {@link PluginClassLoader} for this specific {@link PluginManager}.
     *
     * @return local {@link PluginClassLoader} for this specific {@link PluginManager}
     */
    @NotNull
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
     * Returns global {@link PluginClassLoader} shared between all {@link PluginManager}s.
     *
     * @return global {@link PluginClassLoader} shared between all {@link PluginManager}s
     */
    @NotNull
    protected PluginClassLoader getGlobalClassLoader ()
    {
        if ( globalClassLoader == null )
        {
            synchronized ( PluginManager.class )
            {
                if ( globalClassLoader == null )
                {
                    globalClassLoader = createPluginClassLoader ( new URL[ 0 ] );
                }
            }
        }
        return globalClassLoader;
    }

    /**
     * Returns new {@link PluginClassLoader} for this specific {@link PluginManager}.
     *
     * @param classpath class loader class path
     * @return new {@link PluginClassLoader} for this specific {@link PluginManager}
     */
    @NotNull
    protected PluginClassLoader createPluginClassLoader ( @NotNull final URL[] classpath )
    {
        return new PluginClassLoader ( classpath, PluginManager.class.getClassLoader () );
    }
}