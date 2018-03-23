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

import com.alee.api.jdk.Supplier;
import com.alee.utils.CollectionUtils;
import com.alee.utils.FileUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.XmlUtils;
import com.alee.utils.swing.WebTimer;
import org.slf4j.LoggerFactory;

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
 *
 * You can define settings group file name (they are equal to the SettingsGroup name and should be unique), its location on local or
 * remote drive and each separate property key. Any amount of properties could be stored in each group. Also there are some additional
 * manager settings that allows you to configure SettingsManager to work the way you want it to. The rest of the work is done by the
 * SettingsManager - it decides where, when and how to save the settings files.
 *
 * Settings data aliasing (see XStream documentation and {@link XmlUtils} class) or SettingsGroup file location changes should be done
 * before requesting any of the settings, preferably right at the application startup. Otherwise data might not be properly read and
 * settings will appear empty.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see XmlUtils
 */

public final class SettingsManager
{
    /**
     * Settings change listeners.
     */
    private static final Map<String, Map<String, List<SettingsListener>>> settingsListeners =
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
    private static final Map<String, String> groupFileLocation = new HashMap<String, String> ();

    /**
     * Group settings read success marks.
     */
    private static final Map<String, SettingsGroupState> groupState = new HashMap<String, SettingsGroupState> ();

    /**
     * Cached settings map.
     */
    private static final Map<String, SettingsGroup> groups = new HashMap<String, SettingsGroup> ();

    /**
     * Cached files map.
     */
    private static final Map<String, Object> files = new HashMap<String, Object> ();

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
    private static final List<String> groupsToSaveOnChange = new ArrayList<String> ();

    /**
     * Whether or not settings save logging is enabled.
     */
    private static boolean saveLoggingEnabled = false;

    /**
     * Whether should allow saving settings into files or not.
     * If set to false settings will be available only in runtime and will be lost after application finishes working.
     */
    private static boolean allowSave = true;

    /**
     * Whether {@link SettingsManager} is initialized or not.
     */
    private static boolean initialized = false;

