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

package com.alee.laf.scroll;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;
import java.awt.*;

/**
 * Simple ScrollBarPainter adapter class.
 * It is used to install simple non-specific painters into WebScrollBarUI.
 *
 * @author Mikle Garin
 */

public final class AdaptiveScrollBarPainter<E extends JScrollBar, U extends WebScrollBarUI> extends AdaptivePainter<E, U>
        implements IScrollBarPainter<E, U>
{
    /**
     * Constructs new AdaptiveScrollBarPainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveScrollBarPainter ( final Painter painter )
    {
        super ( painter );
    }

    @Override
    public void setDragged ( final boolean dragged )
    {
        // Ignore this method in adaptive class
    }

    @Override
    public void setTrackBounds ( final Rectangle bounds )
    {
        // Ignore this method in adaptive class
    }

    @Override
    public void setThumbBounds ( final Rectangle bounds )
    {
        // Ignore this method in adaptive class
    }
}