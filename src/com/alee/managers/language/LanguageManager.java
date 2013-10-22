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
import com.alee.utils.CollectionUtils;
import com.alee.utils.CompareUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.XmlUtils;
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
 * Be aware of the fact that all equal key-language pairs will be merged and collected into a single data map.
 *
 * @author Mikle Garin
 */

public final class LanguageManager implements LanguageConstants
{
    // Icons
    public static final ImageIcon other = new ImageIcon ( LanguageManager.class.getResource ( "icons/lang/other.png" ) );

    // Supported languages list
    private static final Object supportedLanguagesLock = new Object ();
    private static List<String> supportedLanguages =
            CollectionUtils.copy ( ENGLISH, RUSSIAN, POLISH, ARABIC, SPANISH, FRENCH, PORTUGUESE, GERMAN );

    // Default language
    public static String DEFAULT = getDefaultLanguageKey ();

    // Current language
    private static String language = DEFAULT;

    // todo Current country
    // private static String country;

    // Forced orientation
    private static ComponentOrientation orientation;

    // Default tooltip type
    private static TooltipType defaultTooltipType = TooltipType.weblaf;

    // Language listeners
    private static final Object languageListenersLock = new Object ();
    private static List<LanguageListener> languageListeners = new ArrayList<LanguageListener> ();

    // Language listeners
    private static final Object languageKeyListenersLock = new Object ();
    private static Map<String, List<LanguageKeyListener>> languageKeyListeners = new HashMap<String, List<LanguageKeyListener>> ();

    // Global dictionary that contains all of the entries and its cache
    private static Dictionary globalDictionary;
    private static Map<String, Value> globalCache = new HashMap<String, Value> ();

    // Global dictionary that contains all of the entries
    private static List<Dictionary> dictionaries = new ArrayList<Dictionary> ();

    // Registered components
    private static final Object componentsLock = new Object ();
    private static Map<Component, String> components = new WeakHashMap<Component, String> ();
    private static Map<Component, Object[]> componentsData = new WeakHashMap<Component, Object[]> ();
    private static Map<Component, String> componentKeysCache = new WeakHashMap<Component, String> ();
    private static Map<Component, AncestorListener> componentsListeners = new WeakHashMap<Component, AncestorListener> ();

    // Registered language containers
    private static final Object languageContainersLock = new Object ();
    private static Map<Container, String> languageContainers = new WeakHashMap<Container, String> ();

    // Registered updaters
    private static final LanguageUpdaterComparator languageUpdaterComparator = new LanguageUpdaterComparator ();
    private static final Object updatersLock = new Object ();
    private static List<LanguageUpdater> updaters = new ArrayList<LanguageUpdater> ();
    private static Map<Component, LanguageUpdater> customUpdaters = new WeakHashMap<Component, LanguageUpdater> ();
    private static Map<Class, LanguageUpdater> updatersCache = new HashMap<Class, LanguageUpdater> ();

    // Tooltips cache
    private static Map<Component, List<WebCustomTooltip>> tooltipsCache = new WeakHashMap<Component, List<WebCustomTooltip>> ();

    // Initialization mark
    private static boolean initialized = false;

