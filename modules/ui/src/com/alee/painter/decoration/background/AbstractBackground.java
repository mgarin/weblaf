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

package com.alee.painter.decoration.background;

import com.alee.painter.decoration.IDecoration;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;

/**
 * Convenient base class for any {@link IBackground} implementation.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> background type
 * @author Mikle Garin
 */
public abstract class AbstractBackground<C extends JComponent, D extends IDecoration<C, D>, I extends AbstractBackground<C, D, I>>
        implements IBackground<C, D, I>
{
    /**
     * Background ID.
     */
    @XStreamAsAttribute
    protected String id;

    /**
     * Whether or not this background should overwrite previous one when merged.
     */
    @XStreamAsAttribute
    protected Boolean overwrite;

    /**
     * Background opacity.
     */
    @XStreamAsAttribute
    protected Float opacity;

    @Override
    public String getId ()
    {
        return id != null ? id : "background";
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

    /**
     * Returns background opacity.
     *
     * @return background opacity
     */
    public float getOpacity ()
    {
        return opacity != null ? opacity : 1f;
    }
}