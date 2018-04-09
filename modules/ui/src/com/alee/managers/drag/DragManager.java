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

import com.alee.api.jdk.BiConsumer;
import com.alee.managers.drag.view.DragViewHandler;
import com.alee.managers.glasspane.GlassPaneManager;
import com.alee.managers.glasspane.WebGlassPane;
import com.alee.utils.ArrayUtils;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.swing.WeakComponentDataList;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This manager simplifies dragged items visual representation creation.
 * You can add customized representation support for DataFlavor by registering new DragViewHandler.
 * So far custom DataFlavor view will be displayed only within application window bounds.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-DragManager">How to use DragManager</a>
 */
public final class DragManager
{
    /**
     * todo 1. Move dragged object display to a separate transparent non-focusable window on systems where it is possible
     */

    /**
     * Various manager listeners.
     *
     * @see DragListener
     */
    private static final WeakComponentDataList<JComponent, DragListener> dragListeners =
            new WeakComponentDataList<JComponent, DragListener> ( "DragManager.DragListener", 50 );

    /**
     * Drag view handlers map.
     *
     * @see DragViewHandler
     */
    private static final Map<DataFlavor, List<DragViewHandler>> viewHandlers = new HashMap<DataFlavor, List<DragViewHandler>> ();

    /**
     * Whether or not something is being dragged right now.
     */
    private static boolean dragging = false;

    /**
     * Drag operation data flavors.
     */
    private static DataFlavor[] flavors = null;

    /**
     * Currently dragged transferable.
     */
    private static Transferable transferable = null;

    /**
     * Dragged object representation variables.
     */
    private static WebGlassPane glassPane;
    private static Object data;
    private static BufferedImage view;
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

            // Global drag listener
            final DragSourceAdapter dsa = new DragSourceAdapter ()
            {
                /**
                 * Informs about drag operation start.
                 *
                 * @param event drag event
                 */
                protected void dragStarted ( final DragSourceDragEvent event )
                {
                    // Deciding on enter what to display for this kind of data
                    final DragSourceContext dsc = event.getDragSourceContext ();
                    final Transferable transferable = dsc.getTransferable ();
                    final DataFlavor[] flavors = transferable.getTransferDataFlavors ();
                    for ( final DataFlavor flavor : flavors )
                    {
                        if ( viewHandlers.containsKey ( flavor ) )
                        {
                            try
                            {
                                data = transferable.getTransferData ( flavor );
                                for ( final DragViewHandler handler : viewHandlers.get ( flavor ) )
                                {
                                    if ( handler.supports ( data, event ) )
                                    {
                                        dragViewHandler = handler;
                                        break;
                                    }
                                }
                                if ( dragViewHandler != null )
                                {
                                    view = dragViewHandler.getView ( data, event );
                                    glassPane = GlassPaneManager.getGlassPane ( dsc.getComponent () );
                                    glassPane.setPaintedImage ( view, getLocation ( glassPane, event, view ) );
                                    break;
                                }
                            }
                            catch ( final Exception e )
                            {
                                LoggerFactory.getLogger ( DragManager.class ).error ( e.toString (), e );
                            }
                        }
                    }

                    // Marking drag operation
                    DragManager.transferable = transferable;
                    DragManager.flavors = flavors;
                    DragManager.dragging = true;

                    // Informing dragListeners
                    dragListeners.forEachData ( new BiConsumer<JComponent, DragListener> ()
                    {
                        @Override
                        public void accept ( final JComponent component, final DragListener dragListener )
                        {
                            dragListener.started ( event );
                        }
                    } );
                }

                @Override
                public void dragEnter ( final DragSourceDragEvent event )
                {
                    // Informing dragListeners
                    dragListeners.forEachData ( new BiConsumer<JComponent, DragListener> ()
                    {
                        @Override
                        public void accept ( final JComponent component, final DragListener dragListener )
                        {
                            dragListener.entered ( event );
                        }
                    } );
                }

                @Override
                public void dragExit ( final DragSourceEvent event )
                {
                    // Informing dragListeners
                    dragListeners.forEachData ( new BiConsumer<JComponent, DragListener> ()
                    {
                        @Override
                        public void accept ( final JComponent component, final DragListener dragListener )
                        {
                            dragListener.exited ( event );
                        }
                    } );
                }

                @Override
                public void dragMouseMoved ( final DragSourceDragEvent event )
                {
                    // Create synthetic drag start event
                    // This is required because there is no drag start even in default listener
                    if ( !dragging )
                    {
                        dragStarted ( event );
                    }

                    // Move displayed data
                    if ( view != null )
                    {
                        final WebGlassPane gp = GlassPaneManager.getGlassPane ( event.getDragSourceContext ().getComponent () );
                        if ( gp != glassPane )
                        {
                            glassPane.clearPaintedImage ();
                            glassPane = gp;
                        }
                        glassPane.setPaintedImage ( view, getLocation ( glassPane, event, view ) );
                    }

                    // Informing dragListeners
                    dragListeners.forEachData ( new BiConsumer<JComponent, DragListener> ()
                    {
                        @Override
                        public void accept ( final JComponent component, final DragListener dragListener )
                        {
                            dragListener.moved ( event );
                        }
                    } );
                }

                @Override
                public void dragDropEnd ( final DragSourceDropEvent event )
                {
                    // Marking drag operation
                    DragManager.dragging = false;
                    DragManager.flavors = null;
                    DragManager.transferable = null;

                    // Cleanup displayed data
                    if ( view != null )
                    {
                        dragViewHandler.dragEnded ( data, event );
                        glassPane.clearPaintedImage ();
                        glassPane = null;
                        data = null;
                        view = null;
                        dragViewHandler = null;
                    }

                    // Informing dragListeners
                    dragListeners.forEachData ( new BiConsumer<JComponent, DragListener> ()
                    {
                        @Override
                        public void accept ( final JComponent component, final DragListener dragListener )
                        {
                            dragListener.finished ( event );
                        }
                    } );
                }

                /**
                 * Returns preferred dragged element location on glass pane.
                 *
                 * @param gp   glass pane
                 * @param dsde drag source drag event
                 * @param view resulting view of the dragged object
                 * @return preferred dragged element location on glass pane
                 */
                public Point getLocation ( final WebGlassPane gp, final DragSourceDragEvent dsde, final BufferedImage view )
                {
                    final Point mp = CoreSwingUtils.getMouseLocation ( gp );
                    final Point vp = dragViewHandler.getViewRelativeLocation ( data, dsde, view );
                    return new Point ( mp.x + vp.x, mp.y + vp.y );
                }
            };

