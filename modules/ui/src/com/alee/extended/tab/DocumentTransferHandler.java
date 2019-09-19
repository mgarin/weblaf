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

package com.alee.extended.tab;

import com.alee.laf.tabbedpane.WebTabbedPane;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.SwingUtils;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static com.alee.extended.tab.DocumentTransferable.dataFlavor;
import static com.alee.extended.tab.DocumentTransferable.transferFlavor;

/**
 * {@link TransferHandler} implementation for {@link DocumentData}.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDocumentPane">How to use WebDocumentPane</a>
 * @see DocumentTransferable
 * @see WebDocumentPane
 */
public class DocumentTransferHandler extends TransferHandler
{
    /**
     * Distance from pane side within which drop is determined as a split action.
     */
    protected static final int dropSize = 40;

    /**
     * PaneData to which this TransferHandler is attached.
     */
    protected final PaneData paneData;

    /**
     * WebTabbedPane from PaneData.
     */
    protected final WebTabbedPane tabbedPane;

    /**
     * Dragged away document index.
     * Used to restore document position if drag is cancelled for some reason.
     */
    protected int documentIndex = -1;

    /**
     * Dragged document.
     */
    protected DocumentData document = null;

    /**
     * Constructs new DocumentDragHandler for the specified PaneData.
     *
     * @param paneData PaneData
     */
    public DocumentTransferHandler ( final PaneData paneData )
    {
        super ();
        this.paneData = paneData;
        this.tabbedPane = paneData.getTabbedPane ();
    }

    @Override
    public int getSourceActions ( final JComponent c )
    {
        return MOVE;
    }

    @Override
    public void exportAsDrag ( final JComponent comp, final InputEvent e, final int action )
    {
        if ( e instanceof MouseEvent && action == MOVE )
        {
            // Find dragged document
            final MouseEvent me = ( MouseEvent ) e;
            final int index = tabbedPane.indexAtLocation ( me.getX (), me.getY () );
            if ( index != -1 )
            {
                // Check whether it can be dragged
                final DocumentData documentData = paneData.get ( index );
                if ( documentData.isDraggable () )
                {
                    // Perform export
                    documentIndex = index;
                    document = documentData;
                    super.exportAsDrag ( comp, e, action );
                    paneData.getDocumentPane ().checkSelection ();
                }
            }
        }
    }

    @Override
    protected Transferable createTransferable ( final JComponent c )
    {
        if ( document != null )
        {
            paneData.remove ( document );
            final WebDocumentPane webDocumentPane = paneData.getDocumentPane ();
            final DocumentPaneTransferInfo transferInfo = new DocumentPaneTransferInfo ( webDocumentPane );
            return new DocumentTransferable ( document, transferInfo );
        }
        else
        {
            return null;
        }
    }

    @Override
    protected void exportDone ( final JComponent source, final Transferable data, final int action )
    {
        // Checking whether new location for the document was found or not
        final WebDocumentPane documentPane = paneData.getDocumentPane ();
        if ( action == NONE && documentPane.getDocument ( document.getId () ) == null )
        {
            // We cannot drag to other document pane and yet document is not in this pane
            // That means that it was dragged out somewhere and didn't find a new place
            // In this case we simply restore the document into this pane
            paneData.add ( document, documentIndex );
            paneData.setSelected ( document );
        }

        // Merging this pane if it has not more documents after drag
        if ( paneData.count () == 0 )
        {
            paneData.getDocumentPane ().merge ( paneData );
        }

        // Clearing dragged document
        documentIndex = -1;
        document = null;
    }

