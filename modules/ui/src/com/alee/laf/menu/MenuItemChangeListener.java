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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.SwingUtils;

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
    @NotNull
    protected JMenuItem menuItem;

    /**
     * Constructs new menu item change listener.
     *
     * @param menuItem menu item to listen
     */
    public MenuItemChangeListener ( @NotNull final JMenuItem menuItem )
    {
        this.menuItem = menuItem;
    }

    @Override
    public void propertyChange ( @NotNull final PropertyChangeEvent evt )
    {
        // Switching listener to new menu item model on its change
        ( ( ButtonModel ) evt.getOldValue () ).removeChangeListener ( this );
        ( ( ButtonModel ) evt.getNewValue () ).addChangeListener ( this );
    }

    @Override
    public void stateChanged ( @NotNull final ChangeEvent e )
    {
        final JPopupMenu popupMenu = SwingUtils.getFirstParent ( menuItem, JPopupMenu.class );
        if ( popupMenu != null )
        {
            final Painter painter = PainterSupport.getPainter ( popupMenu );
            if ( painter instanceof PopupMenuPainter )
            {
                final PopupMenuPainter webPainter = ( PopupMenuPainter ) painter;
                if ( webPainter.getPopupStyle () == PopupStyle.dropdown )
                {
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

    /**
     * Installs {@link MenuItemChangeListener} into the specified {@link JMenuItem} and returns it.
     *
     * @param menuItem {@link JMenuItem} to install {@link MenuItemChangeListener} into
     * @return installed {@link MenuItemChangeListener}
     */
    @NotNull
    public static MenuItemChangeListener install ( @NotNull final JMenuItem menuItem )
    {
        final MenuItemChangeListener listener = new MenuItemChangeListener ( menuItem );
        menuItem.getModel ().addChangeListener ( listener );
        menuItem.addPropertyChangeListener ( AbstractButton.MODEL_CHANGED_PROPERTY, listener );
        return listener;
    }

    /**
     * Uninstalls {@link MenuItemChangeListener} from the specified {@link JMenuItem}.
     *
     * @param menuItem {@link JMenuItem} to uninstall listener from
     * @param listener {@link MenuItemChangeListener} to uninstall
     * @return {@code null} for convenience reasons
     */
    @Nullable
    public static MenuItemChangeListener uninstall ( @NotNull final JMenuItem menuItem, @NotNull final MenuItemChangeListener listener )
    {
        menuItem.removePropertyChangeListener ( AbstractButton.MODEL_CHANGED_PROPERTY, listener );
        menuItem.getModel ().removeChangeListener ( listener );
        return null;
    }
}