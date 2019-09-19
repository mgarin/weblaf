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

package com.alee.utils.jar;

import com.alee.api.annotations.NotNull;
import com.alee.utils.collection.ImmutableList;

import java.util.Comparator;
import java.util.List;

/**
 * {@link Comparator} implementation for {@link JarEntry} objects.
 * It sorts {@link JarEntry}s by their {@link JarEntryType}s.
 *
 * @author Mikle Garin
 */
public final class JarEntryComparator implements Comparator<JarEntry>
{
    /**
     * Static {@link JarEntryComparator} instance.
     */
    private static JarEntryComparator instance;

    /**
     * Returns static {@link JarEntryComparator} instance.
     *
     * @return static {@link JarEntryComparator} instance
     */
    public static JarEntryComparator instance ()
    {
        if ( instance == null )
        {
            instance = new JarEntryComparator ();
        }
        return instance;
    }

    /**
     * {@link JarEntryType}s sorted by priority.
     */
    @NotNull
    private static final List<JarEntryType> typePriority = new ImmutableList<JarEntryType> ( JarEntryType.values () );

    @Override
    public int compare ( final JarEntry e1, final JarEntry e2 )
    {
        return Integer.valueOf ( typePriority.indexOf ( e1.getType () ) )
                .compareTo ( typePriority.indexOf ( e2.getType () ) );
    }
}