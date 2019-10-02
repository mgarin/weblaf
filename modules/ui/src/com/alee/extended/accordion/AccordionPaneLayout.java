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

package com.alee.extended.accordion;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.data.BoxOrientation;
import com.alee.api.merge.Mergeable;
import com.alee.extended.layout.AbstractLayoutManager;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.awt.*;
import java.io.Serializable;

/**
 * {@link LayoutManager} for {@link AccordionPane}.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebAccordion">How to use WebAccordion</a>
 * @see WebAccordion
 * @see AccordionPane
 * @see AccordionModel
 * @see WebAccordionModel
 */
@XStreamAlias ( "AccordionPaneLayout" )
public class AccordionPaneLayout extends AbstractLayoutManager implements Mergeable, Cloneable, Serializable
{
    /**
     * Whether or not header panel should be used for preferred size calculations.
     * Either {@code null} or {@code true} value means header should fit into {@link AccordionPane} preferred width.
     */
    @Nullable
    protected Boolean fitHeader;

    /**
     * Constructs new {@link AccordionPaneLayout} that doesn't use any animations.
     */
    public AccordionPaneLayout ()
    {
        this ( true );
    }

    /**
     * Constructs new {@link AccordionPaneLayout} that doesn't use any animations.
     *
     * @param fitHeader whether or not header panel should be used for preferred size calculations
     */
    public AccordionPaneLayout ( final boolean fitHeader )
    {
        setFitHeader ( fitHeader );
    }

    /**
     * Returns whether or not header panel should be used for preferred size calculations.
     *
     * @return {@code true} if header panel should be used for preferred size calculations, {@code false} otherwise
     */
    public boolean isFitHeader ()
    {
        return fitHeader == null || fitHeader;
    }

    /**
     * Sets whether or not header panel should be used for preferred size calculations.
     *
     * @param fitHeader whether or not header panel should be used for preferred size calculations
     */
    public void setFitHeader ( final boolean fitHeader )
    {
        this.fitHeader = fitHeader;
    }

    @Override
    public void layoutContainer ( @NotNull final Container parent )
    {
        final AccordionPane pane = ( AccordionPane ) parent;
        final Component header = pane.getHeader ();
        final Insets insets = pane.getInsets ();
        final int availableWidth = pane.getWidth () - insets.left - insets.right;
        final int availableHeight = pane.getHeight () - insets.top - insets.bottom;
        final Rectangle bounds = new Rectangle ( insets.left, insets.top, availableWidth, availableHeight );

        final boolean ltr = pane.getComponentOrientation ().isLeftToRight ();
        final BoxOrientation position = pane.getHeaderPosition ();
        final Dimension hps = header.getPreferredSize ();

        // Positioning header
        final int x;
        if ( position.isTop () || position.isBottom () || ( ltr ? position.isLeft () : position.isRight () ) )
        {
            x = bounds.x;
        }
        else
        {
            x = bounds.x + bounds.width - Math.min ( availableWidth, hps.width );
        }
        final int y;
        if ( position.isTop () || position.isLeft () || position.isRight () )
        {
            y = bounds.y;
        }
        else
        {
            y = bounds.y + bounds.height - Math.min ( availableHeight, hps.height );
        }
        final int w;
        if ( position.isTop () || position.isBottom () )
        {
            w = availableWidth;
        }
        else
        {
            w = Math.min ( availableWidth, hps.width );
        }
        final int h;
        if ( position.isLeft () || position.isRight () )
        {
            h = availableHeight;
        }
        else
        {
            h = Math.min ( availableHeight, hps.height );
        }
        header.setBounds ( x, y, w, h );

        // Positioning content if available
        final Component content = pane.getContent ();
        if ( content != null )
        {
            if ( position.isTop () )
            {
                bounds.y += h;
                bounds.height -= h;
            }
            else if ( position.isBottom () )
            {
                bounds.height -= h;
            }
            else if ( ltr && position.isLeft () || !ltr && position.isRight () )
            {
                bounds.x += w;
                bounds.width -= w;
            }
            else
            {
                bounds.width -= w;
            }
            content.setBounds ( bounds );
            content.setVisible ( bounds.width > 0 && bounds.height > 0 );
        }
    }

    @NotNull
    @Override
    public Dimension preferredLayoutSize ( @NotNull final Container parent )
    {
        final AccordionPane pane = ( AccordionPane ) parent;
        final Component header = pane.getHeader ();
        final Component content = pane.getContent ();
        final BoxOrientation position = pane.getHeaderPosition ();
        final boolean vertical = position.isTop () || position.isBottom ();
        final Insets insets = pane.getInsets ();
        final Dimension ps = new Dimension ( 0, 0 );

        final Dimension cps = content != null ? content.getPreferredSize () : new Dimension ();
        ps.width = vertical ? cps.width : 0;
        ps.height = vertical ? 0 : cps.height;

        final Dimension hps = header.getPreferredSize ();
        if ( vertical )
        {
            if ( isFitHeader () )
            {
                ps.width = Math.max ( ps.width, hps.width );
            }
            ps.height += hps.height;
        }
        else
        {
            if ( isFitHeader () )
            {
                ps.height = Math.max ( ps.height, hps.height );
            }
            ps.width += hps.width;
        }

        ps.width += insets.left + insets.right;
        ps.height += insets.top + insets.bottom;

        return ps;
    }

    /**
     * The UI resource version of {@link AccordionPaneLayout}.
     */
    @XStreamAlias ( "AccordionPaneLayout$UIResource" )
    public static final class UIResource extends AccordionPaneLayout implements javax.swing.plaf.UIResource
    {
        /**
         * Implementation is used completely from {@link AccordionPaneLayout}.
         */
    }
}