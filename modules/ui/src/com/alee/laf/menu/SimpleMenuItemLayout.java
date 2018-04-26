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

import com.alee.painter.decoration.IDecoration;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.swing.*;

/**
 * Simple menu item -like layout implementation.
 * It only paints contents placed under {@link #ICON}, {@link #TEXT}, {@link #ACCELERATOR} and {@link #ARROW} constraints.
 * Do not use this layout for any {@link JMenuItem}, use {@link MenuItemLayout} instead as it contains some important features.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> layout type
 * @author Mikle Garin
 */
@XStreamAlias ( "SimpleMenuItemLayout" )
public class SimpleMenuItemLayout<C extends JComponent, D extends IDecoration<C, D>, I extends SimpleMenuItemLayout<C, D, I>>
        extends AbstractMenuItemLayout<C, D, I>
{
    @Override
    protected boolean isAlignTextByIcons ( final C c, final D d )
    {
        return false;
    }

    @Override
    protected int getMaxIconWidth ( final C c, final D d )
    {
        final Icon icon;
        if ( c instanceof AbstractButton )
        {
            icon = ( ( AbstractButton ) c ).getIcon ();
        }
        else if ( c instanceof JLabel )
        {
            icon = ( ( JLabel ) c ).getIcon ();
        }
        else
        {
            icon = null;
        }
        return icon != null ? icon.getIconWidth () : 0;
    }

    @Override
    protected int getIconTextGap ( final C c, final D d )
    {
        final int gap;
        if ( iconTextGap != null )
        {
            gap = iconTextGap;
        }
        else if ( c instanceof AbstractButton )
        {
            gap = ( ( AbstractButton ) c ).getIconTextGap ();
        }
        else if ( c instanceof JLabel )
        {
            gap = ( ( JLabel ) c ).getIconTextGap ();
        }
        else
        {
            gap = 0;
        }
        return gap;
    }
}