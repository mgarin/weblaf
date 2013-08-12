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

import java.awt.*;

/**
 * Default painter implementation that provides a few additional features.
 * Usually this class and not Painter interface is extended by various painters since it already has basic method implementations.
 *
 * @param <E> component type
 * @author Mikle Garin
 * @see DefaultPainter
 * @since 1.4
 */

public abstract class DefaultPainter<E extends Component> implements Painter<E>
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
     * {@inheritDoc}
     */
    public boolean isOpaque ( E c )
    {
        return opaque;
    }

    /**
     * Sets whether visual data provided by this painter is opaque or not.
     *
     * @param opaque whether visual data provided by this painter is opaque or not
     */
    public void setOpaque ( boolean opaque )
    {
        this.opaque = opaque;
    }

    /**
     * {@inheritDoc}
     */
    public Dimension getPreferredSize ( E c )
    {
        return preferredSize;
    }

    /**
     * Sets preferred size for visual data provided by this painter.
     *
     * @param preferredSize preferred size for visual data provided by this painter
     */
    public void setPreferredSize ( Dimension preferredSize )
    {
        this.preferredSize = preferredSize;
    }

    /**
     * {@inheritDoc}
     */
    public Insets getMargin ( E c )
    {
        return margin;
    }

    /**
     * Sets margin required for visual data provided by this painter.
     *
     * @param margin margin required for visual data provided by this painter
     */
    public void setMargin ( Insets margin )
    {
        this.margin = margin;
    }

    /**
     * Sets margin required for visual data provided by this painter.
     *
     * @param top    top margin required for visual data provided by this painter
     * @param left   left margin required for visual data provided by this painter
     * @param bottom bottom margin required for visual data provided by this painter
     * @param right  right margin required for visual data provided by this painter
     */
    public void setMargin ( int top, int left, int bottom, int right )
    {
        setMargin ( new Insets ( top, left, bottom, right ) );
    }

    /**
     * Sets margin required for visual data provided by this painter.
     *
     * @param margin margin required for visual data provided by this painter
     */
    public void setMargin ( int margin )
    {
        setMargin ( margin, margin, margin, margin );
    }
}