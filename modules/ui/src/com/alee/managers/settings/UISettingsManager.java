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
import com.alee.extended.colorchooser.*;
import com.alee.extended.date.DateFieldSettingsProcessor;
import com.alee.extended.date.DateFieldState;
import com.alee.extended.date.WebDateField;
import com.alee.extended.dock.DockablePaneSettingsProcessor;
import com.alee.extended.dock.DockablePaneState;
import com.alee.extended.dock.WebDockablePane;
import com.alee.extended.dock.data.AbstractDockableElement;
import com.alee.extended.dock.data.DockableContentElement;
import com.alee.extended.dock.data.DockableFrameElement;
import com.alee.extended.dock.data.DockableListContainer;
import com.alee.extended.panel.*;
import com.alee.extended.split.MultiSplitPaneSettingsProcessor;
import com.alee.extended.split.MultiSplitState;
import com.alee.extended.split.WebMultiSplitPane;
import com.alee.extended.tab.DocumentPaneSettingsProcessor;
import com.alee.extended.tab.DocumentPaneState;
import com.alee.extended.tab.WebDocumentPane;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.ButtonSettingsProcessor;
import com.alee.laf.button.ButtonState;
import com.alee.laf.colorchooser.HSBColor;
import com.alee.laf.combobox.ComboBoxSettingsProcessor;
import com.alee.laf.combobox.ComboBoxState;
import com.alee.laf.rootpane.RootPaneSettingsProcessor;
import com.alee.laf.rootpane.WindowState;
import com.alee.laf.scroll.ScrollBarSettingsProcessor;
import com.alee.laf.scroll.ScrollBarState;
import com.alee.laf.scroll.ScrollPaneSettingsProcessor;
import com.alee.laf.scroll.ScrollPaneState;
import com.alee.laf.slider.SliderSettingsProcessor;
import com.alee.laf.slider.SliderState;
import com.alee.laf.splitpane.SplitPaneSettingsProcessor;
import com.alee.laf.splitpane.SplitPaneState;
import com.alee.laf.tabbedpane.TabbedPaneSettingsProcessor;
import com.alee.laf.tabbedpane.TabbedPaneState;
import com.alee.laf.text.*;
import com.alee.laf.tree.NodeState;
import com.alee.laf.tree.TreeSettingsProcessor;
import com.alee.laf.tree.TreeState;
import com.alee.laf.tree.WebTree;
import com.alee.utils.ReflectUtils;
import com.alee.utils.XmlUtils;
import com.alee.utils.swing.WeakComponentData;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * {@link SettingsManager} extension that allows registering and processing {@link JComponent} settings.
 * It can save and restore settings upon any changes within or outside of the registered {@link JComponent}s.
 * Each registered {@link JComponent} settings are managed by a {@link SettingsProcessor} instance which is assigned to it.
 * {@link SettingsProcessor} instances are created for each registered {@link JComponent} and handle loading and saving settings.
 * Which {@link SettingsProcessor} implementation is used for registered {@link JComponent} depends on this manager settings.
 * You can also explicitly register a {@link SettingsProcessor} for any {@link JComponent} as long as it supports that {@link JComponent}.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see SettingsManager
 * @see Configuration
 * @see SettingsProcessor
 * @see SettingsMethods
 */
public final class UISettingsManager
{
    /**
     * todo 1. Add component-bound listeners (current ones may cause memory leaks)
     * todo 2. JListSettingsProcessor
     * todo 3. JTableSettingsProcessorw
     * todo 4. JScrollPaneSettingsProcessor
     */

