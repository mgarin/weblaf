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
import com.alee.laf.button.WebButton;

import java.awt.*;

/**
 * User: mgarin Date: 23.01.12 Time: 12:09
 */

public class ButtonsExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Buttons";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled buttons";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Simple button
        WebButton b = new WebButton ( "Simple" );

        // Disabled button
        WebButton db = new WebButton ( "Iconed", loadIcon ( "icon.png" ) );

        return new GroupPanel ( 2, new GroupPanel ( b ), db );
    }
}