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

import com.alee.utils.FileUtils;

import java.io.File;

/**
 * Custom AsyncUniqueNode for WebFileTree.
 *
 * @author Mikle Garin
 */

public class FileTreeNode extends AsyncUniqueNode
{
    /**
     * File for this node.
     */
    protected File file;

    /**
     * Custom node name.
     */
    protected String name = null;

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

    /**
     * Constructs file node for the specified file with custom name.
     *
     * @param file node file
     * @param name custom node name
     */
    public FileTreeNode ( final File file, final String name )
    {
        super ();
        this.file = file;
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId ()
    {
        return file != null ? file.getAbsolutePath () : "File.tree.root";
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

    /**
     * Returns custom name for this node.
     *
     * @return custom name for this node
     */
    public String getName ()
    {
        return name;
    }

    /**
     * Sets custom name for this node.
     *
     * @param name custom name for this node
     */
    public void setName ( final String name )
    {
        this.name = name;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileTreeNode getParent ()
    {
        return ( FileTreeNode ) super.getParent ();
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    public String toString ()
    {
        return name != null ? name : ( file != null ? file.getName () : "root" );
    }
}