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

import com.alee.global.StyleConstants;
import com.alee.utils.ImageUtils;

import javax.swing.*;

/**
 * WebAccordion style class.
 *
 * @author Mikle Garin
 */

public final class WebAccordionStyle
{
    /**
     * Whether animate transition between states or not.
     */
    public static boolean animate = StyleConstants.animate;

    /**
     * Accordion style.
     */
    public static AccordionStyle accordionStyle = AccordionStyle.united;

    /**
     * Accordion orientation.
     */
    public static int orientation = SwingConstants.VERTICAL;

    /**
     * Collapsed state icon.
     */
    public static ImageIcon expandIcon = new ImageIcon ( WebAccordionStyle.class.getResource ( "icons/arrow.png" ) );

    /**
     * Expanded state icon.
     */
    public static ImageIcon collapseIcon = ImageUtils.rotateImage90CW ( expandIcon );

    /**
     * Whether accordion must fill all available space with expanded panes or not
     */
    public static boolean fillSpace = true;

    /**
     * Whether multiply expanded panes are allowed or not
     */
    public static boolean multiplySelectionAllowed = true;

    /**
     * Gap between panes.
     */
    public static int gap = 0;
}