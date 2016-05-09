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

package com.alee.painter.decoration.background;

import com.alee.painter.decoration.IDecoration;
import com.alee.utils.CompareUtils;
import com.alee.utils.GraphicsUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract texure background.
 *
 * @param <E> component type
 * @param <D> decoration type
 * @param <I> background type
 * @author Mikle Garin
 */

public abstract class AbstractTextureBackground<E extends JComponent, D extends IDecoration<E, D>, I extends AbstractTextureBackground<E, D, I>>
        extends AbstractBackground<E, D, I>
{
    /**
     * Cached texture paint.
     * It is cached for performance reasons.
     * You can clean the cache at any time in implementation of this abstract class.
     */
    protected transient TexturePaint paint = null;

    /**
     * Cached texture bounds.
     */
    protected transient Rectangle bounds = null;

    /**
     * Resets texture cache.
     */
    public void clearCache ()
    {
        paint = null;
        bounds = null;
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final D d, final Shape shape )
    {
        final float opacity = getOpacity ();
        if ( opacity > 0 )
        {
            // Checking texture state
            if ( isPaintable () )
            {
                // Updating cached texture paint
                final Rectangle b = shape.getBounds ();
                if ( paint == null || !CompareUtils.equals ( this.bounds, b ) )
                {
                    paint = getTexturePaint ( b );
                }

                // Do not paint anything if texture paint was not set
                if ( paint != null )
                {
                    final Composite oc = GraphicsUtils.setupAlphaComposite ( g2d, opacity, opacity < 1f );
                    final Paint op = GraphicsUtils.setupPaint ( g2d, paint );

                    g2d.setPaint ( paint );
                    g2d.fill ( shape );

                    GraphicsUtils.restorePaint ( g2d, op );
                    GraphicsUtils.restoreComposite ( g2d, oc, opacity < 1f );
                }
            }
        }
    }

    /**
     * Returns whether or not texture background is paintable right now.
     *
     * @return true if texture background is paintable right now, false otherwise
     */
    protected abstract boolean isPaintable ();

    /**
     * Returns texture paint implementation.
     *
     * @param bounds painting bounds
     * @return texture paint implementation
     */
    protected abstract TexturePaint getTexturePaint ( Rectangle bounds );
}