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

package com.alee.extended.breadcrumb.element;

import com.alee.extended.breadcrumb.WebBreadcrumb;
import com.alee.painter.decoration.DecorationState;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Various utilities for {@link WebBreadcrumb} implementation and its elements.
 *
 * @author Mikle Garin
 */
public final class BreadcrumbElementUtils
{
    /**
     * Adds {@link WebBreadcrumb} element states for the specified {@link JComponent}.
     *
     * @param component {@link JComponent} to add states for
     * @param states    {@link List} of states to add elements states into
     */
    public static void addBreadcrumbElementStates ( final JComponent component, final List<String> states )
    {
        final Container parent = component.getParent ();
        if ( parent instanceof WebBreadcrumb )
        {
            final WebBreadcrumb breadcrumb = ( WebBreadcrumb ) parent;

            // Breadcrumb element position states
            final boolean first = breadcrumb.isFirst ( component );
            final boolean last = breadcrumb.isLast ( component );
            if ( first && !last )
            {
                states.add ( DecorationState.first );
            }
            else if ( !first && !last )
            {
                states.add ( DecorationState.middle );
            }
            else if ( last && !first )
            {
                states.add ( DecorationState.last );
            }
            else if ( first && last )
            {
                states.add ( DecorationState.single );
            }

            // Breadcrumb progress states
            final BreadcrumbElementData.ProgressType progressType = breadcrumb.getProgressType ( component );
            if ( progressType == BreadcrumbElementData.ProgressType.progress )
            {
                states.add ( DecorationState.progress );
                final double value = breadcrumb.getProgress ( component );
                final boolean min = value == 0.0;
                final boolean max = value == 1.0;
                if ( min )
                {
                    states.add ( DecorationState.minimum );
                }
                if ( max )
                {
                    states.add ( DecorationState.maximum );
                }
                if ( !min && !max )
                {
                    states.add ( DecorationState.intermediate );
                }
            }
            else if ( progressType == BreadcrumbElementData.ProgressType.indeterminate )
            {
                states.add ( DecorationState.indeterminate );
            }
        }
    }
}