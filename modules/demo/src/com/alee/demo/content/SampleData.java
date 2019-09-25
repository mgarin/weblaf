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

package com.alee.demo.content;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.ui.TextBridge;
import com.alee.demo.content.data.tree.model.SampleAsyncDataProvider;
import com.alee.demo.content.data.tree.model.SampleCustomizedExDataProvider;
import com.alee.demo.content.data.tree.model.SampleExDataProvider;
import com.alee.demo.content.data.tree.model.SampleNode;
import com.alee.extended.tree.AbstractExTreeDataProvider;
import com.alee.extended.tree.AsyncTreeDataProvider;
import com.alee.extended.tree.ExTreeDataProvider;
import com.alee.laf.combobox.WebComboBoxModel;
import com.alee.laf.list.ListCellParameters;
import com.alee.laf.list.WebListModel;
import com.alee.laf.tree.WebTreeModel;
import com.alee.managers.language.LM;
import com.alee.managers.language.LanguageSensitive;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.io.Serializable;
import java.util.List;

/**
 * This utility class provides various sample data for {@link com.alee.demo.DemoApplication}.
 *
 * @author Mikle Garin
 */
public final class SampleData
{
    /**
     * Returns sample short table model.
     *
     * @param editable whether or not model data should be editable
     * @return sample short table model
     */
    public static TableModel createShortTableModel ( final boolean editable )
    {
        return new SampleTableModel ( editable, 5 );
    }

    /**
     * Returns sample long table model.
     *
     * @param editable whether or not model data should be editable
     * @return sample long table model
     */
    public static TableModel createLongTableModel ( final boolean editable )
    {
        return new SampleTableModel ( editable, 12 );
    }

    /**
     * Sample model for example tables.
     */
    private static class SampleTableModel extends AbstractTableModel implements LanguageSensitive
    {
        /**
         * Whether or not model data should be editable.
         */
        private final boolean editable;

        /**
         * Amount of rows.
         */
        private final int rows;

        /**
         * Table data.
         */
        private final Serializable[][] data;

        /**
         * Constructs new {@link SampleTableModel}.
         *
         * @param editable whether or not model data should be editable
         * @param rows     rows
         */
        public SampleTableModel ( final boolean editable, final int rows )
        {
            super ();
            this.editable = editable;
            if ( rows > 12 )
            {
                throw new RuntimeException ( "Unsupported amount of rows: " + rows );
            }
            this.rows = rows;
            this.data = new Serializable[ rows ][ 5 ];
            for ( int row = 0; row < rows; row++ )
            {
                this.data[ row ][ 0 ] = LM.getState ( "demo.example.data.grids.data.row." + row, "first.name" );
                this.data[ row ][ 1 ] = LM.getState ( "demo.example.data.grids.data.row." + row, "last.name" );
                this.data[ row ][ 2 ] = LM.getState ( "demo.example.data.grids.data.row." + row, "hobby" );
                this.data[ row ][ 3 ] = Integer.parseInt ( LM.getState ( "demo.example.data.grids.data.row." + row, "age" ) );
                this.data[ row ][ 4 ] = Boolean.parseBoolean ( LM.getState ( "demo.example.data.grids.data.row." + row, "vegeterian" ) );
            }
        }

        @Override
        public Class<?> getColumnClass ( final int column )
        {
            final Class<?> columnClass;
            switch ( column )
            {
                case 3:
                    columnClass = Integer.class;
                    break;

                case 4:
                    columnClass = Boolean.class;
                    break;

                default:
                    columnClass = String.class;
                    break;
            }
            return columnClass;
        }

        @Override
        public int getColumnCount ()
        {
            return 5;
        }

        @Override
        public String getColumnName ( final int column )
        {
            final String columnName;
            switch ( column )
            {
                case 0:
                    columnName = LM.get ( "demo.example.data.grids.data.column.first.name" );
                    break;

                case 1:
                    columnName = LM.get ( "demo.example.data.grids.data.column.last.name" );
                    break;

                case 2:
                    columnName = LM.get ( "demo.example.data.grids.data.column.hobby" );
                    break;

                case 3:
                    columnName = LM.get ( "demo.example.data.grids.data.column.age" );
                    break;

                case 4:
                    columnName = LM.get ( "demo.example.data.grids.data.column.vegeterian" );
                    break;

                default:
                    throw new RuntimeException ( "There is no column for index: " + column );
            }
            return columnName;
        }

