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

package com.alee.managers.settings;

import com.alee.utils.CollectionUtils;
import com.alee.utils.FileUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.XmlUtils;
import com.alee.utils.swing.WebTimer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This manager allows you to quickly and easily save any serializable data into settings files using simple XML format.
 * <p/>
 * You can define settings group file name (they are equal to the SettingsGroup name and should be unique), its location on local or
 * remote drive and each separate property key. Any amount of properties could be stored in each group. Also there are some additional
 * manager settings that allows you to configure SettingsManager to work the way you want it to. The rest of the work is done by the
 * SettingsManager - it decides where, when and how to save the settings files.
 * <p/>
 * Settings data aliasing (see XStream documentation and XmlUtils class) or SettingsGroup file location changes should be done before
 * requesting any of the settings, preferably right at the application startup. Otherwise data might not be properly read and settings will
 * appear empty.
 *
 * @author Mikle Garin
 * @see XmlUtils
 */

public final class SettingsManager
{
    /**
     * Error output prefix.
     */
    private static final String ERROR_PREFIX = "[SettingsManager] ";

    /**
     * Settings change listeners.
     */
    private static Map<String, Map<String, List<SettingsListener>>> settingsListeners =
            new HashMap<String, Map<String, List<SettingsListener>>> ();

    /**
     * Settings files extension.
     */
    private static String settingsFilesExtension = ".xml";

    /**
     * Backup files extension.
     */
    private static String backupFilesExtension = ".backup";

    /**
     * Default settings directory location.
     */
    private static String defaultSettingsDir = null;

    /**
     * Default settings directory name.
     */
    private static String defaultSettingsDirName = ".weblaf";

    /**
     * Default settings group name.
     */
    private static String defaultSettingsGroup = "default";

    /**
     * Redefined per-group settings save locations.
     */
    private static Map<String, String> groupFileLocation = new HashMap<String, String> ();

    /**
     * Group settings read success marks.
     */
    private static Map<String, SettingsGroupState> groupState = new HashMap<String, SettingsGroupState> ();

    /**
     * Cached settings map.
     */
    private static Map<String, SettingsGroup> groups = new HashMap<String, SettingsGroup> ();

    /**
     * Cached files map.
     */
    private static Map<String, Object> files = new HashMap<String, Object> ();

    /**
     * Whether should save settings right after any changes made or not.
     */
    private static boolean saveOnChange = true;

    /**
     * Whether should save provided default value in "get" calls or not.
     */
    private static boolean saveDefaultValues = true;

    /**
     * Save-on-change scheduler lock object.
     */
    private static final Object saveOnChangeLock = new Object ();

    /**
     * Save-on-change save delay in milliseconds.
     * If larger than 0 then settings will be accumulated and saved all at once as soon as no new changes came within the delay time.
     */
    private static long saveOnChangeDelay = 500;

    /**
     * Save-on-change scheduler timer.
     */
    private static WebTimer groupSaveScheduler = null;

    /**
     * Delayed settings groups to save.
     */
    private static List<String> groupsToSaveOnChange = new ArrayList<String> ();

    /**
     * Whether should display settings load and save exceptions or not.
     * They might occur for example if settings cannot be read due to corrupted file or modified object stucture.
     */
    private static boolean displayExceptions = true;

    /**
     * Whether should allow saving settings into files or not.
     * If set to false settings will be available only in runtime and will be lost after application finishes working.
     */
    private static boolean allowSave = true;

    /**
     * Whether SettingsManager is initialized or not.
     */
    private static boolean initialized = false;

    /**
     * Initializes SettingsManager.
     */
    public static void initialize ()
    {
        if ( !initialized )
        {
            initialized = true;

            // Aliases
            XmlUtils.processAnnotations ( SettingsGroup.class );
            XmlUtils.processAnnotations ( SettingsGroupState.class );
            XmlUtils.processAnnotations ( ReadState.class );

            // Initializing sub-manager
            ComponentSettingsManager.initializeManager ();

            // JVM shutdown hook for settings save
            Runtime.getRuntime ().addShutdownHook ( new Thread ( new Runnable ()
            {
                @Override
                public void run ()
                {
                    saveSettings ();
                }
            } ) );
        }
    }

    /**
     * Returns list of all loaded settings groups.
     *
     * @return list of all loaded settings groups
     */
    public static List<SettingsGroup> getLoadedGroups ()
    {
        return CollectionUtils.copy ( groups.values () );
    }

    /**
     * Returns map of all loaded settings groups.
     *
     * @return map of all loaded settings groups
     */
    public static Map<String, SettingsGroup> getLoadedGroupsMap ()
    {
        return groups;
    }

    /**
     * Registers component for settings auto-save.
     * <p/>
     * Also registered component will be:
     * - listened for settings changes to save them when requested
     * - automatically updated with any loaded settings for that key if requested
     * - automatically updated with any changes made in its settings if requested
     *
     * @param component component to register
     * @param key       component settings key
     */
    public static void registerComponent ( Component component, String key )
    {
        registerComponent ( component, key, ( Object ) null );
    }

