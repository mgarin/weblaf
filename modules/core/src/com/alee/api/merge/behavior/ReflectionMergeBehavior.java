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

import com.alee.api.Overwriting;
import com.alee.api.merge.Merge;
import com.alee.api.merge.MergeException;
import com.alee.utils.CollectionUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.reflection.ClassRelationType;
import com.alee.utils.reflection.ModifierType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Merge behavior for any types of {@link Object}.
 *
 * @author Mikle Garin
 */

public final class ReflectionMergeBehavior implements MergeBehavior
{
    /**
     * Modifiers of fields to ignore.
     */
    private final ArrayList<ModifierType> ignoredModifiers;

    /**
     * Constructs new {@link ReflectionMergeBehavior} ignoring static and transient fields.
     */
    public ReflectionMergeBehavior ()
    {
        this ( ModifierType.STATIC, ModifierType.TRANSIENT );
    }

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
    public boolean supports ( final Object object, final Object merged )
    {
        return !ClassRelationType.of ( object, merged ).isUnrelated ();
    }

    @Override
    public <T> T merge ( final Merge merge, final Object object, final Object merged )
    {
        // Resolving class relations
        final Class<?> objectClass = object.getClass ();
        final Class<?> mergedClass = merged.getClass ();
        final ClassRelationType relation = ClassRelationType.of ( objectClass, mergedClass );

        // Resolving fields that should be merged
        final Class<?> fieldsClass = relation.isSame () || relation.isAncestor () ? objectClass : mergedClass;
        final List<Field> fields = ReflectUtils.getFields ( fieldsClass );

        // Continue only if there are any fields to merge
        if ( !CollectionUtils.isEmpty ( fields ) )
        {
            // Checking whether values should be overwritten instead of being merged
            final boolean overwrite = merged instanceof Overwriting && ( ( Overwriting ) merged ).isOverwrite ();

            // Performing merge for each separate field
            for ( final Field field : fields )
            {
                // Ensure that this field should not be ignored
                if ( !CollectionUtils.isEmpty ( ignoredModifiers ) )
                {
                    boolean ignore = false;
                    final List<ModifierType> modifiers = ModifierType.get ( field );
                    for ( final ModifierType ignoredModifier : ignoredModifiers )
                    {
                        if ( modifiers.contains ( ignoredModifier ) )
                        {
                            ignore = true;
                            break;
                        }
                    }
                    if ( ignore )
                    {
                        continue;
                    }
                }

                try
                {
                    // Resolving merge result
                    final Object mergeResult;
                    if ( overwrite )
                    {
                        // Overwriting value
                        mergeResult = field.get ( merged );
                    }
                    else
                    {
                        // Merging values
                        final Object objectValue = field.get ( object );
                        final Object mergedValue = field.get ( merged );
                        mergeResult = mergedValue != null ? merge.merge ( objectValue, mergedValue ) : objectValue;
                    }

                    // Saving merged value
                    field.set ( object, mergeResult );
                }
                catch ( final IllegalAccessException e )
                {
                    // Throwing merge exception
                    throw new MergeException ( "Unable to merge field " + field + " values for objects " + object + " and " + merged, e );
                }
            }
        }

        return ( T ) object;
    }
}