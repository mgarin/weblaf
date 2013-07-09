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

import com.alee.laf.StyleConstants;
import com.alee.utils.CollectionUtils;
import com.alee.utils.SwingUtils;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: mgarin Date: 21.08.11 Time: 18:12
 * <p/>
 * This manager allows you to track certain component (and its subcomponents if stated so) focus state by implementing either FocusTracker
 * interface or adding global focus listeners
 */

public class FocusManager
{
    private static boolean initialized = false;

    private static List<FocusTracker> trackedList = new ArrayList<FocusTracker> ();
    private static Map<Component, Boolean> focusCache = new HashMap<Component, Boolean> ();

    private static List<GlobalFocusListener> globalFocusListeners = new ArrayList<GlobalFocusListener> ();

    private static Component oldFocusOwner;
    private static Component focusOwner;

    /**
     * FocusManager initialization
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
                public void eventDispatched ( AWTEvent event )
                {
                    if ( event instanceof FocusEvent )
                    {
                        if ( globalFocusListeners.size () > 0 )
                        {
                            FocusEvent focusEvent = ( FocusEvent ) event;
                            if ( focusEvent.getID () == FocusEvent.FOCUS_LOST && focusEvent.getOppositeComponent () == null )
                            {
                                // Focus now outside the window
                                fireGlobalFocusChanged ( focusEvent.getComponent (), null );
                            }
                            else if ( focusEvent.getID () == FocusEvent.FOCUS_GAINED )
                            {
                                // Focus now in window (might have just entered)
                                fireGlobalFocusChanged ( focusEvent.getOppositeComponent (), focusEvent.getComponent () );
                            }
                        }
                    }
                }
            }, AWTEvent.FOCUS_EVENT_MASK );

            // Adding global focus tracker
            registerGlobalFocusListener ( new GlobalFocusListener ()
            {
                public void focusChanged ( Component oldFocus, Component newFocus )
                {
                    oldFocusOwner = oldFocus;
                    focusOwner = newFocus;

                    // Debug info
                    if ( StyleConstants.DEBUG )
                    {
                        String oldName = oldFocus != null ? oldFocus.getClass ().getName () : null;
                        String newName = newFocus != null ? newFocus.getClass ().getName () : null;
                        System.out.println ( "Focus changed: " + oldName + " --> " + newName );
                    }

                    // Checking all added trackers
                    for ( FocusTracker focusTracker : CollectionUtils.copy ( trackedList ) )
                    {
                        // Skip if tracker is disabled
                        if ( focusTracker.isTrackingEnabled () )
                        {
                            // Determining focus owner
                            Component tracked = focusTracker.getTrackedComponent ();
                            boolean focusOwner = focusTracker.isUniteWithChilds () ? SwingUtils.isEqualOrChild ( tracked, newFocus ) :
                                    tracked == newFocus;

                            // Inform type
                            if ( focusTracker.isListenGlobalChange () )
                            {
                                // Firing tracker even if focus hasn't changed for component
                                focusTracker.focusChanged ( focusOwner );
                            }
                            else
                            {
                                // Firing tracker if focus has actually changed
                                if ( getCachedFocusOwnerState ( tracked ) != focusOwner )
                                {
                                    focusTracker.focusChanged ( focusOwner );
                                }
                            }

                            // Caching focus state
                            focusCache.put ( tracked, focusOwner );
                        }
                    }
                }
            } );

            //            KeyboardFocusManager focusManager =
            //                    KeyboardFocusManager.getCurrentKeyboardFocusManager ();
            //            focusManager.addPropertyChangeListener ( new PropertyChangeListener ()
            //            {
            //                public void propertyChange ( PropertyChangeEvent e )
            //                {
            //                    // Check only if there is anything to track
            //                    if ( trackedList.size () > 0 )
            //                    {
            //                        // Listening only to focus changes
            //                        String prop = e.getPropertyName ();
            //                        if ( ( "focusOwner".equals ( prop ) ) )
            //                        {
            //                            // New focus owner
            //                            Component comp = ( Component ) e.getNewValue ();
            //
            //                            // Checking all added trackers
            //                            List<FocusTracker> clonedTrackedList = new ArrayList<FocusTracker> ();
            //                            clonedTrackedList.addAll ( trackedList );
            //                            for ( FocusTracker focusTracker : clonedTrackedList )
            //                            {
            //                                // Determining focus owner
            //                                Component tracked = focusTracker.getTrackedComponent ();
            //                                boolean focusOwner = focusTracker.isUniteWithChilds () ?
            //                                        SwingUtils.isEqualOrChild ( tracked, comp ) :
            //                                        tracked == comp;
            //
            //                                // Firing tracker if focus has actually changed
            //                                if ( focusTracker.isFocusOwner () != focusOwner )
            //                                {
            //                                    focusTracker.focusChanged ( focusOwner );
            //                                }
            //                            }
            //                        }
            //                    }
            //                }
            //            } );
        }
    }

    private static Boolean getCachedFocusOwnerState ( Component tracked )
    {
        return focusCache.containsKey ( tracked ) ? focusCache.get ( tracked ) : false;
    }

    /**
     * Application focus owner component
     */

    public static Component getFocusOwner ()
    {
        return focusOwner;
    }

    /**
     * Application previous focus owner component
     */

    public static Component getOldFocusOwner ()
    {
        return oldFocusOwner;
    }

    /**
     * New GlobalFocusListener registration
     */

    public static void registerGlobalFocusListener ( GlobalFocusListener listener )
    {
        globalFocusListeners.add ( listener );
    }

    /**
     * GlobalFocusListener removal
     */

    public static void unregisterGlobalFocusListener ( GlobalFocusListener listener )
    {
        globalFocusListeners.remove ( listener );
    }

    /**
     * Global focus change notify
     */

    private static void fireGlobalFocusChanged ( Component oldComponent, Component newComponent )
    {
        for ( GlobalFocusListener gfl : CollectionUtils.copy ( globalFocusListeners ) )
        {
            gfl.focusChanged ( oldComponent, newComponent );
        }
    }

    /**
     * New FocusTracker registration
     */

    public static void registerFocusTracker ( FocusTracker focusTracker )
    {
        trackedList.add ( focusTracker );
    }

    /**
     * FocusTracker removal method
     */

    public static void unregisterFocusTracker ( FocusTracker focusTracker )
    {
        trackedList.remove ( focusTracker );
    }
}
