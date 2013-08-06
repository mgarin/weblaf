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

package com.alee.extended.panel;

import com.alee.laf.panel.WebPanel;

import java.awt.*;

/**
 * User: mgarin Date: 30.05.12 Time: 15:23
 */

public class GridPanel extends WebPanel
{
    public GridPanel ( Component... components )
    {
        this ( 1, 0, components );
    }

    public GridPanel ( int rows, int cols, Component... components )
    {
        this ( rows, cols, 0, components );
    }

    public GridPanel ( int gap, Component... components )
    {
        this ( 1, 0, gap, components );
    }

    public GridPanel ( int rows, int cols, int gap, Component... components )
    {
        this ( rows, cols, gap, gap, components );
    }

    public GridPanel ( int rows, int cols, int hgap, int vgap, Component... components )
    {
        super ();

        setOpaque ( false );
        setLayout ( new GridLayout ( rows, cols, hgap, vgap ) );

        for ( Component component : components )
        {
            add ( component );
        }
    }
}