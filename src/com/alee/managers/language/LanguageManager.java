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

import com.alee.laf.WebLookAndFeel;
import com.alee.managers.language.data.Dictionary;
import com.alee.managers.language.data.*;
import com.alee.managers.language.updaters.*;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.managers.tooltip.WebCustomTooltip;
import com.alee.utils.*;
import com.alee.utils.swing.AncestorAdapter;
import com.alee.utils.swing.DataProvider;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * This manager allows you to quickly setup changeable lanugage onto different components and to listen to application-wide language change
 * events. Language could be either loaded from structured xml files or added directly from the application by adding Dictionary type
 * objects into this manager.
 * <p/>
 * Be aware of the fact that all equal key-language pairs will be merged and collected into a global data map.
 * The original list of dictionaries will be preserved and will not be modified, but all translation will be taken from global data map.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-LanguageManager">How to use LanguageManager</a>
 */

public final class LanguageManager implements LanguageConstants
{
    /**
     * Unknown language icon.
     */
    public static final ImageIcon other = new ImageIcon ( LanguageManager.class.getResource ( "icons/lang/other.png" ) );

    /**
     * Supported languages operations synchronization object.
     */
    private static final Object supportedLanguagesLock = new Object ();

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
    private static final List<String> supportedLanguages =
            CollectionUtils.copy ( ENGLISH, RUSSIAN, POLISH, ARABIC, SPANISH, FRENCH, PORTUGUESE, GERMAN );

    /**
     * Default WebLaF language.
     * You can set different value here before initializing WebLaF to decrease amount of UI updates.
     * If possible default language is taken from system properties.
     */
    public static String DEFAULT = getDefaultLanguageKey ();

    /**
     * Currently used language.
     *
     * @see #getLanguage()
     * @see #setLanguage(String)
     */
    private static String language = DEFAULT;

    /**
     * Globally applied orientation.
     * LanguageManager controls this property because usually it is the language that affects default component orientation.
     *
     * @see #getOrientation()
     * @see #setOrientation(java.awt.ComponentOrientation)
     * @see #setOrientation(boolean)
     */
    private static ComponentOrientation orientation;

    /**
     * Default tooltip type used to display tooltips provided inside language files.
     * If exact tooltip type is not specified inside specific translation this type will be used.
     *
     * @see #getDefaultTooltipType()
     * @see #setDefaultTooltipType(com.alee.managers.language.data.TooltipType)
     */
    private static TooltipType defaultTooltipType = TooltipType.weblaf;

    /**
     * Language listeners operations synchronization object.
     */
    private static final Object languageListenersLock = new Object ();

    /**
     * Language changes listeners.
     *
     * @see LanguageListener
     * @see #getLanguageListeners()
     * @see #addLanguageListener(LanguageListener)
     * @see #removeLanguageListener(LanguageListener)
     */
    private static final List<LanguageListener> languageListeners = new ArrayList<LanguageListener> ();

    /**
     * Language changes listeners.
     *
     * @see LanguageListener
     * @see #getLanguageListeners()
     * @see #addLanguageListener(LanguageListener)
     * @see #removeLanguageListener(LanguageListener)
     */
    private static final Map<Component, LanguageListener> componentLanguageListeners = new WeakHashMap<Component, LanguageListener> ();

    /**
     * Language key listeners operations synchronization object.
     */
    private static final Object languageKeyListenersLock = new Object ();

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
    private static final Map<String, List<LanguageKeyListener>> languageKeyListeners = new HashMap<String, List<LanguageKeyListener>> ();

    /**
     * Global dictionary merged from all added dictionaries.
     * Used to store current dictionaries merge result.
     */
    private static Dictionary globalDictionary;

    /**
     * Global values map that contains merged translations for currently selected language.
     * It gets updated on any dictionaries change or global language change.
     */
    private static final Map<String, Value> globalCache = new HashMap<String, Value> ();

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
    private static final List<Dictionary> dictionaries = new ArrayList<Dictionary> ();

    /**
     * Component operations synchronization object.
     */
    private static final Object componentsLock = new Object ();

