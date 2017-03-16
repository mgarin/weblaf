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
import com.alee.api.merge.nullresolver.SkippingNullResolver;
import com.alee.api.merge.type.ExactTypeMergePolicy;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Configurable merge algorithm.
 * It can be customized through the settings provided in its constructor once on creation.
 * To merge any two objects using this class instance simply call {@link #merge(Object, Object)} method.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-Merge">How to use Merge</a>
 * @see MergeNullResolver
 * @see MergePolicy
 * @see MergeBehavior
 * @see Mergeable
 */

public final class Merge implements Serializable
{
    /**
     * Most common merge algorithm which only merges objects of the same type.
     */
    public static Merge EXACT = new Merge (
            new SkippingNullResolver (),
            new ExactTypeMergePolicy (),
            new BasicMergeBehavior (),
            new MergeableMergeBehavior (),
            new IndexArrayMergeBehavior (),
            new MapMergeBehavior (),
            new ListMergeBehavior (),
            new ReflectionMergeBehavior ()
    );

    /**
     * Object merge {@code null} case resolver.
     */
    private final MergeNullResolver nullResolver;

    /**
     * Policy which defines whether objects can be merged or not on a global level.
     */
    private final MergePolicy policy;

    /**
     * List of behaviors taking part in this merge algorithm instance.
     * These behaviors define which object types can actually be merged and which ones will simply be overwritten.
     */
    private final List<MergeBehavior> behaviors;

    /**
     * Constructs new merge algorithm.
     *
     * @param nullResolver object merge {@code null} case resolver
     * @param policy       policy which defines whether objects can be merged or not on a global level
     * @param behaviors    behaviors taking part in this merge algorithm instance
     */
    public Merge ( final MergeNullResolver nullResolver, final MergePolicy policy, final MergeBehavior... behaviors )
    {
        this ( nullResolver, policy, Arrays.asList ( behaviors ) );
    }

    /**
     * Constructs new merge algorithm.
     *
     * @param nullResolver object merge {@code null} case resolver
     * @param policy       policy which defines whether objects can be merged or not on a global level
     * @param behaviors    behaviors taking part in this merge algorithm instance
     */
    public Merge ( final MergeNullResolver nullResolver, final MergePolicy policy, final List<MergeBehavior> behaviors )
    {
        super ();
        this.nullResolver = nullResolver;
        this.policy = policy;
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
            if ( policy.accept ( this, object, merged ) )
            {
                // Trying to find fitting merge behavior
                for ( final MergeBehavior behavior : behaviors )
                {
                    // Checking that behavior supports objects
                    if ( behavior.supports ( this, object, merged ) )
                    {
                        // Executing merge behavior
                        return ( T ) behavior.merge ( this, object, merged );
                    }
                }
            }

            // Simply replacing value
            return ( T ) merged;
        }
        else
        {
            // Resolving null case outcome
            return ( T ) nullResolver.resolve ( this, object, merged );
        }
    }
}