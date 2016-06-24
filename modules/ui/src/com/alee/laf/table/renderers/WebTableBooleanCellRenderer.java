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

import com.alee.laf.checkbox.WebCheckBox;
import com.alee.managers.style.StyleId;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.painter.decoration.Stateful;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Default boolean table cell renderer for WebLaF tables.
 *
 * @author Mikle Garin
 */

public class WebTableBooleanCellRenderer extends WebCheckBox implements TableCellRenderer, Stateful
{
    /**
     * Additional renderer decoration states.
     */
    protected final List<String> states = new ArrayList<String> ( 3 );

    /**
     * Constructs default boolean table cell renderer.
     */
    public WebTableBooleanCellRenderer ()
    {
        super ();
        setName ( "Table.cellRenderer" );
    }

    @Override
    public List<String> getStates ()
    {
        return states;
    }

    /**
     * Updates custom renderer states based on render cycle settings.
     *
     * @param table      table
     * @param value      cell value
     * @param isSelected whether or not cell is selected
     * @param hasFocus   whether or not cell has focus
     * @param row        cell row number
     * @param column     cell column number
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected void updateStates ( final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row,
                                  final int column )
    {
        states.clear ();
        if ( isSelected )
        {
            states.add ( DecorationState.selected );
        }
        if ( hasFocus )
        {
            states.add ( DecorationState.focused );
        }
        if ( value instanceof Stateful )
        {
            states.addAll ( ( ( Stateful ) value ).getStates () );
        }
    }

    /**
     * Updates table cell renderer component style ID.
     *
     * @param table      table
     * @param value      cell value
     * @param isSelected whether or not cell is selected
     * @param hasFocus   whether or not cell has focus
     * @param row        cell row number
     * @param column     cell column number
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected void updateStyleId ( final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row,
                                   final int column )
    {
        setStyleId ( StyleId.tableCellRendererBoolean.at ( table ) );
    }

    /**
     * Updating renderer based on the provided settings.
     *
     * @param table      table
     * @param value      cell value
     * @param isSelected whether or not cell is selected
     * @param hasFocus   whether or not cell has focus
     * @param row        cell row number
     * @param column     cell column number
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected void updateView ( final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row,
                                final int column )
    {
        // Updating renderer visual settings
        setEnabled ( table.isEnabled () );
        setFont ( table.getFont () );
        setComponentOrientation ( table.getComponentOrientation () );

        // Updating selection
        setSelected ( value != null && ( Boolean ) value );
    }

    @Override
    public Component getTableCellRendererComponent ( final JTable table, final Object value, final boolean isSelected,
                                                     final boolean hasFocus, final int row, final int column )
    {
        // Updating custom states
        updateStates ( table, value, isSelected, hasFocus, row, column );

        // Updating style ID
        updateStyleId ( table, value, isSelected, hasFocus, row, column );

        // Updating renderer view
        updateView ( table, value, isSelected, hasFocus, row, column );

        // Updating decoration states for this render cycle
        DecorationUtils.fireStatesChanged ( this );

        return this;
    }

    /**
     * A subclass of {@link com.alee.laf.table.renderers.WebTableBooleanCellRenderer} that implements {@link javax.swing.plaf.UIResource}.
     * It is used to determine cell renderer provided by the UI class to properly uninstall it on UI uninstall.
     */
    public static class UIResource extends WebTableBooleanCellRenderer implements javax.swing.plaf.UIResource
    {
        /**
         * Implementation is used completely from {@link com.alee.laf.table.renderers.WebTableBooleanCellRenderer}.
         */
    }
}