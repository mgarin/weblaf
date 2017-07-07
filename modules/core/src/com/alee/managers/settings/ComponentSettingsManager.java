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

import com.alee.managers.settings.processors.*;
import com.alee.managers.settings.processors.data.WindowSettings;
import com.alee.utils.ReflectUtils;
import com.alee.utils.XmlUtils;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * This SettingsManager sub-manager registers and processes component settings auto-save/restore them on any changes within or outside of
 * that component.
 * <p>
 * This manager should never be called directly (except the case when you register new SettingsProcessor or if you know what you are doing)
 * to avoid any unexpected component behavior.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see com.alee.managers.settings.SettingsManager
 */

public final class ComponentSettingsManager
{
    /**
     * todo 1. Remove {@link #throwExceptions} and always properly throw them
     * todo 2. JListSettingsProcessor
     * todo 3. JTableSettingsProcessor
     * todo 4. JScrollPaneSettingsProcessor
     */

    /**
     * Keys used to store custom data in JComponent.
     */
    public static final String COMPONENT_SETTINGS_PROCESSOR_KEY = "settings.processor";

    /**
     * Registered settings processor classes.
     */
    protected static final Map<Class, Class> settingsProcessorClasses = new LinkedHashMap<Class, Class> ();

    /**
     * Registered component settings processors.
     */
    protected static final Map<JComponent, WeakReference<SettingsProcessor>> settingsProcessors =
            new WeakHashMap<JComponent, WeakReference<SettingsProcessor>> ();

    /**
     * Whether throw exceptions on inappropriate actions or not.
     */
    protected static boolean throwExceptions = true;

    /**
     * Whether ComponentSettingsManager is initialized or not.
     */
    protected static boolean initialized = false;

    /**
     * Initializes ComponentSettingsManager.
     */
    public static void initialize ()
    {
        if ( !initialized )
        {
            initialized = true;

            // Initializing base settings processors
            registerSettingsProcessor ( AbstractButton.class, ButtonSettingsProcessor.class );
            registerSettingsProcessor ( JComboBox.class, ComboBoxSettingsProcessor.class );
            registerSettingsProcessor ( JSlider.class, SliderSettingsProcessor.class );
            registerSettingsProcessor ( JPasswordField.class, PasswordFieldSettingsProcessor.class );
            registerSettingsProcessor ( JTextField.class, TextFieldSettingsProcessor.class );
            registerSettingsProcessor ( JTextComponent.class, TextComponentSettingsProcessor.class );
            registerSettingsProcessor ( JScrollBar.class, ScrollBarSettingsProcessor.class );
            registerSettingsProcessor ( JSplitPane.class, SplitPaneSettingsProcessor.class );
            registerSettingsProcessor ( JTabbedPane.class, TabbedPaneSettingsProcessor.class );
            registerSettingsProcessor ( JRootPane.class, RootPaneSettingsProcessor.class );

            // Initializing data aliases
            XmlUtils.processAnnotations ( WindowSettings.class );
        }
    }

    /**
     * Returns whether exceptions are thrown on inappropriate actions or not.
     *
     * @return true if exceptions are thrown on inappropriate actions, false otherwise
     */
    public static boolean isThrowExceptions ()
    {
        return throwExceptions;
    }

    /**
     * Sets whether throw exceptions on inappropriate actions or not.
     *
     * @param throwExceptions whether throw exceptions on inappropriate actions or not
     */
    public static void setThrowExceptions ( final boolean throwExceptions )
    {
        ComponentSettingsManager.throwExceptions = throwExceptions;
    }

    /**
     * Returns whether the specified component is supported or not.
     *
     * @param component component
     * @return true if the specified component is supported, false otherwise
     */
    public static boolean isComponentSupported ( final JComponent component )
    {
        return isComponentSupported ( component.getClass () );
    }

    /**
     * Returns whether the specified component is supported or not.
     *
     * @param componentType component type
     * @return true if the specified component is supported, false otherwise
     */
    public static boolean isComponentSupported ( final Class<? extends JComponent> componentType )
    {
        // Checking if a suitable component processor exists
        return findSuitableSettingsProcessor ( componentType ) != null;
    }

    /**
     * Registers specified settings processor class for the specified component type.
     *
     * @param componentType     component type
     * @param settingsProcessor settings processor class
     * @param <T>               settings processor type
     */
    public static <T extends SettingsProcessor> void registerSettingsProcessor ( final Class<? extends JComponent> componentType,
                                                                                 final Class<T> settingsProcessor )
    {
        // Saving settings processor under component type
        settingsProcessorClasses.put ( componentType, settingsProcessor );
    }

    /**
     * Returns settings processor class for the specified component type.
     *
     * @param componentType component type
     * @return settings processor class for the specified component type
     */
    protected static Class findSuitableSettingsProcessor ( final Class componentType )
    {
        // Looking through map with strict elements order for proper settings processor
        for ( final Class type : settingsProcessorClasses.keySet () )
        {
            if ( ReflectUtils.isAssignable ( type, componentType ) )
            {
                return settingsProcessorClasses.get ( type );
            }
        }
        return null;
    }

