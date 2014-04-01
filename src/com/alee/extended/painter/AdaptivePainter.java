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

import javax.swing.*;
import java.awt.*;

/**
 * Special painter made to adapts any kind of painters to fit custom painters within the specific UIs.
 * To use it properly you should extend this class and implement UI painter interface methods.
 * In general cases those methods might have no effect since general-type painters do not know anything about component specifics.
 *
 * @author Mikle Garin
 */

public abstract class AdaptivePainter<E extends JComponent> extends AbstractPainter<E>
{
    /**
     * Adapted painter.
     */
    private final Painter painter;

    /**
     * Constructs new AdaptivePainter to adapt specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptivePainter ( final Painter painter )
    {
        super ();
        this.painter = painter;
    }

    /**
     * Returns adapted painter.
     *
     * @return adapted painter
     */
    public Painter getPainter ()
    {
        return painter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void install ( final E c )
    {
        painter.install ( c );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void uninstall ( final E c )
    {
        painter.uninstall ( c );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isOpaque ( final E c )
    {
        return painter.isOpaque ( c );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getPreferredSize ( final E c )
    {
        return painter.getPreferredSize ( c );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Insets getMargin ( final E c )
    {
        return painter.getMargin ( c );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPainterListener ( final PainterListener listener )
    {
        painter.addPainterListener ( listener );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removePainterListener ( final PainterListener listener )
    {
        painter.removePainterListener ( listener );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c )
    {
        painter.paint ( g2d, bounds, c );
    }
}