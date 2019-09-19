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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.extended.WebContainer;
import com.alee.extended.layout.AbstractLayoutManager;
import com.alee.laf.button.WebButton;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleManager;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.painter.decoration.Stateful;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Divider component used by {@link WSplitPaneUI}.
 * This is a better implementation of default Swing {@link javax.swing.plaf.basic.BasicSplitPaneDivider}.
 * Problem with th default implementation is that it is based on {@link Container} and therefore cannot be properly styled.
 *
 * @author Scott Violet
 * @author Mikle Garin
 * @see SplitPaneDescriptor
 * @see WSplitPaneDividerUI
 * @see WebSplitPaneDividerUI
 * @see ISplitPanePainter
 * @see SplitPanePainter
 * @see WebSplitPane
 */
public class WebSplitPaneDivider extends WebContainer<WebSplitPaneDivider, WSplitPaneDividerUI> implements PropertyChangeListener
{
    /**
     * Property identifying {@link JSplitPane} this divider is attached to.
     */
    public static final String SPLIT_PANE_PROPERTY = "splitPane";

    /**
     * {@link JSplitPane} this divider is used for.
     */
    protected transient JSplitPane splitPane;

    /**
     * {@link WSplitPaneUI} of the {@link JSplitPane} this divider is used for.
     */
    protected transient WSplitPaneUI splitPaneUI;

    /**
     * Handles mouse events from both this class, and the split pane.
     * Mouse events are handled for the splitpane since you want to be able to drag when clicking on the border of the divider,
     * which is not drawn by the divider.
     */
    protected transient MouseHandler mouseHandler;

    /**
     * Handles mouse dragging message to do the actual dragging.
     */
    protected transient DragController dragger;

    /**
     * Preferred size of the divider.
     * Note that this size will be used as minimum but can be exceeded if divider's own preferred size is larger.
     */
    protected int dividerSize = 0;

    /**
     * Divider that is used for noncontinuous layout mode.
     */
    protected Component hiddenDivider;

    /**
     * Orientation of the JSplitPane.
     */
    protected int orientation;

    /**
     * Button for quickly toggling the left component.
     */
    protected WebButton leftButton;

    /**
     * Button for quickly toggling the right component.
     */
    protected WebButton rightButton;

    /**
     * Constructs new {@link WebSplitPaneDivider}.
     *
     * @param splitPane {@link JSplitPane} this divider is attached to
     */
    public WebSplitPaneDivider ( final JSplitPane splitPane )
    {
        this ( StyleId.auto, splitPane );
    }

    /**
     * Constructs new {@link WebSplitPaneDivider}.
     *
     * @param id        {@link StyleId}
     * @param splitPane {@link JSplitPane} this divider is attached to
     */
    public WebSplitPaneDivider ( final StyleId id, final JSplitPane splitPane )
    {
        super ();
        setLayout ( new DividerLayout () );
        setSplitPane ( splitPane );
        updateUI ();
        setStyleId ( id );
    }

