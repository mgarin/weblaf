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
import com.alee.api.annotations.Nullable;
import com.alee.demo.api.example.*;
import com.alee.demo.content.SampleData;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.table.*;
import com.alee.managers.language.LM;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class WebTableExample extends AbstractStylePreviewExample
{
    @NotNull
    @Override
    public String getId ()
    {
        return "webtable";
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
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new BasicTable ( StyleId.table ),
                new ScrollableTable ( StyleId.table ),
                new EditableTable ( StyleId.table ),
                new TableTooltips ( StyleId.table )
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
            super ( WebTableExample.this, "basic", FeatureState.updated, styleId );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebTable table = new WebTable ( getStyleId (), SampleData.createShortTableModel ( false ) );
            table.optimizeColumnWidths ( true );
            table.setOptimizeRowHeight ( true );
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

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebTable table = new WebTable ( getStyleId (), SampleData.createLongTableModel ( false ) );
            table.setAutoResizeMode ( JTable.AUTO_RESIZE_OFF );
            table.setVisibleRowCount ( 5 );
            table.optimizeColumnWidths ( true );
            table.setOptimizeRowHeight ( true );
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

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebTable table = new WebTable ( getStyleId (), SampleData.createLongTableModel ( true ) );
            table.setAutoResizeMode ( JTable.AUTO_RESIZE_OFF );
            table.setVisibleRowCount ( 5 );
            table.optimizeColumnWidths ( true );
            table.setOptimizeRowHeight ( true );
            table.setEditable ( true );
            return CollectionUtils.asList ( new WebScrollPane ( table ).setPreferredWidth ( 300 ) );
        }
    }

    /**
     * Custom table tooltips preview.
     */
    protected class TableTooltips extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public TableTooltips ( final StyleId styleId )
        {
            super ( WebTableExample.this, "tooltips", FeatureState.updated, styleId );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebTable table = new WebTable ( getStyleId (), SampleData.createLongTableModel ( true ) );
            table.setAutoResizeMode ( JTable.AUTO_RESIZE_OFF );
            table.setVisibleRowCount ( 5 );
            table.optimizeColumnWidths ( true );
            table.setOptimizeRowHeight ( true );
            table.setHeaderToolTipProvider ( new TableHeaderToolTipProvider<String> ()
            {
                @Nullable
                @Override
                protected String getToolTipText ( @NotNull final JTableHeader tableHeader,
                                                  @NotNull final TableHeaderCellArea<String, JTableHeader> area )
                {
                    return LM.get ( getPreviewLanguageKey ( "header" ), area.column (), area.getValue ( tableHeader ) );
                }
            } );
            table.setToolTipProvider ( new TableToolTipProvider<Object> ()
            {
                @Nullable
                @Override
                protected String getToolTipText ( @NotNull final JTable table, @NotNull final TableCellArea<Object, JTable> area )
                {
                    return LM.get ( getPreviewLanguageKey ( "cell" ), area.row (), area.column (), area.getValue ( table ) );
                }
            } );
            return CollectionUtils.asList ( new WebScrollPane ( table ).setPreferredWidth ( 300 ) );
        }
    }
}