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

package com.alee.laf.grouping;

import com.alee.api.annotations.NotNull;

import java.awt.*;

/**
 * Base for any layout providing component grouping capabilities.
 *
 * @author Mikle Garin
 * @see com.alee.laf.grouping.GroupPaneLayout
 */
public interface GroupingLayout extends LayoutManager2
{
    /**
     * Returns whether or not components should be visually grouped.
     *
     * @return true if components should be visually grouped, false otherwise
     */
    public boolean isGrouping ();

    /**
     * Returns descriptor for painted component sides.
     *
     * @param component painted component
     * @return descriptor for painted component sides
     */
    public String getSides ( @NotNull Component component );

    /**
     * Returns descriptor for painted component lines.
     *
     * @param component painted component
     * @return descriptor for painted component lines
     */
    public String getLines ( @NotNull Component component );
}