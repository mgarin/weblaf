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

package com.alee.extended.layout;

import com.alee.extended.panel.WebAccordion;
import com.alee.extended.panel.WebCollapsiblePane;
import com.alee.laf.grouping.AbstractGroupingLayout;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.utils.general.Pair;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom layout for {@link com.alee.extended.panel.WebAccordion} component.
 *
 * @author Mikle Garin
 */

public class AccordionLayout extends AbstractGroupingLayout
{
    @Override
    public Dimension preferredLayoutSize ( final Container parent )
    {
        return getLayoutSize ( parent, true );
    }

    @Override
    public Dimension minimumLayoutSize ( final Container parent )
    {
        return getLayoutSize ( parent, false );
    }

    /**
     * Returns either minimum or preferred container size.
     *
     * @param parent    container
     * @param preferred whether preferred size should be returned or not
     * @return either minimum or preferred container size
     */
    private Dimension getLayoutSize ( final Container parent, final boolean preferred )
    {
        final WebAccordion accordion = ( WebAccordion ) parent;
        final List<WebCollapsiblePane> panes = accordion.getActualPanesList ();
        final int gap = accordion.getGap ();
        final Dimension ps = new Dimension ();
        final boolean hor = accordion.getOrientation () == SwingConstants.HORIZONTAL;

        for ( final WebCollapsiblePane pane : panes )
        {
            final Dimension cps = preferred || !accordion.isFillSpace () ? pane.getPreferredSize () : pane.getBasePreferredSize ();
            if ( hor )
            {
                ps.width += cps.width;
                ps.height = Math.max ( ps.height, cps.height );
            }
            else
            {
                ps.width = Math.max ( ps.width, cps.width );
                ps.height += cps.height;
            }
        }
        if ( panes.size () > 0 )
        {
            if ( hor )
            {
                ps.width += gap * ( panes.size () - 1 );
            }
            else
            {
                ps.height += gap * ( panes.size () - 1 );
            }
        }

        final Insets insets = parent.getInsets ();
        ps.width += insets.left + insets.right;
        ps.height += insets.top + insets.bottom;
        return ps;
    }

    @Override
    public void layoutContainer ( final Container parent )
    {
        final WebAccordion accordion = ( WebAccordion ) parent;
        final List<WebCollapsiblePane> panes = accordion.getActualPanesList ();
        final int gap = accordion.getGap ();
        final Insets insets = parent.getInsets ();
        final Dimension size = parent.getSize ();
        final int w = size.width - insets.left - insets.right;
        final int h = size.height - insets.top - insets.bottom;
        final boolean hor = accordion.getOrientation () == SwingConstants.HORIZONTAL;

        int x = insets.left;
        int y = insets.top;
        if ( accordion.isFillSpace () )
        {
            // Computing the part available to fill in with panes content
            float totalStates = 0;
            int totalFillLength = ( hor ? size.width - insets.left - insets.right : size.height - insets.top - insets.bottom ) + gap;
            int visuallyExpanded = 0;
            int lastFillIndex = -1;
            final List<Integer> base = new ArrayList<Integer> ();
            for ( final WebCollapsiblePane pane : panes )
            {
                final Dimension bps = pane.getBasePreferredSize ();
                base.add ( hor ? bps.width : bps.height );

                final float expandState = pane.getTransitionProgress ();

                totalStates += expandState;
                totalFillLength -= ( hor ? bps.width : bps.height ) + gap;

                if ( expandState > 0f )
                {
                    lastFillIndex = panes.indexOf ( pane );
                    visuallyExpanded++;
                }
            }
            totalStates = visuallyExpanded == 1 ? 1f : totalStates;
            totalFillLength = Math.max ( totalFillLength, 0 );

            // Layouting panes
            float end = 0f;
            for ( int i = 0; i < panes.size (); i++ )
            {
                final float expandState = panes.get ( i ).getTransitionProgress ();
                int length = base.get ( i );
                if ( expandState > 0f )
                {
                    end += ( totalFillLength * expandState / totalStates ) % 1;
                    length += Math.round ( ( float ) Math.floor ( totalFillLength * expandState / totalStates ) ) +
                            ( i == lastFillIndex ? Math.round ( end ) : 0 );
                }
                panes.get ( i ).setBounds ( x, y, hor ? length : w, hor ? h : length );
                if ( hor )
                {
                    x += length + gap;
                }
                else
                {
                    y += length + gap;
                }
            }
        }
        else
        {
            // Simply layouting panes by preferred size
            for ( final WebCollapsiblePane pane : panes )
            {
                final Dimension cps = pane.getPreferredSize ();
                pane.setBounds ( x, y, hor ? cps.width : w, hor ? h : cps.height );
                if ( hor )
                {
                    x += cps.width + gap;
                }
                else
                {
                    y += cps.height + gap;
                }
            }
        }
    }

    @Override
    protected String sides ()
    {
        return sides != null ? sides : ( sides = "0,0,0,0" );
    }

    @Override
    protected Pair<String, String> getDescriptors ( final Container parent, final Component component, final int index )
    {
        final Pair<String, String> descriptors;
        final WebAccordion accordion = ( WebAccordion ) parent;
        if ( accordion.getGap () == 0 )
        {
            final int last = parent.getComponentCount () - 1;
            final boolean hor = accordion.getOrientation () == SwingConstants.HORIZONTAL;
            final boolean top = ( hor || !hor && index == 0 ) && isPaintTop ();
            final boolean left = ( index == 0 || !hor ) && isPaintLeft ();
            final boolean bottom = ( hor || !hor && index == last ) && isPaintBottom ();
            final boolean right = ( index == last || !hor ) && isPaintLeft ();
            final String sides = DecorationUtils.toString ( top, left, bottom, right );
            final String lines = DecorationUtils.toString ( false, false, !hor && index != last, hor && index != last );
            descriptors = new Pair<String, String> ( sides, lines );
        }
        else
        {
            descriptors = new Pair<String, String> ();
        }
        return descriptors;
    }
}