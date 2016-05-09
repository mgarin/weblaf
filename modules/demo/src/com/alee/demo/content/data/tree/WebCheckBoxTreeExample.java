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
import com.alee.extended.tree.WebCheckBoxTree;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.tree.UniqueNode;
import com.alee.laf.tree.WebTreeModel;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.CompareUtils;
import com.alee.utils.swing.StateProvider;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */

public class WebCheckBoxTreeExample extends AbstractExample
{
    @Override
    public String getId ()
    {
        return "webcheckboxtree";
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
        final BasicTree basic = new BasicTree ( StyleId.checkboxtree );
        final CustomizedTree customized = new CustomizedTree ( StyleId.checkboxtree );
        return CollectionUtils.<Preview>asList ( basic, customized );
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
            super ( WebCheckBoxTreeExample.this, "basic", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final WebCheckBoxTree tree = new WebCheckBoxTree ( getStyleId () );
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
            super ( WebCheckBoxTreeExample.this, "custom", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final WebCheckBoxTree tree = new WebCheckBoxTree ( getStyleId (), createSampleModel () );
            tree.setEditable ( true );
            tree.setVisibleRowCount ( 13 );
            tree.setCheckBoxEnabledStateProvider ( new StateProvider<UniqueNode> ()
            {
                @Override
                public boolean provide ( final UniqueNode node )
                {
                    return !node.isLeaf () || !CompareUtils.equals ( node.getParent ().getUserObject ().toString (), "Disabled" );
                }
            } );
            tree.setCheckBoxVisibleStateProvider ( new StateProvider<UniqueNode> ()
            {
                @Override
                public boolean provide ( final UniqueNode node )
                {
                    return !node.isLeaf () || !CompareUtils.equals ( node.getParent ().getUserObject ().toString (), "Hidden" );
                }
            } );
            tree.setEditableStateProvider ( new StateProvider<UniqueNode> ()
            {
                @Override
                public boolean provide ( final UniqueNode node )
                {
                    return node.isLeaf () && CompareUtils.equals ( node.getParent ().getUserObject ().toString (), "Editable" );
                }
            } );
            tree.expandAll ();
            return CollectionUtils.asList ( new WebScrollPane ( tree ).setPreferredWidth ( 280 ) );
        }
    }

    /**
     * Returns sample tree model for checkbox tree.
     *
     * @return sample tree model for checkbox tree
     */
    protected WebTreeModel createSampleModel ()
    {
        final UniqueNode root = new UniqueNode ( "Checkbox tree" );

        UniqueNode parent = new UniqueNode ( "Disabled" );
        parent.add ( new UniqueNode ( "Can't check this" ) );
        parent.add ( new UniqueNode ( "And this one too" ) );
        parent.add ( new UniqueNode ( "Not even this one" ) );
        root.add ( parent );

        parent = new UniqueNode ( "Hidden" );
        parent.add ( new UniqueNode ( "No check here" ) );
        parent.add ( new UniqueNode ( "And for this one" ) );
        parent.add ( new UniqueNode ( "They're all gone" ) );
        root.add ( parent );

        parent = new UniqueNode ( "Editable" );
        parent.add ( new UniqueNode ( "Edit this node" ) );
        parent.add ( new UniqueNode ( "Or this one instead" ) );
        parent.add ( new UniqueNode ( "This one is editable too" ) );
        root.add ( parent );

        return new WebTreeModel<UniqueNode> ( root );
    }
}