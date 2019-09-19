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

package com.alee.laf.desktoppane;

import com.alee.api.annotations.NotNull;
import com.alee.api.jdk.Objects;
import com.alee.laf.AbstractUIInputListener;
import com.alee.laf.UIAction;
import com.alee.laf.UIActionMap;
import com.alee.utils.LafLookup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;

/**
 * Basic UI input listener for {@link JDesktopPane} implementation.
 * It is partially based on Swing {@link javax.swing.plaf.basic.BasicDesktopPaneUI} but cleaned up and optimized.
 *
 * @param <C> {@link JDesktopPane} type
 * @param <U> {@link WDesktopPaneUI} type
 * @author Steve Wilson
 * @author Mikle Garin
 */
public class WDesktopPaneInputListener<C extends JDesktopPane, U extends WDesktopPaneUI<C>> extends AbstractUIInputListener<C, U>
        implements DesktopPaneInputListener<C>
{
    @Override
    public void install ( @NotNull final C component )
    {
        super.install ( component );

        // Installing ActionMap
        final UIActionMap actionMap = new UIActionMap ();
        actionMap.put ( new Action ( component, Action.RESTORE ) );
        actionMap.put ( new Action ( component, Action.CLOSE ) );
        actionMap.put ( new Action ( component, Action.MOVE ) );
        actionMap.put ( new Action ( component, Action.RESIZE ) );
        actionMap.put ( new Action ( component, Action.LEFT ) );
        actionMap.put ( new Action ( component, Action.SHRINK_LEFT ) );
        actionMap.put ( new Action ( component, Action.RIGHT ) );
        actionMap.put ( new Action ( component, Action.SHRINK_RIGHT ) );
        actionMap.put ( new Action ( component, Action.UP ) );
        actionMap.put ( new Action ( component, Action.SHRINK_UP ) );
        actionMap.put ( new Action ( component, Action.DOWN ) );
        actionMap.put ( new Action ( component, Action.SHRINK_DOWN ) );
        actionMap.put ( new Action ( component, Action.ESCAPE ) );
        actionMap.put ( new Action ( component, Action.MINIMIZE ) );
        actionMap.put ( new Action ( component, Action.MAXIMIZE ) );
        actionMap.put ( new Action ( component, Action.NEXT_FRAME ) );
        actionMap.put ( new Action ( component, Action.PREVIOUS_FRAME ) );
        actionMap.put ( new Action ( component, Action.NAVIGATE_NEXT ) );
        actionMap.put ( new Action ( component, Action.NAVIGATE_PREVIOUS ) );
        SwingUtilities.replaceUIActionMap ( component, actionMap );

        // Installing InputMap
        final InputMap focusedWindowInputMap = LafLookup.getInputMap ( component, JComponent.WHEN_IN_FOCUSED_WINDOW );
        SwingUtilities.replaceUIInputMap ( component, JComponent.WHEN_IN_FOCUSED_WINDOW, focusedWindowInputMap );
        final InputMap focusedAncestorInputMap = LafLookup.getInputMap ( component, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT );
        SwingUtilities.replaceUIInputMap ( component, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, focusedAncestorInputMap );
    }

    @Override
    public void uninstall ( @NotNull final C component )
    {
        // Uninstalling InputMap
        SwingUtilities.replaceUIInputMap ( component, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, null );
        SwingUtilities.replaceUIInputMap ( component, JComponent.WHEN_IN_FOCUSED_WINDOW, null );

        // Uninstalling ActionMap
        SwingUtilities.replaceUIActionMap ( component, null );

        super.uninstall ( component );
    }

    /**
     * Actions for {@link JDesktopPane}.
     *
     * @param <D> {@link JDesktopPane} type
     */
    public static class Action<D extends JDesktopPane> extends UIAction<D>
    {
        /**
         * Supported actions.
         */
        public static final String CLOSE = "close";
        public static final String ESCAPE = "escape";
        public static final String MAXIMIZE = "maximize";
        public static final String MINIMIZE = "minimize";
        public static final String MOVE = "move";
        public static final String RESIZE = "resize";
        public static final String RESTORE = "restore";
        public static final String LEFT = "left";
        public static final String RIGHT = "right";
        public static final String UP = "up";
        public static final String DOWN = "down";
        public static final String SHRINK_LEFT = "shrinkLeft";
        public static final String SHRINK_RIGHT = "shrinkRight";
        public static final String SHRINK_UP = "shrinkUp";
        public static final String SHRINK_DOWN = "shrinkDown";
        public static final String NEXT_FRAME = "selectNextFrame";
        public static final String PREVIOUS_FRAME = "selectPreviousFrame";
        public static final String NAVIGATE_NEXT = "navigateNext";
        public static final String NAVIGATE_PREVIOUS = "navigatePrevious";

        /**
         * Runtime {@link Action} variables.
         */
        private static final int MOVE_RESIZE_INCREMENT = 10;
        private static boolean moving = false;
        private static boolean resizing = false;
        private static JInternalFrame sourceFrame = null;
        private static Component focusOwner = null;

        /**
         * Constructs new desktop {@link Action} with the specified name.
         *
         * @param desktop {@link JDesktopPane}
         * @param name    {@link Action} name
         */
        public Action ( @NotNull final D desktop, @NotNull final String name )
        {
            super ( desktop, name );
        }

        @Override
        public void actionPerformed ( @NotNull final ActionEvent e )
        {
            final D desktop = getComponent ();
            final String action = getName ();

            if ( Objects.equals ( action, CLOSE ) )
            {
                final JInternalFrame internalFrame = desktop.getSelectedFrame ();
                if ( internalFrame != null )
                {
                    internalFrame.doDefaultCloseAction ();
                }
            }
            else if ( Objects.equals ( action, MAXIMIZE ) )
            {
                final JInternalFrame internalFrame = desktop.getSelectedFrame ();
                if ( internalFrame != null )
                {
                    if ( !internalFrame.isMaximum () )
                    {
                        if ( internalFrame.isIcon () )
                        {
                            try
                            {
                                internalFrame.setIcon ( false );
                                internalFrame.setMaximum ( true );
                            }
                            catch ( final PropertyVetoException ignored )
                            {
                            }
                        }
                        else
                        {
                            try
                            {
                                internalFrame.setMaximum ( true );
                            }
                            catch ( final PropertyVetoException ignored )
                            {
                            }
                        }
                    }
                }
            }
            else if ( Objects.equals ( action, MINIMIZE ) )
            {
                final JInternalFrame internalFrame = desktop.getSelectedFrame ();
                if ( internalFrame != null )
                {
                    if ( !internalFrame.isIcon () )
                    {
                        try
                        {
                            internalFrame.setIcon ( true );
                        }
                        catch ( final PropertyVetoException ignored )
                        {
                        }
                    }
                }
            }
            else if ( Objects.equals ( action, RESTORE ) )
            {
                final JInternalFrame internalFrame = desktop.getSelectedFrame ();
                if ( internalFrame != null )
                {
                    try
                    {
                        if ( internalFrame.isIcon () )
                        {
                            internalFrame.setIcon ( false );
                        }
                        else if ( internalFrame.isMaximum () )
                        {
                            internalFrame.setMaximum ( false );
                        }
                        internalFrame.setSelected ( true );
                    }
                    catch ( final PropertyVetoException ignored )
                    {
                    }
                }
            }
            else if ( Objects.equals ( action, ESCAPE ) )
            {
                if ( sourceFrame == desktop.getSelectedFrame () && focusOwner != null )
                {
                    focusOwner.requestFocus ();
                }
                moving = false;
                resizing = false;
                sourceFrame = null;
                focusOwner = null;
            }
            else if ( Objects.equals ( action, MOVE, RESIZE ) )
            {
                sourceFrame = desktop.getSelectedFrame ();
                if ( sourceFrame != null )
                {
                    moving = Objects.equals ( action, MOVE );
                    resizing = Objects.equals ( action, RESIZE );

                    focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager ().getFocusOwner ();
                    if ( !SwingUtilities.isDescendingFrom ( focusOwner, sourceFrame ) )
                    {
                        focusOwner = null;
                    }
                    sourceFrame.requestFocus ();
                }
            }
            else if ( Objects.equals ( action, LEFT, RIGHT, UP, DOWN, SHRINK_RIGHT, SHRINK_LEFT, SHRINK_UP, SHRINK_DOWN ) )
            {
                final JInternalFrame internalFrame = desktop.getSelectedFrame ();
                if ( sourceFrame != null && internalFrame == sourceFrame &&
                        KeyboardFocusManager.getCurrentKeyboardFocusManager ().getFocusOwner () == sourceFrame )
                {
                    final Insets minOnScreenInsets = UIManager.getInsets ( "Desktop.minOnScreenInsets" );
                    final Dimension size = internalFrame.getSize ();
                    final Dimension minSize = internalFrame.getMinimumSize ();
                    final int dpWidth = desktop.getWidth ();
                    final int dpHeight = desktop.getHeight ();
                    int delta;
                    final Point loc = internalFrame.getLocation ();
                    if ( Objects.equals ( action, LEFT ) )
                    {
                        if ( moving )
                        {
                            internalFrame.setLocation (
                                    loc.x + size.width - MOVE_RESIZE_INCREMENT < minOnScreenInsets.right ?
                                            -size.width + minOnScreenInsets.right :
                                            loc.x - MOVE_RESIZE_INCREMENT,
                                    loc.y
                            );
                        }
                        else if ( resizing )
                        {
                            internalFrame.setLocation ( loc.x - MOVE_RESIZE_INCREMENT, loc.y );
                            internalFrame.setSize ( size.width + MOVE_RESIZE_INCREMENT, size.height );
                        }
                    }
                    else if ( Objects.equals ( action, RIGHT ) )
                    {
                        if ( moving )
                        {
                            internalFrame.setLocation (
                                    loc.x + MOVE_RESIZE_INCREMENT > dpWidth - minOnScreenInsets.left ?
                                            dpWidth - minOnScreenInsets.left :
                                            loc.x + MOVE_RESIZE_INCREMENT,
                                    loc.y
                            );
                        }
                        else if ( resizing )
                        {
                            internalFrame.setSize ( size.width + MOVE_RESIZE_INCREMENT, size.height );
                        }
                    }
                    else if ( Objects.equals ( action, UP ) )
                    {
                        if ( moving )
                        {
                            internalFrame.setLocation (
                                    loc.x,
                                    loc.y + size.height - MOVE_RESIZE_INCREMENT < minOnScreenInsets.bottom ?
                                            -size.height + minOnScreenInsets.bottom :
                                            loc.y - MOVE_RESIZE_INCREMENT
                            );
                        }
                        else if ( resizing )
                        {
                            internalFrame.setLocation ( loc.x, loc.y - MOVE_RESIZE_INCREMENT );
                            internalFrame.setSize ( size.width, size.height + MOVE_RESIZE_INCREMENT );
                        }
                    }
                    else if ( Objects.equals ( action, DOWN ) )
                    {
                        if ( moving )
                        {
                            internalFrame.setLocation (
                                    loc.x,
                                    loc.y + MOVE_RESIZE_INCREMENT > dpHeight - minOnScreenInsets.top ?
                                            dpHeight - minOnScreenInsets.top :
                                            loc.y + MOVE_RESIZE_INCREMENT
                            );
                        }
                        else if ( resizing )
                        {
                            internalFrame.setSize ( size.width,
                                    size.height + MOVE_RESIZE_INCREMENT );
                        }
                    }
                    else if ( Objects.equals ( action, SHRINK_LEFT ) && resizing )
                    {
                        // Make sure we don't resize less than minimum size.
                        if ( minSize.width < size.width - MOVE_RESIZE_INCREMENT )
                        {
                            delta = MOVE_RESIZE_INCREMENT;
                        }
                        else
                        {
                            delta = size.width - minSize.width;
                        }

                        // Ensure that we keep the internal frame on the desktop.
                        if ( loc.x + size.width - delta < minOnScreenInsets.left )
                        {
                            delta = loc.x + size.width - minOnScreenInsets.left;
                        }
                        internalFrame.setSize ( size.width - delta, size.height );
                    }
                    else if ( Objects.equals ( action, SHRINK_RIGHT ) && resizing )
                    {
                        // Make sure we don't resize less than minimum size.
                        if ( minSize.width < size.width - MOVE_RESIZE_INCREMENT )
                        {
                            delta = MOVE_RESIZE_INCREMENT;
                        }
                        else
                        {
                            delta = size.width - minSize.width;
                        }

                        // Ensure that we keep the internal frame on the desktop.
                        if ( loc.x + delta > dpWidth - minOnScreenInsets.right )
                        {
                            delta = dpWidth - minOnScreenInsets.right - loc.x;
                        }

                        internalFrame.setLocation ( loc.x + delta, loc.y );
                        internalFrame.setSize ( size.width - delta, size.height );
                    }
                    else if ( Objects.equals ( action, SHRINK_UP ) && resizing )
                    {
                        // Make sure we don't resize less than minimum size.
                        if ( minSize.height < size.height - MOVE_RESIZE_INCREMENT )
                        {
                            delta = MOVE_RESIZE_INCREMENT;
                        }
                        else
                        {
                            delta = size.height - minSize.height;
                        }

                        // Ensure that we keep the internal frame on the desktop.
                        if ( loc.y + size.height - delta < minOnScreenInsets.bottom )
                        {
                            delta = loc.y + size.height - minOnScreenInsets.bottom;
                        }

                        internalFrame.setSize ( size.width, size.height - delta );
                    }
                    else if ( Objects.equals ( action, SHRINK_DOWN ) && resizing )
                    {
                        // Make sure we don't resize less than minimum size.
                        if ( minSize.height < size.height - MOVE_RESIZE_INCREMENT )
                        {
                            delta = MOVE_RESIZE_INCREMENT;
                        }
                        else
                        {
                            delta = size.height - minSize.height;
                        }

                        // Ensure that we keep the internal frame on the desktop.
                        if ( loc.y + delta > dpHeight - minOnScreenInsets.top )
                        {
                            delta = dpHeight - minOnScreenInsets.top - loc.y;
                        }

                        internalFrame.setLocation ( loc.x, loc.y + delta );
                        internalFrame.setSize ( size.width, size.height - delta );
                    }
                }
            }
            else if ( Objects.equals ( action, NEXT_FRAME, PREVIOUS_FRAME ) )
            {
                desktop.selectFrame ( Objects.equals ( action, NEXT_FRAME ) );
            }
            else if ( Objects.equals ( action, NAVIGATE_NEXT, NAVIGATE_PREVIOUS ) )
            {
                final Container cycleRoot = desktop.getFocusCycleRootAncestor ();
                if ( cycleRoot != null )
                {
                    final FocusTraversalPolicy policy = cycleRoot.getFocusTraversalPolicy ();
                    if ( policy != null && policy instanceof SortingFocusTraversalPolicy )
                    {
                        final SortingFocusTraversalPolicy sPolicy = ( SortingFocusTraversalPolicy ) policy;
                        final boolean idc = sPolicy.getImplicitDownCycleTraversal ();
                        try
                        {
                            sPolicy.setImplicitDownCycleTraversal ( false );

                            if ( Objects.equals ( action, NAVIGATE_NEXT ) )
                            {
                                KeyboardFocusManager.getCurrentKeyboardFocusManager ().focusNextComponent ( desktop );
                            }
                            else
                            {
                                KeyboardFocusManager.getCurrentKeyboardFocusManager ().focusPreviousComponent ( desktop );
                            }
                        }
                        finally
                        {
                            sPolicy.setImplicitDownCycleTraversal ( idc );
                        }
                    }
                }
            }
        }

        @Override
        @SuppressWarnings ( "SimplifiableIfStatement" )
        public boolean isEnabled ()
        {
            final boolean enabled;
            final D desktop = getComponent ();
            final String action = getName ();
            if ( Objects.equals ( action, NEXT_FRAME, PREVIOUS_FRAME ) )
            {
                enabled = true;
            }
            else
            {
                final JInternalFrame internalFrame = desktop.getSelectedFrame ();
                if ( internalFrame == null )
                {
                    enabled = false;
                }
                else if ( Objects.equals ( action, CLOSE ) )
                {
                    enabled = internalFrame.isClosable ();
                }
                else if ( Objects.equals ( action, MINIMIZE ) )
                {
                    enabled = internalFrame.isIconifiable ();
                }
                else if ( Objects.equals ( action, MAXIMIZE ) )
                {
                    enabled = internalFrame.isMaximizable ();
                }
                else
                {
                    enabled = true;
                }
            }
            return enabled;
        }
    }
}