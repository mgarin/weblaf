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
 * Common implementations for {@link com.alee.extended.tab.DocumentPaneEventMethods} interface methods.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDocumentPane">How to use WebDocumentPane</a>
 * @see DocumentPaneEventMethods
 * @see WebDocumentPane
 */
public final class DocumentPaneEventMethodsImpl
{
    /**
     * Shortcut method for {@link DocumentListener#opened(DocumentData, PaneData, int)} event.
     *
     * @param documentPane {@link WebDocumentPane}
     * @param runnable     {@link DocumentDataRunnable}
     * @param <T>          {@link DocumentData} type
     * @return created {@link DocumentListener}
     */
    public static <T extends DocumentData> DocumentListener<T> onDocumentOpen ( final WebDocumentPane<T> documentPane,
                                                                                final DocumentDataRunnable<T> runnable )
    {
        final DocumentAdapter<T> documentAdapter = new DocumentAdapter<T> ()
        {
            @Override
            public void opened ( final T document, final PaneData<T> pane, final int index )
            {
                runnable.run ( document, pane, index );
            }
        };
        documentPane.addDocumentListener ( documentAdapter );
        return documentAdapter;
    }

    /**
     * Shortcut method for {@link DocumentListener#selected(DocumentData, PaneData, int)} event.
     *
     * @param documentPane {@link WebDocumentPane}
     * @param runnable     {@link DocumentDataRunnable}
     * @param <T>          {@link DocumentData} type
     * @return created {@link DocumentListener}
     */
    public static <T extends DocumentData> DocumentListener<T> onDocumentSelection ( final WebDocumentPane<T> documentPane,
                                                                                     final DocumentDataRunnable<T> runnable )
    {
        final DocumentAdapter<T> documentAdapter = new DocumentAdapter<T> ()
        {
            @Override
            public void selected ( final T document, final PaneData<T> pane, final int index )
            {
                runnable.run ( document, pane, index );
            }
        };
        documentPane.addDocumentListener ( documentAdapter );
        return documentAdapter;
    }

    /**
     * Shortcut method for {@link DocumentListener#closing(DocumentData, PaneData, int)} event.
     *
     * @param documentPane {@link WebDocumentPane}
     * @param runnable     {@link DocumentDataCancellableRunnable}
     * @param <T>          {@link DocumentData} type
     * @return created {@link DocumentListener}
     */
    public static <T extends DocumentData> DocumentListener<T> onDocumentClosing ( final WebDocumentPane<T> documentPane,
                                                                                   final DocumentDataCancellableRunnable<T> runnable )
    {
        final DocumentAdapter<T> documentAdapter = new DocumentAdapter<T> ()
        {
            @Override
            public boolean closing ( final T document, final PaneData<T> pane, final int index )
            {
                return runnable.run ( document, pane, index );
            }
        };
        documentPane.addDocumentListener ( documentAdapter );
        return documentAdapter;
    }

    /**
     * Shortcut method for {@link DocumentListener#closed(DocumentData, PaneData, int)} event.
     *
     * @param documentPane {@link WebDocumentPane}
     * @param runnable     {@link DocumentDataRunnable}
     * @param <T>          {@link DocumentData} type
     * @return created {@link DocumentListener}
     */
    public static <T extends DocumentData> DocumentListener<T> onDocumentClose ( final WebDocumentPane<T> documentPane,
                                                                                 final DocumentDataRunnable<T> runnable )
    {
        final DocumentAdapter<T> documentAdapter = new DocumentAdapter<T> ()
        {
            @Override
            public void closed ( final T document, final PaneData<T> pane, final int index )
            {
                runnable.run ( document, pane, index );
            }
        };
        documentPane.addDocumentListener ( documentAdapter );
        return documentAdapter;
    }
}