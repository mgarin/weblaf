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
import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.content.AbstractIconContent;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.swing.*;

/**
 * Simple button icon content implementation.
 * It doesn't use button states and simply provides icon returned by {@link AbstractButton#getIcon()}.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Mikle Garin
 */
@XStreamAlias ( "MenuItemStateIcon" )
public class MenuItemStateIcon<C extends JMenuItem, D extends IDecoration<C, D>, I extends MenuItemStateIcon<C, D, I>>
        extends AbstractIconContent<C, D, I>
{
    @NotNull
    @Override
    public String getId ()
    {
        return id != null ? id : "menu-item-state-icon";
    }

    @Nullable
    @Override
    protected Icon getIcon ( @NotNull final C c, @NotNull final D d )
    {
        return ( Icon ) c.getClientProperty ( AbstractStateMenuItemPainter.STATE_ICON_PROPERTY );
    }
}