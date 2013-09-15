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

import com.alee.extended.painter.Painter;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.focus.DefaultFocusTracker;
import com.alee.managers.focus.FocusManager;
import com.alee.utils.CollectionUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.AncestorAdapter;
import com.alee.utils.swing.EmptyMouseAdapter;
import com.alee.utils.swing.FadeStateType;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * This is base popup class which offers basic popups functionality and contains all features needed to create great-looking popups within
 * the window root pane bounds.
 *
 * @author Mikle Garin
 * @see PopupManager
 * @see PopupLayer
 */

public class WebPopup extends WebPanel
{
    protected List<PopupListener> popupListeners = new ArrayList<PopupListener> ( 2 );

    // Popup constants
    protected static final int fadeFps = 24;
    protected static final long fadeTime = 400;

    protected boolean animated = false;
    protected boolean closeOnFocusLoss = false;
    protected boolean requestFocusOnShow = true;
    protected Component defaultFocusComponent = null;
    protected List<Component> focusableChilds = new ArrayList<Component> ();

    protected Component lastComponent = null;
    protected AncestorListener lastListener = null;

    protected boolean focused = false;

    // Animation variables
    protected FadeStateType fadeStateType;
    protected float fade = 0;
    protected WebTimer fadeTimer;

    public WebPopup ()
    {
        this ( PopupManager.getDefaultPopupPainter () );
    }

    public WebPopup ( PopupStyle popupStyle )
    {
        this ( PopupManager.getPopupPainter ( popupStyle ) );
    }

    public WebPopup ( Painter stylePainter )
    {
        super ( stylePainter );
        initializePopup ();
    }

