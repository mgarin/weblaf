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

import com.alee.demo.api.*;
import com.alee.extended.tree.*;
import com.alee.extended.tree.sample.SampleNode;
import com.alee.extended.tree.sample.SampleNodeType;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.util.Collections;
import java.util.List;

/**
 * @author Mikle Garin
 */

public class WebExTreeExample extends AbstractExample
{
    @Override
    public String getId ()
    {
        return "webextree";
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
    protected List<Preview> createPreviews ()
    {
        final BasicTree basic = new BasicTree ( StyleId.tree );
        final EditableTree editable = new EditableTree ( StyleId.tree );
        final DragAndDropTree drag = new DragAndDropTree ( StyleId.tree );
        return CollectionUtils.<Preview>asList ( basic, editable, drag );
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
            super ( WebExTreeExample.this, "basic", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final WebExTree tree = new WebExTree ( getStyleId (), createDataProvider () );
            tree.setVisibleRowCount ( 8 );
            return CollectionUtils.asList ( new WebScrollPane ( tree ).setPreferredWidth ( 200 ) );
        }
    }

    /**
     * Editable extended tree preview.
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
            super ( WebExTreeExample.this, "editable", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final WebExTree tree = new WebExTree ( getStyleId (), createDataProvider () );
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
            super ( WebExTreeExample.this, "dragndrop", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final WebExTree left = new WebExTree ( getStyleId (), createDataProvider () );
            left.setVisibleRowCount ( 8 );
            left.setEditable ( true );
            left.setTransferHandler ( createTransferHandler () );
            left.setDragEnabled ( true );
            left.setDropMode ( DropMode.ON_OR_INSERT );
            final WebScrollPane leftScroll = new WebScrollPane ( left ).setPreferredWidth ( 200 );

            final WebExTree right = new WebExTree ( getStyleId (), createDataProvider () );
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
    protected static ExTreeDataProvider<SampleNode> createDataProvider ()
    {
        return new AbstractExTreeDataProvider<SampleNode> ()
        {
            @Override
            public SampleNode getRoot ()
            {
                return new SampleNode ( SampleNodeType.root, "Root" );
            }

            @Override
            public List<SampleNode> getChildren ( final SampleNode parent )
            {
                switch ( parent.getType () )
                {
                    case root:
                    {
                        return createFolders ();
                    }
                    case folder:
                    {
                        return createLeafs ();
                    }
                    default:
                    {
                        return Collections.EMPTY_LIST;
                    }
                }
            }

            @Override
            public boolean isLeaf ( final SampleNode node )
            {
                return node.getType ().equals ( SampleNodeType.leaf );
            }

            /**
             * Returns folder sample elements.
             *
             * @return folder sample elements
             */
            protected List<SampleNode> createFolders ()
            {
                final SampleNode folder1 = new SampleNode ( SampleNodeType.folder, "Folder 1" );
                final SampleNode folder2 = new SampleNode ( SampleNodeType.folder, "Folder 2" );
                final SampleNode folder3 = new SampleNode ( SampleNodeType.folder, "Folder 3" );
                return CollectionUtils.asList ( folder1, folder2, folder3 );
            }

            /**
             * Returns leaf sample elements.
             *
             * @return leaf sample elements
             */
            protected List<SampleNode> createLeafs ()
            {
                final SampleNode leaf1 = new SampleNode ( SampleNodeType.leaf, "Leaf 1" );
                final SampleNode leaf2 = new SampleNode ( SampleNodeType.leaf, "Leaf 2" );
                final SampleNode leaf3 = new SampleNode ( SampleNodeType.leaf, "Leaf 3" );
                return CollectionUtils.asList ( leaf1, leaf2, leaf3 );
            }
        };
    }

    /**
     * Returns sample tree transfer handler.
     * It will provide base functionality of Drag & Drop for our sample tree.
     *
     * @return sample extended tree transfer handler
     */
    protected static ExTreeTransferHandler<SampleNode, WebExTree<SampleNode>> createTransferHandler ()
    {
        return new ExTreeTransferHandler<SampleNode, WebExTree<SampleNode>> ()
        {
            @Override
            protected List<TreeDropHandler<SampleNode, WebExTree<SampleNode>>> createDropHandlers ()
            {
                return CollectionUtils.<TreeDropHandler<SampleNode, WebExTree<SampleNode>>>asList (
                        new NodesDropHandler<SampleNode, WebExTree<SampleNode>> ()
                        {
                            @Override
                            protected boolean canBeDropped ( final WebExTree<SampleNode> tree, final List<SampleNode> nodes,
                                                             final SampleNode dropLocation, final int dropIndex )
                            {
                                return dropLocation.getType () != SampleNodeType.leaf;
                            }
                        } );
            }

            @Override
            protected SampleNode copy ( final WebExTree<SampleNode> tree, final SampleNode node )
            {
                // We do not need to copy children as {@link com.alee.extended.tree.ExTreeDataProvider} will do that instead
                // We only need to provide a copy of the specified node here
                return node.clone ();
            }

            @Override
            protected boolean canBeDragged ( final WebExTree<SampleNode> tree, final List<SampleNode> nodes )
            {
                // Blocking root drag
                boolean allowed = true;
                for ( final SampleNode node : nodes )
                {
                    if ( node.getType () == SampleNodeType.root )
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