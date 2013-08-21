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

package com.alee.extended.image;

import com.alee.extended.drag.FileDropHandler;
import com.alee.utils.ImageUtils;

import javax.swing.*;
import java.io.File;
import java.util.List;

/**
 * User: mgarin Date: 31.10.11 Time: 13:43
 */

public class GalleryTransferHandler extends FileDropHandler
{
    private WebImageGallery gallery = null;

    public GalleryTransferHandler ( WebImageGallery gallery )
    {
        super ();
        this.gallery = gallery;
    }

    @Override
    protected boolean filesImported ( List<File> files )
    {
        boolean added = false;
        if ( gallery != null )
        {
            for ( File file : files )
            {
                if ( ImageUtils.isImageLoadable ( file.getName () ) )
                {
                    gallery.addImage ( new ImageIcon ( file.getAbsolutePath () ) );
                    added = true;
                }
            }
        }
        return added;
    }
}
