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

import javax.swing.*;
import javax.swing.plaf.ComponentUI;

/**
 * Custom UI for JCheckBoxMenuItem component.
 *
 * @author Mikle Garin
 */

public class WebCheckBoxMenuItemUI extends WebMenuItemUI
{
    /**
     * Returns an instance of the WebCheckBoxMenuItemUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebCheckBoxMenuItemUI
     */
    @SuppressWarnings ("UnusedParameters")
    public static ComponentUI createUI ( JComponent c )
    {
        return new WebCheckBoxMenuItemUI ();
    }

    /**
     * Returns property prefix for this specific UI.
     *
     * @return property prefix for this specific UI
     */
    @Override
    protected String getPropertyPrefix ()
    {
        return "CheckBoxMenuItem";
    }
}