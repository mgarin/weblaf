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

package com.alee.utils.zip;

import java.io.File;
import java.util.EventListener;
import java.util.zip.ZipEntry;

/**
 * The listener interface for receiving unzip operation events.
 *
 * @author Mikle Garin
 */
public interface UnzipListener extends EventListener
{
    /**
     * Notifies about zip file entries amount.
     *
     * @param size entries amount
     */
    public void sizeDetermined ( int size );

    /**
     * Notifies about new unzipped file.
     *
     * @param entry file zip entry
     * @param file  unzipped file
     * @param index file index
     */
    public void fileUnzipped ( ZipEntry entry, File file, int index );
}