    /**
     * Initializes {@link SettingsManager}.
     */
    public static synchronized void initialize ()
    {
        if ( !initialized )
        {
            // Aliases
            XmlUtils.processAnnotations ( SettingsGroup.class );
            XmlUtils.processAnnotations ( SettingsGroupState.class );
            XmlUtils.processAnnotations ( ReadState.class );

            // Updating initialization mark
            initialized = true;
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
     * Returns Boolean value.
     *
     * @param key settings key
     * @return Boolean value
     */
    public static Boolean getBoolean ( final String key )
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
    public static Boolean getBoolean ( final String group, final String key )
    {
        return get ( group, key, ( Boolean ) null );
    }

    /**
     * Returns String value.
     *
     * @param key settings key
     * @return String value
     */
    public static String getString ( final String key )
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
    public static String getString ( final String group, final String key )
    {
        return get ( group, key, ( String ) null );
    }

    /**
     * Returns Integer value.
     *
     * @param key settings key
     * @return Integer value
     */
    public static Integer getInteger ( final String key )
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
    public static Integer getInteger ( final String group, final String key )
    {
        return get ( group, key, ( Integer ) null );
    }

    /**
     * Returns Long value.
     *
     * @param key settings key
     * @return Long value
     */
    public static Long getLong ( final String key )
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
    public static Long getLong ( final String group, final String key )
    {
        return get ( group, key, ( Long ) null );
    }

    /**
     * Returns Float value.
     *
     * @param key settings key
     * @return Float value
     */
    public static Float getFloat ( final String key )
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
    public static Float getFloat ( final String group, final String key )
    {
        return get ( group, key, ( Float ) null );
    }

    /**
     * Returns Double value.
     *
     * @param key settings key
     * @return Double value
     */
    public static Double getDouble ( final String key )
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
    public static Double getDouble ( final String group, final String key )
    {
        return get ( group, key, ( Double ) null );
    }

    /**
     * Returns Dimension value.
     *
     * @param key settings key
     * @return Dimension value
     */
    public static Dimension getDimension ( final String key )
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
    public static Dimension getDimension ( final String group, final String key )
    {
        return get ( group, key, ( Dimension ) null );
    }

    /**
     * Returns Point value.
     *
     * @param key settings key
     * @return Point value
     */
    public static Point getPoint ( final String key )
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
    public static Point getPoint ( final String group, final String key )
    {
        return get ( group, key, ( Point ) null );
    }

    /**
     * Returns Color value.
     *
     * @param key settings key
     * @return Color value
     */
    public static Color getColor ( final String key )
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
    public static Color getColor ( final String group, final String key )
    {
        return get ( group, key, ( Color ) null );
    }

    /**
     * Returns Rectangle value.
     *
     * @param key settings key
     * @return Rectangle value
     */
    public static Rectangle getRectangle ( final String key )
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
    public static Rectangle getRectangle ( final String group, final String key )
    {
        return get ( group, key, ( Rectangle ) null );
    }

    /**
     * Returns Insets value.
     *
     * @param key settings key
     * @return Insets value
     */
    public static Insets getInsets ( final String key )
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
    public static Insets getInsets ( final String group, final String key )
    {
        return get ( group, key, ( Insets ) null );
    }

    /**
     * Returns Object value.
     *
     * @param key settings key
     * @return Object value
     */
    public static <T> T get ( final String key )
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
    public static <T extends DefaultValue> T get ( final String key, final Class<T> defaultValueClass )
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
    public static <T> T get ( final String key, final T defaultValue )
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
    public static <T extends DefaultValue> T get ( final String group, final String key, final Class<T> defaultValueClass )
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
    public static <T> T get ( final String group, final String key, final T defaultValue )
    {
        // Check manager initialization
        initialize ();

        // Get SettingsGroup safely
        final SettingsGroup settingsGroup = getSettingsGroup ( group );

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

        try
        {
            // Returning retrieved value
            return ( T ) value;
        }
        catch ( final ClassCastException e )
        {
            final String msg = "Unable to load settings value for group '%s' and key '%s' because it has inappropriate class type";
            final String fmsg = String.format ( msg, group, key );
            LoggerFactory.getLogger ( SettingsManager.class ).error ( fmsg, e );

            // Saving default value if needed
            if ( saveDefaultValues )
            {
                set ( group, key, defaultValue );
            }

            // Returning default value if no actual found
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
    public static <T extends DefaultValue> T getDefaultValue ( final Class<T> defaultValueClass )
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
            return ReflectUtils.callStaticMethodSafely ( defaultValueClass, "getDefaultValue" );
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
    public static <T> T set ( final String key, final T object )
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
    public static <T> T set ( final String group, final String key, final T object )
    {
        // Check manager initialization
        initialize ();

        // Get SettingsGroup safely
        final SettingsGroup settingsGroup = getSettingsGroup ( group );

        // Put new value
        final T oldValue = settingsGroup.put ( key, object );

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
     * Resets all settings within the default settings group.
     */
    public static void resetDefaultGroup ()
    {
        resetGroup ( defaultSettingsGroup );
    }

    /**
     * Resets all settings within the specified settings group.
     * This will also reset saved settings to ensure they won't be read again.
     *
     * @param group settings group
     */
    public static void resetGroup ( final String group )
    {
        // Removing group file if it exists
        final File dir = new File ( getGroupFilePath ( group ) );
        if ( dir.exists () && dir.isDirectory () )
        {
            final File file = getGroupFile ( group, dir );
            final File backupFile = getGroupBackupFile ( group, dir );
            FileUtils.deleteFiles ( file, backupFile );
        }

        // Resetting group if it was already loaded
        if ( groups.containsKey ( group ) )
        {
            groups.remove ( group );
        }
    }

    /**
     * Resets settings under the key within the default settings group.
     *
     * @param key settings key to reset
     * @param <T> value type
     * @return resetted value
     */
    public static <T> T resetValue ( final String key )
    {
        return resetValue ( defaultSettingsGroup, key );
    }

    /**
     * Resets settings under the key within the specified settings group.
     *
     * @param group settings group
     * @param key   settings key to reset
     * @param <T>   value type
     * @return resetted value
     */
    public static <T> T resetValue ( final String group, final String key )
    {
        Object oldValue = null;

        // Resetting settings under the specified key in the group
        final SettingsGroup settingsGroup = getSettingsGroup ( group );
        if ( settingsGroup != null )
        {
            oldValue = settingsGroup.remove ( key );
        }

        // Forcing settings group save in case value was reset
        if ( oldValue != null )
        {
            saveSettingsGroup ( group );
        }

        return ( T ) oldValue;
    }

    /**
     * Returns settings group for the specified name.
     *
     * @param group settings group name
     * @return settings group for the specified name
     */
    public static SettingsGroup getSettingsGroup ( final String group )
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
    private static SettingsGroup loadSettingsGroup ( final String group )
    {
        SettingsGroup settingsGroup = null;

        // Settings group file           
        final File dir = new File ( getGroupFilePath ( group ) );
        if ( dir.exists () && dir.isDirectory () )
        {
            final File file = getGroupFile ( group, dir );
            final File backupFile = getGroupBackupFile ( group, dir );

            // todo Modify read logic so that;
            // todo 1. Backup reading occurs only if original file cannot be read
            // todo 2. Do not delete the settings that cannot be read right away, just move them aside with ".failed" extension mark
            if ( file.exists () && file.isFile () || backupFile.exists () && backupFile.isFile () )
            {
                // Check if there is a group backup file and restore it
                boolean readFromBackup = false;
                if ( backupFile.exists () && backupFile.isFile () )
                {
                    // Replacing file with backup
                    FileUtils.copyFile ( backupFile, file );

                    // Removing backup file
                    FileUtils.deleteFile ( backupFile );

                    // Backup read mark
                    readFromBackup = true;
                }

                // Try reading SettingsGroup
                if ( file.exists () && file.isFile () )
                {
                    try
                    {
                        // Read single SettingsGroup
                        settingsGroup = XmlUtils.fromXML ( file );

                        // Saving settings group read state
                        groupState.put ( group, new SettingsGroupState ( readFromBackup ? ReadState.restored : ReadState.ok ) );

                        final String state = readFromBackup ? "restored from backup" : "loaded";
                        final String msg = "Settings group '%s' %s successfully";
                        LoggerFactory.getLogger ( SettingsManager.class ).info ( String.format ( msg, group, state ) );
                    }
                    catch ( final Exception e )
                    {
                        final String msg = "Unable to load settings group '%s' due to unexpected exception";
                        LoggerFactory.getLogger ( SettingsManager.class ).error ( String.format ( msg, group ), e );

                        // Delete incorrect SettingsGroup file
                        FileUtils.deleteFile ( file );

                        // Saving settings group read state
                        groupState.put ( group, new SettingsGroupState ( ReadState.failed, e ) );
                    }
                }
            }
            else
            {
                // No group settings file or backup exists, new SettingsGroup will be created
                groupState.put ( group, new SettingsGroupState ( ReadState.created ) );

                final String msg = "Settings group '%s' created successfully";
                LoggerFactory.getLogger ( SettingsManager.class ).info ( String.format ( msg, group ) );
            }
        }
        else
        {
            // No group setting dir exists, new SettingsGroup will be created
            groupState.put ( group, new SettingsGroupState ( ReadState.created ) );

            final String msg = "Settings group '%s' created successfully";
            LoggerFactory.getLogger ( SettingsManager.class ).info ( String.format ( msg, group ) );
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
        for ( final Map.Entry<String, SettingsGroup> entry : groups.entrySet () )
        {
            saveSettingsGroup ( entry.getValue () );
        }

        // Saving all settings files
        for ( final Map.Entry<String, Object> entry : files.entrySet () )
        {
            saveSettings ( entry.getKey (), entry.getValue () );
        }
    }

    /**
     * Saves settings group with the specified name.
     *
     * @param group name of the settings group to save
     */
    public static void saveSettingsGroup ( final String group )
    {
        saveSettingsGroup ( getSettingsGroup ( group ) );
    }

    /**
     * Saves specified settings group.
     *
     * @param settingsGroup settings group to save
     */
    public static void saveSettingsGroup ( final SettingsGroup settingsGroup )
    {
        if ( allowSave )
        {
            try
            {
                // Used values
                final String group = settingsGroup.getName ();
                final File dir = new File ( getGroupFilePath ( group ) );

                // Ensure group settings directory exists and perform save
                if ( FileUtils.ensureDirectoryExists ( dir ) )
                {
                    // Settings file
                    final File file = getGroupFile ( group, dir );

                    // Creating settings backup if there are old settings
                    File backupFile = null;
                    if ( file.exists () )
                    {
                        backupFile = getGroupBackupFile ( group, dir );
                        FileUtils.copyFile ( file, backupFile );
                    }

                    // Saving settings
                    XmlUtils.toXML ( settingsGroup, file );

                    // Removing backup file if save was successful
                    if ( backupFile != null && backupFile.exists () )
                    {
                        FileUtils.deleteFile ( backupFile );
                    }

                    if ( saveLoggingEnabled )
                    {
                        final String msg = "Settings group '%s' saved successfully";
                        LoggerFactory.getLogger ( SettingsManager.class ).info ( String.format ( msg, group ) );
                    }
                }
                else
                {
                    final String msg = "Cannot create settings directory: %s";
                    throw new SettingsException ( String.format ( msg, dir.getAbsolutePath () ) );
                }
            }
            catch ( final Exception e )
            {
                final String msg = "Unable to save settings group '%s' due to unexpected exception";
                LoggerFactory.getLogger ( SettingsManager.class ).error ( String.format ( msg, settingsGroup.getName () ), e );
            }
        }
    }

    /**
     * Returns actual settings group file.
     *
     * @param group settings group name
     * @param dir   settings directory
     * @return actual settings group file
     */
    private static File getGroupFile ( final String group, final File dir )
    {
        return new File ( dir, group + settingsFilesExtension );
    }

    /**
     * Returns backup settings group file.
     *
     * @param group settings group name
     * @param dir   settings directory
     * @return backup settings group file
     */
    private static File getGroupBackupFile ( final String group, final File dir )
    {
        return new File ( dir, group + settingsFilesExtension + backupFilesExtension );
    }

    /**
     * Delays settings group save or performs it immediately according to settings manager configuration.
     *
     * @param group name of the settings group to save
     */
    private static void delayedSaveSettingsGroup ( final String group )
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
                            public void actionPerformed ( final ActionEvent e )
                            {
                                synchronized ( saveOnChangeLock )
                                {
                                    for ( final String group : groupsToSaveOnChange )
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
    public SettingsGroupState getSettingsGroupState ( final String group )
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
    public static Object getSettings ( final String fileName )
    {
        return getSettings ( fileName, null );
    }

    /**
     * Returns value read from the settings file.
     *
     * @param fileName     settings file name
     * @param defaultValue default value
     * @param <T>          default value type
     * @return value read from the settings file
     */
    public static <T> T getSettings ( final String fileName, final T defaultValue )
    {
        return getSettings ( fileName, new Supplier<T> ()
        {
            @Override
            public T get ()
            {
                return defaultValue;
            }
        } );
    }

    /**
     * Returns value read from the settings file.
     *
     * @param fileName     settings file name
     * @param defaultValue default value supplier
     * @param <T>          default value type
     * @return value read from the settings file
     */
    public static <T> T getSettings ( final String fileName, final Supplier<T> defaultValue )
    {
        if ( files.containsKey ( fileName ) )
        {
            return ( T ) files.get ( fileName );
        }
        else
        {
            T value;
            try
            {
                final File settingsFile = getSettingsFile ( fileName );
                if ( settingsFile.exists () )
                {
                    value = XmlUtils.fromXML ( settingsFile );
                }
                else
                {
                    value = defaultValue != null ? defaultValue.get () : null;
                }
            }
            catch ( final Exception e )
            {
                final String msg = "Unable to load settings file '%s' due to unexpected exception";
                LoggerFactory.getLogger ( SettingsManager.class ).error ( String.format ( msg, fileName ), e );

                value = defaultValue != null ? defaultValue.get () : null;
            }
            files.put ( fileName, value );
            return value;
        }
    }

    /**
     * Sets and saves value into settings file with the specified name.
     *
     * @param fileName settings file name
     * @param settings value
     */
    public static void setSettings ( final String fileName, final Object settings )
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
    private static void saveSettings ( final String fileName, final Object settings )
    {
        if ( allowSave )
        {
            final File settingsFile = getSettingsFile ( fileName );
            final File dir = FileUtils.getParent ( settingsFile );
            if ( FileUtils.ensureDirectoryExists ( dir ) )
            {
                XmlUtils.toXML ( settings, settingsFile );
            }
            else
            {
                final String msg = "Cannot create settings directory: %s";
                throw new SettingsException ( String.format ( msg, dir.getAbsolutePath () ) );
            }
        }
    }

    /**
     * Returns whether settings file with the specified name exists or not.
     *
     * @param fileName settings file name
     * @return true if settings file with the specified name exists, false otherwise
     */
    public static boolean settingsExists ( final String fileName )
    {
        return getSettingsFile ( fileName ).exists ();
    }

    /**
     * Returns settings file for the specified file name.
     *
     * @param fileName settings file name
     * @return settings file for the specified file name
     */
    private static File getSettingsFile ( final String fileName )
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
    public static void setSettingsFilesExtension ( final String settingsFilesExtension )
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
    public static void setBackupFilesExtension ( final String backupFilesExtension )
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
    public static void setDefaultSettingsDir ( final String defaultSettingsDir )
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
    public static void setDefaultSettingsDirName ( final String defaultSettingsDir )
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
    public static void setDefaultSettingsGroup ( final String defaultSettingsGroup )
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
    public static void setGroupFilePath ( final String group, final String dir )
    {
        groupFileLocation.put ( group, dir );
    }

    /**
     * Returns directory where settings group file is saved.
     *
     * @param group settings group name
     * @return directory where settings group file is saved
     */
    public static String getGroupFilePath ( final String group )
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
    public static void setSaveOnChange ( final boolean saveOnChange )
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
    public static void setSaveDefaultValues ( final boolean saveDefaultValues )
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
    public static void setSaveOnChangeDelay ( final long saveOnChangeDelay )
    {
        SettingsManager.saveOnChangeDelay = saveOnChangeDelay;
    }

    /**
     * Returns whether settings save log is enabled or not.
     *
     * @return true if settings save log is enabled, false otherwise
     */
    public static boolean isSaveLoggingEnabled ()
    {
        return saveLoggingEnabled;
    }

    /**
     * Sets whether settings save log is enabled or not.
     *
     * @param enabled whether settings save log is enabled or not
     */
    public static void setSaveLoggingEnabled ( final boolean enabled )
    {
        SettingsManager.saveLoggingEnabled = enabled;
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
    public static void setAllowSave ( final boolean allowSave )
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
    public static void addSettingsListener ( final String key, final SettingsListener listener )
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
    public static void addSettingsListener ( final String group, final String key, final SettingsListener listener )
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
    public static void removeSettingsListener ( final String key, final SettingsListener listener )
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
    public static void removeSettingsListener ( final String group, final String key, final SettingsListener listener )
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
    private static void fireSettingsChanged ( final String group, final String key, final Object newValue )
    {
        if ( settingsListeners.containsKey ( group ) )
        {
            if ( settingsListeners.get ( group ).containsKey ( key ) )
            {
                for ( final SettingsListener listener : CollectionUtils.copy ( settingsListeners.get ( group ).get ( key ) ) )
                {
                    listener.settingsChanged ( group, key, newValue );
                }
            }
        }
    }
}