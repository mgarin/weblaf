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

import java.awt.*;

/**
 * Breadcrumb element interface.
 *
 * @param <E> breadcrumb element type
 * @author Mikle Garin
 */

public interface BreadcrumbElement<E extends Component>
{
    /**
     * Sets element type.
     *
     * @param type new element type
     */
    public void setType ( BreadcrumbElementType type );

    /**
     * Sets element margin.
     *
     * @param margin new margin
     */
    public void setMargin ( Insets margin );

    /**
     * Sets element margin.
     *
     * @param top    new top margin
     * @param left   new left margin
     * @param bottom new bottom margin
     * @param right  new right margin
     * @return breadcrumb element
     */
    public E setMargin ( int top, int left, int bottom, int right );

    /**
     * Sets element margin.
     *
     * @param spacing new spacing
     * @return breadcrumb element
     */
    public E setMargin ( int spacing );

    /**
     * Sets element overlap.
     *
     * @param overlap new element overlap
     */
    public void setOverlap ( int overlap );

    /**
     * Sets whether element progress should be displayed or not.
     *
     * @param showProgress whether element progress should be displayed or not
     */
    public void setShowProgress ( boolean showProgress );

    /**
     * Sets element progress value.
     *
     * @param progress new element progress value
     */
    public void setProgress ( float progress );

    /**
     * Returns element painter.
     *
     * @return element painter
     */
    public BreadcrumbElementPainter getPainter ();
}