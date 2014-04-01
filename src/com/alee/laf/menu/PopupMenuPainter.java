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

public interface PopupMenuPainter<E extends JPopupMenu> extends Painter<E>, SpecificPainter
{
    /**
     * Sets whether popup menu is transparent or not.
     * This mark is updated only once per component as it is initialized on UI initialization only.
     *
     * @param transparent whether popup menu is transparent or not
     */
    public void setTransparent ( boolean transparent );

    /**
     * Sets spacing between popup menus.
     *
     * @param spacing spacing between popup menus
     */
    public void setMenuSpacing ( int spacing );

    /**
     * Sets whether should fix initial popup menu location or not.
     * If set to true popup menu will try to use best possible location to show up.
     * <p/>
     * This is set to true by default to place menubar and menu popups correctly.
     * You might want to set this to false for some specific popup menu, but not all of them at once.
     *
     * @param fix whether should fix initial popup menu location or not
     */
    public void setFixLocation ( boolean fix );

    /**
     * Sets preferred popup menu display way.
     * This value is updated right before preparePopupMenu method call.
     *
     * @param way preferred popup menu display way
     */
    public void setPopupMenuWay ( PopupMenuWay way );

    /**
     * Sets popup menu type.
     * This value is updated right before popup menu window becomes visible.
     * You can use it to draw different popup menu decoration for each popup menu type.
     *
     * @param type popup menu type
     */
    public void setPopupMenuType ( final PopupMenuType type );

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