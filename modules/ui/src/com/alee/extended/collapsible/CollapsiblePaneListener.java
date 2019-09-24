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

package com.alee.extended.collapsible;

import com.alee.api.annotations.NotNull;

import java.util.EventListener;

/**
 * {@link WebCollapsiblePane} expansion listener.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebCollapsiblePane">How to use WebCollapsiblePane</a>
 * @see WebCollapsiblePane
 * @see CollapsiblePaneAdapter
 */
public interface CollapsiblePaneListener extends EventListener
{
    /**
     * Informs about {@link WebCollapsiblePane} expansion start.
     * At this point {@link WebCollapsiblePane} is already marked as expanded.
     *
     * @param pane {@link WebCollapsiblePane}
     */
    public void expanding ( @NotNull WebCollapsiblePane pane );

    /**
     * Informs about {@link WebCollapsiblePane} expansion finish.
     *
     * @param pane {@link WebCollapsiblePane}
     */
    public void expanded ( @NotNull WebCollapsiblePane pane );

    /**
     * Informs about {@link WebCollapsiblePane} collapse start.
     * At this point {@link WebCollapsiblePane} is already marked as collapsed.
     *
     * @param pane {@link WebCollapsiblePane}
     */
    public void collapsing ( @NotNull WebCollapsiblePane pane );

    /**
     * Informs about {@link WebCollapsiblePane} collapse finish.
     *
     * @param pane {@link WebCollapsiblePane}
     */
    public void collapsed ( @NotNull WebCollapsiblePane pane );
}