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

package com.alee.laf.list;

import javax.swing.*;
import javax.swing.plaf.ListUI;
import java.awt.*;

/**
 * Custom default list cell renderer for WebLookAndFeel.
 *
 * @author Mikle Garin
 */

public class WebListCellRenderer extends WebListElement implements ListCellRenderer
{
    /**
     * Constructs default list cell renderer.
     */
    public WebListCellRenderer ()
    {
        super ();
        setName ( "List.cellRenderer" );
    }

    /**
     * Returns list cell renderer component.
     *
     * @param list         tree
     * @param value        cell value
     * @param index        cell index
     * @param isSelected   whether cell is selected or not
     * @param cellHasFocus whether cell has focus or not
     * @return cell renderer component
     */
    @Override
    public Component getListCellRendererComponent ( final JList list, final Object value, final int index, final boolean isSelected,
                                                    final boolean cellHasFocus )
    {
        // Visual settings
        setFont ( list.getFont () );
        setEnabled ( list.isEnabled () );
        setForeground ( isSelected ? list.getSelectionForeground () : list.getForeground () );

        // Icon and text
        if ( value instanceof Icon )
        {
            setIcon ( ( Icon ) value );
            setText ( "" );
        }
        else
        {
            setIcon ( null );
            setText ( value == null ? "" : value.toString () );
        }

        // Border
        final ListUI lui = list.getUI ();
        final int sw = lui instanceof WebListUI ? ( ( WebListUI ) lui ).getSelectionShadeWidth () : WebListStyle.selectionShadeWidth;
        setMargin ( sw + 2, sw + ( getIcon () != null ? 2 : 4 ), sw + 2, sw + 4 );

        // Orientation
        setComponentOrientation ( list.getComponentOrientation () );

        return this;
    }

    /**
     * A subclass of WebListCellRenderer that implements UIResource.
     */
    public static class UIResource extends WebListCellRenderer implements javax.swing.plaf.UIResource
    {
        //
    }
}