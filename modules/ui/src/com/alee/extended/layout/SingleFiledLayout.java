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

import java.awt.*;
import java.io.Serializable;

/**
 * SingleFiledLayout lays out components singled filed.  This layout manager is like FlowLayout
 * except that all components are placed in a single row or column.
 *
 * @author Daniel E. Barbalace
 * @version 1.1 April 4, 2002
 */

@SuppressWarnings ( { "ALL" } )
public class SingleFiledLayout implements LayoutManager, Serializable
{
    /**
     * Align components in a column
     */
    public static final int COLUMN = 0;

    /**
     * Align components in a row
     */
    public static final int ROW = 1;

    /**
     * Left justify components
     */
    public static final int LEFT = 0;

    /**
     * Top justify components
     */
    public static final int TOP = 0;

    /**
     * Center components
     */
    public static final int CENTER = 1;

    /**
     * Full justify components
     */
    public static final int FULL = 2;

    /**
     * Bottom justify components
     */
    public static final int BOTTOM = 3;

    /**
     * Right justify components
     */
    public static final int RIGHT = 4;

    /**
     * Default gap -- derived classes may override
     */
    public static int DEFAULT_GAP = 5;

    /**
     * ROW or COLUMN -- should the components be aligned in a row or column
     */
    protected int orientation;

    /**
     * LEFT, TOP, CENTER, FULL, BOTTOM, RIGHT -- how should components of different sizes be
     * aligned
     */
    protected int justification;

    /**
     * Space between components in pixels
     */
    protected int gap;

    /**
     * Constructs an instance of SingleFiledLayout that will align components in a column using the
     * default gap and LEFT justification.
     */
    public SingleFiledLayout ()
    {
        this ( COLUMN, LEFT, DEFAULT_GAP );
    }

    /**
     * Constructs an instance of SingleFiledLayout using the default gap and LEFT or TOP
     * justification.
     *
     * @param orientation ROW or COLUMN -- should the components be aligned in a row or column
     */
    public SingleFiledLayout ( int orientation )
    {
        this ( orientation, LEFT, DEFAULT_GAP );
    }

    /**
     * Constructs an instance of SingleFiledLayout.
     *
     * @param orientation   ROW or COLUMN -- should the components be aligned in a row or column
     * @param justification LEFT, TOP, CENTER, FULL, BOTTOM, RIGHT -- how should components of
     *                      different sizes be aligned
     * @param gap           space between components in pixels
     */
    public SingleFiledLayout ( int orientation, int justification, int gap )
    {
        // Validate parameters
        if ( orientation != ROW )
        {
            orientation = COLUMN;
        }

        if ( ( justification != CENTER ) && ( justification != FULL ) &&
                ( justification != RIGHT ) )
        {
            justification = LEFT;
        }

        if ( gap < 0 )
        {
            gap = 0;
        }

        // Copy parameters
        this.orientation = orientation;
        this.justification = justification;
        this.gap = gap;
    }

    /**
     * To lay out the specified container using this layout.  This method repositions the components
     * in the specified target container. <p> User code should not have to call this method
     * directly.</p>
     *
     * @param container container being served by this layout manager
     */
    public void layoutContainer ( Container container )
    {
        // Use preferred size to get maximum width or height
        Dimension size = container.getSize ();

        // Get the container's insets
        Insets inset = container.getInsets ();

        // Start at top left of container
        int x = inset.left;
        int y = inset.top;

        // Get the components inside the container
        Component component[] = container.getComponents ();

        // Components arranged in a column
        if ( orientation == COLUMN )
        // Add each component
        {
            for ( int counter = 0; counter < component.length; counter++ )
            {
                // Use preferred size of component
                Dimension d = component[ counter ].getPreferredSize ();

                // Align component
                switch ( justification )
                {
                    case LEFT:
                        x = inset.left;
                        break;

                    case CENTER:
                        x = ( ( size.width - d.width ) >> 1 ) + inset.left - inset.right;
                        break;

                    case FULL:
                        x = inset.left;
                        d.width = size.width - inset.left - inset.right;
                        break;

                    case RIGHT:
                        x = size.width - d.width - inset.right;
                        break;
                }

                // Set size and location
                component[ counter ].setBounds ( x, y, d.width, d.height );

                // Increment y
                y += d.height + gap;
            }
        }
        // Components arranged in a row
        else
        // Add each component
        {
            for ( int counter = 0; counter < component.length; counter++ )
            {
                // Use preferred size of component
                Dimension d = component[ counter ].getPreferredSize ();

                // Align component
                switch ( justification )
                {
                    case TOP:
                        y = inset.top;
                        break;

                    case CENTER:
                        y = ( ( size.height - d.height ) >> 1 ) + inset.top - inset.bottom;
                        break;

                    case FULL:
                        y = inset.top;
                        d.height = size.height - inset.top - inset.bottom;
                        break;

                    case BOTTOM:
                        y = size.height - d.height - inset.bottom;
                        break;
                }

                // Set size and location
                component[ counter ].setBounds ( x, y, d.width, d.height );

                // Increment x
                x += d.width + gap;
            }
        }
    }

