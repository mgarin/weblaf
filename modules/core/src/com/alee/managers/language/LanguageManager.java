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
import com.alee.managers.language.data.*;
import com.alee.managers.language.updaters.*;
import com.alee.utils.*;
import com.alee.utils.swing.AncestorAdapter;
import com.alee.utils.swing.DataProvider;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.io.File;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * This manager allows you to quickly setup changeable language onto different components and to listen to application-wide language change
 * events. Language could be either loaded from structured xml files or added directly from the application by adding Dictionary type
 * objects into this manager.
 * <p>
 * Be aware of the fact that all equal key-language pairs will be merged and collected into a global data map.
 * The original list of dictionaries will be preserved and will not be modified, but all translation will be taken from global data map.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-LanguageManager">How to use LanguageManager</a>
 */

public class LanguageManager implements LanguageConstants
{
    /**
     * Unknown language icon.
     */
    public static final ImageIcon other = new ImageIcon ( LanguageManager.class.getResource ( "icons/lang/other.png" ) );

    /**
     * Keys used to store custom data in JComponent.
     */
    public static final String COMPONENT_UPDATER_KEY = "language.updater";
    public static final String COMPONENT_LANGUAGE_LISTENER_KEY = "language.listener";

    /**
     * Supported languages operations synchronization object.
     */
    protected static final Object supportedLanguagesLock = new Object ();

    /**
     * Predefined list of languages supported by WebLaF.
     * You can register additional languages using LanguageManager methods.
     * Make sure you add proper translations for those languages though.
     *
     * @see #getSupportedLanguages()
     * @see #addSupportedLanguage(String)
     * @see #addSupportedLanguage(String, com.alee.managers.language.data.Dictionary)
     * @see #setSupportedLanguages(java.util.Collection)
     * @see #setSupportedLanguages(String...)
     */
    protected static final List<String> supportedLanguages =
            CollectionUtils.asList ( ENGLISH, RUSSIAN, POLISH, ARABIC, SPANISH, FRENCH, PORTUGUESE, GERMAN, ITALIAN, TURKISH );

    /**
     * Default WebLaF language.
     * You can set different value here before initializing WebLaF to decrease amount of UI updates.
     * If possible default language is taken from system properties.
     *
     * @see #setDefaultLanguage(String)
     * @see #getDefaultLanguage()
     */
    protected static String DEFAULT = getDefaultLanguageKey ();

    /**
     * Currently used language.
     *
     * @see #getLanguage()
     * @see #setLanguage(String)
     */
    protected static String language = DEFAULT;

    /**
     * Whether or not locale should be updated along with adjustment of the language.
     *
     * @see #updateLocale()
     */
    protected static boolean updateLocale = true;

    /**
     * Default tooltip type used to display tooltips provided inside language files.
     * If exact tooltip type is not specified inside specific translation this type will be used.
     *
     * @see #getDefaultTooltipType()
     * @see #setDefaultTooltipType(com.alee.managers.language.data.TooltipType)
     */
    protected static TooltipType defaultTooltipType = TooltipType.weblaf;

    /**
     * Language listeners operations synchronization object.
     */
    protected static final Object languageListenersLock = new Object ();

    /**
     * Language changes listeners.
     *
     * @see LanguageListener
     * @see #getLanguageListeners()
     * @see #addLanguageListener(LanguageListener)
     * @see #removeLanguageListener(LanguageListener)
     */
    protected static final List<LanguageListener> languageListeners = new ArrayList<LanguageListener> ();

    /**
     * Language changes listeners.
     *
     * @see LanguageListener
     * @see #getLanguageListeners()
     * @see #addLanguageListener(LanguageListener)
     * @see #removeLanguageListener(LanguageListener)
     */
    protected static final Map<JComponent, WeakReference<LanguageListener>> componentLanguageListeners =
            new WeakHashMap<JComponent, WeakReference<LanguageListener>> ();

    /**
     * Language key listeners operations synchronization object.
     */
    protected static final Object languageKeyListenersLock = new Object ();

    /**
     * Language key changes listeners.
     * Avoid using a lot of these as they might dramatically reduce application performance.
     *
     * @see LanguageKeyListener
     * @see #getLanguageKeyListeners()
     * @see #addLanguageKeyListener(String, LanguageKeyListener)
     * @see #removeLanguageKeyListener(LanguageKeyListener)
     * @see #removeLanguageKeyListeners(String)
     */
    protected static final Map<String, List<LanguageKeyListener>> languageKeyListeners = new HashMap<String, List<LanguageKeyListener>> ();

    /**
     * Global dictionary merged from all added dictionaries.
     * Used to store current dictionaries merge result and as a result improve translations retrieval speed.
     */
    protected static Dictionary globalDictionary;

    /**
     * Global values map that contains merged translations for currently selected language.
     * It gets updated on any dictionaries change or global language change.
     */
    protected static final Map<String, Value> globalCache = new HashMap<String, Value> ();

    /**
     * List of all added dictionaries.
     *
     * @see #addDictionary(Class, String)
     * @see #addDictionary(com.alee.managers.language.data.Dictionary)
     * @see #addDictionary(java.io.File)
     * @see #addDictionary(String)
     * @see #addDictionary(java.net.URL)
     * @see #removeDictionary(String)
     * @see #removeDictionary(com.alee.managers.language.data.Dictionary)
     * @see #getDictionaries()
     */
    protected static final List<Dictionary> dictionaries = new ArrayList<Dictionary> ();

    /**
     * All dictionaries cached by their special source ID.
     * Used to track already added dictionaries and avoid their duplicate versions.
     */
    protected static final Map<String, Dictionary> dictionariesCache = new HashMap<String, Dictionary> ();

    /**
     * Component operations synchronization object.
     */
    protected static final Object componentsLock = new Object ();

    /**
     * Components registered for auto-translation.
     * Specific implementations of LanguageUpdater interface used to translate them.
     *
     * @see #registerComponent(javax.swing.JComponent, String, Object...)
     * @see #updateComponent(javax.swing.JComponent, Object...)
     * @see #updateComponent(javax.swing.JComponent, String, Object...)
     * @see #unregisterComponent(javax.swing.JComponent)
     * @see #isRegisteredComponent(javax.swing.JComponent)
     */
    protected static final Map<JComponent, String> components = new WeakHashMap<JComponent, String> ();

    /**
     * Object data provided with component language key.
     * It is used to format the final translation string.
     *
     * @see #registerComponent(javax.swing.JComponent, String, Object...)
     * @see #unregisterComponent(javax.swing.JComponent)
     * @see #updateComponent(javax.swing.JComponent, Object...)
     * @see #updateComponent(javax.swing.JComponent, String, Object...)
     */
    protected static final Map<JComponent, Object[]> componentsData = new WeakHashMap<JComponent, Object[]> ();

    /**
     * Calculated components cache map.
     * It is getting filled and updated automatically when key is requested.
     *
     * @see #updateComponentKey(javax.swing.JComponent)
     */
    protected static final Map<Component, String> componentKeysCache = new WeakHashMap<Component, String> ();

