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
import com.alee.extended.tree.AbstractTreeTransferHandler;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.tree.UniqueNode;
import com.alee.laf.tree.WebTree;
import com.alee.laf.tree.WebTreeModel;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */

public class WebTreeExample extends AbstractExample
{
    @Override
    public String getId ()
    {
        return "webtree";
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
     * Basic tree preview.
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
            super ( WebTreeExample.this, "basic", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final WebTree tree = new WebTree ( getStyleId () );
            tree.setVisibleRowCount ( 8 );
            return CollectionUtils.asList ( new WebScrollPane ( tree ).setPreferredWidth ( 200 ) );
        }
    }

    /**
     * Editable tree preview.
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
            super ( WebTreeExample.this, "editable", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final WebTree tree = new WebTree ( getStyleId () );
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
            super ( WebTreeExample.this, "dragndrop", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final WebTree left = new WebTree ( getStyleId () );
            left.setVisibleRowCount ( 8 );
            left.setEditable ( true );
            left.setTransferHandler ( createTransferHandler () );
            left.setDragEnabled ( true );
            left.setDropMode ( DropMode.ON_OR_INSERT );
            final WebScrollPane leftScroll = new WebScrollPane ( left ).setPreferredWidth ( 200 );

            final WebTree right = new WebTree ( getStyleId () );
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
     * Returns sample tree transfer handler.
     * It will provide base functionality of Drag & Drop for our sample tree.
     *
     * @return sample extended tree transfer handler
     */
    protected static AbstractTreeTransferHandler<UniqueNode, WebTree<UniqueNode>, WebTreeModel<UniqueNode>> createTransferHandler ()
    {
        return new AbstractTreeTransferHandler<UniqueNode, WebTree<UniqueNode>, WebTreeModel<UniqueNode>> ()
        {
            @Override
            protected UniqueNode copy ( final WebTree<UniqueNode> tree, final UniqueNode node )
            {
                // Custom node and its children copy algorithm
                final UniqueNode copy = new UniqueNode ( node.getId (), node.getUserObject () );
                for ( int i = 0; i < node.getChildCount (); i++ )
                {
                    copy.add ( copy ( tree, ( UniqueNode ) node.getChildAt ( i ) ) );
                }
                return copy;
            }

            @Override
            protected boolean canBeDragged ( final WebTree<UniqueNode> tree, final List<UniqueNode> nodes )
            {
                // Blocking root drag
                boolean allowed = true;
                for ( final UniqueNode node : nodes )
                {
                    if ( node.isRoot () )
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