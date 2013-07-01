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

package com.alee.laf.desktoppane;

import com.alee.laf.StyleConstants;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicDesktopPaneUI;

/**
 * User: mgarin Date: 17.08.11 Time: 23:14
 */

public class WebDesktopPaneUI extends BasicDesktopPaneUI
{
    public static ComponentUI createUI ( JComponent c )
    {
        return new WebDesktopPaneUI ();
    }

    public void installUI ( JComponent c )
    {
        super.installUI ( c );

        // Default settings
        SwingUtils.setOrientation ( c );
        c.setOpaque ( true );
        c.setBackground ( StyleConstants.backgroundColor );
    }

    //    public Dimension getPreferredSize ( JComponent c )
    //    {
    //        Rectangle viewP = desktop.getBounds ();
    //        int maxX = viewP.width + viewP.x, maxY = viewP.height + viewP.y;
    //        int minX = viewP.x, minY = viewP.y;
    //        JInternalFrame f = null;
    //        JInternalFrame[] frames = desktop.getAllFrames ();
    //        for ( JInternalFrame frame : frames )
    //        {
    //            f = frame;
    //            if ( f.getX () < minX )
    //            { // get min X
    //                minX = f.getX ();
    //            }
    //            if ( ( f.getX () + f.getWidth () ) > maxX )
    //            { // get max X
    //                maxX = f.getX () + f.getWidth ();
    //            }
    //            if ( f.getY () < minY )
    //            { // get min Y
    //                minY = f.getY ();
    //            }
    //            if ( ( f.getY () + f.getHeight () ) > maxY )
    //            { // get max Y
    //                maxY = f.getY () + f.getHeight ();
    //            }
    //        }
    //        return new Dimension ( maxX, maxY );
    //    }
}
