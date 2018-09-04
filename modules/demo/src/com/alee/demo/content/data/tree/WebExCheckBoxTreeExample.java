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

import com.alee.api.jdk.Objects;
import com.alee.api.jdk.Predicate;
import com.alee.demo.api.example.*;
import com.alee.demo.content.SampleData;
import com.alee.demo.content.data.tree.model.SampleNode;
import com.alee.demo.content.data.tree.model.SampleTreeCellEditor;
import com.alee.extended.tree.ExTreeDataProvider;
import com.alee.extended.tree.WebExCheckBoxTree;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class WebExCheckBoxTreeExample extends AbstractStylePreviewExample
{
    @Override
    public String getId ()
    {
        return "excheckboxtree";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "excheckboxtree";
    }

    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.extended;
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new BasicTree ( StyleId.excheckboxtree ),
                new CustomizedTree ( StyleId.excheckboxtree )
        );
    }

    /**
     * Basic extended tree preview.
     */
    protected class BasicTree extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public BasicTree ( final StyleId styleId )
        {
            super ( WebExCheckBoxTreeExample.this, "basic", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final ExTreeDataProvider<SampleNode> dataProvider = SampleData.createExTreeDataProvider ();
            final WebExCheckBoxTree tree = new WebExCheckBoxTree ( getStyleId (), dataProvider );
            tree.setVisibleRowCount ( 8 );
            return CollectionUtils.asList ( new WebScrollPane ( tree ).setPreferredWidth ( 200 ) );
        }
    }

    /**
     * Editable extended tree preview.
     */
    protected class CustomizedTree extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public CustomizedTree ( final StyleId styleId )
        {
            super ( WebExCheckBoxTreeExample.this, "custom", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final ExTreeDataProvider<SampleNode> dataProvider = SampleData.createCustomizedExCheckBoxTreeDataProvider ();
            final WebExCheckBoxTree tree = new WebExCheckBoxTree ( getStyleId (), dataProvider, new SampleTreeCellEditor () );
            tree.setVisibleRowCount ( 13 );
            tree.setCheckBoxEnabledStateProvider ( new Predicate<SampleNode> ()
            {
                @Override
                public boolean test ( final SampleNode node )
                {
                    return !node.isLeaf () || Objects.notEquals ( node.getParent ().getId (), "disabled" );
                }
            } );
            tree.setCheckBoxVisibleStateProvider ( new Predicate<SampleNode> ()
            {
                @Override
                public boolean test ( final SampleNode node )
                {
                    return !node.isLeaf () || Objects.notEquals ( node.getParent ().getId (), "hidden" );
                }
            } );
            tree.setEditableStateProvider ( new Predicate<SampleNode> ()
            {
                @Override
                public boolean test ( final SampleNode node )
                {
                    return node.isLeaf () && Objects.equals ( node.getParent ().getId (), "editable" );
                }
            } );
            tree.expandAll ();
            return CollectionUtils.asList ( new WebScrollPane ( tree ).setPreferredWidth ( 280 ) );
        }
    }
}