    /**
     * Components ancestor listeners used to update component keys cache.
     */
    protected static final Map<Component, AncestorListener> componentsListeners = new WeakHashMap<Component, AncestorListener> ();

    /**
     * Language container operations synchronization object.
     */
    protected static final Object languageContainersLock = new Object ();

    /**
     * Registered language containers.
     * Language containers are used to apply language prefix to all container children with translation.
     * It is used for both manual and automatic translation through language updaters.
     *
     * @see #getLanguageContainerKey(java.awt.Container)
     * @see #registerLanguageContainer(java.awt.Container, String)
     * @see #unregisterLanguageContainer(java.awt.Container)
     * @see #combineWithContainerKeys(javax.swing.JComponent, String)
     */
    protected static final Map<Container, String> languageContainers = new WeakHashMap<Container, String> ();

    /**
     * LanguageUpdater operations synchronization object.
     */
    protected static final Object updatersLock = new Object ();

    /**
     * Special comparator for sorting LanguageUpdaters list.
     */
    protected static final LanguageUpdaterComparator languageUpdaterComparator = new LanguageUpdaterComparator ();

    /**
     * Registered language updaters.
     * Language updaters are used to automatically update specific components translation when language changes occur.
     *
     * @see #getLanguageUpdater(javax.swing.JComponent)
     * @see #registerLanguageUpdater(com.alee.managers.language.updaters.LanguageUpdater)
     * @see #unregisterLanguageUpdater(com.alee.managers.language.updaters.LanguageUpdater)
     */
    protected static final List<LanguageUpdater> updaters = new ArrayList<LanguageUpdater> ();

    /**
     * Component-specific language updaters.
     * These are used only for the components they are bound to.
     *
     * @see #getLanguageUpdater(javax.swing.JComponent)
     * @see #registerLanguageUpdater(javax.swing.JComponent, com.alee.managers.language.updaters.LanguageUpdater)
     * @see #unregisterLanguageUpdater(javax.swing.JComponent)
     */
    protected static final Map<JComponent, WeakReference<LanguageUpdater>> customUpdaters =
            new WeakHashMap<JComponent, WeakReference<LanguageUpdater>> ();

    /**
     * Language updaters cache by specific class types.
     * Used to improve LanguageUpdater retrieval speed for language requests.
     * This cache gets fully updated when any language updater is added or removed.
     */
    protected static final Map<Class, LanguageUpdater> updatersCache = new HashMap<Class, LanguageUpdater> ();

    /**
     * Language icons.
     * Supported language icons can be loaded without any effort as they are included in WebLaF library.
     * Custom language icons can be provided from the code using setLanguageIcon(String, javax.swing.ImageIcon) method.
     *
     * @see #setLanguageIcon(String, javax.swing.ImageIcon)
     */
    protected static final Map<String, ImageIcon> languageIcons = new HashMap<String, ImageIcon> ();

    /**
     * Current tooltip support.
     * This is a custom support object that provides tooltips according to language files.
     */
    protected static TooltipLanguageSupport tooltipLanguageSupport;

    /**
     * Whether or not components should check that text passed into their constructors is a translation key or not.
     */
    protected static boolean checkComponentsTextForTranslations = true;

    /**
     * Manager initialization mark.
     */
    protected static boolean initialized = false;

    /**
     * Initializes LanguageManager settings.
     */
    public static synchronized void initialize ()
    {
        if ( !initialized )
        {
            // Initial language
            language = supportedLanguages.contains ( DEFAULT ) ? DEFAULT : ENGLISH;

            // Locale update
            updateLocale ();

            // Default data
            globalDictionary = new Dictionary ();

            // Class aliases
            XmlUtils.processAnnotations ( Dictionary.class );
            XmlUtils.processAnnotations ( LanguageInfo.class );
            XmlUtils.processAnnotations ( Record.class );
            XmlUtils.processAnnotations ( Value.class );
            XmlUtils.processAnnotations ( Text.class );
            XmlUtils.processAnnotations ( Tooltip.class );
            XmlUtils.processAnnotations ( TooltipType.class );
            XmlUtils.processAnnotations ( TooltipWay.class );

            // Basic language updaters
            registerLanguageUpdater ( new JLabelLU () );
            registerLanguageUpdater ( new AbstractButtonLU () );
            registerLanguageUpdater ( new JTextComponentLU () );
            registerLanguageUpdater ( new JTabbedPaneLU () );
            registerLanguageUpdater ( new JProgressBarLU () );
            registerLanguageUpdater ( new JFileChooserLU () );
            registerLanguageUpdater ( new JRootPaneLU () );
            registerLanguageUpdater ( new JInternalFrameLU () );

            // Tooltip support
            setTooltipLanguageSupport ( new SwingTooltipLanguageSupport () );

            // Language listener for components update
            addLanguageListener ( new LanguageListener ()
            {
                @Override
                public void languageChanged ( final String oldLang, final String newLang )
                {
                    updateAll ();
                }

                @Override
                public void dictionaryAdded ( final Dictionary dictionary )
                {
                    updateSmart ( dictionary );
                }

                @Override
                public void dictionaryRemoved ( final Dictionary dictionary )
                {
                    updateSmart ( dictionary );
                }

                @Override
                public void dictionariesCleared ()
                {
                    updateAll ();
                }

                /**
                 * Performs complete components update.
                 */
                private void updateAll ()
                {
                    // Notifying registered key listeners
                    if ( languageKeyListeners.size () > 0 )
                    {
                        fireAllLanguageKeysUpdated ();
                    }

                    // Updating registered components
                    updateComponents ();
                }

                /**
                 * Performs smart components update.
                 *
                 * @param dictionary dictionary containing keys to update related components
                 */
                private void updateSmart ( final Dictionary dictionary )
                {
                    // Gathering all changed keys
                    final List<String> relevantKeys = LanguageUtils.gatherKeys ( dictionary );

                    // Notifying registered key listeners
                    if ( languageKeyListeners.size () > 0 )
                    {
                        for ( final String key : relevantKeys )
                        {
                            fireLanguageKeyUpdated ( key );
                        }
                    }

                    // Updating relevant components
                    updateComponents ( relevantKeys );
                }
            } );

            // Default WebLaF dictionary
            loadDefaultDictionary ();

            initialized = true;
        }
    }

    /**
     * Loads default WebLaF dictionary.
     */
    public static void loadDefaultDictionary ()
    {
        // todo Separate core and ui languages
        LanguageManager.addDictionary ( LanguageManager.class, "resources/language.xml" );
    }

    /**
     * Returns list of currently supported languages.
     * By default it contains list of languages supported by WebLaF, but it can be modified.
     *
     * @return list of currently supported languages
     */
    public static List<String> getSupportedLanguages ()
    {
        synchronized ( supportedLanguagesLock )
        {
            return CollectionUtils.copy ( supportedLanguages );
        }
    }

