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
import com.alee.managers.language.data.*;
import com.alee.utils.CollectionUtils;
import com.alee.utils.XmlUtils;
import com.alee.utils.compare.Filter;
import com.alee.utils.swing.WeakComponentDataList;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * {@link LanguageManager} allows you to easily manage application language {@link Dictionary}s.
 * {@link Dictionary}s could be either loaded from structured xml files or added directly from the code.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-LanguageManager">How to use LanguageManager</a>
 * @see Language
 * @see Dictionary
 */
public final class LanguageManager
{
    /**
     * todo 1. Add option for strict country check for supported languages
     */

    /**
     * Simple filter for {@link #isSuportedLocale(Locale)}.
     */
    @NotNull
    private static final Filter<Locale> SUPPORTED_LOCALES_FILTER = new Filter<Locale> ()
    {
        @Override
        public boolean accept ( final Locale locale )
        {
            return isSuportedLocale ( locale );
        }
    };

    /**
     * Globally used {@link Language}.
     */
    @Nullable
    private static Language language;

    /**
     * {@link Dictionary} containing all registered {@link Dictionary}s.
     * It is used as a global caching layer since each {@link Dictionary} has its own cache.
     */
    @Nullable
    private static Dictionary dictionaries;

    /**
     * {@link Language} changes listeners.
     *
     * @see LanguageListener
     * @see #addLanguageListener(LanguageListener)
     * @see #removeLanguageListener(LanguageListener)
     */
    @NotNull
    private static final List<LanguageListener> languageListeners = new ArrayList<LanguageListener> ();

    /**
     * {@link Language} changes listeners registered for specific {@link JComponent}s.
     *
     * @see LanguageListener
     * @see #addLanguageListener(JComponent, LanguageListener)
     * @see #removeLanguageListener(JComponent, LanguageListener)
     */
    @NotNull
    private static final WeakComponentDataList<JComponent, LanguageListener> componentLanguageListeners =
            new WeakComponentDataList<JComponent, LanguageListener> ( "LanguageManager.LanguageListener", 5 );

    /**
     * {@link Dictionary} changes listeners.
     *
     * @see LanguageListener
     * @see #addDictionaryListener(DictionaryListener)
     * @see #removeDictionaryListener(DictionaryListener)
     */
    @NotNull
    private static final List<DictionaryListener> dictionaryListeners = new ArrayList<DictionaryListener> ();

    /**
     * {@link Dictionary} changes listeners registered for specific {@link JComponent}s.
     *
     * @see LanguageListener
     * @see #addDictionaryListener(JComponent, DictionaryListener)
     * @see #removeDictionaryListener(JComponent, DictionaryListener)
     */
    @NotNull
    private static final WeakComponentDataList<JComponent, DictionaryListener> componentDictionaryListeners =
            new WeakComponentDataList<JComponent, DictionaryListener> ( "LanguageManager.DictionaryListener", 5 );

    /**
     * Manager initialization mark.
     *
     * @see #initialize()
     */
    private static boolean initialized = false;

    /**
     * Initializes LanguageManager settings.
     */
    public static synchronized void initialize ()
    {
        if ( !initialized )
        {
            // Class aliases
            XmlUtils.processAnnotations ( Dictionary.class );
            XmlUtils.processAnnotations ( TranslationInformation.class );
            XmlUtils.processAnnotations ( Record.class );
            XmlUtils.processAnnotations ( Value.class );
            XmlUtils.processAnnotations ( Text.class );

            // Initializing global dictionary
            dictionaries = new Dictionary ( "", "Global dictionary" );

            // Marking initialized
            initialized = true;

            // Adding core module dictionary
            addDictionary ( new Dictionary ( LanguageManager.class, "resources/core-language.xml" ) );
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
            throw new LanguageException ( "LanguageManager must be initialized first" );
        }
    }

    /**
     * Returns {@link Dictionary} containing all registered {@link Dictionary}s.
     *
     * @return {@link Dictionary} containing all registered {@link Dictionary}s
     */
    @NotNull
    public static Dictionary getDictionaries ()
    {
        // Must be initialized
        mustBeInitialized ();

        // Ensure global dictionary is available
        if ( dictionaries == null )
        {
            throw new LanguageException ( "LanguageManager failed to initialized correctly" );
        }
        return dictionaries;
    }

