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

import com.alee.extended.label.WebStyledLabel;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.checkbox.WebCheckBox;
import com.alee.managers.style.StyleId;
import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.painter.decoration.Stateful;
import com.alee.utils.CompareUtils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Default {@link TableCellRenderer} implementation for {@link Boolean} values.
 *
 * @param <C> table type
 * @author Mikle Garin
 */

public class WebTableBooleanCellRenderer<C extends JTable> extends WebCheckBox implements TableCellRenderer, Stateful
{
    /**
     * Additional renderer decoration states.
     */
    protected final List<String> states;

    /**
     * Constructs new {@link WebTableBooleanCellRenderer}.
     */
    public WebTableBooleanCellRenderer ()
    {
        super ();
        setName ( "Table.cellRenderer" );
        states = new ArrayList<String> ( 3 );
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
    protected void updateStates ( final C table, final Boolean value, final boolean isSelected,
                                  final boolean hasFocus, final int row, final int column )
    {
        states.clear ();

        // Basic states
        states.add ( isSelected ? DecorationState.selected : DecorationState.unselected );

        // Focus state
        if ( hasFocus )
        {
            states.add ( DecorationState.focused );
        }

        // todo Add hover state when WTableUI is available
        // states.add ( DecorationState.hover );
    }

    /**
     * Updates table cell renderer component style ID.
     *
     * @param table      {@link JTable}
     * @param value      cell value
     * @param isSelected whether or not cell is selected
     * @param hasFocus   whether or not cell has focus
     * @param row        cell row number
     * @param column     cell column number
     */
    protected void updateStyleId ( final C table, final Boolean value, final boolean isSelected,
                                   final boolean hasFocus, final int row, final int column )
    {
        setStyleId ( StyleId.tableCellRendererBoolean.at ( table ) );
    }

    /**
     * Updating renderer based on the provided settings.
     *
     * @param table      {@link JTable}
     * @param value      cell value
     * @param isSelected whether or not cell is selected
     * @param hasFocus   whether or not cell has focus
     * @param row        cell row number
     * @param column     cell column number
     */
    protected void updateView ( final C table, final Boolean value, final boolean isSelected,
                                final boolean hasFocus, final int row, final int column )
    {
        setEnabled ( enabledForValue ( table, value, isSelected, hasFocus, row, column ) );
        setComponentOrientation ( orientationForValue ( table, value, isSelected, hasFocus, row, column ) );
        setFont ( fontForValue ( table, value, isSelected, hasFocus, row, column ) );
        setForeground ( foregroundForValue ( table, value, isSelected, hasFocus, row, column ) );
        setSelected ( selectedForValue ( table, value, isSelected, hasFocus, row, column ) );
    }

    /**
     * Returns whether or not renderer for the specified cell value should be enabled.
     *
     * @param table      {@link JTable}
     * @param value      cell value
     * @param isSelected whether or not cell is selected
     * @param hasFocus   whether or not cell has focus
     * @param row        cell row number
     * @param column     cell column number
     * @return {@code true} if renderer for the specified cell value should be enabled, {@code false} otherwise
     */
    protected boolean enabledForValue ( final C table, final Boolean value, final boolean isSelected,
                                        final boolean hasFocus, final int row, final int column )
    {
        return table.isEnabled ();
    }

    /**
     * Returns renderer {@link ComponentOrientation} for the specified cell value.
     *
     * @param table      {@link JTable}
     * @param value      cell value
     * @param isSelected whether or not cell is selected
     * @param hasFocus   whether or not cell has focus
     * @param row        cell row number
     * @param column     cell column number
     * @return renderer {@link ComponentOrientation} for the specified cell value
     */
    protected ComponentOrientation orientationForValue ( final C table, final Boolean value, final boolean isSelected,
                                                         final boolean hasFocus, final int row, final int column )
    {
        return table.getComponentOrientation ();
    }

    /**
     * Returns renderer {@link Font} for the specified cell value.
     *
     * @param table      {@link JTable}
     * @param value      cell value
     * @param isSelected whether or not cell is selected
     * @param hasFocus   whether or not cell has focus
     * @param row        cell row number
     * @param column     cell column number
     * @return renderer {@link Font} for the specified cell value
     */
    protected Font fontForValue ( final C table, final Boolean value, final boolean isSelected,
                                  final boolean hasFocus, final int row, final int column )
    {
        return table.getFont ();
    }

    /**
     * Returns renderer foreground color for the specified cell value.
     *
     * @param table      {@link JTable}
     * @param value      cell value
     * @param isSelected whether or not cell is selected
     * @param hasFocus   whether or not cell has focus
     * @param row        cell row number
     * @param column     cell column number
     * @return renderer foreground color for the specified cell value
     */
    protected Color foregroundForValue ( final C table, final Boolean value, final boolean isSelected,
                                         final boolean hasFocus, final int row, final int column )
    {
        return isSelected ? table.getSelectionForeground () : table.getForeground ();
    }

    /**
     * Returns renderer value for the specified cell.
     *
     * @param table      {@link JTable}
     * @param value      cell value
     * @param isSelected whether or not cell is selected
     * @param hasFocus   whether or not cell has focus
     * @param row        cell row number
     * @param column     cell column number
     * @return renderer value for the specified cell
     */
    protected boolean selectedForValue ( final C table, final Boolean value, final boolean isSelected,
                                         final boolean hasFocus, final int row, final int column )
    {
        return value != null && value;
    }

    @Override
    public Component getTableCellRendererComponent ( final JTable table, final Object value, final boolean isSelected,
                                                     final boolean hasFocus, final int row, final int column )
    {
        // Updating custom states
        updateStates ( ( C ) table, ( Boolean ) value, isSelected, hasFocus, row, column );

        // Updating style ID
        updateStyleId ( ( C ) table, ( Boolean ) value, isSelected, hasFocus, row, column );

        // Updating renderer view
        updateView ( ( C ) table, ( Boolean ) value, isSelected, hasFocus, row, column );

        // Updating decoration states for this render cycle
        DecorationUtils.fireStatesChanged ( this );

        return this;
    }

    @Override
    public void validate ()
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public void invalidate ()
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public void revalidate ()
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public void repaint ( final long tm, final int x, final int y, final int width, final int height )
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public void repaint ( final Rectangle r )
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public void repaint ()
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    protected void firePropertyChange ( final String pn, final Object oldValue, final Object newValue )
    {
        /**
         * Overridden for performance reasons.
         * {@link WebLookAndFeel#BORDER_PROPERTY} is listened to ensure that custom borders are preserved.
         */
        if ( CompareUtils.equals ( pn, StyleId.STYLE_PROPERTY, StyleId.PARENT_STYLE_PROPERTY,
                AbstractDecorationPainter.DECORATION_STATES_PROPERTY, WebStyledLabel.STYLE_RANGES_PROPERTY,
                WebLookAndFeel.TEXT_PROPERTY, WebLookAndFeel.BORDER_PROPERTY, WebLookAndFeel.MODEL_PROPERTY ) )
        {
            super.firePropertyChange ( pn, oldValue, newValue );
        }
        else if ( CompareUtils.equals ( pn, WebLookAndFeel.FONT_PROPERTY, WebLookAndFeel.FOREGROUND_PROPERTY ) &&
                oldValue != newValue && getClientProperty ( BasicHTML.propertyKey ) != null )
        {
            super.firePropertyChange ( pn, oldValue, newValue );
        }
    }

    @Override
    public void firePropertyChange ( final String propertyName, final byte oldValue, final byte newValue )
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public void firePropertyChange ( final String propertyName, final char oldValue, final char newValue )
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public void firePropertyChange ( final String propertyName, final short oldValue, final short newValue )
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public void firePropertyChange ( final String propertyName, final int oldValue, final int newValue )
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public void firePropertyChange ( final String propertyName, final long oldValue, final long newValue )
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public void firePropertyChange ( final String propertyName, final float oldValue, final float newValue )
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public void firePropertyChange ( final String propertyName, final double oldValue, final double newValue )
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public void firePropertyChange ( final String propertyName, final boolean oldValue, final boolean newValue )
    {
        /**
         * Overridden for performance reasons.
         */
    }

    /**
     * A subclass of {@link WebTableBooleanCellRenderer} that implements {@link javax.swing.plaf.UIResource}.
     * It is used to determine cell renderer provided by the UI class to properly uninstall it on UI uninstall.
     *
     * @param <C> table type
     */
    public static class UIResource<C extends JTable> extends WebTableBooleanCellRenderer<C> implements javax.swing.plaf.UIResource
    {
        /**
         * Implementation is used completely from {@link WebTableBooleanCellRenderer}.
         */
    }
}