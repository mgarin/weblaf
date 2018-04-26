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

import com.alee.painter.decoration.IDecoration;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract shape providing some general method implementations.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> shape type
 * @author Mikle Garin
 */
public abstract class AbstractShape<C extends JComponent, D extends IDecoration<C, D>, I extends AbstractShape<C, D, I>>
        implements IShape<C, D, I>
{
    /**
     * Shape ID.
     */
    @XStreamAsAttribute
    protected String id;

    /**
     * Whether or not this shape should overwrite previous one when merged.
     */
    @XStreamAsAttribute
    protected Boolean overwrite;

    @Override
    public String getId ()
    {
        return id != null ? id : "shape";
    }

    @Override
    public boolean isOverwrite ()
    {
        return overwrite != null && overwrite;
    }

    @Override
    public void activate ( final C c, final D d )
    {
        // Do nothing by default
    }

    @Override
    public void deactivate ( final C c, final D d )
    {
        // Do nothing by default
    }

    @Override
    public boolean isVisible ( final ShapeType type, final Rectangle bounds, final C c, final D d )
    {
        return true;
    }

    @Override
    public Object[] getShapeSettings ( final Rectangle bounds, final C c, final D d )
    {
        return null;
    }

    @Override
    public StretchInfo getStretchInfo ( final Rectangle bounds, final C c, final D d )
    {
        return null;
    }

    /**
     * Returns point for the specified coordinates.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @return point for the specified coordinates
     */
    protected Point p ( final int x, final int y )
    {
        return new Point ( x, y );
    }
}