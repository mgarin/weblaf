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

import com.alee.api.jdk.BiConsumer;
import com.alee.extended.colorchooser.GradientColorData;
import com.alee.extended.colorchooser.GradientData;
import com.alee.extended.colorchooser.WebGradientColorChooser;
import com.alee.extended.date.WebDateField;
import com.alee.extended.dock.WebDockablePane;
import com.alee.extended.dock.data.AbstractDockableElement;
import com.alee.extended.dock.data.DockableContentElement;
import com.alee.extended.dock.data.DockableFrameElement;
import com.alee.extended.dock.data.DockableListContainer;
import com.alee.extended.panel.WebAccordion;
import com.alee.extended.panel.WebCollapsiblePane;
import com.alee.extended.tab.DocumentPaneState;
import com.alee.extended.tab.WebDocumentPane;
import com.alee.laf.colorchooser.HSBColor;
import com.alee.laf.tree.NodeState;
import com.alee.laf.tree.TreeState;
import com.alee.laf.tree.WebTree;
import com.alee.managers.settings.processors.*;
import com.alee.utils.ReflectUtils;
import com.alee.utils.XmlUtils;
import com.alee.utils.swing.WeakComponentData;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * {@link SettingsManager} extension that allows registering and processing component settings.
 * It can save and restore settings upon any changes within or outside of the registered components.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see SettingsManager
 * @see SettingsProcessor
 */

public final class UISettingsManager
{
    /**
     * todo 1. Add component-bound listeners
     * todo 2. JListSettingsProcessor
     * todo 3. JTableSettingsProcessorw
     * todo 4. JScrollPaneSettingsProcessor
     */

    /**
     * Registered settings processor classes.
     */
    private static final Map<Class, Class> settingsProcessorClasses = new LinkedHashMap<Class, Class> ();

    /**
     * Registered component settings processors.
     */
    private static final WeakComponentData<JComponent, SettingsProcessor> settingsProcessors =
            new WeakComponentData<JComponent, SettingsProcessor> ( "UISettingsManager.SettingsProcessor", 10 );

    /**
     * Whether SettingsManager is initialized or not.
     */
    private static boolean initialized = false;

