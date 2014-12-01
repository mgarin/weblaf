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

package com.alee.utils.swing;

import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikle Garin
 */

public class UnselectableButtonGroup extends ButtonGroup
{
    private final List<ButtonGroupListener> listeners = new ArrayList<ButtonGroupListener> ( 1 );

    private boolean unselectable = true;

    public UnselectableButtonGroup ()
    {
        super ();
    }

    public UnselectableButtonGroup ( final AbstractButton... buttons )
    {
        super ();
        add ( buttons );
    }

    public UnselectableButtonGroup ( final List<AbstractButton> buttons )
    {
        super ();
        add ( buttons );
    }

    public UnselectableButtonGroup ( final boolean unselectable )
    {
        super ();
        setUnselectable ( unselectable );
    }

    public boolean isUnselectable ()
    {
        return unselectable;
    }

    public void setUnselectable ( final boolean unselectable )
    {
        this.unselectable = unselectable;
    }

    @Override
    public void setSelected ( final ButtonModel model, final boolean selected )
    {
        if ( selected || !unselectable )
        {
            super.setSelected ( model, selected );
        }
        else
        {
            clearSelection ();
        }
        fireSelectionChanged ();
    }

    public List<AbstractButton> getButtons ()
    {
        return CollectionUtils.copy ( buttons );
    }

    public void addButtonGroupListener ( final ButtonGroupListener listener )
    {
        listeners.add ( listener );
    }

    public void removeButtonGroupListener ( final ButtonGroupListener listener )
    {
        listeners.remove ( listener );
    }

    public void fireSelectionChanged ()
    {
        for ( final ButtonGroupListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.selectionChanged ();
        }
    }

    public void add ( final AbstractButton... b )
    {
        for ( final AbstractButton button : b )
        {
            add ( button );
        }
    }

    public void add ( final List<AbstractButton> b )
    {
        for ( final AbstractButton button : b )
        {
            add ( button );
        }
    }

    public static UnselectableButtonGroup group ( final AbstractButton... buttons )
    {
        return new UnselectableButtonGroup ( buttons );
    }

    public static UnselectableButtonGroup group ( final List<AbstractButton> buttons )
    {
        return new UnselectableButtonGroup ( buttons );
    }
}