    /**
     * Registers component for settings auto-save.
     * <p/>
     * Also registered component will be:
     * - listened for settings changes to save them when requested
     * - automatically updated with any loaded settings for that key if requested
     * - automatically updated with any changes made in its settings if requested
     *
     * @param component         component to register
     * @param key               component settings key
     * @param defaultValueClass component default value class
     * @param <T>               default value type
     * @see DefaultValue
     */
    public static <T extends DefaultValue> void registerComponent ( Component component, String key, Class<T> defaultValueClass )
    {
        registerComponent ( component, key, getDefaultValue ( defaultValueClass ) );
    }

    /**
     * Registers component for settings auto-save.
     * <p/>
     * Also registered component will be:
     * - listened for settings changes to save them when requested
     * - automatically updated with any loaded settings for that key if requested
     * - automatically updated with any changes made in its settings if requested
     *
     * @param component    component to register
     * @param key          component settings key
     * @param defaultValue component default value
     */
    public static void registerComponent ( Component component, String key, Object defaultValue )
    {
        registerComponent ( component, defaultSettingsGroup, key, defaultValue );
    }

    /**
     * Registers component for settings auto-save.
     * <p/>
     * Also registered component will be:
     * - listened for settings changes to save them when requested
     * - automatically updated with any loaded settings for that key if requested
     * - automatically updated with any changes made in its settings if requested
     *
     * @param component component to register
     * @param group     component settings group
     * @param key       component settings key
     */
    public static void registerComponent ( Component component, String group, String key )
    {
        registerComponent ( component, group, key, ( Object ) null );
    }

    /**
     * Registers component for settings auto-save.
     * <p/>
     * Also registered component will be:
     * - listened for settings changes to save them when requested
     * - automatically updated with any loaded settings for that key if requested
     * - automatically updated with any changes made in its settings if requested
     *
     * @param component         component to register
     * @param group             component settings group
     * @param key               component settings key
     * @param defaultValueClass component default value class
     * @param <T>               default value type
     * @see DefaultValue
     */
    public static <T extends DefaultValue> void registerComponent ( Component component, String group, String key,
                                                                    Class<T> defaultValueClass )
    {
        registerComponent ( component, group, key, getDefaultValue ( defaultValueClass ) );
    }

    /**
     * Registers component for settings auto-save.
     * <p/>
     * Also registered component will be:
     * - listened for settings changes to save them when requested
     * - automatically updated with any loaded settings for that key if requested
     * - automatically updated with any changes made in its settings if requested
     *
     * @param component    component to register
     * @param group        component settings group
     * @param key          component settings key
     * @param defaultValue component default value
     */
    public static void registerComponent ( Component component, String group, String key, Object defaultValue )
    {
        registerComponent ( component, group, key, defaultValue, true, true );
    }

    /**
     * Registers component for settings auto-save.
     * <p/>
     * Also registered component will be:
     * - listened for settings changes to save them when requested
     * - automatically updated with any loaded settings for that key if requested
     * - automatically updated with any changes made in its settings if requested
     *
     * @param component            component to register
     * @param key                  component settings key
     * @param loadInitialSettings  whether to load initial available settings into the component or not
     * @param applySettingsChanges whether to apply settings changes to the component or not
     */
    public static void registerComponent ( Component component, String key, boolean loadInitialSettings, boolean applySettingsChanges )
    {
        registerComponent ( component, key, null, loadInitialSettings, applySettingsChanges );
    }

    /**
     * Registers component for settings auto-save.
     * <p/>
     * Also registered component will be:
     * - listened for settings changes to save them when requested
     * - automatically updated with any loaded settings for that key if requested
     * - automatically updated with any changes made in its settings if requested
     *
     * @param component            component to register
     * @param key                  component settings key
     * @param defaultValueClass    component default value class
     * @param loadInitialSettings  whether to load initial available settings into the component or not
     * @param applySettingsChanges whether to apply settings changes to the component or not
     * @param <T>                  default value type
     * @see DefaultValue
     */
    public static <T extends DefaultValue> void registerComponent ( Component component, String key, Class<T> defaultValueClass,
                                                                    boolean loadInitialSettings, boolean applySettingsChanges )
    {
        registerComponent ( component, key, getDefaultValue ( defaultValueClass ), loadInitialSettings, applySettingsChanges );
    }

    /**
     * Registers component for settings auto-save.
     * <p/>
     * Also registered component will be:
     * - listened for settings changes to save them when requested
     * - automatically updated with any loaded settings for that key if requested
     * - automatically updated with any changes made in its settings if requested
     *
     * @param component            component to register
     * @param key                  component settings key
     * @param defaultValue         component default value
     * @param loadInitialSettings  whether to load initial available settings into the component or not
     * @param applySettingsChanges whether to apply settings changes to the component or not
     */
    public static void registerComponent ( Component component, String key, Object defaultValue, boolean loadInitialSettings,
                                           boolean applySettingsChanges )
    {
        registerComponent ( component, defaultSettingsGroup, key, defaultValue, loadInitialSettings, applySettingsChanges );
    }

