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

package com.alee.laf.table.editors;

import com.alee.laf.checkbox.WebCheckBox;
import com.alee.managers.style.StyleId;
import com.alee.utils.swing.WebDefaultCellEditor;

import javax.swing.*;
import java.awt.*;

/**
 * @author Mikle Garin
 */

public class WebBooleanEditor extends WebDefaultCellEditor<WebCheckBox>
{
    public WebBooleanEditor ()
    {
        super ( new WebCheckBox () );
        setClickCountToStart ( 1 );
    }

    @Override
    public Component getTableCellEditorComponent ( final JTable table, final Object value, final boolean isSelected, final int row,
                                                   final int column )
    {
        editorComponent.setStyleId ( StyleId.tableBooleanCellEditor.at ( table ) );

        final Component editor = super.getTableCellEditorComponent ( table, value, isSelected, row, column );
        editor.setBackground ( table.getSelectionBackground () );
        return editor;
    }
}
