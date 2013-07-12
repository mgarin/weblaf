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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;

/**
 * User: mgarin Date: 17.02.12 Time: 11:55
 */

@XStreamAlias ("ResourceFile")
public class ResourceFile implements Serializable
{
    @XStreamAsAttribute
    private ResourceLocation location;

    @XStreamAsAttribute
    private String source;

    @XStreamAsAttribute
    private String className;

    public ResourceFile ( ResourceLocation location, String source )
    {
        this ( location, source, ( String ) null );
    }

    public ResourceFile ( ResourceLocation location, String source, Class nearClass )
    {
        this ( location, source, nearClass.getCanonicalName () );
    }

    public ResourceFile ( ResourceLocation location, String source, String className )
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

    public void setLocation ( ResourceLocation location )
    {
        this.location = location;
    }

    public String getSource ()
    {
        return source;
    }

    public void setSource ( String source )
    {
        this.source = source;
    }

    public String getClassName ()
    {
        return className;
    }

    public void setClassName ( String className )
    {
        this.className = className;
    }
}