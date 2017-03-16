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

package com.alee.painter.decoration;

import com.alee.api.merge.Mergeable;
import com.alee.utils.MergeUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import javax.swing.*;
import java.util.Iterator;
import java.util.List;

/**
 * Class representing a group of decorations.
 *
 * @param <E> component type
 * @param <D> decoration type
 * @author Mikle Garin
 */

@XStreamAlias ( "Decorations" )
public final class Decorations<E extends JComponent, D extends IDecoration<E, D>>
        implements Iterable<D>, Mergeable<Decorations<E, D>>, Cloneable
{
    /**
     * Whether or not these decorations should overwrite previous ones when merged.
     */
    @XStreamAsAttribute
    private Boolean overwrite;

    /**
     * Available decorations.
     */
    @XStreamImplicit
    private List<D> decorations;

    /**
     * Returns available decorations amount.
     *
     * @return available decorations amount
     */
    public int size ()
    {
        return decorations != null ? decorations.size () : 0;
    }

    @Override
    public Iterator<D> iterator ()
    {
        return decorations.iterator ();
    }

    /**
     * Returns whether or not these decorations should overwrite any previous ones when merged.
     *
     * @return true if these decorations should overwrite any previous ones when merged, false otherwise
     */
    private boolean isOverwrite ()
    {
        return overwrite != null && overwrite;
    }

    @Override
    public Decorations<E, D> merge ( final Decorations<E, D> object )
    {
        overwrite = overwrite != null && overwrite || object.overwrite != null && object.overwrite;
        decorations = object.isOverwrite () ? object.decorations : MergeUtils.merge ( decorations, object.decorations );
        return this;
    }

    @Override
    public Object clone ()
    {
        return MergeUtils.cloneByFieldsSafely ( this );
    }
}