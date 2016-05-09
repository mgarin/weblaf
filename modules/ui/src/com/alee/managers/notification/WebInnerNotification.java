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

package com.alee.managers.notification;

import com.alee.extended.image.WebImage;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.panel.AlignPanel;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.popup.PopupAdapter;
import com.alee.managers.popup.WebInnerPopup;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Custom popup used to display notifications within the application windows.
 *
 * @author Mikle Garin
 * @see com.alee.managers.notification.NotificationManager
 * @see com.alee.managers.notification.NotificationIcon
 * @see com.alee.managers.notification.NotificationOption
 * @see com.alee.managers.popup.WebInnerPopup
 */

public class WebInnerNotification extends WebInnerPopup
{
    /**
     * Notification popup listeners.
     */
    protected List<NotificationListener> listeners = new ArrayList<NotificationListener> ( 1 );

    /**
     * Whether notification can be closed by simple click on popup or not.
     */
    protected boolean clickToClose = true;

    /**
     * Whether notification popup should be closed when some option is selected by user or not.
     * You can disable this and provide your own behavior for options selection through NotificationListener.
     */
    protected boolean closeOnOptionSelection = true;

    /**
     * Whether or not notification option button widths should be equalized.
     */
    protected boolean equalizeButtonWidths = true;

    /**
     * Notification display duration.
     * If set to zero notification will be visible until closed by user.
     */
    protected long displayTime = 0;

    /**
     * Delayed hide timer.
     */
    protected WebTimer displayTimer;

    /**
     * Whether notification is accepted or not.
     */
    protected boolean accepted;

    /**
     * Notification icon.
     */
    protected Icon icon;

    /**
     * Notification content.
     */
    protected Component content;

    /**
     * Notification options.
     */
    protected List<NotificationOption> options;

    /**
     * Notification visual components.
     */
    protected WebImage iconImage;
    protected WebPanel contentPanel;
    protected WebPanel optionsPanel;
    protected AlignPanel westPanel;
    protected AlignPanel centerPanel;
    protected AlignPanel southPanel;

    /**
     * Constructs new notification popup.
     */
    public WebInnerNotification ()
    {
        this ( StyleId.notification );
    }

    /**
     * Constructs new notification popup with the specified style ID.
     *
     * @param id style ID
     */
    public WebInnerNotification ( final StyleId id )
    {
        super ( id );
        initializeNotificationPopup ();
    }