    @Override
    public boolean canImport ( final TransferSupport support )
    {
        try
        {
            final Transferable t = support.getTransferable ();
            if ( t.isDataFlavorSupported ( dataFlavor ) )
            {
                final Object transferData = t.getTransferData ( transferFlavor );
                if ( transferData instanceof DocumentPaneTransferInfo )
                {
                    final DocumentPaneTransferInfo info = ( DocumentPaneTransferInfo ) transferData;
                    final WebDocumentPane dp = paneData.getDocumentPane ();
                    final boolean allowed = dp.isDragBetweenPanesEnabled () && info.isDragBetweenPanesEnabled ();
                    return allowed || dp.getId ().equals ( info.getDocumentPaneId () );
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
            LoggerFactory.getLogger ( DocumentTransferHandler.class ).error ( e.toString (), e );
            return false;
        }
    }

    @Override
    public boolean importData ( final TransferSupport support )
    {
        try
        {
            // Retrieving transferred data
            final Object data = support.getTransferable ().getTransferData ( dataFlavor );
            final DocumentData document = ( DocumentData ) data;

            // We need to check where exactly document was dropped
            // Here are the options what might happen:
            // 1. Dropped in the middle of tabbed pane - simply add document to end
            // 2. Dropped onto tabs - add it between existing documents
            // 3. Dropped on side (N px from the side) - split this pane
            final Point dp = support.getDropLocation ().getDropPoint ();
            final WebTabbedPane pane = ( WebTabbedPane ) support.getComponent ();

            final int index = pane.getTabAt ( dp );
            if ( index != -1 )
            {
                // Dropped on one of tabs, inserting
                final Rectangle tb = pane.getBoundsAt ( index );
                final int di = dp.x < tb.x + tb.width / 2 ? index : index + 1;
                paneData.add ( document, di );
                paneData.setSelected ( document );
                checkFocusState ( paneData );
            }
            else
            {
                int dropSide = -1;
                if ( paneData.getDocumentPane ().isSplitEnabled () && pane.getTabCount () > 0 )
                {
                    final int tabPlacement = pane.getTabPlacement ();
                    final int w = pane.getWidth ();
                    final int h = pane.getHeight ();
                    if ( tabPlacement != SwingConstants.TOP && new Rectangle ( 0, 0, w, dropSize ).contains ( dp ) )
                    {
                        dropSide = SwingConstants.TOP;
                    }
                    else if ( tabPlacement != SwingConstants.BOTTOM && new Rectangle ( 0, h - dropSize, w, dropSize ).contains ( dp ) )
                    {
                        dropSide = SwingConstants.BOTTOM;
                    }
                    else if ( tabPlacement != SwingConstants.LEFT && new Rectangle ( 0, 0, dropSize, h ).contains ( dp ) )
                    {
                        dropSide = SwingConstants.LEFT;
                    }
                    else if ( tabPlacement != SwingConstants.RIGHT && new Rectangle ( w - dropSize, 0, dropSize, h ).contains ( dp ) )
                    {
                        dropSide = SwingConstants.RIGHT;
                    }
                }
                if ( dropSide != -1 )
                {
                    // Dropped on the side of tabbed pane, splitting
                    final PaneData otherPane = paneData.getDocumentPane ().split ( paneData, document, dropSide );
                    otherPane.setSelected ( document );
                    checkFocusState ( otherPane );
                }
                else
                {
                    // Dropped somewhere in the middle of tabbed pane, adding
                    paneData.add ( document );
                    paneData.setSelected ( document );
                    checkFocusState ( paneData );
                }
            }
            return true;
        }
        catch ( final Exception e )
        {
            LoggerFactory.getLogger ( DocumentTransferHandler.class ).error ( e.toString (), e );
            return false;
        }
    }

    /**
     * Special method to transfer focus properly after drag finishes.
     *
     * @param paneData pane to transfer focus into
     */
    protected void checkFocusState ( final PaneData paneData )
    {
        // We have to perform focus transfer AFTER the drag ends, otherwise focus may be lost again
        // That happens due to splits/tabbed panes removal in the process
        CoreSwingUtils.invokeLater ( new Runnable ()
        {
            @Override
            public void run ()
            {
                final WebTabbedPane pane = paneData.getTabbedPane ();
                if ( !SwingUtils.hasFocusOwner ( pane ) )
                {
                    final Component c = pane.getSelectedComponent ();
                    if ( c.isFocusable () || c instanceof Container && SwingUtils.hasFocusableComponent ( ( Container ) c ) )
                    {
                        pane.transferFocus ();
                    }
                    else
                    {
                        pane.requestFocusInWindow ();
                    }
                }
            }
        } );
    }

    /**
     * Quick drag handler installation method.
     *
     * @param paneData pane into which this drag handler should be installed
     */
    public static void install ( final PaneData paneData )
    {
        final WebTabbedPane tabbedPane = paneData.getTabbedPane ();
        final MouseAdapter dragAdapter = new MouseAdapter ()
        {
            protected boolean readyToDrag = false;
            protected Point startingPoint = null;

            @Override
            public void mousePressed ( final MouseEvent e )
            {
                if ( paneData.getDocumentPane ().isDragEnabled () && SwingUtils.isLeftMouseButton ( e ) )
                {
                    readyToDrag = true;
                    startingPoint = e.getPoint ();
                }
            }

            @Override
            public void mouseDragged ( final MouseEvent e )
            {
                if ( readyToDrag )
                {
                    // Ensure that cursor have moved for at least 5 pixels before we start drag operation
                    // This is necessary to avoid accidental tabs drag when they are selected and to make them feel sticky
                    final Point point = e.getPoint ();
                    if ( Math.abs ( startingPoint.x - point.x ) > 5 || Math.abs ( startingPoint.y - point.y ) > 5 )
                    {
                        readyToDrag = false;
                        startingPoint = null;
                        tabbedPane.getTransferHandler ().exportAsDrag ( tabbedPane, e, TransferHandler.MOVE );
                    }
                }
            }

            @Override
            public void mouseReleased ( final MouseEvent e )
            {
                if ( SwingUtils.isLeftMouseButton ( e ) )
                {
                    readyToDrag = false;
                }
            }
        };
        tabbedPane.addMouseListener ( dragAdapter );
        tabbedPane.addMouseMotionListener ( dragAdapter );
        tabbedPane.setTransferHandler ( new DocumentTransferHandler ( paneData ) );
    }
}