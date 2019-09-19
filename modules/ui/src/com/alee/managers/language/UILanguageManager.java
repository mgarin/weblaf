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

package com.alee.managers.language;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.BiConsumer;
import com.alee.api.jdk.Objects;
import com.alee.extended.collapsible.WebCollapsiblePaneLU;
import com.alee.extended.dock.WebDockableFrameLU;
import com.alee.extended.filechooser.WebFileDropLU;
import com.alee.laf.button.AbstractButtonLU;
import com.alee.laf.desktoppane.JInternalFrameLU;
import com.alee.laf.filechooser.JFileChooserLU;
import com.alee.laf.label.JLabelLU;
import com.alee.laf.progressbar.JProgressBarLU;
import com.alee.laf.rootpane.JRootPaneLU;
import com.alee.laf.tabbedpane.JTabbedPaneLU;
import com.alee.laf.text.JTextComponentLU;
import com.alee.laf.tooltip.SwingToolTipLanguage;
import com.alee.managers.language.data.Dictionary;
import com.alee.managers.tooltip.CustomToolTipLanguage;
import com.alee.utils.ArrayUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.WeakComponentData;
import com.alee.utils.swing.WeakComponentDataList;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.List;
import java.util.*;

/**
 * {@link UILanguageManager} is an extension over {@link LanguageManager} that offers extensive Swing components translation support.
 * {@link UILanguageManager} provides various ways of translating components that are registered within this manager and also dynamically
 * updating their translation upon {@link Language} or {@link Dictionary} changes.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-LanguageManager">How to use LanguageManager</a>
 * @see LanguageManager
 * @see Language
 * @see Dictionary
 * @see LanguageUpdater
 */
public final class UILanguageManager
{
    /**
     * Unknown {@link Locale} icon.
     * todo Move into {@link com.alee.managers.icon.IconManager}
     *
     * @see #getLocaleIcon(Locale)
     */
    private static final ImageIcon UNKNOWN_LANGUAGE = new ImageIcon ( UILanguageManager.class.getResource ( "icons/unknown.png" ) );

    /**
     * {@link Locale} icons.
     *
     * Supported icons can be loaded without any effort as they are included in WebLaF library:
     * - {@link #getLanguageIcon(Language)}
     * - {@link #getLocaleIcon(Locale)}
     *
     * Custom icons can be provided from the code using either of two methods:
     * - {@link #setLanguageIcon(Language, Icon)}
     * - {@link #setLocaleIcon(Locale, Icon)}
     *
     * @see #getLanguageIcon(Language)
     * @see #getLocaleIcon(Locale)
     * @see #setLanguageIcon(Language, Icon)
     * @see #setLocaleIcon(Locale, Icon)
     */
    @NotNull
    private static final Map<String, Icon> localeIcons = new HashMap<String, Icon> ();

    /**
     * Whether or not text-containing components should check passed text for translation or not.
     * This mostly affects only extended components like {@link com.alee.laf.label.WebLabel} or {@link com.alee.laf.button.WebButton}.
     */
    private static boolean checkComponentsTextForTranslations;

    /**
     * Components registered for auto-translation.
     * Specific implementations of LanguageUpdater interface used to translate them.
     *
     * @see #registerComponent(JComponent, String, Object...)
     * @see #updateComponent(JComponent, Object...)
     * @see #updateComponent(JComponent, String, Object...)
     * @see #unregisterComponent(JComponent)
     * @see #isRegisteredComponent(JComponent)
     */
    @NotNull
    private static final WeakComponentData<JComponent, TranslationKey> components =
            new WeakComponentData<JComponent, TranslationKey> ( "WebLanguageManager.TranslationKey", 100 );

    /**
     * Special comparator for sorting LanguageUpdaters list.
     */
    @NotNull
    private static final LanguageUpdaterComparator languageUpdaterComparator = new LanguageUpdaterComparator ();

