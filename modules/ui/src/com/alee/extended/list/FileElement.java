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
 */
public class FileElement
{
    /**
     * Element operations lock.
     */
    private final Object lock = new Object ();

    /**
     * Element file.
     */
    private File file;

    /**
     * Whether thumbnail load is queued for this element or not.
     */
    private boolean thumbnailQueued = false;

    /**
     * Whether disabled thumbnail load is queued for this element or not.
     */
    private boolean disabledThumbnailQueued = false;

    /**
     * Cached element thumbnail icon for enabled state.
     */
    private ImageIcon enabledThumbnail = null;

    /**
     * Cached element thumbnail icon for disabled state.
     */
    private ImageIcon disabledThumbnail = null;

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
    public FileElement ( final File file )
    {
        super ();
        this.file = file;
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
    public void setFile ( final File file )
    {
        synchronized ( lock )
        {
            this.file = file;
            if ( file == null )
            {
                thumbnailQueued = false;
                disabledThumbnailQueued = false;
                enabledThumbnail = null;
                disabledThumbnail = null;
            }
        }
    }

    /**
     * Returns whether thumbnail load is queued or not.
     *
     * @return true if thumbnail load is queued, false otherwise
     */
    public boolean isThumbnailQueued ()
    {
        synchronized ( lock )
        {
            return thumbnailQueued;
        }
    }

    /**
     * Sets whether thumbnail load is queued or not.
     *
     * @param thumbnailQueued whether thumbnail load is queued or not
     */
    public void setThumbnailQueued ( final boolean thumbnailQueued )
    {
        synchronized ( lock )
        {
            if ( file != null )
            {
                this.thumbnailQueued = thumbnailQueued;
            }
        }
    }

    /**
     * Returns whether disabled thumbnail load is queued or not.
     *
     * @return true if disabled thumbnail load is queued, false otherwise
     */
    public boolean isDisabledThumbnailQueued ()
    {
        synchronized ( lock )
        {
            return disabledThumbnailQueued;
        }
    }

    /**
     * Sets whether disabled thumbnail load is queued or not.
     *
     * @param disabledThumbnailQueued whether disabled thumbnail load is queued or not
     */
    public void setDisabledThumbnailQueued ( final boolean disabledThumbnailQueued )
    {
        synchronized ( lock )
        {
            if ( file != null )
            {
                this.disabledThumbnailQueued = disabledThumbnailQueued;
            }
        }
    }

    /**
     * Returns cached element thumbnail icon for enabled state.
     *
     * @return cached element thumbnail icon for enabled state
     */
    public ImageIcon getEnabledThumbnail ()
    {
        synchronized ( lock )
        {
            return enabledThumbnail;
        }
    }

    /**
     * Sets cached element thumbnail icon for enabled state.
     *
     * @param enabledThumbnail new cached element thumbnail icon for enabled state
     */
    public void setEnabledThumbnail ( final ImageIcon enabledThumbnail )
    {
        synchronized ( lock )
        {
            this.enabledThumbnail = enabledThumbnail;
        }
    }

    /**
     * Returns cached element thumbnail icon for disabled state.
     *
     * @return cached element thumbnail icon for disabled state
     */
    public ImageIcon getDisabledThumbnail ()
    {
        synchronized ( lock )
        {
            return disabledThumbnail;
        }
    }

    /**
     * Sets cached element thumbnail icon for disabled state.
     *
     * @param disabledThumbnail new cached element thumbnail icon for disabled state
     */
    public void setDisabledThumbnail ( final ImageIcon disabledThumbnail )
    {
        synchronized ( lock )
        {
            this.disabledThumbnail = disabledThumbnail;
        }
    }

    /**
     * Returns file element lock object.
     *
     * @return file element lock object
     */
    public Object getLock ()
    {
        return lock;
    }
}