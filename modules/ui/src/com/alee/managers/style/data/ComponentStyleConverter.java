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
import com.alee.managers.style.SupportedComponent;
import com.alee.utils.ReflectUtils;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Custom XStream converter for ComponentStyle class.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see com.alee.managers.style.StyleManager
 * @see com.alee.managers.style.data.ComponentStyle
 */

public class ComponentStyleConverter extends ReflectionConverter
{
    /**
     * Converter constants.
     */
    public static final String STYLE_NODE = "style";
    public static final String STYLE_ID_ATTRIBUTE = "id";
    public static final String COMPONENT_TYPE_ATTRIBUTE = "type";
    public static final String EXTENDS_ID_ATTRIBUTE = "extends";
    public static final String COMPONENT_NODE = "component";
    public static final String UI_NODE = "ui";
    public static final String PAINTER_NODE = "painter";
    public static final String PAINTER_ID_ATTRIBUTE = "id";
    public static final String PAINTER_CLASS_ATTRIBUTE = "class";
    public static final String IGNORED_ATTRIBUTE = "ignored";

    /**
     * Default component style ID.
     */
    public static final String DEFAULT_STYLE_ID = "default";

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
        final String componentStyleId = componentStyle.getId ();
        writer.addAttribute ( STYLE_ID_ATTRIBUTE, componentStyleId != null ? componentStyleId : DEFAULT_STYLE_ID );

        // Style component type
        writer.addAttribute ( COMPONENT_TYPE_ATTRIBUTE, componentStyle.getType ().toString () );

        // Extended style ID
        final String extendsId = componentStyle.getExtendsId ();
        if ( extendsId != null )
        {
            writer.addAttribute ( EXTENDS_ID_ATTRIBUTE, extendsId );
        }

        // Component properties
        final Map<String, Object> componentProperties = componentStyle.getComponentProperties ();
        if ( componentProperties != null )
        {
            writer.startNode ( COMPONENT_NODE );
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
            writer.startNode ( UI_NODE );
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
                writer.startNode ( PAINTER_NODE );
                writer.addAttribute ( PAINTER_ID_ATTRIBUTE, painterStyle.getId () );
                writer.addAttribute ( PAINTER_CLASS_ATTRIBUTE, painterStyle.getPainterClass () );
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

        // Reading style ID
        final String componentStyleId = reader.getAttribute ( STYLE_ID_ATTRIBUTE );
        componentStyle.setId ( componentStyleId != null ? componentStyleId : DEFAULT_STYLE_ID );

        // Reading style component type
        final SupportedComponent type = SupportedComponent.valueOf ( reader.getAttribute ( COMPONENT_TYPE_ATTRIBUTE ) );
        componentStyle.setType ( type );

        // Reading extended style ID
        componentStyle.setExtendsId ( reader.getAttribute ( EXTENDS_ID_ATTRIBUTE ) );

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
            if ( nodeName.equals ( COMPONENT_NODE ) )
            {
                // Reading component property
                final Class componentClass = type.getComponentClass ();
                while ( reader.hasMoreChildren () )
                {
                    reader.moveDown ();
                    final String propName = reader.getNodeName ();
                    readProperty ( reader, context, componentStyleId, componentProperties, componentClass, propName );
                    reader.moveUp ();
                }
            }
            else if ( nodeName.equals ( UI_NODE ) )
            {
                // Reading UI property
                final Class uiClass = type.getUIClass ();
                while ( reader.hasMoreChildren () )
                {
                    reader.moveDown ();
                    final String propName = reader.getNodeName ();
                    readProperty ( reader, context, componentStyleId, uiProperties, uiClass, propName );
                    reader.moveUp ();
                }
            }
            else if ( nodeName.equals ( PAINTER_NODE ) )
            {
                // Collecting style IDs
                final String indicesString = reader.getAttribute ( PAINTER_ID_ATTRIBUTE );
                final List<String> indices = new ArrayList<String> ( 1 );
                if ( indicesString.contains ( "," ) )
                {
                    final StringTokenizer st = new StringTokenizer ( indicesString, ",", false );
                    while ( st.hasMoreTokens () )
                    {
                        indices.add ( st.nextToken () );
                    }
                }
                else
                {
                    indices.add ( indicesString );
                }

                // Creating separate painter styles for each style ID
                // This might be the case when the same style scheme applied to more than one painter
                final String painterClassName = reader.getAttribute ( PAINTER_CLASS_ATTRIBUTE );
                final List<PainterStyle> separateStyles = new ArrayList<PainterStyle> ( indices.size () );
                for ( final String id : indices )
                {
                    final PainterStyle painterStyle = new PainterStyle ();
                    painterStyle.setId ( id );
                    painterStyle.setPainterClass ( painterClassName );
                    separateStyles.add ( painterStyle );
                }
                context.put ( PAINTER_CLASS_ATTRIBUTE, painterClassName );

                // Reading painter style properties
                // Using LinkedHashMap to keep properties order
                final Map<String, Object> painterProperties = new LinkedHashMap<String, Object> ();
                final Class painterClass = ReflectUtils.getClassSafely ( painterClassName );
                if ( painterClass != null )
                {
                    while ( reader.hasMoreChildren () )
                    {
                        reader.moveDown ();
                        final String propName = reader.getNodeName ();
                        readProperty ( reader, context, componentStyleId, painterProperties, painterClass, propName );
                        reader.moveUp ();
                    }
                }
                for ( final PainterStyle painterStyle : separateStyles )
                {
                    painterStyle.setProperties ( painterProperties );
                }

                // Adding read painter style
                painters.addAll ( separateStyles );
            }
            reader.moveUp ();
        }