    /**
     * Registered language updaters.
     * Language updaters are used to automatically update specific components translation when language changes occur.
     *
     * @see LanguageUpdater
     * @see #registerLanguageUpdater(LanguageUpdater)
     * @see #unregisterLanguageUpdater(LanguageUpdater)
     * @see #getLanguageUpdater(JComponent)
     */
    @NotNull
    private static final List<LanguageUpdater> updaters = new ArrayList<LanguageUpdater> ( 20 );

    /**
     * Component-specific language updaters.
     * These are used only for the components they are bound to.
     *
     * @see LanguageUpdater
     * @see #registerLanguageUpdater(JComponent, LanguageUpdater)
     * @see #unregisterLanguageUpdater(JComponent)
     * @see #getLanguageUpdater(JComponent)
     */
    @NotNull
    private static final WeakComponentData<JComponent, LanguageUpdater> customUpdaters =
            new WeakComponentData<JComponent, LanguageUpdater> ( "WebLanguageManager.LanguageUpdater", 2 );

    /**
     * {@link LanguageUpdater}s cached by specific class types.
     * Used to improve {@link LanguageUpdater} retrieval speed for language requests.
     * This cache gets fully updated when any {@link LanguageUpdater} is added or removed.
     *
     * @see LanguageUpdater
     * @see #getLanguageUpdater(JComponent)
     * @see #getLanguageUpdater(Class)
     */
    @NotNull
    private static final Map<Class, LanguageUpdater> updatersCache = new HashMap<Class, LanguageUpdater> ();

    /**
     * {@link LanguageListener}s registered for specified {@link JComponent}s.
     *
     * @see LanguageListener
     * @see #addLanguageListener(JComponent, LanguageListener)
     * @see #removeLanguageListener(JComponent, LanguageListener)
     * @see #removeLanguageListeners(JComponent)
     */
    @NotNull
    private static final WeakComponentDataList<JComponent, LanguageListener> componentLanguageListeners =
            new WeakComponentDataList<JComponent, LanguageListener> ( "WebLanguageManager.LanguageListener", 50 );

    /**
     * {@link DictionaryListener}s registered for specified {@link JComponent}s.
     *
     * @see DictionaryListener
     * @see #addDictionaryListener(JComponent, DictionaryListener)
     * @see #removeDictionaryListener(JComponent, DictionaryListener)
     * @see #removeDictionaryListeners(JComponent)
     */
    @NotNull
    private static final WeakComponentDataList<JComponent, DictionaryListener> componentDictionaryListeners =
            new WeakComponentDataList<JComponent, DictionaryListener> ( "WebLanguageManager.DictionaryListener", 5 );

    /**
     * Manager initialization mark.
     */
    private static boolean initialized = false;

