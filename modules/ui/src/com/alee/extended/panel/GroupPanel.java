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

package com.alee.extended.panel;

import com.alee.api.data.Orientation;
import com.alee.extended.layout.GroupLayout;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.style.StyleId;

import javax.swing.*;
import java.awt.*;

/**
 * This panel allows you to quickly place a small group of components into a row or a column.
 * You can specify grouping type, orientation and gap between components.
 *
 * If specified grouping type is not "none" then components will be placed according to that grouping type.
 * Otherwise whether component should fill all the space left or not is determined by its FILL_CELL client property.
 *
 * Orientation determines whether components should be placed horizontally or vertically.
 * Gap determines the spacing between the components in pixels.
 *
 * @author Mikle Garin
 */
public class GroupPanel extends WebPanel
{
    /**
     * todo 1. Create separate default style for this panel
     */

    /**
     * Key for boolean value that is used to determine when component should fill all the space left.
     */
    public static final String FILL_CELL = "fill.component.cell";

    /**
     * Constructor that places components horizontally in a single row with a zero gap.
     *
     * @param components components to place
     */
    public GroupPanel ( final Component... components )
    {
        this ( StyleId.panelTransparent, components );
    }

    /**
     * Constructor that places components horizontally in a single row with a zero gap.
     *
     * @param id         style ID
     * @param components components to place
     */
    public GroupPanel ( final StyleId id, final Component... components )
    {
        this ( id, true, components );
    }

    /**
     * Constructor that places components horizontally or vertically in a single row with a zero gap.
     *
     * @param horizontal layout orientation
     * @param components components to place
     */
    public GroupPanel ( final boolean horizontal, final Component... components )
    {
        this ( StyleId.panelTransparent, horizontal, components );
    }

    /**
     * Constructor that places components horizontally or vertically in a single row with a zero gap.
     *
     * @param id         style ID
     * @param horizontal layout orientation
     * @param components components to place
     */
    public GroupPanel ( final StyleId id, final boolean horizontal, final Component... components )
    {
        this ( id, 0, horizontal, components );
    }

    /**
     * Constructor that places components horizontally in a single row with a specified gap.
     *
     * @param gap        gap between components
     * @param components components to place
     */
    public GroupPanel ( final int gap, final Component... components )
    {
        this ( StyleId.panelTransparent, gap, components );
    }

    /**
     * Constructor that places components horizontally in a single row with a specified gap.
     *
     * @param id         style ID
     * @param gap        gap between components
     * @param components components to place
     */
    public GroupPanel ( final StyleId id, final int gap, final Component... components )
    {
        this ( id, gap, true, components );
    }

    /**
     * Constructor that places components horizontally or vertically in a single row with a specified gap.
     *
     * @param gap        gap between components
     * @param horizontal layout orientation
     * @param components components to place
     */
    public GroupPanel ( final int gap, final boolean horizontal, final Component... components )
    {
        this ( StyleId.panelTransparent, gap, horizontal, components );
    }

    /**
     * Constructor that places components horizontally or vertically in a single row with a specified gap.
     *
     * @param id         style ID
     * @param gap        gap between components
     * @param horizontal layout orientation
     * @param components components to place
     */
    public GroupPanel ( final StyleId id, final int gap, final boolean horizontal, final Component... components )
    {
        this ( id, GroupingType.none, gap, horizontal, components );
    }

    /**
     * Constructor that places components horizontally in a single row with a zero gap using special grouping type.
     *
     * @param groupingType special grouping type
     * @param components   components to place
     */
    public GroupPanel ( final GroupingType groupingType, final Component... components )
    {
        this ( StyleId.panelTransparent, groupingType, components );
    }

    /**
     * Constructor that places components horizontally in a single row with a zero gap using special grouping type.
     *
     * @param id           style ID
     * @param groupingType special grouping type
     * @param components   components to place
     */
    public GroupPanel ( final StyleId id, final GroupingType groupingType, final Component... components )
    {
        this ( id, groupingType, true, components );
    }

    /**
     * Constructor that places components horizontally or vertically in a single row with a zero gap using special grouping type.
     *
     * @param groupingType special grouping type
     * @param horizontal   layout orientation
     * @param components   components to place
     */
    public GroupPanel ( final GroupingType groupingType, final boolean horizontal, final Component... components )
    {
        this ( StyleId.panelTransparent, groupingType, horizontal, components );
    }

    /**
     * Constructor that places components horizontally or vertically in a single row with a zero gap using special grouping type.
     *
     * @param id           style ID
     * @param groupingType special grouping type
     * @param horizontal   layout orientation
     * @param components   components to place
     */
    public GroupPanel ( final StyleId id, final GroupingType groupingType, final boolean horizontal, final Component... components )
    {
        this ( id, groupingType, 0, horizontal, components );
    }

