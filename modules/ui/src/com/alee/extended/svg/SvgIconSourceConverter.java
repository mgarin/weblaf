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

package com.alee.extended.svg;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.managers.icon.data.AbstractIconSourceConverter;
import com.alee.utils.xml.DimensionConverter;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.mapper.Mapper;

import java.awt.*;

/**
 * {@link SvgIconSource} converter.
 *
 * @author Mikle Garin
 */
public class SvgIconSourceConverter extends AbstractIconSourceConverter<SvgIcon>
{
    /**
     * Converter constants.
     */
    public static final String SIZE_ATTRIBUTE = "size";

    /**
     * Constructs new {@link SvgIconSourceConverter}.
     *
     * @param mapper             {@link Mapper} implementation
     * @param reflectionProvider {@link ReflectionProvider} implementation
     */
    public SvgIconSourceConverter ( @NotNull final Mapper mapper, @NotNull final ReflectionProvider reflectionProvider )
    {
        super ( mapper, reflectionProvider );
    }

    @Override
    public boolean canConvert ( @NotNull final Class type )
    {
        return type.equals ( SvgIconSource.class );
    }

    @Override
    public Object unmarshal ( @NotNull final HierarchicalStreamReader reader, @NotNull final UnmarshallingContext context )
    {
        return new SvgIconSource (
                readId ( reader, context ),
                readResource ( reader, context ),
                readSize ( reader, context ),
                readAdjustments ( reader, context )
        );
    }

    /**
     * Returns preferred {@link SvgIcon} size or {@code null} if none specified in XML.
     *
     * @param reader  {@link HierarchicalStreamReader}
     * @param context {@link UnmarshallingContext}
     * @return preferred {@link SvgIcon} size or {@code null} if none specified in XML
     */
    @Nullable
    protected Dimension readSize ( @NotNull final HierarchicalStreamReader reader, @NotNull final UnmarshallingContext context )
    {
        final String size = reader.getAttribute ( SIZE_ATTRIBUTE );
        return size != null ? DimensionConverter.dimensionFromString ( size ) : null;
    }
}