    /**
     * Registers component for settings auto-save.
     * <p/>
     * Also registered component will be:
     * - listened for settings changes to save them when requested
     * - automatically updated with any loaded settings for that key if requested
     * - automatically updated with any changes made in its settings if requested
     *
     * @param component            component to register
     * @param group                component settings group
     * @param key                  component settings key
     * @param defaultValueClass    component default value class
     * @param loadInitialSettings  whether to load initial available settings into the component or not
     * @param applySettingsChanges whether to apply settings changes to the component or not
     * @param <T>                  default value type
     * @see DefaultValue
     */
    public static <T extends DefaultValue> void registerComponent ( Component component, String group, String key,
                                                                    Class<T> defaultValueClass, boolean loadInitialSettings,
                                                                    boolean applySettingsChanges )
    {
        registerComponent ( component, group, key, getDefaultValue ( defaultValueClass ), loadInitialSettings, applySettingsChanges );
    }

    /**
     * Registers component for settings auto-save.
     * <p/>
     * Also registered component will be:
     * - listened for settings changes to save them when requested
     * - automatically updated with any loaded settings for that key if requested
     * - automatically updated with any changes made in its settings if requested
     *
     * @param component            component to register
     * @param group                component settings group
     * @param key                  component settings key
     * @param defaultValue         component default value
     * @param loadInitialSettings  whether to load initial available settings into the component or not
     * @param applySettingsChanges whether to apply settings changes to the component or not
     */
    public static void registerComponent ( Component component, String group, String key, Object defaultValue, boolean loadInitialSettings,
                                           boolean applySettingsChanges )
    {
        ComponentSettingsManager.registerComponent (
                new SettingsProcessorData ( component, group, key, defaultValue, loadInitialSettings, applySettingsChanges ) );
    }

    /**
     * Registers component for settings auto-save.
     * <p/>
     * Also registered component will be:
     * - listened for settings changes to save them when requested
     * - automatically updated with any loaded settings for that key if requested
     * - automatically updated with any changes made in its settings if requested
     *
     * @param component         component to register
     * @param settingsProcessor component settings processor
     */
    public static void registerComponent ( Component component, SettingsProcessor settingsProcessor )
    {
        ComponentSettingsManager.registerComponent ( component, settingsProcessor );
    }

    /**
     * Unregisters component from settings auto-save.
     *
     * @param component component to unregister
     */
    public static void unregisterComponent ( Component component )
    {
        ComponentSettingsManager.unregisterComponent ( component );
    }

    /**
     * Loads saved settings into the component if it is registered.
     *
     * @param component component registered for settings auto-save
     */
    public static void loadComponentSettings ( Component component )
    {
        ComponentSettingsManager.loadSettings ( component );
    }

    /**
     * Saves component settings.
     *
     * @param component component registered for settings auto-save
     */
    public static void saveComponentSettings ( Component component )
    {
        ComponentSettingsManager.saveSettings ( component );
    }

    /**
     * Registers specified settings processor class for the specified component type.
     *
     * @param componentType     component type
     * @param settingsProcessor settings processor class
     * @param <T>               settings processor type
     */
    public static <T extends SettingsProcessor> void registerSettingsProcessor ( Class componentType, Class<T> settingsProcessor )
    {
        ComponentSettingsManager.registerSettingsProcessor ( componentType, settingsProcessor );
    }

    /**
     * Returns Boolean value.
     *
     * @param key settings key
     * @return Boolean value
     */
    public static Boolean getBoolean ( String key )
    {
        return get ( key, ( Boolean ) null );
    }

    /**
     * Returns Boolean value.
     *
     * @param group settings group
     * @param key   settings key
     * @return Boolean value
     */
    public static Boolean getBoolean ( String group, String key )
    {
        return get ( group, key, ( Boolean ) null );
    }

    /**
     * Returns String value.
     *
     * @param key settings key
     * @return String value
     */
    public static String getString ( String key )
    {
        return get ( key, ( String ) null );
    }

    /**
     * Returns String value.
     *
     * @param group settings group
     * @param key   settings key
     * @return String value
     */
    public static String getString ( String group, String key )
    {
        return get ( group, key, ( String ) null );
    }

    /**
     * Returns Integer value.
     *
     * @param key settings key
     * @return Integer value
     */
    public static Integer getInteger ( String key )
    {
        return get ( key, ( Integer ) null );
    }

    /**
     * Returns Integer value.
     *
     * @param group settings group
     * @param key   settings key
     * @return Integer value
     */
    public static Integer getInteger ( String group, String key )
    {
        return get ( group, key, ( Integer ) null );
    }