            // Listeners could only be registered on non-headless environment
            if ( !GraphicsEnvironment.isHeadless () )
            {
                DragSource.getDefaultDragSource ().addDragSourceListener ( dsa );
                DragSource.getDefaultDragSource ().addDragSourceMotionListener ( dsa );
            }
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
     * Returns whether or not something with the specified data flavor is being dragged right now within the application.
     *
     * @param flavor data flavor
     * @return true if something with the specified data flavor is being dragged right now within the application, false otherwise
     */
    public static boolean isDragging ( final DataFlavor flavor )
    {
        return dragging && ArrayUtils.contains ( flavor, flavors );
    }

    /**
     * Returns data flavors of the current drag operation.
     *
     * @return data flavors of the current drag operation
     */
    public static DataFlavor[] getFlavors ()
    {
        return dragging ? flavors : null;
    }

    /**
     * Returns currently dragged transferable.
     *
     * @return currently dragged transferable
     */
    public static Transferable getTransferable ()
    {
        return transferable;
    }

    /**
     * Registers {@link DragViewHandler}.
     *
     * @param handler {@link DragViewHandler} to register
     */
    public static void registerViewHandler ( final DragViewHandler handler )
    {
        final DataFlavor flavor = handler.getObjectFlavor ();
        List<DragViewHandler> handlers = viewHandlers.get ( flavor );
        if ( handlers == null )
        {
            handlers = new ArrayList<DragViewHandler> ( 1 );
            viewHandlers.put ( flavor, handlers );
        }
        handlers.add ( handler );
    }

    /**
     * Unregisters {@link DragViewHandler}.
     *
     * @param handler {@link DragViewHandler} to unregister
     */
    public static void unregisterViewHandler ( final DragViewHandler handler )
    {
        final List<DragViewHandler> handlers = viewHandlers.get ( handler.getObjectFlavor () );
        if ( handlers != null )
        {
            handlers.remove ( handler );
        }
    }

    /**
     * Adds {@link DragListener} for the specified {@link JComponent}.
     *
     * @param component {@link JComponent} to add {@link DragListener} for
     * @param listener  {@link DragListener} to add
     */
    public static void addDragListener ( final JComponent component, final DragListener listener )
    {
        dragListeners.add ( component, listener );
    }

    /**
     * Removes {@link DragListener} from the specified {@link JComponent}.
     *
     * @param component {@link JComponent} to remove {@link DragListener} from
     * @param listener  {@link DragListener} to remove
     */
    public static void removeDragListener ( final JComponent component, final DragListener listener )
    {
        dragListeners.remove ( component, listener );
    }
}