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

package com.alee.utils.xml;

import com.alee.utils.CompareUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;

/**
 * Special class representing resource file location.
 * It might either be a file located in network or web, locally or inside the application JAR.
 *
 * @author Mikle Garin
 */

@XStreamAlias ( "ResourceFile" )
public class ResourceFile implements Serializable
{
    /**
     * Resource file location type.
     */
    @XStreamAsAttribute
    private ResourceLocation location;

    /**
     * Resource file path.
     */
    @XStreamAsAttribute
    private String source;

    /**
     * Name of the class relative to which file is located.
     * Specified only in case {@link #location} is set to {@link com.alee.utils.xml.ResourceLocation#nearClass} value.
     */
    @XStreamAsAttribute
    private String className;

    public ResourceFile ( final ResourceLocation location, final String source )
    {
        this ( location, source, ( String ) null );
    }

    public ResourceFile ( final ResourceLocation location, final String source, final Class nearClass )
    {
        this ( location, source, nearClass.getCanonicalName () );
    }

    public ResourceFile ( final ResourceLocation location, final String source, final String className )
    {
        super ();
        setLocation ( location );
        setSource ( source );
        setClassName ( className );
    }

    public ResourceLocation getLocation ()
    {
        return location;
    }

    public void setLocation ( final ResourceLocation location )
    {
        this.location = location;
    }

    public String getSource ()
    {
        return source;
    }

    public void setSource ( final String source )
    {
        this.source = source;
    }

    public String getClassName ()
    {
        return className;
    }

    public void setClassName ( final String className )
    {
        this.className = className;
    }

    @Override
    public boolean equals ( final Object object )
    {
        if ( object != null && object instanceof ResourceFile )
        {
            final ResourceFile other = ( ResourceFile ) object;
            return other.location == location && CompareUtils.equals ( other.source, source ) &&
                    CompareUtils.equals ( other.className, className );
        }
        else
        {
            return false;
        }
    }
}