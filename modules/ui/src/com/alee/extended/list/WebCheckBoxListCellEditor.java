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
import com.alee.managers.style.StyleId;

import javax.swing.*;
import java.awt.*;

/**
 * Custom list cell editor used in WebCheckBoxListCell component.
 *
 * @author Mikle Garin
 */

public class WebCheckBoxListCellEditor extends AbstractListCellEditor<WebTextField, CheckBoxCellData>
{
    /**
     * Creates list cell editor component for the cell under specified index.
     *
     * @param list  list to process
     * @param index cell index
     * @param value cell value
     * @return list cell editor created for the cell under specified index
     */
    @Override
    protected WebTextField createCellEditor ( final JList list, final int index, final CheckBoxCellData value )
    {
        final WebTextField field = new WebTextField ( StyleId.checkboxlistCellEditor.at ( list ) );
        field.setText ( value.getUserObject () != null ? value.getUserObject ().toString () : "" );
        field.selectAll ();
        return field;
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
    @Override
    protected Rectangle getEditorBounds ( final JList list, final int index, final CheckBoxCellData value, final Rectangle cellBounds )
    {
        final WebCheckBoxListElement element = ( ( WebCheckBoxList ) list ).getWebCheckBoxListCellRenderer ().getElement ( list, value );
        final Rectangle ir = element.getWebUI ().getIconRect ();
        final int shear = ir.x + ir.width + element.getIconTextGap () - editor.getInsets ().left;
        return new Rectangle ( shear, 0, cellBounds.width - shear, cellBounds.height );
    }

    /**
     * Returns editor value that will replace the specified old value in the model.
     *
     * @param list     list to process
     * @param index    cell index
     * @param oldValue old cell value
     * @return editor value
     */
    @Override
    public CheckBoxCellData getCellEditorValue ( final JList list, final int index, final CheckBoxCellData oldValue )
    {
        oldValue.setUserObject ( editor.getText () );
        return oldValue;
    }
}