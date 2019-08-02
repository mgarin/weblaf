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

import com.alee.api.jdk.Objects;
import com.alee.utils.TextUtils;

import java.util.Comparator;
import java.util.Locale;

/**
 * Abstract {@link Comparator} for sorting based on provided elements country and priority {@link Locale} country.
 *
 * @param <T> compared elements type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-LanguageManager">How to use LanguageManager</a>
 * @see com.alee.managers.language.LanguageManager
 */
public abstract class AbstractCountryComparator<T> implements Comparator<T>
{
    /**
     * Priority {@link Locale}.
     */
    protected final Locale locale;

    /**
     * Constructs new {@link AbstractCountryComparator}.
     *
     * @param locale priority {@link Locale}
     */
    public AbstractCountryComparator ( final Locale locale )
    {
        this.locale = locale;
    }

    /**
     * Returns result of two countries comparison.
     *
     * @param country1 first country
     * @param country2 second country
     * @return result of two countries comparison
     */
    protected int compareCountries ( final String country1, final String country2 )
    {
        if ( TextUtils.isEmpty ( locale.getCountry () ) )
        {
            return noCountryPriority ( country1, country2 );
        }
        else
        {
            return sameCountryPriority ( country1, country2 );
        }
    }

    /**
     * Returns result of two countries comparison with locale priority.
     *
     * @param country1 first country
     * @param country2 second country
     * @return result of two countries comparison with locale priority
     */
    protected int sameCountryPriority ( final String country1, final String country2 )
    {
        final int result;
        final boolean same1 = Objects.equals ( locale.getCountry (), country1 );
        final boolean same2 = Objects.equals ( locale.getCountry (), country2 );
        if ( same1 && !same2 )
        {
            result = 1;
        }
        else if ( same2 && !same1 )
        {
            result = -1;
        }
        else
        {
            result = noCountryPriority ( country1, country2 );
        }
        return result;
    }

    /**
     * Returns result of two countries comparison with no locale priority.
     *
     * @param country1 first country
     * @param country2 second country
     * @return result of two countries comparison with no locale priority
     */
    protected int noCountryPriority ( final String country1, final String country2 )
    {
        final int result;
        final boolean has1 = TextUtils.notEmpty ( country1 );
        final boolean has2 = TextUtils.notEmpty ( country2 );
        if ( !has1 && has2 )
        {
            result = 1;
        }
        else if ( !has2 && has1 )
        {
            result = -1;
        }
        else
        {
            result = 0;
        }
        return result;
    }
}