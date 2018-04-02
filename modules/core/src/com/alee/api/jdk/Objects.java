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

import com.alee.utils.UtilityException;

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
     * Returns whether the first Object equals to second one.
     * This method will compare objects even if they are null without throwing any exceptions.
     * This method should not be called from any method that overrides object default "equals" method.
     *
     * @param object      object to compare
     * @param compareWith objects to compare with
     * @return true if the first Object equals to any Object from the specified array, false otherwise
     */
    public static boolean equals ( final Object object, final Object compareWith )
    {
        return object == compareWith || object != null && compareWith != null && object.equals ( compareWith );
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
    public static boolean equals ( final Object object, final Object... compareWith )
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
    public static boolean notEquals ( final Object object, final Object compareWith )
    {
        return object != compareWith && ( object == null || compareWith == null || !object.equals ( compareWith ) );
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
    public static boolean notEquals ( final Object object, final Object... compareWith )
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
    private static boolean equalsImpl ( final Object o1, final Object o2 )
    {
        return o1 == o2 || o1 != null && o2 != null && o1.equals ( o2 );
    }

    /**
     * Checks that the specified object reference is not {@code null}.
     *
     * @param obj the object reference to check for nullity
     * @param <T> the type of the reference
     * @return {@code obj} if not {@code null}
     * @throws NullPointerException if {@code obj} is {@code null}
     */
    public static <T> T requireNonNull ( final T obj )
    {
        if ( obj == null )
        {
            throw new NullPointerException ();
        }
        return obj;
    }

    /**
     * Checks that the specified object reference is not {@code null} and throws a customized {@link NullPointerException} if it is.
     *
     * @param obj     the object reference to check for nullity
     * @param message detail message to be used in the event that a {@link NullPointerException} is thrown
     * @param <T>     the type of the reference
     * @return {@code obj} if not {@code null}
     * @throws NullPointerException if {@code obj} is {@code null}
     */
    public static <T> T requireNonNull ( final T obj, final String message )
    {
        if ( obj == null )
        {
            throw new NullPointerException ( message );
        }
        return obj;
    }
}