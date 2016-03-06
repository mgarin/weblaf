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
 * This is a special WebCollapsiblePane events listener that fires four kinds of events.
 * Two of them are fired before the collapsible pane finished either collapsing or expanding and two other fired after.
 * <p>
 * Notice that similar "before" and "after" events could be fired almost in the same time in case WebCollapsiblePane is not animated or its
 * animation speed is increased by collapsible pane settings.
 *
 * @author Mikle Garin
 */

public interface CollapsiblePaneListener extends EventListener
{
    /**
     * Notifies when collapsible pane starts to expand.
     *
     * @param pane collapsible pane
     */
    public void expanding ( WebCollapsiblePane pane );

    /**
     * Notifies when collapsible pane finished expanding.
     *
     * @param pane collapsible pane
     */
    public void expanded ( WebCollapsiblePane pane );

    /**
     * Notifies when collapsible pane starts to collapse.
     *
     * @param pane collapsible pane
     */
    public void collapsing ( WebCollapsiblePane pane );

    /**
     * Notifies when collapsible pane finished collapsing.
     *
     * @param pane collapsible pane
     */
    public void collapsed ( WebCollapsiblePane pane );
}
