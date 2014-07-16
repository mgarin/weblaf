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

package com.alee.extended.button;

import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.menu.PopupMenuWay;
import com.alee.laf.menu.WebPopupMenuUI;
import com.alee.managers.log.Log;
import com.alee.managers.style.SupportedComponent;
import com.alee.utils.ReflectUtils;

import javax.swing.*;
import java.awt.event.*;
import java.io.Serializable;

/**
 * Custom button that displays an additional side (split side) that could call a popup menu.
 * You can specify the displayed menu using setPopupMenu method.
 *
 * @author Mikle Garin
 * @see WebButton
 * @see WebSplitButtonUI
 * @see WebSplitButtonStyle
 * @see #setPopupMenu(javax.swing.JPopupMenu)
 */

public class WebSplitButton extends WebButton implements MouseMotionListener, MouseListener, ActionListener, Serializable
{
    /**
     * Whether should always display popup menu when button is clicked or not.
     * If set to false popup menu will only be displayed when split button part is clicked.
     */
    protected boolean alwaysShowMenu = WebSplitButtonStyle.alwaysShowMenu;

    /**
     * Popup menu display way.
     */
    protected PopupMenuWay popupMenuWay = WebSplitButtonStyle.popupMenuWay;

    /**
     * Split button popup menu.
     */
    protected JPopupMenu popupMenu = null;

    /**
     * Whether mouse is on the split button or not.
     */
    protected boolean onSplit = false;

    /**
     * Constructs new split button.
     */
    public WebSplitButton ()
    {
        super ();
    }

    /**
     * Constructs new split button.
     *
     * @param icon button icon
     */
    public WebSplitButton ( final Icon icon )
    {
        super ( icon );
    }

    /**
     * Constructs new split button.
     *
     * @param text button text
     */
    public WebSplitButton ( final String text )
    {
        super ( text );
    }

    /**
     * Constructs new split button.
     *
     * @param text button text
     * @param icon button icon
     */
    public WebSplitButton ( final String text, final Icon icon )
    {
        super ( text, icon );
    }

    /**
     * Constructs new split button.
     *
     * @param listener button action listener
     */
    public WebSplitButton ( final ActionListener listener )
    {
        super ( listener );
    }

    /**
     * Constructs new split button.
     *
     * @param icon     button icon
     * @param listener button action listener
     */
    public WebSplitButton ( final Icon icon, final ActionListener listener )
    {
        super ( icon, listener );
    }

    /**
     * Constructs new split button.
     *
     * @param text     button text
     * @param listener button action listener
     */
    public WebSplitButton ( final String text, final ActionListener listener )
    {
        super ( text, listener );
    }

    /**
     * Constructs new split button.
     *
     * @param text     button text
     * @param icon     button icon
     * @param listener button action listener
     */
    public WebSplitButton ( final String text, final Icon icon, final ActionListener listener )
    {
        super ( text, icon, listener );
    }

