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

package com.alee.utils.reflection;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * User: mgarin Date: 05.03.12 Time: 13:54
 */

public class JarEntryComparator implements Comparator<JarEntry>
{
    private List<JarEntryType> typePriority = Arrays.asList ( JarEntryType.values () );

    public int compare ( JarEntry e1, JarEntry e2 )
    {
        return Integer.valueOf ( typePriority.indexOf ( e1.getType () ) ).compareTo ( typePriority.indexOf ( e2.getType () ) );
    }
}