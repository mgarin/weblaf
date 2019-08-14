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

package com.alee.managers.hover;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.BiConsumer;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.collection.ImmutableList;
import com.alee.utils.swing.WeakComponentDataList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This manager allows you to track certain component their children hover state by adding your custom {@link HoverTracker} or
 * {@link GlobalHoverListener} to track component hover state.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-HoverManager">How to use HoverManager</a>
 */
public final class HoverManager
{
    /**
     * {@link List} of all registered {@link GlobalHoverListener}s.
     * Use these listeners with care as they are not tied to anything and will remain in memory as long as you keep them registered.
     */
    @NotNull
    private static final List<GlobalHoverListener> globalHoverListeners = new ArrayList<GlobalHoverListener> ( 5 );

    /**
     * {@link GlobalHoverListener}s registered for specific {@link JComponent}s.
     */
    @NotNull
    private static final WeakComponentDataList<JComponent, GlobalHoverListener> globalComponentHoverListeners =
            new WeakComponentDataList<JComponent, GlobalHoverListener> ( "HoverManager.GlobalHoverListener", 5 );

    /**
     * {@link HoverTracker}s registered for specific {@link JComponent}s.
     */
    @NotNull
    private static final WeakComponentDataList<JComponent, HoverTracker> trackers =
            new WeakComponentDataList<JComponent, HoverTracker> ( "HoverManager.HoverTracker", 200 );

    /**
     * Reference to previously hovered {@link Component}.
     */
    private static WeakReference<Component> previousHoverOwner;

    /**
     * Reference to currently hovered {@link Component}.
     */
    private static WeakReference<Component> hoverOwner;

    /**
     * Reference to currently hovered {@link Component}'s {@link Window}.
     * It is saved as a more reliable source of the new hover {@link Component} rather than previous hover {@link Component}.
     */
    private static WeakReference<Window> hoverOwnerWindow;

    /**
     * Queue of hover events for {@link Component}s.
     * Event time acts as a key, order is irrelevant hence the {@link Map} usage.
     * All events are queued on EDT and will simply use this {@link Map} to retrieve hover {@link Component}.
     */
    private static final Map<Long, Component> hoverEventQueue = new HashMap<Long, Component> ();

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

            // Initial values
            previousHoverOwner = new WeakReference<Component> ( null );
            hoverOwner = new WeakReference<Component> ( null );
            hoverOwnerWindow = new WeakReference<Window> ( null );

            /**
             * Listening to mouse ENTER & EXIT events, equivalent of {@link java.awt.event.MouseListener}.
             * This one is important as it is the only listener that will properly fire EXIT event when mouse leaves window bounds.
             * EXIT event will also be fired whenever user alt-tabs out of the window.
             * Listening to ENTER event is added to avoid unnecessary updates when EXIT & ENTER are fired in a sequence.
             */
            Toolkit.getDefaultToolkit ().addAWTEventListener ( new AWTEventListener ()
            {
                @Override
                public void eventDispatched ( final AWTEvent event )
                {
                    final MouseEvent mouseEvent = ( MouseEvent ) event;
                    if ( mouseEvent.getID () == MouseEvent.MOUSE_ENTERED )
                    {
                        queueEvent (
                                mouseEvent.getWhen (),
                                findNearbyHoverOwner ( mouseEvent.getComponent (), mouseEvent.getPoint () )
                        );
                    }
                    else if ( mouseEvent.getID () == MouseEvent.MOUSE_EXITED )
                    {
                        queueEvent (
                                mouseEvent.getWhen (),
                                null
                        );
                    }
                }
            }, AWTEvent.MOUSE_EVENT_MASK );

            /**
             * Listening to mouse MOVE & DRAG events, equivalent of {@link java.awt.event.MouseMotionListener}.
             * Listening to these events is necessary because {@link AWTEvent#MOUSE_EVENT_MASK} doesn't work for all components.
             * Basically mouse ENTER & EXIT are only fired for components that have {@link java.awt.event.MouseListener} installed.
             * That makes usage of {@link AWTEvent#MOUSE_EVENT_MASK} alone impossible even though it would be perfect for hover updates.
             * So instead we're listening to mouse movement over the window to track hovered component and only push updates when needed.
             */
            Toolkit.getDefaultToolkit ().addAWTEventListener ( new AWTEventListener ()
            {
                @Override
                public void eventDispatched ( final AWTEvent event )
                {
                    final MouseEvent mouseEvent = ( MouseEvent ) event;
                    if ( mouseEvent.getID () == MouseEvent.MOUSE_MOVED || mouseEvent.getID () == MouseEvent.MOUSE_DRAGGED )
                    {
                        queueEvent (
                                mouseEvent.getWhen (),
                                findNearbyHoverOwner ( mouseEvent.getComponent (), mouseEvent.getPoint () )
                        );
                    }
                }
            }, AWTEvent.MOUSE_MOTION_EVENT_MASK );

