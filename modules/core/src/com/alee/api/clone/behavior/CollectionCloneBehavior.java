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

package com.alee.api.clone.behavior;

import com.alee.api.clone.Clone;
import com.alee.api.clone.CloneException;
import com.alee.api.clone.GlobalCloneBehavior;
import com.alee.api.clone.RecursiveClone;
import com.alee.utils.ReflectUtils;
import com.alee.utils.collection.ImmutableCollection;
import com.alee.utils.collection.ImmutableList;

import java.util.*;

/**
 * {@link GlobalCloneBehavior} for {@link Collection}s.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-Clone">How to use Clone</a>
 * @see Clone
 */
public class CollectionCloneBehavior implements GlobalCloneBehavior<Collection>
{
    @Override
    public boolean supports ( final RecursiveClone clone, final Object object )
    {
        return object instanceof Collection;
    }

    @Override
    public Collection clone ( final RecursiveClone clone, final Collection collection, final int depth )
    {
        try
        {
            /**
             * Creating new {@link Collection} instance.
             * All most commonly used {@link Collection} types are processed separately.
             * todo Probably better/separate solution for immutable & singleton lists?
             */
            final Collection collectionCopy;
            final Class<? extends Collection> collectionClass = collection.getClass ();
            if ( collectionClass == ImmutableCollection.class ||
                    collectionClass == ImmutableList.class ||
                    collectionClass == ArrayList.class ||
                    collectionClass.getCanonicalName ().equals ( "java.util.Collections.SingletonList" ) )
            {
                // ArrayList instance
                collectionCopy = new ArrayList ( collection.size () );
            }
            else if ( collectionClass == LinkedList.class )
            {
                // LinkedList instance
                collectionCopy = new LinkedList ();
            }
            else if ( collectionClass == Vector.class )
            {
                // Vector instance
                collectionCopy = new Vector ( collection.size () );
            }
            else
            {
                // Attempting to create other collection type instance
                collectionCopy = ReflectUtils.createInstance ( collectionClass );
            }

            /**
             * Cloning {@link Collection} values.
             */
            if ( !collection.isEmpty () )
            {
                // Storing collection copy
                // todo This is a wrong reference for ImmutableCollection and ImmutableList
                clone.store ( collection, collectionCopy );

                // Cloning all values
                for ( final Object element : collection )
                {
                    final Object valueCopy = clone.clone ( element, depth + 1 );
                    collectionCopy.add ( valueCopy );
                }
            }

            /**
             * Returning resulting {@link Collection} copy.
             */
            final Collection result;
            if ( collection instanceof ImmutableCollection )
            {
                result = new ImmutableCollection ( collectionCopy );
            }
            else if ( collection instanceof ImmutableList )
            {
                result = new ImmutableList ( collectionCopy );
            }
            else if ( collectionClass.getCanonicalName ().equals ( "java.util.Collections.SingletonList" ) )
            {
                result = Collections.singletonList ( collectionCopy.iterator ().next () );
            }
            else
            {
                result = collectionCopy;
            }
            return result;
        }
        catch ( final Exception e )
        {
            throw new CloneException ( "Unable to clone collection: " + collection, e );
        }
    }

    @Override
    public boolean isStorable ()
    {
        return true;
    }
}