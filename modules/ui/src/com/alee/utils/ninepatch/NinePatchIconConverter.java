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

import com.alee.managers.style.StyleException;
import com.alee.managers.style.data.ComponentStyleConverter;
import com.alee.managers.style.data.SkinInfoConverter;
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
     * todo 1. Create proper object->xml marshalling strategy
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

    @Override
    public boolean canConvert ( final Class type )
    {
        return type.equals ( NinePatchIcon.class );
    }

    @Override
    public Object unmarshal ( final HierarchicalStreamReader reader, final UnmarshallingContext context )
    {
        // Resolving class this resource is related to
        String nearClassPath = reader.getAttribute ( NEAR_CLASS_ATTRIBUTE );
        Class nearClass;
        if ( nearClassPath != null )
        {
            // Using class provided in "nearClass" attribute
            nearClass = ReflectUtils.getClassSafely ( nearClassPath );

            // It might be an incomplete class name
            // In that case we should try using skin class package to fill it
            if ( nearClass == null )
            {
                final String skinClassName = ( String ) context.get ( SkinInfoConverter.SKIN_CLASS );
                final Class skinClass = ReflectUtils.getClassSafely ( skinClassName );
                if ( skinClass == null )
                {
                    final String msg = "Specified skin class '%s' cannot be found";
                    throw new StyleException ( String.format ( msg, skinClassName ) );
                }
                nearClassPath = skinClass.getPackage ().getName () + "." + nearClassPath;
                nearClass = ReflectUtils.getClassSafely ( nearClassPath );
            }
        }
        else
        {
            // Using path related to painter class
            // Painter class is already resolved here so we simply using it straight away
            nearClass = ( Class ) context.get ( ComponentStyleConverter.CONTEXT_PAINTER_CLASS );
            nearClassPath = nearClass.getCanonicalName ();
        }

        // Reading 9-patch icon
        final String iconPath = reader.getValue ();
        if ( nearClass != null )
        {
            try
            {
                // Read and return new 9-patch icon
                return new NinePatchIcon ( nearClass.getResource ( iconPath ) );
            }
            catch ( final Exception e )
            {
                // Icon cannot be read
                final String msg = "Unable to read 9-patch icon '%s' at path '%s' near class '%s'";
                throw new StyleException ( String.format ( msg, iconPath, nearClassPath, nearClass ), e );
            }
        }
        else
        {
            // Icon location cannot be found
            final String msg = "Unable to find relative class for 9-patch icon '%s'";
            throw new StyleException ( String.format ( msg, iconPath ) );
        }
    }
}