    /**
     * Returns {@link Language} title based on {@link TranslationInformation} provided within added {@link Dictionary}s.
     *
     * @param language {@link Language} to get title for
     * @return {@link Language} title based on {@link TranslationInformation} provided within added {@link Dictionary}s
     */
    @NotNull
    public static String getLanguageTitle ( @NotNull final Language language )
    {
        return getLocaleTitle ( language.getLocale () );
    }

    /**
     * Returns {@link Locale} title based on {@link TranslationInformation} provided within added {@link Dictionary}s.
     * If no {@link TranslationInformation} can be found for {@link Locale} its default methods will be used to generate title.
     *
     * @param locale {@link Locale} to get title for
     * @return {@link Locale} title based on {@link TranslationInformation} provided within added {@link Dictionary}s
     */
    @NotNull
    public static String getLocaleTitle ( @NotNull final Locale locale )
    {
        // Must be initialized
        mustBeInitialized ();

        // Calculating preferred locale title
        final TranslationInformation info = getDictionaries ().getTranslation ( locale );
        return info != null ? info.getTitle () : LanguageUtils.toString ( locale );
    }

    /**
     * Returns {@link List} of all {@link Locale} from all {@link Dictionary}s.
     *
     * @return {@link List} of all {@link Locale} from all {@link Dictionary}s
     */
    @NotNull
    public synchronized List<Locale> getAllLocales ()
    {
        // Must be initialized
        mustBeInitialized ();

        // Collecting all locales
        return getDictionaries ().getAllLocales ();
    }

    /**
     * Returns {@link List} of {@link Locale}s that are supported by all {@link Dictionary}s.
     * Language codes are used to detect whether or not specific {@link Locale} is supported.
     * We can't say that for instance "en_US" {@link Locale} is not supported if we have "en" or "en_GB" translation available.
     * Even though it is not strict "supported" case it is much better than having fully distinct translation for each country.
     *
     * @return {@link List} of {@link Locale}s supported by all {@link Dictionary}s
     */
    @NotNull
    public static List<Locale> getSupportedLocales ()
    {
        // Must be initialized
        mustBeInitialized ();

        // Collecting all supported locales
        return getDictionaries ().getSupportedLocales ();
    }

    /**
     * Returns filtered {@link List} of {@link Locale}s that are supported by all {@link Dictionary}s.
     *
     * @param locales {@link List} of {@link Locale}s to filter
     * @return filtered {@link List} of {@link Locale}s that are supported by all {@link Dictionary}s
     */
    @NotNull
    public static List<Locale> getSupportedLocales ( @NotNull final List<Locale> locales )
    {
        // Must be initialized
        mustBeInitialized ();

        // Filtering out unsupported locales
        return CollectionUtils.filter ( locales, SUPPORTED_LOCALES_FILTER );
    }

    /**
     * Returns whether or not specified {@link Language} is supported.
     *
     * @param language {@link Language}
     * @return {@code true} if specified {@link Language} is supported, {@code false} otherwise
     */
    public static boolean isSuportedLanguage ( @NotNull final Language language )
    {
        // Must be initialized
        mustBeInitialized ();

        // Checking locale support
        return isSuportedLocale ( language.getLocale () );
    }

    /**
     * Returns whether or not specified {@link Locale} is supported.
     *
     * @param locale {@link Locale}
     * @return {@code true} if specified {@link Locale} is supported, {@code false} otherwise
     */
    public static boolean isSuportedLocale ( @NotNull final Locale locale )
    {
        // Must be initialized
        mustBeInitialized ();

        // Checking locale support by language
        // It is important to exclude country from the check
        boolean supported = false;
        for ( final Locale slocale : getSupportedLocales () )
        {
            if ( Objects.equals ( locale.getLanguage (), slocale.getLanguage () ) )
            {
                supported = true;
                break;
            }
        }
        return supported;
    }

    /**
     * Adds new {@link Dictionary}.
     * This method call may cause a lot of UI updates depending on amount of translations contained.
     * If added dictionary contains records with existing keys they will override previously added ones.
     *
     * @param dictionary dictionary to add
     */
    public static void addDictionary ( @NotNull final Dictionary dictionary )
    {
        // Must be initialized
        mustBeInitialized ();

        getDictionaries ().addDictionary ( dictionary );
        fireDictionaryAdded ( dictionary );
    }

