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
 * Special listener that tracks DocumentData changes.
 * Its general purpose is to keep WebDocumentPane updated with DocumentData changes.
 *
 * @author Mikle Garin
 */

public interface DocumentDataListener<T extends DocumentData>
{
    /**
     * Informs about data changes which affects document tab view.
     *
     * @param document modified document
     */
    public void titleChanged ( final T document );

    /**
     * Inform about tab background changes.
     *
     * @param document      modified document
     * @param oldBackground previous background color
     * @param newBackground new background color
     */
    public void backgroundChanged ( final T document, final Color oldBackground, final Color newBackground );

    /**
     * Informs about tab component changes.
     *
     * @param document     modified document
     * @param oldComponent previous tab content
     * @param newComponent new tab content
     */
    public void contentChanged ( final T document, final Component oldComponent, final Component newComponent );
}