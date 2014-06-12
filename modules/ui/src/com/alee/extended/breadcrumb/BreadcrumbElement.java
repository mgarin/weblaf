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

package com.alee.extended.breadcrumb;

/**
 * Breadcrumb element interface.
 *
 * @author Mikle Garin
 */

public interface BreadcrumbElement
{
    /**
     * Sets whether element progress should be displayed or not.
     *
     * @param showProgress whether element progress should be displayed or not
     */
    public void setShowProgress ( boolean showProgress );

    /**
     * Returns whether element progress should be displayed or not.
     *
     * @return true if element progress should be displayed, false otherwise
     */
    public boolean isShowProgress ();

    /**
     * Sets element progress value.
     * This value should be between 0f and 1f.
     *
     * @param progress new element progress value
     */
    public void setProgress ( float progress );

    /**
     * Returns element progress value.
     * This value is between 0f and 1f.
     *
     * @return element progress value
     */
    public float getProgress ();
}