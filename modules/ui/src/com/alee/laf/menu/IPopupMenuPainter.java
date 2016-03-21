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

import javax.swing.*;
import java.awt.*;

/**
 * Base interface for JPopupMenu component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Mikle Garin
 */

public interface IPopupMenuPainter<E extends JPopupMenu, U extends WebPopupMenuUI> extends IPopupPainter<E, U>
{
    /**
     * Prepares popup menu to be displayed.
     * This method is called before the popup for menu is created.
     * Returned coordinate will be used to display new popup for this menu.
     *
     * @param popupMenu JPopupMenu to prepare for display
     * @param invoker   popup menu invoker
     * @param x         screen x location actual popup is to be shown at
     * @param y         screen y location actual popup is to be shown at
     * @return modified popup display location
     */
    public Point preparePopupMenu ( E popupMenu, Component invoker, int x, int y );

    /**
     * Configures created popup to be displayed.
     * This method is called before the popup with menu is displayed.
     *
     * @param popupMenu JPopupMenu to prepare for display
     * @param invoker   popup menu invoker
     * @param x         screen x location actual popup is to be shown at
     * @param y         screen y location actual popup is to be shown at
     * @param popup     popup to be configured
     */
    public void configurePopup ( E popupMenu, Component invoker, int x, int y, Popup popup );
}