        @Override
        public int getRowCount ()
        {
            return rows;
        }

        @Override
        public Object getValueAt ( final int rowIndex, final int columnIndex )
        {
            switch ( columnIndex )
            {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                    return data[ rowIndex ][ columnIndex ];

                default:
                    throw new RuntimeException ( "There is no column data for index: " + columnIndex );
            }
        }

        @Override
        public boolean isCellEditable ( final int rowIndex, final int columnIndex )
        {
            return editable;
        }

        @Override
        public void setValueAt ( final Object aValue, final int rowIndex, final int columnIndex )
        {
            data[ rowIndex ][ columnIndex ] = ( Serializable ) aValue;
        }
    }

    /**
     * Returns sample {@link WebListModel}.
     *
     * @return sample {@link WebListModel}
     */
    public static WebListModel<ListItem> createListModel ()
    {
        return new WebListModel<ListItem> ( createListData () );
    }

    /**
     * Returns sample {@link WebComboBoxModel}.
     *
     * @return sample {@link WebComboBoxModel}
     */
    public static WebComboBoxModel<ListItem> createComboBoxModel ()
    {
        return new WebComboBoxModel<ListItem> ( createListData () );
    }

    /**
     * Returns {@link List} of sample data.
     *
     * @return {@link List} of sample data
     */
    private static List<ListItem> createListData ()
    {
        return CollectionUtils.asList (
                new ListItem ( "item1" ),
                new ListItem ( "item2" ),
                new ListItem ( "item3" )
        );
    }

    /**
     * Sample list item object.
     * It supports translation based on the provided language key in combination with example key.
     */
    public static class ListItem implements TextBridge<ListCellParameters<ListItem, JList>>
    {
        /**
         * Item language key.
         */
        private final String key;

        /**
         * Constructs new {@link ListItem}.
         *
         * @param key item language key
         */
        public ListItem ( final String key )
        {
            super ();
            this.key = key;
        }

        @Nullable
        @Override
        public String getText ( @NotNull final ListCellParameters<ListItem, JList> parameters )
        {
            return LM.get ( "demo.sample.list." + key );
        }
    }

    /**
     * Returns sample {@link AsyncTreeDataProvider} that delays child nodes loading by random (but reasonable) amount of time.
     *
     * @return sample {@link AsyncTreeDataProvider} that delays child nodes loading by random (but reasonable) amount of time
     */
    public static AsyncTreeDataProvider<SampleNode> createDelayingAsyncDataProvider ()
    {
        return new SampleAsyncDataProvider ();
    }

    /**
     * Returns sample {@link WebTreeModel} for checkbox tree.
     *
     * @return sample {@link WebTreeModel} for checkbox tree
     */
    public static WebTreeModel<SampleNode> createCheckBoxTreeModel ()
    {
        return createExTreeDataProvider ().createPlainModel ();
    }

    /**
     * Returns sample {@link ExTreeDataProvider}.
     *
     * @return sample {@link ExTreeDataProvider}
     */
    public static AbstractExTreeDataProvider<SampleNode> createExTreeDataProvider ()
    {
        return new SampleExDataProvider ();
    }

    /**
     * Returns sample {@link WebTreeModel} for checkbox tree.
     *
     * @return sample {@link WebTreeModel} for checkbox tree
     */
    public static WebTreeModel<SampleNode> createCustomizedCheckBoxTreeModel ()
    {
        return createCustomizedExCheckBoxTreeDataProvider ().createPlainModel ();
    }

    /**
     * Returns sample {@link ExTreeDataProvider} for checkbox tree.
     *
     * @return sample {@link ExTreeDataProvider} for checkbox tree
     */
    public static AbstractExTreeDataProvider<SampleNode> createCustomizedExCheckBoxTreeDataProvider ()
    {
        return new SampleCustomizedExDataProvider ();
    }
}