    /**
     * Removes existing {@link Dictionary}.
     * This method call may cause a lot of UI updates depending on amount of translations contained.
     *
     * @param dictionary dictionary to remove
     */
    public static void removeDictionary ( @NotNull final Dictionary dictionary )
    {
        // Must be initialized
        mustBeInitialized ();

        getDictionaries ().removeDictionary ( dictionary );
        fireDictionaryRemoved ( dictionary );
    }

    /**
     * Removes all added dictionaries including WebLaF ones.
     * You can always restore WebLaF dictionary by calling loadDefaultDictionary () method in LanguageManager.
     */
    public static void clearDictionaries ()
    {
        // Must be initialized
        mustBeInitialized ();

        getDictionaries ().clearRecords ();
        getDictionaries ().clearDictionaries ();
        fireDictionariesCleared ();
    }

    /**
     * Returns currently used {@link Locale}.
     *
     * @return currently used {@link Locale}
     */
    @NotNull
    public static Locale getLocale ()
    {
        // Must be initialized
        mustBeInitialized ();

        return getLanguage ().getLocale ();
    }

    /**
     * Returns whether the specified {@link Locale} is currently used or not.
     *
     * @param locale {@link Locale} to check
     * @return true if the specified {@link Locale} is currently used, false otherwise
     */
    public static boolean isCurrentLocale ( @NotNull final Locale locale )
    {
        // Must be initialized
        mustBeInitialized ();

        return getLanguage ().getLocale ().equals ( locale );
    }

    /**
     * Sets currently used {@link Locale}.
     *
     * @param locale {@link Locale} to use
     */
    public static void setLocale ( @NotNull final Locale locale )
    {
        // Must be initialized
        mustBeInitialized ();

        // Updating language
        setLanguage ( new Language ( locale ) );
    }

    /**
     * Returns currently used {@link Language}.
     *
     * @return currently used {@link Language}
     */
    @NotNull
    public static Language getLanguage ()
    {
        // Must be initialized
        mustBeInitialized ();

        // Updating initial language
        // It is lazily initialized to increase possibility of all dictionaries being loaded by the time
        if ( language == null )
        {
            final Locale locale = Locale.getDefault ();
            if ( isSuportedLocale ( locale ) )
            {
                // Using JVM locale by default
                language = new Language ( locale );
            }
            else
            {
                // Checking supported locales
                final List<Locale> locales = getSupportedLocales ();
                if ( !locales.isEmpty () )
                {
                    // Using first locale from supported ones
                    language = new Language ( locales.get ( 0 ) );
                }
                else
                {
                    // Using fallback locale
                    language = new Language ( new Locale ( "en", "GB" ) );
                }
            }
        }
        return language;
    }

    /**
     * Returns whether the specified {@link Language} is currently used or not.
     *
     * @param language {@link Language} to check
     * @return true if the specified {@link Language} is currently used, false otherwise
     */
    public static boolean isCurrentLanguage ( @NotNull final Language language )
    {
        // Must be initialized
        mustBeInitialized ();

        // Comparing languages
        return Objects.equals ( language, getLanguage () );
    }

    /**
     * Sets currently used {@link Language}.
     *
     * @param language {@link Language} to use
     */
    public static void setLanguage ( @NotNull final Language language )
    {
        // Must be initialized
        mustBeInitialized ();

        // Ignore incorrect and pointless changes
        if ( !isCurrentLanguage ( language ) )
        {
            // Saving previous language
            final Language oldLanguage = getLanguage ();

            // Changing language
            LanguageManager.language = language;

            // Firing language change event
            fireLanguageChanged ( oldLanguage, language );
        }
    }

    /**
     * Adds new {@link LanguageListener}.
     *
     * @param listener {@link LanguageListener} to add
     */
    public static void addLanguageListener ( @NotNull final LanguageListener listener )
    {
        synchronized ( languageListeners )
        {
            languageListeners.add ( listener );
        }
    }

    /**
     * Removes {@link LanguageListener}.
     *
     * @param listener {@link LanguageListener} to remove
     */
    public static void removeLanguageListener ( @NotNull final LanguageListener listener )
    {
        synchronized ( languageListeners )
        {
            languageListeners.remove ( listener );
        }
    }

