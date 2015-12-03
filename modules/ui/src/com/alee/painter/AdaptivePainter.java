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

package com.alee.painter;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * Special painter made to adapts any kind of painters to fit custom painters within the specific UIs.
 * To use it properly you should extend this class and implement UI painter interface methods.
 * In general cases those methods might have no effect since general-type painters do not know anything about component specifics.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Mikle Garin
 */

public abstract class AdaptivePainter<E extends JComponent, U extends ComponentUI> extends AbstractPainter<E, U>
        implements SpecificPainter<E, U>
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

    @Override
    public void install ( final E c, final U ui )
    {
        painter.install ( c, ui );
    }

    @Override
    public void uninstall ( final E c, final U ui )
    {
        painter.uninstall ( c, ui );
    }

    @Override
    public Boolean isOpaque ()
    {
        return painter.isOpaque ();
    }

    @Override
    public Insets getBorders ()
    {
        return painter.getBorders ();
    }

    @Override
    public void addPainterListener ( final PainterListener listener )
    {
        painter.addPainterListener ( listener );
    }

    @Override
    public void removePainterListener ( final PainterListener listener )
    {
        painter.removePainterListener ( listener );
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        painter.paint ( g2d, bounds, c, ui );
    }

    @Override
    public Dimension getPreferredSize ()
    {
        return painter.getPreferredSize ();
    }
}