            /**
             * Listening for component SHOW, HIDE, RESIZE and MOVE events, equivalent of {@link java.awt.event.ComponentListener}.
             * This is necesssary to update hover state upon various component state changes, here are some examples:
             * - Component becoming hidden or visible under the cursor location
             * - Component moving under or out of the cursor location due to layout/hierarchy change
             * - Component moving under or out of the cursor location due to window location change
             * - Component moving under or out of the cursor location due to component decoration changes
             */
            Toolkit.getDefaultToolkit ().addAWTEventListener ( new AWTEventListener ()
            {
                @Override
                public void eventDispatched ( final AWTEvent event )
                {
                    queuePostLayoutUpdateEvent ();
                }
            }, AWTEvent.COMPONENT_EVENT_MASK );
        }
    }

    /**
     * Queues hover state change update to be performed later.
     * This is necessary to avoid unwanted updates to be skipped.
     * Most prominent case being {@link MouseEvent#MOUSE_ENTERED} being fired right after {@link MouseEvent#MOUSE_EXITED}.
     * Due to how these events are separated there is no convenient way to perform correct update right away.
     * We have to wait and see whether {@link MouseEvent#MOUSE_EXITED} was fired alone or not.
     *
     * @param time      event time in milliseconds, used to determine paired events
     * @param component {@link Component} that event was fired on
     */
    private static void queueEvent ( final long time, @Nullable final Component component )
    {
        if ( !hoverEventQueue.containsKey ( time ) )
        {
            SwingUtilities.invokeLater ( new Runnable ()
            {
                @Override
                public void run ()
                {
                    if ( hoverEventQueue.containsKey ( time ) )
                    {
                        fireHoverChanged ( hoverEventQueue.remove ( time ) );
                    }
                }
            } );
        }
        hoverEventQueue.put ( time, component );
    }

    /**
     * Queues hover state change update to be performed after all layout updates are done.
     * This call queues magical double {@link SwingUtilities#invokeLater(Runnable)} to make sure it runs past layot updates.
     * This is tricky, but can be explained by the order operations are performed in Swing when some event occurs.
     *
     * Let's say something happens on EDT and UI has to handle it, this is how it goes:
     * (EDT1, EDT2, etc - these are simply different {@link Runnable}s, obviously all executed on EDT)
     *
     * [EDT1]
     * 1. Something has changed, for instance component was removed from layout
     * 2. Past it's removal events are being fired for various listeners (ancestor/container/hierarchy/etc)
     * 3. Our {@link HoverManager} receives {@link AWTEvent} somewhere here and {@link #queuePostLayoutUpdateEvent()} is called
     * 4. Listeners of those events queue later layout {@link JComponent#revalidate()} and {@link JComponent#repaint()}
     * [EDT2]
     * x. Some other stuff might execute here, whatever was queued for "later" in previous EDT run
     * x. It could be some other components performing actions or firing events
     * x. Can also simply be UI repainting something due to user interactions or system event
     * [EDT3]
     * 5. We finally got to validation of our layout, it is being updated here
     * [EDT4]
     * 6. On a separate run we also get a visual update in case layout changed
     * [EDT5]
     * 7. We're finally where we want our {@link #queuePostLayoutUpdateEvent()} method body to actually be performed
     *
     * So the important part in this order is when (3) and (4) are executed.
     * If (4) is done before (3) - which can happen, but depends on implementation - we would only need to send our update event to
     * {@link SwingUtilities#invokeLater(Runnable)} once and we would receive wanted result because (7) will occur after (5) and (6).
     * In reality there is no guarantee that our listener won't receive event earlier than listeners that queue layour and visual updates.
     * That is why we have to use double {@link SwingUtilities#invokeLater(Runnable)} to ensure that it gets past (5) and (6).
     */
    private static void queuePostLayoutUpdateEvent ()
    {
        SwingUtilities.invokeLater ( new Runnable ()
        {
            @Override
            public void run ()
            {
                SwingUtilities.invokeLater ( new Runnable ()
                {
                    @Override
                    public void run ()
                    {
                        fireHoverChanged ( findPossibleHoverOwner () );
                    }
                } );
            }
        } );
    }

    /**
     * Returns possible new hover owner on the {@link Window} of the previous hover owner.
     * We don't need to use previous hover owner {@link Component} because it is less reliable.
     * We also will be extracting it's {@link JRootPane} owner anyway, so it is faster to use {@link Window}.
     * We don't want to use any other {@link Window}s because even though we can access them - there is no guarantee they are on top.
     * Otherwise we're rising to provide hover component in a {@link Window} that is not even physically visible to user.
     *
     * @return possible new hover owner on the {@link Window} of the previous hover owner
     */
    @Nullable
    private static Component findPossibleHoverOwner ()
    {
        final Component newHover;
        final Window currentHoverWindow = hoverOwnerWindow.get ();
        if ( currentHoverWindow != null && currentHoverWindow.isShowing () )
        {
            newHover = findNearbyHoverOwner (
                    currentHoverWindow,
                    CoreSwingUtils.getMouseLocation ( currentHoverWindow )
            );
        }
        else
        {
            newHover = null;
        }
        return newHover;
    }

    /**
     * Returns hover owner in the same window where {@link Component} is located or {@code null} if none are hovered.
     * Note that this method limits hover detection to window implementations that have {@link JRootPane}.
     * Problem with {@link Window} is that we might step outside of the root pane but still hit window decoration with the check.
     *
     * We're not using {@link SwingUtilities#convertPoint(Component, Point, Component)} here for optimization reasons.
     * Retrieving component location on screen is way more time-consuming operation and we don't really need it here.
     *
     * @param component {@link Component} to find hover owner nearby
     * @param point     {@link MouseEvent} point relative to {@link Component}
     * @return hover owner in the same window where {@link Component} is located or {@code null} if none are hovered
     */
    @Nullable
    private static Component findNearbyHoverOwner ( @Nullable final Component component, @NotNull final Point point )
    {
        final Component newHoverOwner;
        if ( component != null )
        {
            final JRootPane rootPane = CoreSwingUtils.getRootPane ( component );
            if ( rootPane != null && rootPane.isShowing () )
            {
                if ( rootPane == component )
                {
                    // Point is relative to the root pane
                    newHoverOwner = CoreSwingUtils.getTopComponentAt ( rootPane, point );
                }
                else if ( rootPane.isAncestorOf ( component ) )
                {
                    // Recalculating point relative to one of the root pane children
                    final Point relative = new Point ( point );
                    Component parent = component;
                    while ( parent != rootPane && parent != null )
                    {
                        final Point location = parent.getLocation ();
                        relative.x += location.x;
                        relative.y += location.y;
                        parent = parent.getParent ();
                    }
                    if ( parent != null )
                    {
                        // Point is relative to one of the root pane children
                        newHoverOwner = CoreSwingUtils.getTopComponentAt ( rootPane, relative );
                    }
                    else
                    {
                        // Something changed in structure and we can't find top component anymore
                        // This happened on toolbar drag, hence this workaround was added to avoid NPE
                        newHoverOwner = null;
                    }
                }
                else
                {
                    // Recalculating point relative to one of the root pane parents
                    final Point relative = new Point ( point );
                    Component parent = rootPane;
                    while ( parent != component && parent != null )
                    {
                        final Point location = parent.getLocation ();
                        relative.x -= location.x;
                        relative.y -= location.y;
                        parent = parent.getParent ();
                    }
                    if ( parent != null )
                    {
                        // Point is relative to one of the root pane parents
                        newHoverOwner = CoreSwingUtils.getTopComponentAt ( rootPane, relative );
                    }
                    else
                    {
                        // Something changed in structure and we can't find top component anymore
                        // This happened on toolbar drag, hence this workaround was added to avoid NPE
                        newHoverOwner = null;
                    }
                }
            }
            else
            {
                // Component window doesn't have root pane
                newHoverOwner = null;
            }
        }
        else
        {
            // Component is unavailable
            newHoverOwner = null;
        }
        return newHoverOwner;
    }

    /**
     * Returns previously hovered {@link Component}.
     *
     * @return previously hovered {@link Component}
     */
    @Nullable
    public static Component getPreviousHoverOwner ()
    {
        return previousHoverOwner.get ();
    }

    /**
     * Returns currently hovered {@link Component}.
     *
     * @return currently hovered {@link Component}
     */
    @Nullable
    public static Component getHoverOwner ()
    {
        return hoverOwner != null ? hoverOwner.get () : null;
    }

    /**
     * Returns currently hovered {@link Window}.
     *
     * @return currently hovered {@link Window}
     */
    @Nullable
    public static Window getHoverOwnerWindow ()
    {
        return hoverOwnerWindow != null ? hoverOwnerWindow.get () : null;
    }

    /**
     * Registers new {@link GlobalHoverListener}.
     * Use these listeners with care as they are not tied to anything and will remain in memory as long as you keep them registered.
     *
     * @param listener {@link GlobalHoverListener} to register
     */
    public static void registerGlobalHoverListener ( @NotNull final GlobalHoverListener listener )
    {
        synchronized ( globalHoverListeners )
        {
            globalHoverListeners.add ( listener );
        }
    }

    /**
     * Unregisters {@link GlobalHoverListener}.
     * Use these listeners with care as they are not tied to anything and will remain in memory as long as you keep them registered.
     *
     * @param listener {@link GlobalHoverListener} to unregister
     */
    public static void unregisterGlobalHoverListener ( @NotNull final GlobalHoverListener listener )
    {
        synchronized ( globalHoverListeners )
        {
            globalHoverListeners.remove ( listener );
        }
    }

    /**
     * Registers new {@link GlobalHoverListener}.
     *
     * @param component {@link JComponent} to register {@link GlobalHoverListener} for
     * @param listener  {@link GlobalHoverListener} to register
     */
    public static void registerGlobalHoverListener ( @NotNull final JComponent component, @NotNull final GlobalHoverListener listener )
    {
        globalComponentHoverListeners.add ( component, listener );
    }

    /**
     * Unregisters {@link GlobalHoverListener}.
     *
     * @param component {@link JComponent} to unregister {@link GlobalHoverListener} from
     * @param listener  {@link GlobalHoverListener} to unregister
     */
    public static void unregisterGlobalHoverListener ( @NotNull final JComponent component, @NotNull final GlobalHoverListener listener )
    {
        globalComponentHoverListeners.remove ( component, listener );
    }

    /**
     * Registers hover tracker.
     *
     * @param component    component to add tracker for
     * @param hoverTracker new hover tracker
     */
    public static void addHoverTracker ( @NotNull final JComponent component, @NotNull final HoverTracker hoverTracker )
    {
        trackers.add ( component, hoverTracker );
    }

    /**
     * Unregisters specified hover tracker.
     *
     * @param component    component to remove tracker from
     * @param hoverTracker hover tracker to unregister
     */
    public static void removeHoverTracker ( @NotNull final JComponent component, @NotNull final HoverTracker hoverTracker )
    {
        trackers.remove ( component, hoverTracker );
    }

    /**
     * Unregisters all hover trackers from the specified component.
     *
     * @param component component to unregister all hover trackers from
     */
    public static void removeHoverTrackers ( @NotNull final JComponent component )
    {
        trackers.clear ( component );
    }

    /**
     * Fires all hover listeners.
     *
     * @param hover newly hovered {@link Component}
     */
    private static void fireHoverChanged ( @Nullable final Component hover )
    {
        /**
         * Last second check that hovered {@link Component} is still showing.
         * We don't ever want to pass hover to {@link Component} that is not currently showing on the screen.
         * Also we don't want to fire events when actual hover component haven't changed.
         */
        final Component newHover = hover == null || hover.isShowing () ? hover : null;
        final Component oldHover = hoverOwner.get ();
        if ( newHover != oldHover )
        {
            // Updating weak references
            previousHoverOwner = new WeakReference<Component> ( oldHover );
            hoverOwner = new WeakReference<Component> ( newHover );
            hoverOwnerWindow = new WeakReference<Window> ( CoreSwingUtils.getWindowAncestor ( newHover ) );

            // Iterating through all registered hover trackers
            trackers.forEachData ( new BiConsumer<JComponent, HoverTracker> ()
            {
                @Override
                public void accept ( final JComponent tracked, final HoverTracker hoverTracker )
                {
                    // Checking whether or not tracker is currently enabled
                    if ( hoverTracker.isEnabled () )
                    {
                        // Checking whether or not component is related to this hover change
                        final boolean isOldHovered = hoverTracker.isInvolved ( tracked, oldHover );
                        final boolean isNewHovered = hoverTracker.isInvolved ( tracked, newHover );
                        if ( isOldHovered || isNewHovered )
                        {
                            // Checking whether or not hover state actually changed for the tracked component
                            if ( hoverTracker.isHovered () != isNewHovered )
                            {
                                // Updating hover state
                                hoverTracker.setHovered ( isNewHovered );

                                // Informing tracker about hover change
                                hoverTracker.hoverChanged ( isNewHovered );
                            }
                        }
                    }
                }
            } );

            // Firing global component listeners
            globalComponentHoverListeners.forEachData ( new BiConsumer<JComponent, GlobalHoverListener> ()
            {
                @Override
                public void accept ( final JComponent component, final GlobalHoverListener listener )
                {
                    listener.hoverChanged ( oldHover, newHover );
                }
            } );

            // Firing global listeners
            final ImmutableList<GlobalHoverListener> globalListenersCopy;
            synchronized ( globalHoverListeners )
            {
                globalListenersCopy = new ImmutableList<GlobalHoverListener> ( HoverManager.globalHoverListeners );
            }
            for ( final GlobalHoverListener listener : globalListenersCopy )
            {
                listener.hoverChanged ( oldHover, newHover );
            }
        }
    }
}