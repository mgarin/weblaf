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

package com.alee.api.merge.behavior;

import com.alee.api.merge.Merge;

import java.util.Map;

/**
 * {@link Map} objects merge behavior.
 * Only elements under the same keys will be merged.
 * Non-existing elements will simply be added into existing map.
 *
 * @author Mikle Garin
 */

public final class MapMergeBehavior implements MergeBehavior
{
    @Override
    public boolean supports ( final Object object, final Object merged )
    {
        return object instanceof Map && merged instanceof Map;
    }

    @Override
    public <T> T merge ( final Merge merge, final Object object, final Object merged )
    {
        final Map<Object, Object> em = ( Map<Object, Object> ) object;
        final Map<Object, Object> mm = ( Map<Object, Object> ) merged;
        for ( final Map.Entry<Object, Object> entry : mm.entrySet () )
        {
            final Object k = entry.getKey ();
            final Object e = em.get ( k );
            final Object v = entry.getValue ();
            em.put ( k, merge.merge ( e, v ) );
        }
        return ( T ) em;
    }
}