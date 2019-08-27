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

import com.alee.api.annotations.NotNull;
import com.alee.managers.language.data.Dictionary;
import com.alee.managers.language.data.Record;
import com.alee.managers.language.data.Text;
import com.alee.managers.language.data.Value;
import com.alee.utils.SwingUtils;
import com.alee.utils.XmlUtils;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Mikle Garin
 */
public class DictionariesTransferHandler extends TransferHandler
{
    private final DataFlavor[] flavors = new DataFlavor[]{ DataFlavor.stringFlavor };

    private final DictionariesTree tree;

    public static void install ( final DictionariesTree tree )
    {
        tree.setTransferHandler ( new DictionariesTransferHandler ( tree ) );
    }

    public DictionariesTransferHandler ( final DictionariesTree tree )
    {
        super ();
        this.tree = tree;

        tree.addMouseMotionListener ( new MouseAdapter ()
        {
            @Override
            public void mouseDragged ( final MouseEvent e )
            {
                if ( SwingUtilities.isLeftMouseButton ( e ) )
                {
                    exportAsDrag ( DictionariesTransferHandler.this.tree, e,
                            SwingUtils.isCtrl ( e ) ? TransferHandler.COPY : TransferHandler.MOVE );
                }
            }
        } );
    }

    public DictionariesTree getTree ()
    {
        return tree;
    }

    @Override
    public int getSourceActions ( final JComponent c )
    {
        return TransferHandler.COPY_OR_MOVE;
    }

    @Override
    protected Transferable createTransferable ( final JComponent c )
    {
        final Transferable transferable;
        final Object object = tree.getSelectedValue ();
        if ( object != null )
        {
            final String xml = XmlUtils.toXML ( object );
            transferable = new Transferable ()
            {

                @Override
                public DataFlavor[] getTransferDataFlavors ()
                {
                    return flavors;
                }

                @Override
                public boolean isDataFlavorSupported ( final DataFlavor flavor )
                {
                    return flavor.equals ( DataFlavor.stringFlavor );
                }

                @NotNull
                @Override
                public Object getTransferData ( final DataFlavor flavor ) throws UnsupportedFlavorException
                {
                    if ( isDataFlavorSupported ( flavor ) )
                    {
                        return xml;
                    }
                    else
                    {
                        throw new UnsupportedFlavorException ( flavor );
                    }
                }
            };
        }
        else
        {
            transferable = null;
        }
        return transferable;
    }

    @Override
    public boolean canImport ( final TransferHandler.TransferSupport info )
    {
        try
        {
            // Calculating drop point
            final Point dp = info.getDropLocation ().getDropPoint ();
            final TreePath dropPath = tree.getPathForLocation ( dp.x, dp.y );
            final DefaultMutableTreeNode dropLocation = ( DefaultMutableTreeNode ) dropPath.getLastPathComponent ();
            final Object dlo = dropLocation.getUserObject ();

            // Dropped object
            final String xml = ( String ) info.getTransferable ().getTransferData ( DataFlavor.stringFlavor );
            final Object droppedObject = XmlUtils.fromXML ( xml );

            // Checking drop types
            if ( dlo == null && droppedObject instanceof Dictionary )
            {
                // Drop Dictionary into root
                return true;
            }
            if ( dlo instanceof Dictionary && ( droppedObject instanceof Dictionary || droppedObject instanceof Record ) )
            {
                // Drop Dictionary or Record into Dictionary
                return true;
            }
            else if ( dlo instanceof Record && droppedObject instanceof Value )
            {
                // Drop Value into Record
                return true;
            }
            else if ( dlo instanceof Value && droppedObject instanceof Text )
            {
                // Drop Text or Tooltip into Value
                return true;
            }
            return false;
        }
        catch ( final Exception e )
        {
            return false;
        }
    }

    @Override
    public boolean importData ( final TransferHandler.TransferSupport info )
    {
        try
        {
            // Calculating drop point
            final Point dp = info.getDropLocation ().getDropPoint ();
            final TreePath dropPath = tree.getPathForLocation ( dp.x, dp.y );
            final DefaultMutableTreeNode dropLocation = ( DefaultMutableTreeNode ) dropPath.getLastPathComponent ();
            final Object dlo = dropLocation.getUserObject ();

            // Dropped object
            final String xml = ( String ) info.getTransferable ().getTransferData ( DataFlavor.stringFlavor );
            final Object droppedObject = XmlUtils.fromXML ( xml );

            // Checking drop types
            if ( dlo == null )
            {
                // Drop Dictionary into root
                if ( droppedObject instanceof Dictionary )
                {
                    final Dictionary dictionary = ( Dictionary ) droppedObject;
                    final DefaultMutableTreeNode dn = tree.createDictionaryNode ( dictionary );
                    tree.getActualModel ().insertNodeInto ( dn, dropLocation, dropLocation.getChildCount () );
                    tree.selectAndShow ( dn );
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else if ( dlo instanceof Dictionary )
            {
                // Drop Dictionary or Record into Dictionary
                final Dictionary dropTo = ( Dictionary ) dlo;
                if ( droppedObject instanceof Dictionary )
                {
                    final Dictionary dictionary = ( Dictionary ) droppedObject;
                    dropTo.addDictionary ( dictionary );
                    final DefaultMutableTreeNode dn = tree.createDictionaryNode ( dictionary );
                    tree.getActualModel ().insertNodeInto ( dn, dropLocation, dropLocation.getChildCount () );
                    tree.selectAndShow ( dn );
                    return true;
                }
                else if ( droppedObject instanceof Record )
                {
                    final Record record = ( Record ) droppedObject;
                    dropTo.addRecord ( record );
                    final DefaultMutableTreeNode rn = tree.createRecordNode ( record );
                    tree.getActualModel ().insertNodeInto ( rn, dropLocation, dropLocation.getChildCount () );
                    tree.selectAndShow ( rn );
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else if ( dlo instanceof Record )
            {
                // Drop Value into Record
                if ( droppedObject instanceof Value )
                {
                    final Value value = ( Value ) droppedObject;
                    ( ( Record ) dlo ).addValue ( value );
                    final DefaultMutableTreeNode vn = tree.createValueNode ( value );
                    tree.getActualModel ().insertNodeInto ( vn, dropLocation, dropLocation.getChildCount () );
                    tree.selectAndShow ( vn );
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else if ( dlo instanceof Value )
            {
                // Drop Text or Tooltip into Value
                final Value dropTo = ( Value ) dlo;
                if ( droppedObject instanceof Text )
                {
                    final Text text = ( Text ) droppedObject;
                    dropTo.addText ( text );
                    final DefaultMutableTreeNode tn = tree.createTextNode ( text );
                    tree.getActualModel ().insertNodeInto ( tn, dropLocation, dropLocation.getChildCount () );
                    tree.selectAndShow ( tn );
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
        catch ( final Exception e )
        {
            return false;
        }
    }
}