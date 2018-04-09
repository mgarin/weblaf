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

package com.alee.managers.focus;

import com.alee.api.jdk.BiConsumer;
import com.alee.utils.collection.ImmutableList;
import com.alee.utils.swing.WeakComponentDataList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.FocusEvent;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * This manager allows you to track certain component their children focus state by adding your custom FocusTracker or global focus
 * listeners to track component focus state.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-FocusManager">How to use FocusManager</a>
 */
public final class FocusManager
{
    /**
     * {@link List} of all registered {@link GlobalFocusListener}s.
     * Use these listeners with care as they are not tied to anything and will remain in memory as long as you keep them registered.
     */
    private static final List<GlobalFocusListener> globalFocusListeners = new ArrayList<GlobalFocusListener> ( 5 );

    /**
     * {@link GlobalFocusListener}s registered for specific {@link JComponent}s.
     */
    private static final WeakComponentDataList<JComponent, GlobalFocusListener> globalComponentFocusListeners =
            new WeakComponentDataList<JComponent, GlobalFocusListener> ( "FocusManager.GlobalFocusListener", 5 );

    /**
     * {@link FocusTracker}s registered for specific {@link JComponent}s.
     */
    private static final WeakComponentDataList<JComponent, FocusTracker> trackers =
            new WeakComponentDataList<JComponent, FocusTracker> ( "FocusManager.FocusTracker", 200 );

    /**
     * Reference to previously focused component.
     */
    private static WeakReference<Component> oldFocusOwner;

    /**
     * Reference to currently focused component.
     */
    private static WeakReference<Component> focusOwner;

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

            // AWT event listener to fire global focus change events
            Toolkit.getDefaultToolkit ().addAWTEventListener ( new AWTEventListener ()
            {
                @Override
                public void eventDispatched ( final AWTEvent event )
                {
                    // Filtering unnecessary events and proceeding only when we have global listeners
                    if ( event instanceof FocusEvent )
                    {
                        // Filtering unnecessary events since each focus change within application generates 2 events - loss and gain
                        final FocusEvent focusEvent = ( FocusEvent ) event;
                        if ( focusEvent.getID () == FocusEvent.FOCUS_LOST && focusEvent.getOppositeComponent () == null )
                        {
                            // Updating weak references
                            oldFocusOwner = new WeakReference<Component> ( focusEvent.getComponent () );
                            focusOwner = new WeakReference<Component> ( null );

                            // Focus moved outside the application
                            fireGlobalFocusChanged ( focusEvent.getComponent (), null );
                        }
                        else if ( focusEvent.getID () == FocusEvent.FOCUS_GAINED )
                        {
                            // Updating weak references
                            oldFocusOwner = new WeakReference<Component> ( focusEvent.getOppositeComponent () );
                            focusOwner = new WeakReference<Component> ( focusEvent.getComponent () );

                            // Focus changed within the application (or might have just entered the window)
                            fireGlobalFocusChanged ( focusEvent.getOppositeComponent (), focusEvent.getComponent () );
                        }
                    }
                }
            }, AWTEvent.FOCUS_EVENT_MASK );

