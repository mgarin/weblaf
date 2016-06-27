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
import com.alee.utils.MergeUtils;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
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
    protected transient Map<String, List<IContent>> contentsCache;

    @Override
    public String getId ()
    {
        return id != null ? id : "layout";
    }

    @Override
    public void activate ( final E c, final D d )
    {
        // Performing default actions
        super.activate ( c, d );

        // Activating content
        if ( !isEmpty ( c, d ) )
        {
            for ( final IContent content : contents )
            {
                content.activate ( c, d );
            }
        }
    }

    @Override
    public void deactivate ( final E c, final D d )
    {
        // Performing default actions
        super.deactivate ( c, d );

        // Deactivating content
        if ( !isEmpty ( c, d ) )
        {
            for ( final IContent content : contents )
            {
                content.deactivate ( c, d );
            }
        }
    }

    @Override
    public boolean isEmpty ( final E c, final D d )
    {
        return CollectionUtils.isEmpty ( contents );
    }

    @Override
    public int getBaseline ( final E c, final D d, final int width, final int height )
    {
        if ( !isEmpty ( c, d ) )
        {
            for ( final IContent content : contents )
            {
                final int baseline = content.getBaseline ( c, d, width, height );
                if ( baseline >= 0 )
                {
                    return baseline;
                }
            }
        }
        return super.getBaseline ( c, d, width, height );
    }

    @Override
    public int getContentCount ( final E c, final D d )
    {
        return !isEmpty ( c, d ) ? contents.size () : 0;
    }

    @Override
    public List<IContent> getContents ( final E c, final D d, final String constraints )
    {
        if ( contentsCache == null )
        {
            contentsCache = new HashMap<String, List<IContent>> ( getContentCount ( c, d ) );
            if ( contents != null )
            {
                for ( final IContent content : contents )
                {
                    final String cst = content.getConstraints ();
                    List<IContent> existing = contentsCache.get ( cst );
                    if ( existing == null )
                    {
                        existing = new ArrayList<IContent> ( 1 );
                        contentsCache.put ( cst, existing );
                    }
                    existing.add ( content );
                }
            }
        }
        return contentsCache.get ( constraints );
    }

    @Override
    public boolean isEmpty ( final E c, final D d, final String constraints )
    {
        final List<IContent> contents = getContents ( c, d, constraints );
        if ( !CollectionUtils.isEmpty ( contents ) )
        {
            for ( final IContent content : contents )
            {
                if ( !content.isEmpty ( c, d ) )
                {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final D d, final String constraints )
    {
        final List<IContent> contents = getContents ( c, d, constraints );
        if ( !CollectionUtils.isEmpty ( contents ) )
        {
            for ( final IContent content : contents )
            {
                content.paint ( g2d, bounds, c, d );
            }
        }
    }

    @Override
    public Dimension getPreferredSize ( final E c, final D d, final Dimension available, final String constraints )
    {
        final Dimension ps = new Dimension ( 0, 0 );
        final List<IContent> contents = getContents ( c, d, constraints );
        if ( !CollectionUtils.isEmpty ( contents ) )
        {
            for ( final IContent content : contents )
            {
                final Dimension size = content.getPreferredSize ( c, d, available );
                ps.width = Math.max ( ps.width, size.width );
                ps.height = Math.max ( ps.height, size.height );
            }
        }
        return ps;
    }

    @Override
    public I merge ( final I layout )
    {
        super.merge ( layout );
        contents = layout.isOverwrite () ? layout.contents : MergeUtils.merge ( contents, layout.contents );
        return ( I ) this;
    }
}