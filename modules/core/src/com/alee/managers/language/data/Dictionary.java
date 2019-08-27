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

package com.alee.managers.language.data;

import com.alee.api.Identifiable;
import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
import com.alee.api.merge.Mergeable;
import com.alee.managers.language.LanguageUtils;
import com.alee.utils.CollectionUtils;
import com.alee.utils.TextUtils;
import com.alee.utils.XmlUtils;
import com.alee.utils.collection.ImmutableList;
import com.alee.utils.collection.ImmutableSet;
import com.alee.utils.compare.Filter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.*;

/**
 * {@link Dictionary} can store multiple language {@link Record}s and {@link Dictionary}s.
 * {@link Dictionary} is a main component used within {@link com.alee.managers.language.LanguageManager} to access actual translations.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-LanguageManager">How to use LanguageManager</a>
 * @see com.alee.managers.language.LanguageManager
 * @see com.alee.managers.language.Language
 * @see Record
 * @see TranslationInformation
 */
@XStreamAlias ( "Dictionary" )
public final class Dictionary implements Identifiable, Mergeable, Cloneable, Serializable
{
    /**
     * {@link Dictionary} identifier prefix.
     */
    @NotNull
    private static final String ID_PREFIX = "DIC";

    /**
     * Unique {@link Dictionary} identifier.
     * It is used to distinct {@link Dictionary} instances in runtime.
     */
    @NotNull
    private final transient String id;

    /**
     * {@link Dictionary} name.
     * This is optional infromation that can be missing.
     */
    @Nullable
    @XStreamAsAttribute
    private String name;

    /**
     * {@link Dictionary} key prefix.
     * Prefix is optional and will simply be ignored if {@code null} or blank.
     * If prefix is specified it will be added in the final language key to all {@link Record}s and sub-{@link Dictionary}s.
     */
    @Nullable
    @XStreamAsAttribute
    private String prefix;

    /**
     * {@link List} of {@link Record}s available in this {@link Dictionary}.
     * These are the main translation containers and provide all necessary information.
     * Though this {@link List} can be empty if this {@link Dictionary} only contains sub-{@link Dictionary}s.
     */
    @Nullable
    @XStreamImplicit ( itemFieldName = "record" )
    private List<Record> records;

    /**
     * {@link List} of sub-{@link Dictionary}s available in this {@link Dictionary}.
     * This {@link List} can be empty if this {@link Dictionary} only contains {@link Record}s.
     * Sub-{@link Dictionary}s can be provided to group nested {@link Record}s or simply for convenience.
     */
    @Nullable
    @XStreamImplicit ( itemFieldName = "Dictionary" )
    private List<Dictionary> dictionaries;

    /**
     * {@link List} of {@link TranslationInformation}s about translations available in this {@link Dictionary}.
     * This is optional infromation and it can be missing if not provided explicitely for this {@link Dictionary}.
     */
    @Nullable
    @XStreamAlias ( "Translations" )
    private List<TranslationInformation> translations;

    /**
     * Cached {@link List} of all {@link Locale}s presented by this {@link Dictionary}.
     */
    @Nullable
    private transient List<Locale> allLocales;

    /**
     * Cached {@link List} of all {@link Locale}s supported by this {@link Dictionary}.
     * {@link Locale} is supported only if all {@link Record}s within this {@link Dictionary} support it.
     */
    @Nullable
    private transient List<Locale> supportedLocales;

    /**
     * {@link Map} containing {@link Record}s cached by their key.
     */
    @Nullable
    private transient Map<String, Record> recordsCache;

    /**
     * {@link Map} containing {@link Dictionary}s cached by {@link Record} keys.
     */
    @Nullable
    private transient Map<String, Dictionary> dictionariesCache;

    /**
     * Constructs new {@link Dictionary}.
     */
    public Dictionary ()
    {
        this ( ( String ) null, null );
    }

