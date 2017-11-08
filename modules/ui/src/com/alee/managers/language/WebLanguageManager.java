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

import com.alee.managers.language.data.Dictionary;
import com.alee.managers.language.updaters.*;
import com.alee.utils.ArrayUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * {@link WebLanguageManager} is an extension over {@link LanguageManager} that offers extensive Swing components translation support.
 * {@link WebLanguageManager} provides various ways of translating components that are registered within this manager and also dynamically
 * updating their translation upon {@link Language} or {@link Dictionary} changes.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-LanguageManager">How to use LanguageManager</a>
 * @see LanguageManager
 * @see Language
 * @see Dictionary
 */

public final class WebLanguageManager
{
    /**
     * Key used to store custom {@link LanguageUpdater} within {@link JComponent}.
     */
    public static final String COMPONENT_UPDATER_KEY = "language.updater";

    /**
     * Key used to store custom {@link LanguageListener}s within {@link JComponent}.
     */
    public static final String COMPONENT_LANGUAGE_LISTENERS_KEY = "language.listener";

    /**
     * Key used to store custom {@link DictionaryListener}s within {@link JComponent}.
     */
    public static final String COMPONENT_DICTIONARY_LISTENERS_KEY = "dictionary.listener";

    /**
     * Unknown language icon.
     *
     * @see #getLocaleIcon(Locale)
     */
    private static final ImageIcon UNKNOWN_LANGUAGE = new ImageIcon ( LanguageManager.class.getResource ( "icons/unknown.png" ) );

    /**
     * Language icons.
     * Supported language icons can be loaded without any effort as they are included in WebLaF library.
     * Custom language icons can be provided from the code using setLanguageIcon(String, ImageIcon) method.
     *
     * @see #getLanguageIcon(Language)
     * @see #getLocaleIcon(Locale)
     * @see #setLanguageIcon(Language, Icon)
     * @see #setLocaleIcon(Locale, Icon)
     */
    private static final Map<String, Icon> languageIcons = new HashMap<String, Icon> ();

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
    private static final Map<JComponent, String> components = new WeakHashMap<JComponent, String> ();

    /**
     * Object data provided with component language key.
     * It is used to format the final translation string.
     *
     * @see #registerComponent(JComponent, String, Object...)
     * @see #unregisterComponent(JComponent)
     * @see #updateComponent(JComponent, Object...)
     * @see #updateComponent(JComponent, String, Object...)
     */
    private static final Map<JComponent, Object[]> componentsData = new WeakHashMap<JComponent, Object[]> ();

    /**
     * Special comparator for sorting LanguageUpdaters list.
     */
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
    private static final Map<JComponent, WeakReference<LanguageUpdater>> customUpdaters =
            new WeakHashMap<JComponent, WeakReference<LanguageUpdater>> ();

    /**
     * Language updaters cache by specific class types.
     * Used to improve LanguageUpdater retrieval speed for language requests.
     * This cache gets fully updated when any language updater is added or removed.
     */
    private static final Map<Class, LanguageUpdater> updatersCache = new HashMap<Class, LanguageUpdater> ();

    /**
     * {@link Map} of {@link JComponent} containing {@link LanguageListener}s tied to them.
     * {@link Boolean} is simply stored to indicate component
     *
     * @see LanguageListener
     * @see #addLanguageListener(JComponent, LanguageListener)
     * @see #removeLanguageListener(JComponent, LanguageListener)
     * @see #removeLanguageListeners(JComponent)
     */
    private static final Map<JComponent, Boolean> componentLanguageListeners = new WeakHashMap<JComponent, Boolean> ( 3 );

    /**
     * {@link Map} of {@link JComponent} containing {@link LanguageListener}s tied to them.
     *
     * @see LanguageListener
     * @see #addLanguageListener(JComponent, LanguageListener)
     * @see #removeLanguageListener(JComponent, LanguageListener)
     * @see #removeLanguageListeners(JComponent)
     */
    private static final Map<JComponent, Boolean> componentDictionaryListeners = new WeakHashMap<JComponent, Boolean> ( 3 );

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
                public void languageChanged ( final Language oldLanguage, final Language newLanguage )
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
                public void dictionaryAdded ( final Dictionary dictionary )
                {
                    // Updating relevant components
                    updateComponents ( dictionary.getKeys () );

                    // Informing listeners tied to components
                    fireDictionaryAdded ( dictionary );
                }

