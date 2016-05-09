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

package com.alee.extended.window;

import com.alee.laf.WebLookAndFeel;
import com.alee.laf.menu.AbstractPopupPainter;
import com.alee.laf.menu.PopupStyle;
import com.alee.laf.rootpane.WebRootPaneUI;
import com.alee.utils.ProprietaryUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.AncestorAdapter;
import com.alee.utils.swing.DataProvider;
import com.alee.utils.swing.WindowFollowBehavior;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Basic painter for WebPopOver component.
 * It is used as default WebPopOver painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Mikle Garin
 */

public class PopOverPainter<E extends JRootPane, U extends WebRootPaneUI> extends AbstractPopupPainter<E, U> implements IPopOverPainter<E, U>
{
    /**
     * Listeners.
     */
    protected ComponentMoveBehavior moveAdapter;
    protected WindowFocusListener focusListener;
    protected ComponentAdapter resizeAdapter;

    /**
     * Whether or not popover is focused.
     */
    protected boolean popOverFocused = false;

    /**
     * Whether or not popover is attached to invoker component.
     */
    protected boolean attached = false;

    /**
     * WebPopOver display source point.
     */
    protected PopOverSourcePoint popOverSourcePoint = PopOverSourcePoint.componentSide;

    /**
     * Preferred direction in which WebPopOver should be displayed.
     */
    protected PopOverDirection preferredDirection = null;

    /**
     * Current display direction.
     */
    protected PopOverDirection currentDirection = null;

    /**
     * Preferred WebPopOver alignment relative to display source point.
     */
    protected PopOverAlignment preferredAlignment = null;

    @Override
    public void install ( final E c, final U ui )
    {
        // Must call super to install default settings
        super.install ( c, ui );

        // Retrieving popover instance
        final WebPopOver popOver = getPopOver ( c );

        // Window focus listener
        focusListener = new WindowFocusListener ()
        {
            @Override
            public void windowGainedFocus ( final WindowEvent e )
            {
                setPopOverFocused ( true );
            }

            @Override
            public void windowLostFocus ( final WindowEvent e )
            {
                setPopOverFocused ( false );
            }
        };
        popOver.addWindowFocusListener ( focusListener );

        // Popover drag listener
        moveAdapter = new ComponentMoveBehavior ()
        {
            @Override
            protected Rectangle getDragStartBounds ( final MouseEvent e )
            {
                if ( popOver.isMovable () )
                {
                    final int sw = getShadeWidth ();
                    return new Rectangle ( sw, sw, c.getWidth () - sw * 2, c.getHeight () - sw * 2 );
                }
                else
                {
                    return null;
                }
            }

            @Override
            public void mouseDragged ( final MouseEvent e )
            {
                // De-attach dialog
                if ( dragging && attached )
                {
                    attached = false;
                    preferredDirection = null;
                    setPopupStyle ( PopupStyle.simple );
                    popOver.fireDetached ();
                }

                super.mouseDragged ( e );
            }
        };
        c.addMouseListener ( moveAdapter );
        c.addMouseMotionListener ( moveAdapter );

        // Custom window shaping
        if ( !ProprietaryUtils.isWindowTransparencyAllowed () && ProprietaryUtils.isWindowShapeAllowed () )
        {
            resizeAdapter = new ComponentAdapter ()
            {
                @Override
                public void componentResized ( final ComponentEvent e )
                {
                    final Rectangle bounds = popOver.getBounds ();
                    bounds.width++;
                    bounds.height++;
                    ProprietaryUtils.setWindowShape ( popOver, provideShape ( c, bounds ) );
                }
            };
            popOver.addComponentListener ( resizeAdapter );
        }
    }

    @Override
    public void uninstall ( final E c, final U ui )
    {
        // Retrieving popover instance
        final WebPopOver popOver = getPopOver ( c );

        // Custom window shaping
        if ( resizeAdapter != null )
        {
            popOver.removeComponentListener ( resizeAdapter );
            resizeAdapter = null;
        }

        // Popover drag listener
        c.removeMouseMotionListener ( moveAdapter );
        c.removeMouseListener ( moveAdapter );
        moveAdapter = null;

        // Window focus listener
        popOver.removeWindowFocusListener ( focusListener );
        focusListener = null;

        // Must call super to uninstall default settings
        super.uninstall ( c, ui );
    }

