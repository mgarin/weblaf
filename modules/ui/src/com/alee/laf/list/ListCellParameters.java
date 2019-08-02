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

import com.alee.api.jdk.Objects;
import com.alee.api.ui.RenderingParameters;

import javax.swing.*;

/**
 * {@link JList} single cell rendering parameters.
 *
 * @param <V> cell value type
 * @param <C> {@link JList} type
 * @author Mikle Garin
 */
public class ListCellParameters<V, C extends JList> implements RenderingParameters
{
    /**
     * {@link JList}.
     */
    protected final C list;

    /**
     * Cell value.
     */
    protected final V value;

    /**
     * Cell index.
     */
    protected final int index;

    /**
     * Whether or not cell is selected.
     */
    protected final boolean selected;

    /**
     * Whether or not cell has focus.
     */
    protected final boolean focused;

    /**
     * Constructs new {@link ListCellParameters}.
     * Parameters are calculated within this constuctor when used.
     *
     * @param list {@link JList}
     * @param area {@link ListCellArea}
     */
    public ListCellParameters ( final C list, final ListCellArea<V, C> area )
    {
        this ( list, area.index () );
    }

    /**
     * Constructs new {@link ListCellParameters}.
     * Parameters are calculated within this constuctor when used.
     *
     * @param list  {@link JList}
     * @param index cell index
     */
    public ListCellParameters ( final C list, final int index )
    {
        this.list = Objects.requireNonNull ( list, "List must not be null" );
        this.value = ( V ) list.getModel ().getElementAt ( index );
        this.index = index;
        this.selected = list.isSelectedIndex ( index );
        this.focused = list.hasFocus () && list.getLeadSelectionIndex () == index;
    }

    /**
     * Constructs new {@link ListCellParameters}.
     * Parameters are predefined whenever this constuctor is used.
     *
     * @param list     {@link JList}
     * @param value    cell value
     * @param index    cell index
     * @param selected whether or not cell is selected
     * @param focused  whether or not cell has focus
     */
    public ListCellParameters ( final C list, final V value, final int index, final boolean selected, final boolean focused )
    {
        this.list = Objects.requireNonNull ( list, "List must not be null" );
        this.value = value;
        this.index = index;
        this.selected = selected;
        this.focused = focused;
    }

    /**
     * Returns {@link JList}.
     *
     * @return {@link JList}
     */
    public C list ()
    {
        return list;
    }

    /**
     * Returns cell value.
     *
     * @return cell value
     */
    public V value ()
    {
        return value;
    }

    /**
     * Returns cell index.
     *
     * @return cell index
     */
    public int index ()
    {
        return index;
    }

    /**
     * Returns whether or not cell is selected.
     *
     * @return {@code true} if cell is selected, {@code false} otherwise
     */
    public boolean isSelected ()
    {
        return selected;
    }

    /**
     * Returns whether or not cell has focus.
     *
     * @return {@code true} if cell has focus, {@code false} otherwise
     */
    public boolean isFocused ()
    {
        return focused;
    }
}