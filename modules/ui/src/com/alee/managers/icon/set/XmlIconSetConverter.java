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

package com.alee.managers.icon.set;

import com.alee.api.annotations.NotNull;
import com.alee.managers.icon.IconException;
import com.alee.managers.icon.data.IconSource;
import com.alee.utils.ReflectUtils;
import com.alee.utils.XmlUtils;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.mapper.Mapper;

/**
 * {@link XmlIconSet} converter.
 *
 * @author Mikle Garin
 */
public class XmlIconSetConverter extends ReflectionConverter
{
    /**
     * Converter constants.
     */
    public static final String ID_ATTRIBUTE = "id";

    /**
     * Constructs new {@link XmlIconSetConverter}.
     *
     * @param mapper             {@link Mapper} implementation
     * @param reflectionProvider {@link ReflectionProvider} implementation
     */
    public XmlIconSetConverter ( @NotNull final Mapper mapper, @NotNull final ReflectionProvider reflectionProvider )
    {
        super ( mapper, reflectionProvider );
    }

    @Override
    public boolean canConvert ( @NotNull final Class type )
    {
        return type.equals ( XmlIconSet.class );
    }

    @NotNull
    @Override
    public Object unmarshal ( @NotNull final HierarchicalStreamReader reader, @NotNull final UnmarshallingContext context )
    {
        try
        {
            final XmlIconSet iconSetData = ( XmlIconSet ) context.currentObject ();

            // Forcefully updating XmlIconSet final identifier
            ReflectUtils.setFieldValue ( iconSetData, "id", reader.getAttribute ( ID_ATTRIBUTE ) );

            // Configuring context with XmlIconSet resource settings
            context.put ( XmlUtils.CONTEXT_NEAR_CLASS, reader.getAttribute ( XmlUtils.NEAR_CLASS_ATTRIBUTE ) );
            context.put ( XmlUtils.CONTEXT_BASE, reader.getAttribute ( XmlUtils.BASE_ATTRIBUTE ) );

            // Reading icons specified in the XmlIconSet
            while ( reader.hasMoreChildren () )
            {
                reader.moveDown ();

                final String nodeName = reader.getNodeName ();
                final Class<?> iconSourceClass = mapper.realClass ( nodeName );
                if ( IconSource.class.isAssignableFrom ( iconSourceClass ) )
                {
                    iconSetData.addIcon ( ( IconSource ) context.convertAnother ( iconSetData, iconSourceClass ) );
                }

                reader.moveUp ();
            }

            // Removing XmlIconSet resource settings from context
            context.put ( XmlUtils.CONTEXT_BASE, null );
            context.put ( XmlUtils.CONTEXT_NEAR_CLASS, null );

            return iconSetData;
        }
        catch ( final Exception e )
        {
            throw new IconException ( "Unable to load XmlIconSet", e );
        }
    }
}