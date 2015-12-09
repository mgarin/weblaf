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

package com.alee.laf.menu;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;
import java.awt.*;

/**
 * Simple PopupMenuPainter adapter class.
 * It is used to install simple non-specific painters into WebPopupMenuUI.
 *
 * @author Mikle Garin
 */

public final class AdaptivePopupMenuPainter<E extends JPopupMenu, U extends WebPopupMenuUI> extends AdaptivePainter<E, U>
        implements IPopupMenuPainter<E, U>
{
    /**
     * Constructs new AdaptiveLabelPainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptivePopupMenuPainter ( final Painter painter )
    {
        super ( painter );
    }

    @Override
    public Point preparePopupMenu ( final E popupMenu, final Component invoker, final int x, final int y )
    {
        return p ( x, y );
    }

    @Override
    public void configurePopup ( final E popupMenu, final Component invoker, final int x, final int y, final Popup popup )
    {
        // Do nothing by default
    }
}