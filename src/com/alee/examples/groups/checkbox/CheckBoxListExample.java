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

package com.alee.examples.groups.checkbox;

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
     * {@inheritDoc}
     */
    @Override
    public String getTitle ()
    {
        return "Checkbox list";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription ()
    {
        return "Web-styled checkbox list";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        // Checkbox list
        final WebCheckBoxList webCheckBoxList = new WebCheckBoxList ( createCheckBoxListModel () );
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
        final CheckBoxListModel model = new CheckBoxListModel ();
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