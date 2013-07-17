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

package com.alee.examples.groups.button;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.checkbox.WebCheckBox;

import java.awt.*;

/**
 * User: mgarin Date: 23.01.12 Time: 16:39
 */

public class CheckBoxExample extends DefaultExample
{
    public String getTitle ()
    {
        return "Checkboxes";
    }

    public String getDescription ()
    {
        return "Web-styled checkboxes";
    }

    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Selected check box
        WebCheckBox cb1 = new WebCheckBox ( "Checkbox 1" );
        cb1.setSelected ( true );

        // Simple check box
        WebCheckBox cb2 = new WebCheckBox ( "Checkbox 2" );

        return new GroupPanel ( 4, false, cb1, cb2 );
    }
}