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

import java.util.EventListener;

/**
 * Documents pane state listener for WebDocumentPane component.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDocumentPane">How to use WebDocumentPane</a>
 * @see com.alee.extended.tab.WebDocumentPane
 */

public interface DocumentPaneListener<T extends DocumentData> extends EventListener
{
    /**
     * Informs that specified PaneData was splitted.
     *
     * @param documentPane WebDocumentPane
     * @param splittedPane splitted PaneData
     * @param newSplitData newly created SplitData
     */
    public void splitted ( final WebDocumentPane<T> documentPane, final PaneData<T> splittedPane, final SplitData<T> newSplitData );

    /**
     * Informs that specified SplitData was merged.
     *
     * @param documentPane     WebDocumentPane
     * @param mergedSplit      merged SplitData
     * @param newStructureData newly created StructureData
     */
    public void merged ( final WebDocumentPane<T> documentPane, final SplitData<T> mergedSplit, final StructureData<T> newStructureData );

    /**
     * Informs that specified SplitData's orientation was changed.
     *
     * @param documentPane WebDocumentPane
     * @param splitData    SplitData which orientation has changed
     */
    public void orientationChanged ( final WebDocumentPane<T> documentPane, final SplitData<T> splitData );

    /**
     * Informs that specified SplitData's sides were swapped.
     *
     * @param documentPane WebDocumentPane
     * @param splitData    SplitData which sides were swapped
     */
    public void sidesSwapped ( final WebDocumentPane<T> documentPane, final SplitData<T> splitData );

    /**
     * Informs that specified SplitData's divider location has changed.
     *
     * @param documentPane WebDocumentPane
     * @param splitData    SplitData which divider location has changed
     */
    public void dividerLocationChanged ( final WebDocumentPane<T> documentPane, final SplitData<T> splitData );
}