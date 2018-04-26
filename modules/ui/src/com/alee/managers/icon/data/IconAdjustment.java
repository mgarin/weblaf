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

package com.alee.managers.icon.data;

import com.alee.api.merge.Mergeable;

import javax.swing.*;
import java.io.Serializable;

/**
 * Basic interface for customizable {@link Icon} adjustments.
 *
 * @param <T> {@link Icon} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-IconManager">How to use IconManager</a>
 * @see com.alee.managers.icon.IconManager
 */
public interface IconAdjustment<T extends Icon> extends Mergeable, Cloneable, Serializable
{
    /**
     * Applies this adjustment to the specified {@link Icon}.
     *
     * @param icon {@link Icon} to adjust
     */
    public void apply ( T icon );
}