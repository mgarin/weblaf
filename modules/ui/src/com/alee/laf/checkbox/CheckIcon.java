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

package com.alee.laf.checkbox;

import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.content.AbstractContent;
import com.alee.utils.GraphicsUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * Checked state icon content for {@link AbstractButton} component.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Mikle Garin
 */
@XStreamAlias ( "CheckIcon" )
public class CheckIcon<C extends AbstractButton, D extends IDecoration<C, D>, I extends CheckIcon<C, D, I>> extends AbstractContent<C, D, I>
{
    /**
     * todo 1. Move check shape into some kind of settings presented in XML
     */

    /**
     * Preferred icon size.
     */
    @XStreamAsAttribute
    protected Dimension size;

    /**
     * Check icon shape stroke.
     */
    @XStreamAsAttribute
    protected Stroke stroke;

    /**
     * Check icon color.
     */
    @XStreamAsAttribute
    protected Color color;

    @Override
    public String getId ()
    {
        return id != null ? id : "check";
    }

    @Override
    public boolean isEmpty ( final C c, final D d )
    {
        return false;
    }

    @Override
    protected void paintContent ( final Graphics2D g2d, final C c, final D d, final Rectangle bounds )
    {
        final Stroke os = GraphicsUtils.setupStroke ( g2d, stroke, stroke != null );
        final Paint op = GraphicsUtils.setupPaint ( g2d, color );

        final int w = bounds.width;
        final int h = bounds.height;
        final int x = bounds.x;
        final int y = bounds.y;
        final GeneralPath gp = new GeneralPath ();
        gp.moveTo ( x + w * 0.1875, y + h * 0.375 );
        gp.lineTo ( x + w * 0.4575, y + h * 0.6875 );
        gp.lineTo ( x + w * 0.875, y + h * 0.125 );
        g2d.draw ( gp );

        GraphicsUtils.restorePaint ( g2d, op );
        GraphicsUtils.restoreStroke ( g2d, os );
    }

    @Override
    protected Dimension getContentPreferredSize ( final C c, final D d, final Dimension available )
    {
        return size != null ? new Dimension ( size ) : new Dimension ( 0, 0 );
    }
}