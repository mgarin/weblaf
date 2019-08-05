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

package com.alee.api.clone;

import com.alee.api.clone.behavior.*;
import com.alee.api.clone.unknownresolver.ExceptionUnknownResolver;
import com.alee.utils.collection.ImmutableList;
import com.alee.utils.reflection.ModifierType;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Configurable algorithm for cloning object instances.
 * It can be customized through the settings provided in its constructor once on creation.
 * To clone any object using this class instance call {@link #clone(Object)} method.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-Clone">How to use Clone</a>
 * @see Clone
 * @see UnknownResolver
 * @see GlobalCloneBehavior
 * @see CloneBehavior
 * @see Cloneable
 */
public final class Clone implements Serializable
{
    /**
     * Common lazy {@link Clone} instances cache.
     *
     * @see #basic()
     * @see #deep()
     */
    private static Map<String, Clone> commons;

    /**
     * Unknown object types case resolver.
     * It is used to resolve clone outcome when cloned objects are not supported by behaviors.
     *
     * @see UnknownResolver
     */
    private final UnknownResolver unknownResolver;

    /**
     * List of behaviors taking part in this clone algorithm instance.
     * These behaviors define which object types can actually be clone and which ones will simply be retained.
     *
     * @see GlobalCloneBehavior
     */
    private final List<GlobalCloneBehavior> behaviors;

    /**
     * Constructs new {@link Clone} algorithm.
     *
     * @param unknownResolver unknown object types case resolver
     * @param behaviors       behaviors taking part in this clone algorithm instance
     */
    public Clone ( final UnknownResolver unknownResolver, final GlobalCloneBehavior... behaviors )
    {
        this ( unknownResolver, new ImmutableList<GlobalCloneBehavior> ( behaviors ) );
    }

    /**
     * Constructs new {@link Clone} algorithm.
     *
     * @param unknownResolver unknown object types case resolver
     * @param behaviors       behaviors taking part in this clone algorithm instance
     */
    public Clone ( final UnknownResolver unknownResolver, final List<GlobalCloneBehavior> behaviors )
    {
        this.unknownResolver = unknownResolver;
        this.behaviors = behaviors instanceof ImmutableList ? behaviors : new ImmutableList<GlobalCloneBehavior> ( behaviors );
    }

    /**
     * Returns clone of the specified object.
     * New {@link Clone.InternalClone} instance is used for every separate clone operation.
     * That is necessary because {@link Clone.InternalClone} stores cloned object references internally to preserve object links.
     *
     * @param object object to clone, should never be {@code null}
     * @param <T>    cloned object type
     * @return clone of the specified object
     */
    public <T> T clone ( final T object )
    {
        return new InternalClone ().clone ( object, 0 );
    }

    /**
     * {@link RecursiveClone} implementation providing access to different {@link Clone} methods.
     * It is used to process recursive clone calls differently from how public {@link RecursiveClone#clone(Object, int)} processes them.
     */
    private class InternalClone extends AbstractRecursiveClone
    {
        @Override
        public <T> T clone ( final T object, final int depth )
        {
            final T result;
            if ( object != null )
            {
                // Checking cached copy references
                final Object cached = retrieve ( object );
                if ( cached == null )
                {
                    // Trying to find fitting clone behavior
                    Object cloneResult = null;
                    GlobalCloneBehavior resultBehavior = null;
                    for ( final GlobalCloneBehavior behavior : behaviors )
                    {
                        // Checking that behavior supports object
                        if ( behavior.supports ( this, object ) )
                        {
                            // Executing clone behavior
                            cloneResult = behavior.clone ( this, object, depth );
                            resultBehavior = behavior;
                            break;
                        }
                    }

                    // Resolving result object
                    if ( resultBehavior != null )
                    {
                        // Result acquired
                        result = ( T ) cloneResult;

                        // Storing object if it not root
                        if ( depth > 0 && resultBehavior.isStorable () )
                        {
                            store ( object, result );
                        }
                    }
                    else
                    {
                        // Unknown type, trying to resolve it
                        result = ( T ) unknownResolver.resolve ( this, object );

                        // Storing object if it not root
                        if ( depth > 0 )
                        {
                            store ( object, result );
                        }
                    }
                }
                else
                {
                    // Returning cached copy reference
                    result = ( T ) cached;
                }
            }
            else
            {
                // Result is null
                result = null;
            }
            return result;
        }

        @Override
        public <T> T cloneFields ( final T object, final int depth )
        {
            for ( final GlobalCloneBehavior behavior : behaviors )
            {
                if ( behavior instanceof ReflectionCloneBehavior )
                {
                    return ( T ) behavior.clone ( this, object, depth );
                }
            }
            throw new CloneException ( "There is no ReflectionCloneBehavior in Clone algorithm" );
        }
    }

    /**
     * Returns {@link Clone} algorithm that is able to clone basic object types as well as simple {@link Collection}s and {@link Map}s.
     * This algorithm is most useful for cases when you don't want to clone any unwanted objects and only focus on few basic types and
     * some extra types which have strictly defined clone behavior through implementing {@link CloneBehavior}.
     *
     * @return {@link Clone} algorithm that is able to clone basic object types as well as simple {@link Collection}s and {@link Map}s
     */
    public static Clone basic ()
    {
        final String identifier = "basic";
        Clone clone = commonInstance ( identifier );
        if ( clone == null )
        {
            clone = new Clone (
                    new ExceptionUnknownResolver (),
                    new BasicCloneBehavior (),
                    new RedefinedCloneBehavior (),
                    new ArrayCloneBehavior (),
                    new MapCloneBehavior (),
                    new SetCloneBehavior (),
                    new CollectionCloneBehavior ()
            );
            commons.put ( identifier, clone );
        }
        return clone;
    }

    /**
     * Returns {@link Clone} algorithm that can also clone custom objects through {@link ReflectionCloneBehavior}.
     * Be careful when using this clone algorithm as it will go through all object references and will clone any existing fields.
     * This algorithm is most useful for cases of cloning complex multi-level structures of objects where defining every clone operation
     * can be difficult and vulnerable to mistakes.
     *
     * @return {@link Clone} algorithm that can also clone custom objects through {@link ReflectionCloneBehavior}
     */
    public static Clone deep ()
    {
        final String identifier = "deep";
        Clone clone = commonInstance ( identifier );
        if ( clone == null )
        {
            clone = new Clone (
                    new ExceptionUnknownResolver (),
                    new BasicCloneBehavior (),
                    new RedefinedCloneBehavior (),
                    new ArrayCloneBehavior (),
                    new MapCloneBehavior (),
                    new SetCloneBehavior (),
                    new CollectionCloneBehavior (),
                    new ReflectionCloneBehavior ( ReflectionCloneBehavior.Policy.cloneable, ModifierType.STATIC )
            );
            commons.put ( identifier, clone );
        }
        return clone;
    }

    /**
     * Returns {@link Clone} algorithm similar to {@link #deep()} that also uses base {@link Object#clone()} API.
     * Be careful when using this clone algorithm as it will go through all object references and will clone any existing fields.
     * Also anything that implements {@link Cloneable} (except for {@link Collection}s and {@link Map}s) will be cloned through
     * {@link Object#clone()} API instead, so be careful when cloning anything using this particular {@link Clone} algorithm.
     * Just like {@link #deep()} clone algorithm this one is most useful for cloning complex multi-level structures of objects while also
     * honoring base {@link Object#clone()} API while going through the structure of objects.
     *
     * @return {@link Clone} algorithm similar to {@link #deep()} that also uses base {@link Object#clone()} API
     */
    public static Clone adaptive ()
    {
        final String identifier = "adaptive";
        Clone clone = commonInstance ( identifier );
        if ( clone == null )
        {
            clone = new Clone (
                    new ExceptionUnknownResolver (),
                    new BasicCloneBehavior (),
                    new RedefinedCloneBehavior (),
                    new ArrayCloneBehavior (),
                    new MapCloneBehavior (),
                    new SetCloneBehavior (),
                    new CollectionCloneBehavior (),
                    new CloneableCloneBehavior (),
                    new ReflectionCloneBehavior ( ReflectionCloneBehavior.Policy.cloneable, ModifierType.STATIC )
            );
            commons.put ( identifier, clone );
        }
        return clone;
    }

    /**
     * Returns common {@link Clone} instance by its indentifier.
     *
     * @param identifier {@link Clone} instance indentifier
     * @return common {@link Clone} instance by its indentifier
     */
    private static Clone commonInstance ( final String identifier )
    {
        if ( commons == null )
        {
            synchronized ( Clone.class )
            {
                if ( commons == null )
                {
                    commons = new ConcurrentHashMap<String, Clone> ( 4 );
                }
            }
        }
        return commons.get ( identifier );
    }
}