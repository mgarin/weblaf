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

package com.alee.laf.table.renderers;

import com.alee.laf.label.WebLabel;
import com.alee.managers.style.StyleId;

import javax.swing.*;
import javax.swing.plaf.UIResource;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

/**
 * @author Mikle Garin
 */

public class WebTableHeaderCellRenderer extends WebLabel implements TableCellRenderer, UIResource
{
    private boolean horizontalTextPositionSet;

    public WebTableHeaderCellRenderer ()
    {
        super ();
    }

    @Override
    public void setHorizontalTextPosition ( final int textPosition )
    {
        horizontalTextPositionSet = true;
        super.setHorizontalTextPosition ( textPosition );
    }

    @Override
    public Component getTableCellRendererComponent ( final JTable table, final Object value, final boolean isSelected,
                                                     final boolean hasFocus, final int row, final int column )
    {
        // Whether we are printing the result or not
        boolean isPaintingForPrint = false;

        // Updating custom style ID
        setStyleId ( StyleId.tableHeaderCellRenderer.at ( table.getTableHeader () ) );

        // Title icon
        Icon sortIcon = null;
        if ( table != null )
        {
            final JTableHeader header = table.getTableHeader ();
            if ( header != null )
            {
                Color fgColor = null;
                Color bgColor = null;
                if ( hasFocus )
                {
                    fgColor = UIManager.getColor ( "TableHeader.focusCellForeground" );
                    bgColor = UIManager.getColor ( "TableHeader.focusCellBackground" );
                }
                if ( fgColor == null )
                {
                    fgColor = header.getForeground ();
                }
                if ( bgColor == null )
                {
                    bgColor = header.getBackground ();
                }
                setForeground ( fgColor );
                setBackground ( bgColor );

                setFont ( header.getFont () );

                isPaintingForPrint = header.isPaintingForPrint ();
            }

            if ( !isPaintingForPrint && table.getRowSorter () != null )
            {
                if ( !horizontalTextPositionSet )
                {
                    // There is a row sorter, and the developer hasn't
                    // set a text position, change to leading.
                    setHorizontalTextPosition ( JLabel.LEADING );
                }
                final List<? extends RowSorter.SortKey> sortKeys = table.getRowSorter ().getSortKeys ();
                if ( sortKeys.size () > 0 && sortKeys.get ( 0 ).getColumn () == table.convertColumnIndexToModel ( column ) )
                {
                    switch ( sortKeys.get ( 0 ).getSortOrder () )
                    {
                        case ASCENDING:
                            sortIcon = UIManager.getIcon ( "Table.ascendingSortIcon" );
                            break;
                        case DESCENDING:
                            sortIcon = UIManager.getIcon ( "Table.descendingSortIcon" );
                            break;
                        case UNSORTED:
                            sortIcon = UIManager.getIcon ( "Table.naturalSortIcon" );
                            break;
                    }
                }
            }
        }
        setIcon ( sortIcon );

        // Title text
        setText ( value == null ? "" : value.toString () );

        return this;
    }

    public static class UIResource extends WebTableHeaderCellRenderer implements javax.swing.plaf.UIResource
    {
    }
}