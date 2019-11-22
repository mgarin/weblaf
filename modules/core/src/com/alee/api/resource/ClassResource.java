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
import com.alee.utils.ReflectUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.InputStream;

/**
 * {@link Class} resource.
 *
 * @author Mikle Garin
 */
@XStreamAlias ( "ClassResource" )
public final class ClassResource implements Resource
{
    /**
     * Name of the {@link Class} relative to which resource is located.
     */
    @NotNull
    @XStreamAsAttribute
    private final String className;

    /**
     * Resource path near {@link Class}.
     */
    @NotNull
    @XStreamAsAttribute
    private final String path;

    /**
     * Constructs new {@link ClassResource}.
     *
     * @param clazz {@link Class} relative to which resource is located
     * @param path  resource path near {@link Class}
     */
    public ClassResource ( @NotNull final Class<?> clazz, @NotNull final String path )
    {
        this ( clazz.getCanonicalName (), path );
    }

    /**
     * Constructs new {@link ClassResource}.
     *
     * @param className name of the {@link Class} relative to which resource is located
     * @param path      resource path near {@link Class}
     */
    public ClassResource ( @NotNull final String className, @NotNull final String path )
    {
        this.className = className;
        this.path = path;
    }

    /**
     * Returns name of the {@link Class} relative to which resource is located.
     *
     * @return name of the {@link Class} relative to which resource is located
     */
    @NotNull
    public String getClassName ()
    {
        return className;
    }

    /**
     * Returns resource path near {@link Class}.
     *
     * @return resource path near {@link Class}
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
            return ReflectUtils.getClass ( className ).getResourceAsStream ( path );
        }
        catch ( final ClassNotFoundException e )
        {
            throw new ResourceException ( "Unable to open ClassResource stream for class and path: " + className + ":" + path, e );
        }
    }
}