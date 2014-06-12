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

package com.alee.laf.list.editor;

import javax.swing.*;
import java.awt.*;

/**
 * This interface provides base methods for list cell editor creation.
 * Cell editor is not available in Swing for JList, this a custom WebLaF-exclusive editor.
 *
 * @param <E> Editor component type
 * @param <T> Editor value type
 * @author Mikle Garin
 */

public interface ListCellEditor<E extends Component, T>
{
    /**
     * Installs cell editor in the list.
     * This method should add all required listeners in the list that will cause editing to start.
     *
     * @param list list to process
     */
    public void install ( JList list );

    /**
     * Uninstalls cell editor from the list.
     * This method should remove all listeners from the list and cleanup all associated resources.
     *
     * @param list list to process
     */
    public void uninstall ( JList list );

    /**
     * Returns whether list cell under the specified index is editable or not.
     *
     * @param list  list to process
     * @param index cell index
     * @param value cell value
     * @return whether list cell under the specified index is editable or not
     */
    public boolean isCellEditable ( JList list, int index, T value );

    /**
     * Returns list cell editor created for the cell under specified index.
     *
     * @param list  list to process
     * @param index cell index
     * @param value cell value
     * @return list cell editor created for the cell under specified index
     */
    public E getCellEditor ( JList list, int index, T value );

    /**
     * Starts specified list cell editing.
     *
     * @param list  list to process
     * @param index cell index
     */
    public void startEdit ( JList list, int index );

    /**
     * Cancels list cell editing.
     *
     * @param list list to process
     */
    public void cancelEdit ( JList list );

    /**
     * Stops list cell editing.
     * Usually if value did not change editCancelled event will be thrown, but that depends on implementation.
     *
     * @param list list to process
     * @return true if cell editing was stopped or cancelled, false otherwise
     */
    public boolean stopEdit ( JList list );

    /**
     * Returns editor value that will replace the specified old value in the model.
     *
     * @param list     list to process
     * @param index    cell index
     * @param oldValue old cell value
     * @return editor value
     */
    public T getCellEditorValue ( JList list, int index, T oldValue );

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
    public boolean updateListModel ( JList list, int index, T oldValue, T newValue, boolean updateSelection );

    /**
     * Notifies that list cell editing has started.
     *
     * @param list  list to process
     * @param index edited cell index
     */
    public void editStarted ( JList list, int index );

    /**
     * Notifies that list cell editing has finished.
     *
     * @param list     list to process
     * @param index    edited cell index
     * @param oldValue old cell value
     * @param newValue new cell value
     */
    public void editStopped ( JList list, int index, T oldValue, T newValue );

    /**
     * Notifies that list cell editing was cancelled.
     *
     * @param list  list to process
     * @param index edited cell index
     */
    public void editCancelled ( JList list, int index );

    /**
     * Returns whether editor is currently active or not.
     *
     * @return true if editor is currently active, false otherwise
     */
    public boolean isEditing ();
}