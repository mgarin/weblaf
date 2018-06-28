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

import com.alee.api.jdk.Supplier;
import com.alee.extended.behavior.ComponentMoveBehavior;
import com.alee.extended.behavior.VisibilityBehavior;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.menu.AbstractPopupPainter;
import com.alee.laf.menu.PopupStyle;
import com.alee.laf.rootpane.WRootPaneUI;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.ProprietaryUtils;
import com.alee.utils.SystemUtils;
import com.alee.utils.swing.AncestorAdapter;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Basic painter for {@link WebPopOver} component.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Mikle Garin
 */
@XStreamAlias ( "PopOverPainter" )
public class PopOverPainter<C extends JRootPane, U extends WRootPaneUI> extends AbstractPopupPainter<C, U>
        implements IPopOverPainter<C, U>
{
    /**
     * todo 1. This painter must extend {@link com.alee.laf.rootpane.RootPanePainter}
     * todo 2. All {@link AbstractPopupPainter} stuff should be done by proper decoration elements instead
     */

    /**
     * Listeners.
     */
    protected transient ComponentMoveBehavior moveAdapter;
    protected transient WindowFocusListener focusListener;
    protected transient ComponentAdapter resizeAdapter;
    protected transient VisibilityBehavior windowVisibilityBehavior;

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
    protected void installPropertiesAndListeners ()
    {
        super.installPropertiesAndListeners ();

        // Retrieving popover instance
        final WebPopOver popOver = getPopOver ( component );

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
        moveAdapter = new ComponentMoveBehavior ( component )
        {
            @Override
            protected Rectangle getDragStartBounds ( final MouseEvent e )
            {
                if ( popOver.isMovable () )
                {
                    final int sw = getShadeWidth ();
                    return new Rectangle ( sw, sw, component.getWidth () - sw * 2, component.getHeight () - sw * 2 );
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
                    repaint ();
                    popOver.fireDetached ();
                }

                super.mouseDragged ( e );
            }
        };
        moveAdapter.install ();

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
                    ProprietaryUtils.setWindowShape ( popOver, provideShape ( component, bounds ) );
                }
            };
            popOver.addComponentListener ( resizeAdapter );
        }

        // Installs behavior that informs L&F about window visibility changes
        // todo Remove this behavior as soon as this painter extends RootPanePainter
        windowVisibilityBehavior = new VisibilityBehavior ( component )
        {
            @Override
            public void displayed ()
            {
                if ( popOver != null )
                {
                    /**
                     * Notifying popover listeners.
                     * Note that if this pop-over is modal this event will be fired only after it is closed.
                     * Unfortunately there is no good way to provide this event after dialog is opened but before it is closed in that case.
                     */
                    popOver.fireOpened ();

                    /**
                     * Notifying L&F about opened window.
                     */
                    WebLookAndFeel.fireWindowDisplayed ( popOver );
                }
            }

            @Override
            public void hidden ()
            {
                if ( popOver != null )
                {
                    /**
                     * Notifying popover listeners.
                     */
                    popOver.fireClosed ();

                    /**
                     * Notifying L&F about closed window.
                     */
                    WebLookAndFeel.fireWindowHidden ( popOver );
                }
            }
        };
        windowVisibilityBehavior.install ();
    }

    @Override
    protected void uninstallPropertiesAndListeners ()
    {
        // Uninstalls behavior that informs L&F about window visibility changes
        // todo Remove this behavior as soon as this painter extends RootPanePainter
        windowVisibilityBehavior.uninstall ();
        windowVisibilityBehavior = null;

        // Retrieving popover instance
        final WebPopOver popOver = getPopOver ( component );

        // Custom window shaping
        if ( resizeAdapter != null )
        {
            popOver.removeComponentListener ( resizeAdapter );
            resizeAdapter = null;
        }

        // Popover drag listener
        moveAdapter.uninstall ();
        moveAdapter = null;

        // Window focus listener
        popOver.removeWindowFocusListener ( focusListener );
        focusListener = null;

        super.uninstallPropertiesAndListeners ();
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

    @Override
    public void configure ( final WebPopOver popOver, final Component invoker, final Supplier<Rectangle> boundsSupplier,
                            final PopOverDirection direction, final PopOverAlignment alignment )
    {
        // Translating coordinates into screen coordinates system
        final Supplier<Rectangle> actualBoundsProvider = boundsSupplier == null ? null : new Supplier<Rectangle> ()
        {
            private Rectangle lastBounds = new Rectangle ();

            @Override
            public Rectangle get ()
            {
                // Invoker might be hidden while WebPopOver is still visible
                // This is why we should simply stop updating its position when that happens
                // It is not the best workaround but at least it will keep us safe from exceptions
                if ( invoker.isShowing () )
                {
                    final Rectangle bounds = boundsSupplier.get ();
                    final Point los = CoreSwingUtils.locationOnScreen ( invoker );
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
    protected void updatePopOverLocation ( final WebPopOver popOver, final Component invoker, final Supplier<Rectangle> boundsProvider )
    {
        if ( boundsProvider != null )
        {
            updatePopOverLocation ( popOver, invoker, boundsProvider.get () );
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
        if ( invoker.isShowing () )
        {
            updatePopOverLocation ( popOver, invoker, CoreSwingUtils.getBoundsOnScreen ( invoker ) );
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

        // WebPopOver preferred size without shadow
        final Dimension size = popOver.getSize ();
        final int sw = getShadeWidth ();
        final int round = getRound ();
        final int cw = getCornerWidth ();
        final Dimension ps = new Dimension ( size.width - sw * 2, size.height - sw * 2 );
        final Rectangle screenBounds = SystemUtils.getDeviceBounds ( invoker, false );

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
                                                   final Supplier<Rectangle> boundsProvider )
    {
        // Invoker component window
        final Window invokerWindow = CoreSwingUtils.getWindowAncestor ( invoker );

        // Invoker window follow adapter
        final WindowFollowBehavior windowFollowBehavior = new WindowFollowBehavior ( popOver, invokerWindow )
        {
            @Override
            public boolean isEnabled ()
            {
                return !PopOverPainter.this.attached;
            }
        };
        windowFollowBehavior.install ();

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
                windowFollowBehavior.uninstall ();
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
     * @param ps              WebPopOver size without shadow widths
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
                final Point location = new Point ( sp.x - ps.width / 2, sp.y - cw - ps.height );
                return checkRightCollision ( checkLeftCollision ( location, screenBounds ), ps, screenBounds );
            }
            else if ( preferredAlignment == ( ltr ? PopOverAlignment.leading : PopOverAlignment.trailing ) )
            {
                return checkLeftCollision ( new Point ( sp.x + cw * 2 + round - ps.width, sp.y - cw - ps.height ), screenBounds );
            }
            else if ( preferredAlignment == ( ltr ? PopOverAlignment.trailing : PopOverAlignment.leading ) )
            {
                return checkRightCollision ( new Point ( sp.x - cw * 2 - round, sp.y - cw - ps.height ), ps, screenBounds );
            }
        }
        else if ( actualDirection == PopOverDirection.down )
        {
            if ( preferredAlignment == PopOverAlignment.centered )
            {
                final Point location = new Point ( sp.x - ps.width / 2, sp.y + cw );
                return checkRightCollision ( checkLeftCollision ( location, screenBounds ), ps, screenBounds );
            }
            else if ( preferredAlignment == ( ltr ? PopOverAlignment.leading : PopOverAlignment.trailing ) )
            {
                return checkLeftCollision ( new Point ( sp.x + cw * 2 + round - ps.width, sp.y + cw ), screenBounds );
            }
            else if ( preferredAlignment == ( ltr ? PopOverAlignment.trailing : PopOverAlignment.leading ) )
            {
                return checkRightCollision ( new Point ( sp.x - cw * 2 - round, sp.y + cw ), ps, screenBounds );
            }
        }
        else if ( actualDirection == ( ltr ? PopOverDirection.left : PopOverDirection.right ) )
        {
            if ( preferredAlignment == PopOverAlignment.centered )
            {
                final Point location = new Point ( sp.x - cw - ps.width, sp.y - ps.height / 2 );
                return checkBottomCollision ( checkTopCollision ( location, screenBounds ), ps, screenBounds );
            }
            else if ( preferredAlignment == PopOverAlignment.leading )
            {
                return checkTopCollision ( new Point ( sp.x - cw - ps.width, sp.y + cw * 2 + round - ps.height ), screenBounds );
            }
            else if ( preferredAlignment == PopOverAlignment.trailing )
            {
                return checkBottomCollision ( new Point ( sp.x - cw - ps.width, sp.y - cw * 2 - round ), ps, screenBounds );
            }
        }
        else if ( actualDirection == ( ltr ? PopOverDirection.right : PopOverDirection.left ) )
        {
            if ( preferredAlignment == PopOverAlignment.centered )
            {
                final Point location = new Point ( sp.x + cw, sp.y - ps.height / 2 );
                return checkBottomCollision ( checkTopCollision ( location, screenBounds ), ps, screenBounds );
            }
            else if ( preferredAlignment == PopOverAlignment.leading )
            {
                return checkTopCollision ( new Point ( sp.x + cw, sp.y + cw * 2 + round - ps.height ), screenBounds );
            }
            else if ( preferredAlignment == PopOverAlignment.trailing )
            {
                return checkBottomCollision ( new Point ( sp.x + cw, sp.y - cw * 2 - round ), ps, screenBounds );
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
     * @param ps           WebPopOver size without shadow widths
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
     * @param ps           WebPopOver size without shadow widths
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
     * @param ps           WebPopOver size without shadow widths
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
            return new Point ( ib.x + ib.width / 2, ib.y + ib.height / 2 );
        }
        else
        {
            if ( direction == PopOverDirection.up )
            {
                return new Point ( ib.x + ib.width / 2, ib.y );
            }
            else if ( direction == PopOverDirection.down )
            {
                return new Point ( ib.x + ib.width / 2, ib.y + ib.height );
            }
            else if ( direction == ( ltr ? PopOverDirection.left : PopOverDirection.right ) )
            {
                return new Point ( ib.x, ib.y + ib.height / 2 );
            }
            else if ( direction == ( ltr ? PopOverDirection.right : PopOverDirection.left ) )
            {
                return new Point ( ib.x + ib.width, ib.y + ib.height / 2 );
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
    protected final WebPopOver getPopOver ( final C c )
    {
        return ( WebPopOver ) c.getClientProperty ( WebPopOver.POPOVER_INSTANCE );
    }
}