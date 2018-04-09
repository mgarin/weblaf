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
 * {@link MethodExtension} for {@link DocumentListener} available in {@link WebDocumentPane}.
 *
 * @param <T> {@link DocumentData} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDocumentPane">How to use WebDocumentPane</a>
 * @see MethodExtension
 * @see DocumentPaneEventMethodsImpl
 * @see WebDocumentPane
 */
public interface DocumentPaneEventMethods<T extends DocumentData> extends MethodExtension
{
    /**
     * Shortcut method for {@link DocumentListener#opened(DocumentData, PaneData, int)} event.
     *
     * @param runnable {@link DocumentDataRunnable}
     * @return created {@link DocumentListener}
     */
    public DocumentListener<T> onDocumentOpen ( DocumentDataRunnable<T> runnable );

    /**
     * Shortcut method for {@link DocumentListener#selected(DocumentData, PaneData, int)} event.
     *
     * @param runnable {@link DocumentDataRunnable}
     * @return created {@link DocumentListener}
     */
    public DocumentListener<T> onDocumentSelection ( DocumentDataRunnable<T> runnable );

    /**
     * Shortcut method for {@link DocumentListener#closing(DocumentData, PaneData, int)} event.
     *
     * @param runnable {@link DocumentDataCancellableRunnable}
     * @return created {@link DocumentListener}
     */
    public DocumentListener<T> onDocumentClosing ( DocumentDataCancellableRunnable<T> runnable );

    /**
     * Shortcut method for {@link DocumentListener#closed(DocumentData, PaneData, int)} event.
     *
     * @param runnable {@link DocumentDataRunnable}
     * @return created {@link DocumentListener}
     */
    public DocumentListener<T> onDocumentClose ( DocumentDataRunnable<T> runnable );
}