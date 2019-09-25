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

import com.alee.api.annotations.NotNull;
import com.alee.demo.api.example.*;
import com.alee.demo.api.example.wiki.OracleWikiPage;
import com.alee.demo.api.example.wiki.WikiPage;
import com.alee.demo.content.SampleData;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class JTableExample extends AbstractStylePreviewExample
{
    @NotNull
    @Override
    public String getId ()
    {
        return "jtable";
    }

    @NotNull
    @Override
    protected String getStyleFileName ()
    {
        return "table";
    }

    @NotNull
    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.swing;
    }

    @NotNull
    @Override
    public WikiPage getWikiPage ()
    {
        return new OracleWikiPage ( "How to Use Tables", "table" );
    }

    @NotNull
    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new BasicTable ( StyleId.table ),
                new ScrollableTable ( StyleId.table ),
                new EditableTable ( StyleId.table )
        );
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

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final JTable table = new JTable ( SampleData.createShortTableModel ( false ) );
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

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final JTable table = new JTable ( SampleData.createLongTableModel ( false ) );
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

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final JTable table = new JTable ( SampleData.createLongTableModel ( true ) );
            table.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
            table.setAutoResizeMode ( JTable.AUTO_RESIZE_OFF );
            return CollectionUtils.asList ( new WebScrollPane ( table ).setPreferredSize ( 300, 100 ) );
        }
    }
}