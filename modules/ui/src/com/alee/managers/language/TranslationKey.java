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

/**
 * This object encapsulates a single translation key.
 * It might also store translation data if it is needed.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-LanguageManager">How to use LanguageManager</a>
 * @see LanguageManager
 */
public final class TranslationKey
{
    /**
     * Immutable translation key.
     */
    private final String key;

    /**
     * Mutable translation data.
     * Used for compiling resulting translation
     */
    private Object[] data;

    /**
     * Constructs new {@link TranslationKey}.
     *
     * @param key  translation key
     * @param data translation data
     */
    private TranslationKey ( final String key, final Object... data )
    {
        super ();
        this.key = key;
        this.data = data;
    }

    /**
     * Returns translation key.
     *
     * @return translation key
     */
    public String getKey ()
    {
        return key;
    }

    /**
     * Returns translation data.
     *
     * @return translation data
     */
    public Object[] getData ()
    {
        return data;
    }

    /**
     * Updates translation data.
     *
     * @param data new translation data
     */
    public void setData ( final Object[] data )
    {
        this.data = data;
    }

    /**
     * Returns new {@link TranslationKey} instance.
     *
     * @param key  translation key
     * @param data translation data
     * @return new {@link TranslationKey} instance
     */
    public static TranslationKey of ( final String key, final Object... data )
    {
        return new TranslationKey ( key, data );
    }
}