    /**
     * Returns Long value.
     *
     * @param key settings key
     * @return Long value
     */
    public static Long getLong ( String key )
    {
        return get ( key, ( Long ) null );
    }

    /**
     * Returns Long value.
     *
     * @param group settings group
     * @param key   settings key
     * @return Long value
     */
    public static Long getLong ( String group, String key )
    {
        return get ( group, key, ( Long ) null );
    }

    /**
     * Returns Float value.
     *
     * @param key settings key
     * @return Float value
     */
    public static Float getFloat ( String key )
    {
        return get ( key, ( Float ) null );
    }

    /**
     * Returns Float value.
     *
     * @param group settings group
     * @param key   settings key
     * @return Float value
     */
    public static Float getFloat ( String group, String key )
    {
        return get ( group, key, ( Float ) null );
    }

    /**
     * Returns Double value.
     *
     * @param key settings key
     * @return Double value
     */
    public static Double getDouble ( String key )
    {
        return get ( key, ( Double ) null );
    }

    /**
     * Returns Double value.
     *
     * @param group settings group
     * @param key   settings key
     * @return Double value
     */
    public static Double getDouble ( String group, String key )
    {
        return get ( group, key, ( Double ) null );
    }

    /**
     * Returns Dimension value.
     *
     * @param key settings key
     * @return Dimension value
     */
    public static Dimension getDimension ( String key )
    {
        return get ( key, ( Dimension ) null );
    }

    /**
     * Returns Dimension value.
     *
     * @param group settings group
     * @param key   settings key
     * @return Dimension value
     */
    public static Dimension getDimension ( String group, String key )
    {
        return get ( group, key, ( Dimension ) null );
    }

    /**
     * Returns Point value.
     *
     * @param key settings key
     * @return Point value
     */
    public static Point getPoint ( String key )
    {
        return get ( key, ( Point ) null );
    }

    /**
     * Returns Point value.
     *
     * @param group settings group
     * @param key   settings key
     * @return Point value
     */
    public static Point getPoint ( String group, String key )
    {
        return get ( group, key, ( Point ) null );
    }

    /**
     * Returns Color value.
     *
     * @param key settings key
     * @return Color value
     */
    public static Color getColor ( String key )
    {
        return get ( key, ( Color ) null );
    }

    /**
     * Returns Color value.
     *
     * @param group settings group
     * @param key   settings key
     * @return Color value
     */
    public static Color getColor ( String group, String key )
    {
        return get ( group, key, ( Color ) null );
    }

    /**
     * Returns Rectangle value.
     *
     * @param key settings key
     * @return Rectangle value
     */
    public static Rectangle getRectangle ( String key )
    {
        return get ( key, ( Rectangle ) null );
    }

    /**
     * Returns Rectangle value.
     *
     * @param group settings group
     * @param key   settings key
     * @return Rectangle value
     */
    public static Rectangle getRectangle ( String group, String key )
    {
        return get ( group, key, ( Rectangle ) null );
    }

    /**
     * Returns Insets value.
     *
     * @param key settings key
     * @return Insets value
     */
    public static Insets getInsets ( String key )
    {
        return get ( key, ( Insets ) null );
    }

    /**
     * Returns Insets value.
     *
     * @param group settings group
     * @param key   settings key
     * @return Insets value
     */
    public static Insets getInsets ( String group, String key )
    {
        return get ( group, key, ( Insets ) null );
    }

    /**
     * Returns Object value.
     *
     * @param key settings key
     * @return Object value
     */
    public static <T> T get ( String key )
    {
        return get ( key, ( T ) null );
    }

    /**
     * Returns typed value.
     *
     * @param key               settings key
     * @param defaultValueClass default value class
     * @param <T>               default value type
     * @return typed value
     */
    public static <T extends DefaultValue> T get ( String key, Class<T> defaultValueClass )
    {
        return get ( defaultSettingsGroup, key, getDefaultValue ( defaultValueClass ) );
    }

    /**
     * Returns typed value.
     *
     * @param key          settings key
     * @param defaultValue default value
     * @param <T>          default value type
     * @return typed value
     */
    public static <T> T get ( String key, T defaultValue )
    {
        return get ( defaultSettingsGroup, key, defaultValue );
    }

    /**
     * Returns typed value.
     *
     * @param group             settings group
     * @param key               settings key
     * @param defaultValueClass default value class
     * @param <T>               default value type
     * @return typed value
     */
    public static <T extends DefaultValue> T get ( String group, String key, Class<T> defaultValueClass )
    {
        return get ( group, key, getDefaultValue ( defaultValueClass ) );
    }

