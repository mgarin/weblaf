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

import com.alee.api.merge.GlobalMergeBehavior;
import com.alee.api.merge.Merge;
import com.alee.api.merge.MergeException;
import com.alee.api.merge.Overwriting;
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

public final class ReflectionMergeBehavior implements GlobalMergeBehavior<Object, Object, Object>
{
    /**
     * todo 1. Make result of merging objects with related (but not equal) classes configurable?
     */

    /**
     * Modifiers of fields to ignore.
     */
    private final List<ModifierType> ignoredModifiers;

    /**
     * Constructs new {@link ReflectionMergeBehavior} ignoring fields with specified modifiers.
     *
     * @param ignoredModifiers modifiers of fields to ignore
     */
    public ReflectionMergeBehavior ( final ModifierType... ignoredModifiers )
    {
        super ();
        this.ignoredModifiers = CollectionUtils.asList ( ignoredModifiers );
    }

    @Override
    public boolean supports ( final Merge merge, final Object base, final Object merged )
    {
        return ClassRelationType.of ( base, merged ).isRelated ();
    }

    @Override
    public Object merge ( final Merge merge, final Object base, final Object merged )
    {
        // Resolving object classes relation
        final ClassRelationType relation = ClassRelationType.of ( base, merged );

        // Choosing resulting object based on relation
        final Object result;
        if ( relation.isDescendant () )
        {
            // When base object class is parent to merged object class we simply return merged object
            // We can in theory merge fields that exist in parent object, but that wouldn't be too obvious
            result = merged;
        }
        else
        {
            // Using fields from merged object as it either has the same class as base object or parent class
            final List<Field> fields = ReflectUtils.getFields ( merged.getClass () );

            // Continue only if there are any fields to merge
            if ( CollectionUtils.notEmpty ( fields ) )
            {
                // Checking whether values should be overwritten instead of being merged
                final boolean overwrite = merged instanceof Overwriting && ( ( Overwriting ) merged ).isOverwrite ();

                // Performing merge for each separate field
                for ( final Field field : fields )
                {
                    // Ensure that this field should not be ignored
                    if ( ReflectUtils.hasNoneOfModifiers ( field, ignoredModifiers ) )
                    {
                        try
                        {
                            // Resolving merge result
                            final Object mergeResult;
                            if ( overwrite )
                            {
                                // Overwriting value if requested
                                mergeResult = field.get ( merged );
                            }
                            else
                            {
                                // Merging non-primitive values
                                final Object objectValue = field.get ( base );
                                final Object mergedValue = field.get ( merged );
                                mergeResult = mergedValue != null ? merge.merge ( objectValue, mergedValue ) : objectValue;
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

            // Return base object where we merged field values
            result = base;
        }
        return result;
    }
}