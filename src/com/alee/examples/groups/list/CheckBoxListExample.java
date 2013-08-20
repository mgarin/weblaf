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
import com.alee.extended.list.CheckBoxListModel;
import com.alee.extended.list.WebCheckBoxList;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.scroll.WebScrollPane;

import java.awt.*;

/**
 * CheckBox list example.
 *
 * @author Mikle Garin
 */

public class CheckBoxListExample extends DefaultExample
{
    /**
     * Returns example title.
     *
     * @return example title
     */
    public String getTitle ()
    {
        return "Checkbox list";
    }

    /**
     * Returns short example description.
     *
     * @return short example description
     */
    public String getDescription ()
    {
        return "Web-styled checkbox list";
    }

    /**
     * Returns preview component for this example.
     *
     * @param owner demo application main frame
     * @return preview component
     */
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Checkbox list
        WebCheckBoxList webCheckBoxList = new WebCheckBoxList ( createCheckBoxListModel () );
        webCheckBoxList.setVisibleRowCount ( 4 );
        webCheckBoxList.setSelectedIndex ( 0 );
        webCheckBoxList.setEditable ( true );
        return new GroupPanel ( new WebScrollPane ( webCheckBoxList ) );
    }

    /**
     * Returns demo checkbox list model.
     *
     * @return demo checkbox list model
     */
    private CheckBoxListModel createCheckBoxListModel ()
    {
        CheckBoxListModel model = new CheckBoxListModel ();
        model.addCheckBoxElement ( "Element 1", true );
        model.addCheckBoxElement ( "Element 2" );
        model.addCheckBoxElement ( "Element 3" );
        model.addCheckBoxElement ( "Some other text" );
        model.addCheckBoxElement ( "One more line", true );
        model.addCheckBoxElement ( "And one more", true );
        model.addCheckBoxElement ( "Last one" );
        return model;
    }
}