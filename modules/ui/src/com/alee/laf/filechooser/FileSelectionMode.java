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

package com.alee.laf.filechooser;

import com.alee.utils.FileUtils;

import javax.swing.*;
import java.io.File;

/**
 * @author Mikle Garin
 */

public enum FileSelectionMode
{
    /**
     * Files only selection mode.
     */
    filesOnly ( JFileChooser.FILES_ONLY ),

    /**
     * Directories only selection mode.
     */
    directoriesOnly ( JFileChooser.DIRECTORIES_ONLY ),

    /**
     * Files and directories selection mode.
     */
    filesAndDirectories ( JFileChooser.FILES_AND_DIRECTORIES );

    /**
     * Selection mode ID in {@link javax.swing.JFileChooser}
     */
    private final int modeId;

    /**
     * Constructs new selection mode enum for the specified mode ID.
     *
     * @param modeId selection mode ID
     */
    private FileSelectionMode ( final int modeId )
    {
        this.modeId = modeId;
    }

    /**
     * Returns selection mode ID.
     *
     * @return selection mode ID
     */
    public int getModeId ()
    {
        return modeId;
    }

    /**
     * Returns whether or not this mode accepts specified file.
     *
     * @param file {@link File} to check
     * @return {@code true} if this mode accepts specified file, {@code false} otherwise
     */
    public boolean accept ( final File file )
    {
        switch ( this )
        {
            case filesOnly:
            {
                return !file.exists () || FileUtils.isFile ( file );
            }
            case directoriesOnly:
            {
                return !file.exists () || FileUtils.isDirectory ( file );
            }
            default:
            {
                return true;
            }
        }
    }

    /**
     * Returns selection mode for the specified selection mode ID.
     *
     * @param modeId selection mode ID
     * @return selection mode for the specified selection mode ID
     */
    public static FileSelectionMode get ( final int modeId )
    {
        for ( final FileSelectionMode mode : FileSelectionMode.values () )
        {
            if ( mode.getModeId () == modeId )
            {
                return mode;
            }
        }
        return null;
    }
}