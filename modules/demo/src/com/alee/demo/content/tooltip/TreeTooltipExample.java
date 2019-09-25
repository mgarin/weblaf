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

package com.alee.demo.content.tooltip;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.demo.api.example.*;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.tree.*;
import com.alee.managers.language.LM;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class TreeTooltipExample extends AbstractStylePreviewExample
{
    @NotNull
    @Override
    public String getId ()
    {
        return "treetooltip";
    }

    @NotNull
    @Override
    protected String getStyleFileName ()
    {
        return "customtooltip";
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
                new SwingTooltips ( FeatureState.release, StyleId.tooltip ),
                new FullRowSwingTooltips ( FeatureState.release, StyleId.tooltip ),
                new CustomTooltips ( FeatureState.release, StyleId.customtooltip )
        );
    }

    /**
     * Swing tree tooltips.
     */
    protected class SwingTooltips extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param featureState feature state
         * @param styleId      preview style ID
         */
        public SwingTooltips ( final FeatureState featureState, final StyleId styleId )
        {
            super ( TreeTooltipExample.this, "swing", featureState, styleId );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final JTree tree = new JTree ();
            tree.setVisibleRowCount ( 8 );
            tree.setCellRenderer ( new WebTreeCellRenderer<TreeNode, JTree, TreeNodeParameters<TreeNode, JTree>> ()
            {
                @Override
                protected void updateView ( final TreeNodeParameters<TreeNode, JTree> parameters )
                {
                    super.updateView ( parameters );
                    setToolTipText ( textForValue ( parameters ) );
                }
            } );
            ToolTipManager.sharedInstance ().registerComponent ( tree );
            return CollectionUtils.asList ( new WebScrollPane ( tree ).setPreferredWidth ( 200 ) );
        }
    }

    /**
     * Full row Swing tree tooltips.
     */
    protected class FullRowSwingTooltips extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param featureState feature state
         * @param styleId      preview style ID
         */
        public FullRowSwingTooltips ( final FeatureState featureState, final StyleId styleId )
        {
            super ( TreeTooltipExample.this, "fullrow", featureState, styleId );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebTree tree = new WebTree ();
            tree.setVisibleRowCount ( 8 );
            tree.setCellRenderer ( new WebTreeCellRenderer<TreeNode, JTree, TreeNodeParameters<TreeNode, JTree>> ()
            {
                @Override
                protected void updateView ( final TreeNodeParameters<TreeNode, JTree> parameters )
                {
                    super.updateView ( parameters );
                    setToolTipText ( textForValue ( parameters ) );
                }
            } );
            ToolTipManager.sharedInstance ().registerComponent ( tree );
            return CollectionUtils.asList ( new WebScrollPane ( tree ).setPreferredWidth ( 200 ) );
        }
    }

    /**
     * Custom tree tooltips.
     */
    protected class CustomTooltips extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param featureState feature state
         * @param styleId      preview style ID
         */
        public CustomTooltips ( final FeatureState featureState, final StyleId styleId )
        {
            super ( TreeTooltipExample.this, "custom", featureState, styleId );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final JTree tree = new JTree ();
            tree.setVisibleRowCount ( 8 );
            tree.putClientProperty ( WebTree.TOOLTIP_PROVIDER_PROPERTY, new TreeToolTipProvider<DefaultMutableTreeNode> ()
            {
                @Nullable
                @Override
                protected String getToolTipText ( @NotNull final JTree component,
                                                  @NotNull final TreeCellArea<DefaultMutableTreeNode, JTree> area )
                {
                    return LM.get ( getPreviewLanguageKey ( "node" ), area.row (), area.getValue ( tree ).getUserObject () );
                }
            } );
            return CollectionUtils.asList ( new WebScrollPane ( tree ).setPreferredWidth ( 200 ) );
        }
    }
}