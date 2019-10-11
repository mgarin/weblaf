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

package com.alee.demo.frames.inspector;

import com.alee.api.annotations.NotNull;
import com.alee.api.data.CompassDirection;
import com.alee.demo.DemoApplication;
import com.alee.demo.skin.DemoIcons;
import com.alee.extended.dock.DockableFrameState;
import com.alee.extended.dock.WebDockableFrame;
import com.alee.extended.inspector.InterfaceInspector;
import com.alee.managers.style.StyleId;

/**
 * {@link DemoApplication} inspector frame.
 *
 * @author Mikle Garin
 */
public final class InspectorFrame extends WebDockableFrame
{
    /**
     * Frame ID.
     */
    public static final String ID = "demo.inspector";

    /**
     * Constructs examples frame.
     *
     * @param application demo application
     */
    public InspectorFrame ( @NotNull final DemoApplication application )
    {
        super ( StyleId.dockableframeCompact, ID, DemoIcons.inspector16, "demo.inspector.title" );
        setState ( DockableFrameState.minimized );
        setPosition ( CompassDirection.east );
        setPreferredSize ( 300, 200 );

        final InterfaceInspector inspector = new InterfaceInspector ();
        add ( inspector );
    }
}