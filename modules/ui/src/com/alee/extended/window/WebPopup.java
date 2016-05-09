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

import com.alee.global.StyleConstants;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.focus.FocusManager;
import com.alee.managers.focus.GlobalFocusListener;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.WindowUtils;
import com.alee.utils.swing.PopupListener;
import com.alee.utils.swing.WebTimer;
import com.alee.utils.swing.WindowFollowBehavior;
import com.alee.utils.swing.WindowMethods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom extension that makes use of Swing heavy-weight popup.
 * It also provides same basic methods to manipulate popup window and its settings.
 *
 * @author Mikle Garin
 */

public class WebPopup extends WebPanel implements WindowMethods<JWindow>
{
    /**
     * Popup listeners.
     */
    protected List<PopupListener> listeners = new ArrayList<PopupListener> ( 2 );

    /**
     * Whether should close popup on any action outside of this popup or not.
     */
    protected boolean closeOnOuterAction = true;

    /**
     * Whether popup window should follow invoker's window or not.
     */
    protected boolean followInvoker = false;

    /**
     * Whether popup window should always be on top of other windows or not.
     */
    protected boolean alwaysOnTop = false;

    /**
     * Popup window opacity.
     */
    protected float opacity = 1f;

    /**
     * Whether should animate popup display/hide or not.
     */
    protected boolean animate = true;

    /**
     * Single animation step progress.
     * Making this value bigger will speedup the animation, reduce required resources but will also make it less soft.
     */
    protected float stepProgress = 0.05f;

    /**
     * Popup window display progress.
     * When popup is fully displayed = 1f.
     * When popup is fully hidden = 0f;
     */
    protected float displayProgress = 0f;

    /**
     * Show/hide actions synchronization object.
     */
    protected final Object sync = new Object ();

    /**
     * Whether popup is being displayed or not.
     */
    protected boolean displaying = false;

    /**
     * Whether popup is being hidden or not.
     */
    protected boolean hiding = false;

    /**
     * Show action animation timer.
     */
    protected WebTimer showAnimator = null;

    /**
     * Hide action animation timer.
     */
    protected WebTimer hideAnimator = null;

    /**
     * Actions to perform on full display.
     */
    protected final List<Runnable> onFullDisplay = new ArrayList<Runnable> ();

    /**
     * Actions to perform on full hide.
     */
    protected final List<Runnable> onFullHide = new ArrayList<Runnable> ();

    /**
     * Listeners synchronization object.
     */
    protected final Object lsync = new Object ();

    /**
     * Window in which popup content is currently displayed.
     */
    protected WebPopupWindow window;

    /**
     * Invoker component.
     */
    protected Component invoker;

    /**
     * Invoker component window.
     */
    protected Window invokerWindow;

    /**
     * Custom global mouse listener that closes popup.
     */
    protected AWTEventListener mouseListener;

    /**
     * Custom global focus listener that closes popup.
     */
    protected GlobalFocusListener focusListener;

    /**
     * Invoker follow adapter.
     */
    protected WindowFollowBehavior followAdapter;

    public WebPopup ()
    {
        super ( StyleId.popup );
    }

    public WebPopup ( final Component component )
    {
        super ( StyleId.popup, component );
    }

    public WebPopup ( final LayoutManager layout, final Component... components )
    {
        super ( StyleId.popup, layout, components );
    }

    public WebPopup ( final StyleId styleId )
    {
        super ( styleId );
    }

    public WebPopup ( final StyleId styleId, final Component component )
    {
        super ( styleId, component );
    }

    public WebPopup ( final StyleId styleId, final LayoutManager layout, final Component... components )
    {
        super ( styleId, layout, components );
    }

    public boolean isCloseOnOuterAction ()
    {
        return closeOnOuterAction;
    }

    public void setCloseOnOuterAction ( final boolean closeOnOuterAction )
    {
        this.closeOnOuterAction = closeOnOuterAction;
    }

    public JWindow getWindow ()
    {
        return window;
    }

    public Component getInvoker ()
    {
        return invoker;
    }

    public Window getInvokerWindow ()
    {
        return invokerWindow;
    }

    public float getDisplayProgress ()
    {
        return displayProgress;
    }

    public boolean isDisplaying ()
    {
        return displaying;
    }

    public boolean isHiding ()
    {
        return hiding;
    }

    @Override
    public void setOpaque ( final boolean isOpaque )
    {
        super.setOpaque ( isOpaque );
        setWindowOpaque ( isOpaque );
    }

    @Override
    public JWindow setWindowOpaque ( final boolean opaque )
    {
        return updateOpaque ();
    }

