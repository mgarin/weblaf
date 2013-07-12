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
import com.alee.managers.hotkey.HotkeyManager;
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
 * @since 1.4
 */

public class WebFileListCellEditor extends AbstractListCellEditor<WebTextField, FileElement>
{
    /**
     * Last saved selection.
     */
    private Object savedSelection = null;

    /**
     * Installs start edit actions in the list.
     *
     * @param list list to process
     */
    protected void installStartEditActions ( final JList list )
    {
        keyAdapter = new KeyAdapter ()
        {
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
     * Uninstalls start edit actions from the list.
     *
     * @param list list to process
     */
    protected void uninstallStartEditActions ( JList list )
    {
        list.removeKeyListener ( keyAdapter );
    }

    /**
     * Returns whether list cell under the specified index is editable or not.
     *
     * @param list  list to process
     * @param index cell index
     * @param value cell value
     * @return whether list cell under the specified index is editable or not
     */
    public boolean isCellEditable ( JList list, int index, FileElement value )
    {
        // Check if file can be edited
        File file = value.getFile ();
        return value != null && file.getParentFile () != null && file.canWrite () && file.getParentFile ().canWrite () &&
                super.isCellEditable ( list, index, value );
    }

    /**
     * Creates list cell editor component for the cell nder specified index.
     *
     * @param list  list to process
     * @param index cell index
     * @param value cell value
     * @return list cell editor created for the cell under specified index
     */
    protected WebTextField createCellEditor ( JList list, int index, FileElement value )
    {
        File file = value.getFile ();
        String name = file.getName ();

        WebTextField editor = WebTextField.createWebTextField ( true, WebListStyle.selectionRound, WebListStyle.selectionShadeWidth );
        editor.setDrawFocus ( false );
        editor.setText ( name );
        editor.setSelectionStart ( 0 );
        editor.setSelectionEnd ( file.isDirectory () ? name.length () : FileUtils.getFileNamePart ( name ).length () );

        if ( list instanceof WebFileList )
        {
            final boolean tiles = ( ( WebFileList ) list ).getFileViewType ().equals ( FileViewType.tiles );
            editor.setHorizontalAlignment ( tiles ? WebTextField.LEFT : WebTextField.CENTER );
        }

        return editor;
    }

    /**
     * Returns list cell editor bounds within the cell.
     *
     * @param list       list to process
     * @param index      cell index
     * @param value      cell value
     * @param cellBounds cell bounds
     * @return list cell editor bounds within the list
     */
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
     * Returns editor value that will replace the specified old value in the model.
     *
     * @param list     list to process
     * @param index    cell index
     * @param oldValue old cell value
     * @return editor value
     */
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
     * Returns whether value update operation completed successfully or not.
     * Basically this method should replace old value with a new one in list model and update list view.
     *
     * @param list            list to process
     * @param index           cell index
     * @param oldValue        old cell value
     * @param newValue        new cell value
     * @param updateSelection whether update list selection or not
     * @return true if list model was updated
     */
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

    /**
     * Notifies that list cell editing has started.
     *
     * @param list  list to process
     * @param index edited cell index
     */
    public void editStarted ( JList list, int index )
    {
        // todo Better way to avoid hotkeys interception
        // Temporary workaround to disable hotkeys like DELETE and such from working outside the editor
        HotkeyManager.disableHotkeys ();

        super.editStarted ( list, index );
    }

    /**
     * Notifies that list cell editing has finished.
     *
     * @param list     list to process
     * @param index    edited cell index
     * @param oldValue old cell value
     * @param newValue new cell value
     */
    public void editStopped ( JList list, int index, FileElement oldValue, FileElement newValue )
    {
        // todo Better way to avoid hotkeys interception
        // Temporary workaround to disable hotkeys like DELETE and such from working outside the editor
        HotkeyManager.enableHotkeys ();

        super.editStopped ( list, index, oldValue, newValue );
    }

    /**
     * Notifies that list cell editing was cancelled.
     *
     * @param list  list to process
     * @param index edited cell index
     */
    public void editCancelled ( JList list, int index )
    {
        // todo Better way to avoid hotkeys interception
        // Temporary workaround to disable hotkeys like DELETE and such from working outside the editor
        HotkeyManager.enableHotkeys ();

        super.editCancelled ( list, index );
    }
}