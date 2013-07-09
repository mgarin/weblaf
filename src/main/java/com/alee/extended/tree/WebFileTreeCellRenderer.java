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

import com.alee.laf.tree.WebTreeCellRenderer;
import com.alee.laf.tree.WebTreeElement;
import com.alee.utils.FileUtils;

import javax.swing.*;
import java.io.File;

/**
 * File tree cell renderer.
 *
 * @author Mikle Garin
 * @since 1.4
 */

public class WebFileTreeCellRenderer extends WebTreeCellRenderer
{
    /**
     * Returns custom tree cell renderer component.
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
    public WebTreeElement getTreeCellRendererComponent ( JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf,
                                                         int row, boolean hasFocus )
    {
        super.getTreeCellRendererComponent ( tree, value, isSelected, expanded, leaf, row, hasFocus );

        // Values
        final FileTreeNode node = ( FileTreeNode ) value;
        final File file = node.getFile ();

        // File icon
        setIcon ( node.isBusy () ? node.getLoaderIcon () : ( file != null ? FileUtils.getFileIcon ( file, false ) : null ) );

        // File name
        if ( node.getName () != null )
        {
            setText ( node.getName () );
        }
        else if ( file != null )
        {
            String name = FileUtils.getDisplayFileName ( file );
            if ( name != null && !name.trim ().equals ( "" ) )
            {
                setText ( name );
            }
            else
            {
                name = file.getName ();
                if ( !name.trim ().equals ( "" ) )
                {
                    setText ( name != null ? name : "" );
                }
                else
                {
                    setText ( FileUtils.getFileDescription ( file, null ).getDescription () );
                }
            }
        }

        return this;
    }
}