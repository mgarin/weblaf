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
 * Documents state listener for WebDocumentPane component.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDocumentPane">How to use WebDocumentPane</a>
 * @see com.alee.extended.tab.WebDocumentPane
 */

public interface DocumentListener<T extends DocumentData> extends EventListener
{
    /**
     * Informs that provided document was opened inside document pane.
     *
     * @param document document data
     * @param pane     specific document pane
     * @param index    document tab index
     */
    public void opened ( T document, PaneData<T> pane, int index );

    /**
     * Informs that provided document was selected inside document pane.
     * This will also occur when changing focus between panes in split mode as there is only one selected document at a time.
     *
     * @param document document data
     * @param pane     specific document pane
     * @param index    document tab index
     */
    public void selected ( T document, PaneData<T> pane, int index );

    /**
     * Informs that provided document is being closed.
     * Returns whether document is allowed to close or not.
     *
     * @param document document data
     * @param pane     specific document pane
     * @param index    document tab index
     * @return true if document is allowed to close, false otherwise
     */
    public boolean closing ( T document, PaneData<T> pane, int index );

    /**
     * Informs that provided document is closed.
     *
     * @param document document data
     * @param pane     specific document pane
     * @param index    document tab index
     */
    public void closed ( T document, PaneData<T> pane, int index );
}