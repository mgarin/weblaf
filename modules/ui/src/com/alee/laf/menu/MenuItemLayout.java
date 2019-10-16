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

import com.alee.api.jdk.Objects;
import com.alee.painter.decoration.IDecoration;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.awt.*;

/**
 * {@link JMenuItem} layout implementation.
 * It only paints contents placed under {@link #ICON}, {@link #TEXT}, {@link #ACCELERATOR} and {@link #ARROW} constraints.
 * If you want to use a similar layout for non-{@link JMenuItem} component - use {@link SimpleMenuItemLayout} instead.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> layout type
 * @author Mikle Garin
 */
@XStreamAlias ( "MenuItemLayout" )
public class MenuItemLayout<C extends JMenuItem, D extends IDecoration<C, D>, I extends MenuItemLayout<C, D, I>>
        extends AbstractMenuItemLayout<C, D, I>
{
    /**
     * Whether or not menu items text should be aligned by maximum icon size.
     */
    @XStreamAsAttribute
    protected Boolean alignTextByIcons;

    @Override
    protected boolean isAlignTextByIcons ( final C c, final D d )
    {
        return alignTextByIcons == null || alignTextByIcons;
    }

    @Override
    protected int getMaxIconWidth ( final C c, final D d )
    {
        int max = 0;
        if ( isAlignTextByIcons ( c, d ) && c.getParent () instanceof JPopupMenu )
        {
            final JPopupMenu popupMenu = ( JPopupMenu ) c.getParent ();
            for ( int i = 0; i < popupMenu.getComponentCount (); i++ )
            {
                final Component component = popupMenu.getComponent ( i );
                if ( component instanceof JMenuItem )
                {
                    final AbstractButton otherItem = ( AbstractButton ) component;
                    if ( otherItem.getIcon () != null )
                    {
                        max = Math.max ( max, otherItem.getIcon ().getIconWidth () );
                    }
                }
            }
        }
        else
        {
            final Icon icon = c.getIcon ();
            max = icon != null ? icon.getIconWidth () : 0;
        }
        return max;
    }

    @Override
    protected int getIconTextGap ( final C c, final D d )
    {
        return iconTextGap != null ? iconTextGap : c.getIconTextGap ();
    }

    @Override
    public boolean isEmpty ( final C c, final D d, final String constraints )
    {
        // Checking default emptiness conditions
        boolean empty = super.isEmpty ( c, d, constraints );

        // Extra conditions for menu items
        if ( !empty )
        {
            if ( Objects.equals ( constraints, ACCELERATOR ) )
            {
                // Accelerator is only displayed for menu items within popup menu and when it is specified
                empty = !isInPopupMenu ( c, d ) || c.getAccelerator () == null;
            }
            else if ( Objects.equals ( constraints, ARROW ) )
            {
                // Menu arrow is only displayed for menu items within popup menu
                empty = !isInPopupMenu ( c, d );
            }
        }

        return empty;
    }

    /**
     * Returns whether or not the specified menu item is placed within popup menu.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return true if the specified menu item is placed within popup menu, false otherwise
     */
    protected boolean isInPopupMenu ( final C c, final D d )
    {
        return c.getParent () != null && c.getParent () instanceof JPopupMenu;
    }
}