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

package com.alee.laf.rootpane;

import javax.swing.*;
import javax.swing.plaf.basic.BasicRootPaneUI;
import java.awt.*;

/**
 * Pluggable look and feel interface for {@link WebRootPane} component.
 *
 * @author Mikle Garin
 */
public abstract class WRootPaneUI extends BasicRootPaneUI
{
    /**
     * Returns whether or not this {@link WRootPaneUI} provides custom window decoration.
     *
     * @return {@code true} if this {@link WRootPaneUI} provides custom window decoration, {@code false} otherwise
     */
    public abstract boolean isDecorated ();

    /**
     * Installs window decorations.
     */
    public abstract void installWindowDecorations ();

    /**
     * Uninstalls window decorations.
     */
    public abstract void uninstallWindowDecorations ();

    /**
     * Returns whether or not window title component should be displayed.
     *
     * @return true if window title component should be displayed, false otherwise
     */
    public abstract boolean isDisplayTitleComponent ();

    /**
     * Sets whether or not window title component should be displayed.
     *
     * @param display whether or not window title component should be displayed
     */
    public abstract void setDisplayTitleComponent ( boolean display );

    /**
     * Returns window title component.
     *
     * @return window title component
     */
    public abstract JComponent getTitleComponent ();

    /**
     * Sets window title component.
     *
     * @param title new window title component
     */
    public abstract void setTitleComponent ( JComponent title );

    /**
     * Returns whether or not window buttons should be displayed.
     *
     * @return true if window buttons should be displayed, false otherwise
     */
    public abstract boolean isDisplayWindowButtons ();

    /**
     * Sets whether or not window buttons should be displayed.
     *
     * @param display whether or not window buttons should be displayed
     */
    public abstract void setDisplayWindowButtons ( boolean display );

    /**
     * Returns whether or not window minimize button should be displayed.
     *
     * @return true if window minimize button should be displayed, false otherwise
     */
    public abstract boolean isDisplayMinimizeButton ();

    /**
     * Sets whether or not window minimize button should be displayed.
     *
     * @param display whether or not window minimize button should be displayed
     */
    public abstract void setDisplayMinimizeButton ( boolean display );

    /**
     * Returns whether or not window maximize button should be displayed.
     *
     * @return true if window maximize button should be displayed, false otherwise
     */
    public abstract boolean isDisplayMaximizeButton ();

    /**
     * Sets whether or not window maximize button should be displayed.
     *
     * @param display whether or not window maximize button should be displayed
     */
    public abstract void setDisplayMaximizeButton ( boolean display );

    /**
     * Returns whether or not window close button should be displayed.
     *
     * @return true if window close button should be displayed, false otherwise
     */
    public abstract boolean isDisplayCloseButton ();

    /**
     * Sets whether or not window close button should be displayed.
     *
     * @param display whether or not window close button should be displayed
     */
    public abstract void setDisplayCloseButton ( boolean display );

    /**
     * Returns window buttons panel.
     *
     * @return window buttons panel
     */
    public abstract JComponent getButtonsPanel ();

    /**
     * Returns whether or not menu bar should be displayed.
     *
     * @return true if menu bar should be displayed, false otherwise
     */
    public abstract boolean isDisplayMenuBar ();

    /**
     * Sets whether or not menu bar should be displayed.
     *
     * @param display whether or not menu bar should be displayed
     */
    public abstract void setDisplayMenuBar ( boolean display );

    /**
     * Returns whether or not window is resizable.
     *
     * @return true if window is resizable, false otherwise
     */
    protected abstract boolean isResizable ();

    /**
     * Returns whether or not window this root pane is attached to is maximized.
     *
     * @return true if window this root pane is attached to is maximized, false otherwise
     */
    public abstract boolean isIconified ();

    /**
     * Returns whether or not window this root pane is attached to is maximized.
     *
     * @return true if window this root pane is attached to is maximized, false otherwise
     */
    public abstract boolean isMaximized ();

    /**
     * Returns window for the root pane this UI is applied to.
     *
     * @return window for the root pane this UI is applied to
     */
    public abstract Window getWindow ();

    /**
     * Returns whether or not this root pane is attached to frame.
     *
     * @return true if this root pane is attached to frame, false otherwise
     */
    public abstract boolean isFrame ();

    /**
     * Returns whether or not this root pane is attached to dialog.
     *
     * @return true if this root pane is attached to dialog, false otherwise
     */
    public abstract boolean isDialog ();

    /**
     * Closes the Window.
     */
    public abstract void close ();

    /**
     * Iconifies the Frame.
     */
    public abstract void iconify ();

    /**
     * Maximizes the Frame.
     */
    public abstract void maximize ();

    /**
     * Maximizes the Frame to the west (left) half of the screen.
     * todo Unfortunately MAXIMIZED_VERT state seems not to be supported anywhere
     */
    public abstract void maximizeWest ();

    /**
     * Maximizes the Frame to the east (right) half of the screen.
     * todo Unfortunately MAXIMIZED_VERT state seems not to be supported anywhere
     */
    public abstract void maximizeEast ();

    /**
     * Restores the Frame size.
     */
    public abstract void restore ();
}