    @Override
    public boolean isDecorated ()
    {
        return true;
    }

    /**
     * Returns whether this WebPopOver is focus owner or not.
     *
     * @return true if this WebPopOver is focus owner, false otherwise
     */
    public boolean isPopOverFocused ()
    {
        return popOverFocused;
    }

    /**
     * Sets whether this WebPopOver is focus owner or not.
     *
     * @param focused whether this WebPopOver is focus owner or not
     */
    public void setPopOverFocused ( final boolean focused )
    {
        if ( this.popOverFocused != focused )
        {
            this.popOverFocused = focused;
            repaint ();
        }
    }

    @Override
    protected float getCurrentShadeOpacity ()
    {
        // Reducing the shade when WebPopOver is not focused
        return popOverFocused ? shadeOpacity : shadeOpacity * 0.7f;
    }

    /**
     * Returns popup display source point.
     *
     * @return popup display source point
     */
    public PopOverSourcePoint getPopOverSourcePoint ()
    {
        return popOverSourcePoint;
    }

    /**
     * Sets popup display source point.
     *
     * @param popOverSourcePoint popup display source point
     */
    public void setPopOverSourcePoint ( final PopOverSourcePoint popOverSourcePoint )
    {
        this.popOverSourcePoint = popOverSourcePoint;
    }

    /**
     * Returns preferred direction in which WebPopOver should be displayed.
     *
     * @return preferred direction in which WebPopOver should be displayed
     */
    public PopOverDirection getPreferredDirection ()
    {
        return preferredDirection;
    }

    /**
     * Sets preferred direction in which WebPopOver should be displayed.
     *
     * @param direction preferred direction in which WebPopOver should be displayed
     */
    public void setPreferredDirection ( final PopOverDirection direction )
    {
        this.preferredDirection = direction;
    }

    /**
     * Returns current display direction.
     *
     * @return current display direction
     */
    public PopOverDirection getCurrentDirection ()
    {
        return currentDirection;
    }

    /**
     * Sets current display direction.
     *
     * @param direction current display direction
     */
    public void setCurrentDirection ( final PopOverDirection direction )
    {
        this.currentDirection = direction;
    }

    /**
     * Displays unattached WebPopOver at the specified screen location.
     *
     * @param popOver  WebPopOver to configure
     * @param location WebPopOver location on screen
     */
    @Override
    public void configure ( final WebPopOver popOver, final PopOverLocation location )
    {
        // Updating WebPopOver variables
        attached = false;
        preferredDirection = null;
        setPopupStyle ( PopupStyle.simple );

        // Updating dialog location on screen and size
        final Dimension ss = Toolkit.getDefaultToolkit ().getScreenSize ();
        popOver.pack ();
        switch ( location )
        {
            case center:
            {
                popOver.setLocation ( ss.width / 2 - popOver.getWidth () / 2, ss.height / 2 - popOver.getHeight () / 2 );
                break;
            }
            case topLeft:
            {
                popOver.setLocation ( 0, 0 );
                break;
            }
            case topRight:
            {
                popOver.setLocation ( ss.width - popOver.getWidth (), 0 );
                break;
            }
            case bottomLeft:
            {
                popOver.setLocation ( 0, ss.height - popOver.getHeight () );
                break;
            }
            case bottomRight:
            {
                popOver.setLocation ( ss.width - popOver.getWidth (), ss.height - popOver.getHeight () );
                break;
            }
            case topCenter:
            {
                popOver.setLocation ( ss.width / 2 - popOver.getWidth () / 2, 0 );
                break;
            }
            case bottomCenter:
            {
                popOver.setLocation ( ss.width / 2 - popOver.getWidth () / 2, ss.height - popOver.getHeight () );
                break;
            }
            case leftCenter:
            {
                popOver.setLocation ( 0, ss.height / 2 - popOver.getHeight () / 2 );
                break;
            }
            case rightCenter:
            {
                popOver.setLocation ( ss.width - popOver.getWidth (), ss.height / 2 - popOver.getHeight () / 2 );
                break;
            }
        }
    }

