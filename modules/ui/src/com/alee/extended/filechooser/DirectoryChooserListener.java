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

package com.alee.extended.filechooser;

import java.io.File;
import java.util.EventListener;

/**
 * Special listener for WebDirectoryChooserPanel component.
 *
 * @author Mikle Garin
 * @see WebDirectoryChooserPanel
 * @see WebDirectoryChooser
 */
public interface DirectoryChooserListener extends EventListener
{
    /**
     * Notifies about directory selection changes.
     *
     * @param file newly selected directory or null if none selected
     */
    public void selectionChanged ( File file );

    /**
     * Notifies about directory selection accept.
     *
     * @param file newly selected directory or null if none selected
     */
    public void accepted ( File file );

    /**
     * Notifies about directory selection cancel.
     */
    public void cancelled ();
}