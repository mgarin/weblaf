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
 * User: mgarin Date: 25.06.12 Time: 17:07
 */

public interface BreadcrumbElement<T extends Component>
{
    public void setType ( BreadcrumbElementType type );

    public void setMargin ( Insets margin );

    public T setMargin ( int top, int left, int bottom, int right );

    public T setMargin ( int spacing );

    public void setOverlap ( int overlap );

    public void setShowProgress ( boolean showProgress );

    public void setProgress ( float progress );

    public BreadcrumbElementPainter getPainter ();
}