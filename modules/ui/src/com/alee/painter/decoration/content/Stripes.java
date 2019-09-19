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

package com.alee.painter.decoration.content;

import com.alee.api.annotations.Nullable;
import com.alee.api.data.BoxOrientation;
import com.alee.api.data.Orientation;
import com.alee.api.merge.behavior.OverwriteOnMerge;
import com.alee.painter.decoration.DecorationException;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.GraphicsUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Abstract content representing multiple parallel stripes.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Mikle Garin
 */
@XStreamAlias ( "Stripes" )
public class Stripes<C extends JComponent, D extends IDecoration<C, D>, I extends Stripes<C, D, I>> extends AbstractContent<C, D, I>
{
    /**
     * Stripes orientation.
     */
    @XStreamAsAttribute
    protected Orientation orientation;

    /**
     * Stripes alignment.
     * It can contain different values depending on {@link #orientation}.
     */
    @XStreamAsAttribute
    protected BoxOrientation align;

    /**
     * Stripes data.
     * At least one {@link Stripe} must always be provided.
     */
    @XStreamImplicit
    @OverwriteOnMerge
    protected List<Stripe> stripes;

    @Nullable
    @Override
    public String getId ()
    {
        return id != null ? id : "stripes";
    }

    /**
     * Returns stripes orientation.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return stripes orientation
     */
    public Orientation getOrientation ( final C c, final D d )
    {
        if ( orientation != null )
        {
            return orientation;
        }
        else
        {
            throw new DecorationException ( "Stripe orientation must be specified" );
        }
    }

    /**
     * Returns stripes alignment within provided bounds.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return stripes alignment within provided bounds
     */
    public BoxOrientation getAlign ( final C c, final D d )
    {
        final BoxOrientation actualAlign;
        if ( align != null )
        {
            if ( getOrientation ( c, d ).isVertical () )
            {
                if ( align.isLeft () || align.isCenter () || align.isRight () )
                {
                    actualAlign = align;
                }
                else
                {
                    throw new DecorationException ( "Vertical stripe cannot use " + align + " alignment" );
                }
            }
            else
            {
                if ( align.isTop () || align.isCenter () || align.isBottom () )
                {
                    actualAlign = align;
                }
                else
                {
                    throw new DecorationException ( "Horizontal stripe cannot use " + align + " alignment" );
                }
            }
        }
        else
        {
            actualAlign = BoxOrientation.center;
        }
        return actualAlign;
    }

    /**
     * Returns stripes count.
     *
     * @return stripes count
     */
    protected int getStripesCount ()
    {
        return stripes != null ? stripes.size () : 0;
    }

    @Override
    public boolean isEmpty ( final C c, final D d )
    {
        return getStripesCount () == 0;
    }

    @Override
    protected void paintContent ( final Graphics2D g2d, final C c, final D d, final Rectangle bounds )
    {
        // Display settings
        final boolean ltr = isLeftToRight ( c, d );
        final Orientation orientation = getOrientation ( c, d );
        final BoxOrientation align = getAlign ( c, d );

        // Painting each stripe
        for ( int i = 0; i < stripes.size (); i++ )
        {
            // Current stripe
            final Stripe stripe = stripes.get ( i );

            // Calculating stripe coordinates
            final int x1;
            final int y1;
            final int x2;
            final int y2;
            if ( orientation.isVertical () )
            {
                if ( ltr ? align.isLeft () : align.isRight () )
                {
                    x1 = x2 = bounds.x + i;
                }
                else if ( !ltr ? align.isLeft () : align.isRight () )
                {
                    x1 = x2 = bounds.x + bounds.width - i - 1;
                }
                else
                {
                    x1 = x2 = bounds.x + ( bounds.width - stripes.size () ) / 2 + i;
                }
                y1 = bounds.y;
                y2 = bounds.y + bounds.height - 1;
            }
            else
            {
                if ( align.isTop () )
                {
                    y1 = y2 = bounds.y + i;
                }
                else if ( align.isBottom () )
                {
                    y1 = y2 = bounds.y + bounds.height - stripes.size () + i;
                }
                else
                {
                    y1 = y2 = bounds.y + ( bounds.height - stripes.size () ) / 2 + i;
                }
                x1 = bounds.x;
                x2 = bounds.x + bounds.width - 1;
            }

            // Painting stripe
            final Stroke stroke = GraphicsUtils.setupStroke ( g2d, stripe.getStroke (), stripe.getStroke () != null );
            final Paint op = GraphicsUtils.setupPaint ( g2d, stripe.getPaint ( x1, y1, x2, y2 ) );
            g2d.drawLine ( x1, y1, x2, y2 );
            GraphicsUtils.restorePaint ( g2d, op );
            GraphicsUtils.restoreStroke ( g2d, stroke, stripe.getStroke () != null );
        }
    }

    @Override
    protected Dimension getContentPreferredSize ( final C c, final D d, final Dimension available )
    {
        final int stripes = getStripesCount ();
        final Orientation orientation = getOrientation ( c, d );
        return new Dimension ( orientation.isVertical () ? stripes : 0, orientation.isVertical () ? 0 : stripes );
    }
}