    /**
     * Registered settings processor classes.
     */
    private static final Map<Class<? extends JComponent>, Class<? extends SettingsProcessor>> settingsProcessorClasses =
            new LinkedHashMap<Class<? extends JComponent>, Class<? extends SettingsProcessor>> ();

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
     * Initializes {@link UISettingsManager}.
     */
    public static synchronized void initialize ()
    {
        if ( !initialized )
        {
            initialized = true;

            // Ensure core manager is initialized
            SettingsManager.initialize ();

            // Initializing data aliases
            XmlUtils.processAnnotations ( ButtonState.class );
            XmlUtils.processAnnotations ( TextComponentState.class );
            XmlUtils.processAnnotations ( PasswordFieldState.class );
            XmlUtils.processAnnotations ( DateFieldState.class );
            XmlUtils.processAnnotations ( ComboBoxState.class );
            XmlUtils.processAnnotations ( SliderState.class );
            XmlUtils.processAnnotations ( SplitPaneState.class );
            XmlUtils.processAnnotations ( ScrollBarState.class );
            XmlUtils.processAnnotations ( ScrollPaneState.class );
            XmlUtils.processAnnotations ( WindowState.class );
            XmlUtils.processAnnotations ( CollapsiblePaneState.class );
            XmlUtils.processAnnotations ( AccordionState.class );
            XmlUtils.processAnnotations ( MultiSplitState.class );
            XmlUtils.processAnnotations ( TabbedPaneState.class );
            XmlUtils.processAnnotations ( DocumentPaneState.class );
            XmlUtils.processAnnotations ( TreeState.class );
            XmlUtils.processAnnotations ( NodeState.class );
            XmlUtils.processAnnotations ( GradientColorChooserState.class );
            XmlUtils.processAnnotations ( GradientData.class );
            XmlUtils.processAnnotations ( GradientColorData.class );
            XmlUtils.processAnnotations ( HSBColor.class );
            XmlUtils.processAnnotations ( DockablePaneState.class );
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
            registerSettingsProcessor ( JScrollPane.class, ScrollPaneSettingsProcessor.class );
            registerSettingsProcessor ( JSplitPane.class, SplitPaneSettingsProcessor.class );
            registerSettingsProcessor ( JTabbedPane.class, TabbedPaneSettingsProcessor.class );
            registerSettingsProcessor ( JRootPane.class, RootPaneSettingsProcessor.class );

            // Register additional component settings processors
            registerSettingsProcessor ( WebMultiSplitPane.class, MultiSplitPaneSettingsProcessor.class );
            registerSettingsProcessor ( WebDocumentPane.class, DocumentPaneSettingsProcessor.class );
            registerSettingsProcessor ( WebTree.class, TreeSettingsProcessor.class );
            registerSettingsProcessor ( WebDateField.class, DateFieldSettingsProcessor.class );
            registerSettingsProcessor ( WebCollapsiblePane.class, CollapsiblePaneSettingsProcessor.class );
            registerSettingsProcessor ( WebAccordion.class, AccordionSettingsProcessor.class );
            registerSettingsProcessor ( WebGradientColorChooser.class, GradientColorChooserSettingsProcessor.class );
            registerSettingsProcessor ( WebDockablePane.class, DockablePaneSettingsProcessor.class );

            // Settings listener that applies settings changes to components
            // It is added here instead of being added from processor to avoid memory leaks
            SettingsManager.addSettingsListener ( new SettingsListener ()
            {
                @Override
                public void settingsChanged ( final String group, final String key, final Object oldValue, final Object newValue )
                {
                    settingsProcessors.forEach ( new BiConsumer<JComponent, SettingsProcessor> ()
                    {
                        @Override
                        public void accept ( final JComponent component, final SettingsProcessor processor )
                        {
                            final Configuration configuration = processor.configuration ();
                            if ( configuration.isApplySettingsChanges () &&
                                    group.equals ( configuration.group () ) && key.equals ( configuration.key () ) )
                            {
                                processor.load ();
                            }
                        }
                    } );
                }
            } );
        }
    }

    /**
     * Returns whether the specified {@link JComponent} is supported or not.
     *
     * @param component component
     * @return {@code true} if the specified {@link JComponent} is supported, {@code false} otherwise
     */
    public static boolean isComponentSupported ( final JComponent component )
    {
        return isComponentSupported ( component.getClass () );
    }

    /**
     * Returns whether the specified {@link JComponent} type is supported or not.
     *
     * @param componentType component type
     * @return {@code true} if the specified {@link JComponent} type is supported, {@code false} otherwise
     */
    public static boolean isComponentSupported ( final Class<? extends JComponent> componentType )
    {
        return findSuitableSettingsProcessorClass ( componentType ) != null;
    }

