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
 * Special {@link Comparator} implementation for {@link TranslationInformation} based on provided {@link Locale} country.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-LanguageManager">How to use LanguageManager</a>
 * @see com.alee.managers.language.LanguageManager
 */
public final class TranslationInformationComparator extends AbstractCountryComparator<TranslationInformation>
{
    /**
     * Constructs new {@link TranslationInformationComparator}.
     *
     * @param locale priority {@link Locale}
     */
    public TranslationInformationComparator ( final Locale locale )
    {
        super ( locale );
    }

    @Override
    public int compare ( final TranslationInformation info1, final TranslationInformation info2 )
    {
        return compareCountries ( info1.getLocale ().getCountry (), info2.getLocale ().getCountry () );
    }
}