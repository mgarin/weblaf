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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
import com.alee.extended.behavior.Behavior;
import com.alee.extended.behavior.BehaviorException;
import com.alee.managers.hotkey.Hotkey;
import com.alee.utils.CoreSwingUtils;

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
     * {@link ComponentHighlighter} used to highlight hovered components.
     */
    @Nullable
    protected ComponentHighlighter hoverHighlighter;

    /**
     * {@link AWTEventListener} .
     */
    @Nullable
    protected AWTEventListener awtEventListener;

    /**
     * Root component for this highlighter.
     */
    @Nullable
    protected Component root;

    /**
     * Inspection listener.
     */
    @Nullable
    protected InspectionListener listener;

    /**
     * Returns whether or not this {@link ComponentInspectBehavior} is already installed.
     *
     * @return {@code true} if this {@link ComponentInspectBehavior} is already installed, {@code false} otherwise
     */
    public boolean isInstalled ()
    {
        return hoverHighlighter != null;
    }

    /**
     * Installs {@link ComponentInspectBehavior} in the specified root {@link Component}.
     * In case {@link Component} is {@code null} behavior will be able to inspect any element on any window.
     *
     * @param root     root {@link Component}
     * @param listener {@link InspectionListener}
     */
    public void install ( @Nullable final Component root, @NotNull final InspectionListener listener )
    {
        if ( !isInstalled () )
        {
            // Saving root component
            this.root = root;

            // Saving inspection listener
            this.listener = listener;

            // Creating hover highlighter
            this.hoverHighlighter = new ComponentHighlighter ();

            // Adding listeners
            // todo Replace with HoverManager usage?
            awtEventListener = new AWTEventListener ()
            {
                @Override
                public void eventDispatched ( @NotNull final AWTEvent event )
                {
                    final int eventId = event.getID ();
                    if ( event instanceof MouseEvent )
                    {
                        // Limiting affected area of UI elements by the root
                        final MouseEvent mouseEvent = ( MouseEvent ) event;
                        final Component source = ( Component ) event.getSource ();
                        final Component component = CoreSwingUtils.getTopComponentAt ( source, mouseEvent.getPoint () );
                        if ( component != null && component.isShowing () && ( ComponentInspectBehavior.this.root == null ||
                                CoreSwingUtils.isAncestorOf ( ComponentInspectBehavior.this.root, component ) ) )
                        {
                            // Performing on-event actions
                            if ( Objects.equals ( eventId, MouseEvent.MOUSE_PRESSED ) )
                            {
                                // Firing events
                                ComponentInspectBehavior.this.listener.inspected ( component );

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
                            ComponentInspectBehavior.this.listener.cancelled ();

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
        else
        {
            throw new BehaviorException ( "ComponentInspectBehavior is already installed" );
        }
    }

    /**
     * Uninstalls {@link ComponentInspectBehavior}.
     */
    public void uninstall ()
    {
        if ( isInstalled () )
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
        else
        {
            throw new BehaviorException ( "ComponentInspectBehavior is not installed" );
        }
    }

    /**
     * Displays hover {@link ComponentHighlighter}.
     * If another {@link ComponentHighlighter} was already displaed it will be hidden.
     *
     * @param component component to display {@link ComponentHighlighter} for
     */
    protected void showInspector ( @NotNull final Component component )
    {
        if ( isInstalled () )
        {
            hideInspector ();
            hoverHighlighter.install ( component );
        }
        else
        {
            throw new BehaviorException ( "ComponentInspectBehavior is not installed" );
        }
    }

    /**
     * Hides displayed {@link ComponentHighlighter}.
     */
    protected void hideInspector ()
    {
        if ( isInstalled () )
        {
            hoverHighlighter.uninstall ();
        }
        else
        {
            throw new BehaviorException ( "ComponentInspectBehavior is not installed" );
        }
    }
}