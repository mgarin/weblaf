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
 * {@link WebDocumentPane} structure change listener.
 *
 * @param <T> {@link DocumentData} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDocumentPane">How to use WebDocumentPane</a>
 * @see WebDocumentPane
 */
public interface DocumentPaneListener<T extends DocumentData> extends EventListener
{
    /**
     * Informs that specified {@link PaneData} was splitted.
     *
     * @param documentPane {@link WebDocumentPane}
     * @param splittedPane splitted {@link PaneData}
     * @param newSplitData newly created {@link SplitData}
     */
    public void splitted ( WebDocumentPane<T> documentPane, PaneData<T> splittedPane, SplitData<T> newSplitData );

    /**
     * Informs that specified {@link SplitData} was merged.
     *
     * @param documentPane     {@link WebDocumentPane}
     * @param mergedSplit      merged {@link SplitData}
     * @param newStructureData newly created {@link StructureData}
     */
    public void merged ( WebDocumentPane<T> documentPane, SplitData<T> mergedSplit, StructureData<T> newStructureData );

    /**
     * Informs that specified {@link SplitData}'s orientation was changed.
     *
     * @param documentPane {@link WebDocumentPane}
     * @param splitData    {@link SplitData} which orientation has changed
     */
    public void orientationChanged ( WebDocumentPane<T> documentPane, SplitData<T> splitData );

    /**
     * Informs that specified {@link SplitData}'s sides were swapped.
     *
     * @param documentPane {@link WebDocumentPane}
     * @param splitData    {@link SplitData} which sides were swapped
     */
    public void sidesSwapped ( WebDocumentPane<T> documentPane, SplitData<T> splitData );

    /**
     * Informs that specified {@link SplitData}'s divider location has changed.
     *
     * @param documentPane {@link WebDocumentPane}
     * @param splitData    {@link SplitData} which divider location has changed
     */
    public void dividerLocationChanged ( WebDocumentPane<T> documentPane, SplitData<T> splitData );
}