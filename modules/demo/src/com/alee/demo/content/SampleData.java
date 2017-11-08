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

package com.alee.demo.content;

import com.alee.api.TitleSupport;
import com.alee.managers.language.LM;
import com.alee.utils.CollectionUtils;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.List;

/**
 * This utility class provides various sample data for {@link com.alee.demo.DemoApplication}.
 *
 * @author Mikle Garin
 */

public final class SampleData
{
    /**
     * Returns sample short table model.
     *
     * @return sample short table model
     */
    public static TableModel createShortTableModel ()
    {
        final Object[] columns = { "First Name", "Last Name", "Hobby", "Age", "Vegetarian" };
        final Object[] kathy = { "Kathy", "Smith", "Snowboarding", 19, false };
        final Object[] john = { "John", "Doe", "Rowing", 32, true };
        final Object[] sue = { "Sue", "Black", "Knitting", 56, false };
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
    public static TableModel createLongTableModel ()
    {
        final Object[] columns = { "First Name", "Last Name", "Hobby", "Age", "Vegetarian" };
        final Object[] kathy = { "Kathy", "Smith", "Snowboarding", 19, false };
        final Object[] john = { "John", "Doe", "Rowing", 32, true };
        final Object[] sue = { "Sue", "Black", "Knitting", 56, false };
        final Object[] jane = { "Jane", "White", "Speed reading", 20, true };
        final Object[] joe = { "Joe", "Brown", "Swimming", 14, false };
        final Object[] sven = { "Sven", "Alister", "Boxing", 36, false };
        final Object[] allen = { "Allen", "Snow", "Diving", 18, true };
        final Object[] mikle = { "Mikle", "Garin", "Judo", 27, false };
        final Object[] jake = { "Jake", "Alduin", "Fencing", 35, false };
        final Object[] andrew = { "Andrew", "Garfild", "Programming", 26, false };
        final Object[] kate = { "Kate", "Matthew", "Bowling", 22, false };
        final Object[] paul = { "Paul", "Johnson", "Modelling", 38, false };
        final Object[][] data = { kathy, john, sue, jane, joe, sven, allen, mikle, jake, andrew, kate, paul };
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
     * Returns {@link List} of sample data.
     *
     * @return {@link List} of sample data
     */
    public static List<ListItem> createSampleListData ()
    {
        return CollectionUtils.asList (
                new ListItem ( "item1" ),
                new ListItem ( "item2" ),
                new ListItem ( "item3" )
        );
    }

    /**
     * Sample list item object.
     * It supports translation based on the provided language key in combination with example key.
     */
    private static class ListItem implements TitleSupport
    {
        /**
         * Item language key.
         */
        private final String language;

        /**
         * Constructs new {@link ListItem}.
         *
         * @param language item language key
         */
        public ListItem ( final String language )
        {
            super ();
            this.language = language;
        }

        @Override
        public String getTitle ()
        {
            return LM.get ( "demo.sample.list." + language );
        }
    }
}