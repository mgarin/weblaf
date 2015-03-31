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

import com.alee.utils.CollectionUtils;
import com.alee.utils.FileUtils;

import java.io.File;
import java.util.List;

/**
 * Simple abstract thumbnail provider that allows passing extensions in its constructor.
 *
 * @author Mikle Garin
 */

public abstract class AbstractThumbnailProvider implements FileThumbnailProvider
{
    /**
     * Extensions accepted by this provider.
     */
    private final List<String> extensions;

    /**
     * Constructs new thumbnail provider that accepts all extensions.
     */
    public AbstractThumbnailProvider ()
    {
        this ( ( List<String> ) null );
    }

    /**
     * Constructs new thumbnail provider that accepts specified extensions..
     *
     * @param extensions extensions to be accepted
     */
    public AbstractThumbnailProvider ( final String... extensions )
    {
        this ( CollectionUtils.asList ( extensions ) );
    }

    /**
     * Constructs new thumbnail provider that accepts specified extensions..
     *
     * @param extensions extensions to be accepted
     */
    public AbstractThumbnailProvider ( final List<String> extensions )
    {
        super ();
        this.extensions = extensions;
    }

    /**
     * Returns extensions accepted by this provider.
     *
     * @return extensions accepted by this provider
     */
    public List<String> getExtensions ()
    {
        return extensions;
    }

    @Override
    public boolean accept ( final File file )
    {
        return extensions == null || extensions.contains ( FileUtils.getFileExtPart ( file.getName (), false ) );
    }
}