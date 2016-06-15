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

package com.alee.painter.decoration.layout;

import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.content.IContent;
import com.alee.utils.CompareUtils;
import com.alee.utils.GraphicsUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Popular {@link java.awt.BorderLayout} implementation of {@link com.alee.painter.decoration.layout.IContentLayout}.
 *
 * @param <E> component type
 * @param <D> decoration type
 * @param <I> layout type
 * @author Mikle Garin
 */

@XStreamAlias ( "BorderLayout" )
public class BorderContentLayout<E extends JComponent, D extends IDecoration<E, D>, I extends IconTextContentLayout<E, D, I>>
        extends AbstractContentLayout<E, D, I>
{
    /**
     * Layout constraints.
     */
    public static final String NORTH = "north";
    public static final String SOUTH = "south";
    public static final String WEST = "west";
    public static final String EAST = "east";
    public static final String CENTER = "center";

    /**
     * Horizontal gap between content elements.
     */
    @XStreamAsAttribute
    protected int hgap;

    /**
     * Vertical gap between content elements.
     */
    @XStreamAsAttribute
    protected int vgap;

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final D d, final List<? extends IContent> contents )
    {
        // Proper content clipping
        final Shape oc = GraphicsUtils.setupClip ( g2d, bounds );

        // Painting contents
        final boolean ltr = c.getComponentOrientation ().isLeftToRight ();
        int y = bounds.y;
        int height = bounds.height;
        int x = bounds.x;
        int width = bounds.width;
        IContent child;
        if ( ( child = getContent ( contents, NORTH, ltr ) ) != null )
        {
            final Dimension ps = child.getPreferredSize ( c, d );
            child.paint ( g2d, new Rectangle ( x, y, width, ps.height ), c, d );
            y += ps.height + vgap;
        }
        if ( ( child = getContent ( contents, SOUTH, ltr ) ) != null )
        {
            final Dimension ps = child.getPreferredSize ( c, d );
            child.paint ( g2d, new Rectangle ( x, y + height - ps.height, width, ps.height ), c, d );
            height -= ps.height + vgap;
        }
        if ( ( child = getContent ( contents, EAST, ltr ) ) != null )
        {
            final Dimension ps = child.getPreferredSize ( c, d );
            child.paint ( g2d, new Rectangle ( x + width - ps.width, y, ps.width, height ), c, d );
            width -= ps.width + hgap;
        }
        if ( ( child = getContent ( contents, WEST, ltr ) ) != null )
        {
            final Dimension ps = child.getPreferredSize ( c, d );
            child.paint ( g2d, new Rectangle ( x, y, ps.width, height ), c, d );
            x += ps.width + hgap;
        }
        if ( ( child = getContent ( contents, CENTER, ltr ) ) != null )
        {
            child.paint ( g2d, new Rectangle ( x, y, width, height ), c, d );
        }

        // Restoring clip area
        GraphicsUtils.restoreClip ( g2d, oc );
    }

    /**
     * Returns content for the specified constraints.
     *
     * @param contents    available contents
     * @param constraints content constraints
     * @param ltr         component orientation
     * @return content for the specified constraints
     */
    protected IContent getContent ( final List<? extends IContent> contents, String constraints, final boolean ltr )
    {
        if ( !ltr )
        {
            if ( CompareUtils.equals ( constraints, WEST ) )
            {
                constraints = EAST;
            }
            if ( CompareUtils.equals ( constraints, EAST ) )
            {
                constraints = WEST;
            }
        }
        for ( final IContent content : contents )
        {
            final String c = content.getConstraints ();
            if ( CompareUtils.equals ( c, constraints ) )
            {
                return content;
            }
        }
        return null;
    }

    @Override
    public Dimension getPreferredSize ( final E c, final D d, final List<? extends IContent> contents )
    {
        // todo Implement this
        return new Dimension ( 0, 0 );
    }
}