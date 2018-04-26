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
import com.alee.utils.CollectionUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.reflection.ModifierType;
import com.alee.utils.reflection.Unsafe;

import java.lang.reflect.Field;
import java.util.List;

/**
 * {@link GlobalCloneBehavior} for any types of {@link Object}.
 *
 * @param <O> cloned object type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-Clone">How to use Clone</a>
 * @see Clone
 */
public class ReflectionCloneBehavior<O> implements GlobalCloneBehavior<O>
{
    /**
     * Behavior {@link Policy}.
     */
    private final Policy policy;

    /**
     * Modifiers of fields to ignore.
     */
    private final List<ModifierType> ignoredModifiers;

    /**
     * Constructs new {@link ReflectionCloneBehavior} ignoring fields with specified modifiers.
     *
     * @param policy           behavior {@link Policy}
     * @param ignoredModifiers modifiers of fields to ignore
     */
    public ReflectionCloneBehavior ( final Policy policy, final ModifierType... ignoredModifiers )
    {
        this.policy = policy;
        this.ignoredModifiers = CollectionUtils.asList ( ignoredModifiers );
    }

    @Override
    public boolean supports ( final RecursiveClone clone, final Object object )
    {
        return policy == Policy.all || object instanceof Cloneable;
    }

    @Override
    public O clone ( final RecursiveClone clone, final O object, final int depth )
    {
        /**
         * Creating object instance copy.
         * {@link Unsafe} is used to instantiate objects without calling their constructors.
         * This is important to avoid any unwanted field changes within the resulting object.
         */
        final O copy = ( O ) Unsafe.allocateInstance ( object.getClass () );

        /**
         * Cloning field values.
         */
        final List<Field> fields = ReflectUtils.getFields ( object );
        if ( !fields.isEmpty () )
        {
            // Storing object copy
            clone.store ( object, copy );

            // Cloning all field values excluding ones with ignored modifiers or annotated with OmitFieldClone
            for ( final Field field : fields )
            {
                if ( ReflectUtils.hasNoneOfModifiers ( field, ignoredModifiers ) )
                {
                    final boolean preserve = field.getAnnotation ( PreserveOnClone.class ) != null;
                    final boolean omit = field.getAnnotation ( OmitOnClone.class ) != null;
                    if ( preserve && omit )
                    {
                        // Ensure annotations are used properly and objective is clear
                        throw new CloneException ( "Mutually exclusive annotations are used for field: " + field );
                    }
                    else if ( preserve )
                    {
                        try
                        {
                            // Retrieving original object field value
                            final Object value = field.get ( object );

                            // Updating field
                            ReflectUtils.setFieldValue ( copy, field, value );
                        }
                        catch ( final Exception e )
                        {
                            // Something went totally wrong
                            throw new CloneException ( "Unable to preserve object field: " + field, e );
                        }
                    }
                    else if ( !omit )
                    {
                        try
                        {
                            // Retrieving original object field value
                            final Object value = field.get ( object );

                            // Creating value clone if possible
                            final Object valueCopy = clone.clone ( value, depth + 1 );

                            // Updating field
                            ReflectUtils.setFieldValue ( copy, field, valueCopy );
                        }
                        catch ( final Exception e )
                        {
                            // Something went totally wrong
                            throw new CloneException ( "Unable to clone object field: " + field, e );
                        }
                    }
                }
            }
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
         * Only clones objects implementing {@link Cloneable}.
         */
        cloneable,

        /**
         * Clones any objects.
         */
        all
    }
}