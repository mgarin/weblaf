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
import com.alee.extended.tree.WebCheckBoxTree;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.tree.WebTreeModel;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class WebCheckBoxTreeExample extends AbstractStylePreviewExample
{
    @Override
    public String getId ()
    {
        return "checkboxtree";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "checkboxtree";
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
                new BasicTree ( StyleId.checkboxtree ),
                new CustomizedTree ( StyleId.checkboxtree )
        );
    }

    /**
     * Basic {@link WebCheckBoxTree} preview.
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
            super ( WebCheckBoxTreeExample.this, "basic", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebTreeModel<SampleNode> model = SampleData.createCheckBoxTreeModel ();
            final WebCheckBoxTree tree = new WebCheckBoxTree ( getStyleId (), model );
            tree.setVisibleRowCount ( 8 );
            return CollectionUtils.asList ( new WebScrollPane ( tree ).setPreferredWidth ( 200 ) );
        }
    }

    /**
     * Customized and editable {@link WebCheckBoxTree} preview.
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
            super ( WebCheckBoxTreeExample.this, "custom", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebTreeModel<SampleNode> model = SampleData.createCustomizedCheckBoxTreeModel ();
            final WebCheckBoxTree tree = new WebCheckBoxTree ( getStyleId (), model );
            tree.setEditable ( true );
            tree.setCellEditor ( new SampleTreeCellEditor () );
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