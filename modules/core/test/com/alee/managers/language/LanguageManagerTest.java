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
import com.alee.managers.language.data.Record;
import com.alee.managers.language.data.Text;
import com.alee.managers.language.data.Value;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.List;
import java.util.Locale;

/**
 * Set of JUnit tests for {@link LanguageManager}.
 *
 * @author Mikle Garin
 */
@FixMethodOrder ( MethodSorters.JVM )
public final class LanguageManagerTest
{
    /**
     * Initializes {@link LanguageManager}.
     */
    @BeforeClass
    public static void initialize ()
    {
        LanguageManager.initialize ();
    }

    /**
     * Tests supported locales output.
     */
    @Test
    public void supportedLocales ()
    {
        // Checking that all locales are supported by default
        final List<Locale> allLocales = LanguageManager.getDictionaries ().getAllLocales ();
        final List<Locale> coreLocales = LanguageManager.getSupportedLocales ();
        if ( coreLocales.size () != allLocales.size () )
        {
            throw new LanguageException ( "Incorrect core supported locales count: " + coreLocales.size () );
        }

        // Adding dictionary to limit supported locales down to
        final Dictionary limiter = new Dictionary ( "limit" );
        limiter.addRecord ( new Record ( "record", new Value ( localeFor ( "en", "" ), new Text ( "Test" ) ) ) );
        limiter.addRecord ( new Record ( "record", new Value ( localeFor ( "ru", "" ), new Text ( "Тест" ) ) ) );
        LanguageManager.addDictionary ( limiter );

        // Checking limiter dictionary locales count
        final List<Locale> limiterLocales = limiter.getSupportedLocales ();
        if ( limiterLocales.size () != 2 )
        {
            throw new LanguageException ( "Incorrect limiter dictionary supported locales count: " + limiterLocales.size () );
        }

        // Limiting supported locales down to two and checking again
        final List<Locale> limitedLocales = LanguageManager.getSupportedLocales ();
        if ( limitedLocales.size () != limiterLocales.size () )
        {
            throw new LanguageException ( "Incorrect modified supported locales count: " + limitedLocales.size () );
        }

        // Removing custom dictionary to remove limitation
        LanguageManager.removeDictionary ( limiter );

        // Checking that all locales are supported once again
        final List<Locale> restoredLocales = LanguageManager.getSupportedLocales ();
        if ( restoredLocales.size () != allLocales.size () )
        {
            throw new LanguageException ( "Incorrect restored supported locales count: " + restoredLocales.size () );
        }
    }

    /**
     * Tests core module translation retrieval.
     */
    @Test
    public void coreTranslationRetrieval ()
    {
        final String key = "weblaf.time.units.short.nanosecond";
        checkTranslationRetrieval ( "en", "", key, "ns" );
        checkTranslationRetrieval ( "en", "US", key, "ns" );
        checkTranslationRetrieval ( "en", "GB", key, "ns" );
        checkTranslationRetrieval ( "ru", "", key, "нс" );
    }

    /**
     * Tests {@link LM} accessor usage.
     */
    @Test
    public void accessorUsage ()
    {
        // Updating globally used locale
        LanguageManager.setLocale ( localeFor ( "en", "" ) );

        // Checking locale in current LM supplier which should retrieve it from LanguageManager
        if ( !LM.getLocale ().getLanguage ().equals ( "en" ) )
        {
            throw new LanguageException ( "Unexpected locale: " + LM.getLanguage ().getLocale () );
        }

        // Checking core translation
        checkTranslationRetrieval ( "weblaf.time.units.short.nanosecond", "ns" );
    }

