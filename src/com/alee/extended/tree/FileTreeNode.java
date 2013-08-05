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

package com.alee.extended.tree;

import java.io.File;

/**
 * User: mgarin Date: 15.10.2010 Time: 14:34:35
 */

public class FileTreeNode extends AsyncUniqueNode
{
    private File file;
    private String name = null;

    public FileTreeNode ( File file )
    {
        super ();
        this.file = file;
    }

    public FileTreeNode ( File file, String name )
    {
        super ();
        this.file = file;
        this.name = name;
    }

    public File getFile ()
    {
        return file;
    }

    public void setFile ( File file )
    {
        this.file = file;
    }

    public String getName ()
    {
        return name;
    }

    public void setName ( String name )
    {
        this.name = name;

    }

    public String toString ()
    {
        return name != null ? name : ( file != null ? file.getName () : "root" );
    }
}