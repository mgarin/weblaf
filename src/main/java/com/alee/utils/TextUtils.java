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

import com.alee.utils.text.SimpleTextProvider;
import com.alee.utils.text.TextProvider;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * This class provides a set of utilities to work with various text usage cases.
 *
 * @author Mikle Garin
 * @since 1.3
 */

public class TextUtils
{
    /**
     * Separators used to determine words in text.
     */
    private static final List<String> textSeparators =
            Arrays.asList ( " ", ".", ",", ":", ";", "/", "\\", "\n", "\t", "|", "{", "}", "[", "]", "(", ")", "<", ">", "-", "+", "\"",
                    "'", "*", "%", "$", "#", "@", "!", "~", "^", "&", "?" );

    /**
     * Text provider for any type of objects.
     */
    private static final SimpleTextProvider simpleTextProvider = new SimpleTextProvider ();

    /**
     * Default ID part length.
     */
    private static final int idPartLength = 5;

    /**
     * Default ID prefix.
     */
    private static final String defaultIdPrefix = "WebLaF";

    /**
     * Default ID suffix.
     */
    private static final String defaultIdSuffix = "ID";

    /**
     * Returns first number found in text.
     *
     * @param text text to search through
     * @return first found number
     */
    public static Integer findFirstNumber ( String text )
    {
        int start = -1;
        StringBuilder sb = new StringBuilder ( "" );
        for ( int j = 0; j < text.length (); j++ )
        {
            final char ch = text.charAt ( j );
            if ( Character.isDigit ( ch ) )
            {
                sb.append ( ch );
            }
            else if ( sb.length () > 0 )
            {
                break;
            }
        }
        return Integer.parseInt ( sb.toString () );
    }

    /**
     * Returns a word from text at the specified location.
     *
     * @param text     text to retrieve the word from
     * @param location word location
     * @return word
     */
    public static String getWord ( String text, int location )
    {
        int wordStart = location;
        int wordEnd = location;

        // At the word start
        while ( wordEnd < text.length () - 1 && !textSeparators.contains ( text.substring ( wordEnd, wordEnd + 1 ) ) )
        {
            wordEnd++;
        }

        // At the word end
        while ( wordStart > 0 && !textSeparators.contains ( text.substring ( wordStart - 1, wordStart ) ) )
        {
            wordStart--;
        }

        return wordStart == wordEnd ? null : text.substring ( wordStart, wordEnd );
    }

    /**
     * Returns text with first lines removed.
     *
     * @param text  text to crop
     * @param count lines count to crop
     * @return cropped text
     */
    public static String removeFirstLines ( String text, int count )
    {
        int found = 0;
        int index = 0;
        while ( found < count )
        {
            index = text.indexOf ( "\n", index );
            if ( index != -1 )
            {
                index += 1;
                found++;
            }
            else
            {
                return "";
            }
        }
        return text.substring ( index );
    }

    /**
     * Returns a list of text parts splitted using specified separator.
     *
     * @param text      text to split
     * @param separator text parts separator
     * @return list of splitted parts
     */
    public static List<String> split ( String text, String separator )
    {
        return Arrays.asList ( text.split ( separator ) );
    }

    /**
     * Returns point extracted from text.
     *
     * @param text text to extract point from
     * @return extracted point
     */
    public static Point parsePoint ( String text )
    {
        return parsePoint ( text, "," );
    }

    /**
     * Returns point extracted from text.
     *
     * @param text      text to extract point from
     * @param separator point values separator
     * @return extracted point
     */
    public static Point parsePoint ( String text, String separator )
    {
        String[] parts = text.split ( separator );
        return parts.length == 2 ? new Point ( Integer.parseInt ( parts[ 0 ].trim () ), Integer.parseInt ( parts[ 1 ].trim () ) ) : null;
    }

    /**
     * Returns text with all control symbols removed.
     * This method works faster than simple replaceAll("\\p{Cntrl}", "").
     *
     * @param text text to modify
     * @return text without conytol symbols
     */
    public static String removeControlSymbols ( String text )
    {
        int length = text.length ();
        char[] oldChars = new char[ length ];
        text.getChars ( 0, length, oldChars, 0 );
        int newLen = 0;
        for ( int j = 0; j < length; j++ )
        {
            final char ch = oldChars[ j ];
            if ( ch >= ' ' )
            {
                oldChars[ newLen ] = ch;
                newLen++;
            }
        }
        return text;
    }