    /**
     * Constructs new {@link Dictionary}.
     *
     * @param prefix {@link Dictionary} key prefix
     */
    public Dictionary ( @Nullable final String prefix )
    {
        this ( prefix, null );
    }

    /**
     * Constructs new {@link Dictionary}.
     *
     * @param prefix {@link Dictionary} key prefix
     * @param name   {@link Dictionary} name
     */
    public Dictionary ( @Nullable final String prefix, @Nullable final String name )
    {
        this.id = TextUtils.generateId ( ID_PREFIX );
        this.prefix = prefix;
        this.name = name;
    }

    /**
     * Loads {@link Dictionary} from the specified resource.
     *
     * @param nearClass {@link Class} used to locale resource
     * @param resource  resource to load {@link Dictionary} from
     */
    public Dictionary ( @NotNull final Class nearClass, @NotNull final String resource )
    {
        this.id = TextUtils.generateId ( ID_PREFIX );
        XmlUtils.getXStream ().fromXML ( nearClass.getResourceAsStream ( resource ), this );
    }

    /**
     * Loads {@link Dictionary} from the specified {@link URL}.
     *
     * @param url {@link URL} to load {@link Dictionary} from
     */
    public Dictionary ( @NotNull final URL url )
    {
        this.id = TextUtils.generateId ( ID_PREFIX );
        XmlUtils.getXStream ().fromXML ( url, this );
    }

    /**
     * Loads {@link Dictionary} from the specified {@link File}.
     *
     * @param file {@link File} to load {@link Dictionary} from
     */
    public Dictionary ( @NotNull final File file )
    {
        this.id = TextUtils.generateId ( ID_PREFIX );
        XmlUtils.getXStream ().fromXML ( file, this );
    }

    /**
     * Loads {@link Dictionary} from the specified {@link InputStream}.
     *
     * @param inputStream {@link InputStream} to load {@link Dictionary} from
     */
    public Dictionary ( @NotNull final InputStream inputStream )
    {
        this.id = TextUtils.generateId ( ID_PREFIX );
        XmlUtils.getXStream ().fromXML ( inputStream, this );
    }

    @NotNull
    @Override
    public String getId ()
    {
        return id;
    }

    /**
     * Returns {@link Dictionary} name.
     *
     * @return {@link Dictionary} name
     */
    @Nullable
    public String getName ()
    {
        return name;
    }

    /**
     * Sets {@link Dictionary} name.
     *
     * @param name new {@link Dictionary} name
     */
    public synchronized void setName ( @Nullable final String name )
    {
        this.name = name;
    }

    /**
     * Returns {@link Dictionary} prefix.
     *
     * @return {@link Dictionary} prefix
     */
    @Nullable
    public String getPrefix ()
    {
        return prefix;
    }

    /**
     * Sets {@link Dictionary} prefix.
     *
     * @param prefix new {@link Dictionary} prefix
     */
    public synchronized void setPrefix ( @Nullable final String prefix )
    {
        this.prefix = prefix;
    }

    /**
     * Returns amount of {@link Record}s in this {@link Dictionary}.
     * Note that this method doesn't count {@link Record}s from sub-{@link Dictionary}s.
     *
     * @return amount of {@link Record}s in this {@link Dictionary}
     */
    public synchronized int recordsCount ()
    {
        return records != null ? records.size () : 0;
    }

    /**
     * Returns total amount of {@link Record}s in this {@link Dictionary} and all sub-{@link Dictionary}s.
     *
     * @return total amount of {@link Record}s in this {@link Dictionary} and all sub-{@link Dictionary}s
     */
    public synchronized int totalRecordsCount ()
    {
        int count = records != null ? records.size () : 0;
        if ( CollectionUtils.notEmpty ( dictionaries ) )
        {
            for ( final Dictionary dictionary : dictionaries )
            {
                count += dictionary.totalRecordsCount ();
            }
        }
        return count;
    }