    /**
     * Returns typed value.
     *
     * @param group        settings group
     * @param key          settings key
     * @param defaultValue default value
     * @param <T>          default value type
     * @return typed value
     */
    public static <T> T get ( String group, String key, T defaultValue )
    {
        // Check manager initialization
        initialize ();

        // Get SettingsGroup safely
        SettingsGroup settingsGroup = getSettingsGroup ( group );

        // Get value
        Object value = settingsGroup.get ( key );

        // Applying default value if needed
        if ( value == null && defaultValue != null )
        {
            value = defaultValue;

            // Saving default value if needed
            if ( saveDefaultValues )
            {
                set ( group, key, value );
            }
        }

        // Returning default value if no actual found
        try
        {
            return ( T ) value;
        }
        catch ( ClassCastException e )
        {
            // Displaying what caused the problem if needed
            if ( displayExceptions )
            {
                System.err.println ( ERROR_PREFIX + "Unable to load settings value for group \"" + group + "\" and key \"" + key +
                        "\" because it has inappropriate class type:" );
                e.printStackTrace ();
            }

            // Saving default value if needed
            if ( saveDefaultValues )
            {
                set ( group, key, defaultValue );
            }

            return defaultValue;
        }
    }

    /**
     * Returns default value for the specified class.
     *
     * @param defaultValueClass default value class
     * @param <T>               default value type
     * @return default value for the specified class
     */
    public static <T extends DefaultValue> T getDefaultValue ( Class<T> defaultValueClass )
    {
        if ( defaultValueClass == null )
        {
            // Workaround for cases with accidental method calls
            // Also works fine if this method was called but class is actually null
            return null;
        }
        else
        {
            // Call special static method that should be in each class that implements DefaultValue interface
            return ( T ) ReflectUtils.callStaticMethodSafely ( defaultValueClass, "getDefaultValue" );
        }
    }

    /**
     * Sets value for the specified settings key.
     *
     * @param key    settings key
     * @param object new value
     * @param <T>    new value type
     * @return old value for the specified settings key
     */
    public static <T> T set ( String key, T object )
    {
        return set ( defaultSettingsGroup, key, object );
    }

    /**
     * Sets value for the specified settings group and key.
     *
     * @param group  settings group
     * @param key    settings key
     * @param object new value
     * @param <T>    new value type
     * @return old value for the specified settings key
     */
    public static <T> T set ( String group, String key, T object )
    {
        // Check manager initialization
        initialize ();

        // Get SettingsGroup safely
        SettingsGroup settingsGroup = getSettingsGroup ( group );

        // Put new value
        T oldValue = settingsGroup.put ( key, object );

        // Save group if needed
        if ( saveOnChange )
        {
            delayedSaveSettingsGroup ( group );
        }

        // Inform about changes
        fireSettingsChanged ( group, key, object );

        return oldValue;
    }

    /**
     * Returns settings group for the specified name.
     *
     * @param group settings group name
     * @return settings group for the specified name
     */
    public static SettingsGroup getSettingsGroup ( String group )
    {
        if ( groups.containsKey ( group ) )
        {
            return groups.get ( group );
        }
        else
        {
            return loadSettingsGroup ( group );
        }
    }

    /**
     * Loads and returns settings group for the specified name.
     *
     * @param group settings group name
     * @return loaded settings group for the specified name
     */
    private static SettingsGroup loadSettingsGroup ( String group )
    {
        SettingsGroup settingsGroup = null;

        // Settings group file           
        File dir = new File ( getGroupFileLocation ( group ) );
        if ( dir.exists () && dir.isDirectory () )
        {
            File file = new File ( dir, group + settingsFilesExtension );
            File dumpFile = new File ( dir, file.getName () + backupFilesExtension );

            if ( file.exists () && file.isFile () || dumpFile.exists () && dumpFile.isFile () )
            {
                // Check if there is a group dump file and restore it
                boolean readFromDump = false;
                if ( dumpFile.exists () && dumpFile.isFile () )
                {
                    // Replacing file with dump
                    FileUtils.copyFile ( dumpFile, file );

                    // Removing dump file
                    FileUtils.deleteFile ( dumpFile );

                    // Dump read mark
                    readFromDump = true;
                }

                // Try reading SettingsGroup
                if ( file.exists () && file.isFile () )
                {
                    try
                    {
                        // Read single SettingsGroup
                        settingsGroup = XmlUtils.fromXML ( file );

                        // Saving settings group read state
                        groupState.put ( group, new SettingsGroupState ( readFromDump ? ReadState.restored : ReadState.ok ) );
                    }
                    catch ( Throwable e )
                    {
                        // Display errors
                        if ( displayExceptions )
                        {
                            System.err.println ( ERROR_PREFIX + "Unable to load settings group \"" + group +
                                    "\" due to unexpected exception:" );
                            e.printStackTrace ();
                        }

                        // Delete incorrect SettingsGroup file
                        FileUtils.deleteFile ( file );

                        // Saving settings group read state
                        groupState.put ( group, new SettingsGroupState ( ReadState.failed, e ) );
                    }
                }
            }
            else
            {
                // No group settings file or dump exists, new SettingsGroup will be created
                groupState.put ( group, new SettingsGroupState ( ReadState.created ) );
            }
        }
        else
        {
            // No group setting dir exists, new SettingsGroup will be created
            groupState.put ( group, new SettingsGroupState ( ReadState.created ) );
        }

        // Create new SettingsGroup
        if ( settingsGroup == null )
        {
            settingsGroup = new SettingsGroup ( group );
        }

        groups.put ( group, settingsGroup );
        return settingsGroup;
    }

