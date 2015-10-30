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

package com.alee.managers.drag;

import com.alee.managers.glasspane.GlassPaneManager;
import com.alee.managers.glasspane.WebGlassPane;
import com.alee.managers.log.Log;
import com.alee.utils.SwingUtils;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * This manager simplifies dragged items visual representation creation.
 * You can add customized representation support for DataFlavor by registering new DragViewHandler.
 * So far custom DataFlavor view will be displayed only within application window bounds.
 *
 * @author Mikle Garin
 */

public final class DragManager
{
    /**
     * todo 1. Move dragged object display to a separate transparent non-focusable window
     */

    /**
     * Drag view handlers map.
     */
    private static Map<DataFlavor, DragViewHandler> viewHandlers;

    /**
     * Whether or not something is being dragged right now.
     */
    private static boolean dragging = false;

    /**
     * Dragged object representation variables.
     */
    private static WebGlassPane glassPane;
    private static Object data;
    private static BufferedImage view;
    private static Component dropLocation;
    private static DragViewHandler dragViewHandler;

    /**
     * Whether manager is initialized or not.
     */
    private static boolean initialized = false;

    /**
     * Initializes manager if it wasn't already initialized.
     */
    public static synchronized void initialize ()
    {
        // To avoid more than one initialization
        if ( !initialized )
        {
            // Remember that initialization happened
            initialized = true;

            // View handlers map
            viewHandlers = new HashMap<DataFlavor, DragViewHandler> ();

            final DragSourceAdapter dsa = new DragSourceAdapter ()
            {
                @Override
                public void dragEnter ( final DragSourceDragEvent dsde )
                {
                    dragEnterImpl ( dsde );
                }

                /**
                 * Performs actions on drag enter.
                 *
                 * @param dsde drag source drag event
                 */
                protected void dragEnterImpl ( final DragSourceDragEvent dsde )
                {
                    // todo Do not recreate view few times while dragging

                    // Save drop location component
                    final DragSourceContext dsc = dsde.getDragSourceContext ();
                    dropLocation = dsc.getComponent ();

                    // Deciding on enter what to display for this kind of data
                    final Transferable transferable = dsc.getTransferable ();
                    final DataFlavor[] flavors = transferable.getTransferDataFlavors ();
                    for ( final DataFlavor flavor : flavors )
                    {
                        if ( viewHandlers.containsKey ( flavor ) )
                        {
                            try
                            {
                                data = transferable.getTransferData ( flavor );
                                dragViewHandler = viewHandlers.get ( flavor );
                                view = dragViewHandler.getView ( data, dsde );

                                glassPane = GlassPaneManager.getGlassPane ( dsc.getComponent () );
                                glassPane.setPaintedImage ( view, getLocation ( glassPane, dsde ) );

                                break;
                            }
                            catch ( final Throwable e )
                            {
                                Log.error ( DragManager.class, e );
                            }
                        }
                    }

                    // Marking drag operation
                    dragging = true;
                }

                @Override
                public void dragMouseMoved ( final DragSourceDragEvent dsde )
                {
                    final DragSourceContext dsc = dsde.getDragSourceContext ();
                    if ( dsc.getComponent () != dropLocation )
                    {
                        dragEnterImpl ( dsde );
                    }

                    // Move displayed data
                    if ( view != null )
                    {
                        final WebGlassPane gp = GlassPaneManager.getGlassPane ( dsde.getDragSourceContext ().getComponent () );
                        if ( gp != glassPane )
                        {
                            glassPane.clearPaintedImage ();
                            glassPane = gp;
                        }
                        glassPane.setPaintedImage ( view, getLocation ( glassPane, dsde ) );
                    }
                }

                /**
                 * Returns preferred dragged element location on glass pane.
                 *
                 * @param gp   glass pane
                 * @param dsde drag source drag event
                 * @return preferred dragged element location on glass pane
                 */
                public Point getLocation ( final WebGlassPane gp, final DragSourceDragEvent dsde )
                {
                    final Point mp = SwingUtils.getMousePoint ( gp );
                    final Point vp = dragViewHandler.getViewRelativeLocation ( data, dsde );
                    return new Point ( mp.x + vp.x, mp.y + vp.y );
                }

                @Override
                public void dragDropEnd ( final DragSourceDropEvent dsde )
                {
                    // Marking drag operation
                    dragging = false;

                    // Cleanup drop location component
                    dropLocation = null;

                    // Cleanup displayed data
                    if ( view != null )
                    {
                        dragViewHandler.dragEnded ( data, dsde );
                        glassPane.clearPaintedImage ();
                        glassPane = null;
                        data = null;
                        view = null;
                        dragViewHandler = null;
                    }
                }
            };
            DragSource.getDefaultDragSource ().addDragSourceListener ( dsa );
            DragSource.getDefaultDragSource ().addDragSourceMotionListener ( dsa );
        }
    }

    /**
     * Returns whether or not something is being dragged right now within the application.
     *
     * @return true if something is being dragged right now within the application, false otherwise
     */
    public static boolean isDragging ()
    {
        return dragging;
    }

    /**
     * Registers new DragViewHandler.
     *
     * @param dragViewHandler DragViewHandler to register
     */
    public static void registerViewHandler ( final DragViewHandler dragViewHandler )
    {
        viewHandlers.put ( dragViewHandler.getObjectFlavor (), dragViewHandler );
    }

    /**
     * Unregisters new DragViewHandler.
     *
     * @param dragViewHandler DragViewHandler to unregister
     */
    public static void unregisterViewHandler ( final DragViewHandler dragViewHandler )
    {
        viewHandlers.remove ( dragViewHandler.getObjectFlavor () );
    }
}