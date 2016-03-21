package com.alee.laf.rootpane;

import com.alee.painter.decoration.AbstractContainerPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowStateListener;
import java.util.List;

/**
 * Basic painter for JRootPane component.
 * It is used as WebRootPaneUI default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 * @author Mikle Garin
 */

public class RootPanePainter<E extends JRootPane, U extends WebRootPaneUI, D extends IDecoration<E, D>>
        extends AbstractContainerPainter<E, U, D> implements IRootPanePainter<E, U>
{
    /**
     * Listeners.
     */
    protected WindowFocusListener windowFocusListener;
    protected WindowStateListener windowStateListener;

    /**
     * Runtime variables.
     */
    protected boolean maximized = false;

    @Override
    public void install ( final E c, final U ui )
    {
        super.install ( c, ui );

        // Installing window-related settings
        final Window window = SwingUtils.getWindowAncestor ( c );
        if ( window != null )
        {
            // Enabling window decorations
            if ( isDecorated () )
            {
                enableWindowDecoration ( c, window );
            }
            else
            {
                disableWindowDecoration ( c, window );
            }

            // Window state change listener
            if ( window instanceof Frame )
            {
                windowStateListener = new WindowStateListener ()
                {
                    @Override
                    public void windowStateChanged ( final WindowEvent e )
                    {
                        if ( isDecorated () )
                        {
                            updateDecorationState ();
                        }
                    }
                };
                window.addWindowStateListener ( windowStateListener );
            }
        }
    }

    @Override
    public void uninstall ( final E c, final U ui )
    {
        // Uninstalling window-related settings
        final Window window = SwingUtils.getWindowAncestor ( c );
        if ( window != null )
        {
            // Disabling window decorations
            if ( isDecorated () )
            {
                disableWindowDecoration ( c, window );
            }

            // Removing listeners
            if ( windowStateListener != null )
            {
                window.removeWindowStateListener ( windowStateListener );
                windowStateListener = null;
            }
        }

        super.uninstall ( c, ui );
    }

    @Override
    protected void installFocusListener ()
    {
        final Window window = SwingUtils.getWindowAncestor ( component );
        if ( window != null && usesState ( DecorationState.focused ) )
        {
            windowFocusListener = new WindowFocusListener ()
            {
                @Override
                public void windowGainedFocus ( final WindowEvent e )
                {
                    // Updating focus state
                    RootPanePainter.this.focused = true;

                    // Updating decoration
                    if ( isDecorated () )
                    {
                        updateDecorationState ();
                    }
                }

                @Override
                public void windowLostFocus ( final WindowEvent e )
                {
                    // Updating decoration
                    RootPanePainter.this.focused = false;

                    // Updating decoration
                    if ( isDecorated () )
                    {
                        updateDecorationState ();
                    }
                }
            };
            window.addWindowFocusListener ( windowFocusListener );
        }
    }

    @Override
    protected void uninstallFocusListener ()
    {
        final Window window = SwingUtils.getWindowAncestor ( component );
        if ( window != null && windowFocusListener != null )
        {
            window.removeWindowFocusListener ( windowFocusListener );
            windowFocusListener = null;
        }
    }

    @Override
    protected List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();
        if ( ui.isIconified () )
        {
            states.add ( DecorationState.iconified );
        }
        if ( ui.isMaximized () )
        {
            states.add ( DecorationState.maximized );
        }
        if ( SwingUtils.isFullScreen ( component ) )
        {
            states.add ( DecorationState.fullscreen );
        }
        return states;
    }

    /**
     * Enables root pane window decoration.
     *
     * @param c      root pane
     * @param window window to enable decoration for
     */
    protected void enableWindowDecoration ( final E c, final Window window )
    {
        if ( !window.isDisplayable () )
        {
            if ( window instanceof Frame )
            {
                ( ( Frame ) window ).setUndecorated ( true );
                c.setWindowDecorationStyle ( JRootPane.FRAME );
            }
            else if ( window instanceof Dialog )
            {
                ( ( Dialog ) window ).setUndecorated ( true );
                c.setWindowDecorationStyle ( JRootPane.PLAIN_DIALOG );
            }
        }
    }

    /**
     * Disables root pane window decoration.
     *
     * @param c      root pane
     * @param window window to disable decoration for
     */
    protected void disableWindowDecoration ( final E c, final Window window )
    {
        if ( !window.isDisplayable () )
        {
            if ( window instanceof Frame )
            {
                ( ( Frame ) window ).setUndecorated ( false );
                c.setWindowDecorationStyle ( JRootPane.NONE );
            }
            else if ( window instanceof Dialog )
            {
                ( ( Dialog ) window ).setUndecorated ( false );
                c.setWindowDecorationStyle ( JRootPane.NONE );
            }
        }
    }

    /**
     * Returns whether or not root pane window is currently active.
     *
     * @param c root pane
     * @return true if root pane window is currently active, false otherwise
     */
    protected boolean isActive ( final E c )
    {
        return SwingUtils.getWindowAncestor ( c ).isFocused ();
    }
}