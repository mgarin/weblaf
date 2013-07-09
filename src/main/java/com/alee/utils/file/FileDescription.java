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

/**
 * User: mgarin Date: 08.07.11 Time: 12:16
 */

public class FileDescription
{
    private String name;
    private String size;
    private String description;
    private String date;

    public FileDescription ()
    {
        super ();
    }

    public FileDescription ( String name, String size, String description, String date )
    {
        super ();
        this.name = name;
        this.size = size;
        this.description = description;
        this.date = date;
    }

    public String getName ()
    {
        return name;
    }

    public void setName ( String name )
    {
        this.name = name;
    }

    public String getSize ()
    {
        return size;
    }

    public void setSize ( String size )
    {
        this.size = size;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription ( String description )
    {
        this.description = description;
    }

    public String getDate ()
    {
        return date;
    }

    public void setDate ( String date )
    {
        this.date = date;
    }
}
