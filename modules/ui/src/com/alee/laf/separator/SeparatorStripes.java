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

package com.alee.laf.separator;

import com.alee.api.data.BoxOrientation;
import com.alee.api.data.Orientation;
import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.content.Stripes;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.swing.*;

/**
 * Custom separator content representing multiple parallel separator stripes.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Mikle Garin
 */
@XStreamAlias ( "SeparatorStripes" )
public class SeparatorStripes<C extends JSeparator, D extends IDecoration<C, D>, I extends SeparatorStripes<C, D, I>>
        extends Stripes<C, D, I>
{
    @Override
    public Orientation getOrientation ( final C c, final D d )
    {
        return Orientation.get ( c.getOrientation () );
    }

    @Override
    public BoxOrientation getAlign ( final C c, final D d )
    {
        return BoxOrientation.center;
    }
}