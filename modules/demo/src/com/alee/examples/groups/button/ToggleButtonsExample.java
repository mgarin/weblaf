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
import com.alee.laf.button.WebToggleButton;

import java.awt.*;

/**
 * User: mgarin Date: 23.01.12 Time: 15:22
 */

public class ToggleButtonsExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Toggle buttons";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled toggle buttons";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Simple toggle button
        WebToggleButton b = new WebToggleButton ( "Simple" );

        // Iconed toggle button
        WebToggleButton ib = new WebToggleButton ( "Iconed", loadIcon ( "icon.png" ) );

        // Toggle button with transparent icon
        WebToggleButton db = new WebToggleButton ( "Transparent", loadIcon ( "icon.png" ) );
        db.setShadeToggleIcon ( true );

        return new GroupPanel ( 2, b, ib, db );
    }
}