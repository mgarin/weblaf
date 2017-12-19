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

package com.alee.laf;

import javax.swing.*;
import javax.swing.plaf.ActionMapUIResource;

/**
 * {@link ActionMap} based on {@link ActionMapUIResource}.
 * Provides a single extra {@link #put(Action)} method for convenience.
 *
 * @author Mikle Garin
 * @see UIAction
 */

public class UIActionMap extends ActionMapUIResource
{
    /**
     * Adds specified {@link Action} into map.
     *
     * @param action {@link Action} to add
     */
    public void put ( final Action action )
    {
        put ( action.getValue ( Action.NAME ), action );
    }
}