    /**
     * Components registered for auto-translation.
     * Specific implementations of LanguageUpdater interface used to translate them.
     *
     * @see #registerComponent(java.awt.Component, String, Object...)
     * @see #updateComponent(java.awt.Component, Object...)
     * @see #updateComponent(java.awt.Component, String, Object...)
     * @see #unregisterComponent(java.awt.Component)
     * @see #isRegisteredComponent(java.awt.Component)
     */
    private static final Map<Component, String> components = new WeakHashMap<Component, String> ();

    /**
     * Object data provided with component language key.
     * It is used to format the final translation string.
     *
     * @see #registerComponent(java.awt.Component, String, Object...)
     * @see #unregisterComponent(java.awt.Component)
     * @see #updateComponent(java.awt.Component, Object...)
     * @see #updateComponent(java.awt.Component, String, Object...)
     */
    private static final Map<Component, Object[]> componentsData = new WeakHashMap<Component, Object[]> ();

    /**
     * Calculated components cache map.
     * It is getting filled and updated automatically when key is requested.
     *
     * @see #updateComponentKey(java.awt.Component)
     */
    private static final Map<Component, String> componentKeysCache = new WeakHashMap<Component, String> ();

    /**
     * Components ancestor listeners used to update component keys cache.
     */
    private static final Map<Component, AncestorListener> componentsListeners = new WeakHashMap<Component, AncestorListener> ();

    /**
     * Language container operations synchronization object.
     */
    private static final Object languageContainersLock = new Object ();

    /**
     * Registered language containers.
     * Language containers are used to apply language prefix to all container childs with translation.
     * It is used for both manual and automatic translation through language updaters.
     *
     * @see #getLanguageContainerKey(java.awt.Container)
     * @see #registerLanguageContainer(java.awt.Container, String)
     * @see #unregisterLanguageContainer(java.awt.Container)
     * @see #combineWithContainerKeys(java.awt.Component, String)
     */
    private static final Map<Container, String> languageContainers = new WeakHashMap<Container, String> ();

    /**
     * LanguageUpdater operations synchronization object.
     */
    private static final Object updatersLock = new Object ();

    /**
     * Special comparator for sorting LanguageUpdaters list.
     */
    private static final LanguageUpdaterComparator languageUpdaterComparator = new LanguageUpdaterComparator ();

    /**
     * Registered language updaters.
     * Language updaters are used to automatically update specific components translation when language changes occur.
     *
     * @see #getLanguageUpdater(java.awt.Component)
     * @see #registerLanguageUpdater(com.alee.managers.language.updaters.LanguageUpdater)
     * @see #unregisterLanguageUpdater(com.alee.managers.language.updaters.LanguageUpdater)
     */
    private static final List<LanguageUpdater> updaters = new ArrayList<LanguageUpdater> ();

    /**
     * Component-specific language updaters.
     * These are used only for the components they are bound to.
     *
     * @see #getLanguageUpdater(java.awt.Component)
     * @see #registerLanguageUpdater(java.awt.Component, com.alee.managers.language.updaters.LanguageUpdater)
     * @see #unregisterLanguageUpdater(java.awt.Component)
     */
    private static final Map<Component, LanguageUpdater> customUpdaters = new WeakHashMap<Component, LanguageUpdater> ();

    /**
     * Language updaters cache by specific class types.
     * Used to improve LanguageUpdater retrieval speed for language requests.
     * This cache gets fully updated when any language updater is added or removed.
     */
    private static final Map<Class, LanguageUpdater> updatersCache = new HashMap<Class, LanguageUpdater> ();

    /**
     * Components custom WebLaF tooltips cache.
     * Used for proper tooltips disposal and update.
     */
    private static final Map<Component, List<WebCustomTooltip>> tooltipsCache = new WeakHashMap<Component, List<WebCustomTooltip>> ();

