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

import com.alee.utils.compare.Filter;
import com.alee.utils.text.DelayFormatException;
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
 */

public final class TextUtils
{
    /**
     * Constants for time calculations.
     */
    private static final int msInDay = 86400000;
    private static final int msInHour = 3600000;
    private static final int msInMinute = 60000;
    private static final int msInSecond = 1000;

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
     * Returns text with all line breaks removed.
     *
     * @param text text to remove line breaks from
     * @return text with all line breaks removed
     */
    public static String removeLineBreaks ( final String text )
    {
        return text.replaceAll ( "\\r\\n|\\r|\\n", "" );
    }

    /**
     * Returns first number found in text.
     *
     * @param text text to search through
     * @return first found number
     */
    public static Integer findFirstNumber ( final String text )
    {
        final StringBuilder sb = new StringBuilder ( "" );
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
    public static String getWord ( final String text, final int location )
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
     * Returns a word start index at the specified location.
     *
     * @param text     text to retrieve the word start index from
     * @param location word location
     * @return word start index
     */
    public static int getWordStart ( final String text, final int location )
    {
        int wordStart = location;
        while ( wordStart > 0 && !textSeparators.contains ( text.substring ( wordStart - 1, wordStart ) ) )
        {
            wordStart--;
        }
        return wordStart;
    }

    /**
     * Returns a word end index at the specified location.
     *
     * @param text     text to retrieve the word end index from
     * @param location word location
     * @return word end index
     */
    public static int getWordEnd ( final String text, final int location )
    {
        int wordEnd = location;
        while ( wordEnd < text.length () - 1 && !textSeparators.contains ( text.substring ( wordEnd, wordEnd + 1 ) ) )
        {
            wordEnd++;
        }
        return wordEnd;
    }

    /**
     * Returns text with first lines removed.
     *
     * @param text  text to crop
     * @param count lines count to crop
     * @return cropped text
     */
    public static String removeFirstLines ( final String text, final int count )
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
    public static List<String> split ( final String text, final String separator )
    {
        return Arrays.asList ( text.split ( separator ) );
    }

    /**
     * Returns point extracted from text.
     *
     * @param text text to extract point from
     * @return extracted point
     */
    public static Point parsePoint ( final String text )
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
    public static Point parsePoint ( final String text, final String separator )
    {
        final String[] parts = text.split ( separator );
        return parts.length == 2 ? new Point ( Integer.parseInt ( parts[ 0 ].trim () ), Integer.parseInt ( parts[ 1 ].trim () ) ) : null;
    }

    /**
     * Returns text with all control symbols removed.
     * This method works faster than simple replaceAll("\\p{Cntrl}", "").
     *
     * @param text text to modify
     * @return text without conytol symbols
     */
    public static String removeControlSymbols ( final String text )
    {
        final int length = text.length ();
        final char[] oldChars = new char[ length ];
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
    public static String shortenText ( final String text, final int maxLength, final boolean addDots )
    {
        return text.length () <= maxLength ? text :
                text.substring ( 0, maxLength > 3 && addDots ? maxLength - 3 : maxLength ) + ( addDots ? "..." : "" );
    }

    /**
     * Returns shortened text.
     *
     * @param text      text to shorten
     * @param maxLength maximum shortened text lenght
     * @param addDots   add dots at the end of the text when shortened
     * @return shortened text
     */
    public static String shortenTextEnd ( final String text, final int maxLength, final boolean addDots )
    {
        return text.length () <= maxLength ? text :
                ( addDots ? "..." : "" ) + text.substring ( text.length () - ( maxLength > 3 && addDots ? maxLength - 3 : maxLength ) );
    }

    /**
     * Returns a list of text parts splitted using specified separator.
     *
     * @param string    text to split
     * @param separator text parts separator
     * @return list of splitted parts
     */
    public static List<String> stringToList ( final String string, final String separator )
    {
        final List<String> strings = new ArrayList<String> ();
        if ( string != null )
        {
            final StringTokenizer tokenizer = new StringTokenizer ( string, separator, false );
            while ( tokenizer.hasMoreTokens () )
            {
                strings.add ( tokenizer.nextToken ().trim () );
            }
        }
        return strings;
    }

    /**
     * Returns a list of integer parts splitted using specified separator.
     *
     * @param string    text to split
     * @param separator text parts separator
     * @return list of splitted parts
     */
    public static List<Integer> stringToIntList ( final String string, final String separator )
    {
        final List<String> stringList = stringToList ( string, separator );
        if ( stringList != null )
        {
            final List<Integer> intList = new ArrayList<Integer> ( stringList.size () );
            for ( final String s : stringList )
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
    public static String listToString ( final List list, final String separator )
    {
        return listToString ( list, separator, simpleTextProvider );
    }

    /**
     * Returns single text combined using list of strings and specified separator.
     *
     * @param list         list to combine into single text
     * @param separator    text parts separator
     * @param textProvider text provider
     * @return single text
     */
    public static <T> String listToString ( final List<T> list, final String separator, final TextProvider<T> textProvider )
    {
        return listToString ( list, separator, textProvider, null );
    }

    /**
     * Returns single text combined using list of strings and specified separator.
     *
     * @param list         list to combine into single text
     * @param separator    text parts separator
     * @param textProvider text provider
     * @param filter       list elements filter
     * @return single text
     */
    public static <T> String listToString ( final List<T> list, final String separator, final TextProvider<T> textProvider,
                                            final Filter<T> filter )
    {
        if ( list != null && list.size () > 0 )
        {
            final StringBuilder stringBuilder = new StringBuilder ();
            boolean hasPreviouslyAccepted = false;
            for ( final T object : list )
            {
                if ( filter == null || filter.accept ( object ) )
                {
                    if ( hasPreviouslyAccepted )
                    {
                        stringBuilder.append ( separator );
                    }
                    stringBuilder.append ( textProvider.provide ( object ) );
                    hasPreviouslyAccepted = true;
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
     * Converts list of enumeration constants into string with list of enumeration constants and returns it.
     *
     * @param enumList enumeration constants list
     * @param <E>      enumeration type
     * @return string with list of enumeration constants
     */
    public static <E extends Enum<E>> String enumListToString ( final List<E> enumList )
    {
        if ( enumList != null && enumList.size () > 0 )
        {
            final int end = enumList.size () - 1;
            final StringBuilder stringBuilder = new StringBuilder ();
            for ( int i = 0; i <= end; i++ )
            {
                stringBuilder.append ( enumList.get ( i ) );
                stringBuilder.append ( i != end ? "," : "" );
            }
            return stringBuilder.toString ();
        }
        else
        {
            return null;
        }
    }

    /**
     * Converts string with list of enumeration constants into real list of enumeration constants and returns it.
     *
     * @param enumString enumeration constants string list
     * @param enumClass  enumeration class
     * @param <E>        enumeration type
     * @return list of enumeration constants
     */
    public static <E extends Enum<E>> List<E> enumStringToList ( final String enumString, final Class<E> enumClass )
    {
        final List<E> enumerations;
        if ( enumString != null )
        {
            final StringTokenizer tokenizer = new StringTokenizer ( enumString, ",", false );
            enumerations = new ArrayList<E> ();
            while ( tokenizer.hasMoreTokens () )
            {
                enumerations.add ( Enum.valueOf ( enumClass, tokenizer.nextToken ().trim () ) );
            }
        }
        else
        {
            enumerations = new ArrayList<E> ( 0 );
        }
        return enumerations;
    }

    /**
     * Converts and returns specified parts which are not null into single string.
     *
     * @param separator separator to place between parts
     * @param parts     parts to unite
     * @return united non-null parts
     */
    public static String unite ( final String separator, final String... parts )
    {
        if ( parts != null && parts.length > 0 )
        {
            final StringBuilder sb = new StringBuilder ();
            boolean hasPrevious = false;
            for ( final String part : parts )
            {
                if ( !isEmpty ( part ) )
                {
                    if ( hasPrevious )
                    {
                        sb.append ( separator );
                    }
                    sb.append ( part );
                    hasPrevious = true;
                }
            }
            return sb.toString ();
        }
        else
        {
            return "";
        }
    }

    /**
     * Returns whether specified text is empty or not.
     *
     * @param text text to process
     * @return true if specified text is empty, false otherwise
     */
    public static boolean isEmpty ( final String text )
    {
        return text == null || text.trim ().isEmpty ();
    }

    /**
     * Creates new string filled with specified amount of same characters.
     *
     * @param character character to fill string with
     * @param length    string length
     * @return new string filled with specified amount of same characters
     */
    public static String createString ( final String character, int length )
    {
        final StringBuilder sb = new StringBuilder ( length );
        while ( length-- > 0 )
        {
            sb.append ( character );
        }
        return sb.toString ();
    }

    /**
     * Replaces all occurences of str found in the specified text with provided text.
     *
     * @param text       text to replace string occurences in
     * @param ignoreCase whether should ignore case while searching for occurences or not
     * @param str        text to replace
     * @param provider   text replacement
     * @return text with replaced occurences of the specified str
     */
    public static String replaceAll ( final String text, final boolean ignoreCase, final String str, final TextProvider<String> provider )
    {
        final String exp = ignoreCase ? str.toLowerCase () : str;
        int match = 0;
        int prev = 0;
        final StringBuilder builder = new StringBuilder ( text.length () );
        for ( int i = 0; i < text.length (); i++ )
        {
            final char ch = text.charAt ( i );
            if ( exp.charAt ( match ) == ( ignoreCase ? Character.toLowerCase ( ch ) : ch ) )
            {
                match++;
            }
            else if ( exp.charAt ( 0 ) == ( ignoreCase ? Character.toLowerCase ( ch ) : ch ) )
            {
                match = 1;
            }
            else
            {
                match = 0;
            }
            if ( match == exp.length () )
            {
                final int start = i - exp.length () + 1;
                final String part = text.substring ( start, start + exp.length () );
                builder.append ( text.substring ( prev, start ) );
                builder.append ( provider.provide ( part ) );
                prev = start + exp.length ();
                match = 0;
            }
            else if ( i == text.length () - 1 )
            {
                builder.append ( text.substring ( prev ) );
            }
        }
        return builder.toString ();
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
    public static String generateId ( final String prefix )
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
    public static String generateId ( final String prefix, final String suffix )
    {
        return ( prefix == null ? defaultIdPrefix : prefix ) + "-" + generateId ( idPartLength ) + "-" + generateId ( idPartLength ) + "-" +
                generateId ( idPartLength ) + "-" + generateId ( idPartLength ) + "-" + ( suffix == null ? defaultIdSuffix : suffix );
    }

    /**
     * Returns randomly generated ID part with specified length.
     *
     * @param length part length in symbols
     * @return ID part
     */
    public static String generateId ( final int length )
    {
        final StringBuilder stringBuilder = new StringBuilder ( length );
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
            stringBuilder.append ( ( char ) ( MathUtils.random ( range ) + next ) );
        }
        return stringBuilder.toString ();
    }

    /**
     * Either returns delay retrieved from string or throws an exception if it cannot be parsed.
     * Full string format is "Xd Yh Zm s ms" but you can skip any part of it. Yet you must specify atleast one value.
     * For example string "2h 5s" will be a valid delay declaration and will be converted into (2*60*60*1000+5*1000) long value.
     *
     * @param delay string delay
     * @return delay retrieved from string
     */
    public static long parseDelay ( final String delay ) throws DelayFormatException
    {
        try
        {
            long summ = 0;
            final String[] parts = delay.split ( " " );
            for ( final String part : parts )
            {
                for ( int i = 0; i < part.length (); i++ )
                {
                    if ( !Character.isDigit ( part.charAt ( i ) ) )
                    {
                        final int time = Integer.parseInt ( part.substring ( 0, i ) );
                        final PartType type = PartType.valueOf ( part.substring ( i ) );
                        switch ( type )
                        {
                            case d:
                                summ += time * msInDay;
                                break;
                            case h:
                                summ += time * msInHour;
                                break;
                            case m:
                                summ += time * msInMinute;
                                break;
                            case s:
                                summ += time * msInSecond;
                                break;
                            case ms:
                                summ += time;
                                break;
                        }
                        break;
                    }
                }
            }
            return summ;
        }
        catch ( final Throwable e )
        {
            throw new DelayFormatException ( e );
        }
    }

    /**
     * Returns delay string representation.
     *
     * @param delay delay to process
     * @return delay string representation
     */
    public static String toStringDelay ( final long delay )
    {
        if ( delay <= 0 )
        {
            throw new IllegalArgumentException ( "Invalid delay: " + delay );
        }

        long time = delay;

        final long d = time / msInDay;
        time = time - d * msInDay;

        final long h = time / msInHour;
        time = time - h * msInHour;

        final long m = time / msInMinute;
        time = time - m * msInMinute;

        final long s = time / msInSecond;
        time = time - s * msInSecond;

        final long ms = time;

        final String stringDelay = ( d > 0 ? d + "d " : "" ) +
                ( h > 0 ? h + "h " : "" ) +
                ( m > 0 ? m + "m " : "" ) +
                ( s > 0 ? s + "s " : "" ) +
                ( ms > 0 ? ms + "ms " : "" );

        return stringDelay.trim ();
    }

    /**
     * Time part type enumeration used to parse string delay.
     */
    protected static enum PartType
    {
        d, h, m, s, ms
    }
}