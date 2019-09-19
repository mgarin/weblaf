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

package com.alee.utils.swing.extensions;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.managers.hotkey.HotkeyData;
import com.alee.utils.MathUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.MouseButton;

import java.awt.*;
import java.awt.event.*;

/**
 * Common implementations for {@link com.alee.utils.swing.extensions.EventMethods} interface methods.
 *
 * @author Mikle Garin
 * @see com.alee.utils.swing.extensions.EventMethods
 */
public final class EventMethodsImpl
{
    /**
     * Shortcut method for double-click mouse event.
     *
     * @param component component to handle events for
     * @param runnable  mouse event runnable
     * @return created {@link MouseAdapter}
     */
    @NotNull
    public static MouseAdapter onMousePress ( @NotNull final Component component, @NotNull final MouseEventRunnable runnable )
    {
        return onMousePress ( component, null, runnable );
    }

    /**
     * Shortcut method for double-click mouse event.
     *
     * @param component   component to handle events for
     * @param mouseButton mouse button filter
     * @param runnable    mouse event runnable
     * @return created {@link MouseAdapter}
     */
    @NotNull
    public static MouseAdapter onMousePress ( @NotNull final Component component, @Nullable final MouseButton mouseButton,
                                              @NotNull final MouseEventRunnable runnable )
    {
        final MouseAdapter mouseAdapter = new MouseAdapter ()
        {
            @Override
            public void mousePressed ( @NotNull final MouseEvent e )
            {
                if ( mouseButton == null || mouseButton == MouseButton.get ( e ) )
                {
                    runnable.run ( e );
                }
            }
        };
        component.addMouseListener ( mouseAdapter );
        return mouseAdapter;
    }

    /**
     * Shortcut method for mouse enter event.
     *
     * @param component component to handle events for
     * @param runnable  mouse event runnable
     * @return created {@link MouseAdapter}
     */
    @NotNull
    public static MouseAdapter onMouseEnter ( @NotNull final Component component, @NotNull final MouseEventRunnable runnable )
    {
        final MouseAdapter mouseAdapter = new MouseAdapter ()
        {
            @Override
            public void mouseEntered ( @NotNull final MouseEvent e )
            {
                runnable.run ( e );
            }
        };
        component.addMouseListener ( mouseAdapter );
        return mouseAdapter;
    }

    /**
     * Shortcut method for mouse exit event.
     *
     * @param component component to handle events for
     * @param runnable  mouse event runnable
     * @return created {@link MouseAdapter}
     */
    @NotNull
    public static MouseAdapter onMouseExit ( @NotNull final Component component, @NotNull final MouseEventRunnable runnable )
    {
        final MouseAdapter mouseAdapter = new MouseAdapter ()
        {
            @Override
            public void mouseExited ( @NotNull final MouseEvent e )
            {
                runnable.run ( e );
            }
        };
        component.addMouseListener ( mouseAdapter );
        return mouseAdapter;
    }

    /**
     * Shortcut method for mouse drag event.
     *
     * @param component component to handle events for
     * @param runnable  mouse event runnable
     * @return created {@link MouseAdapter}
     */
    @NotNull
    public static MouseAdapter onMouseDrag ( @NotNull final Component component, @NotNull final MouseEventRunnable runnable )
    {
        return onMouseDrag ( component, null, runnable );
    }

    /**
     * Shortcut method for mouse drag event.
     *
     * @param component   component to handle events for
     * @param mouseButton mouse button filter
     * @param runnable    mouse event runnable
     * @return created {@link MouseAdapter}
     */
    @NotNull
    public static MouseAdapter onMouseDrag ( @NotNull final Component component, @Nullable final MouseButton mouseButton,
                                             @NotNull final MouseEventRunnable runnable )
    {
        final MouseAdapter mouseAdapter = new MouseAdapter ()
        {
            @Override
            public void mouseDragged ( @NotNull final MouseEvent e )
            {
                if ( mouseButton == null || mouseButton == MouseButton.get ( e ) )
                {
                    runnable.run ( e );
                }
            }
        };
        component.addMouseMotionListener ( mouseAdapter );
        return mouseAdapter;
    }

    /**
     * Shortcut method for mouse click event.
     *
     * @param component component to handle events for
     * @param runnable  mouse event runnable
     * @return created {@link MouseAdapter}
     */
    @NotNull
    public static MouseAdapter onMouseClick ( @NotNull final Component component, @NotNull final MouseEventRunnable runnable )
    {
        return onMouseClick ( component, null, runnable );
    }