    /**
     * Displays unattached WebPopOver at the specified location.
     *
     * @param popOver WebPopOver to configure
     * @param x       WebPopOver X location
     * @param y       WebPopOver Y location
     */
    @Override
    public void configure ( final WebPopOver popOver, final int x, final int y )
    {
        // Updating WebPopOver variables
        attached = false;
        preferredDirection = null;
        setPopupStyle ( PopupStyle.simple );

        // Updating dialog location on screen and size
        popOver.pack ();
        popOver.setLocation ( x - getShadeWidth (), y - getShadeWidth () );
    }

    /**
     * Displays WebPopOver attached to the invoker component area and faced to specified direction.
     * It will also be aligned using the specified alignment type when possible.
     * WebPopOver opened in this way will always auto-follow invoker's ancestor window.
     *
     * @param popOver        WebPopOver to configure
     * @param invoker        invoker component
     * @param boundsProvider source area bounds provider
     * @param direction      preferred display direction
     * @param alignment      preferred display alignment
     */
    @Override
    public void configure ( final WebPopOver popOver, final Component invoker, final DataProvider<Rectangle> boundsProvider,
                            final PopOverDirection direction, final PopOverAlignment alignment )
    {
        // Translating coordinates into screen coordinates system
        final DataProvider<Rectangle> actualBoundsProvider = boundsProvider == null ? null : new DataProvider<Rectangle> ()
        {
            private Rectangle lastBounds = new Rectangle ();

            @Override
            public Rectangle provide ()
            {
                // Invoker might be hidden while WebPopOver is still visible
                // This is why we should simply stop updating its position when that happens
                // It is not the best workaround but at least it will keep us safe from exceptions
                if ( invoker.isShowing () )
                {
                    final Rectangle bounds = boundsProvider.provide ();
                    final Point los = invoker.getLocationOnScreen ();
                    lastBounds = new Rectangle ( los.x + bounds.x, los.y + bounds.y, bounds.width, bounds.height );
                }
                return lastBounds;
            }
        };

        // Updating WebPopOver variables
        attached = true;
        preferredDirection = direction != null ? direction : PopOverDirection.down;
        preferredAlignment = alignment;

        // Updating dialog location on screen and size
        popOver.pack ();
        updatePopOverLocation ( popOver, invoker, actualBoundsProvider );
        installPopOverLocationUpdater ( popOver, invoker, actualBoundsProvider );
    }

    /**
     * Updates WebPopOver location on screen.
     *
     * @param popOver        WebPopOver to configure
     * @param invoker        invoker component
     * @param boundsProvider source area bounds provider
     */
    protected void updatePopOverLocation ( final WebPopOver popOver, final Component invoker, final DataProvider<Rectangle> boundsProvider )
    {
        if ( boundsProvider != null )
        {
            updatePopOverLocation ( popOver, invoker, boundsProvider.provide () );
        }
        else
        {
            updatePopOverLocation ( popOver, invoker );
        }
    }

    /**
     * Updates WebPopOver location on screen.
     *
     * @param popOver WebPopOver to configure
     * @param invoker invoker component
     */
    protected void updatePopOverLocation ( final WebPopOver popOver, final Component invoker )
    {
        if ( invoker instanceof Window )
        {
            // Applying proper painter style
            setPopupStyle ( PopupStyle.simple );

            // Determining final WebPopOver position
            final Rectangle ib = invoker.getBounds ();
            final Dimension size = popOver.getSize ();

            // Updating current direction
            currentDirection = null;

            // Updating WebPopOver location
            popOver.setLocation ( ib.x + ib.width / 2 - size.width / 2, ib.y + ib.height / 2 - size.height / 2 );
        }
        else if ( invoker.isShowing () )
        {
            // Updating WebPopOver location in a smarter way
            updatePopOverLocation ( popOver, invoker, SwingUtils.getBoundsOnScreen ( invoker ) );
        }
    }

