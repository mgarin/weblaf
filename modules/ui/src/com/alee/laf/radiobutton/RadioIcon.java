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

package com.alee.laf.radiobutton;

import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.content.AbstractContent;
import com.alee.utils.GraphicsUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Checked state icon content for {@link AbstractButton} component.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Mikle Garin
 */
@XStreamAlias ( "RadioIcon" )
public class RadioIcon<C extends AbstractButton, D extends IDecoration<C, D>, I extends RadioIcon<C, D, I>> extends AbstractContent<C, D, I>
{
    /**
     * Preferred icon size.
     */
    @XStreamAsAttribute
    protected Dimension size;

    /**
     * Left side background color.
     */
    @XStreamAsAttribute
    protected Color leftColor;

    /**
     * Right side background color.
     */
    @XStreamAsAttribute
    protected Color rightColor;

    @Override
    public String getId ()
    {
        return id != null ? id : "radio";
    }

    @Override
    public boolean isEmpty ( final C c, final D d )
    {
        return false;
    }

    @Override
    protected void paintContent ( final Graphics2D g2d, final C c, final D d, final Rectangle bounds )
    {
        final int x = bounds.x;
        final int y = bounds.y;
        final int w = bounds.width;
        final int h = bounds.height;

        // Configuring graphics
        final GradientPaint paint = new GradientPaint ( x, 0, leftColor, x + w, 0, rightColor );
        final Paint op = GraphicsUtils.setupPaint ( g2d, paint );

        // Filling content shape
        g2d.fill ( new Ellipse2D.Double ( x, y, w, h ) );

        // Restoring graphics settings
        GraphicsUtils.restorePaint ( g2d, op );
    }

    @Override
    protected Dimension getContentPreferredSize ( final C c, final D d, final Dimension available )
    {
        return size != null ? new Dimension ( size ) : new Dimension ( 0, 0 );
    }
}