    /**
     * Tests runtime {@link Dictionary} creation and usage.
     */
    @Test
    public void runtimeDictionaryUsage ()
    {
        final String simpleKey = "weblaf.test.title";
        final String subdictionaryKey = "weblaf.test.sub.title";
        final String enText = "Sample test";
        final String ruText = "Простой тест";

        // Ensuring our records don't exist yet
        checkTranslationRetrieval ( "en", "", simpleKey, simpleKey );
        checkTranslationRetrieval ( "en", "US", simpleKey, simpleKey );
        checkTranslationRetrieval ( "en", "GB", simpleKey, simpleKey );
        checkTranslationRetrieval ( "ru", "", simpleKey, simpleKey );
        checkTranslationRetrieval ( "en", "", subdictionaryKey, subdictionaryKey );
        checkTranslationRetrieval ( "en", "US", subdictionaryKey, subdictionaryKey );
        checkTranslationRetrieval ( "en", "GB", subdictionaryKey, subdictionaryKey );
        checkTranslationRetrieval ( "ru", "", subdictionaryKey, subdictionaryKey );

        // Checking that they are also considered absent
        checkTranslationAbsense ( "en", "", simpleKey );
        checkTranslationAbsense ( "en", "US", simpleKey );
        checkTranslationAbsense ( "en", "GB", simpleKey );
        checkTranslationAbsense ( "ru", "", simpleKey );
        checkTranslationAbsense ( "en", "", subdictionaryKey );
        checkTranslationAbsense ( "en", "US", subdictionaryKey );
        checkTranslationAbsense ( "en", "GB", subdictionaryKey );
        checkTranslationAbsense ( "ru", "", subdictionaryKey );

        // Creating new runtime dictionary
        final Dictionary dictionary = new Dictionary ( "weblaf.test" );
        dictionary.addRecord ( new Record ( "title", new Value ( localeFor ( "en", "" ), new Text ( enText ) ) ) );
        dictionary.addRecord ( new Record ( "title", new Value ( localeFor ( "ru", "" ), new Text ( ruText ) ) ) );
        final Dictionary subdictionary = new Dictionary ( "sub" );
        subdictionary.addRecord ( new Record ( "title", new Value ( localeFor ( "en", "" ), new Text ( enText ) ) ) );
        subdictionary.addRecord ( new Record ( "title", new Value ( localeFor ( "ru", "" ), new Text ( ruText ) ) ) );
        dictionary.addDictionary ( subdictionary );

        // Adding runtime dictionary into LanguageManager
        LanguageManager.addDictionary ( dictionary );

        // Ensuring our records exist now
        checkTranslationExistence ( "en", "", simpleKey );
        checkTranslationExistence ( "en", "US", simpleKey );
        checkTranslationExistence ( "en", "GB", simpleKey );
        checkTranslationExistence ( "ru", "", simpleKey );
        checkTranslationExistence ( "en", "", subdictionaryKey );
        checkTranslationExistence ( "en", "US", subdictionaryKey );
        checkTranslationExistence ( "en", "GB", subdictionaryKey );
        checkTranslationExistence ( "ru", "", subdictionaryKey );

        // Trying values retrieval
        checkTranslationRetrieval ( "en", "", simpleKey, enText );
        checkTranslationRetrieval ( "en", "US", simpleKey, enText );
        checkTranslationRetrieval ( "en", "GB", simpleKey, enText );
        checkTranslationRetrieval ( "ru", "", simpleKey, ruText );
        checkTranslationRetrieval ( "en", "", subdictionaryKey, enText );
        checkTranslationRetrieval ( "en", "US", subdictionaryKey, enText );
        checkTranslationRetrieval ( "en", "GB", subdictionaryKey, enText );
        checkTranslationRetrieval ( "ru", "", subdictionaryKey, ruText );

        // Removing dictionary
        LanguageManager.removeDictionary ( dictionary );

        // Ensuring nothing was left in cache
        checkTranslationRetrieval ( "en", "", simpleKey, simpleKey );
        checkTranslationRetrieval ( "en", "US", simpleKey, simpleKey );
        checkTranslationRetrieval ( "en", "GB", simpleKey, simpleKey );
        checkTranslationRetrieval ( "ru", "", simpleKey, simpleKey );
        checkTranslationRetrieval ( "en", "", subdictionaryKey, subdictionaryKey );
        checkTranslationRetrieval ( "en", "US", subdictionaryKey, subdictionaryKey );
        checkTranslationRetrieval ( "en", "GB", subdictionaryKey, subdictionaryKey );
        checkTranslationRetrieval ( "ru", "", subdictionaryKey, subdictionaryKey );

        // Checking that even after our retrieval attempt records are considered absent
        checkTranslationAbsense ( "en", "", simpleKey );
        checkTranslationAbsense ( "en", "US", simpleKey );
        checkTranslationAbsense ( "en", "GB", simpleKey );
        checkTranslationAbsense ( "ru", "", simpleKey );
        checkTranslationAbsense ( "en", "", subdictionaryKey );
        checkTranslationAbsense ( "en", "US", subdictionaryKey );
        checkTranslationAbsense ( "en", "GB", subdictionaryKey );
        checkTranslationAbsense ( "ru", "", subdictionaryKey );
    }

