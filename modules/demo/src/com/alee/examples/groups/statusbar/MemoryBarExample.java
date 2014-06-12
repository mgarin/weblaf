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
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.statusbar.WebMemoryBar;

import java.awt.*;

/**
 * User: mgarin Date: 22.06.12 Time: 15:11
 */

public class MemoryBarExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Memory bars";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled memory bars";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Memory bar that displays maximum, allocated and used memory
        WebMemoryBar memoryBar1 = new WebMemoryBar ();
        memoryBar1.setShowMaximumMemory ( true );
        memoryBar1.setPreferredWidth ( memoryBar1.getPreferredSize ().width + 20 );

        // Memory bar that displays allocated and used memory
        WebMemoryBar memoryBar2 = new WebMemoryBar ();
        memoryBar2.setShowMaximumMemory ( false );
        memoryBar2.setPreferredWidth ( memoryBar2.getPreferredSize ().width + 20 );

        // Memory bar without border and background
        WebMemoryBar memoryBar3 = new WebMemoryBar ();
        memoryBar3.setShowMaximumMemory ( false );
        memoryBar3.setDrawBorder ( false );
        memoryBar3.setFillBackground ( false );
        memoryBar3.setPreferredWidth ( memoryBar3.getPreferredSize ().width + 20 );

        return new GroupPanel ( 4, false, memoryBar1, memoryBar2, memoryBar3 );
    }
}