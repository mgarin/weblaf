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

package com.alee.managers.style.skin.web.data.shade;

import com.alee.managers.style.skin.web.data.decoration.IDecoration;
import com.alee.utils.MergeUtils;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.awt.*;

/**
 * @author Mikle Garin
 */

public abstract class AbstractShade<E extends JComponent, D extends IDecoration<E, D>, I extends AbstractShade<E, D, I>>
        implements IShade<E, D, I>
{
    /**
     * Default shade color.
     */
    protected static final Color defaultColor = new Color ( 200, 200, 200 );

    /**
     * Shade type.
     */
    @XStreamAsAttribute
    protected ShadeType type;

    /**
     * Shade transparency.
     */
    @XStreamAsAttribute
    protected Float transparency;

    /**
     * Shade width.
     */
    @XStreamAsAttribute
    protected Integer width;

    /**
     * Shade color.
     */
    @XStreamAsAttribute
    protected Color color;

    @Override
    public String getId ()
    {
        return getType ().toString ();
    }

    @Override
    public ShadeType getType ()
    {
        return type != null ? type : ShadeType.outer;
    }

    @Override
    public float getTransparency ()
    {
        return transparency != null ? transparency : 0.7f;
    }

    @Override
    public int getWidth ()
    {
        return width != null ? width : 0;
    }

    @Override
    public Color getColor ()
    {
        return color != null ? color : defaultColor;
    }

    @Override
    public I merge ( final I shade )
    {
        if ( shade.transparency != null )
        {
            transparency = shade.transparency;
        }
        if ( shade.width != null )
        {
            width = shade.width;
        }
        if ( shade.color != null )
        {
            color = shade.color;
        }
        return ( I ) this;
    }

    @Override
    public I clone ()
    {
        return ( I ) MergeUtils.cloneByFieldsSafely ( this );
    }
}