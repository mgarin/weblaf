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
import com.alee.painter.decoration.content.IContent;
import com.alee.painter.decoration.layout.IContentLayout;
import com.alee.utils.CollectionUtils;
import com.alee.utils.MergeUtils;
import com.alee.utils.SwingUtils;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract component state decoration providing custom contents and layout for them.
 * It is separated as it might be useful in many kinds of decorations.
 *
 * @param <E> component type
 * @param <I> decoration type
 * @author Mikle Garin
 */

public abstract class ContentDecoration<E extends JComponent, I extends ContentDecoration<E, I>> extends AbstractDecoration<E, I>
{
    /**
     * Optional decoration contents layout.
     * You can only provide single layout per decoration instance.
     * Implicit list is only used to provide convenient XML descriptor for this field.
     *
     * @see com.alee.painter.decoration.layout.IContentLayout
     */
    @XStreamImplicit
    protected List<IContentLayout> layout = new ArrayList<IContentLayout> ( 1 );

    /**
     * Optional decoration contents.
     * It can be anything contained within the decoration or placed on top of the decoration.
     * Contents are placed based on either layout, if one was provided, or their own bounds setting.
     *
     * @see com.alee.painter.decoration.content.IContent
     * @see com.alee.painter.decoration.content.AbstractContent
     */
    @XStreamImplicit
    protected List<IContent> contents = new ArrayList<IContent> ( 1 );

    @Override
    public boolean hasContent ()
    {
        return !CollectionUtils.isEmpty ( contents );
    }

    /**
     * Returns decoration contents layout.
     *
     * @return decoration contents layout
     */
    public IContentLayout getLayout ()
    {
        return !CollectionUtils.isEmpty ( layout ) ? layout.get ( 0 ) : null;
    }

    /**
     * Returns decoration content.
     *
     * @return decoration content
     */
    public List<IContent> getContent ()
    {
        return hasContent () ? contents : null;
    }

    /**
     * Basic implementation of contents painting.
     *
     * @param g2d    graphics context
     * @param bounds painting bounds
     * @param c      painted component
     */
    protected void paintContent ( final Graphics2D g2d, final Rectangle bounds, final E c )
    {
        if ( hasContent () )
        {
            final IContentLayout layout = getLayout ();
            final List<IContent> contents = getContent ();
            if ( layout != null )
            {
                // Painting layout directly
                // Layout will handle all content painting on its own
                layout.paint ( g2d, bounds, c, this, contents );
            }
            else
            {
                // Painting contents center within content bounds
                // This layout strategy is used by default when layout is not available
                for ( final IContent content : contents )
                {
                    final Rectangle b = content.getBoundsType ().of ( c, this, bounds );
                    if ( b.width > 0 && b.height > 0 && c.getVisibleRect ().intersects ( b ) )
                    {
                        content.paint ( g2d, b, c, ContentDecoration.this );
                    }
                }
            }
        }
    }

    @Override
    public Dimension getPreferredSize ( final E c )
    {
        if ( size != null )
        {
            return size;
        }
        else if ( hasContent () )
        {
            final IContentLayout layout = getLayout ();
            final List<IContent> contents = getContent ();
            Dimension ps = null;
            Insets bi;
            if ( layout != null )
            {
                final Bounds bounds = layout.getBoundsType ();
                bi = isSection () ? bounds.insets ( c, this ) : bounds.insets ( c );
                ps = layout.getPreferredSize ( c, this, contents );
                ps.width += bi.left + bi.right;
                ps.height += bi.top + bi.bottom;
            }
            else
            {
                for ( final IContent content : contents )
                {
                    final Bounds bounds = content.getBoundsType ();
                    bi = isSection () ? bounds.insets ( c, this ) : bounds.insets ( c );
                    final Dimension cps = content.getPreferredSize ( c, this );
                    cps.width += bi.left + bi.right;
                    cps.height += bi.top + bi.bottom;
                    ps = SwingUtils.max ( cps, ps );
                }
            }
            return new Dimension ( ps.width, ps.height );
        }
        else
        {
            return null;
        }
    }

    @Override
    public I merge ( final I decoration )
    {
        super.merge ( decoration );
        layout = decoration.isOverwrite () ? decoration.layout : MergeUtils.merge ( layout, decoration.layout );
        contents = decoration.isOverwrite () ? decoration.contents : MergeUtils.merge ( contents, decoration.contents );
        return ( I ) this;
    }
}