    /**
     * Returns {@link List} of {@link Record}s this {@link Dictionary} contains.
     *
     * @return {@link List} of {@link Record}s this {@link Dictionary} contains
     */
    @NotNull
    public synchronized List<Record> getRecords ()
    {
        return records != null ? new ImmutableList<Record> ( records ) : new ImmutableList<Record> ();
    }

    /**
     * Sets {@link List} of {@link Record}s for this {@link Dictionary}.
     *
     * @param records new {@link List} of {@link Record}s for this {@link Dictionary}
     */
    public synchronized void setRecords ( @Nullable final List<Record> records )
    {
        this.records = records;
    }

    /**
     * Returns {@link Record} for the specified language key.
     * Search will be perfomed in this {@link Dictionary} and all sub-{@link Dictionary}s.
     *
     * @param key    {@link Record} language key
     * @param locale {@link Locale}
     * @return {@link Record} for the specified language key
     */
    @Nullable
    public synchronized Record getRecord ( @NotNull final String key, @NotNull final Locale locale )
    {
        final Record result;
        final String cacheKey = key + "." + LanguageUtils.toString ( locale );
        if ( recordsCache != null && recordsCache.containsKey ( cacheKey ) )
        {
            // Cached record in this dictionary
            result = recordsCache.get ( cacheKey );
        }
        else
        {
            final String dicPrefix = usablePrefix ();
            final String subKey = key.startsWith ( dicPrefix ) ? key.substring ( dicPrefix.length () ) : null;
            if ( subKey != null )
            {
                if ( dictionariesCache != null && dictionariesCache.containsKey ( cacheKey ) )
                {
                    // Cached dictionary that contains record
                    result = dictionariesCache.get ( cacheKey ).getRecord ( subKey, locale );
                }
                else
                {
                    Record record = null;
                    Dictionary source = this;

                    final Comparator<Record> comparator = new RecordCountryComparator ( locale );

                    // Resolving most fitting record within this dictionary
                    if ( CollectionUtils.notEmpty ( records ) )
                    {
                        // Collecting records for the key
                        final List<Record> fitting = collectLocalRecords ( subKey, new ArrayList<Record> ( 3 ) );

                        // Resolving most fitting one
                        record = CollectionUtils.max ( fitting, comparator );
                    }

                    // Resolving most fitting record within this dictionary and all sub-dictionaries
                    if ( CollectionUtils.notEmpty ( dictionaries ) )
                    {
                        for ( final Dictionary dictionary : dictionaries )
                        {
                            // Resolving most fitting one from sub-dictionary
                            final Record subRecord = dictionary.getRecord ( subKey, locale );

                            // Resolving most fitting one
                            if ( subRecord != null && ( record == null || comparator.compare ( record, subRecord ) > 0 ) )
                            {
                                record = subRecord;
                                source = dictionary;
                            }
                        }
                    }
                    result = record;

                    // Caching result
                    if ( source == this )
                    {
                        if ( recordsCache == null )
                        {
                            recordsCache = new HashMap<String, Record> ( recordsCount () );
                        }
                        recordsCache.put ( cacheKey, result );
                    }
                    else
                    {
                        if ( dictionariesCache == null )
                        {
                            dictionariesCache = new HashMap<String, Dictionary> ( dictionariesCount () * 5 );
                        }
                        dictionariesCache.put ( cacheKey, source );
                    }
                }
            }
            else
            {
                // Key doesn't start with dictionary prefix
                result = null;
            }
        }
        return result;
    }

    /**
     * Returns {@link List} of {@link Record}s for the specified language key.
     * Without specific {@link Locale} multiple {@link Record}s might be returned if translations for the same key are spreaded.
     *
     * @param key {@link Record} language key
     * @return {@link List} of {@link Record}s for the specified language key
     */
    @NotNull
    private synchronized List<Record> getRecords ( @NotNull final String key )
    {
        return getRecords ( key, new ArrayList<Record> () );
    }

