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

import com.alee.api.IconSupport;
import com.alee.api.TitleSupport;
import com.alee.utils.FileUtils;

import javax.swing.*;
import java.io.File;

/**
 * Custom AsyncUniqueNode for WebFileTree.
 *
 * @author Mikle Garin
 */

public class FileTreeNode extends AsyncUniqueNode implements IconSupport, TitleSupport
{
    /**
     * Root node ID.
     */
    public static final String rootId = "File.tree.root";

    /**
     * File for this node.
     */
    protected File file;

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
        super ();
        this.file = file;
    }

    @Override
    public String getId ()
    {
        return file != null ? file.getAbsolutePath () : rootId;
    }

    /**
     * Returns file for this node.
     *
     * @return file for this node
     */
    public File getFile ()
    {
        return file;
    }

    /**
     * Sets file for this node.
     *
     * @param file file for this node
     */
    public void setFile ( final File file )
    {
        this.file = file;
    }

    @Override
    public Icon getNodeIcon ()
    {
        return file != null ? FileUtils.getFileIcon ( file, false ) : null;
    }

    @Override
    public String getTitle ()
    {
        if ( title != null )
        {
            return title;
        }
        else if ( file != null )
        {
            String name = FileUtils.getDisplayFileName ( file );
            if ( name != null && !name.trim ().equals ( "" ) )
            {
                return name;
            }
            else
            {
                name = file.getName ();
                if ( !name.trim ().equals ( "" ) )
                {
                    return name != null ? name : "";
                }
                else
                {
                    return FileUtils.getFileDescription ( file, null ).getDescription ();
                }
            }
        }
        else
        {
            return rootId;
        }
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

    @Override
    public FileTreeNode getParent ()
    {
        return ( FileTreeNode ) super.getParent ();
    }

    @Override
    public FileTreeNode getChildAt ( final int index )
    {
        return ( FileTreeNode ) super.getChildAt ( index );
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