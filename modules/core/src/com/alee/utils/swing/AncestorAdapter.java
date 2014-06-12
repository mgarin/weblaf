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

import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 * Adapter to support notification when changes occur to a JComponent or one of its ancestors. These include movement and when the
 * component becomes visible or invisible, either by the setVisible() method or by being added or removed from the component hierarchy.
 *
 * @author Mikle Garin
 */

public abstract class AncestorAdapter implements AncestorListener
{
    @Override
    public void ancestorAdded ( final AncestorEvent event )
    {
        // Do nothing by default
    }

    @Override
    public void ancestorRemoved ( final AncestorEvent event )
    {
        // Do nothing by default
    }

    @Override
    public void ancestorMoved ( final AncestorEvent event )
    {
        // Do nothing by default
    }
}