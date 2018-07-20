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

import com.alee.painter.decoration.AbstractContainerPainter;
import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.IDecoration;

/**
 * Basic painter for {@link WebBreadcrumb} component.
 * It is used as {@link WebBreadcrumbUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Mikle Garin
 */
public class BreadcrumbPainter<C extends WebBreadcrumb, U extends WBreadcrumbUI<C>, D extends IDecoration<C, D>>
        extends AbstractContainerPainter<C, U, D> implements IBreadcrumbPainter<C, U>
{
    /**
     * Implementation is used completely from {@link AbstractDecorationPainter}.
     */
}