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

package com.alee.managers.style;

import com.alee.api.annotations.NotNull;
import com.alee.utils.CoreSwingUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Component {@link StyleId} container that can be used for convenient {@link StyleId} construction.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see StyleManager
 */
public final class ChildStyleId
{
    /**
     * Style identifier.
     * Identifies some specific component style.
     */
    @NotNull
    private final String id;

    /**
     * Constructs new child {@link StyleId} container.
     *
     * @param id style identifier
     */
    private ChildStyleId ( @NotNull final String id )
    {
        this.id = id;
    }

    /**
     * Returns child style identifier.
     *
     * @return child style identifier
     */
    @NotNull
    public String getId ()
    {
        return id;
    }

    /**
     * Returns completed style identifier for the child style.
     *
     * @param parent parent component
     * @return completed style identifier for the child style
     */
    @NotNull
    public StyleId at ( @NotNull final JComponent parent )
    {
        return StyleId.of ( getId (), parent );
    }

    /**
     * Returns completed style identifier for the child style.
     *
     * @param parent parent component
     * @return completed style identifier for the child style
     */
    @NotNull
    public StyleId at ( @NotNull final Window parent )
    {
        return at ( CoreSwingUtils.getNonNullRootPane ( parent ) );
    }

    /**
     * Returns new child style identifier container.
     *
     * @param id style identifier
     * @return new child style identifier container
     */
    @NotNull
    public static ChildStyleId of ( @NotNull final String id )
    {
        return new ChildStyleId ( id );
    }
}