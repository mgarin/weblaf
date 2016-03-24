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

package com.alee.painter.decoration.content;

import com.alee.managers.style.Bounds;
import com.alee.painter.decoration.IDecoration;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;

/**
 * Abstract content providing some general method implementations.
 *
 * @param <E> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Mikle Garin
 */

public abstract class AbstractContent<E extends JComponent, D extends IDecoration<E, D>, I extends AbstractContent<E, D, I>>
        implements IContent<E, D, I>
{
    /**
     * Bounds this content should be restricted with.
     *
     * @see com.alee.managers.style.Bounds
     */
    @XStreamAsAttribute
    protected Bounds bounds;

    @Override
    public Bounds getBoundsType ()
    {
        return bounds != null ? bounds : Bounds.padding;
    }
}