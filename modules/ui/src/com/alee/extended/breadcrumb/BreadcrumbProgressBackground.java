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

import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.background.AbstractClipBackground;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;

/**
 * {@link AbstractClipBackground} implementation that clips background according to element progress value.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> background type
 * @author Mikle Garin
 */
@XStreamAlias ( "BreadcrumbProgressBackground" )
public class BreadcrumbProgressBackground<C extends JComponent, D extends IDecoration<C, D>, I extends BreadcrumbProgressBackground<C, D, I>>
        extends AbstractClipBackground<C, D, I>
{
    @Override
    protected Shape clip ( final Graphics2D g2d, final Rectangle bounds, final C c, final D d, final Shape shape )
    {
        final Shape clippedShape;
        final Container parent = c.getParent ();
        if ( parent instanceof WebBreadcrumb )
        {
            final WebBreadcrumb breadcrumb = ( WebBreadcrumb ) parent;
            final double progress = breadcrumb.getProgress ( c );

            final boolean ltr = c.getComponentOrientation ().isLeftToRight ();
            final Rectangle shapeBounds = shape.getBounds ();
            final int pw = ( int ) Math.round ( shapeBounds.width * progress );
            shapeBounds.x = ltr ? shapeBounds.x : shapeBounds.x + shapeBounds.width - pw;
            shapeBounds.width = pw;

            final Area area = new Area ( shape );
            area.intersect ( new Area ( shapeBounds ) );
            clippedShape = area;
        }
        else
        {
            clippedShape = shape;
        }
        return clippedShape;
    }
}