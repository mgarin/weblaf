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

import com.alee.managers.style.StyleId;

import javax.swing.*;
import java.text.NumberFormat;

/**
 * Default {@link javax.swing.table.TableCellRenderer} implementation for {@link Float} values.
 *
 * @param <V> cell value type
 * @param <C> table type
 * @author Mikle Garin
 */

public class WebTableFloatCellRenderer<V extends Float, C extends JTable> extends WebTableNumberCellRenderer<V, C>
{
    /**
     * Number formatter.
     */
    protected NumberFormat numberFormat;

    @Override
    protected void updateStyleId ( final C table, final V value, final boolean isSelected,
                                   final boolean hasFocus, final int row, final int column )
    {
        setStyleId ( StyleId.tableCellRendererFloat.at ( table ) );
    }

    @Override
    protected String textForValue ( final C table, final V value, final boolean isSelected,
                                    final boolean hasFocus, final int row, final int column )
    {
        return value != null ? getNumberFormat ().format ( value ) : "";
    }

    /**
     * Returns {@link NumberFormat}.
     *
     * @return {@link NumberFormat}
     */
    protected NumberFormat getNumberFormat ()
    {
        if ( numberFormat == null )
        {
            numberFormat = NumberFormat.getInstance ();
        }
        return numberFormat;
    }

    /**
     * A subclass of {@link WebTableFloatCellRenderer} that implements {@link javax.swing.plaf.UIResource}.
     * It is used to determine cell renderer provided by the UI class to properly uninstall it on UI uninstall.
     *
     * @param <V> cell value type
     * @param <C> table type
     */
    public static class UIResource<V extends Float, C extends JTable> extends WebTableFloatCellRenderer<V, C>
            implements javax.swing.plaf.UIResource
    {
        /**
         * Implementation is used completely from {@link WebTableFloatCellRenderer}.
         */
    }
}