    protected JWindow updateOpaque ()
    {
        if ( window != null )
        {
            WindowUtils.setWindowOpaque ( window, isOpaque () );
        }
        return window;
    }

    @Override
    public boolean isWindowOpaque ()
    {
        return isOpaque ();
    }

    @Override
    public JWindow setWindowOpacity ( final float opacity )
    {
        this.opacity = opacity;
        return updateOpacity ();
    }

    protected JWindow updateOpacity ()
    {
        if ( window != null )
        {
            WindowUtils.setWindowOpacity ( window, opacity * displayProgress );
        }
        return window;
    }

    @Override
    public float getWindowOpacity ()
    {
        return opacity;
    }

    public boolean isFollowInvoker ()
    {
        return followInvoker;
    }

    public void setFollowInvoker ( final boolean followInvoker )
    {
        this.followInvoker = followInvoker;
        if ( followInvoker )
        {
            if ( window != null && followAdapter == null && invokerWindow != null )
            {
                // Adding follow adapter
                installFollowAdapter ();
            }
        }
        else
        {
            if ( window != null && followAdapter != null && invokerWindow != null )
            {
                // Removing follow adapter
                uninstallFollowAdapter ();
            }
        }
    }

    protected void installFollowAdapter ()
    {
        followAdapter = WindowFollowBehavior.install ( window, invokerWindow );
    }

    protected void uninstallFollowAdapter ()
    {
        WindowFollowBehavior.uninstall ( window, invokerWindow );
        followAdapter = null;
    }

    public boolean isAlwaysOnTop ()
    {
        return alwaysOnTop;
    }

    public void setAlwaysOnTop ( final boolean alwaysOnTop )
    {
        this.alwaysOnTop = alwaysOnTop;
        if ( window != null )
        {
            window.setAlwaysOnTop ( alwaysOnTop );
        }
    }

    public boolean isAnimate ()
    {
        return animate;
    }

    public void setAnimate ( final boolean animate )
    {
        this.animate = animate;
    }

    public float getStepProgress ()
    {
        return stepProgress;
    }

    public void setStepProgress ( final float stepProgress )
    {
        this.stepProgress = stepProgress;
    }

    /**
     * Shows popup window.
     * Depending on settings it might take a while to animate the show action.
     *
     * @param invoker  invoker component
     * @param location popup location relative to invoker
     * @return this popup
     */
    public WebPopup showPopup ( final Component invoker, final Point location )
    {
        return showPopup ( invoker, location.x, location.y );
    }

    /**
     * Shows popup window.
     * Depending on settings it might take a while to animate the show action.
     *
     * @param invoker invoker component
     * @param x       popup X coordinate relative to invoker
     * @param y       popup Y coordinate relative to invoker
     * @return this popup
     */
    public WebPopup showPopup ( final Component invoker, final int x, final int y )
    {
        showPopupImpl ( invoker, x, y );
        return this;
    }