    /**
     * Returns {@link List} of {@link Record}s for the specified language key.
     * This method doesn't ask for {@link Locale}, so multiple {@link Record}s can be returned as a result.
     *
     * @param key     {@link Record} language key
     * @param results {@link List} to collect {@link Record}s into
     * @return {@link List} of {@link Record}s for the specified language key
     */
    @NotNull
    private synchronized List<Record> getRecords ( @NotNull final String key, @NotNull final List<Record> results )
    {
        final String dicPrefix = usablePrefix ();
        final String subKey = key.startsWith ( dicPrefix ) ? key.substring ( dicPrefix.length () ) : null;
        if ( subKey != null )
        {
            collectLocalRecords ( subKey, results );
            if ( CollectionUtils.notEmpty ( dictionaries ) )
            {
                for ( final Dictionary dictionary : dictionaries )
                {
                    dictionary.getRecords ( subKey, results );
                }
            }
        }
        return results;
    }

    /**
     * Returns {@link List} of {@link Record}s from this {@link Dictionary} only for the specified language key.
     * This method doesn't ask for {@link Locale}, so multiple {@link Record}s can be returned as a result.
     *
     * @param key     {@link Record} language key
     * @param results {@link List} to collect {@link Record}s into
     * @return {@link List} of {@link Record}s from this {@link Dictionary} only for the specified language key
     */
    @NotNull
    private synchronized List<Record> collectLocalRecords ( @NotNull final String key, @NotNull final List<Record> results )
    {
        if ( CollectionUtils.notEmpty ( records ) )
        {
            for ( final Record record : records )
            {
                if ( Objects.equals ( record.getKey (), key ) )
                {
                    results.add ( record );
                }
            }
        }
        return results;
    }

    /**
     * Adds new {@link Record} into this {@link Dictionary} and returns it.
     *
     * @param record {@link Record} to add
     */
    public synchronized void addRecord ( @NotNull final Record record )
    {
        if ( records == null )
        {
            records = new ArrayList<Record> ( 1 );
        }

        // Adding record
        records.add ( record );

        // Destroying caches
        destroyRecordCaches ( record );
    }

    /**
     * Removes {@link Record} from this {@link Dictionary}.
     *
     * @param record {@link Record} to remove
     */
    public synchronized void removeRecord ( @NotNull final Record record )
    {
        if ( records != null )
        {
            // Removing record
            records.remove ( record );

            // Destroying caches
            destroyRecordCaches ( record );
        }
    }

    /**
     * Removes {@link Record} with the specified key from this {@link Dictionary}.
     *
     * @param key key of {@link Record} to remove
     */
    public synchronized void removeRecord ( @NotNull final String key )
    {
        if ( CollectionUtils.notEmpty ( records ) )
        {
            final Iterator<Record> iterator = records.iterator ();
            while ( iterator.hasNext () )
            {
                final Record record = iterator.next ();
                if ( record.getKey ().equals ( key ) )
                {
                    // Removing record
                    iterator.remove ();

                    // Destroying caches
                    destroyRecordCaches ( record );

                    break;
                }
            }
        }
    }

    /**
     * Destroys caches for the specified {@link Dictionary}.
     *
     * @param record {@link Dictionary} to destroy caches for
     */
    private void destroyRecordCaches ( @NotNull final Record record )
    {
        // Clearing
        clearLocaleCaches ();
        if ( recordsCache != null || dictionariesCache != null )
        {
            final Set<String> keys = new ImmutableSet<String> ( record.getKey () );
            if ( recordsCache != null )
            {
                destroyKeys ( recordsCache.keySet ().iterator (), keys );
            }
            if ( dictionariesCache != null )
            {
                destroyKeys ( dictionariesCache.keySet ().iterator (), keys );
            }
        }
    }

    /**
     * Removes all {@link Record}s from this {@link Dictionary}.
     */
    public synchronized void clearRecords ()
    {
        if ( records != null )
        {
            // Removing all records
            records.clear ();
            records = null;

            // Destroying caches
            destroyRecordCaches ();
        }
    }