    /**
     * Initializes SettingsManager.
     */
    public static synchronized void initialize ()
    {
        if ( !initialized )
        {
            initialized = true;

            // Ensure core manager is initialized
            SettingsManager.initialize ();

            // Initializing data aliases
            XmlUtils.processAnnotations ( WindowSettings.class );
            XmlUtils.processAnnotations ( DocumentPaneState.class );
            XmlUtils.processAnnotations ( TreeState.class );
            XmlUtils.processAnnotations ( NodeState.class );
            XmlUtils.processAnnotations ( GradientData.class );
            XmlUtils.processAnnotations ( GradientColorData.class );
            XmlUtils.processAnnotations ( HSBColor.class );
            XmlUtils.processAnnotations ( AbstractDockableElement.class );
            XmlUtils.processAnnotations ( DockableContentElement.class );
            XmlUtils.processAnnotations ( DockableFrameElement.class );
            XmlUtils.processAnnotations ( DockableListContainer.class );

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

            // Register additional component settings processors
            registerSettingsProcessor ( WebDocumentPane.class, DocumentPaneSettingsProcessor.class );
            registerSettingsProcessor ( WebTree.class, TreeSettingsProcessor.class );
            registerSettingsProcessor ( WebDateField.class, DateFieldSettingsProcessor.class );
            registerSettingsProcessor ( WebCollapsiblePane.class, CollapsiblePaneSettingsProcessor.class );
            registerSettingsProcessor ( WebAccordion.class, AccordionSettingsProcessor.class );
            registerSettingsProcessor ( WebGradientColorChooser.class, GradientColorChooserSettingsProcessor.class );
            registerSettingsProcessor ( WebDockablePane.class, DockablePaneSettingsProcessor.class );
        }
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
    private static Class findSuitableSettingsProcessor ( final Class componentType )
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
    private static SettingsProcessor createSettingsProcessor ( final SettingsProcessorData data )
    {
        final Class settingsProcessorClass = findSuitableSettingsProcessor ( data.getComponent ().getClass () );
        if ( settingsProcessorClass != null )
        {
            try
            {
                // Creating new settings processor from registered class
                return ReflectUtils.createInstance ( settingsProcessorClass, data );
            }
            catch ( final Exception e )
            {
                // Throw processor instantiation exception
                final String msg = "Unable to instantiate SettingsProcessor class: %s";
                throw new SettingsException ( String.format ( msg, settingsProcessorClass ), e );
            }
        }
        else
        {
            // Throw missing processor mapping exception
            final String msg = "Unable to find SettingsProcessor for data: %s";
            throw new SettingsException ( String.format ( msg, data ) );
        }
    }

    /**
     * Registers component for settings auto-save.
     *
     * Also registered component will be:
     * - listened for settings changes to save them when requested
     * - automatically updated with any loaded settings for that key if requested
     * - automatically updated with any changes made in its settings if requested
     *
     * @param component component to register
     * @param key       component settings key
     */
    public static void registerComponent ( final JComponent component, final String key )
    {
        registerComponent ( component, key, ( Object ) null );
    }

    /**
     * Registers component for settings auto-save.
     *
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
    public static <T extends DefaultValue> void registerComponent ( final JComponent component, final String key,
                                                                    final Class<T> defaultValueClass )
    {
        registerComponent ( component, key, SettingsManager.getDefaultValue ( defaultValueClass ) );
    }

    /**
     * Registers component for settings auto-save.
     *
     * Also registered component will be:
     * - listened for settings changes to save them when requested
     * - automatically updated with any loaded settings for that key if requested
     * - automatically updated with any changes made in its settings if requested
     *
     * @param component    component to register
     * @param key          component settings key
     * @param defaultValue component default value
     */
    public static void registerComponent ( final JComponent component, final String key, final Object defaultValue )
    {
        registerComponent ( component, SettingsManager.getDefaultSettingsGroup (), key, defaultValue );
    }

    /**
     * Registers component for settings auto-save.
     *
     * Also registered component will be:
     * - listened for settings changes to save them when requested
     * - automatically updated with any loaded settings for that key if requested
     * - automatically updated with any changes made in its settings if requested
     *
     * @param component component to register
     * @param group     component settings group
     * @param key       component settings key
     */
    public static void registerComponent ( final JComponent component, final String group, final String key )
    {
        registerComponent ( component, group, key, ( Object ) null );
    }

    /**
     * Registers component for settings auto-save.
     *
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
    public static <T extends DefaultValue> void registerComponent ( final JComponent component, final String group, final String key,
                                                                    final Class<T> defaultValueClass )
    {
        registerComponent ( component, group, key, SettingsManager.getDefaultValue ( defaultValueClass ) );
    }

    /**
     * Registers component for settings auto-save.
     *
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
    public static void registerComponent ( final JComponent component, final String group, final String key, final Object defaultValue )
    {
        registerComponent ( component, group, key, defaultValue, true, true );
    }

    /**
     * Registers component for settings auto-save.
     *
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
    public static void registerComponent ( final JComponent component, final String key, final boolean loadInitialSettings,
                                           final boolean applySettingsChanges )
    {
        registerComponent ( component, key, null, loadInitialSettings, applySettingsChanges );
    }

    /**
     * Registers component for settings auto-save.
     *
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
    public static <T extends DefaultValue> void registerComponent ( final JComponent component, final String key,
                                                                    final Class<T> defaultValueClass, final boolean loadInitialSettings,
                                                                    final boolean applySettingsChanges )
    {
        registerComponent ( component, key, SettingsManager.getDefaultValue ( defaultValueClass ), loadInitialSettings,
                applySettingsChanges );
    }

    /**
     * Registers component for settings auto-save.
     *
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
    public static void registerComponent ( final JComponent component, final String key, final Object defaultValue,
                                           final boolean loadInitialSettings, final boolean applySettingsChanges )
    {
        registerComponent ( component, SettingsManager.getDefaultSettingsGroup (), key, defaultValue, loadInitialSettings,
                applySettingsChanges );
    }

    /**
     * Registers component for settings auto-save.
     *
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
    public static <T extends DefaultValue> void registerComponent ( final JComponent component, final String group, final String key,
                                                                    final Class<T> defaultValueClass, final boolean loadInitialSettings,
                                                                    final boolean applySettingsChanges )
    {
        registerComponent ( component, group, key, SettingsManager.getDefaultValue ( defaultValueClass ), loadInitialSettings,
                applySettingsChanges );
    }

    /**
     * Registers component for settings auto-save.
     *
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
    public static void registerComponent ( final JComponent component, final String group, final String key, final Object defaultValue,
                                           final boolean loadInitialSettings, final boolean applySettingsChanges )
    {
        registerComponent (
                new SettingsProcessorData ( component, group, key, defaultValue, loadInitialSettings, applySettingsChanges ) );
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

        // Saving current component settings processor
        registerComponent ( data.getComponent (), settingsProcessor );
    }

    /**
     * Registers component using the specified {@link SettingsProcessor}.
     * Any old SettingsProcessor for that component will be unregistered if operation is successful.
     *
     * @param component         {@link JComponent} to register {@link SettingsProcessor} for
     * @param settingsProcessor {@link SettingsProcessor}
     */
    public static void registerComponent ( final JComponent component, final SettingsProcessor settingsProcessor )
    {
        settingsProcessors.set ( component, settingsProcessor, new BiConsumer<JComponent, SettingsProcessor> ()
        {
            @Override
            public void accept ( final JComponent component, final SettingsProcessor settingsProcessor )
            {
                settingsProcessor.destroy ();
            }
        } );
    }

    /**
     * Loads saved settings into the specified {@link JComponent} if it is registered.
     *
     * @param component component registered for settings auto-save
     */
    public static void loadSettings ( final JComponent component )
    {
        if ( settingsProcessors.contains ( component ) )
        {
            settingsProcessors.get ( component ).load ();
        }
        else
        {
            final String msg = "Processor is not registered for component: %s";
            throw new SettingsException ( String.format ( msg, component ) );
        }
    }

    /**
     * Saves all registered components settings.
     */
    public static void saveSettings ()
    {
        // Saving plain settings
        SettingsManager.saveSettings ();

        // Saving component settings
        settingsProcessors.forEach ( new BiConsumer<JComponent, SettingsProcessor> ()
        {
            @Override
            public void accept ( final JComponent component, final SettingsProcessor settingsProcessor )
            {
                saveSettings ( component );
            }
        } );
    }

    /**
     * Saves component settings.
     *
     * @param component component registered for settings auto-save
     */
    public static void saveSettings ( final JComponent component )
    {
        if ( settingsProcessors.contains ( component ) )
        {
            settingsProcessors.get ( component ).save ( false );
        }
        else
        {
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
        settingsProcessors.clear ( component, new BiConsumer<JComponent, SettingsProcessor> ()
        {
            @Override
            public void accept ( final JComponent component, final SettingsProcessor settingsProcessor )
            {
                settingsProcessor.destroy ();
            }
        } );
    }
}