    /**
     * Performs popup show operation.
     * Depending on settings it might take a while to animate the show action.
     *
     * @param invoker invoker component
     * @param x       popup X coordinate relative to invoker
     * @param y       popup Y coordinate relative to invoker
     */
    protected void showPopupImpl ( final Component invoker, final int x, final int y )
    {
        synchronized ( sync )
        {
            // Ignore action if popup is being displayed or already displayed
            if ( displaying || !hiding && window != null )
            {
                return;
            }

            // Stop hiding popup
            if ( hiding )
            {
                if ( hideAnimator != null )
                {
                    hideAnimator.stop ();
                    hideAnimator = null;
                }
                hiding = false;
                completePopupHide ();
            }

            // Set state to displaying
            displaying = true;

            // Saving invoker
            this.invoker = invoker;
            this.invokerWindow = SwingUtils.getWindowAncestor ( invoker );

            // Updating display state
            this.displayProgress = animate ? 0f : 1f;

            // Creating popup
            window = createWindow ();

            // Updating content and location
            window.add ( this );
            if ( invokerWindow != null )
            {
                final Rectangle bos = SwingUtils.getBoundsOnScreen ( invoker );
                window.setLocation ( bos.x + x, bos.y + y );
            }
            else
            {
                window.setLocation ( x, y );
            }
            window.pack ();

            // Updating always on top settin
            window.setAlwaysOnTop ( alwaysOnTop );

            // Modifying opacity if needed
            updateOpaque ();
            updateOpacity ();

            // Informing about popup display
            firePopupWillBeOpened ();

            // Creating menu hide mouse event listener (when mouse pressed outside of the menu)
            mouseListener = new AWTEventListener ()
            {
                @Override
                public void eventDispatched ( final AWTEvent event )
                {
                    if ( closeOnOuterAction )
                    {
                        final MouseEvent e = ( MouseEvent ) event;
                        if ( e.getID () == MouseEvent.MOUSE_PRESSED )
                        {
                            final Component component = e.getComponent ();
                            if ( window != component && !window.isAncestorOf ( component ) )
                            {
                                hidePopup ();
                            }
                        }
                    }
                }
            };
            Toolkit.getDefaultToolkit ().addAWTEventListener ( mouseListener, AWTEvent.MOUSE_EVENT_MASK );

            // Creating menu hide focus event listener (when focus leaves application)
            focusListener = new GlobalFocusListener ()
            {
                @Override
                public void focusChanged ( final Component oldFocus, final Component newFocus )
                {
                    if ( closeOnOuterAction && newFocus == null )
                    {
                        hidePopup ();
                    }
                }
            };
            FocusManager.registerGlobalFocusListener ( focusListener );

            // Displaying popup
            window.setVisible ( true );

            // Trasferring focus into content
            transferFocus ();

            // Animating popup display
            if ( animate )
            {
                showAnimator = WebTimer.repeat ( StyleConstants.fps48, 0L, new ActionListener ()
                {
                    @Override
                    public void actionPerformed ( final ActionEvent e )
                    {
                        synchronized ( sync )
                        {
                            if ( displayProgress < 1f )
                            {
                                displayProgress = Math.min ( displayProgress + stepProgress, 1f );
                                setWindowOpacity ( displayProgress );
                            }
                            else
                            {
                                showAnimator.stop ();
                                showAnimator = null;
                                displaying = false;
                                fullyDisplayed ();
                            }
                            showAnimationStepPerformed ();
                        }
                    }
                } );
            }

            // Adding follow behavior if needed
            if ( followInvoker && invokerWindow != null )
            {
                installFollowAdapter ();
            }

            if ( !animate )
            {
                displaying = false;
            }

            firePopupOpened ();

            if ( !animate )
            {
                fullyDisplayed ();
            }
        }
    }

    /**
     * Returns new window for popup content.
     *
     * @return new window for popup content
     */
    protected WebPopupWindow createWindow ()
    {
        return new WebPopupWindow ( invokerWindow );
    }

    /**
     * Hides popup window.
     * Depending on settings it might take a while to animate the hide action.
     *
     * @return this popup
     */
    public WebPopup hidePopup ()
    {
        hidePopupImpl ();
        return this;
    }

    /**
     * Performs popup hide operation.
     * Depending on settings it might take a while to animate the hide action.
     */
    protected void hidePopupImpl ()
    {
        synchronized ( sync )
        {
            // Ignore action if popup is being hidden or already hidden
            if ( hiding || window == null || window != null && !window.isShowing () )
            {
                return;
            }

            // Set state to displaying
            hiding = true;

            // Stop hiding popup
            if ( displaying )
            {
                if ( showAnimator != null )
                {
                    showAnimator.stop ();
                }
                displaying = false;
            }

            // Updating display state
            this.displayProgress = animate ? 1f : 0f;

            if ( animate )
            {
                hideAnimator = WebTimer.repeat ( StyleConstants.fps48, 0L, new ActionListener ()
                {
                    @Override
                    public void actionPerformed ( final ActionEvent e )
                    {
                        synchronized ( sync )
                        {
                            if ( displayProgress > 0f )
                            {
                                displayProgress = Math.max ( displayProgress - stepProgress, 0f );
                                setWindowOpacity ( displayProgress );
                            }
                            else
                            {
                                completePopupHide ();
                                hideAnimator.stop ();
                                hideAnimator = null;
                                hiding = false;
                            }
                            hideAnimationStepPerformed ();
                        }
                    }
                } );
            }
            else
            {
                completePopupHide ();
            }
        }
    }

    /**
     * Completes popup hide operation.
     */
    protected void completePopupHide ()
    {
        firePopupWillBeClosed ();

        // Removing follow adapter
        if ( followInvoker && invokerWindow != null )
        {
            uninstallFollowAdapter ();
        }

        // Removing popup hide event listeners
        Toolkit.getDefaultToolkit ().removeAWTEventListener ( mouseListener );
        mouseListener = null;
        FocusManager.unregisterGlobalFocusListener ( focusListener );
        focusListener = null;

        // Removing follow adapter
        invokerWindow = null;
        invoker = null;

        // Disposing of popup window
        window.dispose ();
        window = null;

        firePopupClosed ();
        fullyHidden ();
    }

