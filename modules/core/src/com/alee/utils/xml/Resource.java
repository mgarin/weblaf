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

import com.alee.api.jdk.Objects;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.File;
import java.io.Serializable;

/**
 * Special class representing single resource.
 * It might either be a JAR resource path, local path, network path or web address.
 *
 * @author Mikle Garin
 */
@XStreamAlias ( "Resource" )
public class Resource implements Serializable
{
    /**
     * todo 1. Probably replace with customized URIs for various cases
     */

    /**
     * Resource location.
     */
    @XStreamAsAttribute
    private ResourceLocation location;

    /**
     * Name of the class relative to which resource is located.
     * Specified only in case {@link #location} is set to {@link com.alee.utils.xml.ResourceLocation#nearClass} value.
     */
    @XStreamAsAttribute
    private String className;

    /**
     * Resource path.
     */
    @XStreamAsAttribute
    private String path;

    /**
     * Constructs new resource.
     *
     * @param path resource file path
     */
    public Resource ( final String path )
    {
        this ( ResourceLocation.filePath, null, path );
    }

    /**
     * Constructs new resource.
     *
     * @param file resource file
     */
    public Resource ( final File file )
    {
        this ( ResourceLocation.filePath, null, file.getAbsolutePath () );
    }

    /**
     * Constructs new resource.
     *
     * @param className class name for relative path
     * @param path      resource path
     */
    public Resource ( final String className, final String path )
    {
        this ( ResourceLocation.nearClass, className, path );
    }

    /**
     * Constructs new resource.
     *
     * @param clazz class for relative path
     * @param path  resource path
     */
    public Resource ( final Class clazz, final String path )
    {
        this ( ResourceLocation.nearClass, clazz.getCanonicalName (), path );
    }

    /**
     * Constructs new resource.
     *
     * @param location  resource location
     * @param className class name for relative path
     * @param path      resource path
     */
    public Resource ( final ResourceLocation location, final String className, final String path )
    {
        super ();
        setLocation ( location );
        setPath ( path );
        setClassName ( className );
    }

    /**
     * Returns resource location.
     *
     * @return resource location
     */
    public ResourceLocation getLocation ()
    {
        return location;
    }

    /**
     * Sets resource location.
     *
     * @param location resource location
     */
    public void setLocation ( final ResourceLocation location )
    {
        this.location = location;
    }

    /**
     * Returns name of the class relative to which resource is located.
     *
     * @return name of the class relative to which resource is located
     */
    public String getClassName ()
    {
        return className;
    }

    /**
     * Sets name of the class relative to which resource is located.
     *
     * @param name name of the class relative to which resource is located
     */
    public void setClassName ( final String name )
    {
        this.className = name;
    }

    /**
     * Returns resource path.
     *
     * @return resource path
     */
    public String getPath ()
    {
        return path;
    }

    /**
     * Sets resource path.
     *
     * @param path resource path
     */
    public void setPath ( final String path )
    {
        this.path = path;
    }

    @Override
    public boolean equals ( final Object object )
    {
        final boolean equals;
        if ( object != null && object instanceof Resource )
        {
            final Resource other = ( Resource ) object;
            equals = other.location == location &&
                    Objects.equals ( other.className, className ) &&
                    Objects.equals ( other.path, path );
        }
        else
        {
            equals = false;
        }
        return equals;
    }
}