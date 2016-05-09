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

package com.alee.extended.window;

import com.alee.laf.menu.IPopupPainter;
import com.alee.laf.rootpane.IRootPanePainter;
import com.alee.laf.rootpane.WebRootPaneUI;
import com.alee.utils.swing.DataProvider;

import javax.swing.*;
import java.awt.*;

/**
 * Base interface for WebPopOver component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Mikle Garin
 */

public interface IPopOverPainter<E extends JRootPane, U extends WebRootPaneUI> extends IRootPanePainter<E, U>, IPopupPainter<E, U>
{
    /**
     * Configures popover to be displayed as unattached at the specified screen location.
     *
     * @param popOver  popover to configure
     * @param location location on screen
     */
    public void configure ( final WebPopOver popOver, final PopOverLocation location );

    /**
     * Configures popover to be displayed as unattached at the specified location.
     *
     * @param popOver popover to configure
     * @param x       X location
     * @param y       Y location
     */
    public void configure ( final WebPopOver popOver, final int x, final int y );

    /**
     * Configures popover to be displayed as attached to the invoker component and faced to specified direction.
     * Popover will also be using the specified alignment type when it possible according to its position on the screen.
     * Popover opened in this way will always auto-follow invoker's ancestor window on the screen when it is moved.
     *
     * @param popOver        popover to configure
     * @param invoker        invoker component
     * @param boundsProvider source area provider
     * @param direction      preferred display direction
     * @param alignment      preferred display alignment
     */
    public void configure ( final WebPopOver popOver, final Component invoker, final DataProvider<Rectangle> boundsProvider,
                            final PopOverDirection direction, final PopOverAlignment alignment );
}