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

package com.alee.utils;

import com.alee.managers.log.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * This class provides a set of utilities to simplify work with swing drag and drop.
 *
 * @author Mikle Garin
 */

public final class DragUtils
{
    /**
     * URI list mime type.
     */
    public static final String URI_LIST_MIME_TYPE = "text/uri-list;class=java.lang.String";

    /**
     * URI string data separator.
     */
    public static final String uriListSeparator = "\r\n";

    /**
     * URI list data flavor.
     */
    private static DataFlavor uriListFlavor = null;

    /**
     * Returns imported image retrieved from the specified transferable.
     *
     * @param t transferable
     * @return imported image
     */
    public static Image getImportedImage ( final Transferable t )
    {
        if ( t.isDataFlavorSupported ( DataFlavor.imageFlavor ) )
        {
            try
            {
                final Object data = t.getTransferData ( DataFlavor.imageFlavor );
                if ( data instanceof Image )
                {
                    return ( Image ) data;
                }
            }
            catch ( final UnsupportedFlavorException e )
            {
                //
            }
            catch ( final IOException e )
            {
                //
            }
        }
        return null;
    }

    /**
     * Returns list of imported files retrieved from the specified transferable.
     *
     * @param t transferable
     * @return list of imported files
     */
    public static List<File> getImportedFiles ( final Transferable t )
    {
        // From files list (Linux/MacOS)
        try
        {
            if ( hasURIListFlavor ( t.getTransferDataFlavors () ) )
            {
                // Parsing incoming files
                return textURIListToFileList ( ( String ) t.getTransferData ( getUriListDataFlavor () ) );
            }
        }
        catch ( final Throwable e )
        {
            //
        }

        // From URL
        try
        {
            if ( hasURIListFlavor ( t.getTransferDataFlavors () ) )
            {
                // File link
                final String url = ( String ) t.getTransferData ( getUriListDataFlavor () );
                final File file = new File ( new URL ( url ).getPath () );

                // Returning file
                return Arrays.asList ( file );
            }
        }
        catch ( final Throwable e )
        {
            //
        }

        // From files list (Windows)
        try
        {
            // Getting files list
            return ( List<File> ) t.getTransferData ( DataFlavor.javaFileListFlavor );
        }
        catch ( final Throwable e )
        {
            //
        }

        return null;
    }

    /**
     * Returns list of files from the specified text URI list.
     *
     * @param data text list of URI
     * @return list of files
     */
    public static List<File> textURIListToFileList ( final String data )
    {
        final List<File> list = new ArrayList<File> ( 1 );
        for ( final StringTokenizer st = new StringTokenizer ( data, uriListSeparator ); st.hasMoreTokens (); )
        {
            final String s = st.nextToken ();
            if ( s.startsWith ( "#" ) )
            {
                // the line is a comment (as per the RFC 2483)
                continue;
            }
            try
            {
                list.add ( new File ( new URI ( s ) ) );
            }
            catch ( final Throwable e )
            {
                //
            }
        }
        return list;
    }

    /**
     * Returns text URI list for the specified list of files.
     *
     * @param files list of files to convert
     * @return text URI list
     */
    public static String fileListToTextURIList ( final List<File> files )
    {
        final StringBuilder sb = new StringBuilder ();
        for ( final File file : files )
        {
            sb.append ( file.toURI ().toASCIIString () );
            sb.append ( uriListSeparator );
        }
        return sb.toString ();
    }

    /**
     * Returns whether flavors array has URI list flavor or not.
     *
     * @param flavors flavors array
     * @return true if flavors array has URI list flavor, false otherwise
     */
    public static boolean hasURIListFlavor ( final DataFlavor[] flavors )
    {
        for ( final DataFlavor flavor : flavors )
        {
            if ( getUriListDataFlavor ().equals ( flavor ) )
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns URI list data flavor.
     *
     * @return URI list data flavor
     */
    public static DataFlavor getUriListDataFlavor ()
    {
        if ( uriListFlavor == null )
        {
            try
            {
                uriListFlavor = new DataFlavor ( URI_LIST_MIME_TYPE );
            }
            catch ( final Throwable e )
            {
                Log.error ( DragUtils.class, e );
            }
        }
        return uriListFlavor;
    }

    /**
     * Returns whether can pass drop action to closest component parent that has its own TransferHandler.
     * This might be used to make some components that has drag handler transparent for drop actions.
     *
     * @param info transfer support
     * @return true if drop action succeed, false otherwise
     */
    public static boolean canPassDrop ( final TransferHandler.TransferSupport info )
    {
        return canPassDrop ( info.getComponent (), info );
    }

    /**
     * Returns whether can pass drop action to closest component parent that has its own TransferHandler.
     * This might be used to make some components that has drag handler transparent for drop actions.
     *
     * @param component component to pass drop action from
     * @param info      transfer support
     * @return true if drop action succeed, false otherwise
     */
    public static boolean canPassDrop ( final Component component, final TransferHandler.TransferSupport info )
    {
        final Container parent = component.getParent ();
        if ( parent != null && parent instanceof JComponent )
        {
            final TransferHandler th = ( ( JComponent ) parent ).getTransferHandler ();
            return th != null ? th.canImport ( info ) : canPassDrop ( parent, info );
        }
        return false;
    }

    /**
     * Passes drop action to closest component parent that has its own TransferHandler.
     * This might be used to make some components that has drag handler transparent for drop actions.
     *
     * @param info transfer support
     * @return true if drop action succeed, false otherwise
     */
    public static boolean passDropAction ( final TransferHandler.TransferSupport info )
    {
        return passDropAction ( info.getComponent (), info );
    }

    /**
     * Passes drop action to closest component parent that has its own TransferHandler.
     * This might be used to make some components that has drag handler transparent for drop actions.
     *
     * @param component component to pass drop action from
     * @param info      transfer support
     * @return true if drop action succeed, false otherwise
     */
    public static boolean passDropAction ( final Component component, final TransferHandler.TransferSupport info )
    {
        final Container parent = component.getParent ();
        if ( parent != null && parent instanceof JComponent )
        {
            final TransferHandler th = ( ( JComponent ) parent ).getTransferHandler ();
            return th != null ? th.importData ( info ) : passDropAction ( parent, info );
        }
        return false;
    }
}