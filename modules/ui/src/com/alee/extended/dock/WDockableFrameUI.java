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

package com.alee.extended.dock;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * Pluggable look and feel interface for {@link WebDockableFrame} component.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDockablePane">How to use WebDockablePane</a>
 * @see WebDockablePane
 */

public abstract class WDockableFrameUI extends ComponentUI
{
    /**
     * Returns sidebar button for this frame.
     * Sidebar button usually acts as a frame tab on the sidebar.
     *
     * @return sidebar button for this frame
     */
    public abstract JComponent getSidebarButton ();

    /**
     * Returns minimum frame dialog size.
     *
     * @return minimum frame dialog size
     */
    public abstract Dimension getMinimumDialogSize ();
}