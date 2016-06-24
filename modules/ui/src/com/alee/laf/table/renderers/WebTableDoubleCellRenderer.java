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
 * Default floating point numbers table cell renderer for WebLaF tables.
 *
 * @author Mikle Garin
 */

public class WebTableDoubleCellRenderer extends WebTableNumberCellRenderer
{
    /**
     * Number formatter.
     */
    protected NumberFormat formatter;

    @Override
    protected void updateStyleId ( final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row,
                                   final int column )
    {
        setStyleId ( StyleId.tableCellRendererDouble.at ( table ) );
    }

    @Override
    protected void updateView ( final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row,
                                final int column )
    {
        initializeFormatter ();
        setText ( value != null ? formatter.format ( value ) : "" );
    }

    /**
     * Initializes number formatter.
     */
    protected void initializeFormatter ()
    {
        if ( formatter == null )
        {
            formatter = NumberFormat.getInstance ();
        }
    }

    /**
     * A subclass of {@link com.alee.laf.table.renderers.WebTableDoubleCellRenderer} that implements {@link javax.swing.plaf.UIResource}.
     * It is used to determine cell renderer provided by the UI class to properly uninstall it on UI uninstall.
     */
    public static class UIResource extends WebTableDoubleCellRenderer implements javax.swing.plaf.UIResource
    {
        /**
         * Implementation is used completely from {@link com.alee.laf.table.renderers.WebTableDoubleCellRenderer}.
         */
    }
}