    /**
     * Saves all settings groups and files.
     */
    public static void saveSettings ()
    {
        // Saving all settings groups
        for ( Map.Entry<String, SettingsGroup> entry : groups.entrySet () )
        {
            saveSettingsGroup ( entry.getValue () );
        }

        // Saving all settings files
        for ( Map.Entry<String, Object> entry : files.entrySet () )
        {
            saveSettings ( entry.getKey (), entry.getValue () );
        }

        // Saving all component settings
        ComponentSettingsManager.saveSettings ();
    }

    /**
     * Saves settings group with the specified name.
     *
     * @param group name of the settings group to save
     */
    public static void saveSettingsGroup ( String group )
    {
        saveSettingsGroup ( getSettingsGroup ( group ) );
    }

    /**
     * Saves specified settings group.
     *
     * @param settingsGroup settings group to save
     */
    public static void saveSettingsGroup ( SettingsGroup settingsGroup )
    {
        if ( allowSave )
        {
            try
            {
                // Used values
                String group = settingsGroup.getName ();
                File dir = new File ( getGroupFileLocation ( group ) );

                // Ensure group settings directory exists and perform save
                if ( FileUtils.ensureDirectoryExists ( dir ) )
                {
                    // Settings file
                    File file = new File ( dir, group + settingsFilesExtension );

                    // Creating settings dump if there are old settings
                    File dumpFile = null;
                    if ( file.exists () )
                    {
                        dumpFile = new File ( dir, group + settingsFilesExtension + backupFilesExtension );
                        FileUtils.copyFile ( file, dumpFile );
                    }

                    // Saving settings
                    XmlUtils.toXML ( settingsGroup, file );

                    // Clearing dump file
                    if ( dumpFile != null && dumpFile.exists () )
                    {
                        FileUtils.deleteFile ( dumpFile );
                    }
                }
                else
                {
                    throw new RuntimeException ( "Cannot create settings directory: " + dir.getAbsolutePath () );
                }
            }
            catch ( Throwable e )
            {
                if ( displayExceptions )
                {
                    System.err.println ( ERROR_PREFIX + "Unable to save settings group \"" + settingsGroup.getName () +
                            "\" due to unexpected exception:" );
                    e.printStackTrace ();
                }
            }
        }
    }

    /**
     * Delays settings group save or performs it immediately according to settings manager configuration.
     *
     * @param group name of the settings group to save
     */
    private static void delayedSaveSettingsGroup ( String group )
    {
        // Determining when we should save changes into file system
        if ( saveOnChangeDelay > 0 )
        {
            // Delaying save
            synchronized ( saveOnChangeLock )
            {
                // Adding group for delayed save
                if ( !groupsToSaveOnChange.contains ( group ) )
                {
                    groupsToSaveOnChange.add ( group );
                }

                // Launching scheduler if it is not yet launched
                if ( groupSaveScheduler == null || !groupSaveScheduler.isRunning () )
                {
                    if ( groupSaveScheduler == null )
                    {
                        groupSaveScheduler = new WebTimer ( "SettingsManager.groupSaveScheduler", saveOnChangeDelay, new ActionListener ()
                        {
                            @Override
                            public void actionPerformed ( ActionEvent e )
                            {
                                synchronized ( saveOnChangeLock )
                                {
                                    for ( String group : groupsToSaveOnChange )
                                    {
                                        saveSettingsGroup ( group );
                                    }
                                    groupsToSaveOnChange.clear ();
                                }
                            }
                        } );
                        groupSaveScheduler.setRepeats ( false );
                    }
                    else
                    {
                        groupSaveScheduler.setDelay ( saveOnChangeDelay );
                    }
                    groupSaveScheduler.start ();
                }
            }
        }
        else
        {
            // Saving right away
            saveSettingsGroup ( group );
        }
    }

    /**
     * Returns settings group state.
     *
     * @param group settings group name
     * @return settings group state
     */
    public SettingsGroupState getSettingsGroupState ( String group )
    {
        if ( !groupState.containsKey ( group ) )
        {
            groupState.put ( group, new SettingsGroupState ( ReadState.none ) );
        }
        return groupState.get ( group );
    }

    /**
     * Returns value read from the settings file.
     *
     * @param fileName settings file name
     * @return value read from the settings file
     */
    public static Object getSettings ( String fileName )
    {
        return getSettings ( fileName, null );
    }

