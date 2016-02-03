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

package com.alee.managers.style.skin.web.data.shape;

import com.alee.managers.style.skin.web.data.decoration.IDecoration;
import com.alee.utils.MergeUtils;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;

/**
 * @author Mikle Garin
 */

public abstract class AbstractShape<E extends JComponent, I extends IDecoration<E, I>> implements IShape<E,I>
{
    /**
     * Shape ID.
     */
    @XStreamAsAttribute
    protected String id;

    @Override
    public String getId ()
    {
        return id != null ? id : getDefaultId ();
    }

    /**
     * Returns default shape ID.
     *
     * @return default shape ID
     */
    protected String getDefaultId ()
    {
        return "shape";
    }

    @Override
    public I clone ()
    {
        return ( I ) MergeUtils.cloneByFieldsSafely ( this );
    }
}