    /**
     * Registers specified {@link SettingsProcessor} class for the specified {@link JComponent} type.
     *
     * @param componentType     {@link JComponent} type
     * @param settingsProcessor {@link SettingsProcessor} class
     * @return {@link SettingsProcessor} class previously registered for the specified {@link JComponent} type
     */
    public static Class<? extends SettingsProcessor> registerSettingsProcessor ( final Class<? extends JComponent> componentType,
                                                                                 final Class<? extends SettingsProcessor> settingsProcessor )
    {
        return settingsProcessorClasses.put ( componentType, settingsProcessor );
    }

    /**
     * Registers {@link JComponent} for settings auto-save.
     *
     * Registered {@link JComponent} will be:
     * - tracked for settings changes (depends on {@link SettingsProcessor}) to save them when requested
     * - automatically updated with any loaded settings for that key if requested
     * - automatically updated with any changes made in its settings if requested
     *
     * @param component {@link JComponent} to register for settings auto-save
     * @param data      {@link Configuration} for {@link SettingsProcessor}
     */
    public static void registerComponent ( final JComponent component, final Configuration data )
    {
        registerComponent ( component, createSettingsProcessor ( component, data ) );
    }

    /**
     * Registers {@link JComponent} for settings auto-save.
     * {@link JComponent} is required here simply to check that registered component is the same as the one in {@link SettingsProcessor}.
     *
     * Registered {@link JComponent} will be:
     * - tracked for settings changes (depends on {@link SettingsProcessor}) to save them when requested
     * - automatically updated with any loaded settings for that key if requested
     * - automatically updated with any changes made in its settings if requested
     *
     * @param component {@link JComponent} to register for settings auto-save
     * @param processor {@link SettingsProcessor}
     */
    public static void registerComponent ( final JComponent component, final SettingsProcessor processor )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Ensure that component within processor is the same one
        if ( component != processor.component () )
        {
            throw new SettingsException ( "SettingsProcessor cannot be referencing different component" );
        }

        // Saving component settings processor
        settingsProcessors.set ( processor.component (), processor, new BiConsumer<JComponent, SettingsProcessor> ()
        {
            @Override
            public void accept ( final JComponent component, final SettingsProcessor settingsProcessor )
            {
                settingsProcessor.destroy ();
            }
        } );
    }

    /**
     * Unregisters {@link JComponent} from settings auto-save.
     *
     * @param component component to unregister
     */
    public static void unregisterComponent ( final JComponent component )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Removing component settings processor
        settingsProcessors.clear ( component, new BiConsumer<JComponent, SettingsProcessor> ()
        {
            @Override
            public void accept ( final JComponent component, final SettingsProcessor settingsProcessor )
            {
                settingsProcessor.destroy ();
            }
        } );
    }

    /**
     * Loads previously saved settings for the specified {@link JComponent} if it is registered.
     *
     * @param component {@link JComponent} to load settings for
     */
    public static void loadSettings ( final JComponent component )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Loading settings if possible
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
     * Saves settings for the specified {@link JComponent} if it is registered.
     *
     * @param component {@link JComponent} to save settings for
     */
    public static void saveSettings ( final JComponent component )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Saving settings
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
     * Saves all registered {@link JComponent}s settings.
     */
    public static void saveSettings ()
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

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
     * Returns {@link SettingsProcessor} class for the specified {@link JComponent} type.
     *
     * @param componentType {@link JComponent} type
     * @return {@link SettingsProcessor} class for the specified {@link JComponent} type
     */
    private static Class<? extends SettingsProcessor> findSuitableSettingsProcessorClass ( final Class<? extends JComponent> componentType )
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
     * Returns new {@link SettingsProcessor} instance for the specified {@link JComponent} and {@link Configuration}.
     *
     * @param component     {@link JComponent} to create {@link SettingsProcessor} for
     * @param configuration {@link Configuration} for {@link SettingsProcessor}
     * @return new {@link SettingsProcessor} instance for the specified {@link JComponent} and {@link Configuration}
     */
    private static SettingsProcessor createSettingsProcessor ( final JComponent component, final Configuration configuration )
    {
        final Class settingsProcessorClass = findSuitableSettingsProcessorClass ( component.getClass () );
        if ( settingsProcessorClass != null )
        {
            try
            {
                // Creating new settings processor from registered class
                return ReflectUtils.createInstance ( settingsProcessorClass, component, configuration );
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
            throw new SettingsException ( String.format ( msg, configuration ) );
        }
    }
}