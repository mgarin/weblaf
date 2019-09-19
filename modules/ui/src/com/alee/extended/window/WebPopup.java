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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.data.CompassDirection;
import com.alee.api.jdk.Function;
import com.alee.extended.WebContainer;
import com.alee.extended.behavior.ComponentResizeBehavior;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.window.WindowMethods;
import com.alee.laf.window.WindowMethodsImpl;
import com.alee.managers.focus.FocusManager;
import com.alee.managers.focus.GlobalFocusListener;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleManager;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.ProprietaryUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.EmptyComponent;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.ref.WeakReference;

/**
 * Custom extension that makes use of Swing heavy-weight popup.
 * It also provides various methods to manipulate underlying popup window.
 *
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application LaF as this component will use Web-UI in any case.
 *
 * @param <T> popup type
 * @author Mikle Garin
 * @see WebContainer
 * @see WebPopupUI
 * @see PopupPainter
 */
public class WebPopup<T extends WebPopup<T>> extends WebContainer<T, WPopupUI>
        implements Popup, PopupMethods, WindowMethods<WebPopupWindow>
{
    /**
     * todo 1. Move all action implementations into UI
     * todo 2. Provide property change fire calls on specific settings changes
     * todo 3. [JDK7+] Provide Window setting `setAutoRequestFocus`
     */

    /**
     * Whether or not should close popup on any action outside of this popup.
     */
    protected boolean closeOnOuterAction = true;

    /**
     * Whether should close popup on focus loss or not.
     */
    protected boolean closeOnFocusLoss = false;

    /**
     * Whether or not popup window should be draggable.
     */
    protected boolean draggable = false;

    /**
     * Whether or not this popup should be resizable.
     */
    protected boolean resizable = false;

    /**
     * Whether or not popup should follow invoker's window.
     */
    protected boolean followInvoker = false;

    /**
     * Whether or not popup window should always be on top of other windows.
     */
    protected boolean alwaysOnTop = false;

    /**
     * Popup window opacity.
     */
    protected float opacity = 1f;

    /**
     * Whether should animate popup display/hide or not.
     */
    protected boolean animate = ProprietaryUtils.isWindowTransparencyAllowed ();

    /**
     * Single animation step size, should be larger than {@code 0f} and lesser than {@code 1f}.
     * Making this value bigger will speedup the animation, reduce required resources but will also make it less soft.
     */
    protected float fadeStepSize = 0.1f;

    /**
     * Popup window display progress.
     * When popup is fully displayed = 1f.
     * When popup is fully hidden = 0f;
     */
    protected float visibilityProgress = 0f;

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
     * Default focus {@link Component}.
     */
    protected transient WeakReference<Component> defaultFocus;

    /**
     * Custom global mouse listener that closes popup.
     */
    protected transient AWTEventListener mouseListener;

    /**
     * Custom global focus listener that closes popup.
     */
    protected transient GlobalFocusListener focusListener;

    /**
     * {@link ComponentResizeBehavior}.
     */
    protected transient ComponentResizeBehavior resizeBehavior;

    /**
     * Invoker follow adapter.
     */
    protected transient WindowFollowBehavior followBehavior;

    /**
     * Whether or not popup is undergoing opacity change.
     * This is a workaround for older JDKs changing {@link Window} contents opacity along with {@link Window} opacity.
     */
    protected transient boolean changingOpacity = false;

    /**
     * Constructs new popup.
     */
    public WebPopup ()
    {
        this ( StyleId.auto );
    }

    /**
     * Constructs new popup.
     *
     * @param component popup content
     */
    public WebPopup ( final Component component )
    {
        this ( StyleId.auto, component );
    }

    /**
     * Constructs new popup.
     *
     * @param layout     popup layout
     * @param components popup contents
     */
    public WebPopup ( final LayoutManager layout, final Component... components )
    {
        this ( StyleId.auto, layout, components );
    }

    /**
     * Constructs new popup.
     *
     * @param styleId style ID
     */
    public WebPopup ( final StyleId styleId )
    {
        this ( styleId, null );
    }

    /**
     * Constructs new popup.
     *
     * @param styleId   style ID
     * @param component popup content
     */
    public WebPopup ( final StyleId styleId, final Component component )
    {
        this ( styleId, new BorderLayout (), component );
    }

    /**
     * Constructs new popup.
     *
     * @param styleId    style ID
     * @param layout     popup layout
     * @param components popup contents
     */
    public WebPopup ( final StyleId styleId, final LayoutManager layout, final Component... components )
    {
        super ();
        setFocusCycleRoot ( true );
        setLayout ( layout );
        updateUI ();
        setStyleId ( styleId );
        add ( components );
    }

    @NotNull
    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.popup;
    }

    /**
     * Returns whether or not popup should auto-close on any action performed in application outside of this popup.
     *
     * @return true if popup should auto-close on any action performed in application outside of this popup, false otherwise
     */
    public boolean isCloseOnOuterAction ()
    {
        return closeOnOuterAction;
    }

    /**
     * Sets whether or not popup should auto-close on any action performed in application outside of this popup.
     *
     * @param close whether or not popup should auto-close on any action performed in application outside of this popup
     */
    public void setCloseOnOuterAction ( final boolean close )
    {
        this.closeOnOuterAction = close;
    }

    /**
     * Returns whether should close popup on focus loss or not.
     *
     * @return true if should close popup on focus loss, false otherwise
     */
    public boolean isCloseOnFocusLoss ()
    {
        return closeOnFocusLoss;
    }

    /**
     * Sets whether should close popup on focus loss or not.
     *
     * @param closeOnFocusLoss whether should close popup on focus loss or not
     */
    public void setCloseOnFocusLoss ( final boolean closeOnFocusLoss )
    {
        this.closeOnFocusLoss = closeOnFocusLoss;
        if ( window != null )
        {
            window.setCloseOnFocusLoss ( closeOnFocusLoss );
        }
    }

    /**
     * Returns window used for this popup.
     * This is usually {@code null} unless popup is displayed.
     *
     * @return window used for this popup
     */
    public JWindow getWindow ()
    {
        return window;
    }

    /**
     * Returns invoker component.
     *
     * @return invoker component
     */
    public Component getInvoker ()
    {
        return invoker;
    }

    /**
     * Returns invoker window.
     *
     * @return invoker window
     */
    public Window getInvokerWindow ()
    {
        return invokerWindow;
    }

    /**
     * Returns popup visibility progress state.
     *
     * @return popup visibility progress state
     */
    public float getVisibilityProgress ()
    {
        return visibilityProgress;
    }

    /**
     * Returns whether or not popup is in the middle of display transition.
     * That could only be {@code true} when {@link #animate} is set to {@code true}.
     *
     * @return true if popup is in the middle of display transition, false otherwise
     */
    public boolean isDisplaying ()
    {
        return displaying;
    }

    /**
     * Returns whether or not popup is in the middle of hide transition.
     * That could only be {@code true} when {@link #animate} is set to {@code true}.
     *
     * @return true if popup is in the middle of hide transition, false otherwise
     */
    public boolean isHiding ()
    {
        return hiding;
    }

    /**
     * Returns whether or not popup window should be draggable.
     *
     * @return {@code true} if popup window should be draggable, {@code false} otherwise
     */
    public boolean isDraggable ()
    {
        return draggable;
    }

    /**
     * Sets whether or not popup window should be draggable.
     *
     * @param draggable whether or not popup window should be draggable
     */
    public void setDraggable ( final boolean draggable )
    {
        if ( this.draggable != draggable )
        {
            this.draggable = draggable;
            updateResizeBehavior ();
        }
    }

    /**
     * Returns whether or not this popup is resizable.
     *
     * @return true if this popup is resizable, false otherwise
     */
    public boolean isResizable ()
    {
        return resizable;
    }

    /**
     * Sets whether or not this popup should be resizable.
     *
     * @param resizable whether or not this popup should be resizable
     */
    public void setResizable ( final boolean resizable )
    {
        if ( this.resizable != resizable )
        {
            this.resizable = resizable;
            updateResizeBehavior ();
        }
    }

    /**
     * Updates {@link ComponentResizeBehavior}.
     */
    protected void updateResizeBehavior ()
    {
        if ( isDraggable () || isResizable () )
        {
            if ( resizeBehavior == null )
            {
                installResizeBehavior ();
            }
        }
        else
        {
            uninstallResizeBehavior ();
        }
    }

    /**
     * Installs {@link ComponentResizeBehavior}.
     */
    protected void installResizeBehavior ()
    {
        resizeBehavior = new ComponentResizeBehavior ( this, new ResizeDirections ( 6 ) );
        resizeBehavior.install ();
    }

    /**
     * Uninstalls {@link ComponentResizeBehavior}.
     */
    protected void uninstallResizeBehavior ()
    {
        resizeBehavior.uninstall ();
        resizeBehavior = null;
    }

    /**
     * Returns whether or not popup should follow invoker's window.
     *
     * @return true if popup should follow invoker's window, false otherwise
     */
    public boolean isFollowInvoker ()
    {
        return followInvoker;
    }

    /**
     * Sets whether or not popup should follow invoker's window.
     *
     * @param followInvoker whether or not popup should follow invoker's window
     */
    public void setFollowInvoker ( final boolean followInvoker )
    {
        if ( this.followInvoker != followInvoker )
        {
            this.followInvoker = followInvoker;
            if ( followInvoker )
            {
                if ( followBehavior == null && window != null && invokerWindow != null )
                {
                    installFollowBehavior ();
                }
            }
            else
            {
                if ( followBehavior != null && window != null && invokerWindow != null )
                {
                    uninstallFollowBehavior ();
                }
            }
        }
    }

    /**
     * Installs {@link WindowFollowBehavior}.
     */
    protected void installFollowBehavior ()
    {
        followBehavior = new WindowFollowBehavior ( window, invokerWindow );
        followBehavior.install ();
    }

    /**
     * Uninstalls {@link WindowFollowBehavior}.
     */
    protected void uninstallFollowBehavior ()
    {
        followBehavior.uninstall ();
        followBehavior = null;
    }

    /**
     * Returns whether or not this popup should always be on top of other windows.
     *
     * @return true if this popup should always be on top of other windows, false otherwise
     */
    public boolean isAlwaysOnTop ()
    {
        return alwaysOnTop;
    }

    /**
     * Sets whether or not this popup should always be on top of other windows.
     *
     * @param alwaysOnTop whether or not this popup should always be on top of other windows
     */
    public void setAlwaysOnTop ( final boolean alwaysOnTop )
    {
        this.alwaysOnTop = alwaysOnTop;
        if ( window != null )
        {
            window.setAlwaysOnTop ( alwaysOnTop );
        }
    }

    /**
     * Returns whether or not popup display and hide transitions should be animated.
     *
     * @return true if popup display and hide transitions should be animated, false otherwise
     */
    public boolean isAnimate ()
    {
        return animate;
    }

    /**
     * Sets whether popup display and hide transitions should be animated.
     *
     * @param animate whether popup display and hide transitions should be animated
     */
    public void setAnimate ( final boolean animate )
    {
        this.animate = animate;
    }

    /**
     * Returns single fade animation step size.
     *
     * @return single fade animation step size
     */
    public float getFadeStepSize ()
    {
        return fadeStepSize;
    }

    /**
     * Sets single fade animation step size.
     *
     * @param fadeStepSize single fade animation step size
     */
    public void setFadeStepSize ( final float fadeStepSize )
    {
        this.fadeStepSize = fadeStepSize;
    }

    /**
     * Returns default focus {@link Component} or {@code null} if none provided.
     *
     * @return default focus {@link Component} or {@code null} if none provided
     */
    public Component getDefaultFocus ()
    {
        return defaultFocus != null ? defaultFocus.get () : null;
    }

    /**
     * Sets default focus {@link Component}.
     * Can be set to {@code null} to enable default focus behavior.
     *
     * @param defaultFocus new default focus {@link Component}
     */
    public void setDefaultFocus ( final Component defaultFocus )
    {
        this.defaultFocus = new WeakReference<Component> ( defaultFocus );
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
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Ignore action if popup is being displayed or already displayed
        if ( !displaying && ( window == null || hiding ) )
        {
            // Stop hiding popup
            if ( window != null )
            {
                if ( hideAnimator != null )
                {
                    hideAnimator.stop ();
                    hideAnimator = null;
                }
                window.dispose ();
                hiding = false;
            }

            // Set state to displaying
            displaying = true;

            // Saving invoker
            this.invoker = invoker;
            this.invokerWindow = CoreSwingUtils.getWindowAncestor ( invoker );

            // Updating display state
            this.visibilityProgress = animate ? 0f : 1f;

            // Creating popup
            window = createWindow ( x, y );

            // Informing about popup display
            firePopupWillBeOpened ();

            // Creating menu hide mouse event listener (when mouse pressed outside of the menu)
            mouseListener = new AWTEventListener ()
            {
                @Override
                public void eventDispatched ( final AWTEvent event )
                {
                    if ( isCloseOnOuterAction () )
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
                public void focusChanged ( @Nullable final Component oldFocus, @Nullable final Component newFocus )
                {
                    if ( isCloseOnOuterAction () && newFocus == null )
                    {
                        hidePopup ();
                    }
                }
            };
            FocusManager.registerGlobalFocusListener ( this, focusListener );

            // Displaying popup
            window.setVisible ( true );

            // Transferring focus
            final Component defaultFocus = getDefaultFocus ();
            if ( defaultFocus != null && WebPopup.this.isAncestorOf ( defaultFocus ) && defaultFocus.isShowing () )
            {
                // Transferring focus into specified component located inside of the popup
                defaultFocus.requestFocus ();
            }
            else
            {
                // Transferring focus into first focusable component
                final Component nextFocus = SwingUtils.findFocusableComponent ( this );
                if ( nextFocus != null && nextFocus.isShowing () )
                {
                    nextFocus.requestFocus ();
                }
                else
                {
                    window.requestFocus ();
                }
            }

            // Animating popup display
            if ( animate )
            {
                showAnimator = WebTimer.repeat ( SwingUtils.frameRateDelay ( 48 ), 0L, true, new ActionListener ()
                {
                    @Override
                    public void actionPerformed ( final ActionEvent e )
                    {
                        if ( visibilityProgress < 1f )
                        {
                            visibilityProgress = Math.min ( visibilityProgress + fadeStepSize, 1f );
                            setWindowOpacity ( visibilityProgress );
                        }
                        else
                        {
                            showAnimator.stop ();
                            showAnimator = null;
                            displaying = false;
                        }
                        showAnimationStepPerformed ();
                    }
                } );
            }

            // Adding follow behavior if needed
            if ( followInvoker && invokerWindow != null )
            {
                installFollowBehavior ();
            }

            if ( !animate )
            {
                displaying = false;
            }

            firePopupOpened ();
        }
    }

    /**
     * Returns new window for popup content.
     *
     * @param x popup X coordinate relative to invoker
     * @param y popup Y coordinate relative to invoker
     * @return new window for popup content
     */
    protected WebPopupWindow createWindow ( final int x, final int y )
    {
        // Creating custom popup window
        final WebPopupWindow window = new WebPopupWindow ( invokerWindow );

        // Placing popup into window
        window.setContentPane ( this );

        // Udating window location
        if ( invokerWindow != null )
        {
            final Rectangle bos = CoreSwingUtils.getBoundsOnScreen ( invoker, false );
            window.setLocation ( bos.x + x, bos.y + y );
        }
        else
        {
            window.setLocation ( x, y );
        }

        // Listener
        window.addWindowListener ( new WindowAdapter ()
        {
            @Override
            public void windowClosed ( @NotNull final WindowEvent e )
            {
                completePopupHide ();
            }
        } );

        // Packing window size to preferred
        window.pack ();

        // Updating window settings
        window.setCloseOnFocusLoss ( isCloseOnFocusLoss () );
        window.setAlwaysOnTop ( isAlwaysOnTop () );

        // Modifying opacity if needed
        updateOpaque ( window );
        updateOpacity ( window );

        return window;
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
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Ignore action if popup is being hidden or already hidden
        if ( !hiding && window != null && window.isShowing () )
        {
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
            this.visibilityProgress = animate ? 1f : 0f;

            if ( animate )
            {
                hideAnimator = WebTimer.repeat ( SwingUtils.frameRateDelay ( 48 ), 0L, true, new ActionListener ()
                {
                    @Override
                    public void actionPerformed ( final ActionEvent e )
                    {
                        if ( visibilityProgress > 0f )
                        {
                            visibilityProgress = Math.max ( visibilityProgress - fadeStepSize, 0f );
                            setWindowOpacity ( visibilityProgress );
                        }
                        else
                        {
                            window.dispose ();
                            hideAnimator.stop ();
                            hideAnimator = null;
                            hiding = false;
                        }
                        hideAnimationStepPerformed ();
                    }
                } );
            }
            else
            {
                window.dispose ();
                hiding = false;
            }
        }
    }

    /**
     * Completes popup hide operation.
     */
    protected void completePopupHide ()
    {
        if ( window != null )
        {
            firePopupWillBeClosed ();

            // Detaching this popup from window
            window.setContentPane ( new EmptyComponent () );

            // Removing follow adapter
            if ( followInvoker && invokerWindow != null )
            {
                uninstallFollowBehavior ();
            }

            // Removing popup hide event listeners
            Toolkit.getDefaultToolkit ().removeAWTEventListener ( mouseListener );
            mouseListener = null;
            FocusManager.unregisterGlobalFocusListener ( this, focusListener );
            focusListener = null;

            // Removing follow adapter
            invokerWindow = null;
            invoker = null;

            // Disposing of popup window
            window.dispose ();
            window = null;

            firePopupClosed ();
        }
    }

    /**
     * Called with each show animation step performed.
     * This method is a placeholder for overriding classes.
     */
    protected void showAnimationStepPerformed ()
    {
        // todo Replace with appropriate listeners
    }

    /**
     * Called with each hide animation step performed.
     * This method is a placeholder for overriding classes.
     */
    protected void hideAnimationStepPerformed ()
    {
        // todo Replace with appropriate listeners
    }

    @Override
    public void addPopupListener ( final PopupListener listener )
    {
        listenerList.add ( PopupListener.class, listener );
    }

    @Override
    public void removePopupListener ( final PopupListener listener )
    {
        listenerList.remove ( PopupListener.class, listener );
    }

    @Override
    public PopupListener beforePopupOpen ( final Runnable action )
    {
        return PopupMethodsImpl.beforePopupOpen ( this, action );
    }

    @Override
    public PopupListener onPopupOpen ( final Runnable action )
    {
        return PopupMethodsImpl.onPopupOpen ( this, action );
    }

    @Override
    public PopupListener beforePopupClose ( final Runnable action )
    {
        return PopupMethodsImpl.beforePopupClose ( this, action );
    }

    @Override
    public PopupListener onPopupClose ( final Runnable action )
    {
        return PopupMethodsImpl.onPopupClose ( this, action );
    }

    /**
     * Notifies listeners that popup will now be opened.
     */
    public void firePopupWillBeOpened ()
    {
        for ( final PopupListener listener : listenerList.getListeners ( PopupListener.class ) )
        {
            listener.popupWillBeOpened ();
        }
    }

    /**
     * Notifies listeners that popup was opened.
     */
    public void firePopupOpened ()
    {
        for ( final PopupListener listener : listenerList.getListeners ( PopupListener.class ) )
        {
            listener.popupOpened ();
        }
    }

    /**
     * Notifies listeners that popup will now be closed.
     */
    public void firePopupWillBeClosed ()
    {
        for ( final PopupListener listener : listenerList.getListeners ( PopupListener.class ) )
        {
            listener.popupWillBeClosed ();
        }
    }

    /**
     * Notifies listeners that popup was closed.
     */
    public void firePopupClosed ()
    {
        for ( final PopupListener listener : listenerList.getListeners ( PopupListener.class ) )
        {
            listener.popupClosed ();
        }
    }

    /**
     * Packs popup window to fit content preferred size.
     *
     * @return popup window
     */
    public WebPopupWindow pack ()
    {
        if ( window != null )
        {
            window.pack ();
        }
        return window;
    }

    @Override
    public void setOpaque ( final boolean isOpaque )
    {
        if ( !changingOpacity )
        {
            changingOpacity = true;
            super.setOpaque ( isOpaque );
            setWindowOpaque ( isOpaque );
            changingOpacity = false;
        }
    }

    @Override
    public boolean isWindowOpaque ()
    {
        return isOpaque ();
    }

    @Override
    public WebPopupWindow setWindowOpaque ( final boolean opaque )
    {
        return updateOpaque ( window );
    }

    /**
     * Updates popup window opaque state and returns the window used for this popup.
     *
     * @param window popup window
     * @return window used for this popup
     */
    protected WebPopupWindow updateOpaque ( final WebPopupWindow window )
    {
        if ( window != null )
        {
            WindowMethodsImpl.setWindowOpaque ( window, isOpaque () );
        }
        return window;
    }

    @Override
    public float getWindowOpacity ()
    {
        return opacity;
    }

    @Override
    public WebPopupWindow setWindowOpacity ( final float opacity )
    {
        this.opacity = opacity;
        return updateOpacity ( window );
    }

    /**
     * Updates popup window opacity and returns the window used for this popup.
     *
     * @param window popup window
     * @return window used for this popup
     */
    protected WebPopupWindow updateOpacity ( final WebPopupWindow window )
    {
        if ( window != null )
        {
            WindowMethodsImpl.setWindowOpacity ( window, opacity * visibilityProgress );
        }
        return window;
    }

    @Override
    public WebPopupWindow center ()
    {
        return WindowMethodsImpl.center ( window );
    }

    @Override
    public WebPopupWindow center ( final Component relativeTo )
    {
        return WindowMethodsImpl.center ( window, relativeTo );
    }

    @Override
    public WebPopupWindow center ( final int width, final int height )
    {
        return WindowMethodsImpl.center ( window, width, height );
    }

    @Override
    public WebPopupWindow center ( final Component relativeTo, final int width, final int height )
    {
        return WindowMethodsImpl.center ( window, relativeTo, width, height );
    }

    @Override
    public WebPopupWindow packToWidth ( final int width )
    {
        return WindowMethodsImpl.packToWidth ( window, width );
    }

    @Override
    public WebPopupWindow packToHeight ( final int height )
    {
        return WindowMethodsImpl.packToHeight ( window, height );
    }

    /**
     * Returns the look and feel (LaF) object that renders this component.
     *
     * @return the StatusBarUI object that renders this component
     */
    public WPopupUI getUI ()
    {
        return ( WPopupUI ) ui;
    }

    /**
     * Sets the LaF object that renders this component.
     *
     * @param ui {@link WPopupUI}
     */
    public void setUI ( final WPopupUI ui )
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
     * {@link Function} providing appropriate resize {@link CompassDirection} according to specified {@link Point} on the {@link WebPopup}.
     */
    public class ResizeDirections implements Function<Point, CompassDirection>
    {
        /**
         * Distance from the inner popup border at which {@link Component} resize is still available.
         */
        protected final int distance;

        /**
         * Constructs new {@link ResizeDirections}.
         *
         * @param distance distance from the inner popup border at which resizer is still available
         */
        public ResizeDirections ( final int distance )
        {
            this.distance = distance;
        }

        /**
         * Returns distance from the inner popup border at which resizer is still available.
         *
         * @return distance from the inner popup border at which resizer is still available
         */
        public int distance ()
        {
            return distance;
        }

        @Override
        public CompassDirection apply ( final Point point )
        {
            final CompassDirection direction;
            if ( isResizable () )
            {
                final Insets i = getInsets ();
                if ( point.getY () < i.top + distance )
                {
                    if ( point.getX () < i.left + distance )
                    {
                        direction = CompassDirection.northWest;
                    }
                    else if ( point.getX () > getWidth () - i.right - distance )
                    {
                        direction = CompassDirection.northEast;
                    }
                    else
                    {
                        direction = CompassDirection.north;
                    }
                }
                else if ( point.getY () > getHeight () - i.bottom - distance )
                {
                    if ( point.getX () < i.left + distance )
                    {
                        direction = CompassDirection.southWest;
                    }
                    else if ( point.getX () > getWidth () - i.right - distance )
                    {
                        direction = CompassDirection.southEast;
                    }
                    else
                    {
                        direction = CompassDirection.south;
                    }
                }
                else if ( point.getX () < i.left + distance )
                {
                    direction = CompassDirection.west;
                }
                else if ( point.getX () > getWidth () - i.right - distance )
                {
                    direction = CompassDirection.east;
                }
                else if ( isDraggable () )
                {
                    direction = CompassDirection.center;
                }
                else
                {
                    direction = null;
                }
            }
            else if ( isDraggable () )
            {
                direction = CompassDirection.center;
            }
            else
            {
                direction = null;
            }
            return direction;
        }
    }
}