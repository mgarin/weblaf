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

import com.alee.api.annotations.NotNull;
import com.alee.api.ui.TextBridge;
import com.alee.laf.tree.TreeNodeParameters;
import com.alee.utils.FileUtils;

import javax.swing.*;
import java.io.File;

/**
 * {@link AsyncUniqueNode} representing single {@link File}.
 *
 * @author Mikle Garin
 */
public class FileTreeNode extends AsyncUniqueNode<FileTreeNode, File>
        implements TextBridge<TreeNodeParameters<FileTreeNode, WebAsyncTree<FileTreeNode>>>
{
    /**
     * Root node ID.
     */
    public static final String rootId = "File.tree.root";

    /**
     * Custom node title.
     */
    protected String title = null;

    /**
     * Constructs file node for the specified file.
     *
     * @param file node file
     */
    public FileTreeNode ( final File file )
    {
        super ( file );
    }

    @NotNull
    @Override
    public String getId ()
    {
        final File file = getUserObject ();
        return file != null ? file.getAbsolutePath () : rootId;
    }

    /**
     * Returns file for this node.
     *
     * @return file for this node
     */
    public File getFile ()
    {
        return getUserObject ();
    }

    /**
     * Sets file for this node.
     *
     * @param file file for this node
     */
    public void setFile ( final File file )
    {
        setUserObject ( file );
    }

    @Override
    public Icon getNodeIcon ( final TreeNodeParameters<FileTreeNode, WebAsyncTree<FileTreeNode>> parameters )
    {
        final File file = getUserObject ();
        return file != null ? FileUtils.getFileIcon ( file, false ) : null;
    }

    @Override
    public String getText ( @NotNull final TreeNodeParameters<FileTreeNode, WebAsyncTree<FileTreeNode>> parameters )
    {
        return getTitle ();
    }

    /**
     * Returns node title.
     *
     * @return node title
     */
    public String getTitle ()
    {
        final String title;
        if ( this.title != null )
        {
            title = this.title;
        }
        else
        {
            final File file = getUserObject ();
            if ( file != null )
            {
                String name = FileUtils.getDisplayFileName ( file );
                if ( name != null && !name.trim ().equals ( "" ) )
                {
                    title = name;
                }
                else
                {
                    name = file.getName ();
                    if ( !name.trim ().equals ( "" ) )
                    {
                        title = name != null ? name : "";
                    }
                    else
                    {
                        title = FileUtils.getFileDescription ( file, null ).getDescription ();
                    }
                }
            }
            else
            {
                title = rootId;
            }
        }
        return title;
    }

    /**
     * Sets custom name for this node.
     *
     * @param title custom name for this node
     */
    public void setTitle ( final String title )
    {
        this.title = title;
    }

    /**
     * Returns index of child node with the specified file.
     *
     * @param file file to search for in child nodes
     * @return index of child node with the specified file
     */
    public int indexOfFileChild ( final File file )
    {
        for ( int i = 0; i < getChildCount (); i++ )
        {
            if ( FileUtils.equals ( getChildAt ( i ).getFile (), file ) )
            {
                return i;
            }
        }
        return -1;
    }

    @Override
    public String toString ()
    {
        return getTitle ();
    }
}