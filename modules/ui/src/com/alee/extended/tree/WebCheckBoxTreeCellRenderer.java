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

import com.alee.api.ui.ChildStyleIdBridge;
import com.alee.api.ui.StyleIdBridge;
import com.alee.extended.checkbox.WebTristateCheckBox;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.tree.WTreeUI;
import com.alee.managers.style.ChildStyleId;
import com.alee.managers.style.StyleId;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.painter.decoration.Stateful;

import javax.swing.*;
import javax.swing.plaf.TreeUI;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Default {@link CheckBoxTreeCellRenderer} implementation for {@link WebCheckBoxTree}.
 * It is based on {@link WebPanel} and uses {@link WebTristateCheckBox} and actual {@link TreeCellRenderer} to create complete view.
 *
 * @param <N> {@link MutableTreeNode} type
 * @param <C> {@link WebCheckBoxTree} type
 * @param <P> {@link CheckBoxTreeNodeParameters} type
 * @author Mikle Garin
 * @see CheckBoxTreeNodeParameters
 */
public class WebCheckBoxTreeCellRenderer<N extends MutableTreeNode, C extends WebCheckBoxTree<N>, P extends CheckBoxTreeNodeParameters<N, C>>
        extends WebPanel implements CheckBoxTreeCellRenderer, Stateful
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
     * @param parameters {@link CheckBoxTreeNodeParameters}
     */
    protected void updateStates ( final P parameters )
    {
        states.clear ();

        // Selection state
        states.add ( parameters.isSelected () ? DecorationState.selected : DecorationState.unselected );

        // Expansion state
        states.add ( parameters.isExpanded () ? DecorationState.expanded : DecorationState.collapsed );

        // Focus state
        if ( parameters.isFocused () )
        {
            states.add ( DecorationState.focused );
        }

        // Leaf state
        if ( parameters.isLeaf () )
        {
            states.add ( DecorationState.leaf );
        }

        // Hover state
        final TreeUI ui = parameters.tree ().getUI ();
        if ( ui instanceof WTreeUI )
        {
            if ( ( ( WTreeUI ) ui ).getHoverRow () == parameters.row () )
            {
                states.add ( DecorationState.hover );
            }
        }

        // Checkbox check state
        states.add ( parameters.tree ().getCheckState ( parameters.node () ).name () );

        // Checkbox visibility
        states.add ( parameters.isCheckBoxVisible () ? DecorationState.checkVisible : DecorationState.checkHidden );

        // Extra states provided by node
        states.addAll ( DecorationUtils.getExtraStates ( parameters.node () ) );
    }

    /**
     * Updates tree cell renderer component style ID.
     *
     * @param parameters {@link CheckBoxTreeNodeParameters}
     */
    protected void updateStyleId ( final P parameters )
    {
        // Renderer style identifier
        StyleId id = null;
        if ( parameters.node () instanceof ChildStyleIdBridge )
        {
            final ChildStyleIdBridge childStyleIdBridge = ( ChildStyleIdBridge ) parameters.node ();
            final ChildStyleId childStyleId = childStyleIdBridge.getChildStyleId ( parameters );
            if ( childStyleId != null )
            {
                id = childStyleId.at ( parameters.tree () );
            }
        }
        else if ( parameters.node () instanceof StyleIdBridge )
        {
            final StyleIdBridge styleIdBridge = ( StyleIdBridge ) parameters.node ();
            final StyleId styleId = styleIdBridge.getStyleId ( parameters );
            if ( styleId != null )
            {
                id = styleId;
            }
        }
        if ( id == null )
        {
            id = StyleId.checkboxtreeCellRenderer.at ( parameters.tree () );
        }
        setStyleId ( id );

        // Checkbox style identifier
        checkBox.setStyleId ( StyleId.checkboxtreeCheckBox.at ( this ) );
    }

    /**
     * Updating renderer based on the provided settings.
     *
     * @param parameters {@link CheckBoxTreeNodeParameters}
     */
    protected void updateView ( final P parameters )
    {
        // Updating renderer
        removeAll ();
        setLayout ( new BorderLayout ( parameters.tree ().getCheckBoxRendererGap (), 0 ) );
        setEnabled ( parameters.isCheckBoxEnabled () );
        setComponentOrientation ( orientationForValue ( parameters ) );

        // Configuring check box
        if ( parameters.isCheckBoxVisible () )
        {
            checkBox.setEnabled ( parameters.isCheckBoxEnabled () );
            checkBox.setState ( parameters.tree ().getCheckState ( parameters.node () ) );
            add ( checkBox, BorderLayout.LINE_START );
        }

        // Updating actual cell renderer
        final TreeCellRenderer actual = parameters.tree ().getActualRenderer ();
        final Component renderer = actual.getTreeCellRendererComponent ( parameters.tree (), parameters.node (), parameters.isSelected (),
                parameters.isExpanded (), parameters.isLeaf (), parameters.row (), parameters.isFocused () );
        add ( renderer, BorderLayout.CENTER );
    }

    /**
     * Returns renderer {@link ComponentOrientation} for the specified {@link MutableTreeNode}.
     *
     * @param parameters {@link CheckBoxTreeNodeParameters}
     * @return renderer {@link ComponentOrientation} for the specified {@link MutableTreeNode}
     */
    protected ComponentOrientation orientationForValue ( final P parameters )
    {
        return parameters.tree ().getComponentOrientation ();
    }

    /**
     * Returns tree cell renderer component.
     *
     * @param tree       {@link WebCheckBoxTree}
     * @param node       {@link MutableTreeNode}
     * @param isSelected whether or not {@link MutableTreeNode} is selected
     * @param expanded   whether or not {@link MutableTreeNode} is expanded
     * @param leaf       whether or not {@link MutableTreeNode} is leaf
     * @param row        {@link MutableTreeNode} row number
     * @param hasFocus   whether or not {@link MutableTreeNode} has focus
     * @return cell renderer component
     */
    @Override
    public Component getTreeCellRendererComponent ( final JTree tree, final Object node, final boolean isSelected,
                                                    final boolean expanded, final boolean leaf, final int row, final boolean hasFocus )
    {
        // Forming rendering parameters
        final P parameters = getRenderingParameters ( ( C ) tree, ( N ) node, isSelected, expanded, leaf, row, hasFocus );

        // Updating custom states
        updateStates ( parameters );

        // Updating style ID
        updateStyleId ( parameters );

        // Updating renderer view
        updateView ( parameters );

        // Updating decoration states for this render cycle
        DecorationUtils.fireStatesChanged ( this );

        return this;
    }

    /**
     * Returns {@link CheckBoxTreeNodeParameters}.
     *
     * @param tree       {@link WebCheckBoxTree}
     * @param node       {@link MutableTreeNode}
     * @param isSelected whether or not {@link MutableTreeNode} is selected
     * @param expanded   whether or not {@link MutableTreeNode} is expanded
     * @param leaf       whether or not {@link MutableTreeNode} is leaf
     * @param row        {@link MutableTreeNode} row number
     * @param hasFocus   whether or not {@link MutableTreeNode} has focus
     * @return {@link CheckBoxTreeNodeParameters}
     */
    protected P getRenderingParameters ( final C tree, final N node, final boolean isSelected,
                                         final boolean expanded, final boolean leaf, final int row, final boolean hasFocus )
    {
        return ( P ) new CheckBoxTreeNodeParameters<N, C> ( tree, node, row, leaf, isSelected, expanded, hasFocus );
    }

    @Override
    public final void repaint ( final long tm, final int x, final int y, final int width, final int height )
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public final void repaint ( final Rectangle r )
    {
        /**
         * Overridden for performance reasons.
         */
    }

    @Override
    public final void repaint ()
    {
        /**
         * Overridden for performance reasons.
         */
    }

    /**
     * A subclass of {@link WebCheckBoxTreeCellRenderer} that implements {@link javax.swing.plaf.UIResource}.
     * It is used to determine renderer provided by the UI class to properly uninstall it on UI uninstall.
     *
     * @param <N> {@link MutableTreeNode} type
     * @param <C> {@link WebCheckBoxTree} type
     * @param <P> {@link CheckBoxTreeNodeParameters} type
     */
    public static final class UIResource<N extends MutableTreeNode, C extends WebCheckBoxTree<N>, P extends CheckBoxTreeNodeParameters<N, C>>
            extends WebCheckBoxTreeCellRenderer<N, C, P> implements javax.swing.plaf.UIResource
    {
        /**
         * Implementation is used completely from {@link WebCheckBoxTreeCellRenderer}.
         */
    }
}