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

import com.alee.api.merge.GlobalMergeBehavior;
import com.alee.api.merge.Merge;
import com.alee.api.merge.RecursiveMerge;
import com.alee.utils.ReflectUtils;

import java.lang.reflect.Array;

/**
 * {@link Array} merge behavior by their element indices.
 * Only elements under the same indices will be merged, everything else will be added at the end of array.
 * If existing array is smaller than merged array new array will be created for the merge result.
 * Also new array will be created for the merge result if existing and merged array component types are inconsistent.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-Merge">How to use Merge</a>
 * @see Merge
 */
public class IndexArrayMergeBehavior implements GlobalMergeBehavior<Object, Object, Object>
{
    /**
     * todo 1. Provide a different merge behavior similar to {@link ListMergeBehavior}
     */

    @Override
    public boolean supports ( final RecursiveMerge merge, final Class<Object> type, final Object base, final Object merged )
    {
        return base.getClass ().isArray () && merged.getClass ().isArray ();
    }

    @Override
    public Object merge ( final RecursiveMerge merge, final Class type, final Object base, final Object merged, final int depth )
    {
        // Calculating resulting array size
        final int el = Array.getLength ( base );
        final int ml = Array.getLength ( merged );
        final int rl = Math.max ( el, ml );

        // Determining resulting array type
        final Class baseType = base.getClass ().getComponentType ();
        final Class mergedType = merged.getClass ().getComponentType ();
        final Class resultingType = baseType == mergedType ? baseType : ReflectUtils.getClosestSuperclass ( baseType, mergedType );

        // Picking resulting array instance
        final Object result = baseType == mergedType && el >= ml ? base : Array.newInstance ( resultingType, rl );

        // Merging two arrays
        for ( int i = 0; i < rl; i++ )
        {
            if ( i < el && i < ml )
            {
                final Object ev = Array.get ( base, i );
                final Object mv = Array.get ( merged, i );
                Array.set ( result, i, merge.merge ( resultingType, ev, mv, depth + 1 ) );
            }
            else if ( i < el )
            {
                final Object ev = Array.get ( base, i );
                Array.set ( result, i, ev );
            }
            else
            {
                final Object mv = Array.get ( merged, i );
                Array.set ( result, i, mv );
            }
        }

        return result;
    }
}