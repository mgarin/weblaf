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

package com.alee.extended.label;

import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Set of JUnit tests for {@link WebStyledLabel} and related classes.
 *
 * @author Mikle Garin
 */
@FixMethodOrder ( MethodSorters.JVM )
public final class StyledLabelTest
{
    /**
     * Tests {@link StyleRanges} parsing edge cases when plain text should be the same as input text.
     */
    @Test
    public void plainText ()
    {
        final StyleRanges test1 = new StyleRanges ( "Sample {text} with no style" );
        checkParsingResult (
                test1.getStyledText (),
                test1.getPlainText (),
                test1.getStyledText ()
        );

        final StyleRanges test2 = new StyleRanges ( "{text}" );
        checkParsingResult (
                test2.getStyledText (),
                test2.getPlainText (),
                test2.getStyledText ()
        );

        final StyleRanges test3 = new StyleRanges ( "{{{text}}}" );
        checkParsingResult (
                test3.getStyledText (),
                test3.getPlainText (),
                test3.getStyledText ()
        );

        final StyleRanges test4 = new StyleRanges ( "{text:test}" );
        checkParsingResult (
                test4.getStyledText (),
                test4.getPlainText (),
                test4.getStyledText ()
        );

        final StyleRanges test5 = new StyleRanges ( "{text" );
        checkParsingResult (
                test5.getStyledText (),
                test5.getPlainText (),
                test5.getStyledText ()
        );

        final StyleRanges test6 = new StyleRanges ( "{text:b" );
        checkParsingResult (
                test6.getStyledText (),
                test6.getPlainText (),
                test6.getStyledText ()
        );
    }

    /**
     * Tests {@link StyleRanges} parsing.
     */
    @Test
    public void styleRangesParsing ()
    {
        final StyleRanges test1 = new StyleRanges ( "Sample {text:b} with {some:color(red)} styles" );
        checkParsingResult (
                test1.getStyledText (),
                test1.getPlainText (),
                "Sample text with some styles"
        );

        final StyleRanges test2 = new StyleRanges ( "{text:b}" );
        checkParsingResult (
                test2.getStyledText (),
                test2.getPlainText (),
                "text"
        );

        final StyleRanges test3 = new StyleRanges ( "{text:b;i;c(red)}" );
        checkParsingResult (
                test3.getStyledText (),
                test3.getPlainText (),
                "text"
        );

        final StyleRanges test4 = new StyleRanges ( "{Complex styled text:b;i;c(255,0,0)}" );
        checkParsingResult (
                test4.getStyledText (),
                test4.getPlainText (),
                "Complex styled text"
        );
    }

    /**
     * Asserts style parsing result.
     *
     * @param input    input text
     * @param result   parsing result
     * @param expected expected parsing result
     */
    private void checkParsingResult ( @Nullable final String input, @Nullable final String result, @Nullable final String expected )
    {
        if ( !Objects.equals ( result, expected ) )
        {
            throw new RuntimeException ( String.format (
                    "Unexpected style syntax parsing outcome for text: %s" + "\n" + "Expected: %s" + "\n" + "Result: %s",
                    input, expected, result
            ) );
        }
    }
}