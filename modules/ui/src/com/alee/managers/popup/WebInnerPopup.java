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

package com.alee.managers.popup;

import com.alee.api.jdk.Supplier;
import com.alee.extended.window.Popup;
import com.alee.extended.window.PopupListener;
import com.alee.extended.window.PopupMethods;
import com.alee.extended.window.PopupMethodsImpl;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.focus.DefaultFocusTracker;
import com.alee.managers.focus.FocusManager;
import com.alee.managers.style.StyleId;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.AncestorAdapter;
import com.alee.utils.swing.EmptyMouseAdapter;
import com.alee.utils.swing.FadeStateType;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.*;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * This is base popup class which offers basic popups functionality and contains all features needed to create great-looking popups within
 * the window root pane bounds. To create one outside of the window root pane bounds consider using customized dialogs, frames or existing
 * {@link com.alee.extended.window.WebPopOver} component.
 *
 * @author Mikle Garin
 * @see com.alee.managers.popup.PopupManager
 * @see com.alee.managers.popup.PopupLayer
 */
public class WebInnerPopup extends WebPanel implements Popup, PopupMethods
{
    // Popup constants
    protected static final int fadeFps = 24;
    protected static final long fadeTime = 400;

    protected boolean animated = false;
    protected boolean closeOnFocusLoss = false;
    protected boolean requestFocusOnShow = true;
    protected Component defaultFocusComponent = null;
    protected List<WeakReference<Component>> focusableChildren = new ArrayList<WeakReference<Component>> ();

    protected Component lastComponent = null;
    protected ComponentListener lastComponentListener = null;
    protected AncestorListener lastAncestorListener = null;

    protected boolean focused = false;

    // Animation variables
    protected FadeStateType fadeStateType;
    protected float fade = 0;
    protected WebTimer fadeTimer;

    // Focus tracker strong reference
    protected DefaultFocusTracker focusTracker;

    public WebInnerPopup ()
    {
        this ( PopupManager.getDefaultPopupStyleId () );
    }

    public WebInnerPopup ( final StyleId styleId )
    {
        super ( styleId );
        initializePopup ();
    }

