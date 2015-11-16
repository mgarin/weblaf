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

package com.alee.demo.api;

import javax.swing.*;
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
}