    /**
     * Returns value read from the settings file.
     *
     * @param fileName     settings file name
     * @param defaultValue default value class
     * @param <T>          default value type
     * @return value read from the settings file
     */
    public static <T> T getSettings ( String fileName, T defaultValue )
    {
        if ( files.containsKey ( fileName ) )
        {
            return ( T ) files.get ( fileName );
        }
        else
        {
            Object value;
            try
            {
                value = XmlUtils.fromXML ( getSettingsFile ( fileName ) );
            }
            catch ( Throwable e )
            {
                value = defaultValue;
            }
            files.put ( fileName, value );
            return ( T ) value;
        }
    }

    /**
     * Sets and saves value into settings file with the specified name.
     *
     * @param fileName settings file name
     * @param settings value
     */
    public static void setSettings ( String fileName, Object settings )
    {
        files.put ( fileName, settings );
        if ( saveOnChange )
        {
            saveSettings ( fileName, settings );
        }
    }

    /**
     * Saves value into settings file with the specified name.
     *
     * @param fileName settings file name
     * @param settings value
     */
    private static void saveSettings ( String fileName, Object settings )
    {
        if ( allowSave )
        {
            XmlUtils.toXML ( settings, getSettingsFile ( fileName ) );
        }
    }

    /**
     * Returns whether settings file with the specified name exists or not.
     *
     * @param fileName settings file name
     * @return true if settings file with the specified name exists, false otherwise
     */
    public static boolean settingsExists ( String fileName )
    {
        return getSettingsFile ( fileName ).exists ();
    }

    /**
     * Returns settings file for the specified file name.
     *
     * @param fileName settings file name
     * @return settings file for the specified file name
     */
    private static File getSettingsFile ( String fileName )
    {
        return new File ( getDefaultSettingsDir (), fileName );
    }

    /**
     * Returns settings files extension.
     *
     * @return settings files extension
     */
    public static String getSettingsFilesExtension ()
    {
        return settingsFilesExtension;
    }

    /**
     * Sets settings files extension
     *
     * @param settingsFilesExtension new settings files extension
     */
    public static void setSettingsFilesExtension ( String settingsFilesExtension )
    {
        SettingsManager.settingsFilesExtension = settingsFilesExtension;
    }

    /**
     * Returns backup files extension.
     *
     * @return backup files extension
     */
    public static String getBackupFilesExtension ()
    {
        return backupFilesExtension;
    }

    /**
     * Sets backup files extension
     *
     * @param backupFilesExtension new backup files extension
     */
    public static void setBackupFilesExtension ( String backupFilesExtension )
    {
        SettingsManager.backupFilesExtension = backupFilesExtension;
    }

    /**
     * Returns default settings directory.
     *
     * @return default settings directory
     */
    public static String getDefaultSettingsDir ()
    {
        if ( defaultSettingsDir == null )
        {
            return FileUtils.getUserHomePath () + defaultSettingsDirName;
        }
        else
        {
            return defaultSettingsDir;
        }
    }

    /**
     * Sets default settings directory.
     *
     * @param defaultSettingsDir new default settings directory
     */
    public static void setDefaultSettingsDir ( String defaultSettingsDir )
    {
        SettingsManager.defaultSettingsDir = defaultSettingsDir;
    }

    /**
     * Returns default settings directory name.
     *
     * @return default settings directory name
     */
    public static String getDefaultSettingsDirName ()
    {
        return defaultSettingsDirName;
    }

    /**
     * Sets default settings directory name.
     *
     * @param defaultSettingsDir new default settings directory name
     */
    public static void setDefaultSettingsDirName ( String defaultSettingsDir )
    {
        SettingsManager.defaultSettingsDirName = defaultSettingsDir;
    }

    /**
     * Returns default settings group name.
     *
     * @return default settings group name
     */
    public static String getDefaultSettingsGroup ()
    {
        return defaultSettingsGroup;
    }

    /**
     * Sets default settings group name.
     *
     * @param defaultSettingsGroup new default settings group name
     */
    public static void setDefaultSettingsGroup ( String defaultSettingsGroup )
    {
        SettingsManager.defaultSettingsGroup = defaultSettingsGroup;
    }

    /**
     * Sets custom directory where settings group file will be saved.
     * Location that was used for this group before will be simply ignored.
     *
     * @param group settings group name
     * @param dir   settings group file directory
     */
    public static void setGroupFileLocation ( String group, String dir )
    {
        groupFileLocation.put ( group, dir );
    }

    /**
     * Returns directory where settings group file is saved.
     *
     * @param group settings group name
     * @return directory where settings group file is saved
     */
    public static String getGroupFileLocation ( String group )
    {
        if ( groupFileLocation.containsKey ( group ) )
        {
            return groupFileLocation.get ( group );
        }
        else
        {
            return getDefaultSettingsDir ();
        }
    }

    /**
     * Returns whether should save settings right after any changes made or not.
     *
     * @return true if should save settings right after any changes made, false otherwise
     */
    public static boolean isSaveOnChange ()
    {
        return saveOnChange;
    }

