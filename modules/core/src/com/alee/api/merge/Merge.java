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

import com.alee.api.clone.Clone;
import com.alee.api.clone.CloneException;
import com.alee.api.matcher.IdentifiableMatcher;
import com.alee.api.merge.behavior.*;
import com.alee.api.merge.clonepolicy.PerformClonePolicy;
import com.alee.api.merge.clonepolicy.SkipClonePolicy;
import com.alee.api.merge.nullresolver.SkippingNullResolver;
import com.alee.api.merge.unknownresolver.ExceptionUnknownResolver;
import com.alee.utils.collection.ImmutableList;
import com.alee.utils.reflection.ModifierType;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Configurable algorithm for merging object instances.
 * It can be customized through the settings provided in its constructor once on creation.
 * To merge any two objects using this class instance call {@link #merge(Object, Object)} method.
 * To merge multiple objects using this class instance call {@link #merge(Object, Object, Object...)} method.
 * To merge all objects from a single collection using this class instance call {@link #merge(Collection)} method.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-Merge">How to use Merge</a>
 * @see NullResolver
 * @see UnknownResolver
 * @see GlobalMergeBehavior
 * @see MergeBehavior
 * @see Mergeable
 * @see Overwriting
 */
public final class Merge implements Serializable
{
    /**
     * Common lazy {@link Merge} instances cache.
     */
    private static Map<String, Merge> commons;

    /**
     * {@link Clone} algorithm used to copy merged objects.
     * Whether or not it will actually be used depends on {@link ClonePolicy} implementations used.
     */
    private final Clone clone;

    /**
     * {@link ClonePolicy} for base object.
     */
    private final ClonePolicy baseClonePolicy;

    /**
     * {@link ClonePolicy} for merged objects.
     */
    private final ClonePolicy mergedClonePolicy;

    /**
     * Object merge {@code null} case resolver.
     * It is used to resolve merge outcome when either of {@code source} and {@code merged} objects are {@code null}.
     *
     * @see NullResolver
     */
    private final NullResolver nullResolver;

    /**
     * Unknown object types case resolver.
     * It is used to resolve merge outcome when either of {@code source} and {@code merged} objects are not supported by behaviors.
     */
    private final UnknownResolver unknownResolver;

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
     * @param nullResolver    object merge {@code null} case resolver
     * @param unknownResolver unknown object types case resolver
     * @param behaviors       behaviors taking part in this merge algorithm instance
     */
    public Merge ( final NullResolver nullResolver, final UnknownResolver unknownResolver,
                   final GlobalMergeBehavior... behaviors )
    {
        this ( null, new SkipClonePolicy (), new SkipClonePolicy (),
                nullResolver, unknownResolver, new ImmutableList<GlobalMergeBehavior> ( behaviors ) );
    }

    /**
     * Constructs new {@link Merge} algorithm.
     *
     * @param clone             {@link Clone} algorithm used to copy merged objects
     * @param baseClonePolicy   {@link ClonePolicy} for base object
     * @param mergedClonePolicy {@link ClonePolicy} for merged objects
     * @param nullResolver      object merge {@code null} case resolver
     * @param unknownResolver   unknown object types case resolver
     * @param behaviors         behaviors taking part in this merge algorithm instance
     */
    public Merge ( final Clone clone, final ClonePolicy baseClonePolicy, final ClonePolicy mergedClonePolicy,
                   final NullResolver nullResolver, final UnknownResolver unknownResolver,
                   final GlobalMergeBehavior... behaviors )
    {
        this ( clone, baseClonePolicy, mergedClonePolicy,
                nullResolver, unknownResolver, new ImmutableList<GlobalMergeBehavior> ( behaviors ) );
    }

    /**
     * Constructs new {@link Merge} algorithm.
     *
     * @param nullResolver    object merge {@code null} case resolver
     * @param unknownResolver unknown object types case resolver
     * @param behaviors       behaviors taking part in this merge algorithm instance
     */
    public Merge ( final NullResolver nullResolver, final UnknownResolver unknownResolver,
                   final List<GlobalMergeBehavior> behaviors )
    {
        this ( null, new SkipClonePolicy (), new SkipClonePolicy (),
                nullResolver, unknownResolver, behaviors );
    }

    /**
     * Constructs new {@link Merge} algorithm.
     *
     * @param clone             {@link Clone} algorithm used to copy merged objects
     * @param baseClonePolicy   {@link ClonePolicy} for base object
     * @param mergedClonePolicy {@link ClonePolicy} for merged objects
     * @param nullResolver      object merge {@code null} case resolver
     * @param unknownResolver   unknown object types case resolver
     * @param behaviors         behaviors taking part in this merge algorithm instance
     */
    public Merge ( final Clone clone, final ClonePolicy baseClonePolicy, final ClonePolicy mergedClonePolicy,
                   final NullResolver nullResolver, final UnknownResolver unknownResolver,
                   final List<GlobalMergeBehavior> behaviors )
    {
        this.clone = clone;
        this.baseClonePolicy = baseClonePolicy;
        this.mergedClonePolicy = mergedClonePolicy;
        this.nullResolver = nullResolver;
        this.unknownResolver = unknownResolver;
        this.behaviors = behaviors instanceof ImmutableList ? behaviors : new ImmutableList<GlobalMergeBehavior> ( behaviors );
    }

    /**
     * Performs merge of the two provided objects and returns resulting object.
     * Depending on the case it might be one of the two provided objects, their copy or their merge result.
     * Whether or not {@code base} and/or {@code merged} will be copied depends on {@link #baseClonePolicy} and {@link #mergedClonePolicy}.
     *
     * @param base   base object
     * @param merged object to merge
     * @param <T>    resulting object type
     * @return merge result
     */
    public <T> T merge ( final Object base, final Object merged )
    {
        final Object baseCopy = cloneBase ( base );
        final Object mergedCopy = cloneMerged ( merged );
        final InternalMerge internalMerge = new InternalMerge ();
        return internalMerge.merge ( Object.class, baseCopy, mergedCopy, 0 );
    }

    /**
     * Performs merge of all provided objects and returns resulting object.
     * Depending on the case it might be one of the two provided objects, their copy or their merge result.
     * Whether or not {@code base} and/or {@code merged} will be copied depends on {@link #baseClonePolicy} and {@link #mergedClonePolicy}.
     *
     * @param base   base object
     * @param merged object to merge
     * @param more   more objects to merge
     * @param <T>    resulting object type
     * @return merge result
     */
    public <T> T merge ( final Object base, final Object merged, final Object... more )
    {
        final Object baseCopy = cloneBase ( base );
        final Object mergedCopy = cloneMerged ( merged );
        final InternalMerge internalMerge = new InternalMerge ();
        Object result = internalMerge.merge ( Object.class, baseCopy, mergedCopy, 0 );
        for ( final Object another : more )
        {
            final Object anotherCopy = cloneMerged ( another );
            result = internalMerge.merge ( Object.class, result, anotherCopy, 0 );
        }
        return ( T ) result;
    }

    /**
     * Performs merge of all provided objects and returns resulting object.
     * Depending on the case it might be one of the provided objects, their copy or their merge result.
     * Whether or not {@code base} and/or {@code merged} will be copied depends on {@link #baseClonePolicy} and {@link #mergedClonePolicy}.
     *
     * @param objects objects to merge
     * @param <T>     resulting object type
     * @return merge result
     */
    public <T> T merge ( final Collection<?> objects )
    {
        if ( objects.size () > 0 )
        {
            final Iterator<?> iterator = objects.iterator ();
            final InternalMerge internalMerge = new InternalMerge ();
            Object result = cloneBase ( iterator.next () );
            while ( iterator.hasNext () )
            {
                final Object mergedCopy = cloneMerged ( iterator.next () );
                result = internalMerge.merge ( Object.class, result, mergedCopy, 0 );
            }
            return ( T ) result;
        }
        else
        {
            throw new MergeException ( "At least one object must be specified for merge operation" );
        }
    }

    /**
     * Returns either object or its clone based on {@link #baseClonePolicy}.
     * This is an utility method mostly for {@link GlobalMergeBehavior} implementations.
     *
     * @param base object to clone
     * @return either object or its clone based on {@link #baseClonePolicy}
     */
    private Object cloneBase ( final Object base )
    {
        return baseClonePolicy.clone ( clone, base );
    }

    /**
     * Returns either object or its clone based on {@link #mergedClonePolicy}.
     * This is an utility method mostly for {@link GlobalMergeBehavior} implementations.
     *
     * @param merged object to clone
     * @return either object or its clone based on {@link #mergedClonePolicy}
     */
    private Object cloneMerged ( final Object merged )
    {
        return mergedClonePolicy.clone ( clone, merged );
    }

    /**
     * {@link RecursiveMerge} implementation providing access to different {@link Merge} methods.
     * It is used to process recursive merge calls differently from how public {@link Merge} methods process them.
     */
    private class InternalMerge implements RecursiveMerge
    {
        @Override
        public Object overwrite ( final Object base, final Object merged )
        {
            if ( base != null && merged != null )
            {
                return merged;
            }
            else
            {
                return nullResolver.resolve ( this, base, merged );
            }
        }

        @Override
        public <T> T merge ( final Class type, final Object base, final Object merged, final int depth )
        {
            final T result;
            if ( base != null && merged != null )
            {
                // Ensuring that merged object doesn't want to fully overwrite source one
                if ( !( merged instanceof Overwriting && ( ( Overwriting ) merged ).isOverwrite () ) )
                {
                    // Trying to find fitting merge behavior
                    Object mergeResult = null;
                    for ( final GlobalMergeBehavior behavior : behaviors )
                    {
                        // Checking that behavior supports objects
                        if ( behavior.supports ( this, type, base, merged ) )
                        {
                            // Executing merge behavior
                            mergeResult = behavior.merge ( this, type, base, merged, depth );
                            break;
                        }
                    }

                    // Resolving result object
                    result = mergeResult != null ? ( T ) mergeResult : ( T ) unknownResolver.resolve ( this, base, merged );
                }
                else
                {
                    // Merged fully overwrites base
                    result = ( T ) merged;
                }
            }
            else
            {
                // Resolving null case outcome
                result = ( T ) nullResolver.resolve ( this, base, merged );
            }
            return result;
        }

        @Override
        public <T> T mergeFields ( final Class type, final Object base, final Object merged, final int depth )
        {
            for ( final GlobalMergeBehavior behavior : behaviors )
            {
                if ( behavior instanceof ReflectionMergeBehavior )
                {
                    return ( T ) behavior.merge ( this, type, base, merged, depth );
                }
            }
            throw new CloneException ( "There is no ReflectionMergeBehavior in Merge algorithm" );
        }
    }

    /**
     * Returns {@link Merge} algorithm that is able to merge basic object types.
     * It also creates copy for both base and merged objects before merging them together.
     *
     * @return {@link Merge} algorithm that is able to merge basic object types
     */
    public static Merge basic ()
    {
        final String identifier = "basic";
        Merge merge = commonInstance ( identifier );
        if ( merge == null )
        {
            merge = new Merge (
                    Clone.deep (),
                    new PerformClonePolicy (),
                    new PerformClonePolicy (),
                    new SkippingNullResolver (),
                    new ExceptionUnknownResolver (),
                    new BasicMergeBehavior (),
                    new MergeableMergeBehavior (),
                    new IndexArrayMergeBehavior (),
                    new MapMergeBehavior (),
                    new ListMergeBehavior ( new IdentifiableMatcher () )
            );
            commons.put ( identifier, merge );
        }
        return merge;
    }

    /**
     * Returns {@link Merge} algorithm that is able to merge basic object types.
     *
     * @return {@link Merge} algorithm that is able to merge basic object types
     */
    public static Merge basicRaw ()
    {
        final String identifier = "basicRaw";
        Merge merge = commonInstance ( identifier );
        if ( merge == null )
        {
            merge = new Merge (
                    new SkippingNullResolver (),
                    new ExceptionUnknownResolver (),
                    new BasicMergeBehavior (),
                    new MergeableMergeBehavior (),
                    new IndexArrayMergeBehavior (),
                    new MapMergeBehavior (),
                    new ListMergeBehavior ( new IdentifiableMatcher () )
            );
            commons.put ( identifier, merge );
        }
        return merge;
    }

    /**
     * Returns {@link Merge} algorithm that can also merge custom objects through {@link ReflectionMergeBehavior}.
     * Be careful when using this merge algorithm as it will go through all object references and will merge any existing fields.
     * It also creates copy for both base and merged objects before merging them together.
     *
     * @return {@link Merge} algorithm that can also merge custom objects through {@link ReflectionMergeBehavior}
     */
    public static Merge deep ()
    {
        final String identifier = "deep";
        Merge merge = commonInstance ( identifier );
        if ( merge == null )
        {
            merge = new Merge (
                    Clone.deep (),
                    new PerformClonePolicy (),
                    new PerformClonePolicy (),
                    new SkippingNullResolver (),
                    new ExceptionUnknownResolver (),
                    new BasicMergeBehavior (),
                    new MergeableMergeBehavior (),
                    new IndexArrayMergeBehavior (),
                    new MapMergeBehavior (),
                    new ListMergeBehavior ( new IdentifiableMatcher () ),
                    new ReflectionMergeBehavior ( ReflectionMergeBehavior.Policy.mergeable, ModifierType.STATIC )
            );
            commons.put ( identifier, merge );
        }
        return merge;
    }

    /**
     * Returns {@link Merge} algorithm that can also merge custom objects through {@link ReflectionMergeBehavior}.
     * Be careful when using this merge algorithm as it will go through all object references and will merge any existing fields.
     *
     * @return {@link Merge} algorithm that can also merge custom objects through {@link ReflectionMergeBehavior}
     */
    public static Merge deepRaw ()
    {
        final String identifier = "deepRaw";
        Merge merge = commonInstance ( identifier );
        if ( merge == null )
        {
            merge = new Merge (
                    new SkippingNullResolver (),
                    new ExceptionUnknownResolver (),
                    new BasicMergeBehavior (),
                    new MergeableMergeBehavior (),
                    new IndexArrayMergeBehavior (),
                    new MapMergeBehavior (),
                    new ListMergeBehavior ( new IdentifiableMatcher () ),
                    new ReflectionMergeBehavior ( ReflectionMergeBehavior.Policy.mergeable, ModifierType.STATIC )
            );
            commons.put ( identifier, merge );
        }
        return merge;
    }

    /**
     * Returns common {@link Merge} instance by its indentifier.
     *
     * @param identifier {@link Merge} instance indentifier
     * @return common {@link Merge} instance by its indentifier
     */
    private static Merge commonInstance ( final String identifier )
    {
        if ( commons == null )
        {
            synchronized ( Merge.class )
            {
                if ( commons == null )
                {
                    commons = new ConcurrentHashMap<String, Merge> ( 4 );
                }
            }
        }
        return commons.get ( identifier );
    }
}