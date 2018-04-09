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

import java.util.Comparator;
import java.util.Locale;

/**
 * Special {@link Comparator} implementation for {@link Record} based on provided {@link Locale} country.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-LanguageManager">How to use LanguageManager</a>
 * @see com.alee.managers.language.LanguageManager
 */
public final class RecordCountryComparator extends AbstractCountryComparator<Record>
{
    /**
     * Constructs new {@link RecordCountryComparator}.
     *
     * @param locale priority {@link Locale}
     */
    public RecordCountryComparator ( final Locale locale )
    {
        super ( locale );
    }

    @Override
    public int compare ( final Record record1, final Record record2 )
    {
        final Value value1 = record1.getValue ( locale );
        final Value value2 = record2.getValue ( locale );
        if ( value1 == null && value2 == null )
        {
            return 0;
        }
        else if ( value1 != null && value2 == null )
        {
            return 1;
        }
        else if ( value1 == null && value2 != null )
        {
            return -1;
        }
        else
        {
            return compareCountries ( value1.getLocale ().getCountry (), value2.getLocale ().getCountry () );
        }
    }
}