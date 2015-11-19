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

import com.alee.extended.checkbox.WebTristateCheckBox;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.style.StyleId;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

/**
 * Custom default cell renderer for WebCheckBoxTree.
 *
 * @author Mikle Garin
 */

public class WebCheckBoxTreeCellRenderer extends WebPanel implements CheckBoxTreeCellRenderer
{
    /**
     * Checkbox tree.
     */
    protected WebCheckBoxTree checkBoxTree;

    /**
     * Checkbox component used to renderer checkbox on the tree.
     */
    protected WebTristateCheckBox checkBox;

    /**
     * Constructs new checkbox tree cell renderer.
     *
     * @param checkBoxTree checkbox tree to process
     */
    public WebCheckBoxTreeCellRenderer ( final WebCheckBoxTree checkBoxTree )
    {
        super ();
        this.checkBoxTree = checkBoxTree;

        setOpaque ( false );

        checkBox = new WebTristateCheckBox ();
        add ( checkBox, BorderLayout.LINE_START );
    }

    @Override
    public int getCheckBoxRendererGap ()
    {
        return checkBox.getMargin ().right;
    }

    @Override
    public void setCheckBoxRendererGap ( final int checkBoxRendererGap )
    {
        checkBox.getMargin ().right = checkBoxRendererGap;
    }

    @Override
    public int getCheckBoxWidth ()
    {
        return checkBox.getPreferredSize ().width;
    }

    @Override
    public WebTristateCheckBox getCheckBox ()
    {
        return checkBox;
    }

    /**
     * Returns tree cell renderer component.
     *
     * @param tree     tree
     * @param value    cell value
     * @param selected whether cell is selected or not
     * @param expanded whether cell is expanded or not
     * @param leaf     whether cell is leaf or not
     * @param row      cell row number
     * @param hasFocus whether cell has focus or not
     * @return cell renderer component
     */
    @Override
    public Component getTreeCellRendererComponent ( final JTree tree, final Object value, final boolean selected, final boolean expanded,
                                                    final boolean leaf, final int row, final boolean hasFocus )
    {
        final DefaultMutableTreeNode node = ( DefaultMutableTreeNode ) value;
        if ( checkBoxTree.isCheckBoxVisible () && checkBoxTree.isCheckBoxVisible ( node ) )
        {
            // Updating check state
            checkBox.setStyleId ( StyleId.checkboxtreeCellRenderer.at ( tree ) );
            checkBox.setEnabled ( checkBoxTree.isCheckingByUserEnabled () && checkBoxTree.isCheckBoxEnabled ( node ) );
            checkBox.setState ( checkBoxTree.getCheckState ( node ) );

            // Updating actual cell renderer
            final TreeCellRenderer renderer = checkBoxTree.getActualRenderer ();
            add ( renderer.getTreeCellRendererComponent ( tree, value, selected, expanded, leaf, row, hasFocus ), BorderLayout.CENTER );

            return this;
        }
        else
        {
            // Returning actual cell renderer
            final TreeCellRenderer renderer = checkBoxTree.getActualRenderer ();
            return renderer.getTreeCellRendererComponent ( tree, value, selected, expanded, leaf, row, hasFocus );
        }
    }
}