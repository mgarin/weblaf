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

/**
 * Runtime data {@link WebBreadcrumb} keeps on each added element.
 *
 * @author Mikle Garin
 */
public class BreadcrumbElementData
{
    /**
     * Displayed progress type.
     */
    protected ProgressType progressType;

    /**
     * Breadcrumb element progress.
     * It can be any value between {@code 0.0} and {@code 1.0}.
     */
    protected double progress;

    /**
     * Constructs new {@link BreadcrumbElementData}.
     */
    public BreadcrumbElementData ()
    {
        this.progressType = ProgressType.none;
        this.progress = 0d;
    }

    /**
     * Returns displayed progress type.
     *
     * @return displayed progress type
     */
    public ProgressType getProgressType ()
    {
        return progressType;
    }

    /**
     * Sets displayed progress type.
     *
     * @param type new displayed progress type
     */
    public void setProgressType ( final ProgressType type )
    {
        this.progressType = type;
    }

    /**
     * Returns breadcrumb element progress.
     *
     * @return breadcrumb element progress
     */
    public double getProgress ()
    {
        return progress;
    }

    /**
     * Sets breadcrumb element progress.
     *
     * @param progress breadcrumb element progress
     */
    public void setProgress ( final double progress )
    {
        this.progress = progress;
    }

    /**
     * Enumeration representing possible progress types.
     */
    public static enum ProgressType
    {
        /**
         * Do not display progress.
         */
        none,

        /**
         * Display specific progress.
         */
        progress,

        /**
         * Display indeterminate progress.
         */
        indeterminate
    }
}