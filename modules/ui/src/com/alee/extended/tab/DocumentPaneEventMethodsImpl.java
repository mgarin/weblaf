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
 * @see com.alee.extended.tab.DocumentPaneEventMethods
 */

public final class DocumentPaneEventMethodsImpl
{
    /**
     * Shortcut method for document open event.
     *
     * @param documentPane document pane to handle events for
     * @param runnable     document data runnable
     * @return used document adapter
     */
    public static <T extends DocumentData> DocumentAdapter<T> onDocumentOpen ( final WebDocumentPane<T> documentPane,
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
     * Shortcut method for document selection event.
     *
     * @param documentPane document pane to handle events for
     * @param runnable     document data runnable
     * @return used document adapter
     */
    public static <T extends DocumentData> DocumentAdapter<T> onDocumentSelection ( final WebDocumentPane<T> documentPane,
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
     * Shortcut method for document closing event.
     *
     * @param documentPane document pane to handle events for
     * @param runnable     document data cancellable runnable
     * @return used document adapter
     */
    public static <T extends DocumentData> DocumentAdapter<T> onDocumentClosing ( final WebDocumentPane<T> documentPane,
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
     * Shortcut method for document close event.
     *
     * @param documentPane document pane to handle events for
     * @param runnable     document data runnable
     * @return used document adapter
     */
    public static <T extends DocumentData> DocumentAdapter<T> onDocumentClose ( final WebDocumentPane<T> documentPane,
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