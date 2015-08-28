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
import com.alee.managers.style.StyleableComponent;
import com.alee.utils.CompareUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.TextUtils;
import com.alee.utils.xml.InsetsConverter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;

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
    public static final String COMPONENT_TYPE_ATTRIBUTE = "type";
    public static final String STYLE_ID_ATTRIBUTE = "id";
    public static final String STYLE_IDS_SEPARATOR = ",";
    public static final String EXTENDS_ID_ATTRIBUTE = "extends";
    public static final String MARGIN_ATTRIBUTE = "margin";
    public static final String PADDING_ATTRIBUTE = "padding";
    public static final String COMPONENT_NODE = "component";
    public static final String COMPONENT_CLASS_ATTRIBUTE = "class";
    public static final String UI_NODE = "ui";
    public static final String PAINTER_NODE = "painter";
    public static final String PAINTER_ID_ATTRIBUTE = "id";
    public static final String PAINTER_IDS_SEPARATOR = ",";
    public static final String DEFAULT_PAINTER_ID = "painter";
    public static final String PAINTER_CLASS_ATTRIBUTE = "class";
    public static final String IGNORED_ATTRIBUTE = "ignored";

    /**
     * Context variables.
     */
    public static final String PAINTER_CLASS = "painter.class";

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
        final Map<String, Object> componentProperties = componentStyle.getComponentProperties ();
        final Map<String, Object> uiProperties = componentStyle.getUIProperties ();
        final List<PainterStyle> painters = componentStyle.getPainters ();

        // Style component type
        final StyleableComponent sc = componentStyle.getType ();
        writer.addAttribute ( COMPONENT_TYPE_ATTRIBUTE, sc.toString () );

        // Component style ID
        final String styleId = componentStyle.getId ();
        if ( styleId != null && !sc.getDefaultStyleId ().getCompleteId ().equals ( styleId ) )
        {
            writer.addAttribute ( STYLE_ID_ATTRIBUTE, styleId );
        }

        // Extended style ID
        final String extendsId = componentStyle.getExtendsId ();
        if ( extendsId != null )
        {
            writer.addAttribute ( EXTENDS_ID_ATTRIBUTE, extendsId );
        }

        // Margin and padding
        if ( uiProperties != null )
        {
            final Insets margin = ( Insets ) uiProperties.get ( MARGIN_ATTRIBUTE );
            if ( margin != null )
            {
                writer.addAttribute ( MARGIN_ATTRIBUTE, InsetsConverter.insetsToString ( margin ) );
            }
            final Insets padding = ( Insets ) uiProperties.get ( PADDING_ATTRIBUTE );
            if ( padding != null )
            {
                writer.addAttribute ( PADDING_ATTRIBUTE, InsetsConverter.insetsToString ( padding ) );
            }
        }

        // Component properties
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
        if ( uiProperties != null )
        {
            writer.startNode ( UI_NODE );
            for ( final Map.Entry<String, Object> property : uiProperties.entrySet () )
            {
                final String key = property.getKey ();
                if ( !CompareUtils.equals ( key, MARGIN_ATTRIBUTE, PADDING_ATTRIBUTE ) )
                {
                    writer.startNode ( key );
                    context.convertAnother ( property.getValue () );
                    writer.endNode ();
                }
            }
            writer.endNode ();
        }

        // Painters
        if ( painters != null )
        {
            for ( final PainterStyle painterStyle : painters )
            {
                writer.startNode ( PAINTER_NODE );

                // Writing painter ID
                // Non-default painter ID might be specified to provide multiply painters
                if ( !CompareUtils.equals ( painterStyle.getId (), DEFAULT_PAINTER_ID ) )
                {
                    writer.addAttribute ( PAINTER_ID_ATTRIBUTE, painterStyle.getId () );
                }

                // Writing painter canonical class name
                // That name is shortened if it has the same package as the skin class
                final String skinClassName = ( String ) context.get ( SkinInfoConverter.SKIN_CLASS );
                final Class skinClass = ReflectUtils.getClassSafely ( skinClassName );
                if ( skinClass == null )
                {
                    throw new StyleException ( "Specified skin class cannot be found: " + skinClassName );
                }
                final String painterClassName = painterStyle.getPainterClass ();
                final Class painterClass = ReflectUtils.getClassSafely ( painterClassName );
                if ( painterClass == null )
                {
                    throw new StyleException ( "Specified painter class cannot be found: " + painterClassName );
                }
                final String skinPackage = skinClass.getPackage ().getName ();
                final String painterPackage = painterClass.getPackage ().getName ();
                if ( skinPackage.equals ( painterPackage ) )
                {
                    writer.addAttribute ( PAINTER_CLASS_ATTRIBUTE, painterClassName.substring ( skinPackage.length () + 1 ) );
                }
                else
                {
                    writer.addAttribute ( PAINTER_CLASS_ATTRIBUTE, painterClassName );
                }

                // Writing painter properties
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
        final ComponentStyle style = new ComponentStyle ();
        final Map<String, Object> componentProperties = new LinkedHashMap<String, Object> ();
        final Map<String, Object> uiProperties = new LinkedHashMap<String, Object> ();
        final List<PainterStyle> painters = new ArrayList<PainterStyle> ();
        final List<ComponentStyle> styles = new ArrayList<ComponentStyle> ();

        // Reading style component type
        final StyleableComponent type = StyleableComponent.valueOf ( reader.getAttribute ( COMPONENT_TYPE_ATTRIBUTE ) );
        style.setType ( type );

        // Reading style ID
        final String componentStyleId = reader.getAttribute ( STYLE_ID_ATTRIBUTE );
        style.setId ( componentStyleId != null ? componentStyleId : type.getDefaultStyleId ().getCompleteId () );

        // Reading extended style ID
        final String extendsId = reader.getAttribute ( EXTENDS_ID_ATTRIBUTE );
        style.setExtendsId ( extendsId != null ? extendsId : type.getDefaultStyleId ().getCompleteId () );

        // Reading margin and padding
        final String margin = reader.getAttribute ( MARGIN_ATTRIBUTE );
        if ( margin != null )
        {
            uiProperties.put ( MARGIN_ATTRIBUTE, InsetsConverter.insetsFromString ( margin ) );
        }
        final String padding = reader.getAttribute ( PADDING_ATTRIBUTE );
        if ( padding != null )
        {
            uiProperties.put ( PADDING_ATTRIBUTE, InsetsConverter.insetsFromString ( padding ) );
        }

        // Reading component style properties and painters
        // Using LinkedHashMap to keep properties order intact
        while ( reader.hasMoreChildren () )
        {
            // Read next node
            reader.moveDown ();
            final String nodeName = reader.getNodeName ();
            if ( nodeName.equals ( COMPONENT_NODE ) )
            {
                // Reading component property
                final String componentClassName = reader.getAttribute ( COMPONENT_CLASS_ATTRIBUTE );
                final Class<? extends JComponent> cc = ReflectUtils.getClassSafely ( componentClassName );
                final Class<? extends JComponent> typeClass = type.getComponentClass ();
                if ( cc != null && !typeClass.isAssignableFrom ( cc ) )
                {
                    throw new StyleException ( "Specified custom component class \"" + cc.getCanonicalName () +
                            "\" is not assignable from the base component class \"" + typeClass.getCanonicalName () + "\"" );
                }
                final Class<? extends JComponent> componentClass = cc != null ? cc : typeClass;
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
                final String ids = reader.getAttribute ( PAINTER_ID_ATTRIBUTE );
                final boolean emptyIds = TextUtils.isEmpty ( ids );
                final List<String> indices = new ArrayList<String> ( 1 );
                if ( !emptyIds && ids.contains ( PAINTER_IDS_SEPARATOR ) )
                {
                    final StringTokenizer st = new StringTokenizer ( ids, PAINTER_IDS_SEPARATOR, false );
                    while ( st.hasMoreTokens () )
                    {
                        final String id = st.nextToken ();
                        indices.add ( TextUtils.isEmpty ( id ) ? DEFAULT_PAINTER_ID : id );
                    }
                }
                else
                {
                    indices.add ( emptyIds ? DEFAULT_PAINTER_ID : ids );
                }

                // Reading painter class name
                // It might have been shortened so we might have to check its name combined with skin package
                // That check is performed only when class cannot be found by its original path
                final String skinClassName = ( String ) context.get ( SkinInfoConverter.SKIN_CLASS );
                final Class skinClass = ReflectUtils.getClassSafely ( skinClassName );
                if ( skinClass == null )
                {
                    throw new StyleException ( "Specified skin class cannot be found: " + skinClassName );
                }
                final String providedPainterClassName = reader.getAttribute ( PAINTER_CLASS_ATTRIBUTE );
                String painterClassName = providedPainterClassName;
                Class painterClass = ReflectUtils.getClassSafely ( painterClassName );
                if ( painterClass == null )
                {
                    final String skinPackage = skinClass.getPackage ().getName ();
                    painterClassName = skinPackage + "." + painterClassName;
                    painterClass = ReflectUtils.getClassSafely ( painterClassName );
                    if ( painterClass == null )
                    {
                        throw new StyleException ( "Specified painter class cannot be found: " + providedPainterClassName );
                    }
                }

                // Creating separate painter styles for each style ID
                // This might be the case when the same style scheme applied to more than one painter
                final List<PainterStyle> separateStyles = new ArrayList<PainterStyle> ( indices.size () );
                for ( final String id : indices )
                {
                    final PainterStyle painterStyle = new PainterStyle ();
                    painterStyle.setId ( id );
                    painterStyle.setPainterClass ( painterClassName );
                    separateStyles.add ( painterStyle );
                }

                // Providing painter class to subsequent converters
                context.put ( PAINTER_CLASS, painterClassName );

                // Reading painter style properties
                // Using LinkedHashMap to keep properties order
                final Map<String, Object> painterProperties = new LinkedHashMap<String, Object> ();
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
            else if ( nodeName.equals ( STYLE_NODE ) )
            {
                // Reading another component style
                final ComponentStyle childStyle = ( ComponentStyle ) context.convertAnother ( style, ComponentStyle.class );

                // Updating parent style
                childStyle.setParent ( style );

                // Adding child style
                final String styleId = childStyle.getId ();
                if ( styleId.contains ( STYLE_IDS_SEPARATOR ) )
                {
                    // Separating if multiple styles
                    final List<String> styleIds = TextUtils.stringToList ( styleId, STYLE_IDS_SEPARATOR );
                    for ( final String id : styleIds )
                    {
                        // Filtering empty IDs
                        if ( !TextUtils.isEmpty ( id ) )
                        {
                            // Adding one of multiply child styles
                            styles.add ( childStyle.clone ().setId ( id.trim () ) );
                        }
                    }
                }
                else
                {
                    // Adding single child style
                    styles.add ( childStyle );
                }
            }
            reader.moveUp ();
        }

        // Marking base painter
        if ( style.getExtendsId () == null )
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
        style.setComponentProperties ( componentProperties );
        style.setUIProperties ( uiProperties );
        style.setPainters ( painters );
        style.setStyles ( styles );

        return style;
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
                try
                {
                    properties.put ( propertyName, context.convertAnother ( properties, fieldClass ) );
                }
                catch ( final Throwable e )
                {
                    throw new StyleException ( "Component property \"" + propertyName + "\" value from style \"" + componentStyleId +
                            "\" cannot be read" );
                }
            }
            else
            {
                final Method getter = ReflectUtils.getFieldGetter ( propertyClass, propertyName );
                if ( getter != null )
                {
                    try
                    {
                        final Class<?> rClass = getter.getReturnType ();
                        properties.put ( propertyName, context.convertAnother ( properties, rClass ) );
                    }
                    catch ( final Throwable e )
                    {
                        throw new StyleException ( "Component property \"" + propertyName + "\" value from style \"" + componentStyleId +
                                "\" cannot be read" );
                    }
                }
                else
                {
                    throw new StyleException ( "Component property \"" + propertyName + "\" type from style \"" + componentStyleId +
                            "\" cannot be determined! Make sure it points to existing field or getter method" );
                }
            }
        }
    }
}