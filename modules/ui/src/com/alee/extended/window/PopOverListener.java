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

package com.alee.extended.window;

import java.util.EventListener;

/**
 * {@link WebPopOver} state change listening.
 *
 * @author Mikle Garin
 */
public interface PopOverListener extends EventListener
{
    /**
     * todo 1. Add reattach event when it is available in {@link WebPopOver}
     */

    /**
     * Informs that {@link WebPopOver} is being opened.
     *
     * @param popOver {@link WebPopOver}
     */
    public void opening ( WebPopOver popOver );

    /**
     * Informs that {@link WebPopOver} was opened.
     *
     * @param popOver {@link WebPopOver}
     */
    public void opened ( WebPopOver popOver );

    /**
     * Informs that {@link WebPopOver#show} was called while it was already opened forcing it to update location.
     *
     * @param popOver {@link WebPopOver}
     */
    public void reopened ( WebPopOver popOver );

    /**
     * Informs that user dragged {@link WebPopOver} so that it became unattached from invoker component.
     *
     * @param popOver {@link WebPopOver}
     */
    public void detached ( WebPopOver popOver );

    /**
     * Informs that {@link WebPopOver} was closed due to losing focus or some other cause.
     *
     * @param popOver {@link WebPopOver}
     */
    public void closed ( WebPopOver popOver );
}