    /**
     * Destroys caches all {@link Record} caches.
     */
    private void destroyRecordCaches ()
    {
        clearLocaleCaches ();
        if ( recordsCache != null )
        {
            recordsCache.clear ();
            recordsCache = null;
        }
    }

    /**
     * Returns {@link Set} of all {@link Record} keys for this {@link Dictionary}.
     * This will include all keys for {@link Record}s in sub-{@link Dictionary}s.
     *
     * @return {@link Set} of all {@link Record} keys for this {@link Dictionary}
     */
    @NotNull
    public synchronized Set<String> getKeys ()
    {
        return getKeys ( "" );
    }

    /**
     * Returns {@link Set} of all {@link Record} keys for this {@link Dictionary}.
     * This will include all keys for {@link Record}s in sub-{@link Dictionary}s.
     *
     * @param prefix hierarchy prefix
     * @return {@link Set} of all {@link Record} keys for this {@link Dictionary}
     */
    @NotNull
    private Set<String> getKeys ( @NotNull final String prefix )
    {
        return collectKeys ( prefix, new HashSet<String> ( totalRecordsCount () ) );
    }

    /**
     * Returns {@link Set} of all {@link Record} keys for this {@link Dictionary}.
     * This will include all keys for {@link Record}s in sub-{@link Dictionary}s.
     *
     * @param prefix hierarchy prefix
     * @param keys   {@link Set} to collect {@link Record} keys into
     * @return {@link Set} of all {@link Record} keys for this {@link Dictionary}
     */
    @NotNull
    private Set<String> collectKeys ( @NotNull final String prefix, @NotNull final Set<String> keys )
    {
        final String p = prefix + usablePrefix ();
        if ( records != null )
        {
            for ( final Record record : records )
            {
                keys.add ( p + record.getKey () );
            }
        }
        if ( dictionaries != null )
        {
            for ( final Dictionary dictionary : dictionaries )
            {
                dictionary.collectKeys ( p, keys );
            }
        }
        return keys;
    }

    /**
     * Returns amount of sub-{@link Dictionary}s in this {@link Dictionary}.
     *
     * @return amount of sub-{@link Dictionary}s in this {@link Dictionary}
     */
    public synchronized int dictionariesCount ()
    {
        return dictionaries != null ? dictionaries.size () : 0;
    }

    /**
     * Returns {@link List} of {@link Dictionary}s this {@link Dictionary} contains.
     *
     * @return {@link List} of {@link Dictionary}s this {@link Dictionary} contains
     */
    @NotNull
    public synchronized List<Dictionary> getDictionaries ()
    {
        return dictionaries != null ? new ImmutableList<Dictionary> ( dictionaries ) : new ImmutableList<Dictionary> ();
    }

    /**
     * Sets {@link List} of {@link Dictionary}s for this {@link Dictionary}.
     *
     * @param dictionaries new {@link List} of {@link Dictionary}s for this {@link Dictionary}
     */
    public synchronized void setDictionaries ( @Nullable final List<Dictionary> dictionaries )
    {
        this.dictionaries = dictionaries;
    }

    /**
     * Adds new child {@link Dictionary}.
     *
     * @param dictionary child {@link Dictionary} to add
     */
    public synchronized void addDictionary ( @NotNull final Dictionary dictionary )
    {
        // Ensuring dictionaries list exists
        if ( dictionaries == null )
        {
            dictionaries = new ArrayList<Dictionary> ();
        }

        // Adding dictionary
        dictionaries.add ( dictionary );

        // Destroying caches
        destroyDictionaryCaches ( dictionary );
    }

    /**
     * Removes child {@link Dictionary}.
     *
     * @param dictionary child {@link Dictionary} to remove
     */
    public synchronized void removeDictionary ( @NotNull final Dictionary dictionary )
    {
        if ( dictionaries != null )
        {
            // Removing dictionary
            dictionaries.remove ( dictionary );

            // Destroying caches
            destroyDictionaryCaches ( dictionary );
        }
    }

