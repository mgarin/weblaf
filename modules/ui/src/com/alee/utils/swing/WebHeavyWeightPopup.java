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

package com.alee.utils.swing;

import com.alee.extended.painter.Painter;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.focus.FocusManager;
import com.alee.managers.focus.GlobalFocusListener;
import com.alee.utils.CollectionUtils;
import com.alee.utils.ProprietaryUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.WindowUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom extension that makes use of Swing heavy-weight popup.
 * It also provides same basic methods to manipulate popup window and its settings.
 *
 * @author Mikle Garin
 */

public class WebHeavyWeightPopup extends WebPanel implements WindowMethods<JWindow>
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
     * Underlying Swing popup in which content is currently displayed.
     */
    protected Popup popup;

    /**
     * Window in which popup content is currently displayed.
     */
    protected JWindow window;

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
     * Whether popup window should be opaque or not.
     */
    protected boolean opaque;

    /**
     * Whether popup window should follow invoker's window or not.
     */
    protected boolean followInvoker;

    /**
     * Popup window opacity.
     */
    protected float opacity;

    /**
     * Invoker follow adapter.
     */
    protected WindowFollowAdapter followAdapter;

    public WebHeavyWeightPopup ()
    {
        super ();
    }

    public WebHeavyWeightPopup ( final Component component )
    {
        super ( component );
    }

    public WebHeavyWeightPopup ( final Painter painter )
    {
        super ( painter );
    }

    public WebHeavyWeightPopup ( final LayoutManager layout, final Painter painter )
    {
        super ( layout, painter );
    }

    public WebHeavyWeightPopup ( final Painter painter, final Component component )
    {
        super ( painter, component );
    }

    public WebHeavyWeightPopup ( final LayoutManager layout, final Painter painter, final Component... components )
    {
        super ( layout, painter, components );
    }

    public WebHeavyWeightPopup ( final LayoutManager layout )
    {
        super ( layout );
    }

    public WebHeavyWeightPopup ( final LayoutManager layout, final Component... components )
    {
        super ( layout, components );
    }

    public WebHeavyWeightPopup ( final String styleId )
    {
        super ( styleId );
    }

    public WebHeavyWeightPopup ( final String styleId, final LayoutManager layout )
    {
        super ( styleId, layout );
    }

    public WebHeavyWeightPopup ( final String styleId, final Component component )
    {
        super ( styleId, component );
    }

    public boolean isCloseOnOuterAction ()
    {
        return closeOnOuterAction;
    }

    public void setCloseOnOuterAction ( final boolean closeOnOuterAction )
    {
        this.closeOnOuterAction = closeOnOuterAction;
    }

    public Popup getPopup ()
    {
        return popup;
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

    public WebHeavyWeightPopup showPopup ( final Component invoker, final Point location )
    {
        return showPopup ( invoker, location.x, location.y );
    }

    public WebHeavyWeightPopup showPopup ( final Component invoker, final int x, final int y )
    {
        // Saving invoker
        this.invoker = invoker;
        this.invokerWindow = SwingUtils.getWindowAncestor ( invoker );

        // Creating popup
        final Rectangle bos = SwingUtils.getBoundsOnScreen ( invoker );
        this.popup = ProprietaryUtils.createHeavyweightPopup ( invoker, this, bos.x + x, bos.y + y );
        this.window = ( JWindow ) SwingUtils.getWindowAncestor ( this );

        // Modifying opacity if needed
        updateOpaque ();
        updateOpacity ();

        firePopupWillBeOpened ();

        // Displaying popup
        this.popup.show ();

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
                        if ( !isAncestorOf ( component ) )
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

        // Adding follow behavior if needed
        if ( followInvoker )
        {
            installFollowAdapter ();
        }

        firePopupOpened ();

        return this;
    }

    public WebHeavyWeightPopup hidePopup ()
    {
        firePopupWillBeClosed ();

        // Removing follow adapter
        uninstallFollowAdapter ();

        // Removing popup hide event listeners
        Toolkit.getDefaultToolkit ().removeAWTEventListener ( mouseListener );
        mouseListener = null;
        FocusManager.unregisterGlobalFocusListener ( focusListener );
        focusListener = null;

        // Removing follow adapter
        invokerWindow = null;
        invoker = null;

        // Disposing of popup window
        popup.hide ();
        popup = null;
        window = null;

        firePopupClosed ();

        return this;
    }

    public void addPopupListener ( final PopupListener listener )
    {
        listeners.add ( listener );
    }

    public void removePopupListener ( final PopupListener listener )
    {
        listeners.remove ( listener );
    }

    public void firePopupWillBeOpened ()
    {
        for ( final PopupListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.popupWillBeOpened ();
        }
    }

    public void firePopupOpened ()
    {
        for ( final PopupListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.popupOpened ();
        }
    }

    public void firePopupWillBeClosed ()
    {
        for ( final PopupListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.popupWillBeClosed ();
        }
    }

    public void firePopupClosed ()
    {
        for ( final PopupListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.popupClosed ();
        }
    }

    @Override
    public JWindow setWindowOpaque ( final boolean opaque )
    {
        this.opaque = opaque;
        return updateOpaque ();
    }

    protected JWindow updateOpaque ()
    {
        if ( window != null )
        {
            WindowUtils.setWindowOpaque ( window, opaque );
        }
        return window;
    }

    @Override
    public boolean isWindowOpaque ()
    {
        return opaque;
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
            WindowUtils.setWindowOpacity ( window, opacity );
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
            if ( window != null && followAdapter == null )
            {
                // Adding follow adapter
                installFollowAdapter ();
            }
        }
        else
        {
            if ( window != null && followAdapter != null )
            {
                // Removing follow adapter
                uninstallFollowAdapter ();
            }
        }
    }

    protected void installFollowAdapter ()
    {
        followAdapter = WindowFollowAdapter.install ( window, invokerWindow );
    }

    protected void uninstallFollowAdapter ()
    {
        WindowFollowAdapter.uninstall ( invokerWindow, followAdapter );
        followAdapter = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JWindow center ()
    {
        return WindowUtils.center ( window );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JWindow center ( final Component relativeTo )
    {
        return WindowUtils.center ( window, relativeTo );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JWindow center ( final int width, final int height )
    {
        return WindowUtils.center ( window, width, height );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JWindow center ( final Component relativeTo, final int width, final int height )
    {
        return WindowUtils.center ( window, relativeTo, width, height );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JWindow packToWidth ( final int width )
    {
        return WindowUtils.packToWidth ( window, width );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JWindow packToHeight ( final int height )
    {
        return WindowUtils.packToHeight ( window, height );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JWindow packAndCenter ()
    {
        return WindowUtils.packAndCenter ( window );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JWindow packAndCenter ( final boolean animate )
    {
        return WindowUtils.packAndCenter ( window, animate );
    }
}