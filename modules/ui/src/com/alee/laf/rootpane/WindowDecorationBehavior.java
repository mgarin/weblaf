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

package com.alee.laf.rootpane;

import com.alee.api.data.CompassDirection;
import com.alee.extended.behavior.ComponentMoveBehavior;
import com.alee.laf.window.WebDialog;
import com.alee.managers.animation.easing.Quadratic;
import com.alee.managers.animation.framerate.FixedFrameRate;
import com.alee.managers.animation.transition.AbstractTransition;
import com.alee.managers.animation.transition.TimedTransition;
import com.alee.managers.animation.transition.Transition;
import com.alee.managers.animation.transition.TransitionAdapter;
import com.alee.managers.style.StyleId;
import com.alee.utils.SwingUtils;
import com.alee.utils.SystemUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * This is a special behavior for windows with custom decoration enabled.
 * It provides most common window decoration features like window drag or maximize/restore on title double click.
 *
 * @author Mikle Garin
 */
public class WindowDecorationBehavior extends ComponentMoveBehavior
{
    /**
     * todo 1. Properly adjust window location when dragged in maximized state
     * todo 2. Eased fade out transition for glass dialog
     */

    /**
     * {@link WRootPaneUI} containing title this behavior is attached to.
     */
    protected final WRootPaneUI rootPaneUI;

    /**
     * Custom-styled glass dialog displaying side frame might be attached to.
     */
    protected WebDialog glassDialog;

    /**
     * Window bounds in normal state.
     */
    protected Rectangle normalBounds;

    /**
     * Window stick bounds.
     */
    protected Rectangle stickBounds;

    /**
     * Window stick direction.
     */
    protected CompassDirection stickDirection;

    /**
     * Fade-in transition.
     */
    protected final AbstractTransition<Float> fadeIn;

    /**
     * Constructs new behavior for windows with custom decoration.
     *
     * @param rootPaneUI {@link WRootPaneUI}
     */
    public WindowDecorationBehavior ( final WRootPaneUI rootPaneUI )
    {
        super ( rootPaneUI.getTitleComponent () );
        this.rootPaneUI = rootPaneUI;
        this.fadeIn = new TimedTransition<Float> ( 0f, 1f, new FixedFrameRate ( 30 ), new Quadratic.Out (), 200L );
        this.fadeIn.addListener ( new TransitionAdapter<Float> ()
        {
            @Override
            public void started ( final Transition transition, final Float value )
            {
                // Ensure stick bounds are present
                if ( stickBounds != null )
                {
                    // Applying proper bounds
                    glassDialog.setBounds ( stickBounds );

                    // Updating opacity
                    glassDialog.setWindowOpacity ( value );
                }
            }

            @Override
            public void adjusted ( final Transition transition, final Float value )
            {
                // Ensure stick bounds are present
                if ( stickBounds != null )
                {
                    // Updating opacity
                    glassDialog.setWindowOpacity ( value );
                }
            }
        } );
    }

    @Override
    public void mouseClicked ( final MouseEvent e )
    {
        if ( SwingUtils.isDoubleClick ( e ) && isMaximizable () )
        {
            if ( rootPaneUI.isMaximized () )
            {
                rootPaneUI.restore ();
            }
            else
            {
                rootPaneUI.maximize ();
            }
        }
    }

    @Override
    public void mouseDragged ( final MouseEvent e )
    {
        if ( dragging && rootPaneUI.isMaximized () )
        {
            // todo Adjust to mouse location properly
            // initialPoint = new Point ( initialPoint.x + shadeWidth, initialPoint.y + shadeWidth );
            rootPaneUI.restore ();
        }
        super.mouseDragged ( e );
    }

    @Override
    protected void componentMoveStarted ( final Point mouse, final Point location )
    {
        if ( isStickingAvailable () )
        {
            normalBounds = getWindow ().getBounds ();
        }
    }

    @Override
    protected void componentMoved ( final Point mouse, final Point location )
    {
        if ( isStickingAvailable () )
        {
            // Checking mouse position
            final GraphicsDevice device = SystemUtils.getGraphicsDevice ( mouse );
            final Rectangle screenBounds = SystemUtils.getDeviceBounds ( device, true );
            if ( screenBounds.contains ( mouse ) )
            {
                // Calculating possible stick direction
                final CompassDirection direction;
                final int state;
                if ( mouse.y < screenBounds.y + 10 )
                {
                    direction = CompassDirection.north;
                    state = Frame.MAXIMIZED_BOTH;
                }
                else if ( mouse.x < screenBounds.x + 10 )
                {
                    direction = CompassDirection.west;
                    state = Frame.MAXIMIZED_VERT;
                }
                else if ( mouse.x > screenBounds.x + screenBounds.width - 10 )
                {
                    direction = CompassDirection.east;
                    state = Frame.MAXIMIZED_VERT;
                }
                else
                {
                    direction = null;
                    state = -1;
                }

                // Updating glass dialog visibility
                if ( direction != null && SystemUtils.isFrameStateSupported ( state ) )
                {
                    // Displaying glass dialog
                    displayGlassDialog ( screenBounds, direction );
                }
                else
                {
                    // Disposing glass dialog
                    disposeGlassDialog ();
                }
            }
            else
            {
                // Disposing glass dialog
                disposeGlassDialog ();
            }
        }
    }

