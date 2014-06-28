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

import com.alee.extended.painter.AdaptivePainter;
import com.alee.extended.painter.Painter;

import javax.swing.*;

/**
 * Simple ScrollBarButtonPainter adapter class.
 * It is used to install simple non-specific button painters into WebScrollBarUI.
 *
 * @author Mikle Garin
 */

public class AdaptiveScrollBarButtonPainter<E extends AbstractButton> extends AdaptivePainter<E> implements ScrollBarButtonPainter<E>
{
    /**
     * Constructs new AdaptiveScrollBarButtonPainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveScrollBarButtonPainter ( final Painter painter )
    {
        super ( painter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setButtonType ( final ScrollBarButtonType type )
    {
        // Ignore this method in adaptive class
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setScrollbar ( final JScrollBar scrollbar )
    {
        // Ignore this method in adaptive class
    }
}