    /**
     * Initializes LanguageManager settings.
     */
    public static synchronized void initialize ()
    {
        if ( !initialized )
        {
            // Initializing LanguageManager if it is not yet initialized
            LanguageManager.initialize ();

            // Initializing default settings
            setCheckComponentsTextForTranslations ( true );

            // Marking initialized
            initialized = true;

            // Basic language updaters
            registerLanguageUpdater ( new ToolTipLU () );
            registerLanguageUpdater ( new JLabelLU () );
            registerLanguageUpdater ( new AbstractButtonLU () );
            registerLanguageUpdater ( new JTextComponentLU () );
            registerLanguageUpdater ( new JTabbedPaneLU () );
            registerLanguageUpdater ( new JProgressBarLU () );
            registerLanguageUpdater ( new JFileChooserLU () );
            registerLanguageUpdater ( new JRootPaneLU () );
            registerLanguageUpdater ( new JInternalFrameLU () );
            registerLanguageUpdater ( new WebFileDropLU () );
            registerLanguageUpdater ( new WebCollapsiblePaneLU () );
            registerLanguageUpdater ( new WebDockableFrameLU () );

            // Special language updates
            registerLanguageUpdater ( new SwingToolTipLanguage () );
            registerLanguageUpdater ( new CustomToolTipLanguage () );

            // Default type of tooltips added from translations
            AbstractToolTipLanguage.setDefaultToolTipType ( CustomToolTipLanguage.TYPE );

            // Language listener for components update
            LanguageManager.addLanguageListener ( new LanguageListener ()
            {
                @Override
                public void languageChanged ( @NotNull final Language oldLanguage, @NotNull final Language newLanguage )
                {
                    // Updating components orientation
                    final ComponentOrientation oo = ComponentOrientation.getOrientation ( oldLanguage.getLocale () );
                    final ComponentOrientation no = ComponentOrientation.getOrientation ( newLanguage.getLocale () );
                    if ( oo.isLeftToRight () != no.isLeftToRight () )
                    {
                        SwingUtils.updateGlobalOrientation ();
                    }

                    // Updating registered components
                    updateComponents ();

                    // Informing listeners tied to components
                    fireLanguageChanged ( oldLanguage, newLanguage );
                }
            } );
            LanguageManager.addDictionaryListener ( new DictionaryListener ()
            {
                @Override
                public void dictionaryAdded ( @NotNull final Dictionary dictionary )
                {
                    // Updating relevant components
                    updateComponents ( dictionary.getKeys () );

                    // Informing listeners tied to components
                    fireDictionaryAdded ( dictionary );
                }

                @Override
                public void dictionaryRemoved ( @NotNull final Dictionary dictionary )
                {
                    // Updating relevant components
                    updateComponents ( dictionary.getKeys () );

                    // Informing listeners tied to components
                    fireDictionaryRemoved ( dictionary );
                }

                @Override
                public void dictionariesCleared ()
                {
                    // Updating registered components
                    updateComponents ();

                    // Informing listeners tied to components
                    fireDictionariesCleared ();
                }
            } );

            // Default WebLaF dictionary
            LanguageManager.addDictionary ( new Dictionary ( UILanguageManager.class, "resources/ui-language.xml" ) );
        }
    }

    /**
     * Throws {@link LanguageException} if manager is not yet initialized.
     *
     * @throws LanguageException if manager is not yet initialized
     */
    private static void mustBeInitialized () throws LanguageException
    {
        if ( !initialized )
        {
            throw new LanguageException ( "WebLanguageManager must be initialized first" );
        }
    }

    /**
     * Returns whether or not text-containing components should check passed text for translation or not.
     *
     * @return {@code true} if text-containing components should check passed text for translation, {@code false} otherwise
     */
    public static boolean isCheckComponentsTextForTranslations ()
    {
        return checkComponentsTextForTranslations;
    }

    /**
     * Sets whether or not text-containing components should check passed text for translation or not.
     *
     * @param check whether or not text-containing components should check passed text for translation or not
     */
    public static void setCheckComponentsTextForTranslations ( final boolean check )
    {
        checkComponentsTextForTranslations = check;
    }

    /**
     * Returns {@link Icon} for the specified {@link Language}.
     * By default there are {@link Icon}s only for languages supported by WebLaF.
     *
     * @param language language to retrieve {@link Icon} for
     * @return {@link Icon} for the specified {@link Language}
     */
    @NotNull
    public static Icon getLanguageIcon ( @NotNull final Language language )
    {
        return getLocaleIcon ( language.getLocale () );
    }

    /**
     * Returns {@link Icon} for the specified {@link Locale}.
     * By default there are {@link Icon}s only for languages supported by WebLaF.
     *
     * @param locale {@link Locale} to retrieve {@link Icon} for
     * @return {@link Icon} for the specified {@link Locale}
     */
    @NotNull
    public static Icon getLocaleIcon ( @NotNull final Locale locale )
    {
        final Icon localeIcon;
        final String key = LanguageUtils.toString ( locale );
        if ( localeIcons.containsKey ( key ) )
        {
            localeIcon = localeIcons.get ( key );
        }
        else
        {
            ImageIcon icon;
            try
            {
                URL res = UILanguageManager.class.getResource ( "icons/" + key + ".png" );
                if ( res == null )
                {
                    res = UILanguageManager.class.getResource ( "icons/" + locale.getLanguage () + ".png" );
                }
                icon = new ImageIcon ( res );
            }
            catch ( final Exception e )
            {
                icon = UNKNOWN_LANGUAGE;
            }
            localeIcons.put ( key, icon );
            localeIcon = icon;
        }
        return localeIcon;
    }