                @Override
                public void dictionaryRemoved ( final Dictionary dictionary )
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
            LanguageManager.addDictionary ( new Dictionary ( WebLanguageManager.class, "resources/ui-language.xml" ) );
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
    public static Icon getLanguageIcon ( final Language language )
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
    public static Icon getLocaleIcon ( final Locale locale )
    {
        final String key = LanguageUtils.toString ( locale );
        if ( languageIcons.containsKey ( key ) )
        {
            return languageIcons.get ( key );
        }
        else
        {
            ImageIcon icon;
            try
            {
                URL res = WebLanguageManager.class.getResource ( "icons/" + key + ".png" );
                if ( res == null )
                {
                    res = WebLanguageManager.class.getResource ( "icons/" + locale.getLanguage () + ".png" );
                }
                icon = new ImageIcon ( res );
            }
            catch ( final Exception e )
            {
                icon = UNKNOWN_LANGUAGE;
            }
            languageIcons.put ( key, icon );
            return icon;
        }
    }

    /**
     * Sets {@link Icon} for the specified {@link Language}.
     *
     * @param language {@link Language} to set {@link Icon} for
     * @param icon     {@link Icon}
     * @return {@link Icon} previously set for this {@link Language}
     */
    public static Icon setLanguageIcon ( final Language language, final Icon icon )
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
    public static Icon setLocaleIcon ( final Locale locale, final Icon icon )
    {
        final String key = LanguageUtils.toString ( locale );
        return languageIcons.put ( key, icon );
    }

    /**
     * Register custom {@link LanguageUpdater}.
     * Each {@link LanguageUpdater} is tied to a certain component class and can perform language updates only for that component type.
     *
     * @param updater new {@link LanguageUpdater}
     * @see LanguageUpdater
     */
    public static void registerLanguageUpdater ( final LanguageUpdater updater )
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
    public static void unregisterLanguageUpdater ( final LanguageUpdater updater )
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
    public static void registerLanguageUpdater ( final JComponent component, final LanguageUpdater updater )
    {
        synchronized ( updaters )
        {
            component.putClientProperty ( COMPONENT_UPDATER_KEY, updater );
            customUpdaters.put ( component, new WeakReference<LanguageUpdater> ( updater ) );
        }
    }

    /**
     * Unregisters {@link JComponent}'s custom {@link LanguageUpdater}.
     *
     * @param component {@link JComponent} to unregister custom {@link LanguageUpdater} from
     */
    public static void unregisterLanguageUpdater ( final JComponent component )
    {
        synchronized ( updaters )
        {
            component.putClientProperty ( COMPONENT_UPDATER_KEY, null );
            customUpdaters.remove ( component );
        }
    }

    /**
     * Returns {@link LanguageUpdater} currently used for the specified component.
     * This method might return either a custom per-component {@link LanguageUpdater} or global LanguageUpdater.
     * In case {@link LanguageUpdater} cannot be found for the specified component an exception will be thrown.
     *
     * @param component component to retrieve {@link LanguageUpdater} for
     * @return {@link LanguageUpdater} currently used for the specified component
     */
    public static LanguageUpdater getLanguageUpdater ( final JComponent component )
    {
        synchronized ( updaters )
        {
            // Checking custom updaters first
            final WeakReference<LanguageUpdater> updaterReference = customUpdaters.get ( component );
            final LanguageUpdater customUpdater = updaterReference != null ? updaterReference.get () : null;
            if ( customUpdater != null )
            {
                return customUpdater;
            }

            // Retrieving cached updater
            LanguageUpdater updater = updatersCache.get ( component.getClass () );

            // Checking found updater
            if ( updater != null )
            {
                // Returning cached updater
                return updater;
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

                return updater;
            }
        }
    }

