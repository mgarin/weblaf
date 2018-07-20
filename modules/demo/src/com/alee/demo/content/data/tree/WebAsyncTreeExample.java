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
import com.alee.extended.tree.*;
import com.alee.extended.tree.sample.SampleAsyncDataProvider;
import com.alee.extended.tree.sample.SampleNode;
import com.alee.extended.tree.sample.SampleObjectType;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.MathUtils;
import com.alee.utils.ThreadUtils;

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
        return "webasynctree";
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
                new BasicTree ( StyleId.tree ),
                new EditableTree ( StyleId.tree ),
                new DragAndDropTree ( StyleId.tree )
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
            final WebAsyncTree tree = new WebAsyncTree ( getStyleId (), createDataProvider () );
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
            final WebAsyncTree tree = new WebAsyncTree ( getStyleId (), createDataProvider () );
            tree.setVisibleRowCount ( 8 );
            tree.setEditable ( true );
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
            final WebAsyncTree left = new WebAsyncTree ( getStyleId (), createDataProvider () );
            left.setVisibleRowCount ( 8 );
            left.setEditable ( true );
            left.setTransferHandler ( createTransferHandler () );
            left.setDragEnabled ( true );
            left.setDropMode ( DropMode.ON_OR_INSERT );
            final WebScrollPane leftScroll = new WebScrollPane ( left ).setPreferredWidth ( 200 );

            final WebAsyncTree right = new WebAsyncTree ( getStyleId (), createDataProvider () );
            right.setVisibleRowCount ( 8 );
            right.setEditable ( true );
            right.setTransferHandler ( createTransferHandler () );
            right.setDragEnabled ( true );
            right.setDropMode ( DropMode.ON_OR_INSERT );
            final WebScrollPane rightScroll = new WebScrollPane ( right ).setPreferredWidth ( 200 );

            return CollectionUtils.asList ( leftScroll, rightScroll );
        }
    }

    /**
     * Returns sample tree data provider.
     * It will provide all tree data we need instead of the model.
     *
     * @return sample tree data provider
     */
    protected static AsyncTreeDataProvider<SampleNode> createDataProvider ()
    {
        return new SampleAsyncDataProvider ()
        {
            /**
             * Adds extra loading time to showcase asynchronous data loading.
             * It only excludes root node children to avoid awkward demo startup.
             * Stalling the thread here won't hurt the UI since it is always executed on non-EDT thread.
             */
            @Override
            public void loadChildren ( final SampleNode parent, final NodesLoadCallback<SampleNode> listener )
            {
                if ( parent.getUserObject ().getType () != SampleObjectType.root )
                {
                    ThreadUtils.sleepSafely ( MathUtils.random ( 100, 2000 ) );
                }
                super.loadChildren ( parent, listener );
            }
        };
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