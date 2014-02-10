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

import com.alee.managers.style.SupportedComponent;
import com.alee.utils.ReflectUtils;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Custom XStream converter for skin style information.
 *
 * @author Mikle Garin
 */

public class ComponentStyleConverter extends ReflectionConverter
{
    /**
     * Constructs ComponentStyleConverter with the specified mapper and reflection provider.
     *
     * @param mapper             mapper
     * @param reflectionProvider reflection provider
     */
    public ComponentStyleConverter ( final Mapper mapper, final ReflectionProvider reflectionProvider )
    {
        super ( mapper, reflectionProvider );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canConvert ( final Class type )
    {
        return type.equals ( ComponentStyle.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void marshal ( final Object source, final HierarchicalStreamWriter writer, final MarshallingContext context )
    {
        final ComponentStyle componentStyle = ( ComponentStyle ) source;

        // Component style ID
        writer.addAttribute ( "id", componentStyle.getId ().toString () );

        // Component properties
        final Map<String, Object> componentProperties = componentStyle.getComponentProperties ();
        if ( componentProperties != null )
        {
            writer.startNode ( "component" );
            for ( final Map.Entry<String, Object> property : componentProperties.entrySet () )
            {
                writer.startNode ( property.getKey () );
                context.convertAnother ( property.getValue () );
                writer.endNode ();
            }
            writer.endNode ();
        }

        // UI properties
        final Map<String, Object> uiProperties = componentStyle.getUIProperties ();
        if ( uiProperties != null )
        {
            writer.startNode ( "ui" );
            for ( final Map.Entry<String, Object> property : uiProperties.entrySet () )
            {
                writer.startNode ( property.getKey () );
                context.convertAnother ( property.getValue () );
                writer.endNode ();
            }
            writer.endNode ();
        }

        // Painters
        final List<PainterStyle> painters = componentStyle.getPainters ();
        if ( painters != null )
        {
            for ( final PainterStyle painterStyle : painters )
            {
                writer.startNode ( "painter" );
                writer.addAttribute ( "id", painterStyle.getId () );
                writer.addAttribute ( "class", painterStyle.getPainterClass () );
                for ( final Map.Entry<String, Object> property : painterStyle.getProperties ().entrySet () )
                {
                    writer.startNode ( property.getKey () );
                    context.convertAnother ( property.getValue () );
                    writer.endNode ();
                }
                writer.endNode ();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object unmarshal ( final HierarchicalStreamReader reader, final UnmarshallingContext context )
    {
        // Creating component style
        final ComponentStyle componentStyle = new ComponentStyle ();
        final SupportedComponent type = SupportedComponent.valueOf ( reader.getAttribute ( "id" ) );
        componentStyle.setId ( type );

        // Reading component style properties and painters
        // Using LinkedHashMap to keep properties order intact
        final Map<String, Object> componentProperties = new LinkedHashMap<String, Object> ();
        final Map<String, Object> uiProperties = new LinkedHashMap<String, Object> ();
        final List<PainterStyle> painters = new ArrayList<PainterStyle> ();
        while ( reader.hasMoreChildren () )
        {
            // Read next node
            reader.moveDown ();
            final String nodeName = reader.getNodeName ();
            if ( nodeName.equals ( "component" ) )
            {
                // Reading component property
                while ( reader.hasMoreChildren () )
                {
                    reader.moveDown ();
                    final Class componentClass = type.getComponentClass ();
                    final String subNodeName = reader.getNodeName ();
                    final Class fieldClass = ReflectUtils.getFieldTypeSafely ( componentClass, subNodeName );
                    if ( fieldClass != null )
                    {
                        componentProperties.put ( subNodeName, context.convertAnother ( uiProperties, fieldClass ) );
                    }
                    reader.moveUp ();
                }
            }
            else if ( nodeName.equals ( "ui" ) )
            {
                // Reading UI property
                while ( reader.hasMoreChildren () )
                {
                    reader.moveDown ();
                    final Class uiClass = type.getUIClass ();
                    final String subNodeName = reader.getNodeName ();
                    final Class fieldClass = ReflectUtils.getFieldTypeSafely ( uiClass, subNodeName );
                    if ( fieldClass != null )
                    {
                        uiProperties.put ( subNodeName, context.convertAnother ( uiProperties, fieldClass ) );
                    }
                    reader.moveUp ();
                }
            }
            else if ( nodeName.equals ( "painter" ) )
            {
                // Creating painter style
                final PainterStyle painterStyle = new PainterStyle ();
                painterStyle.setId ( reader.getAttribute ( "id" ) );
                painterStyle.setPainterClass ( reader.getAttribute ( "class" ) );

                // Reading painter style properties
                // Using LinkedHashMap to keep properties order
                final Map<String, Object> painterProperties = new LinkedHashMap<String, Object> ();
                final Class painterClass = ReflectUtils.getClassSafely ( painterStyle.getPainterClass () );
                if ( painterClass != null )
                {
                    while ( reader.hasMoreChildren () )
                    {
                        reader.moveDown ();
                        final String propName = reader.getNodeName ();
                        final Class fieldClass = ReflectUtils.getFieldTypeSafely ( painterClass, propName );
                        if ( fieldClass != null )
                        {
                            painterProperties.put ( propName, context.convertAnother ( painterProperties, fieldClass ) );
                        }
                        reader.moveUp ();
                    }
                }
                painterStyle.setProperties ( painterProperties );

                // Adding read painter style
                painters.add ( painterStyle );
            }
            reader.moveUp ();
        }

        // Updating values we have just read
        componentStyle.setComponentProperties ( componentProperties );
        componentStyle.setUIProperties ( uiProperties );
        componentStyle.setPainters ( painters );

        return componentStyle;
    }
}