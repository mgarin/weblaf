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

import com.alee.api.jdk.Supplier;
import com.alee.laf.rootpane.IRootPanePainter;
import com.alee.laf.rootpane.WRootPaneUI;

import javax.swing.*;
import java.awt.*;

/**
 * Base interface for {@link WebPopOver} component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Mikle Garin
 */

public interface IPopOverPainter<C extends JRootPane, U extends WRootPaneUI> extends IRootPanePainter<C, U>
{
    /**
     * Configures popover to be displayed as unattached at the specified screen location.
     *
     * @param popOver  popover to configure
     * @param location location on screen
     */
    public void configure ( WebPopOver popOver, PopOverLocation location );

    /**
     * Configures popover to be displayed as unattached at the specified location.
     *
     * @param popOver popover to configure
     * @param x       X location
     * @param y       Y location
     */
    public void configure ( WebPopOver popOver, int x, int y );

    /**
     * Configures popover to be displayed as attached to the invoker component and faced to specified direction.
     * Popover will also be using the specified alignment type when it possible according to its position on the screen.
     * Popover opened in this way will always auto-follow invoker's ancestor window on the screen when it is moved.
     *
     * @param popOver        popover to configure
     * @param invoker        invoker component
     * @param boundsSupplier source area provider
     * @param direction      preferred display direction
     * @param alignment      preferred display alignment
     */
    public void configure ( WebPopOver popOver, Component invoker, Supplier<Rectangle> boundsSupplier,
                            PopOverDirection direction, PopOverAlignment alignment );
}