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
import com.alee.laf.menu.MenubarStyle;
import com.alee.laf.menu.WebMenuBar;

import java.awt.*;

/**
 * User: mgarin Date: 24.01.12 Time: 14:55
 */

public class StandaloneMenuBarExample extends DefaultMenuBarExample
{
    public String getTitle ()
    {
        return "Standalone menu bar";
    }

    public String getDescription ()
    {
        return "Standalone-styled menu bar";
    }

    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        WebMenuBar menuBar = new WebMenuBar ();
        menuBar.setMenubarStyle ( MenubarStyle.standalone );
        setupMenuBar ( menuBar );
        return menuBar;
    }
}