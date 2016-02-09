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
import com.alee.utils.compare.Filter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Asynchronous data provider for WebFileTree.
 *
 * @author Mikle Garin
 */

public class FileTreeDataProvider extends AbstractAsyncTreeDataProvider<FileTreeNode>
{
    /**
     * Tree root files.
     */
    protected List<File> rootFiles;

    /**
     * Constructs file tree data provider with the specified files as root.
     *
     * @param rootFiles tree root files
     */
    public FileTreeDataProvider ( final File... rootFiles )
    {
        super ();
        this.rootFiles = CollectionUtils.asList ( rootFiles );
        this.comparator = new FileTreeNodeComparator ();
        this.filter = new FileTreeNodeFilter ();
    }

    /**
     * Constructs file tree data provider with the specified files as root.
     *
     * @param rootFiles tree root files
     */
    public FileTreeDataProvider ( final List<File> rootFiles )
    {
        super ();
        this.rootFiles = rootFiles;
        this.comparator = new FileTreeNodeComparator ();
        this.filter = new FileTreeNodeFilter ();
    }

    /**
     * Constructs file tree data provider with the specified files as root.
     *
     * @param filter    tree nodes filter
     * @param rootFiles tree root files
     */
    public FileTreeDataProvider ( final Filter<FileTreeNode> filter, final File... rootFiles )
    {
        super ();
        this.rootFiles = CollectionUtils.asList ( rootFiles );
        this.comparator = new FileTreeNodeComparator ();
        this.filter = filter;
    }

    /**
     * Constructs file tree data provider with the specified files as root.
     *
     * @param filter    tree nodes filter
     * @param rootFiles tree root files
     */
    public FileTreeDataProvider ( final Filter<FileTreeNode> filter, final List<File> rootFiles )
    {
        super ();
        this.rootFiles = rootFiles;
        this.comparator = new FileTreeNodeComparator ();
        this.filter = filter;
    }

    @Override
    public FileTreeNode getRoot ()
    {
        return rootFiles.size () == 1 ? new FileTreeNode ( rootFiles.get ( 0 ) ) : new FileTreeNode ( null );
    }

    @Override
    public void loadChildren ( final FileTreeNode parent, final ChildrenListener<FileTreeNode> listener )
    {
        try
        {
            listener.loadCompleted ( parent.getFile () == null ? getRootChildren () : getFileChildren ( parent ) );
        }
        catch ( final Throwable cause )
        {
            listener.loadFailed ( cause );
        }
    }

    /**
     * Returns root child nodes.
     *
     * @return root child nodes
     */
    protected List<FileTreeNode> getRootChildren ()
    {
        final List<FileTreeNode> children = new ArrayList<FileTreeNode> ( rootFiles.size () );
        for ( final File rootFile : rootFiles )
        {
            children.add ( new FileTreeNode ( rootFile ) );
        }
        return children;
    }

    /**
     * Returns child nodes for specified node.
     *
     * @param node parent node
     * @return child nodes
     */
    public List<FileTreeNode> getFileChildren ( final FileTreeNode node )
    {
        final File[] childrenArray = node.getFile ().listFiles ();
        if ( childrenArray == null || childrenArray.length == 0 )
        {
            return new ArrayList<FileTreeNode> ( 0 );
        }
        else
        {
            final List<FileTreeNode> children = new ArrayList<FileTreeNode> ( childrenArray.length );
            for ( final File f : childrenArray )
            {
                children.add ( new FileTreeNode ( f ) );
            }
            return children;
        }
    }

    @Override
    public Filter<FileTreeNode> getChildrenFilter ( final FileTreeNode node )
    {
        // We must not filter out given roots
        return node.getFile () == null ? null : super.getChildrenFilter ( node );
    }

    @Override
    public boolean isLeaf ( final FileTreeNode node )
    {
        return node.getFile () != null && !FileUtils.isDirectory ( node.getFile () );
    }
}