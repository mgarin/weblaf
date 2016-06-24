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

/**
 * Default numbers table cell renderer for WebLaF tables.
 *
 * @author Mikle Garin
 */

public class WebTableNumberCellRenderer extends WebTableCellRenderer
{
    @Override
    protected void updateStyleId ( final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row,
                                   final int column )
    {
        setStyleId ( StyleId.tableCellRendererNumber.at ( table ) );
    }

    /**
     * A subclass of {@link com.alee.laf.table.renderers.WebTableNumberCellRenderer} that implements {@link javax.swing.plaf.UIResource}.
     * It is used to determine cell renderer provided by the UI class to properly uninstall it on UI uninstall.
     */
    public static class UIResource extends WebTableNumberCellRenderer implements javax.swing.plaf.UIResource
    {
        /**
         * Implementation is used completely from {@link com.alee.laf.table.renderers.WebTableNumberCellRenderer}.
         */
    }
}