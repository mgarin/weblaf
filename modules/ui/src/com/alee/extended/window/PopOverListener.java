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
 * Custom listener for WebPopOver state listening.
 *
 * @author Mikle Garin
 */

public interface PopOverListener extends EventListener
{
    /**
     * Informs that WebPopOver is being opened.
     *
     * @param popOver event source
     */
    public void opening ( final WebPopOver popOver );

    /**
     * Informs that WebPopOver was opened.
     *
     * @param popOver event source
     */
    public void opened ( final WebPopOver popOver );

    /**
     * Informs that WebPopOver.show was called while it was opened forcing it to update location.
     *
     * @param popOver event source
     */
    public void reopened ( final WebPopOver popOver );

    /**
     * Informs that user dragged WebPopOver so that it became unattached from invoker component.
     *
     * @param popOver event source
     */
    public void detached ( WebPopOver popOver );

    /**
     * Informs that WebPopOver was closed due to losing focus or some other cause.
     *
     * @param popOver event source
     */
    public void closed ( WebPopOver popOver );
}