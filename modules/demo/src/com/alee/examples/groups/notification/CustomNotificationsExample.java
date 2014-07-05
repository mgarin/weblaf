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

package com.alee.examples.groups.notification;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.panel.CenterPanel;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.time.ClockType;
import com.alee.extended.time.WebClock;
import com.alee.laf.button.WebButton;
import com.alee.managers.notification.NotificationIcon;
import com.alee.managers.notification.NotificationManager;
import com.alee.managers.notification.WebNotification;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Custom notifications example.
 *
 * @author Mikle Garin
 */

public class CustomNotificationsExample extends DefaultExample
{
    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle ()
    {
        return "Custom notifications";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription ()
    {
        return "Web-styled custom notifications";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        final WebButton notification1 = new WebButton ( "Simple custom content" );
        notification1.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                final WebNotification notificationPopup = new WebNotification ();
                notificationPopup.setIcon ( NotificationIcon.question );

                final JLabel label = new JLabel ( "Are you sure you want to" );
                final WebButton button = new WebButton ( "discard changes?" );
                button.setRolloverDecoratedOnly ( true );
                button.setDrawFocus ( false );
                button.setLeftRightSpacing ( 0 );
                button.setBoldFont ();
                button.addActionListener ( new ActionListener ()
                {
                    /**
                     * {@inheritDoc}
                     */
                    @Override
                    public void actionPerformed ( final ActionEvent e )
                    {
                        notificationPopup.hidePopup ();
                    }
                } );
                final CenterPanel centerPanel = new CenterPanel ( button, false, true );
                notificationPopup.setContent ( new GroupPanel ( 2, label, centerPanel ) );

                NotificationManager.showNotification ( notificationPopup );
            }
        } );

        final WebButton notification2 = new WebButton ( "Limited duration notification" );
        notification2.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                final WebNotification notificationPopup = new WebNotification ();
                notificationPopup.setIcon ( NotificationIcon.clock );
                notificationPopup.setDisplayTime ( 5000 );

                final WebClock clock = new WebClock ();
                clock.setClockType ( ClockType.timer );
                clock.setTimeLeft ( 6000 );
                clock.setTimePattern ( "'This notification will close in' ss 'seconds'" );
                notificationPopup.setContent ( new GroupPanel ( clock ) );

                NotificationManager.showNotification ( notificationPopup );
                clock.start ();
            }
        } );

        return new GroupPanel ( 4, false, notification1, notification2 );
    }
}