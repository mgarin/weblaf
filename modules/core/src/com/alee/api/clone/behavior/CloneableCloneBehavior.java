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
import com.alee.api.clone.GlobalCloneBehavior;
import com.alee.api.clone.RecursiveClone;
import com.alee.utils.ReflectUtils;

/**
 * {@link GlobalCloneBehavior} for objects implementing {@link Cloneable}.
 *
 * @param <T> {@link Cloneable} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-Clone">How to use Clone</a>
 * @see Clone
 * @see Cloneable
 */
public class CloneableCloneBehavior<T extends Cloneable> implements GlobalCloneBehavior<T>
{
    @Override
    public boolean supports ( final RecursiveClone clone, final Object object )
    {
        return object instanceof Cloneable;
    }

    @Override
    public T clone ( final RecursiveClone clone, final T object, final int depth )
    {
        /**
         * Using standard {@link Object#clone()} method.
         * This is only called in case object is instance of {@link Cloneable}.
         */
        return ReflectUtils.cloneSafely ( object );
    }

    @Override
    public boolean isStorable ()
    {
        return true;
    }
}