    /**
     * Constructor that places components horizontally in a single row with a specified gap using special grouping type.
     *
     * @param groupingType special grouping type
     * @param gap          gap between components
     * @param components   components to place
     */
    public GroupPanel ( final GroupingType groupingType, final int gap, final Component... components )
    {
        this ( StyleId.panelTransparent, groupingType, gap, components );
    }

    /**
     * Constructor that places components horizontally in a single row with a specified gap using special grouping type.
     *
     * @param id           style ID
     * @param groupingType special grouping type
     * @param gap          gap between components
     * @param components   components to place
     */
    public GroupPanel ( final StyleId id, final GroupingType groupingType, final int gap, final Component... components )
    {
        this ( id, groupingType, gap, true, components );
    }

    /**
     * Constructor that places components horizontally or vertically in a single row with a specified gap using special grouping type.
     *
     * @param groupingType special grouping type
     * @param gap          gap between components
     * @param horizontal   layout orientation
     * @param components   components to place
     */
    public GroupPanel ( final GroupingType groupingType, final int gap, final boolean horizontal, final Component... components )
    {
        this ( StyleId.panelTransparent, groupingType, gap, horizontal, components );
    }

    /**
     * Constructor that places components horizontally or vertically in a single row with a specified gap using special grouping type.
     *
     * @param id           style ID
     * @param groupingType special grouping type
     * @param gap          gap between components
     * @param horizontal   layout orientation
     * @param components   components to place
     */
    public GroupPanel ( final StyleId id, final GroupingType groupingType, final int gap, final boolean horizontal,
                        final Component... components )
    {
        super ( id, new GroupLayout ( horizontal ? Orientation.horizontal : Orientation.vertical, gap ) );

        // Placing components
        for ( int i = 0; i < components.length; i++ )
        {
            final Component component = components[ i ];
            switch ( groupingType )
            {
                case none:
                {
                    // Client property used to determine whether component should fill all the space left or not
                    add ( component, isFill ( component ) ? GroupLayout.FILL : GroupLayout.PREFERRED );
                    break;
                }
                case fillFirst:
                {
                    // First component fills all the space left
                    add ( component, i == 0 ? GroupLayout.FILL : GroupLayout.PREFERRED );
                    break;
                }
                case fillMiddle:
                {
                    // Components int the middle fill all the space left
                    add ( component, i != 0 && i != components.length - 1 ? GroupLayout.FILL : GroupLayout.PREFERRED );
                    break;
                }
                case fillLast:
                {
                    // Last component fills all the space left
                    add ( component, i == components.length - 1 ? GroupLayout.FILL : GroupLayout.PREFERRED );
                    break;
                }
                case fillFirstAndLast:
                {
                    // First and last components fill all the space left
                    add ( component, i == 0 || i == components.length - 1 ? GroupLayout.FILL : GroupLayout.PREFERRED );
                    break;
                }
                case fillAll:
                {
                    // All components get the same amount of space
                    add ( component, GroupLayout.FILL );
                    break;
                }
            }
        }
    }

    /**
     * Returns actual layout for this GroupPanel.
     *
     * @return GroupLayout
     */
    public GroupLayout getActualLayout ()
    {
        return ( GroupLayout ) getLayout ();
    }

    /**
     * Returns layout orientation.
     *
     * @return layout orientation
     */
    public Orientation getOrientation ()
    {
        return getActualLayout ().getOrientation ();
    }

    /**
     * Sets the layout orientation.
     *
     * @param orientation layout orientation
     */
    public void setOrientation ( final Orientation orientation )
    {
        getActualLayout ().setOrientation ( orientation );
        revalidate ();
        repaint ();
    }

    /**
     * Returns gap in pixels between the components.
     *
     * @return gap between components
     */
    public int getGap ()
    {
        return getActualLayout ().getGap ();
    }

    /**
     * Sets the gap between components in pixels.
     *
     * @param gap gap between components
     */
    public void setGap ( final int gap )
    {
        getActualLayout ().setGap ( gap );
        revalidate ();
        repaint ();
    }

    /**
     * Returns whether this component should fill all the space left
     *
     * @param component component to analyze
     * @return true if the component should fill all the space left, false otherwise
     */
    public static boolean isFill ( final Component component )
    {
        final boolean fill;
        if ( component instanceof JComponent )
        {
            final JComponent jComponent = ( JComponent ) component;
            final Boolean b = ( Boolean ) jComponent.getClientProperty ( FILL_CELL );
            fill = b != null && b;
        }
        else
        {
            fill = false;
        }
        return fill;
    }
}