/* Copyright (c) 2008 Sven Jacobs

   Permission is hereby granted, free of charge, to any person obtaining a copy
   of this software and associated documentation files (the "Software"), to deal
   in the Software without restriction, including without limitation the rights
   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
   copies of the Software, and to permit persons to whom the Software is
   furnished to do so, subject to the following conditions:

   The above copyright notice and this permission notice shall be included in
   all copies or substantial portions of the Software.

   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
   THE SOFTWARE. 
*/

package com.alee.utils.text;

import java.io.Serializable;

/**
 * Simple lorem ipsum text generator.
 * Suitable for creating sample data for test cases and performance tests.
 *
 * @author Sven Jacobs
 * @version 1.0
 */
public class LoremIpsum implements Serializable
{
    /**
     * Complete lorem ipsum text.
     */
    public static final String LOREM_IPSUM = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, " +
            "sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. " +
            "At vero eos et accusam et justo duo dolores et ea rebum. " +
            "Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";

    /**
     * Separated lorem ipsum words.
     */
    private final String[] loremIpsumWords;

    /**
     * Constructs new {@link LoremIpsum}.
     */
    public LoremIpsum ()
    {
        this.loremIpsumWords = LOREM_IPSUM.split ( "\\s" );
    }

    /**
     * Returns one sentence (50 words) of the lorem ipsum text.
     *
     * @return 50 words of lorem ipsum text
     */
    public String getWords ()
    {
        return getWords ( 50 );
    }

    /**
     * Returns words from the lorem ipsum text.
     *
     * @param amount Amount of words
     * @return Lorem ipsum text
     */
    public String getWords ( final int amount )
    {
        return getWords ( amount, 0 );
    }

    /**
     * Returns words from the lorem ipsum text.
     *
     * @param amount     Amount of words
     * @param startIndex Start index of word to begin with (must be &gt;= 0 and &lt; 50)
     * @return Lorem ipsum text
     * @throws IndexOutOfBoundsException If startIndex is &lt; 0 or &gt; 49
     */
    public String getWords ( final int amount, final int startIndex )
    {
        if ( startIndex < 0 || startIndex > 49 )
        {
            throw new IndexOutOfBoundsException ( "startIndex must be >= 0 and < 50" );
        }
        int word = startIndex;
        final StringBuilder lorem = new StringBuilder ();
        for ( int i = 0; i < amount; i++ )
        {
            if ( word == 50 )
            {
                word = 0;
            }
            lorem.append ( loremIpsumWords[ word ] );
            if ( i < amount - 1 )
            {
                lorem.append ( ' ' );
            }
            word++;
        }
        return lorem.toString ();
    }

    /**
     * Returns two paragraphs of lorem ipsum.
     *
     * @return Lorem ipsum paragraphs
     */
    public String getParagraphs ()
    {
        return getParagraphs ( 2 );
    }

    /**
     * Returns paragraphs of lorem ipsum.
     *
     * @param amount Amount of paragraphs
     * @return Lorem ipsum paragraphs
     */
    public String getParagraphs ( final int amount )
    {
        final StringBuilder lorem = new StringBuilder ();
        for ( int i = 0; i < amount; i++ )
        {
            lorem.append ( LOREM_IPSUM );
            if ( i < amount - 1 )
            {
                lorem.append ( "\n\n" );
            }
        }
        return lorem.toString ();
    }
}