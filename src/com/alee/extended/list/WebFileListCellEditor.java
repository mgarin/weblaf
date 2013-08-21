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

import com.alee.laf.list.WebListStyle;
import com.alee.laf.list.editor.AbstractListCellEditor;
import com.alee.laf.text.WebTextField;
import com.alee.managers.hotkey.Hotkey;
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
    private Object savedSelection = null;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void installStartEditActions ( final JList list )
    {
        keyAdapter = new KeyAdapter ()
        {
            @Override
            public void keyReleased ( KeyEvent e )
            {
                if ( Hotkey.F2.isTriggered ( e ) )
                {
                    startEdit ( list, list.getSelectedIndex () );
                }
            }
        };
        list.addKeyListener ( keyAdapter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void uninstallStartEditActions ( JList list )
    {
        list.removeKeyListener ( keyAdapter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCellEditable ( JList list, int index, FileElement value )
    {
        File file = value != null ? value.getFile () : null;
        return file != null && FileUtils.isNameEditable ( file ) && super.isCellEditable ( list, index, value );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected WebTextField createCellEditor ( JList list, int index, FileElement value )
    {
        WebTextField editor = WebTextField.createWebTextField ( true, WebListStyle.selectionRound, WebListStyle.selectionShadeWidth );
        editor.setDrawFocus ( false );
        FileUtils.displayFileName ( editor, value.getFile () );

        if ( list instanceof WebFileList )
        {
            final boolean tiles = ( ( WebFileList ) list ).getFileListViewType ().equals ( FileListViewType.tiles );
            editor.setHorizontalAlignment ( tiles ? WebTextField.LEFT : WebTextField.CENTER );
        }

        return editor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Rectangle getEditorBounds ( JList list, int index, FileElement value, Rectangle cellBounds )
    {
        if ( list instanceof WebFileList )
        {
            WebFileListCellRenderer cellRenderer = ( ( WebFileList ) list ).getWebFileListCellRenderer ();
            Rectangle dpBounds = cellRenderer.getDescriptionPanel ().getBounds ();
            Dimension size = editor.getPreferredSize ();
            return new Rectangle ( dpBounds.x, dpBounds.y + dpBounds.height / 2 - size.height / 2, dpBounds.width, size.height );
        }
        else
        {
            return super.getEditorBounds ( list, index, value, cellBounds );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileElement getCellEditorValue ( JList list, int index, FileElement oldValue )
    {
        // Saving initial selection
        savedSelection = list.getSelectedValue ();

        // Finishing edit
        File renamed = new File ( oldValue.getFile ().getParent (), editor.getText () );
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean updateListModel ( JList list, int index, FileElement oldValue, FileElement newValue, boolean updateSelection )
    {
        // Updating model
        if ( list.getModel () instanceof FileListModel )
        {
            FileListModel model = ( FileListModel ) list.getModel ();

            // If name was actually changed
            if ( !oldValue.getFile ().getAbsolutePath ().equals ( newValue.getFile ().getAbsolutePath () ) )
            {
                // Updating model value
                model.setElementAt ( newValue, index );

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