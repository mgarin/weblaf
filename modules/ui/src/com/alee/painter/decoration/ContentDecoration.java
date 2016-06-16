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
    public boolean hasContent ()
    {
        return !CollectionUtils.isEmpty ( contents );
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
            // Painting contents center within content bounds
            // This layout strategy is used by default when layout is not available
            for ( final IContent content : getContent () )
            {
                // Painting content within bounds it requests
                // We cannot check visible rect here since it is zero on {@link javax.swing.CellRendererPane}
                final Bounds bt = content.getBoundsType ();
                final Rectangle b = isSection () ? bt.of ( c, this, bounds ) : bt.of ( c, bounds );
                if ( b.width > 0 && b.height > 0 )
                {
                    content.paint ( g2d, b, c, ContentDecoration.this );
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
            Dimension ps = null;
            for ( final IContent content : getContent () )
            {
                final Bounds bounds = content.getBoundsType ();
                final Insets bi = isSection () ? bounds.insets ( c, this ) : bounds.insets ( c );
                final Dimension cps = content.getPreferredSize ( c, this );
                cps.width += bi.left + bi.right;
                cps.height += bi.top + bi.bottom;
                ps = SwingUtils.max ( cps, ps );
            }
            return ps;
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
        contents = decoration.isOverwrite () ? decoration.contents : MergeUtils.merge ( contents, decoration.contents );
        return ( I ) this;
    }
}