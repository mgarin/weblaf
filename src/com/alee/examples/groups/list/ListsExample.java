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

package com.alee.examples.groups.list;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.list.WebList;
import com.alee.laf.scroll.WebScrollPane;

import java.awt.*;

/**
 * User: mgarin Date: 17.02.12 Time: 13:05
 */

public class ListsExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Editable list";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled editable list";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Editable list
        WebList editableList = new WebList ( createSampleData () );
        editableList.setVisibleRowCount ( 4 );
        editableList.setSelectedIndex ( 0 );
        editableList.setEditable ( true );
        return new GroupPanel ( new WebScrollPane ( editableList ) );
    }

    private static String[] createSampleData ()
    {
        return new String[]{ "Editable element 1", "Editable element 2", "Editable element 3", "Editable element 4", "Editable element 5",
                "Editable element 6" };
    }
}