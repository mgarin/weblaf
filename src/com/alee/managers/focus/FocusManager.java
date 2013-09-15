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

import com.alee.laf.GlobalConstants;
import com.alee.utils.SwingUtils;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.FocusEvent;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.List;

/**
 * This manager allows you to track certain component their childs focus state by adding your custom FocusTracker or global focus
 * listeners to track component focus state.
 *
 * @author Mikle Garin
 */

public final class FocusManager
{
    /**
     * Tracker list and cache lock.
     */
    private static final Object trackersLock = new Object ();

    /**
     * Focus trackers list.
     */
    private static Map<Component, Map<FocusTracker, Boolean>> trackers = new WeakHashMap<Component, Map<FocusTracker, Boolean>> ();

    /**
     * Global focus listeners lock.
     */
    private static final Object listenersLock = new Object ();

    /**
     * Global focus listeners list.
     */
    private static List<GlobalFocusListener> globalFocusListeners = new ArrayList<GlobalFocusListener> ( 2 );

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
    public static void initialize ()
    {
        // To avoid more than one initialization
        if ( !initialized )
        {
            // Remember that initialization happened
            initialized = true;

            // Global focus listener
            Toolkit.getDefaultToolkit ().addAWTEventListener ( new AWTEventListener ()
            {
                @Override
                public void eventDispatched ( AWTEvent event )
                {
                    if ( event instanceof FocusEvent )
                    {
                        if ( globalFocusListeners.size () > 0 )
                        {
                            // Filtering unnecessary events (each focus change within application generates 2 events - lost/gained)
                            final FocusEvent focusEvent = ( FocusEvent ) event;
                            if ( focusEvent.getID () == FocusEvent.FOCUS_LOST && focusEvent.getOppositeComponent () == null )
                            {
                                // Focus moved outside the application
                                fireGlobalFocusChanged ( focusEvent.getComponent (), null );
                            }
                            else if ( focusEvent.getID () == FocusEvent.FOCUS_GAINED )
                            {
                                // Focus changed within the application (or might have just entered the window)
                                fireGlobalFocusChanged ( focusEvent.getOppositeComponent (), focusEvent.getComponent () );
                            }
                        }
                    }
                }
            }, AWTEvent.FOCUS_EVENT_MASK );

            // Adding global focus tracker for trackers
            registerGlobalFocusListener ( new GlobalFocusListener ()
            {
                @Override
                public void focusChanged ( Component oldFocus, Component newFocus )
                {
                    oldFocusOwner = new WeakReference<Component> ( oldFocus );
                    focusOwner = new WeakReference<Component> ( newFocus );

                    // Debug info
                    if ( GlobalConstants.DEBUG )
                    {
                        final String oldName = oldFocus != null ? oldFocus.getClass ().getName () : null;
                        final String newName = newFocus != null ? newFocus.getClass ().getName () : null;
                        System.out.println ( "Focus changed: " + oldName + " --> " + newName );
                    }

                    // Checking all added trackers
                    synchronized ( trackersLock )
                    {
                        // Iterating through registered components
                        final Iterator<Map.Entry<Component, Map<FocusTracker, Boolean>>> iterator = trackers.entrySet ().iterator ();
                        while ( iterator.hasNext () )
                        {
                            // Retrieving tracked component and its trackers
                            final Map.Entry<Component, Map<FocusTracker, Boolean>> entry = iterator.next ();
                            final Component tracked = entry.getKey ();
                            final Map<FocusTracker, Boolean> componentTrackers = entry.getValue ();
                            final Iterator<Map.Entry<FocusTracker, Boolean>> innerIterator = componentTrackers.entrySet ().iterator ();

                            // Iterating through registered component trackers
                            while ( innerIterator.hasNext () )
                            {
                                // Retrieving tracker and its last state
                                final Map.Entry<FocusTracker, Boolean> innerEntry = innerIterator.next ();
                                final FocusTracker focusTracker = innerEntry.getKey ();
                                final Boolean trackerStateCache = innerEntry.getValue ();

                                // Checking state change
                                if ( tracked != null )
                                {
                                    // Skip if tracker is disabled
                                    if ( focusTracker.isTrackingEnabled () )
                                    {
                                        // Determining component is focused or not
                                        final boolean unite = focusTracker.isUniteWithChilds ();
                                        final boolean focused =
                                                unite ? SwingUtils.isEqualOrChild ( tracked, newFocus ) : tracked == newFocus;

                                        // Informing about focus changes if needed
                                        if ( trackerStateCache != focused )
                                        {
                                            focusTracker.focusChanged ( focused );
                                        }

                                        // Caching focus state
                                        innerEntry.setValue ( focused );
                                    }
                                }
                                else
                                {
                                    // Remove tracker because tracked component was removed by GC
                                    innerIterator.remove ();
                                }
                            }
                            if ( componentTrackers == null || componentTrackers.size () == 0 )
                            {
                                iterator.remove ();
                            }
                        }
                    }
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
        return focusOwner.get ();
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
     * Registers global focus listener.
     *
     * @param listener new global focus listener
     */
    public static void registerGlobalFocusListener ( GlobalFocusListener listener )
    {
        synchronized ( listenersLock )
        {
            globalFocusListeners.add ( listener );
        }
    }

    /**
     * Unregisters global focus listener.
     *
     * @param listener global focus listener to unregister
     */
    public static void unregisterGlobalFocusListener ( GlobalFocusListener listener )
    {
        synchronized ( listenersLock )
        {
            globalFocusListeners.remove ( listener );
        }
    }

    /**
     * Fires about global focus change.
     *
     * @param oldComponent previously focused component
     * @param newComponent currently focused component
     */
    private static void fireGlobalFocusChanged ( Component oldComponent, Component newComponent )
    {
        synchronized ( listenersLock )
        {
            for ( GlobalFocusListener listener : globalFocusListeners )
            {
                listener.focusChanged ( oldComponent, newComponent );
            }
        }
    }

    /**
     * Registers focus tracker.
     *
     * @param focusTracker new focus tracker
     */
    public static void addFocusTracker ( Component component, FocusTracker focusTracker )
    {
        synchronized ( trackersLock )
        {
            Map<FocusTracker, Boolean> componentTrackers = trackers.get ( component );
            if ( componentTrackers == null )
            {
                componentTrackers = new HashMap<FocusTracker, Boolean> ();
                trackers.put ( component, componentTrackers );
            }
            componentTrackers.put ( focusTracker,
                    focusTracker.isUniteWithChilds () ? SwingUtils.hasFocusOwner ( component ) : component.isFocusOwner () );
        }
    }

    /**
     * Unregisters focus tracker.
     *
     * @param focusTracker focus tracker to unregister
     */
    public static void removeFocusTracker ( FocusTracker focusTracker )
    {
        synchronized ( trackersLock )
        {
            final Iterator<Map.Entry<Component, Map<FocusTracker, Boolean>>> iterator = trackers.entrySet ().iterator ();
            while ( iterator.hasNext () )
            {
                final Map.Entry<Component, Map<FocusTracker, Boolean>> entry = iterator.next ();
                final Map<FocusTracker, Boolean> componentTrackers = entry.getValue ();
                componentTrackers.remove ( focusTracker );
                if ( componentTrackers.size () == 0 )
                {
                    iterator.remove ();
                }
            }
        }
    }
}