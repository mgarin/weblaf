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

import java.lang.reflect.Array;

/**
 * {@link GlobalCloneBehavior} for arrays.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-Clone">How to use Clone</a>
 * @see Clone
 */
public class ArrayCloneBehavior implements GlobalCloneBehavior<Object>
{
    @Override
    public boolean supports ( final RecursiveClone clone, final Object object )
    {
        return object.getClass ().isArray ();
    }

    @Override
    public Object clone ( final RecursiveClone clone, final Object array, final int depth )
    {
        try
        {
            /**
             * Creating new array instance.
             */
            final Class<?> type = array.getClass ().getComponentType ();
            final int length = Array.getLength ( array );
            final Object arrayCopy = Array.newInstance ( type, length );

            /**
             * Cloning array values.
             */
            if ( length > 0 )
            {
                // Storing array copy
                clone.store ( array, arrayCopy );

                // Cloning all values
                for ( int i = 0; i < length; i++ )
                {
                    final Object value = Array.get ( array, i );
                    final Object valueCopy = clone.clone ( value, depth + 1 );
                    Array.set ( arrayCopy, i, valueCopy );
                }
            }

            /**
             * Returning resulting array copy.
             */
            return arrayCopy;
        }
        catch ( final Exception e )
        {
            throw new CloneException ( "Unable to instantiate array: " + array.getClass (), e );
        }
    }

    @Override
    public boolean isStorable ()
    {
        return true;
    }
}