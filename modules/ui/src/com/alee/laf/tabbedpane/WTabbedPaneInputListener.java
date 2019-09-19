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

package com.alee.laf.tabbedpane;

import com.alee.api.annotations.NotNull;
import com.alee.api.jdk.Objects;
import com.alee.laf.AbstractUIInputListener;
import com.alee.laf.UIAction;
import com.alee.laf.UIActionMap;
import com.alee.laf.viewport.WebViewport;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.LafLookup;
import com.alee.utils.MathUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentInputMapUIResource;
import javax.swing.plaf.TabbedPaneUI;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Basic UI input listener for {@link WTabbedPaneUI} implementation.
 * It is partially based on Swing {@link javax.swing.plaf.basic.BasicTabbedPaneUI} but cleaned up and optimized.
 *
 * A few nuances:
 * - Focused tab support have been cut, focused tab is now basically always the selected one
 * - Tab navigation through keys have been simplified to avoid mess in the code and user confusion
 * - Scroll forward and backward actions are simplified to just move view for it's width/height forward or backward respectively
 *
 * @param <C> {@link JTabbedPane} type
 * @param <U> {@link WTabbedPaneUI} type
 * @author Jeff Dinkins
 * @author Arnaud Weber
 * @author Mikle Garin
 */
public class WTabbedPaneInputListener<C extends JTabbedPane, U extends WTabbedPaneUI<C>> extends AbstractUIInputListener<C, U>
        implements TabbedPaneInputListener<C>, PropertyChangeListener, MouseListener, MouseWheelListener
{
    /**
     * Mnemonic {@link InputMap}.
     */
    protected InputMap mnemonicInputMap;

    @Override
    public void install ( @NotNull final C component )
    {
        super.install ( component );

        // Installing listeners
        component.addPropertyChangeListener ( this );
        component.addMouseListener ( this );
        component.addMouseWheelListener ( this );

        // Installing ActionMap
        final UIActionMap actionMap = new UIActionMap ();
        actionMap.put ( new Actions ( component, Actions.NEXT ) );
        actionMap.put ( new Actions ( component, Actions.PREVIOUS ) );
        actionMap.put ( new Actions ( component, Actions.RIGHT ) );
        actionMap.put ( new Actions ( component, Actions.LEFT ) );
        actionMap.put ( new Actions ( component, Actions.UP ) );
        actionMap.put ( new Actions ( component, Actions.DOWN ) );
        actionMap.put ( new Actions ( component, Actions.PAGE_UP ) );
        actionMap.put ( new Actions ( component, Actions.PAGE_DOWN ) );
        actionMap.put ( new Actions ( component, Actions.REQUEST_FOCUS ) );
        actionMap.put ( new Actions ( component, Actions.REQUEST_FOCUS_FOR_VISIBLE ) );
        actionMap.put ( new Actions ( component, Actions.SET_SELECTED ) );
        actionMap.put ( new Actions ( component, Actions.SCROLL_FORWARD ) );
        actionMap.put ( new Actions ( component, Actions.SCROLL_BACKWARD ) );
        SwingUtilities.replaceUIActionMap ( component, actionMap );

        // Installing InputMap
        final InputMap focusedMap = LafLookup.getInputMap ( component, JComponent.WHEN_FOCUSED );
        SwingUtilities.replaceUIInputMap ( component, JComponent.WHEN_FOCUSED, focusedMap );
        final InputMap ancestorMap = LafLookup.getInputMap ( component, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT );
        SwingUtilities.replaceUIInputMap ( component, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, ancestorMap );

        // Installing mnemonic InputMap
        mnemonicInputMap = new ComponentInputMapUIResource ( component );
        mnemonicInputMap.setParent ( SwingUtilities.getUIInputMap ( component, JComponent.WHEN_IN_FOCUSED_WINDOW ) );
        SwingUtilities.replaceUIInputMap ( component, JComponent.WHEN_IN_FOCUSED_WINDOW, mnemonicInputMap );
    }

    @Override
    public void uninstall ( @NotNull final C component )
    {
        // Uninstalling mnemonic InputMap
        SwingUtilities.replaceUIInputMap ( component, JComponent.WHEN_IN_FOCUSED_WINDOW, null );
        mnemonicInputMap = null;

        // Uninstalling InputMap
        SwingUtilities.replaceUIInputMap ( component, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, null );
        SwingUtilities.replaceUIInputMap ( component, JComponent.WHEN_FOCUSED, null );

        // Uninstalling ActionMap
        SwingUtilities.replaceUIActionMap ( component, null );

        // Uninstalling listeners
        component.removeMouseWheelListener ( this );
        component.removeMouseListener ( this );
        component.removePropertyChangeListener ( this );

        super.uninstall ( component );
    }

    @Override
    public void propertyChange ( final PropertyChangeEvent evt )
    {
        final String property = evt.getPropertyName ();
        if ( Objects.equals ( property, WebTabbedPane.MNEMONIC_AT_PROPERTY ) )
        {
            // We only need to update InputMap here
            // Display index property change even will perform visual updates if necessary
            updateMnemonics ();
        }
        else if ( Objects.equals ( property, WebTabbedPane.DISPLAYED_MNEMONIC_INDEX_AT_PROPERTY ) )
        {
            // Updating all displayed mnemonics
            // Unfor
            updateDisplayedMnemonics ();
        }
    }

    @Override
    public void tabAdded ( @NotNull final Tab tab, final int index )
    {
        // We only need to update InputMap here
        // Tab is initially created with correct mnemonic if it was specified
        updateMnemonics ();
    }

    @Override
    public void tabRemoved ( final int index )
    {
        // We only need to update InputMap here
        // Visual updates are not needed since tab was removed
        updateMnemonics ();
    }

    /**
     * Reloads all available tab mnemonics.
     * This should be invoked when a memonic changes, when the title of a mnemonic changes, or when tabs are added/removed.
     */
    protected void updateMnemonics ()
    {
        resetMnemonics ();
        for ( int counter = component.getTabCount () - 1; counter >= 0; counter-- )
        {
            final int mnemonic = component.getMnemonicAt ( counter );
            if ( mnemonic > 0 )
            {
                mnemonicInputMap.put ( KeyStroke.getKeyStroke ( mnemonic, Event.ALT_MASK ), "setSelectedIndex" );
            }
        }
    }

    /**
     * Resets the mnemonics bindings to an empty state.
     */
    protected void resetMnemonics ()
    {
        mnemonicInputMap.clear ();
    }

    /**
     * Updates all displayed tab mnemonics.
     */
    protected void updateDisplayedMnemonics ()
    {
        final TabContainer tabContainer = componentUI.getTabContainer ();
        if ( tabContainer != null )
        {
            for ( int i = 0; i < tabContainer.getComponentCount (); i++ )
            {
                final Tab tab = ( Tab ) tabContainer.getComponent ( i );
                tab.setDisplayedMnemonicIndex ( component.getDisplayedMnemonicIndexAt ( i ) );
            }
        }
    }

    @Override
    public void mouseClicked ( final MouseEvent e )
    {
    }

    @Override
    public void mousePressed ( final MouseEvent e )
    {
        if ( component.isEnabled () )
        {
            final int tabIndex = componentUI.tabForCoordinate ( component, e.getX (), e.getY () );
            if ( tabIndex >= 0 && component.isEnabledAt ( tabIndex ) )
            {
                if ( tabIndex != component.getSelectedIndex () )
                {
                    component.setSelectedIndex ( tabIndex );
                }
                else if ( component.isRequestFocusEnabled () )
                {
                    component.requestFocusInWindow ();
                }
            }
        }
    }

    @Override
    public void mouseReleased ( final MouseEvent e )
    {
    }

    @Override
    public void mouseEntered ( final MouseEvent e )
    {
    }

    @Override
    public void mouseExited ( final MouseEvent e )
    {
    }

    @Override
    public void mouseWheelMoved ( final MouseWheelEvent e )
    {
        final WebViewport tabViewport = componentUI.getTabViewport ();
        final TabContainer tabContainer = componentUI.getTabContainer ();
        if ( tabViewport != null && tabContainer != null )
        {
            if ( CoreSwingUtils.getRelativeBounds ( tabContainer, component ).contains ( e.getPoint () ) )
            {
                final int tabPlacement = component.getTabPlacement ();
                final boolean horizontal = tabPlacement == JTabbedPane.TOP || tabPlacement == JTabbedPane.BOTTOM;
                final Point viewPosition = tabViewport.getViewPosition ();
                tabViewport.setViewPosition ( new Point (
                        horizontal ? MathUtils.limit (
                                0,
                                viewPosition.x + 5 * e.getUnitsToScroll (),
                                tabViewport.getViewSize ().width - tabViewport.getExtentSize ().width
                        ) : viewPosition.x,
                        horizontal ? viewPosition.y : MathUtils.limit (
                                0,
                                viewPosition.y + 5 * e.getUnitsToScroll (),
                                tabViewport.getViewSize ().height - tabViewport.getExtentSize ().height
                        )
                ) );
            }
        }
    }

    /**
     * Actions for {@link JTabbedPane}.
     *
     * @param <T> {@link JTabbedPane} type
     */
    public static class Actions<T extends JTabbedPane> extends UIAction<T>
    {
        /**
         * Supported actions.
         */
        public final static String NEXT = "navigateNext";
        public final static String PREVIOUS = "navigatePrevious";
        public final static String RIGHT = "navigateRight";
        public final static String LEFT = "navigateLeft";
        public final static String UP = "navigateUp";
        public final static String DOWN = "navigateDown";
        public final static String PAGE_UP = "navigatePageUp";
        public final static String PAGE_DOWN = "navigatePageDown";
        public final static String REQUEST_FOCUS = "requestFocus";
        public final static String REQUEST_FOCUS_FOR_VISIBLE = "requestFocusForVisibleComponent";
        public final static String SET_SELECTED = "setSelectedIndex";
        public final static String SCROLL_FORWARD = "scrollTabsForwardAction";
        public final static String SCROLL_BACKWARD = "scrollTabsBackwardAction";

        /**
         * Constructs new button {@link Actions} with the specified name.
         *
         * @param tabbedPane {@link JTabbedPane}
         * @param name       {@link Actions} name
         */
        public Actions ( @NotNull final T tabbedPane, @NotNull final String name )
        {
            super ( tabbedPane, name );
        }

        @Override
        public void actionPerformed ( final ActionEvent e )
        {
            final String key = getName ();
            final T tabbedPane = getComponent ();
            if ( key.equals ( NEXT ) )
            {
                selectTab ( tabbedPane, SwingConstants.NEXT );
            }
            else if ( key.equals ( PREVIOUS ) )
            {
                selectTab ( tabbedPane, SwingConstants.PREVIOUS );
            }
            else if ( key.equals ( RIGHT ) )
            {
                selectTab ( tabbedPane, SwingConstants.EAST );
            }
            else if ( key.equals ( LEFT ) )
            {
                selectTab ( tabbedPane, SwingConstants.WEST );
            }
            else if ( key.equals ( UP ) )
            {
                selectTab ( tabbedPane, SwingConstants.NORTH );
            }
            else if ( key.equals ( DOWN ) )
            {
                selectTab ( tabbedPane, SwingConstants.SOUTH );
            }
            else if ( key.equals ( PAGE_UP ) )
            {
                final int tabPlacement = tabbedPane.getTabPlacement ();
                if ( tabPlacement == SwingConstants.TOP || tabPlacement == SwingConstants.BOTTOM )
                {
                    selectTab ( tabbedPane, SwingConstants.WEST );
                }
                else
                {
                    selectTab ( tabbedPane, SwingConstants.NORTH );
                }
            }
            else if ( key.equals ( PAGE_DOWN ) )
            {
                final int tabPlacement = tabbedPane.getTabPlacement ();
                if ( tabPlacement == SwingConstants.TOP || tabPlacement == SwingConstants.BOTTOM )
                {
                    selectTab ( tabbedPane, SwingConstants.EAST );
                }
                else
                {
                    selectTab ( tabbedPane, SwingConstants.SOUTH );
                }
            }
            else if ( key.equals ( REQUEST_FOCUS ) )
            {
                tabbedPane.requestFocusInWindow ();
            }
            else if ( key.equals ( REQUEST_FOCUS_FOR_VISIBLE ) )
            {
                requestFocusForVisibleComponent ( tabbedPane );
            }
            else if ( key.equals ( SET_SELECTED ) )
            {
                final String command = e.getActionCommand ();
                if ( command != null && command.length () > 0 )
                {
                    int mnemonic = e.getActionCommand ().charAt ( 0 );
                    if ( mnemonic >= 'a' && mnemonic <= 'z' )
                    {
                        mnemonic -= 'a' - 'A';
                    }
                    for ( int i = 0; i < tabbedPane.getTabCount (); i++ )
                    {
                        if ( mnemonic == tabbedPane.getMnemonicAt ( i ) )
                        {
                            tabbedPane.setSelectedIndex ( i );
                            break;
                        }
                    }
                }
            }
            else if ( key.equals ( SCROLL_FORWARD ) )
            {
                if ( tabbedPane.getTabLayoutPolicy () == JTabbedPane.SCROLL_TAB_LAYOUT )
                {
                    scrollForward ( tabbedPane );
                }
            }
            else if ( key.equals ( SCROLL_BACKWARD ) )
            {
                if ( tabbedPane.getTabLayoutPolicy () == JTabbedPane.SCROLL_TAB_LAYOUT )
                {
                    scrollBackward ( tabbedPane );
                }
            }
        }

        /**
         * Selects tab according to specified direction from the currently selected one.
         *
         * @param tabbedPane {@link JTabbedPane}
         * @param direction  selection direction
         */
        protected void selectTab ( final T tabbedPane, final int direction )
        {
            final int tabCount = tabbedPane.getTabCount ();
            if ( tabCount > 0 )
            {
                final int tabPlacement = tabbedPane.getTabPlacement ();
                final int current = tabbedPane.getSelectedIndex ();
                final boolean leftToRight = tabbedPane.getComponentOrientation ().isLeftToRight ();

                switch ( tabPlacement )
                {
                    case SwingConstants.BOTTOM:
                    case SwingConstants.TOP:
                        switch ( direction )
                        {
                            case SwingConstants.NEXT:
                            case SwingConstants.SOUTH:
                                selectNextTab ( tabbedPane, current );
                                break;

                            case SwingConstants.PREVIOUS:
                            case SwingConstants.NORTH:
                                selectPreviousTab ( tabbedPane, current );
                                break;

                            case SwingConstants.WEST:
                                if ( leftToRight )
                                {
                                    selectPreviousTab ( tabbedPane, current );
                                }
                                else
                                {
                                    selectNextTab ( tabbedPane, current );
                                }
                                break;

                            case SwingConstants.EAST:
                                if ( leftToRight )
                                {
                                    selectNextTab ( tabbedPane, current );
                                }
                                else
                                {
                                    selectPreviousTab ( tabbedPane, current );
                                }
                                break;
                        }
                        break;

                    case SwingConstants.LEFT:
                    case SwingConstants.RIGHT:
                        switch ( direction )
                        {
                            case SwingConstants.NEXT:
                            case SwingConstants.SOUTH:
                                selectNextTab ( tabbedPane, current );
                                break;

                            case SwingConstants.PREVIOUS:
                            case SwingConstants.NORTH:
                                selectPreviousTab ( tabbedPane, current );
                                break;

                            case SwingConstants.WEST:
                                if ( ( tabPlacement == SwingConstants.LEFT ) == leftToRight )
                                {
                                    selectPreviousTab ( tabbedPane, current );
                                }
                                else
                                {
                                    selectNextTab ( tabbedPane, current );
                                }
                                break;

                            case SwingConstants.EAST:
                                if ( ( tabPlacement == SwingConstants.LEFT ) == leftToRight )
                                {
                                    selectNextTab ( tabbedPane, current );
                                }
                                else
                                {
                                    selectPreviousTab ( tabbedPane, current );
                                }
                                break;
                        }
                        break;
                }
            }
        }

        /**
         * Selects next tab.
         *
         * @param tabbedPane {@link JTabbedPane}
         * @param current    current tab index
         */
        protected void selectNextTab ( @NotNull final T tabbedPane, final int current )
        {
            int tabIndex = getNextTabIndex ( tabbedPane, current );
            while ( tabIndex != current && !tabbedPane.isEnabledAt ( tabIndex ) )
            {
                tabIndex = getNextTabIndex ( tabbedPane, tabIndex );
            }
            tabbedPane.setSelectedIndex ( tabIndex );
        }

        /**
         * Selects previous tab.
         *
         * @param tabbedPane {@link JTabbedPane}
         * @param current    current tab index
         */
        protected void selectPreviousTab ( @NotNull final T tabbedPane, final int current )
        {
            int tabIndex = getPreviousTabIndex ( tabbedPane, current );
            while ( tabIndex != current && !tabbedPane.isEnabledAt ( tabIndex ) )
            {
                tabIndex = getPreviousTabIndex ( tabbedPane, tabIndex );
            }
            tabbedPane.setSelectedIndex ( tabIndex );
        }

        /**
         * Returns previous tab index.
         * todo Allow cyclic tab selection?
         *
         * @param tabbedPane {@link JTabbedPane}
         * @param base       base tab index
         * @return previous tab index
         */
        protected int getPreviousTabIndex ( @NotNull final T tabbedPane, final int base )
        {
            return base > 0 ? base - 1 : 0;
        }

        /**
         * Returns next tab index.
         * todo Allow cyclic tab selection?
         *
         * @param tabbedPane {@link JTabbedPane}
         * @param base       base tab index
         * @return next tab index
         */
        protected int getNextTabIndex ( @NotNull final T tabbedPane, final int base )
        {
            return base < tabbedPane.getTabCount () - 1 ? base + 1 : tabbedPane.getTabCount () - 1;
        }

        /**
         * Transfers focus to {@link Component} visible in the selected tab.
         *
         * @param tabbedPane {@link JTabbedPane}
         */
        protected void requestFocusForVisibleComponent ( @NotNull final T tabbedPane )
        {
            final Component component = tabbedPane.getSelectedComponent ();
            if ( component != null )
            {
                component.transferFocus ();
            }
        }

        /**
         * Moves {@link JTabbedPane} scrollable tabs view forward.
         *
         * @param tabbedPane {@link JTabbedPane}
         */
        protected void scrollForward ( final T tabbedPane )
        {
            scroll ( tabbedPane, 1 );
        }

        /**
         * Moves {@link JTabbedPane} scrollable tabs view backward.
         *
         * @param tabbedPane {@link JTabbedPane}
         */
        protected void scrollBackward ( final T tabbedPane )
        {
            scroll ( tabbedPane, -1 );
        }

        /**
         * Moves {@link JTabbedPane} scrollable tabs view according to scroll amount.
         *
         * @param tabbedPane   {@link JTabbedPane}
         * @param scrollAmount either positive or negative blocks scroll amount
         */
        protected void scroll ( final T tabbedPane, final int scrollAmount )
        {
            final TabbedPaneUI ui = tabbedPane.getUI ();
            if ( ui instanceof WTabbedPaneUI )
            {
                final TabContainer tabContainer = ( ( WTabbedPaneUI ) ui ).getTabContainer ();
                if ( tabContainer != null )
                {
                    final boolean horizontal = tabbedPane.getTabPlacement () == JTabbedPane.TOP ||
                            tabbedPane.getTabPlacement () == JTabbedPane.BOTTOM;
                    final Rectangle vr = tabContainer.getVisibleRect ();
                    final Rectangle bounds = new Rectangle ( tabContainer.getSize () );
                    if ( horizontal )
                    {
                        vr.x += scrollAmount * vr.width;
                    }
                    else
                    {
                        vr.y += scrollAmount * vr.height;
                    }
                    tabContainer.scrollRectToVisible ( bounds.intersection ( vr ) );
                }
            }
        }
    }
}