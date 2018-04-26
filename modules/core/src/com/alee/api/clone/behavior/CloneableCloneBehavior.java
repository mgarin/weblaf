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

import com.alee.api.clone.*;
import com.alee.utils.ReflectUtils;

/**
 * {@link GlobalCloneBehavior} for objects implementing {@link Cloneable} or {@link CloneBehavior}.
 *
 * @param <T> {@link CloneBehavior} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-Clone">How to use Clone</a>
 * @see Clone
 * @see CloneBehavior
 */
public class CloneableCloneBehavior<T extends Cloneable> implements GlobalCloneBehavior<T>
{
    /**
     * Behavior {@link Policy}.
     */
    private final Policy policy;

    /**
     * Constructs new {@link CloneableCloneBehavior}.
     *
     * @param policy behavior {@link Policy}
     */
    public CloneableCloneBehavior ( final Policy policy )
    {
        this.policy = policy;
    }

    @Override
    public boolean supports ( final RecursiveClone clone, final Object object )
    {
        return policy == Policy.strict ? object instanceof CloneBehavior : object instanceof Cloneable;
    }

    @Override
    public T clone ( final RecursiveClone clone, final T object, final int depth )
    {
        final T copy;
        if ( object instanceof CloneBehavior )
        {
            /**
             * Using special behavior predefined in the object within {@link CloneBehavior#clone(RecursiveClone, int)} method.
             * This is the recommended way of providing clone behavior within the object itself.
             * {@link GlobalCloneBehavior} can also be created to support object from within {@link Clone} itself.
             */
            copy = ( ( CloneBehavior<T> ) object ).clone ( clone, depth );
        }
        else if ( object instanceof Cloneable )
        {
            /**
             * Using standard {@link Object#clone()} method.
             * This is only called in case object is instance of {@link Cloneable}.
             */
            copy = ReflectUtils.cloneSafely ( object );
        }
        else
        {
            /**
             * Unable to clone object.
             * Normaly we should not be able to reach this.
             */
            throw new CloneException ( "Unsupported object type: " + object );
        }
        return copy;
    }

    @Override
    public boolean isStorable ()
    {
        return true;
    }

    /**
     * Behavior policy.
     */
    public static enum Policy
    {
        /**
         * Only objects implementing {@link CloneBehavior} are supported.
         * Therefore method {@link #clone()} will only be called for objects implementing {@link CloneBehavior}.
         */
        strict,

        /**
         * Objects implementing {@link CloneBehavior} or {@link Cloneable} are supported.
         * Therefore method {@link #clone()} will only be called for objects implementing {@link CloneBehavior} or {@link Cloneable}.
         */
        adaptive
    }
}