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
 * This class represents single file description fields.
 *
 * @author Mikle Garin
 */
public class FileDescription
{
    /**
     * File name.
     */
    private String name;

    /**
     * File size.
     */
    private String size;

    /**
     * File description.
     */
    private String description;

    /**
     * File modification date.
     */
    private String date;

    /**
     * Constructs empty file description.
     */
    public FileDescription ()
    {
        super ();
    }

    /**
     * Constructs file description with the specified values.
     *
     * @param name        file name
     * @param size        file size
     * @param description file description
     * @param date        file modification date
     */
    public FileDescription ( final String name, final String size, final String description, final String date )
    {
        super ();
        this.name = name;
        this.size = size;
        this.description = description;
        this.date = date;
    }

    /**
     * Returns file name.
     *
     * @return file name
     */
    public String getName ()
    {
        return name;
    }

    /**
     * Sets file name.
     *
     * @param name new file name
     */
    public void setName ( final String name )
    {
        this.name = name;
    }

    /**
     * Returns file size.
     *
     * @return file size
     */
    public String getSize ()
    {
        return size;
    }

    /**
     * Sets file size.
     *
     * @param size new file size
     */
    public void setSize ( final String size )
    {
        this.size = size;
    }

    /**
     * Returns file description.
     *
     * @return file description
     */
    public String getDescription ()
    {
        return description;
    }

    /**
     * Sets file description.
     *
     * @param description new file description
     */
    public void setDescription ( final String description )
    {
        this.description = description;
    }

    /**
     * Returns modification date.
     *
     * @return modification date
     */
    public String getDate ()
    {
        return date;
    }

    /**
     * Sets modification date.
     *
     * @param date new modification date
     */
    public void setDate ( final String date )
    {
        this.date = date;
    }
}