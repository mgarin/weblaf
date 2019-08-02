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
 * Base interface for visual parts within {@link com.alee.extended.tab.WebDocumentPane}.
 *
 * @param <T> {@link DocumentData} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDocumentPane">How to use WebDocumentPane</a>
 * @see com.alee.extended.tab.WebDocumentPane
 */
public interface StructureData<T extends DocumentData>
{
    /**
     * Returns actual {@link Component} behind this {@link StructureData}.
     *
     * @return actual {@link Component} behind this {@link StructureData}
     */
    public Component getComponent ();

    /**
     * Returns closest {@link PaneData}.
     * It is most commonly used to select default active {@link PaneData}.
     *
     * @return closest {@link PaneData}
     */
    public PaneData<T> findClosestPane ();

    /**
     * Returns current {@link DocumentPaneState} for this {@link StructureData}.
     * Might return {@code null} when last side's document is dragged or moved or something is splitted/merged.
     *
     * @return current {@link DocumentPaneState} for this {@link StructureData}
     * @see DocumentPaneState
     */
    public DocumentPaneState getDocumentPaneState ();
}