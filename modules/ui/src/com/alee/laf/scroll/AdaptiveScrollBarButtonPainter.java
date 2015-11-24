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

import com.alee.laf.button.WebButtonUI;
import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple ScrollBarButtonPainter adapter class.
 * It is used to install simple non-specific button painters into WebScrollBarUI.
 *
 * @author Mikle Garin
 */

public final class AdaptiveScrollBarButtonPainter<E extends JButton, U extends WebButtonUI> extends AdaptivePainter<E, U>
        implements ScrollBarButtonPainter<E, U>
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
}