    /**
     * Constructs new split button.
     *
     * @param a button action
     */
    public WebSplitButton ( final Action a )
    {
        super ( a );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void init ( final String text, final Icon icon )
    {
        super.init ( text, icon );

        addMouseMotionListener ( this );
        addMouseListener ( this );
        addActionListener ( this );
    }

    /**
     * Returns the popup menu if set, null otherwise.
     *
     * @return popup menu
     */
    public JPopupMenu getPopupMenu ()
    {
        return popupMenu;
    }

    /**
     * Sets the popup menu to be displayed, when the split part of the button is clicked.
     *
     * @param popupMenu popup menu to be displayed, when the split part of the button is clicked
     */
    public void setPopupMenu ( final JPopupMenu popupMenu )
    {
        this.popupMenu = popupMenu;
    }

    /**
     * Returns whether should always display popup menu when button is clicked or not.
     *
     * @return true if should always display popup menu when button is clicked, false otherwise
     */
    public boolean isAlwaysShowMenu ()
    {
        return alwaysShowMenu;
    }

    /**
     * Sets whether should always display popup menu when button is clicked or not.
     *
     * @param alwaysShowMenu whether should always display popup menu when button is clicked or not
     */
    public void setAlwaysShowMenu ( final boolean alwaysShowMenu )
    {
        this.alwaysShowMenu = alwaysShowMenu;
    }

    /**
     * Returns approximate popup menu display way.
     *
     * @return approximate popup menu display way
     */
    public PopupMenuWay getPopupMenuWay ()
    {
        return popupMenuWay;
    }

    /**
     * Sets approximate popup menu display way.
     * This will have effect only if WebPopupMenuUI is used as popup menu UI.
     * Otherwise this variable will have no effect on popup menu display way.
     *
     * @param way approximate popup menu display way
     */
    public void setPopupMenuWay ( final PopupMenuWay way )
    {
        this.popupMenuWay = way;
    }

    /**
     * Returns split button icon.
     *
     * @return split button icon
     */
    public ImageIcon getSplitIcon ()
    {
        return getWebUI ().getSplitIcon ();
    }

    /**
     * Sets split button icon
     *
     * @param splitIcon new split button icon
     */
    public void setSplitIcon ( final ImageIcon splitIcon )
    {
        getWebUI ().setSplitIcon ( splitIcon );
    }

    /**
     * Returns gap between split icon and split part sides.
     *
     * @return gap between split icon and split part sides
     */
    public int getSplitIconGap ()
    {
        return getWebUI ().getSplitIconGap ();
    }

    /**
     * Sets gap between split icon and split part sides
     *
     * @param splitIconGap gap between split icon and split part sides
     */
    public void setSplitIconGap ( final int splitIconGap )
    {
        getWebUI ().setSplitIconGap ( splitIconGap );
    }

    /**
     * Returns gap between split part and button content.
     *
     * @return gap between split part and button content
     */
    public int getContentGap ()
    {
        return getWebUI ().getContentGap ();
    }

    /**
     * Sets gap between split part and button content.
     *
     * @param contentGap gap between split part and button content
     */
    public void setContentGap ( final int contentGap )
    {
        getWebUI ().setContentGap ( contentGap );
    }

    /**
     * Adds SplitButtonListener to the button.
     *
     * @param listener the SplitButtonListener to be added
     */
    public void addSplitButtonListener ( final SplitButtonListener listener )
    {
        listenerList.add ( SplitButtonListener.class, listener );
    }

    /**
     * Removes SplitButtonListener from the button.
     * If the listener is the currently set Action for the button, then the Action is set to null.
     *
     * @param listener the SplitButtonListener to be removed
     */
    public void removeSplitButtonListener ( final SplitButtonListener listener )
    {
        if ( ( listener != null ) && ( getAction () == listener ) )
        {
            setAction ( null );
        }
        else
        {
            listenerList.remove ( SplitButtonListener.class, listener );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed ( final ActionEvent e )
    {
        if ( popupMenu == null )
        {
            fireButtonClicked ( e );
        }
        else if ( alwaysShowMenu )
        {
            showPopupMenu ();
            fireButtonClicked ( e );
        }
        else if ( onSplit )
        {
            showPopupMenu ();
            fireSplitbuttonClicked ( e );
        }
        else
        {
            fireButtonClicked ( e );
        }
    }

    /**
     * Displays split button popup menu.
     */
    public void showPopupMenu ()
    {
        if ( popupMenu.getUI () instanceof WebPopupMenuUI )
        {
            ( ( WebPopupMenuUI ) popupMenu.getUI () ).setPopupMenuWay ( popupMenuWay );
        }
        if ( getComponentOrientation ().isLeftToRight () )
        {
            popupMenu.show ( this, 0, getHeight () );
        }
        else
        {
            popupMenu.show ( this, getWidth () - popupMenu.getPreferredSize ().width, getHeight () );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseMoved ( final MouseEvent e )
    {
        final boolean wasOnSplit = onSplit;
        onSplit = getWebUI ().getSplitButtonHitbox ( this ).contains ( e.getPoint () );
        if ( wasOnSplit != onSplit )
        {
            repaint ( getWebUI ().getSplitButtonBounds ( this ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseDragged ( final MouseEvent e )
    {
        // Unused listener method
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseClicked ( final MouseEvent e )
    {
        // Unused listener method
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mousePressed ( final MouseEvent e )
    {
        // Unused listener method
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseReleased ( final MouseEvent e )
    {
        // Unused listener method
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseEntered ( final MouseEvent e )
    {
        // Unused listener method
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseExited ( final MouseEvent e )
    {
        final boolean wasOnSplit = onSplit;
        onSplit = false;
        if ( wasOnSplit != onSplit )
        {
            repaint ( getWebUI ().getSplitButtonBounds ( this ) );
        }
    }

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the {@code event}
     * parameter.
     *
     * @param event the {@code ActionEvent} object
     * @see javax.swing.event.EventListenerList
     */
    protected void fireButtonClicked ( final ActionEvent event )
    {
        // Guaranteed to return a non-null array
        final Object[] listeners = listenerList.getListenerList ();
        ActionEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for ( int i = listeners.length - 2; i >= 0; i -= 2 )
        {
            if ( listeners[ i ] == SplitButtonListener.class )
            {
                // Lazily create the event:
                if ( e == null )
                {
                    String actionCommand = event.getActionCommand ();
                    if ( actionCommand == null )
                    {
                        actionCommand = getActionCommand ();
                    }
                    e = new ActionEvent ( WebSplitButton.this, ActionEvent.ACTION_PERFORMED, actionCommand, event.getWhen (),
                            event.getModifiers () );
                }
                ( ( SplitButtonListener ) listeners[ i + 1 ] ).buttonClicked ( e );
            }
        }
    }

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the {@code event}
     * parameter.
     *
     * @param event the {@code ActionEvent} object
     * @see javax.swing.event.EventListenerList
     */
    protected void fireSplitbuttonClicked ( final ActionEvent event )
    {
        // Guaranteed to return a non-null array
        final Object[] listeners = listenerList.getListenerList ();
        ActionEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for ( int i = listeners.length - 2; i >= 0; i -= 2 )
        {
            if ( listeners[ i ] == SplitButtonListener.class )
            {
                // Lazily create the event:
                if ( e == null )
                {
                    String actionCommand = event.getActionCommand ();
                    if ( actionCommand == null )
                    {
                        actionCommand = getActionCommand ();
                    }
                    e = new ActionEvent ( WebSplitButton.this, ActionEvent.ACTION_PERFORMED, actionCommand, event.getWhen (),
                            event.getModifiers () );
                }
                ( ( SplitButtonListener ) listeners[ i + 1 ] ).splitButtonClicked ( e );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebSplitButtonUI getWebUI ()
    {
        return ( WebSplitButtonUI ) getUI ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebSplitButtonUI ) )
        {
            try
            {
                setUI ( ( WebSplitButtonUI ) ReflectUtils.createInstance ( WebLookAndFeel.splitButtonUI ) );
            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebSplitButtonUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUIClassID ()
    {
        return SupportedComponent.splitButton.getUIClassID ();
    }
}