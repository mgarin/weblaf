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
 * Simple {@link IContentLayout} implementation with two positions for component with possible overflow and trailing component.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> layout type
 * @author Mikle Garin
 * @see AbstractContentLayout
 * @see IContentLayout
 */
@XStreamAlias ( "OverflowLineLayout" )
public class OverflowLineLayout<C extends JComponent, D extends IDecoration<C, D>, I extends OverflowLineLayout<C, D, I>>
        extends AbstractContentLayout<C, D, I>
{
    /**
     * Leading component constraints.
     * It is always placed before the {@link #OVERFLOW} component.
     */
    protected static final String LEADING = "leading";

    /**
     * Central component constraints.
     * Whenever it doesn't fit into available size it will be shrinked appropriately.
     */
    protected static final String OVERFLOW = "overflow";

    /**
     * Trailing component constraints.
     * It is always placed after the {@link #OVERFLOW} component.
     * Whenever {@link #OVERFLOW} has enough space it will be positioned right after its preferred size plus {@link #gap}.
     */
    protected static final String TRAILING = "trailing";

    /**
     * Horizontal gap between content elements.
     */
    @Nullable
    @XStreamAsAttribute
    protected Integer gap;

    /**
     * Returns gap between content elements.
     *
     * @return gap between content elements
     */
    protected int getGap ()
    {
        return gap != null ? gap : 0;
    }

    @NotNull
    @Override
    public List<IContent> getContents ( @NotNull final C c, @NotNull final D d, @Nullable String constraints )
    {
        // Handling constraints depending on component orientation
        final boolean ltr = isLeftToRight ( c, d );
        if ( !ltr )
        {
            if ( Objects.equals ( constraints, LEADING ) )
            {
                constraints = TRAILING;
            }
            else if ( Objects.equals ( constraints, TRAILING ) )
            {
                constraints = LEADING;
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
        final boolean ltr = isLeftToRight ( c, d );
        final int gap = getGap ();

        final Dimension lps = getPreferredSize ( c, d, new Dimension ( bounds.width, bounds.height ), LEADING
        );
        final Dimension tps = getPreferredSize ( c, d, new Dimension ( Math.max ( 0, bounds.width - lps.width - gap ), bounds.height ),
                TRAILING
        );
        final Dimension ops = getPreferredSize ( c, d,
                new Dimension ( Math.max ( 0, bounds.width - lps.width - gap - tps.width - gap ), bounds.height ), OVERFLOW
        );

        int x = ltr ? bounds.x : Math.max ( bounds.x, bounds.x + bounds.width - lps.width - gap - ops.width - gap - tps.width );
        final int y = bounds.y;
        int width = Math.min ( bounds.width, lps.width + gap + ops.width + gap + tps.width );
        final int height = bounds.height;
        if ( !isEmpty ( c, d, LEADING ) )
        {
            layoutData.put ( LEADING, new Rectangle ( x, y, lps.width, height ) );
            x += lps.width + gap;
            width -= lps.width + gap;
        }
        if ( !isEmpty ( c, d, TRAILING ) )
        {
            final int tx = Math.max ( x, x + width - tps.width );
            layoutData.put ( TRAILING, new Rectangle ( tx, y, tps.width, height ) );
            width -= tps.width + gap;
        }
        if ( !isEmpty ( c, d, OVERFLOW ) )
        {
            layoutData.put ( OVERFLOW, new Rectangle ( x, y, width, height ) );
        }
        return layoutData;
    }

    @NotNull
    @Override
    protected Dimension getContentPreferredSize ( @NotNull final C c, @NotNull final D d, @NotNull final Dimension available )
    {
        final Dimension ps = new Dimension ( 0, 0 );
        final int gap = getGap ();
        if ( !isEmpty ( c, d, LEADING ) )
        {
            final Dimension cps = getPreferredSize ( c, d, available, LEADING );
            ps.width += cps.width + gap;
            ps.height = Math.max ( ps.height, cps.height );
            available.width -= ps.width + gap;
        }
        if ( !isEmpty ( c, d, TRAILING ) )
        {
            final Dimension cps = getPreferredSize ( c, d, available, TRAILING );
            ps.width += cps.width + gap;
            ps.height = Math.max ( ps.height, cps.height );
            available.width -= ps.width + gap;
        }
        if ( !isEmpty ( c, d, OVERFLOW ) )
        {
            final Dimension cps = getPreferredSize ( c, d, available, OVERFLOW );
            ps.width += cps.width;
            ps.height = Math.max ( ps.height, cps.height );
        }
        return ps;
    }
}