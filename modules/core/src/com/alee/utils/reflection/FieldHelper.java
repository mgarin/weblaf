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

package com.alee.utils.reflection;

import com.alee.utils.ReflectUtils;

import java.lang.reflect.Field;

/**
 * Field helper
 *
 * @author Alexandr Zernov
 */
public final class FieldHelper
{
    /*
     * Note: modules/core/java12/com/alee/utils/reflection/FieldHelper.java
     */

    /**
     * Changes {@link Field} modifiers.
     * Be aware that this is not supported JDK feature and only used in some hacky cases.
     *
     * @param field     {@link Field}
     * @param modifiers new {@link Field} modifiers
     * @throws IllegalAccessException if field is inaccessible
     */
    public static void setFieldModifiers ( final Field field, final int modifiers ) throws IllegalAccessException
    {
        try
        {
            final Field mods = ReflectUtils.getField ( Field.class, "modifiers" );
            mods.set ( field, modifiers );
        }
        catch ( final NoSuchFieldException e )
        {
            throw new ReflectionException ( "Unable to update field modifiers: " + field + " -> " + modifiers );
        }
    }
}