    /**
     * Initializes various popup settings.
     */
    protected void initializePopup ()
    {
        // Popup doesn't allow focus to move outside of it
        setFocusCycleRoot ( true );

        // Listeners to block events passing to underlying components
        EmptyMouseAdapter.install ( this );

        // Fade in-out timer
        fadeTimer = new WebTimer ( "WebPopup.fade", 1000 / fadeFps );
        fadeTimer.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                final float roundsCount = fadeTime / ( 1000f / fadeFps );
                final float fadeSpeed = 1f / roundsCount;
                if ( fadeStateType.equals ( FadeStateType.fadeIn ) )
                {
                    if ( fade < 1f )
                    {
                        fade = Math.min ( fade + fadeSpeed, 1f );
                        WebInnerPopup.this.repaint ();
                    }
                    else
                    {
                        fadeTimer.stop ();
                    }
                }
                else if ( fadeStateType.equals ( FadeStateType.fadeOut ) )
                {
                    if ( fade > 0 )
                    {
                        fade = Math.max ( fade - fadeSpeed, 0f );
                        WebInnerPopup.this.repaint ();
                    }
                    else
                    {
                        hidePopupImpl ();
                        fadeTimer.stop ();
                    }
                }
            }
        } );

        // Listener to determine popup appearance and disappearance
        addAncestorListener ( new AncestorAdapter ()
        {
            @Override
            public void ancestorAdded ( final AncestorEvent event )
            {
                if ( requestFocusOnShow )
                {
                    if ( defaultFocusComponent != null )
                    {
                        defaultFocusComponent.requestFocusInWindow ();
                    }
                    else
                    {
                        WebInnerPopup.this.transferFocus ();
                    }
                }

                // Starting fade-in animation
                if ( animated )
                {
                    fade = 0;
                    fadeStateType = FadeStateType.fadeIn;
                    fadeTimer.start ();
                }
                else
                {
                    fade = 1;
                }

                // Informing about popup state change
                firePopupOpened ();
            }

            @Override
            public void ancestorRemoved ( final AncestorEvent event )
            {
                // Informing about popup state change
                firePopupClosed ();
            }
        } );

        // Focus tracking
        focusTracker = new DefaultFocusTracker ( this, true )
        {
            @Override
            public void focusChanged ( final boolean focused )
            {
                WebInnerPopup.this.focusChanged ( focused );
            }
        };
        FocusManager.addFocusTracker ( this, focusTracker );
    }

    /**
     * Called when this popup receive or lose focus.
     * You can your own behavior for focus change by overriding this method.
     *
     * @param focused whether popup has focus or not
     */
    protected void focusChanged ( final boolean focused )
    {
        // todo Replace with MultiFocusTracker (for multiple components)
        if ( WebInnerPopup.this.isShowing () && !focused && !isChildFocused () && closeOnFocusLoss )
        {
            hidePopup ();
        }
    }

    /**
     * Returns popup layer this WebPopup is added into.
     *
     * @return popup layer this WebPopup is added into
     */
    public PopupLayer getPopupLayer ()
    {
        return ( PopupLayer ) getParent ();
    }

    /**
     * Popup settings
     */

    public boolean isAnimated ()
    {
        return animated;
    }

    public void setAnimated ( final boolean animated )
    {
        this.animated = animated;
    }

    public boolean isCloseOnFocusLoss ()
    {
        return closeOnFocusLoss;
    }

    public void setCloseOnFocusLoss ( final boolean closeOnFocusLoss )
    {
        this.closeOnFocusLoss = closeOnFocusLoss;
    }

    public boolean isRequestFocusOnShow ()
    {
        return requestFocusOnShow;
    }

    public void setRequestFocusOnShow ( final boolean requestFocusOnShow )
    {
        this.requestFocusOnShow = requestFocusOnShow;
    }

    public Component getDefaultFocusComponent ()
    {
        return defaultFocusComponent;
    }

    public void setDefaultFocusComponent ( final Component defaultFocusComponent )
    {
        this.defaultFocusComponent = defaultFocusComponent;
    }

    /**
     * Returns focusable children that don't force dialog to close even if it set to close on focus loss.
     *
     * @return focusable children that don't force dialog to close even if it set to close on focus loss
     */
    public List<Component> getFocusableChildren ()
    {
        final List<Component> actualFocusableChildren = new ArrayList<Component> ( focusableChildren.size () );
        for ( final WeakReference<Component> focusableChild : focusableChildren )
        {
            final Component component = focusableChild.get ();
            if ( component != null )
            {
                actualFocusableChildren.add ( component );
            }
        }
        return actualFocusableChildren;
    }

    /**
     * Adds focusable child that won't force dialog to close even if it set to close on focus loss.
     *
     * @param child focusable child that won't force dialog to close even if it set to close on focus loss
     */
    public void addFocusableChild ( final Component child )
    {
        focusableChildren.add ( new WeakReference<Component> ( child ) );
    }

    /**
     * Removes focusable child that doesn't force dialog to close even if it set to close on focus loss.
     *
     * @param child focusable child that doesn't force dialog to close even if it set to close on focus loss
     */
    public void removeFocusableChild ( final Component child )
    {
        focusableChildren.remove ( child );
    }

    /**
     * Returns whether one of focusable children is focused or not.
     *
     * @return true if one of focusable children is focused, false otherwise
     */
    public boolean isChildFocused ()
    {
        for ( final WeakReference<Component> focusableChild : focusableChildren )
        {
            final Component component = focusableChild.get ();
            if ( component != null )
            {
                if ( SwingUtils.hasFocusOwner ( component ) )
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Popup display methods
     */

    public void showAsPopupMenu ( final Component component )
    {
        showPopup ( component, new Supplier<Rectangle> ()
        {
            @Override
            public Rectangle get ()
            {
                // Determining component position inside window
                final Rectangle cb = CoreSwingUtils.getBoundsInWindow ( component );
                final Dimension rps = CoreSwingUtils.getRootPane ( component ).getSize ();
                final Dimension ps = WebInnerPopup.this.getPreferredSize ();
                //        Painter bp = getPainter ();
                //        Insets bm = bp != null ? bp.getMargin ( this ) : new Insets ( 0, 0, 0, 0 );

                // Choosing display way
                final Point p = new Point ();
                if ( cb.x + ps.width < rps.width || rps.width - cb.x - ps.width > cb.x )
                {
                    // Expanding popup to the right side
                    p.x = 0;
                }
                else
                {
                    // Expanding popup to the left side
                    p.x = cb.width - ps.width;
                }
                if ( cb.y + cb.height + ps.height < rps.height || rps.height - cb.y - cb.height - ps.height > cb.y )
                {
                    // Displaying popup below the component
                    p.y = cb.height;
                }
                else
                {
                    // Displaying popup above the component
                    p.y = -ps.height;
                }

                // Returning proper location
                return new Rectangle ( p, WebInnerPopup.this.getPreferredSize () );
            }
        } );
    }

    public void showPopup ( final Component component )
    {
        PopupManager.showPopup ( component, this, requestFocusOnShow );
        clearLocationListeners ();
    }

    public void showPopup ( final Component component, final Rectangle bounds )
    {
        showPopup ( component, bounds.x, bounds.y, bounds.width, bounds.height );
    }

    public void showPopup ( final Component component, final int x, final int y, final int width, final int height )
    {
        updatePopupBounds ( component, x, y, width, height );
        PopupManager.showPopup ( component, this, requestFocusOnShow );
        updateComponentAncestorListener ( component, x, y, width, height );
    }

    public void showPopup ( final Component component, final Point location )
    {
        showPopup ( component, location.x, location.y );
    }

    public void showPopup ( final Component component, final int x, final int y )
    {
        showPopup ( component, new Supplier<Rectangle> ()
        {
            @Override
            public Rectangle get ()
            {
                final Dimension ps = WebInnerPopup.this.getPreferredSize ();
                return new Rectangle ( x, y, ps.width, ps.height );
            }
        } );
    }

    public void showPopup ( final Component component, final Supplier<Rectangle> boundsSupplier )
    {
        updatePopupBounds ( component, boundsSupplier.get () );
        PopupManager.showPopup ( component, this, requestFocusOnShow );
        updateLocationListeners ( component, boundsSupplier );
    }

    protected void updatePopupBounds ( final Component component, final Rectangle bounds )
    {
        updatePopupBounds ( component, bounds.x, bounds.y, bounds.width, bounds.height );
    }

    protected void updatePopupBounds ( final Component component, final int x, final int y, final int width, final int height )
    {
        // Updating popup bounds with component-relative values
        if ( component.isShowing () )
        {
            final Rectangle cb = CoreSwingUtils.getBoundsInWindow ( component );
            setBounds ( cb.x + x, cb.y + y, width, height );
            revalidate ();
            repaint ();
        }
    }

    protected void updateComponentAncestorListener ( final Component component, final int x, final int y, final int width,
                                                     final int height )
    {
        clearLocationListeners ();

        lastComponent = component;
        if ( component instanceof JComponent )
        {
            lastAncestorListener = new AncestorAdapter ()
            {
                @Override
                public void ancestorMoved ( final AncestorEvent event )
                {
                    updatePopupBounds ( component, x, y, width, height );
                }
            };
            ( ( JComponent ) lastComponent ).addAncestorListener ( lastAncestorListener );
        }
    }

    protected void updateLocationListeners ( final Component component, final Supplier<Rectangle> boundsSupplier )
    {
        clearLocationListeners ();

        lastComponent = component;

        lastComponentListener = new ComponentAdapter ()
        {
            @Override
            public void componentResized ( final ComponentEvent e )
            {
                updatePopupBounds ( component, boundsSupplier.get () );
            }

            @Override
            public void componentMoved ( final ComponentEvent e )
            {
                updatePopupBounds ( component, boundsSupplier.get () );
            }
        };
        lastComponent.addComponentListener ( lastComponentListener );

        if ( component instanceof JComponent )
        {
            lastAncestorListener = new AncestorAdapter ()
            {
                @Override
                public void ancestorMoved ( final AncestorEvent event )
                {
                    updatePopupBounds ( component, boundsSupplier.get () );
                }
            };
            ( ( JComponent ) lastComponent ).addAncestorListener ( lastAncestorListener );
        }
    }

    protected void clearLocationListeners ()
    {
        if ( lastComponent != null )
        {
            if ( lastComponentListener != null )
            {
                lastComponent.removeComponentListener ( lastComponentListener );
            }
            if ( lastAncestorListener != null && lastComponent instanceof JComponent )
            {
                ( ( JComponent ) lastComponent ).removeAncestorListener ( lastAncestorListener );
            }
        }
    }

    /**
     * Modal popup display methods
     */

    public void showPopupAsModal ( final Component component )
    {
        showPopupAsModal ( component, false, false );
    }

    public void showPopupAsModal ( final Component component, final boolean hfill, final boolean vfill )
    {
        PopupManager.showModalPopup ( component, this, hfill, vfill );
    }

    /**
     * Popup hide methods
     */

    public void hidePopup ()
    {
        if ( animated )
        {
            fadeStateType = FadeStateType.fadeOut;
            if ( !fadeTimer.isRunning () )
            {
                fadeTimer.start ();
            }
        }
        else
        {
            hidePopupImpl ();
        }
    }

    protected void hidePopupImpl ()
    {
        clearLocationListeners ();

        final PopupLayer layer = ( PopupLayer ) this.getParent ();
        if ( layer != null )
        {
            layer.hidePopup ( this );
        }
    }

    /**
     * Popup pack method
     */

    public void packPopup ()
    {
        setSize ( getPreferredSize () );
    }

    public void updateBounds ()
    {
        if ( lastAncestorListener != null )
        {
            // todo Replace with a better solution
            lastAncestorListener.ancestorMoved ( null );
        }
    }

    /**
     * Shape-based point check
     */

    @Override
    public boolean contains ( final int x, final int y )
    {
        return fadeStateType != FadeStateType.fadeOut && getShape ().contains ( x, y );
    }

    /**
     * Popup listeners
     */

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

    @Override
    protected void paintComponent ( final Graphics g )
    {
        // Fade animation and opacity
        if ( fade < 1f )
        {
            GraphicsUtils.setupAlphaComposite ( ( Graphics2D ) g, fade );
        }
        super.paintComponent ( g );
    }
}