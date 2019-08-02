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

import com.alee.api.jdk.Supplier;
import com.alee.managers.language.data.Record;
import com.alee.managers.language.data.Text;

import java.util.Locale;

/**
 * Bridge class providing quick and convenient access to specific {@link Language} provided by {@link #languageSupplier}.
 * To gain access to multiple languages simultaneously use separate {@link Language} instances.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-LanguageManager">How to use LanguageManager</a>
 * @see LanguageManager
 * @see Language
 */
public final class LM
{
    /**
     * Empty language data.
     * Exists for methods usage convenience.
     */
    public static final Object[] emptyData = new Object[ 0 ];

    /**
     * {@link Supplier} for universal {@link Language}.
     * It can be replaced with any {@link Supplier} providing preferred {@link Language} instance.
     */
    private static Supplier<Language> languageSupplier = new Supplier<Language> ()
    {
        @Override
        public Language get ()
        {
            return LanguageManager.getLanguage ();
        }
    };

    /**
     * Returns {@link Supplier} for universal {@link Language}.
     *
     * @return {@link Supplier} for universal {@link Language}
     */
    public static Supplier<Language> getLanguageSupplier ()
    {
        return languageSupplier;
    }

    /**
     * Sets {@link Supplier} for universal {@link Language}.
     *
     * @param supplier {@link Supplier} for universal {@link Language}
     */
    public static void setLanguageSupplier ( final Supplier<Language> supplier )
    {
        languageSupplier = supplier;
    }

    /**
     * Returns specific {@link Language} instance currently used by this bridge class.
     *
     * @return specific {@link Language} instance currently used by this bridge class
     */
    public static Language getLanguage ()
    {
        return languageSupplier.get ();
    }

    /**
     * Returns {@link Locale} from {@link Language} instance currently used by this bridge class.
     *
     * @return {@link Locale} from {@link Language} instance currently used by this bridge class
     */
    public static Locale getLocale ()
    {
        return getLanguage ().getLocale ();
    }

    /**
     * Returns text for the specified language key from this {@link Language}.
     *
     * @param key  language key to retrieve text for
     * @param data language data
     * @return text for the specified language key from this {@link Language}
     */
    public static String get ( final String key, final Object... data )
    {
        return getLanguage ().get ( key, data );
    }

    /**
     * Returns text for the specified language key and state from this {@link Language}.
     *
     * @param key   language key to retrieve text for
     * @param state {@link Text} state
     * @param data  language data
     * @return text for the specified language key and state from this {@link Language}
     */
    public static String getState ( final String key, final String state, final Object... data )
    {
        return getLanguage ().getState ( key, state, data );
    }

    /**
     * Returns mnemonic for the specified language key from this {@link Language}.
     *
     * @param key language key to retrieve mnemonic for
     * @return mnemonic for the specified language key from this {@link Language}
     */
    public static int getMnemonic ( final String key )
    {
        return getLanguage ().getMnemonic ( key );
    }

    /**
     * Returns mnemonic for the specified language key from this {@link Language}.
     *
     * @param key   language key to retrieve mnemonic for
     * @param state {@link Text} state
     * @return mnemonic for the specified language key from this {@link Language}
     */
    public static int getMnemonic ( final String key, final String state )
    {
        return getLanguage ().getMnemonic ( key, state );
    }

    /**
     * Returns whether or not specified language key exists in default {@link Language}.
     * This will basically check existance of at least single {@link Record} with the specified key in default {@link Language}.
     *
     * @param key language key to check
     * @return {@code true} if specified language key exists in default {@link Language}, {@code false} otherwise
     */
    public static boolean contains ( final String key )
    {
        return getLanguage ().contains ( key );
    }

    /**
     * Returns whether or not at least single {@link Text} exists for specified language key.
     *
     * @param key language key to check
     * @return {@code true} if at least single {@link Text} exists for specified language key, {@code false} otherwise
     */
    public static boolean containsText ( final String key )
    {
        return getLanguage ().containsText ( key );
    }

    /**
     * Returns whether or not at least single {@link Text} exists for specified language key and state.
     *
     * @param key   language key to check
     * @param state {@link Text} state
     * @return {@code true} if at least single {@link Text} exists for specified language key and state, {@code false} otherwise
     */
    public static boolean containsText ( final String key, final String state )
    {
        return getLanguage ().containsText ( key, state );
    }
}