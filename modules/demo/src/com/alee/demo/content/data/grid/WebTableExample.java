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

import com.alee.demo.api.example.AbstractStylePreview;
import com.alee.demo.api.example.FeatureState;
import com.alee.demo.api.example.FeatureType;
import com.alee.demo.api.example.Preview;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.table.WebTable;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */

public class WebTableExample extends AbstractTableExample
{
    @Override
    public String getId ()
    {
        return "webtable";
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
            super ( WebTableExample.this, "basic", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final WebTable table = new WebTable ( getStyleId (), createShortTableModel () );
            table.optimizeColumnWidths ( true );
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
            super ( WebTableExample.this, "scrollable", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final WebTable table = new WebTable ( getStyleId (), createLongTableModel () );
            table.setAutoResizeMode ( JTable.AUTO_RESIZE_OFF );
            table.setVisibleRowCount ( 5 );
            table.optimizeColumnWidths ( true );
            return CollectionUtils.asList ( new WebScrollPane ( table ).setPreferredWidth ( 300 ) );
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
            super ( WebTableExample.this, "editable", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final WebTable table = new WebTable ( getStyleId (), createLongTableModel () );
            table.setAutoResizeMode ( JTable.AUTO_RESIZE_OFF );
            table.setVisibleRowCount ( 5 );
            table.optimizeColumnWidths ( true );
            table.setEditable ( true );
            return CollectionUtils.asList ( new WebScrollPane ( table ).setPreferredWidth ( 300 ) );
        }
    }
}