    /**
     * Updates WebPopOver location on screen.
     *
     * @param popOver       WebPopOver to configure
     * @param invoker       invoker component
     * @param invokerBounds invoker component bounds on screen
     */
    protected void updatePopOverLocation ( final WebPopOver popOver, final Component invoker, final Rectangle invokerBounds )
    {
        // Applying proper painter style
        setPopupStyle ( PopupStyle.dropdown );

        // WebPopOver preferred size without shade
        final Dimension size = popOver.getSize ();
        final int sw = getShadeWidth ();
        final int round = getRound ();
        final int cw = getCornerWidth ();
        final Dimension ps = new Dimension ( size.width - sw * 2, size.height - sw * 2 );
        final Rectangle sb = SwingUtils.getScreenBounds ( invoker );
        final Rectangle screenBounds = sb != null ? sb : SwingUtils.getWindowAncestor ( invoker ).getGraphicsConfiguration ().getBounds ();

        // Determining actual direction
        final PopOverDirection actualDirection = getActualDirection ( invokerBounds, ltr, cw, ps, screenBounds );
        setCornerSide ( actualDirection.getCornerSide ( ltr ) );
        currentDirection = actualDirection;

        // Determining position according to alignment
        final Point actualLocation = getActualLocation ( invokerBounds, ltr, round, cw, ps, screenBounds, actualDirection );
        actualLocation.x -= sw;
        actualLocation.y -= sw;

        // Updating corner position
        setCornerAlignment ( -1 );
        setRelativeCorner ( getRelativeCorner ( invokerBounds, actualDirection, actualLocation ) );

        // Updating WebPopOver location
        popOver.setLocation ( actualLocation );
    }

