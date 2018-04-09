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

import com.alee.api.jdk.Objects;
import com.alee.managers.language.data.Dictionary;
import com.alee.managers.language.data.Record;
import com.alee.managers.language.data.Text;
import com.alee.managers.language.data.Value;
import com.alee.utils.TextUtils;

import java.io.Serializable;
import java.util.Locale;

/**
 * Provides access to translations for one specific language.
 * This class doesn't store anything on its own and only acts as a proxy to {@link LanguageManager}.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-LanguageManager">How to use LanguageManager</a>
 * @see LanguageManager
 */
public class Language implements Serializable
{
    /**
     * {@link Locale} of this {@link Language}.
     */
    protected final Locale locale;

    /**
     * Constructs new {@link Language} for the specified {@link Locale}.
     *
     * @param locale {@link Locale} for new {@link Language}
     */
    public Language ( final Locale locale )
    {
        super ();
        this.locale = locale;
    }

    /**
     * Returns {@link Locale} of this {@link Language}.
     *
     * @return {@link Locale} of this {@link Language}
     */
    public Locale getLocale ()
    {
        return locale;
    }

    /**
     * Returns {@link Dictionary} containing all registered {@link Dictionary}s.
     *
     * @return {@link Dictionary} containing all registered {@link Dictionary}s
     */
    public Dictionary getDictionaries ()
    {
        return LanguageManager.getDictionaries ();
    }

    /**
     * Returns {@link Record} for the specified language key.
     *
     * @param key language key to retrieve {@link Record} for
     * @return {@link Record} for the specified language key
     */
    public Record getRecord ( final String key )
    {
        return TextUtils.notEmpty ( key ) ? getDictionaries ().getRecord ( key, locale ) : null;
    }

    /**
     * Returns {@link Value} for the specified language key and {@link Locale} from this {@link Language}.
     *
     * @param key language key to retrieve {@link Value} for
     * @return {@link Value} for the specified language key and {@link Locale} from this {@link Language}
     */
    public Value getValue ( final String key )
    {
        final Record record = getRecord ( key );
        return record != null ? record.getValue ( locale ) : null;
    }

    /**
     * Returns default state {@link Text} for the specified language key and {@link Locale} from this {@link Language}.
     *
     * @param key language key to retrieve {@link Text} for
     * @return default state {@link Text} for the specified language key and {@link Locale} from this {@link Language}
     */
    public Text getText ( final String key )
    {
        final Value value = getValue ( key );
        return value != null ? value.getText () : null;
    }

    /**
     * Returns {@link Text} for the specified language key and {@link Locale} from this {@link Language}.
     *
     * @param key   language key to retrieve {@link Text} for
     * @param state {@link Text} state
     * @return {@link Text} for the specified language key and {@link Locale} from this {@link Language}
     */
    public Text getText ( final String key, final String state )
    {
        final Value value = getValue ( key );
        return value != null ? value.getText ( state ) : null;
    }

    /**
     * Returns text for the specified language key from this {@link Language}.
     *
     * @param key  language key to retrieve text for
     * @param data language data
     * @return text for the specified language key from this {@link Language}
     */
    public String get ( final String key, final Object... data )
    {
        final Text text = getText ( key );
        return text != null ? text.getText ( data ) : key;
    }

    /**
     * Returns text for the specified language key and state from this {@link Language}.
     *
     * @param key   language key to retrieve text for
     * @param state {@link Text} state
     * @param data  language data
     * @return text for the specified language key and state from this {@link Language}
     */
    public String getState ( final String key, final String state, final Object... data )
    {
        final Text text = getText ( key, state );
        return text != null ? text.getText ( data ) : key;
    }

    /**
     * Returns mnemonic for the specified language key from this {@link Language}.
     *
     * @param key language key to retrieve mnemonic for
     * @return mnemonic for the specified language key from this {@link Language}
     */
    public int getMnemonic ( final String key )
    {
        final Text text = getText ( key );
        return text != null ? text.getMnemonic () : -1;
    }

    /**
     * Returns mnemonic for the specified language key from this {@link Language}.
     *
     * @param key   language key to retrieve mnemonic for
     * @param state {@link Text} state
     * @return mnemonic for the specified language key from this {@link Language}
     */
    public int getMnemonic ( final String key, final String state )
    {
        final Text text = getText ( key, state );
        return text != null ? text.getMnemonic () : -1;
    }

    /**
     * Returns whether or not specified language key exists in this {@link Language}.
     * This will basically check existance of at least single {@link Record} with the key.
     *
     * @param key language key to check
     * @return {@code true} if specified language key exists in this {@link Language}, {@code false} otherwise
     */
    public boolean contains ( final String key )
    {
        return getRecord ( key ) != null;
    }

    /**
     * Returns whether or not at least single {@link Text} exists for specified language key.
     *
     * @param key language key to check
     * @return {@code true} if at least single {@link Text} exists for specified language key, {@code false} otherwise
     */
    public boolean containsText ( final String key )
    {
        return getText ( key ) != null;
    }

    /**
     * Returns whether or not at least single {@link Text} exists for specified language key and state.
     *
     * @param key   language key to check
     * @param state {@link Text} state
     * @return {@code true} if at least single {@link Text} exists for specified language key and state, {@code false} otherwise
     */
    public boolean containsText ( final String key, final String state )
    {
        return getText ( key, state ) != null;
    }

    @Override
    public boolean equals ( final Object language )
    {
        return language instanceof Language && Objects.equals ( getLocale (), ( ( Language ) language ).getLocale () );
    }

    @Override
    public String toString ()
    {
        return "Language[" + locale.toString () + "]";
    }
}