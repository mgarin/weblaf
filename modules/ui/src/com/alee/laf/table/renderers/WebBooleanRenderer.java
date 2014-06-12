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

import javax.swing.*;
import javax.swing.plaf.UIResource;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * User: mgarin Date: 31.10.12 Time: 15:27
 */

public class WebBooleanRenderer extends WebCheckBox implements TableCellRenderer, UIResource
{
    public WebBooleanRenderer ()
    {
        super ();
        setOpaque ( true );
        setAnimated ( false );
        setHorizontalAlignment ( JLabel.CENTER );
        setShadeWidth ( 0 );
        setIconWidth ( 12 );
        setIconHeight ( 12 );
    }

    @Override
    public Component getTableCellRendererComponent ( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column )
    {
        setForeground ( isSelected ? table.getSelectionForeground () : table.getForeground () );
        setBackground ( isSelected ? table.getSelectionBackground () : table.getBackground () );
        setSelected ( value != null && ( Boolean ) value );
        setEnabled ( table.isEnabled () );
        return this;
    }
}