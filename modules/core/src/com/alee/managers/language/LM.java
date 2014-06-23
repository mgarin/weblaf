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

import com.alee.managers.language.data.Value;

/**
 * This is short-named reference class for some basic LanguageManager methods.
 * It is added to reduce the code size of language values requests in cases of direct translations use.
 *
 * @author Mikle Garin
 */

public class LM implements LanguageConstants
{
    /**
     * Returns translation for specified language key.
     *
     * @param key language key
     * @return key translation
     */
    public static String get ( final String key )
    {
        return LanguageManager.get ( key );
    }

    /**
     * Returns translation for specified language key and formatting data.
     *
     * @param key  language key
     * @param data formatting data
     * @return key translation
     */
    public static String get ( final String key, final Object... data )
    {
        return LanguageManager.get ( key, data );
    }


    /**
     * Returns whether specified language key exists or not.
     *
     * @param key language key to check
     * @return whether specified language key exists or not
     */
    public static boolean contains ( final String key )
    {
        return LanguageManager.contains ( key );
    }

    /**
     * Returns mnemonic for specified language key.
     *
     * @param key language key
     * @return mnemonic
     */
    public static Character getMnemonic ( final String key )
    {
        return LanguageManager.getMnemonic ( key );
    }

    /**
     * Returns language value for specified language key.
     *
     * @param key language key
     * @return language value
     */
    public static Value getValue ( final String key )
    {
        return LanguageManager.getValue ( key );
    }
}