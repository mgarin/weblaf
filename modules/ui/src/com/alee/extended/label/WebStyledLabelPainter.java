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

import com.alee.laf.WebLookAndFeel;
import com.alee.managers.log.Log;
import com.alee.managers.style.skin.web.WebLabelPainter;
import com.alee.utils.FontUtils;
import com.alee.utils.SwingUtils;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikle Garin
 */

public class WebStyledLabelPainter<E extends WebStyledLabel, U extends WebStyledLabelUI> extends WebLabelPainter<E, U>
        implements StyledLabelPainter<E, U>, SwingConstants
{
    /**
     * Style settings.
     */
    protected int preferredRowCount = WebStyledLabelStyle.preferredRowCount;
    protected boolean ignoreColorSettings = WebStyledLabelStyle.ignoreColorSettings;
    protected float scriptFontRatio = WebStyledLabelStyle.scriptFontRatio;
    protected String truncatedTextSuffix = WebStyledLabelStyle.truncatedTextSuffix;

    /**
     * Runtime variables.
     */
    protected final List<TextRange> textRanges = new ArrayList<TextRange> ();
    protected boolean truncated = false;

    /**
     * Returns preferred row count.
     *
     * @return preferred row count
     */
    public int getPreferredRowCount ()
    {
        return preferredRowCount;
    }

    /**
     * {@inheritDoc}
     */
    public void setPreferredRowCount ( final int rows )
    {
        this.preferredRowCount = rows;
        revalidate ();
        repaint ();
    }

    /**
     * Returns whether color settings should be ignored or not.
     *
     * @return true if color settings should be ignored, false otherwise
     */
    public boolean isIgnoreColorSettings ()
    {
        return ignoreColorSettings;
    }

    /**
     * {@inheritDoc}
     */
    public void setIgnoreColorSettings ( final boolean ignore )
    {
        this.ignoreColorSettings = ignore;
        repaint ();
    }

    /**
     * Returns subscript and superscript font ratio.
     *
     * @return subscript and superscript font ratio
     */
    public float getScriptFontRatio ()
    {
        return scriptFontRatio;
    }

    /**
     * {@inheritDoc}
     */
    public void setScriptFontRatio ( final float ratio )
    {
        this.scriptFontRatio = ratio;
        revalidate ();
        repaint ();
    }

    /**
     * Returns truncated text suffix.
     *
     * @return truncated text suffix
     */
    public String getTruncatedTextSuffix ()
    {
        return truncatedTextSuffix;
    }

    /**
     * {@inheritDoc}
     */
    public void setTruncatedTextSuffix ( final String suffix )
    {
        this.truncatedTextSuffix = suffix;
        revalidate ();
        repaint ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTextRanges ()
    {
        textRanges.clear ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void paintEnabledText ( final E label, final Graphics2D g2d, final String text, final int textX, final int textY )
    {
        paintStyledText ( label, g2d, textX, textY );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void paintDisabledText ( final E label, final Graphics2D g2d, final String text, final int textX, final int textY )
    {
        paintStyledText ( label, g2d, textX, textY );
    }

    /**
     * Paints styled text.
     *
     * @param label painted label
     * @param g     graphics context
     * @param textX text X coordinate
     * @param textY text Y coordinate
     */
    protected void paintStyledText ( final E label, final Graphics2D g, final int textX, final int textY )
    {
        // Resetting truncated flag
        truncated = false;

        // Patinting styled text
        final int labelWidth = getLabelWidth ( label );
        final int textWidth = getTextWidth ( label );
        final int w = Math.min ( labelWidth, textWidth );
        paintStyledTextImpl ( label, g, textX, textY, w );
    }

    /**
     * Returns current label width.
     *
     * @param label painted label
     * @return current label width
     */
    protected int getLabelWidth ( final E label )
    {
        int lw = label.getWidth ();
        if ( label.isLineWrap () )
        {
            final int oldPreferredWidth = label.getPreferredWidth ();
            final int oldRows = label.getRows ();
            try
            {
                label.setRows ( 0 );
                lw = getPreferredSize ().width;
                label.setPreferredWidth ( oldPreferredWidth > 0 ? Math.min ( label.getWidth (), oldPreferredWidth ) : label.getWidth () );
                final Dimension sizeOnWidth = getPreferredSize ();
                if ( sizeOnWidth.width < lw )
                {
                    lw = sizeOnWidth.width;
                }
            }
            finally
            {
                label.setPreferredWidth ( oldPreferredWidth );
                label.setRows ( oldRows );
            }
        }
        return lw;
    }

    /**
     * Returns current label's text width.
     *
     * @param label painted label
     * @return current label's text width
     */
    protected int getTextWidth ( final E label )
    {
        int textWidth = label.getWidth ();
        if ( label.getInsets () != null )
        {
            textWidth -= label.getInsets ().left + label.getInsets ().right;
        }
        if ( label.getIcon () != null && label.getHorizontalTextPosition () != SwingConstants.CENTER )
        {
            textWidth -= label.getIcon ().getIconWidth () + label.getIconTextGap ();
        }
        return textWidth;
    }

    /**
     * Paints styled text.
     *
     * @param label painted label
     * @param g     graphics context
     * @param textX text X coordinate
     * @param textY text Y coordinate
     * @param w     painted text max width
     * @return painted rows amount
     */
    protected int paintStyledTextImpl ( final E label, final Graphics2D g, final int textX, int textY, final int w )
    {
        final Insets insets = label.getInsets ();
        final int labelHeight = getLabelHeight ( label, insets );
        final int startX = getStartX ( label, textX, insets );
        final int endX = w + startX;

        int mnemonicIndex = label.getDisplayedMnemonicIndex ();
        if ( UIManager.getLookAndFeel () instanceof WebLookAndFeel && WebLookAndFeel.isMnemonicHidden () ||
                UIManager.getLookAndFeel () instanceof WindowsLookAndFeel && WindowsLookAndFeel.isMnemonicHidden () )
        {
            mnemonicIndex = -1;
        }

        int y;
        int x = startX;

        int charDisplayed = 0;
        boolean displayMnemonic;
        int mneIndex = 0;

        Font font = StyledLabelUtils.getFont ( label );
        final FontMetrics fm = label.getFontMetrics ( font );
        FontMetrics fm2;
        FontMetrics nextFm2 = null;
        final int defaultFontSize = font.getSize ();

        String nextS;
        int maxRowHeight = fm.getHeight ();
        int minStartY = fm.getAscent ();

        int horizontalAlignment = label.getHorizontalAlignment ();
        switch ( horizontalAlignment )
        {
            case LEADING:
                horizontalAlignment = label.getComponentOrientation ().isLeftToRight () ? LEFT : RIGHT;
                break;
            case TRAILING:
                horizontalAlignment = label.getComponentOrientation ().isLeftToRight () ? RIGHT : LEFT;
                break;
        }

        for ( final TextRange textRange : textRanges )
        {
            final StyleRange style = textRange.styleRange;
            final int size = ( style != null && ( style.isSuperscript () || style.isSubscript () ) ) ?
                    Math.round ( ( float ) defaultFontSize / scriptFontRatio ) : defaultFontSize;

            font = StyledLabelUtils.getFont ( label );
            if ( style != null && ( ( style.getStyle () != -1 && font.getStyle () != style.getStyle () ) || font.getSize () != size ) )
            {
                font = FontUtils.getCachedDerivedFont ( font, style.getStyle () == -1 ? font.getStyle () : style.getStyle (), size );
                fm2 = label.getFontMetrics ( font );
                maxRowHeight = Math.max ( maxRowHeight, fm2.getHeight () );
                minStartY = Math.max ( minStartY, fm2.getAscent () );
            }
        }

        boolean lineWrap = label.isLineWrap ();
        if ( !lineWrap )
        {
            for ( final TextRange textRange : textRanges )
            {
                if ( textRange.text.endsWith ( "\n" ) )
                {
                    lineWrap = true;
                    break;
                }
            }
        }
        if ( lineWrap && textY < minStartY )
        {
            textY = minStartY;
        }

        int nextRowStartIndex = 0;
        int rowCount = 0;
        int rowStartOffset = 0;
        for ( int i = 0; i < textRanges.size (); i++ )
        {
            final TextRange textRange = textRanges.get ( i );
            final StyleRange style = textRange.styleRange;

            if ( mnemonicIndex >= 0 && textRange.text.length () - nextRowStartIndex > mnemonicIndex - charDisplayed )
            {
                displayMnemonic = true;
                mneIndex = mnemonicIndex - charDisplayed;
            }
            else
            {
                displayMnemonic = false;
            }
            charDisplayed += textRange.text.length () - nextRowStartIndex;
            if ( textRange.text.contains ( "\r" ) || textRange.text.contains ( "\n" ) )
            {
                final boolean lastRow = ( label.getMaximumRows () > 0 && rowCount >= label.getMaximumRows () - 1 ) ||
                        textY + maxRowHeight + Math.max ( 0, label.getRowGap () ) > labelHeight;
                if ( horizontalAlignment != LEFT && g != null )
                {
                    if ( lastRow && i != textRanges.size () - 1 )
                    {
                        x += fm.stringWidth ( truncatedTextSuffix );
                    }
                    paintRow ( label, g, startX, x, endX, textY, rowStartOffset, style.getStartIndex () + textRange.text.length (),
                            lastRow );
                }
                rowStartOffset = style.getStartIndex ();
                nextRowStartIndex = 0;
                nextFm2 = null;
                if ( !lastRow )
                {
                    rowStartOffset += style.getLength ();
                    rowCount++;
                    x = startX;
                    textY += maxRowHeight + Math.max ( 0, label.getRowGap () );

                    // continue to paint truncated text suffix if lastRow is true
                    continue;
                }
                else if ( horizontalAlignment != LEFT && g != null )
                {
                    break;
                }
            }

            y = textY;

            if ( nextFm2 == null )
            {
                final int size = ( style != null && ( style.isSuperscript () || style.isSubscript () ) ) ?
                        Math.round ( ( float ) defaultFontSize / scriptFontRatio ) : defaultFontSize;

                font = StyledLabelUtils.getFont ( label );
                if ( style != null && ( ( style.getStyle () != -1 && font.getStyle () != style.getStyle () ) || font.getSize () != size ) )
                {
                    font = FontUtils.getCachedDerivedFont ( font, style.getStyle () == -1 ? font.getStyle () : style.getStyle (), size );
                    fm2 = label.getFontMetrics ( font );
                }
                else
                {
                    fm2 = fm;
                }
            }
            else
            {
                fm2 = nextFm2;
            }

            if ( g != null )
            {
                g.setFont ( font );
            }

            boolean stop = false;
            String s = textRange.text.substring ( Math.min ( nextRowStartIndex, textRange.text.length () ) );
            if ( s.contains ( "\r" ) || s.contains ( "\n" ) )
            {
                s = truncatedTextSuffix;
                stop = true;
            }

            int strWidth = fm2.stringWidth ( s );

            boolean wrapped = false;
            final int widthLeft = endX - x;
            if ( widthLeft < strWidth && widthLeft >= 0 )
            {
                if ( label.isLineWrap () &&
                        ( ( label.getMaximumRows () > 0 && rowCount < label.getMaximumRows () - 1 ) || label.getMaximumRows () <= 0 ) &&
                        y + maxRowHeight + Math.max ( 0, label.getRowGap () ) <= labelHeight )
                {
                    wrapped = true;
                    int availLength = s.length () * widthLeft / strWidth + 1;
                    int nextWordStartIndex;
                    int nextRowStartIndexInSubString = 0;
                    boolean needBreak = false;
                    boolean needContinue = false;
                    int loopCount = 0;
                    do
                    {
                        final String subString = s.substring ( 0, Math.max ( 0, Math.min ( availLength, s.length () ) ) );
                        int firstRowWordEndIndex = StyledLabelUtils.findFirstRowWordEndIndex ( subString );
                        nextWordStartIndex =
                                firstRowWordEndIndex < 0 ? 0 : StyledLabelUtils.findNextWordStartIndex ( s, firstRowWordEndIndex );
                        if ( firstRowWordEndIndex < 0 )
                        {
                            if ( x != startX )
                            {
                                final boolean lastRow = label.getMaximumRows () > 0 && rowCount >= label.getMaximumRows () - 1;
                                if ( horizontalAlignment != LEFT && g != null )
                                {
                                    paintRow ( label, g, startX, x, endX, textY, rowStartOffset,
                                            style.getStartIndex () + Math.min ( nextRowStartIndex, textRange.text.length () ), lastRow );
                                }
                                textY += maxRowHeight + Math.max ( 0, label.getRowGap () );
                                x = startX;
                                i--;
                                rowCount++;
                                rowStartOffset = style.getStartIndex () + Math.min ( nextRowStartIndex, textRange.text.length () );
                                if ( lastRow )
                                {
                                    needBreak = true;
                                }
                                needContinue = true;
                                break;
                            }
                            else
                            {
                                firstRowWordEndIndex = 0;
                                nextWordStartIndex = Math.min ( s.length (), availLength );
                            }
                        }
                        nextRowStartIndexInSubString = firstRowWordEndIndex + 1;
                        final String subStringThisRow = s.substring ( 0, Math.min ( nextRowStartIndexInSubString, s.length () ) );
                        strWidth = fm2.stringWidth ( subStringThisRow );
                        if ( strWidth > widthLeft )
                        {
                            availLength = subString.length () * widthLeft / strWidth;
                        }
                        loopCount++;
                        if ( loopCount > 15 )
                        {
                            Log.error ( "Styled label paint error: " + textRange );
                            break;
                        }
                    }
                    while ( strWidth > widthLeft && availLength > 0 );
                    if ( needBreak )
                    {
                        break;
                    }
                    if ( needContinue )
                    {
                        continue;
                    }
                    while ( nextRowStartIndexInSubString < nextWordStartIndex )
                    {
                        strWidth += fm2.charWidth ( s.charAt ( nextRowStartIndexInSubString ) );
                        if ( strWidth >= widthLeft )
                        {
                            break;
                        }
                        nextRowStartIndexInSubString++;
                    }
                    s = s.substring ( 0, Math.min ( nextRowStartIndexInSubString, s.length () ) );
                    strWidth = fm2.stringWidth ( s );
                    charDisplayed -= textRange.text.length () - nextRowStartIndex;
                    if ( displayMnemonic )
                    {
                        if ( mnemonicIndex >= 0 && s.length () > mnemonicIndex - charDisplayed )
                        {
                            displayMnemonic = true;
                            mneIndex = mnemonicIndex - charDisplayed;
                        }
                        else
                        {
                            displayMnemonic = false;
                        }
                    }
                    charDisplayed += s.length ();
                    nextRowStartIndex += nextRowStartIndexInSubString;
                }
                else
                {
                    // use this method to clip string
                    s = SwingUtilities
                            .layoutCompoundLabel ( label, fm2, s, null, label.getVerticalAlignment (), label.getHorizontalAlignment (),
                                    label.getVerticalTextPosition (), label.getHorizontalTextPosition (),
                                    new Rectangle ( x, y, widthLeft, labelHeight ), new Rectangle (), new Rectangle (), 0 );
                    strWidth = fm2.stringWidth ( s );
                }
                stop = !lineWrap || y + maxRowHeight + Math.max ( 0, label.getRowGap () ) > labelHeight ||
                        ( label.getMaximumRows () > 0 && rowCount >= label.getMaximumRows () - 1 );
            }
            else if ( lineWrap )
            {
                nextRowStartIndex = 0;
            }
            else if ( i < textRanges.size () - 1 )
            {
                final TextRange nextTextRange = textRanges.get ( i + 1 );
                final String nextText = nextTextRange.text;
                final StyleRange nextStyle = nextTextRange.styleRange;
                final int size = ( nextStyle != null && ( nextStyle.isSuperscript () || nextStyle.isSubscript () ) ) ?
                        Math.round ( ( float ) defaultFontSize / scriptFontRatio ) : defaultFontSize;

                font = StyledLabelUtils.getFont ( label );
                if ( nextStyle != null &&
                        ( ( nextStyle.getStyle () != -1 && font.getStyle () != nextStyle.getStyle () ) || font.getSize () != size ) )
                {
                    font = FontUtils
                            .getCachedDerivedFont ( font, nextStyle.getStyle () == -1 ? font.getStyle () : nextStyle.getStyle (), size );
                    nextFm2 = label.getFontMetrics ( font );
                }
                else
                {
                    nextFm2 = fm;
                }
                if ( nextFm2.stringWidth ( nextText ) > widthLeft - strWidth )
                {
                    nextS = SwingUtilities.layoutCompoundLabel ( label, nextFm2, nextText, null, label.getVerticalAlignment (),
                            label.getHorizontalAlignment (), label.getVerticalTextPosition (), label.getHorizontalTextPosition (),
                            new Rectangle ( x + strWidth, y, widthLeft - strWidth, labelHeight ), new Rectangle (), new Rectangle (), 0 );
                    if ( nextFm2.stringWidth ( nextS ) > widthLeft - strWidth )
                    {
                        s = SwingUtilities
                                .layoutCompoundLabel ( label, fm2, s, null, label.getVerticalAlignment (), label.getHorizontalAlignment (),
                                        label.getVerticalTextPosition (), label.getHorizontalTextPosition (),
                                        new Rectangle ( x, y, strWidth - 1, labelHeight ), new Rectangle (), new Rectangle (), 0 );
                        strWidth = fm2.stringWidth ( s );
                        stop = true;
                    }
                }
            }

            // start of actual painting
            if ( rowCount > 0 && x == startX && s.startsWith ( " " ) )
            {
                s = s.substring ( 1 );
                strWidth = fm2.stringWidth ( s );
            }
            if ( horizontalAlignment == LEFT && g != null )
            {
                if ( style != null && style.isSuperscript () )
                {
                    y -= fm.getHeight () - fm2.getHeight ();
                }

                if ( style != null && style.getBackground () != null )
                {
                    g.setColor ( style.getBackground () );
                    g.fillRect ( x, y - fm2.getHeight (), strWidth, fm2.getHeight () + 4 );
                }

                Color textColor = ( style != null && !ignoreColorSettings && style.getForeground () != null ) ? style.getForeground () :
                        label.getForeground ();
                if ( !label.isEnabled () )
                {
                    textColor = UIManager.getColor ( "Label.disabledForeground" );
                }
                g.setColor ( textColor );

                if ( displayMnemonic )
                {
                    SwingUtils.drawStringUnderlineCharAt ( g, s, mneIndex, x, y );
                }
                else
                {
                    SwingUtils.drawString ( g, s, x, y );
                }

                if ( style != null )
                {
                    if ( style.isStrikeThrough () )
                    {
                        final int lineY = y + ( fm2.getDescent () - fm2.getAscent () ) / 2;
                        g.drawLine ( x, lineY, x + strWidth - 1, lineY );
                    }
                    if ( style.isDoubleStrikeThrough () )
                    {
                        final int lineY = y + ( fm2.getDescent () - fm2.getAscent () ) / 2;
                        g.drawLine ( x, lineY - 1, x + strWidth - 1, lineY - 1 );
                        g.drawLine ( x, lineY + 1, x + strWidth - 1, lineY + 1 );
                    }
                    if ( style.isUnderlined () )
                    {
                        final int lineY = y + 1;
                        g.drawLine ( x, lineY, x + strWidth - 1, lineY );
                    }
                    if ( style.isWaved () )
                    {
                        final int waveY = y + 1;
                        for ( int waveX = x; waveX < x + strWidth; waveX += 4 )
                        {
                            if ( waveX + 2 <= x + strWidth - 1 )
                            {
                                g.drawLine ( waveX, waveY + 2, waveX + 2, waveY );
                            }
                            if ( waveX + 4 <= x + strWidth - 1 )
                            {
                                g.drawLine ( waveX + 3, waveY + 1, waveX + 4, waveY + 2 );
                            }
                        }
                    }
                }
            }
            // end of actual painting

            if ( stop )
            {
                if ( horizontalAlignment != LEFT && g != null )
                {
                    x += strWidth;
                    paintRow ( label, g, startX, x, endX, textY, rowStartOffset, label.getText ().length (), true );
                }

                // Marking truncated
                truncated = true;

                break;
            }

            if ( wrapped )
            {
                final boolean lastRow = ( label.getMaximumRows () > 0 && rowCount >= label.getMaximumRows () - 1 ) ||
                        textY + maxRowHeight + Math.max ( 0, label.getRowGap () ) > labelHeight;
                if ( horizontalAlignment != LEFT && g != null )
                {
                    x += strWidth;
                    paintRow ( label, g, startX, x, endX, textY, rowStartOffset,
                            style.getStartIndex () + Math.min ( nextRowStartIndex, textRange.text.length () ), lastRow );
                }
                textY += maxRowHeight + Math.max ( 0, label.getRowGap () );
                x = startX;
                i--;
                rowCount++;
                rowStartOffset = style.getStartIndex () + Math.min ( nextRowStartIndex, textRange.text.length () );
                if ( lastRow )
                {
                    break;
                }
            }
            else
            {
                x += strWidth;
            }
            if ( i == textRanges.size () - 1 )
            {
                if ( horizontalAlignment != LEFT && g != null )
                {
                    paintRow ( label, g, startX, x, endX, textY, rowStartOffset, -1, true );
                }
            }
        }
        return ( int ) Math.ceil ( ( double ) textY / maxRowHeight );
    }

    /**
     * Returns start X coordinate.
     *
     * @param label  painted label
     * @param textX  text X coordinate
     * @param insets label insets
     * @return start X coordinate
     */
    protected int getStartX ( final E label, final int textX, final Insets insets )
    {
        int leftMostX = 0;
        if ( insets != null )
        {
            leftMostX += insets.left;
        }
        if ( label.getIcon () != null )
        {
            int horizontalTextPosition = label.getHorizontalTextPosition ();
            if ( ( horizontalTextPosition == SwingConstants.TRAILING && label.getComponentOrientation ().isLeftToRight () ) ||
                    ( horizontalTextPosition == SwingConstants.LEADING && !label.getComponentOrientation ().isLeftToRight () ) )
            {
                horizontalTextPosition = SwingConstants.RIGHT;
            }
            if ( horizontalTextPosition == SwingConstants.RIGHT )
            {
                leftMostX += label.getIcon ().getIconWidth () + label.getIconTextGap ();
            }
        }
        return textX < leftMostX ? leftMostX : textX;
    }

    /**
     * Returns current label height.
     *
     * @param label  painted label
     * @param insets label insets
     * @return current label height
     */
    protected int getLabelHeight ( final E label, final Insets insets )
    {
        int labelHeight = label.getHeight ();
        if ( labelHeight <= 0 )
        {
            labelHeight = Integer.MAX_VALUE;
        }
        if ( insets != null )
        {
            labelHeight -= insets.top + insets.bottom;
        }
        return labelHeight;
    }

    /**
     * Paints single text row.
     *
     * @param label          painted label
     * @param g              graphics context
     * @param leftAlignmentX left alignment X coordinate
     * @param thisLineEndX   line end X coordinate
     * @param rightMostX     right most X coordinate
     * @param textY          text Y coordinate
     * @param startOffset    start offset
     * @param endOffset      end offset
     * @param lastRow        whether this is the last row or not
     */
    @SuppressWarnings ( "StatementWithEmptyBody" )
    protected void paintRow ( final E label, final Graphics2D g, final int leftAlignmentX, final int thisLineEndX, final int rightMostX,
                              final int textY, final int startOffset, final int endOffset, final boolean lastRow )
    {
        if ( g == null )
        {
            return;
        }
        int horizontalTextPosition = label.getHorizontalTextPosition ();
        int horizontalAlignment = label.getHorizontalAlignment ();
        if ( ( horizontalTextPosition == SwingConstants.TRAILING && !label.getComponentOrientation ().isLeftToRight () ) ||
                ( horizontalTextPosition == SwingConstants.LEADING && label.getComponentOrientation ().isLeftToRight () ) )
        {
            horizontalTextPosition = SwingConstants.LEFT;
        }
        if ( ( horizontalTextPosition == SwingConstants.LEADING && !label.getComponentOrientation ().isLeftToRight () ) ||
                ( horizontalTextPosition == SwingConstants.TRAILING && label.getComponentOrientation ().isLeftToRight () ) )
        {
            horizontalTextPosition = SwingConstants.RIGHT;
        }
        if ( ( horizontalAlignment == SwingConstants.TRAILING && !label.getComponentOrientation ().isLeftToRight () ) ||
                ( horizontalAlignment == SwingConstants.LEADING && label.getComponentOrientation ().isLeftToRight () ) )
        {
            horizontalAlignment = SwingConstants.LEFT;
        }
        if ( ( horizontalAlignment == SwingConstants.LEADING && !label.getComponentOrientation ().isLeftToRight () ) ||
                ( horizontalAlignment == SwingConstants.TRAILING && label.getComponentOrientation ().isLeftToRight () ) )
        {
            horizontalAlignment = SwingConstants.RIGHT;
        }

        final Insets insets = label.getInsets ();
        int textX = leftAlignmentX;
        int paintWidth = thisLineEndX - leftAlignmentX;
        if ( horizontalAlignment == RIGHT )
        {
            paintWidth = thisLineEndX - textX;
            textX = label.getWidth () - paintWidth;
            if ( insets != null )
            {
                textX -= insets.right;
            }
            if ( label.getIcon () != null && horizontalTextPosition == SwingConstants.LEFT )
            {
                textX -= label.getIcon ().getIconWidth () + label.getIconTextGap ();
            }
        }
        else if ( horizontalAlignment == CENTER )
        {
            int leftMostX = 0;
            if ( horizontalTextPosition == SwingConstants.RIGHT && label.getIcon () != null )
            {
                leftMostX += label.getIcon ().getIconWidth () + label.getIconTextGap ();
            }
            int labelWidth = label.getWidth ();
            if ( insets != null )
            {
                labelWidth -= insets.right + insets.left;
                leftMostX += insets.left;
            }
            if ( label.getIcon () != null && horizontalTextPosition != SwingConstants.CENTER )
            {
                labelWidth -= label.getIcon ().getIconWidth () + label.getIconTextGap ();
            }
            textX = leftMostX + ( labelWidth - paintWidth ) / 2;
        }
        paintWidth = Math.min ( paintWidth, rightMostX - leftAlignmentX );

        int mnemonicIndex = label.getDisplayedMnemonicIndex ();
        if ( UIManager.getLookAndFeel () instanceof WindowsLookAndFeel && WindowsLookAndFeel.isMnemonicHidden () )
        {
            mnemonicIndex = -1;
        }

        int charDisplayed = 0;
        boolean displayMnemonic;
        int mneIndex = 0;
        Font font = StyledLabelUtils.getFont ( label );
        final FontMetrics fm = label.getFontMetrics ( font );
        FontMetrics fm2;
        FontMetrics nextFm2 = null;
        final int defaultFontSize = font.getSize ();

        int x = textX;
        for ( int i = 0; i < textRanges.size () && ( endOffset < 0 || charDisplayed < endOffset ); i++ )
        {
            final TextRange textRange = textRanges.get ( i );
            final StyleRange style = textRange.styleRange;
            int length = style.getLength ();
            if ( length < 0 )
            {
                length = textRange.text.length ();
            }
            if ( style.getStartIndex () + length <= startOffset )
            {
                charDisplayed += length;
                continue;
            }
            final int nextRowStartIndex = style.getStartIndex () >= startOffset ? 0 : startOffset - style.getStartIndex ();
            charDisplayed += nextRowStartIndex;

            if ( mnemonicIndex >= 0 && textRange.text.length () - nextRowStartIndex > mnemonicIndex - charDisplayed )
            {
                displayMnemonic = true;
                mneIndex = mnemonicIndex - charDisplayed;
            }
            else
            {
                displayMnemonic = false;
            }
            int paintLength = textRange.text.length () - nextRowStartIndex;
            if ( endOffset >= 0 && charDisplayed + paintLength >= endOffset )
            {
                paintLength = endOffset - charDisplayed;
            }
            charDisplayed += paintLength;

            int y = textY;

            if ( nextFm2 == null )
            {
                final int size = ( style != null && ( style.isSuperscript () || style.isSubscript () ) ) ?
                        Math.round ( ( float ) defaultFontSize / scriptFontRatio ) : defaultFontSize;

                font = StyledLabelUtils.getFont ( label );
                if ( style != null && ( ( style.getStyle () != -1 && font.getStyle () != style.getStyle () ) || font.getSize () != size ) )
                {
                    font = FontUtils.getCachedDerivedFont ( font, style.getStyle () == -1 ? font.getStyle () : style.getStyle (), size );
                    fm2 = label.getFontMetrics ( font );
                }
                else
                {
                    fm2 = fm;
                }
            }
            else
            {
                fm2 = nextFm2;
            }

            g.setFont ( font );

            String s = textRange.text.substring ( Math.min ( nextRowStartIndex, textRange.text.length () ) );
            if ( startOffset > 0 && x == textX && s.startsWith ( " " ) )
            {
                s = s.substring ( 1 );
            }
            if ( s.length () > paintLength )
            {
                s = s.substring ( 0, paintLength );
            }
            if ( s.contains ( "\r" ) || s.contains ( "\n" ) )
            {
                if ( textRange.styleRange.getStartIndex () + textRange.styleRange.getLength () >= endOffset )
                {
                    break;
                }
                s = truncatedTextSuffix;
            }

            int strWidth = fm2.stringWidth ( s );

            final int widthLeft = paintWidth + textX - x;
            if ( widthLeft < strWidth )
            {
                if ( strWidth <= 0 )
                {
                    return;
                }
                if ( label.isLineWrap () && !lastRow )
                {
                    int availLength = s.length () * widthLeft / strWidth + 1;
                    int nextWordStartIndex;
                    int nextRowStartIndexInSubString;
                    int loopCount = 0;
                    do
                    {
                        final String subString = s.substring ( 0, Math.max ( 0, Math.min ( availLength, s.length () ) ) );
                        int firstRowWordEndIndex = StyledLabelUtils.findFirstRowWordEndIndex ( subString );
                        nextWordStartIndex =
                                firstRowWordEndIndex < 0 ? 0 : StyledLabelUtils.findNextWordStartIndex ( s, firstRowWordEndIndex );
                        if ( firstRowWordEndIndex < 0 )
                        {
                            if ( x == textX )
                            {
                                firstRowWordEndIndex = 0;
                                nextWordStartIndex = Math.min ( s.length (), availLength );
                            }
                        }
                        nextRowStartIndexInSubString = firstRowWordEndIndex + 1;
                        final String subStringThisRow = s.substring ( 0, Math.min ( nextRowStartIndexInSubString, s.length () ) );
                        strWidth = fm2.stringWidth ( subStringThisRow );
                        if ( strWidth > widthLeft )
                        {
                            availLength = subString.length () * widthLeft / strWidth;
                        }
                        loopCount++;
                        if ( loopCount > 50 )
                        {
                            Log.error ( "Styled label paint error: " + textRange );
                            break;
                        }
                    }
                    while ( strWidth > widthLeft && availLength > 0 );
                    while ( nextRowStartIndexInSubString < nextWordStartIndex )
                    {
                        strWidth += fm2.charWidth ( s.charAt ( nextRowStartIndexInSubString ) );
                        if ( strWidth >= widthLeft )
                        {
                            break;
                        }
                        nextRowStartIndexInSubString++;
                    }
                    s = s.substring ( 0, Math.min ( nextRowStartIndexInSubString, s.length () ) );
                    strWidth = fm2.stringWidth ( s );
                    charDisplayed -= textRange.text.length () - nextRowStartIndex;
                    if ( displayMnemonic )
                    {
                        if ( mnemonicIndex >= 0 && s.length () > mnemonicIndex - charDisplayed )
                        {
                            displayMnemonic = true;
                            mneIndex = mnemonicIndex - charDisplayed;
                        }
                        else
                        {
                            displayMnemonic = false;
                        }
                    }
                    charDisplayed += s.length ();
                }
                else
                {
                    // use this method to clip string
                    s = SwingUtilities
                            .layoutCompoundLabel ( label, fm2, s, null, label.getVerticalAlignment (), label.getHorizontalAlignment (),
                                    label.getVerticalTextPosition (), label.getHorizontalTextPosition (),
                                    new Rectangle ( x, y, widthLeft, label.getHeight () ), new Rectangle (), new Rectangle (), 0 );
                    strWidth = fm2.stringWidth ( s );
                }
            }
            else if ( label.isLineWrap () )
            {
                // Do nothing
            }
            else if ( i < textRanges.size () - 1 )
            {
                final TextRange nextTextRange = textRanges.get ( i + 1 );
                final String nextText = nextTextRange.text;
                final StyleRange nextStyle = nextTextRange.styleRange;
                final int size = ( nextStyle != null && ( nextStyle.isSuperscript () || nextStyle.isSubscript () ) ) ?
                        Math.round ( ( float ) defaultFontSize / scriptFontRatio ) : defaultFontSize;

                font = StyledLabelUtils.getFont ( label );
                if ( nextStyle != null &&
                        ( ( nextStyle.getStyle () != -1 && font.getStyle () != nextStyle.getStyle () ) || font.getSize () != size ) )
                {
                    font = FontUtils
                            .getCachedDerivedFont ( font, nextStyle.getStyle () == -1 ? font.getStyle () : nextStyle.getStyle (), size );
                    nextFm2 = label.getFontMetrics ( font );
                }
                else
                {
                    nextFm2 = fm;
                }
                if ( nextFm2.stringWidth ( nextText ) > widthLeft - strWidth )
                {
                    final String nextS = SwingUtilities.layoutCompoundLabel ( label, nextFm2, nextText, null, label.getVerticalAlignment (),
                            label.getHorizontalAlignment (), label.getVerticalTextPosition (), label.getHorizontalTextPosition (),
                            new Rectangle ( x + strWidth, y, widthLeft - strWidth, label.getHeight () ), new Rectangle (), new Rectangle (),
                            0 );
                    if ( nextFm2.stringWidth ( nextS ) > widthLeft - strWidth )
                    {
                        s = SwingUtilities
                                .layoutCompoundLabel ( label, fm2, s, null, label.getVerticalAlignment (), label.getHorizontalAlignment (),
                                        label.getVerticalTextPosition (), label.getHorizontalTextPosition (),
                                        new Rectangle ( x, y, strWidth - 1, label.getHeight () ), new Rectangle (), new Rectangle (), 0 );
                        strWidth = fm2.stringWidth ( s );
                    }
                }
            }

            // start of actual painting
            if ( style != null && style.isSuperscript () )
            {
                y -= fm.getHeight () - fm2.getHeight ();
            }

            if ( style != null && style.getBackground () != null )
            {
                g.setColor ( style.getBackground () );
                g.fillRect ( x, y - fm2.getHeight (), strWidth, fm2.getHeight () + 4 );
            }

            Color textColor = ( style != null && !ignoreColorSettings && style.getForeground () != null ) ? style.getForeground () :
                    label.getForeground ();
            if ( !label.isEnabled () )
            {
                textColor = UIManager.getColor ( "Label.disabledForeground" );
            }
            g.setColor ( textColor );

            if ( displayMnemonic )
            {
                SwingUtils.drawStringUnderlineCharAt ( g, s, mneIndex, x, y );
            }
            else
            {
                SwingUtils.drawString ( g, s, x, y );
            }

            if ( style != null )
            {
                if ( style.isStrikeThrough () )
                {
                    final int lineY = y + ( fm2.getDescent () - fm2.getAscent () ) / 2;
                    g.drawLine ( x, lineY, x + strWidth - 1, lineY );
                }
                if ( style.isDoubleStrikeThrough () )
                {
                    final int lineY = y + ( fm2.getDescent () - fm2.getAscent () ) / 2;
                    g.drawLine ( x, lineY - 1, x + strWidth - 1, lineY - 1 );
                    g.drawLine ( x, lineY + 1, x + strWidth - 1, lineY + 1 );
                }
                if ( style.isUnderlined () )
                {
                    final int lineY = y + 1;
                    g.drawLine ( x, lineY, x + strWidth - 1, lineY );
                }
                if ( style.isWaved () )
                {
                    final int waveY = y + 1;
                    for ( int waveX = x; waveX < x + strWidth; waveX += 4 )
                    {
                        if ( waveX + 2 <= x + strWidth - 1 )
                        {
                            g.drawLine ( waveX, waveY + 2, waveX + 2, waveY );
                        }
                        if ( waveX + 4 <= x + strWidth - 1 )
                        {
                            g.drawLine ( waveX + 3, waveY + 1, waveX + 4, waveY + 2 );
                        }
                    }
                }
            }

            // End of actual painting
            x += strWidth;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String layoutCL ( final E label, final FontMetrics fontMetrics, final String text, final Icon icon, final Rectangle viewR,
                                final Rectangle iconR, final Rectangle textR )
    {
        Dimension size = null;
        if ( label instanceof WebStyledLabel )
        {
            final int oldPreferredWidth = label.getPreferredWidth ();
            final int oldRows = label.getRows ();
            try
            {
                if ( label.isLineWrap () && label.getWidth () > 0 )
                {
                    label.setPreferredWidth ( label.getWidth () );
                }
                size = getPreferredSize ();
                if ( oldPreferredWidth > 0 && oldPreferredWidth < label.getWidth () )
                {
                    label.setPreferredWidth ( oldPreferredWidth );
                    size = getPreferredSize ();
                }
                else if ( label.isLineWrap () && label.getMinimumRows () > 0 )
                {
                    label.setPreferredWidth ( 0 );
                    label.setRows ( 0 );
                    final Dimension minSize = getPreferredSize ();
                    if ( minSize.height > size.height )
                    {
                        size = minSize;
                    }
                }
            }
            finally
            {
                label.setPreferredWidth ( oldPreferredWidth );
                label.setRows ( oldRows );
            }
        }
        else
        {
            size = label.getPreferredSize ();
        }
        textR.width = size.width;
        textR.height = size.height;
        if ( label.getIcon () != null )
        {
            textR.width -= label.getIcon ().getIconWidth () + label.getIconTextGap ();
        }

        final int va = label.getVerticalAlignment ();
        final int ha = label.getHorizontalAlignment ();
        final int vtp = label.getVerticalTextPosition ();
        final int htp = label.getHorizontalTextPosition ();
        final int gap = label.getIconTextGap ();
        return StyledLabelUtils.layoutCompoundLabel ( label, text, icon, va, ha, vtp, htp, viewR, iconR, textR, gap );
    }
}