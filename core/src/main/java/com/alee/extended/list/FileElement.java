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

import javax.swing.*;
import java.io.File;

/**
 * This class represents single file list element.
 *
 * @author Mikle Garin
 * @since 1.4
 */

public class FileElement
{
    /**
     * Whether thumbnail load is queued for this element or not.
     */
    private boolean thumbnailQueued = false;

    /**
     * Cached element thumbnail icon for enabled state.
     */
    private ImageIcon enabledThumbnail = null;

    /**
     * Cached element thumbnail icon for disabled state.
     */
    private ImageIcon disabledThumbnail = null;

    /**
     * Element file.
     */
    private File file;

    /**
     * Constructs element without file.
     */
    public FileElement ()
    {
        super ();
    }

    /**
     * Constructs element with specified file.
     */
    public FileElement ( File file )
    {
        super ();
        this.file = file;
    }

    /**
     * Returns whether thumbnail load is queued or not.
     *
     * @return true if thumbnail load is queued, false otherwise
     */
    public boolean isThumbnailQueued ()
    {
        return thumbnailQueued;
    }

    /**
     * Sets whether thumbnail load is queued or not.
     *
     * @param thumbnailQueued whether thumbnail load is queued or not
     */
    public void setThumbnailQueued ( boolean thumbnailQueued )
    {
        this.thumbnailQueued = thumbnailQueued;
    }

    /**
     * Returns cached element thumbnail icon for enabled state.
     *
     * @return cached element thumbnail icon for enabled state
     */
    public ImageIcon getEnabledThumbnail ()
    {
        return enabledThumbnail;
    }

    /**
     * Sets cached element thumbnail icon for enabled state.
     *
     * @param enabledThumbnail new cached element thumbnail icon for enabled state
     */
    public void setEnabledThumbnail ( ImageIcon enabledThumbnail )
    {
        this.enabledThumbnail = enabledThumbnail;
    }

    /**
     * Returns cached element thumbnail icon for disabled state.
     *
     * @return cached element thumbnail icon for disabled state
     */
    public ImageIcon getDisabledThumbnail ()
    {
        return disabledThumbnail;
    }

    /**
     * Sets cached element thumbnail icon for disabled state.
     *
     * @param disabledThumbnail new cached element thumbnail icon for disabled state
     */
    public void setDisabledThumbnail ( ImageIcon disabledThumbnail )
    {
        this.disabledThumbnail = disabledThumbnail;
    }

    /**
     * Returns element file.
     *
     * @return element file
     */
    public File getFile ()
    {
        return file;
    }

    /**
     * Sets element file.
     *
     * @param file new element file
     */
    public void setFile ( File file )
    {
        this.file = file;
    }
}