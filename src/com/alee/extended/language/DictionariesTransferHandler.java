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

import com.alee.managers.language.data.*;
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
import java.io.IOException;

/**
 * User: mgarin Date: 04.06.12 Time: 15:32
 */

public class DictionariesTransferHandler extends TransferHandler
{
    private final DataFlavor[] flavors = new DataFlavor[]{ DataFlavor.stringFlavor };

    private DictionariesTree tree;

    public static void install ( DictionariesTree tree )
    {
        tree.setTransferHandler ( new DictionariesTransferHandler ( tree ) );
    }

    public DictionariesTransferHandler ( DictionariesTree tree )
    {
        super ();
        this.tree = tree;

        tree.addMouseMotionListener ( new MouseAdapter ()
        {
            @Override
            public void mouseDragged ( MouseEvent e )
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
    public int getSourceActions ( JComponent c )
    {
        return TransferHandler.COPY_OR_MOVE;
    }

    @Override
    protected Transferable createTransferable ( JComponent c )
    {
        Object object = tree.getSelectedValue ();
        if ( object == null )
        {
            return null;
        }

        final String xml = XmlUtils.toXML ( object );
        return new Transferable ()
        {

            @Override
            public DataFlavor[] getTransferDataFlavors ()
            {
                return flavors;
            }

            @Override
            public boolean isDataFlavorSupported ( DataFlavor flavor )
            {
                return flavor.equals ( DataFlavor.stringFlavor );
            }

            @Override
            public Object getTransferData ( DataFlavor flavor ) throws UnsupportedFlavorException, IOException
            {
                if ( isDataFlavorSupported ( flavor ) )
                {
                    return xml;
                }
                else
                {
                    return null;
                }
            }
        };
    }

    @Override
    public boolean canImport ( TransferHandler.TransferSupport info )
    {
        try
        {
            // Calculating drop point
            Point dp = info.getDropLocation ().getDropPoint ();
            TreePath dropPath = tree.getPathForLocation ( dp.x, dp.y );
            DefaultMutableTreeNode dropLocation = ( DefaultMutableTreeNode ) dropPath.getLastPathComponent ();
            Object dlo = dropLocation.getUserObject ();

            // Dropped object
            String xml = ( String ) info.getTransferable ().getTransferData ( DataFlavor.stringFlavor );
            Object droppedObject = XmlUtils.fromXML ( xml );

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
            else if ( dlo instanceof Value && ( droppedObject instanceof Text || droppedObject instanceof Tooltip ) )
            {
                // Drop Text or Tooltip into Value
                return true;
            }
            return false;
        }
        catch ( Throwable e )
        {
            return false;
        }
    }

    @Override
    public boolean importData ( TransferHandler.TransferSupport info )
    {
        try
        {
            // Calculating drop point
            Point dp = info.getDropLocation ().getDropPoint ();
            TreePath dropPath = tree.getPathForLocation ( dp.x, dp.y );
            DefaultMutableTreeNode dropLocation = ( DefaultMutableTreeNode ) dropPath.getLastPathComponent ();
            Object dlo = dropLocation.getUserObject ();

            // Dropped object
            String xml = ( String ) info.getTransferable ().getTransferData ( DataFlavor.stringFlavor );
            Object droppedObject = XmlUtils.fromXML ( xml );

            // Checking drop types
            if ( dlo == null )
            {
                // Drop Dictionary into root
                if ( droppedObject instanceof Dictionary )
                {
                    Dictionary dictionary = ( Dictionary ) droppedObject;
                    dictionary.setId ();
                    DefaultMutableTreeNode dn = tree.createDictionaryNode ( dictionary );
                    tree.getActualModel ().insertNodeInto ( dn, dropLocation, dropLocation.getChildCount () );
                    tree.selectAndShow ( dn );
                    return true;
                }
                else
                {
                    return false;
                }
            }
            if ( dlo instanceof Dictionary )
            {
                // Drop Dictionary or Record into Dictionary
                Dictionary dropTo = ( Dictionary ) dlo;
                if ( droppedObject instanceof Dictionary )
                {
                    Dictionary dictionary = ( Dictionary ) droppedObject;
                    dictionary.setId ();
                    dropTo.addSubdictionary ( dictionary );
                    DefaultMutableTreeNode dn = tree.createDictionaryNode ( dictionary );
                    tree.getActualModel ().insertNodeInto ( dn, dropLocation, dropLocation.getChildCount () );
                    tree.selectAndShow ( dn );
                    return true;
                }
                else if ( droppedObject instanceof Record )
                {
                    Record record = ( Record ) droppedObject;
                    record = dropTo.addRecord ( record );
                    DefaultMutableTreeNode rn = tree.createRecordNode ( record );
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
                    Value value = ( Value ) droppedObject;
                    value = ( ( Record ) dlo ).addValue ( value );
                    DefaultMutableTreeNode vn = tree.createValueNode ( value );
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
                Value dropTo = ( Value ) dlo;
                if ( droppedObject instanceof Text )
                {
                    Text text = ( Text ) droppedObject;
                    text = dropTo.addText ( text );
                    DefaultMutableTreeNode tn = tree.createTextNode ( text );
                    tree.getActualModel ().insertNodeInto ( tn, dropLocation, dropLocation.getChildCount () );
                    tree.selectAndShow ( tn );
                    return true;
                }
                else if ( droppedObject instanceof Tooltip )
                {
                    Tooltip tooltip = ( Tooltip ) droppedObject;
                    tooltip = dropTo.addTooltip ( tooltip );
                    DefaultMutableTreeNode tn = tree.createTooltipNode ( tooltip );
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
        catch ( Throwable e )
        {
            return false;
        }
    }
}