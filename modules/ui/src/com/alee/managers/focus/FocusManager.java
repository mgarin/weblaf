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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
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
    @NotNull
    private static final List<GlobalFocusListener> globalFocusListeners = new ArrayList<GlobalFocusListener> ( 5 );

    /**
     * {@link GlobalFocusListener}s registered for specific {@link JComponent}s.
     */
    @NotNull
    private static final WeakComponentDataList<JComponent, GlobalFocusListener> globalComponentFocusListeners =
            new WeakComponentDataList<JComponent, GlobalFocusListener> ( "FocusManager.GlobalFocusListener", 5 );

    /**
     * {@link FocusTracker}s registered for specific {@link JComponent}s.
     */
    @NotNull
    private static final WeakComponentDataList<JComponent, FocusTracker> trackers =
            new WeakComponentDataList<JComponent, FocusTracker> ( "FocusManager.FocusTracker", 200 );

    /**
     * Reference to previously focused component.
     */
    private static WeakReference<Component> previousFocusOwner;

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

            // Initial values
            previousFocusOwner = new WeakReference<Component> ( null );
            focusOwner = new WeakReference<Component> ( javax.swing.FocusManager.getCurrentManager ().getFocusOwner () );

            // AWT event listener to fire global focus change events
            Toolkit.getDefaultToolkit ().addAWTEventListener ( new AWTEventListener ()
            {
                @Override
                public void eventDispatched ( final AWTEvent event )
                {
                    final FocusEvent focusEvent = ( FocusEvent ) event;
                    if ( focusEvent.getID () == FocusEvent.FOCUS_LOST && focusEvent.getOppositeComponent () == null )
                    {
                        fireFocusChanged (
                                focusEvent.getComponent (),
                                null
                        );
                    }
                    else if ( focusEvent.getID () == FocusEvent.FOCUS_GAINED )
                    {
                        fireFocusChanged (
                                focusEvent.getOppositeComponent (),
                                focusEvent.getComponent ()
                        );
                    }
                }
            }, AWTEvent.FOCUS_EVENT_MASK );
        }
    }

    /**
     * Returns previously focused component.
     *
     * @return previously focused component
     */
    @Nullable
    public static Component getPreviousFocusOwner ()
    {
        return previousFocusOwner.get ();
    }

    /**
     * Returns currently focused component.
     *
     * @return currently focused component
     */
    @Nullable
    public static Component getFocusOwner ()
    {
        return focusOwner != null ? focusOwner.get () : null;
    }

    /**
     * Registers new {@link GlobalFocusListener}.
     * Use these listeners with care as they are not tied to anything and will remain in memory as long as you keep them registered.
     *
     * @param listener {@link GlobalFocusListener} to register
     */
    public static void registerGlobalFocusListener ( @NotNull final GlobalFocusListener listener )
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
    public static void unregisterGlobalFocusListener ( @NotNull final GlobalFocusListener listener )
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
    public static void registerGlobalFocusListener ( @NotNull final JComponent component, @NotNull final GlobalFocusListener listener )
    {
        globalComponentFocusListeners.add ( component, listener );
    }

    /**
     * Unregisters {@link GlobalFocusListener}.
     *
     * @param component {@link JComponent} to unregister {@link GlobalFocusListener} from
     * @param listener  {@link GlobalFocusListener} to unregister
     */
    public static void unregisterGlobalFocusListener ( @NotNull final JComponent component, @NotNull final GlobalFocusListener listener )
    {
        globalComponentFocusListeners.remove ( component, listener );
    }

    /**
     * Registers focus tracker.
     *
     * @param component    component to add tracker for
     * @param focusTracker new focus tracker
     */
    public static void addFocusTracker ( @NotNull final JComponent component, @NotNull final FocusTracker focusTracker )
    {
        trackers.add ( component, focusTracker );
    }

    /**
     * Unregisters specified focus tracker.
     *
     * @param component    component to remove tracker from
     * @param focusTracker focus tracker to unregister
     */
    public static void removeFocusTracker ( @NotNull final JComponent component, @NotNull final FocusTracker focusTracker )
    {
        trackers.remove ( component, focusTracker );
    }

    /**
     * Unregisters all focus trackers from the specified component.
     *
     * @param component component to unregister all focus trackers from
     */
    public static void removeFocusTrackers ( @NotNull final JComponent component )
    {
        trackers.clear ( component );
    }

    /**
     * Fires {@link GlobalFocusListener#focusChanged(Component, Component)} event.
     *
     * @param oldFocus previously focused {@link Component}
     * @param newFocus currently focused {@link Component}
     */
    private static void fireFocusChanged ( @Nullable final Component oldFocus, @Nullable final Component newFocus )
    {
        // Updating weak references
        previousFocusOwner = new WeakReference<Component> ( oldFocus );
        focusOwner = new WeakReference<Component> ( newFocus );

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

        // Firing global component listeners
        globalComponentFocusListeners.forEachData ( new BiConsumer<JComponent, GlobalFocusListener> ()
        {
            @Override
            public void accept ( final JComponent component, final GlobalFocusListener listener )
            {
                listener.focusChanged ( oldFocus, newFocus );
            }
        } );

        // Firing global listeners
        final ImmutableList<GlobalFocusListener> globalListenersCopy;
        synchronized ( globalFocusListeners )
        {
            globalListenersCopy = new ImmutableList<GlobalFocusListener> ( FocusManager.globalFocusListeners );
        }
        for ( final GlobalFocusListener listener : globalListenersCopy )
        {
            listener.focusChanged ( oldFocus, newFocus );
        }
    }
}