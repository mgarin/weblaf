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

import com.alee.laf.tree.WebTreeElement;
import com.alee.utils.FileUtils;

import javax.swing.*;
import java.io.File;

/**
 * File tree cell renderer.
 *
 * @author Mikle Garin
 */

public class WebFileTreeCellRenderer extends WebAsyncTreeCellRenderer
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
    @Override
    public WebTreeElement getTreeCellRendererComponent ( final JTree tree, final Object value, final boolean isSelected,
                                                         final boolean expanded, final boolean leaf, final int row, final boolean hasFocus )
    {
        super.getTreeCellRendererComponent ( tree, value, isSelected, expanded, leaf, row, hasFocus );

        // Values
        final FileTreeNode node = ( FileTreeNode ) value;
        final File file = node.getFile ();

        // File icon
        if ( !node.isLoading () )
        {
            final ImageIcon icon = file != null ? FileUtils.getFileIcon ( file, false ) : null;
            setIcon ( node.isFailed () ? getFailedStateIcon ( icon ) : icon );
        }

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