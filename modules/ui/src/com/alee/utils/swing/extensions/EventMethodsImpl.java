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
     * @return used mouse adapter
     */
    public static MouseAdapter onMousePress ( final Component component, final MouseEventRunnable runnable )
    {
        return onMousePress ( component, null, runnable );
    }

    /**
     * Shortcut method for double-click mouse event.
     *
     * @param component   component to handle events for
     * @param mouseButton mouse button filter
     * @param runnable    mouse event runnable
     * @return used mouse adapter
     */
    public static MouseAdapter onMousePress ( final Component component, final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        final MouseAdapter mouseAdapter = new MouseAdapter ()
        {
            @Override
            public void mousePressed ( final MouseEvent e )
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
     * @return used mouse adapter
     */
    public static MouseAdapter onMouseEnter ( final Component component, final MouseEventRunnable runnable )
    {
        final MouseAdapter mouseAdapter = new MouseAdapter ()
        {
            @Override
            public void mouseEntered ( final MouseEvent e )
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
     * @return used mouse adapter
     */
    public static MouseAdapter onMouseExit ( final Component component, final MouseEventRunnable runnable )
    {
        final MouseAdapter mouseAdapter = new MouseAdapter ()
        {
            @Override
            public void mouseExited ( final MouseEvent e )
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
     * @return used mouse adapter
     */
    public static MouseAdapter onMouseDrag ( final Component component, final MouseEventRunnable runnable )
    {
        return onMouseDrag ( component, null, runnable );
    }

    /**
     * Shortcut method for mouse drag event.
     *
     * @param component   component to handle events for
     * @param mouseButton mouse button filter
     * @param runnable    mouse event runnable
     * @return used mouse adapter
     */
    public static MouseAdapter onMouseDrag ( final Component component, final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        final MouseAdapter mouseAdapter = new MouseAdapter ()
        {
            @Override
            public void mouseDragged ( final MouseEvent e )
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
     * @return used mouse adapter
     */
    public static MouseAdapter onMouseClick ( final Component component, final MouseEventRunnable runnable )
    {
        return onMouseClick ( component, null, runnable );
    }

    /**
     * Shortcut method for mouse click event.
     *
     * @param component   component to handle events for
     * @param mouseButton mouse button filter
     * @param runnable    mouse event runnable
     * @return used mouse adapter
     */
    public static MouseAdapter onMouseClick ( final Component component, final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        final MouseAdapter mouseAdapter = new MouseAdapter ()
        {
            @Override
            public void mouseClicked ( final MouseEvent e )
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
     * @return used mouse adapter
     */
    public static MouseAdapter onDoubleClick ( final Component component, final MouseEventRunnable runnable )
    {
        final MouseAdapter mouseAdapter = new MouseAdapter ()
        {
            @Override
            public void mouseClicked ( final MouseEvent e )
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
     * @return used mouse adapter
     */
    public static MouseAdapter onMenuTrigger ( final Component component, final MouseEventRunnable runnable )
    {
        final MouseAdapter mouseAdapter = new MouseAdapter ()
        {
            @Override
            public void mousePressed ( final MouseEvent e )
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
     * @return used key adapter
     */
    public static KeyAdapter onKeyType ( final Component component, final KeyEventRunnable runnable )
    {
        return onKeyType ( component, null, runnable );
    }

    /**
     * Shortcut method for key type event.
     *
     * @param component component to handle events for
     * @param hotkey    hotkey filter
     * @param runnable  key event runnable
     * @return used key adapter
     */
    public static KeyAdapter onKeyType ( final Component component, final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        final KeyAdapter keyAdapter = new KeyAdapter ()
        {
            @Override
            public void keyTyped ( final KeyEvent e )
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
     * @return used key adapter
     */
    public static KeyAdapter onKeyPress ( final Component component, final KeyEventRunnable runnable )
    {
        return onKeyPress ( component, null, runnable );
    }

    /**
     * Shortcut method for key press event.
     *
     * @param component component to handle events for
     * @param hotkey    hotkey filter
     * @param runnable  key event runnable
     * @return used key adapter
     */
    public static KeyAdapter onKeyPress ( final Component component, final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        final KeyAdapter keyAdapter = new KeyAdapter ()
        {
            @Override
            public void keyPressed ( final KeyEvent e )
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
     * @return used key adapter
     */
    public static KeyAdapter onKeyRelease ( final Component component, final KeyEventRunnable runnable )
    {
        return onKeyRelease ( component, null, runnable );
    }

    /**
     * Shortcut method for key release event.
     *
     * @param component component to handle events for
     * @param hotkey    hotkey filter
     * @param runnable  key event runnable
     * @return used key adapter
     */
    public static KeyAdapter onKeyRelease ( final Component component, final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        final KeyAdapter keyAdapter = new KeyAdapter ()
        {
            @Override
            public void keyReleased ( final KeyEvent e )
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
     * @return used focus adapter
     */
    public static FocusAdapter onFocusGain ( final Component component, final FocusEventRunnable runnable )
    {
        final FocusAdapter focusAdapter = new FocusAdapter ()
        {
            @Override
            public void focusGained ( final FocusEvent e )
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
     * @return used focus adapter
     */
    public static FocusAdapter onFocusLoss ( final Component component, final FocusEventRunnable runnable )
    {
        final FocusAdapter focusAdapter = new FocusAdapter ()
        {
            @Override
            public void focusLost ( final FocusEvent e )
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
     * @return used mouse adapter
     */
    public static MouseAdapter onDragStart ( final Component component, final int shift, final MouseEventRunnable runnable )
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
     * @return used mouse adapter
     */
    public static MouseAdapter onDragStart ( final Component component, final int shift, final MouseButton mouseButton,
                                             final MouseEventRunnable runnable )
    {
        final MouseAdapter mouseAdapter = new MouseAdapter ()
        {
            private Point start = null;

            @Override
            public void mousePressed ( final MouseEvent e )
            {
                if ( isButton ( e ) )
                {
                    start = e.getPoint ();
                }
            }

            @Override
            public void mouseDragged ( final MouseEvent e )
            {
                if ( start != null && MathUtils.distance ( start, e.getPoint () ) > shift )
                {
                    runnable.run ( e );
                    start = null;
                }
            }

            @Override
            public void mouseReleased ( final MouseEvent e )
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
            private boolean isButton ( final MouseEvent e )
            {
                return mouseButton == null || mouseButton == MouseButton.get ( e );
            }
        };
        component.addMouseListener ( mouseAdapter );
        component.addMouseMotionListener ( mouseAdapter );
        return mouseAdapter;
    }
}