    /**
     * Sets whether should save settings right after any changes made or not.
     *
     * @param saveOnChange whether should save settings right after any changes made or not
     */
    public static void setSaveOnChange ( boolean saveOnChange )
    {
        SettingsManager.saveOnChange = saveOnChange;
    }

    /**
     * Sets whether should save provided default value in "get" calls or not.
     *
     * @return true if should save provided default value in "get" calls, false otherwise
     */
    public static boolean isSaveDefaultValues ()
    {
        return saveDefaultValues;
    }

    /**
     * Sets whether should save provided default value in "get" calls or not.
     *
     * @param saveDefaultValues whether should save provided default value in "get" calls or not
     */
    public static void setSaveDefaultValues ( boolean saveDefaultValues )
    {
        SettingsManager.saveDefaultValues = saveDefaultValues;
    }

    /**
     * Returns save-on-change delay in milliseconds.
     *
     * @return save-on-change delay in milliseconds.
     */
    public static long getSaveOnChangeDelay ()
    {
        return saveOnChangeDelay;
    }

    /**
     * Sets save-on-change delay in milliseconds.
     * If larger than 0 then settings will be accumulated and saved all at once as soon as no new changes came within the delay time.
     *
     * @param saveOnChangeDelay new save-on-change delay in milliseconds
     */
    public static void setSaveOnChangeDelay ( long saveOnChangeDelay )
    {
        SettingsManager.saveOnChangeDelay = saveOnChangeDelay;
    }

    /**
     * Returns whether should display settings load and save error messages or not.
     *
     * @return true if should display settings load and save error messages, false otherwise
     */
    public static boolean isDisplayExceptions ()
    {
        return displayExceptions;
    }

    /**
     * Sets whether should display settings load and save error messages or not.
     *
     * @param displayExceptions whether should display settings load and save error messages or not
     */
    public static void setDisplayExceptions ( boolean displayExceptions )
    {
        SettingsManager.displayExceptions = displayExceptions;
    }

    /**
     * Returns whether should allow saving settings into files or not.
     *
     * @return true if should allow saving settings into files, false otherwise
     */
    public static boolean isAllowSave ()
    {
        return allowSave;
    }

    /**
     * Sets whether should allow saving settings into files or not.
     *
     * @param allowSave whether should allow saving settings into files or not
     */
    public static void setAllowSave ( boolean allowSave )
    {
        SettingsManager.allowSave = allowSave;
    }

    /**
     * Disables saving settings into files.
     */
    public static void disableSave ()
    {
        setAllowSave ( false );
    }

    /**
     * Enables saving settings into files.
     */
    public static void enableSave ()
    {
        setAllowSave ( true );
    }

    /**
     * Adds setting listener.
     *
     * @param key      settings key to listen to
     * @param listener settings listener to add
     */
    public static void addSettingsListener ( String key, SettingsListener listener )
    {
        addSettingsListener ( getDefaultSettingsGroup (), key, listener );
    }

    /**
     * Adds setting listener.
     *
     * @param group    settings group to listen to
     * @param key      settings key to listen to
     * @param listener settings listener to add
     */
    public static void addSettingsListener ( String group, String key, SettingsListener listener )
    {
        if ( !settingsListeners.containsKey ( group ) )
        {
            settingsListeners.put ( group, new HashMap<String, List<SettingsListener>> () );
        }
        if ( !settingsListeners.get ( group ).containsKey ( key ) )
        {
            settingsListeners.get ( group ).put ( key, new ArrayList<SettingsListener> () );
        }
        settingsListeners.get ( group ).get ( key ).add ( listener );
    }

    /**
     * Removes settings listener.
     *
     * @param key      listened settings key
     * @param listener settings listener to remove
     */
    public static void removeSettingsListener ( String key, SettingsListener listener )
    {
        removeSettingsListener ( getDefaultSettingsGroup (), key, listener );
    }

    /**
     * Removes settings listener.
     *
     * @param group    listened settings group
     * @param key      listened settings key
     * @param listener settings listener to remove
     */
    public static void removeSettingsListener ( String group, String key, SettingsListener listener )
    {
        if ( settingsListeners.containsKey ( group ) )
        {
            if ( settingsListeners.get ( group ).containsKey ( key ) )
            {
                settingsListeners.get ( group ).get ( key ).remove ( listener );
            }
        }
    }

    /**
     * Notifies that value for the specified settings group and key has changed.
     *
     * @param group    settings group
     * @param key      settings key
     * @param newValue new value
     */
    private static void fireSettingsChanged ( String group, String key, Object newValue )
    {
        if ( settingsListeners.containsKey ( group ) )
        {
            if ( settingsListeners.get ( group ).containsKey ( key ) )
            {
                for ( SettingsListener listener : CollectionUtils.copy ( settingsListeners.get ( group ).get ( key ) ) )
                {
                    listener.settingsChanged ( group, key, newValue );
                }
            }
        }
    }
}