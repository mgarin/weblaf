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

package com.alee.extended.painter;

/**
 * This interface should be implemented by any component/UI/painter that supports partial decoration.
 * It will allow component view to be configured dynamically, according to its location in the application.
 * These methods can also be used to configure component decoration manually.
 *
 * @author Mikle Garin
 */

public interface PartialDecoration
{
    /**
     * Sets whether should paint top side or not.
     *
     * @param top whether should paint top side or not
     */
    public void setPaintTop ( boolean top );

    /**
     * Sets whether should paint left side or not.
     *
     * @param left whether should paint left side or not
     */
    public void setPaintLeft ( boolean left );

    /**
     * Sets whether should paint bottom side or not.
     *
     * @param bottom whether should paint bottom side or not
     */
    public void setPaintBottom ( boolean bottom );

    /**
     * Sets whether should paint right side or not.
     *
     * @param right whether should paint right side or not
     */
    public void setPaintRight ( boolean right );

    /**
     * Sets whether should paint specific sides or not.
     *
     * @param top    whether should paint top side or not
     * @param left   whether should paint left side or not
     * @param bottom whether should paint bottom side or not
     * @param right  whether should paint right side or not
     */
    public void setPaintSides ( boolean top, boolean left, boolean bottom, boolean right );

    /**
     * Sets whether should paint top side line or not.
     *
     * @param top whether should paint top side line or not
     */
    public void setPaintTopLine ( boolean top );

    /**
     * Sets whether should paint left side line or not.
     *
     * @param left whether should paint left side line or not
     */
    public void setPaintLeftLine ( boolean left );

    /**
     * Sets whether should paint bottom side line or not.
     *
     * @param bottom whether should paint bottom side line or not
     */
    public void setPaintBottomLine ( boolean bottom );

    /**
     * Sets whether should paint right side line or not.
     *
     * @param right whether should paint right side line or not
     */
    public void setPaintRightLine ( boolean right );

    /**
     * Sets whether should paint specific side lines or not.
     *
     * @param top    whether should paint top side line or not
     * @param left   whether should paint left side line or not
     * @param bottom whether should paint bottom side line or not
     * @param right  whether should paint right side line or not
     */
    public void setPaintSideLines ( boolean top, boolean left, boolean bottom, boolean right );
}