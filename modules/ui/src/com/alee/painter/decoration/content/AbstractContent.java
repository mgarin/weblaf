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

import com.alee.laf.label.Rotation;
import com.alee.managers.style.Bounds;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.MergeUtils;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Abstract content providing some general method implementations.
 *
 * @param <E> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Mikle Garin
 */

public abstract class AbstractContent<E extends JComponent, D extends IDecoration<E, D>, I extends AbstractContent<E, D, I>>
        implements IContent<E, D, I>
{
    /**
     * Content ID.
     */
    @XStreamAsAttribute
    protected String id;

    /**
     * Bounds this content should be restricted with.
     *
     * @see com.alee.managers.style.Bounds
     */
    @XStreamAsAttribute
    protected Bounds bounds;

    /**
     * Content constraints within {@link com.alee.painter.decoration.layout.IContentLayout}.
     */
    @XStreamAsAttribute
    protected String constraints;

    /**
     * Content padding.
     */
    @XStreamAsAttribute
    protected Insets padding;

    /**
     * Content rotation.
     */
    @XStreamAsAttribute
    protected Rotation rotation;

    @Override
    public String getId ()
    {
        return id != null ? id : "content";
    }

    @Override
    public void activate ( final E c, final D d )
    {
        // Do nothing by default
    }

    @Override
    public void deactivate ( final E c, final D d )
    {
        // Do nothing by default
    }

    @Override
    public Bounds getBoundsType ()
    {
        return bounds != null ? bounds : Bounds.padding;
    }

    @Override
    public String getConstraints ()
    {
        return constraints;
    }

    /**
     * Returns content rotation.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return content rotation
     */
    protected Rotation getRotation ( final E c, final D d )
    {
        return rotation != null ? rotation : Rotation.none;
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final D d )
    {
        // Applying graphics settings
        final AffineTransform transform = g2d.getTransform ();

        // Applying rotation
        final Rotation rotation = getActualRotation ( c, d );
        if ( rotation != Rotation.none )
        {
            double angle = 0;
            double rX = 0;
            double rY = 0;
            switch ( rotation )
            {
                case clockwise:
                    angle = Math.PI / 2;
                    rX = bounds.width;
                    rY = bounds.width;
                    break;

                case upsideDown:
                    angle = Math.PI;
                    rX = bounds.width;
                    rY = bounds.height;
                    break;

                case counterClockwise:
                    angle = -Math.PI / 2;
                    rX = bounds.height;
                    rY = bounds.height;
                    break;
            }
            g2d.rotate ( angle, bounds.x + rX / 2, bounds.y + rY / 2 );
        }
        final Rectangle rotatedBounds = rotation.transpose ( bounds );

        // Proper content clipping
        final Shape oc = GraphicsUtils.setupClip ( g2d, rotatedBounds );

        // Content padding
        if ( padding != null )
        {
            rotatedBounds.x += padding.left;
            rotatedBounds.y += padding.top;
            rotatedBounds.width -= padding.left + padding.right;
            rotatedBounds.height -= padding.top + padding.bottom;
        }

        // Painting content
        paintContent ( g2d, rotatedBounds, c, d );

        // Restoring clip area
        GraphicsUtils.restoreClip ( g2d, oc );

        // Restoring graphics settings
        g2d.setTransform ( transform );
    }

    /**
     * Paints content.
     *
     * @param g2d    graphics context
     * @param bounds painting bounds
     * @param c      painted component
     * @param d      painted decoration state
     */
    protected abstract void paintContent ( final Graphics2D g2d, final Rectangle bounds, final E c, final D d );

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
        final Dimension ps = getContentPreferredSize ( c, d, available );

        // Content padding
        if ( padding != null )
        {
            ps.width += padding.left + padding.right;
            ps.height += padding.top + padding.bottom;
        }

        return getActualRotation ( c, d ).transpose ( ps );
    }

    /**
     * Returns content preferred size.
     *
     * @param c         painted component
     * @param d         painted decoration state
     * @param available available space
     * @return content preferred size
     */
    protected abstract Dimension getContentPreferredSize ( E c, D d, Dimension available );

    /**
     * Returns actual content rotation.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return actual content rotation
     */
    protected Rotation getActualRotation ( final E c, final D d )
    {
        final Rotation tr = getRotation ( c, d );
        return c.getComponentOrientation ().isLeftToRight () ? tr : tr.rightToLeft ();
    }

    @Override
    public I merge ( final I content )
    {
        if ( content.bounds != null )
        {
            bounds = content.bounds;
        }
        if ( content.constraints != null )
        {
            constraints = content.constraints;
        }
        if ( content.padding != null )
        {
            padding = content.padding;
        }
        if ( content.rotation != null )
        {
            rotation = content.rotation;
        }
        return ( I ) this;
    }

    @Override
    public I clone ()
    {
        return ( I ) MergeUtils.cloneByFieldsSafely ( this );
    }
}