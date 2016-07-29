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

package com.alee.api.merge;

import com.alee.api.merge.behavior.*;
import com.alee.api.merge.type.ExactTypeMergePolicy;
import com.alee.api.merge.type.MergePolicy;

import java.util.Arrays;
import java.util.List;

/**
 * Configurable merge algorithm.
 * It can be customized through the settings provided in its constructor once on creation.
 *
 * @author Mikle Garin
 * @see MergePolicy
 * @see MergeBehavior
 */

public final class Merge
{
    /**
     * Common merge algorithm which only merges objects of the same type.
     */
    public static Merge EXACT = new Merge ( false, new ExactTypeMergePolicy (), new MergeableMergeBehavior (), new ArrayMergeBehavior (),
            new MapMergeBehavior (), new ListMergeBehavior () );

    /**
     * Whether or not should merge {@code null} values on top of non-{@code null} ones.
     */
    private final boolean replaceWithNulls;

    /**
     * Policy which defines whether objects can be merged or not on a global level.
     * This interface implementation works pretty much as a condition allowing two objects to be merged.
     */
    private final MergePolicy mergePolicy;

    /**
     * List of behaviors taking part in this merge algorithm instance.
     * These behaviors define which object types can actually be merged and which ones will simply be overwritten.
     */
    private final List<MergeBehavior> behaviors;

    /**
     * Constructs new merge algorithm.
     *
     * @param replaceWithNulls whether or not should merge {@code null} values on top of non-{@code null} ones
     * @param mergePolicy      policy which defines whether objects can be merged or not on a global level
     * @param behaviors        behaviors taking part in this merge algorithm instance
     */
    public Merge ( final boolean replaceWithNulls, final MergePolicy mergePolicy, final MergeBehavior... behaviors )
    {
        this ( replaceWithNulls, mergePolicy, Arrays.asList ( behaviors ) );
    }

    /**
     * Constructs new merge algorithm.
     *
     * @param replaceWithNulls whether or not should merge {@code null} values on top of non-{@code null} ones
     * @param mergePolicy      policy which defines whether objects can be merged or not on a global level
     * @param behaviors        behaviors taking part in this merge algorithm instance
     */
    public Merge ( final boolean replaceWithNulls, final MergePolicy mergePolicy, final List<MergeBehavior> behaviors )
    {
        super ();
        this.replaceWithNulls = replaceWithNulls;
        this.mergePolicy = mergePolicy;
        this.behaviors = behaviors;
    }

    /**
     * Performs merge of the two provided objects and returns resulting object.
     * Depending on the case it might be one of the two provided objects or their merge result.
     *
     * @param object base object
     * @param merged object to merge
     * @param <T>    resulting object type
     * @return merge result
     */
    public <T> T merge ( final Object object, final Object merged )
    {
        // We can only merge non-null objects
        if ( object != null && merged != null )
        {
            // Checking merge possibility
            if ( mergePolicy.accept ( object, merged ) )
            {
                // Trying to find fitting merge behavior
                for ( final MergeBehavior behavior : behaviors )
                {
                    if ( behavior.supports ( object, merged ) )
                    {
                        return ( T ) behavior.merge ( this, object, merged );
                    }
                }

                // Simply replacing value
                return ( T ) merged;
            }
            else
            {
                // Simply replacing value
                return ( T ) merged;
            }
        }
        else if ( merged != null || replaceWithNulls )
        {
            // Replacing value
            return ( T ) merged;
        }
        else
        {
            // Returning existing value
            return ( T ) object;
        }
    }
}