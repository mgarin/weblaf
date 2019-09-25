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

package com.alee.demo.content.data.tree;

import com.alee.api.annotations.NotNull;
import com.alee.demo.api.example.*;
import com.alee.demo.content.SampleData;
import com.alee.demo.content.data.tree.model.SampleNode;
import com.alee.demo.content.data.tree.model.SampleTreeCellEditor;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.tree.*;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class WebTreeFilterFieldExample extends AbstractStylePreviewExample
{
    @NotNull
    @Override
    public String getId ()
    {
        return "treefilterfield";
    }

    @NotNull
    @Override
    protected String getStyleFileName ()
    {
        return "treefilterfield";
    }

    @NotNull
    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.extended;
    }

    @NotNull
    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new ExTree ( StyleId.extree ),
                new AsyncTree ( StyleId.asynctree ),
                new ExCheckBoxTree ( StyleId.excheckboxtree )
        );
    }

    /**
     * {@link WebExTree} filtering preview.
     */
    protected class ExTree extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public ExTree ( final StyleId styleId )
        {
            super ( WebTreeFilterFieldExample.this, "extree", FeatureState.release, styleId );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final ExTreeDataProvider<SampleNode> dataProvider = SampleData.createExTreeDataProvider ();
            final WebExTree tree = new WebExTree ( getStyleId (), dataProvider, new SampleTreeCellEditor () );
            tree.setVisibleRowCount ( 8 );

            final WebScrollPane treeScroll = new WebScrollPane ( tree );
            treeScroll.setPreferredWidth ( 200 );

            final WebTreeFilterField filterField = new WebTreeFilterField ( tree );

            return CollectionUtils.asList ( new GroupPanel ( 8, false, filterField, treeScroll ) );
        }
    }

    /**
     * {@link WebAsyncTree} filtering preview.
     */
    protected class AsyncTree extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public AsyncTree ( final StyleId styleId )
        {
            super ( WebTreeFilterFieldExample.this, "asynctree", FeatureState.updated, styleId );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final AsyncTreeDataProvider<SampleNode> dataProvider = SampleData.createDelayingAsyncDataProvider ();
            final WebAsyncTree tree = new WebAsyncTree ( getStyleId (), dataProvider, new SampleTreeCellEditor () );
            tree.setVisibleRowCount ( 8 );

            final WebScrollPane treeScroll = new WebScrollPane ( tree );
            treeScroll.setPreferredWidth ( 200 );

            final WebTreeFilterField filterField = new WebTreeFilterField ( tree );

            return CollectionUtils.asList ( new GroupPanel ( 8, false, filterField, treeScroll ) );
        }
    }

    /**
     * {@link WebExCheckBoxTree} filtering preview.
     */
    protected class ExCheckBoxTree extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public ExCheckBoxTree ( final StyleId styleId )
        {
            super ( WebTreeFilterFieldExample.this, "excheckboxtree", FeatureState.updated, styleId );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final ExTreeDataProvider<SampleNode> dataProvider = SampleData.createExTreeDataProvider ();
            final WebExCheckBoxTree tree = new WebExCheckBoxTree ( getStyleId (), dataProvider, new SampleTreeCellEditor () );
            tree.setVisibleRowCount ( 8 );

            final WebScrollPane treeScroll = new WebScrollPane ( tree );
            treeScroll.setPreferredWidth ( 200 );

            final WebTreeFilterField filterField = new WebTreeFilterField ( tree );

            return CollectionUtils.asList ( new GroupPanel ( 8, false, filterField, treeScroll ) );
        }
    }
}