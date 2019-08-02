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

/**
 * Simple rectangular bounds shape implementation.
 * Since provided shape is quite simple it is not cached.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> shape type
 * @author Mikle Garin
 */
@XStreamAlias ( "BoundsShape" )
public class BoundsShape<C extends JComponent, D extends WebDecoration<C, D>, I extends BoundsShape<C, D, I>> extends AbstractShape<C, D, I>
{
    @Override
    public Shape getShape ( final ShapeType type, final Rectangle bounds, final C c, final D d )
    {
        final int bgShear = type.isBorder () ? -1 : 0;
        final int sw = d.getShadowWidth ( ShadowType.outer );
        return new Rectangle ( bounds.x + sw, bounds.y + sw, bounds.width - sw * 2 + bgShear, bounds.height - sw * 2 + bgShear );
    }
}