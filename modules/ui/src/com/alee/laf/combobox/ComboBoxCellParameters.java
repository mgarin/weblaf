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

package com.alee.laf.combobox;

import com.alee.laf.list.ListCellParameters;

import javax.swing.*;

/**
 * {@link JComboBox} box and popup {@link JList} single cell rendering parameters.
 *
 * @param <V> cell value type
 * @param <C> {@link JList} type
 * @author Mikle Garin
 */
public class ComboBoxCellParameters<V, C extends JList> extends ListCellParameters<V, C>
{
    /**
     * {@link JComboBox} that owns {@link JList}.
     */
    protected final JComboBox comboBox;

    /**
     * Constructs new {@link ComboBoxCellParameters}.
     * Parameters are calculated within this constuctor when used.
     *
     * @param list  {@link JList}
     * @param index cell index
     */
    public ComboBoxCellParameters ( final C list, final int index )
    {
        super ( list, index );
        this.comboBox = ( JComboBox ) list.getClientProperty ( WebComboBoxUI.COMBOBOX_INSTANCE );
    }

    /**
     * Constructs new {@link ComboBoxCellParameters}.
     * Parameters are predefined whenever this constuctor is used.
     *
     * @param list     {@link JList}
     * @param value    cell value
     * @param index    cell index
     * @param selected whether or not cell is selected
     * @param focused  whether or not cell has focus
     */
    public ComboBoxCellParameters ( final C list, final V value, final int index, final boolean selected, final boolean focused )
    {
        super ( list, value, index, selected, focused );
        this.comboBox = ( JComboBox ) list.getClientProperty ( WebComboBoxUI.COMBOBOX_INSTANCE );
    }

    /**
     * Returns {@link JComboBox} that owns {@link JList}.
     *
     * @return {@link JComboBox} that owns {@link JList}
     */
    public JComboBox comboBox ()
    {
        return comboBox;
    }
}