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

import com.alee.api.jdk.Objects;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Set of JUnit tests for {@link TextUtils}.
 *
 * @author Mikle Garin
 */
@FixMethodOrder ( MethodSorters.JVM )
public final class TextUtilsTest
{
    /**
     * Word detection test.
     */
    @Test
    public void wordDetection ()
    {
        final String small = smallSentence ();
        assertWord ( small, 0, null );
        assertWord ( small, "Short", 0 );
        assertWord ( small, "Short", 2 );
        assertWord ( small, "Short", 4 );
        assertWord ( small, small.length (), null );

        final String simple = simpleSentence ();
        assertWord ( simple, "sample", 0 );
        assertWord ( simple, "sample", 3 );
        assertWord ( simple, "sample", 5 );
        assertWord ( simple, "it", 0 );
        assertWord ( simple, "it", 1 );
        assertWord ( simple, simple.length (), null );

        final String twisted = twistedSentence ();
        assertWord ( twisted, "mple", 0 );
        assertWord ( twisted, "mple", 1 );
        assertWord ( twisted, "mple", 3 );
        assertWord ( twisted, 26, null );
        assertWord ( twisted, "long", 0 );
        assertWord ( twisted, "long", 1 );
        assertWord ( twisted, "long", 3 );
        assertWord ( twisted, "Don", 0 );
        assertWord ( twisted, "Don", 1 );
        assertWord ( twisted, "Don", 2 );
        assertWord ( twisted, twisted.length (), null );
    }

    /**
     * Asserts word detection.
     *
     * @param sentence   sentence to look for word in
     * @param word       word to look for
     * @param indexShift index shift for test
     */
    private void assertWord ( final String sentence, final String word, final int indexShift )
    {
        assertWord ( sentence, sentence.indexOf ( word ) + indexShift, word );
    }

    /**
     * Asserts word detection.
     *
     * @param sentence sentence to look for word in
     * @param location location to find word at
     * @param word     word to compare with
     */
    private void assertWord ( final String sentence, final int location, final String word )
    {
        assert Objects.equals ( TextUtils.getWord ( sentence, location ), word ) : wordDetectionErrorText ();
    }

    /**
     * Word start detection test.
     */
    @Test
    public void wordStartDetection ()
    {
        final String small = smallSentence ();
        assertWordStart ( small, 0, -1 );
        assertWordStart ( small, "Short", 0 );
        assertWordStart ( small, "Short", 2 );
        assertWordStart ( small, "Short", 4 );
        assertWordStart ( small, small.length (), -1 );

        final String simple = simpleSentence ();
        assertWordStart ( simple, "sample", 0 );
        assertWordStart ( simple, "sample", 3 );
        assertWordStart ( simple, "sample", 5 );
        assertWordStart ( simple, "it", 0 );
        assertWordStart ( simple, "it", 1 );
        assertWordStart ( simple, simple.length (), -1 );

        final String twisted = twistedSentence ();
        assertWordStart ( twisted, "mple", 0 );
        assertWordStart ( twisted, "mple", 1 );
        assertWordStart ( twisted, "mple", 3 );
        assertWordStart ( twisted, "long", 0 );
        assertWordStart ( twisted, "long", 1 );
        assertWordStart ( twisted, "long", 3 );
        assertWordStart ( twisted, "Don", 0 );
        assertWordStart ( twisted, "Don", 1 );
        assertWordStart ( twisted, "Don", 2 );
        assertWordStart ( twisted, twisted.length (), -1 );
    }

    /**
     * Asserts word start detection.
     *
     * @param sentence   sentence to look for word in
     * @param word       word to look for
     * @param indexShift index shift for test
     */
    private void assertWordStart ( final String sentence, final String word, final int indexShift )
    {
        final int startIndex = sentence.indexOf ( word );
        assertWordStart ( sentence, startIndex + indexShift, startIndex );
    }

    /**
     * Asserts word start detection.
     *
     * @param sentence sentence to look for word in
     * @param location location to find word at
     * @param start    word start index to compare with
     */
    private void assertWordStart ( final String sentence, final int location, final int start )
    {
        assert Objects.equals ( TextUtils.getWordStart ( sentence, location ), start ) : wordDetectionErrorText ();
    }

    /**
     * Returns new small sentence for tests.
     *
     * @return new small sentence for tests
     */
    private static String smallSentence ()
    {
        return " Short ";
    }

    /**
     * Returns new simple sentence for tests.
     *
     * @return new simple sentence for tests
     */
    private static String simpleSentence ()
    {
        return "Just a sample long sentence for running these tests. Don't mind it.";
    }

    /**
     * Returns new twisted sentence for tests.
     *
     * @return new twisted sentence for tests
     */
    private static String twistedSentence ()
    {
        return "Just ;#$^a$%! sa@#%mple  \t!\n,  long;sentence (for) .running \nthese \"tests\\. /Don't *mind it . !";
    }

    /**
     * Returns word detection error text.
     *
     * @return word detection error text
     */
    private static String wordDetectionErrorText ()
    {
        return "Word detection failed";
    }
}