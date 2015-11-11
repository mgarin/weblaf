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

package com.alee.utils.filefilter;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * @author Mikle Garin
 */

public class SwingFileFilterAdapter extends AbstractFileFilterAdapter
{
    private final FileFilter fileFilter;

    public SwingFileFilterAdapter ( final FileFilter fileFilter, final ImageIcon icon, final String description )
    {
        super ( icon, description );
        this.fileFilter = fileFilter;
    }

    public FileFilter getFileFilter ()
    {
        return fileFilter;
    }

    @Override
    public boolean accept ( final File file )
    {
        return fileFilter == null || fileFilter.accept ( file );
    }
}