    /**
     * Shortcut method for mouse click event.
     *
     * @param component   component to handle events for
     * @param mouseButton mouse button filter
     * @param runnable    mouse event runnable
     * @return created {@link MouseAdapter}
     */
    @NotNull
    public static MouseAdapter onMouseClick ( @NotNull final Component component, @Nullable final MouseButton mouseButton,
                                              @NotNull final MouseEventRunnable runnable )
    {
        final MouseAdapter mouseAdapter = new MouseAdapter ()
        {
            @Override
            public void mouseClicked ( @NotNull final MouseEvent e )
            {
                if ( mouseButton == null || mouseButton == MouseButton.get ( e ) )
                {
                    runnable.run ( e );
                }
            }
        };
        component.addMouseListener ( mouseAdapter );
        return mouseAdapter;
    }

    /**
     * Shortcut method for double-click mouse event.
     *
     * @param component component to handle events for
     * @param runnable  mouse event runnable
     * @return created {@link MouseAdapter}
     */
    @NotNull
    public static MouseAdapter onDoubleClick ( @NotNull final Component component, @NotNull final MouseEventRunnable runnable )
    {
        final MouseAdapter mouseAdapter = new MouseAdapter ()
        {
            @Override
            public void mouseClicked ( @NotNull final MouseEvent e )
            {
                if ( SwingUtils.isDoubleClick ( e ) )
                {
                    runnable.run ( e );
                }
            }
        };
        component.addMouseListener ( mouseAdapter );
        return mouseAdapter;
    }

    /**
     * Shortcut method for mouse event triggering popup menu.
     *
     * @param component component to handle events for
     * @param runnable  mouse event runnable
     * @return created {@link MouseAdapter}
     */
    @NotNull
    public static MouseAdapter onMenuTrigger ( @NotNull final Component component, @NotNull final MouseEventRunnable runnable )
    {
        final MouseAdapter mouseAdapter = new MouseAdapter ()
        {
            @Override
            public void mousePressed ( @NotNull final MouseEvent e )
            {
                if ( SwingUtils.isRightMouseButton ( e ) )
                {
                    runnable.run ( e );
                }
            }
        };
        component.addMouseListener ( mouseAdapter );
        return mouseAdapter;
    }

    /**
     * Shortcut method for key type event.
     *
     * @param component component to handle events for
     * @param runnable  key event runnable
     * @return created {@link KeyAdapter}
     */
    @NotNull
    public static KeyAdapter onKeyType ( @NotNull final Component component, @NotNull final KeyEventRunnable runnable )
    {
        return onKeyType ( component, null, runnable );
    }

    /**
     * Shortcut method for key type event.
     *
     * @param component component to handle events for
     * @param hotkey    hotkey filter
     * @param runnable  key event runnable
     * @return created {@link KeyAdapter}
     */
    @NotNull
    public static KeyAdapter onKeyType ( @NotNull final Component component, @Nullable final HotkeyData hotkey,
                                         @NotNull final KeyEventRunnable runnable )
    {
        final KeyAdapter keyAdapter = new KeyAdapter ()
        {
            @Override
            public void keyTyped ( @NotNull final KeyEvent e )
            {
                if ( hotkey == null || hotkey.isTriggered ( e ) )
                {
                    runnable.run ( e );
                }
            }
        };
        component.addKeyListener ( keyAdapter );
        return keyAdapter;
    }

    /**
     * Shortcut method for key press event.
     *
     * @param component component to handle events for
     * @param runnable  key event runnable
     * @return created {@link KeyAdapter}
     */
    @NotNull
    public static KeyAdapter onKeyPress ( @NotNull final Component component, @NotNull final KeyEventRunnable runnable )
    {
        return onKeyPress ( component, null, runnable );
    }

    /**
     * Shortcut method for key press event.
     *
     * @param component component to handle events for
     * @param hotkey    hotkey filter
     * @param runnable  key event runnable
     * @return created {@link KeyAdapter}
     */
    @NotNull
    public static KeyAdapter onKeyPress ( @NotNull final Component component, @Nullable final HotkeyData hotkey,
                                          @NotNull final KeyEventRunnable runnable )
    {
        final KeyAdapter keyAdapter = new KeyAdapter ()
        {
            @Override
            public void keyPressed ( @NotNull final KeyEvent e )
            {
                if ( hotkey == null || hotkey.isTriggered ( e ) )
                {
                    runnable.run ( e );
                }
            }
        };
        component.addKeyListener ( keyAdapter );
        return keyAdapter;
    }

