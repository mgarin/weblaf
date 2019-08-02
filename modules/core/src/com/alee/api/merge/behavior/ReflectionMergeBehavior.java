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

package com.alee.api.merge.behavior;

import com.alee.api.merge.*;
import com.alee.utils.CollectionUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.reflection.ClassRelationType;
import com.alee.utils.reflection.ModifierType;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Tricky merge behavior for any types of {@link Object} with related class types.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-Merge">How to use Merge</a>
 * @see Merge
 * @see ClassRelationType#isRelated()
 */
public class ReflectionMergeBehavior implements GlobalMergeBehavior<Object, Object, Object>
{
    /**
     * todo 1. Make result of merging objects with related (but not equal) classes configurable?
     */

    /**
     * Behavior {@link Policy}.
     */
    private final Policy policy;

    /**
     * Modifiers of fields to ignore.
     */
    private final List<ModifierType> ignoredModifiers;

    /**
     * Constructs new {@link ReflectionMergeBehavior} ignoring fields with specified modifiers.
     *
     * @param policy           behavior {@link Policy}
     * @param ignoredModifiers modifiers of fields to ignore
     */
    public ReflectionMergeBehavior ( final Policy policy, final ModifierType... ignoredModifiers )
    {
        this.policy = policy;
        this.ignoredModifiers = CollectionUtils.asList ( ignoredModifiers );
    }

    @Override
    public boolean supports ( final RecursiveMerge merge, final Class<Object> type, final Object base, final Object merged )
    {
        return ( policy == Policy.all || base instanceof Mergeable && merged instanceof Mergeable ) &&
                type.isAssignableFrom ( base.getClass () ) && type.isAssignableFrom ( merged.getClass () );
    }

    @Override
    public Object merge ( final RecursiveMerge merge, final Class type, final Object base, final Object merged, final int depth )
    {
        // Resolving object classes relation
        final ClassRelationType relation = ClassRelationType.of ( base, merged );

        // Choosing resulting object based on relation
        final Object result;
        if ( relation.isSame () || relation.isAncestor () )
        {
            // Using fields from merged object as it is either an instance of the same or parent class
            final List<Field> fields = ReflectUtils.getFields ( merged.getClass () );

            // Continue only if there are any fields to merge
            if ( CollectionUtils.notEmpty ( fields ) )
            {
                // Performing merge for each separate field
                for ( final Field field : fields )
                {
                    // Ensure that this field should not be ignored
                    if ( ReflectUtils.hasNoneOfModifiers ( field, ignoredModifiers ) )
                    {
                        final Class<?> fieldType = field.getType ();
                        if ( field.getAnnotation ( OmitOnMerge.class ) != null )
                        {
                            try
                            {
                                // Retrieving default value
                                final Object value;
                                if ( fieldType.isPrimitive () )
                                {
                                    value = ReflectUtils.getDefaultPrimitiveValue ( fieldType );
                                }
                                else
                                {
                                    value = null;
                                }

                                // Nullifying field value
                                ReflectUtils.setFieldValue ( base, field, value );
                            }
                            catch ( final Exception e )
                            {
                                // Throwing merge exception
                                final String message = "Unable to omit field {%s} value";
                                throw new MergeException ( String.format ( message, field ), e );
                            }
                        }
                        else if ( field.getAnnotation ( PreserveOnMerge.class ) == null )
                        {
                            try
                            {
                                // Resolving merge result
                                final Object mergeResult;
                                final Object baseValue = field.get ( base );
                                final Object mergedValue = field.get ( merged );
                                if ( field.getAnnotation ( OverwriteOnMerge.class ) == null )
                                {
                                    /**
                                     * Allowing {@link Merge} to merge field values.
                                     * It is important to delegate this task to {@link Merge} as soon as possible to preserve its behavior.
                                     */
                                    mergeResult = merge.merge ( fieldType, baseValue, mergedValue, depth + 1 );
                                }
                                else
                                {
                                    /**
                                     * Allowing {@link Merge} to overwrite field value.
                                     * We have to rely on {@link Merge} due to merged object possibly being {@code null}.
                                     */
                                    mergeResult = merge.overwrite ( baseValue, mergedValue );
                                }

                                // Saving merged value
                                ReflectUtils.setFieldValue ( base, field, mergeResult );
                            }
                            catch ( final Exception e )
                            {
                                // Throwing merge exception
                                final String message = "Unable to merge field {%s} values for objects {%s} and {%s}";
                                throw new MergeException ( String.format ( message, field, base, merged ), e );
                            }
                        }
                    }
                }
            }

            // Return base object where we merged field values
            result = base;
        }
        else
        {
            // When base object class is parent to merged object class we simply return merged object
            // We can in theory merge fields that exist in parent object, but that wouldn't be too obvious
            result = merged;
        }
        return result;
    }

    /**
     * Behavior policy.
     */
    public static enum Policy
    {
        /**
         * Only merges objects implementing {@link Mergeable}.
         */
        mergeable,

        /**
         * Merges any objects.
         */
        all
    }
}