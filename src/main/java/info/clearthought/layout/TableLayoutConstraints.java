/*
 * ====================================================================
 *
 * The Clearthought Software License, Version 1.0
 *
 * Copyright (c) 2001 Daniel Barbalace.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. The original software may not be altered.  However, the classes
 *    provided may be subclasses as long as the subclasses are not
 *    packaged in the info.clearthought package or any subpackage of
 *    info.clearthought.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR, AFFILATED BUSINESSES,
 * OR ANYONE ELSE BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 */

package info.clearthought.layout;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * TableLayoutConstraints binds components to their constraints.
 *
 * @author Daniel E. Barbalace
 * @version 2.0 March 14, 2004
 */

public class TableLayoutConstraints implements TableLayoutConstants
{
    /**
     * Cell in which the upper left corner of the component lays
     */
    public int col1, row1;

    /**
     * Cell in which the lower right corner of the component lays
     */
    public int col2, row2;

    /**
     * Horizontal justification if component occupies just one cell
     */
    public int hAlign;

    /**
     * Verical justification if component occupies just one cell
     */
    public int vAlign;

    /**
     * Constructs an TableLayoutConstraints with the default settings.  This constructor is
     * equivalent to TableLayoutConstraints(0, 0, 0, 0, FULL, FULL).
     */

    public TableLayoutConstraints ()
    {
        col1 = row1 = col2 = 0;
        hAlign = vAlign = FULL;
    }

    /**
     * Constructs an TableLayoutConstraints a set of constraints.
     *
     * @param col column where the component is placed
     * @param row row where the component is placed
     */

    public TableLayoutConstraints ( int col, int row )
    {
        this ( col, row, col, row, FULL, FULL );
    }

    /**
     * Constructs an TableLayoutConstraints a set of constraints.
     *
     * @param col1 column where upper-left cornor of the component is placed
     * @param row1 row where upper-left cornor of the component is placed
     * @param col2 column where lower-right cornor of the component is placed
     * @param row2 row where lower-right cornor of the component is placed
     */

    public TableLayoutConstraints ( int col1, int row1, int col2, int row2 )
    {
        this ( col1, row1, col2, row2, FULL, FULL );
    }

    /**
     * Constructs an TableLayoutConstraints a set of constraints.
     *
     * @param col1   column where upper-left cornor of the component is placed
     * @param row1   row where upper-left cornor of the component is placed
     * @param col2   column where lower-right cornor of the component is placed
     * @param row2   row where lower-right cornor of the component is placed
     * @param hAlign horizontal justification of a component in a single cell
     * @param vAlign vertical justification of a component in a single cell
     */

    public TableLayoutConstraints ( int col1, int row1, int col2, int row2, int hAlign, int vAlign )
    {
        this.col1 = col1;
        this.row1 = row1;
        this.col2 = col2;
        this.row2 = row2;

        if ( ( hAlign == LEFT ) || ( hAlign == RIGHT ) || ( hAlign == CENTER ) ||
                ( hAlign == FULL ) || ( hAlign == TRAILING ) )
        {
            this.hAlign = hAlign;
        }
        else
        {
            this.hAlign = FULL;
        }

        if ( ( vAlign == LEFT ) || ( vAlign == RIGHT ) || ( vAlign == CENTER ) )
        {
            this.vAlign = vAlign;
        }
        else
        {
            this.vAlign = FULL;
        }
    }

    /**
     * Constructs an TableLayoutConstraints from a string.
     *
     * @param constraints indicates TableLayoutConstraints's position and justification as a string
     *                    in the form "column, row" or "column, row, horizontal justification,
     *                    vertical justification" or "column 1, row 1, column 2, row 2" or "column
     *                    1, row 1, column 2, row 2, horizontal justification, vertical
     *                    justification". It is also acceptable to delimit the paramters with spaces
     *                    instead of commas.
     */

