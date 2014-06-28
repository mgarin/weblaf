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

import com.alee.utils.swing.WebDefaultCellEditor;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Constructor;

/**
 * User: mgarin Date: 31.10.12 Time: 15:30
 */

public class WebGenericEditor extends WebDefaultCellEditor
{
    private Class[] argTypes = new Class[]{ String.class };
    private Constructor constructor;
    private Object value;

    public WebGenericEditor ()
    {
        super ( new GenericCellEditor () );
        getComponent ().setName ( "Table.editor" );
    }

    @Override
    public boolean stopCellEditing ()
    {
        Component component = getComponent ();
        try
        {
            value = constructor.newInstance ( super.getCellEditorValue () );
            updateValidationState ( component, true );
            return super.stopCellEditing ();
        }
        catch ( Exception e )
        {
            updateValidationState ( component, false );
            return false;
        }
    }

    @Override
    public Component getTableCellEditorComponent ( JTable table, Object value, boolean isSelected, int row, int column )
    {
        this.value = null;
        try
        {
            Class type = table.getColumnClass ( column );
            if ( type == Object.class )
            {
                type = String.class;
            }
            constructor = type.getConstructor ( argTypes );
        }
        catch ( Exception e )
        {
            return null;
        }

        Component cellEditorComponent = super.getTableCellEditorComponent ( table, value, isSelected, row, column );
        updateValidationState ( cellEditorComponent, true );
        return cellEditorComponent;
    }

    private void updateValidationState ( Component component, boolean valid )
    {
        if ( component instanceof GenericCellEditor )
        {
            ( ( GenericCellEditor ) component ).setInvalidValue ( !valid );
        }
    }

    @Override
    public Object getCellEditorValue ()
    {
        return value;
    }
}