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

import com.alee.demo.api.*;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.List;

/**
 * @author Mikle Garin
 */

public class JTableExample extends AbstractExample
{
    @Override
    public String getId ()
    {
        return "jtable";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "table";
    }

    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.swing;
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        final BasicTable basic = new BasicTable ( StyleId.table );
        final ScrollableTable scrollable = new ScrollableTable ( StyleId.table );
        final EditableTable editable = new EditableTable ( StyleId.table );
        return CollectionUtils.<Preview>asList ( basic, scrollable, editable );
    }

    /**
     * Basic table preview.
     */
    protected class BasicTable extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public BasicTable ( final StyleId styleId )
        {
            super ( JTableExample.this, "basic", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final JTable table = new JTable ( createShortTableModel () );
            table.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
            return CollectionUtils.asList ( table );
        }
    }

    /**
     * Scrollable table preview.
     */
    protected class ScrollableTable extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public ScrollableTable ( final StyleId styleId )
        {
            super ( JTableExample.this, "scrollable", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final JTable table = new JTable (  createLongTableModel () );
            table.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
            table.setAutoResizeMode ( JTable.AUTO_RESIZE_OFF );
            return CollectionUtils.asList ( new WebScrollPane ( table ).setPreferredSize ( 300, 100 ) );
        }
    }

    /**
     * Editable table preview.
     */
    protected class EditableTable extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public EditableTable ( final StyleId styleId )
        {
            super ( JTableExample.this, "editable", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final JTable table = new JTable ( createLongTableModel () );
            table.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
            table.setAutoResizeMode ( JTable.AUTO_RESIZE_OFF );
            return CollectionUtils.asList ( new WebScrollPane ( table ).setPreferredSize ( 300, 100 ) );
        }
    }

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
        return new DefaultTableModel ( data, columns );
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
        return new DefaultTableModel ( data, columns );
    }
}