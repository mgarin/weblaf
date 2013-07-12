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
import com.alee.laf.radiobutton.WebRadioButton;
import com.alee.utils.swing.UnselectableButtonGroup;

import java.awt.*;

/**
 * User: mgarin Date: 23.01.12 Time: 16:45
 */

public class RadioButtonExample extends DefaultExample
{
    public String getTitle ()
    {
        return "Radiobuttons";
    }

    public String getDescription ()
    {
        return "Web-styled radiobuttons";
    }

    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Selected radio button
        WebRadioButton rb1 = new WebRadioButton ( "Radiobutton 1" );
        rb1.setSelected ( true );

        // Simple radio button
        WebRadioButton rb2 = new WebRadioButton ( "Radiobutton 2" );

        // Grouping buttons with custom button group that allows deselection
        UnselectableButtonGroup.group ( rb1, rb2 );

        return new GroupPanel ( 4, false, rb1, rb2 );
    }
}