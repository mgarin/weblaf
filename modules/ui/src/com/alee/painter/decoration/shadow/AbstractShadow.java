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

package com.alee.painter.decoration.shadow;

import com.alee.painter.decoration.IDecoration;
import com.alee.utils.MergeUtils;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract shadow providing some general method implementations.
 *
 * @param <E> component type
 * @param <D> decoration type
 * @param <I> shadow type
 * @author Mikle Garin
 */

public abstract class AbstractShadow<E extends JComponent, D extends IDecoration<E, D>, I extends AbstractShadow<E, D, I>>
        implements IShadow<E, D, I>
{
    /**
     * Shadow type.
     */
    @XStreamAsAttribute
    protected ShadowType type;

    /**
     * Shadow opacity.
     */
    @XStreamAsAttribute
    protected Float opacity;

    /**
     * Shadow width.
     */
    @XStreamAsAttribute
    protected Integer width;

    /**
     * Shadow color.
     */
    @XStreamAsAttribute
    protected Color color;

    @Override
    public String getId ()
    {
        return getType ().toString ();
    }

    @Override
    public ShadowType getType ()
    {
        return type != null ? type : ShadowType.outer;
    }

    /**
     * Returns shadow opacity.
     *
     * @return shadow opacity
     */
    public float getOpacity ()
    {
        return opacity != null ? opacity : 0.7f;
    }

    @Override
    public int getWidth ()
    {
        return width != null ? width : 0;
    }

    /**
     * Returns shadow color.
     *
     * @return shadow color
     */
    public Color getColor ()
    {
        return color != null ? color : Color.BLACK;
    }

    @Override
    public I merge ( final I shadow )
    {
        if ( shadow.opacity != null )
        {
            opacity = shadow.opacity;
        }
        if ( shadow.width != null )
        {
            width = shadow.width;
        }
        if ( shadow.color != null )
        {
            color = shadow.color;
        }
        return ( I ) this;
    }

    @Override
    public I clone ()
    {
        return ( I ) MergeUtils.cloneByFieldsSafely ( this );
    }
}