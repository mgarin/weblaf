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

package com.alee.examples.groups.menubar;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.laf.menu.WebMenuBar;

import java.awt.*;

/**
 * User: mgarin Date: 24.01.12 Time: 14:50
 */

public class MenuBarExample extends DefaultMenuBarExample
{
    @Override
    public String getTitle ()
    {
        return "Menu bar";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled menu bar";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        WebMenuBar menuBar = new WebMenuBar ();
        setupMenuBar ( menuBar );
        return menuBar;
    }
}