        // Marking base painter
        if ( componentStyle.getExtendsId () == null )
        {
            if ( painters.size () == 1 )
            {
                painters.get ( 0 ).setBase ( true );
            }
            else
            {
                boolean baseSet = false;
                for ( final PainterStyle painter : painters )
                {
                    if ( painter.isBase () )
                    {
                        baseSet = true;
                        break;
                    }
                }
                if ( !baseSet && painters.size () > 0 )
                {
                    painters.get ( 0 ).setBase ( true );
                }
            }
        }

        // Updating values we have just read
        componentStyle.setComponentProperties ( componentProperties );
        componentStyle.setUIProperties ( uiProperties );
        componentStyle.setPainters ( painters );

        return componentStyle;
    }

    /**
     * Parses single style property into properties map.
     *
     * @param reader           hierarchical stream reader
     * @param context          unmarshalling context
     * @param componentStyleId component style ID
     * @param properties       properties
     * @param propertyClass    property class
     * @param propertyName     property name
     */
    protected void readProperty ( final HierarchicalStreamReader reader, final UnmarshallingContext context, final String componentStyleId,
                                  final Map<String, Object> properties, final Class propertyClass, final String propertyName )
    {
        final String ignored = reader.getAttribute ( IGNORED_ATTRIBUTE );
        if ( ignored != null && Boolean.parseBoolean ( ignored ) )
        {
            properties.put ( propertyName, IgnoredValue.VALUE );
        }
        else
        {
            final Class fieldClass = ReflectUtils.getFieldTypeSafely ( propertyClass, propertyName );
            if ( fieldClass != null )
            {
                properties.put ( propertyName, context.convertAnother ( properties, fieldClass ) );
            }
            else
            {
                final Method getter = ReflectUtils.getFieldGetter ( propertyClass, propertyName );
                if ( getter != null )
                {
                    final Class<?> rClass = getter.getReturnType ();
                    properties.put ( propertyName, context.convertAnother ( properties, rClass ) );
                }
                else
                {
                    throw new StyleException ( "Component property \"" + propertyName + "\" type from style \"" + componentStyleId +
                            "\" cannot be determined! Make sure it points to existing field or getter method." );
                }
            }
        }
    }
}