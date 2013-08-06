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

import com.alee.utils.CollectionUtils;
import com.alee.utils.FileUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Asynchronous data provider for WebFileTree.
 *
 * @author Mikle Garin
 * @since 1.4
 */

public class FileTreeDataProvider implements AsyncTreeDataProvider<FileTreeNode>
{
    /**
     * File filter.
     */
    private FileFilter fileFilter;

    /**
     * Tree root files.
     */
    private List<File> rootFiles;

    /**
     * Constructs file tree data provider with the specified files as root.
     *
     * @param rootFiles tree root files
     */
    public FileTreeDataProvider ( File... rootFiles )
    {
        super ();
        this.fileFilter = WebFileTreeStyle.fileFilter;
        this.rootFiles = CollectionUtils.copy ( rootFiles );
    }

    /**
     * Constructs file tree data provider with the specified files as root.
     *
     * @param rootFiles tree root files
     */
    public FileTreeDataProvider ( List<File> rootFiles )
    {
        super ();
        this.fileFilter = WebFileTreeStyle.fileFilter;
        this.rootFiles = rootFiles;
    }

    /**
     * Constructs file tree data provider with the specified files as root.
     *
     * @param fileFilter tree file filter
     * @param rootFiles  tree root files
     */
    public FileTreeDataProvider ( FileFilter fileFilter, File... rootFiles )
    {
        super ();
        this.fileFilter = fileFilter;
        this.rootFiles = CollectionUtils.copy ( rootFiles );
    }

    /**
     * Constructs file tree data provider with the specified files as root.
     *
     * @param fileFilter tree file filter
     * @param rootFiles  tree root files
     */
    public FileTreeDataProvider ( FileFilter fileFilter, List<File> rootFiles )
    {
        super ();
        this.fileFilter = fileFilter;
        this.rootFiles = rootFiles;
    }

    /**
     * Returns tree files filter.
     *
     * @return files filter
     */
    public FileFilter getFileFilter ()
    {
        return fileFilter;
    }

    /**
     * Sets tree files filter.
     *
     * @param fileFilter new files filter
     */
    public void setFileFilter ( FileFilter fileFilter )
    {
        // todo Apply filter to existing childs
        this.fileFilter = fileFilter;
    }

    /**
     * Returns file tree root node.
     *
     * @return file tree root node
     */
    public FileTreeNode getRoot ()
    {
        return new FileTreeNode ( null );
    }

    /**
     * Returns child nodes for specified node.
     *
     * @param node parent node
     * @return child nodes
     */
    public List<FileTreeNode> getChilds ( FileTreeNode node )
    {
        return node.getFile () == null ? getRootChilds () : getFileChilds ( node );
    }

    /**
     * Returns root child nodes.
     *
     * @return root child nodes
     */
    private List<FileTreeNode> getRootChilds ()
    {
        List<FileTreeNode> childs = new ArrayList<FileTreeNode> ();
        for ( File rootFile : rootFiles )
        {
            childs.add ( new FileTreeNode ( rootFile ) );
        }
        return childs;
    }

    /**
     * Returns child nodes for specified node.
     *
     * @param node parent node
     * @return child nodes
     */
    public List<FileTreeNode> getFileChilds ( FileTreeNode node )
    {
        // Retrieving files list from file system
        final File file = node.getFile ();
        File[] childsList = file.listFiles ();
        if ( childsList != null )
        {
            // Filtering files
            if ( fileFilter != null )
            {
                int decay = 0;
                for ( int i = childsList.length - 1; i >= 0; i-- )
                {
                    if ( fileFilter != null && !fileFilter.accept ( childsList[ i ] ) )
                    {
                        childsList[ i ] = null;
                        decay++;
                    }
                }
                final File[] newList = new File[ childsList.length - decay ];
                int index = 0;
                for ( File f : childsList )
                {
                    if ( f != null )
                    {
                        newList[ index ] = f;
                        index++;
                    }
                }
                childsList = newList;
            }

            // Sorting childs
            FileUtils.sortFiles ( childsList );
        }
        else
        {
            childsList = new File[ 0 ];
        }

        // Creating child nodes
        List<FileTreeNode> childs = new ArrayList<FileTreeNode> ();
        for ( File f : childsList )
        {
            childs.add ( new FileTreeNode ( f ) );
        }

        return childs;
    }

    /**
     * Returns whether specified node is leaf or not.
     *
     * @param node node
     * @return true if specified node is leaf, false otherwise
     */
    public boolean isLeaf ( FileTreeNode node )
    {
        return node.getFile () != null && !FileUtils.isDirectory ( node.getFile () );
    }
}