    /**
     * Language icons.
     * Supported language icons can be loaded without any effort as they are included in WebLaF library.
     * Custom language icons can be provided from the code using setLanguageIcon(String, javax.swing.ImageIcon) method.
     *
     * @see #setLanguageIcon(String, javax.swing.ImageIcon)
     */
    private static final Map<String, ImageIcon> languageIcons = new HashMap<String, ImageIcon> ();

    /**
     * Manager initialization mark.
     */
    private static boolean initialized = false;

    /**
     * Initializes LanguageManager settings.
     */
    public static void initialize ()
    {
        if ( !initialized )
        {
            initialized = true;

            // Initial language
            language = supportedLanguages.contains ( DEFAULT ) ? DEFAULT : ENGLISH;
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

            // Basic language updaters
            registerLanguageUpdater ( new JLabelLU () );
            registerLanguageUpdater ( new AbstractButtonLU () );
            registerLanguageUpdater ( new JTextComponentLU () );
            registerLanguageUpdater ( new JTabbedPaneLU () );
            registerLanguageUpdater ( new WebTextFieldLU () );
            registerLanguageUpdater ( new WebFormattedTextFieldLU () );
            registerLanguageUpdater ( new WebPasswordFieldLU () );
            registerLanguageUpdater ( new JProgressBarLU () );
            registerLanguageUpdater ( new JFileChooserLU () );
            registerLanguageUpdater ( new FrameLU () );
            registerLanguageUpdater ( new DialogLU () );
            registerLanguageUpdater ( new JInternalFrameLU () );
            registerLanguageUpdater ( new WebFileDropLU () );
            registerLanguageUpdater ( new WebCollapsiblePaneLU () );
            registerLanguageUpdater ( new WebDockableFrameLU () );

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

                private void updateAll ()
                {
                    // Notifying registered key listeners
                    if ( languageKeyListeners.size () > 0 )
                    {
                        fireAllLanguageKeysUpdated ();
                    }

                    // Updating registered components
                    updateAllComponents ();
                }

                private void updateSmart ( final Dictionary dictionary )
                {
                    // Gathering all changed keys
                    final List<String> relevantKeys = gatherKeys ( dictionary );

                    // Notifying registered key listeners
                    if ( languageKeyListeners.size () > 0 )
                    {
                        for ( final String key : relevantKeys )
                        {
                            fireLanguageKeyUpdated ( key );
                        }
                    }

                    // Updating relevant components
                    updateAllComponents ( relevantKeys );
                }

                private List<String> gatherKeys ( final Dictionary dictionary )
                {
                    final List<String> relevantKeys = new ArrayList<String> ();
                    gatherKeys ( dictionary, relevantKeys );
                    return relevantKeys;
                }

                private void gatherKeys ( final Dictionary dictionary, final List<String> relevantKeys )
                {
                    if ( dictionary.getRecords () != null )
                    {
                        for ( final Record record : dictionary.getRecords () )
                        {
                            relevantKeys.add ( record.getKey () );
                        }
                    }
                    if ( dictionary.getSubdictionaries () != null )
                    {
                        for ( final Dictionary subDictionary : dictionary.getSubdictionaries () )
                        {
                            gatherKeys ( subDictionary, relevantKeys );
                        }
                    }
                }
            } );