    /**
     * Determines the preferred size of the container argument using this layout. The preferred size
     * is the smallest size that, if used for the container's size, will ensure that no component is
     * truncated when the component is it's preferred size.
     *
     * @param container container being served by this layout manager
     * @return a dimension indicating the container's preferred size
     */
    public Dimension preferredLayoutSize ( Container container )
    {
        int totalWidth = 0;  // Width of all components
        int totalHeight = 0; // Height of all components

        // Get the components inside the container
        Component component[] = container.getComponents ();

        // Components arranged in a column
        if ( orientation == COLUMN )
        {
            // Add each component
            for ( int counter = 0; counter < component.length; counter++ )
            {
                Dimension d = component[ counter ].getPreferredSize ();

                if ( totalWidth < d.width )
                {
                    totalWidth = d.width;
                }

                totalHeight += d.height + gap;
            }

            // Subtract extra gap
            totalHeight -= gap;
        }
        // Components arranged in a row
        else
        {
            // Add each component
            for ( int counter = 0; counter < component.length; counter++ )
            {
                Dimension d = component[ counter ].getPreferredSize ();

                totalWidth += d.width + gap;

                if ( totalHeight < d.height )
                {
                    totalHeight = d.height;
                }
            }

            // Subtract extra gap
            totalWidth -= gap;
        }

        // Add insets to preferred size
        Insets inset = container.getInsets ();
        totalWidth += inset.left + inset.right;
        totalHeight += inset.top + inset.bottom;

        return new Dimension ( totalWidth, totalHeight );
    }

    /**
     * Determines the minimum size of the container argument using this layout. The minimum size is
     * the smallest size that, if used for the container's size, will ensure that no component is
     * truncated. The minimum size is the preferred size.
     *
     * @param container container being served by this layout manager
     * @return a dimension indicating the container's minimum size
     */
    public Dimension minimumLayoutSize ( Container container )
    {
        int totalWidth = 0;  // Width of all components
        int totalHeight = 0; // Height of all components

        // Get the components inside the container
        Component component[] = container.getComponents ();

        // Components arranged in a column
        if ( orientation == COLUMN )
        {
            // Add each component
            for ( int counter = 0; counter < component.length; counter++ )
            {
                Dimension d = component[ counter ].getMinimumSize ();

                if ( totalWidth < d.width )
                {
                    totalWidth = d.width;
                }

                totalHeight += d.height + gap;
            }

            // Subtract extra gap
            totalHeight -= gap;
        }
        // Components arranged in a row
        else
        {
            // Add each component
            for ( int counter = 0; counter < component.length; counter++ )
            {
                Dimension d = component[ counter ].getMinimumSize ();

                totalWidth += d.width + gap;

                if ( totalHeight < d.height )
                {
                    totalHeight = d.height;
                }
            }

            // Subtract extra gap
            totalWidth = -gap;
        }

        // Add insets to preferred size
        Insets inset = container.getInsets ();
        totalWidth += inset.left + inset.right;
        totalHeight += inset.top + inset.bottom;

        return new Dimension ( totalWidth, totalHeight );
    }

    /**
     * Adds the specified component with the specified name to the layout.
     *
     * @param name      dummy parameter
     * @param component component to add
     */
    public void addLayoutComponent ( String name, Component component )
    {
        //
    }

    /**
     * Removes the specified component with the specified name from the layout.
     *
     * @param component component being removed
     */
    public void removeLayoutComponent ( Component component )
    {
        //
    }
}
