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

package com.alee.laf.toolbar;

import com.alee.extended.layout.ToolbarLayout;
import com.alee.laf.StyleConstants;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 17.01.12 Time: 17:17
 */

public class WhiteSpace extends JComponent implements SwingConstants
{
    private int spacing;
    private int orientation;

    public WhiteSpace ()
    {
        this ( StyleConstants.contentSpacing );
    }

    public WhiteSpace ( int spacing )
    {
        this ( spacing, -1 );
    }

    public WhiteSpace ( int spacing, int orientation )
    {
        super ();
        SwingUtils.setOrientation ( this );
        setSpacing ( spacing );
        setOrientation ( orientation );
    }

    public int getOrientation ()
    {
        return orientation;
    }

    public void setOrientation ( int orientation )
    {
        this.orientation = orientation;
    }

    public int getSpacing ()
    {
        return spacing;
    }

    public void setSpacing ( int spacing )
    {
        this.spacing = spacing;
    }

    @Override
    public Dimension getPreferredSize ()
    {
        Container container = getParent ();
        if ( container != null && container.getLayout () instanceof ToolbarLayout )
        {
            ToolbarLayout layout = ( ToolbarLayout ) container.getLayout ();
            return new Dimension ( layout.getOrientation () != VERTICAL ? spacing : 0,
                    layout.getOrientation () != HORIZONTAL ? spacing : 0 );
        }
        else
        {
            return new Dimension ( orientation != VERTICAL ? spacing : 0, orientation != HORIZONTAL ? spacing : 0 );
        }
    }
}
