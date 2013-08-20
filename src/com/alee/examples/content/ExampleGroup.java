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

import com.alee.laf.separator.WebSeparator;
import com.alee.laf.tabbedpane.WebTabbedPane;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * This interface provides base methods for WebLaF demo example groups managed by ExampleManager.
 *
 * @author Mikle Garin
 */

public interface ExampleGroup
{
    /**
     * Returns example group icon.
     *
     * @return example group icon
     */
    public Icon getGroupIcon ();

    /**
     * Returns example group name.
     *
     * @return example group name
     */
    public String getGroupName ();

    /**
     * Returns short example group description.
     *
     * @return short example group description
     */
    public String getGroupDescription ();

    /**
     * Returns whether this example group contains only one example or not.
     *
     * @return true if this example group contains only one example, false otherwise
     */
    public boolean isSingleExample ();

    /**
     * Returns whether this example group should display a watermark in demo application or not.
     *
     * @return true if this example group should display a watermark in demo application, false otherwise
     */
    public boolean isShowWatermark ();

    /**
     * Returns an enumeration constant that represents average development state of example components in the group.
     *
     * @return average development state of example components in the group
     */
    public FeatureState getFeatureGroupState ();

    /**
     * Returns a list of examples for this example group.
     *
     * @return list of examples
     */
    public List<Example> getGroupExamples ();

    /**
     * Method provided to modify example group tab.
     *
     * @param tabIndex   index of example group tab
     * @param tabbedPane tabbed pane that contains example group tab
     */
    public void modifyExampleTab ( int tabIndex, WebTabbedPane tabbedPane );

    /**
     * Method provided to modify examples separator.
     *
     * @param separator examples separator
     * @return modified examples separator
     */
    public WebSeparator modifySeparator ( WebSeparator separator );

    /**
     * Returns preferred foreground color for example group demo page.
     *
     * @return preferred foreground color for example group demo page
     */
    public Color getPreferredForeground ();

    /**
     * Returns examples content side width relative to the whole available to example group width.
     *
     * @return examples content side width relative to the whole available to example group width
     */
    public double getContentPartSize ();
}