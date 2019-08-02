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

package com.alee.api.jdk;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.managers.language.LM;
import com.alee.utils.UtilityException;

import java.util.Arrays;

/**
 * This is a copy of JDK7 {@code java.util.Objects} class for JDK6 support.
 *
 * @author Mikle Garin
 */
public final class Objects
{
    /**
     * Private constructor to avoid instantiation.
     */
    private Objects ()
    {
        throw new UtilityException ( "Utility classes are not meant to be instantiated" );
    }

    /**
     * Generates a hash code for a sequence of input values.
     * The hash code is generated as if all the input values were placed into an array,
     * and that array were hashed by calling {@link Arrays#hashCode(Object[])}.
     *
     * @param values the values to be hashed
     * @return a hash value of the sequence of input values
     * @see Arrays#hashCode(Object[])
     */
    public static int hash ( @Nullable final Object... values )
    {
        return Arrays.hashCode ( values );
    }

    /**
     * Returns whether the first Object equals to second one.
     * This method will compare objects even if they are null without throwing any exceptions.
     * This method should not be called from any method that overrides object default "equals" method.
     *
     * @param object      object to compare
     * @param compareWith objects to compare with
     * @return true if the first Object equals to any Object from the specified array, false otherwise
     */
    public static boolean equals ( @Nullable final Object object, @Nullable final Object compareWith )
    {
        return object == compareWith || object != null && object.equals ( compareWith );
    }

    /**
     * Returns whether the first Object equals to any Object from the specified array.
     * This method will compare objects even if they are null without throwing any exceptions.
     * This method should not be called from any method that overrides object default "equals" method.
     *
     * @param object      object to compare
     * @param compareWith object to compare with
     * @return true if the first Object equals to any Object from the specified array, false otherwise
     */
    public static boolean equals ( @Nullable final Object object, @Nullable final Object... compareWith )
    {
        if ( compareWith != null && compareWith.length > 0 )
        {
            for ( final Object o : compareWith )
            {
                if ( equalsImpl ( object, o ) )
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns whether the first Object is not equals to the second one.
     * This method will compare objects even if they are null without throwing any exceptions.
     * This method should not be called from any method that overrides object default "equals" method.
     *
     * @param object      object to compare
     * @param compareWith object to compare with
     * @return true if the first Object is not equals to any Object from the specified array, false otherwise
     */
    public static boolean notEquals ( @Nullable final Object object, @Nullable final Object compareWith )
    {
        return object != compareWith && ( object == null || !object.equals ( compareWith ) );
    }

    /**
     * Returns whether the first Object is not equals to any Object from the specified array.
     * This method will compare objects even if they are null without throwing any exceptions.
     * This method should not be called from any method that overrides object default "equals" method.
     *
     * @param object      object to compare
     * @param compareWith objects to compare with
     * @return true if the first Object is not equals to any Object from the specified array, false otherwise
     */
    public static boolean notEquals ( @Nullable final Object object, @Nullable final Object... compareWith )
    {
        if ( compareWith != null && compareWith.length > 0 )
        {
            for ( final Object o : compareWith )
            {
                if ( equalsImpl ( object, o ) )
                {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns whether the first Object equals to the second Object or not.
     * This method will compare two objects even if they are null without throwing any exceptions.
     * This method should not be called from any method that overrides object default "equals" method.
     *
     * @param o1 first Object
     * @param o2 second Object
     * @return true if the first Object equals to the second Object, false otherwise
     */
    private static boolean equalsImpl ( @Nullable final Object o1, @Nullable final Object o2 )
    {
        return o1 == o2 || o1 != null && o1.equals ( o2 );
    }

    /**
     * Checks that the specified {@link Object} is not {@code null} and throws a {@link NullPointerException} if it is.
     *
     * @param object {@link Object} to check for being null
     * @param <T>    {@link Object} type
     * @return {@link Object} if not {@code null}
     * @throws NullPointerException if {@link Object} is {@code null}
     */
    @NotNull
    public static <T> T requireNonNull ( @Nullable final T object )
    {
        return requireNonNull ( object, "Object must not be null" );
    }

    /**
     * Checks that the specified {@link Object} is not {@code null} and throws a customized {@link NullPointerException} if it is.
     *
     * @param object  {@link Object} to check for being null
     * @param message detailed message used in {@link NullPointerException}
     * @param <T>     {@link Object} type
     * @return {@link Object} if not {@code null}
     * @throws NullPointerException if {@link Object} is {@code null}
     */
    @NotNull
    public static <T> T requireNonNull ( @Nullable final T object, @NotNull final String message )
    {
        if ( object == null )
        {
            throw new NullPointerException (
                    LM.contains ( message ) ? LM.get ( message ) : message
            );
        }
        return object;
    }

    /**
     * Checks that the specified {@link Object} is not {@code null} and throws a customized {@link RuntimeException} if it is.
     *
     * @param object            {@link Object} to check for being null
     * @param exceptionSupplier {@link Supplier} for a customized {@link RuntimeException}
     * @param <T>               {@link Object} type
     * @return {@link Object} if not {@code null}
     * @throws RuntimeException if {@link Object} is {@code null}
     */
    @NotNull
    public static <T> T requireNonNull ( @Nullable final T object, @NotNull final Supplier<RuntimeException> exceptionSupplier )
    {
        if ( object == null )
        {
            throw exceptionSupplier.get ();
        }
        return object;
    }
}