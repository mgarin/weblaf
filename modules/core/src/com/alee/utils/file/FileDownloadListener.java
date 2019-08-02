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

package com.alee.utils.file;

import java.io.File;
import java.util.EventListener;

/**
 * This listener interface provides all required methods to observe and manipulate the download process.
 * Check out downloadFile methods from FileUtils to see how this listener can be used.
 *
 * @author Mikle Garin
 */
public interface FileDownloadListener extends EventListener
{
    /**
     * Called when remote file size retrieved.
     *
     * @param totalSize file size
     */
    public void sizeDetermined ( int totalSize );

    /**
     * Called when another file part have been downloaded.
     *
     * @param totalBytesDownloaded total bytes downloaded so far
     */
    public void partDownloaded ( int totalBytesDownloaded );

    /**
     * Called when file download completed.
     *
     * @param file downloaded local file
     */
    public void fileDownloaded ( File file );

    /**
     * Called when file download failed.
     *
     * @param e exception that caused download to fail
     */
    public void fileDownloadFailed ( Throwable e );

    /**
     * Returns whether download operation should stop or not.
     * This method is getting called before and after each time-consuming operation (for example file part download).
     *
     * @return true if you want to stop download, false otherwise
     */
    public boolean shouldStopDownload ();
}