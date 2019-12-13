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

import com.alee.api.annotations.NotNull;
import com.alee.managers.style.StyleException;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.utils.ReflectUtils;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.mapper.CannotResolveClassException;
import com.thoughtworks.xstream.mapper.Mapper;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

/**
 * Custom XStream converter for {@link PainterStyle} class.
 *
 * Note that this converter does not handle direct style painters - those are explicitely converted by
 * {@link ComponentStyleConverter} as it provides an additional degree of associated features.
 *
 * @author Mikle Garin
 */
public final class PainterStyleConverter extends ReflectionConverter
{
    /**
     * todo 1. Rework this class to actually be used as a proper converter for {@link PainterStyle} within style
     * todo 2. Provide appropriate marshalling implementation
     */

    /**
     * Constructs PainterConverter with the specified mapper and reflection provider.
     *
     * @param mapper             mapper
     * @param reflectionProvider reflection provider
     */
    public PainterStyleConverter ( @NotNull final Mapper mapper, @NotNull final ReflectionProvider reflectionProvider )
    {
        super ( mapper, reflectionProvider );
    }

    @Override
    public boolean canConvert ( @NotNull final Class type )
    {
        return Painter.class.isAssignableFrom ( type );
    }

    @NotNull
    @Override
    public Object unmarshal ( @NotNull final HierarchicalStreamReader reader, @NotNull final UnmarshallingContext context )
    {
        // Retrieving style identifier from context
        // It must be there at all times, whether this painter is read from style or another painter
        final String styleId = ( String ) context.get ( ComponentStyleConverter.CONTEXT_STYLE_ID );
        if ( styleId == null )
        {
            throw new StyleException ( "Context style identifier must be specified" );
        }

        // Retrieving overwrite policy
        final String ow = reader.getAttribute ( ComponentStyleConverter.OVERWRITE_ATTRIBUTE );
        final Boolean overwrite = ow != null ? Boolean.parseBoolean ( ow ) : null;

        // Retrieving default painter class based on parent painter and this node name
        // Basically we are reading this painter as a field of another painter here
        final Class<? extends Painter> parent = ( Class<? extends Painter> ) context.get ( ComponentStyleConverter.CONTEXT_PAINTER_CLASS );
        final Class<? extends Painter> defaultPainter = getDefaultPainter ( parent, reader.getNodeName () );

        // Unmarshalling painter class
        final Class<? extends Painter> painterClass =
                PainterStyleConverter.unmarshalPainterClass ( reader, context, mapper, defaultPainter, styleId );

        // Providing painter class to subsequent converters
        context.put ( ComponentStyleConverter.CONTEXT_PAINTER_CLASS, painterClass );

        // Reading painter style properties
        // Using LinkedHashMap to keep properties order
        final LinkedHashMap<String, Object> painterProperties = new LinkedHashMap<String, Object> ();
        StyleConverterUtils.readProperties ( reader, context, mapper, painterProperties, painterClass, styleId );

        // Creating painter style
        final PainterStyle painterStyle = new PainterStyle ();
        painterStyle.setOverwrite ( overwrite );
        painterStyle.setPainterClass ( painterClass.getCanonicalName () );
        painterStyle.setProperties ( painterProperties );

        // Cleaning up context
        context.put ( ComponentStyleConverter.CONTEXT_PAINTER_CLASS, parent );

        return painterStyle;
    }

    /**
     * Retuns default painter class for the painter field in specified class.
     *
     * @param inClass class containing painter referencing field
     * @param field   painter referencing field name
     * @return default painter class for the painter field in specified class
     */
    @NotNull
    private Class<? extends Painter> getDefaultPainter ( @NotNull final Class<?> inClass, @NotNull final String field )
    {
        try
        {
            final Field painterField = ReflectUtils.getField ( inClass, field );
            final DefaultPainter defaultPainter = painterField.getAnnotation ( DefaultPainter.class );
            if ( defaultPainter != null )
            {
                return defaultPainter.value ();
            }
            else
            {
                final String msg = "Painter field '%s' in class '%s' doesn't have DefaultPainter annotation";
                throw new StyleException ( String.format ( msg, field, inClass ) );
            }
        }
        catch ( final Exception e )
        {
            final String msg = "Unable to find Painter field '%s' in class '%s' for class retrieval from DefaultPainter annotation";
            throw new StyleException ( String.format ( msg, field, inClass ) );
        }
    }

    /**
     * Unmarshal painter class according to the specified class attribute.
     *
     * @param reader              {@link com.thoughtworks.xstream.io.HierarchicalStreamReader}
     * @param context             {@link com.thoughtworks.xstream.converters.UnmarshallingContext}
     * @param mapper              {@link com.thoughtworks.xstream.mapper.Mapper}
     * @param defaultPainterClass default painter class
     * @param styleId             {@link com.alee.managers.style.StyleId} of the painter's style
     * @return painter class according to the specified class attribute
     * @throws com.alee.managers.style.StyleException if painter class cannot be resolved
     */
    @NotNull
    public static Class<? extends Painter> unmarshalPainterClass ( @NotNull final HierarchicalStreamReader reader,
                                                                   @NotNull final UnmarshallingContext context,
                                                                   @NotNull final Mapper mapper,
                                                                   @NotNull final Class<? extends Painter> defaultPainterClass,
                                                                   @NotNull final String styleId )
    {
        // Reading painter class name
        // It might have been shortened so we might have to check its name combined with skin package
        // That check is performed only when class cannot be found by its original path
        Class<? extends Painter> painterClass;
        String painterClassName = reader.getAttribute ( ComponentStyleConverter.CLASS_ATTRIBUTE );
        if ( painterClassName != null )
        {
            try
            {
                // Trying to read painter class directly by name
                // This will also resolve XStream class name aliases
                painterClass = mapper.realClass ( painterClassName );
            }
            catch ( final CannotResolveClassException e )
            {
                // Checking skin reference existence
                // This reference will only exist within skin parsing sequence
                final String skinClassName = ( String ) context.get ( SkinInfoConverter.SKIN_CLASS );
                final Class skinClass = ReflectUtils.getClassSafely ( skinClassName );
                if ( skinClass == null )
                {
                    final String msg = "Class '%s' for style '%s' cannot be found";
                    throw new StyleException ( String.format ( msg, painterClassName, styleId ), e );
                }

                // Trying to retrieve skin class from skin package
                final String skinPackage = skinClass.getPackage ().getName ();
                painterClassName = skinPackage + "." + painterClassName;
                painterClass = ReflectUtils.getClassSafely ( painterClassName );
                if ( painterClass == null )
                {
                    final String msg = "Class '%s' for style '%s' cannot be found";
                    throw new StyleException ( String.format ( msg, painterClassName, styleId ), e );
                }
            }
        }
        else
        {
            // Return default painter class
            painterClass = defaultPainterClass;
        }
        return painterClass;
    }
}