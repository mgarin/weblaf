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

import com.alee.utils.TextUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A set of utility methods for {@link LanguageManager} and related classes.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-LanguageManager">How to use LanguageManager</a>
 * @see LanguageManager
 */
public final class LanguageUtils
{
    /**
     * Map with cached {@link Locale} instances.
     * todo With JDK8 it can be replaced with appropriate {@link Locale} methods usage
     */
    private static final Map<String, Locale> localesCache = new HashMap<String, Locale> ( 5 );

    /**
     * Locale country separator.
     */
    private static final String COUNTRY_SEPARATOR = "-";

    /**
     * Returns {@link String} representation of {@link Locale}.
     *
     * @param locale {@link Locale} to convert into {@link String}
     * @return {@link String} representation of {@link Locale}
     */
    public static String toString ( final Locale locale )
    {
        if ( locale != null )
        {
            final String lang = ( locale.getLanguage () != null ? locale.getLanguage () : "" ).toLowerCase ( Locale.ROOT );
            final String country = ( locale.getCountry () != null ? locale.getCountry () : "" ).toUpperCase ( Locale.ROOT );
            return lang + ( TextUtils.notEmpty ( country ) ? COUNTRY_SEPARATOR + country : "" );
        }
        else
        {
            throw new LanguageException ( "Locale was not specified" );
        }
    }

    /**
     * Returns {@link Locale} parsed from its {@link String} representation.
     *
     * @param locale {@link String} to parse into {@link Locale}
     * @return {@link Locale} parsed from its {@link String} representation
     */
    public static Locale fromString ( final String locale )
    {
        if ( locale != null )
        {
            final int s = locale.indexOf ( COUNTRY_SEPARATOR );
            final String lang = ( s != -1 ? locale.substring ( 0, s ) : locale ).toLowerCase ( Locale.ROOT );
            final String country = ( s != -1 ? locale.substring ( s + COUNTRY_SEPARATOR.length () ) : "" ).toUpperCase ( Locale.ROOT );
            final String key = lang + ( TextUtils.notEmpty ( country ) ? COUNTRY_SEPARATOR + country : "" );
            if ( !localesCache.containsKey ( key ) )
            {
                localesCache.put ( key, new Locale ( lang, country ) );
            }
            return localesCache.get ( key );
        }
        else
        {
            throw new LanguageException ( "Locale was not specified" );
        }
    }

    /**
     * Returns system {@link Locale}.
     * Note that this is not purely "system" locale, but the one JVM receive at startup, it can also be preconfigured.
     * Also it is not tied to {@link Locale#getDefault()} so it will still return {@link Locale} based on system properties.
     *
     * @return system {@link Locale}
     */
    public static Locale getSystemLocale ()
    {
        final String language = System.getProperty ( "user.language" );
        if ( TextUtils.notEmpty ( language ) )
        {
            // Constructing system locale
            final String country = System.getProperty ( "user.country" );
            final String variant = System.getProperty ( "user.variant" );
            return new Locale (
                    language,
                    TextUtils.notEmpty ( country ) ? country : "",
                    TextUtils.notEmpty ( variant ) ? variant : ""
            );
        }
        else
        {
            // Constructing fallback locale
            return new Locale ( "en", "US" );
        }
    }
}