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
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Abstract {@link IContentLayout} implementations providing basic content definitions and methods to work with layout content.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> layout type
 * @author Mikle Garin
 */
public abstract class AbstractContentLayout<C extends JComponent, D extends IDecoration<C, D>, I extends AbstractContentLayout<C, D, I>>
        extends AbstractContent<C, D, I> implements IContentLayout<C, D, I>
{
    /**
     * Optional layout contents.
     * Contents can be standalone elements or complex layout elements containing other contents.
     *
     * @see IContent
     * @see AbstractContent
     * @see IContentLayout
     * @see AbstractContentLayout
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
    public void activate ( final C c, final D d )
    {
        // Performing default actions
        super.activate ( c, d );

        // Activating content
        for ( final IContent content : getContents ( c, d ) )
        {
            content.activate ( c, d );
        }
    }

    @Override
    public void deactivate ( final C c, final D d )
    {
        // Performing default actions
        super.deactivate ( c, d );

        // Deactivating content
        for ( final IContent content : getContents ( c, d ) )
        {
            content.deactivate ( c, d );
        }
    }

    @Override
    public boolean isEmpty ( final C c, final D d )
    {
        for ( final IContent content : getContents ( c, d ) )
        {
            if ( !content.isEmpty ( c, d ) )
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns whether or not specified content is empty.
     *
     * @param c           painted component
     * @param d           painted decoration state
     * @param constraints content constraints
     * @return true if specified content is empty, false otherwise
     */
    public boolean isEmpty ( final C c, final D d, final String constraints )
    {
        for ( final IContent content : getContents ( c, d, constraints ) )
        {
            if ( !content.isEmpty ( c, d ) )
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<IContent> getContents ( final C c, final D d )
    {
        return CollectionUtils.notEmpty ( contents ) ? contents : Collections.<IContent>emptyList ();
    }

    /**
     * Returns contents placed under the specified constraints.
     *
     * @param c           painted component
     * @param d           painted decoration state
     * @param constraints content constraints
     * @return contents placed under the specified constraints
     */
    protected List<IContent> getContents ( final C c, final D d, final String constraints )
    {
        if ( CollectionUtils.notEmpty ( contents ) )
        {
            final Map<String, List<IContent>> contentsCache = getContentsCache ( c, d );
            final List<IContent> contents = contentsCache.get ( constraints );
            return CollectionUtils.notEmpty ( contents ) ? contents : Collections.<IContent>emptyList ();
        }
        return Collections.emptyList ();
    }

    /**
     * Returns contents cache for existing constraints.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return contents cache for existing constraints
     */
    protected Map<String, List<IContent>> getContentsCache ( final C c, final D d )
    {
        if ( contentsCache == null )
        {
            final List<IContent> contents = getContents ( c, d );
            contentsCache = new HashMap<String, List<IContent>> ( contents.size () );
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
        return contentsCache;
    }

    @Override
    public boolean hasContentBaseline ( final C c, final D d )
    {
        // Simply whether or not any of the contents have meaningful baseline
        // If this behavior is not sufficient it can be overridden in specific layout implementation
        for ( final IContent content : getContents ( c, d ) )
        {
            // We are only interested in non-empty contents which provide reasonable baseline
            if ( !content.isEmpty ( c, d ) && content.hasBaseline ( c, d ) )
            {
                // todo Allow marking specific content to be prioritized baseline provider?
                // todo Though for that we would need to iterate through all contents every time
                return true;
            }
        }
        return false;
    }

    @Override
    public int getContentBaseline ( final C c, final D d, final Rectangle bounds )
    {
        // Simply return baseline of the first content that has it
        // If this behavior is not sufficient it can be overridden in specific layout implementation
        for ( final IContent content : getContents ( c, d ) )
        {
            // We are only interested in non-empty contents which provide reasonable baseline
            if ( !content.isEmpty ( c, d ) && content.hasBaseline ( c, d ) )
            {
                // Performing full layout and calculating content baseline based on its bounds
                // This might not seem reasonable, but we need to perform full layout to determine content bounds properly
                final ContentLayoutData layoutData = layoutContent ( c, d, bounds );
                final Rectangle b = layoutData.get ( content.getConstraints () );
                return content.getBaseline ( c, d, b );
            }
        }
        return -1;
    }

    @Override
    public Component.BaselineResizeBehavior getContentBaselineResizeBehavior ( final C c, final D d )
    {
        // Simply return OTHER behavior type as it is simply impossible to "guess" how specific layout acts
        // It is up to layout to override this method and provide an appropriate baseline resize behavior
        return Component.BaselineResizeBehavior.OTHER;
    }

    @Override
    protected void paintContent ( final Graphics2D g2d, final C c, final D d, final Rectangle bounds )
    {
        // Painting only if there is something to paint
        // This global check is added to avoid running full layout
        if ( !isEmpty ( c, d ) )
        {
            // Performing full layout and iterating through the available constraints
            final ContentLayoutData layoutData = layoutContent ( c, d, bounds );
            for ( final String constraints : layoutData.keySet () )
            {
                // Painting all contents within the constraint
                final Rectangle b = layoutData.get ( constraints );
                for ( final IContent content : getContents ( c, d, constraints ) )
                {
                    // Ensure that we only paint non-empty content
                    if ( !content.isEmpty ( c, d ) )
                    {
                        // Painting content in the bounds provided for content constraints
                        content.paint ( g2d, c, d, b );
                    }
                }
            }
        }
    }

    /**
     * Returns preferred size of contents placed under the specified constraints.
     *
     * @param c           painted component
     * @param d           painted decoration state
     * @param constraints content constraints
     * @param available   theoretically available space for this content
     * @return preferred size of contents placed under the specified constraints
     */
    protected Dimension getPreferredSize ( final C c, final D d, final String constraints, final Dimension available )
    {
        final Dimension ps = new Dimension ( 0, 0 );
        for ( final IContent content : getContents ( c, d, constraints ) )
        {
            if ( !content.isEmpty ( c, d ) )
            {
                final Dimension size = content.getPreferredSize ( c, d, new Dimension ( available ) );
                ps.width = Math.max ( ps.width, size.width );
                ps.height = Math.max ( ps.height, size.height );
            }
        }
        return ps;
    }
}