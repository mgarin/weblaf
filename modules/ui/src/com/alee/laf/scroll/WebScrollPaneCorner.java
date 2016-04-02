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

package com.alee.laf.scroll;

import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;

/**
 * @author Mikle Garin
 */

public class WebScrollPaneCorner extends JComponent
{
    /**
     * todo 1. Create UI for this corner component
     * todo 2. Create custom painter for corners
     */

    protected Color borderColor;

    private final String corner;

    public WebScrollPaneCorner ( final String corner )
    {
        super ();
        this.corner = corner;
        SwingUtils.setOrientation ( this );
    }

    public Color getBorderColor ()
    {
        return borderColor;
    }

    public void setBorderColor ( Color borderColor )
    {
        this.borderColor = borderColor;
    }

    @Override
    protected void paintComponent ( final Graphics g )
    {
        super.paintComponent ( g );

        final boolean ltr = getComponentOrientation ().isLeftToRight ();
        final Color backgroundColor = getBackground ();
        if ( corner.equals ( JScrollPane.LOWER_LEADING_CORNER ) )
        {
            final int vBorder = ltr ? getWidth () - 1 : 0;
            g.setColor ( backgroundColor );
            g.fillRect ( 0, 0, getWidth (), getHeight () );
            g.setColor ( borderColor );
            g.drawLine ( 0, 0, getWidth () - 1, 0 );
            g.drawLine ( vBorder, 0, vBorder, getHeight () - 1 );
        }
        else if ( corner.equals ( JScrollPane.LOWER_TRAILING_CORNER ) )
        {
            final int vBorder = ltr ? 0 : getWidth () - 1;
            g.setColor ( backgroundColor );
            g.fillRect ( 0, 0, getWidth (), getHeight () );
            g.setColor ( borderColor );
            g.drawLine ( 0, 0, getWidth () - 1, 0 );
            g.drawLine ( vBorder, 0, vBorder, getHeight () - 1 );
        }
        else if ( corner.equals ( JScrollPane.UPPER_TRAILING_CORNER ) )
        {
            final int vBorder = ltr ? 0 : getWidth () - 1;
            g.setColor ( backgroundColor );
            g.fillRect ( 0, 0, getWidth (), getHeight () );
            g.setColor ( borderColor );
            g.drawLine ( 0, getHeight () - 1, getWidth () - 1, getHeight () - 1 );
            g.drawLine ( vBorder, 0, vBorder, getHeight () - 1 );
        }
    }

    @Override
    public Dimension getPreferredSize ()
    {
        // We don't want corners to force scroll pane size changes
        // By default this value is not even queued though
        return new Dimension ( 0, 0 );
    }
}