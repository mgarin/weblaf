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

import com.alee.api.clone.behavior.OmitOnClone;
import com.alee.api.merge.Merge;
import com.alee.api.merge.behavior.OmitOnMerge;
import com.alee.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Base implementation of text ranges.
 * It builds text ranges using provided {@link #plainText} and {@link #styleRanges}.
 * Resulting list of text ranges is always cached in {@link #textRanges} field.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebStyledLabel">How to use WebStyledLabel</a>
 */
public class TextRanges implements ITextRanges
{
    /**
     * Comparator for style range sorting.
     */
    protected static final Comparator<StyleRange> styleRangeComparator = new StyleRangeComparator ();

    /**
     * Special text parts which should always be enclosed in a separated {@link TextRange}.
     */
    protected static final List<String> specialParts = CollectionUtils.asList ( "\r\n", "\n", "\r" );

    /**
     * Plain text.
     */
    protected final String plainText;

    /**
     * Style ranges list.
     */
    protected final List<StyleRange> styleRanges;

    /**
     * Cached text ranges built using {@link #plainText} and {@link #styleRanges}.
     */
    @OmitOnClone
    @OmitOnMerge
    protected transient List<TextRange> textRanges;

    /**
     * Constructs new text ranges.
     *
     * @param plainText   plain text
     * @param styleRanges style ranges list
     */
    public TextRanges ( final String plainText, final List<StyleRange> styleRanges )
    {
        super ();
        this.plainText = plainText;
        this.styleRanges = styleRanges;
    }

    @Override
    public List<TextRange> getTextRanges ()
    {
        if ( textRanges == null )
        {
            // Creating list of approximately correct size
            textRanges = new ArrayList<TextRange> ( ( int ) ( styleRanges.size () * 1.4 + 1 ) );

            // Sorting style ranges by their positions
            Collections.sort ( styleRanges, styleRangeComparator );

            // Checking whether text is empty or not
            if ( plainText != null )
            {
                final int length = plainText.length ();
                if ( length > 0 )
                {
                    // Iterating through style borders
                    // todo This can and should be heavily optimized later on
                    // todo First split the whole text length into pieces by borders and remember styles for each piece
                    // todo After that iterate through all borders until the end without any additional iterations
                    int start = 0;
                    int end;
                    while ( start < plainText.length () )
                    {
                        // Determining closest end of range
                        end = plainText.length ();
                        for ( final String specialPart : specialParts )
                        {
                            // Checking special part borders
                            final int partStart = plainText.indexOf ( specialPart, start );
                            if ( partStart != -1 )
                            {
                                final int partEnd = partStart + specialPart.length ();
                                if ( partEnd > start )
                                {
                                    end = Math.min ( end, partStart > start ? partStart : partEnd );
                                }
                            }
                        }
                        for ( final StyleRange style : styleRanges )
                        {
                            // Checking style range borders
                            final int styleStart = style.getStartIndex ();
                            final int styleEnd = styleStart + style.getLength ();
                            if ( styleEnd > start )
                            {
                                end = Math.min ( end, styleStart > start ? styleStart : styleEnd );
                            }
                        }

                        // Extracting text part
                        final String part = plainText.substring ( start, end );

                        // Collecting styles included into that range
                        // All of the found styles will be fully included as they should also be fully covering the range
                        StyleRange styleRange = null;
                        if ( !specialParts.contains ( part ) )
                        {
                            for ( final StyleRange style : styleRanges )
                            {
                                // Checking intersection of the style range with current text range
                                final int styleStart = style.getStartIndex ();
                                final int styleEnd = styleStart + style.getLength ();
                                if ( Math.max ( styleStart, start ) < Math.min ( styleEnd, end ) )
                                {
                                    // Merging style ranges
                                    styleRange = Merge.deep ().merge ( styleRange, style );
                                }
                            }
                        }
                        styleRange = styleRange != null ? new StyleRange ( start, end - start, styleRange ) : null;

                        // Adding new text range
                        textRanges.add ( new TextRange ( part, styleRange ) );

                        // Moving forward to the next closest style borrt;
                        start = end;
                    }

                    // Adding enclosing text range if needed
                    if ( start < plainText.length () )
                    {
                        textRanges.add ( new TextRange ( plainText.substring ( start ) ) );
                    }
                }
            }
        }
        return textRanges;
    }
}