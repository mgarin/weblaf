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
 * @author Mikle Garin
 */

public class FlowPanel extends WebPanel
{

    public FlowPanel ( final Component... components )
    {
        this ( 0, components );
    }

    public FlowPanel ( final int gap, final Component... components )
    {
        this ( FlowLayout.LEADING, gap, components );
    }

    public FlowPanel ( final int alignment, final int gap, final Component... components )
    {
        super ( new FlowLayout ( alignment, gap, 0 ) );
        if ( components != null )
        {
            for ( final Component component : components )
            {
                add ( component );
            }
        }
    }
}