    /**
     * Destroys caches for the specified {@link Dictionary}.
     *
     * @param dictionary {@link Dictionary} to destroy caches for
     */
    private void destroyDictionaryCaches ( @NotNull final Dictionary dictionary )
    {
        clearLocaleCaches ();
        if ( recordsCache != null || dictionariesCache != null )
        {
            final Set<String> keys = dictionary.getKeys ( usablePrefix () );
            if ( recordsCache != null )
            {
                destroyKeys ( recordsCache.keySet ().iterator (), keys );
            }
            if ( dictionariesCache != null )
            {
                destroyKeys ( dictionariesCache.keySet ().iterator (), keys );
            }
        }
    }

    /**
     * Clears all locale caches.
     */
    private void clearLocaleCaches ()
    {
        if ( allLocales != null )
        {
            allLocales.clear ();
            allLocales = null;
        }
        if ( supportedLocales != null )
        {
            supportedLocales.clear ();
            supportedLocales = null;
        }
    }

    /**
     * Destroys caches for the specified keys.
     *
     * @param cachedKeys cached keys
     * @param keys       keys to remove from cache
     */
    private void destroyKeys ( @NotNull final Iterator<String> cachedKeys, @NotNull final Set<String> keys )
    {
        while ( cachedKeys.hasNext () )
        {
            final String cachedKey = cachedKeys.next ();
            for ( final String key : keys )
            {
                if ( cachedKey.startsWith ( key ) )
                {
                    cachedKeys.remove ();
                    break;
                }
            }
        }
    }

    /**
     * Removes all child {@link Dictionary}s.
     */
    public synchronized void clearDictionaries ()
    {
        if ( dictionaries != null )
        {
            // Removing all dictionaries
            dictionaries.clear ();
            dictionaries = null;

            // Destroying caches
            destroyDictionaryCaches ();
        }
    }

    /**
     * Destroys caches all {@link Dictionary} caches.
     */
    private void destroyDictionaryCaches ()
    {
        if ( dictionariesCache != null )
        {
            dictionariesCache.clear ();
            dictionariesCache = null;
        }
    }

    /**
     * Returns {@link List} of {@link TranslationInformation}s contained in this dictionary.
     *
     * @return {@link List} of {@link TranslationInformation}s contained in this dictionary
     */
    @NotNull
    public synchronized List<TranslationInformation> getTranslations ()
    {
        return translations != null ? new ImmutableList<TranslationInformation> ( translations ) :
                new ImmutableList<TranslationInformation> ();
    }

    /**
     * Sets {@link List} of {@link TranslationInformation}s for this dictionary.
     *
     * @param translations new {@link List} of {@link TranslationInformation}s for this dictionary
     */
    public synchronized void setTranslations ( @Nullable final List<TranslationInformation> translations )
    {
        this.translations = translations;
    }

    /**
     * Returns {@link TranslationInformation} for the specified {@link Locale}.
     *
     * @param locale {@link Locale}
     * @return {@link TranslationInformation} for the specified {@link Locale}
     */
    @Nullable
    public synchronized TranslationInformation getTranslation ( @NotNull final Locale locale )
    {
        final List<TranslationInformation> translations = new ArrayList<TranslationInformation> ( 1 + dictionariesCount () );

        // Collecting existing information on provided translations
        collectLanguages ( locale, translations );

        // Creating auto-generated information if none provided
        // This can be useful when information is not provided explicitely in any of the dictionaries
        if ( CollectionUtils.isEmpty ( translations ) )
        {
            final ArrayList<Locale> locales = new ArrayList<Locale> ();

            // Collecting supported locales
            collectLocales ( locales );

            // Creating auto-generated information on each locale
            for ( final Locale lc : locales )
            {
                translations.add ( new TranslationInformation ( lc, lc.getDisplayLanguage (), "WebLaF" ) );
            }
        }

        // Choosing most fitting one
        final TranslationInformation info;
        if ( translations.size () > 1 )
        {
            // Choosing most fitting translation according to comparator
            info = Collections.max ( translations, new TranslationInformationComparator ( locale ) );
        }
        else if ( translations.size () == 1 )
        {
            // There is only one existing translation fitting the specified locale
            info = translations.get ( 0 );
        }
        else
        {
            // No fitting translations present in this dictionary for the specified locale
            info = null;
        }
        return info;
    }

