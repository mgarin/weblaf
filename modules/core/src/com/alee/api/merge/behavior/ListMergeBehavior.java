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

import com.alee.api.duplicate.DuplicateResolver;
import com.alee.api.duplicate.RejectDuplicates;
import com.alee.api.matcher.Matcher;
import com.alee.api.merge.GlobalMergeBehavior;
import com.alee.api.merge.Merge;
import com.alee.api.merge.MergeException;

import java.util.List;

/**
 * Smart {@link List} merge behavior that tracks `Identifiable` elements
 * It will attempt to find identifiable elements in list and merge them.
 * Other elements will simply be added to the end of the list in provided order.
 * This is the best way we can handle list elements merge without any additional information on the elements.
 *
 * @param <T> {@link List} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-Merge">How to use Merge</a>
 * @see Merge
 */
public class ListMergeBehavior<T extends List> implements GlobalMergeBehavior<T, T, T>
{
    /**
     * todo 1. Merging two lists of Identifiable elements gives unexpected results (https://github.com/mgarin/weblaf/issues/448)
     * todo 2. Provide an appropriate support for {@link com.alee.utils.collection.ImmutableList}
     */

    /**
     * {@link Matcher} for {@link List} elements.
     * It is used to detect duplicates and match list elements that can be merged.
     */
    private final Matcher matcher;

    /**
     * {@link DuplicateResolver} used for solving cases with duplicates in base {@link List}.
     */
    private final DuplicateResolver baseDuplicateResolver;

    /**
     * {@link DuplicateResolver} used for solving cases with duplicates in merged {@link List}.
     */
    private final DuplicateResolver mergedDuplicateResolver;

    /**
     * Constructs new {@link ListMergeBehavior}.
     *
     * @param matcher {@link Matcher} for list elements
     */
    public ListMergeBehavior ( final Matcher matcher )
    {
        this ( matcher, new RejectDuplicates ( matcher ), new RejectDuplicates ( matcher ) );
    }

    /**
     * Constructs new {@link ListMergeBehavior}.
     *
     * @param matcher                 {@link Matcher} for list elements
     * @param baseDuplicateResolver   {@link DuplicateResolver} used for solving cases with duplicates in base {@link List}
     * @param mergedDuplicateResolver {@link DuplicateResolver} used for solving cases with duplicates in merged {@link List}
     */
    public ListMergeBehavior ( final Matcher matcher, final DuplicateResolver baseDuplicateResolver,
                               final DuplicateResolver mergedDuplicateResolver )
    {
        this.matcher = matcher;
        this.baseDuplicateResolver = baseDuplicateResolver;
        this.mergedDuplicateResolver = mergedDuplicateResolver;
    }

    @Override
    public boolean supports ( final Merge merge, final Class<T> type, final Object base, final Object merged )
    {
        return base instanceof List && merged instanceof List;
    }

    @Override
    public T merge ( final Merge merge, final Class<T> type, final T base, final T merged )
    {
        // Checking for duplicates in base list according to specified matcher
        // This might do nothing, modify base list or even throw an exception depending on implementation
        baseDuplicateResolver.resolve ( base );

        // Checking for duplicates in merged list according to specified matcher
        // This might do nothing, modify merged list or even throw an exception depending on implementation
        mergedDuplicateResolver.resolve ( merged );

        // Merging list objects
        for ( final Object mergedObject : merged )
        {
            // We only merge matched objects
            if ( matcher.supports ( mergedObject ) )
            {
                // Looking for object of the same type which is also identifiable in the existing list
                // Then we compare their IDs and merge them using the same algorithm if IDs are equal
                boolean matched = false;
                for ( int j = 0; j < base.size (); j++ )
                {
                    final Object existingObject = base.get ( j );
                    if ( matcher.match ( mergedObject, existingObject ) )
                    {
                        base.set ( j, merge.mergeRaw ( Object.class, existingObject, mergedObject ) );
                        matched = true;
                        break;
                    }
                }
                if ( !matched )
                {
                    // Simply adding object to the end of the list
                    base.add ( mergedObject );
                }
            }
            else if ( mergedObject != null )
            {
                // Simply adding non-identifiable object to the end of the list
                base.add ( mergedObject );
            }
            else
            {
                // Throw an exception for null list elements
                // todo Probably add some options to resolve this case?
                throw new MergeException ( "ListMergeBehavior doesn't support merging null list elements" );
            }
        }

        return base;
    }
}