    /**
     * Called with each show animation step performed.
     * This method is a placeholder for overriding classes.
     */
    protected void showAnimationStepPerformed ()
    {
        // Do nothing by default
    }

    /**
     * Called with each hide animation step performed.
     * This method is a placeholder for overriding classes.
     */
    protected void hideAnimationStepPerformed ()
    {
        // Do nothing by default
    }

    /**
     * Performs provided action when menu is fully displayed.
     * Might be useful to display sub-menus or perform some other actions.
     * Be aware that this action will be performed only once and then removed from the actions list.
     *
     * @param action action to perform
     */
    public void onFullDisplay ( final Runnable action )
    {
        synchronized ( lsync )
        {
            if ( !isShowing () || isDisplaying () )
            {
                onFullDisplay.add ( action );
            }
            else if ( isShowing () && !isHiding () )
            {
                action.run ();
            }
        }
    }

    /**
     * Performs actions waiting for menu display animation finish.
     */
    public void fullyDisplayed ()
    {
        synchronized ( lsync )
        {
            for ( final Runnable runnable : onFullDisplay )
            {
                runnable.run ();
            }
            onFullDisplay.clear ();
        }
    }

    /**
     * Performs provided action when menu is fully hidden.
     * Be aware that this action will be performed only once and then removed from the actions list.
     *
     * @param action action to perform
     */
    public void onFullHide ( final Runnable action )
    {
        synchronized ( lsync )
        {
            if ( isShowing () )
            {
                onFullHide.add ( action );
            }
            else
            {
                action.run ();
            }
        }
    }

    /**
     * Performs actions waiting for menu hide animation finish.
     */
    public void fullyHidden ()
    {
        synchronized ( lsync )
        {
            for ( final Runnable runnable : onFullHide )
            {
                runnable.run ();
            }
            onFullHide.clear ();
        }
    }

    /**
     * Adds popup listener.
     *
     * @param listener popup listener
     */
    public void addPopupListener ( final PopupListener listener )
    {
        synchronized ( lsync )
        {
            listeners.add ( listener );
        }
    }

    /**
     * Removes popup listener.
     *
     * @param listener popup listener
     */
    public void removePopupListener ( final PopupListener listener )
    {
        synchronized ( lsync )
        {
            listeners.remove ( listener );
        }
    }

    /**
     * Notifies listeners that popup will now be opened.
     */
    public void firePopupWillBeOpened ()
    {
        synchronized ( lsync )
        {
            for ( final PopupListener listener : CollectionUtils.copy ( listeners ) )
            {
                listener.popupWillBeOpened ();
            }
        }
    }

    /**
     * Notifies listeners that popup was opened.
     */
    public void firePopupOpened ()
    {
        synchronized ( lsync )
        {
            for ( final PopupListener listener : CollectionUtils.copy ( listeners ) )
            {
                listener.popupOpened ();
            }
        }
    }

    /**
     * Notifies listeners that popup will now be closed.
     */
    public void firePopupWillBeClosed ()
    {
        synchronized ( lsync )
        {
            for ( final PopupListener listener : CollectionUtils.copy ( listeners ) )
            {
                listener.popupWillBeClosed ();
            }
        }
    }

    /**
     * Notifies listeners that popup was closed.
     */
    public void firePopupClosed ()
    {
        synchronized ( lsync )
        {
            for ( final PopupListener listener : CollectionUtils.copy ( listeners ) )
            {
                listener.popupClosed ();
            }
        }
    }

    /**
     * Packs popup window to fit content preferred size.
     *
     * @return popup window
     */
    public JWindow pack ()
    {
        if ( window != null )
        {
            window.pack ();
        }
        return window;
    }

    @Override
    public JWindow center ()
    {
        return WindowUtils.center ( window );
    }

    @Override
    public JWindow center ( final Component relativeTo )
    {
        return WindowUtils.center ( window, relativeTo );
    }

    @Override
    public JWindow center ( final int width, final int height )
    {
        return WindowUtils.center ( window, width, height );
    }

    @Override
    public JWindow center ( final Component relativeTo, final int width, final int height )
    {
        return WindowUtils.center ( window, relativeTo, width, height );
    }

    @Override
    public JWindow packToWidth ( final int width )
    {
        return WindowUtils.packToWidth ( window, width );
    }

    @Override
    public JWindow packToHeight ( final int height )
    {
        return WindowUtils.packToHeight ( window, height );
    }

    @Override
    public JWindow packAndCenter ()
    {
        return WindowUtils.packAndCenter ( window );
    }

    @Override
    public JWindow packAndCenter ( final boolean animate )
    {
        return WindowUtils.packAndCenter ( window, animate );
    }
}