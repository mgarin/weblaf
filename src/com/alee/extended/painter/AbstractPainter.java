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

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This abstract painter provides a few additional features.
 * Usually this class is extended by various painters instead of Painter interface.
 *
 * @param <E> component type
 * @author Mikle Garin
 * @see Painter
 */

public abstract class AbstractPainter<E extends Component> implements Painter<E>
{
    /**
     * Whether visual data is opaque or not.
     */
    protected boolean opaque = false;

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
    public boolean isOpaque ( final E c )
    {
        return opaque;
    }

    /**
     * Sets whether visual data provided by this painter is opaque or not.
     *
     * @param opaque whether visual data provided by this painter is opaque or not
     */
    public void setOpaque ( final boolean opaque )
    {
        this.opaque = opaque;
        fireRepaint ();
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
        fireRevalidate ();
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
        fireRevalidate ();
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
     * Fired when painter size and visual representation changes.
     * Calls both revalidate and update listener methods.
     */
    public void fireUpdate ()
    {
        for ( final PainterListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.revalidate ();
            listener.repaint ();
        }
    }

    /**
     * Fired when painter visual representation changes.
     */
    public void fireRepaint ()
    {
        for ( final PainterListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.repaint ();
        }
    }

    /**
     * Fired when painter size changes.
     */
    public void fireRevalidate ()
    {
        for ( final PainterListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.revalidate ();
        }
    }
}