            // Global focus listener to fire tracked components focus change events
            registerGlobalFocusListener ( new GlobalFocusListener ()
            {
                @Override
                public void focusChanged ( final Component oldFocus, final Component newFocus )
                {
                    // Firing tracked focus change events
                    fireTrackedFocusChanged ( oldFocus, newFocus );
                }
            } );
        }
    }

    /**
     * Returns currently focused component.
     *
     * @return currently focused component
     */
    public static Component getFocusOwner ()
    {
        return focusOwner != null ? focusOwner.get () : null;
    }

    /**
     * Returns previously focused component.
     *
     * @return previously focused component
     */
    public static Component getOldFocusOwner ()
    {
        return oldFocusOwner.get ();
    }

    /**
     * Registers new {@link GlobalFocusListener}.
     * Use these listeners with care as they are not tied to anything and will remain in memory as long as you keep them registered.
     *
     * @param listener {@link GlobalFocusListener} to register
     */
    public static void registerGlobalFocusListener ( final GlobalFocusListener listener )
    {
        synchronized ( globalFocusListeners )
        {
            globalFocusListeners.add ( listener );
        }
    }

    /**
     * Unregisters {@link GlobalFocusListener}.
     * Use these listeners with care as they are not tied to anything and will remain in memory as long as you keep them registered.
     *
     * @param listener {@link GlobalFocusListener} to unregister
     */
    public static void unregisterGlobalFocusListener ( final GlobalFocusListener listener )
    {
        synchronized ( globalFocusListeners )
        {
            globalFocusListeners.remove ( listener );
        }
    }

    /**
     * Registers new {@link GlobalFocusListener}.
     *
     * @param component {@link JComponent} to register {@link GlobalFocusListener} for
     * @param listener  {@link GlobalFocusListener} to register
     */
    public static void registerGlobalFocusListener ( final JComponent component, final GlobalFocusListener listener )
    {
        globalComponentFocusListeners.add ( component, listener );
    }

    /**
     * Unregisters {@link GlobalFocusListener}.
     *
     * @param component {@link JComponent} to unregister {@link GlobalFocusListener} from
     * @param listener  {@link GlobalFocusListener} to unregister
     */
    public static void unregisterGlobalFocusListener ( final JComponent component, final GlobalFocusListener listener )
    {
        globalComponentFocusListeners.remove ( component, listener );
    }

    /**
     * Fires {@link GlobalFocusListener#focusChanged(Component, Component)} event.
     *
     * @param oldFocus previously focused {@link Component}
     * @param newFocus currently focused {@link Component}
     */
    private static void fireGlobalFocusChanged ( final Component oldFocus, final Component newFocus )
    {
        final ImmutableList<GlobalFocusListener> globalListenersCopy;
        synchronized ( globalFocusListeners )
        {
            globalListenersCopy = new ImmutableList<GlobalFocusListener> ( FocusManager.globalFocusListeners );
        }
        for ( final GlobalFocusListener listener : globalListenersCopy )
        {
            listener.focusChanged ( oldFocus, newFocus );
        }
        globalComponentFocusListeners.forEachData ( new BiConsumer<JComponent, GlobalFocusListener> ()
        {
            @Override
            public void accept ( final JComponent component, final GlobalFocusListener listener )
            {
                listener.focusChanged ( oldFocus, newFocus );
            }
        } );
    }

    /**
     * Registers focus tracker.
     *
     * @param component    component to add tracker for
     * @param focusTracker new focus tracker
     */
    public static void addFocusTracker ( final JComponent component, final FocusTracker focusTracker )
    {
        trackers.add ( component, focusTracker );
    }

    /**
     * Unregisters specified focus tracker.
     *
     * @param component    component to remove tracker from
     * @param focusTracker focus tracker to unregister
     */
    public static void removeFocusTracker ( final JComponent component, final FocusTracker focusTracker )
    {
        trackers.remove ( component, focusTracker );
    }

    /**
     * Unregisters all focus trackers from the specified component.
     *
     * @param component component to unregister all focus trackers from
     */
    public static void removeFocusTrackers ( final JComponent component )
    {
        trackers.clear ( component );
    }

    /**
     * Fires {@link FocusTracker#focusChanged(boolean)} event for involved components.
     *
     * @param oldFocus previously focused {@link Component}
     * @param newFocus currently focused {@link Component}
     */
    private static void fireTrackedFocusChanged ( final Component oldFocus, final Component newFocus )
    {
        // Iterating through all registered focus trackers
        trackers.forEachData ( new BiConsumer<JComponent, FocusTracker> ()
        {
            @Override
            public void accept ( final JComponent tracked, final FocusTracker focusTracker )
            {
                // Checking whether or not tracker is currently enabled
                if ( focusTracker.isEnabled () )
                {
                    // Checking whether or not component is related to this focus change
                    final boolean isOldFocused = focusTracker.isInvolved ( tracked, oldFocus );
                    final boolean isNewFocused = focusTracker.isInvolved ( tracked, newFocus );
                    if ( isOldFocused || isNewFocused )
                    {
                        // Checking whether or not focus state actually changed for the tracked component
                        if ( focusTracker.isFocused () != isNewFocused )
                        {
                            // Updating focus state
                            focusTracker.setFocused ( isNewFocused );

                            // Informing tracker about focus change
                            focusTracker.focusChanged ( isNewFocused );
                        }
                    }
                }
            }
        } );
    }
}