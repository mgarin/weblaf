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

package com.alee.extended.painter;

import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This abstract painter provides a few additional useful features atop of the Painter interface.
 * Usually this class is extended by various painters instead of implementing Painter interface directly.
 *
 * @param <E> component type
 * @author Mikle Garin
 * @see Painter
 */

public abstract class AbstractPainter<E extends JComponent> implements Painter<E>
{
    /**
     * Whether visual data is opaque or not.
     */
    protected Boolean opaque = false;

    /**
     * Visual data preferred size.
     */
    protected Dimension preferredSize = new Dimension ( 0, 0 );

    /**
     * Visual data margin.
     */
    protected Insets margin = new Insets ( 0, 0, 0, 0 );

    /**
     * Painter listeners.
     */
    protected List<PainterListener> listeners = new ArrayList<PainterListener> ( 1 );

    /**
     * {@inheritDoc}
     */
    @Override
    public void install ( final E c )
    {
        // Simply do nothing by default
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void uninstall ( final E c )
    {
        // Simply do nothing by default
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isOpaque ( final E c )
    {
        return opaque;
    }

    /**
     * Sets whether visual data provided by this painter is opaque or not.
     *
     * @param opaque whether visual data provided by this painter is opaque or not
     */
    public void setOpaque ( final Boolean opaque )
    {
        this.opaque = opaque;
        repaint ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getPreferredSize ( final E c )
    {
        return preferredSize;
    }

    /**
     * Sets preferred size for visual data provided by this painter.
     *
     * @param preferredSize preferred size for visual data provided by this painter
     */
    public void setPreferredSize ( final Dimension preferredSize )
    {
        this.preferredSize = preferredSize;
        revalidate ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Insets getMargin ( final E c )
    {
        return margin;
    }

    /**
     * Sets margin required for visual data provided by this painter.
     *
     * @param margin margin required for visual data provided by this painter
     */
    public void setMargin ( final Insets margin )
    {
        this.margin = margin;
        revalidate ();
    }

    /**
     * Sets margin required for visual data provided by this painter.
     *
     * @param top    top margin required for visual data provided by this painter
     * @param left   left margin required for visual data provided by this painter
     * @param bottom bottom margin required for visual data provided by this painter
     * @param right  right margin required for visual data provided by this painter
     */
    public void setMargin ( final int top, final int left, final int bottom, final int right )
    {
        setMargin ( new Insets ( top, left, bottom, right ) );
    }

    /**
     * Sets margin required for visual data provided by this painter.
     *
     * @param margin margin required for visual data provided by this painter
     */
    public void setMargin ( final int margin )
    {
        setMargin ( margin, margin, margin, margin );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPainterListener ( final PainterListener listener )
    {
        listeners.add ( listener );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removePainterListener ( final PainterListener listener )
    {
        listeners.remove ( listener );
    }

    /**
     * Should be called when painter visual representation changes.
     */
    public void repaint ()
    {
        for ( final PainterListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.repaint ();
        }
    }

    /**
     * Should be called when part of painter visual representation changes.
     *
     * @param bounds part bounds
     */
    public void repaint ( final Rectangle bounds )
    {
        repaint ( bounds.x, bounds.y, bounds.width, bounds.height );
    }

    /**
     * Should be called when part of painter visual representation changes.
     *
     * @param x      part bounds X coordinate
     * @param y      part bounds Y coordinate
     * @param width  part bounds width
     * @param height part bounds height
     */
    public void repaint ( final int x, final int y, final int width, final int height )
    {
        for ( final PainterListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.repaint ( x, y, width, height );
        }
    }

    /**
     * Should be called when painter size or border changes.
     */
    public void revalidate ()
    {
        for ( final PainterListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.revalidate ();
        }
    }

    /**
     * Should be called when painter opacity changes.
     */
    public void updateOpacity ()
    {
        for ( final PainterListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.updateOpacity ();
        }
    }

    /**
     * Should be called when painter size, border and visual representation changes.
     * Calls both revalidate and update listener methods.
     */
    public void updateAll ()
    {
        for ( final PainterListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.updateOpacity ();
            listener.revalidate ();
            listener.repaint ();
        }
    }

    /**
     * Returns point for the specified coordinates.
     * Might be useful for points generation in various cases
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @return point for the specified coordinates
     */
    protected Point p ( final int x, final int y )
    {
        return new Point ( x, y );
    }
}