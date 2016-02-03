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

package com.alee.managers.style.skin.web.data.background;

import com.alee.managers.style.skin.web.data.decoration.IDecoration;
import com.alee.utils.ReflectUtils;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;

/**
 * @author Mikle Garin
 */

public abstract class AbstractBackground<E extends JComponent, D extends IDecoration<E, D>, I extends AbstractBackground<E, D, I>>
        implements IBackground<E, D, I>
{
    /**
     * Default background ID.
     */
    private static final String defaultId = "background";

    /**
     * Background ID.
     */
    @XStreamAsAttribute
    protected String id;

    /**
     * Background transparency.
     */
    @XStreamAsAttribute
    protected Float transparency = 1f;

    @Override
    public String getId ()
    {
        return id != null ? id : defaultId;
    }

    /**
     * Returns background transparency.
     *
     * @return background transparency
     */
    public float getTransparency ()
    {
        return transparency != null ? transparency : 1f;
    }

    @Override
    public I merge ( final I background )
    {
        if ( background.transparency != null )
        {
            transparency = background.transparency;
        }
        return ( I ) this;
    }

    @Override
    public I clone ()
    {
        return ( I ) ReflectUtils.cloneByFieldsSafely ( this );
    }
}