    /**
     * Sets {@link Icon} for the specified {@link Language}.
     *
     * @param language {@link Language} to set {@link Icon} for
     * @param icon     {@link Icon}
     * @return {@link Icon} previously set for this {@link Language}
     */
    @Nullable
    public static Icon setLanguageIcon ( @NotNull final Language language, @Nullable final Icon icon )
    {
        return setLocaleIcon ( language.getLocale (), icon );
    }

    /**
     * Sets {@link Icon} for the specified {@link Locale}.
     *
     * @param locale {@link Locale} to set {@link Icon} for
     * @param icon   {@link Icon}
     * @return {@link Icon} previously set for this {@link Locale}
     */
    @Nullable
    public static Icon setLocaleIcon ( @NotNull final Locale locale, @Nullable final Icon icon )
    {
        final String key = LanguageUtils.toString ( locale );
        return localeIcons.put ( key, icon );
    }

    /**
     * Register custom {@link LanguageUpdater}.
     * Each {@link LanguageUpdater} is tied to a certain component class and can perform language updates only for that component type.
     *
     * @param updater new {@link LanguageUpdater}
     * @see LanguageUpdater
     */
    public static void registerLanguageUpdater ( @NotNull final LanguageUpdater updater )
    {
        synchronized ( updaters )
        {
            // Removing LanguageUpdater for same class if exists
            final Iterator<LanguageUpdater> iterator = updaters.iterator ();
            while ( iterator.hasNext () )
            {
                if ( updater.getComponentClass () == iterator.next ().getComponentClass () )
                {
                    iterator.remove ();
                }
            }

            // Adding LanguageUpdater
            updaters.add ( updater );
            updatersCache.clear ();
        }
    }

    /**
     * Unregister custom {@link LanguageUpdater}.
     *
     * @param updater {@link LanguageUpdater} to unregister
     */
    public static void unregisterLanguageUpdater ( @NotNull final LanguageUpdater updater )
    {
        synchronized ( updaters )
        {
            // Removing LanguageUpdater
            updaters.remove ( updater );
            updatersCache.clear ();
        }
    }

    /**
     * Registers custom {@link LanguageUpdater} for specific {@link JComponent}.
     *
     * @param component {@link JComponent} to register {@link LanguageUpdater} for
     * @param updater   custom {@link LanguageUpdater}
     */
    public static void registerLanguageUpdater ( @NotNull final JComponent component, @NotNull final LanguageUpdater updater )
    {
        customUpdaters.set ( component, updater );
    }

    /**
     * Unregisters {@link JComponent}'s custom {@link LanguageUpdater}.
     *
     * @param component {@link JComponent} to unregister custom {@link LanguageUpdater} from
     */
    public static void unregisterLanguageUpdater ( @NotNull final JComponent component )
    {
        customUpdaters.clear ( component );
    }

