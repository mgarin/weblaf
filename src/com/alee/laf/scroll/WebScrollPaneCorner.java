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
 * User: mgarin Date: 06.05.11 Time: 17:55
 */

public class WebScrollPaneCorner extends JComponent
{
    // todo Move all paintings into a separate UI

    public WebScrollPaneCorner ()
    {
        super ();
        SwingUtils.setOrientation ( this );
    }

    @Override
    protected void paintComponent ( final Graphics g )
    {
        super.paintComponent ( g );

        final int vBorder = getComponentOrientation ().isLeftToRight () ? 0 : getWidth () - 1;
        g.setColor ( WebScrollBarStyle.trackBackgroundColor );
        g.fillRect ( 0, 0, getWidth (), getHeight () );
        g.setColor ( WebScrollBarStyle.trackBorderColor );
        g.drawLine ( 0, 0, getWidth () - 1, 0 );
        g.drawLine ( vBorder, 0, vBorder, getHeight () - 1 );
    }
}