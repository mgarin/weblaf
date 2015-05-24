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

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.SpecificPainter;

import javax.swing.*;
import java.awt.*;

/**
 * Base interface for JPopupMenu component painters.
 *
 * @author Mikle Garin
 */

public interface PopupMenuPainter<E extends JPopupMenu, U extends WebPopupMenuUI> extends Painter<E, U>, SpecificPainter
{
    /**
     * Prepares popup menu to be displayed.
     *
     * @param popupMenu JPopupMenu to prepare for display
     * @param x         screen x location actual popup is to be shown at
     * @param y         screen y location actual popup is to be shown at
     * @return modified popup display location
     */
    public Point preparePopupMenu ( E popupMenu, Component invoker, int x, int y );
}