            // Default WebLaF dictionary
            loadDefaultDictionary ();
        }
    }

    /**
     * Loads default WebLaF dictionary.
     */
    public static void loadDefaultDictionary ()
    {
        LanguageManager.addDictionary ( WebLookAndFeel.class, "resources/language.xml" );
    }

    /**
     * Supported languages
     */

    public static List<String> getSupportedLanguages ()
    {
        synchronized ( supportedLanguagesLock )
        {
            return CollectionUtils.copy ( supportedLanguages );
        }
    }

    public static void setSupportedLanguages ( final Collection<String> supportedLanguages )
    {
        synchronized ( supportedLanguagesLock )
        {
            LanguageManager.supportedLanguages.clear ();
            LanguageManager.supportedLanguages.addAll ( supportedLanguages );
        }
    }

    public static void setSupportedLanguages ( final String... supportedLanguages )
    {
        synchronized ( supportedLanguagesLock )
        {
            LanguageManager.supportedLanguages.clear ();
            Collections.addAll ( LanguageManager.supportedLanguages, supportedLanguages );
        }
    }

    public static void addSupportedLanguage ( final String language )
    {
        synchronized ( supportedLanguagesLock )
        {
            supportedLanguages.add ( language );
        }
    }

    public static void addSupportedLanguage ( final String language, final Dictionary dictionary )
    {
        synchronized ( supportedLanguagesLock )
        {
            addDictionary ( dictionary );
            supportedLanguages.add ( language );
        }
    }

    public static void removeSupportedLanguage ( final String language )
    {
        synchronized ( supportedLanguagesLock )
        {
            supportedLanguages.remove ( language );
        }
    }

    public static void clearSupportedLanguages ()
    {
        synchronized ( supportedLanguagesLock )
        {
            supportedLanguages.clear ();
        }
    }

    /**
     * Components registration
     */

    public static void registerComponent ( final Component component, final String key, Object... data )
    {
        // Nullifying data if it has no values
        if ( data != null && data.length == 0 )
        {
            data = null;
        }

        synchronized ( componentsLock )
        {
            components.put ( component, key );
            if ( data != null )
            {
                componentsData.put ( component, data );
            }
        }

        updateComponent ( component, key );
        synchronized ( componentsLock )
        {
            if ( component instanceof JComponent )
            {
                final JComponent jComponent = ( JComponent ) component;
                final AncestorAdapter listener = new AncestorAdapter ()
                {
                    @Override
                    public void ancestorAdded ( final AncestorEvent event )
                    {
                        updateComponentKey ( component );
                    }
                };
                jComponent.addAncestorListener ( listener );
                componentsListeners.put ( component, listener );
            }
        }
    }

    public static void updateComponentsTree ( final Component component )
    {
        updateComponentKey ( component );
        if ( component instanceof Container )
        {
            for ( final Component child : ( ( Container ) component ).getComponents () )
            {
                updateComponentsTree ( child );
            }
        }
    }

    private static void updateComponentKey ( final Component component )
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

    public static void unregisterComponent ( final Component component )
    {
        synchronized ( componentsLock )
        {
            components.remove ( component );
            componentsData.remove ( component );
            if ( component instanceof JComponent )
            {
                final JComponent jComponent = ( JComponent ) component;
                final AncestorListener listener = componentsListeners.get ( jComponent );
                jComponent.removeAncestorListener ( listener );
                componentsListeners.remove ( component );
            }
        }
    }

    public static boolean isRegisteredComponent ( final Component component )
    {
        synchronized ( componentsLock )
        {
            return components.containsKey ( component );
        }
    }

    public static String getComponentKey ( final Component component )
    {
        synchronized ( componentsLock )
        {
            return components.get ( component );
        }
    }

    /**
     * Components language updaters registration
     */

    public static void registerLanguageUpdater ( final LanguageUpdater updater )
    {
        synchronized ( updatersLock )
        {
            updaters.add ( updater );
            updatersCache.clear ();
        }
    }

    public static void unregisterLanguageUpdater ( final LanguageUpdater updater )
    {
        synchronized ( updatersLock )
        {
            updaters.remove ( updater );
            updatersCache.clear ();
        }
    }

    public static void registerLanguageUpdater ( final Component component, final LanguageUpdater updater )
    {
        synchronized ( updatersLock )
        {
            customUpdaters.put ( component, updater );
        }
    }

    public static void unregisterLanguageUpdater ( final Component component )
    {
        synchronized ( updatersLock )
        {
            customUpdaters.remove ( component );
        }
    }

    public static LanguageUpdater getLanguageUpdater ( final Component component )
    {
        synchronized ( updatersLock )
        {
            // Checking custom updaters first
            final LanguageUpdater customUpdater = customUpdaters.get ( component );
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

                // Caching calculated updater
                updatersCache.put ( component.getClass (), updater );

                return updater;
            }
        }
    }

    /**
     * Components language update methods
     */

    public static void updateAllComponents ()
    {
        synchronized ( componentsLock )
        {
            for ( final Map.Entry<Component, String> entry : components.entrySet () )
            {
                updateComponent ( entry.getKey (), entry.getValue () );
            }
        }
    }

    public static void updateAllComponents ( final List<String> keys )
    {
        synchronized ( componentsLock )
        {
            for ( final Map.Entry<Component, String> entry : components.entrySet () )
            {
                if ( keys.contains ( entry.getValue () ) )
                {
                    updateComponent ( entry.getKey (), entry.getValue () );
                }
            }
        }
    }

    public static void updateComponent ( final Component component, final Object... data )
    {
        final String key = components.get ( component );
        if ( key != null )
        {
            updateComponent ( component, key, data );
        }
    }

    public static void updateComponent ( final Component component, final String key, Object... data )
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

        // Removing old cached tooltips
        final boolean swingComponent = component instanceof JComponent;
        if ( tooltipsCache.containsKey ( component ) )
        {
            // Clearing Swing tooltip
            if ( swingComponent )
            {
                ( ( JComponent ) component ).setToolTipText ( null );
            }

            // Clearing WebLaF tooltips
            TooltipManager.removeTooltips ( component, tooltipsCache.get ( component ) );
            tooltipsCache.get ( component ).clear ();
        }
        // Adding new tooltips
        if ( value != null && value.getTooltips () != null && value.getTooltips ().size () > 0 )
        {
            for ( final Tooltip tooltip : value.getTooltips () )
            {
                if ( tooltip.getType ().equals ( TooltipType.swing ) )
                {
                    if ( swingComponent )
                    {
                        ( ( JComponent ) component ).setToolTipText ( tooltip.getText () );
                    }
                }
                else
                {
                    if ( tooltip.getDelay () != null )
                    {
                        cacheTip ( TooltipManager.setTooltip ( component, tooltip.getText (), tooltip.getWay (), tooltip.getDelay () ) );
                    }
                    else
                    {
                        cacheTip ( TooltipManager.setTooltip ( component, tooltip.getText () ) );
                    }
                }
            }
        }
    }

    private static Object[] parseData ( final Object... data )
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

    private static void cacheTip ( final WebCustomTooltip tooltip )
    {
        final Component component = tooltip.getComponent ();

        // Creating array if it is needed
        if ( !tooltipsCache.containsKey ( component ) )
        {
            tooltipsCache.put ( component, new ArrayList<WebCustomTooltip> () );
        }

        // Updating cache
        tooltipsCache.get ( component ).add ( tooltip );
    }

    /**
     * Language icon
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
     * Default tooltip type
     */

    public static TooltipType getDefaultTooltipType ()
    {
        return defaultTooltipType;
    }

    public static void setDefaultTooltipType ( final TooltipType defaultTooltipType )
    {
        LanguageManager.defaultTooltipType = defaultTooltipType;
    }

    /**
     * Language methods
     */

    public static String getLanguage ()
    {
        return language;
    }

    public static boolean isCurrentLanguage ( final String language )
    {
        return LanguageManager.language.equals ( language );
    }

    public static void setLanguage ( final String language )
    {
        if ( initialized )
        {
            // Ignore incorrect and pointless changes
            if ( language == null || getLanguage ().trim ().toLowerCase ().equals ( language.trim ().toLowerCase () ) )
            {
                return;
            }

            // Saving old orientation for update optimizations
            final ComponentOrientation oldComponentOrientation = getOrientation ();

            // Changing language
            final String oldLanguage = LanguageManager.language;
            LanguageManager.language = language;

            // Updating locale
            updateLocale ();

            // Updating global cache
            rebuildCache ();

            // Updating orientation
            if ( oldComponentOrientation.isLeftToRight () != getOrientation ().isLeftToRight () )
            {
                SwingUtils.updateGlobalOrientations ();
            }

            // Firing language change event
            fireLanguageChanged ( oldLanguage, language );
        }
        else
        {
            // Simply applying default language
            DEFAULT = language;
        }
    }

    public static void switchLanguage ()
    {
        if ( supportedLanguages.size () > 0 )
        {
            final int current = supportedLanguages.indexOf ( getLanguage () );
            setLanguage ( supportedLanguages.get ( ( current == -1 || current == supportedLanguages.size () - 1 ) ? 0 : current + 1 ) );
        }
    }

    private static void updateLocale ()
    {
        // Proper locale for language
        for ( final Locale locale : Locale.getAvailableLocales () )
        {
            if ( locale.getLanguage ().equals ( language ) )
            {
                Locale.setDefault ( locale );
                return;
            }
        }
        Locale.setDefault ( new Locale ( language ) );

        // todo In future, with JDK7+
        // Locale.setDefault ( Locale.forLanguageTag ( language ) );
    }

    /**
     * Global default orientation
     */

    public static boolean isLeftToRight ()
    {
        return getOrientation ().isLeftToRight ();
    }

    public static ComponentOrientation getOrientation ()
    {
        if ( orientation != null )
        {
            return orientation;
        }
        else
        {
            return ComponentOrientation.getOrientation ( Locale.getDefault () );
        }
    }

    /**
     * Returns orientation opposite to current global component orientation.
     *
     * @return orientation opposite to current global component orientation
     */
    public static ComponentOrientation getOppositeOrientation ()
    {
        return isLeftToRight () ? ComponentOrientation.RIGHT_TO_LEFT : ComponentOrientation.LEFT_TO_RIGHT;
    }

    public static void setOrientation ( final boolean leftToRight )
    {
        setOrientation ( leftToRight ? ComponentOrientation.LEFT_TO_RIGHT : ComponentOrientation.RIGHT_TO_LEFT );
    }

    public static void setOrientation ( final ComponentOrientation orientation )
    {
        LanguageManager.orientation = orientation;
        SwingUtils.updateGlobalOrientations ();
    }

    public static void changeOrientation ()
    {
        setOrientation ( !getOrientation ().isLeftToRight () );
    }

    /**
     * Global dictionary that aggregates all added dictionaries, records and values
     */

    public static Dictionary getGlobalDictionary ()
    {
        return globalDictionary;
    }

    /**
     * Loaded dictionaries
     */

    public static List<Dictionary> getDictionaries ()
    {
        return CollectionUtils.copy ( dictionaries );
    }

    /**
     * Loads dictionary from xml
     */

    public static Dictionary loadDictionary ( final Class nearClass, final String resource )
    {
        return loadDictionary ( nearClass.getResource ( resource ) );
    }

    public static Dictionary loadDictionary ( final URL url )
    {
        return XmlUtils.fromXML ( url );
    }

    public static Dictionary loadDictionary ( final String path )
    {
        return loadDictionary ( new File ( path ) );
    }

    public static Dictionary loadDictionary ( final File file )
    {
        return XmlUtils.fromXML ( file );
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
        return addDictionary ( loadDictionary ( nearClass, resource ) );
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
        return addDictionary ( loadDictionary ( url ) );
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
        return addDictionary ( loadDictionary ( path ) );
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
        return addDictionary ( loadDictionary ( file ) );
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
        // Removing dictionary with the same ID first
        if ( isDictionaryAdded ( dictionary ) )
        {
            removeDictionary ( dictionary );
        }

        // Updating dictionaries
        dictionaries.add ( dictionary );

        // Updating global dictionary
        mergeDictionary ( dictionary );

        // Updating global cache
        updateCache ( dictionary );

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

            // Updating dictionaries
            dictionaries.remove ( dictionary );
            for ( final Dictionary d : dictionaries )
            {
                mergeDictionary ( d );
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

    private static void mergeDictionary ( final Dictionary dictionary )
    {
        mergeDictionary ( dictionary.getPrefix (), dictionary );
    }

    private static void mergeDictionary ( String prefix, final Dictionary dictionary )
    {
        // Determining prefix
        prefix = prefix != null && !prefix.equals ( "" ) ? prefix + "." : "";

        // Merging current level records
        if ( dictionary.getRecords () != null )
        {
            for ( final Record record : dictionary.getRecords () )
            {
                final Record clone = record.clone ();
                clone.setKey ( prefix + clone.getKey () );
                globalDictionary.addRecord ( clone );
            }
        }

        // Merging language information data
        if ( dictionary.getLanguageInfos () != null )
        {
            for ( final LanguageInfo info : dictionary.getLanguageInfos () )
            {
                globalDictionary.addLanguageInfo ( info );
            }
        }

        // Parsing subdictionaries
        if ( dictionary.getSubdictionaries () != null )
        {
            for ( final Dictionary subDictionary : dictionary.getSubdictionaries () )
            {
                final String sp = subDictionary.getPrefix ();
                final String subPrefix = prefix + ( sp != null && !sp.equals ( "" ) ? sp : "" );
                mergeDictionary ( subPrefix, subDictionary );
            }
        }
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
     * Value request methods
     */

    public static String get ( final String key )
    {
        final Value value = getValue ( key );
        return value != null ? value.getText () : key;
    }

    public static String get ( final String key, final Object... data )
    {
        final String text = get ( key );
        final Object[] actualData = parseData ( data );
        return String.format ( text, actualData );
    }

    public static Character getMnemonic ( final String key )
    {
        final Value value = getValue ( key );
        return value != null ? value.getMnemonic () : null;
    }

    public static Value getValue ( final String key )
    {
        // Global cache might be null when LanguageManager is not initialized
        return globalCache != null ? globalCache.get ( key ) : null;
    }

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
     * Component value request methods
     */

    public static String get ( final Component component, final String key )
    {
        return get ( combineWithContainerKeys ( component, key ) );
    }

    public static Character getMnemonic ( final Component component, final String key )
    {
        return getMnemonic ( combineWithContainerKeys ( component, key ) );
    }

    public static Value getValue ( final Component component, final String key )
    {
        return getValue ( combineWithContainerKeys ( component, key ) );
    }

    public static Value getNotNullValue ( final Component component, final String key )
    {
        return getNotNullValue ( combineWithContainerKeys ( component, key ) );
    }

    public static Value getValue ( final Component component, final String key, final String additionalKey )
    {
        return getValue ( combineWithContainerKeys ( component, key ) + "." + additionalKey );
    }

    public static Value getNotNullValue ( final Component component, final String key, final String additionalKey )
    {
        return getNotNullValue ( combineWithContainerKeys ( component, key ) + "." + additionalKey );
    }

    /**
     * Language container methods
     */

    public static String combineWithContainerKeys ( final Component component, final String key )
    {
        final String cachedKey = componentKeysCache.get ( component );
        return cachedKey != null ? cachedKey : combineWithContainerKeysImpl ( component, key );
    }

    private static String combineWithContainerKeysImpl ( final Component component, final String key )
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

    public static void registerLanguageContainer ( final Container container, final String key )
    {
        synchronized ( languageContainersLock )
        {
            languageContainers.put ( container, key );
        }
    }

    public static void unregisterLanguageContainer ( final Container container )
    {
        synchronized ( languageContainersLock )
        {
            languageContainers.remove ( container );
        }
    }

    public static String getLanguageContainerKey ( final Container container )
    {
        synchronized ( languageContainersLock )
        {
            return languageContainers.get ( container );
        }
    }

    /**
     * Cache operations
     */

    private static void rebuildCache ()
    {
        clearCache ();
        updateCache ( globalDictionary );
    }

    private static void clearCache ()
    {
        globalCache.clear ();
    }

    private static void updateCache ( final Dictionary dictionary )
    {
        updateCache ( dictionary.getPrefix (), dictionary );
    }

    private static void updateCache ( String prefix, final Dictionary dictionary )
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

        // Parsing subdictionaries
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
     * Default system language key
     */

    public static String getDefaultLanguageKey ()
    {
        final String systemLang = getSystemLanguageKey ();
        return supportedLanguages.contains ( systemLang ) ? systemLang : ENGLISH;
    }

    public static String getSystemLanguageKey ()
    {
        return System.getProperty ( "user.language" );
    }

    /**
     * Language listeners operations
     */

    public static List<LanguageListener> getLanguageListeners ()
    {
        synchronized ( languageListenersLock )
        {
            return CollectionUtils.copy ( languageListeners );
        }
    }

    public static void addLanguageListener ( final LanguageListener listener )
    {
        synchronized ( languageListenersLock )
        {
            languageListeners.add ( listener );
        }
    }

    public static void removeLanguageListener ( final LanguageListener listener )
    {
        synchronized ( languageListenersLock )
        {
            languageListeners.remove ( listener );
        }
    }

    public static Map<Component, LanguageListener> getComponentLanguageListeners ()
    {
        synchronized ( languageListenersLock )
        {
            return MapUtils.copyMap ( componentLanguageListeners );
        }
    }

    public static void addLanguageListener ( final Component component, final LanguageListener listener )
    {
        synchronized ( languageListenersLock )
        {
            componentLanguageListeners.put ( component, listener );
        }
    }

    public static void removeLanguageListener ( final Component component )
    {
        synchronized ( languageListenersLock )
        {
            componentLanguageListeners.remove ( component );
        }
    }

    private static void fireLanguageChanged ( final String oldLang, final String newLang )
    {
        synchronized ( languageListenersLock )
        {
            for ( final LanguageListener listener : languageListeners )
            {
                listener.languageChanged ( oldLang, newLang );
            }
            for ( final Map.Entry<Component, LanguageListener> entry : componentLanguageListeners.entrySet () )
            {
                final Component key = entry.getKey ();
                if ( key != null )
                {
                    final LanguageListener value = entry.getValue ();
                    if ( value != null )
                    {
                        value.languageChanged ( oldLang, newLang );
                    }
                }
            }
        }
    }

    private static void fireDictionaryAdded ( final Dictionary dictionary )
    {
        synchronized ( languageListenersLock )
        {
            for ( final LanguageListener listener : languageListeners )
            {
                listener.dictionaryAdded ( dictionary );
            }
            for ( final Map.Entry<Component, LanguageListener> entry : componentLanguageListeners.entrySet () )
            {
                final Component key = entry.getKey ();
                if ( key != null )
                {
                    final LanguageListener value = entry.getValue ();
                    if ( value != null )
                    {
                        value.dictionaryAdded ( dictionary );
                    }
                }
            }
        }
    }

    private static void fireDictionaryRemoved ( final Dictionary dictionary )
    {
        synchronized ( languageListenersLock )
        {
            for ( final LanguageListener listener : languageListeners )
            {
                listener.dictionaryRemoved ( dictionary );
            }
            for ( final Map.Entry<Component, LanguageListener> entry : componentLanguageListeners.entrySet () )
            {
                final Component key = entry.getKey ();
                if ( key != null )
                {
                    final LanguageListener value = entry.getValue ();
                    if ( value != null )
                    {
                        value.dictionaryRemoved ( dictionary );
                    }
                }
            }
        }
    }

    private static void fireDictionariesCleared ()
    {
        synchronized ( languageListenersLock )
        {
            for ( final LanguageListener listener : languageListeners )
            {
                listener.dictionariesCleared ();
            }
            for ( final Map.Entry<Component, LanguageListener> entry : componentLanguageListeners.entrySet () )
            {
                final Component key = entry.getKey ();
                if ( key != null )
                {
                    final LanguageListener value = entry.getValue ();
                    if ( value != null )
                    {
                        value.dictionariesCleared ();
                    }
                }
            }
        }
    }

    /**
     * Language key listeners operations
     */

    public static Map<String, List<LanguageKeyListener>> getLanguageKeyListeners ()
    {
        return languageKeyListeners;
    }

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

    public static void removeLanguageKeyListeners ( final String key )
    {
        synchronized ( languageKeyListenersLock )
        {
            languageKeyListeners.remove ( key );
        }
    }

    private static void fireLanguageKeyUpdated ( final String key )
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

    private static void fireAllLanguageKeysUpdated ()
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