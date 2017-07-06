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

package com.alee.extended.tree;

import com.alee.api.ChildStyleSupport;
import com.alee.api.StyleSupport;
import com.alee.extended.checkbox.WebTristateCheckBox;
import com.alee.laf.checkbox.CheckState;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.style.ChildStyleId;
import com.alee.managers.style.StyleId;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.painter.decoration.Stateful;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom default cell renderer for {@link WebCheckBoxTree}.
 *
 * @author Mikle Garin
 */

public class WebCheckBoxTreeCellRenderer extends WebPanel implements CheckBoxTreeCellRenderer, Stateful
{
    /**
     * todo 1. Optimize to update only on actual renderer changes, checkbox should simply change visibility
     */

    /**
     * Current renderer states.
     */
    protected List<String> states;

    /**
     * Checkbox component used to renderer checkbox on the tree.
     */
    protected WebTristateCheckBox checkBox;

    /**
     * Constructs new checkbox tree cell renderer.
     */
    public WebCheckBoxTreeCellRenderer ()
    {
        super ( ( LayoutManager ) null );
        setName ( "Tree.cellRenderer" );
        states = new ArrayList<String> ( 3 );
        checkBox = new WebTristateCheckBox ();
    }

    @Override
    public List<String> getStates ()
    {
        return states;
    }

    @Override
    public WebTristateCheckBox getCheckBox ()
    {
        return checkBox;
    }

    /**
     * Updates custom renderer states based on render cycle settings.
     *
     * @param tree       {@link WebCheckBoxTree}
     * @param node       {@link DefaultMutableTreeNode}
     * @param isSelected whether or not cell is selected
     * @param expanded   whether or not cell is expanded
     * @param leaf       whether or not cell is leaf
     * @param row        cell row number
     * @param hasFocus   whether or not cell has focus
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected void updateStates ( final WebCheckBoxTree tree, final DefaultMutableTreeNode node, final boolean isSelected,
                                  final boolean expanded, final boolean leaf, final int row, final boolean hasFocus )
    {
        states.clear ();

        // Basic states
        states.add ( isSelected ? DecorationState.selected : DecorationState.unselected );
        states.add ( expanded ? DecorationState.expanded : DecorationState.collapsed );
        if ( hasFocus )
        {
            states.add ( DecorationState.focused );
        }
        if ( leaf )
        {
            states.add ( DecorationState.leaf );
        }

        // Checkbox check state
        final CheckState checkState = tree.getCheckState ( node );
        states.add ( checkState.name () );

        // Checkbox visibility
        final boolean checkVisible = isCheckBoxVisible ( tree, node );
        states.add ( checkVisible ? DecorationState.checkVisible : DecorationState.checkHidden );

        // Extra states provided by node
        states.addAll ( DecorationUtils.getExtraStates ( node ) );
    }

    /**
     * Updates tree cell renderer component style ID.
     *
     * @param tree       {@link WebCheckBoxTree}
     * @param node       {@link DefaultMutableTreeNode}
     * @param isSelected whether or not cell is selected
     * @param expanded   whether or not cell is expanded
     * @param leaf       whether or not cell is leaf
     * @param row        cell row number
     * @param hasFocus   whether or not cell has focus
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected void updateStyleId ( final WebCheckBoxTree tree, final DefaultMutableTreeNode node, final boolean isSelected,
                                   final boolean expanded, final boolean leaf, final int row, final boolean hasFocus )
    {
        // Renderer style identifier
        StyleId id = null;
        if ( node instanceof ChildStyleSupport )
        {
            final ChildStyleId childStyleId = ( ( ChildStyleSupport ) node ).getChildStyleId ();
            if ( childStyleId != null )
            {
                id = childStyleId.at ( tree );
            }
        }
        else if ( node instanceof StyleSupport )
        {
            final StyleId styleId = ( ( StyleSupport ) node ).getStyleId ();
            if ( styleId != null )
            {
                id = styleId;
            }
        }
        if ( id == null )
        {
            id = StyleId.checkboxtreeCellRenderer.at ( tree );
        }
        setStyleId ( id );

        // Checkbox style identifier
        checkBox.setStyleId ( StyleId.checkboxtreeCheckBox.at ( this ) );
    }

    /**
     * Updating renderer based on the provided settings.
     *
     * @param tree       {@link WebCheckBoxTree}
     * @param node       {@link DefaultMutableTreeNode}
     * @param isSelected whether or not cell is selected
     * @param expanded   whether or not cell is expanded
     * @param leaf       whether or not cell is leaf
     * @param row        cell row number
     * @param hasFocus   whether or not cell has focus
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected void updateView ( final WebCheckBoxTree tree, final DefaultMutableTreeNode node, final boolean isSelected,
                                final boolean expanded, final boolean leaf, final int row, final boolean hasFocus )
    {
        final boolean visible = isCheckBoxVisible ( tree, node );
        final boolean enabled = isCheckingEnabled ( tree, node );

        // Updating renderer
        removeAll ();
        setLayout ( new BorderLayout ( tree.getCheckBoxRendererGap (), 0 ) );
        setEnabled ( enabled );
        setComponentOrientation ( tree.getComponentOrientation () );

        // Configuring check box
        if ( visible )
        {
            checkBox.setEnabled ( enabled );
            checkBox.setState ( tree.getCheckState ( node ) );
            add ( checkBox, BorderLayout.LINE_START );
        }

        // Updating actual cell renderer
        final TreeCellRenderer actual = tree.getActualRenderer ();
        final Component renderer = actual.getTreeCellRendererComponent ( tree, node, isSelected, expanded, leaf, row, hasFocus );
        add ( renderer, BorderLayout.CENTER );
    }

    /**
     * Returns tree cell renderer component.
     *
     * @param tree       tree
     * @param value      cell value
     * @param isSelected whether cell is selected or not
     * @param expanded   whether cell is expanded or not
     * @param leaf       whether cell is leaf or not
     * @param row        cell row number
     * @param hasFocus   whether cell has focus or not
     * @return cell renderer component
     */
    @Override
    public Component getTreeCellRendererComponent ( final JTree tree, final Object value, final boolean isSelected, final boolean expanded,
                                                    final boolean leaf, final int row, final boolean hasFocus )
    {
        final WebCheckBoxTree checkBoxTree = ( WebCheckBoxTree ) tree;
        final DefaultMutableTreeNode node = ( DefaultMutableTreeNode ) value;

        // Updating custom states
        updateStates ( checkBoxTree, node, isSelected, expanded, leaf, row, hasFocus );

        // Updating style ID
        updateStyleId ( checkBoxTree, node, isSelected, expanded, leaf, row, hasFocus );

        // Updating renderer view
        updateView ( checkBoxTree, node, isSelected, expanded, leaf, row, hasFocus );

        // Updating decoration states for this render cycle
        DecorationUtils.fireStatesChanged ( this );

        return this;
    }