    /**
     * Initializes various notification popup settings.
     */
    protected void initializeNotificationPopup ()
    {
        setLayout ( new BorderLayout ( 15, 5 ) );
        setAnimated ( true );

        iconImage = new WebImage ();
        westPanel = new AlignPanel ( iconImage, SwingConstants.CENTER, SwingConstants.CENTER );
        updateIcon ();

        contentPanel = new WebPanel ( StyleId.panelTransparent );
        centerPanel = new AlignPanel ( contentPanel, SwingConstants.CENTER, SwingConstants.CENTER );
        updateContent ();

        optionsPanel = new WebPanel ( StyleId.panelTransparent, new HorizontalFlowLayout ( 4, false ) );
        southPanel = new AlignPanel ( optionsPanel, SwingConstants.RIGHT, SwingConstants.CENTER );
        updateOptionButtons ();

        addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mousePressed ( final MouseEvent e )
            {
                if ( clickToClose )
                {
                    if ( SwingUtils.isLeftMouseButton ( e ) )
                    {
                        acceptAndHide ();
                    }
                    else
                    {
                        hidePopup ();
                    }
                }
            }
        } );
        addPopupListener ( new PopupAdapter ()
        {
            @Override
            public void popupWillBeOpened ()
            {
                accepted = false;
            }

            @Override
            public void popupOpened ()
            {
                startDelayedClose ();
            }

            @Override
            public void popupWillBeClosed ()
            {
                stopDelayedClose ();
                if ( accepted )
                {
                    fireAccepted ();
                }
                else
                {
                    fireClosed ();
                }
            }
        } );
    }

    /**
     * Starts delayed close timer.
     */
    private void startDelayedClose ()
    {
        if ( displayTime > 0 && ( displayTimer == null || !displayTimer.isRunning () ) )
        {
            displayTimer = WebTimer.delay ( displayTime, new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    hidePopup ();
                }
            } );
        }
    }

    /**
     * Stops delayed close timer.
     */
    private void stopDelayedClose ()
    {
        if ( displayTimer != null && displayTimer.isRunning () )
        {
            displayTimer.stop ();
        }
    }

    /**
     * Returns notification icon.
     *
     * @return notification icon
     */
    public Icon getIcon ()
    {
        return icon;
    }

    /**
     * Sets notification icon.
     *
     * @param icon new notification icon
     */
    public void setIcon ( final Icon icon )
    {
        this.icon = icon;
        updateIcon ();
    }

    /**
     * Sets notification icon type.
     *
     * @param iconType new notification icon type
     */
    public void setIcon ( final NotificationIcon iconType )
    {
        setIcon ( iconType.getIcon () );
    }

    /**
     * Updates visible notification icon.
     */
    protected void updateIcon ()
    {
        iconImage.setIcon ( icon );
        if ( icon != null )
        {
            if ( !contains ( westPanel ) )
            {
                add ( westPanel, BorderLayout.WEST );
            }
        }
        else
        {
            if ( contains ( westPanel ) )
            {
                remove ( westPanel );
            }
        }
        revalidate ();
    }

    /**
     * Returns notification content.
     *
     * @return notification content
     */
    public Component getContent ()
    {
        return content;
    }

    /**
     * Sets notification content.
     *
     * @param content new notification content
     */
    public void setContent ( final Component content )
    {
        this.content = content;
        updateContent ();
    }

    /**
     * Sets notification text content.
     *
     * @param content new notification text content
     */
    public void setContent ( final String content )
    {
        setContent ( new WebLabel ( content ) );
    }

    /**
     * Updates visible notification content.
     */
    protected void updateContent ()
    {
        if ( content != null )
        {
            contentPanel.removeAll ();
            contentPanel.add ( content );
            if ( !contains ( centerPanel ) )
            {
                add ( centerPanel, BorderLayout.CENTER );
            }
        }
        else
        {
            contentPanel.removeAll ();
            if ( contains ( centerPanel ) )
            {
                remove ( centerPanel );
            }
        }
        revalidate ();
    }

    /**
     * Returns notification options list.
     *
     * @return notification options list
     */
    public List<NotificationOption> getOptions ()
    {
        return options;
    }

    /**
     * Sets notification options.
     *
     * @param options new notification options
     */
    public void setOptions ( final NotificationOption... options )
    {
        setOptions ( Arrays.asList ( options ) );
    }

    /**
     * Sets notification options list.
     *
     * @param options new notification options list
     */
    public void setOptions ( final List<NotificationOption> options )
    {
        this.options = options;
        updateOptionButtons ();
    }

    /**
     * Updates visible notification options.
     */
    protected void updateOptionButtons ()
    {
        optionsPanel.removeAll ();
        if ( !CollectionUtils.isEmpty ( options ) )
        {
            for ( final NotificationOption option : options )
            {
                final StyleId id = StyleId.notificationOption.at ( WebInnerNotification.this );
                final WebButton optionButton = new WebButton ( id, option.getLanguageKey (), new ActionListener ()
                {
                    @Override
                    public void actionPerformed ( final ActionEvent e )
                    {
                        fireOptionSelected ( option );
                        if ( closeOnOptionSelection )
                        {
                            acceptAndHide ();
                        }
                    }
                } );
                optionsPanel.add ( optionButton );
            }
            if ( equalizeButtonWidths )
            {
                final List<String> properties = Arrays.asList ( AbstractButton.TEXT_CHANGED_PROPERTY );
                SwingUtils.equalizeComponentsWidth ( properties, optionsPanel.getComponents () );
            }
            if ( !contains ( southPanel ) )
            {
                add ( southPanel, BorderLayout.SOUTH );
            }
        }
        else
        {
            if ( contains ( southPanel ) )
            {
                remove ( southPanel );
            }
        }
        revalidate ();
    }

    /**
     * Accepts notification and closes popup.
     */
    public void acceptAndHide ()
    {
        accepted = true;
        hidePopup ();
    }

    /**
     * Returns whether notification can be closed by simple click on popup or not.
     *
     * @return true if notification can be closed by simple click on popup, false otherwise
     */
    public boolean isClickToClose ()
    {
        return clickToClose;
    }

    /**
     * Sets whether notification can be closed by simple click on popup or not.
     *
     * @param clickToClose whether notification can be closed by simple click on popup or not
     */
    public void setClickToClose ( final boolean clickToClose )
    {
        this.clickToClose = clickToClose;
    }

    /**
     * Whether notification popup should be closed when some option is selected by user or not.
     *
     * @return true if notification popup should be closed when some option is selected by user, false otherwise
     */
    public boolean isCloseOnOptionSelection ()
    {
        return closeOnOptionSelection;
    }

    /**
     * Returns whether notification popup should be closed when some option is selected by user or not.
     *
     * @param closeOnOptionSelection whether notification popup should be closed when some option is selected by user or not
     */
    public void setCloseOnOptionSelection ( final boolean closeOnOptionSelection )
    {
        this.closeOnOptionSelection = closeOnOptionSelection;
    }

    /**
     * Returns whether or not notification option button widths should be equalized.
     *
     * @return true if notification option button widths should be equalized, false otherwise
     */
    public boolean isEqualizeButtonWidths ()
    {
        return equalizeButtonWidths;
    }

    /**
     * Sets whether or not notification option button widths should be equalized.
     *
     * @param equalizeButtonWidths whether or not notification option button widths should be equalized
     */
    public void setEqualizeButtonWidths ( final boolean equalizeButtonWidths )
    {
        this.equalizeButtonWidths = equalizeButtonWidths;
        updateOptionButtons ();
    }

    /**
     * Returns notification display time.
     *
     * @return notification display time
     */
    public long getDisplayTime ()
    {
        return displayTime;
    }

    /**
     * Sets notification display time.
     *
     * @param displayTime new notification display time
     */
    public void setDisplayTime ( final long displayTime )
    {
        this.displayTime = displayTime;
        if ( isShowing () )
        {
            if ( displayTime > 0 )
            {
                startDelayedClose ();
            }
            else
            {
                stopDelayedClose ();
            }
        }
    }

    /**
     * Adds directory chooser listener.
     *
     * @param listener directory chooser listener to add
     */
    public void addNotificationListener ( final NotificationListener listener )
    {
        listeners.add ( listener );
    }

    /**
     * Removes directory chooser listener.
     *
     * @param listener directory chooser listener to remove
     */
    public void removeNotificationListener ( final NotificationListener listener )
    {
        listeners.remove ( listener );
    }

    /**
     * Fires when notification options is selected.
     *
     * @param option selected option
     */
    public void fireOptionSelected ( final NotificationOption option )
    {
        for ( final NotificationListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.optionSelected ( option );
        }
    }

    /**
     * Fires when notification accepted and closed.
     */
    public void fireAccepted ()
    {
        for ( final NotificationListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.accepted ();
        }
    }

    /**
     * Fires when notification simply closed.
     */
    public void fireClosed ()
    {
        for ( final NotificationListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.closed ();
        }
    }
}