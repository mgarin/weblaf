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

package com.alee.extended.list;

import com.alee.managers.style.StyleId;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom default checkbox list cell renderer for WebLookAndFeel.
 *
 * @author Mikle Garin
 */

public class WebCheckBoxListCellRenderer implements ListCellRenderer
{
    /**
     * Checkbox list elements cache.
     */
    protected Map<String, WebCheckBoxListElement> elements = new HashMap<String, WebCheckBoxListElement> ();

    /**
     * Constructs default checkbox list cell renderer.
     */
    public WebCheckBoxListCellRenderer ()
    {
        super ();
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
        // Cell data
        final CheckBoxCellData data = ( CheckBoxCellData ) value;

        // Actual renderer for cell
        final WebCheckBoxListElement renderer = getElement ( list, data );

        // Visual settings
        renderer.setFont ( list.getFont () );
        renderer.setEnabled ( list.isEnabled () );
        renderer.setForeground ( isSelected ? list.getSelectionForeground () : list.getForeground () );

        // Selection and text
        renderer.setText ( data.getUserObject () == null ? "" : data.getUserObject ().toString () );

        // Orientation
        renderer.setComponentOrientation ( list.getComponentOrientation () );

        return renderer;
    }

    /**
     * Returns cached checkbox element for specified data.
     *
     * @param list list
     * @param data data to process
     * @return cached checkbox element
     */
    public WebCheckBoxListElement getElement ( final JList list, final CheckBoxCellData data )
    {
        final String key = data.getId ();
        if ( elements.containsKey ( key ) )
        {
            final WebCheckBoxListElement element = elements.get ( key );
            element.setSelected ( data.isSelected () );
            return element;
        }
        else
        {
            final StyleId elementId = StyleId.checkboxlistCellRenderer.at ( list );
            final WebCheckBoxListElement element = new WebCheckBoxListElement ( elementId, data.isSelected () );
            elements.put ( key, element );
            return element;
        }
    }
}