    /**
     * Returns shortened text.
     *
     * @param text      text to shorten
     * @param maxLength maximum shortened text lenght
     * @param addDots   add dots at the end of the text when shortened
     * @return shortened text
     */
    public static String shortenText ( String text, int maxLength, boolean addDots )
    {
        if ( text.length () <= maxLength )
        {
            return text;
        }
        else
        {
            return text.substring ( 0, maxLength > 3 && addDots ? maxLength - 3 : maxLength ) + ( addDots ? "..." : "" );
        }
    }

    /**
     * Returns a list of text parts splitted using specified separator.
     *
     * @param string    text to split
     * @param separator text parts separator
     * @return list of splitted parts
     */
    public static List<String> stringToList ( String string, String separator )
    {
        final List<String> imageTags = new ArrayList<String> ();
        if ( string != null )
        {
            final StringTokenizer tokenizer = new StringTokenizer ( string, separator, false );
            while ( tokenizer.hasMoreTokens () )
            {
                imageTags.add ( tokenizer.nextToken ().trim () );
            }
        }
        return imageTags;
    }

    /**
     * Returns a list of integer parts splitted using specified separator.
     *
     * @param string    text to split
     * @param separator text parts separator
     * @return list of splitted parts
     */
    public static List<Integer> stringToIntList ( String string, String separator )
    {
        final List<String> stringList = stringToList ( string, separator );
        if ( stringList != null )
        {
            final List<Integer> intList = new ArrayList<Integer> ( stringList.size () );
            for ( String s : stringList )
            {
                intList.add ( Integer.parseInt ( s ) );
            }
            return intList;
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns single text combined using list of strings and specified separator.
     *
     * @param list      list to combine into single text
     * @param separator text parts separator
     * @return single text
     */
    public static String listToString ( List list, String separator )
    {
        return listToString ( list, separator, simpleTextProvider );
    }

    /**
     * Returns single text combined using list of strings and specified separator.
     *
     * @param list      list to combine into single text
     * @param separator text parts separator
     * @return single text
     */
    public static <T> String listToString ( List<T> list, String separator, TextProvider<T> textProvider )
    {
        if ( list.size () > 0 )
        {
            final StringBuilder stringBuilder = new StringBuilder ();
            if ( list != null )
            {
                final int end = list.size () - 1;
                for ( int i = 0; i <= end; i++ )
                {
                    stringBuilder.append ( textProvider.provide ( list.get ( i ) ) );
                    stringBuilder.append ( i != end ? separator : "" );
                }
            }
            return stringBuilder.toString ();
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns random ID with default prefix and suffix.
     *
     * @return ID
     */
    public static String generateId ()
    {
        return generateId ( null, null );
    }

    /**
     * Returns random ID with specified prefix and default suffix.
     *
     * @param prefix id prefix
     * @return ID
     */
    public static String generateId ( String prefix )
    {
        return generateId ( prefix, null );
    }

    /**
     * Returns random ID with specified prefix and suffix.
     *
     * @param prefix id prefix
     * @param suffix id suffix
     * @return ID
     */
    public static String generateId ( String prefix, String suffix )
    {
        final StringBuilder idBuilder = new StringBuilder ();
        idBuilder.append ( prefix == null ? defaultIdPrefix : prefix );
        idBuilder.append ( "-" );
        idBuilder.append ( generateId ( idPartLength ) );
        idBuilder.append ( "-" );
        idBuilder.append ( generateId ( idPartLength ) );
        idBuilder.append ( "-" );
        idBuilder.append ( generateId ( idPartLength ) );
        idBuilder.append ( "-" );
        idBuilder.append ( generateId ( idPartLength ) );
        idBuilder.append ( "-" );
        idBuilder.append ( suffix == null ? defaultIdSuffix : suffix );
        return idBuilder.toString ();
    }

    /**
     * Returns randomly generated ID part with specified length.
     *
     * @param length part length in symbols
     * @return ID part
     */
    private static String generateId ( int length )
    {
        StringBuilder stringBuilder = new StringBuilder ( length );
        for ( int i = 0; i < length; i++ )
        {
            char next = 0;
            int range = 10;
            switch ( MathUtils.random ( 3 ) )
            {
                case 0:
                {
                    next = '0';
                    range = 10;
                    break;
                }
                case 1:
                {
                    next = 'a';
                    range = 26;
                    break;
                }
                case 2:
                {
                    next = 'A';
                    range = 26;
                    break;
                }
            }
            stringBuilder.append ( ( char ) ( ( MathUtils.random ( range ) ) + next ) );
        }
        return stringBuilder.toString ();
    }
}