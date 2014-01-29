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
import java.awt.*;

/**
 * Base painter for JPopupMenu component.
 * It is used as WebPopupMenuUI default styling.
 *
 * @author Mikle Garin
 */

public class PopupMenuPainter<E extends JPopupMenu> extends WebPopupPainter<E>
{
    /**
     * {@inheritDoc}
     */
    @Override
    public Insets getMargin ( final E c )
    {
        final Insets margin = super.getMargin ( c );
        margin.top += round;
        margin.bottom += round;
        return margin;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void paintTransparentPopup ( final Graphics2D g2d, final E popupMenu )
    {
        final Dimension menuSize = popupMenu.getSize ();

        // Painting shade
        paintShade ( g2d, popupMenu, menuSize );

        // Painting background
        paintBackground ( g2d, popupMenu, menuSize );

        // Painting dropdown corner fill
        // This is a specific for WebPopupMenuUI feature
        paintDropdownCornerFill ( g2d, popupMenu, menuSize );

        // Painting border
        paintBorder ( g2d, popupMenu, menuSize );
    }

    /**
     * Paints dropdown-styled popup menu corner fill if menu item near it is selected.
     *
     * @param g2d       graphics context
     * @param popupMenu popup menu
     * @param menuSize  menu size
     */
    protected void paintDropdownCornerFill ( final Graphics2D g2d, final E popupMenu, final Dimension menuSize )
    {
        if ( popupPainterStyle == PopupPainterStyle.dropdown && round == 0 )
        {
            // Checking whether corner should be filled or not
            final boolean north = cornerSide == NORTH;
            final int zIndex = north ? 0 : popupMenu.getComponentCount () - 1;
            final Component component = popupMenu.getComponent ( zIndex );
            if ( component instanceof JMenuItem )
            {
                final JMenuItem menuItem = ( JMenuItem ) component;
                if ( menuItem.isEnabled () && ( menuItem.getModel ().isArmed () || menuItem.isSelected () ) )
                {
                    // Filling corner properly
                    if ( menuItem.getUI () instanceof WebMenuUI )
                    {
                        final WebMenuUI ui = ( WebMenuUI ) menuItem.getUI ();
                        g2d.setPaint ( north ? ui.getNorthCornerFill () : ui.getSouthCornerFill () );
                        g2d.fill ( getDropdownCornerShape ( popupMenu, menuSize, true ) );
                    }
                    else if ( menuItem.getUI () instanceof WebMenuItemUI )
                    {
                        final WebMenuItemUI ui = ( WebMenuItemUI ) menuItem.getUI ();
                        g2d.setPaint ( north ? ui.getNorthCornerFill () : ui.getSouthCornerFill () );
                        g2d.fill ( getDropdownCornerShape ( popupMenu, menuSize, true ) );
                    }
                }
            }
        }
    }
}