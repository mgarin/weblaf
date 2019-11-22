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
import com.alee.api.annotations.Nullable;
import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.background.ColorBackground;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.awt.*;

/**
 * {@link Tab} background.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> background type
 * @author Mikle Garin
 */
@XStreamAlias ( "TabBackground" )
public class TabBackground<C extends Tab, D extends IDecoration<C, D>, I extends TabBackground<C, D, I>>
        extends ColorBackground<C, D, I>
{
    @Nullable
    @Override
    protected Color getColor ( @NotNull final C c, @NotNull final D d )
    {
        return color != null ? color : c.getTabbedPane ().getBackgroundAt ( c.getIndex () );
    }
}