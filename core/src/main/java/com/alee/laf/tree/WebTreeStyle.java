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

package com.alee.laf.tree;

import com.alee.laf.StyleConstants;

import java.awt.*;

/**
 * WebTree style class.
 *
 * @author Mikle Garin
 * @since 1.4
 */

public final class WebTreeStyle
{
    /**
     * Show rollover node highlight.
     */
    public static boolean highlightRolloverNode = true;

    /**
     * Draw tree structure lines.
     */
    public static boolean paintLines = true;

    /**
     * Tree structure lines color.
     */
    public static Color linesColor = Color.GRAY;

    /**
     * Cells selection style.
     */
    public static TreeSelectionStyle selectionStyle = TreeSelectionStyle.line;

    /**
     * Nodes selection rounding.
     */
    public static int selectionRound = StyleConstants.smallRound;

    /**
     * Nodes selection shade width.
     */
    public static int selectionShadeWidth = StyleConstants.shadeWidth;

    /**
     * Should allow selector.
     */
    public static boolean selectorEnabled = true;

    /**
     * Selector background color.
     */
    public static Color selectorColor = new Color ( 0, 0, 255, 50 );

    /**
     * Selector border color.
     */
    public static Color selectorBorderColor = Color.GRAY;

    /**
     * Selector rounding.
     */
    public static int selectorRound = StyleConstants.smallRound;

    /**
     * Selector border stroke.
     */
    public static BasicStroke selectorStroke =
            new BasicStroke ( 1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0f, new float[]{ 3f, 3f }, 0f );
}