    /**
     * Colects {@link TranslationInformation}s related to specified {@link Locale} into provided {@link List}.
     *
     * @param locale       {@link Locale} to collect {@link TranslationInformation}s for
     * @param translations {@link List} to collect {@link TranslationInformation}s into
     */
    private void collectLanguages ( @NotNull final Locale locale, @NotNull final List<TranslationInformation> translations )
    {
        if ( CollectionUtils.notEmpty ( this.translations ) )
        {
            for ( final TranslationInformation language : this.translations )
            {
                if ( language.getLocale ().getLanguage ().equals ( locale.getLanguage () ) )
                {
                    translations.add ( language );
                }
            }
        }
        if ( CollectionUtils.notEmpty ( dictionaries ) )
        {
            for ( final Dictionary subdictionary : dictionaries )
            {
                subdictionary.collectLanguages ( locale, translations );
            }
        }
    }

    /**
     * Colects supported {@link Locale}s into provided {@link List}.
     *
     * {@link Locale}s will be taken from the first {@link Record} that has {@link Value}s. This works on assumption every or at least first
     * {@link Record} in dictionary supports all {@link Locale}s used across all other {@link Record}s.
     *
     * @param locales {@link List} to collect supported {@link Locale}s into
     */
    private void collectLocales ( @NotNull final List<Locale> locales )
    {
        if ( CollectionUtils.notEmpty ( records ) )
        {
            for ( final Record record : records )
            {
                // Collect all locales from first record we encounter
                for ( final Value value : record.getValues () )
                {
                    locales.add ( value.getLocale () );
                }

                // We only need a single set of locales
                if ( CollectionUtils.notEmpty ( locales ) )
                {
                    break;
                }
            }
        }
        if ( CollectionUtils.isEmpty ( locales ) && CollectionUtils.notEmpty ( dictionaries ) )
        {
            for ( final Dictionary subdictionary : dictionaries )
            {
                // Trying to collect locales from subdictionaries
                subdictionary.collectLocales ( locales );

                // We only need a single set of locales
                if ( CollectionUtils.notEmpty ( locales ) )
                {
                    break;
                }
            }
        }
    }

    /**
     * Adds {@link TranslationInformation} for this {@link Dictionary}.
     *
     * @param translation {@link TranslationInformation} to add
     */
    public synchronized void addTranslation ( @NotNull final TranslationInformation translation )
    {
        if ( translations == null )
        {
            translations = new ArrayList<TranslationInformation> ( 1 );
        }
        translations.add ( translation );
    }

    /**
     * Returns {@link List} of all {@link Locale} from this {@link Dictionary}.
     *
     * @return {@link List} of all {@link Locale} from this {@link Dictionary}
     */
    @NotNull
    public synchronized List<Locale> getAllLocales ()
    {
        if ( allLocales == null )
        {
            allLocales = new ArrayList<Locale> ();
            collectAllLocales ( allLocales );
        }
        return new ImmutableList<Locale> ( allLocales );
    }

    /**
     * Collects all {@link Locale}s from this {@link Dictionary}.
     *
     * @param locales {@link List} to put {@link Locale}s into
     */
    private void collectAllLocales ( @NotNull final List<Locale> locales )
    {
        if ( CollectionUtils.notEmpty ( records ) )
        {
            for ( final Record record : records )
            {
                record.collectAllLocales ( locales );
            }
        }
        if ( CollectionUtils.notEmpty ( dictionaries ) )
        {
            for ( final Dictionary dictionary : dictionaries )
            {
                dictionary.collectAllLocales ( locales );
            }
        }
    }

