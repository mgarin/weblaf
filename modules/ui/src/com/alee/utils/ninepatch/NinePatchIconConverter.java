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

package com.alee.utils.ninepatch;

import com.alee.managers.log.Log;
import com.alee.managers.style.data.ComponentStyleConverter;
import com.alee.utils.ReflectUtils;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.mapper.Mapper;

/**
 * Custom XStream converter for NinePatchIcon class.
 *
 * @author Mikle Garin
 */

public class NinePatchIconConverter extends ReflectionConverter
{
    /**
     * todo 1. Create proper object->xml marshalling strategy (or not?)
     */

    /**
     * Converter constants.
     */
    public static final String NEAR_CLASS_ATTRIBUTE = "nearClass";

    /**
     * Constructs NinePatchIconConverter with the specified mapper and reflection provider.
     *
     * @param mapper             mapper
     * @param reflectionProvider reflection provider
     */
    public NinePatchIconConverter ( final Mapper mapper, final ReflectionProvider reflectionProvider )
    {
        super ( mapper, reflectionProvider );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canConvert ( final Class type )
    {
        return type.equals ( NinePatchIcon.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object unmarshal ( final HierarchicalStreamReader reader, final UnmarshallingContext context )
    {
        String nearClass = reader.getAttribute ( NEAR_CLASS_ATTRIBUTE );
        if ( nearClass == null )
        {
            nearClass = ( String ) context.get ( ComponentStyleConverter.PAINTER_CLASS_ATTRIBUTE );
        }
        if ( nearClass == null )
        {
            return null;
        }
        else
        {
            final Class nearRealClass = ReflectUtils.getClassSafely ( nearClass );
            if ( nearRealClass == null )
            {
                return null;
            }
            else
            {
                final String iconPath = reader.getValue ();
                try
                {
                    return new NinePatchIcon ( nearRealClass.getResource ( iconPath ) );
                }
                catch ( final Throwable e )
                {
                    Log.error ( this, "Unable to read 9-patch icon near class \"" + nearClass + "\": " + iconPath, e );
                    return null;
                }
            }
        }
    }
}