    /**
     * Manager initialization
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
            registerLanguageUpdater ( new WebTextFieldLU () );
            registerLanguageUpdater ( new WebFormattedTextFieldLU () );
            registerLanguageUpdater ( new WebPasswordFieldLU () );
            registerLanguageUpdater ( new JProgressBarLU () );
            registerLanguageUpdater ( new JFileChooserLU () );
            registerLanguageUpdater ( new FrameLU () );
            registerLanguageUpdater ( new DialogLU () );
            registerLanguageUpdater ( new JInternalFrameLU () );
            registerLanguageUpdater ( new WebFileDropLU () );

            // Language listener for components update
            addLanguageListener ( new LanguageListener ()
            {
                @Override
                public void languageChanged ( String oldLang, String newLang )
                {
                    updateAll ();
                }

                @Override
                public void dictionaryAdded ( Dictionary dictionary )
                {
                    updateSmart ( dictionary );
                }

                @Override
                public void dictionaryRemoved ( Dictionary dictionary )
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

                private void updateSmart ( Dictionary dictionary )
                {
                    // Gathering all changed keys
                    final List<String> relevantKeys = gatherKeys ( dictionary );

                    // Notifying registered key listeners
                    if ( languageKeyListeners.size () > 0 )
                    {
                        for ( String key : relevantKeys )
                        {
                            fireLanguageKeyUpdated ( key );
                        }
                    }

                    // Updating relevant components
                    updateAllComponents ( relevantKeys );
                }

                private List<String> gatherKeys ( Dictionary dictionary )
                {
                    final List<String> relevantKeys = new ArrayList<String> ();
                    gatherKeys ( dictionary, relevantKeys );
                    return relevantKeys;
                }

                private void gatherKeys ( Dictionary dictionary, List<String> relevantKeys )
                {
                    if ( dictionary.getRecords () != null )
                    {
                        for ( Record record : dictionary.getRecords () )
                        {
                            relevantKeys.add ( record.getKey () );
                        }
                    }
                    if ( dictionary.getSubdictionaries () != null )
                    {
                        for ( Dictionary subDictionary : dictionary.getSubdictionaries () )
                        {
                            gatherKeys ( subDictionary, relevantKeys );
                        }
                    }
                }
            } );

            // Basic language file
            LanguageManager.addDictionary ( WebLookAndFeel.class, "resources/language.xml" );
        }
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

    public static void setSupportedLanguages ( Collection<String> supportedLanguages )
    {
        synchronized ( supportedLanguagesLock )
        {
            LanguageManager.supportedLanguages.clear ();
            LanguageManager.supportedLanguages.addAll ( supportedLanguages );
        }
    }

    public static void setSupportedLanguages ( String... supportedLanguages )
    {
        synchronized ( supportedLanguagesLock )
        {
            LanguageManager.supportedLanguages.clear ();
            Collections.addAll ( LanguageManager.supportedLanguages, supportedLanguages );
        }
    }

    public static void addSupportedLanguage ( String language )
    {
        synchronized ( supportedLanguagesLock )
        {
            supportedLanguages.add ( language );
        }
    }

    public static void addSupportedLanguage ( String language, Dictionary dictionary )
    {
        synchronized ( supportedLanguagesLock )
        {
            addDictionary ( dictionary );
            supportedLanguages.add ( language );
        }
    }

    public static void removeSupportedLanguage ( String language )
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
                    public void ancestorAdded ( AncestorEvent event )
                    {
                        updateComponentKey ( component );
                    }

                    @Override
                    public void ancestorMoved ( AncestorEvent event )
                    {
                        updateComponent ( component );
                    }
                };
                jComponent.addAncestorListener ( listener );
                componentsListeners.put ( component, listener );
            }
        }
    }

    public static void updateComponentsTree ( Component component )
    {
        updateComponentKey ( component );
        if ( component instanceof Container )
        {
            for ( Component child : ( ( Container ) component ).getComponents () )
            {
                updateComponentsTree ( child );
            }
        }
    }

    private static void updateComponentKey ( Component component )
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

    public static void unregisterComponent ( Component component )
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

    public static boolean isRegisteredComponent ( Component component )
    {
        synchronized ( componentsLock )
        {
            return components.containsKey ( component );
        }
    }

    public static String getComponentKey ( Component component )
    {
        synchronized ( componentsLock )
        {
            return components.get ( component );
        }
    }

    /**
     * Components language updaters registration
     */

    public static void registerLanguageUpdater ( LanguageUpdater updater )
    {
        synchronized ( updatersLock )
        {
            updaters.add ( updater );
            updatersCache.clear ();
        }
    }

    public static void unregisterLanguageUpdater ( LanguageUpdater updater )
    {
        synchronized ( updatersLock )
        {
            updaters.remove ( updater );
            updatersCache.clear ();
        }
    }

