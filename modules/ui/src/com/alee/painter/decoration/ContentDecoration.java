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

package com.alee.painter.decoration;

import com.alee.managers.style.Bounds;
import com.alee.managers.style.BoundsType;
import com.alee.painter.decoration.content.IContent;
import com.alee.utils.CollectionUtils;
import com.alee.utils.SwingUtils;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Abstract component state decoration providing custom contents and layout for them.
 * It is separated as it might be useful in many kinds of decorations.
 *
 * @param <C> component type
 * @param <I> decoration type
 * @author Mikle Garin
 */
public abstract class ContentDecoration<C extends JComponent, I extends ContentDecoration<C, I>> extends AbstractDecoration<C, I>
{
    /**
     * Optional decoration contents.
     * Contents can be standalone elements or complex layout elements containing other contents.
     *
     * @see com.alee.painter.decoration.content.IContent
     * @see com.alee.painter.decoration.content.AbstractContent
     * @see com.alee.painter.decoration.layout.IContentLayout
     * @see com.alee.painter.decoration.layout.AbstractContentLayout
     */
    @XStreamImplicit
    protected List<IContent> contents = new ArrayList<IContent> ( 1 );

    @Override
    public void activate ( final C c )
    {
        // Performing default actions
        super.activate ( c );

        // Activating content
        for ( final IContent content : getContent () )
        {
            content.activate ( c, this );
        }
    }

    @Override
    public void deactivate ( final C c )
    {
        // Performing default actions
        super.deactivate ( c );

        // Deactivating content
        for ( final IContent content : getContent () )
        {
            content.deactivate ( c, this );
        }
    }

    @Override
    public boolean hasContent ()
    {
        return CollectionUtils.notEmpty ( contents );
    }

    /**
     * Returns decoration content.
     *
     * @return decoration content
     */
    public List<IContent> getContent ()
    {
        return hasContent () ? contents : Collections.<IContent>emptyList ();
    }

    @Override
    public int getBaseline ( final C c, final Bounds bounds )
    {
        // Creating additional bounds
        final Bounds borderBounds = new Bounds ( bounds, BoundsType.border, c, this );
        final Bounds paddingBounds = new Bounds ( borderBounds, BoundsType.padding, c, this );

        // Looking for the first available content with baseline
        for ( final IContent content : getContent () )
        {
            if ( content.hasBaseline ( c, ContentDecoration.this ) )
            {
                // Return baseline provided by content
                final Rectangle b = paddingBounds.get ( content.getBoundsType () );
                return content.getBaseline ( c, ContentDecoration.this, b );
            }
        }

        // Return default baseline
        return super.getBaseline ( c, bounds );
    }

    @Override
    public Component.BaselineResizeBehavior getBaselineResizeBehavior ( final C c )
    {
        // Looking for the first available content with baseline
        for ( final IContent content : getContent () )
        {
            if ( content.hasBaseline ( c, ContentDecoration.this ) )
            {
                // Return baseline behavior provided by content
                return content.getBaselineResizeBehavior ( c, ContentDecoration.this );
            }
        }

        // Return default baseline behavior
        return super.getBaselineResizeBehavior ( c );
    }

    /**
     * Basic implementation of contents painting.
     *
     * @param g2d    graphics context
     * @param bounds painting bounds
     * @param c      painted component
     */
    protected void paintContent ( final Graphics2D g2d, final Bounds bounds, final C c )
    {
        // Creating additional bounds
        final Bounds borderBounds = new Bounds ( bounds, BoundsType.border, c, this );
        final Bounds paddingBounds = new Bounds ( borderBounds, BoundsType.padding, c, this );

        // Painting all decoration contents
        for ( final IContent content : getContent () )
        {
            // Painting content within bounds it requests
            // We cannot check visible rect here since it is zero on {@link javax.swing.CellRendererPane}
            final Rectangle b = paddingBounds.get ( content.getBoundsType () );
            if ( b.width > 0 && b.height > 0 )
            {
                content.paint ( g2d, c, ContentDecoration.this, b );
            }
        }
    }

    @Override
    public Dimension getPreferredSize ( final C c )
    {
        if ( size != null )
        {
            return size;
        }
        else
        {
            Dimension ps = null;
            for ( final IContent content : getContent () )
            {
                final BoundsType bt = content.getBoundsType ();
                final Insets bi = isSection () ? bt.border ( c, this ) : bt.border ( c );
                final Dimension available = isSection () ? new Dimension ( Short.MAX_VALUE, Short.MAX_VALUE ) : bt.bounds ( c ).getSize ();
                final Dimension cps = content.getPreferredSize ( c, this, available );
                cps.width += bi.left + bi.right;
                cps.height += bi.top + bi.bottom;
                ps = SwingUtils.max ( cps, ps );
            }
            return ps;
        }
    }
}