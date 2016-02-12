package com.alee.managers.style.skin.web;

import com.alee.laf.rootpane.IRootPanePainter;
import com.alee.laf.rootpane.WebRootPaneUI;
import com.alee.managers.style.skin.web.data.DecorationState;
import com.alee.managers.style.skin.web.data.decoration.IDecoration;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowStateListener;
import java.util.List;

/**
 * @author Alexandr Zernov
 * @author Mikle Garin
 */

public class WebRootPanePainter<E extends JRootPane, U extends WebRootPaneUI, D extends IDecoration<E, D>>
        extends AbstractDecorationPainter<E, U, D> implements IRootPanePainter<E, U>
{
    /**
     * Style settings.
     */
    protected Boolean decorated;

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
                    WebRootPanePainter.this.focused = true;

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
                    WebRootPanePainter.this.focused = false;

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
    public boolean isDecorated ()
    {
        return decorated != null && decorated;
    }

    @Override
    protected List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();
        if ( ui.isMaximized () )
        {
            states.add ( DecorationState.maximized );
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

    @Override
    public Boolean isOpaque ()
    {
        return !isDecorated ();
    }

    @Override
    public Insets getBorders ()
    {
        return isDecorated () ? super.getBorders () : null;
    }

    @Override
    protected boolean isDecorationPaintAllowed ( final D decoration )
    {
        return isDecorated () && super.isDecorationPaintAllowed ( decoration );
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