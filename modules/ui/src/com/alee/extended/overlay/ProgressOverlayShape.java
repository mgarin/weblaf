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

package com.alee.extended.overlay;

import com.alee.api.annotations.NotNull;
import com.alee.extended.canvas.WebCanvas;
import com.alee.painter.PainterSupport;
import com.alee.painter.decoration.WebDecoration;
import com.alee.painter.decoration.shape.AbstractShape;
import com.alee.painter.decoration.shape.ShapeType;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.swing.*;
import java.awt.*;

/**
 * Shape provided by component of {@link WebOverlay} in which {@link WebCanvas} is added as an {@link Overlay}.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> shape type
 * @author Mikle Garin
 */
@XStreamAlias ( "ProgressOverlayShape" )
public class ProgressOverlayShape<C extends WebCanvas, D extends WebDecoration<C, D>, I extends ProgressOverlayShape<C, D, I>>
        extends AbstractShape<C, D, I>
{
    @NotNull
    @Override
    public Shape getShape ( @NotNull final ShapeType type, @NotNull final Rectangle bounds, @NotNull final C c, @NotNull final D d )
    {
        Shape shape = bounds;
        if ( c.getParent () instanceof WebProgressOverlay )
        {
            final JComponent content = ( ( WebProgressOverlay ) c.getParent () ).getContent ();
            if ( content != null )
            {
                shape = PainterSupport.getShape ( content );
            }
        }
        return shape;
    }
}