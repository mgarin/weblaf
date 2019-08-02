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

package com.alee.extended.link;

import com.alee.utils.FileUtils;
import com.alee.utils.WebUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * Custom link action opening specified folder in native file system browser or a file in associated application.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebLink">How to use WebLink</a>
 * @see WebLink
 */
public class FileLinkAction extends AsyncLinkAction
{
    /**
     * File or folder to be opened.
     */
    private final File file;

    /**
     * Constructs new {@link FileLinkAction}.
     *
     * @param path path of the file or folder to be opened
     */
    public FileLinkAction ( final String path )
    {
        this ( new File ( path ) );
    }

    /**
     * Constructs new {@link FileLinkAction}.
     *
     * @param file file or folder to be opened
     */
    public FileLinkAction ( final File file )
    {
        super ();
        this.file = file;
    }

    @Override
    public Icon getIcon ()
    {
        return FileUtils.getFileIcon ( file );
    }

    @Override
    public String getText ()
    {
        return FileUtils.getDisplayFileName ( file );
    }

    @Override
    protected void asyncLinkExecuted ( final ActionEvent event )
    {
        WebUtils.openFileSafely ( file );
    }
}