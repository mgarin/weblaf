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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Special listener for {@link JMenuItem} that performs popup menu decoration updates.
 * It uses {@link ChangeListener} to track menu selection changes and {@link PropertyChangeListener} to track menu item model changes.
 *
 * @author Mikle Garin
 */

public class MenuItemChangeListener implements ChangeListener, PropertyChangeListener
{
    /**
     * {@link JMenuItem} to listen events for.
     */
    protected JMenuItem menuItem;

    /**
     * Installs menu item model change listener and returns it.
     *
     * @param menuItem menu item to install listener into
     * @return installed model change listener
     */
    public static MenuItemChangeListener install ( final JMenuItem menuItem )
    {
        final MenuItemChangeListener listener = new MenuItemChangeListener ( menuItem );
        menuItem.getModel ().addChangeListener ( listener );
        menuItem.addPropertyChangeListener ( AbstractButton.MODEL_CHANGED_PROPERTY, listener );
        return listener;
    }

    /**
     * Uninstalls menu item model change listener from specified menu item.
     *
     * @param listener listener to uninstall
     * @param menuItem menu item to uninstall listener from
     * @return {@code null} for convenience reasons
     */
    public static MenuItemChangeListener uninstall ( final MenuItemChangeListener listener, final JMenuItem menuItem )
    {
        menuItem.removePropertyChangeListener ( AbstractButton.MODEL_CHANGED_PROPERTY, listener );
        menuItem.getModel ().removeChangeListener ( listener );
        return null;
    }

    /**
     * Constructs new menu item change listener.
     *
     * @param menuItem menu item to listen
     */
    public MenuItemChangeListener ( final JMenuItem menuItem )
    {
        super ();
        this.menuItem = menuItem;
    }

    @Override
    public void propertyChange ( final PropertyChangeEvent evt )
    {
        // Switching listener to new menu item model on its change
        ( ( ButtonModel ) evt.getOldValue () ).removeChangeListener ( this );
        ( ( ButtonModel ) evt.getNewValue () ).addChangeListener ( this );
    }

    @Override
    public void stateChanged ( final ChangeEvent e )
    {
        // Checking whether we have a JPopupMenu parent
        final Container parent = menuItem.getParent ();
        if ( parent instanceof JPopupMenu )
        {
            // Checking whether WebPopupMenuUI is used or not
            final JPopupMenu popupMenu = ( JPopupMenu ) parent;
            if ( popupMenu.getUI () instanceof WebPopupMenuUI )
            {
                // Checking whether PopupMenuPainter is used or not
                final WebPopupMenuUI ui = ( WebPopupMenuUI ) popupMenu.getUI ();
                if ( ui.getPainter () instanceof PopupMenuPainter )
                {
                    // Checking painter sttyle settings
                    final PopupMenuPainter webPainter = ( PopupMenuPainter ) ui.getPainter ();
                    if ( webPainter.getPopupStyle () == PopupStyle.dropdown )
                    {
                        // Checking whether or not this item state change affects the corner
                        final int zOrder = popupMenu.getComponentZOrder ( menuItem );
                        if ( webPainter.getCornerSide () == SwingConstants.NORTH && zOrder == 0 )
                        {
                            // Repainting only corner bounds
                            popupMenu.repaint ( 0, 0, popupMenu.getWidth (), menuItem.getBounds ().y );
                        }
                        else if ( webPainter.getCornerSide () == SwingConstants.SOUTH && zOrder == popupMenu.getComponentCount () - 1 )
                        {
                            // Repainting only corner bounds
                            final Rectangle itemBounds = menuItem.getBounds ();
                            final int y = itemBounds.y + itemBounds.height;
                            popupMenu.repaint ( 0, y, popupMenu.getWidth (), popupMenu.getHeight () - y );
                        }
                    }
                }
            }
        }
    }
}