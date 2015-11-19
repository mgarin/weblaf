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

import javax.swing.*;
import javax.swing.plaf.UIResource;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * @author Mikle Garin
 */

public class WebBooleanRenderer extends WebCheckBox implements TableCellRenderer, UIResource
{
    @Override
    public Component getTableCellRendererComponent ( final JTable table, final Object value, final boolean isSelected,
                                                     final boolean hasFocus, final int row, final int column )
    {
        setStyleId ( StyleId.tableBooleanCellRenderer.at ( table ) );
        setForeground ( isSelected ? table.getSelectionForeground () : table.getForeground () );
        setBackground ( isSelected ? table.getSelectionBackground () : table.getBackground () );
        setSelected ( value != null && ( Boolean ) value );
        setEnabled ( table.isEnabled () );
        return this;
    }
}