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

package com.alee.managers.tooltip;

import javax.swing.*;
import java.awt.*;

/**
 * Contains various utility methods for {@link ComponentArea} implementations.
 *
 * @param <V> area value type
 * @param <C> component type
 * @author Mikle Garin
 */
public abstract class AbstractComponentArea<V, C extends JComponent> implements ComponentArea<V, C>
{
    /**
     * Adjusts provided bounds to match actual content area.
     *
     * @param component component
     * @param content   area content
     * @param bounds    content bounds to adjust
     */
    protected void adjustBounds ( final C component, final Component content, final Rectangle bounds )
    {
        if ( content instanceof JLabel )
        {
            /**
             * Adjusting simple label content bounds.
             */
            final JLabel label = ( JLabel ) content;
            final int align = label.getHorizontalAlignment ();
            adjustBounds ( component, content, bounds, align );
        }
        else if ( content instanceof JCheckBox || content instanceof JRadioButton )
        {
            /**
             * Adjusting checkbox and radiobutton content bounds.
             * It is not reasonable for common buttons as they commonly have outer decoration.
             * It is also not reasonable for menu elements due to the way custom tooltips are displayed.
             */
            final AbstractButton button = ( AbstractButton ) content;
            final int align = button.getHorizontalAlignment ();
            adjustBounds ( component, content, bounds, align );
        }
    }

    /**
     * Adjusts provided bounds to match actual content area and alignment.
     *
     * @param component component
     * @param content   area content
     * @param bounds    content bounds to adjust
     * @param align     content alignment
     */
    protected void adjustBounds ( final C component, final Component content, final Rectangle bounds, final int align )
    {
        final boolean ltr = content.getComponentOrientation ().isLeftToRight ();
        if ( align == SwingConstants.LEFT || ltr ? align == SwingConstants.LEADING : align == SwingConstants.TRAILING )
        {
            // Content is on the left
            final Dimension preferred = content.getPreferredSize ();
            bounds.width = preferred.width;
        }
        else if ( align == SwingConstants.RIGHT || ltr ? align == SwingConstants.TRAILING : align == SwingConstants.LEADING )
        {
            // Content is on the right
            final Dimension preferred = content.getPreferredSize ();
            bounds.x = bounds.x + bounds.width - preferred.width;
            bounds.width = preferred.width;
        }
    }
}