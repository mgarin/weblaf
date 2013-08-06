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

package com.alee.laf.menu;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;

/**
 * User: mgarin Date: 17.08.11 Time: 22:55
 */

public class WebRadioButtonMenuItemUI extends WebMenuItemUI
{
    public static ComponentUI createUI ( JComponent c )
    {
        return new WebRadioButtonMenuItemUI ();
    }

    //    public void processMouseEvent ( JMenuItem item, MouseEvent e, MenuElement path[],
    //                                    MenuSelectionManager manager )
    //    {
    //        Point p = e.getPoint ();
    //        if ( p.x >= 0 && p.x < item.getWidth () && p.y >= 0 && p.y < item.getHeight () )
    //        {
    //            if ( e.getID () == MouseEvent.MOUSE_RELEASED )
    //            {
    //                manager.clearSelectedPath ();
    //                item.doClick ( 0 );
    //                item.setArmed ( false );
    //            }
    //            else
    //            {
    //                manager.setSelectedPath ( path );
    //            }
    //        }
    //        else if ( item.getModel ().isArmed () )
    //        {
    //            MenuElement newPath[] = new MenuElement[ path.length - 1 ];
    //            int i, c;
    //            for ( i = 0, c = path.length - 1; i < c; i++ )
    //            {
    //                newPath[ i ] = path[ i ];
    //            }
    //            manager.setSelectedPath ( newPath );
    //        }
    //    }
}