    public TableLayoutConstraints ( String constraints )
    {
        // Use default values for any parameter not specified or specified
        // incorrectly.  The default parameters place the component in a single
        // cell at column 0, row 0.  The component is fully justified.
        col1 = 0;
        row1 = 0;
        col2 = 0;
        row2 = 0;
        hAlign = FULL;
        vAlign = FULL;

        // Parse constraints using spaces or commas
        StringTokenizer st = new StringTokenizer ( constraints, ", " );
        int numToken = st.countTokens ();

        try
        {
            // Check constraints
            if ( ( numToken != 2 ) && ( numToken != 4 ) && ( numToken != 6 ) )
            {
                throw new RuntimeException ();
            }

            // Get the first column (assume component is in only one column)
            String tokenA = st.nextToken ();
            col1 = new Integer ( tokenA );
            col2 = col1;

            // Get the first row (assume component is in only one row)
            String tokenB = st.nextToken ();
            row1 = new Integer ( tokenB );
            row2 = row1;

            // Get next two tokens
            tokenA = st.nextToken ();
            tokenB = st.nextToken ();

            try
            {
                // Attempt to use tokens A and B as col2 and row2
                col2 = new Integer ( tokenA );
                row2 = new Integer ( tokenB );

                // Get next two tokens
                tokenA = st.nextToken ();
                tokenB = st.nextToken ();
            }
            catch ( NumberFormatException error )
            {
                col2 = col1;
                row2 = row1;
            }

            // Check if token means horizontally justification the component
            if ( ( tokenA.equalsIgnoreCase ( "L" ) ) || ( tokenA.equalsIgnoreCase ( "LEFT" ) ) )
            {
                hAlign = LEFT;
            }
            else if ( ( tokenA.equalsIgnoreCase ( "C" ) ) ||
                    ( tokenA.equalsIgnoreCase ( "CENTER" ) ) )
            {
                hAlign = CENTER;
            }
            else if ( ( tokenA.equalsIgnoreCase ( "F" ) ) ||
                    ( tokenA.equalsIgnoreCase ( "FULL" ) ) )
            {
                hAlign = FULL;
            }
            else if ( ( tokenA.equalsIgnoreCase ( "R" ) ) ||
                    ( tokenA.equalsIgnoreCase ( "RIGHT" ) ) )
            {
                hAlign = RIGHT;
            }
            else if ( ( tokenA.equalsIgnoreCase ( "LD" ) ) ||
                    ( tokenA.equalsIgnoreCase ( "LEADING" ) ) )
            {
                hAlign = LEADING;
            }
            else if ( ( tokenA.equalsIgnoreCase ( "TL" ) ) ||
                    ( tokenA.equalsIgnoreCase ( "TRAILING" ) ) )
            {
                hAlign = TRAILING;
            }
            else
            {
                throw new RuntimeException ();
            }

            // Check if token means horizontally justification the component
            if ( ( tokenB.equalsIgnoreCase ( "T" ) ) || ( tokenB.equalsIgnoreCase ( "TOP" ) ) )
            {
                vAlign = TOP;
            }
            else if ( ( tokenB.equalsIgnoreCase ( "C" ) ) ||
                    ( tokenB.equalsIgnoreCase ( "CENTER" ) ) )
            {
                vAlign = CENTER;
            }
            else if ( ( tokenB.equalsIgnoreCase ( "F" ) ) ||
                    ( tokenB.equalsIgnoreCase ( "FULL" ) ) )
            {
                vAlign = FULL;
            }
            else if ( ( tokenB.equalsIgnoreCase ( "B" ) ) ||
                    ( tokenB.equalsIgnoreCase ( "BOTTOM" ) ) )
            {
                vAlign = BOTTOM;
            }
            else
            {
                throw new RuntimeException ();
            }
        }
        catch ( NoSuchElementException ignored )
        {
        }
        catch ( RuntimeException error )
        {
            throw new IllegalArgumentException (
                    "Expected constraints in one of the following formats:\n" +
                            "  col1, row1\n  col1, row1, col2, row2\n" +
                            "  col1, row1, hAlign, vAlign\n" +
                            "  col1, row1, col2, row2, hAlign, vAlign\n" +
                            "Constraints provided '" + constraints + "'" );
        }

        // Make sure row2 >= row1
        if ( row2 < row1 )
        {
            row2 = row1;
        }

        // Make sure col2 >= col1
        if ( col2 < col1 )
        {
            col2 = col1;
        }
    }

    /**
     * Gets a string representation of this TableLayoutConstraints.
     *
     * @return a string in the form "row 1, column 1, row 2, column 2, horizontal justification,
     *         vertical justification"
     */

    public String toString ()
    {
        StringBuilder buffer = new StringBuilder ();

        buffer.append ( col1 );
        buffer.append ( ", " );
        buffer.append ( row1 );
        buffer.append ( ", " );

        buffer.append ( col2 );
        buffer.append ( ", " );
        buffer.append ( row2 );
        buffer.append ( ", " );

        final String h[] = { "left", "center", "full", "right", "leading", "trailing" };
        final String v[] = { "top", "center", "full", "bottom" };

        buffer.append ( h[ hAlign ] );
        buffer.append ( ", " );
        buffer.append ( v[ vAlign ] );

        return buffer.toString ();
    }
}
