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

package com.alee.managers.version;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import java.io.Serializable;

/**
 * Web Look and Feel library version information class.
 *
 * @author Mikle Garin
 */
@XStreamAlias ( "VersionInfo" )
public final class VersionInfo implements Serializable
{
    /**
     * Full library name.
     */
    @XStreamAsAttribute
    private String name;

    /**
     * Version number.
     */
    @XStreamAsAttribute
    private int version;

    /**
     * Build number.
     */
    @XStreamAsAttribute
    private int build;

    /**
     * Build type.
     */
    @XStreamAsAttribute
    private String type;

    /**
     * Version release date.
     */
    @XStreamAsAttribute
    @XStreamConverter ( VersionDateConverter.class )
    private long date;

    /**
     * Short version description.
     */
    @XStreamAsAttribute
    private String comment;

    /**
     * Constructs empty version information class.
     */
    public VersionInfo ()
    {
        super ();
    }

    /**
     * Returns full library name.
     *
     * @return full library name
     */
    public String getName ()
    {
        return name;
    }

    /**
     * Sets full library name.
     *
     * @param name full library name
     */
    public void setName ( final String name )
    {
        this.name = name;
    }

    /**
     * Returns library version.
     *
     * @return library version
     */
    public int getVersion ()
    {
        return version;
    }

    /**
     * Sets library version.
     *
     * @param version library version
     */
    public void setVersion ( final int version )
    {
        this.version = version;
    }

    /**
     * Returns build number.
     *
     * @return build number
     */
    public int getBuild ()
    {
        return build;
    }

    /**
     * Sets build number.
     *
     * @param build build number
     */
    public void setBuild ( final int build )
    {
        this.build = build;
    }

    /**
     * Returns build type.
     *
     * @return build type
     */
    public String getType ()
    {
        return type;
    }

    /**
     * Sets build type.
     *
     * @param type build type
     */
    public void setType ( final String type )
    {
        this.type = type;
    }

    /**
     * Returns version release date.
     *
     * @return version release date
     */
    public long getDate ()
    {
        return date;
    }

    /**
     * Sets version release date.
     *
     * @param date version release date
     */
    public void setDate ( final long date )
    {
        this.date = date;
    }

    /**
     * Returns short version description.
     *
     * @return short version description
     */
    public String getComment ()
    {
        return comment;
    }

    /**
     * Sets short version description.
     *
     * @param comment short version description
     */
    public void setComment ( final String comment )
    {
        this.comment = comment;
    }

    /**
     * Returns a negative integer, zero, or a positive integer as version is less than, equal to, or greater than the specified one.
     *
     * @param version version to compare with
     * @return a negative integer, zero, or a positive integer as version is less than, equal to, or greater than the specified one
     */
    public int compareTo ( final VersionInfo version )
    {
        if ( this.version < version.getVersion () )
        {
            return -1;
        }
        else if ( this.version > version.getVersion () )
        {
            return 1;
        }
        else
        {
            if ( this.build < version.getBuild () )
            {
                return -1;
            }
            if ( this.build > version.getBuild () )
            {
                return 1;
            }
            else
            {
                return 0;
            }
        }
    }

    @Override
    public String toString ()
    {
        return name + " v" + version + "." + build + ( build > 0 ? " (" + type + ")" : "" );
    }
}