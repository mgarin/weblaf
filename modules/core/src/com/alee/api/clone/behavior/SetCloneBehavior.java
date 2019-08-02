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
import com.alee.utils.collection.ImmutableSet;
import com.alee.utils.collection.WeakHashSet;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * {@link GlobalCloneBehavior} for {@link Set}s.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-Clone">How to use Clone</a>
 * @see Clone
 */
public class SetCloneBehavior implements GlobalCloneBehavior<Set>
{
    @Override
    public boolean supports ( final RecursiveClone clone, final Object object )
    {
        return object instanceof Set;
    }

    @Override
    public Set clone ( final RecursiveClone clone, final Set set, final int depth )
    {
        try
        {
            /**
             * Creating new {@link Set} instance.
             * All most commonly used {@link Set} types are processed separately.
             */
            final Set setCopy;
            final Class<? extends Set> setClass = set.getClass ();
            if ( setClass == ImmutableSet.class ||
                    setClass == HashSet.class )
            {
                // HashSet instance
                setCopy = new HashSet ( set.size () );
            }
            else if ( setClass == WeakHashSet.class )
            {
                // WeakHashSet instance
                setCopy = new WeakHashSet ( set.size () );
            }
            else if ( setClass == TreeSet.class )
            {
                // TreeSet instance
                setCopy = new TreeSet ();
            }
            else
            {
                // Attempting to create other set type instance
                setCopy = ReflectUtils.createInstance ( setClass );
            }

            /**
             * Cloning {@link Set} values.
             */
            if ( !set.isEmpty () )
            {
                // Storing set copy
                // todo This is a wrong reference for ImmutableSet
                clone.store ( set, setCopy );

                // Cloning all values
                for ( final Object element : set )
                {
                    final Object valueCopy = clone.clone ( element, depth + 1 );
                    setCopy.add ( valueCopy );
                }
            }

            /**
             * Returning resulting {@link Set} copy.
             */
            final Set result;
            if ( set instanceof ImmutableSet )
            {
                result = new ImmutableSet ( setCopy );
            }
            else
            {
                result = setCopy;
            }
            return result;
        }
        catch ( final Exception e )
        {
            throw new CloneException ( "Unable to clone set: " + set, e );
        }
    }

    @Override
    public boolean isStorable ()
    {
        return true;
    }
}