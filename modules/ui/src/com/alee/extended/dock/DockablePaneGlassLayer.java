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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.extended.behavior.VisibilityBehavior;
import com.alee.extended.dock.data.DockableElement;
import com.alee.extended.dock.data.ResizeData;
import com.alee.extended.dock.drag.FrameDragData;
import com.alee.extended.dock.drag.FrameDragViewHandler;
import com.alee.extended.dock.drag.FrameDropData;
import com.alee.extended.dock.drag.FrameTransferable;
import com.alee.managers.drag.DragAdapter;
import com.alee.managers.drag.DragManager;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.ProprietaryUtils;
import com.alee.utils.SwingUtils;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Custom glass layer for {@link WebDockablePane}.
 * Unlike {@link JRootPane} glass layer it only covers dockable pane itself.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDockablePane">How to use WebDockablePane</a>
 * @see WebDockablePane
 */
public class DockablePaneGlassLayer extends JComponent
{
    /**
     * todo 1. Maybe create new WebGlassLayer component with its own UI/painter with customizable multi subpainters?
     */

    /**
     * {@link WebDockablePane} this glass pane is attached to.
     */
    @NotNull
    protected final WebDockablePane dockablePane;

    /**
     * {@link FrameDropData} containing information about dragged frame.
     */
    @Nullable
    protected FrameDropData frameDropData;

    /**
     * {@link ResizeData} containting information about resized elements.
     */
    @Nullable
    protected ResizeData resizeData;

    /**
     * Constructs new dockable pane glass layer.
     *
     * @param dockablePane {@link WebDockablePane}
     */
    public DockablePaneGlassLayer ( @NotNull final WebDockablePane dockablePane )
    {
        super ();
        this.dockablePane = dockablePane;

        // Visual settings
        setOpaque ( false );

        // Ensure our glass layer doesn't interfere with AWT and opaque components
        ProprietaryUtils.enableMixingCutoutShape ( this );

        // Resize listener
        final MouseAdapter mouseListener = new MouseAdapter ()
        {
            /**
             * Resize start {@link Point}.
             */
            @Nullable
            private Point initialPoint = null;

            /**
             * Left resized {@link DockableElement}.
             */
            @Nullable
            private DockableElement left = null;

            /**
             * Right resized {@link DockableElement}.
             */
            @Nullable
            private DockableElement right = null;

            /**
             * Inital left {@link DockableElement} size.
             */
            @Nullable
            private Dimension initialLeftSize = null;

            /**
             * Inital right {@link DockableElement} size.
             */
            @Nullable
            private Dimension initialRightSize = null;

            @Override
            public void mousePressed ( @NotNull final MouseEvent e )
            {
                if ( SwingUtils.isLeftMouseButton ( e ) )
                {
                    initialPoint = e.getPoint ();
                    resizeData = getResizeData ( e.getX (), e.getY () );
                    if ( resizeData != null )
                    {
                        left = dockablePane.getModel ().getElement ( resizeData.leftElementId () );
                        right = dockablePane.getModel ().getElement ( resizeData.rightElementId () );

                        final Rectangle leftBounds = left.getBounds ();
                        if ( leftBounds != null )
                        {
                            initialLeftSize = leftBounds.getSize ();
                        }
                        else
                        {
                            final String leftId = resizeData.leftElementId ();
                            cleanupResizeVariables ();
                            throw new RuntimeException ( "Unable to retrieve left element bounds: " + leftId );
                        }

                        final Rectangle rightBounds = right.getBounds ();
                        if ( rightBounds != null )
                        {
                            initialRightSize = rightBounds.getSize ();
                        }
                        else
                        {
                            final String rightId = resizeData.rightElementId ();
                            cleanupResizeVariables ();
                            throw new RuntimeException ( "Unable to retrieve bounds: " + rightId );
                        }
                    }
                    else
                    {
                        cleanupResizeVariables ();
                    }
                }
            }

            @Override
            public void mouseDragged ( @NotNull final MouseEvent e )
            {
                if ( initialPoint != null && resizeData != null && left != null && right != null &&
                        initialLeftSize != null && initialRightSize != null )
                {
                    // Resizing elements
                    final Dimension minLeft = left.getMinimumSize ( dockablePane );
                    final Dimension minRight = right.getMinimumSize ( dockablePane );
                    if ( resizeData.orientation ().isHorizontal () )
                    {
                        final int m = e.getX () - initialPoint.x;
                        final int change = m < 0 ? Math.max ( minLeft.width - initialLeftSize.width, m ) :
                                Math.min ( initialRightSize.width - minRight.width, m );
                        if ( !left.isContent () )
                        {
                            left.setSize ( new Dimension ( initialLeftSize.width + change, left.getSize ().height ) );
                        }
                        if ( !right.isContent () )
                        {
                            right.setSize ( new Dimension ( initialRightSize.width - change, right.getSize ().height ) );
                        }
                    }
                    else
                    {
                        final int m = e.getY () - initialPoint.y;
                        final int change = m < 0 ? Math.max ( minLeft.height - initialLeftSize.height, m ) :
                                Math.min ( initialRightSize.height - minRight.height, m );
                        if ( !left.isContent () )
                        {
                            left.setSize ( new Dimension ( left.getSize ().width, initialLeftSize.height + change ) );
                        }
                        if ( !right.isContent () )
                        {
                            right.setSize ( new Dimension ( right.getSize ().width, initialRightSize.height - change ) );
                        }
                    }

                    // Updatick dockable layout
                    dockablePane.revalidate ();
                }
            }

            @Override
            public void mouseReleased ( @NotNull final MouseEvent e )
            {
                if ( SwingUtils.isLeftMouseButton ( e ) && resizeData != null )
                {
                    cleanupResizeVariables ();
                }
            }

            /**
             * Cleans up resize variables.
             */
            private void cleanupResizeVariables ()
            {
                initialPoint = null;
                resizeData = null;
                left = null;
                right = null;
            }

            @Override
            public void mouseMoved ( @NotNull final MouseEvent e )
            {
                final ResizeData data = getResizeData ( e.getX (), e.getY () );
                if ( data != null )
                {
                    // todo Decide cursor direction based on resized frames?
                    if ( data.orientation ().isVertical () )
                    {
                        DockablePaneGlassLayer.this.setCursor ( Cursor.getPredefinedCursor ( Cursor.S_RESIZE_CURSOR ) );
                    }
                    else
                    {
                        DockablePaneGlassLayer.this.setCursor ( Cursor.getPredefinedCursor ( Cursor.E_RESIZE_CURSOR ) );
                    }
                }
                else
                {
                    DockablePaneGlassLayer.this.setCursor ( Cursor.getDefaultCursor () );
                }
            }
        };
        addMouseListener ( mouseListener );
        addMouseMotionListener ( mouseListener );

        // Frame drop handler
        setTransferHandler ( new TransferHandler ()
        {
            @Override
            public boolean canImport ( @NotNull final TransferSupport support )
            {
                final FrameDropData old = frameDropData;
                frameDropData = support.isDataFlavorSupported ( FrameTransferable.dataFlavor ) ?
                        dockablePane.getModel ().dropData ( dockablePane, support ) : null;
                if ( old != frameDropData )
                {
                    repaint ();
                }
                return frameDropData != null;
            }

            @Override
            public boolean importData ( @NotNull final TransferSupport support )
            {
                boolean imported;
                try
                {
                    imported = dockablePane.getModel ().drop ( dockablePane, support );
                }
                catch ( final Exception e )
                {
                    LoggerFactory.getLogger ( CoreSwingUtils.class ).error ( "Unable to complete drop operation", e );
                    imported = false;
                }
                return imported;
            }
        } );

        // Drag listener
        final DragAdapter dragListener = new DragAdapter ()
        {
            @Override
            public void exited ( @NotNull final DragSourceEvent event )
            {
                clearDropLocation ();
            }

            @Override
            public void finished ( @NotNull final DragSourceDropEvent event )
            {
                clearDropLocation ();
            }

            /**
             * Clears frame drop location data.
             */
            private void clearDropLocation ()
            {
                if ( frameDropData != null )
                {
                    frameDropData = null;
                    dockablePane.repaint ();
                }
            }
        };

        // Drag view handler
        final FrameDragViewHandler dragViewHandler = new FrameDragViewHandler ( dockablePane );

        // Visibility listener
        new VisibilityBehavior<DockablePaneGlassLayer> ( this )
        {
            @Override
            protected void displayed ( @NotNull final DockablePaneGlassLayer component )
            {
                DragManager.addDragListener ( component, dragListener );
                DragManager.registerViewHandler ( dragViewHandler );
            }

            @Override
            protected void hidden ( @NotNull final DockablePaneGlassLayer component )
            {
                DragManager.unregisterViewHandler ( dragViewHandler );
                DragManager.removeDragListener ( component, dragListener );
            }
        }.install ();
    }

