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

package com.alee.extended.button;

import com.alee.api.annotations.NotNull;
import com.alee.laf.button.WButtonUI;

/**
 * Pluggable look and feel interface for {@link WebSplitButton} component.
 *
 * @param <C> component type
 * @author Mikle Garin
 */
public abstract class WSplitButtonUI<C extends WebSplitButton> extends WButtonUI<C>
{
    @NotNull
    @Override
    public String getPropertyPrefix ()
    {
        return "SplitButton.";
    }

    /**
     * Returns whether or not mouse is currently over the split menu button.
     *
     * @return {@code true} if mouse is currently over the split menu button, {@code false} otherwise
     */
    public abstract boolean isOnSplit ();
}