    /**
     * Tests XML {@link Dictionary} creation and usage.
     */
    @Test
    public void xmlDictionaryUsage ()
    {
        final String simpleKey = "weblaf.test.record";
        final String countryKey = "weblaf.test.country.record";
        final String multiKey = "weblaf.test.multi.record";
        final String enText = "English";
        final String enUsText = "US English";
        final String enGbText = "GB English";
        final String ruText = "Русский";

        final String subdictionaryKey1 = "weblaf.test.sub.first.record";
        final String subdictionaryKey2 = "weblaf.test.sub.first.second.record";
        final String subdictionaryKey3 = "weblaf.test.sub.first.second.third.record";
        final String subdictionaryText1 = "First record";
        final String subdictionaryText2 = "Second record";
        final String subdictionaryText3 = "Third record";

        final String complexKey1 = "weblaf.test.complex.key.record.title";
        final String complexKey2 = "weblaf.test.complex.key.sub.record.title";
        final String complexText = "Complex record";

        // Ensuring our records don't exist yet
        checkTranslationAbsense ( "en", "", simpleKey );
        checkTranslationAbsense ( "en", "US", simpleKey );
        checkTranslationAbsense ( "en", "GB", simpleKey );
        checkTranslationAbsense ( "ru", "", simpleKey );
        checkTranslationAbsense ( "en", "", countryKey );
        checkTranslationAbsense ( "en", "US", countryKey );
        checkTranslationAbsense ( "en", "GB", countryKey );
        checkTranslationAbsense ( "ru", "", countryKey );
        checkTranslationAbsense ( "en", "", multiKey );
        checkTranslationAbsense ( "en", "US", multiKey );
        checkTranslationAbsense ( "en", "GB", multiKey );
        checkTranslationAbsense ( "ru", "", multiKey );
        checkTranslationAbsense ( "en", "", subdictionaryKey1 );
        checkTranslationAbsense ( "en", "", subdictionaryKey2 );
        checkTranslationAbsense ( "en", "", subdictionaryKey3 );
        checkTranslationAbsense ( "en", "", complexKey1 );
        checkTranslationAbsense ( "en", "", complexKey2 );

        // Loading XML dictionary
        final Dictionary dictionary = new Dictionary ( LanguageManagerTest.class, "Dictionary.xml" );

        // Adding XML dictionary into LanguageManager
        LanguageManager.addDictionary ( dictionary );

        // Simple record
        checkTranslationRetrieval ( "en", "", simpleKey, enText );
        checkTranslationRetrieval ( "en", "US", simpleKey, enText );
        checkTranslationRetrieval ( "en", "GB", simpleKey, enText );
        checkTranslationRetrieval ( "ru", "", simpleKey, ruText );

        // Dictionary with country-related record
        checkTranslationRetrieval ( "en", "", countryKey, enText );
        checkTranslationRetrieval ( "en", "US", countryKey, enUsText );
        checkTranslationRetrieval ( "en", "GB", countryKey, enGbText );
        checkTranslationRetrieval ( "ru", "", countryKey, ruText );

        // Dictionary with multiple records for same key
        checkTranslationRetrieval ( "en", "", multiKey, enText );
        checkTranslationRetrieval ( "en", "US", multiKey, enUsText );
        checkTranslationRetrieval ( "en", "GB", multiKey, enGbText );
        checkTranslationRetrieval ( "ru", "", multiKey, ruText );

        // Dictionary with records within subdictionaries
        checkTranslationRetrieval ( "en", "", subdictionaryKey1, subdictionaryText1 );
        checkTranslationRetrieval ( "en", "", subdictionaryKey2, subdictionaryText2 );
        checkTranslationRetrieval ( "en", "", subdictionaryKey3, subdictionaryText3 );

        // Dictionary and records with complex keys
        checkTranslationRetrieval ( "en", "", complexKey1, complexText );
        checkTranslationRetrieval ( "en", "", complexKey2, complexText );

        // Removing dictionary
        LanguageManager.removeDictionary ( dictionary );

        // Checking that even after our retrieval attempt records are considered absent
        checkTranslationAbsense ( "en", "", simpleKey );
        checkTranslationAbsense ( "en", "US", simpleKey );
        checkTranslationAbsense ( "en", "GB", simpleKey );
        checkTranslationAbsense ( "ru", "", simpleKey );
        checkTranslationAbsense ( "en", "", countryKey );
        checkTranslationAbsense ( "en", "US", countryKey );
        checkTranslationAbsense ( "en", "GB", countryKey );
        checkTranslationAbsense ( "ru", "", countryKey );
        checkTranslationAbsense ( "en", "", multiKey );
        checkTranslationAbsense ( "en", "US", multiKey );
        checkTranslationAbsense ( "en", "GB", multiKey );
        checkTranslationAbsense ( "ru", "", multiKey );
        checkTranslationAbsense ( "en", "", subdictionaryKey1 );
        checkTranslationAbsense ( "en", "", subdictionaryKey2 );
        checkTranslationAbsense ( "en", "", subdictionaryKey3 );
        checkTranslationAbsense ( "en", "", complexKey1 );
        checkTranslationAbsense ( "en", "", complexKey2 );
    }

