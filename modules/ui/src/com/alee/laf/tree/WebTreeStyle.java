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

import com.alee.global.StyleConstants;

import java.awt.*;

/**
 * WebTree style class.
 *
 * @author Mikle Garin
 */

public final class WebTreeStyle
{
    /**
     * Whether autoexpand node on selection or not.
     */
    public static boolean autoExpandSelectedPath = false;

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

    /**
     * Whether selection should be web-colored or not.
     * In case it is not web-colored selectionBackgroundColor value will be used as background color.
     */
    public static boolean webColoredSelection = true;

    /**
     * Selection border color.
     */
    public static Color selectionBorderColor = Color.GRAY;

    /**
     * Selection background color.
     * It is used only when webColoredSelection is set to false.
     */
    public static Color selectionBackgroundColor = StyleConstants.bottomBgColor;

    /**
     * Drop cell highlight shade width.
     */
    public static int dropCellShadeWidth = 8;

    /**
     * Node line indent.
     */
    public static int nodeLineIndent = 12;
}