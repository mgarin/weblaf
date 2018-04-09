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

import com.alee.managers.drag.view.SimpleDragViewHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DragSourceDragEvent;

/**
 * {@link SimpleDragViewHandler} implementation for {@link WebDocumentPane} document.
 *
 * @param <T> {@link DocumentData} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDocumentPane">How to use WebDocumentPane</a>
 * @see WebDocumentPane
 * @see com.alee.managers.drag.DragManager
 */
public class DocumentDragViewHandler<T extends DocumentData> extends SimpleDragViewHandler<T>
{
    /**
     * {@link WebDocumentPane} using this drag view handler.
     */
    protected final WebDocumentPane documentPane;

    /**
     * Constructs new {@link DocumentDragViewHandler}.
     *
     * @param documentPane {@link WebDocumentPane} using this drag view handler
     */
    public DocumentDragViewHandler ( final WebDocumentPane documentPane )
    {
        super ();
        this.documentPane = documentPane;
    }

    @Override
    public boolean supports ( final T object, final DragSourceDragEvent event )
    {
        return true;
    }

    @Override
    public DataFlavor getObjectFlavor ()
    {
        return DocumentTransferable.dataFlavor;
    }

    @Override
    protected FontMetrics getFontMetrics ( final T document )
    {
        return documentPane.getFontMetrics ( documentPane.getFont () );
    }

    @Override
    protected Icon getIcon ( final T document )
    {
        return document.getIcon ();
    }

    @Override
    protected Color getForeground ( final T document )
    {
        return document.getForeground ();
    }

    @Override
    protected String getText ( final T document )
    {
        return document.getTitle ();
    }
}