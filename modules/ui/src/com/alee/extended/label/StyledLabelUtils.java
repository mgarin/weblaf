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

import com.alee.utils.TextUtils;
import com.alee.utils.xml.ColorConverter;

import javax.swing.*;
import javax.swing.text.View;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * This class provides a set of utilities for WebStyledLabel component.
 *
 * @author Mikle Garin
 */

public final class StyledLabelUtils implements SwingConstants
{
    /**
     * Custom StyleRange comparator.
     * Compares style ranges by their positions.
     */
    private static final Comparator<StyleRange> styleRangeComparator = new StyleRangeComparator ();

    /**
     * Parses label style ranges into text ranges.
     * All parsed text ranges are stored within provided list.
     *
     * @param label      label to parse text ranges for
     * @param textRanges list to store text ranges into
     */
    public static void buildTextRanges ( final WebStyledLabel label, final List<TextRange> textRanges )
    {
        textRanges.clear ();

        // Sorting style ranges by their positions
        final List<StyleRange> styleRanges = label.getStyleRanges ();
        Collections.sort ( styleRanges, styleRangeComparator );

        // Checking whether text is empty or not
        final String s = label.getText ();
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

    private static void addStyledTexts ( String text, StyleRange range, final List<TextRange> textRanges )
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
     * Returns label custom font if exists or default label font otherwise.
     *
     * @param label label to retrieve font for
     * @return label custom font if exists or default label font otherwise
     */
    public static Font getFont ( final WebStyledLabel label )
    {
        Font font = label.getFont ();
        if ( font == null )
        {
            font = UIManager.getFont ( "Label.font" );
        }
        return font;
    }

    public static int findNextWordStartIndex ( final String string, final int firstRowEndIndex )
    {
        boolean skipFirstWord = firstRowEndIndex < 0;
        for ( int i = firstRowEndIndex + 1; i < string.length (); i++ )
        {
            final char c = string.charAt ( i );
            if ( c != ' ' && c != '\t' && c != '\r' && c != '\n' )
            {
                if ( !skipFirstWord )
                {
                    return i;
                }
            }
            else
            {
                skipFirstWord = false;
            }
        }
        return string.length ();
    }

    public static int findFirstRowWordEndIndex ( final String string )
    {
        boolean spaceFound = false;
        for ( int i = string.length () - 1; i >= 0; i-- )
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
        return -1;
    }

    /**
     * Computes compound label icon and text positions relative to viewR rectangle.
     * Component orientation is also taken into account and properly applied.
     *
     * @param label                  painted label
     * @param text                   painted text
     * @param icon                   painted icon
     * @param verticalAlignment      vertical alignment
     * @param horizontalAlignment    horizontal alignment
     * @param verticalTextPosition   vertical text position
     * @param horizontalTextPosition horizontal text position
     * @param viewR                  view rectangle
     * @param iconR                  icon rectangle
     * @param textR                  text rectangle
     * @param textIconGap            gap between text and icon
     * @return layout string
     */
    public static String layoutCompoundLabel ( final WebStyledLabel label, final String text, final Icon icon, final int verticalAlignment,
                                               final int horizontalAlignment, final int verticalTextPosition,
                                               final int horizontalTextPosition, final Rectangle viewR, final Rectangle iconR,
                                               final Rectangle textR, final int textIconGap )
    {
        boolean orientationIsLeftToRight = true;
        int hAlign = horizontalAlignment;
        int hTextPos = horizontalTextPosition;

        if ( label != null )
        {
            if ( !label.getComponentOrientation ().isLeftToRight () )
            {
                orientationIsLeftToRight = false;
            }
        }

        // Translate LEADING/TRAILING values in horizontalAlignment to LEFT/RIGHT values depending on the components orientation
        switch ( horizontalAlignment )
        {
            case LEADING:
                hAlign = ( orientationIsLeftToRight ) ? LEFT : RIGHT;
                break;
            case TRAILING:
                hAlign = ( orientationIsLeftToRight ) ? RIGHT : LEFT;
                break;
        }

        // Translate LEADING/TRAILING values in horizontalTextPosition to LEFT/RIGHT values depending on the components orientation
        switch ( horizontalTextPosition )
        {
            case LEADING:
                hTextPos = ( orientationIsLeftToRight ) ? LEFT : RIGHT;
                break;
            case TRAILING:
                hTextPos = ( orientationIsLeftToRight ) ? RIGHT : LEFT;
                break;
        }

        return layoutCompoundLabelImpl ( label, text, icon, verticalAlignment, hAlign, verticalTextPosition, hTextPos, viewR, iconR, textR,
                textIconGap );
    }

    /**
     * Computes compound label icon and text positions relative to viewR rectangle.
     *
     * @param text                   painted text
     * @param icon                   painted icon
     * @param verticalAlignment      vertical alignment
     * @param horizontalAlignment    horizontal alignment
     * @param verticalTextPosition   vertical text position
     * @param horizontalTextPosition horizontal text position
     * @param viewR                  view rectangle
     * @param iconR                  icon rectangle
     * @param textR                  text rectangle
     * @param textIconGap            gap between text and icon
     * @return layout string
     */
    public static String layoutCompoundLabel ( final String text, final Icon icon, final int verticalAlignment,
                                               final int horizontalAlignment, final int verticalTextPosition,
                                               final int horizontalTextPosition, final Rectangle viewR, final Rectangle iconR,
                                               final Rectangle textR, final int textIconGap )
    {
        return layoutCompoundLabelImpl ( null, text, icon, verticalAlignment, horizontalAlignment, verticalTextPosition,
                horizontalTextPosition, viewR, iconR, textR, textIconGap );
    }

    /**
     * Compute and return the location of the icons origin, the location of origin of the text baseline, and a possibly
     * clipped version of the compound labels string.  Locations are computed relative to the viewR rectangle. This
     * layoutCompoundLabel() does not know how to handle LEADING/TRAILING values in horizontalTextPosition (they will
     * default to RIGHT) and in horizontalAlignment (they will default to CENTER). Use the other version of
     * layoutCompoundLabel() instead.
     *
     * @param label                  painted label
     * @param text                   painted text
     * @param icon                   painted icon
     * @param verticalAlignment      vertical alignment
     * @param horizontalAlignment    horizontal alignment
     * @param verticalTextPosition   vertical text position
     * @param horizontalTextPosition horizontal text position
     * @param viewR                  view rectangle
     * @param iconR                  icon rectangle
     * @param textR                  text rectangle
     * @param textIconGap            gap between text and icon
     * @return the layout string
     */
    private static String layoutCompoundLabelImpl ( final WebStyledLabel label, String text, final Icon icon, final int verticalAlignment,
                                                    final int horizontalAlignment, final int verticalTextPosition,
                                                    final int horizontalTextPosition, final Rectangle viewR, final Rectangle iconR,
                                                    final Rectangle textR, final int textIconGap )
    {
        // Initialize the icon bounds rectangle iconR.
        if ( icon != null )
        {
            iconR.width = icon.getIconWidth ();
            iconR.height = icon.getIconHeight ();
        }
        else
        {
            iconR.width = iconR.height = 0;
        }

        // Initialize the text bounds rectangle textR
        // If a null or and empty String was specified we substitute "" here and use 0,0,0,0 for textR
        final boolean textIsEmpty = ( text == null ) || text.equals ( "" );
        final int lsb = 0;

        // Unless both text and icon are non-null, the value of textIconGap is ignored
        final int gap;

        final View v;
        if ( textIsEmpty )
        {
            textR.width = textR.height = 0;
            text = "";
            gap = 0;
        }
        else
        {
            final int availTextWidth;
            gap = ( icon == null ) ? 0 : textIconGap;

            if ( horizontalTextPosition == CENTER )
            {
                availTextWidth = viewR.width;
            }
            else
            {
                availTextWidth = viewR.width - ( iconR.width + gap );
            }
            v = ( label != null ) ? ( View ) label.getClientProperty ( "html" ) : null;
            if ( v != null )
            {
                textR.width = Math.min ( availTextWidth, ( int ) v.getPreferredSpan ( View.X_AXIS ) );
                textR.height = ( int ) v.getPreferredSpan ( View.Y_AXIS );
            }
        }

        // Compute textR.x,y given the verticalTextPosition and horizontalTextPosition properties
        if ( verticalTextPosition == TOP )
        {
            if ( horizontalTextPosition != CENTER )
            {
                textR.y = 0;
            }
            else
            {
                textR.y = -( textR.height + gap );
            }
        }
        else if ( verticalTextPosition == CENTER )
        {
            textR.y = ( iconR.height / 2 ) - ( textR.height / 2 );
        }
        else
        {
            if ( horizontalTextPosition != CENTER )
            {
                textR.y = iconR.height - textR.height;
            }
            else
            {
                textR.y = iconR.height + gap;
            }
        }

        if ( horizontalTextPosition == LEFT )
        {
            textR.x = -( textR.width + gap );
        }
        else if ( horizontalTextPosition == CENTER )
        {
            textR.x = ( iconR.width / 2 ) - ( textR.width / 2 );
        }
        else
        {
            textR.x = iconR.width + gap;
        }

        // labelR is the rectangle that contains iconR and textR
        // Move it to its proper position given the labelAlignment properties
        // To avoid actually allocating a Rectangle, Rectangle.union has been inlined below
        final int labelR_x = Math.min ( iconR.x, textR.x );
        final int labelR_width = Math.max ( iconR.x + iconR.width, textR.x + textR.width ) - labelR_x;
        final int labelR_y = Math.min ( iconR.y, textR.y );
        final int labelR_height = Math.max ( iconR.y + iconR.height, textR.y + textR.height ) - labelR_y;

        final int dx;
        final int dy;

        if ( verticalAlignment == TOP )
        {
            dy = viewR.y - labelR_y;
        }
        else if ( verticalAlignment == CENTER )
        {
            dy = ( viewR.y + ( viewR.height / 2 ) ) - ( labelR_y + ( labelR_height / 2 ) );
        }
        else
        {
            dy = ( viewR.y + viewR.height ) - ( labelR_y + labelR_height );
        }

        if ( horizontalAlignment == LEFT )
        {
            dx = viewR.x - labelR_x;
        }
        else if ( horizontalAlignment == RIGHT )
        {
            dx = ( viewR.x + viewR.width ) - ( labelR_x + labelR_width );
        }
        else
        {
            dx = ( viewR.x + ( viewR.width / 2 ) ) - ( labelR_x + ( labelR_width / 2 ) );
        }

        // Translate textR and iconR by dx,dy.
        textR.x += dx;
        textR.y += dy;
        iconR.x += dx;
        iconR.y += dy;

        if ( lsb < 0 )
        {
            // lsb is negative. Shift the x location so that the text is
            // visually drawn at the right location.
            textR.x -= lsb;
        }

        int maxIconY = viewR.height / 2;
        final Insets insets = label.getInsets ();
        final int leftMostX = viewR.x;
        int rightMostX = viewR.x + viewR.width;
        rightMostX -= iconR.width;
        if ( horizontalTextPosition == SwingConstants.CENTER )
        {
            if ( viewR.width < textR.width )
            {
                iconR.x = ( leftMostX + rightMostX ) / 2;
            }
            else
            {
                iconR.x = textR.x + ( textR.width - iconR.width ) / 2;
            }
        }
        else if ( iconR.x < leftMostX )
        {
            textR.x += leftMostX - iconR.x;
            iconR.x = leftMostX;
        }
        else if ( iconR.x > rightMostX && horizontalAlignment != LEFT )
        {
            textR.x -= iconR.x - rightMostX;
            iconR.x = rightMostX;
        }
        if ( insets != null )
        {
            maxIconY -= ( insets.bottom + insets.top ) / 2;
        }
        if ( icon != null )
        {
            maxIconY -= icon.getIconHeight () / 2;
        }
        if ( verticalAlignment == TOP )
        {
            iconR.y = Math.min ( maxIconY, iconR.y );
        }
        else if ( verticalAlignment == BOTTOM )
        {
            iconR.y = Math.max ( maxIconY, iconR.y );
        }

        return text;
    }

    public static String getPlainText ( final String text, final List<StyleRange> styles )
    {
        if ( TextUtils.isEmpty ( text ) )
        {
            return text;
        }

        String plainText = "";
        String trimmedText = text;
        int begin = nextUnescaped ( trimmedText, "{", 0 );
        if ( begin != -1 )
        {
            while ( begin != -1 )
            {
                final int end = nextUnescaped ( trimmedText, "}", begin + 1 );
                if ( end != -1 )
                {
                    // Parsing statement
                    final String statement = trimmedText.substring ( begin + 1, end );
                    final TextRange range = parseStatement ( statement );

                    // Including parsed statement or simple text
                    if ( range != null )
                    {
                        // We found some styles in the statement
                        // Adding text and style range and proceeding
                        plainText += trimmedText.substring ( 0, begin );
                        range.getStyleRange ().setStartIndex ( plainText.length () );
                        styles.add ( range.getStyleRange () );
                        plainText += range.getText ();
                    }
                    else
                    {
                        // We didn't find any style statements
                        if ( statement.equals ( "br" ) )
                        {
                            // Adding linebreak and proceeding
                            plainText += trimmedText.substring ( 0, begin ) + "\n";
                        }
                        else
                        {
                            // Adding plain text and proceeding
                            plainText += trimmedText.substring ( 0, begin ) + statement;
                        }
                    }

                    // Continue to next
                    trimmedText = trimmedText.substring ( end + 1 );
                    begin = nextUnescaped ( trimmedText, "{", 0 );
                }
                else
                {
                    break;
                }
            }
            plainText += trimmedText;
        }
        else
        {
            plainText = text;
        }
        return plainText;
    }

    private static int nextUnescaped ( final String trimmedText, final String pattern, final int from )
    {
        // todo
        return trimmedText.indexOf ( pattern, from );
    }

    private static TextRange parseStatement ( final String statement )
    {
        final int sep = statement.indexOf ( ":" );
        if ( sep != -1 )
        {
            try
            {
                final String text = statement.substring ( 0, sep );
                boolean p = false;
                boolean b = false;
                boolean i = false;
                Color fg = null;
                Color bg = null;
                final List<CustomStyle> customStyles = new ArrayList<CustomStyle> ();

                final String vars = statement.substring ( sep + 1 );
                final StringTokenizer st = new StringTokenizer ( vars, ";", false );
                int styles = 0;
                while ( st.hasMoreTokens () )
                {
                    final String token = st.nextToken ();
                    if ( token.equals ( "p" ) || token.equals ( "plain" ) )
                    {
                        p = true;
                        styles++;
                    }
                    else if ( token.equals ( "b" ) || token.equals ( "bold" ) )
                    {
                        b = true;
                        styles++;
                    }
                    else if ( token.equals ( "i" ) || token.equals ( "italic" ) )
                    {
                        i = true;
                        styles++;
                    }
                    else if ( token.equals ( "u" ) || token.equals ( "underlined" ) )
                    {
                        customStyles.add ( CustomStyle.underlined );
                        styles++;
                    }
                    else if ( token.equals ( "sp" ) || token.equals ( "sup" ) || token.equals ( "superscript" ) )
                    {
                        customStyles.add ( CustomStyle.superscript );
                        styles++;
                    }
                    else if ( token.equals ( "sb" ) || token.equals ( "sub" ) || token.equals ( "subscript" ) )
                    {
                        customStyles.add ( CustomStyle.subscript );
                        styles++;
                    }
                    else if ( token.equals ( "s" ) || token.equals ( "strike" ) )
                    {
                        customStyles.add ( CustomStyle.strikeThrough );
                        styles++;
                    }
                    else if ( token.equals ( "ds" ) || token.equals ( "doublestrike" ) )
                    {
                        customStyles.add ( CustomStyle.doubleStrikeThrough );
                        styles++;
                    }
                    else if ( token.startsWith ( "c" ) || token.startsWith ( "color" ) ||
                            token.startsWith ( "f" ) || token.startsWith ( "foreground" ) )
                    {
                        final Color color = parseColor ( token );
                        if ( color != null )
                        {
                            fg = color;
                            styles++;
                        }
                    }
                    else if ( token.startsWith ( "b" ) || token.startsWith ( "background" ) )
                    {
                        final Color color = parseColor ( token );
                        if ( color != null )
                        {
                            bg = color;
                            styles++;
                        }
                    }
                    // Other variables are simply ignored so far
                    // New possible variables might be added in future
                }

                // Create style range only if some style was actually found
                if ( styles > 0 )
                {
                    // Combining TextRange and StyleRange from retrieved data
                    final int style = b && i ? Font.BOLD | Font.ITALIC : b ? Font.BOLD : i ? Font.ITALIC : p ? Font.PLAIN : -1;
                    final CustomStyle[] cs = customStyles.toArray ( new CustomStyle[ customStyles.size () ] );
                    return new TextRange ( text, new StyleRange ( 0, text.length (), style, fg, bg, cs ) );
                }
                else
                {
                    return null;
                }
            }
            catch ( final Throwable e )
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    private static Color parseColor ( final String statement )
    {
        final int i1 = statement.indexOf ( "(" );
        final int i2 = statement.lastIndexOf ( ")" );
        if ( i1 != -1 && i2 != -1 )
        {
            try
            {
                final String colorString = statement.substring ( i1 + 1, i2 );
                return ColorConverter.colorFromString ( colorString );
            }
            catch ( final Throwable e )
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }
}