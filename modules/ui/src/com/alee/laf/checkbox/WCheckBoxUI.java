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

package com.alee.laf.checkbox;

import com.alee.api.annotations.NotNull;
import com.alee.laf.button.WButtonUI;

import javax.swing.*;
import java.awt.*;

/**
 * Pluggable look and feel interface for {@link JCheckBox} component.
 *
 * @param <C> component type
 * @author Mikle Garin
 */
public abstract class WCheckBoxUI<C extends JCheckBox> extends WButtonUI<C>
{
    @NotNull
    @Override
    public String getPropertyPrefix ()
    {
        return "CheckBox.";
    }

    /**
     * Returns icon bounds.
     *
     * @return icon bounds
     */
    public abstract Rectangle getIconBounds ();
}