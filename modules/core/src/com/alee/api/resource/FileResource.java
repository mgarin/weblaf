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

package com.alee.api.resource;

import com.alee.api.annotations.NotNull;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * File system resource.
 *
 * @author Mikle Garin
 */
@XStreamAlias ( "FileResource" )
public final class FileResource implements Resource
{
    /**
     * Resource file path.
     */
    @NotNull
    @XStreamAsAttribute
    private final String path;

    /**
     * Constructs new {@link FileResource}.
     *
     * @param file resource {@link File}
     */
    public FileResource ( @NotNull final File file )
    {
        this ( file.getAbsolutePath () );
    }

    /**
     * Constructs new {@link FileResource}.
     *
     * @param path resource file path
     */
    public FileResource ( @NotNull final String path )
    {
        this.path = path;
    }

    /**
     * Returns resource file path.
     *
     * @return resource file path
     */
    @NotNull
    public String getPath ()
    {
        return path;
    }

    @NotNull
    @Override
    public InputStream getInputStream ()
    {
        try
        {
            return new FileInputStream ( path );
        }
        catch ( final FileNotFoundException e )
        {
            throw new ResourceException ( "Unable to open FileResource stream for path: " + path, e );
        }
    }
}