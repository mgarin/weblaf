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

package com.alee.examples.groups.scrollpane;

import com.alee.examples.content.DefaultExampleGroup;
import com.alee.examples.content.Example;
import com.alee.laf.tabbedpane.WebTabbedPane;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * User: mgarin Date: 14.02.12 Time: 13:35
 */

public class ScrollPaneGroup extends DefaultExampleGroup
{
    @Override
    public Icon getGroupIcon ()
    {
        return loadGroupIcon ( "scrollpane.png" );
    }

    @Override
    public String getGroupName ()
    {
        return "Scroll pane";
    }

    @Override
    public String getGroupDescription ()
    {
        return "Scroll pane usage example";
    }

    @Override
    public boolean isSingleExample ()
    {
        return true;
    }

    @Override
    public List<Example> getGroupExamples ()
    {
        return CollectionUtils.copy ( ( Example ) new SimpleScrollPaneExample () );
    }

    @Override
    public void modifyExampleTab ( int tabIndex, WebTabbedPane tabbedPane )
    {
        tabbedPane.setBackgroundAt ( tabIndex, Color.WHITE );
    }
}