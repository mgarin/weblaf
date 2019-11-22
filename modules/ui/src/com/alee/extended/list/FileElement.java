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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;

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
    @NotNull
    protected final Object lock = new Object ();

    /**
     * Element file.
     */
    @NotNull
    protected File file;

    /**
     * Whether thumbnail load is queued for this element or not.
     */
    protected boolean thumbnailQueued = false;

    /**
     * Whether disabled thumbnail load is queued for this element or not.
     */
    protected boolean disabledThumbnailQueued = false;

    /**
     * Cached element thumbnail icon for enabled state.
     */
    @Nullable
    protected Icon enabledThumbnail = null;

    /**
     * Cached element thumbnail icon for disabled state.
     */
    @Nullable
    protected Icon disabledThumbnail = null;

    /**
     * Constructs element with specified file.
     *
     * @param file {@link File}
     */
    public FileElement ( @NotNull final File file )
    {
        this.file = file;
    }

    /**
     * Returns element file.
     *
     * @return element file
     */
    @NotNull
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
            this.thumbnailQueued = thumbnailQueued;
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
            this.disabledThumbnailQueued = disabledThumbnailQueued;
        }
    }

    /**
     * Returns cached element thumbnail icon for enabled state.
     *
     * @return cached element thumbnail icon for enabled state
     */
    @Nullable
    public Icon getEnabledThumbnail ()
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
    public void setEnabledThumbnail ( @Nullable final Icon enabledThumbnail )
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
    @Nullable
    public Icon getDisabledThumbnail ()
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
    public void setDisabledThumbnail ( @Nullable final Icon disabledThumbnail )
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
    @NotNull
    public Object getLock ()
    {
        return lock;
    }
}