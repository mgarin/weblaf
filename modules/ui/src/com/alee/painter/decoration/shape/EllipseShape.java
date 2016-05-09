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

package com.alee.painter.decoration.shape;

import com.alee.painter.decoration.WebDecoration;
import com.alee.painter.decoration.shadow.ShadowType;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Ellipse shape implementation.
 *
 * @param <E> component type
 * @param <D> decoration type
 * @param <I> shape type
 * @author Mikle Garin
 */

@XStreamAlias ("EllipseShape")
public class EllipseShape<E extends JComponent, D extends WebDecoration<E, D>, I extends EllipseShape<E, D, I>>
        extends AbstractShape<E, D, I>
{
    @Override
    public Insets getBorderInsets ( final E c, final D d )
    {
        // Ellipse doesn't support visual grouping so insets are quite simple
        final int borderWidth = ( int ) Math.round ( Math.floor ( d.getBorderWidth () ) );
        final int shadowWidth = d.getShadeWidth ( ShadowType.outer );
        final int spacing = shadowWidth + borderWidth;
        return new Insets ( spacing, spacing, spacing, spacing );
    }

    @Override
    public Shape getShape ( final ShapeType type, final Rectangle bounds, final E c, final D d )
    {
        // Ellipse shape is quite simple so there is no need to cache it
        final int sw = d.getShadeWidth ( ShadowType.outer );
        final int bgShear = type.isBorder () ? -1 : 0;
        return new Ellipse2D.Double ( bounds.x + sw, bounds.y + sw, bounds.width - sw * 2 + bgShear, bounds.height - sw * 2 + bgShear );
    }
}