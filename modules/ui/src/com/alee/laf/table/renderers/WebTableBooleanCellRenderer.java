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

import com.alee.api.jdk.Objects;
import com.alee.extended.label.WebStyledLabel;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.checkbox.WebCheckBox;
import com.alee.laf.table.TableCellParameters;
import com.alee.managers.style.StyleId;
import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.painter.decoration.Stateful;

import javax.swing.*;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Default {@link TableCellRenderer} implementation for {@link Boolean} values.
 *
 * @param <C> {@link JTable} type
 * @param <P> {@link TableCellParameters} type
 * @author Mikle Garin
 */
public class WebTableBooleanCellRenderer<C extends JTable, P extends TableCellParameters<Boolean, C>>
        extends WebCheckBox implements TableCellRenderer, Stateful
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
     * @param parameters {@link TableCellParameters}
     */
    protected void updateStates ( final P parameters )
    {
        states.clear ();

        // Basic states
        states.add ( parameters.isSelected () ? DecorationState.selected : DecorationState.unselected );

        // Focus state
        if ( parameters.isFocused () )
        {
            states.add ( DecorationState.focused );
        }

        // todo Add hover state when WTableUI is available
        // states.add ( DecorationState.hover );
    }

    /**
     * Updates table cell renderer component style ID.
     *
     * @param parameters {@link TableCellParameters}
     */
    protected void updateStyleId ( final P parameters )
    {
        setStyleId ( StyleId.tableCellRendererBoolean.at ( parameters.table () ) );
    }

    /**
     * Updating renderer based on the provided settings.
     *
     * @param parameters {@link TableCellParameters}
     */
    protected void updateView ( final P parameters )
    {
        setEnabled ( enabledForValue ( parameters ) );
        setComponentOrientation ( orientationForValue ( parameters ) );
        setFont ( fontForValue ( parameters ) );
        setForeground ( foregroundForValue ( parameters ) );
        setHorizontalAlignment ( horizontalAlignmentForValue ( parameters ) );
        setSelected ( selectedForValue ( parameters ) );
        setText ( textForValue ( parameters ) );
    }

    /**
     * Returns whether or not renderer for the specified cell value should be enabled.
     *
     * @param parameters {@link TableCellParameters}
     * @return {@code true} if renderer for the specified cell value should be enabled, {@code false} otherwise
     */
    protected boolean enabledForValue ( final P parameters )
    {
        return parameters.table ().isEnabled ();
    }

    /**
     * Returns renderer {@link ComponentOrientation} for the specified cell value.
     *
     * @param parameters {@link TableCellParameters}
     * @return renderer {@link ComponentOrientation} for the specified cell value
     */
    protected ComponentOrientation orientationForValue ( final P parameters )
    {
        return parameters.table ().getComponentOrientation ();
    }

    /**
     * Returns renderer {@link Font} for the specified cell value.
     *
     * @param parameters {@link TableCellParameters}
     * @return renderer {@link Font} for the specified cell value
     */
    protected Font fontForValue ( final P parameters )
    {
        return parameters.table ().getFont ();
    }

    /**
     * Returns renderer foreground color for the specified cell value.
     *
     * @param parameters {@link TableCellParameters}
     * @return renderer foreground color for the specified cell value
     */
    protected Color foregroundForValue ( final P parameters )
    {
        return parameters.isSelected () ? parameters.table ().getSelectionForeground () : parameters.table ().getForeground ();
    }

    /**
     * Returns renderer horizontal alignment for the specified cell value.
     *
     * @param parameters {@link TableCellParameters}
     * @return renderer horizontal alignment for the specified cell value
     */
    protected int horizontalAlignmentForValue ( final P parameters )
    {
        return SwingConstants.CENTER;
    }

    /**
     * Returns renderer value for the specified cell.
     *
     * @param parameters {@link TableCellParameters}
     * @return renderer value for the specified cell
     */
    protected boolean selectedForValue ( final P parameters )
    {
        return parameters.value () != null && parameters.value ();
    }

    /**
     * Returns renderer text for the specified cell value.
     *
     * @param parameters {@link TableCellParameters}
     * @return renderer text for the specified cell value
     */
    protected String textForValue ( final P parameters )
    {
        return null;
    }

    @Override
    public Component getTableCellRendererComponent ( final JTable table, final Object value, final boolean isSelected,
                                                     final boolean hasFocus, final int row, final int column )
    {
        // Forming cell parameters
        final P parameters = getRenderingParameters ( ( C ) table, ( Boolean ) value, isSelected, hasFocus, row, column );

        // Updating custom states
        updateStates ( parameters );

        // Updating style ID
        updateStyleId ( parameters );

        // Updating renderer view
        updateView ( parameters );

        // Updating decoration states for this render cycle
        DecorationUtils.fireStatesChanged ( this );

        return this;
    }

    /**
     * Returns {@link TableCellParameters}.
     *
     * @param table      {@link JTable}
     * @param value      cell value
     * @param isSelected whether or not cell is selected
     * @param hasFocus   whether or not cell has focus
     * @param row        cell row number
     * @param column     cell column number
     * @return {@link TableCellParameters}
     */
    protected P getRenderingParameters ( final C table, final Boolean value, final boolean isSelected,
                                         final boolean hasFocus, final int row, final int column )
    {
        return ( P ) new TableCellParameters<Boolean, C> ( table, value, row, column, isSelected, hasFocus );
    }

    @Override
    public final void validate ()
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public final void invalidate ()
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public final void revalidate ()
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public final void repaint ( final long tm, final int x, final int y, final int width, final int height )
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public final void repaint ( final Rectangle r )
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public final void repaint ()
    {
        /**
         * Overridden for performance reasons.
         */
    }

    /**
     * Checks whether or not specified property change should actually be fired.
     * All property fire methods are overridden and made final for performance reasons.
     *
     * @param propertyName changed property name
     * @param oldValue     old property value
     * @param newValue     new property value
     */
    protected void checkPropertyChange ( final String propertyName, final Object oldValue, final Object newValue )
    {
        if ( Objects.equals ( propertyName, StyleId.STYLE_PROPERTY, StyleId.PARENT_STYLE_PROPERTY,
                AbstractDecorationPainter.DECORATION_STATES_PROPERTY, WebStyledLabel.STYLE_RANGES_PROPERTY,
                WebLookAndFeel.TEXT_PROPERTY, WebLookAndFeel.BORDER_PROPERTY, WebLookAndFeel.MODEL_PROPERTY ) )
        {
            allowPropertyChange ( propertyName, oldValue, newValue );
        }
        else if ( Objects.equals ( propertyName, WebLookAndFeel.FONT_PROPERTY, WebLookAndFeel.FOREGROUND_PROPERTY ) &&
                oldValue != newValue && getClientProperty ( BasicHTML.propertyKey ) != null )
        {
            allowPropertyChange ( propertyName, oldValue, newValue );
        }
    }

    /**
     * Allows property change event to be fired.
     *
     * @param propertyName changed property name
     * @param oldValue     old property value
     * @param newValue     new property value
     */
    protected void allowPropertyChange ( final String propertyName, final Object oldValue, final Object newValue )
    {
        super.firePropertyChange ( propertyName, oldValue, newValue );
    }

    @Override
    protected final void firePropertyChange ( final String propertyName, final Object oldValue, final Object newValue )
    {
        checkPropertyChange ( propertyName, oldValue, newValue );
    }

    @Override
    public final void firePropertyChange ( final String propertyName, final byte oldValue, final byte newValue )
    {
        checkPropertyChange ( propertyName, oldValue, newValue );
    }

    @Override
    public final void firePropertyChange ( final String propertyName, final char oldValue, final char newValue )
    {
        checkPropertyChange ( propertyName, oldValue, newValue );
    }

    @Override
    public final void firePropertyChange ( final String propertyName, final short oldValue, final short newValue )
    {
        checkPropertyChange ( propertyName, oldValue, newValue );
    }

    @Override
    public final void firePropertyChange ( final String propertyName, final int oldValue, final int newValue )
    {
        checkPropertyChange ( propertyName, oldValue, newValue );
    }

    @Override
    public final void firePropertyChange ( final String propertyName, final long oldValue, final long newValue )
    {
        checkPropertyChange ( propertyName, oldValue, newValue );
    }

    @Override
    public final void firePropertyChange ( final String propertyName, final float oldValue, final float newValue )
    {
        checkPropertyChange ( propertyName, oldValue, newValue );
    }

    @Override
    public final void firePropertyChange ( final String propertyName, final double oldValue, final double newValue )
    {
        checkPropertyChange ( propertyName, oldValue, newValue );
    }

    @Override
    public final void firePropertyChange ( final String propertyName, final boolean oldValue, final boolean newValue )
    {
        checkPropertyChange ( propertyName, oldValue, newValue );
    }

    /**
     * A subclass of {@link WebTableBooleanCellRenderer} that implements {@link javax.swing.plaf.UIResource}.
     * It is used to determine cell renderer provided by the UI class to properly uninstall it on UI uninstall.
     *
     * @param <C> {@link JTable} type
     * @param <P> {@link TableCellParameters} type
     */
    public static final class UIResource<C extends JTable, P extends TableCellParameters<Boolean, C>>
            extends WebTableBooleanCellRenderer<C, P> implements javax.swing.plaf.UIResource
    {
        /**
         * Implementation is used completely from {@link WebTableBooleanCellRenderer}.
         */
    }
}