    /**
     * Returns new SettingsProcessor instance for the specified SettingsProcessorData.
     *
     * @param data SettingsProcessorData
     * @return new SettingsProcessor instance for the specified SettingsProcessorData
     */
    protected static SettingsProcessor createSettingsProcessor ( final SettingsProcessorData data )
    {
        // Creating new settings processor from registered class
        final Class settingsProcessorClass = findSuitableSettingsProcessor ( data.getComponent ().getClass () );
        if ( settingsProcessorClass != null )
        {
            try
            {
                return ReflectUtils.createInstance ( settingsProcessorClass, data );
            }
            catch ( final Exception e )
            {
                if ( throwExceptions )
                {
                    // Throw cannot instantiate SettingsProcessor exception
                    final String msg = "Unable to instantiate SettingsProcessor class: %s";
                    throw new SettingsException ( String.format ( msg, settingsProcessorClass ), e );
                }
                else
                {
                    return null;
                }
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * Registers component using the specified SettingsProcessorData.
     * Any old SettingsProcessor for that component will be unregistered if operation is successful.
     *
     * @param data SettingsProcessorData
     */
    public static void registerComponent ( final SettingsProcessorData data )
    {
        // Creating new component settings processor if needed
        final SettingsProcessor settingsProcessor = createSettingsProcessor ( data );
        if ( settingsProcessor != null )
        {
            // Saving current component settings processor
            registerComponent ( data.getComponent (), settingsProcessor );
        }
        else if ( throwExceptions )
        {
            // Throw unsupported component exception
            final String msg = "Component type is not supported: %s";
            throw new SettingsException ( String.format ( msg, data.getComponent ().getClass () ) );
        }
    }

    /**
     * Registers component using the specified SettingsProcessor.
     * Any old SettingsProcessor for that component will be unregistered if operation is successful.
     *
     * @param component         component to register
     * @param settingsProcessor component settings processor
     */
    public static void registerComponent ( final JComponent component, final SettingsProcessor settingsProcessor )
    {
        // Ensure that there is not registered processor for this component
        unregisterComponent ( component );

        // Saving new settings processor for the specified component
        component.putClientProperty ( COMPONENT_SETTINGS_PROCESSOR_KEY, settingsProcessor );
        settingsProcessors.put ( component, new WeakReference<SettingsProcessor> ( settingsProcessor ) );
    }

    /**
     * Loads saved settings into the component if it is registered.
     *
     * @param component component registered for settings auto-save
     */
    public static void loadSettings ( final JComponent component )
    {
        // Retrieving component settings processor
        final WeakReference<SettingsProcessor> reference = settingsProcessors.get ( component );
        final SettingsProcessor settingsProcessor = reference != null ? reference.get () : null;
        if ( settingsProcessor != null )
        {
            // Reloading saved settings into component
            settingsProcessor.load ();
        }
        else if ( throwExceptions )
        {
            // Throw unsupported component exception
            final String msg = "Processor is not registered for component: %s";
            throw new SettingsException ( String.format ( msg, component ) );
        }
    }

    /**
     * Saves all registered components settings.
     */
    public static void saveSettings ()
    {
        for ( final Map.Entry<JComponent, WeakReference<SettingsProcessor>> entry : settingsProcessors.entrySet () )
        {
            saveSettings ( entry.getKey () );
        }
    }

    /**
     * Saves component settings.
     *
     * @param component component registered for settings auto-save
     */
    public static void saveSettings ( final JComponent component )
    {
        // Retrieving component settings processor
        final WeakReference<SettingsProcessor> reference = settingsProcessors.get ( component );
        final SettingsProcessor settingsProcessor = reference != null ? reference.get () : null;
        if ( settingsProcessor != null )
        {
            // Saving component settings
            settingsProcessor.save ( false );
        }
        else if ( throwExceptions )
        {
            // Throw unsupported component exception
            final String msg = "Processor is not registered for component: %s";
            throw new SettingsException ( String.format ( msg, component ) );
        }
    }

    /**
     * Unregisters component from settings auto-save.
     *
     * @param component component to unregister
     */
    public static void unregisterComponent ( final JComponent component )
    {
        // Checking if component processor exists
        final WeakReference<SettingsProcessor> reference = settingsProcessors.get ( component );
        final SettingsProcessor settingsProcessor = reference != null ? reference.get () : null;
        if ( settingsProcessor != null )
        {
            // Unregistering component listeners and actions
            settingsProcessor.destroy ();

            // Removing current component settings processor
            component.putClientProperty ( COMPONENT_SETTINGS_PROCESSOR_KEY, null );
            settingsProcessors.remove ( component );
        }
    }
}