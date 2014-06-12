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

import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.laf.panel.WebPanel;

import java.awt.*;

/**
 * User: mgarin Date: 28.09.12 Time: 16:31
 */

public class VerticalPanel extends WebPanel
{
    public VerticalPanel ( Component... components )
    {
        this ( true, components );
    }

    public VerticalPanel ( int gap, Component... components )
    {
        this ( gap, true, components );
    }

    public VerticalPanel ( boolean fill, Component... components )
    {
        this ( 0, fill, components );
    }

    public VerticalPanel ( int gap, boolean fill, Component... components )
    {
        super ( new VerticalFlowLayout ( VerticalFlowLayout.TOP, gap, gap, fill, false ) );

        for ( Component component : components )
        {
            add ( component );
        }
    }
}