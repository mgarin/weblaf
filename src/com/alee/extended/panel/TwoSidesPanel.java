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

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 24.02.12 Time: 16:18
 */

public class TwoSidesPanel extends WebPanel implements SwingConstants
{
    public TwoSidesPanel ( Component start, Component end )
    {
        this ( HORIZONTAL, start, end );
    }

    public TwoSidesPanel ( int orientation, Component start, Component end )
    {
        this ( orientation, 0, start, end );
    }

    public TwoSidesPanel ( int orientation, int spacing, Component start, Component end )
    {
        super ();

        setOpaque ( false );
        setLayout ( new BorderLayout ( spacing, spacing ) );

        if ( orientation == HORIZONTAL )
        {
            add ( start, BorderLayout.LINE_START );
            add ( end, BorderLayout.LINE_END );
        }
        else
        {
            add ( start, BorderLayout.PAGE_START );
            add ( end, BorderLayout.PAGE_END );
        }
    }
}