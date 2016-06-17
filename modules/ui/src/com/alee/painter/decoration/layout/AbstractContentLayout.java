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
import com.alee.painter.decoration.content.AbstractContent;
import com.alee.painter.decoration.content.IContent;
import com.alee.utils.CollectionUtils;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.MergeUtils;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract content layout providing some general method implementations.
 *
 * @param <E> component type
 * @param <D> decoration type
 * @param <I> layout type
 * @author Mikle Garin
 */

public abstract class AbstractContentLayout<E extends JComponent, D extends IDecoration<E, D>, I extends AbstractContentLayout<E, D, I>>
        extends AbstractContent<E, D, I> implements IContentLayout<E, D, I>
{
    /**
     * todo 1. Implement layout contents rotation (graphics + preferred size)
     */

    /**
     * Layout content padding.
     * Whether it will be used or not is decided within layout implementation.
     * It is highly recommended to use it as it provides an additional useful layer of customization.
     */
    @XStreamAsAttribute
    protected Insets padding;

    /**
     * Optional layout contents.
     * Contents can be standalone elements or complex layout elements containing other contents.
     *
     * @see com.alee.painter.decoration.content.IContent
     * @see com.alee.painter.decoration.content.AbstractContent
     * @see com.alee.painter.decoration.layout.IContentLayout
     * @see com.alee.painter.decoration.layout.AbstractContentLayout
     */
    @XStreamImplicit
    protected List<IContent> contents;

    /**
     * Contents cache map.
     * It is used for optimal contents retrieval.
     */
    protected transient Map<String, IContent> contentsCache;

    @Override
    public String getId ()
    {
        return id != null ? id : "layout";
    }

    @Override
    public boolean isEmpty ( final E c, final D d )
    {
        return CollectionUtils.isEmpty ( contents );
    }

    @Override
    public int getContentCount ( final E c, final D d )
    {
        return contents != null ? contents.size () : 0;
    }

    @Override
    public boolean isEmpty ( final E c, final D d, final String constraints )
    {
        final IContent content = getContent ( c, d, constraints );
        return content == null || content.isEmpty ( c, d );
    }

    @Override
    public IContent getContent ( final E c, final D d, final String constraints )
    {
        if ( contentsCache == null )
        {
            contentsCache = new HashMap<String, IContent> ( getContentCount ( c, d ) );
            if ( contents != null )
            {
                for ( final IContent content : contents )
                {
                    contentsCache.put ( content.getConstraints (), content );
                }
            }
        }
        return contentsCache.get ( constraints );
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final D d )
    {
        // Content padding
        if ( padding != null )
        {
            bounds.x += padding.left;
            bounds.y += padding.top;
            bounds.width -= padding.left + padding.right;
            bounds.height -= padding.top + padding.bottom;
        }

        // Proper content clipping
        final Shape oc = GraphicsUtils.setupClip ( g2d, bounds );

        // Painting layout content
        paintImpl ( g2d, bounds, c, d );

        // Restoring clip area
        GraphicsUtils.restoreClip ( g2d, oc );
    }

    /**
     * Paints layout content.
     *
     * @param g2d    graphics context
     * @param bounds painting bounds
     * @param c      painted component
     * @param d      painted decoration state
     */
    protected abstract void paintImpl ( Graphics2D g2d, Rectangle bounds, E c, D d );

    @Override
    public Dimension getPreferredSize ( final E c, final D d, final Dimension available )
    {
        // Content padding
        if ( padding != null )
        {
            available.width -= padding.left + padding.right;
            available.height -= padding.top + padding.bottom;
        }

        // Content preferred size
        final Dimension ps = getPreferredSizeImpl ( c, d, available );

        // Content padding
        if ( padding != null )
        {
            ps.width += padding.left + padding.right;
            ps.height += padding.top + padding.bottom;
        }

        return ps;
    }

    /**
     * Returns layout preferred size.
     *
     * @param c         painted component
     * @param d         painted decoration state
     * @param available theoretically available space for this container
     * @return layout preferred size
     */
    protected abstract Dimension getPreferredSizeImpl ( E c, D d, Dimension available );

    @Override
    public I merge ( final I layout )
    {
        super.merge ( layout );
        if ( layout.padding != null )
        {
            padding = layout.padding;
        }
        contents = MergeUtils.merge ( contents, layout.contents );
        return ( I ) this;
    }
}