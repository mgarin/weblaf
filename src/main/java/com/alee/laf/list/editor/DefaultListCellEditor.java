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

import com.alee.laf.list.WebListStyle;
import com.alee.laf.text.WebTextField;

import javax.swing.*;
import java.awt.*;

/**
 * Default list cell editor that is based on various components.
 *
 * @author Mikle Garin
 * @since 1.3
 */

public class DefaultListCellEditor extends AbstractListCellEditor
{
    // todo WebComboBox/WebCheckBox-based default editor

    /**
     * Creates list cell editor component for the cell nder specified index.
     *
     * @param list  list to process
     * @param index cell index
     * @param value cell value
     * @return list cell editor created for the cell under specified index
     */
    protected Component createCellEditor ( JList list, int index, Object value )
    {
        WebTextField field = WebTextField.createWebTextField ( true, WebListStyle.selectionRound, WebListStyle.selectionShadeWidth );
        field.setDrawFocus ( false );
        field.setText ( value != null ? value.toString () : "" );
        field.selectAll ();
        return field;
    }

    /**
     * Returns editor value that will replace the specified old value in the model.
     *
     * @param list     list to process
     * @param index    cell index
     * @param oldValue old cell value
     * @return editor value
     */
    public Object getCellEditorValue ( JList list, int index, Object oldValue )
    {
        return ( ( WebTextField ) editor ).getText ();
    }
}