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

package com.alee.demo.content.data.grid;

import com.alee.demo.api.AbstractExample;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * @author Mikle Garin
 */

public abstract class AbstractTableExample extends AbstractExample
{
    /**
     * Returns sample short table model.
     *
     * @return sample short table model
     */
    protected static TableModel createShortTableModel ()
    {
        final Object[] columns = { "First Name", "Last Name", "Sport", "# of Years", "Vegetarian" };
        final Object[] kathy = { "Kathy", "Smith", "Snowboarding", 5, false };
        final Object[] john = { "John", "Doe", "Rowing", 3, true };
        final Object[] sue = { "Sue", "Black", "Knitting", 2, false };
        final Object[] jane = { "Jane", "White", "Speed reading", 20, true };
        final Object[][] data = { kathy, john, sue, jane };
        return new DefaultTableModel ( data, columns )
        {
            @Override
            public Class<?> getColumnClass ( final int col )
            {
                return col == 3 ? Integer.class : col == 4 ? Boolean.class : String.class;
            }
        };
    }

    /**
     * Returns sample long table model.
     *
     * @return sample long table model
     */
    protected static TableModel createLongTableModel ()
    {
        final Object[] columns = { "First Name", "Last Name", "Sport", "# of Years", "Vegetarian" };
        final Object[] kathy = { "Kathy", "Smith", "Snowboarding", 5, false };
        final Object[] john = { "John", "Doe", "Rowing", 3, true };
        final Object[] sue = { "Sue", "Black", "Knitting", 2, false };
        final Object[] jane = { "Jane", "White", "Speed reading", 20, true };
        final Object[] joe = { "Joe", "Brown", "Pool", 10, false };
        final Object[] sven = { "Sven", "Alister", "Boxing", 36, false };
        final Object[] allen = { "Allen", "Snow", "Diving", 18, true };
        final Object[] mikle = { "Mikle", "Garin", "Judo", 26, false };
        final Object[][] data = { kathy, john, sue, jane, joe, sven, allen, mikle };
        return new DefaultTableModel ( data, columns )
        {
            @Override
            public Class<?> getColumnClass ( final int col )
            {
                return col == 3 ? Integer.class : col == 4 ? Boolean.class : String.class;
            }
        };
    }
}