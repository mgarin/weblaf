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

import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;

/**
 * This class provides a set of utilities for menu elements.
 * This is a library utility class and its not intended for use outside of the menu elements.
 *
 * @author Mikle Garin
 */

public final class MenuUtils implements SwingConstants
{
    /**
     * Returns maximum icon width for this menu item.
     * It might take into account other menu items within popup menu.
     *
     * @param menuItem             menu item to process
     * @param alignTextToMenuIcons whether menu item text should be aligned to icons or not
     * @return maximum icon width for this menu item
     */
    public static int getIconPlaceholderWidth ( final JMenuItem menuItem, final boolean alignTextToMenuIcons )
    {
        if ( alignTextToMenuIcons && menuItem.getParent () instanceof JPopupMenu )
        {
            int max = 0;
            final JPopupMenu popupMenu = ( JPopupMenu ) menuItem.getParent ();
            for ( int i = 0; i < popupMenu.getComponentCount (); i++ )
            {
                final Component component = popupMenu.getComponent ( i );
                if ( component instanceof JMenuItem )
                {
                    final JMenuItem otherItem = ( JMenuItem ) component;
                    if ( otherItem.getIcon () != null )
                    {
                        max = Math.max ( max, otherItem.getIcon ().getIconWidth () );
                    }
                    else if ( component instanceof JCheckBoxMenuItem || component instanceof JRadioButtonMenuItem )
                    {
                        max = Math.max ( max, 16 );
                    }
                }
            }
            return max;
        }
        else
        {
            final Icon icon = menuItem.getIcon ();
            return icon != null ? icon.getIconWidth () : 0;
        }
    }

    /**
     * Returns menu item accelerator text.
     *
     * @param menuItem menu item to process
     * @return menu item accelerator text
     */
    public static String getAcceleratorText ( final JMenuItem menuItem )
    {
        final KeyStroke accelerator = menuItem.getAccelerator ();
        return accelerator != null ? SwingUtils.hotkeyToString ( accelerator ) : null;
    }
}