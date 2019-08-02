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

package com.alee.extended.layout;

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
    public TableLayoutConstraints ( final int col, final int row )
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
    public TableLayoutConstraints ( final int col1, final int row1, final int col2, final int row2 )
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
    public TableLayoutConstraints ( final int col1, final int row1, final int col2, final int row2, final int hAlign, final int vAlign )
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
    public TableLayoutConstraints ( final String constraints )
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
        final StringTokenizer st = new StringTokenizer ( constraints, ", " );
        final int numToken = st.countTokens ();

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
            catch ( final NumberFormatException error )
            {
                col2 = col1;
                row2 = row1;
            }

            // Check if token means horizontally justification the component
            if ( tokenA.equalsIgnoreCase ( "L" ) || tokenA.equalsIgnoreCase ( "LEFT" ) )
            {
                hAlign = LEFT;
            }
            else if ( tokenA.equalsIgnoreCase ( "C" ) || tokenA.equalsIgnoreCase ( "CENTER" ) )
            {
                hAlign = CENTER;
            }
            else if ( tokenA.equalsIgnoreCase ( "F" ) || tokenA.equalsIgnoreCase ( "FULL" ) )
            {
                hAlign = FULL;
            }
            else if ( tokenA.equalsIgnoreCase ( "R" ) || tokenA.equalsIgnoreCase ( "RIGHT" ) )
            {
                hAlign = RIGHT;
            }
            else if ( tokenA.equalsIgnoreCase ( "LD" ) || tokenA.equalsIgnoreCase ( "LEADING" ) )
            {
                hAlign = LEADING;
            }
            else if ( tokenA.equalsIgnoreCase ( "TL" ) || tokenA.equalsIgnoreCase ( "TRAILING" ) )
            {
                hAlign = TRAILING;
            }
            else
            {
                throw new RuntimeException ();
            }

            // Check if token means horizontally justification the component
            if ( tokenB.equalsIgnoreCase ( "T" ) || tokenB.equalsIgnoreCase ( "TOP" ) )
            {
                vAlign = TOP;
            }
            else if ( tokenB.equalsIgnoreCase ( "C" ) || tokenB.equalsIgnoreCase ( "CENTER" ) )
            {
                vAlign = CENTER;
            }
            else if ( tokenB.equalsIgnoreCase ( "F" ) || tokenB.equalsIgnoreCase ( "FULL" ) )
            {
                vAlign = FULL;
            }
            else if ( tokenB.equalsIgnoreCase ( "B" ) || tokenB.equalsIgnoreCase ( "BOTTOM" ) )
            {
                vAlign = BOTTOM;
            }
            else
            {
                throw new RuntimeException ();
            }
        }
        catch ( final NoSuchElementException ignored )
        {
        }
        catch ( final RuntimeException error )
        {
            throw new IllegalArgumentException ( "Expected constraints in one of the following formats:\n" +
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
     * vertical justification"
     */
    @Override
    public String toString ()
    {
        final String h[] = { "left", "center", "full", "right", "leading", "trailing" };
        final String v[] = { "top", "center", "full", "bottom" };
        return String.valueOf ( col1 ) + ", " + row1 + ", " + col2 + ", " + row2 + ", " + h[ hAlign ] + ", " + v[ vAlign ];
    }
}