    @Override
    protected void componentMoveEnded ( final Point mouse, final Point location )
    {
        if ( isStickingAvailable () )
        {
            // Applying changes
            if ( stickDirection != null )
            {
                switch ( stickDirection )
                {
                    case west:
                    {
                        rootPaneUI.maximizeWest ();
                        break;
                    }
                    case east:
                    {
                        rootPaneUI.maximizeEast ();
                        break;
                    }
                    case north:
                    default:
                    {
                        rootPaneUI.maximize ();
                        break;
                    }
                }
            }
            else
            {
                // Resetting normal bounds as we won't need them
                normalBounds = null;
            }

            // Disposing glass dialog
            disposeGlassDialog ();
        }
    }

    /**
     * Displays glass dialog.
     *
     * @param screen    glass dialog bounds
     * @param direction stick direction
     */
    protected void displayGlassDialog ( final Rectangle screen, final CompassDirection direction )
    {
        // On direction change we have to dispose previous dialog
        if ( direction != stickDirection )
        {
            disposeGlassDialog ();
        }

        // Checking dialog visibility
        if ( !isGlassDialogVisible () )
        {
            // Checking dialog existence and correctness
            final Window window = getWindow ();
            if ( glassDialog == null || window != glassDialog.getOwner () )
            {
                // Disposing previous dialog
                // This would only happen if the root pane window has changed
                if ( glassDialog != null )
                {
                    glassDialog.dispose ();
                    glassDialog = null;
                }

                // Creating new dialog
                glassDialog = new WebDialog ( StyleId.frameGlassDialog.at ( window ), window );
                glassDialog.setAlwaysOnTop ( true );
                glassDialog.pack ();
            }

            // Updating dialog settings
            glassDialog.setBounds ( 0, 0, 100, 100 );
            glassDialog.setWindowOpacity ( 0f );

            // Updating stick direction and bounds
            stickDirection = direction;
            if ( direction == CompassDirection.west )
            {
                stickBounds = new Rectangle ( screen.x, screen.y, screen.width / 2, screen.height );
            }
            else if ( direction == CompassDirection.east )
            {
                stickBounds = new Rectangle ( screen.x + screen.width - screen.width / 2, screen.y, screen.width / 2, screen.height );
            }
            else
            {
                stickBounds = screen;
            }

            // Displaying dialog
            glassDialog.setVisible ( true );

            // Starting display animation
            fadeIn.play ();
        }
    }

    /**
     * Disposes glass dialog.
     */
    protected void disposeGlassDialog ()
    {
        if ( isGlassDialogVisible () )
        {
            // Halt animation
            fadeIn.stop ();

            // Updating stick direction and bounds
            stickDirection = null;
            stickBounds = null;

            // todo Reduce it slowly?
            // Resetting opacity beforehand to avoid issues on dialog reopen
            glassDialog.setWindowOpacity ( 0f );

            // Hiding dialog
            glassDialog.setVisible ( false );
        }
    }

    /**
     * Returns whether or not glass dialog is visible.
     *
     * @return whether or not glass dialog is visible
     */
    protected boolean isGlassDialogVisible ()
    {
        return glassDialog != null && glassDialog.isShowing ();
    }

    /**
     * Returns window this behavior is working with at this moment.
     *
     * @return window this behavior is working with at this moment
     */
    private Window getWindow ()
    {
        return rootPaneUI.getWindow ();
    }

    /**
     * Returns whether or not {@link #rootPaneUI} is used for {@link JFrame} root pane.
     *
     * @return {@code true} if {@link #rootPaneUI} is used for {@link JFrame} root pane, {@code false} otherwise
     */
    protected boolean isFrame ()
    {
        return rootPaneUI.isFrame ();
    }

    /**
     * Returns frame this behavior is working with at this moment.
     *
     * @return frame this behavior is working with at this moment
     */
    protected JFrame getFrame ()
    {
        if ( !isFrame () )
        {
            throw new RuntimeException ( "Incorrect window type requested" );
        }
        return ( JFrame ) getWindow ();
    }

    /**
     * Returns whether or not resize is available for this behavior.
     *
     * @return {@code true} if resize is available for this behavior, {@code false} otherwise
     */
    protected boolean isResizable ()
    {
        return isFrame () && getFrame ().isResizable ();
    }

    /**
     * Returns whether or not maximize is available for this behavior.
     *
     * @return {@code true} if maximize is available for this behavior, {@code false} otherwise
     */
    protected boolean isMaximizable ()
    {
        return isResizable () && rootPaneUI.isDisplayMaximizeButton ();
    }

    /**
     * Returns whether or not sticking to display sides is available for this behavior.
     *
     * @return {@code true} if sticking to display sides is available for this behavior, {@code false} otherwise
     */
    protected boolean isStickingAvailable ()
    {
        return SystemUtils.isWindows () && isMaximizable ();
    }
}