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

package com.alee.extended.dock;

import com.alee.api.annotations.NotNull;
import com.alee.api.data.CompassDirection;
import com.alee.painter.decoration.AbstractSectionDecorationPainter;
import com.alee.painter.decoration.IDecoration;

import java.util.List;

/**
 * Simple sidebar painter based on {@link AbstractSectionDecorationPainter}.
 * It is used within {@link DockablePanePainter} to paint sidebar areas background.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Mikle Garin
 */
public class DockableAreaPainter<C extends WebDockablePane, U extends WDockablePaneUI<C>, D extends IDecoration<C, D>>
        extends AbstractSectionDecorationPainter<C, U, D> implements IDockableAreaPainter<C, U>
{
    /**
     * Painted {@link WebDockablePane} area.
     */
    protected transient CompassDirection area;

    @Override
    public String getSectionId ()
    {
        return "sidebar";
    }

    @Override
    protected boolean isFocused ()
    {
        return false;
    }

    @NotNull
    @Override
    public List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();

        // Dockable pane area state
        if ( area != null )
        {
            states.add ( area.name () );
        }

        return states;
    }

    @Override
    public boolean hasDecorationFor ( @NotNull final CompassDirection area )
    {
        return usesState ( area.name () );
    }

    @Override
    public void prepareToPaint ( @NotNull final CompassDirection area )
    {
        this.area = area;
        updateDecorationState ();
    }
}