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
import com.alee.api.merge.MergeException;
import com.alee.api.merge.RecursiveMerge;
import com.alee.utils.ReflectUtils;
import com.alee.utils.collection.ImmutableList;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link List} merge behavior by their element indices.
 * Only elements under the same indices will be merged, everything else will be added at the end of list.
 * If existing list is smaller than merged one new list will be created for the merge result.
 * Also new array will be created for the merge result if existing and merged array component types are inconsistent.
 *
 * @param <T> {@link List} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-Merge">How to use Merge</a>
 * @see Merge
 */
public class IndexListMergeBehavior<T extends List> implements GlobalMergeBehavior<T, T, T>
{
    @Override
    public boolean supports ( final RecursiveMerge merge, final Class<T> type, final Object base, final Object merged )
    {
        return base instanceof List && merged instanceof List;
    }

    @Override
    public T merge ( final RecursiveMerge merge, final Class type, final T base, final T merged, final int depth )
    {
        try
        {
            // Calculating resulting list size
            final int el = base.size ();
            final int ml = merged.size ();
            final int rl = Math.max ( el, ml );

            // Picking resulting list instance
            final List list;
            if ( el >= ml && !( base instanceof ImmutableList ) )
            {
                // Base list is big enough to include all elements
                list = base;
            }
            else if ( base instanceof ArrayList || base instanceof ImmutableList )
            {
                // Base list is not big enough or it is immutable
                list = new ArrayList ( rl );
            }
            else
            {
                // Base list is not big enough and of some custom type
                list = ReflectUtils.createInstance ( base.getClass () );
            }

            // Merging two lists
            for ( int i = 0; i < rl; i++ )
            {
                if ( i < el && i < ml )
                {
                    final Object ev = base.get ( i );
                    final Object mv = merged.get ( i );
                    list.set ( i, merge.merge ( Object.class, ev, mv, depth + 1 ) );
                }
                else if ( i < el )
                {
                    final Object ev = base.get ( i );
                    list.set ( i, ev );
                }
                else
                {
                    final Object mv = merged.get ( i );
                    list.set ( i, mv );
                }
            }

            // Determining resulting list
            final T result;
            if ( base instanceof ImmutableList )
            {
                // Immutable list can only be created once we have resulting data
                result = ( T ) new ImmutableList ( list );
            }
            else
            {
                // Any other list can be returned "as is"
                result = ( T ) list;
            }
            return result;
        }
        catch ( final Exception e )
        {
            // Something went wrong upong merging lists
            throw new MergeException ( "Unable to merge lists", e );
        }
    }
}