    /**
     * Sets supported languages.
     *
     * @param supportedLanguages collection of supported languages
     */
    public static void setSupportedLanguages ( final Collection<String> supportedLanguages )
    {
        synchronized ( supportedLanguagesLock )
        {
            // Checking that supported languages list is not empty
            if ( CollectionUtils.isEmpty ( supportedLanguages ) )
            {
                throw new LanguageException ( "There should be at least one supported language at any given time" );
            }

            // Replacing list of supported languages
            LanguageManager.supportedLanguages.clear ();
            LanguageManager.supportedLanguages.addAll ( supportedLanguages );

            // Replacing currently used language if it is not supported anymore
            if ( isSupportedLanguage ( getLanguage () ) )
            {
                // We will simply use first available supported language
                setLanguage ( supportedLanguages.iterator ().next () );
            }
        }
    }

    /**
     * Returns whether or not specified language is currently supported.
     *
     * @param language language to check
     * @return true if specified language is currently supported, false otherwise
     */
    public static boolean isSupportedLanguage ( final String language )
    {
        synchronized ( supportedLanguagesLock )
        {
            return supportedLanguages.contains ( language );
        }
    }

    /**
     * Sets supported languages.
     *
     * @param supportedLanguages supported languages
     */
    public static void setSupportedLanguages ( final String... supportedLanguages )
    {
        setSupportedLanguages ( CollectionUtils.asList ( supportedLanguages ) );
    }

    /**
     * Adds supported language.
     *
     * @param language new supported language
     */
    public static void addSupportedLanguage ( final String language )
    {
        synchronized ( supportedLanguagesLock )
        {
            supportedLanguages.add ( language );
        }
    }

    /**
     * Adds supported language and new dictionary.
     *
     * @param language   new supported language
     * @param dictionary new dictionary
     */
    public static void addSupportedLanguage ( final String language, final Dictionary dictionary )
    {
        synchronized ( supportedLanguagesLock )
        {
            supportedLanguages.add ( language );
            addDictionary ( dictionary );
        }
    }

    /**
     * Removes supported language.
     *
     * @param language supported language to remove
     */
    public static void removeSupportedLanguage ( final String language )
    {
        synchronized ( supportedLanguagesLock )
        {
            supportedLanguages.remove ( language );
        }
    }

    /**
     * Clears list of supported languages.
     */
    public static void clearSupportedLanguages ()
    {
        synchronized ( supportedLanguagesLock )
        {
            supportedLanguages.clear ();
        }
    }

    /**
     * Registers component for language updates.
     * This component language will be automatically updated using existing LanguageUpdater.
     *
     * @param component component to register
     * @param key       component language key
     * @param data      component language data
     * @see com.alee.managers.language.updaters.LanguageUpdater
     */
    public static void registerComponent ( final JComponent component, final String key, Object... data )
    {
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
        synchronized ( componentsLock )
        {
            components.put ( component, key );
            if ( data != null )
            {
                componentsData.put ( component, data );
            }
        }

        // Updating component language
        updateComponent ( component, key );

        // Registering component listener
        synchronized ( componentsLock )
        {
            final WeakReference<JComponent> ref = new WeakReference<JComponent> ( component );
            final AncestorAdapter listener = new AncestorAdapter ()
            {
                @Override
                public void ancestorAdded ( final AncestorEvent event )
                {
                    updateComponentKey ( ref.get () );
                }
            };
            component.addAncestorListener ( listener );
            componentsListeners.put ( component, listener );
        }
    }

    /**
     * Updates components tree language keys according to their container keys.
     *
     * @param component component to update
     */
    public static void updateComponentsTree ( final JComponent component )
    {
        updateComponentKey ( component );
        if ( component instanceof Container )
        {
            for ( final Component child : component.getComponents () )
            {
                if ( child instanceof JComponent )
                {
                    updateComponentsTree ( ( JComponent ) child );
                }
            }
        }
    }

    /**
     * Updates component language key according to its container keys.
     *
     * @param component component to update
     */
    protected static void updateComponentKey ( final JComponent component )
    {
        final String key = getComponentKey ( component );
        if ( key != null )
        {
            final String oldKey = componentKeysCache.get ( component );
            final String newKey = combineWithContainerKeysImpl ( component, key );
            if ( oldKey == null || !CompareUtils.equals ( oldKey, newKey ) )
            {
                LanguageManager.updateComponent ( component, key );
            }
        }
    }

    /**
     * Unregisters component from language updates.
     *
     * @param component component to unregister
     */
    public static void unregisterComponent ( final JComponent component )
    {
        synchronized ( componentsLock )
        {
            if ( components.containsKey ( component ) )
            {
                components.remove ( component );
                componentsData.remove ( component );
                componentKeysCache.remove ( component );

                final AncestorListener listener = componentsListeners.get ( component );
                component.removeAncestorListener ( listener );
                componentsListeners.remove ( component );
            }
        }
    }

    /**
     * Returns whether component is registered for language updates or not.
     *
     * @param component component to check
     * @return true if component is registered for language updates, false otherwise
     */
    public static boolean isRegisteredComponent ( final JComponent component )
    {
        synchronized ( componentsLock )
        {
            return components.containsKey ( component );
        }
    }

    /**
     * Returns component language key.
     * Note that this is the key which was used to register the component.
     * Its actual language key might be different in case there is a language container under this component.
     *
     * @param component component to retrieve language key for
     * @return component language key
     */
    public static String getComponentKey ( final JComponent component )
    {
        synchronized ( componentsLock )
        {
            return components.get ( component );
        }
    }

