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
import com.alee.utils.swing.WebDefaultCellEditor;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 31.10.12 Time: 15:36
 */

public class WebBooleanEditor extends WebDefaultCellEditor
{
    public WebBooleanEditor ()
    {
        super ( createEditorComponent () );
        JCheckBox checkBox = ( JCheckBox ) getComponent ();
        checkBox.setHorizontalAlignment ( JCheckBox.CENTER );
        setClickCountToStart ( 1 );
    }

    @Override
    public Component getTableCellEditorComponent ( JTable table, Object value, boolean isSelected, int row, int column )
    {
        final Component editor = super.getTableCellEditorComponent ( table, value, isSelected, row, column );
        editor.setBackground ( table.getSelectionBackground () );
        return editor;
    }

    private static WebCheckBox createEditorComponent ()
    {
        WebCheckBox editor = new WebCheckBox ();
        editor.setAnimated ( false );
        editor.setOpaque ( true );
        editor.setFocusable ( false );
        editor.setShadeWidth ( 0 );
        editor.setIconWidth ( 12 );
        editor.setIconHeight ( 12 );
        return editor;
    }
}
