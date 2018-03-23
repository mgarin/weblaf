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
import com.alee.managers.language.data.Dictionary;
import com.alee.managers.language.data.Record;
import com.alee.managers.language.data.Text;
import com.alee.managers.language.data.Value;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @author Mikle Garin
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
            @Override
            public void treeExpanded ( final TreeExpansionEvent event )
            {
                final Object object = event.getPath ().getLastPathComponent ();
                final DefaultMutableTreeNode node = ( DefaultMutableTreeNode ) object;
                final Object uo = node.getUserObject ();
                if ( uo instanceof Record )
                {
                    for ( int i = 0; i < node.getChildCount (); i++ )
                    {
                        final DefaultMutableTreeNode child = ( DefaultMutableTreeNode ) node.getChildAt ( i );
                        expandPath ( new TreePath ( child.getPath () ) );
                    }
                }
            }

            @Override
            public void treeCollapsed ( final TreeExpansionEvent event )
            {
                //
            }
        } );

        // Hotkeys listener
        addKeyListener ( new KeyAdapter ()
        {
            @Override
            public void keyPressed ( final KeyEvent e )
            {
                if ( Hotkey.DELETE.isTriggered ( e ) )
                {
                    final DefaultMutableTreeNode node = getSelectedNode ();
                    if ( node != null && node.getUserObject () != null )
                    {
                        // Saving selection row
                        final int row = getRowForPath ( getSelectionPath () );

                        // Removing actual data
                        final Object type = node.getUserObject ();
                        if ( type instanceof Dictionary )
                        {
                            // Removing Dictionary from subdictionaries list
                            final Object uo = ( ( DefaultMutableTreeNode ) node.getParent () ).getUserObject ();
                            if ( uo != null )
                            {
                                final Dictionary parent = ( Dictionary ) uo;
                                parent.removeDictionary ( ( Dictionary ) type );
                            }
                        }
                        else if ( type instanceof Record )
                        {
                            // Removing Record from Dictionary
                            final Object uo = ( ( DefaultMutableTreeNode ) node.getParent () ).getUserObject ();
                            ( ( Dictionary ) uo ).removeRecord ( ( Record ) type );
                        }
                        else if ( type instanceof Value )
                        {
                            // Removing Value from Record
                            final Object uo = ( ( DefaultMutableTreeNode ) node.getParent () ).getUserObject ();
                            ( ( Record ) uo ).removeValue ( ( Value ) type );
                        }
                        else if ( type instanceof Text )
                        {
                            // Removing Text from Value
                            final Object uo = ( ( DefaultMutableTreeNode ) node.getParent () ).getUserObject ();
                            ( ( Value ) uo ).removeText ( ( Text ) type );
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

    public void selectAndShow ( final DefaultMutableTreeNode node )
    {
        final TreePath path = new TreePath ( node.getPath () );
        setSelectionPath ( path );
        scrollPathToVisible ( path );
    }

    @Override
    public DefaultMutableTreeNode getSelectedNode ()
    {
        final TreePath path = getSelectionPath ();
        return path != null ? ( DefaultMutableTreeNode ) path.getLastPathComponent () : null;
    }

    public Object getSelectedValue ()
    {
        final TreePath path = getSelectionPath ();
        if ( path != null )
        {
            final DefaultMutableTreeNode node = ( DefaultMutableTreeNode ) path.getLastPathComponent ();
            return node.getUserObject ();
        }
        return null;
    }

    public void expandTillRecords ()
    {
        int i = 0;
        while ( i < getRowCount () )
        {
            final Object object = getPathForRow ( i ).getLastPathComponent ();
            final Object uo = ( ( DefaultMutableTreeNode ) object ).getUserObject ();
            if ( uo == null || uo instanceof Dictionary )
            {
                expandRow ( i );
            }
            i++;
        }
    }

    public void loadDictionary ( final Dictionary dictionary )
    {
        final DefaultMutableTreeNode dn = createDictionaryNode ( dictionary );
        model.insertNodeInto ( dn, root, root.getChildCount () );
    }

    public DefaultMutableTreeNode createDictionaryNode ( final Dictionary dictionary )
    {
        final DefaultMutableTreeNode dn = new DefaultMutableTreeNode ( dictionary );
        for ( final Record record : dictionary.getRecords () )
        {
            dn.add ( createRecordNode ( record ) );
        }
        for ( final Dictionary sub : dictionary.getDictionaries () )
        {
            dn.add ( createDictionaryNode ( sub ) );
        }
        return dn;
    }

    public DefaultMutableTreeNode createRecordNode ( final Record record )
    {
        final DefaultMutableTreeNode rn = new DefaultMutableTreeNode ( record );
        if ( record.getValues () != null )
        {
            for ( final Value value : record.getValues () )
            {
                rn.add ( createValueNode ( value ) );
            }
        }
        return rn;
    }

    public DefaultMutableTreeNode createValueNode ( final Value value )
    {
        final DefaultMutableTreeNode vn = new DefaultMutableTreeNode ( value );
        if ( value.getTexts () != null )
        {
            for ( final Text text : value.getTexts () )
            {
                vn.add ( createTextNode ( text ) );
            }
        }
        return vn;
    }

    public DefaultMutableTreeNode createTextNode ( final Text text )
    {
        return new DefaultMutableTreeNode ( text );
    }

    public DefaultTreeModel getActualModel ()
    {
        return model;
    }
}