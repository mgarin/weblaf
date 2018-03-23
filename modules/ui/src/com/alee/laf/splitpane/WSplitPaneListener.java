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

package com.alee.laf.splitpane;

import com.alee.laf.AbstractUIInputListener;
import com.alee.laf.UIAction;
import com.alee.laf.UIActionMap;
import com.alee.utils.LafLookup;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;

/**
 * Basic listener for {@link WSplitPaneUI} implementation.
 * It is based on listeners from common Swing {@link javax.swing.plaf.basic.BasicSplitPaneUI} but cleaned up and optimized.
 *
 * @param <C> {@link JSplitPane} type
 * @param <U> {@link WSplitPaneUI} type
 * @author Mikle Garin
 */

public class WSplitPaneListener<C extends JSplitPane, U extends WSplitPaneUI<C>>
        extends AbstractUIInputListener<C, U> implements PropertyChangeListener, FocusListener
{
    /**
     * Keys to use for forward focus traversal when the JComponent is managing focus.
     */
    protected Set managingFocusForwardTraversalKeys;

    /**
     * Keys to use for backward focus traversal when the JComponent is managing focus.
     */
    protected Set managingFocusBackwardTraversalKeys;

    @Override
    public void install ( final C component )
    {
        super.install ( component );

        // Installing listeners
        component.addPropertyChangeListener ( this );
        component.addFocusListener ( this );

        // Setting focus traversal key
        managingFocusForwardTraversalKeys = new HashSet ();
        managingFocusForwardTraversalKeys.add ( KeyStroke.getKeyStroke ( KeyEvent.VK_TAB, 0 ) );
        component.setFocusTraversalKeys ( KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, managingFocusForwardTraversalKeys );
        managingFocusBackwardTraversalKeys = new HashSet ();
        managingFocusBackwardTraversalKeys.add ( KeyStroke.getKeyStroke ( KeyEvent.VK_TAB, InputEvent.SHIFT_MASK ) );
        component.setFocusTraversalKeys ( KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, managingFocusBackwardTraversalKeys );

        // Installing ActionMap
        final UIActionMap actionMap = new UIActionMap ();
        actionMap.put ( new Action ( component, Action.NEGATIVE_INCREMENT ) );
        actionMap.put ( new Action ( component, Action.POSITIVE_INCREMENT ) );
        actionMap.put ( new Action ( component, Action.SELECT_MIN ) );
        actionMap.put ( new Action ( component, Action.SELECT_MAX ) );
        actionMap.put ( new Action ( component, Action.START_RESIZE ) );
        actionMap.put ( new Action ( component, Action.TOGGLE_FOCUS ) );
        actionMap.put ( new Action ( component, Action.FOCUS_OUT_FORWARD ) );
        actionMap.put ( new Action ( component, Action.FOCUS_OUT_BACKWARD ) );
        SwingUtilities.replaceUIActionMap ( component, actionMap );

        // Installing InputMap
        final InputMap inputMap = LafLookup.getInputMap ( component, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT );
        SwingUtilities.replaceUIInputMap ( component, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, inputMap );
    }

    @Override
    public void uninstall ( final C component )
    {
        // Uninstalling InputMap
        SwingUtilities.replaceUIInputMap ( component, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, null );

        // Uninstalling ActionMap
        SwingUtilities.replaceUIActionMap ( component, null );

        // Removing focus traversal keys
        component.setFocusTraversalKeys ( KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null );
        managingFocusBackwardTraversalKeys = null;
        component.setFocusTraversalKeys ( KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null );
        managingFocusForwardTraversalKeys = null;

        // Uninstalling listeners
        component.removeFocusListener ( this );
        component.removePropertyChangeListener ( this );

        super.uninstall ( component );
    }

    @Override
    public void propertyChange ( final PropertyChangeEvent event )
    {
        if ( event.getSource () == component )
        {
            final String changeName = event.getPropertyName ();
            if ( changeName.equals ( JSplitPane.ORIENTATION_PROPERTY ) )
            {
                componentUI.setOrientation ( component.getOrientation () );
                componentUI.resetLayoutManager ();
            }
            else if ( changeName.equals ( JSplitPane.CONTINUOUS_LAYOUT_PROPERTY ) )
            {
                componentUI.setContinuousLayout ( component.isContinuousLayout () );
                if ( !componentUI.isContinuousLayout () )
                {
                    if ( componentUI.getNonContinuousLayoutDivider () == null )
                    {
                        componentUI.setNonContinuousLayoutDivider ( componentUI.createNonContinuousDivider (), true );
                    }
                    else if ( componentUI.getNonContinuousLayoutDivider ().getParent () == null )
                    {
                        componentUI.setNonContinuousLayoutDivider ( componentUI.getNonContinuousLayoutDivider (), true );
                    }
                }
            }
            else if ( changeName.equals ( JSplitPane.DIVIDER_SIZE_PROPERTY ) )
            {
                componentUI.setDividerSize ( component.getDividerSize () );
                component.revalidate ();
                component.repaint ();
            }
        }
    }

    @Override
    public void focusGained ( final FocusEvent event )
    {
        componentUI.setDividerKeyboardResize ( true );
    }

    @Override
    public void focusLost ( final FocusEvent event )
    {
        componentUI.setDividerKeyboardResize ( false );
    }

    /**
     * Actions for {@link JSplitPane}.
     *
     * @param <S> {@link JSplitPane} type
     */
    protected class Action<S extends JSplitPane> extends UIAction<S>
    {
        /**
         * Supported actions.
         */
        public static final String NEGATIVE_INCREMENT = "negativeIncrement";
        public static final String POSITIVE_INCREMENT = "positiveIncrement";
        public static final String SELECT_MIN = "selectMin";
        public static final String SELECT_MAX = "selectMax";
        public static final String START_RESIZE = "startResize";
        public static final String TOGGLE_FOCUS = "toggleFocus";
        public static final String FOCUS_OUT_FORWARD = "focusOutForward";
        public static final String FOCUS_OUT_BACKWARD = "focusOutBackward";

        /**
         * Constrcuts new {@link Action} for the specified key.
         *
         * @param splitPane {@link JSplitPane}
         * @param name      {@link Action} name
         */
        public Action ( final S splitPane, final String name )
        {
            super ( splitPane, name );
        }

        @Override
        public void actionPerformed ( final ActionEvent event )
        {
            final S splitPane = getComponent ();
            final WSplitPaneUI splitPaneUI = LafUtils.getUI ( splitPane );
            final String key = getName ();
            if ( key.equals ( NEGATIVE_INCREMENT ) )
            {
                if ( splitPaneUI.isDividerKeyboardResize () )
                {
                    splitPane.setDividerLocation (
                            Math.max ( 0, splitPaneUI.getDividerLocation ( splitPane ) - splitPaneUI.getKeyboardMoveIncrement () ) );
                }
            }
            else if ( key.equals ( POSITIVE_INCREMENT ) )
            {
                if ( splitPaneUI.isDividerKeyboardResize () )
                {
                    splitPane.setDividerLocation ( splitPaneUI.getDividerLocation ( splitPane ) + splitPaneUI.getKeyboardMoveIncrement () );
                }
            }
            else if ( key.equals ( SELECT_MIN ) )
            {
                if ( splitPaneUI.isDividerKeyboardResize () )
                {
                    splitPane.setDividerLocation ( 0 );
                }
            }
            else if ( key.equals ( SELECT_MAX ) )
            {
                if ( splitPaneUI.isDividerKeyboardResize () )
                {
                    final Insets insets = splitPane.getInsets ();
                    final int bottomI = insets != null ? insets.bottom : 0;
                    final int rightI = insets != null ? insets.right : 0;
                    if ( splitPaneUI.orientation == JSplitPane.VERTICAL_SPLIT )
                    {
                        splitPane.setDividerLocation ( splitPane.getHeight () - bottomI );
                    }
                    else
                    {
                        splitPane.setDividerLocation ( splitPane.getWidth () - rightI );
                    }
                }
            }
            else if ( key.equals ( START_RESIZE ) )
            {
                if ( !splitPaneUI.isDividerKeyboardResize () )
                {
                    splitPane.requestFocus ();
                }
                else
                {
                    final JSplitPane parentSplitPane = ( JSplitPane ) SwingUtilities.getAncestorOfClass ( JSplitPane.class, splitPane );
                    if ( parentSplitPane != null )
                    {
                        parentSplitPane.requestFocus ();
                    }
                }
            }
            else if ( key.equals ( TOGGLE_FOCUS ) )
            {
                toggleFocus ( splitPane );
            }
            else if ( key.equals ( FOCUS_OUT_FORWARD ) )
            {
                moveFocus ( splitPane, 1 );
            }
            else if ( key.equals ( FOCUS_OUT_BACKWARD ) )
            {
                moveFocus ( splitPane, -1 );
            }
        }

        /**
         * Moves focus in the specified directon.
         *
         * @param splitPane {@link JSplitPane}
         * @param direction focus move direction
         */
        protected void moveFocus ( final S splitPane, final int direction )
        {
            Container rootAncestor = splitPane.getFocusCycleRootAncestor ();
            FocusTraversalPolicy policy = rootAncestor.getFocusTraversalPolicy ();
            Component focusOn = direction > 0 ?
                    policy.getComponentAfter ( rootAncestor, splitPane ) :
                    policy.getComponentBefore ( rootAncestor, splitPane );
            final HashSet focusFrom = new HashSet ();
            if ( splitPane.isAncestorOf ( focusOn ) )
            {
                do
                {
                    focusFrom.add ( focusOn );
                    rootAncestor = focusOn.getFocusCycleRootAncestor ();
                    policy = rootAncestor.getFocusTraversalPolicy ();
                    focusOn = direction > 0 ?
                            policy.getComponentAfter ( rootAncestor, focusOn ) :
                            policy.getComponentBefore ( rootAncestor, focusOn );
                }
                while ( splitPane.isAncestorOf ( focusOn ) && !focusFrom.contains ( focusOn ) );
            }
            if ( focusOn != null && !splitPane.isAncestorOf ( focusOn ) )
            {
                focusOn.requestFocus ();
            }
        }

        /**
         * Toggles focus between split pane sides.
         *
         * @param splitPane {@link JSplitPane}
         */
        protected void toggleFocus ( final S splitPane )
        {
            final Component left = splitPane.getLeftComponent ();
            final Component right = splitPane.getRightComponent ();
            final KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager ();
            final Component focus = manager.getFocusOwner ();
            final Component focusOn = getNextSide ( splitPane, focus );
            if ( focusOn != null )
            {
                /**
                 * Don't change the focus if the new focused component belongs to the same splitpane and the same side.
                 */
                if ( focus == null ||
                        !( SwingUtilities.isDescendingFrom ( focus, left ) && SwingUtilities.isDescendingFrom ( focusOn, left ) ||
                                SwingUtilities.isDescendingFrom ( focus, right ) && SwingUtilities.isDescendingFrom ( focusOn, right ) ) )
                {
                    SwingUtils.compositeRequestFocus ( focusOn );
                }
            }
        }

        /**
         * Returns next available side {@link Component}.
         *
         * @param splitPane {@link JSplitPane}
         * @param focus     {@link Component} focus owner
         * @return next available side {@link Component}
         */
        protected Component getNextSide ( final JSplitPane splitPane, final Component focus )
        {
            final Component left = splitPane.getLeftComponent ();
            final Component right = splitPane.getRightComponent ();
            Component next;
            if ( focus != null && SwingUtilities.isDescendingFrom ( focus, left ) && right != null )
            {
                next = getFirstAvailableComponent ( right );
                if ( next != null )
                {
                    return next;
                }
            }
            final JSplitPane parentSplitPane = ( JSplitPane ) SwingUtilities.getAncestorOfClass ( JSplitPane.class, splitPane );
            if ( parentSplitPane != null )
            {
                /**
                 * Focus next side of the parent split pane.
                 */
                next = getNextSide ( parentSplitPane, focus );
            }
            else
            {
                next = getFirstAvailableComponent ( left );
                if ( next == null )
                {
                    next = getFirstAvailableComponent ( right );
                }
            }
            return next;
        }

        /**
         * Returns first available {@link Component}.
         *
         * @param component {@link Component} to search in
         * @return first available {@link Component}
         */
        protected Component getFirstAvailableComponent ( Component component )
        {
            if ( component != null && component instanceof JSplitPane )
            {
                final JSplitPane splitPane = ( JSplitPane ) component;
                final Component left = getFirstAvailableComponent ( splitPane.getLeftComponent () );
                component = left != null ? left : getFirstAvailableComponent ( splitPane.getRightComponent () );
            }
            return component;
        }
    }
}