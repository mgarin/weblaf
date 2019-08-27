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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.painter.decoration.IDecoration;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract shadow providing some general method implementations.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> shadow type
 * @author Mikle Garin
 */
public abstract class AbstractShadow<C extends JComponent, D extends IDecoration<C, D>, I extends AbstractShadow<C, D, I>>
        implements IShadow<C, D, I>
{
    /**
     * Whether or not this shadow should overwrite previous one when merged.
     */
    @XStreamAsAttribute
    protected Boolean overwrite;

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

    @Nullable
    @Override
    public String getId ()
    {
        return getType ().name ();
    }

    @Override
    public boolean isOverwrite ()
    {
        return overwrite != null && overwrite;
    }

    @Override
    public void activate ( final C c, final D d )
    {
        /**
         * Do nothing by default.
         */
    }

    @Override
    public void deactivate ( final C c, final D d )
    {
        /**
         * Do nothing by default.
         */
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
}