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

package com.alee.extended.dock.drag;

import com.alee.extended.dock.WebDockableFrame;
import com.alee.extended.dock.WebDockablePane;
import com.alee.managers.drag.DragException;
import com.alee.managers.drag.view.ComponentDragViewHandler;
import com.alee.utils.SwingUtils;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.image.BufferedImage;
import java.lang.ref.WeakReference;

/**
 * Custom {@link ComponentDragViewHandler} for {@link WebDockableFrame} document.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDockablePane">How to use WebDockablePane</a>
 * @see WebDockableFrame
 * @see com.alee.extended.dock.WebDockablePane
 * @see com.alee.managers.drag.DragManager
 */
public class FrameDragViewHandler extends ComponentDragViewHandler<WebDockableFrame, FrameDragData>
{
    /**
     * {@link WeakReference} to {@link com.alee.extended.dock.WebDockablePane} using this drag view handler.
     */
    protected final WeakReference<WebDockablePane> dockablePane;

    /**
     * Constructs new {@link com.alee.extended.dock.drag.FrameDragViewHandler}.
     *
     * @param dockablePane {@link com.alee.extended.dock.WebDockablePane} using this drag view handler
     */
    public FrameDragViewHandler ( final WebDockablePane dockablePane )
    {
        super ();
        this.dockablePane = new WeakReference<WebDockablePane> ( dockablePane );
    }

    @Override
    public DataFlavor getObjectFlavor ()
    {
        return FrameTransferable.dataFlavor;
    }

    @Override
    public boolean supports ( final FrameDragData object, final DragSourceDragEvent event )
    {
        return dockablePane.get ().getFrame ( object.getId () ) != null;
    }

    @Override
    public WebDockableFrame getComponent ( final FrameDragData object, final DragSourceDragEvent event )
    {
        final WebDockablePane pane = dockablePane.get ();
        if ( pane == null )
        {
            final String msg = "Unable to resolve WebDockablePane for dragged frame with identifier: %s";
            throw new DragException ( String.format ( msg, object.getId () ) );
        }
        return pane.getFrame ( object.getId () );
    }

    @Override
    protected Point calculateViewRelativeLocation ( final WebDockableFrame frame, final DragSourceDragEvent event )
    {
        final Point location = super.calculateViewRelativeLocation ( frame, event );
        final WebDockablePane pane = frame.getDockablePane ();
        final Dimension size = pane.getModel ().getElement ( frame.getId () ).getSize ();
        final Dimension current = frame.getSize ();
        if ( current.width > size.width && Math.abs ( location.x ) > size.width )
        {
            location.x = location.x * size.width / current.width;
        }
        return location;
    }

    @Override
    protected BufferedImage createComponentView ( final WebDockableFrame frame )
    {
        final WebDockablePane pane = frame.getDockablePane ();
        final Dimension size = pane.getModel ().getElement ( frame.getId () ).getSize ();
        return SwingUtils.createComponentSnapshot ( frame, size.width, size.height, getSnapshotOpacity () );
    }
}