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
    public static <T> boolean isEmpty ( final T... data )
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
    public static <T> boolean notEmpty ( final T... data )
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
    public static boolean contains ( final int number, final int[] array )
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
    public static boolean contains ( final long number, final long[] array )
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
    public static boolean contains ( final float number, final float[] array )
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
    public static boolean contains ( final double number, final double[] array )
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
    public static boolean contains ( final char character, final char[] array )
    {
        return indexOf ( character, array ) != -1;
    }

    /**
     * Returns whether array contains the specified byte or not.
     *
     * @param data byte to find
     * @param array  array to process
     * @return {@code true} if array contains the specified byte, {@code false} otherwise
     */
    public static boolean contains ( final byte data, final byte[] array )
    {
        return indexOf ( data, array ) != -1;
    }

    /**
     * Returns whether array contains the specified text or not.
     *
     * @param text text to find
     * @param array  array to process
     * @return {@code true} if array contains the specified text, {@code false} otherwise
     */
    public static boolean contains ( final String text, final String[] array )
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
    public static boolean contains ( final Object object, final Object[] array )
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
    public static int indexOf ( final int number, final int[] array )
    {
        for ( int i = 0; i < array.length; i++ )
        {
            if ( array[ i ] == number )
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns index of specified long in array.
     *
     * @param number long to find
     * @param array  array to process
     * @return index of specified long in array
     */
    public static int indexOf ( final long number, final long[] array )
    {
        for ( int i = 0; i < array.length; i++ )
        {
            if ( array[ i ] == number )
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns index of specified float in array.
     *
     * @param number float to find
     * @param array  array to process
     * @return index of specified float in array
     */
    public static int indexOf ( final float number, final float[] array )
    {
        for ( int i = 0; i < array.length; i++ )
        {
            if ( array[ i ] == number )
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns index of specified double in array.
     *
     * @param number double to find
     * @param array  array to process
     * @return index of specified double in array
     */
    public static int indexOf ( final double number, final double[] array )
    {
        for ( int i = 0; i < array.length; i++ )
        {
            if ( array[ i ] == number )
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns index of specified character in array.
     *
     * @param character character to find
     * @param array     array to process
     * @return index of specified character in array
     */
    public static int indexOf ( final char character, final char[] array )
    {
        for ( int i = 0; i < array.length; i++ )
        {
            if ( array[ i ] == character )
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns index of specified byte in array.
     *
     * @param data  byte to find
     * @param array array to process
     * @return index of specified byte in array
     */
    public static int indexOf ( final byte data, final byte[] array )
    {
        for ( int i = 0; i < array.length; i++ )
        {
            if ( array[ i ] == data )
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns index of specified text in array.
     *
     * @param text  text to find
     * @param array array to process
     * @return index of specified text in array
     */
    public static int indexOf ( final String text, final String[] array )
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
    public static int indexOf ( final String text, final String[] array, final boolean ignoreCase )
    {
        for ( int i = 0; i < array.length; i++ )
        {
            final String txt = array[ i ];
            if ( txt == null && text == null ||
                    txt != null && text != null && ( ignoreCase ? txt.equalsIgnoreCase ( text ) : txt.equals ( text ) ) )
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns index of specified object in array.
     *
     * @param object object to find
     * @param array  array to process
     * @return index of specified object in array
     */
    public static int indexOf ( final Object object, final Object[] array )
    {
        for ( int i = 0; i < array.length; i++ )
        {
            final Object obj = array[ i ];
            if ( obj == null && object == null || obj != null && object != null && obj.equals ( object ) )
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns last index of specified object in array.
     *
     * @param object object to find
     * @param array  array to process
     * @return last index of specified object in array
     */
    public static int lastIndexOf ( final Object object, final Object[] array )
    {
        for ( int i = array.length - 1; i >= 0; i-- )
        {
            final Object obj = array[ i ];
            if ( obj == null && object == null || obj != null && object != null && obj.equals ( object ) )
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns new array with the specified object removed.
     *
     * @param array  array to process
     * @param object object to remove
     * @param <T>    data type
     * @return new array with the specified object removed
     */
    public static <T> T[] remove ( final T[] array, final T object )
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
    public static int[] remove ( final int[] array, final int index )
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
    public static float[] remove ( final float[] array, final int index )
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
    public static double[] remove ( final double[] array, final int index )
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
    public static char[] remove ( final char[] array, final int index )
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
    public static byte[] remove ( final byte[] array, final int index )
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
    public static boolean[] remove ( final boolean[] array, final int index )
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
    public static <T> T[] remove ( final T[] array, final int index )
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
    public static int[] insert ( final int[] array, final int index, final int object )
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
    public static float[] insert ( final float[] array, final int index, final float object )
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
    public static double[] insert ( final double[] array, final int index, final double object )
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
    public static char[] insert ( final char[] array, final int index, final char object )
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
    public static byte[] insert ( final byte[] array, final int index, final byte object )
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
    public static boolean[] insert ( final boolean[] array, final int index, final boolean object )
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
    public static <T> T[] insert ( final T[] array, final int index, final T object )
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
    public static <T> T[] createArray ( final T[] array, final int length )
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
    public static <T> T[] createArray ( final Class<T> classType, final int length )
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
    public static boolean equals ( final int[] array1, final int[] array2 )
    {
        if ( array1 == null && array2 == null )
        {
            return true;
        }
        else if ( array1 == null || array2 == null || array1.length != array2.length )
        {
            return false;
        }
        else
        {
            for ( final int number : array1 )
            {
                if ( !contains ( number, array2 ) )
                {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Returns whether arrays are equal or not.
     *
     * @param array1 first array
     * @param array2 second array
     * @return {@code true} if arrays are equal, {@code false} otherwise
     */
    public static boolean equals ( final long[] array1, final long[] array2 )
    {
        if ( array1 == null && array2 == null )
        {
            return true;
        }
        else if ( array1 == null || array2 == null || array1.length != array2.length )
        {
            return false;
        }
        else
        {
            for ( final long number : array1 )
            {
                if ( !contains ( number, array2 ) )
                {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Returns whether arrays are equal or not.
     *
     * @param array1 first array
     * @param array2 second array
     * @return {@code true} if arrays are equal, {@code false} otherwise
     */
    public static boolean equals ( final float[] array1, final float[] array2 )
    {
        if ( array1 == null && array2 == null )
        {
            return true;
        }
        else if ( array1 == null || array2 == null || array1.length != array2.length )
        {
            return false;
        }
        else
        {
            for ( final float number : array1 )
            {
                if ( !contains ( number, array2 ) )
                {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Returns whether arrays are equal or not.
     *
     * @param array1 first array
     * @param array2 second array
     * @return {@code true} if arrays are equal, {@code false} otherwise
     */
    public static boolean equals ( final double[] array1, final double[] array2 )
    {
        if ( array1 == null && array2 == null )
        {
            return true;
        }
        else if ( array1 == null || array2 == null || array1.length != array2.length )
        {
            return false;
        }
        else
        {
            for ( final double number : array1 )
            {
                if ( !contains ( number, array2 ) )
                {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Returns whether arrays are equal or not.
     *
     * @param array1 first array
     * @param array2 second array
     * @return {@code true} if arrays are equal, {@code false} otherwise
     */
    public static boolean equals ( final char[] array1, final char[] array2 )
    {
        if ( array1 == null && array2 == null )
        {
            return true;
        }
        else if ( array1 == null || array2 == null || array1.length != array2.length )
        {
            return false;
        }
        else
        {
            for ( final char character : array1 )
            {
                if ( !contains ( character, array2 ) )
                {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Returns whether arrays are equal or not.
     *
     * @param array1 first array
     * @param array2 second array
     * @return {@code true} if arrays are equal, {@code false} otherwise
     */
    public static boolean equals ( final byte[] array1, final byte[] array2 )
    {
        if ( array1 == null && array2 == null )
        {
            return true;
        }
        else if ( array1 == null || array2 == null || array1.length != array2.length )
        {
            return false;
        }
        else
        {
            for ( final byte data : array1 )
            {
                if ( !contains ( data, array2 ) )
                {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Returns whether arrays are equal or not.
     *
     * @param array1 first array
     * @param array2 second array
     * @return {@code true} if arrays are equal, {@code false} otherwise
     */
    public static boolean equals ( final String[] array1, final String[] array2 )
    {
        if ( array1 == null && array2 == null )
        {
            return true;
        }
        else if ( array1 == null || array2 == null || array1.length != array2.length )
        {
            return false;
        }
        else
        {
            for ( final String text : array1 )
            {
                if ( !contains ( text, array2 ) )
                {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Returns whether arrays are equal or not.
     *
     * @param array1 first array
     * @param array2 second array
     * @param <T>    data type
     * @return {@code true} if arrays are equal, {@code false} otherwise
     */
    public static <T> boolean equals ( final T[] array1, final T[] array2 )
    {
        if ( array1 == null && array2 == null )
        {
            return true;
        }
        else if ( array1 == null || array2 == null || array1.length != array2.length )
        {
            return false;
        }
        else
        {
            for ( final Object object : array1 )
            {
                if ( !contains ( object, array2 ) )
                {
                    return false;
                }
            }
            return true;
        }
    }
}