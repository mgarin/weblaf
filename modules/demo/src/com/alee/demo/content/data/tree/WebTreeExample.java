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
import com.alee.api.annotations.Nullable;
import com.alee.demo.api.example.*;
import com.alee.extended.tree.AbstractTreeTransferHandler;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.tree.*;
import com.alee.managers.language.LM;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class WebTreeExample extends AbstractStylePreviewExample
{
    @NotNull
    @Override
    public String getId ()
    {
        return "webtree";
    }

    @NotNull
    @Override
    protected String getStyleFileName ()
    {
        return "tree";
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
                new BasicTree ( StyleId.tree ),
                new EditableTree ( StyleId.tree ),
                new TreeTooltips ( StyleId.tree ),
                new DragAndDropTree ( StyleId.tree )
        );
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

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
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

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebTree tree = new WebTree ( getStyleId () );
            tree.setVisibleRowCount ( 8 );
            tree.setEditable ( true );
            return CollectionUtils.asList ( new WebScrollPane ( tree ).setPreferredWidth ( 200 ) );
        }
    }

    /**
     * Custom tree tooltips preview.
     */
    protected class TreeTooltips extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public TreeTooltips ( final StyleId styleId )
        {
            super ( WebTreeExample.this, "tooltips", FeatureState.updated, styleId );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebTree tree = new WebTree ( getStyleId () );
            tree.setVisibleRowCount ( 8 );
            tree.setToolTipProvider ( new TreeToolTipProvider<UniqueNode> ()
            {
                @Override
                @Nullable
                protected String getToolTipText ( @NotNull final JTree tree, @NotNull final TreeCellArea<UniqueNode, JTree> area )
                {
                    return LM.get ( getPreviewLanguageKey ( "node" ), area.row (), area.getValue ( tree ).getUserObject () );
                }
            } );
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

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
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
            public int getSourceActions ( final JComponent c )
            {
                return MOVE;
            }

            @Override
            protected UniqueNode copy ( final WebTree<UniqueNode> tree, final WebTreeModel<UniqueNode> model, final UniqueNode node )
            {
                // Custom node and its children copy algorithm
                final UniqueNode copy = new UniqueNode ( node.getId (), node.getUserObject () );
                for ( int i = 0; i < node.getChildCount (); i++ )
                {
                    copy.add ( copy ( tree, model, ( UniqueNode ) node.getChildAt ( i ) ) );
                }
                return copy;
            }

            @Override
            protected boolean canBeDragged ( final WebTree<UniqueNode> tree, final WebTreeModel<UniqueNode> model,
                                             final List<UniqueNode> nodes )
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