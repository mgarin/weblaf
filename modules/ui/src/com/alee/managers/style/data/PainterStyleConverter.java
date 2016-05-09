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

package com.alee.managers.style.data;

import com.alee.managers.style.StyleException;
import com.alee.painter.Painter;
import com.alee.utils.ReflectUtils;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.mapper.Mapper;

import java.util.LinkedHashMap;

/**
 * Custom XStream converter for {@link com.alee.managers.style.data.PainterStyle} class.
 *
 * Note that this converter does not handle direct style painters - those are explicitely converted by
 * {@link com.alee.managers.style.data.ComponentStyleConverter} as it provides an additional degree of associated features.
 *
 * @author Mikle Garin
 */

public final class PainterStyleConverter extends ReflectionConverter
{
    /**
     * Constructs PainterConverter with the specified mapper and reflection provider.
     *
     * @param mapper             mapper
     * @param reflectionProvider reflection provider
     */
    public PainterStyleConverter ( final Mapper mapper, final ReflectionProvider reflectionProvider )
    {
        super ( mapper, reflectionProvider );
    }

    @Override
    public boolean canConvert ( final Class type )
    {
        return Painter.class.isAssignableFrom ( type );
    }

    @Override
    public Object unmarshal ( final HierarchicalStreamReader reader, final UnmarshallingContext context )
    {
        // Trying to retrieve style ID from context
        // It will be there only if this painter is read from style read sequence
        // Either way it is not critical for painter unmarshal so we will simply ignore it if its not there
        final String styleId = ( String ) context.get ( ComponentStyleConverter.CONTEXT_STYLE_ID );

        // Retrieving default painter class based on parent painter and this node name
        // Basically we are reading this painter as a field of another painter here
        final Class<? extends Painter> parent = ( Class<? extends Painter> ) context.get ( ComponentStyleConverter.CONTEXT_PAINTER_CLASS );
        final Class<? extends Painter> defaultPainter = StyleConverterUtils.getDefaultPainter ( parent, reader.getNodeName () );

        // Unmarshalling painter class
        final Class painterClass = PainterStyleConverter.unmarshalPainterClass ( reader, context, defaultPainter, styleId );

        // Providing painter class to subsequent converters
        context.put ( ComponentStyleConverter.CONTEXT_PAINTER_CLASS, painterClass );

        // Reading painter style properties
        // Using LinkedHashMap to keep properties order
        final LinkedHashMap<String, Object> painterProperties = new LinkedHashMap<String, Object> ();
        StyleConverterUtils.readProperties ( reader, context, painterProperties, painterClass, styleId );

        // Creating painter style
        final PainterStyle painterStyle = new PainterStyle ();
        painterStyle.setPainterClass ( painterClass.getCanonicalName () );
        painterStyle.setProperties ( painterProperties );

        // Cleaning up context
        context.put ( ComponentStyleConverter.CONTEXT_PAINTER_CLASS, parent );

        return painterStyle;
    }

    /**
     * Unmarshal painter class according to the specified class attribute.
     *
     * @param reader              {@link com.thoughtworks.xstream.io.HierarchicalStreamReader}
     * @param context             {@link com.thoughtworks.xstream.converters.UnmarshallingContext}
     * @param defaultPainterClass default painter class
     * @param styleId             style ID
     * @return painter class
     * @throws com.alee.managers.style.StyleException if painter class cannot be resolved
     */
    public static Class unmarshalPainterClass ( final HierarchicalStreamReader reader, final UnmarshallingContext context,
                                                final Class<? extends Painter> defaultPainterClass, final String styleId )
    {
        // Reading painter class name
        // It might have been shortened so we might have to check its name combined with skin package
        // That check is performed only when class cannot be found by its original path
        String painterClassName = reader.getAttribute ( ComponentStyleConverter.PAINTER_CLASS_ATTRIBUTE );
        if ( painterClassName != null )
        {
            // Trying to read painter class directly by name
            Class painterClass = ReflectUtils.getClassSafely ( painterClassName );

            // Resolving shortened painter class name
            if ( painterClass == null )
            {
                // Checking skin reference existance
                // This reference will only exist within skin parsing sequence
                final String skinClassName = ( String ) context.get ( SkinInfoConverter.SKIN_CLASS );
                final Class skinClass = ReflectUtils.getClassSafely ( skinClassName );
                if ( skinClass == null )
                {
                    throw new StyleException ( "Class \"" + painterClassName + "\" for style \"" + styleId + "\" cannot be found" );
                }

                // Trying to retrieve skin class from skin package
                final String skinPackage = skinClass.getPackage ().getName ();
                painterClassName = skinPackage + "." + painterClassName;
                painterClass = ReflectUtils.getClassSafely ( painterClassName );
                if ( painterClass == null )
                {
                    throw new StyleException ( "Class \"" + painterClassName + "\" for style \"" + styleId + "\" cannot be found" );
                }
            }

            // Return resolved painter class
            return painterClass;
        }
        else if ( defaultPainterClass != null )
        {
            // Return default painter class
            return defaultPainterClass;
        }
        else
        {
            // None found
            throw new StyleException ( "Painter class for style \"" + styleId + "\" was not specified " );
        }
    }
}