    /**
     * Register custom LanguageUpdater.
     * Each LanguageUpdater is tied to a certain component class and can perform language updates only for that component type.
     *
     * @param updater new LanguageUpdater
     * @see com.alee.managers.language.updaters.LanguageUpdater
     */
    public static void registerLanguageUpdater ( final LanguageUpdater updater )
    {
        synchronized ( updatersLock )
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
     * Unregister custom LanguageUpdater.
     *
     * @param updater LanguageUpdater to unregister
     */
    public static void unregisterLanguageUpdater ( final LanguageUpdater updater )
    {
        synchronized ( updatersLock )
        {
            // Removing LanguageUpdater
            updaters.remove ( updater );
            updatersCache.clear ();
        }
    }

    /**
     * Registers custom LanguageUpdater for specific component.
     *
     * @param component component to register LanguageUpdater for
     * @param updater   custom LanguageUpdater
     */
    public static void registerLanguageUpdater ( final JComponent component, final LanguageUpdater updater )
    {
        synchronized ( updatersLock )
        {
            component.putClientProperty ( COMPONENT_UPDATER_KEY, updater );
            customUpdaters.put ( component, new WeakReference<LanguageUpdater> ( updater ) );
        }
    }

    /**
     * Unregisters component's custom LanguageUpdater.
     *
     * @param component component to unregister custom LanguageUpdater from
     */
    public static void unregisterLanguageUpdater ( final JComponent component )
    {
        synchronized ( updatersLock )
        {
            component.putClientProperty ( COMPONENT_UPDATER_KEY, null );
            customUpdaters.remove ( component );
        }
    }

    /**
     * Returns LanguageUpdater currently used for the specified component.
     * This method might return either a custom per-component LanguageUpdater or global LanguageUpdater.
     * In case LanguageUpdater cannot be found for the specified component an exception will be thrown.
     *
     * @param component component to retrieve LanguageUpdater for
     * @return LanguageUpdater currently used for the specified component
     */
    public static LanguageUpdater getLanguageUpdater ( final JComponent component )
    {
        synchronized ( updatersLock )
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
                    // Usually this shouldn't happen unless you register an unsupported component
                    throw new RuntimeException ( "Unable to find LanguageUpdater for component: " + component );
                }

                // Caching calculated updater
                updatersCache.put ( component.getClass (), updater );

                return updater;
            }
        }
    }

    /**
     * Forces full language update for all registered components.
     */
    public static void updateComponents ()
    {
        synchronized ( componentsLock )
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
    public static void updateComponents ( final List<String> keys )
    {
        synchronized ( componentsLock )
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
        final String key = components.get ( component );
        if ( key != null )
        {
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
    public static void updateComponent ( final JComponent component, final String key, Object... data )
    {
        // Nullifying data if it has no values
        if ( data != null && data.length == 0 )
        {
            data = null;
        }

        // Not-null value for specified key
        final Value value = getNotNullValue ( component, key );

        // Actualized value data
        final Object[] actualData;
        synchronized ( componentsLock )
        {
            if ( data != null )
            {
                componentsData.put ( component, data );
                actualData = data;
            }
            else
            {
                actualData = componentsData.get ( component );
            }
        }

        // Updating component language
        final LanguageUpdater updater = getLanguageUpdater ( component );
        if ( updater != null )
        {
            updater.update ( component, key, value, parseData ( actualData ) );
        }

        // Updating component tooltip language
        if ( tooltipLanguageSupport != null )
        {
            tooltipLanguageSupport.setupTooltip ( component, value );
        }
    }

    /**
     * Returns language data transformed into its final form.
     *
     * @param data language data to process
     * @return language data transformed into its final form
     */
    public static Object[] parseData ( final Object... data )
    {
        if ( data != null )
        {
            final Object[] finalData = new Object[ data.length ];
            for ( int i = 0; i < data.length; i++ )
            {
                final Object object = data[ i ];
                if ( object != null && object instanceof DataProvider )
                {
                    finalData[ i ] = ( ( DataProvider ) object ).provide ();
                }
                else
                {
                    finalData[ i ] = object;
                }
            }
            return finalData;
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns icon for the specified language.
     * By default there are icons only for languages supported by WebLaF.
     *
     * @param language language to retrieve icon for
     * @return icon for the specified language
     */
    public static ImageIcon getLanguageIcon ( final String language )
    {
        if ( languageIcons.containsKey ( language ) )
        {
            return languageIcons.get ( language );
        }
        else
        {
            ImageIcon icon;
            try
            {
                final URL res = LanguageManager.class.getResource ( "icons/lang/" + language + ".png" );
                icon = new ImageIcon ( res );
            }
            catch ( final Throwable e )
            {
                icon = other;
            }
            languageIcons.put ( language, icon );
            return icon;
        }
    }

    /**
     * Sets icon for the specified language.
     *
     * @param language language to set icon for
     * @param icon     language icon
     * @return icon previously set for this language
     */
    public static ImageIcon setLanguageIcon ( final String language, final ImageIcon icon )
    {
        return languageIcons.put ( language, icon );
    }

    /**
     * Returns language title in that language translation.
     *
     * @param language language to get title for
     * @return language title in that language translation
     */
    public static String getLanguageTitle ( final String language )
    {
        final LanguageInfo info = globalDictionary.getLanguageInfo ( language );
        return info != null ? info.getTitle () : null;
    }

    /**
     * Returns current tooltip language support.
     *
     * @return current tooltip language support
     * @see com.alee.managers.language.TooltipLanguageSupport
     * @see com.alee.managers.language.SwingTooltipLanguageSupport
     */
    public static TooltipLanguageSupport getTooltipLanguageSupport ()
    {
        return tooltipLanguageSupport;
    }

    /**
     * Sets tooltip language support.
     *
     * @param support new tooltip language support
     * @see com.alee.managers.language.TooltipLanguageSupport
     * @see com.alee.managers.language.SwingTooltipLanguageSupport
     */
    public static void setTooltipLanguageSupport ( final TooltipLanguageSupport support )
    {
        LanguageManager.tooltipLanguageSupport = support;
    }

    /**
     * Returns default tooltip type.
     * This tooltip type is used for all tooltips which doesn't have a specified type in language file.
     *
     * @return default tooltip type
     * @see com.alee.managers.language.data.TooltipType
     */
    public static TooltipType getDefaultTooltipType ()
    {
        return defaultTooltipType;
    }

    /**
     * Sets default tooltip type.
     * This tooltip type is used for all tooltips which doesn't have a specified type in language file.
     *
     * @param defaultTooltipType new default tooltip type
     * @see com.alee.managers.language.data.TooltipType
     */
    public static void setDefaultTooltipType ( final TooltipType defaultTooltipType )
    {
        LanguageManager.defaultTooltipType = defaultTooltipType;
    }

    /**
     * Returns default language.
     * This language is used when LanguageManager is initialized.
     *
     * @return default language
     */
    public static String getDefaultLanguage ()
    {
        return DEFAULT;
    }

    /**
     * Sets default language.
     * This language is used when LanguageManager is initialized.
     *
     * @param lang new default language
     */
    public static void setDefaultLanguage ( final String lang )
    {
        LanguageManager.DEFAULT = lang;
    }

    /**
     * Returns currently used language.
     *
     * @return currently used language
     */
    public static String getLanguage ()
    {
        return language;
    }

    /**
     * Returns whether the specified language is currently used or not.
     *
     * @param language language to check
     * @return true if the specified language is currently used, false otherwise
     */
    public static boolean isCurrentLanguage ( final String language )
    {
        return LanguageManager.language.equals ( language );
    }

    /**
     * Sets currently used language.
     * In case LanguageManager is not yet initialized this will simply set default language
     *
     * @param language new language
     */
    public static void setLanguage ( final String language )
    {
        if ( initialized )
        {
            // Ignore incorrect and pointless changes
            if ( language == null || isCurrentLanguage ( language ) )
            {
                return;
            }

            // Changing language
            final String oldLanguage = LanguageManager.language;
            LanguageManager.language = language;

            // Updating locale
            updateLocale ();

            // Updating global cache
            rebuildCache ();

            // Firing language change event
            fireLanguageChanged ( oldLanguage, language );
        }
        else
        {
            // Simply applying default language
            DEFAULT = language;
        }
    }

    /**
     * Sets currently used language.
     * In case LanguageManager is not yet initialized this will simply set default language
     *
     * @param language       used language
     * @param otherLanguages other available languages
     */
    public static void setLanguages ( final String language, final String... otherLanguages )
    {
        // Supported languages
        final List<String> supported = CollectionUtils.asList ( otherLanguages );
        supported.add ( 0, language );
        setSupportedLanguages ( supported );

        // Current language
        setLanguage ( language );
    }

    /**
     * Switches current language to next language in supported languages list.
     */
    public static void switchLanguage ()
    {
        if ( supportedLanguages.size () > 0 )
        {
            final int current = supportedLanguages.indexOf ( getLanguage () );
            setLanguage ( supportedLanguages.get ( ( current == -1 || current == supportedLanguages.size () - 1 ) ? 0 : current + 1 ) );
        }
    }

    /**
     * Returns whether or not locale should be updated along with adjustment of the language.
     *
     * @return true if locale should be updated along with adjustment of the language, false otherwise
     */
    public static boolean isUpdateLocale ()
    {
        return updateLocale;
    }

    /**
     * Sets whether or not locale should be updated along with adjustment of the language
     *
     * @param update whether or not locale should be updated along with adjustment of the language
     */
    public static void setUpdateLocale ( final boolean update )
    {
        LanguageManager.updateLocale = update;
        if ( update )
        {
            updateLocale ();
        }
    }

    /**
     * Updates Locale according to currently used language.
     */
    protected static void updateLocale ()
    {
        if ( updateLocale )
        {
            // Proper locale for language
            Locale.setDefault ( getLocale ( language ) );

            // todo Use this one instead after switching to JDK8+ support
            // Locale.setDefault ( Locale.forLanguageTag ( language ) );
        }
    }

    /**
     * Returns Locale for the specified language.
     *
     * @param language language to return Locale for
     * @return Locale for the specified language
     */
    public static Locale getLocale ( final String language )
    {
        Locale detected = null;
        for ( final Locale locale : Locale.getAvailableLocales () )
        {
            if ( locale.getLanguage ().equals ( language ) )
            {
                detected = locale;
            }
        }
        return detected != null ? detected : new Locale ( language );
    }

    /**
     * Returns global dictionary that aggregates all added dictionaries.
     *
     * @return global dictionary that aggregates all added dictionaries
     */
    public static Dictionary getGlobalDictionary ()
    {
        return globalDictionary;
    }

    /**
     * Returns all dictionaries added into LanguageManager.
     *
     * @return all dictionaries added into LanguageManager
     */
    public static List<Dictionary> getDictionaries ()
    {
        return CollectionUtils.copy ( dictionaries );
    }

    /**
     * Adds new language dictionary into LanguageManager.
     * This method call may cause a lot of UI updates depending on amount of translations contained.
     * If added dictionary contains records with existing keys they will override previously added ones.
     *
     * @param nearClass class to look near for the dictionary file
     * @param resource  path to dictionary file
     * @return newly added dictionary
     */
    public static Dictionary addDictionary ( final Class nearClass, final String resource )
    {
        final String cacheKey = getDictionaryCacheKey ( nearClass, resource );
        if ( dictionariesCache.containsKey ( cacheKey ) )
        {
            return dictionariesCache.get ( cacheKey );
        }
        return addDictionary ( cacheKey, loadDictionary ( nearClass, resource ) );
    }

    /**
     * Returns dictionary cache key.
     *
     * @param nearClass class to look near for the dictionary file
     * @param resource  path to dictionary file
     * @return dictionary cache key
     */
    public static String getDictionaryCacheKey ( final Class nearClass, final String resource )
    {
        return NetUtils.getAddress ( nearClass.getResource ( resource ) );
    }

    /**
     * Returns dictionary loaded from the specified location near class.
     *
     * @param nearClass class near which dictionary XML is located
     * @param resource  dictionary XML file
     * @return dictionary loaded from the specified location near class
     */
    public static Dictionary loadDictionary ( final Class nearClass, final String resource )
    {
        return loadDictionary ( nearClass.getResource ( resource ) );
    }

    /**
     * Adds new language dictionary into LanguageManager.
     * This method call may cause a lot of UI updates depending on amount of translations contained.
     * If added dictionary contains records with existing keys they will override previously added ones.
     *
     * @param url dictionary file url
     * @return newly added dictionary
     */
    public static Dictionary addDictionary ( final URL url )
    {
        final String cacheKey = getDictionaryCacheKey ( url );
        if ( dictionariesCache.containsKey ( cacheKey ) )
        {
            return dictionariesCache.get ( cacheKey );
        }
        return addDictionary ( cacheKey, loadDictionary ( url ) );
    }

    /**
     * Returns dictionary cache key.
     *
     * @param url dictionary file url
     * @return dictionary cache key
     */
    public static String getDictionaryCacheKey ( final URL url )
    {
        return NetUtils.getAddress ( url );
    }

    /**
     * Returns dictionary loaded from the specified URL.
     *
     * @param url URL to load dictionary from
     * @return dictionary loaded from the specified URL
     */
    public static Dictionary loadDictionary ( final URL url )
    {
        return XmlUtils.fromXML ( url );
    }

    /**
     * Adds new language dictionary into LanguageManager.
     * This method call may cause a lot of UI updates depending on amount of translations contained.
     * If added dictionary contains records with existing keys they will override previously added ones.
     *
     * @param path path to dictionary file
     * @return newly added dictionary
     */
    public static Dictionary addDictionary ( final String path )
    {
        final String cacheKey = getDictionaryCacheKey ( path );
        if ( dictionariesCache.containsKey ( cacheKey ) )
        {
            return dictionariesCache.get ( cacheKey );
        }
        return addDictionary ( cacheKey, loadDictionary ( path ) );
    }

    /**
     * Returns dictionary cache key.
     *
     * @param path path to dictionary file
     * @return dictionary cache key
     */
    public static String getDictionaryCacheKey ( final String path )
    {
        return path;
    }

    /**
     * Returns dictionary loaded from the specified file path.
     *
     * @param path file path to load dictionary from
     * @return dictionary loaded from the specified file path
     */
    public static Dictionary loadDictionary ( final String path )
    {
        return loadDictionary ( new File ( path ) );
    }

    /**
     * Adds new language dictionary into LanguageManager.
     * This method call may cause a lot of UI updates depending on amount of translations contained.
     * If added dictionary contains records with existing keys they will override previously added ones.
     *
     * @param file dictionary file
     * @return newly added dictionary
     */
    public static Dictionary addDictionary ( final File file )
    {
        final String cacheKey = getDictionaryCacheKey ( file );
        if ( dictionariesCache.containsKey ( cacheKey ) )
        {
            return dictionariesCache.get ( cacheKey );
        }
        return addDictionary ( cacheKey, loadDictionary ( file ) );
    }

    /**
     * Returns dictionary cache key.
     *
     * @param file dictionary file
     * @return dictionary cache key
     */
    public static String getDictionaryCacheKey ( final File file )
    {
        return file.getAbsolutePath ();
    }

    /**
     * Returns dictionary loaded from the specified file.
     *
     * @param file file to load dictionary from
     * @return dictionary loaded from the specified file
     */
    public static Dictionary loadDictionary ( final File file )
    {
        return XmlUtils.fromXML ( file );
    }

    /**
     * Adds new language dictionary into LanguageManager.
     * This method call may cause a lot of UI updates depending on amount of translations contained.
     * If added dictionary contains records with existing keys they will override previously added ones.
     *
     * @param dictionary dictionary to add
     * @return newly added dictionary
     */
    public static Dictionary addDictionary ( final Dictionary dictionary )
    {
        return addDictionary ( null, dictionary );
    }

    /**
     * Adds new language dictionary into LanguageManager.
     * This method call may cause a lot of UI updates depending on amount of translations contained.
     * If added dictionary contains records with existing keys they will override previously added ones.
     *
     * @param cacheKey   dictionary cache key
     * @param dictionary dictionary to add
     * @return newly added dictionary
     */
    public static Dictionary addDictionary ( final String cacheKey, final Dictionary dictionary )
    {
        // Return cached dictionary if one exists
        if ( dictionariesCache.containsKey ( cacheKey ) )
        {
            return dictionariesCache.get ( cacheKey );
        }

        // Removing dictionary with the same ID first
        if ( isDictionaryAdded ( dictionary ) )
        {
            removeDictionary ( dictionary );
        }

        // Updating dictionaries
        dictionaries.add ( dictionary );

        // Updating global dictionary
        LanguageUtils.mergeDictionary ( dictionary, globalDictionary );

        // Updating global cache
        updateCache ( dictionary );

        // Caching dictionary by its cache key
        if ( cacheKey != null )
        {
            dictionariesCache.put ( cacheKey, dictionary );
        }

        // Firing add event
        fireDictionaryAdded ( dictionary );

        return dictionary;
    }

    /**
     * Removes dictionary from LanguageManager.
     * This method call may cause a lot of UI updates depending on amount of translations contained.
     *
     * @param id ID of the dictionary to remove
     * @return removed dictionary
     */
    public static Dictionary removeDictionary ( final String id )
    {
        return removeDictionary ( getDictionary ( id ) );
    }

    /**
     * Removes dictionary from LanguageManager.
     * This method call may cause a lot of UI updates depending on amount of translations contained.
     *
     * @param dictionary dictionary to remove
     * @return removed dictionary
     */
    public static Dictionary removeDictionary ( final Dictionary dictionary )
    {
        if ( dictionary != null && isDictionaryAdded ( dictionary ) )
        {
            // Clearing global dictionaries storage
            globalDictionary.clear ();

            // Removing dictionary
            dictionaries.remove ( dictionary );

            // Removing dictionary from cache
            MapUtils.removeAllValues ( dictionariesCache, dictionary );

            // Updating dictionaries
            for ( final Dictionary d : dictionaries )
            {
                LanguageUtils.mergeDictionary ( d, globalDictionary );
            }

            // Updating global cache
            rebuildCache ();

            // Firing removal event
            fireDictionaryRemoved ( dictionary );

            return dictionary;
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns whether specified dictionary is added or not.
     *
     * @param dictionary dictionary to look for
     * @return true if dictionary is added, false otherwise
     */
    public static boolean isDictionaryAdded ( final Dictionary dictionary )
    {
        return isDictionaryAdded ( dictionary.getId () );
    }

    /**
     * Returns whether dictionary with the specified ID is added or not.
     *
     * @param id ID of the dictionary to look for
     * @return true if dictionary is added, false otherwise
     */
    public static boolean isDictionaryAdded ( final String id )
    {
        for ( final Dictionary dictionary : dictionaries )
        {
            if ( dictionary.getId ().equals ( id ) )
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns dictionary for the specified ID or null if it was not found.
     *
     * @param id ID of the dictionary to look for
     * @return dictionary for the specified ID or null if it was not found
     */
    public static Dictionary getDictionary ( final String id )
    {
        for ( final Dictionary dictionary : dictionaries )
        {
            if ( dictionary.getId ().equals ( id ) )
            {
                return dictionary;
            }
        }
        return null;
    }

    /**
     * Removes all added dictionaries including WebLaF ones.
     * You can always restore WebLaF dictionary by calling loadDefaultDictionary () method in LanguageManager.
     *
     * @see #loadDefaultDictionary()
     */
    public static void clearDictionaries ()
    {
        globalDictionary.clear ();
        dictionaries.clear ();
        clearCache ();
        fireDictionariesCleared ();
    }

    /**
     * Returns list of languages supported by the dictionary with the specified ID.
     *
     * @param dictionaryId dictionary ID
     * @return list of languages supported by the dictionary with the specified ID
     */
    public static List<String> getSupportedLanguages ( final String dictionaryId )
    {
        return getSupportedLanguages ( getDictionary ( dictionaryId ) );
    }

    /**
     * Returns list of languages supported by the specified dictionary.
     *
     * @param dictionary dictionary
     * @return list of languages supported by the specified dictionary
     */
    public static List<String> getSupportedLanguages ( final Dictionary dictionary )
    {
        return dictionary == null ? Collections.EMPTY_LIST : dictionary.getSupportedLanguages ();
    }

    /**
     * Returns translation for the specified language key.
     *
     * @param key language key to retrieve translation for
     * @return translation for the specified language key
     */
    public static String get ( final String key )
    {
        final Value value = getValue ( key );
        return value != null ? value.getText () : key;
    }

    /**
     * Returns translation for the specified language key.
     *
     * @param key  language key to retrieve translation for
     * @param data language data
     * @return translation for the specified language key
     */
    public static String get ( final String key, final Object... data )
    {
        final String text = get ( key );
        return text != null ? String.format ( text, parseData ( data ) ) : null;
    }

    /**
     * Returns mnemonic for the specified language key.
     *
     * @param key language key to retrieve mnemonic for
     * @return mnemonic for the specified language key
     */
    public static Character getMnemonic ( final String key )
    {
        final Value value = getValue ( key );
        return value != null ? value.getMnemonic () : null;
    }

    /**
     * Returns value for the specified language key.
     *
     * @param key language key to retrieve value for
     * @return value for the specified language key
     */
    public static Value getValue ( final String key )
    {
        // Global cache might be null when LanguageManager is not initialized
        return globalCache != null ? globalCache.get ( key ) : null;
    }

    /**
     * Returns non-null value for the specified language key.
     *
     * @param key language key to retrieve value for
     * @return non-null value for the specified language key
     */
    public static Value getNotNullValue ( final String key )
    {
        // Not-null value returned in any case
        final Value value = getValue ( key );
        if ( value != null )
        {
            return value;
        }
        else
        {
            final Value tmpValue = new Value ( getLanguage (), key );
            globalCache.put ( key, tmpValue );
            return tmpValue;
        }
    }

    /**
     * Returns whether specified language key exists or not.
     *
     * @param key language key to check
     * @return whether specified language key exists or not
     */
    public static boolean contains ( final String key )
    {
        return globalCache.containsKey ( key );
    }

    /**
     * Returns component translation.
     *
     * @param component component to retrieve translation for
     * @param key       component language key
     * @return component translation
     */
    public static String get ( final JComponent component, final String key )
    {
        return get ( combineWithContainerKeys ( component, key ) );
    }

    /**
     * Returns component mnemonic.
     *
     * @param component component to retrieve mnemonic for
     * @param key       component language key
     * @return component mnemonic
     */
    public static Character getMnemonic ( final JComponent component, final String key )
    {
        return getMnemonic ( combineWithContainerKeys ( component, key ) );
    }

    /**
     * Returns component language value.
     *
     * @param component component to retrieve language value for
     * @param key       component language key
     * @return component language value
     */
    public static Value getValue ( final JComponent component, final String key )
    {
        return getValue ( combineWithContainerKeys ( component, key ) );
    }

    /**
     * Returns non-null component language value.
     *
     * @param component component to retrieve language value for
     * @param key       component language key
     * @return non-null component language value
     */
    public static Value getNotNullValue ( final JComponent component, final String key )
    {
        return getNotNullValue ( combineWithContainerKeys ( component, key ) );
    }

    /**
     * Returns component language value.
     *
     * @param component     component to retrieve language value for
     * @param key           component language key
     * @param additionalKey additional language key
     * @return component language value
     */
    public static Value getValue ( final JComponent component, final String key, final String additionalKey )
    {
        return getValue ( combineWithContainerKeys ( component, key ) + "." + additionalKey );
    }

    /**
     * Returns non-null component language value.
     *
     * @param component     component to retrieve language value for
     * @param key           component language key
     * @param additionalKey additional language key
     * @return non-null component language value
     */
    public static Value getNotNullValue ( final JComponent component, final String key, final String additionalKey )
    {
        return getNotNullValue ( combineWithContainerKeys ( component, key ) + "." + additionalKey );
    }

    /**
     * Returns component language key combined with its containers keys.
     *
     * @param component component to retrieve language key for
     * @param key       component language key
     * @return component language key combined with its containers keys
     */
    public static String combineWithContainerKeys ( final JComponent component, final String key )
    {
        final String cachedKey = componentKeysCache.get ( component );
        return cachedKey != null ? cachedKey : combineWithContainerKeysImpl ( component, key );
    }

    /**
     * Returns component language key combined with its containers keys.
     *
     * @param component component to retrieve language key for
     * @param key       component language key
     * @return component language key combined with its containers keys
     */
    protected static String combineWithContainerKeysImpl ( final JComponent component, final String key )
    {
        final StringBuilder sb = new StringBuilder ( key );
        if ( component != null )
        {
            Container parent = component.getParent ();
            while ( parent != null )
            {
                final String containerKey = getLanguageContainerKey ( parent );
                if ( containerKey != null )
                {
                    sb.insert ( 0, containerKey + "." );
                }
                parent = parent.getParent ();
            }
        }
        final String cachedKey = sb.toString ();
        componentKeysCache.put ( component, cachedKey );
        return cachedKey;
    }

    /**
     * Registers language container key.
     *
     * @param container container to register
     * @param key       language container key
     */
    public static void registerLanguageContainer ( final Container container, final String key )
    {
        synchronized ( languageContainersLock )
        {
            languageContainers.put ( container, key );
        }
    }

    /**
     * Unregisters language container key.
     *
     * @param container container to unregister
     */
    public static void unregisterLanguageContainer ( final Container container )
    {
        synchronized ( languageContainersLock )
        {
            languageContainers.remove ( container );
        }
    }

    /**
     * Returns language container key for the specified container.
     *
     * @param container container to retrieve language container key for
     * @return language container key for the specified container
     */
    public static String getLanguageContainerKey ( final Container container )
    {
        synchronized ( languageContainersLock )
        {
            return languageContainers.get ( container );
        }
    }

    /**
     * Rebuilds global dictionaries cache from a scratch.
     * This is required for cases when dictionary changes cannot be tracked or when current language changes.
     */
    protected static void rebuildCache ()
    {
        clearCache ();
        updateCache ( globalDictionary );
    }

    /**
     * Clears global dictionaries cache.
     */
    protected static void clearCache ()
    {
        globalCache.clear ();
    }

    /**
     * Updates global dictionaries cache with the specified dictionary.
     *
     * @param dictionary dictionary to update cache with
     */
    protected static void updateCache ( final Dictionary dictionary )
    {
        updateCache ( dictionary.getPrefix (), dictionary );
    }

    /**
     * Updates global dictionaries cache with the specified dictionary.
     *
     * @param prefix     dictionary prefix
     * @param dictionary dictionary to update cache with
     */
    protected static void updateCache ( String prefix, final Dictionary dictionary )
    {
        // Determining prefix
        prefix = prefix != null && !prefix.equals ( "" ) ? prefix + "." : "";

        // Parsing current level records
        if ( dictionary.getRecords () != null )
        {
            for ( final Record record : dictionary.getRecords () )
            {
                final Value value = record.getValue ( language );
                if ( value != null && value.getHotkey () == null && record.getHotkey () != null )
                {
                    value.setHotkey ( record.getHotkey () );
                }
                globalCache.put ( prefix + record.getKey (), value );
            }
        }

        // Parsing sub-dictionaries
        if ( dictionary.getSubdictionaries () != null )
        {
            for ( final Dictionary subDictionary : dictionary.getSubdictionaries () )
            {
                final String sp = subDictionary.getPrefix ();
                final String subPrefix = prefix + ( sp != null && !sp.equals ( "" ) ? sp : "" );
                updateCache ( subPrefix, subDictionary );
            }
        }
    }

    /**
     * Returns whether or not components should check that text passed into their constructors is a translation key or not.
     *
     * @return true if components should check that text passed into their constructor is a translation key or not, false otherwise
     */
    public static boolean isCheckComponentsTextForTranslations ()
    {
        return checkComponentsTextForTranslations;
    }

    /**
     * Sets whether or not components should check that text passed into their constructors is a translation key or not.
     *
     * @param check whether or not components should check that text passed into their constructors is a translation key or not
     */
    public static void setCheckComponentsTextForTranslations ( final boolean check )
    {
        checkComponentsTextForTranslations = check;
    }

    /**
     * Returns default language key.
     *
     * @return default language key
     */
    public static String getDefaultLanguageKey ()
    {
        final String systemLang = getSystemLanguageKey ();
        return supportedLanguages.contains ( systemLang ) ? systemLang : ENGLISH;
    }

    /**
     * Returns system language key.
     *
     * @return system language key
     */
    public static String getSystemLanguageKey ()
    {
        return System.getProperty ( "user.language" );
    }

    /**
     * Returns language listeners.
     *
     * @return language listeners
     */
    public static List<LanguageListener> getLanguageListeners ()
    {
        synchronized ( languageListenersLock )
        {
            return CollectionUtils.copy ( languageListeners );
        }
    }

    /**
     * Adds new language listener.
     *
     * @param listener new language listener
     */
    public static void addLanguageListener ( final LanguageListener listener )
    {
        synchronized ( languageListenersLock )
        {
            languageListeners.add ( listener );
        }
    }

    /**
     * Removes language listener.
     *
     * @param listener language listener to remove
     */
    public static void removeLanguageListener ( final LanguageListener listener )
    {
        synchronized ( languageListenersLock )
        {
            languageListeners.remove ( listener );
        }
    }

    /**
     * Returns component language listeners.
     *
     * @return component language listeners
     */
    public static Map<Component, LanguageListener> getComponentLanguageListeners ()
    {
        synchronized ( languageListenersLock )
        {
            final Map<Component, LanguageListener> listeners =
                    new HashMap<Component, LanguageListener> ( componentLanguageListeners.size () );
            for ( final Map.Entry<JComponent, WeakReference<LanguageListener>> entry : componentLanguageListeners.entrySet () )
            {
                final JComponent component = entry.getKey ();
                if ( component != null )
                {
                    final WeakReference<LanguageListener> listenerReference = entry.getValue ();
                    final LanguageListener listener = listenerReference != null ? listenerReference.get () : null;
                    if ( listener != null )
                    {
                        listeners.put ( component, listener );
                    }
                }
            }
            return listeners;
        }
    }

    /**
     * Adds language listener for the specified component.
     *
     * @param component component to add language listener for
     * @param listener  new language listener
     */
    public static void addLanguageListener ( final JComponent component, final LanguageListener listener )
    {
        synchronized ( languageListenersLock )
        {
            component.putClientProperty ( COMPONENT_LANGUAGE_LISTENER_KEY, listener );
            componentLanguageListeners.put ( component, new WeakReference<LanguageListener> ( listener ) );
        }
    }

    /**
     * Removes language listener from the specified component.
     *
     * @param component component to remove language listener from
     */
    public static void removeLanguageListener ( final JComponent component )
    {
        synchronized ( languageListenersLock )
        {
            component.putClientProperty ( COMPONENT_LANGUAGE_LISTENER_KEY, null );
            componentLanguageListeners.remove ( component );
        }
    }

    /**
     * Fires language changed event whenever current language changes.
     *
     * @param oldLang old language
     * @param newLang new language
     */
    protected static void fireLanguageChanged ( final String oldLang, final String newLang )
    {
        synchronized ( languageListenersLock )
        {
            for ( final LanguageListener listener : languageListeners )
            {
                listener.languageChanged ( oldLang, newLang );
            }
            for ( final Map.Entry<JComponent, WeakReference<LanguageListener>> entry : componentLanguageListeners.entrySet () )
            {
                final JComponent key = entry.getKey ();
                if ( key != null )
                {
                    final WeakReference<LanguageListener> listenerReference = entry.getValue ();
                    final LanguageListener listener = listenerReference != null ? listenerReference.get () : null;
                    if ( listener != null )
                    {
                        listener.languageChanged ( oldLang, newLang );
                    }
                }
            }
        }
    }

    /**
     * Fires dictionary added event whenever new dictionary is added into LanguageManager.
     *
     * @param dictionary new dictionary
     */
    protected static void fireDictionaryAdded ( final Dictionary dictionary )
    {
        synchronized ( languageListenersLock )
        {
            for ( final LanguageListener listener : languageListeners )
            {
                listener.dictionaryAdded ( dictionary );
            }
            for ( final Map.Entry<JComponent, WeakReference<LanguageListener>> entry : componentLanguageListeners.entrySet () )
            {
                final JComponent key = entry.getKey ();
                if ( key != null )
                {
                    final WeakReference<LanguageListener> listenerReference = entry.getValue ();
                    final LanguageListener listener = listenerReference != null ? listenerReference.get () : null;
                    if ( listener != null )
                    {
                        listener.dictionaryAdded ( dictionary );
                    }
                }
            }
        }
    }

    /**
     * Fires dictionary removed event whenever dictionary is removed from LanguageManager.
     *
     * @param dictionary removed dictionary
     */
    protected static void fireDictionaryRemoved ( final Dictionary dictionary )
    {
        synchronized ( languageListenersLock )
        {
            for ( final LanguageListener listener : languageListeners )
            {
                listener.dictionaryRemoved ( dictionary );
            }
            for ( final Map.Entry<JComponent, WeakReference<LanguageListener>> entry : componentLanguageListeners.entrySet () )
            {
                final JComponent key = entry.getKey ();
                if ( key != null )
                {
                    final WeakReference<LanguageListener> listenerReference = entry.getValue ();
                    final LanguageListener listener = listenerReference != null ? listenerReference.get () : null;
                    if ( listener != null )
                    {
                        listener.dictionaryRemoved ( dictionary );
                    }
                }
            }
        }
    }

    /**
     * Fires dictionaries cleared event whenever all dictionaries are removed from LanguageManager.
     */
    protected static void fireDictionariesCleared ()
    {
        synchronized ( languageListenersLock )
        {
            for ( final LanguageListener listener : languageListeners )
            {
                listener.dictionariesCleared ();
            }
            for ( final Map.Entry<JComponent, WeakReference<LanguageListener>> entry : componentLanguageListeners.entrySet () )
            {
                final JComponent key = entry.getKey ();
                if ( key != null )
                {
                    final WeakReference<LanguageListener> listenerReference = entry.getValue ();
                    final LanguageListener listener = listenerReference != null ? listenerReference.get () : null;
                    if ( listener != null )
                    {
                        listener.dictionariesCleared ();
                    }
                }
            }
        }
    }

    /**
     * Returns language key listeners.
     *
     * @return language key listeners
     */
    public static Map<String, List<LanguageKeyListener>> getLanguageKeyListeners ()
    {
        return languageKeyListeners;
    }

    /**
     * Adds language key listener.
     *
     * @param key      language key to register listener for
     * @param listener new language key listener
     */
    public static void addLanguageKeyListener ( final String key, final LanguageKeyListener listener )
    {
        synchronized ( languageKeyListenersLock )
        {
            List<LanguageKeyListener> listeners = languageKeyListeners.get ( key );
            if ( listeners == null )
            {
                listeners = new ArrayList<LanguageKeyListener> ();
                languageKeyListeners.put ( key, listeners );
            }
            listeners.add ( listener );
        }
    }

    /**
     * Removes language key listener
     *
     * @param listener language key listener to remove
     */
    public static void removeLanguageKeyListener ( final LanguageKeyListener listener )
    {
        synchronized ( languageKeyListenersLock )
        {
            for ( final Map.Entry<String, List<LanguageKeyListener>> entry : languageKeyListeners.entrySet () )
            {
                entry.getValue ().remove ( listener );
            }
        }
    }

    /**
     * Removes language key listener.
     *
     * @param key language key to remove listeners for
     */
    public static void removeLanguageKeyListeners ( final String key )
    {
        synchronized ( languageKeyListenersLock )
        {
            languageKeyListeners.remove ( key );
        }
    }

    /**
     * Fires language key updated event whenever specified dictionary key is updated with new translation.
     *
     * @param key updated language key
     */
    protected static void fireLanguageKeyUpdated ( final String key )
    {
        synchronized ( languageKeyListenersLock )
        {
            final List<LanguageKeyListener> listeners = languageKeyListeners.get ( key );
            if ( listeners != null )
            {
                final Value value = getValue ( key );
                for ( final LanguageKeyListener listener : CollectionUtils.copy ( listeners ) )
                {
                    listener.languageKeyUpdated ( key, value );
                }
            }
        }
    }

    /**
     * Fires language key updated event for all language keys.
     */
    protected static void fireAllLanguageKeysUpdated ()
    {
        synchronized ( languageKeyListenersLock )
        {
            for ( final Map.Entry<String, List<LanguageKeyListener>> entry : languageKeyListeners.entrySet () )
            {
                final Value value = getValue ( entry.getKey () );
                for ( final LanguageKeyListener listener : CollectionUtils.copy ( entry.getValue () ) )
                {
                    listener.languageKeyUpdated ( entry.getKey (), value );
                }
            }
        }
    }
}