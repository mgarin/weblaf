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

package com.alee.painter.decoration.content;

import com.alee.extended.label.StyleRange;
import com.alee.extended.label.StyleRangeComparator;
import com.alee.extended.label.TextRange;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.FontUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.general.Pair;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Abstract implementation of styled text content
 *
 * @param <E> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Alexandr Zernov
 */

@SuppressWarnings ( "UnusedParameters" )
public abstract class AbstractStyledTextContent<E extends JComponent, D extends IDecoration<E, D>, I extends AbstractStyledTextContent<E, D, I>>
        extends AbstractTextContent<E, D, I>
{
    /**
     * todo 1. Implement minimum rows count
     */

    /**
     * Comparator for style range sorting.
     */
    protected static transient final Comparator<StyleRange> styleRangeComparator = new StyleRangeComparator ();

    /**
     * Whether or not should ignore style font color settings.
     */
    @XStreamAsAttribute
    protected Boolean ignoreStyleColors;

    /**
     * Script font ratio.
     */
    @XStreamAsAttribute
    protected Float scriptFontRatio;

    /**
     * Whether or not should keep hard line breaks.
     */
    @XStreamAsAttribute
    protected Boolean preserveLineBreaks;

    /**
     * Size of gaps between label rows.
     */
    @XStreamAsAttribute
    protected Integer rowGap;

    /**
     * Runtime variables.
     */
    protected transient final List<TextRange> textRanges = new ArrayList<TextRange> ();

    @Override
    public void activate ( final E c, final D d )
    {
        super.activate ( c, d );

        buildTextRanges ( c, d, textRanges );
    }

    @Override
    public void deactivate ( final E c, final D d )
    {
        textRanges.clear ();

        super.deactivate ( c, d );
    }

    /**
     * Returns whether or not ignore style font color settings.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return true if ignore style font color settings, false otherwise
     */
    protected boolean isIgnoreColorSettings ( final E c, final D d )
    {
        return ignoreStyleColors != null && ignoreStyleColors;
    }

    /**
     * Returns script font ratio.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return script font ratio
     */
    protected Float getScriptFontRatio ( final E c, final D d )
    {
        return scriptFontRatio != null ? scriptFontRatio : 1.5f;
    }

    /**
     * Returns whether hard breaks are preserved or not.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return true if hard breaks are preserved, false otherwise
     */
    protected boolean isPreserveLineBreaks ( final E c, final D d )
    {
        return preserveLineBreaks == null || preserveLineBreaks;
    }

    /**
     * Returns text row gap.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return text row gap
     */
    protected int getRowGap ( final E c, final D d )
    {
        return rowGap != null ? rowGap : 0;
    }

    /**
     * Returns list of text style ranges.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return list of text style ranges
     */
    protected abstract List<StyleRange> getStyleRanges ( E c, D d );

    /**
     * Returns maximum rows count.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return maximum rows count
     */
    protected abstract int getMaximumRows ( E c, D d );

    /**
     * Returns text wrapping type.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return text wrapping type
     */
    protected abstract TextWrap getWrapType ( E c, D d );

    @Override
    protected void paintText ( final Graphics2D g2d, final Rectangle bounds, final E c, final D d )
    {
        final int x = bounds.x;
        int y = bounds.y;
        final int rg = Math.max ( 0, getRowGap ( c, d ) );

        // Layout the text
        final List<Row> rows = layout ( c, d, bounds );

        if ( !rows.isEmpty () )
        {
            // Calculating y-axis offset
            final int va = getVerticalAlignment ( c, d );
            if ( va != TOP )
            {
                // Calculating total height
                int th = -rg;
                for ( final Row row : rows )
                {
                    th += row.height + rg;
                }

                if ( th < bounds.height )
                {
                    switch ( va )
                    {
                        case CENTER:
                            y += ( bounds.height - th ) / 2;
                            break;

                        case BOTTOM:
                            y += bounds.height - th;
                            break;
                    }
                }
            }

            final Pair<Integer, Integer> fs = getFontSize ( c, d );
            y += fs.getKey ();

            // Painting the text
            for ( int i = 0; i < rows.size (); i++ )
            {
                final Row row = rows.get ( i );
                paintRow ( c, d, g2d, bounds, x, y, row, i == rows.size () - 1 );
                y += row.height + rg;
            }
        }
    }

    /**
     * Performs styled text layout.
     *
     * @param c      painted component
     * @param d      painted decoration state
     * @param bounds painting bounds
     * @return List of rows to paint
     */
    protected List<Row> layout ( final E c, final D d, final Rectangle bounds )
    {
        final int endY = bounds.y + bounds.height;
        final int endX = bounds.x + bounds.width;

        final Pair<Integer, Integer> fs = getFontSize ( c, d );
        final int maxRowHeight = fs.getValue ();
        final int maxAscent = fs.getKey ();

        int x = bounds.x;
        int y = bounds.y + maxAscent;

        final int mnemonicIndex = getMnemonicIndex ( c, d );
        final int mr = getMaximumRows ( c, d );
        final int rg = getRowGap ( c, d );
        final TextWrap wt = getWrapType ( c, d );
        final boolean preserveLineBreaks = isPreserveLineBreaks ( c, d );

        Font font = c.getFont ();
        final int defaultFontSize = font.getSize ();

        int rowCount = 0;
        int charDisplayed = 0;
        int nextRowStartIndex = 0;
        Row row = new Row ( maxRowHeight );

        boolean readyToPaint = false;
        final List<Row> rows = new ArrayList<Row> ();

        // Painting the text
        for ( int i = 0; i < textRanges.size (); i++ )
        {
            final TextRange textRange = textRanges.get ( i );
            final StyleRange style = textRange.styleRange;

            if ( textRange.text.equals ( "\n" ) )
            {
                if ( !row.isEmpty () && ( wt == TextWrap.none || preserveLineBreaks ) )
                {
                    i++;
                    readyToPaint = true;
                }

                // todo May be append space!?
            }
            else if ( nextRowStartIndex == 0 || nextRowStartIndex < textRange.text.length () )
            {
                String s = textRange.text.substring ( nextRowStartIndex, textRange.text.length () );

                // Updating font if need
                final int size = ( style != null && ( style.isSuperscript () || style.isSubscript () ) ) ?
                        Math.round ( ( float ) defaultFontSize / getScriptFontRatio ( c, d ) ) : defaultFontSize;

                font = c.getFont ();
                if ( style != null && ( ( style.getStyle () != -1 && font.getStyle () != style.getStyle () ) || font.getSize () != size ) )
                {
                    font = FontUtils.getCachedDerivedFont ( font, style.getStyle () == -1 ? font.getStyle () : style.getStyle (), size );
                }
                final FontMetrics cfm = c.getFontMetrics ( font );

                int strWidth = cfm.stringWidth ( s );
                final int widthLeft = endX - x;

                if ( wt != TextWrap.none && widthLeft < strWidth && widthLeft >= 0 )
                {
                    if ( ( ( mr > 0 && rowCount < mr - 1 ) || mr <= 0 ) && y + maxRowHeight + Math.max ( 0, rg ) <= endY )
                    {
                        int availLength = s.length () * widthLeft / strWidth + 1; // Optimistic prognoses
                        int firstWordOffset = Math.max ( 0, findFirstWordFromIndex ( s, 0 ) );
                        int nextRowStartInSubString = 0;

                        do
                        {
                            final String subStringThisRow;
                            int lastInWordEndIndex;
                            if ( wt == TextWrap.word || wt == TextWrap.mixed )
                            {
                                final String subString = s.substring ( 0, Math.max ( 0, Math.min ( availLength, s.length () ) ) );

                                // Searching last word start
                                lastInWordEndIndex = findLastRowWordStartIndex ( subString.trim () );

                                // Only one word in row left
                                if ( lastInWordEndIndex < 0 )
                                {
                                    if ( wt == TextWrap.word )
                                    {
                                        if ( row.isEmpty () )
                                        {
                                            // Search last index of the first word end
                                            nextRowStartInSubString = firstWordOffset + findFirstRowWordEndIndex ( s.trim () );
                                        }

                                        break;
                                    }
                                    else
                                    {
                                        if ( row.isEmpty () )
                                        {
                                            lastInWordEndIndex = availLength - 1;
                                            firstWordOffset = 0;
                                        }
                                    }
                                }

                                nextRowStartInSubString = firstWordOffset + lastInWordEndIndex + 1;
                                subStringThisRow = subString.substring ( 0, Math.min ( nextRowStartInSubString, subString.length () ) );
                            }
                            else//if ( wt == WrapType.character )
                            {
                                subStringThisRow = s.substring ( 0, Math.max ( 0, Math.min ( availLength, s.length () ) ) );
                                nextRowStartInSubString = availLength;
                                firstWordOffset = 0;
                            }

                            strWidth = cfm.stringWidth ( subStringThisRow );
                            if ( strWidth > widthLeft )
                            {
                                // todo Try to optimize
                                availLength--;
                            }
                        }
                        while ( strWidth > widthLeft && availLength > 0 );

                        if ( nextRowStartInSubString > 0 && ( availLength > 0 || ( availLength <= 0 && row.isEmpty () ) ) )
                        {
                            // Extracting wrapped text fragment
                            s = s.substring ( 0, Math.min ( nextRowStartInSubString, s.length () ) );
                            strWidth = row.append ( s, style, cfm, charDisplayed, mnemonicIndex );
                            charDisplayed += s.length ();
                            nextRowStartIndex += nextRowStartInSubString;
                        }
                        else if ( row.isEmpty () )
                        {
                            // Skipping current text range
                            i++;
                            strWidth = row.append ( s, style, cfm, charDisplayed, mnemonicIndex );
                            charDisplayed += s.length ();
                            nextRowStartIndex = 0;
                        }
                        else
                        {
                            strWidth = 0;
                            nextRowStartIndex = 0;
                        }
                    }
                    else
                    {
                        strWidth = row.append ( s, style, cfm, charDisplayed, mnemonicIndex );
                        charDisplayed += s.length ();
                        nextRowStartIndex = 0;
                    }

                    readyToPaint = true;
                }
                else
                {
                    strWidth = row.append ( s, style, cfm, charDisplayed, mnemonicIndex );
                    charDisplayed += s.length ();
                    nextRowStartIndex = 0;
                }

                x += strWidth;
            }
            else
            {
                nextRowStartIndex = 0;
            }

            if ( readyToPaint && !row.isEmpty () )
            {
                rows.add ( row );

                rowCount++;
                row = new Row ( maxRowHeight );
                readyToPaint = false;

                // Setting up next row
                y += maxRowHeight + Math.max ( 0, rg );
                x = bounds.x;
                i--;

                // Checking that row is last
                if ( y > endY || ( mr > 0 && rowCount >= mr - 1 ) )
                {
                    break;
                }
            }
        }

        // Painting last row if need
        if ( !row.isEmpty () )
        {
            rows.add ( row );
        }

        return rows;
    }

    /**
     * Paints single styled text row.
     *
     * @param c      painted component
     * @param d      painted decoration state
     * @param g2d    graphics context
     * @param bounds painting bounds
     * @param textX  text X coordinate
     * @param textY  text Y coordinate
     * @param row    painted row
     * @param isLast whether or not painted row is last
     */
    private void paintRow ( final E c, final D d, final Graphics2D g2d, final Rectangle bounds, final int textX, final int textY,
                            final Row row, final boolean isLast )
    {
        int horizontalAlignment = getHorizontalAlignment ( c, d );
        final boolean ltr = c.getComponentOrientation ().isLeftToRight ();
        if ( ( horizontalAlignment == SwingConstants.TRAILING && !ltr ) || ( horizontalAlignment == SwingConstants.LEADING && ltr ) )
        {
            horizontalAlignment = SwingConstants.LEFT;
        }
        else if ( ( horizontalAlignment == SwingConstants.LEADING && !ltr ) || ( horizontalAlignment == SwingConstants.TRAILING && ltr ) )
        {
            horizontalAlignment = SwingConstants.RIGHT;
        }

        int x = textX;

        if ( bounds.width > row.width )
        {
            switch ( horizontalAlignment )
            {
                case CENTER:
                    x += ( bounds.width - row.width ) / 2;
                    break;
                case RIGHT:
                    x += bounds.width - row.width;
            }
        }

        final Font font = c.getFont ();
        final int defaultFontSize = font.getSize ();
        final FontMetrics fm = c.getFontMetrics ( font );
        final TextWrap wt = getWrapType ( c, d );
        final boolean truncated = isTruncated ( c, d );
        int charDisplayed = 0;

        for ( final TextRange textRange : row.fragments )
        {
            final StyleRange style = textRange.getStyleRange ();

            // Updating font if need
            final int size = ( style != null && ( style.isSuperscript () || style.isSubscript () ) ) ?
                    Math.round ( ( float ) defaultFontSize / getScriptFontRatio ( c, d ) ) : defaultFontSize;

            Font cFont = c.getFont ();
            if ( style != null && ( ( style.getStyle () != -1 && cFont.getStyle () != style.getStyle () ) || cFont.getSize () != size ) )
            {
                cFont = FontUtils.getCachedDerivedFont ( cFont, style.getStyle () == -1 ? cFont.getStyle () : style.getStyle (), size );
            }
            final FontMetrics cfm = c.getFontMetrics ( cFont );

            int y = textY;
            String s = textRange.text;
            final int strWidth = cfm.stringWidth ( s );

            // Checking mnemonic
            int mneIndex = -1;
            if ( row.mnemonic >= 0 && row.mnemonic < charDisplayed + s.length () )
            {
                mneIndex = row.mnemonic - charDisplayed;
            }

            // Checking trim needs
            final int availableWidth = bounds.width + bounds.x - x;
            if ( truncated && availableWidth < strWidth &&
                    ( wt == TextWrap.none || wt == TextWrap.word || isLast ) )
            {
                // Clip string
                s = SwingUtilities.layoutCompoundLabel ( cfm, s, null, 0, horizontalAlignment, 0, 0,
                        new Rectangle ( x, y, availableWidth, bounds.height ), new Rectangle (), new Rectangle (), 0 );
            }

            // Starting of actual painting
            g2d.setFont ( cFont );

            if ( style != null )
            {
                if ( style.isSuperscript () )
                {
                    y -= fm.getHeight () - cfm.getHeight ();
                }
                else if ( style.isSubscript () )
                {
                    y += fm.getDescent () - cfm.getDescent ();
                }
            }

            if ( style != null && style.getBackground () != null )
            {
                g2d.setPaint ( style.getBackground () );
                g2d.fillRect ( x, y - cfm.getAscent (), strWidth, cfm.getAscent () + cfm.getDescent () );
            }

            final boolean useStyleForeground = style != null && !isIgnoreColorSettings ( c, d ) && style.getForeground () != null;
            final Color textColor = useStyleForeground ? style.getForeground () : getColor ( c, d );
            g2d.setPaint ( textColor );
            paintStyledTextFragment ( c, d, g2d, s, x, y, mneIndex, cfm, style, strWidth );

            x += strWidth;
            charDisplayed += s.length ();
        }
    }

    /**
     * Returns font max height and ascent.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return font max height and ascent
     */
    protected Pair<Integer, Integer> getFontSize ( final E c, final D d )
    {
        final Font font = c.getFont ();
        final int defaultFontSize = font.getSize ();
        final FontMetrics fm = c.getFontMetrics ( font );

        int maxHeight = fm.getHeight ();
        int maxAscent = fm.getAscent ();

        for ( final TextRange textRange : textRanges )
        {
            final StyleRange style = textRange.styleRange;
            final int size = ( style != null && ( style.isSuperscript () || style.isSubscript () ) ) ?
                    Math.round ( ( float ) defaultFontSize / getScriptFontRatio ( c, d ) ) : defaultFontSize;

            Font cFont = font;
            if ( style != null && ( ( style.getStyle () != -1 && cFont.getStyle () != style.getStyle () ) || cFont.getSize () != size ) )
            {
                cFont = FontUtils.getCachedDerivedFont ( cFont, style.getStyle () == -1 ? cFont.getStyle () : style.getStyle (), size );
                final FontMetrics fm2 = c.getFontMetrics ( cFont );
                maxHeight = Math.max ( maxHeight, fm2.getHeight () );
                maxAscent = Math.max ( maxAscent, fm2.getAscent () );
            }
        }

        return new Pair<Integer, Integer> ( maxAscent, maxHeight );
    }

    /**
     * Actually paints styled text fragment.
     *
     * @param c        painted component
     * @param d        painted decoration state
     * @param g2d      graphics context
     * @param s        text fragment
     * @param x        text X coordinate
     * @param y        text Y coordinate
     * @param mneIndex index of mnemonic
     * @param fm       text fragment font metrics
     * @param style    style of text fragment
     * @param strWidth text fragment width
     */
    protected void paintStyledTextFragment ( final E c, final D d, final Graphics2D g2d, final String s, final int x, final int y,
                                             final int mneIndex, final FontMetrics fm, final StyleRange style, final int strWidth )
    {
        paintTextFragment ( c, d, g2d, s, x, y, mneIndex );

        if ( style != null )
        {
            if ( style.isStrikeThrough () )
            {
                final int lineY = y + ( fm.getDescent () - fm.getAscent () ) / 2;
                g2d.drawLine ( x, lineY, x + strWidth - 1, lineY );
            }
            if ( style.isDoubleStrikeThrough () )
            {
                final int lineY = y + ( fm.getDescent () - fm.getAscent () ) / 2;
                g2d.drawLine ( x, lineY - 1, x + strWidth - 1, lineY - 1 );
                g2d.drawLine ( x, lineY + 1, x + strWidth - 1, lineY + 1 );
            }
            if ( style.isUnderlined () )
            {
                final int lineY = y + 1;
                g2d.drawLine ( x, lineY, x + strWidth - 1, lineY );
            }
            if ( style.isWaved () )
            {
                final int waveY = y + 1;
                for ( int waveX = x; waveX < x + strWidth; waveX += 4 )
                {
                    if ( waveX + 2 <= x + strWidth - 1 )
                    {
                        g2d.drawLine ( waveX, waveY + 2, waveX + 2, waveY );
                    }
                    if ( waveX + 4 <= x + strWidth - 1 )
                    {
                        g2d.drawLine ( waveX + 3, waveY + 1, waveX + 4, waveY + 2 );
                    }
                }
            }
        }
    }

    @Override
    protected Dimension getPreferredTextSize ( final E c, final D d, final Dimension available )
    {
        final Dimension vSize = getPreferredStyledTextSize ( c, d, new Dimension ( Short.MAX_VALUE, Short.MAX_VALUE ) );
        final Dimension hSize = getPreferredStyledTextSize ( c, d, available );
        return SwingUtils.max ( vSize, hSize );
    }

    /**
     * Returns preferred styled text size.
     *
     * @param c         painted component
     * @param d         painted decoration state
     * @param available theoretically available space for this content
     * @return preferred styled text size
     */
    protected Dimension getPreferredStyledTextSize ( final E c, final D d, final Dimension available )
    {
        final Dimension ps = new Dimension ( 0, 0 );
        final List<Row> rows = layout ( c, d, new Rectangle ( 0, 0, available.width, available.height ) );
        if ( !rows.isEmpty () )
        {
            final int rg = Math.max ( 0, getRowGap ( c, d ) );
            ps.height -= rg;
            for ( final Row row : rows )
            {
                ps.width = Math.max ( ps.width, row.width );
                ps.height += row.height + rg;
            }
        }
        return ps;
    }

    /**
     * Parses label style ranges into text ranges.
     * All parsed text ranges are stored within provided list.
     * <p/>
     *
     * @param c          painted component
     * @param d          painted decoration state
     * @param textRanges list to store text ranges into
     */
    protected void buildTextRanges ( final E c, final D d, final List<TextRange> textRanges )
    {
        textRanges.clear ();

        // Sorting style ranges by their positions
        final List<StyleRange> styleRanges = getStyleRanges ( c, d );
        Collections.sort ( styleRanges, styleRangeComparator );

        // Checking whether text is empty or not
        final String s = getText ( c, d );
        if ( s != null && s.length () > 0 )
        {
            int index = 0;
            for ( final StyleRange styleRange : styleRanges )
            {
                if ( index >= s.length () )
                {
                    break;
                }

                // Add text range for the gap between current and previous style
                if ( styleRange.getStartIndex () > index )
                {
                    final String text = s.substring ( index, Math.min ( styleRange.getStartIndex (), s.length () ) );
                    final StyleRange newRange = new StyleRange ( index, styleRange.getStartIndex () - index );
                    addStyledTexts ( text, newRange, textRanges );
                    index = styleRange.getStartIndex ();
                }

                // Add text range for customized style
                if ( styleRange.getStartIndex () == index )
                {
                    // Either till the end or not
                    if ( styleRange.getLength () == -1 )
                    {
                        final String text = s.substring ( index );
                        addStyledTexts ( text, styleRange, textRanges );
                        index = s.length ();
                    }
                    else
                    {
                        final String text = s.substring ( index, Math.min ( index + styleRange.getLength (), s.length () ) );
                        addStyledTexts ( text, styleRange, textRanges );
                        index += styleRange.getLength ();
                    }
                }
            }

            // Add enclosing text range
            if ( index < s.length () )
            {
                final String text = s.substring ( index, s.length () );
                final StyleRange range = new StyleRange ( index, s.length () - index );
                addStyledTexts ( text, range, textRanges );
            }
        }
    }

    /**
     * Adds parsed text ranges into provided list.
     *
     * @param text       text to parse
     * @param range      {@link com.alee.extended.label.StyleRange}
     * @param textRanges list of {@link com.alee.extended.label.TextRange} to add ranges into
     */
    protected void addStyledTexts ( String text, StyleRange range, final List<TextRange> textRanges )
    {
        // Copying style range to avoid original parameters changes
        range = new StyleRange ( range );
        int index1 = text.indexOf ( '\r' );
        int index2 = text.indexOf ( '\n' );

        while ( index1 >= 0 || index2 >= 0 )
        {
            int index = index1 >= 0 ? index1 : -1;
            if ( index2 >= 0 && ( index2 < index1 || index < 0 ) )
            {
                index = index2;
            }

            final String subString = text.substring ( 0, index );
            StyleRange newRange = new StyleRange ( range );
            newRange.setStartIndex ( range.getStartIndex () );
            newRange.setLength ( index );
            textRanges.add ( new TextRange ( subString, newRange ) );

            int length = 1;
            if ( text.charAt ( index ) == '\r' && index + 1 < text.length () && text.charAt ( index + 1 ) == '\n' )
            {
                length++;
            }

            newRange = new StyleRange ( range );
            newRange.setStartIndex ( range.getStartIndex () + index );
            newRange.setLength ( length );
            textRanges.add ( new TextRange ( text.substring ( index, index + length ), newRange ) );

            text = text.substring ( index + length );
            range.setStartIndex ( range.getStartIndex () + index + length );
            range.setLength ( range.getLength () - index - length );

            index1 = text.indexOf ( '\r' );
            index2 = text.indexOf ( '\n' );
        }
        if ( text.length () > 0 )
        {
            textRanges.add ( new TextRange ( text, range ) );
        }
    }

    /**
     * Returns begin index of last word in the specified text.
     *
     * @param string text to process
     * @return begin index of last word in the specified text
     */
    public static int findLastRowWordStartIndex ( final String string )
    {
        boolean spaceFound = false;
        boolean skipSpace = true;
        for ( int i = string.length () - 1; i >= 0; i-- )
        {
            final char c = string.charAt ( i );
            if ( !spaceFound && !skipSpace )
            {
                if ( c == ' ' || c == '\t' || c == '\r' || c == '\n' )
                {
                    spaceFound = true;
                }
            }
            else
            {
                if ( c != ' ' && c != '\t' && c != '\r' && c != '\n' )
                {
                    if ( spaceFound )
                    {
                        return i;
                    }
                    skipSpace = false;
                }
            }
        }
        return -1;
    }

    /**
     * Returns begin index of first word after specified index.
     *
     * @param string text to process
     * @param from   index to start search from
     * @return begin index of first word after specified index
     */
    public static int findFirstWordFromIndex ( final String string, final int from )
    {
        for ( int i = from; i < string.length (); i++ )
        {
            final char c = string.charAt ( i );
            if ( c != ' ' && c != '\t' && c != '\r' && c != '\n' )
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns last index of the first word end.
     *
     * @param string text to process
     * @return last index of the first word end
     */
    public static int findFirstRowWordEndIndex ( final String string )
    {
        boolean spaceFound = false;
        for ( int i = 0; i < string.length (); i++ )
        {
            final char c = string.charAt ( i );
            if ( !spaceFound )
            {
                if ( c == ' ' || c == '\t' || c == '\r' || c == '\n' )
                {
                    spaceFound = true;
                }
            }
            else
            {
                if ( c != ' ' && c != '\t' && c != '\r' && c != '\n' )
                {
                    return i;
                }
            }
        }
        return string.length ();
    }

    @Override
    public I merge ( final I content )
    {
        super.merge ( content );
        ignoreStyleColors = content.isOverwrite () || content.ignoreStyleColors != null ? content.ignoreStyleColors : ignoreStyleColors;
        scriptFontRatio = content.isOverwrite () || content.scriptFontRatio != null ? content.scriptFontRatio : scriptFontRatio;
        truncate = content.isOverwrite () || content.truncate != null ? content.truncate : truncate;
        preserveLineBreaks = content.isOverwrite () || content.preserveLineBreaks != null ? content.preserveLineBreaks : preserveLineBreaks;
        rowGap = content.isOverwrite () || content.rowGap != null ? content.rowGap : rowGap;
        return ( I ) this;
    }
}