    /**
     * Returns {@link LanguageUpdater} currently used for the specified component.
     * This method might return either a custom per-component {@link LanguageUpdater} or global LanguageUpdater.
     * In case {@link LanguageUpdater} cannot be found for the specified component an exception will be thrown.
     *
     * @param component component to retrieve {@link LanguageUpdater} for
     * @return {@link LanguageUpdater} currently used for the specified component
     */
    @NotNull
    public static LanguageUpdater getLanguageUpdater ( @NotNull final JComponent component )
    {
        final LanguageUpdater updater;
        final LanguageUpdater customUpdater = customUpdaters.get ( component );
        if ( customUpdater != null )
        {
            // Found custom updater
            updater = customUpdater;
        }
        else
        {
            synchronized ( updaters )
            {
                final LanguageUpdater cachedUpdater = updatersCache.get ( component.getClass () );
                if ( cachedUpdater != null )
                {
                    // Found cached updater
                    updater = cachedUpdater;
                }
                else
                {
                    // Searching for a suitable component updater if none cached yet
                    final List<LanguageUpdater> foundUpdaters = new ArrayList<LanguageUpdater> ();
                    for ( final LanguageUpdater lu : updaters )
                    {
                        if ( lu.getComponentClass ().isInstance ( component ) )
                        {
                            foundUpdaters.add ( lu );
                        }
                    }

                    // Determining the best updater according to class hierarchy
                    if ( foundUpdaters.size () == 1 )
                    {
                        // Single updater
                        updater = foundUpdaters.get ( 0 );
                    }
                    else if ( foundUpdaters.size () > 1 )
                    {
                        // More than one updater
                        Collections.sort ( foundUpdaters, languageUpdaterComparator );
                        updater = foundUpdaters.get ( 0 );
                    }
                    else
                    {
                        // Throw an exception in case no LanguageUpdater found
                        // Usually this shouldn't happen unless you try to register an unsupported component
                        throw new RuntimeException ( "Unable to find LanguageUpdater for component: " + component );
                    }

                    // Caching resolved updater
                    updatersCache.put ( component.getClass (), updater );
                }
            }
        }
        return updater;
    }

    /**
     * Returns {@link LanguageUpdater} currently used for the specified component class.
     * In case {@link LanguageUpdater} cannot be found for the specified component an exception will be thrown.
     *
     * @param clazz component class to retrieve {@link LanguageUpdater} for
     * @return {@link LanguageUpdater} currently used for the specified component class
     */
    @NotNull
    public static LanguageUpdater getLanguageUpdater ( @NotNull final Class<? extends JComponent> clazz )
    {
        LanguageUpdater updater;
        synchronized ( updaters )
        {
            // Retrieving cached updater
            updater = updatersCache.get ( clazz );

            // Looking for updater if necessary
            if ( updater == null )
            {
                // Searching for a suitable component updater if none cached yet
                final List<LanguageUpdater> foundUpdaters = new ArrayList<LanguageUpdater> ();
                for ( final LanguageUpdater lu : updaters )
                {
                    if ( lu.getComponentClass ().isAssignableFrom ( clazz ) )
                    {
                        foundUpdaters.add ( lu );
                    }
                }

                // Determining the best updater according to class hierarchy
                if ( foundUpdaters.size () == 1 )
                {
                    // Single updater
                    updater = foundUpdaters.get ( 0 );
                }
                else if ( foundUpdaters.size () > 1 )
                {
                    // More than one updater
                    Collections.sort ( foundUpdaters, languageUpdaterComparator );
                    updater = foundUpdaters.get ( 0 );
                }
                else
                {
                    // Throw an exception in case no LanguageUpdater found
                    // Usually this shouldn't happen unless you try to register an unsupported component
                    throw new RuntimeException ( "Unable to find LanguageUpdater for component class: " + clazz );
                }

                // Caching resolved updater
                updatersCache.put ( clazz, updater );
            }
        }
        return updater;
    }

    /**
     * Returns proper initial component text.
     *
     * @param key  text provided into component constructor
     * @param data language data, may not be passed
     * @return proper initial component text
     */
    @Nullable
    public static String getInitialText ( @Nullable final String key, @NotNull final Object... data )
    {
        // Must be initialized
        mustBeInitialized ();

        // Retrieving initial text
        final String text;
        if ( isCheckComponentsTextForTranslations () && LM.contains ( key ) )
        {
            // If record exists we try to find default text
            if ( LM.containsText ( key ) )
            {
                // Default text exists
                text = LM.get ( key, data );
            }
            else
            {
                // There is not default text
                text = null;
            }
        }
        else
        {
            // Language key cannot be recognized
            text = key;
        }
        return text;
    }

