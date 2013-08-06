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

package com.alee.extended.language;

import com.alee.laf.tree.WebTree;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.language.data.*;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * User: mgarin Date: 04.06.12 Time: 15:29
 */

public class DictionariesTree extends WebTree
{
    private DefaultMutableTreeNode root;
    private DefaultTreeModel model;

    public DictionariesTree ()
    {
        super ();
        setEditable ( true );
        setShowsRootHandles ( false );
        setSelectionMode ( TreeSelectionModel.SINGLE_TREE_SELECTION );
        setDragEnabled ( true );

        // D&D handler
        DictionariesTransferHandler.install ( this );

        // Model
        root = new DefaultMutableTreeNode ( null );
        model = new DefaultTreeModel ( root );
        setModel ( model );

        // Renderer & editor
        setCellRenderer ( new DictionariesTreeRenderer () );
        setCellEditor ( new DictionariesTreeEditor ( this ) );

        // Full record expansion
        addTreeExpansionListener ( new TreeExpansionListener ()
        {
            public void treeExpanded ( TreeExpansionEvent event )
            {
                Object object = event.getPath ().getLastPathComponent ();
                DefaultMutableTreeNode node = ( DefaultMutableTreeNode ) object;
                Object uo = node.getUserObject ();
                if ( uo instanceof Record )
                {
                    for ( int i = 0; i < node.getChildCount (); i++ )
                    {
                        DefaultMutableTreeNode child = ( DefaultMutableTreeNode ) node.getChildAt ( i );
                        expandPath ( new TreePath ( child.getPath () ) );
                    }
                }
            }

            public void treeCollapsed ( TreeExpansionEvent event )
            {
                //
            }
        } );

        // Hotkeys listener
        addKeyListener ( new KeyAdapter ()
        {
            public void keyPressed ( KeyEvent e )
            {
                if ( Hotkey.DELETE.isTriggered ( e ) )
                {
                    DefaultMutableTreeNode node = getSelectedNode ();
                    if ( node != null && node.getUserObject () != null )
                    {
                        // Saving selection row
                        int row = getRowForPath ( getSelectionPath () );

                        // Removing actual data
                        Object type = node.getUserObject ();
                        if ( type instanceof Dictionary )
                        {
                            // Removing Dictionary from subdictionaries list
                            Object uo = ( ( DefaultMutableTreeNode ) node.getParent () ).getUserObject ();
                            if ( uo != null )
                            {
                                Dictionary parent = ( Dictionary ) uo;
                                parent.removeSubdictionary ( ( Dictionary ) type );
                            }
                        }
                        else if ( type instanceof Record )
                        {
                            // Removing Record from Dictionary
                            Object uo = ( ( DefaultMutableTreeNode ) node.getParent () ).getUserObject ();
                            ( ( Dictionary ) uo ).removeRecord ( ( Record ) type );
                        }
                        else if ( type instanceof Value )
                        {
                            // Removing Value from Record
                            Object uo = ( ( DefaultMutableTreeNode ) node.getParent () ).getUserObject ();
                            ( ( Record ) uo ).removeValue ( ( Value ) type );
                        }
                        else if ( type instanceof Text )
                        {
                            // Removing Text from Value
                            Object uo = ( ( DefaultMutableTreeNode ) node.getParent () ).getUserObject ();
                            ( ( Value ) uo ).removeText ( ( Text ) type );
                        }
                        else if ( type instanceof Tooltip )
                        {
                            // Removing Tooltip from Value
                            Object uo = ( ( DefaultMutableTreeNode ) node.getParent () ).getUserObject ();
                            ( ( Value ) uo ).removeTooltip ( ( Tooltip ) type );
                        }

                        // Removing node from tree model
                        model.removeNodeFromParent ( node );

                        // Restoring selection
                        if ( getRowCount () > row )
                        {
                            setSelectionRow ( row );
                        }
                        else if ( row > 0 )
                        {
                            setSelectionRow ( row - 1 );
                        }
                    }
                }
            }
        } );
    }

    public void selectAndShow ( DefaultMutableTreeNode node )
    {
        TreePath path = new TreePath ( node.getPath () );
        setSelectionPath ( path );
        scrollPathToVisible ( path );
    }

    public DefaultMutableTreeNode getSelectedNode ()
    {
        TreePath path = getSelectionPath ();
        if ( path != null )
        {
            return ( DefaultMutableTreeNode ) path.getLastPathComponent ();
        }
        else
        {
            return null;
        }
    }

    public Object getSelectedValue ()
    {
        TreePath path = getSelectionPath ();
        if ( path != null )
        {
            DefaultMutableTreeNode node = ( DefaultMutableTreeNode ) path.getLastPathComponent ();
            return node.getUserObject ();
        }
        else
        {
            return null;
        }
    }

    public void expandTillRecords ()
    {
        int i = 0;
        while ( i < getRowCount () )
        {
            Object object = getPathForRow ( i ).getLastPathComponent ();
            Object uo = ( ( DefaultMutableTreeNode ) object ).getUserObject ();
            if ( uo == null || uo instanceof Dictionary )
            {
                expandRow ( i );
            }
            i++;
        }
    }

    public void loadDictionary ( Dictionary dictionary )
    {
        DefaultMutableTreeNode dn = createDictionaryNode ( dictionary );
        model.insertNodeInto ( dn, root, root.getChildCount () );
    }

    public DefaultMutableTreeNode createDictionaryNode ( Dictionary dictionary )
    {
        DefaultMutableTreeNode dn = new DefaultMutableTreeNode ( dictionary );
        if ( dictionary.getRecords () != null )
        {
            for ( Record record : dictionary.getRecords () )
            {
                dn.add ( createRecordNode ( record ) );
            }
        }
        if ( dictionary.getSubdictionaries () != null )
        {
            for ( Dictionary sub : dictionary.getSubdictionaries () )
            {
                dn.add ( createDictionaryNode ( sub ) );
            }
        }
        return dn;
    }

    public DefaultMutableTreeNode createRecordNode ( Record record )
    {
        DefaultMutableTreeNode rn = new DefaultMutableTreeNode ( record );
        if ( record.getValues () != null )
        {
            for ( Value value : record.getValues () )
            {
                rn.add ( createValueNode ( value ) );
            }
        }
        return rn;
    }

    public DefaultMutableTreeNode createValueNode ( Value value )
    {
        DefaultMutableTreeNode vn = new DefaultMutableTreeNode ( value );
        if ( value.getTexts () != null )
        {
            for ( Text text : value.getTexts () )
            {
                vn.add ( createTextNode ( text ) );
            }
        }
        if ( value.getTooltips () != null )
        {
            for ( Tooltip tooltip : value.getTooltips () )
            {
                vn.add ( createTooltipNode ( tooltip ) );
            }
        }
        return vn;
    }

    public DefaultMutableTreeNode createTextNode ( Text text )
    {
        return new DefaultMutableTreeNode ( text );
    }

    public DefaultMutableTreeNode createTooltipNode ( Tooltip tooltip )
    {
        return new DefaultMutableTreeNode ( tooltip );
    }

    public DefaultTreeModel getActualModel ()
    {
        return model;
    }
}