    /**
     * Installs listeners to update WebPopOver location.
     *
     * @param popOver        WebPopOver to configure
     * @param invoker        invoker component
     * @param boundsProvider source area bounds provider
     */
    protected void installPopOverLocationUpdater ( final WebPopOver popOver, final Component invoker,
                                                   final DataProvider<Rectangle> boundsProvider )
    {
        // Invoker component window
        final Window invokerWindow = SwingUtils.getWindowAncestor ( invoker );

        // Invoker window follow adapter
        final WindowFollowBehavior windowFollowBehavior = new WindowFollowBehavior ( popOver, invokerWindow )
        {
            @Override
            public boolean isEnabled ()
            {
                return !attached;
            }
        };
        invokerWindow.addComponentListener ( windowFollowBehavior );

        // Invoker window state listener
        final WindowStateListener windowStateListener = new WindowStateListener ()
        {
            @Override
            public void windowStateChanged ( final WindowEvent e )
            {
                if ( attached )
                {
                    if ( e.getNewState () == WindowEvent.WINDOW_ICONIFIED )
                    {
                        popOver.setVisible ( false );
                    }
                    else if ( e.getOldState () == WindowEvent.WINDOW_ICONIFIED )
                    {
                        popOver.setVisible ( true );
                    }
                }
            }
        };
        invokerWindow.addWindowStateListener ( windowStateListener );

        // Invoker window adapter
        final ComponentAdapter invokerWindowAdapter = new ComponentAdapter ()
        {
            @Override
            public void componentMoved ( final ComponentEvent e )
            {
                if ( attached )
                {
                    updatePopOverLocation ( popOver, invoker, boundsProvider );
                    windowFollowBehavior.updateLastLocation ();
                }
            }
        };
        invokerWindow.addComponentListener ( invokerWindowAdapter );

        // Invoker component adapter
        final ComponentAdapter invokerAdapter = new ComponentAdapter ()
        {
            @Override
            public void componentMoved ( final ComponentEvent e )
            {
                if ( attached )
                {
                    updatePopOverLocation ( popOver, invoker, boundsProvider );
                    windowFollowBehavior.updateLastLocation ();
                }
            }

            @Override
            public void componentResized ( final ComponentEvent e )
            {
                if ( attached )
                {
                    updatePopOverLocation ( popOver, invoker, boundsProvider );
                    windowFollowBehavior.updateLastLocation ();
                }
            }
        };
        invoker.addComponentListener ( invokerAdapter );

        final AncestorAdapter ancestorAdapter;
        if ( invoker instanceof JComponent )
        {
            ancestorAdapter = new AncestorAdapter ()
            {
                @Override
                public void ancestorMoved ( final AncestorEvent event )
                {
                    if ( attached )
                    {
                        updatePopOverLocation ( popOver, invoker, boundsProvider );
                        windowFollowBehavior.updateLastLocation ();
                    }
                }

                // todo Probably hide WebPopOver in some cases here?
                //                @Override
                //                public void ancestorRemoved ( final AncestorEvent event )
                //                {
                //                    super.ancestorRemoved ( event );
                //                }
            };
            ( ( JComponent ) invoker ).addAncestorListener ( ancestorAdapter );
        }
        else
        {
            ancestorAdapter = null;
        }

        // WebPopOver orientation change listener
        final PropertyChangeListener orientationListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                updatePopOverLocation ( popOver, invoker, boundsProvider );
                windowFollowBehavior.updateLastLocation ();
            }
        };
        popOver.addPropertyChangeListener ( WebLookAndFeel.COMPONENT_ORIENTATION_PROPERTY, orientationListener );

        // Removing all listeners on window close event
        popOver.addPopOverListener ( new PopOverAdapter ()
        {
            @Override
            public void reopened ( final WebPopOver popOver )
            {
                destroy ();
            }

            @Override
            public void closed ( final WebPopOver popOver )
            {
                destroy ();
            }

            /**
             * Destroys popover.
             */
            protected void destroy ()
            {
                popOver.removePopOverListener ( this );
                invokerWindow.removeComponentListener ( invokerWindowAdapter );
                invokerWindow.removeComponentListener ( windowFollowBehavior );
                invoker.removeComponentListener ( invokerAdapter );
                if ( invoker instanceof JComponent )
                {
                    ( ( JComponent ) invoker ).removeAncestorListener ( ancestorAdapter );
                }
                popOver.removePropertyChangeListener ( WebLookAndFeel.COMPONENT_ORIENTATION_PROPERTY, orientationListener );
            }
        } );
    }

    /**
     * Returns relative corner position.
     *
     * @param ib              invoker component bounds on screen
     * @param actualDirection actual WebPopOver direction
     * @param actualLocation  actual WebPopOver location
     * @return relative corner position
     */
    protected int getRelativeCorner ( final Rectangle ib, final PopOverDirection actualDirection, final Point actualLocation )
    {
        switch ( actualDirection )
        {
            case up:
            case down:
                return ib.x + ib.width / 2 - actualLocation.x;
            case left:
            case right:
                return ib.y + ib.height / 2 - actualLocation.y;
        }
        return -1;
    }

    /**
     * Returns actual WebPopOver location.
     * Shade width is not yet taken into account within this location.
     *
     * @param ib              invoker component bounds on screen
     * @param ltr             whether LTR orientation is active or not
     * @param round           corners round
     * @param cw              corner width
     * @param ps              WebPopOver size without shade widths
     * @param screenBounds    screen bounds
     * @param actualDirection actual WebPopOver direction
     * @return actual WebPopOver location
     */
    protected Point getActualLocation ( final Rectangle ib, final boolean ltr, final int round, final int cw, final Dimension ps,
                                        final Rectangle screenBounds, final PopOverDirection actualDirection )
    {
        final Point sp = getActualSourcePoint ( ib, ltr, actualDirection );
        if ( actualDirection == PopOverDirection.up )
        {
            if ( preferredAlignment == PopOverAlignment.centered )
            {
                final Point location = p ( sp.x - ps.width / 2, sp.y - cw - ps.height );
                return checkRightCollision ( checkLeftCollision ( location, screenBounds ), ps, screenBounds );
            }
            else if ( preferredAlignment == ( ltr ? PopOverAlignment.leading : PopOverAlignment.trailing ) )
            {
                return checkLeftCollision ( p ( sp.x + cw * 2 + round - ps.width, sp.y - cw - ps.height ), screenBounds );
            }
            else if ( preferredAlignment == ( ltr ? PopOverAlignment.trailing : PopOverAlignment.leading ) )
            {
                return checkRightCollision ( p ( sp.x - cw * 2 - round, sp.y - cw - ps.height ), ps, screenBounds );
            }
        }
        else if ( actualDirection == PopOverDirection.down )
        {
            if ( preferredAlignment == PopOverAlignment.centered )
            {
                final Point location = p ( sp.x - ps.width / 2, sp.y + cw );
                return checkRightCollision ( checkLeftCollision ( location, screenBounds ), ps, screenBounds );
            }
            else if ( preferredAlignment == ( ltr ? PopOverAlignment.leading : PopOverAlignment.trailing ) )
            {
                return checkLeftCollision ( p ( sp.x + cw * 2 + round - ps.width, sp.y + cw ), screenBounds );
            }
            else if ( preferredAlignment == ( ltr ? PopOverAlignment.trailing : PopOverAlignment.leading ) )
            {
                return checkRightCollision ( p ( sp.x - cw * 2 - round, sp.y + cw ), ps, screenBounds );
            }
        }
        else if ( actualDirection == ( ltr ? PopOverDirection.left : PopOverDirection.right ) )
        {
            if ( preferredAlignment == PopOverAlignment.centered )
            {
                final Point location = p ( sp.x - cw - ps.width, sp.y - ps.height / 2 );
                return checkBottomCollision ( checkTopCollision ( location, screenBounds ), ps, screenBounds );
            }
            else if ( preferredAlignment == PopOverAlignment.leading )
            {
                return checkTopCollision ( p ( sp.x - cw - ps.width, sp.y + cw * 2 + round - ps.height ), screenBounds );
            }
            else if ( preferredAlignment == PopOverAlignment.trailing )
            {
                return checkBottomCollision ( p ( sp.x - cw - ps.width, sp.y - cw * 2 - round ), ps, screenBounds );
            }
        }
        else if ( actualDirection == ( ltr ? PopOverDirection.right : PopOverDirection.left ) )
        {
            if ( preferredAlignment == PopOverAlignment.centered )
            {
                final Point location = p ( sp.x + cw, sp.y - ps.height / 2 );
                return checkBottomCollision ( checkTopCollision ( location, screenBounds ), ps, screenBounds );
            }
            else if ( preferredAlignment == PopOverAlignment.leading )
            {
                return checkTopCollision ( p ( sp.x + cw, sp.y + cw * 2 + round - ps.height ), screenBounds );
            }
            else if ( preferredAlignment == PopOverAlignment.trailing )
            {
                return checkBottomCollision ( p ( sp.x + cw, sp.y - cw * 2 - round ), ps, screenBounds );
            }
        }
        return null;
    }

    /**
     * Checks whether WebPopOver will collide with top screen border and modifies location accordingly.
     *
     * @param location     approximate WebPopOver location
     * @param screenBounds screen bounds
     * @return either modified or unmodified WebPopOver location
     */
    protected Point checkTopCollision ( final Point location, final Rectangle screenBounds )
    {
        if ( location.y < screenBounds.y )
        {
            location.y = screenBounds.y;
        }
        return location;
    }

    /**
     * Checks whether WebPopOver will collide with bottom screen border and modifies location accordingly.
     *
     * @param location     approximate WebPopOver location
     * @param ps           WebPopOver size without shade widths
     * @param screenBounds screen bounds
     * @return either modified or unmodified WebPopOver location
     */
    protected Point checkBottomCollision ( final Point location, final Dimension ps, final Rectangle screenBounds )
    {
        if ( location.y + ps.height > screenBounds.y + screenBounds.height )
        {
            location.y = screenBounds.y + screenBounds.height - ps.height;
        }
        return location;
    }

    /**
     * Checks whether WebPopOver will collide with left screen border and modifies location accordingly.
     *
     * @param location     approximate WebPopOver location
     * @param screenBounds screen bounds
     * @return either modified or unmodified WebPopOver location
     */
    protected Point checkLeftCollision ( final Point location, final Rectangle screenBounds )
    {
        if ( location.x < screenBounds.x )
        {
            location.x = screenBounds.x;
        }
        return location;
    }

    /**
     * Checks whether WebPopOver will collide with right screen border and modifies location accordingly.
     *
     * @param location     approximate WebPopOver location
     * @param ps           WebPopOver size without shade widths
     * @param screenBounds screen bounds
     * @return either modified or unmodified WebPopOver location
     */
    protected Point checkRightCollision ( final Point location, final Dimension ps, final Rectangle screenBounds )
    {
        if ( location.x + ps.width > screenBounds.x + screenBounds.width )
        {
            location.x = screenBounds.x + screenBounds.width - ps.width;
        }
        return location;
    }

    /**
     * Returns actual direction depending on preferred WebPopOver direction, its sizes and source point.
     *
     * @param ib           invoker component bounds on screen
     * @param ltr          whether LTR orientation is active or not
     * @param cw           corner with
     * @param ps           WebPopOver size without shade widths
     * @param screenBounds screen bounds
     * @return actual WebPopOver direction
     */
    protected PopOverDirection getActualDirection ( final Rectangle ib, final boolean ltr, final int cw, final Dimension ps,
                                                    final Rectangle screenBounds )
    {
        for ( final PopOverDirection checkedDirection : preferredDirection.getPriority () )
        {
            final Point sp = getActualSourcePoint ( ib, ltr, checkedDirection );
            if ( checkedDirection == PopOverDirection.up )
            {
                if ( sp.y - cw - ps.height > screenBounds.y )
                {
                    return checkedDirection;
                }
            }
            else if ( checkedDirection == PopOverDirection.down )
            {
                if ( sp.y + cw + ps.height < screenBounds.y + screenBounds.height )
                {
                    return checkedDirection;
                }
            }
            else if ( checkedDirection == ( ltr ? PopOverDirection.left : PopOverDirection.right ) )
            {
                if ( sp.x - cw - ps.width > screenBounds.x )
                {
                    return checkedDirection;
                }
            }
            else if ( checkedDirection == ( ltr ? PopOverDirection.right : PopOverDirection.left ) )
            {
                if ( sp.x + cw + ps.width < screenBounds.x + screenBounds.width )
                {
                    return checkedDirection;
                }
            }
        }
        return preferredDirection;
    }

    /**
     * Returns actual source point depending on WebPopOver direction and invoker component location on screen.
     *
     * @param ib        invoker component bounds on screen
     * @param ltr       whether LTR orientation is active or not
     * @param direction WebPopOver direction  @return actual source point
     * @return actual source point depending on WebPopOver direction and invoker component location on screen
     */
    protected Point getActualSourcePoint ( final Rectangle ib, final boolean ltr, final PopOverDirection direction )
    {
        if ( popOverSourcePoint == PopOverSourcePoint.componentCenter )
        {
            return p ( ib.x + ib.width / 2, ib.y + ib.height / 2 );
        }
        else
        {
            if ( direction == PopOverDirection.up )
            {
                return p ( ib.x + ib.width / 2, ib.y );
            }
            else if ( direction == PopOverDirection.down )
            {
                return p ( ib.x + ib.width / 2, ib.y + ib.height );
            }
            else if ( direction == ( ltr ? PopOverDirection.left : PopOverDirection.right ) )
            {
                return p ( ib.x, ib.y + ib.height / 2 );
            }
            else if ( direction == ( ltr ? PopOverDirection.right : PopOverDirection.left ) )
            {
                return p ( ib.x + ib.width, ib.y + ib.height / 2 );
            }
        }
        return null;
    }

    /**
     * Returns popover instance for the specified root pane.
     *
     * @param c root pane
     * @return popover instance for the specified root pane
     */
    protected final WebPopOver getPopOver ( final E c )
    {
        return ( WebPopOver ) c.getClientProperty ( WebPopOver.POPOVER_INSTANCE );
    }
}