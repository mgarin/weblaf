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

import com.alee.laf.WebLookAndFeel;
import com.alee.managers.drag.transfer.FilesTransferHandler;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.FileUtils;
import com.alee.utils.compare.Filter;
import org.slf4j.LoggerFactory;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This component is a file tree with asynchronous children loading.
 * It also contains a few additional methods to find, select and edit visible in tree files.
 *
 * @author Mikle Garin
 */
public class WebFileTree extends WebAsyncTree<FileTreeNode>
{
    /**
     * Whether allow files location search by dropping a file onto the tree or not.
     */
    protected boolean filesDropSearchEnabled;

    /**
     * File lookup drop handler.
     */
    protected FilesTransferHandler fileLookupDropHandler = null;

    /**
     * Delayed selection ID to determine wether it is the last one requested or not.
     */
    protected int delayedSelectionId = 0;

    /**
     * Costructs file tree with system hard drives as root.
     */
    public WebFileTree ()
    {
        this ( StyleId.auto, FileTreeRootType.drives );
    }

    /**
     * Constructs file tree with the specified root type.
     *
     * @param rootType file tree root type
     */
    public WebFileTree ( final FileTreeRootType rootType )
    {
        this ( StyleId.auto, rootType.getRoots () );
    }

    /**
     * Constructs file tree with file under specified path as root.
     *
     * @param rootPath path to root file
     */
    public WebFileTree ( final String rootPath )
    {
        this ( StyleId.auto, new File ( rootPath ) );
    }

    /**
     * Constructs file tree with specified files as root.
     *
     * @param rootFiles root files
     */
    public WebFileTree ( final File... rootFiles )
    {
        this ( StyleId.auto, CollectionUtils.asList ( rootFiles ) );
    }

    /**
     * Constructs file tree with specified files as root.
     *
     * @param rootFiles root files
     */
    public WebFileTree ( final List<File> rootFiles )
    {
        this ( StyleId.auto, rootFiles );
    }

    /**
     * Costructs file tree with system hard drives as root.
     *
     * @param id style ID
     */
    public WebFileTree ( final StyleId id )
    {
        this ( id, FileTreeRootType.drives );
    }

    /**
     * Constructs file tree with the specified root type.
     *
     * @param id       style ID
     * @param rootType file tree root type
     */
    public WebFileTree ( final StyleId id, final FileTreeRootType rootType )
    {
        this ( id, rootType.getRoots () );
    }

    /**
     * Constructs file tree with file under specified path as root.
     *
     * @param id       style ID
     * @param rootPath path to root file
     */
    public WebFileTree ( final StyleId id, final String rootPath )
    {
        this ( id, new File ( rootPath ) );
    }

    /**
     * Constructs file tree with specified files as root.
     *
     * @param id        style ID
     * @param rootFiles root files
     */
    public WebFileTree ( final StyleId id, final File... rootFiles )
    {
        this ( id, CollectionUtils.asList ( rootFiles ) );
    }

    /**
     * Constructs file tree with specified files as root.
     *
     * @param id        style ID
     * @param rootFiles root files
     */
    public WebFileTree ( final StyleId id, final List<File> rootFiles )
    {
        super ( id, new FileTreeDataProvider ( rootFiles ), new WebFileTreeCellEditor () );

        // Visual settings
        setEditable ( false );
        setShowsRootHandles ( true );
        setRootVisible ( rootFiles != null && rootFiles.size () == 1 );

        // Transfer handler
        setFilesDropSearchEnabled ( true );
    }

    @Override
    public FileTreeDataProvider getDataProvider ()
    {
        return ( FileTreeDataProvider ) super.getDataProvider ();
    }