    /**
     * Returns {@link LanguageUpdater} currently used for the specified component class.
     * In case {@link LanguageUpdater} cannot be found for the specified component an exception will be thrown.
     *
     * @param clazz component class to retrieve {@link LanguageUpdater} for
     * @return {@link LanguageUpdater} currently used for the specified component class
     */
    public static LanguageUpdater getLanguageUpdater ( final Class<? extends JComponent> clazz )
    {
        synchronized ( updaters )
        {
            // Retrieving cached updater
            LanguageUpdater updater = updatersCache.get ( clazz );

            // Checking found updater
            if ( updater != null )
            {
                // Returning cached updater
                return updater;
            }
            else
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

                return updater;
            }
        }
    }

    /**
     * Returns proper initial component text.
     *
     * @param key  text provided into component constructor
     * @param data language data, may not be passed
     * @return proper initial component text
     */
    public static String getInitialText ( final String key, final Object... data )
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
    public static void registerInitialLanguage ( final LanguageMethods component, final String key, final Object... data )
    {
        // Must be initialized
        mustBeInitialized ();

        // Registering component
        if ( isCheckComponentsTextForTranslations () && LM.contains ( key ) )
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
    public static void registerComponent ( final JComponent component, final String key, Object... data )
    {
        // Must be initialized
        mustBeInitialized ();

        // Properly remove previously installed language
        unregisterComponent ( component );

        // Simply unregister component if {@code null} key was provided
        if ( key == null )
        {
            return;
        }

        // Nullifying data if it has no values
        if ( data != null && data.length == 0 )
        {
            data = null;
        }

        // Saving component
        synchronized ( components )
        {
            components.put ( component, key );
            if ( data != null )
            {
                componentsData.put ( component, data );
            }
        }

        // Updating component language
        updateComponent ( component, key );
    }

    /**
     * Unregisters component from language updates.
     *
     * @param component component to unregister
     */
    public static void unregisterComponent ( final JComponent component )
    {
        // Must be initialized
        mustBeInitialized ();

        synchronized ( components )
        {
            if ( components.containsKey ( component ) )
            {
                components.remove ( component );
                componentsData.remove ( component );
            }
        }
    }

    /**
     * Returns whether component is registered for language updates or not.
     *
     * @param component component to check
     * @return {@code true} if component is registered for language updates, {@code false} otherwise
     */
    public static boolean isRegisteredComponent ( final JComponent component )
    {
        // Must be initialized
        mustBeInitialized ();

        // Checking whether or not component is registered
        synchronized ( components )
        {
            return components.containsKey ( component );
        }
    }

    /**
     * Returns language key which was used to register specified component.
     *
     * @param component component to retrieve language key for
     * @return language key which was used to register specified component
     */
    public static String getComponentKey ( final JComponent component )
    {
        // Must be initialized
        mustBeInitialized ();

        // Retrieving registered component key
        synchronized ( components )
        {
            return components.get ( component );
        }
    }

    /**
     * Forces full language update for all registered components.
     */
    public static void updateComponents ()
    {
        // Must be initialized
        mustBeInitialized ();

        // Updating all registered components
        synchronized ( components )
        {
            for ( final Map.Entry<JComponent, String> entry : components.entrySet () )
            {
                updateComponent ( entry.getKey (), entry.getValue () );
            }
        }
    }

    /**
     * Forces language update for components with the specified keys.
     *
     * @param keys language keys of the components to update
     */
    public static void updateComponents ( final Set<String> keys )
    {
        // Must be initialized
        mustBeInitialized ();

        // Updating components registered for provided keys
        synchronized ( components )
        {
            for ( final Map.Entry<JComponent, String> entry : components.entrySet () )
            {
                if ( keys.contains ( entry.getValue () ) )
                {
                    updateComponent ( entry.getKey (), entry.getValue () );
                }
            }
        }
    }

    /**
     * Forces component language update.
     *
     * @param component component to update
     * @param data      component language data
     */
    public static void updateComponent ( final JComponent component, final Object... data )
    {
        // Must be initialized
        mustBeInitialized ();

        // Checking that component is registered
        final String key = components.get ( component );
        if ( key != null )
        {
            // Updating component language
            updateComponent ( component, key, data );
        }
    }

    /**
     * Forces component language update.
     *
     * @param component component to update
     * @param key       component language key
     * @param data      component language data
     */
    public static void updateComponent ( final JComponent component, final String key, final Object... data )
    {
        // Must be initialized
        mustBeInitialized ();

        // Checking component updater existence
        final LanguageUpdater updater = getLanguageUpdater ( component );
        if ( updater != null )
        {
            // Retrieving actual component data
            final Object[] actualData;
            synchronized ( components )
            {
                if ( ArrayUtils.notEmpty ( data ) )
                {
                    // Saving new component data
                    componentsData.put ( component, data );
                    actualData = data;
                }
                else
                {
                    // Retrieving previously provided component data
                    actualData = componentsData.get ( component );
                }
            }

            // Updating component language
            updater.update ( component, LM.getLanguage (), key, actualData );
        }
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
    public static void addLanguageListener ( final JComponent component, final LanguageListener listener )
    {
        synchronized ( componentLanguageListeners )
        {
            List<LanguageListener> listeners = ( List<LanguageListener> ) component.getClientProperty ( COMPONENT_LANGUAGE_LISTENERS_KEY );
            if ( listeners == null )
            {
                listeners = new ArrayList<LanguageListener> ( 1 );
                component.putClientProperty ( COMPONENT_LANGUAGE_LISTENERS_KEY, listeners );
            }
            listeners.add ( listener );
            componentLanguageListeners.put ( component, true );
        }
    }

    /**
     * Removes {@link LanguageListener} tied to the specified {@link JComponent}.
     *
     * @param component {@link JComponent} to remove tied {@link LanguageListener} from
     * @param listener  {@link LanguageListener} to remove
     */
    public static void removeLanguageListener ( final JComponent component, final LanguageListener listener )
    {
        synchronized ( componentLanguageListeners )
        {
            final List<LanguageListener> listeners = ( List<LanguageListener> ) component
                    .getClientProperty ( COMPONENT_LANGUAGE_LISTENERS_KEY );
            if ( listeners != null )
            {
                listeners.remove ( listener );
                if ( listeners.isEmpty () )
                {
                    component.putClientProperty ( COMPONENT_LANGUAGE_LISTENERS_KEY, null );
                    componentLanguageListeners.remove ( component );
                }
            }
        }
    }

    /**
     * Removes all {@link LanguageListener}s tied to the specified {@link JComponent}.
     *
     * @param component {@link JComponent} to remove all tied {@link LanguageListener}s from
     */
    public static void removeLanguageListeners ( final JComponent component )
    {
        synchronized ( componentLanguageListeners )
        {
            final List<LanguageListener> listeners = ( List<LanguageListener> ) component
                    .getClientProperty ( COMPONENT_LANGUAGE_LISTENERS_KEY );
            if ( listeners != null )
            {
                listeners.clear ();
                component.putClientProperty ( COMPONENT_LANGUAGE_LISTENERS_KEY, null );
            }
            componentLanguageListeners.remove ( component );
        }
    }

    /**
     * Fires language changed event whenever current {@link Language} changes.
     *
     * @param oldLanguage old {@link Language}
     * @param newLanguage new {@link Language}
     */
    private static void fireLanguageChanged ( final Language oldLanguage, final Language newLanguage )
    {
        synchronized ( componentLanguageListeners )
        {
            for ( final Map.Entry<JComponent, Boolean> entry : componentLanguageListeners.entrySet () )
            {
                final JComponent component = entry.getKey ();
                if ( component != null )
                {
                    final List<LanguageListener> listeners = ( List<LanguageListener> ) component
                            .getClientProperty ( COMPONENT_LANGUAGE_LISTENERS_KEY );
                    if ( listeners != null )
                    {
                        for ( final LanguageListener listener : listeners )
                        {
                            listener.languageChanged ( oldLanguage, newLanguage );
                        }
                    }
                }
            }
        }
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
    public static void addDictionaryListener ( final JComponent component, final DictionaryListener listener )
    {
        synchronized ( componentDictionaryListeners )
        {
            List<DictionaryListener> listeners = ( List<DictionaryListener> ) component
                    .getClientProperty ( COMPONENT_DICTIONARY_LISTENERS_KEY );
            if ( listeners == null )
            {
                listeners = new ArrayList<DictionaryListener> ( 1 );
                component.putClientProperty ( COMPONENT_DICTIONARY_LISTENERS_KEY, listeners );
            }
            listeners.add ( listener );
            componentDictionaryListeners.put ( component, true );
        }
    }

    /**
     * Removes {@link DictionaryListener} tied to the specified {@link JComponent}.
     *
     * @param component {@link JComponent} to remove tied {@link DictionaryListener} from
     * @param listener  {@link DictionaryListener} to remove
     */
    public static void removeDictionaryListener ( final JComponent component, final DictionaryListener listener )
    {
        synchronized ( componentDictionaryListeners )
        {
            final List<DictionaryListener> listeners = ( List<DictionaryListener> ) component
                    .getClientProperty ( COMPONENT_DICTIONARY_LISTENERS_KEY );
            if ( listeners != null )
            {
                listeners.remove ( listener );
                if ( listeners.isEmpty () )
                {
                    component.putClientProperty ( COMPONENT_DICTIONARY_LISTENERS_KEY, null );
                    componentDictionaryListeners.remove ( component );
                }
            }
        }
    }

    /**
     * Removes all {@link DictionaryListener}s tied to the specified {@link JComponent}.
     *
     * @param component {@link JComponent} to remove all tied {@link DictionaryListener}s from
     */
    public static void removeDictionaryListeners ( final JComponent component )
    {
        synchronized ( componentDictionaryListeners )
        {
            final List<DictionaryListener> listeners = ( List<DictionaryListener> ) component
                    .getClientProperty ( COMPONENT_DICTIONARY_LISTENERS_KEY );
            if ( listeners != null )
            {
                listeners.clear ();
                component.putClientProperty ( COMPONENT_DICTIONARY_LISTENERS_KEY, null );
            }
            componentDictionaryListeners.remove ( component );
        }
    }

    /**
     * Fires {@link Dictionary} added event whenever new {@link Dictionary} is added.
     *
     * @param dictionary new {@link Dictionary}
     */
    private static void fireDictionaryAdded ( final Dictionary dictionary )
    {
        synchronized ( componentDictionaryListeners )
        {
            for ( final Map.Entry<JComponent, Boolean> entry : componentDictionaryListeners.entrySet () )
            {
                final JComponent component = entry.getKey ();
                if ( component != null )
                {
                    final List<DictionaryListener> listeners = ( List<DictionaryListener> ) component
                            .getClientProperty ( COMPONENT_DICTIONARY_LISTENERS_KEY );
                    if ( listeners != null )
                    {
                        for ( final DictionaryListener listener : listeners )
                        {
                            listener.dictionaryAdded ( dictionary );
                        }
                    }
                }
            }
        }
    }

    /**
     * Fires {@link Dictionary} removed event whenever {@link Dictionary} is removed.
     *
     * @param dictionary removed {@link Dictionary}
     */
    private static void fireDictionaryRemoved ( final Dictionary dictionary )
    {
        synchronized ( componentDictionaryListeners )
        {
            for ( final Map.Entry<JComponent, Boolean> entry : componentDictionaryListeners.entrySet () )
            {
                final JComponent component = entry.getKey ();
                if ( component != null )
                {
                    final List<DictionaryListener> listeners = ( List<DictionaryListener> ) component
                            .getClientProperty ( COMPONENT_DICTIONARY_LISTENERS_KEY );
                    if ( listeners != null )
                    {
                        for ( final DictionaryListener listener : listeners )
                        {
                            listener.dictionaryRemoved ( dictionary );
                        }
                    }
                }
            }
        }
    }

    /**
     * Fires {@link Dictionary}s cleared event whenever all {@link Dictionary}s are removed.
     */
    private static void fireDictionariesCleared ()
    {
        synchronized ( componentDictionaryListeners )
        {
            for ( final Map.Entry<JComponent, Boolean> entry : componentDictionaryListeners.entrySet () )
            {
                final JComponent component = entry.getKey ();
                if ( component != null )
                {
                    final List<DictionaryListener> listeners = ( List<DictionaryListener> ) component
                            .getClientProperty ( COMPONENT_DICTIONARY_LISTENERS_KEY );
                    if ( listeners != null )
                    {
                        for ( final DictionaryListener listener : listeners )
                        {
                            listener.dictionariesCleared ();
                        }
                    }
                }
            }
        }
    }
}