    /**
     * Shortcut method for key release event.
     *
     * @param component component to handle events for
     * @param runnable  key event runnable
     * @return created {@link KeyAdapter}
     */
    @NotNull
    public static KeyAdapter onKeyRelease ( @NotNull final Component component, @NotNull final KeyEventRunnable runnable )
    {
        return onKeyRelease ( component, null, runnable );
    }

    /**
     * Shortcut method for key release event.
     *
     * @param component component to handle events for
     * @param hotkey    hotkey filter
     * @param runnable  key event runnable
     * @return created {@link KeyAdapter}
     */
    @NotNull
    public static KeyAdapter onKeyRelease ( @NotNull final Component component, @Nullable final HotkeyData hotkey,
                                            @NotNull final KeyEventRunnable runnable )
    {
        final KeyAdapter keyAdapter = new KeyAdapter ()
        {
            @Override
            public void keyReleased ( @NotNull final KeyEvent e )
            {
                if ( hotkey == null || hotkey.isTriggered ( e ) )
                {
                    runnable.run ( e );
                }
            }
        };
        component.addKeyListener ( keyAdapter );
        return keyAdapter;
    }

    /**
     * Shortcut method for focus gain event.
     *
     * @param component component to handle events for
     * @param runnable  focus event runnable
     * @return created {@link FocusAdapter}
     */
    @NotNull
    public static FocusAdapter onFocusGain ( @NotNull final Component component, @NotNull final FocusEventRunnable runnable )
    {
        final FocusAdapter focusAdapter = new FocusAdapter ()
        {
            @Override
            public void focusGained ( @NotNull final FocusEvent e )
            {
                runnable.run ( e );
            }
        };
        component.addFocusListener ( focusAdapter );
        return focusAdapter;
    }

    /**
     * Shortcut method for focus loss event.
     *
     * @param component component to handle events for
     * @param runnable  focus event runnable
     * @return created {@link FocusAdapter}
     */
    @NotNull
    public static FocusAdapter onFocusLoss ( @NotNull final Component component, @NotNull final FocusEventRunnable runnable )
    {
        final FocusAdapter focusAdapter = new FocusAdapter ()
        {
            @Override
            public void focusLost ( @NotNull final FocusEvent e )
            {
                runnable.run ( e );
            }
        };
        component.addFocusListener ( focusAdapter );
        return focusAdapter;
    }

    /**
     * Shortcut method for drag start.
     * This is a special event that requires a sequence of conditions to be triggered.
     * This event will also be only triggered once per drag operation.
     *
     * @param component component to handle events for
     * @param shift     coordinate shift required to start drag
     * @param runnable  mouse event runnable
     * @return created {@link MouseAdapter}
     */
    @NotNull
    public static MouseAdapter onDragStart ( @NotNull final Component component, final int shift,
                                             @NotNull final MouseEventRunnable runnable )
    {
        return onDragStart ( component, shift, MouseButton.left, runnable );
    }

    /**
     * Shortcut method for drag start.
     * This is a special event that requires a sequence of conditions to be triggered.
     * This event will also be only triggered once per drag operation.
     *
     * @param component   component to handle events for
     * @param shift       coordinate shift required to start drag
     * @param mouseButton mouse button filter
     * @param runnable    mouse event runnable
     * @return created {@link MouseAdapter}
     */
    @NotNull
    public static MouseAdapter onDragStart ( @NotNull final Component component, final int shift, @Nullable final MouseButton mouseButton,
                                             @NotNull final MouseEventRunnable runnable )
    {
        final MouseAdapter mouseAdapter = new MouseAdapter ()
        {
            private Point start = null;

            @Override
            public void mousePressed ( @NotNull final MouseEvent e )
            {
                if ( isButton ( e ) )
                {
                    start = e.getPoint ();
                }
            }

            @Override
            public void mouseDragged ( @NotNull final MouseEvent e )
            {
                if ( start != null && MathUtils.distance ( start, e.getPoint () ) > shift )
                {
                    runnable.run ( e );
                    start = null;
                }
            }

            @Override
            public void mouseReleased ( @NotNull final MouseEvent e )
            {
                if ( isButton ( e ) && start != null )
                {
                    start = null;
                }
            }

            /**
             * Returns whether or not event fits the button requirement.
             *
             * @param e mouse event
             * @return true if event fits the button requirement, false otherwise
             */
            private boolean isButton ( @NotNull final MouseEvent e )
            {
                return mouseButton == null || mouseButton == MouseButton.get ( e );
            }
        };
        component.addMouseListener ( mouseAdapter );
        component.addMouseMotionListener ( mouseAdapter );
        return mouseAdapter;
    }
}