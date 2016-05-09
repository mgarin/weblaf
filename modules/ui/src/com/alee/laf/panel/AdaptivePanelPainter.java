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

package com.alee.laf.panel;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple PanelPainter adapter class.
 * It is used to install simple non-specific painters into WebPanelUI.
 *
 * @author Mikle Garin
 */

public final class AdaptivePanelPainter<E extends JPanel, U extends WebPanelUI> extends AdaptivePainter<E, U> implements IPanelPainter<E, U>
{
    /**
     * Constructs new AdaptivePanelPainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptivePanelPainter ( final Painter painter )
    {
        super ( painter );
    }
}