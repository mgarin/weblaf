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
 * Base interface for any {@link Icon} source.
 *
 * @param <I> {@link Icon} type
 * @author Mikle Garin
 */
public interface IconSource<I extends Icon> extends Identifiable, Overwriting, Cloneable, Serializable
{
    /**
     * {@link IconSource} identifier prefix.
     */
    public static final String ID_PREFIX = "IconSource";

    /**
     * Returns {@link Resource} containing {@link Icon}.
     *
     * @return {@link Resource} containing {@link Icon}
     */
    @NotNull
    public Resource getResource ();

    /**
     * Returns {@link Icon} described by this {@link IconSource}.
     *
     * @return {@link Icon} described by this {@link IconSource}
     */
    @NotNull
    public I loadIcon ();
}