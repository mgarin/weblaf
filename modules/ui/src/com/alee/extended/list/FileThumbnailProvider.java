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

package com.alee.extended.list;

import com.alee.utils.compare.Filter;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileFilter;

/**
 * Base interface for custom thumbnail providers.
 *
 * @author Mikle Garin
 */

public interface FileThumbnailProvider extends Filter<File>, FileFilter
{
    /**
     * Returns whether or not this provider can generate thumbnail for the specified file.
     *
     * @param file file to provide thumbnail for
     * @return true if this provider can generate thumbnail for the specified file, false otherwise
     */
    @Override
    public boolean accept ( File file );

    /**
     * Returns custom file thumbnail icon.
     * Preferred size represents area available for the icon, though you are free to provide an icon with different size.
     * It will then be automatically rescaled down if too large or kept in the middle of the available area if it is too small.
     *
     * @param file    file to provide thumbnail for
     * @param size    preferred thumbnail size
     * @param preview whether thumbnail should be a small preview of file content or not
     * @return custom file thumbnail
     */
    public ImageIcon provide ( File file, Dimension size, boolean preview );
}