    /**
     * Registers proper initial component language key.
     *
     * @param component translated component
     * @param key       text provided into component constructor
     * @param data      language data, may not be passed
     */
    public static void registerInitialLanguage ( @NotNull final LanguageMethods component, @Nullable final String key,
                                                 @NotNull final Object... data )
    {
        // Must be initialized
        mustBeInitialized ();

        // Registering component
        if ( isCheckComponentsTextForTranslations () && key != null && LM.contains ( key ) )
        {
            component.setLanguage ( key, data );
        }
    }

    /**
     * Registers component for language updates.
     * This component language will be automatically updated using existing LanguageUpdater.
     *
     * @param component component to register
     * @param key       component language key
     * @param data      component language data
     * @see LanguageUpdater
     */
    public static void registerComponent ( @NotNull final JComponent component, @NotNull final String key, @Nullable final Object... data )
    {
        // Must be initialized
        mustBeInitialized ();

        // Properly remove previously installed language
        unregisterComponent ( component );

        // Nullifying data if it has no values
        final Object[] actualData = data != null && data.length == 0 ? null : data;

        // Registering component
        components.set ( component, new TranslationKey ( key, actualData ) );

        // Updating component language
        updateComponent ( component );
    }

    /**
     * Unregisters component from language updates.
     *
     * @param component component to unregister
     */
    public static void unregisterComponent ( @NotNull final JComponent component )
    {
        // Must be initialized
        mustBeInitialized ();

        // Unregistering component
        components.clear ( component );
    }

    /**
     * Returns whether component is registered for language updates or not.
     *
     * @param component component to check
     * @return {@code true} if component is registered for language updates, {@code false} otherwise
     */
    public static boolean isRegisteredComponent ( @NotNull final JComponent component )
    {
        // Must be initialized
        mustBeInitialized ();

        // Checking whether or not component is registered
        return components.contains ( component );
    }

    /**
     * Returns language key which was used to register specified component.
     *
     * @param component component to retrieve language key for
     * @return language key which was used to register specified component
     */
    @Nullable
    public static String getComponentKey ( @NotNull final JComponent component )
    {
        // Must be initialized
        mustBeInitialized ();

        // Retrieving registered component key
        final TranslationKey translationKey = components.get ( component );
        return translationKey != null ? translationKey.getKey () : null;
    }

    /**
     * Forces full language update for all registered components.
     */
    public static void updateComponents ()
    {
        // Must be initialized
        mustBeInitialized ();

        // Updating all registered components
        components.forEach ( new BiConsumer<JComponent, TranslationKey> ()
        {
            @Override
            public void accept ( @NotNull final JComponent component, @NotNull final TranslationKey translationKey )
            {
                updateComponent ( component );
            }
        } );
    }

    /**
     * Forces language update for components with the specified keys.
     *
     * @param keys language keys of the components to update
     */
    public static void updateComponents ( @NotNull final Set<String> keys )
    {
        // Must be initialized
        mustBeInitialized ();

        // Updating components registered for provided keys
        components.forEach ( new BiConsumer<JComponent, TranslationKey> ()
        {
            @Override
            public void accept ( @NotNull final JComponent component, @NotNull final TranslationKey translationKey )
            {
                if ( keys.contains ( translationKey.getKey () ) )
                {
                    updateComponent ( component );
                }
            }
        } );
    }

