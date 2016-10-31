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

package com.alee.painter.decoration.border;

import com.alee.painter.decoration.IDecoration;
import com.alee.utils.MergeUtils;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;

/**
 * Abstract border providing some general method implementations.
 *
 * @param <E> component type
 * @param <D> decoration type
 * @param <I> border type
 * @author Mikle Garin
 */

public abstract class AbstractBorder<E extends JComponent, D extends IDecoration<E, D>, I extends AbstractBorder<E, D, I>>
        implements IBorder<E, D, I>
{
    /**
     * Border ID.
     */
    @XStreamAsAttribute
    protected String id;

    /**
     * Whether or not this border should overwrite previous one when merged.
     */
    @XStreamAsAttribute
    protected Boolean overwrite;

    /**
     * Shade opacity.
     */
    @XStreamAsAttribute
    protected Float opacity;

    @Override
    public String getId ()
    {
        return id != null ? id : "border";
    }

    @Override
    public boolean isOverwrite ()
    {
        return overwrite != null && overwrite;
    }

    @Override
    public void activate ( final E c, final D d )
    {
        // Do nothing by default
    }

    @Override
    public void deactivate ( final E c, final D d )
    {
        // Do nothing by default
    }

    @Override
    public float getOpacity ()
    {
        return opacity != null ? opacity : 1f;
    }

    @Override
    public I merge ( final I border )
    {
        overwrite = overwrite != null && overwrite || border.overwrite != null && border.overwrite;
        opacity = border.isOverwrite () || border.opacity != null ? border.opacity : opacity;
        return ( I ) this;
    }

    @Override
    public I clone ()
    {
        return ( I ) MergeUtils.cloneByFieldsSafely ( this );
    }
}