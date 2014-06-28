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

package com.alee.examples.groups.table;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.table.WebTable;
import com.alee.laf.table.renderers.WebTableCellRenderer;
import com.alee.utils.swing.WebDefaultCellEditor;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;

/**
 * User: mgarin Date: 30.01.12 Time: 16:14
 */

public class TableExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Editable table";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled editable table";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Table
        WebTable table = new WebTable ( new ExampleTableModel () );
        WebScrollPane scrollPane = new WebScrollPane ( table );

        // Custom column
        TableColumn column = table.getColumnModel ().getColumn ( 1 );

        // Custom renderer
        WebTableCellRenderer renderer = new WebTableCellRenderer ();
        renderer.setToolTipText ( "Click for combo box" );
        column.setCellRenderer ( renderer );

        // Custom editor
        JComboBox comboBox = new JComboBox ();
        comboBox.addItem ( "Snowboarding" );
        comboBox.addItem ( "Rowing" );
        comboBox.addItem ( "Knitting" );
        comboBox.addItem ( "Speed reading" );
        comboBox.addItem ( "Pool" );
        comboBox.addItem ( "None of the above" );
        column.setCellEditor ( new WebDefaultCellEditor ( comboBox ) );

        // Better column sizes
        initColumnSizes ( table );

        return scrollPane;
    }

    private void initColumnSizes ( JTable table )
    {
        ExampleTableModel model = ( ExampleTableModel ) table.getModel ();
        TableColumn column;
        Component comp;
        int headerWidth;
        int cellWidth;
        Object[] longValues = model.longValues;
        TableCellRenderer headerRenderer = table.getTableHeader ().getDefaultRenderer ();

        for ( int i = 0; i < model.getColumnCount (); i++ )
        {
            column = table.getColumnModel ().getColumn ( i );

            comp = headerRenderer.getTableCellRendererComponent ( null, column.getHeaderValue (), false, false, 0, 0 );
            headerWidth = comp.getPreferredSize ().width;

            comp = table.getDefaultRenderer ( model.getColumnClass ( i ) ).
                    getTableCellRendererComponent ( table, longValues[ i ], false, false, 0, i );
            cellWidth = comp.getPreferredSize ().width;

            column.setPreferredWidth ( Math.max ( headerWidth, cellWidth ) );
        }
    }

    public class ExampleTableModel extends AbstractTableModel
    {
        private String[] columnNames = { "Name", "Sport", "# of Years", "Vegetarian" };
        private Object[][] data = { { "Kathy", "Snowboarding", 5, false }, { "John", "Rowing", 3, true }, { "Sue", "Knitting", 2, false },
                { "Jane", "Speed reading", 20, true }, { "Joe", "Pool", 10, false } };

        public final Object[] longValues = { "Jane", "None of the above", 20, Boolean.TRUE };

        @Override
        public int getColumnCount ()
        {
            return columnNames.length;
        }

        @Override
        public int getRowCount ()
        {
            return data.length;
        }

        @Override
        public String getColumnName ( int col )
        {
            return columnNames[ col ];
        }

        @Override
        public Object getValueAt ( int row, int col )
        {
            return data[ row ][ col ];
        }

        @Override
        public Class getColumnClass ( int c )
        {
            return longValues[ c ].getClass ();
        }

        @Override
        public boolean isCellEditable ( int row, int col )
        {
            return col >= 1;
        }

        @Override
        public void setValueAt ( Object value, int row, int col )
        {
            data[ row ][ col ] = value;
            fireTableCellUpdated ( row, col );
        }
    }
}