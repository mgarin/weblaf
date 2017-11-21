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

import com.alee.api.TitleSupport;
import com.alee.managers.language.LM;
import com.alee.managers.language.LanguageSensitive;
import com.alee.utils.CollectionUtils;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
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
     * @return sample short table model
     */
    public static TableModel createShortTableModel ()
    {
        return new SampleTableModel ( 5 );
    }

    /**
     * Returns sample long table model.
     *
     * @return sample long table model
     */
    public static TableModel createLongTableModel ()
    {
        return new SampleTableModel ( 12 );
    }

    /**
     * Sample model for example tables.
     */
    private static class SampleTableModel extends AbstractTableModel implements LanguageSensitive
    {
        /**
         * Table data.
         */
        private static final Object[][] data = new Object[][]{
                { 19, false }, { 32, true }, { 56, false }, { 20, true }, { 14, false }, { 36, false },
                { 18, true }, { 27, false }, { 35, false }, { 26, false }, { 22, false }, { 38, false }
        };

        /**
         * Amount of rows.
         */
        private final int rows;

        /**
         * Constructs new {@link SampleTableModel}.
         *
         * @param rows rows
         */
        public SampleTableModel ( final int rows )
        {
            super ();
            if ( rows > data.length )
            {
                throw new RuntimeException ( "Unsupported amount of rows: " + rows );
            }
            this.rows = rows;
        }

        @Override
        public Class<?> getColumnClass ( final int column )
        {
            switch ( column )
            {
                case 3:
                    return Integer.class;

                case 4:
                    return Boolean.class;

                default:
                    return String.class;
            }
        }

        @Override
        public int getColumnCount ()
        {
            return 5;
        }

        @Override
        public String getColumnName ( final int column )
        {
            switch ( column )
            {
                case 0:
                    return LM.get ( "demo.example.data.grids.data.column.first.name" );

                case 1:
                    return LM.get ( "demo.example.data.grids.data.column.last.name" );

                case 2:
                    return LM.get ( "demo.example.data.grids.data.column.hobby" );

                case 3:
                    return LM.get ( "demo.example.data.grids.data.column.age" );

                case 4:
                    return LM.get ( "demo.example.data.grids.data.column.vegeterian" );

                default:
                    throw new RuntimeException ( "There is no column for index: " + column );
            }
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
                    return LM.getState ( "demo.example.data.grids.data.row." + rowIndex, "first.name" );

                case 1:
                    return LM.getState ( "demo.example.data.grids.data.row." + rowIndex, "last.name" );

                case 2:
                    return LM.getState ( "demo.example.data.grids.data.row." + rowIndex, "hobby" );

                case 3:
                    return data[ rowIndex ][ 0 ];

                case 4:
                    return data[ rowIndex ][ 1 ];

                default:
                    throw new RuntimeException ( "There is no column data for index: " + columnIndex );
            }
        }

        @Override
        public boolean isCellEditable ( final int rowIndex, final int columnIndex )
        {
            return true;
        }

        @Override
        public void setValueAt ( final Object aValue, final int rowIndex, final int columnIndex )
        {
            data[ rowIndex ][ columnIndex ] = aValue;
        }
    }

    /**
     * Returns {@link List} of sample data.
     *
     * @return {@link List} of sample data
     */
    public static List<ListItem> createSampleListData ()
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
    private static class ListItem implements TitleSupport
    {
        /**
         * Item language key.
         */
        private final String language;

        /**
         * Constructs new {@link ListItem}.
         *
         * @param language item language key
         */
        public ListItem ( final String language )
        {
            super ();
            this.language = language;
        }

        @Override
        public String getTitle ()
        {
            return LM.get ( "demo.sample.list." + language );
        }
    }
}