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

package com.alee.examples.groups.table;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.table.WebTable;

import java.awt.*;

/**
 * User: mgarin Date: 31.01.12 Time: 15:05
 */

public class StaticTableExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Static table";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled table without auto-resize";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Table data
        String[] headers = { "Header 1", "Header 2", "Header 3", "Header 4", "Header 5", "Header 6" };
        String[][] data = { { "1", "2", "3", "4", "5", "6" }, { "7", "8", "9", "10", "11", "12" }, { "13", "14", "15", "16", "17", "18" },
                { "19", "20", "21", "22", "23", "24" }, { "25", "26", "27", "28", "29", "30" }, { "31", "32", "33", "34", "35", "36" },
                { "37", "38", "39", "40", "41", "42" }, { "43", "44", "45", "46", "47", "48" }, { "49", "50", "51", "52", "53", "54" } };

        // Static table
        WebTable table = new WebTable ( data, headers );
        table.setEditable ( false );
        table.setAutoResizeMode ( WebTable.AUTO_RESIZE_OFF );
        table.setRowSelectionAllowed ( false );
        table.setColumnSelectionAllowed ( true );
        table.setPreferredScrollableViewportSize ( new Dimension ( 300, 100 ) );
        return new WebScrollPane ( table );
    }
}