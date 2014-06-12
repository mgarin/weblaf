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

package com.alee.examples.content;

import com.alee.extended.icon.OrientedIcon;
import com.alee.laf.separator.WebSeparator;
import com.alee.laf.tabbedpane.WebTabbedPane;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * This abstract class provides additional methods for WebLaF demo example groups.
 *
 * @author Mikle Garin
 */

public abstract class DefaultExampleGroup implements ExampleGroup
{
    /**
     * Returns false by default.
     *
     * @return false
     */
    @Override
    public boolean isSingleExample ()
    {
        return false;
    }

    /**
     * Returns true by default.
     *
     * @return true
     */
    @Override
    public boolean isShowWatermark ()
    {
        return true;
    }

    /**
     * Returns release feature state by default.
     *
     * @return release feature state
     */
    @Override
    public FeatureState getFeatureGroupState ()
    {
        return FeatureState.release;
    }

    /**
     * Does not modify example group tab by default.
     *
     * @param tabIndex   index of example group tab
     * @param tabbedPane tabbed pane that contains example group tab
     */
    @Override
    public void modifyExampleTab ( int tabIndex, WebTabbedPane tabbedPane )
    {
        //
    }

    /**
     * Does not modify example separator by default.
     *
     * @param separator examples separator
     * @return examples separator
     */
    @Override
    public WebSeparator modifySeparator ( WebSeparator separator )
    {
        return separator;
    }

    /**
     * Returns black foreground by default.
     *
     * @return black foreground
     */
    @Override
    public Color getPreferredForeground ()
    {
        return Color.BLACK;
    }

    /**
     * Returns 50% content width by default.
     *
     * @return 50% content width
     */
    @Override
    public double getContentPartSize ()
    {
        return 0.5f;
    }

    /**
     * Returns group image icon loaded from group icons package.
     *
     * @param path path to icon inside group icons package
     * @return group image icon
     */
    public ImageIcon loadGroupIcon ( String path )
    {
        return new OrientedIcon ( getIconResource ( path ) );
    }

    /**
     * Returns image icon loaded from group icons package.
     *
     * @param path path to icon inside group icons package
     * @return image icon
     */
    public ImageIcon loadIcon ( String path )
    {
        return new ImageIcon ( getIconResource ( path ) );
    }

    /**
     * Returns icon url.
     *
     * @param path path to icon inside group icons package
     * @return icon url
     */
    private URL getIconResource ( String path )
    {
        return getClass ().getResource ( "icons/" + path );
    }

    /**
     * Returns resource url.
     *
     * @param path path to resource inside group resources package
     * @return resource url
     */
    public URL getResource ( String path )
    {
        return getClass ().getResource ( "resources/" + path );
    }
}