    /**
     * Forces {@link JComponent} language update.
     *
     * @param component {@link JComponent} to update
     * @param data      new formatting data
     */
    public static void updateComponent ( @NotNull final JComponent component, @Nullable final Object... data )
    {
        // Must be initialized
        mustBeInitialized ();

        // Retrieving tranlation settings
        final TranslationKey translationKey = components.get ( component );
        if ( translationKey != null )
        {
            // Retrieving actual data for update
            final Object[] actualData = getActualData ( component, translationKey.getKey (), data );

            // Updating component translation data
            translationKey.setData ( actualData );

            // Updating component language
            final LanguageUpdater updater = getLanguageUpdater ( component );
            updater.update ( component, LM.getLanguage (), translationKey.getKey (), translationKey.getData () );
        }
        else
        {
            throw new LanguageException ( "Component is not registered yet: " + component );
        }
    }

    /**
     * Forces {@link JComponent} language update.
     *
     * @param component {@link JComponent} to update
     * @param key       new language key
     * @param data      new formatting data
     */
    public static void updateComponent ( @NotNull final JComponent component, @NotNull final String key, @Nullable final Object... data )
    {
        // Must be initialized
        mustBeInitialized ();

        // Checking that component is registered
        if ( isRegisteredComponent ( component ) )
        {
            // Retrieving actual data for update
            final Object[] actualData = getActualData ( component, key, data );

            // Updating component translation settings
            final TranslationKey translationKey = new TranslationKey ( key, actualData );
            components.set ( component, translationKey );

            // Updating component language
            final LanguageUpdater updater = getLanguageUpdater ( component );
            updater.update ( component, LM.getLanguage (), translationKey.getKey (), translationKey.getData () );
        }
        else
        {
            // Premature update call
            throw new LanguageException ( "Component is not registered yet: " + component );
        }
    }

    /**
     * Returns actual {@link JComponent} translation data based on its translation settings.
     *
     * @param component {@link JComponent} to get actual data for
     * @param key       translation key
     * @param data      translation data
     * @return actual {@link JComponent} translation data based on its translation settings
     */
    @Nullable
    private static Object[] getActualData ( @NotNull final JComponent component, @NotNull final String key, @Nullable final Object[] data )
    {
        final Object[] actualData;
        final TranslationKey oldKey = components.get ( component );
        if ( oldKey != null )
        {
            if ( Objects.equals ( oldKey.getKey (), key ) )
            {
                /**
                 * When keys are identical we want to check passed data.
                 */
                if ( ArrayUtils.notEmpty ( data ) )
                {
                    /**
                     * Non-empty data is automatically used.
                     */
                    actualData = data;
                }
                else
                {
                    /**
                     * New data is empty, we will be using old data.
                     * This is important to avoid issues with the vararg method usage and for general convenience.
                     */
                    actualData = oldKey.getData ();
                }
            }
            else
            {
                /**
                 * New key will always accept new data, even if it's empty.
                 */
                actualData = data;
            }
        }
        else
        {
            throw new LanguageException ( "Component is not registered yet: " + component );
        }
        return actualData;
    }

    /**
     * Adds new {@link LanguageListener} tied to the specified {@link JComponent}.
     * Unlike {@link LanguageManager#addLanguageListener(LanguageListener)} using this method will not store hard references
     * to the {@link LanguageListener} outside of the {@link JComponent} to avoid any mermory leaks. So if specified {@link JComponent}
     * will for instance be destroyed provided {@link LanguageListener} will also be destroyed.
     *
     * @param component {@link JComponent} to tie {@link LanguageListener} to
     * @param listener  {@link LanguageListener} to add
     */
    public static void addLanguageListener ( @NotNull final JComponent component, @NotNull final LanguageListener listener )
    {
        componentLanguageListeners.add ( component, listener );
    }

    /**
     * Removes {@link LanguageListener} tied to the specified {@link JComponent}.
     *
     * @param component {@link JComponent} to remove tied {@link LanguageListener} from
     * @param listener  {@link LanguageListener} to remove
     */
    public static void removeLanguageListener ( @NotNull final JComponent component, @NotNull final LanguageListener listener )
    {
        componentLanguageListeners.remove ( component, listener );
    }

