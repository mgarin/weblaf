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
     * Actual content renderer.
     */
    protected TreeCellRenderer actualRenderer;

    /**
     * Checkbox component used to renderer checkbox on the tree.
     */
    protected WebTristateCheckBox checkBox;

    /**
     * Constructs new checkbox tree cell renderer.
     *
     * @param actualRenderer actual content renderer
     */
    public WebCheckBoxTreeCellRenderer ( final TreeCellRenderer actualRenderer )
    {
        super ();
        this.actualRenderer = actualRenderer;

        setOpaque ( false );

        checkBox = new WebTristateCheckBox ();
        checkBox.setAnimated ( false );
        checkBox.setMargin ( 0, 4, 0, WebCheckBoxTreeStyle.checkBoxRendererGap );
        add ( checkBox, BorderLayout.LINE_START );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCheckBoxRendererGap ()
    {
        return checkBox.getMargin ().right;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCheckBoxRendererGap ( final int checkBoxRendererGap )
    {
        checkBox.getMargin ().right = checkBoxRendererGap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TreeCellRenderer getActualRenderer ()
    {
        return actualRenderer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setActualRenderer ( final TreeCellRenderer actualRenderer )
    {
        this.actualRenderer = actualRenderer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCheckBoxWidth ()
    {
        return checkBox.getPreferredSize ().width;
    }

    /**
     * {@inheritDoc}
     */
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
        // Updating check state
        checkBox.setState ( ( ( WebCheckBoxTree ) tree ).getCheckState ( ( DefaultMutableTreeNode ) value ) );

        // Updating actual cell renderer
        add ( actualRenderer.getTreeCellRendererComponent ( tree, value, selected, expanded, leaf, row, hasFocus ), BorderLayout.CENTER );

        return this;
    }
}