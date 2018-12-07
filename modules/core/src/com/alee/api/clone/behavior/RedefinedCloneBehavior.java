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
import com.alee.api.clone.CloneBehavior;
import com.alee.api.clone.GlobalCloneBehavior;
import com.alee.api.clone.RecursiveClone;

/**
 * {@link GlobalCloneBehavior} for objects implementing {@link CloneBehavior}.
 *
 * @param <T> {@link CloneBehavior} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-Clone">How to use Clone</a>
 * @see Clone
 * @see CloneBehavior
 */
public class RedefinedCloneBehavior<T extends CloneBehavior<T>> implements GlobalCloneBehavior<T>
{
    @Override
    public boolean supports ( final RecursiveClone clone, final Object object )
    {
        return object instanceof CloneBehavior;
    }

    @Override
    public T clone ( final RecursiveClone clone, final T object, final int depth )
    {
        /**
         * Using special behavior predefined in the object within {@link CloneBehavior#clone(RecursiveClone, int)} method.
         * This is the recommended way of providing clone behavior within the object itself.
         * {@link GlobalCloneBehavior} can also be created to support object from within {@link Clone} itself.
         */
        return object.clone ( clone, depth );
    }

    @Override
    public boolean isStorable ()
    {
        return true;
    }
}