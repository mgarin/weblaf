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

package com.alee.managers.icon.data;

import com.alee.api.annotations.NotNull;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.mapper.Mapper;

import javax.swing.*;

/**
 * {@link ImageIconSource} converter.
 *
 * @author Mikle Garin
 */
public class ImageIconSourceConverter extends AbstractIconSourceConverter<ImageIcon>
{
    /**
     * Constructs new {@link ImageIconSourceConverter}.
     *
     * @param mapper             {@link Mapper} implementation
     * @param reflectionProvider {@link ReflectionProvider} implementation
     */
    public ImageIconSourceConverter ( @NotNull final Mapper mapper, @NotNull final ReflectionProvider reflectionProvider )
    {
        super ( mapper, reflectionProvider );
    }

    @Override
    public boolean canConvert ( @NotNull final Class type )
    {
        return type.equals ( ImageIconSource.class );
    }

    @NotNull
    @Override
    public Object unmarshal ( @NotNull final HierarchicalStreamReader reader, @NotNull final UnmarshallingContext context )
    {
        return new ImageIconSource (
                readId ( reader, context ),
                readResource ( reader, context ),
                readAdjustments ( reader, context )
        );
    }
}