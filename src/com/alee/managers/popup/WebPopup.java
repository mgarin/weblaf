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
import com.alee.managers.focus.FocusManager;
import com.alee.managers.focus.FocusTracker;
import com.alee.utils.CollectionUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.AncestorAdapter;
import com.alee.utils.swing.EmptyMouseAdapter;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mgarin Date: 26.03.12 Time: 17:06
 * <p/>
 * This is base popup class which offers basic popups functionality and contains all features needed to create great-looking popups within
 * the window root pane bounds.
 */

public class WebPopup extends WebPanel implements FocusTracker
{
    private List<PopupListener> popupListeners = new ArrayList<PopupListener> ();

    private boolean closeOnFocusLoss = false;
    private boolean requestFocusOnShow = true;
    private Component defaultFocusComponent = null;
    private List<Component> focusableChilds = new ArrayList<Component> ();

    private Component lastComponent = null;
    private AncestorListener lastListener = null;

    protected boolean focused = false;

    public WebPopup ()
    {
        this ( PopupManager.getPopupPainter () );
    }

    public WebPopup ( PopupStyle popupStyle )
    {
        this ( PopupManager.getPopupPainter ( popupStyle ) );
    }

    public WebPopup ( Painter style )
    {
        super ();
        setOpaque ( false );

        // Default popup style
        setPainter ( style );

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
                firePopupOpened ();
            }

            @Override
            public void ancestorRemoved ( AncestorEvent event )
            {
                firePopupClosed ();
            }
        } );

        // Listeners to block events passing to underlying components
        EmptyMouseAdapter.install ( this );

        // Focus manager
        FocusManager.registerFocusTracker ( this );

        // Focus traversal policy
        setFocusCycleRoot ( true );
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
     * Focus tracker methods
     */

    @Override
    public boolean isTrackingEnabled ()
    {
        return WebPopup.this.isShowing ();
    }

    @Override
    public Component getTrackedComponent ()
    {
        return WebPopup.this;
    }

    @Override
    public boolean isUniteWithChilds ()
    {
        return true;
    }

    @Override
    public boolean isListenGlobalChange ()
    {
        return true;
    }

    @Override
    public void focusChanged ( boolean focused )
    {
        this.focused = focused;
        if ( WebPopup.this.isShowing () && !focused && !isChildFocused () && closeOnFocusLoss )
        {
            hidePopup ();
        }
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

    public void showPopup ( final Component component )
    {
        // Detrmining component position inside window
        Rectangle cb = SwingUtils.getBoundsInWindow ( component );
        Dimension rps = SwingUtils.getRootPaneAncestor ( component ).getSize ();
        Dimension ps = WebPopup.this.getPreferredSize ();
        //        Painter bp = getPainter ();
        //        Insets bm = bp != null ? bp.getMargin ( this ) : new Insets ( 0, 0, 0, 0 );

        // Choosing display way
        int x;
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
        int y;
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

    public void showPopup ( Component component, Point location )
    {
        showPopup ( component, location.x, location.y );
    }

    public void showPopup ( Component component, int x, int y )
    {
        Dimension ps = WebPopup.this.getPreferredSize ();
        showPopup ( component, x, y, ps.width, ps.height );
    }

    public void showPopup ( Component component, Rectangle bounds )
    {
        showPopup ( component, bounds.x, bounds.y, bounds.width, bounds.height );
    }

    public void showPopup ( Component component, int x, int y, int width, int height )
    {
        // Updating popup location
        updatePopupBounds ( component, x, y, width, height );

        // Displaying popup
        PopupManager.showPopup ( component, this, requestFocusOnShow );

        // Updating component "follow" listener
        updateComponentAncestorListener ( component, x, y, width, height );
    }

    private void updatePopupBounds ( Component component, int x, int y, int width, int height )
    {
        // Updating popup bounds with component-relative values
        if ( component.isShowing () )
        {
            Rectangle cb = SwingUtils.getBoundsInWindow ( component );
            setBounds ( cb.x + x, cb.y + y, width, height );
        }
    }

    private void updateComponentAncestorListener ( final Component component, final int x, final int y, final int width, final int height )
    {
        if ( lastComponent != null && lastListener != null && lastComponent instanceof JComponent )
        {
            ( ( JComponent ) lastComponent ).removeAncestorListener ( lastListener );
        }

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
        PopupLayer layer = ( PopupLayer ) getParent ();
        if ( layer != null )
        {
            layer.hidePopup ( WebPopup.this );
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

    @Override
    public boolean contains ( int x, int y )
    {
        return provideShape ().contains ( x, y );
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
}