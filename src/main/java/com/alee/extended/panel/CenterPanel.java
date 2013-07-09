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
import info.clearthought.layout.TableLayout;

import java.awt.*;

/**
 * User: mgarin Date: 01.07.11 Time: 13:24
 */

public class CenterPanel extends WebPanel
{
    public CenterPanel ( Component component )
    {
        this ( component, true, true );
    }

    public CenterPanel ( Component component, boolean centerHor, boolean centerVer )
    {
        super ();
        setOpaque ( false );

        if ( centerHor && centerVer )
        {
            setLayout ( new TableLayout ( new double[][]{ { TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL },
                    { TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL } } ) );
            add ( component, "1,1" );
        }
        else if ( centerHor )
        {
            setLayout ( new TableLayout (
                    new double[][]{ { TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL }, { TableLayout.PREFERRED } } ) );
            add ( component, "1,0" );
        }
        else if ( centerVer )
        {
            setLayout ( new TableLayout (
                    new double[][]{ { TableLayout.PREFERRED }, { TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL } } ) );
            add ( component, "0,1" );
        }
        else
        {
            setLayout ( new BorderLayout ( 0, 0 ) );
            add ( component, BorderLayout.CENTER );
        }
    }
}
