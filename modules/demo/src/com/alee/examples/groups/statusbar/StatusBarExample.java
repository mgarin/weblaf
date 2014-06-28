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

package com.alee.examples.groups.statusbar;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.layout.ToolbarLayout;
import com.alee.extended.statusbar.WebMemoryBar;
import com.alee.extended.statusbar.WebStatusBar;
import com.alee.extended.statusbar.WebStatusLabel;

import java.awt.*;

/**
 * User: mgarin Date: 24.01.12 Time: 15:05
 */

public class StatusBarExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Status bar";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled status bar";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Simple status bar
        WebStatusBar statusBar = new WebStatusBar ();

        // Simple label
        statusBar.add ( new WebStatusLabel ( "Just a simple status bar", loadIcon ( "info.png" ) ) );

        // Simple memory bar
        WebMemoryBar memoryBar = new WebMemoryBar ();
        memoryBar.setPreferredWidth ( memoryBar.getPreferredSize ().width + 20 );
        statusBar.add ( memoryBar, ToolbarLayout.END );

        return statusBar;
    }
}