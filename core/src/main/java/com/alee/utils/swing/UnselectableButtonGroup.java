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
 * User: mgarin Date: 15.06.11 Time: 15:48
 */

public class UnselectableButtonGroup extends ButtonGroup
{
    private List<ButtonGroupListener> listeners = new ArrayList<ButtonGroupListener> ();

    private boolean unselectable = true;

    public UnselectableButtonGroup ()
    {
        super ();
    }

    public UnselectableButtonGroup ( AbstractButton... buttons )
    {
        super ();
        add ( buttons );
    }

    public UnselectableButtonGroup ( List<AbstractButton> buttons )
    {
        super ();
        add ( buttons );
    }

    public boolean isUnselectable ()
    {
        return unselectable;
    }

    public void setUnselectable ( boolean unselectable )
    {
        this.unselectable = unselectable;
    }

    public void setSelected ( ButtonModel model, boolean selected )
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

    public void addButtonGroupListener ( ButtonGroupListener listener )
    {
        listeners.add ( listener );
    }

    public void removeButtonGroupListener ( ButtonGroupListener listener )
    {
        listeners.remove ( listener );
    }

    public void fireSelectionChanged ()
    {
        for ( ButtonGroupListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.selectionChanged ();
        }
    }

    public void add ( AbstractButton... b )
    {
        for ( AbstractButton button : b )
        {
            add ( button );
        }
    }

    public void add ( List<AbstractButton> b )
    {
        for ( AbstractButton button : b )
        {
            add ( button );
        }
    }

    public static UnselectableButtonGroup group ( AbstractButton... buttons )
    {
        return new UnselectableButtonGroup ( buttons );
    }

    public static UnselectableButtonGroup group ( List<AbstractButton> buttons )
    {
        return new UnselectableButtonGroup ( buttons );
    }
}