    /**
     * Initializes various popup settings.
     */
    protected void initializePopup ()
    {
        setOpaque ( false );

        // Popup doesn't allow focus to move outside of it
        setFocusCycleRoot ( true );

        // Listeners to block events passing to underlying components
        EmptyMouseAdapter.install ( this );

        // Fade in-out timer
        fadeTimer = new WebTimer ( "WebPopup.fade", 1000 / fadeFps );
        fadeTimer.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( ActionEvent e )
            {
                final float roundsCount = fadeTime / ( 1000f / fadeFps );
                final float fadeSpeed = 1f / roundsCount;
                if ( fadeStateType.equals ( FadeStateType.fadeIn ) )
                {
                    if ( fade < 1f )
                    {
                        fade = Math.min ( fade + fadeSpeed, 1f );
                        WebPopup.this.repaint ();
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
                        WebPopup.this.repaint ();
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
            public void ancestorAdded ( AncestorEvent event )
            {
                if ( requestFocusOnShow )
                {
                    if ( defaultFocusComponent != null )
                    {
                        defaultFocusComponent.requestFocusInWindow ();
                    }
                    else
                    {
                        WebPopup.this.transferFocus ();
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
            public void ancestorRemoved ( AncestorEvent event )
            {
                // Informing about popup state change
                firePopupClosed ();
            }
        } );

        // Focus tracking
        FocusManager.addFocusTracker ( this, new DefaultFocusTracker ( true )
        {
            @Override
            public boolean isTrackingEnabled ()
            {
                return WebPopup.this.isShowing ();
            }

            @Override
            public void focusChanged ( boolean focused )
            {
                WebPopup.this.focusChanged ( focused );
            }
        } );
    }

    /**
     * Called when this popup recieve or lose focus.
     * You can your own behavior for focus change by overriding this method.
     *
     * @param focused whether popup has focus or not
     */
    protected void focusChanged ( boolean focused )
    {
        // todo Replace with MultiFocusTracker (for multiply components)
        if ( WebPopup.this.isShowing () && !focused && !isChildFocused () && closeOnFocusLoss )
        {
            hidePopup ();
        }
    }

    /**
     * Popup styling
     */

    public void setPopupStyle ( PopupStyle popupStyle )
    {
        setPainter ( PopupManager.getPopupPainter ( popupStyle ) );
    }

    /**
     * Popup settings
     */

    public boolean isAnimated ()
    {
        return animated;
    }

    public void setAnimated ( boolean animated )
    {
        this.animated = animated;
    }

    public boolean isCloseOnFocusLoss ()
    {
        return closeOnFocusLoss;
    }

    public void setCloseOnFocusLoss ( boolean closeOnFocusLoss )
    {
        this.closeOnFocusLoss = closeOnFocusLoss;
    }

    public boolean isRequestFocusOnShow ()
    {
        return requestFocusOnShow;
    }

    public void setRequestFocusOnShow ( boolean requestFocusOnShow )
    {
        this.requestFocusOnShow = requestFocusOnShow;
    }

    public Component getDefaultFocusComponent ()
    {
        return defaultFocusComponent;
    }

    public void setDefaultFocusComponent ( Component defaultFocusComponent )
    {
        this.defaultFocusComponent = defaultFocusComponent;
    }

    /**
     * Focusable components which will not force popup to close
     */

    public List<Component> getFocusableChilds ()
    {
        return focusableChilds;
    }

    public void addFocusableChild ( Component child )
    {
        focusableChilds.add ( child );
    }

    public void removeFocusableChild ( Component child )
    {
        focusableChilds.remove ( child );
    }

    public boolean isChildFocused ()
    {
        for ( Component child : focusableChilds )
        {
            if ( SwingUtils.hasFocusOwner ( child ) )
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Popup display methods
     */

    public void showAsPopupMenu ( final Component component )
    {
        // Detrmining component position inside window
        final Rectangle cb = SwingUtils.getBoundsInWindow ( component );
        final Dimension rps = SwingUtils.getRootPane ( component ).getSize ();
        final Dimension ps = WebPopup.this.getPreferredSize ();
        //        Painter bp = getPainter ();
        //        Insets bm = bp != null ? bp.getMargin ( this ) : new Insets ( 0, 0, 0, 0 );

        // Choosing display way
        final int x;
        if ( cb.x + ps.width < rps.width || rps.width - cb.x - ps.width > cb.x )
        {
            // Expanding popup to the right side
            x = 0;
        }
        else
        {
            // Expanding popup to the left side
            x = cb.width - ps.width;
        }
        final int y;
        if ( cb.y + cb.height + ps.height < rps.height || rps.height - cb.y - cb.height - ps.height > cb.y )
        {
            // Displaying popup below the component
            y = cb.height;
        }
        else
        {
            // Displaying popup above the component
            y = -ps.height;
        }

        showPopup ( component, x, y );
    }

    public void showPopup ( Component component )
    {
        PopupManager.showPopup ( component, this, requestFocusOnShow );
        clearComponentAncestorListener ();
    }

    public void showPopup ( Component component, Point location )
    {
        showPopup ( component, location.x, location.y );
    }

    public void showPopup ( Component component, int x, int y )
    {
        final Dimension ps = WebPopup.this.getPreferredSize ();
        showPopup ( component, x, y, ps.width, ps.height );
    }

    public void showPopup ( Component component, Rectangle bounds )
    {
        showPopup ( component, bounds.x, bounds.y, bounds.width, bounds.height );
    }

    public void showPopup ( Component component, int x, int y, int width, int height )
    {
        updatePopupBounds ( component, x, y, width, height );
        PopupManager.showPopup ( component, this, requestFocusOnShow );
        updateComponentAncestorListener ( component, x, y, width, height );
    }

    protected void updatePopupBounds ( Component component, int x, int y, int width, int height )
    {
        // Updating popup bounds with component-relative values
        if ( component.isShowing () )
        {
            final Rectangle cb = SwingUtils.getBoundsInWindow ( component );
            setBounds ( cb.x + x, cb.y + y, width, height );
        }
    }

    protected void updateComponentAncestorListener ( final Component component, final int x, final int y, final int width,
                                                     final int height )
    {
        clearComponentAncestorListener ();

        lastComponent = component;
        if ( component instanceof JComponent )
        {
            lastListener = new AncestorAdapter ()
            {
                @Override
                public void ancestorMoved ( AncestorEvent event )
                {
                    updatePopupBounds ( component, x, y, width, height );
                }
            };
            ( ( JComponent ) lastComponent ).addAncestorListener ( lastListener );
        }
    }

    protected void clearComponentAncestorListener ()
    {
        if ( lastComponent != null && lastListener != null && lastComponent instanceof JComponent )
        {
            ( ( JComponent ) lastComponent ).removeAncestorListener ( lastListener );
        }
    }

    /**
     * Modal popup display methods
     */

    public void showPopupAsModal ( Component component )
    {
        showPopupAsModal ( component, false, false );
    }

    public void showPopupAsModal ( Component component, boolean hfill, boolean vfill )
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
        clearComponentAncestorListener ();

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

    /**
     * Shape-based point check
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains ( int x, int y )
    {
        return provideShape ().contains ( x, y ) && fadeStateType != FadeStateType.fadeOut;
    }

    /**
     * Popup listeners
     */

    public void addPopupListener ( PopupListener listener )
    {
        popupListeners.add ( listener );
    }

    public void removePopupListener ( PopupListener listener )
    {
        popupListeners.remove ( listener );
    }

    public void firePopupWillBeOpened ()
    {
        for ( PopupListener listener : CollectionUtils.copy ( popupListeners ) )
        {
            listener.popupWillBeOpened ();
        }
    }

    public void firePopupOpened ()
    {
        for ( PopupListener listener : CollectionUtils.copy ( popupListeners ) )
        {
            listener.popupOpened ();
        }
    }

    public void firePopupWillBeClosed ()
    {
        for ( PopupListener listener : CollectionUtils.copy ( popupListeners ) )
        {
            listener.popupWillBeClosed ();
        }
    }

    public void firePopupClosed ()
    {
        for ( PopupListener listener : CollectionUtils.copy ( popupListeners ) )
        {
            listener.popupClosed ();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void paintComponent ( Graphics g )
    {
        // Fade animation and transparency
        if ( fade < 1f )
        {
            LafUtils.setupAlphaComposite ( ( Graphics2D ) g, fade );
        }
        super.paintComponent ( g );
    }
}