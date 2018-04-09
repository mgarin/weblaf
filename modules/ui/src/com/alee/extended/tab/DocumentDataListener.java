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

package com.alee.extended.tab;

import java.awt.*;

/**
 * Special listener that tracks {@link DocumentData} changes.
 * Its general purpose is to keep {@link WebDocumentPane} updated with {@link DocumentData} changes.
 *
 * @param <T> {@link DocumentData} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDocumentPane">How to use WebDocumentPane</a>
 * @see WebDocumentPane
 */
public interface DocumentDataListener<T extends DocumentData>
{
    /**
     * Informs about {@link DocumentData} title change.
     * todo Provide old and new title similar to other methods?
     *
     * @param document modified {@link DocumentData}
     */
    public void titleChanged ( T document );

    /**
     * Inform about {@link DocumentData} background {@link Color} change.
     *
     * @param document      modified {@link DocumentData}
     * @param oldBackground previous background {@link Color}
     * @param newBackground new background {@link Color}
     */
    public void backgroundChanged ( T document, Color oldBackground, Color newBackground );

    /**
     * Informs about {@link DocumentData} content {@link Component} change.
     *
     * @param document     modified {@link DocumentData}
     * @param oldComponent previous tab content {@link Component}
     * @param newComponent new tab content {@link Component}
     */
    public void contentChanged ( T document, Component oldComponent, Component newComponent );
}