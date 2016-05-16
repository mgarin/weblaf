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

import com.alee.api.jdk.Function;
import com.alee.extended.behavior.ComponentResizeBehavior;
import com.alee.global.StyleConstants;
import com.alee.laf.window.WindowMethods;
import com.alee.laf.window.WindowMethodsImpl;
import com.alee.managers.focus.FocusManager;
import com.alee.managers.focus.GlobalFocusListener;
import com.alee.managers.hotkey.HotkeyData;
import com.alee.managers.language.data.TooltipWay;
import com.alee.managers.log.Log;
import com.alee.managers.style.*;
import com.alee.managers.tooltip.ToolTipMethods;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.managers.tooltip.WebCustomTooltip;
import com.alee.painter.Paintable;
import com.alee.painter.Painter;
import com.alee.painter.decoration.states.CompassDirection;
import com.alee.utils.ProprietaryUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.MouseButton;
import com.alee.utils.swing.WebTimer;
import com.alee.utils.swing.extensions.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Custom extension that makes use of Swing heavy-weight popup.
 * It also provides same basic methods to manipulate popup window and its settings.
 *
 * @param <T> popup type
 * @author Mikle Garin
 */

public class WebPopup<T extends WebPopup<T>> extends JComponent
        implements Popup, PopupMethods, Styleable, Skinnable, Paintable, ShapeProvider, MarginSupport, PaddingSupport, ContainerMethods<T>,
        EventMethods, ToolTipMethods, SizeMethods<T>, WindowMethods<WebPopupWindow>
{
    /**
     * Whether or not this popup should be resizable.
     */
    protected boolean resizable = false;

    /**
     * Whether or not should close popup on any action outside of this popup.
     */
    protected boolean closeOnOuterAction = true;

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
     * Single animation step size.
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

    /**
     * Function that provides resize direction based on popup location.
     */
    protected Function<Point, CompassDirection> resizeDirectives;

    /**
     * Constructs new popup.
     */
    public WebPopup ()
    {
        this ( StyleId.popup );
    }

    /**
     * Constructs new popup.
     *
     * @param component popup content
     */
    public WebPopup ( final Component component )
    {
        this ( StyleId.popup, component );
    }

    /**
     * Constructs new popup.
     *
     * @param layout     popup layout
     * @param components popup contents
     */
    public WebPopup ( final LayoutManager layout, final Component... components )
    {
        this ( StyleId.popup, layout, components );
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
        setLayout ( layout );
        updateUI ();
        setStyleId ( styleId );
        add ( components );
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
            if ( resizable )
            {
                ComponentResizeBehavior.install ( this, getResizeDirectives () );
            }
            else
            {
                ComponentResizeBehavior.uninstall ( this );
            }
        }
    }

    /**
     * Returns popup resize directives.
     *
     * @return popup resize directives
     */
    protected Function<Point, CompassDirection> getResizeDirectives ()
    {
        return new Function<Point, CompassDirection> ()
        {
            @Override
            public CompassDirection apply ( final Point point )
            {
                final Insets i = getInsets ();
                final int resizer = 6;
                if ( point.getY () < i.top + resizer )
                {
                    if ( point.getX () < i.left + resizer )
                    {
                        return CompassDirection.northWest;
                    }
                    else if ( point.getX () > getWidth () - i.right - resizer )
                    {
                        return CompassDirection.northEast;
                    }
                    else
                    {
                        return CompassDirection.north;
                    }
                }
                else if ( point.getY () > getHeight () - i.bottom - resizer )
                {
                    if ( point.getX () < i.left + resizer )
                    {
                        return CompassDirection.southWest;
                    }
                    else if ( point.getX () > getWidth () - i.right - resizer )
                    {
                        return CompassDirection.southEast;
                    }
                    else
                    {
                        return CompassDirection.south;
                    }
                }
                else if ( point.getX () < i.left + resizer )
                {
                    return CompassDirection.west;
                }
                else if ( point.getX () > getWidth () - i.right - resizer )
                {
                    return CompassDirection.east;
                }
                else
                {
                    return null;
                }
            }
        };
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

    @Override
    public void setOpaque ( final boolean isOpaque )
    {
        super.setOpaque ( isOpaque );
        setWindowOpaque ( isOpaque );
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
    }

    /**
     * Installs invoker follow behavior.
     */
    protected void installFollowAdapter ()
    {
        followAdapter = WindowFollowBehavior.install ( window, invokerWindow );
    }

    /**
     * Uninstalls invoker follow behavior.
     */
    protected void uninstallFollowAdapter ()
    {
        WindowFollowBehavior.uninstall ( window, invokerWindow );
        followAdapter = null;
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
            this.visibilityProgress = animate ? 0f : 1f;

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
            this.visibilityProgress = animate ? 1f : 0f;

            if ( animate )
            {
                hideAnimator = WebTimer.repeat ( StyleConstants.fps48, 0L, new ActionListener ()
                {
                    @Override
                    public void actionPerformed ( final ActionEvent e )
                    {
                        synchronized ( sync )
                        {
                            if ( visibilityProgress > 0f )
                            {
                                visibilityProgress = Math.max ( visibilityProgress - fadeStepSize, 0f );
                                setWindowOpacity ( visibilityProgress );
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
                hiding = false;
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

    @Override
    public void addPopupListener ( final PopupListener listener )
    {
        synchronized ( lsync )
        {
            listenerList.add ( PopupListener.class, listener );
        }
    }

    @Override
    public void removePopupListener ( final PopupListener listener )
    {
        synchronized ( lsync )
        {
            listenerList.remove ( PopupListener.class, listener );
        }
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
    public StyleId getStyleId ()
    {
        return getWebUI ().getStyleId ();
    }

    @Override
    public StyleId setStyleId ( final StyleId id )
    {
        return getWebUI ().setStyleId ( id );
    }

    @Override
    public Skin getSkin ()
    {
        return StyleManager.getSkin ( this );
    }

    @Override
    public Skin setSkin ( final Skin skin )
    {
        return StyleManager.setSkin ( this, skin );
    }

    @Override
    public Skin setSkin ( final Skin skin, final boolean recursively )
    {
        return StyleManager.setSkin ( this, skin, recursively );
    }

    @Override
    public Skin restoreSkin ()
    {
        return StyleManager.restoreSkin ( this );
    }

    @Override
    public void addStyleListener ( final StyleListener listener )
    {
        StyleManager.addStyleListener ( this, listener );
    }

    @Override
    public void removeStyleListener ( final StyleListener listener )
    {
        StyleManager.removeStyleListener ( this, listener );
    }

    @Override
    public Map<String, Painter> getCustomPainters ()
    {
        return StyleManager.getCustomPainters ( this );
    }

    @Override
    public Painter getCustomPainter ()
    {
        return StyleManager.getCustomPainter ( this );
    }

    @Override
    public Painter getCustomPainter ( final String id )
    {
        return StyleManager.getCustomPainter ( this, id );
    }

    @Override
    public Painter setCustomPainter ( final Painter painter )
    {
        return StyleManager.setCustomPainter ( this, painter );
    }

    @Override
    public Painter setCustomPainter ( final String id, final Painter painter )
    {
        return StyleManager.setCustomPainter ( this, id, painter );
    }

    @Override
    public boolean restoreDefaultPainters ()
    {
        return StyleManager.restoreDefaultPainters ( this );
    }

    @Override
    public Shape provideShape ()
    {
        return getWebUI ().provideShape ();
    }

    @Override
    public Insets getMargin ()
    {
        return getWebUI ().getMargin ();
    }

    /**
     * Sets new margin.
     *
     * @param margin new margin
     */
    public void setMargin ( final int margin )
    {
        setMargin ( margin, margin, margin, margin );
    }

    /**
     * Sets new margin.
     *
     * @param top    new top margin
     * @param left   new left margin
     * @param bottom new bottom margin
     * @param right  new right margin
     */
    public void setMargin ( final int top, final int left, final int bottom, final int right )
    {
        setMargin ( new Insets ( top, left, bottom, right ) );
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        getWebUI ().setMargin ( margin );
    }

    @Override
    public Insets getPadding ()
    {
        return getWebUI ().getPadding ();
    }

    /**
     * Sets new padding.
     *
     * @param padding new padding
     */
    public void setPadding ( final int padding )
    {
        setPadding ( padding, padding, padding, padding );
    }

    /**
     * Sets new padding.
     *
     * @param top    new top padding
     * @param left   new left padding
     * @param bottom new bottom padding
     * @param right  new right padding
     */
    public void setPadding ( final int top, final int left, final int bottom, final int right )
    {
        setPadding ( new Insets ( top, left, bottom, right ) );
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        getWebUI ().setPadding ( padding );
    }

    /**
     * Returns Web-UI applied to this class.
     *
     * @return Web-UI applied to this class
     */
    private WebPopupUI getWebUI ()
    {
        return ( WebPopupUI ) getUI ();
    }

    /**
     * Returns the look and feel (L&amp;F) object that renders this component.
     *
     * @return the StatusBarUI object that renders this component
     */
    public PopupUI getUI ()
    {
        return ( PopupUI ) ui;
    }

    /**
     * Installs a Web-UI into this component.
     */
    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebPopupUI ) )
        {
            try
            {
                setUI ( UIManager.getUI ( this ) );
            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebPopupUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }

    @Override
    public String getUIClassID ()
    {
        return StyleableComponent.popup.getUIClassID ();
    }

    @Override
    public boolean contains ( final Component component )
    {
        return ContainerMethodsImpl.contains ( this, component );
    }

    @Override
    public T add ( final List<? extends Component> components )
    {
        return ContainerMethodsImpl.add ( this, components );
    }

    @Override
    public T add ( final List<? extends Component> components, final int index )
    {
        return ContainerMethodsImpl.add ( this, components, index );
    }

    @Override
    public T add ( final List<? extends Component> components, final Object constraints )
    {
        return ContainerMethodsImpl.add ( this, components, constraints );
    }

    @Override
    public T add ( final Component component1, final Component component2 )
    {
        return ContainerMethodsImpl.add ( this, component1, component2 );
    }

    @Override
    public T add ( final Component... components )
    {
        return ContainerMethodsImpl.add ( this, components );
    }

    @Override
    public T remove ( final List<? extends Component> components )
    {
        return ContainerMethodsImpl.remove ( this, components );
    }

    @Override
    public T remove ( final Component... components )
    {
        return ContainerMethodsImpl.remove ( this, components );
    }

    @Override
    public T removeAll ( final Class<? extends Component> componentClass )
    {
        return ContainerMethodsImpl.removeAll ( this, componentClass );
    }

    @Override
    public Component getFirstComponent ()
    {
        return ContainerMethodsImpl.getFirstComponent ( this );
    }

    @Override
    public Component getLastComponent ()
    {
        return ContainerMethodsImpl.getLastComponent ( this );
    }

    @Override
    public T equalizeComponentsWidth ()
    {
        return ContainerMethodsImpl.equalizeComponentsWidth ( this );
    }

    @Override
    public T equalizeComponentsHeight ()
    {
        return ContainerMethodsImpl.equalizeComponentsHeight ( this );
    }

    @Override
    public T equalizeComponentsSize ()
    {
        return ContainerMethodsImpl.equalizeComponentsSize ( this );
    }

    @Override
    public MouseAdapter onMousePress ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMousePress ( this, runnable );
    }

    @Override
    public MouseAdapter onMousePress ( final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMousePress ( this, mouseButton, runnable );
    }

    @Override
    public MouseAdapter onMouseEnter ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseEnter ( this, runnable );
    }

    @Override
    public MouseAdapter onMouseExit ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseExit ( this, runnable );
    }

    @Override
    public MouseAdapter onMouseDrag ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseDrag ( this, runnable );
    }

    @Override
    public MouseAdapter onMouseDrag ( final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseDrag ( this, mouseButton, runnable );
    }

    @Override
    public MouseAdapter onMouseClick ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseClick ( this, runnable );
    }

    @Override
    public MouseAdapter onMouseClick ( final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseClick ( this, mouseButton, runnable );
    }

    @Override
    public MouseAdapter onDoubleClick ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onDoubleClick ( this, runnable );
    }

    @Override
    public MouseAdapter onMenuTrigger ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMenuTrigger ( this, runnable );
    }

    @Override
    public KeyAdapter onKeyType ( final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyType ( this, runnable );
    }

    @Override
    public KeyAdapter onKeyType ( final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyType ( this, hotkey, runnable );
    }

    @Override
    public KeyAdapter onKeyPress ( final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyPress ( this, runnable );
    }

    @Override
    public KeyAdapter onKeyPress ( final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyPress ( this, hotkey, runnable );
    }

    @Override
    public KeyAdapter onKeyRelease ( final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyRelease ( this, runnable );
    }

    @Override
    public KeyAdapter onKeyRelease ( final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyRelease ( this, hotkey, runnable );
    }

    @Override
    public FocusAdapter onFocusGain ( final FocusEventRunnable runnable )
    {
        return EventMethodsImpl.onFocusGain ( this, runnable );
    }

    @Override
    public FocusAdapter onFocusLoss ( final FocusEventRunnable runnable )
    {
        return EventMethodsImpl.onFocusLoss ( this, runnable );
    }

    @Override
    public WebCustomTooltip setToolTip ( final String tooltip )
    {
        return TooltipManager.setTooltip ( this, tooltip );
    }

    @Override
    public WebCustomTooltip setToolTip ( final Icon icon, final String tooltip )
    {
        return TooltipManager.setTooltip ( this, icon, tooltip );
    }

    @Override
    public WebCustomTooltip setToolTip ( final String tooltip, final TooltipWay tooltipWay )
    {
        return TooltipManager.setTooltip ( this, tooltip, tooltipWay );
    }

    @Override
    public WebCustomTooltip setToolTip ( final Icon icon, final String tooltip, final TooltipWay tooltipWay )
    {
        return TooltipManager.setTooltip ( this, icon, tooltip, tooltipWay );
    }

    @Override
    public WebCustomTooltip setToolTip ( final String tooltip, final TooltipWay tooltipWay, final int delay )
    {
        return TooltipManager.setTooltip ( this, tooltip, tooltipWay, delay );
    }

    @Override
    public WebCustomTooltip setToolTip ( final Icon icon, final String tooltip, final TooltipWay tooltipWay, final int delay )
    {
        return TooltipManager.setTooltip ( this, icon, tooltip, tooltipWay, delay );
    }

    @Override
    public WebCustomTooltip setToolTip ( final JComponent tooltip )
    {
        return TooltipManager.setTooltip ( this, tooltip );
    }

    @Override
    public WebCustomTooltip setToolTip ( final JComponent tooltip, final int delay )
    {
        return TooltipManager.setTooltip ( this, tooltip, delay );
    }

    @Override
    public WebCustomTooltip setToolTip ( final JComponent tooltip, final TooltipWay tooltipWay )
    {
        return TooltipManager.setTooltip ( this, tooltip, tooltipWay );
    }

    @Override
    public WebCustomTooltip setToolTip ( final JComponent tooltip, final TooltipWay tooltipWay, final int delay )
    {
        return TooltipManager.setTooltip ( this, tooltip, tooltipWay, delay );
    }

    @Override
    public WebCustomTooltip addToolTip ( final String tooltip )
    {
        return TooltipManager.addTooltip ( this, tooltip );
    }

    @Override
    public WebCustomTooltip addToolTip ( final Icon icon, final String tooltip )
    {
        return TooltipManager.addTooltip ( this, icon, tooltip );
    }

    @Override
    public WebCustomTooltip addToolTip ( final String tooltip, final TooltipWay tooltipWay )
    {
        return TooltipManager.addTooltip ( this, tooltip, tooltipWay );
    }

    @Override
    public WebCustomTooltip addToolTip ( final Icon icon, final String tooltip, final TooltipWay tooltipWay )
    {
        return TooltipManager.addTooltip ( this, icon, tooltip, tooltipWay );
    }

    @Override
    public WebCustomTooltip addToolTip ( final String tooltip, final TooltipWay tooltipWay, final int delay )
    {
        return TooltipManager.addTooltip ( this, tooltip, tooltipWay, delay );
    }

    @Override
    public WebCustomTooltip addToolTip ( final Icon icon, final String tooltip, final TooltipWay tooltipWay, final int delay )
    {
        return TooltipManager.addTooltip ( this, icon, tooltip, tooltipWay, delay );
    }

    @Override
    public WebCustomTooltip addToolTip ( final JComponent tooltip )
    {
        return TooltipManager.addTooltip ( this, tooltip );
    }

    @Override
    public WebCustomTooltip addToolTip ( final JComponent tooltip, final int delay )
    {
        return TooltipManager.addTooltip ( this, tooltip, delay );
    }

    @Override
    public WebCustomTooltip addToolTip ( final JComponent tooltip, final TooltipWay tooltipWay )
    {
        return TooltipManager.addTooltip ( this, tooltip, tooltipWay );
    }

    @Override
    public WebCustomTooltip addToolTip ( final JComponent tooltip, final TooltipWay tooltipWay, final int delay )
    {
        return TooltipManager.addTooltip ( this, tooltip, tooltipWay, delay );
    }

    @Override
    public void removeToolTip ( final WebCustomTooltip tooltip )
    {
        TooltipManager.removeTooltip ( this, tooltip );
    }

    @Override
    public void removeToolTips ()
    {
        TooltipManager.removeTooltips ( this );
    }

    @Override
    public void removeToolTips ( final WebCustomTooltip... tooltips )
    {
        TooltipManager.removeTooltips ( this, tooltips );
    }

    @Override
    public void removeToolTips ( final List<WebCustomTooltip> tooltips )
    {
        TooltipManager.removeTooltips ( this, tooltips );
    }

    @Override
    public int getPreferredWidth ()
    {
        return SizeMethodsImpl.getPreferredWidth ( this );
    }

    @Override
    public T setPreferredWidth ( final int preferredWidth )
    {
        return SizeMethodsImpl.setPreferredWidth ( this, preferredWidth );
    }

    @Override
    public int getPreferredHeight ()
    {
        return SizeMethodsImpl.getPreferredHeight ( this );
    }

    @Override
    public T setPreferredHeight ( final int preferredHeight )
    {
        return SizeMethodsImpl.setPreferredHeight ( this, preferredHeight );
    }

    @Override
    public int getMinimumWidth ()
    {
        return SizeMethodsImpl.getMinimumWidth ( this );
    }

    @Override
    public T setMinimumWidth ( final int minimumWidth )
    {
        return SizeMethodsImpl.setMinimumWidth ( this, minimumWidth );
    }

    @Override
    public int getMinimumHeight ()
    {
        return SizeMethodsImpl.getMinimumHeight ( this );
    }

    @Override
    public T setMinimumHeight ( final int minimumHeight )
    {
        return SizeMethodsImpl.setMinimumHeight ( this, minimumHeight );
    }

    @Override
    public int getMaximumWidth ()
    {
        return SizeMethodsImpl.getMaximumWidth ( this );
    }

    @Override
    public T setMaximumWidth ( final int maximumWidth )
    {
        return SizeMethodsImpl.setMaximumWidth ( this, maximumWidth );
    }

    @Override
    public int getMaximumHeight ()
    {
        return SizeMethodsImpl.getMaximumHeight ( this );
    }

    @Override
    public T setMaximumHeight ( final int maximumHeight )
    {
        return SizeMethodsImpl.setMaximumHeight ( this, maximumHeight );
    }

    @Override
    public Dimension getPreferredSize ()
    {
        return SizeMethodsImpl.getPreferredSize ( this, super.getPreferredSize () );
    }

    @Override
    public Dimension getOriginalPreferredSize ()
    {
        return SizeMethodsImpl.getOriginalPreferredSize ( this, super.getPreferredSize () );
    }

    @Override
    public T setPreferredSize ( final int width, final int height )
    {
        return SizeMethodsImpl.setPreferredSize ( this, width, height );
    }

    @Override
    public boolean isWindowOpaque ()
    {
        return isOpaque ();
    }

    @Override
    public WebPopupWindow setWindowOpaque ( final boolean opaque )
    {
        return updateOpaque ();
    }

    /**
     * Updates popup window opaque state and returns the window used for this popup.
     *
     * @return window used for this popup
     */
    protected WebPopupWindow updateOpaque ()
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
        return updateOpacity ();
    }

    /**
     * Updates popup window opacity and returns the window used for this popup.
     *
     * @return window used for this popup
     */
    protected WebPopupWindow updateOpacity ()
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
}