    public static void registerLanguageUpdater ( Component component, LanguageUpdater updater )
    {
        synchronized ( updatersLock )
        {
            customUpdaters.put ( component, updater );
        }
    }

    public static void unregisterLanguageUpdater ( Component component )
    {
        synchronized ( updatersLock )
        {
            customUpdaters.remove ( component );
        }
    }

    public static LanguageUpdater getLanguageUpdater ( Component component )
    {
        synchronized ( updatersLock )
        {
            // Checking custom updaters first
            LanguageUpdater customUpdater = customUpdaters.get ( component );
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
                for ( LanguageUpdater lu : updaters )
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
            for ( Map.Entry<Component, String> entry : components.entrySet () )
            {
                updateComponent ( entry.getKey (), entry.getValue () );
            }
        }
    }

    public static void updateAllComponents ( List<String> keys )
    {
        synchronized ( componentsLock )
        {
            for ( Map.Entry<Component, String> entry : components.entrySet () )
            {
                if ( keys.contains ( entry.getValue () ) )
                {
                    updateComponent ( entry.getKey (), entry.getValue () );
                }
            }
        }
    }

    public static void updateComponent ( Component component, Object... data )
    {
        final String key = components.get ( component );
        if ( key != null )
        {
            updateComponent ( component, key, data );
        }
    }

    public static void updateComponent ( Component component, String key, Object... data )
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
            for ( Tooltip tooltip : value.getTooltips () )
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

    private static Object[] parseData ( Object... data )
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

    private static void cacheTip ( WebCustomTooltip tooltip )
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

    private static Map<String, ImageIcon> languageIcons = new HashMap<String, ImageIcon> ();

    public static ImageIcon getLanguageIcon ( String lang )
    {
        if ( languageIcons.containsKey ( lang ) )
        {
            return languageIcons.get ( lang );
        }
        else
        {
            ImageIcon icon;
            try
            {
                URL res = LanguageManager.class.getResource ( "icons/lang/" + lang + ".png" );
                icon = new ImageIcon ( res );
            }
            catch ( Throwable e )
            {
                icon = other;
            }
            languageIcons.put ( lang, icon );
            return icon;
        }
    }

    /**
     * Default tooltip type
     */

    public static TooltipType getDefaultTooltipType ()
    {
        return defaultTooltipType;
    }

    public static void setDefaultTooltipType ( TooltipType defaultTooltipType )
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

    public static boolean isCurrentLanguage ( String language )
    {
        return LanguageManager.language.equals ( language );
    }

    public static void setLanguage ( String language )
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

    private static void updateLocale ()
    {
        // Proper locale for language
        for ( Locale locale : Locale.getAvailableLocales () )
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

    public static void setOrientation ( boolean leftToRight )
    {
        setOrientation ( leftToRight ? ComponentOrientation.LEFT_TO_RIGHT : ComponentOrientation.RIGHT_TO_LEFT );
    }

    public static void setOrientation ( ComponentOrientation orientation )
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
        return dictionaries;
    }

    /**
     * Loads dictionary from xml
     */

    public static Dictionary loadDictionary ( Class nearClass, String resource )
    {
        return loadDictionary ( nearClass.getResource ( resource ) );
    }

    public static Dictionary loadDictionary ( URL url )
    {
        return XmlUtils.fromXML ( url );
    }

    public static Dictionary loadDictionary ( String path )
    {
        return loadDictionary ( new File ( path ) );
    }

    public static Dictionary loadDictionary ( File file )
    {
        return XmlUtils.fromXML ( file );
    }

    /**
     * Dictionary change methods
     */

    public static void addDictionary ( Class nearClass, String resource )
    {
        addDictionary ( loadDictionary ( nearClass, resource ) );
    }

    public static void addDictionary ( URL url )
    {
        addDictionary ( loadDictionary ( url ) );
    }

    public static void addDictionary ( String path )
    {
        addDictionary ( loadDictionary ( path ) );
    }

