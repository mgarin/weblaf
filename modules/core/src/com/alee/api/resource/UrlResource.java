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
import com.alee.utils.NetUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * {@link URL} resource.
 *
 * @author Mikle Garin
 */
@XStreamAlias ( "UrlResource" )
public final class UrlResource implements Resource
{
    /**
     * Resource {@link URL} address.
     */
    @NotNull
    @XStreamAsAttribute
    private final String url;

    /**
     * Constructs new {@link UrlResource}.
     *
     * @param url resource {@link URL}
     */
    public UrlResource ( @NotNull final URL url )
    {
        this ( NetUtils.getAddress ( url ) );
    }

    /**
     * Constructs new {@link UrlResource}.
     *
     * @param url resource {@link URL} address
     */
    public UrlResource ( @NotNull final String url )
    {
        this.url = url;
    }

    /**
     * Returns resource {@link URL} address.
     *
     * @return resource {@link URL} address
     */
    @NotNull
    public String getUrl ()
    {
        return url;
    }

    @NotNull
    @Override
    public InputStream getInputStream ()
    {
        try
        {
            return new URL ( url ).openStream ();
        }
        catch ( final IOException e )
        {
            throw new ResourceException ( "Unable to open UrlResource stream for url: " + url, e );
        }
    }
}