    /**
     * Returns whether or not checkbox for the specified {@link WebCheckBoxTree} node is currently visible.
     *
     * @param tree {@link WebCheckBoxTree}
     * @param node {@link DefaultMutableTreeNode}
     * @return {@code true} if checkbox for the specified {@link WebCheckBoxTree} node is currently visible, {@code false} otherwise
     */
    protected boolean isCheckBoxVisible ( final WebCheckBoxTree tree, final DefaultMutableTreeNode node )
    {
        return tree.isCheckBoxVisible () && tree.isCheckBoxVisible ( node );
    }

    /**
     * Returns whether or not checking for the specified {@link WebCheckBoxTree} node is currently enabled.
     *
     * @param tree {@link WebCheckBoxTree}
     * @param node {@link DefaultMutableTreeNode}
     * @return {@code true} if checking for the specified {@link WebCheckBoxTree} node is currently enabled, {@code false} otherwise
     */
    protected boolean isCheckingEnabled ( final WebCheckBoxTree tree, final DefaultMutableTreeNode node )
    {
        return tree.isEnabled () && tree.isCheckingByUserEnabled () && tree.isCheckBoxEnabled ( node );
    }

    @Override
    public void repaint ( final long tm, final int x, final int y, final int width, final int height )
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public void repaint ( final Rectangle r )
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public void repaint ()
    {
        /**
         * Overridden for performance reasons.
         */
    }
}