    /**
     * Adds new {@link LanguageListener}.
     *
     * @param component {@link JComponent} to add {@link LanguageListener} for
     * @param listener  {@link LanguageListener} to add
     */
    public static void addLanguageListener ( @NotNull final JComponent component, @NotNull final LanguageListener listener )
    {
        componentLanguageListeners.add ( component, listener );
    }

    /**
     * Removes {@link LanguageListener}.
     *
     * @param component {@link JComponent} to remove {@link LanguageListener} from
     * @param listener  {@link LanguageListener} to remove
     */
    public static void removeLanguageListener ( @NotNull final JComponent component, @NotNull final LanguageListener listener )
    {
        componentLanguageListeners.remove ( component, listener );
    }

    /**
     * Fires {@link Language} changed event whenever current {@link Language} changes.
     *
     * @param oldLanguage old {@link Language}
     * @param newLanguage new {@link Language}
     */
    private static void fireLanguageChanged ( @NotNull final Language oldLanguage, @NotNull final Language newLanguage )
    {
        componentLanguageListeners.forEachData ( new BiConsumer<JComponent, LanguageListener> ()
        {
            @Override
            public void accept ( final JComponent component, final LanguageListener listener )
            {
                listener.languageChanged ( oldLanguage, newLanguage );
            }
        } );
        synchronized ( languageListeners )
        {
            for ( final LanguageListener listener : CollectionUtils.copy ( languageListeners ) )
            {
                listener.languageChanged ( oldLanguage, newLanguage );
            }
        }
    }

    /**
     * Adds new {@link DictionaryListener}.
     *
     * @param listener {@link DictionaryListener} to add
     */
    public static void addDictionaryListener ( @NotNull final DictionaryListener listener )
    {
        synchronized ( dictionaryListeners )
        {
            dictionaryListeners.add ( listener );
        }
    }

    /**
     * Removes {@link DictionaryListener}.
     *
     * @param listener {@link DictionaryListener} to remove
     */
    public static void removeDictionaryListener ( @NotNull final DictionaryListener listener )
    {
        synchronized ( dictionaryListeners )
        {
            dictionaryListeners.remove ( listener );
        }
    }

    /**
     * Adds new {@link LanguageListener}.
     *
     * @param component {@link JComponent} to add {@link LanguageListener} for
     * @param listener  {@link LanguageListener} to add
     */
    public static void addDictionaryListener ( @NotNull final JComponent component, @NotNull final DictionaryListener listener )
    {
        componentDictionaryListeners.add ( component, listener );
    }

    /**
     * Removes {@link LanguageListener}.
     *
     * @param component {@link JComponent} to remove {@link LanguageListener} from
     * @param listener  {@link LanguageListener} to remove
     */
    public static void removeDictionaryListener ( @NotNull final JComponent component, @NotNull final DictionaryListener listener )
    {
        componentDictionaryListeners.remove ( component, listener );
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
            public void accept ( final JComponent component, final DictionaryListener listener )
            {
                listener.dictionaryAdded ( dictionary );
            }
        } );
        synchronized ( dictionaryListeners )
        {
            for ( final DictionaryListener listener : CollectionUtils.copy ( dictionaryListeners ) )
            {
                listener.dictionaryAdded ( dictionary );
            }
        }
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
            public void accept ( final JComponent component, final DictionaryListener listener )
            {
                listener.dictionaryRemoved ( dictionary );
            }
        } );
        synchronized ( dictionaryListeners )
        {
            for ( final DictionaryListener listener : CollectionUtils.copy ( dictionaryListeners ) )
            {
                listener.dictionaryRemoved ( dictionary );
            }
        }
    }

    /**
     * Fires {@link Dictionary}s cleared event whenever all {@link Dictionary}s are removed.
     */
    private static void fireDictionariesCleared ()
    {
        componentDictionaryListeners.forEachData ( new BiConsumer<JComponent, DictionaryListener> ()
        {
            @Override
            public void accept ( final JComponent component, final DictionaryListener listener )
            {
                listener.dictionariesCleared ();
            }
        } );
        synchronized ( dictionaryListeners )
        {
            for ( final DictionaryListener listener : CollectionUtils.copy ( dictionaryListeners ) )
            {
                listener.dictionariesCleared ();
            }
        }
    }
}