    @Override
    protected void paintComponent ( @NotNull final Graphics g )
    {
        if ( frameDropData != null )
        {
            final Graphics2D g2d = ( Graphics2D ) g;
            final int sw = 3;
            final int delta = 1; // sw % 2 != 0 ? 1 : 0;
            final int hf = ( int ) Math.round ( Math.floor ( sw / 2f ) );
            final Stroke os = GraphicsUtils.setupStroke ( g2d, new BasicStroke ( sw ) );

            final Rectangle dl = frameDropData.getHighlight ();
            g2d.setPaint ( new Color ( 0, 0, 255, 55 ) );
            g2d.fillRect ( dl.x + sw, dl.y + sw, dl.width - sw * 2 + delta, dl.height - sw * 2 + delta );
            g2d.setPaint ( new Color ( 0, 0, 0, 100 ) );
            g2d.drawRect ( dl.x + hf, dl.y + hf, dl.width - hf * 2, dl.height - hf * 2 );

            GraphicsUtils.restoreStroke ( g2d, os );
        }
    }

    @Override
    public boolean contains ( final int x, final int y )
    {
        return checkDrag () || checkResize ( x, y );
    }

    /**
     * Returns whether or not frame is being dragged.
     *
     * @return true if frame is being dragged, false otherwise
     */
    protected boolean checkDrag ()
    {
        boolean isDragged = false;
        if ( frameDropData != null )
        {
            isDragged = true;
        }
        else
        {
            if ( DragManager.isDragging ( FrameTransferable.dataFlavor ) )
            {
                try
                {
                    final Transferable transferable = DragManager.getTransferable ();
                    final FrameDragData data = ( FrameDragData ) transferable.getTransferData ( FrameTransferable.dataFlavor );
                    isDragged = dockablePane.findFrame ( data.getId () ) != null;
                }
                catch ( final Exception ignored )
                {
                    //
                }
            }
        }
        return isDragged;
    }

    /**
     * Returns whether or not resize operation is rolling or specified point is on top of resize gripper.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @return true if resize operation is rolling or specified point is on top of resize gripper, false otherwise
     */
    protected boolean checkResize ( final int x, final int y )
    {
        return resizeData != null || getResizeData ( x, y ) != null;
    }

    /**
     * Returns resize data under specified coordinate or {@code null}.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @return resize data under specified coordinate or {@code null}
     */
    @Nullable
    protected ResizeData getResizeData ( final int x, final int y )
    {
        return dockablePane.getModel ().getResizeData ( x, y );
    }
}