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
public class BorderContentLayout<E extends JComponent, D extends IDecoration<E, D>, I extends BorderContentLayout<E, D, I>>
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
    protected Integer hgap;

    /**
     * Vertical gap between content elements.
     */
    @XStreamAsAttribute
    protected Integer vgap;

    /**
     * Returns horizontal gap between content elements.
     *
     * @return horizontal gap between content elements
     */
    protected int getHorizontalGap ()
    {
        return hgap != null ? hgap : 0;
    }

    /**
     * Returns vertical gap between content elements.
     *
     * @return vertical gap between content elements
     */
    protected int getVerticalGap ()
    {
        return vgap != null ? vgap : 0;
    }

    @Override
    public List<IContent> getContents ( final E c, final D d, String constraints )
    {
        final boolean ltr = c.getComponentOrientation ().isLeftToRight ();
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
        return super.getContents ( c, d, constraints );
    }

    @Override
    protected void paintContent ( final Graphics2D g2d, final Rectangle bounds, final E c, final D d )
    {
        final int hgap = getHorizontalGap ();
        final int vgap = getVerticalGap ();
        int y = bounds.y;
        int height = bounds.height;
        int x = bounds.x;
        int width = bounds.width;
        if ( !isEmpty ( c, d, NORTH ) )
        {
            final Dimension ps = getPreferredSize ( c, d, new Dimension ( width, height ), NORTH );
            paint ( g2d, new Rectangle ( x, y, width, ps.height ), c, d, NORTH );
            y += ps.height + vgap;
            height -= ps.height + vgap;
        }
        if ( !isEmpty ( c, d, SOUTH ) )
        {
            final Dimension ps = getPreferredSize ( c, d, new Dimension ( width, height ), SOUTH );
            paint ( g2d, new Rectangle ( x, y + height - ps.height, width, ps.height ), c, d, SOUTH );
            height -= ps.height + vgap;
        }
        if ( !isEmpty ( c, d, EAST ) )
        {
            final Dimension ps = getPreferredSize ( c, d, new Dimension ( width, height ), EAST );
            paint ( g2d, new Rectangle ( x + width - ps.width, y, ps.width, height ), c, d, EAST );
            width -= ps.width + hgap;
        }
        if ( !isEmpty ( c, d, WEST ) )
        {
            final Dimension ps = getPreferredSize ( c, d, new Dimension ( width, height ), WEST );
            paint ( g2d, new Rectangle ( x, y, ps.width, height ), c, d, WEST );
            x += ps.width + hgap;
            width -= ps.width + hgap;
        }
        if ( !isEmpty ( c, d, CENTER ) )
        {
            paint ( g2d, new Rectangle ( x, y, width, height ), c, d, CENTER );
        }
    }

    @Override
    protected Dimension getContentPreferredSize ( final E c, final D d, final Dimension available )
    {
        final int hgap = getHorizontalGap ();
        final int vgap = getVerticalGap ();
        final Dimension ps = new Dimension ( 0, 0 );
        if ( !isEmpty ( c, d, NORTH ) )
        {
            final Dimension cps = getPreferredSize ( c, d, available, NORTH );
            ps.width = Math.max ( ps.width, cps.width );
            ps.height += cps.height + vgap;
            available.height -= ps.height + vgap;
        }
        if ( !isEmpty ( c, d, SOUTH ) )
        {
            final Dimension cps = getPreferredSize ( c, d, available, SOUTH );
            ps.width = Math.max ( ps.width, cps.width );
            ps.height += cps.height + vgap;
            available.height -= ps.height + vgap;
        }
        int centerHeight = 0;
        int centerWidth = 0;
        if ( !isEmpty ( c, d, EAST ) )
        {
            final Dimension cps = getPreferredSize ( c, d, available, EAST );
            centerWidth += cps.width + hgap;
            centerHeight = Math.max ( centerHeight, cps.height );
            available.width -= ps.width + hgap;
        }
        if ( !isEmpty ( c, d, WEST ) )
        {
            final Dimension cps = getPreferredSize ( c, d, available, WEST );
            centerWidth += cps.width + hgap;
            centerHeight = Math.max ( centerHeight, cps.height );
            available.width -= ps.width + hgap;
        }
        if ( !isEmpty ( c, d, CENTER ) )
        {
            final Dimension cps = getPreferredSize ( c, d, available, CENTER );
            centerWidth += cps.width;
            centerHeight = Math.max ( centerHeight, cps.height );
        }
        ps.width = Math.max ( ps.width, centerWidth );
        ps.height += centerHeight;
        return ps;
    }

    @Override
    public I merge ( final I layout )
    {
        super.merge ( layout );
        hgap = layout.isOverwrite () || layout.hgap != null ? layout.hgap : hgap;
        vgap = layout.isOverwrite () || layout.vgap != null ? layout.vgap : vgap;
        return ( I ) this;
    }
}