    @Override
    public void setModel ( final TreeModel newModel )
    {
        // Disable asynchronous loading for the model installation time
        // This made to load initial data without delay using EDT
        // This is some kind of workaround for file chooser to allow it proper file expansion on first load
        final boolean async = isAsyncLoading ();
        setAsyncLoading ( false );
        super.setModel ( newModel );
        setAsyncLoading ( async );
    }

    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.filetree;
    }

    /**
     * Returns file drop handler that locates file in the tree when dropped.
     *
     * @return file lookup drop handler
     */
    protected FilesTransferHandler getFileLookupDropHandler ()
    {
        if ( fileLookupDropHandler == null )
        {
            fileLookupDropHandler = new FilesTransferHandler ( false, filesDropSearchEnabled )
            {
                @Override
                public boolean isDropEnabled ()
                {
                    return filesDropSearchEnabled;
                }

                @Override
                public boolean filesDropped ( final List<File> files )
                {
                    // Selecting dragged files in tree
                    if ( files.size () > 0 )
                    {
                        setSelectedFile ( files.get ( 0 ), true );
                        return true;
                    }
                    return false;
                }
            };
        }
        return fileLookupDropHandler;
    }

    /**
     * Returns whether files search by dropping system files on the tree enabled or not.
     *
     * @return true if files search by dropping system files on the tree enabled, false otherwise
     */
    public boolean isFilesDropSearchEnabled ()
    {
        return filesDropSearchEnabled;
    }

    /**
     * Sets whether files search by dropping system files on the tree enabled or not
     *
     * @param filesDropSearchEnabled whether files search by dropping system files on the tree enabled or not
     */
    public void setFilesDropSearchEnabled ( final boolean filesDropSearchEnabled )
    {
        this.filesDropSearchEnabled = filesDropSearchEnabled;

        final FilesTransferHandler lookupDropHandler = getFileLookupDropHandler ();
        if ( filesDropSearchEnabled )
        {
            setTransferHandler ( lookupDropHandler );
        }
        else if ( getTransferHandler () == lookupDropHandler )
        {
            setTransferHandler ( null );
        }
    }

    /**
     * Returns tree files filter.
     *
     * @return files filter
     */
    public Filter<File> getFileFilter ()
    {
        final Filter<FileTreeNode> filter = getFilter ();
        return filter instanceof FileTreeNodeFilter ? ( ( FileTreeNodeFilter ) filter ).getFilter () : null;
    }

    /**
     * Sets tree files filter.
     *
     * @param filter new files filter
     */
    public void setFileFilter ( final Filter<File> filter )
    {
        // We don't update old FileTreeNodeFilter with new file filter as this will force us to manually update tree
        // So we simply create and set new FileTreeNodeFilter so that model can handle filter change and upate the tree
        setFilter ( filter != null ? new FileTreeNodeFilter ( filter ) : null );
    }

    /**
     * Changes displayed tree root name.
     *
     * @param rootName new root name
     */
    public void setRootName ( final String rootName )
    {
        final FileTreeNode rootNode = getRootNode ();
        rootNode.setTitle ( rootName );
        getModel ().updateNode ( rootNode );
    }

    /**
     * Finds and selects specified file in tree.
     * This method might not have any effect in case the specified field doesn't exist under the file tree root.
     *
     * @param file file to select
     */
    public void setSelectedFile ( final File file )
    {
        setSelectedFile ( file, false );
    }

    /**
     * Finds and selects specified file in tree.
     * This method might not have any effect in case the specified field doesn't exist under the file tree root.
     *
     * @param file   file to select
     * @param expand whether to expand selected file or not
     */
    public void setSelectedFile ( final File file, final boolean expand )
    {
        expandToFile ( file, true, expand );
    }

    /**
     * Expands tree structure to the specified file and expands that file node.
     * This method might not have any effect in case the specified field doesn't exist under the file tree root.
     *
     * @param file file to expand
     */
    public void expandFile ( final File file )
    {
        expandToFile ( file, false, true );
    }

    /**
     * Expands tree structure to the specified file.
     * This method might not have any effect in case the specified field doesn't exist under the file tree root.
     *
     * @param file file to expand tree sctructure to
     */
    public void expandToFile ( final File file )
    {
        expandToFile ( file, false, false );
    }

    /**
     * Expands tree structure to the specified file.
     * This method might not have any effect in case the specified field doesn't exist under the file tree root.
     *
     * @param file   file to expand tree sctructure to
     * @param select whether to select file or not
     */
    public void expandToFile ( final File file, final boolean select )
    {
        expandToFile ( file, select, false );
    }

    /**
     * Expands tree structure to the specified file.
     * This method might not have any effect in case the specified field doesn't exist under the file tree root.
     *
     * @param file   file to expand tree sctructure to
     * @param select whether to select file or not
     * @param expand whether to expand file or not
     */
    public void expandToFile ( final File file, final boolean select, final boolean expand )
    {
        expandToFile ( file, select, expand, null );
    }

    /**
     * Expands tree structure to the specified file.
     * This method might not have any effect in case the specified field doesn't exist under the file tree root.
     * todo Replace Runnable with listener (nodeExpanded, beforeSelection, afterSelection, completed)
     *
     * @param file        file to expand tree sctructure to
     * @param select      whether to select file or not
     * @param expand      whether to expand file or not
     * @param finalAction action performed after maximum possible file path has been expanded
     */
    public void expandToFile ( final File file, final boolean select, final boolean expand, final Runnable finalAction )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Expanding path
        if ( file != null )
        {
            delayedSelectionId++;
            final int selectionId = delayedSelectionId;

            // Expanding whole path
            final FileTreeNode node = getClosestNode ( file );
            if ( node != null )
            {
                if ( FileUtils.equals ( node.getFile (), file ) )
                {
                    if ( select && selectionId == delayedSelectionId )
                    {
                        performFileSelection ( node, expand );
                    }
                    else
                    {
                        if ( expand )
                        {
                            expandNode ( node );
                        }
                        scrollToNode ( node );
                    }
                    if ( finalAction != null )
                    {
                        finalAction.run ();
                    }
                }
                else
                {
                    final List<File> path = FileUtils.getFilePath ( file );

                    // Removing already opened nodes
                    final int index = path.indexOf ( node.getFile () );
                    for ( int i = index; i >= 0; i-- )
                    {
                        path.remove ( i );
                    }

                    // Opening path to file
                    addAsyncTreeListener ( new AsyncTreeAdapter<FileTreeNode> ()
                    {
                        private FileTreeNode lastNode = node;

                        @Override
                        public void loadCompleted ( final FileTreeNode parent, final List<FileTreeNode> children )
                        {
                            if ( parent == lastNode )
                            {
                                // Searching for path part in children
                                boolean found = false;
                                for ( final FileTreeNode child : children )
                                {
                                    if ( child.getFile ().equals ( path.get ( 0 ) ) )
                                    {
                                        found = true;
                                        if ( path.size () == 1 )
                                        {
                                            removeAsyncTreeListener ( this );
                                            if ( select && selectionId == delayedSelectionId )
                                            {
                                                performFileSelection ( child, expand );
                                            }
                                            else
                                            {
                                                if ( expand )
                                                {
                                                    expandNode ( child );
                                                }
                                                scrollToNode ( child );
                                            }
                                            if ( finalAction != null )
                                            {
                                                finalAction.run ();
                                            }
                                            break;
                                        }
                                        else
                                        {
                                            lastNode = child;
                                            path.remove ( 0 );
                                            reloadNode ( child );
                                            break;
                                        }
                                    }
                                }
                                if ( !found )
                                {
                                    removeAsyncTreeListener ( this );
                                    if ( select && selectionId == delayedSelectionId )
                                    {
                                        performFileSelection ( parent, false );
                                    }
                                    else
                                    {
                                        scrollToNode ( parent );
                                    }
                                    if ( finalAction != null )
                                    {
                                        finalAction.run ();
                                    }
                                }
                            }
                        }

                        @Override
                        public void loadFailed ( final FileTreeNode parent, final Throwable cause )
                        {
                            if ( parent == lastNode )
                            {
                                removeAsyncTreeListener ( this );
                                if ( select && selectionId == delayedSelectionId )
                                {
                                    performFileSelection ( parent, false );
                                }
                                else
                                {
                                    scrollToNode ( parent );
                                }
                                if ( finalAction != null )
                                {
                                    finalAction.run ();
                                }
                            }
                        }
                    } );

                    // Reload node to make sure we see up-to-date files list
                    reloadNode ( node );
                }
            }
        }
        else if ( select )
        {
            clearSelection ();
        }
    }

    /**
     * Performs the actual file selection.
     *
     * @param node   node to select
     * @param expand should expand the node
     */
    protected void performFileSelection ( final FileTreeNode node, final boolean expand )
    {
        try
        {
            // Selecting node
            final TreePath path = node.getTreePath ();
            setSelectionPath ( path );

            // Expanding if requested
            if ( expand )
            {
                // Expanding
                if ( !isExpandSelected () )
                {
                    expandPath ( path );
                }

                // todo Use a better view rect?
                // Scrolling view to node children
                final Rectangle pathBounds = getPathBounds ( path );
                if ( pathBounds != null )
                {
                    final Rectangle vr = getVisibleRect ();
                    final Rectangle rect = new Rectangle ( vr.x, pathBounds.y, vr.width, vr.height );
                    scrollRectToVisible ( rect );
                }
            }
            else
            {
                // todo Use a better view rect?
                // Properly scrolling view
                scrollPathToVisible ( path );
            }
        }
        catch ( final Exception e )
        {
            LoggerFactory.getLogger ( WebFileTree.class ).error ( e.toString (), e );
        }
    }

    /**
     * Returns selected file.
     *
     * @return selected file
     */
    public File getSelectedFile ()
    {
        final FileTreeNode selectedNode = getSelectedNode ();
        return selectedNode != null ? selectedNode.getFile () : null;
    }

    /**
     * Returns selected files.
     *
     * @return selected files
     */
    public List<File> getSelectedFiles ()
    {
        final List<File> selectedFiles = new ArrayList<File> ();
        if ( getSelectionPaths () != null )
        {
            for ( final TreePath path : getSelectionPaths () )
            {
                selectedFiles.add ( getNodeForPath ( path ).getFile () );
            }
        }
        return selectedFiles;
    }

    /**
     * Returns selected nodes.
     *
     * @return selected nodes
     */
    @Override
    public List<FileTreeNode> getSelectedNodes ()
    {
        final List<FileTreeNode> selectedNodes = new ArrayList<FileTreeNode> ();
        if ( getSelectionPaths () != null )
        {
            for ( final TreePath path : getSelectionPaths () )
            {
                selectedNodes.add ( getNodeForPath ( path ) );
            }
        }
        return selectedNodes;
    }

    /**
     * Adds new file into tree structure.
     * This method will have effect only if node with parent file exists and it has already loaded children.
     *
     * @param parent parent file
     * @param file   added file
     * @return true if tree structure was changed by the operation, false otherwise
     */
    public boolean addFile ( final File parent, final File file )
    {
        return addFiles ( parent, file );
    }

    /**
     * Adds new file into tree structure.
     * This method will have effect only if node with parent file exists and it has already loaded children.
     *
     * @param parentNode parent node
     * @param file       added file
     * @return true if tree structure was changed by the operation, false otherwise
     */
    public boolean addFile ( final FileTreeNode parentNode, final File file )
    {
        return addFiles ( parentNode, file );
    }

    /**
     * Adds new files into tree structure.
     * This method will have effect only if node with parent file exists and it has already loaded children.
     *
     * @param parent parent file
     * @param files  added files
     * @return true if tree structure was changed by the operation, false otherwise
     */
    public boolean addFiles ( final File parent, final File... files )
    {
        return addFiles ( parent, CollectionUtils.asList ( files ) );
    }

    /**
     * Adds new files into tree structure.
     * This method will have effect only if node with parent file exists and it has already loaded children.
     *
     * @param parentNode parent node
     * @param files      added files
     * @return true if tree structure was changed by the operation, false otherwise
     */
    public boolean addFiles ( final FileTreeNode parentNode, final File... files )
    {
        return addFiles ( parentNode, CollectionUtils.asList ( files ) );
    }

    /**
     * Adds new files into tree structure.
     * This method will have effect only if node with parent file exists and it has already loaded children.
     *
     * @param parent parent file
     * @param files  added files
     * @return true if tree structure was changed by the operation, false otherwise
     */
    public boolean addFiles ( final File parent, final List<File> files )
    {
        // Checking that node for the file exists
        final FileTreeNode parentNode = getNode ( parent );
        return parentNode != null && addFiles ( parentNode, files );
    }

    /**
     * Adds new files into tree structure.
     * This method will have effect only if node with parent file exists and it has already loaded children.
     *
     * @param parentNode parent node
     * @param files      added files
     * @return true if tree structure was changed by the operation, false otherwise
     */
    public boolean addFiles ( final FileTreeNode parentNode, final List<File> files )
    {
        if ( hasBeenExpanded ( getPathForNode ( parentNode ) ) )
        {
            final List<FileTreeNode> childNodes = new ArrayList<FileTreeNode> ( files.size () );
            for ( final File file : files )
            {
                childNodes.add ( new FileTreeNode ( file ) );
            }
            addChildNodes ( parentNode, childNodes );
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Removes file from tree structure.
     * This method will have effect only if node with the specified file exists.
     *
     * @param file removed file
     */
    public void removeFile ( final File file )
    {
        removeNode ( getNode ( file ) );
    }

    /**
     * Removes files from tree structure.
     * This method only works if nodes with the specified files exist.
     *
     * @param files removed files
     */
    public void removeFiles ( final File... files )
    {
        // todo Optimize (multi-node delete method in model)
        for ( final File file : files )
        {
            removeFile ( file );
        }
    }

    /**
     * Removes files from tree structure.
     * This method only works if nodes with the specified files exist.
     *
     * @param files removed files
     */
    public void removeFiles ( final List<File> files )
    {
        // todo Optimize (multi-node delete method in model)
        for ( final File file : files )
        {
            removeFile ( file );
        }
    }

    /**
     * Starts editing cell with the specified file.
     *
     * @param file file to edit
     */
    public void startEditingFile ( final File file )
    {
        final FileTreeNode node = getNode ( file );
        if ( node != null )
        {
            startEditingNode ( node );
        }
        else
        {
            expandToFile ( file, false, false, new Runnable ()
            {
                @Override
                public void run ()
                {
                    final FileTreeNode node = getNode ( file );
                    if ( node != null )
                    {
                        startEditingNode ( node );
                    }
                }
            } );
        }
    }

    /**
     * Returns files collected from loaded node children.
     * This method will not force children load.
     *
     * @param node node
     * @return files from node children
     */
    public List<File> getFileChildren ( final FileTreeNode node )
    {
        final List<File> files = new ArrayList<File> ();
        for ( int i = 0; i < node.getChildCount (); i++ )
        {
            files.add ( node.getChildAt ( i ).getFile () );
        }
        return files;
    }

    /**
     * Returns node for the specified file if it is already loaded.
     * Returns null if node for this file was not loaded yet or if the specified file doesn't exist under the file tree root.
     *
     * @param file file to search for
     * @return file node
     */
    public FileTreeNode getNode ( final File file )
    {
        final FileTreeNode node = getClosestNode ( file );
        return node != null && FileUtils.equals ( file, node.getFile () ) ? node : null;
    }

    /**
     * Returns loaded and closest to file node.
     * Might return null in case file path parts were not found in tree.
     *
     * @param file file to look for
     * @return closest to file node
     */
    public FileTreeNode getClosestNode ( final File file )
    {
        return getClosestNode ( getModel ().getRoot (), FileUtils.getFilePath ( file ) );
    }

    /**
     * Returns loaded and closest to file node.
     * Might return null in case file path parts were not found in tree.
     *
     * @param node node to look into
     * @param path path of the file to look for
     * @return closest to file node
     */
    protected FileTreeNode getClosestNode ( final FileTreeNode node, final List<File> path )
    {
        // Check if this file is part of the file path
        final File file = node.getFile ();
        if ( file != null )
        {
            if ( path.contains ( file ) )
            {
                // Filter out upper levels of path
                final int index = path.indexOf ( file );
                for ( int i = index; i >= 0; i-- )
                {
                    path.remove ( i );
                }

                // Find the deepest laoded path node
                return getDeepestPathNode ( node, path );
            }
        }

        // Check child nodes
        for ( int i = 0; i < node.getChildCount (); i++ )
        {
            final FileTreeNode found = getClosestNode ( node.getChildAt ( i ), path );
            if ( found != null )
            {
                return found;
            }
        }

        // No nodes found
        return null;
    }

    /**
     * Returns deepest path node available.
     *
     * @param pathNode current path node
     * @param path     files path
     * @return deepest path node available
     */
    protected FileTreeNode getDeepestPathNode ( final FileTreeNode pathNode, final List<File> path )
    {
        if ( path.size () > 0 )
        {
            for ( int i = 0; i < pathNode.getChildCount (); i++ )
            {
                final FileTreeNode child = pathNode.getChildAt ( i );
                if ( child.getFile ().equals ( path.get ( 0 ) ) )
                {
                    path.remove ( 0 );
                    return getDeepestPathNode ( child, path );
                }
            }
        }
        return pathNode;
    }

    /**
     * Reloads child files for the specified folder.
     * Unlike asynchronous methods this one works in EDT and forces to wait until the nodes load finishes.
     *
     * @param folder folder to reload children for
     */
    public void reloadChildrenSync ( final File folder )
    {
        reloadChildrenSync ( folder, false );
    }

    /**
     * Reloads child files for the specified folder and selects folder node if requested.
     * Unlike asynchronous methods this one works in EDT and forces to wait until the nodes load finishes.
     *
     * @param folder folder to reload children for
     * @param select whether select folder node or not
     */
    public void reloadChildrenSync ( final File folder, final boolean select )
    {
        final FileTreeNode node = getNode ( folder );
        if ( node != null )
        {
            reloadNodeSync ( node, select );
        }
    }

    /**
     * Reloads child files for the specified folder.
     *
     * @param folder folder to reload children for
     */
    public void reloadChildren ( final File folder )
    {
        reloadChildren ( folder, false );
    }

    /**
     * Reloads child files for the specified folder and selects folder node if requested.
     *
     * @param folder folder to reload children for
     * @param select whether select folder node or not
     */
    public void reloadChildren ( final File folder, final boolean select )
    {
        final FileTreeNode node = getNode ( folder );
        if ( node != null )
        {
            reloadNode ( node, select );
        }
    }
}