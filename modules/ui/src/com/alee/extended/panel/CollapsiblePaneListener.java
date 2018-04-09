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

package com.alee.extended.panel;

import java.util.EventListener;

/**
 * {@link WebCollapsiblePane} expansion listener.
 *
 * @author Mikle Garin
 */
public interface CollapsiblePaneListener extends EventListener
{
    /**
     * Informs about {@link WebCollapsiblePane} expansion start.
     *
     * @param collapsiblePane {@link WebCollapsiblePane}
     */
    public void expanding ( WebCollapsiblePane collapsiblePane );

    /**
     * Informs about {@link WebCollapsiblePane} expansion finish.
     *
     * @param collapsiblePane {@link WebCollapsiblePane}
     */
    public void expanded ( WebCollapsiblePane collapsiblePane );

    /**
     * Informs about {@link WebCollapsiblePane} collapse start.
     *
     * @param collapsiblePane {@link WebCollapsiblePane}
     */
    public void collapsing ( WebCollapsiblePane collapsiblePane );

    /**
     * Informs about {@link WebCollapsiblePane} collapse finish.
     *
     * @param collapsiblePane {@link WebCollapsiblePane}
     */
    public void collapsed ( WebCollapsiblePane collapsiblePane );
}