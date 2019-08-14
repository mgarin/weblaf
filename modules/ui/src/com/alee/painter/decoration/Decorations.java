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

import com.alee.api.annotations.NotNull;
import com.alee.api.merge.Overwriting;
import com.alee.utils.collection.EmptyIterator;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import javax.swing.*;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * Class representing a group of decorations.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @author Mikle Garin
 * @see IDecoration
 */
@XStreamAlias ( "decorations" )
public final class Decorations<C extends JComponent, D extends IDecoration<C, D>>
        implements Iterable<D>, Overwriting, Cloneable, Serializable
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

    @NotNull
    @Override
    public Iterator<D> iterator ()
    {
        return decorations != null ? decorations.iterator () : EmptyIterator.<D>instance ();
    }

    @Override
    public boolean isOverwrite ()
    {
        return overwrite != null && overwrite;
    }
}