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

import java.util.Comparator;

/**
 * Special comparator for sorting LanguageUpdaters list.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-LanguageManager">How to use LanguageManager</a>
 * @see com.alee.managers.language.LanguageManager
 */
public final class LanguageUpdaterComparator implements Comparator<LanguageUpdater>
{
    @Override
    public int compare ( final LanguageUpdater lu1, final LanguageUpdater lu2 )
    {
        final Class cc1 = lu1.getComponentClass ();
        final Class cc2 = lu2.getComponentClass ();
        if ( cc1.equals ( cc2 ) )
        {
            return 0;
        }
        else if ( cc1.isAssignableFrom ( cc2 ) )
        {
            return 1;
        }
        else
        {
            return -1;
        }
    }
}