    /**
     * Tests runtime {@link Locale} change.
     */
    @Test
    public void runtimeLocaleChange ()
    {
        final String simpleKey = "weblaf.test.record";
        final String countryKey = "weblaf.test.country.record";
        final String multiKey = "weblaf.test.multi.record";
        final String enText = "English";
        final String enUsText = "US English";
        final String enGbText = "GB English";
        final String ruText = "Русский";

        // Ensuring our records don't exist yet
        checkTranslationAbsense ( "en", "", simpleKey );
        checkTranslationAbsense ( "en", "US", simpleKey );
        checkTranslationAbsense ( "en", "GB", simpleKey );
        checkTranslationAbsense ( "ru", "", simpleKey );
        checkTranslationAbsense ( "en", "", countryKey );
        checkTranslationAbsense ( "en", "US", countryKey );
        checkTranslationAbsense ( "en", "GB", countryKey );
        checkTranslationAbsense ( "ru", "", countryKey );
        checkTranslationAbsense ( "en", "", multiKey );
        checkTranslationAbsense ( "en", "US", multiKey );
        checkTranslationAbsense ( "en", "GB", multiKey );
        checkTranslationAbsense ( "ru", "", multiKey );

        // Loading XML dictionary
        final Dictionary dictionary = new Dictionary ( LanguageManagerTest.class, "Dictionary.xml" );

        // Adding XML dictionary into LanguageManager
        LanguageManager.addDictionary ( dictionary );

        // Checking English text
        LanguageManager.setLocale ( localeFor ( "en", "" ) );
        checkTranslationRetrieval ( simpleKey, enText );
        checkTranslationRetrieval ( countryKey, enText );
        checkTranslationRetrieval ( multiKey, enText );

        // Checking English US text
        LanguageManager.setLocale ( localeFor ( "en", "US" ) );
        checkTranslationRetrieval ( simpleKey, enText );
        checkTranslationRetrieval ( countryKey, enUsText );
        checkTranslationRetrieval ( multiKey, enUsText );

        // Checking English US text
        LanguageManager.setLocale ( localeFor ( "en", "GB" ) );
        checkTranslationRetrieval ( simpleKey, enText );
        checkTranslationRetrieval ( countryKey, enGbText );
        checkTranslationRetrieval ( multiKey, enGbText );

        // Checking Russian text
        LanguageManager.setLocale ( localeFor ( "ru", "" ) );
        checkTranslationRetrieval ( simpleKey, ruText );
        checkTranslationRetrieval ( countryKey, ruText );
        checkTranslationRetrieval ( multiKey, ruText );

        // Removing dictionary
        LanguageManager.removeDictionary ( dictionary );

        // Checking that even after our retrieval attempt records are considered absent
        checkTranslationAbsense ( "en", "", simpleKey );
        checkTranslationAbsense ( "en", "US", simpleKey );
        checkTranslationAbsense ( "en", "GB", simpleKey );
        checkTranslationAbsense ( "ru", "", simpleKey );
        checkTranslationAbsense ( "en", "", countryKey );
        checkTranslationAbsense ( "en", "US", countryKey );
        checkTranslationAbsense ( "en", "GB", countryKey );
        checkTranslationAbsense ( "ru", "", countryKey );
        checkTranslationAbsense ( "en", "", multiKey );
        checkTranslationAbsense ( "en", "US", multiKey );
        checkTranslationAbsense ( "en", "GB", multiKey );
        checkTranslationAbsense ( "ru", "", multiKey );
    }

