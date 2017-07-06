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

package com.alee.demo.api.example;

import com.alee.demo.DemoApplication;
import com.alee.managers.notification.NotificationManager;
import com.alee.managers.notification.WebInnerNotification;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * @author Mikle Garin
 */

public abstract class AbstractExampleElement implements ExampleElement
{
    /**
     * Parent group reference.
     */
    protected ExampleGroup group;

    @Override
    public String getGroupId ()
    {
        if ( group != null )
        {
            final String groupId = group.getGroupId ();
            return groupId != null ? groupId + "." + group.getId () : group.getId ();
        }
        return null;
    }

    @Override
    public String getTitle ()
    {
        return getExampleLanguagePrefix () + "title";
    }

    /**
     * Returns language prefix for this example element.
     *
     * @return language prefix for this example element
     */
    protected String getExampleLanguagePrefix ()
    {
        final String groupId = getGroupId ();
        return "demo.example." + ( groupId != null ? groupId + "." : "" ) + getId () + ".";
    }

    /**
     * Returns complete language key for this example for the specified key part.
     *
     * @param key language key part
     * @return complete language key for this example for the specified key part
     */
    protected String getExampleLanguageKey ( final String key )
    {
        return getExampleLanguagePrefix () + key;
    }

    /**
     * Sets parent group reference.
     *
     * @param group parent group
     */
    public void setGroup ( final ExampleGroup group )
    {
        this.group = group;
    }

    /**
     * Returns icon retrieved from icons package.
     *
     * @param path icon path
     * @return icon retrieved from icons package
     */
    protected ImageIcon loadIcon ( final String path )
    {
        final String fullPath = "icons/" + path;
        final URL resource = getClass ().getResource ( fullPath );
        if ( resource == null )
        {
            throw new RuntimeException ( "Unable to load image: " + getClass ().getCanonicalName () + " -> " + fullPath );
        }
        return new ImageIcon ( resource );
    }

    /**
     * Returns icon retrieved from example-specific icons package.
     *
     * @param path icon path
     * @return icon retrieved from example-specific icons package
     */
    protected ImageIcon loadExampleIcon ( final String path )
    {
        return loadIcon ( getId () + "/" + path );
    }

    /**
     * Displays event notification and returns it.
     *
     * @param message event message
     * @return displayed event notification
     */
    protected static WebInnerNotification notifyAboutEvent ( final String message )
    {
        return notifyAboutEvent ( DemoApplication.getInstance (), message );
    }

    /**
     * Displays event notification and returns it.
     *
     * @param parent  parent window
     * @param message event message
     * @return displayed event notification
     */
    protected static WebInnerNotification notifyAboutEvent ( final Window parent, final String message )
    {
        final WebInnerNotification notification = NotificationManager.showInnerNotification ( parent, message );
        notification.setDisplayTime ( 2000L );
        return notification;
    }
}