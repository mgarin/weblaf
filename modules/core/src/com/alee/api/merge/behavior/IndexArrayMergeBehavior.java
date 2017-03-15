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
import com.alee.api.merge.MergeBehavior;

import java.lang.reflect.Array;

/**
 * {@link Array} merge behavior by their element indices.
 * Only elements under the same indices will be merged, everything else will be added at the end of array.
 * If existing array is smaller than merged array new array will be created for the merge result.
 * Also new array will be created for the merge result if existing and merged array component types are inconsistent.
 *
 * @author Mikle Garin
 */

public final class IndexArrayMergeBehavior implements MergeBehavior
{
    /**
     * todo 1. Provide a different merge behavior similar to {@link ListMergeBehavior}
     */

    @Override
    public boolean supports ( final Merge merge, final Object object, final Object merged )
    {
        return object.getClass ().isArray () && merged.getClass ().isArray ();
    }

    @Override
    public <T> T merge ( final Merge merge, final Object object, final Object merged )
    {
        final int el = Array.getLength ( object );
        final int ml = Array.getLength ( merged );
        final Class<?> et = object.getClass ().getComponentType ();
        final Class<?> mt = merged.getClass ().getComponentType ();
        final Class<?> type = et == mt ? mt : Object.class;
        final Object result = et == mt && el >= ml ? object : Array.newInstance ( type, ml );
        for ( int i = 0; i < ml; i++ )
        {
            if ( i < el && i < ml )
            {
                final Object ev = Array.get ( object, i );
                final Object mv = Array.get ( merged, i );
                Array.set ( result, i, merge.merge ( ev, mv ) );
            }
            else if ( i < el )
            {
                final Object ev = Array.get ( object, i );
                Array.set ( result, i, ev );
            }
            else
            {
                final Object mv = Array.get ( merged, i );
                Array.set ( result, i, mv );
            }
        }
        return ( T ) result;
    }
}