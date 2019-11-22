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

import com.alee.api.Identifiable;
import com.alee.api.annotations.NotNull;
import com.alee.api.merge.Overwriting;
import com.alee.api.resource.Resource;

import javax.swing.*;
import java.io.Serializable;

/**
 * Base interface
 *
 * @param <T> icon type
 * @author Mikle Garin
 */
public interface IconData<T extends Icon> extends Identifiable, Overwriting, Cloneable, Serializable
{
    /**
     * {@link IconData} identifier prefix.
     */
    public static final String ID_PREFIX = "IconData";

    /**
     * Returns {@link Resource} containing {@link Icon} data.
     *
     * @return {@link Resource} containing {@link Icon} data
     */
    @NotNull
    public Resource getResource ();

    /**
     * Returns icon described by this data class.
     *
     * @return icon described by this data class
     */
    @NotNull
    public T loadIcon ();
}