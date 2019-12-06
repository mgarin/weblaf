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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.content.IContent;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Popular {@link java.awt.BorderLayout} implementation of {@link IContentLayout}.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> layout type
 * @author Mikle Garin
 * @see java.awt.BorderLayout
 * @see AbstractContentLayout
 * @see IContentLayout
 */
@XStreamAlias ( "BorderLayout" )
public class BorderLayout<C extends JComponent, D extends IDecoration<C, D>, I extends BorderLayout<C, D, I>>
        extends AbstractContentLayout<C, D, I>
{
    /**
     * Layout constraints.
     */
    protected static final String NORTH = "north";
    protected static final String SOUTH = "south";
    protected static final String WEST = "west";
    protected static final String EAST = "east";
    protected static final String CENTER = "center";

    /**
     * Horizontal gap between content elements.
     */
    @Nullable
    @XStreamAsAttribute
    protected Integer hgap;

    /**
     * Vertical gap between content elements.
     */
    @Nullable
    @XStreamAsAttribute
    protected Integer vgap;

    /**
     * Returns horizontal gap between content elements.
     *
     * @param c {@link JComponent} that is being painted
     * @param d {@link IDecoration} state
     * @return horizontal gap between content elements
     */
    protected int getHorizontalGap ( @NotNull final C c, @NotNull final D d )
    {
        return hgap != null ? hgap : 0;
    }

    /**
     * Returns vertical gap between content elements.
     *
     * @param c {@link JComponent} that is being painted
     * @param d {@link IDecoration} state
     * @return vertical gap between content elements
     */
    protected int getVerticalGap ( @NotNull final C c, @NotNull final D d )
    {
        return vgap != null ? vgap : 0;
    }

    @NotNull
    @Override
    public List<IContent> getContents ( @NotNull final C c, @NotNull final D d, @Nullable String constraints )
    {
        // Handling constraints depending on component orientation
        final boolean ltr = isLeftToRight ( c, d );
        if ( !ltr )
        {
            if ( Objects.equals ( constraints, WEST ) )
            {
                constraints = EAST;
            }
            else if ( Objects.equals ( constraints, EAST ) )
            {
                constraints = WEST;
            }
        }

        // Returning content according to the constraints
        return super.getContents ( c, d, constraints );
    }

    @NotNull
    @Override
    public ContentLayoutData layoutContent ( @NotNull final C c, @NotNull final D d, @NotNull final Rectangle bounds )
    {
        final ContentLayoutData layoutData = new ContentLayoutData ( 5 );
        final int hgap = getHorizontalGap ( c, d );
        final int vgap = getVerticalGap ( c, d );
        int x = bounds.x;
        int y = bounds.y;
        int width = bounds.width;
        int height = bounds.height;
        if ( !isEmpty ( c, d, NORTH ) )
        {
            final Dimension ps = getPreferredSize ( c, d, new Dimension ( width, height ), NORTH );
            layoutData.put ( NORTH, new Rectangle ( x, y, width, ps.height ) );
            y += ps.height + vgap;
            height -= ps.height + vgap;
        }
        if ( !isEmpty ( c, d, SOUTH ) )
        {
            final Dimension ps = getPreferredSize ( c, d, new Dimension ( width, height ), SOUTH );
            layoutData.put ( SOUTH, new Rectangle ( x, y + height - ps.height, width, ps.height ) );
            height -= ps.height + vgap;
        }
        if ( !isEmpty ( c, d, EAST ) )
        {
            final Dimension ps = getPreferredSize ( c, d, new Dimension ( width, height ), EAST );
            layoutData.put ( EAST, new Rectangle ( x + width - ps.width, y, ps.width, height ) );
            width -= ps.width + hgap;
        }
        if ( !isEmpty ( c, d, WEST ) )
        {
            final Dimension ps = getPreferredSize ( c, d, new Dimension ( width, height ), WEST );
            layoutData.put ( WEST, new Rectangle ( x, y, ps.width, height ) );
            x += ps.width + hgap;
            width -= ps.width + hgap;
        }
        if ( !isEmpty ( c, d, CENTER ) )
        {
            layoutData.put ( CENTER, new Rectangle ( x, y, width, height ) );
        }
        return layoutData;
    }

    @NotNull
    @Override
    protected Dimension getContentPreferredSize ( @NotNull final C c, @NotNull final D d, @NotNull final Dimension available )
    {
        final int hgap = getHorizontalGap ( c, d );
        final int vgap = getVerticalGap ( c, d );
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
        int centerWidth = 0;
        int centerHeight = 0;
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
}