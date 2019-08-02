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

/**
 * Adapter for {@link DocumentPaneListener}.
 *
 * @param <T> {@link DocumentData} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDocumentPane">How to use WebDocumentPane</a>
 * @see WebDocumentPane
 */
public abstract class DocumentPaneAdapter<T extends DocumentData> implements DocumentPaneListener<T>
{
    @Override
    public void splitted ( final WebDocumentPane<T> documentPane, final PaneData<T> splittedPane, final SplitData<T> newSplitData )
    {
        /**
         * Do nothing by default.
         */
    }

    @Override
    public void merged ( final WebDocumentPane<T> documentPane, final SplitData<T> mergedSplit, final StructureData<T> newStructureData )
    {
        /**
         * Do nothing by default.
         */
    }

    @Override
    public void orientationChanged ( final WebDocumentPane<T> documentPane, final SplitData<T> splitData )
    {
        /**
         * Do nothing by default.
         */
    }

    @Override
    public void sidesSwapped ( final WebDocumentPane<T> documentPane, final SplitData<T> splitData )
    {
        /**
         * Do nothing by default.
         */
    }

    @Override
    public void dividerLocationChanged ( final WebDocumentPane<T> documentPane, final SplitData<T> splitData )
    {
        /**
         * Do nothing by default.
         */
    }
}