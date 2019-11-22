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
import com.alee.api.resource.Resource;
import com.alee.utils.XmlUtils;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.mapper.Mapper;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract {@link IconSource} converter containing methods for reading basic information.
 *
 * @param <I> {@link Icon} type
 * @author Mikle Garin
 */
public class AbstractIconSourceConverter<I extends Icon> extends ReflectionConverter
{
    /**
     * Converter constants.
     */
    public static final String ID_ATTRIBUTE = "id";

    /**
     * Constructs new {@link AbstractIconSourceConverter}.
     *
     * @param mapper             {@link Mapper} implementation
     * @param reflectionProvider {@link ReflectionProvider} implementation
     */
    public AbstractIconSourceConverter ( @NotNull final Mapper mapper, @NotNull final ReflectionProvider reflectionProvider )
    {
        super ( mapper, reflectionProvider );
    }

    /**
     * Returns unique {@link Icon} identifier.
     *
     * @param reader  {@link HierarchicalStreamReader}
     * @param context {@link UnmarshallingContext}
     * @return unique {@link Icon} identifier
     */
    @NotNull
    protected String readId ( @NotNull final HierarchicalStreamReader reader, @NotNull final UnmarshallingContext context )
    {
        return reader.getAttribute ( ID_ATTRIBUTE );
    }

    /**
     * Returns {@link Resource} for the {@link Icon}.
     *
     * @param reader  {@link HierarchicalStreamReader}
     * @param context {@link UnmarshallingContext}
     * @return {@link Resource} for the {@link Icon}
     */
    @NotNull
    protected Resource readResource ( @NotNull final HierarchicalStreamReader reader, @NotNull final UnmarshallingContext context )
    {
        return XmlUtils.readResource ( reader, context, mapper );
    }

    /**
     * Returns {@link List} of {@link IconAdjustment}s specified in XML.
     *
     * @param reader  {@link HierarchicalStreamReader}
     * @param context {@link UnmarshallingContext}
     * @return {@link List} of {@link IconAdjustment}s specified in XML
     */
    @NotNull
    protected List<IconAdjustment<I>> readAdjustments ( @NotNull final HierarchicalStreamReader reader,
                                                        @NotNull final UnmarshallingContext context )
    {
        final List<IconAdjustment<I>> adjustments = new ArrayList<IconAdjustment<I>> ();
        while ( reader.hasMoreChildren () )
        {
            reader.moveDown ();

            final String nodeName = reader.getNodeName ();
            final Class<?> iconSourceClass = mapper.realClass ( nodeName );
            if ( IconAdjustment.class.isAssignableFrom ( iconSourceClass ) )
            {
                adjustments.add ( ( IconAdjustment ) context.convertAnother ( adjustments, iconSourceClass ) );
            }

            reader.moveUp ();
        }
        return adjustments;
    }
}