    /**
     * Returns {@link List} of {@link Locale} supported by this {@link Dictionary}.
     * Language codes are used to detect whether or not specific {@link Locale} is supported.
     * We can't say that for instance "en_US" {@link Locale} is not supported if we have "en" or "en_GB" translation available.
     * Even though it is not strict "supported" case it is much better than having fully distinct translation for each country.
     *
     * @return {@link List} of {@link Locale} supported by this {@link Dictionary}
     */
    @NotNull
    public synchronized List<Locale> getSupportedLocales ()
    {
        if ( supportedLocales == null )
        {
            supportedLocales = collectSupportedLocales ( null );
        }
        if ( supportedLocales == null )
        {
            supportedLocales = new ArrayList<Locale> ( 0 );
        }
        return new ImmutableList<Locale> ( supportedLocales );
    }

    /**
     * Returns all {@link Locale}s from this {@link Dictionary} and all sub-{@link Dictionary} {@link Record}s.
     * Note that {@link Locale}s intersection is only used across different {@link Dictionary}s.
     * Singke {@link Dictionary} can have {@link Record} with varying {@link Locale}s but they will all be counted in for it.
     * Basically any {@link Locale} found within single {@link Dictionary} is considered to be supported.
     *
     * @param locales {@link List} of {@link Locale}s collected so far, can be {@code null}
     * @return all {@link Locale}s from this {@link Dictionary} and all sub-{@link Dictionary} {@link Record}s
     */
    @Nullable
    protected List<Locale> collectSupportedLocales ( @Nullable List<Locale> locales )
    {
        if ( CollectionUtils.notEmpty ( records ) )
        {
            if ( locales == null )
            {
                locales = new ArrayList<Locale> ();
                for ( final Record record : records )
                {
                    record.collectAllLocales ( locales );
                }
            }
            else
            {
                locales = filterLocales ( locales );
            }
        }
        if ( CollectionUtils.notEmpty ( dictionaries ) )
        {
            for ( final Dictionary dictionary : dictionaries )
            {
                locales = dictionary.collectSupportedLocales ( locales );
            }
        }
        return locales;
    }

    /**
     * Returns {@link List} of {@link Locale}s filtered by language codes in this {@link Dictionary}.
     *
     * @param locales unfiltered {@link List} of {@link Locale}s
     * @return {@link List} of {@link Locale}s filtered by language codes in this {@link Dictionary}
     */
    @NotNull
    private List<Locale> filterLocales ( @NotNull final List<Locale> locales )
    {
        final List<Locale> filtered;
        if ( CollectionUtils.notEmpty ( records ) )
        {
            final List<String> languageCodes = new ArrayList<String> ();
            for ( final Record record : records )
            {
                record.collectAllCodes ( languageCodes );
            }
            if ( languageCodes.size () > 0 )
            {
                filtered = CollectionUtils.filter ( locales, new Filter<Locale> ()
                {
                    @Override
                    public boolean accept ( final Locale locale )
                    {
                        return languageCodes.contains ( locale.getLanguage () );
                    }
                } );
            }
            else
            {
                filtered = locales;
            }
        }
        else
        {
            filtered = locales;
        }
        return filtered;
    }

    /**
     * Returns usable prefix for this {@link Dictionary}.
     *
     * @return usable prefix for this {@link Dictionary}
     */
    @NotNull
    private String usablePrefix ()
    {
        return TextUtils.notEmpty ( prefix ) ? prefix + "." : "";
    }

    @Override
    public boolean equals ( @Nullable final Object obj )
    {
        return obj != null && obj instanceof Dictionary && ( ( Dictionary ) obj ).getId ().equals ( getId () );
    }

    @NotNull
    @Override
    public String toString ()
    {
        return ( name != null ? name + " " : "" ) + ( getPrefix () != null ? " [" + getPrefix () + "]" : "" ) +
                ( recordsCount () > 0 ? " [R:" + recordsCount () + "]" : "" ) +
                ( dictionariesCount () > 0 ? " [D:" + dictionariesCount () + "]" : "" );
    }
}