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
public class LanguageManagerTest
{
    /**
     * Initializes {@link LanguageManager}.
     */
    @BeforeClass
    public static void initialize ()
    {
        LanguageManager.initialize ();
        LanguageManager.setLocale ( new Locale ( "en" ) );
    }

    /**
     * Tests supported locales output.
     */
    @Test
    public void supportedLocalesTest ()
    {
        // Checking default locales count
        final List<Locale> allLocales = LanguageManager.getDictionaries ().getAllLocales ();
        if ( allLocales.size () != 12 )
        {
            throw new LanguageException ( "Incorrect core locales count: " + allLocales.size () );
        }

        // Checking that all locales are supported by default
        final List<Locale> coreLocales = LanguageManager.getSupportedLocales ();
        if ( coreLocales.size () != allLocales.size () )
        {
            throw new LanguageException ( "Incorrect core supported locales count: " + coreLocales.size () );
        }

        // Adding dictionary to limit supported locales down to
        final Dictionary limiter = new Dictionary ( "limit" );
        limiter.addRecord ( new Record ( "record", new Value ( new Locale ( "en" ), new Text ( "Test" ) ) ) );
        limiter.addRecord ( new Record ( "record", new Value ( new Locale ( "ru" ), new Text ( "Тест" ) ) ) );
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
        // We explicitly set EN locale on initialization, it should still be there
        if ( !LM.getLanguage ().getLocale ().getLanguage ().equals ( "en" ) )
        {
            throw new LanguageException ( "Unexpected locale: " + LM.getLanguage ().getLocale () );
        }

        // Checking core translation through LM
        final String key = "weblaf.time.units.short.nanosecond";
        final String expected = "ns";
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
     * Tests runtime {@link Dictionary} creation and usage.
     */
    @Test
    public void runtimeDictionaryUsage ()
    {
        final String key = "weblaf.test.title";
        final String en = "Sample test";
        final String ru = "Простой тест";

        // Ensuring our records don't exist yet
        checkTranslationRetrieval ( "en", "", key, key );
        checkTranslationRetrieval ( "en", "US", key, key );
        checkTranslationRetrieval ( "en", "GB", key, key );
        checkTranslationRetrieval ( "ru", "", key, key );

        // Checking that they are also considered absent
        checkTranslationAbsense ( "en", "", key );
        checkTranslationAbsense ( "en", "US", key );
        checkTranslationAbsense ( "en", "GB", key );
        checkTranslationAbsense ( "ru", "", key );

        // Creating new dictionary
        final Dictionary dictionary = new Dictionary ( "weblaf.test" );
        dictionary.addRecord ( new Record ( "title", new Value ( new Locale ( "en" ), new Text ( en ) ) ) );
        dictionary.addRecord ( new Record ( "title", new Value ( new Locale ( "ru" ), new Text ( ru ) ) ) );

        // Adding dictionary
        LanguageManager.addDictionary ( dictionary );

        // Ensuring our records exist now
        checkTranslationExistence ( "en", "", key );
        checkTranslationExistence ( "en", "US", key );
        checkTranslationExistence ( "en", "GB", key );
        checkTranslationExistence ( "ru", "", key );

        // Trying values retrieval
        checkTranslationRetrieval ( "en", "", key, en );
        checkTranslationRetrieval ( "en", "US", key, en );
        checkTranslationRetrieval ( "en", "GB", key, en );
        checkTranslationRetrieval ( "ru", "", key, ru );

        // Removing dictionary
        LanguageManager.removeDictionary ( dictionary );

        // Ensuring nothing was left in cache
        checkTranslationRetrieval ( "en", "", key, key );
        checkTranslationRetrieval ( "en", "US", key, key );
        checkTranslationRetrieval ( "en", "GB", key, key );
        checkTranslationRetrieval ( "ru", "", key, key );

        // Checking that even after our retrieval attempt records are considered absent
        checkTranslationAbsense ( "en", "", key );
        checkTranslationAbsense ( "en", "US", key );
        checkTranslationAbsense ( "en", "GB", key );
        checkTranslationAbsense ( "ru", "", key );
    }

    /**
     * Tests XML {@link Dictionary} creation and usage.
     */
    @Test
    public void xmlDictionaryUsage ()
    {
        // Loading dictionary
        final Dictionary dictionary = new Dictionary ( LanguageManagerTest.class, "Dictionary.xml" );

        // Adding dictionary
        LanguageManager.addDictionary ( dictionary );

        // Simple record
        checkTranslationRetrieval ( "en", "", "weblaf.test.record", "English" );
        checkTranslationRetrieval ( "en", "US", "weblaf.test.record", "English" );
        checkTranslationRetrieval ( "en", "GB", "weblaf.test.record", "English" );
        checkTranslationRetrieval ( "ru", "", "weblaf.test.record", "Русский" );

        // Dictionary with country-related record
        checkTranslationRetrieval ( "en", "", "weblaf.test.country.record", "English" );
        checkTranslationRetrieval ( "en", "US", "weblaf.test.country.record", "US English" );
        checkTranslationRetrieval ( "en", "GB", "weblaf.test.country.record", "GB English" );
        checkTranslationRetrieval ( "ru", "", "weblaf.test.country.record", "Русский" );

        // Dictionary with multiple records for same key
        checkTranslationRetrieval ( "en", "", "weblaf.test.multi.record", "English" );
        checkTranslationRetrieval ( "en", "US", "weblaf.test.multi.record", "US English" );
        checkTranslationRetrieval ( "en", "GB", "weblaf.test.multi.record", "English" );
        checkTranslationRetrieval ( "ru", "", "weblaf.test.multi.record", "Русский" );

        // Removing dictionary
        LanguageManager.removeDictionary ( dictionary );

        // Checking that even after our retrieval attempt records are considered absent
        checkTranslationAbsense ( "en", "", "weblaf.test.record" );
        checkTranslationAbsense ( "en", "US", "weblaf.test.record" );
        checkTranslationAbsense ( "en", "GB", "weblaf.test.record" );
        checkTranslationAbsense ( "ru", "", "weblaf.test.record" );
        checkTranslationAbsense ( "en", "", "weblaf.test.country.record" );
        checkTranslationAbsense ( "en", "US", "weblaf.test.country.record" );
        checkTranslationAbsense ( "en", "GB", "weblaf.test.country.record" );
        checkTranslationAbsense ( "ru", "", "weblaf.test.country.record" );
        checkTranslationAbsense ( "en", "", "weblaf.test.multi.record" );
        checkTranslationAbsense ( "en", "US", "weblaf.test.multi.record" );
        checkTranslationAbsense ( "en", "GB", "weblaf.test.multi.record" );
        checkTranslationAbsense ( "ru", "", "weblaf.test.multi.record" );
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
     * @param code    language code
     * @param country language country
     * @return {@link Language} for the specified code and country
     */
    private Language languageFor ( final String code, final String country )
    {
        return new Language ( new Locale ( code, country ) );
    }
}