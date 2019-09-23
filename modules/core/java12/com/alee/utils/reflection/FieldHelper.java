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

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;

/**
 * Java 12 compatible field helper.
 *
 * @author Alexandr Zernov
 */
public final class FieldHelper
{
    private static final VarHandle MODIFIERS;

    static
    {
        try
        {
            final var lookup = MethodHandles.privateLookupIn ( Field.class, MethodHandles.lookup () );
            MODIFIERS = lookup.findVarHandle ( Field.class, "modifiers", int.class );
        }
        catch ( final IllegalAccessException | NoSuchFieldException ex )
        {
            throw new RuntimeException ( ex );
        }
    }

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
        MODIFIERS.set ( field, modifiers );
    }
}