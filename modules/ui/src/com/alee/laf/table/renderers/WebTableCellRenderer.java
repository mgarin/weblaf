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

import com.alee.api.*;
import com.alee.extended.label.WebStyledLabel;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.style.ChildStyleId;
import com.alee.managers.style.StyleId;
import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.painter.decoration.Stateful;
import com.alee.utils.CompareUtils;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Default {@link TableCellRenderer} implementation based on {@link WebStyledLabel}.
 * Unlike {@link javax.swing.table.DefaultTableCellRenderer} it contains multiple methods for convenient renderer customization.
 * Also since it is based on {@link WebStyledLabel} it retains all of its extra features.
 *
 * @author Mikle Garin
 */

public class WebTableCellRenderer extends WebStyledLabel implements TableCellRenderer, Stateful
{
    /**
     * todo 1. Add generic type for values
     */

    /**
     * Additional renderer decoration states.
     */
    protected final List<String> states;

    /**
     * Constructs default table cell renderer.
     */
    public WebTableCellRenderer ()
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
    @SuppressWarnings ( "UnusedParameters" )
    protected void updateStates ( final JTable table, final Object value, final boolean isSelected,
                                  final boolean hasFocus, final int row, final int column )
    {
        // Resetting states
        states.clear ();

        // Selection state
        states.add ( isSelected ? DecorationState.selected : DecorationState.unselected );

        // Focus state
        if ( hasFocus )
        {
            states.add ( DecorationState.focused );
        }

        // todo Add hover state when WTableUI is available
        // states.add ( DecorationState.hover );

        // Extra states provided by value
        states.addAll ( DecorationUtils.getExtraStates ( value ) );
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
    protected void updateStyleId ( final JTable table, final Object value, final boolean isSelected,
                                   final boolean hasFocus, final int row, final int column )
    {
        StyleId id = null;
        if ( value instanceof ChildStyleSupport )
        {
            final ChildStyleId childStyleId = ( ( ChildStyleSupport ) value ).getChildStyleId ();
            if ( childStyleId != null )
            {
                id = childStyleId.at ( table );
            }
        }
        else if ( value instanceof StyleSupport )
        {
            final StyleId styleId = ( ( StyleSupport ) value ).getStyleId ();
            if ( styleId != null )
            {
                id = styleId;
            }
        }
        if ( id == null )
        {
            id = StyleId.tableCellRenderer.at ( table );
        }
        setStyleId ( id );
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
    protected void updateView ( final JTable table, final Object value, final boolean isSelected,
                                final boolean hasFocus, final int row, final int column )
    {
        // Updating renderer visual settings
        setEnabled ( enabledForValue ( table, value, isSelected, hasFocus, row, column ) );
        setComponentOrientation ( orientationForValue ( table, value, isSelected, hasFocus, row, column ) );
        setFont ( fontForValue ( table, value, isSelected, hasFocus, row, column ) );
        setForeground ( foregroundForValue ( table, value, isSelected, hasFocus, row, column ) );
        setIcon ( iconForValue ( table, value, isSelected, hasFocus, row, column ) );
        setText ( textForValue ( table, value, isSelected, hasFocus, row, column ) );
    }

    /**
     * Returns whether or not renderer for the specified cell value should be enabled.
     *
     * @param table      table
     * @param value      cell value
     * @param isSelected whether or not cell is selected
     * @param hasFocus   whether or not cell has focus
     * @param row        cell row number
     * @param column     cell column number
     * @return {@code true} if renderer for the specified cell value should be enabled, {@code false} otherwise
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected boolean enabledForValue ( final JTable table, final Object value, final boolean isSelected,
                                        final boolean hasFocus, final int row, final int column )
    {
        return table.isEnabled ();
    }

    /**
     * Returns renderer {@link ComponentOrientation} for the specified cell value.
     *
     * @param table      table
     * @param value      cell value
     * @param isSelected whether or not cell is selected
     * @param hasFocus   whether or not cell has focus
     * @param row        cell row number
     * @param column     cell column number
     * @return renderer {@link ComponentOrientation} for the specified cell value
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected ComponentOrientation orientationForValue ( final JTable table, final Object value, final boolean isSelected,
                                                         final boolean hasFocus, final int row, final int column )
    {
        return table.getComponentOrientation ();
    }

    /**
     * Returns renderer {@link Font} for the specified cell value.
     *
     * @param table      table
     * @param value      cell value
     * @param isSelected whether or not cell is selected
     * @param hasFocus   whether or not cell has focus
     * @param row        cell row number
     * @param column     cell column number
     * @return renderer {@link Font} for the specified cell value
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected Font fontForValue ( final JTable table, final Object value, final boolean isSelected,
                                  final boolean hasFocus, final int row, final int column )
    {
        return table.getFont ();
    }

    /**
     * Returns renderer foreground color for the specified cell value.
     *
     * @param table      table
     * @param value      cell value
     * @param isSelected whether or not cell is selected
     * @param hasFocus   whether or not cell has focus
     * @param row        cell row number
     * @param column     cell column number
     * @return renderer foreground color for the specified cell value
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected Color foregroundForValue ( final JTable table, final Object value, final boolean isSelected,
                                         final boolean hasFocus, final int row, final int column )
    {
        final Color foreground;
        if ( value instanceof ColorSupport )
        {
            final Color color = ( ( ColorSupport ) value ).getColor ();
            foreground = color != null ? color : isSelected ? table.getSelectionForeground () : table.getForeground ();
        }
        else
        {
            foreground = isSelected ? table.getSelectionForeground () : table.getForeground ();
        }
        return foreground;
    }

    /**
     * Returns renderer icon for the specified cell value.
     *
     * @param table      table
     * @param value      cell value
     * @param isSelected whether or not cell is selected
     * @param hasFocus   whether or not cell has focus
     * @param row        cell row number
     * @param column     cell column number
     * @return renderer icon for the specified cell value
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected Icon iconForValue ( final JTable table, final Object value, final boolean isSelected,
                                  final boolean hasFocus, final int row, final int column )
    {
        final Icon icon;
        if ( value instanceof IconSupport )
        {
            icon = ( ( IconSupport ) value ).getIcon ();
        }
        else
        {
            icon = value instanceof Icon ? ( Icon ) value : null;
        }
        return icon;
    }

    /**
     * Returns renderer text for the specified cell value.
     *
     * @param table      table
     * @param value      cell value
     * @param isSelected whether or not cell is selected
     * @param hasFocus   whether or not cell has focus
     * @param row        cell row number
     * @param column     cell column number
     * @return renderer text for the specified cell value
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected String textForValue ( final JTable table, final Object value, final boolean isSelected,
                                    final boolean hasFocus, final int row, final int column )
    {
        final String text;
        if ( value instanceof TitleSupport )
        {
            text = ( ( TitleSupport ) value ).getTitle ();
        }
        else
        {
            text = value != null && !( value instanceof Icon ) ? value.toString () : "";
        }
        return text;
    }

    /**
     * Returns table cell renderer component.
     *
     * @param table      table
     * @param value      cell value
     * @param isSelected whether or not cell is selected
     * @param hasFocus   whether or not cell has focus
     * @param row        cell row number
     * @param column     cell column number
     * @return table cell renderer component
     */
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
         */
        if ( CompareUtils.equals ( pn, StyleId.STYLE_PROPERTY, StyleId.PARENT_STYLE_PROPERTY, WebLookAndFeel.TEXT_PROPERTY,
                AbstractDecorationPainter.DECORATION_STATES_PROPERTY, WebStyledLabel.STYLE_RANGES_PROPERTY ) )
        {
            super.firePropertyChange ( pn, oldValue, newValue );
        }
        else if ( CompareUtils.equals ( pn, WebLookAndFeel.FONT_PROPERTY, WebLookAndFeel.FOREGROUND_PROPERTY ) &&
                oldValue != newValue && getClientProperty ( javax.swing.plaf.basic.BasicHTML.propertyKey ) != null )
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
     * A subclass of {@link WebTableCellRenderer} that implements {@link javax.swing.plaf.UIResource}.
     * It is used to determine cell renderer provided by the UI class to properly uninstall it on UI uninstall.
     */
    public static class UIResource extends WebTableCellRenderer implements javax.swing.plaf.UIResource
    {
        /**
         * Implementation is used completely from {@link WebTableCellRenderer}.
         */
    }
}