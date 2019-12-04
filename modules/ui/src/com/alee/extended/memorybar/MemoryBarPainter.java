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

package com.alee.extended.memorybar;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
import com.alee.laf.button.AbstractButtonModelPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import java.util.List;

/**
 * Basic painter for {@link WebMemoryBar} component.
 * It is used as {@link WMemoryBarUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Mikle Garin
 */
public class MemoryBarPainter<C extends WebMemoryBar, U extends WMemoryBarUI<C>, D extends IDecoration<C, D>>
        extends AbstractButtonModelPainter<C, U, D> implements IMemoryBarPainter<C, U>
{
    @NotNull
    @Override
    public List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();
        states.add ( component.getOrientation ().isHorizontal () ? DecorationState.horizontal : DecorationState.vertical );
        return states;
    }

    @Override
    protected void propertyChanged ( @NotNull final String property, @Nullable final Object oldValue, @Nullable final Object newValue )
    {
        // Perform basic actions on property changes
        super.propertyChanged ( property, oldValue, newValue );

        // Updating states
        if ( Objects.equals ( property, WebMemoryBar.ORIENTATION_PROPERTY ) )
        {
            updateDecorationState ();
        }
    }

    @NotNull
    @Override
    protected ButtonModel getButtonModel ()
    {
        return component.getModel ();
    }
}