    public static void addDictionary ( File file )
    {
        addDictionary ( loadDictionary ( file ) );
    }

    public static void addDictionary ( Dictionary dictionary )
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
    }

    public static void removeDictionary ( Dictionary dictionary )
    {
        removeDictionary ( dictionary.getId () );
    }

    public static void removeDictionary ( String id )
    {
        if ( isDictionaryAdded ( id ) )
        {
            final Dictionary dictionary = getDictionary ( id );

            // Clearing global dictionaries storage
            globalDictionary.clear ();

            // Updating dictionaries
            dictionaries.remove ( dictionary );
            for ( Dictionary d : dictionaries )
            {
                mergeDictionary ( d );
            }

            // Updating global cache
            rebuildCache ();

            // Firing removal event
            fireDictionaryRemoved ( dictionary );
        }
    }

    public static boolean isDictionaryAdded ( Dictionary dictionary )
    {
        return isDictionaryAdded ( dictionary.getId () );
    }

    public static boolean isDictionaryAdded ( String id )
    {
        for ( Dictionary dictionary : dictionaries )
        {
            if ( dictionary.getId ().equals ( id ) )
            {
                return true;
            }
        }
        return false;
    }

    public static Dictionary getDictionary ( String id )
    {
        for ( Dictionary dictionary : dictionaries )
        {
            if ( dictionary.getId ().equals ( id ) )
            {
                return dictionary;
            }
        }
        return null;
    }

    private static void mergeDictionary ( Dictionary dictionary )
    {
        mergeDictionary ( dictionary.getPrefix (), dictionary );
    }

    private static void mergeDictionary ( String prefix, Dictionary dictionary )
    {
        // Determining prefix
        prefix = prefix != null && !prefix.equals ( "" ) ? prefix + "." : "";

        // Parsing current level records
        if ( dictionary.getRecords () != null )
        {
            for ( Record record : dictionary.getRecords () )
            {
                final Record clone = record.clone ();
                clone.setKey ( prefix + clone.getKey () );
                globalDictionary.addRecord ( clone );
            }
        }

        // Parsing subdictionaries
        if ( dictionary.getSubdictionaries () != null )
        {
            for ( Dictionary subDictionary : dictionary.getSubdictionaries () )
            {
                final String sp = subDictionary.getPrefix ();
                String subPrefix = prefix + ( sp != null && !sp.equals ( "" ) ? sp : "" );
                mergeDictionary ( subPrefix, subDictionary );
            }
        }
    }

    public static void clearDictionaries ()
    {
        globalDictionary.clear ();
        dictionaries.clear ();
        clearCache ();
        fireDictionariesCleared ();
    }

    /**
     * Value request methods
     */

    public static String get ( String key )
    {
        final Value value = getValue ( key );
        return value != null ? value.getText () : key;
    }

    public static Character getMnemonic ( String key )
    {
        final Value value = getValue ( key );
        return value != null ? value.getMnemonic () : null;
    }

    public static Value getValue ( String key )
    {
        // Global cache might be null when LanguageManager is not initialized
        return globalCache != null ? globalCache.get ( key ) : null;
    }

    public static Value getNotNullValue ( String key )
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
     * Component value request methods
     */

    public static String get ( Component component, String key )
    {
        return get ( combineWithContainerKeys ( component, key ) );
    }

    public static Character getMnemonic ( Component component, String key )
    {
        return getMnemonic ( combineWithContainerKeys ( component, key ) );
    }

    public static Value getValue ( Component component, String key )
    {
        return getValue ( combineWithContainerKeys ( component, key ) );
    }

    public static Value getNotNullValue ( Component component, String key )
    {
        return getNotNullValue ( combineWithContainerKeys ( component, key ) );
    }

    /**
     * Language container methods
     */

    private static String combineWithContainerKeys ( final Component component, final String key )
    {
        final String cachedKey = componentKeysCache.get ( component );
        return cachedKey != null ? cachedKey : combineWithContainerKeysImpl ( component, key );
    }

    private static String combineWithContainerKeysImpl ( Component component, String key )
    {
        final String cachedKey;
        //        if ( key != null )
        //        {
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
        cachedKey = sb.toString ();
        //        }
        //        else
        //        {
        //            cachedKey = null;
        //        }
        componentKeysCache.put ( component, cachedKey );
        return cachedKey;
    }

    public static void registerLanguageContainer ( Container container, String key )
    {
        synchronized ( languageContainersLock )
        {
            languageContainers.put ( container, key );
        }
    }

    public static void unregisterLanguageContainer ( Container container )
    {
        synchronized ( languageContainersLock )
        {
            languageContainers.remove ( container );
        }
    }

    public static String getLanguageContainerKey ( Container container )
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

    private static void updateCache ( Dictionary dictionary )
    {
        updateCache ( dictionary.getPrefix (), dictionary );
    }

    private static void updateCache ( String prefix, Dictionary dictionary )
    {
        // Determining prefix
        prefix = prefix != null && !prefix.equals ( "" ) ? prefix + "." : "";

        // Parsing current level records
        if ( dictionary.getRecords () != null )
        {
            for ( Record record : dictionary.getRecords () )
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
            for ( Dictionary subDictionary : dictionary.getSubdictionaries () )
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

    public static void addLanguageListener ( LanguageListener listener )
    {
        synchronized ( languageListenersLock )
        {
            languageListeners.add ( listener );
        }
    }

    public static void removeLanguageListener ( LanguageListener listener )
    {
        synchronized ( languageListenersLock )
        {
            languageListeners.remove ( listener );
        }
    }

    private static void fireLanguageChanged ( String oldLang, String newLang )
    {
        synchronized ( languageListenersLock )
        {
            for ( LanguageListener listener : languageListeners )
            {
                listener.languageChanged ( oldLang, newLang );
            }
        }
    }

    private static void fireDictionaryAdded ( Dictionary dictionary )
    {
        synchronized ( languageListenersLock )
        {
            for ( LanguageListener listener : languageListeners )
            {
                listener.dictionaryAdded ( dictionary );
            }
        }
    }

    private static void fireDictionaryRemoved ( Dictionary dictionary )
    {
        synchronized ( languageListenersLock )
        {
            for ( LanguageListener listener : languageListeners )
            {
                listener.dictionaryRemoved ( dictionary );
            }
        }
    }

    private static void fireDictionariesCleared ()
    {
        synchronized ( languageListenersLock )
        {
            for ( LanguageListener listener : languageListeners )
            {
                listener.dictionariesCleared ();
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

    public static void addLanguageKeyListener ( String key, LanguageKeyListener listener )
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

    public static void removeLanguageKeyListener ( LanguageKeyListener listener )
    {
        synchronized ( languageKeyListenersLock )
        {
            for ( Map.Entry<String, List<LanguageKeyListener>> entry : languageKeyListeners.entrySet () )
            {
                entry.getValue ().remove ( listener );
            }
        }
    }

    public static void removeLanguageKeyListeners ( String key )
    {
        synchronized ( languageKeyListenersLock )
        {
            languageKeyListeners.remove ( key );
        }
    }

    private static void fireLanguageKeyUpdated ( String key )
    {
        synchronized ( languageKeyListenersLock )
        {
            final List<LanguageKeyListener> listeners = languageKeyListeners.get ( key );
            if ( listeners != null )
            {
                final Value value = getValue ( key );
                for ( LanguageKeyListener listener : CollectionUtils.copy ( listeners ) )
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
            for ( Map.Entry<String, List<LanguageKeyListener>> entry : languageKeyListeners.entrySet () )
            {
                final Value value = getValue ( entry.getKey () );
                for ( LanguageKeyListener listener : CollectionUtils.copy ( entry.getValue () ) )
                {
                    listener.languageKeyUpdated ( entry.getKey (), value );
                }
            }
        }
    }
}