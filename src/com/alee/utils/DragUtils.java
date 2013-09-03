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
    private static final String URI_LIST_MIME_TYPE = "text/uri-list;class=java.lang.String";

    /**
     * URI list data flavor.
     */
    private static DataFlavor uriListFlavor = null;

    /**
     * Returns improrted image retrieved from the specified transferable.
     *
     * @param t transferable
     * @return improrted image
     */
    public static Image getImportedImage ( Transferable t )
    {
        if ( t.isDataFlavorSupported ( DataFlavor.imageFlavor ) )
        {
            try
            {
                Object data = t.getTransferData ( DataFlavor.imageFlavor );
                if ( data instanceof Image )
                {
                    return ( Image ) data;
                }
            }
            catch ( UnsupportedFlavorException e )
            {
                //
            }
            catch ( IOException e )
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
    public static List<File> getImportedFiles ( Transferable t )
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
        catch ( Throwable e )
        {
            //
        }

        // From URL
        try
        {
            if ( hasURIListFlavor ( t.getTransferDataFlavors () ) )
            {
                // File link
                String url = ( String ) t.getTransferData ( getUriListDataFlavor () );
                final File file = new File ( new URL ( url ).getPath () );

                // Returning file
                return Arrays.asList ( file );
            }
        }
        catch ( Throwable e )
        {
            //
        }

        // From files list (Windows)
        try
        {
            // Getting files list
            return ( List<File> ) t.getTransferData ( DataFlavor.javaFileListFlavor );
        }
        catch ( Throwable e )
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
    public static List<File> textURIListToFileList ( String data )
    {
        List<File> list = new ArrayList<File> ( 1 );
        for ( StringTokenizer st = new StringTokenizer ( data, "\r\n" ); st.hasMoreTokens (); )
        {
            String s = st.nextToken ();
            if ( s.startsWith ( "#" ) )
            {
                // the line is a comment (as per the RFC 2483)
                continue;
            }
            try
            {
                list.add ( new File ( new URI ( s ) ) );
            }
            catch ( Throwable e )
            {
                //
            }
        }
        return list;
    }

    /**
     * Returns whether flavors array has URI list flavor or not.
     *
     * @param flavors flavors array
     * @return true if flavors array has URI list flavor, false otherwise
     */
    public static boolean hasURIListFlavor ( DataFlavor[] flavors )
    {
        for ( DataFlavor flavor : flavors )
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
                return uriListFlavor = new DataFlavor ( URI_LIST_MIME_TYPE );
            }
            catch ( Throwable e )
            {
                return null;
            }
        }
        else
        {
            return uriListFlavor;
        }
    }
}