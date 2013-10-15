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

package com.alee.extended.tree;

/**
 * WebCheckBoxTree style class.
 *
 * @author Mikle Garin
 */

public final class WebCheckBoxTreeStyle
{
    /**
     * Retu
     */
    public static boolean recursiveChecking = true;

    /**
     * Gap between checkbox and actual tree renderer.
     */
    public static int checkBoxRendererGap = 0;

    /**
     * Whether checkboxes are visible in the tree or not.
     */
    public static boolean checkBoxVisible = true;

    /**
     * Whether user can interact with checkboxes to change their check state or not.
     */
    public static boolean checkingEnabled = true;

    /**
     * Whether partially checked node should be checked or unchecked on toggle.
     */
    public static boolean checkMixedOnToggle = true;
}