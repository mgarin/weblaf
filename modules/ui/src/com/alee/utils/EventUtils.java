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

package com.alee.utils;

import com.alee.extended.tab.*;
import com.alee.extended.window.PopOverAdapter;
import com.alee.extended.window.PopOverEventRunnable;
import com.alee.extended.window.WebPopOver;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.hotkey.HotkeyData;
import com.alee.utils.general.Pair;
import com.alee.utils.swing.*;

import javax.swing.event.DocumentEvent;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * This class provides a set of utilities to work with various Swing events.
 *
 * @author Mikle Garin
 */

public final class EventUtils
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
                if ( SwingUtils.isLeftMouseButton ( e ) && e.getClickCount () == 2 )
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
     * Shortcut method for window closing event.
     *
     * @param window   window to handle events for
     * @param runnable window event runnable
     * @return used window adapter
     */
    public static WindowAdapter onClosing ( final Window window, final WindowEventRunnable runnable )
    {
        final WindowAdapter windowAdapter = new WindowAdapter ()
        {
            @Override
            public void windowClosing ( final WindowEvent e )
            {
                runnable.run ( e );
            }
        };
        window.addWindowListener ( windowAdapter );
        return windowAdapter;
    }

    /**
     * Shortcut method for window close event.
     *
     * @param window   window to handle events for
     * @param runnable component event runnable
     * @return used window close adapter
     */
    public static WindowCloseAdapter onClose ( final Window window, final ComponentEventRunnable runnable )
    {
        final WindowCloseAdapter windowAdapter = new WindowCloseAdapter ()
        {
            @Override
            public void closed ( final ComponentEvent e )
            {
                runnable.run ( e );
            }
        };
        window.addWindowListener ( windowAdapter );
        window.addComponentListener ( windowAdapter );
        return windowAdapter;
    }

    /**
     * Shortcut method for document change event.
     *
     * @param textComponent text component to handle events for
     * @param runnable      document event runnable
     * @return used document change and property change listeners
     */
    public static Pair<DocumentChangeListener, PropertyChangeListener> onChange ( final JTextComponent textComponent,
                                                                                  final DocumentEventRunnable runnable )
    {
        // Listening to document content changes
        final DocumentChangeListener documentChangeListener = new DocumentChangeListener ()
        {
            @Override
            public void documentChanged ( final DocumentEvent e )
            {
                runnable.run ( e );
            }
        };
        textComponent.getDocument ().addDocumentListener ( documentChangeListener );

        // Listening to component document changes
        final PropertyChangeListener propertyChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent e )
            {
                final Object oldDocument = e.getOldValue ();
                if ( oldDocument != null && oldDocument instanceof Document )
                {
                    ( ( Document ) oldDocument ).removeDocumentListener ( documentChangeListener );
                }
                final Object newDocument = e.getNewValue ();
                if ( newDocument != null && newDocument instanceof Document )
                {
                    ( ( Document ) newDocument ).addDocumentListener ( documentChangeListener );
                }
            }
        };
        textComponent.addPropertyChangeListener ( WebLookAndFeel.DOCUMENT_PROPERTY, propertyChangeListener );

        return new Pair<DocumentChangeListener, PropertyChangeListener> ( documentChangeListener, propertyChangeListener );
    }

    /**
     * Shortcut method for popover open event.
     *
     * @param popOver  popover to handle events for
     * @param runnable popover event runnable
     * @return used popover adapter
     */
    public static PopOverAdapter onOpen ( final WebPopOver popOver, final PopOverEventRunnable runnable )
    {
        final PopOverAdapter popOverAdapter = new PopOverAdapter ()
        {
            @Override
            public void opened ( final WebPopOver popOver )
            {
                runnable.run ( popOver );
            }
        };
        popOver.addPopOverListener ( popOverAdapter );
        return popOverAdapter;
    }

    /**
     * Shortcut method for popover reopen event.
     *
     * @param popOver  popover to handle events for
     * @param runnable popover event runnable
     * @return used popover adapter
     */
    public static PopOverAdapter onReopen ( final WebPopOver popOver, final PopOverEventRunnable runnable )
    {
        final PopOverAdapter popOverAdapter = new PopOverAdapter ()
        {
            @Override
            public void reopened ( final WebPopOver popOver )
            {
                runnable.run ( popOver );
            }
        };
        popOver.addPopOverListener ( popOverAdapter );
        return popOverAdapter;
    }

    /**
     * Shortcut method for popover detach event.
     *
     * @param popOver  popover to handle events for
     * @param runnable popover event runnable
     * @return used popover adapter
     */
    public static PopOverAdapter onDetach ( final WebPopOver popOver, final PopOverEventRunnable runnable )
    {
        final PopOverAdapter popOverAdapter = new PopOverAdapter ()
        {
            @Override
            public void detached ( final WebPopOver popOver )
            {
                runnable.run ( popOver );
            }
        };
        popOver.addPopOverListener ( popOverAdapter );
        return popOverAdapter;
    }

    /**
     * Shortcut method for popover close event.
     *
     * @param popOver  popover to handle events for
     * @param runnable popover event runnable
     * @return used popover adapter
     */
    public static PopOverAdapter onClose ( final WebPopOver popOver, final PopOverEventRunnable runnable )
    {
        final PopOverAdapter popOverAdapter = new PopOverAdapter ()
        {
            @Override
            public void closed ( final WebPopOver popOver )
            {
                runnable.run ( popOver );
            }
        };
        popOver.addPopOverListener ( popOverAdapter );
        return popOverAdapter;
    }

    /**
     * Shortcut method for document open event.
     *
     * @param documentPane document pane to handle events for
     * @param runnable     document data runnable
     * @return used document adapter
     */
    public static <T extends DocumentData> DocumentAdapter<T> onDocumentOpen ( final WebDocumentPane<T> documentPane,
                                                                               final DocumentDataRunnable<T> runnable )
    {
        final DocumentAdapter<T> documentAdapter = new DocumentAdapter<T> ()
        {
            @Override
            public void opened ( final T document, final PaneData<T> pane, final int index )
            {
                runnable.run ( document, pane, index );
            }
        };
        documentPane.addDocumentListener ( documentAdapter );
        return documentAdapter;
    }

    /**
     * Shortcut method for document selection event.
     *
     * @param documentPane document pane to handle events for
     * @param runnable     document data runnable
     * @return used document adapter
     */
    public static <T extends DocumentData> DocumentAdapter<T> onDocumentSelection ( final WebDocumentPane<T> documentPane,
                                                                                    final DocumentDataRunnable<T> runnable )
    {
        final DocumentAdapter<T> documentAdapter = new DocumentAdapter<T> ()
        {
            @Override
            public void selected ( final T document, final PaneData<T> pane, final int index )
            {
                runnable.run ( document, pane, index );
            }
        };
        documentPane.addDocumentListener ( documentAdapter );
        return documentAdapter;
    }

    /**
     * Shortcut method for document closing event.
     *
     * @param documentPane document pane to handle events for
     * @param runnable     document data cancellable runnable
     * @return used document adapter
     */
    public static <T extends DocumentData> DocumentAdapter<T> onDocumentClosing ( final WebDocumentPane<T> documentPane,
                                                                                  final DocumentDataCancellableRunnable<T> runnable )
    {
        final DocumentAdapter<T> documentAdapter = new DocumentAdapter<T> ()
        {
            @Override
            public boolean closing ( final T document, final PaneData<T> pane, final int index )
            {
                return runnable.run ( document, pane, index );
            }
        };
        documentPane.addDocumentListener ( documentAdapter );
        return documentAdapter;
    }

    /**
     * Shortcut method for document close event.
     *
     * @param documentPane document pane to handle events for
     * @param runnable     document data runnable
     * @return used document adapter
     */
    public static <T extends DocumentData> DocumentAdapter<T> onDocumentClose ( final WebDocumentPane<T> documentPane,
                                                                                final DocumentDataRunnable<T> runnable )
    {
        final DocumentAdapter<T> documentAdapter = new DocumentAdapter<T> ()
        {
            @Override
            public void closed ( final T document, final PaneData<T> pane, final int index )
            {
                runnable.run ( document, pane, index );
            }
        };
        documentPane.addDocumentListener ( documentAdapter );
        return documentAdapter;
    }
}