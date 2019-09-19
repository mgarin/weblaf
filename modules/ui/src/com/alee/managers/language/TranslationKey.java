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
    @NotNull
    private final String key;

    /**
     * Mutable translation data.
     * Used for compiling resulting translation
     */
    @Nullable
    private Object[] data;

    /**
     * Constructs new {@link TranslationKey}.
     *
     * @param key  translation key
     * @param data translation data
     */
    public TranslationKey ( @NotNull final String key, @Nullable final Object... data )
    {
        this.key = key;
        this.data = data;
    }

    /**
     * Returns translation key.
     *
     * @return translation key
     */
    @NotNull
    public String getKey ()
    {
        return key;
    }

    /**
     * Returns translation data.
     *
     * @return translation data
     */
    @Nullable
    public Object[] getData ()
    {
        return data;
    }

    /**
     * Updates translation data.
     *
     * @param data new translation data
     */
    public void setData ( @Nullable final Object[] data )
    {
        this.data = data;
    }
}