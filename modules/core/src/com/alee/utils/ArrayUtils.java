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

package com.alee.utils;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;

import java.lang.reflect.Array;

/**
 * This class provides a set of utilities to work with arrays.
 *
 * @author Mikle Garin
 */
public final class ArrayUtils
{
    /**
     * Private constructor to avoid instantiation.
     */
    private ArrayUtils ()
    {
        throw new UtilityException ( "Utility classes are not meant to be instantiated" );
    }

    /**
     * Returns whether or not data is empty.
     *
     * @param data data
     * @param <T>  data type
     * @return {@code true} if data is empty, {@code false} otherwise
     */
    public static <T> boolean isEmpty ( @Nullable final T... data )
    {
        return data == null || data.length == 0;
    }

    /**
     * Returns whether or not data is empty.
     *
     * @param data data
     * @param <T>  data type
     * @return {@code true} if data is not empty, {@code false} otherwise
     */
    public static <T> boolean notEmpty ( @Nullable final T... data )
    {
        return data != null && data.length > 0;
    }

    /**
     * Returns whether array contains the specified number or not.
     *
     * @param number number to find
     * @param array  array to process
     * @return {@code true} if array contains the specified number, {@code false} otherwise
     */
    public static boolean contains ( final int number, @NotNull final int[] array )
    {
        return indexOf ( number, array ) != -1;
    }

    /**
     * Returns whether array contains the specified number or not.
     *
     * @param number number to find
     * @param array  array to process
     * @return {@code true} if array contains the specified number, {@code false} otherwise
     */
    public static boolean contains ( final long number, @NotNull final long[] array )
    {
        return indexOf ( number, array ) != -1;
    }

    /**
     * Returns whether array contains the specified number or not.
     *
     * @param number number to find
     * @param array  array to process
     * @return {@code true} if array contains the specified number, {@code false} otherwise
     */
    public static boolean contains ( final float number, @NotNull final float[] array )
    {
        return indexOf ( number, array ) != -1;
    }

    /**
     * Returns whether array contains the specified number or not.
     *
     * @param number number to find
     * @param array  array to process
     * @return {@code true} if array contains the specified number, {@code false} otherwise
     */
    public static boolean contains ( final double number, @NotNull final double[] array )
    {
        return indexOf ( number, array ) != -1;
    }

    /**
     * Returns whether array contains the specified character or not.
     *
     * @param character character to find
     * @param array     array to process
     * @return {@code true} if array contains the specified character, {@code false} otherwise
     */
    public static boolean contains ( final char character, @NotNull final char[] array )
    {
        return indexOf ( character, array ) != -1;
    }

    /**
     * Returns whether array contains the specified byte or not.
     *
     * @param data  byte to find
     * @param array array to process
     * @return {@code true} if array contains the specified byte, {@code false} otherwise
     */
    public static boolean contains ( final byte data, @NotNull final byte[] array )
    {
        return indexOf ( data, array ) != -1;
    }

    /**
     * Returns whether array contains the specified text or not.
     *
     * @param text  text to find
     * @param array array to process
     * @return {@code true} if array contains the specified text, {@code false} otherwise
     */
    public static boolean contains ( @Nullable final String text, @NotNull final String[] array )
    {
        return indexOf ( text, array ) != -1;
    }

    /**
     * Returns whether array contains the specified object or not.
     *
     * @param object object to find
     * @param array  array to process
     * @return {@code true} if array contains the specified object, {@code false} otherwise
     */
    public static boolean contains ( @Nullable final Object object, @NotNull final Object[] array )
    {
        return indexOf ( object, array ) != -1;
    }

