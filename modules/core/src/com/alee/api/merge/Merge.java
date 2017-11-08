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

import com.alee.api.matcher.IdentifiableMatcher;
import com.alee.api.merge.behavior.*;
import com.alee.api.merge.nullresolver.SkippingNullResolver;
import com.alee.api.merge.type.ExactTypeMergePolicy;
import com.alee.utils.collection.ImmutableList;
import com.alee.utils.reflection.ModifierType;

import java.io.Serializable;
import java.util.List;

/**
 * Configurable algorithm for merging object instances.
 * It can be customized through the settings provided in its constructor once on creation.
 * To merge any two objects using this class instance simply call {@link #merge(Object, Object)} method.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-Merge">How to use Merge</a>
 * @see MergeNullResolver
 * @see MergePolicy
 * @see GlobalMergeBehavior
 * @see MergeBehavior
 * @see Mergeable
 * @see Overwriting
 */

public final class Merge implements Serializable
{
    /**
     * {@link GlobalMergeBehavior}s list for common merge operations.
     * It contains behaviors for smart merging of any primitive and simple types, arrays, maps and lists.
     * It also contains {@link ReflectionMergeBehavior} which allows merging any other types of objects by accessing their fields directly.
     */
    public static final List<GlobalMergeBehavior> COMMON_MERGE_BEHAVIORS = new ImmutableList<GlobalMergeBehavior> (
            new BasicMergeBehavior (),
            new MergeableMergeBehavior (),
            new IndexArrayMergeBehavior (),
            new MapMergeBehavior (),
            new ListMergeBehavior ( new IdentifiableMatcher () )
    );

    /**
     * One of the most common {@link Merge} algorithms.
     * Whenever {@code merged} value is {@code null} it is skipped and {@code source} value is used instead.
     * Also it only clones objects of the same type, other objects are fully overwritten upon merge.
     */
    public static final Merge COMMON = new Merge (
            new SkippingNullResolver (),
            new ExactTypeMergePolicy (),
            COMMON_MERGE_BEHAVIORS
    );

    /**
     * {@link GlobalMergeBehavior}s list for deep merge operations.
     * It contains behaviors for smart merging of any primitive and simple types, arrays, maps and lists.
     */
    public static final List<GlobalMergeBehavior> DEEP_MERGE_BEHAVIORS = new ImmutableList<GlobalMergeBehavior> (
            new BasicMergeBehavior (),
            new MergeableMergeBehavior (),
            new IndexArrayMergeBehavior (),
            new MapMergeBehavior (),
            new ListMergeBehavior ( new IdentifiableMatcher () ),
            new ReflectionMergeBehavior ( ModifierType.STATIC, ModifierType.TRANSIENT )
    );

    /**
     * More enhanced {@link Merge} algorithm using {@link ReflectionMergeBehavior}.
     * Be careful when using this merge algorithm as it will go through all object references and will merge any existing fields.
     * Whenever {@code merged} value is {@code null} it is skipped and {@code source} value is used instead.
     * Also it only clones objects of the same type, other objects are fully overwritten upon merge.
     */
    public static final Merge DEEP = new Merge (
            new SkippingNullResolver (),
            new ExactTypeMergePolicy (),
            DEEP_MERGE_BEHAVIORS
    );

    /**
     * Object merge {@code null} case resolver.
     * It is used to resolve merge outcome when either of {@code source} and {@code merged} objects are {@code null}.
     *
     * @see MergeNullResolver
     */
    private final MergeNullResolver nullResolver;

    /**
     * Policy which defines whether objects can be merged or not on a global level.
     *
     * @see MergePolicy
     */
    private final MergePolicy policy;

    /**
     * List of behaviors taking part in this merge algorithm instance.
     * These behaviors define which object types can actually be merged and which ones will simply be overwritten.
     *
     * @see GlobalMergeBehavior
     */
    private final List<GlobalMergeBehavior> behaviors;

    /**
     * Constructs new {@link Merge} algorithm.
     *
     * @param nullResolver object merge {@code null} case resolver
     * @param policy       policy which defines whether objects can be merged or not on a global level
     * @param behaviors    behaviors taking part in this merge algorithm instance
     */
    public Merge ( final MergeNullResolver nullResolver, final MergePolicy policy, final GlobalMergeBehavior... behaviors )
    {
        this ( nullResolver, policy, new ImmutableList<GlobalMergeBehavior> ( behaviors ) );
    }

    /**
     * Constructs new {@link Merge} algorithm.
     *
     * @param nullResolver object merge {@code null} case resolver
     * @param policy       policy which defines whether objects can be merged or not on a global level
     * @param behaviors    behaviors taking part in this merge algorithm instance
     */
    public Merge ( final MergeNullResolver nullResolver, final MergePolicy policy, final List<GlobalMergeBehavior> behaviors )
    {
        super ();
        this.nullResolver = nullResolver;
        this.policy = policy;
        this.behaviors = behaviors instanceof ImmutableList ? behaviors : new ImmutableList<GlobalMergeBehavior> ( behaviors );
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
                // Ensuring that merged object doesn't want to fully overwrite source one
                if ( !isOverwrite ( merged ) )
                {
                    // Trying to find fitting merge behavior
                    for ( final GlobalMergeBehavior behavior : behaviors )
                    {
                        // Checking that behavior supports objects
                        if ( behavior.supports ( this, object, merged ) )
                        {
                            // Executing merge behavior
                            return ( T ) behavior.merge ( this, object, merged );
                        }
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

    /**
     * Returns whether or not specified object should overwrite another one upon merge.
     *
     * @param object object to check overwrite flag for
     * @return {@code true} if specified object should overwrite another one upon merge, {@code false} otherwise
     */
    private boolean isOverwrite ( final Object object )
    {
        return object instanceof Overwriting && ( ( Overwriting ) object ).isOverwrite ();
    }
}