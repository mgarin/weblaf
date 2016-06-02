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

package com.alee.painter.decoration.layout;

import com.alee.managers.style.Bounds;
import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.content.IContent;
import com.alee.utils.CompareUtils;
import com.alee.utils.MergeUtils;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.util.List;

/**
 * Abstract content layout providing some general method implementations.
 *
 * @param <E> component type
 * @param <D> decoration type
 * @param <I> layout type
 * @author Mikle Garin
 */

public abstract class AbstractContentLayout<E extends JComponent, D extends IDecoration<E, D>, I extends AbstractContentLayout<E, D, I>>
        implements IContentLayout<E, D, I>
{
    /**
     * todo 1. Implement layout contents rotation (graphics + preferred size)
     */

    /**
     * Content layout ID.
     */
    @XStreamAsAttribute
    protected String id;

    /**
     * Bounds layout contents should be restricted with.
     *
     * @see com.alee.managers.style.Bounds
     */
    @XStreamAsAttribute
    protected Bounds bounds;

    @Override
    public String getId ()
    {
        return id != null ? id : "layout";
    }

    @Override
    public Bounds getBoundsType ()
    {
        return bounds != null ? bounds : Bounds.padding;
    }

    /**
     * Returns content placed under the specified constraints.
     *
     * @param contents    available contents
     * @param constraints constraints to find content for
     * @return content placed under the specified constraints
     */
    public IContent getContent ( final List<? extends IContent> contents, final String constraints )
    {
        for ( final IContent content : contents )
        {
            if ( CompareUtils.equals ( content.getConstraints (), constraints ) )
            {
                return content;
            }
        }
        return null;
    }

    /**
     * Returns whether or not specified content is empty.
     *
     * @param c       painted component
     * @param d       painted decoration state
     * @param content content
     * @return true if specified content is empty, false otherwise
     */
    public boolean isEmpty ( final E c, final D d, final IContent content )
    {
        return content == null || content.isEmpty ( c, d );
    }

    @Override
    public I merge ( final I layout )
    {
        if ( layout.bounds != null )
        {
            bounds = layout.bounds;
        }
        return ( I ) this;
    }

    @Override
    public I clone ()
    {
        return ( I ) MergeUtils.cloneByFieldsSafely ( this );
    }
}