    /**
     * Returns index of specified int in array.
     *
     * @param number int to find
     * @param array  array to process
     * @return index of specified int in array
     */
    public static int indexOf ( final int number, @NotNull final int[] array )
    {
        int index = -1;
        for ( int i = 0; i < array.length; i++ )
        {
            if ( array[ i ] == number )
            {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * Returns index of specified long in array.
     *
     * @param number long to find
     * @param array  array to process
     * @return index of specified long in array
     */
    public static int indexOf ( final long number, @NotNull final long[] array )
    {
        int index = -1;
        for ( int i = 0; i < array.length; i++ )
        {
            if ( array[ i ] == number )
            {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * Returns index of specified float in array.
     *
     * @param number float to find
     * @param array  array to process
     * @return index of specified float in array
     */
    public static int indexOf ( final float number, @NotNull final float[] array )
    {
        int index = -1;
        for ( int i = 0; i < array.length; i++ )
        {
            if ( array[ i ] == number )
            {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * Returns index of specified double in array.
     *
     * @param number double to find
     * @param array  array to process
     * @return index of specified double in array
     */
    public static int indexOf ( final double number, @NotNull final double[] array )
    {
        int index = -1;
        for ( int i = 0; i < array.length; i++ )
        {
            if ( array[ i ] == number )
            {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * Returns index of specified character in array.
     *
     * @param character character to find
     * @param array     array to process
     * @return index of specified character in array
     */
    public static int indexOf ( final char character, @NotNull final char[] array )
    {
        int index = -1;
        for ( int i = 0; i < array.length; i++ )
        {
            if ( array[ i ] == character )
            {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * Returns index of specified byte in array.
     *
     * @param data  byte to find
     * @param array array to process
     * @return index of specified byte in array
     */
    public static int indexOf ( final byte data, @NotNull final byte[] array )
    {
        int index = -1;
        for ( int i = 0; i < array.length; i++ )
        {
            if ( array[ i ] == data )
            {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * Returns index of specified text in array.
     *
     * @param text  text to find
     * @param array array to process
     * @return index of specified text in array
     */
    public static int indexOf ( @Nullable final String text, @NotNull final String[] array )
    {
        return indexOf ( text, array, false );
    }

    /**
     * Returns index of specified text in array.
     *
     * @param text       text to find
     * @param array      array to process
     * @param ignoreCase whether ignore text case or not
     * @return index of specified text in array
     */
    public static int indexOf ( @Nullable final String text, @NotNull final String[] array, final boolean ignoreCase )
    {
        int index = -1;
        for ( int i = 0; i < array.length; i++ )
        {
            final String txt = array[ i ];
            if ( txt == null && text == null ||
                    txt != null && text != null && ( ignoreCase ? txt.equalsIgnoreCase ( text ) : txt.equals ( text ) ) )
            {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * Returns index of specified object in array.
     *
     * @param object object to find
     * @param array  array to process
     * @return index of specified object in array
     */
    public static int indexOf ( @Nullable final Object object, @NotNull final Object[] array )
    {
        int index = -1;
        for ( int i = 0; i < array.length; i++ )
        {
            final Object obj = array[ i ];
            if ( obj == null && object == null || obj != null && obj.equals ( object ) )
            {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * Returns last index of specified object in array.
     *
     * @param object object to find
     * @param array  array to process
     * @return last index of specified object in array
     */
    public static int lastIndexOf ( @Nullable final Object object, @NotNull final Object[] array )
    {
        int index = -1;
        for ( int i = array.length - 1; i >= 0; i-- )
        {
            final Object obj = array[ i ];
            if ( obj == null && object == null || obj != null && obj.equals ( object ) )
            {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * Returns new array with the specified object removed.
     *
     * @param array  array to process
     * @param object object to remove
     * @param <T>    data type
     * @return new array with the specified object removed
     */
    @NotNull
    public static <T> T[] remove ( @NotNull final T[] array, @Nullable final T object )
    {
        final T[] newArray = createArray ( array, array.length - 1 );
        int mod = 0;
        for ( int i = 0; i < array.length; i++ )
        {
            if ( mod == 0 && array[ i ] == object )
            {
                mod = -1;
            }
            else
            {
                newArray[ i + mod ] = array[ i ];
            }
        }
        return newArray;
    }

    /**
     * Returns new array with the int under specified index removed.
     *
     * @param array array to process
     * @param index index of int to remove
     * @return new array with the int under specified index removed
     */
    @NotNull
    public static int[] remove ( @NotNull final int[] array, final int index )
    {
        final int[] newArray = new int[ array.length - 1 ];
        int mod = 0;
        for ( int i = 0; i < array.length; i++ )
        {
            if ( i == index )
            {
                mod = -1;
            }
            else
            {
                newArray[ i + mod ] = array[ i ];
            }
        }
        return newArray;
    }

    /**
     * Returns new array with the float under specified index removed.
     *
     * @param array array to process
     * @param index index of float to remove
     * @return new array with the float under specified index removed
     */
    @NotNull
    public static float[] remove ( @NotNull final float[] array, final int index )
    {
        final float[] newArray = new float[ array.length - 1 ];
        int mod = 0;
        for ( int i = 0; i < array.length; i++ )
        {
            if ( i == index )
            {
                mod = -1;
            }
            else
            {
                newArray[ i + mod ] = array[ i ];
            }
        }
        return newArray;
    }

    /**
     * Returns new array with the double under specified index removed.
     *
     * @param array array to process
     * @param index index of double to remove
     * @return new array with the double under specified index removed
     */
    @NotNull
    public static double[] remove ( @NotNull final double[] array, final int index )
    {
        final double[] newArray = new double[ array.length - 1 ];
        int mod = 0;
        for ( int i = 0; i < array.length; i++ )
        {
            if ( i == index )
            {
                mod = -1;
            }
            else
            {
                newArray[ i + mod ] = array[ i ];
            }
        }
        return newArray;
    }

    /**
     * Returns new array with the char under specified index removed.
     *
     * @param array array to process
     * @param index index of char to remove
     * @return new array with the char under specified index removed
     */
    @NotNull
    public static char[] remove ( @NotNull final char[] array, final int index )
    {
        final char[] newArray = new char[ array.length - 1 ];
        int mod = 0;
        for ( int i = 0; i < array.length; i++ )
        {
            if ( i == index )
            {
                mod = -1;
            }
            else
            {
                newArray[ i + mod ] = array[ i ];
            }
        }
        return newArray;
    }

    /**
     * Returns new array with the byte under specified index removed.
     *
     * @param array array to process
     * @param index index of byte to remove
     * @return new array with the byte under specified index removed
     */
    @NotNull
    public static byte[] remove ( @NotNull final byte[] array, final int index )
    {
        final byte[] newArray = new byte[ array.length - 1 ];
        int mod = 0;
        for ( int i = 0; i < array.length; i++ )
        {
            if ( i == index )
            {
                mod = -1;
            }
            else
            {
                newArray[ i + mod ] = array[ i ];
            }
        }
        return newArray;
    }

    /**
     * Returns new array with the boolean under specified index removed.
     *
     * @param array array to process
     * @param index index of boolean to remove
     * @return new array with the boolean under specified index removed
     */
    @NotNull
    public static boolean[] remove ( @NotNull final boolean[] array, final int index )
    {
        final boolean[] newArray = new boolean[ array.length - 1 ];
        int mod = 0;
        for ( int i = 0; i < array.length; i++ )
        {
            if ( i == index )
            {
                mod = -1;
            }
            else
            {
                newArray[ i + mod ] = array[ i ];
            }
        }
        return newArray;
    }

    /**
     * Returns new array with the object under specified index removed.
     *
     * @param array array to process
     * @param index index of object to remove
     * @param <T>   data type
     * @return new array with the object under specified index removed
     */
    @NotNull
    public static <T> T[] remove ( @NotNull final T[] array, final int index )
    {
        final T[] newArray = createArray ( array, array.length - 1 );
        int mod = 0;
        for ( int i = 0; i < array.length; i++ )
        {
            if ( i == index )
            {
                mod = -1;
            }
            else
            {
                newArray[ i + mod ] = array[ i ];
            }
        }
        return newArray;
    }

    /**
     * Returns new array with int inserted at the specified index.
     *
     * @param array  array to process
     * @param index  insert index
     * @param object int to insert
     * @return new array with int inserted at the specified index
     */
    @NotNull
    public static int[] insert ( @NotNull final int[] array, final int index, final int object )
    {
        final int[] newArray = new int[ array.length + 1 ];
        int mod = 0;
        for ( int i = 0; i < array.length; i++ )
        {
            if ( i == index )
            {
                newArray[ i ] = object;
                mod = 1;
            }
            newArray[ i + mod ] = array[ i ];
        }
        return newArray;
    }

    /**
     * Returns new array with float inserted at the specified index.
     *
     * @param array  array to process
     * @param index  insert index
     * @param object float to insert
     * @return new array with float inserted at the specified index
     */
    @NotNull
    public static float[] insert ( @NotNull final float[] array, final int index, final float object )
    {
        final float[] newArray = new float[ array.length + 1 ];
        int mod = 0;
        for ( int i = 0; i < array.length; i++ )
        {
            if ( i == index )
            {
                newArray[ i ] = object;
                mod = 1;
            }
            newArray[ i + mod ] = array[ i ];
        }
        return newArray;
    }

    /**
     * Returns new array with double inserted at the specified index.
     *
     * @param array  array to process
     * @param index  insert index
     * @param object double to insert
     * @return new array with double inserted at the specified index
     */
    @NotNull
    public static double[] insert ( @NotNull final double[] array, final int index, final double object )
    {
        final double[] newArray = new double[ array.length + 1 ];
        int mod = 0;
        for ( int i = 0; i < array.length; i++ )
        {
            if ( i == index )
            {
                newArray[ i ] = object;
                mod = 1;
            }
            newArray[ i + mod ] = array[ i ];
        }
        return newArray;
    }

    /**
     * Returns new array with char inserted at the specified index.
     *
     * @param array  array to process
     * @param index  insert index
     * @param object char to insert
     * @return new array with char inserted at the specified index
     */
    @NotNull
    public static char[] insert ( @NotNull final char[] array, final int index, final char object )
    {
        final char[] newArray = new char[ array.length + 1 ];
        int mod = 0;
        for ( int i = 0; i < array.length; i++ )
        {
            if ( i == index )
            {
                newArray[ i ] = object;
                mod = 1;
            }
            newArray[ i + mod ] = array[ i ];
        }
        return newArray;
    }

    /**
     * Returns new array with byte inserted at the specified index.
     *
     * @param array  array to process
     * @param index  insert index
     * @param object byte to insert
     * @return new array with byte inserted at the specified index
     */
    @NotNull
    public static byte[] insert ( @NotNull final byte[] array, final int index, final byte object )
    {
        final byte[] newArray = new byte[ array.length + 1 ];
        int mod = 0;
        for ( int i = 0; i < array.length; i++ )
        {
            if ( i == index )
            {
                newArray[ i ] = object;
                mod = 1;
            }
            newArray[ i + mod ] = array[ i ];
        }
        return newArray;
    }

    /**
     * Returns new array with boolean inserted at the specified index.
     *
     * @param array  array to process
     * @param index  insert index
     * @param object boolean to insert
     * @return new array with boolean inserted at the specified index
     */
    @NotNull
    public static boolean[] insert ( @NotNull final boolean[] array, final int index, final boolean object )
    {
        final boolean[] newArray = new boolean[ array.length + 1 ];
        int mod = 0;
        for ( int i = 0; i < array.length; i++ )
        {
            if ( i == index )
            {
                newArray[ i ] = object;
                mod = 1;
            }
            newArray[ i + mod ] = array[ i ];
        }
        return newArray;
    }

    /**
     * Returns new array with object inserted at the specified index.
     *
     * @param array  array to process
     * @param index  insert index
     * @param object object to insert
     * @param <T>    data type
     * @return new array with object inserted at the specified index
     */
    @NotNull
    public static <T> T[] insert ( @NotNull final T[] array, final int index, @Nullable final T object )
    {
        final T[] newArray = createArray ( array, array.length + 1 );
        int mod = 0;
        for ( int i = 0; i < array.length; i++ )
        {
            if ( i == index )
            {
                newArray[ i ] = object;
                mod = 1;
            }
            newArray[ i + mod ] = array[ i ];
        }
        return newArray;
    }

    /**
     * Returns new array with the component class type from the specified array.
     *
     * @param array  array to retrieve class type from
     * @param length array length
     * @param <T>    data type
     * @return new array with the component class type from the specified array
     */
    @NotNull
    public static <T> T[] createArray ( @NotNull final T[] array, final int length )
    {
        return createArray ( ( Class<T> ) array.getClass ().getComponentType (), length );
    }

    /**
     * Returns new array with the specified component class type.
     *
     * @param classType component class type
     * @param length    array length
     * @param <T>       data type
     * @return new array with the specified component class type
     */
    @NotNull
    public static <T> T[] createArray ( @NotNull final Class<T> classType, final int length )
    {
        return ( T[] ) Array.newInstance ( classType, length );
    }

    /**
     * Returns whether arrays are equal or not.
     *
     * @param array1 first array
     * @param array2 second array
     * @return {@code true} if arrays are equal, {@code false} otherwise
     */
    public static boolean equals ( @Nullable final int[] array1, @Nullable final int[] array2 )
    {
        final boolean equals;
        if ( array1 == null && array2 == null )
        {
            equals = true;
        }
        else if ( array1 == null || array2 == null || array1.length != array2.length )
        {
            equals = false;
        }
        else
        {
            boolean eq = true;
            for ( final int number : array1 )
            {
                if ( !contains ( number, array2 ) )
                {
                    eq = false;
                    break;
                }
            }
            equals = eq;
        }
        return equals;
    }

    /**
     * Returns whether arrays are equal or not.
     *
     * @param array1 first array
     * @param array2 second array
     * @return {@code true} if arrays are equal, {@code false} otherwise
     */
    public static boolean equals ( @Nullable final long[] array1, @Nullable final long[] array2 )
    {
        final boolean equals;
        if ( array1 == null && array2 == null )
        {
            equals = true;
        }
        else if ( array1 == null || array2 == null || array1.length != array2.length )
        {
            equals = false;
        }
        else
        {
            boolean eq = true;
            for ( final long number : array1 )
            {
                if ( !contains ( number, array2 ) )
                {
                    eq = false;
                    break;
                }
            }
            equals = eq;
        }
        return equals;
    }

    /**
     * Returns whether arrays are equal or not.
     *
     * @param array1 first array
     * @param array2 second array
     * @return {@code true} if arrays are equal, {@code false} otherwise
     */
    public static boolean equals ( @Nullable final float[] array1, @Nullable final float[] array2 )
    {
        final boolean equals;
        if ( array1 == null && array2 == null )
        {
            equals = true;
        }
        else if ( array1 == null || array2 == null || array1.length != array2.length )
        {
            equals = false;
        }
        else
        {
            boolean eq = true;
            for ( final float number : array1 )
            {
                if ( !contains ( number, array2 ) )
                {
                    eq = false;
                    break;
                }
            }
            equals = eq;
        }
        return equals;
    }

    /**
     * Returns whether arrays are equal or not.
     *
     * @param array1 first array
     * @param array2 second array
     * @return {@code true} if arrays are equal, {@code false} otherwise
     */
    public static boolean equals ( @Nullable final double[] array1, @Nullable final double[] array2 )
    {
        final boolean equals;
        if ( array1 == null && array2 == null )
        {
            equals = true;
        }
        else if ( array1 == null || array2 == null || array1.length != array2.length )
        {
            equals = false;
        }
        else
        {
            boolean eq = true;
            for ( final double number : array1 )
            {
                if ( !contains ( number, array2 ) )
                {
                    eq = false;
                    break;
                }
            }
            equals = eq;
        }
        return equals;
    }

    /**
     * Returns whether arrays are equal or not.
     *
     * @param array1 first array
     * @param array2 second array
     * @return {@code true} if arrays are equal, {@code false} otherwise
     */
    public static boolean equals ( @Nullable final char[] array1, @Nullable final char[] array2 )
    {
        final boolean equals;
        if ( array1 == null && array2 == null )
        {
            equals = true;
        }
        else if ( array1 == null || array2 == null || array1.length != array2.length )
        {
            equals = false;
        }
        else
        {
            boolean eq = true;
            for ( final char character : array1 )
            {
                if ( !contains ( character, array2 ) )
                {
                    eq = false;
                    break;
                }
            }
            equals = eq;
        }
        return equals;
    }

    /**
     * Returns whether arrays are equal or not.
     *
     * @param array1 first array
     * @param array2 second array
     * @return {@code true} if arrays are equal, {@code false} otherwise
     */
    public static boolean equals ( @Nullable final byte[] array1, @Nullable final byte[] array2 )
    {
        final boolean equals;
        if ( array1 == null && array2 == null )
        {
            equals = true;
        }
        else if ( array1 == null || array2 == null || array1.length != array2.length )
        {
            equals = false;
        }
        else
        {
            boolean eq = true;
            for ( final byte data : array1 )
            {
                if ( !contains ( data, array2 ) )
                {
                    eq = false;
                    break;
                }
            }
            equals = eq;
        }
        return equals;
    }

    /**
     * Returns whether arrays are equal or not.
     *
     * @param array1 first array
     * @param array2 second array
     * @return {@code true} if arrays are equal, {@code false} otherwise
     */
    public static boolean equals ( @Nullable final String[] array1, @Nullable final String[] array2 )
    {
        final boolean equals;
        if ( array1 == null && array2 == null )
        {
            equals = true;
        }
        else if ( array1 == null || array2 == null || array1.length != array2.length )
        {
            equals = false;
        }
        else
        {
            boolean eq = true;
            for ( final String text : array1 )
            {
                if ( !contains ( text, array2 ) )
                {
                    eq = false;
                    break;
                }
            }
            equals = eq;
        }
        return equals;
    }

    /**
     * Returns whether arrays are equal or not.
     *
     * @param array1 first array
     * @param array2 second array
     * @param <T>    data type
     * @return {@code true} if arrays are equal, {@code false} otherwise
     */
    public static <T> boolean equals ( @Nullable final T[] array1, @Nullable final T[] array2 )
    {
        final boolean equals;
        if ( array1 == null && array2 == null )
        {
            equals = true;
        }
        else if ( array1 == null || array2 == null || array1.length != array2.length )
        {
            equals = false;
        }
        else
        {
            boolean eq = true;
            for ( final Object object : array1 )
            {
                if ( !contains ( object, array2 ) )
                {
                    eq = false;
                    break;
                }
            }
            equals = eq;
        }
        return equals;
    }

    /**
     * Returns item from the array at the specified index.
     * Index can be larger than array size, allowing round robin item selection.
     *
     * @param index index in the array or a number larger than array size, cannot be less than zero
     * @param items array items
     * @param <T>   item type
     * @return item from the array at the specified index
     */
    @Nullable
    public static <T> T roundRobin ( final int index, @NotNull final T... items )
    {
        return items.length > 0 ? items[ index % items.length ] : null;
    }
}