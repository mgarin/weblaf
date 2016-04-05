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

import com.alee.utils.swing.extensions.MethodExtension;

/**
 * This interface provides a set of methods that should be added into WebDocumentPane that supports custom WebLaF events.
 *
 * @author Mikle Garin
 * @see com.alee.utils.swing.extensions.MethodExtension
 * @see com.alee.extended.tab.DocumentPaneEventMethodsImpl
 */

public interface DocumentPaneEventMethods<T extends DocumentData> extends MethodExtension
{
    /**
     * Shortcut method for document open event.
     *
     * @param runnable document data runnable
     * @return used document adapter
     */
    public DocumentAdapter<T> onDocumentOpen ( DocumentDataRunnable<T> runnable );

    /**
     * Shortcut method for document selection event.
     *
     * @param runnable document data runnable
     * @return used document adapter
     */
    public DocumentAdapter<T> onDocumentSelection ( DocumentDataRunnable<T> runnable );

    /**
     * Shortcut method for document closing event.
     *
     * @param runnable document data cancellable runnable
     * @return used document adapter
     */
    public DocumentAdapter<T> onDocumentClosing ( DocumentDataCancellableRunnable<T> runnable );

    /**
     * Shortcut method for document close event.
     *
     * @param runnable document data runnable
     * @return used document adapter
     */
    public DocumentAdapter<T> onDocumentClose ( DocumentDataRunnable<T> runnable );
}