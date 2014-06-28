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

package com.alee.examples.groups.combobox;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.combobox.WebComboBox;

import java.awt.*;

/**
 * User: mgarin Date: 14.02.12 Time: 14:21
 */

public class EditableComboBoxExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Editable combobox";
    }

    @Override
    public String getDescription ()
    {
        return "Editable Web-styled combobox";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        String[] items = { "Edit me 1", "Edit me 2", "Edit me 3", "Edit me 4", "Edit me 5" };
        WebComboBox comboBox = new WebComboBox ( items );
        comboBox.setEditable ( true );
        comboBox.setEditorColumns ( 6 );
        return new GroupPanel ( comboBox );
    }
}