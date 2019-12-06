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

package com.alee.laf.tabbedpane;

import com.alee.api.annotations.NotNull;
import com.alee.laf.button.StyledButtonText;
import com.alee.painter.decoration.IDecoration;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.awt.*;

/**
 * Styled text content implementation for {@link TabMenuItem}.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Mikle Garin
 */
@XStreamAlias ( "TabMenuItemText" )
public class TabMenuItemText<C extends TabMenuItem, D extends IDecoration<C, D>, I extends TabMenuItemText<C, D, I>>
        extends StyledButtonText<C, D, I>
{
    @NotNull
    @Override
    protected Color getCustomColor ( @NotNull final C c, @NotNull final D d )
    {
        return c.getTabbedPane ().getForegroundAt ( c.getIndex () );
    }
}