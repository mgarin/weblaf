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

import com.alee.demo.api.example.*;
import com.alee.demo.api.example.wiki.WebLafWikiPage;
import com.alee.demo.api.example.wiki.WikiPage;
import com.alee.demo.content.SampleData;
import com.alee.demo.content.data.tree.model.SampleNode;
import com.alee.demo.content.data.tree.model.SampleObjectType;
import com.alee.demo.content.data.tree.model.SampleTreeCellEditor;
import com.alee.extended.tree.*;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class WebAsyncTreeExample extends AbstractStylePreviewExample
{
    @Override
    public String getId ()
    {
        return "asynctree";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "tree";
    }

    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.extended;
    }

    @Override
    public WikiPage getWikiPage ()
    {
        return new WebLafWikiPage ( "How to use WebAsyncTree" );
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new BasicTree ( StyleId.asynctree ),
                new EditableTree ( StyleId.asynctree ),
                new DragAndDropTree ( StyleId.asynctree )
        );
    }

    /**
     * Basic asynchronous tree preview.
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
            super ( WebAsyncTreeExample.this, "basic", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final AsyncTreeDataProvider<SampleNode> dataProvider = SampleData.createDelayingAsyncDataProvider ();
            final WebAsyncTree tree = new WebAsyncTree ( getStyleId (), dataProvider );
            tree.setVisibleRowCount ( 8 );
            return CollectionUtils.asList ( new WebScrollPane ( tree ).setPreferredWidth ( 200 ) );
        }
    }

    /**
     * Editable asynchronous tree preview.
     */
    protected class EditableTree extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public EditableTree ( final StyleId styleId )
        {
            super ( WebAsyncTreeExample.this, "editable", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final AsyncTreeDataProvider<SampleNode> dataProvider = SampleData.createDelayingAsyncDataProvider ();
            final WebAsyncTree tree = new WebAsyncTree ( getStyleId (), dataProvider, new SampleTreeCellEditor () );
            tree.setVisibleRowCount ( 8 );
            return CollectionUtils.asList ( new WebScrollPane ( tree ).setPreferredWidth ( 200 ) );
        }
    }

    /**
     * Drag and drop tree preview.
     */
    protected class DragAndDropTree extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public DragAndDropTree ( final StyleId styleId )
        {
            super ( WebAsyncTreeExample.this, "dragndrop", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final AsyncTreeDataProvider<SampleNode> leftDataProvider = SampleData.createDelayingAsyncDataProvider ();
            final WebAsyncTree left = new WebAsyncTree ( getStyleId (), leftDataProvider, new SampleTreeCellEditor () );
            left.setVisibleRowCount ( 8 );
            left.setDragEnabled ( true );
            left.setDropMode ( DropMode.ON_OR_INSERT );
            left.setTransferHandler ( createTransferHandler () );
            final WebScrollPane leftScroll = new WebScrollPane ( left ).setPreferredWidth ( 200 );

            final AsyncTreeDataProvider<SampleNode> rightDataProvider = SampleData.createDelayingAsyncDataProvider ();
            final WebAsyncTree right = new WebAsyncTree ( getStyleId (), rightDataProvider, new SampleTreeCellEditor () );
            right.setVisibleRowCount ( 8 );
            right.setDragEnabled ( true );
            right.setDropMode ( DropMode.ON_OR_INSERT );
            right.setTransferHandler ( createTransferHandler () );
            final WebScrollPane rightScroll = new WebScrollPane ( right ).setPreferredWidth ( 200 );

            return CollectionUtils.asList ( leftScroll, rightScroll );
        }
    }

    /**
     * Returns sample tree transfer handler.
     * It will provide base functionality of DnD for our sample tree.
     *
     * @return sample extended tree transfer handler
     */
    protected static AsyncTreeTransferHandler<SampleNode, WebAsyncTree<SampleNode>, AsyncTreeModel<SampleNode>> createTransferHandler ()
    {
        return new AsyncTreeTransferHandler<SampleNode, WebAsyncTree<SampleNode>, AsyncTreeModel<SampleNode>> ()
        {
            /**
             * Forcing this {@link TransferHandler} to move nodes.
             */
            @Override
            public int getSourceActions ( final JComponent c )
            {
                return MOVE;
            }

            /**
             * Blocks drop on {@link SampleObjectType#leaf} nodes.
             */
            @Override
            protected List<? extends TreeDropHandler> createDropHandlers ()
            {
                return CollectionUtils.asList ( new NodesDropHandler<SampleNode, WebAsyncTree<SampleNode>, AsyncTreeModel<SampleNode>> ()
                {
                    @Override
                    protected boolean canDrop ( final TransferSupport support, final WebAsyncTree<SampleNode> tree,
                                                final AsyncTreeModel<SampleNode> model, final SampleNode destination, final int dropIndex,
                                                final List<SampleNode> nodes )
                    {
                        return destination.getUserObject ().getType () != SampleObjectType.leaf;
                    }
                } );
            }

            /**
             * We do not need to copy children as {@link AsyncTreeDataProvider} will do that instead.
             * We only need to provide a copy of the specified node here.
             */
            @Override
            protected SampleNode copy ( final WebAsyncTree<SampleNode> tree, final AsyncTreeModel<SampleNode> model, final SampleNode node )
            {
                return node.clone ();
            }

            /**
             * Blocks root element drag.
             */
            @Override
            protected boolean canBeDragged ( final WebAsyncTree<SampleNode> tree, final AsyncTreeModel<SampleNode> model,
                                             final List<SampleNode> nodes )
            {
                boolean allowed = true;
                for ( final SampleNode node : nodes )
                {
                    if ( node.getUserObject ().getType () == SampleObjectType.root )
                    {
                        allowed = false;
                        break;
                    }
                }
                return allowed;
            }
        };
    }
}