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

package com.alee.extended.inspector;

import com.alee.api.jdk.Objects;
import com.alee.extended.behavior.Behavior;
import com.alee.managers.hotkey.Hotkey;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.SwingUtils;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Custom behavior using {@link ComponentHighlighter} to highlight any {@link java.awt.Component} user hovers on the application UI.
 * Upon mouse press behavior will inform about component selection and {@link #uninstall()} right away.
 * It can also be cancelled by pressing ESCAPE hotkey which will be listened within affected area.
 * Note that this behavior will consume all mouse events until it is uninstalled.
 *
 * @author Mikle Garin
 */
public final class ComponentInspectBehavior implements Behavior
{
    /**
     * Component inspector used to highlight hovered components.
     */
    protected ComponentHighlighter hoverHighlighter;

    /**
     * Events listener.
     */
    protected AWTEventListener awtEventListener;

    /**
     * Root component for this highlighter.
     */
    protected Component root;

    /**
     * Inspection listener.
     */
    protected InspectionListener listener;

    /**
     * Installs component inspect behavior in the specified root component.
     *
     * @param root     root component
     * @param listener inspection listener
     */
    public void install ( final Component root, final InspectionListener listener )
    {
        // Saving root component
        this.root = root;

        // Saving inspection listener
        this.listener = listener;

        // Creating hover highlighter
        this.hoverHighlighter = new ComponentHighlighter ();

        // Adding listeners
        awtEventListener = new AWTEventListener ()
        {
            @Override
            public void eventDispatched ( final AWTEvent event )
            {
                final int eventId = event.getID ();
                if ( event instanceof MouseEvent )
                {
                    // Limiting affected area of UI elements by the root
                    final MouseEvent mouseEvent = ( MouseEvent ) event;
                    final Component source = ( Component ) event.getSource ();
                    final Component component = SwingUtils.getTopComponentAt ( source, mouseEvent.getPoint () );
                    if ( component != null && component.isShowing () && ( ComponentInspectBehavior.this.root == null ||
                            CoreSwingUtils.isAncestorOf ( ComponentInspectBehavior.this.root, component ) ) )
                    {
                        // Performing on-event actions
                        if ( Objects.equals ( eventId, MouseEvent.MOUSE_PRESSED ) )
                        {
                            // Firing events
                            if ( component != null )
                            {
                                listener.inspected ( component );
                            }
                            else
                            {
                                listener.cancelled ();
                            }

                            // Uninstalling behavior
                            uninstall ();
                        }
                        else if ( Objects.equals ( eventId, MouseEvent.MOUSE_ENTERED, MouseEvent.MOUSE_MOVED ) )
                        {
                            // Checking that hovered component has actually changed
                            if ( hoverHighlighter != null && hoverHighlighter.getComponent () != component )
                            {
                                // Displaying inspector for newly hovered component
                                showInspector ( component );
                            }
                        }
                        else if ( Objects.equals ( eventId, MouseEvent.MOUSE_EXITED ) )
                        {
                            // Hiding inspector
                            hideInspector ();
                        }

                        // Consuming event to avoid it being processed by someone else
                        mouseEvent.consume ();
                    }
                }
                else if ( event instanceof KeyEvent )
                {
                    // Uninstalling behavior on ESCAPE press
                    final KeyEvent keyEvent = ( KeyEvent ) event;
                    if ( Objects.equals ( eventId, KeyEvent.KEY_PRESSED ) && Hotkey.ESCAPE.isTriggered ( keyEvent ) )
                    {
                        // Firing events
                        listener.cancelled ();

                        // Uninstalling behavior
                        uninstall ();
                    }

                    // Consuming event to avoid it being processed by someone else
                    keyEvent.consume ();
                }
            }
        };
        Toolkit.getDefaultToolkit ().addAWTEventListener ( awtEventListener, AWTEvent.MOUSE_EVENT_MASK );
        Toolkit.getDefaultToolkit ().addAWTEventListener ( awtEventListener, AWTEvent.MOUSE_MOTION_EVENT_MASK );
        Toolkit.getDefaultToolkit ().addAWTEventListener ( awtEventListener, AWTEvent.KEY_EVENT_MASK );
    }

    /**
     * Uninstalls component inspect behavior.
     */
    public void uninstall ()
    {
        // Removing listeners
        Toolkit.getDefaultToolkit ().removeAWTEventListener ( awtEventListener );

        // Hiding inspector
        hideInspector ();

        // Cleaning up
        this.hoverHighlighter = null;
        this.listener = null;
        this.root = null;
    }

    /**
     * Displays hover {@link ComponentHighlighter}.
     * If another {@link ComponentHighlighter} was already displaed it will be hidden.
     *
     * @param component component to display {@link ComponentHighlighter} for
     */
    protected void showInspector ( final Component component )
    {
        hideInspector ();
        hoverHighlighter.install ( component );
    }

    /**
     * Hides displayed {@link ComponentHighlighter}.
     */
    protected void hideInspector ()
    {
        hoverHighlighter.uninstall ();
    }
}