    /**
     * Removes all {@link LanguageListener}s tied to the specified {@link JComponent}.
     *
     * @param component {@link JComponent} to remove all tied {@link LanguageListener}s from
     */
    public static void removeLanguageListeners ( @NotNull final JComponent component )
    {
        componentLanguageListeners.clear ( component );
    }

    /**
     * Fires language changed event whenever current {@link Language} changes.
     *
     * @param oldLanguage old {@link Language}
     * @param newLanguage new {@link Language}
     */
    private static void fireLanguageChanged ( @NotNull final Language oldLanguage, @NotNull final Language newLanguage )
    {
        componentLanguageListeners.forEachData ( new BiConsumer<JComponent, LanguageListener> ()
        {
            @Override
            public void accept ( @NotNull final JComponent component, @NotNull final LanguageListener languageListener )
            {
                languageListener.languageChanged ( oldLanguage, newLanguage );
            }
        } );
    }

    /**
     * Adds new {@link DictionaryListener} tied to the specified {@link JComponent}.
     *
     * Unlike {@link LanguageManager#addDictionaryListener(DictionaryListener)} using this method will not store hard references
     * to the {@link DictionaryListener} outside of the {@link JComponent} to avoid any mermory leaks. So if specified {@link JComponent}
     * will for instance be destroyed provided {@link DictionaryListener} will also be destroyed.
     *
     * @param component {@link JComponent} to tie {@link DictionaryListener} to
     * @param listener  {@link DictionaryListener} to add
     */
    public static void addDictionaryListener ( @NotNull final JComponent component, @NotNull final DictionaryListener listener )
    {
        componentDictionaryListeners.add ( component, listener );
    }

    /**
     * Removes {@link DictionaryListener} tied to the specified {@link JComponent}.
     *
     * @param component {@link JComponent} to remove tied {@link DictionaryListener} from
     * @param listener  {@link DictionaryListener} to remove
     */
    public static void removeDictionaryListener ( @NotNull final JComponent component, @NotNull final DictionaryListener listener )
    {
        componentDictionaryListeners.remove ( component, listener );
    }

    /**
     * Removes all {@link DictionaryListener}s tied to the specified {@link JComponent}.
     *
     * @param component {@link JComponent} to remove all tied {@link DictionaryListener}s from
     */
    public static void removeDictionaryListeners ( @NotNull final JComponent component )
    {
        componentDictionaryListeners.clear ( component );
    }

    /**
     * Fires {@link Dictionary} added event whenever new {@link Dictionary} is added.
     *
     * @param dictionary new {@link Dictionary}
     */
    private static void fireDictionaryAdded ( @NotNull final Dictionary dictionary )
    {
        componentDictionaryListeners.forEachData ( new BiConsumer<JComponent, DictionaryListener> ()
        {
            @Override
            public void accept ( @NotNull final JComponent component, @NotNull final DictionaryListener languageListener )
            {
                languageListener.dictionaryAdded ( dictionary );
            }
        } );
    }

    /**
     * Fires {@link Dictionary} removed event whenever {@link Dictionary} is removed.
     *
     * @param dictionary removed {@link Dictionary}
     */
    private static void fireDictionaryRemoved ( @NotNull final Dictionary dictionary )
    {
        componentDictionaryListeners.forEachData ( new BiConsumer<JComponent, DictionaryListener> ()
        {
            @Override
            public void accept ( @NotNull final JComponent component, @NotNull final DictionaryListener languageListener )
            {
                languageListener.dictionaryRemoved ( dictionary );
            }
        } );
    }

    /**
     * Fires {@link Dictionary}s cleared event whenever all {@link Dictionary}s are removed.
     */
    private static void fireDictionariesCleared ()
    {
        componentDictionaryListeners.forEachData ( new BiConsumer<JComponent, DictionaryListener> ()
        {
            @Override
            public void accept ( @NotNull final JComponent component, @NotNull final DictionaryListener languageListener )
            {
                languageListener.dictionariesCleared ();
            }
        } );
    }
}