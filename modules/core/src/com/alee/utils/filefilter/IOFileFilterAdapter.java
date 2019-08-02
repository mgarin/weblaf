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
import java.io.File;
import java.io.FileFilter;

/**
 * {@link DescriptiveFileFilter} adapter for {@link java.io.FileFilter}.
 *
 * @author Mikle Garin
 */
public class IOFileFilterAdapter extends DescriptiveFileFilter
{
    /**
     * Adapted filter.
     */
    private final FileFilter fileFilter;

    /**
     * Constructs new {@link java.io.FileFilter} adapter class.
     *
     * @param fileFilter  adapted filter
     * @param icon        filter icon
     * @param description filter description
     */
    public IOFileFilterAdapter ( final FileFilter fileFilter, final ImageIcon icon, final String description )
    {
        super ( icon, description );
        this.fileFilter = fileFilter;
    }

    /**
     * Returns adapted filter.
     *
     * @return adapted filter
     */
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