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

package com.alee.extended.list;

import com.alee.laf.list.editor.AbstractListCellEditor;
import com.alee.laf.text.WebTextField;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.style.StyleId;
import com.alee.utils.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

/**
 * Custom list cell editor used in WebFileList component.
 *
 * @author Mikle Garin
 */

public class WebFileListCellEditor extends AbstractListCellEditor<WebTextField, FileElement>
{
    /**
     * Last saved selection.
     */
    protected Object savedSelection = null;

    @Override
    protected void installStartEditActions ( final JList list )
    {
        keyAdapter = new KeyAdapter ()
        {
            @Override
            public void keyReleased ( final KeyEvent e )
            {
                if ( Hotkey.F2.isTriggered ( e ) )
                {
                    startEdit ( list, list.getSelectedIndex () );
                }
            }
        };
        list.addKeyListener ( keyAdapter );
    }

    @Override
    protected void uninstallStartEditActions ( final JList list )
    {
        list.removeKeyListener ( keyAdapter );
    }

    @Override
    public boolean isCellEditable ( final JList list, final int index, final FileElement value )
    {
        final File file = value != null ? value.getFile () : null;
        return file != null && FileUtils.isNameEditable ( file ) && super.isCellEditable ( list, index, value );
    }

    @Override
    protected WebTextField createCellEditor ( final JList list, final int index, final FileElement value )
    {
        final WebTextField editor = new WebTextField ( StyleId.filelistCellEditor.at ( list ) );
        FileUtils.displayFileName ( editor, value.getFile () );

        if ( list instanceof WebFileList )
        {
            final boolean tiles = ( ( WebFileList ) list ).getFileListViewType ().equals ( FileListViewType.tiles );
            editor.setHorizontalAlignment ( tiles ? WebTextField.LEFT : WebTextField.CENTER );
        }

        return editor;
    }

    @Override
    protected Rectangle getEditorBounds ( final JList list, final int index, final FileElement value, final Rectangle cellBounds )
    {
        if ( list instanceof WebFileList )
        {
            final WebFileListCellRenderer cellRenderer = ( ( WebFileList ) list ).getWebFileListCellRenderer ();
            final Rectangle dpBounds = cellRenderer.getDescriptionBounds ();
            final Dimension size = editor.getPreferredSize ();
            return new Rectangle ( dpBounds.x, dpBounds.y + dpBounds.height / 2 - size.height / 2, dpBounds.width, size.height );
        }
        else
        {
            return super.getEditorBounds ( list, index, value, cellBounds );
        }
    }

    @Override
    public FileElement getCellEditorValue ( final JList list, final int index, final FileElement oldValue )
    {
        // Saving initial selection
        savedSelection = list.getSelectedValue ();

        // Finishing edit
        final File renamed = new File ( oldValue.getFile ().getParent (), editor.getText () );
        if ( oldValue.getFile ().renameTo ( renamed ) )
        {
            if ( savedSelection == oldValue )
            {
                savedSelection = renamed;
            }
            return new FileElement ( renamed );
        }
        else
        {
            return oldValue;
        }
    }

    @Override
    public boolean updateListModel ( final JList list, final int index, final FileElement oldValue, final FileElement newValue,
                                     final boolean updateSelection )
    {
        // Updating model
        if ( list.getModel () instanceof FileListModel )
        {
            final FileListModel model = ( FileListModel ) list.getModel ();

            // If name was actually changed
            if ( !oldValue.getFile ().getAbsolutePath ().equals ( newValue.getFile ().getAbsolutePath () ) )
            {
                // Updating model value
                model.set ( index, newValue );

                // Updating list
                if ( savedSelection != null )
                {
                    list.setSelectedValue ( savedSelection, true );
                }
                else
                {
                    list.clearSelection ();
                }
                list.repaint ();
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return super.updateListModel ( list, index, oldValue, newValue, updateSelection );
        }
    }
}