    /**
     * Asserts translation existence.
     *
     * @param code    language code
     * @param country language country
     * @param key     translation key
     */
    private void checkTranslationExistence ( final String code, final String country, final String key )
    {
        if ( !languageFor ( code, country ).containsText ( key ) )
        {
            throw new LanguageException ( String.format (
                "Translation for lang[%s] country[%s] key[%s] still exists",
                code, country, key
            ) );
        }
    }

    /**
     * Asserts translation result.
     *
     * @param key      translation key
     * @param expected expected translation result
     */
    private void checkTranslationRetrieval ( final String key, final String expected )
    {
        final String result = LM.get ( key );
        if ( !result.equals ( expected ) )
        {
            throw new LanguageException ( String.format (
                "Unexpected translation for key[%s]" + "\n" + "Expected: %s" + "\n" + "Result: %s",
                key, expected, result
            ) );
        }
    }

    /**
     * Asserts translation result.
     *
     * @param code     language code
     * @param country  language country
     * @param key      translation key
     * @param expected expected translation result
     */
    private void checkTranslationRetrieval ( final String code, final String country, final String key, final String expected )
    {
        final String result = languageFor ( code, country ).get ( key );
        if ( !result.equals ( expected ) )
        {
            throw new LanguageException ( String.format (
                "Unexpected translation for lang[%s] country[%s] key[%s]" + "\n" + "Expected: %s" + "\n" + "Result: %s",
                code, country, key, expected, result
            ) );
        }
    }

    /**
     * Asserts translation absense.
     *
     * @param code    language code
     * @param country language country
     * @param key     translation key
     */
    private void checkTranslationAbsense ( final String code, final String country, final String key )
    {
        if ( languageFor ( code, country ).containsText ( key ) )
        {
            throw new LanguageException ( String.format (
                "Translation for lang[%s] country[%s] key[%s] still exists",
                code, country, key
            ) );
        }
    }

    /**
     * Returns {@link Language} for the specified code and country.
     *
     * @param code    {@link Language} code
     * @param country {@link Language} country
     * @return {@link Language} for the specified code and country
     */
    private Language languageFor ( final String code, final String country )
    {
        return new Language ( localeFor ( code, country ) );
    }

    /**
     * Returns {@link Locale} for the specified code and country.
     *
     * @param code    {@link Locale} code
     * @param country {@link Locale} country
     * @return {@link Locale} for the specified code and country
     */
    protected Locale localeFor ( final String code, final String country )
    {
        return new Locale ( code, country );
    }
}