    @NotNull
    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.splitpanedivider;
    }

    /**
     * Sets {@link JSplitPane} this divider is attached to.
     *
     * @param splitPane {@link JSplitPane} this divider is attached to
     */
    protected void setSplitPane ( final JSplitPane splitPane )
    {
        final JSplitPane old = this.splitPane;

        /**
         * Uninstalling old split pane.
         */
        if ( this.splitPane != null )
        {
            // Unregistering property change listener
            this.splitPane.removePropertyChangeListener ( this );

            // Unregistering mouse handler
            if ( mouseHandler != null )
            {
                this.splitPane.removeMouseListener ( mouseHandler );
                this.splitPane.removeMouseMotionListener ( mouseHandler );
                removeMouseListener ( mouseHandler );
                removeMouseMotionListener ( mouseHandler );
                mouseHandler = null;
            }
        }

        /**
         * Saving new split pane and its UI.
         */
        this.splitPane = splitPane;
        this.splitPaneUI = splitPane != null ? ( WSplitPaneUI ) splitPane.getUI () : null;

        /**
         * Installing new split pane.
         */
        if ( this.splitPane != null )
        {
            // Updating orientation and cursor
            updateOrientationAndCursor ();

            // Registering mouse handler
            if ( mouseHandler == null )
            {
                mouseHandler = new MouseHandler ();
            }
            this.splitPane.addMouseListener ( mouseHandler );
            this.splitPane.addMouseMotionListener ( mouseHandler );
            addMouseListener ( mouseHandler );
            addMouseMotionListener ( mouseHandler );

            // Registering property change listener
            this.splitPane.addPropertyChangeListener ( this );

            // Updating one touch buttons
            if ( this.splitPane.isOneTouchExpandable () )
            {
                updateOneTouchButtons ();
            }
        }

        /**
         * Informing about split pane change.
         */
        firePropertyChange ( SPLIT_PANE_PROPERTY, old, this.splitPane );
    }

    /**
     * Returns {@link JSplitPane} this divider is used for.
     *
     * @return {@link JSplitPane} this divider is used for
     */
    public JSplitPane getSplitPane ()
    {
        return splitPane;
    }

    /**
     * Returns {@link WSplitPaneUI} of the {@link JSplitPane} this divider is used for.
     *
     * @return {@link WSplitPaneUI} of the {@link JSplitPane} this divider is used for
     */
    public WSplitPaneUI getSplitPaneUI ()
    {
        return splitPaneUI;
    }

    /**
     * Sets divider size.
     * That will be divider width if for {@link JSplitPane#HORIZONTAL_SPLIT} and height for {@link JSplitPane#VERTICAL_SPLIT}.
     *
     * @param size new divider size
     */
    public void setDividerSize ( final int size )
    {
        dividerSize = size;
    }

    /**
     * Returns divider size.
     * That is divider width if for {@link JSplitPane#HORIZONTAL_SPLIT} and height for {@link JSplitPane#VERTICAL_SPLIT}.
     *
     * @return divider size
     */
    public int getDividerSize ()
    {
        return dividerSize;
    }

    @Override
    public void propertyChange ( final PropertyChangeEvent e )
    {
        if ( e.getSource () == splitPane )
        {
            if ( e.getPropertyName ().equals ( JSplitPane.ORIENTATION_PROPERTY ) )
            {
                updateOrientationAndCursor ();
                updateOneTouchButtons ();
            }
            else if ( e.getPropertyName ().equals ( JSplitPane.ONE_TOUCH_EXPANDABLE_PROPERTY ) )
            {
                updateOneTouchButtons ();
            }
        }
    }

    /**
     * Updates locally stored {@link JSplitPane} orientation and divider cursor.
     */
    protected void updateOrientationAndCursor ()
    {
        orientation = splitPane.getOrientation ();
        setCursor ( orientation == JSplitPane.HORIZONTAL_SPLIT ?
                Cursor.getPredefinedCursor ( Cursor.E_RESIZE_CURSOR ) :
                Cursor.getPredefinedCursor ( Cursor.S_RESIZE_CURSOR ) );
    }

    /**
     * Updates divider one-touch buttons.
     * Will create the {@link #leftButton} and {@link #rightButton} if they are {@code null}.
     * Will update {@link #leftButton} and {@link #rightButton} decoration states if buttons exist.
     */
    protected void updateOneTouchButtons ()
    {
        if ( splitPane.isOneTouchExpandable () )
        {
            if ( leftButton == null && rightButton == null )
            {
                /**
                 * Creating left and right buttons.
                 */
                leftButton = createLeftOneTouchButton ();
                rightButton = createRightOneTouchButton ();

                /**
                 * Adding them onto the divider.
                 */
                add ( leftButton );
                add ( rightButton );
            }
            else
            {
                /**
                 * Updating button decoration states.
                 */
                DecorationUtils.fireStatesChanged ( leftButton );
                DecorationUtils.fireStatesChanged ( rightButton );
            }
        }

        /**
         * Revalidating divider.
         */
        revalidate ();
    }

    /**
     * Creates and returns new {@link WebButton} that can be used to collapse the left component in {@link JSplitPane}.
     *
     * @return new {@link WebButton} that can be used to collapse the left component in {@link JSplitPane}
     */
    protected WebButton createLeftOneTouchButton ()
    {
        final OneTouchButton button = new OneTouchButton ( StyleId.splitpanedividerOneTouchLeftButton.at ( this ) );
        button.addActionListener ( new OneTouchActionHandler ( true ) );
        return button;
    }

    /**
     * Creates and returns new {@link WebButton} that can be used to collapse the right component in {@link JSplitPane}.
     *
     * @return new {@link WebButton} that can be used to collapse the right component in {@link JSplitPane}
     */
    protected WebButton createRightOneTouchButton ()
    {
        final OneTouchButton button = new OneTouchButton ( StyleId.splitpanedividerOneTouchRightButton.at ( this ) );
        button.addActionListener ( new OneTouchActionHandler ( false ) );
        return button;
    }

    /**
     * Message to prepare for dragging. This messages the {@link WSplitPaneUI} with startDragging.
     */
    protected void prepareForDragging ()
    {
        splitPaneUI.startDragging ();
    }

    /**
     * Messages the {@link WSplitPaneUI} with dragDividerTo that this instance is contained in.
     *
     * @param location new divider location
     */
    protected void dragDividerTo ( final int location )
    {
        splitPaneUI.dragDividerTo ( location );
    }

    /**
     * Messages the {@link WSplitPaneUI} with finishDraggingTo that this instance is contained in.
     *
     * @param location last divider location
     */
    protected void finishDraggingTo ( final int location )
    {
        splitPaneUI.finishDraggingTo ( location );
    }

    /**
     * Returns the look and feel (LaF) object that renders this component.
     *
     * @return the {@link WSplitPaneDividerUI} object that renders this component
     */
    public WSplitPaneDividerUI getUI ()
    {
        return ( WSplitPaneDividerUI ) ui;
    }

    /**
     * Sets the LaF object that renders this component.
     *
     * @param ui {@link WSplitPaneDividerUI}
     */
    public void setUI ( final WSplitPaneDividerUI ui )
    {
        super.setUI ( ui );
    }

    @Override
    public void updateUI ()
    {
        StyleManager.getDescriptor ( this ).updateUI ( this );
    }

    @NotNull
    @Override
    public String getUIClassID ()
    {
        return StyleManager.getDescriptor ( this ).getUIClassId ();
    }

    /**
     * Custom one-touch button.
     */
    public class OneTouchButton extends WebButton implements Stateful
    {
        /**
         * Constructs new {@link OneTouchButton}.
         *
         * @param id style ID
         */
        public OneTouchButton ( final StyleId id )
        {
            super ( id );
        }

        @Nullable
        @Override
        public List<String> getStates ()
        {
            final List<String> states;
            final JSplitPane splitPane = getSplitPane ();
            if ( splitPane != null )
            {
                // Additional split pane states
                states = new ArrayList<String> ( 1 );

                // Buttons positioning orientation
                final boolean vertical = splitPane.getOrientation () == JSplitPane.HORIZONTAL_SPLIT;
                states.add ( vertical ? DecorationState.vertical : DecorationState.horizontal );

                // One-touch
                if ( splitPane.isOneTouchExpandable () )
                {
                    states.add ( DecorationState.oneTouch );
                }
            }
            else
            {
                // No additional states
                states = null;
            }
            return states;
        }
    }

    /**
     * MouseHandler is responsible for converting mouse events (released, dragged...) into the appropriate DragController methods.
     */
    protected class MouseHandler extends MouseAdapter
    {
        /**
         * Starts the dragging session by creating the appropriate instance of DragController.
         */
        @Override
        public void mousePressed ( final MouseEvent e )
        {
            if ( ( e.getSource () == WebSplitPaneDivider.this || e.getSource () == splitPane ) && dragger == null &&
                    splitPane.isEnabled () )
            {
                final Component newHiddenDivider = splitPaneUI.getNonContinuousLayoutDivider ();
                if ( hiddenDivider != newHiddenDivider )
                {
                    if ( hiddenDivider != null )
                    {
                        hiddenDivider.removeMouseListener ( this );
                        hiddenDivider.removeMouseMotionListener ( this );
                    }
                    hiddenDivider = newHiddenDivider;
                    if ( hiddenDivider != null )
                    {
                        hiddenDivider.addMouseMotionListener ( this );
                        hiddenDivider.addMouseListener ( this );
                    }
                }
                if ( splitPane.getLeftComponent () != null && splitPane.getRightComponent () != null )
                {
                    if ( orientation == JSplitPane.HORIZONTAL_SPLIT )
                    {
                        dragger = new DragController ( e );
                    }
                    else
                    {
                        dragger = new VerticalDragController ( e );
                    }
                    if ( !dragger.isValid () )
                    {
                        dragger = null;
                    }
                    else
                    {
                        prepareForDragging ();
                        dragger.continueDrag ( e );
                    }
                }
                e.consume ();
            }
        }

        /**
         * If dragger is not null it is messaged with completeDrag.
         */
        @Override
        public void mouseReleased ( final MouseEvent e )
        {
            if ( dragger != null )
            {
                if ( e.getSource () == splitPane )
                {
                    dragger.completeDrag ( e.getX (), e.getY () );
                }
                else if ( e.getSource () == WebSplitPaneDivider.this )
                {
                    final Point ourLoc = getLocation ();
                    dragger.completeDrag ( e.getX () + ourLoc.x, e.getY () + ourLoc.y );
                }
                else if ( e.getSource () == hiddenDivider )
                {
                    final Point hDividerLoc = hiddenDivider.getLocation ();
                    final int ourX = e.getX () + hDividerLoc.x;
                    final int ourY = e.getY () + hDividerLoc.y;

                    dragger.completeDrag ( ourX, ourY );
                }
                dragger = null;
                e.consume ();
            }
        }

        /**
         * If dragger is not null it is messaged with continueDrag.
         */
        @Override
        public void mouseDragged ( final MouseEvent e )
        {
            if ( dragger != null )
            {
                if ( e.getSource () == splitPane )
                {
                    dragger.continueDrag ( e.getX (), e.getY () );
                }
                else if ( e.getSource () == WebSplitPaneDivider.this )
                {
                    final Point ourLoc = getLocation ();
                    dragger.continueDrag ( e.getX () + ourLoc.x, e.getY () + ourLoc.y );
                }
                else if ( e.getSource () == hiddenDivider )
                {
                    final Point hDividerLoc = hiddenDivider.getLocation ();
                    final int ourX = e.getX () + hDividerLoc.x;
                    final int ourY = e.getY () + hDividerLoc.y;

                    dragger.continueDrag ( ourX, ourY );
                }
                e.consume ();
            }
        }
    }

    /**
     * Handles the events during a dragging session for a {@link JSplitPane#HORIZONTAL_SPLIT} oriented split pane.
     * This continually messages {@code dragDividerTo} and then when done messages {@code finishDraggingTo}.
     * When an instance is created it should be messaged with {@code isValid} to insure that dragging can happen.
     * Dragging won't be allowed if the two views can not be resized.
     */
    protected class DragController
    {
        /**
         * Initial location of the divider.
         */
        protected int initialX;

        /**
         * Maximum and minimum positions to drag to.
         */
        protected int maxX, minX;

        /**
         * Initial location the mouse down happened at.
         */
        protected int offset;

        /**
         * Constructs new {@link DragController} to handle the specified {@link MouseEvent}.
         *
         * @param event {@link MouseEvent}
         */
        protected DragController ( final MouseEvent event )
        {
            super ();

            final JSplitPane splitPane = splitPaneUI.getSplitPane ();
            final Component leftC = splitPane.getLeftComponent ();
            final Component rightC = splitPane.getRightComponent ();

            initialX = getLocation ().x;
            if ( event.getSource () == WebSplitPaneDivider.this )
            {
                offset = event.getX ();
            }
            else
            {
                // splitPane
                offset = event.getX () - initialX;
            }
            if ( leftC == null || rightC == null || offset < -1 || offset >= getSize ().width )
            {
                // Don't allow dragging.
                maxX = -1;
            }
            else
            {
                final Insets insets = splitPane.getInsets ();
                if ( leftC.isVisible () )
                {
                    minX = leftC.getMinimumSize ().width;
                    if ( insets != null )
                    {
                        minX += insets.left;
                    }
                }
                else
                {
                    minX = 0;
                }
                if ( rightC.isVisible () )
                {
                    final int right = insets != null ? insets.right : 0;
                    maxX = Math.max ( 0, splitPane.getSize ().width - ( getSize ().width + right ) - rightC.getMinimumSize ().width );
                }
                else
                {
                    final int right = insets != null ? insets.right : 0;
                    maxX = Math.max ( 0, splitPane.getSize ().width - ( getSize ().width + right ) );
                }
                if ( maxX < minX )
                {
                    minX = maxX = 0;
                }
            }
        }

        /**
         * Returns whether or not the dragging session is valid.
         *
         * @return {@code true} if the dragging session is valid, {@code false} otherwise
         */
        protected boolean isValid ()
        {
            return maxX > 0;
        }

        /**
         * Returns new position to put the divider at based on the passed in {@link MouseEvent}.
         *
         * @param event {@link MouseEvent}
         * @return new position to put the divider at based on the passed in {@link MouseEvent}
         */
        protected int positionForMouseEvent ( final MouseEvent event )
        {
            int newX = event.getSource () == WebSplitPaneDivider.this ? event.getX () + getLocation ().x : event.getX ();
            newX = Math.min ( maxX, Math.max ( minX, newX - offset ) );
            return newX;
        }

        /**
         * Returns x argument, since this is used for horizontal splits.
         *
         * @param x X coordinate
         * @param y Y coordinate
         * @return x argument, since this is used for horizontal splits
         */
        protected int getNeededLocation ( final int x, final int y )
        {
            return Math.min ( maxX, Math.max ( minX, x - offset ) );
        }

        /**
         * Continues drag operation.
         *
         * @param newX new X coordinate
         * @param newY new Y coordinate
         */
        protected void continueDrag ( final int newX, final int newY )
        {
            dragDividerTo ( getNeededLocation ( newX, newY ) );
        }

        /**
         * Messages dragDividerTo with the new location for the {@link MouseEvent}.
         *
         * @param event {@link MouseEvent}
         */
        protected void continueDrag ( final MouseEvent event )
        {
            dragDividerTo ( positionForMouseEvent ( event ) );
        }

        /**
         * Finishes drag operation.
         *
         * @param x X coordinate
         * @param y Y coordinate
         */
        protected void completeDrag ( final int x, final int y )
        {
            finishDraggingTo ( getNeededLocation ( x, y ) );
        }
    }

    /**
     * Handles the events during a dragging session for a {@link JSplitPane#VERTICAL_SPLIT} oriented split pane.
     * This continually messages {@code dragDividerTo} and then when done messages {@code finishDraggingTo}.
     * When an instance is created it should be messaged with {@code isValid} to insure that dragging can happen.
     * Dragging won't be allowed if the two views can not be resized.
     */
    protected class VerticalDragController extends DragController
    {
        /**
         * Constructs new {@link VerticalDragController} to handle the specified {@link MouseEvent}.
         *
         * @param event {@link MouseEvent}
         */
        protected VerticalDragController ( final MouseEvent event )
        {
            super ( event );

            final JSplitPane splitPane = splitPaneUI.getSplitPane ();
            final Component leftC = splitPane.getLeftComponent ();
            final Component rightC = splitPane.getRightComponent ();

            initialX = getLocation ().y;
            if ( event.getSource () == WebSplitPaneDivider.this )
            {
                offset = event.getY ();
            }
            else
            {
                offset = event.getY () - initialX;
            }
            if ( leftC == null || rightC == null || offset < -1 || offset > getSize ().height )
            {
                // Don't allow dragging.
                maxX = -1;
            }
            else
            {
                final Insets insets = splitPane.getInsets ();
                if ( leftC.isVisible () )
                {
                    minX = leftC.getMinimumSize ().height;
                    if ( insets != null )
                    {
                        minX += insets.top;
                    }
                }
                else
                {
                    minX = 0;
                }
                if ( rightC.isVisible () )
                {
                    final int bottom = insets != null ? insets.bottom : 0;
                    maxX = Math.max ( 0, splitPane.getSize ().height - ( getSize ().height + bottom ) - rightC.getMinimumSize ().height );
                }
                else
                {
                    final int bottom = insets != null ? insets.bottom : 0;
                    maxX = Math.max ( 0, splitPane.getSize ().height - ( getSize ().height + bottom ) );
                }
                if ( maxX < minX )
                {
                    minX = maxX = 0;
                }
            }
        }

        @Override
        protected int getNeededLocation ( final int x, final int y )
        {
            final int newY;
            newY = Math.min ( maxX, Math.max ( minX, y - offset ) );
            return newY;
        }

        @Override
        protected int positionForMouseEvent ( final MouseEvent e )
        {
            int newY = e.getSource () == WebSplitPaneDivider.this ? e.getY () + getLocation ().y : e.getY ();
            newY = Math.min ( maxX, Math.max ( minX, newY - offset ) );
            return newY;
        }
    }

    /**
     * {@link LayoutManager} for {@link WebSplitPaneDivider}.
     * Layout for the divider involves appropriately moving the left/right buttons around.
     */
    protected class DividerLayout extends AbstractLayoutManager
    {
        @Override
        public void layoutContainer ( @NotNull final Container container )
        {
            if ( leftButton != null && rightButton != null && container == WebSplitPaneDivider.this )
            {
                if ( splitPane.isOneTouchExpandable () )
                {
                    /**
                     * Buttons positioning is replaced with custom code to avoid some unnecessary restrictions.
                     * Check out {@link javax.swing.plaf.basic.BasicSplitPaneDivider.DividerLayout#layoutContainer(Container)} for original.
                     */
                    final Insets insets = getInsets ();
                    final Dimension lps = leftButton.getPreferredSize ();
                    final Dimension rps = rightButton.getPreferredSize ();
                    if ( orientation == JSplitPane.VERTICAL_SPLIT )
                    {
                        final boolean ltr = getComponentOrientation ().isLeftToRight ();
                        final int dividerHeight = getHeight () - ( insets != null ? insets.top + insets.bottom : 0 );
                        final int y = insets != null ? insets.top : 0;
                        if ( ltr )
                        {
                            final int x = insets != null ? insets.left : 0;
                            leftButton.setBounds ( x, y, lps.width, dividerHeight );
                            rightButton.setBounds ( x + lps.width, y, rps.width, dividerHeight );
                        }
                        else
                        {
                            final int x = getWidth () - lps.width - rps.width - ( insets != null ? insets.right : 0 );
                            rightButton.setBounds ( x, y, rps.width, dividerHeight );
                            leftButton.setBounds ( x + rps.width, y, lps.width, dividerHeight );
                        }
                    }
                    else
                    {
                        final int x = insets != null ? insets.left : 0;
                        final int y = insets != null ? insets.top : 0;
                        final int dividerWidth = getWidth () - ( insets != null ? insets.left + insets.right : 0 );

                        leftButton.setBounds ( x, y, dividerWidth, lps.height );
                        rightButton.setBounds ( x, y + lps.height, dividerWidth, rps.height );
                    }

                    // Displaying buttons
                    leftButton.setVisible ( true );
                    rightButton.setVisible ( true );
                }
                else
                {
                    // Hiding buttons
                    leftButton.setVisible ( false );
                    rightButton.setVisible ( false );
                }
            }
        }

        @NotNull
        @Override
        public Dimension minimumLayoutSize ( @NotNull final Container container )
        {
            return preferredLayoutSize ( container );
        }

        /**
         * NOTE: This isn't really used due to divider being forcefully positioned according to predefined sizes.
         * I leave a proper implementation for this method in hopes of using it at some point instead of workarounds.
         */
        @NotNull
        @Override
        public Dimension preferredLayoutSize ( @NotNull final Container container )
        {
            final Dimension ps;
            if ( container == WebSplitPaneDivider.this && splitPane != null )
            {
                Dimension buttonSize = null;
                if ( splitPane.isOneTouchExpandable () )
                {
                    if ( leftButton != null )
                    {
                        buttonSize = leftButton.getPreferredSize ();
                    }
                    if ( rightButton != null )
                    {
                        buttonSize = SwingUtils.max ( buttonSize, rightButton.getPreferredSize () );
                    }
                }

                final Insets insets = getInsets ();
                int dividerWidth = getDividerSize ();
                if ( orientation == JSplitPane.VERTICAL_SPLIT )
                {
                    if ( buttonSize != null )
                    {
                        int size = buttonSize.height;
                        if ( insets != null )
                        {
                            size += insets.top + insets.bottom;
                        }
                        dividerWidth = Math.max ( dividerWidth, size );
                    }
                    ps = new Dimension ( 1, dividerWidth );
                }
                else
                {
                    if ( buttonSize != null )
                    {
                        int size = buttonSize.width;
                        if ( insets != null )
                        {
                            size += insets.left + insets.right;
                        }
                        dividerWidth = Math.max ( dividerWidth, size );
                    }
                    ps = new Dimension ( dividerWidth, 1 );
                }
            }
            else
            {
                ps = new Dimension ( 0, 0 );
            }
            return ps;
        }
    }

    /**
     * Listeners installed on the one touch expandable buttons.
     */
    protected class OneTouchActionHandler implements ActionListener
    {
        /**
         * True indicates the resize should go the minimum (top or left)
         * vs false which indicates the resize should go to the maximum.
         */
        private final boolean toMinimum;

        /**
         * Constructs new {@link OneTouchActionHandler}.
         *
         * @param toMinimum handler direction
         */
        public OneTouchActionHandler ( final boolean toMinimum )
        {
            this.toMinimum = toMinimum;
        }

        @Override
        public void actionPerformed ( final ActionEvent e )
        {
            final Insets insets = splitPane.getInsets ();
            final int lastLoc = splitPane.getLastDividerLocation ();
            final int currentLoc = splitPaneUI.getDividerLocation ( splitPane );
            final int newLoc;

            // We use the location from the UI directly, as the location the
            // JSplitPane itself maintains is not necessarly correct.
            if ( toMinimum )
            {
                if ( orientation == JSplitPane.VERTICAL_SPLIT )
                {
                    if ( currentLoc >= splitPane.getHeight () - insets.bottom - getHeight () )
                    {
                        final int maxLoc = splitPane.getMaximumDividerLocation ();
                        newLoc = Math.min ( lastLoc, maxLoc );
                        splitPaneUI.setKeepHidden ( false );
                    }
                    else
                    {
                        newLoc = insets.top;
                        splitPaneUI.setKeepHidden ( true );
                    }
                }
                else
                {
                    if ( currentLoc >= splitPane.getWidth () - insets.right - getWidth () )
                    {
                        final int maxLoc = splitPane.getMaximumDividerLocation ();
                        newLoc = Math.min ( lastLoc, maxLoc );
                        splitPaneUI.setKeepHidden ( false );
                    }
                    else
                    {
                        newLoc = insets.left;
                        splitPaneUI.setKeepHidden ( true );
                    }
                }
            }
            else
            {
                if ( orientation == JSplitPane.VERTICAL_SPLIT )
                {
                    if ( currentLoc == insets.top )
                    {
                        final int maxLoc = splitPane.getMaximumDividerLocation ();
                        newLoc = Math.min ( lastLoc, maxLoc );
                        splitPaneUI.setKeepHidden ( false );
                    }
                    else
                    {
                        newLoc = splitPane.getHeight () - getHeight () - insets.top;
                        splitPaneUI.setKeepHidden ( true );
                    }
                }
                else
                {
                    if ( currentLoc == insets.left )
                    {
                        final int maxLoc = splitPane.getMaximumDividerLocation ();
                        newLoc = Math.min ( lastLoc, maxLoc );
                        splitPaneUI.setKeepHidden ( false );
                    }
                    else
                    {
                        newLoc = splitPane.getWidth () - getWidth () - insets.left;
                        splitPaneUI.setKeepHidden ( true );
                    }
                }
            }
            if ( currentLoc != newLoc )
            {
                splitPane.setDividerLocation ( newLoc );
                // We do this in case the dividers notion of the location
                // differs from the real location.
                splitPane.setLastDividerLocation ( currentLoc );
            }
        }
    }
}