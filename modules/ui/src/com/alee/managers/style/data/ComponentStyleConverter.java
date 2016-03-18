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
import com.alee.painter.Painter;
import com.alee.utils.CompareUtils;
import com.alee.utils.MapUtils;
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
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Custom XStream converter for {@link com.alee.managers.style.data.ComponentStyle} class.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see com.alee.managers.style.StyleManager
 * @see com.alee.managers.style.data.ComponentStyle
 */

public final class ComponentStyleConverter extends ReflectionConverter
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
    public static final String CLASS_ATTRIBUTE = "class";
    public static final String UI_NODE = "ui";
    public static final String PAINTER_NODE = "painter";
    public static final String PAINTER_ID_ATTRIBUTE = "id";
    public static final String PAINTER_IDS_SEPARATOR = ",";
    public static final String PAINTER_CLASS_ATTRIBUTE = "class";
    public static final String DEFAULT_PAINTER_ID = "painter";

    /**
     * Context variables.
     */
    public static final String CONTEXT_COMPONENT_TYPE = "component.type";
    public static final String CONTEXT_STYLE_ID = "style.id";
    public static final String CONTEXT_COMPONENT_CLASS = "component.class";
    public static final String CONTEXT_UI_CLASS = "ui.class";
    public static final String CONTEXT_PAINTER_CLASS = "painter.class";

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

    @Override
    public boolean canConvert ( final Class type )
    {
        return type.equals ( ComponentStyle.class );
    }

    @Override
    public void marshal ( final Object source, final HierarchicalStreamWriter writer, final MarshallingContext context )
    {
        final ComponentStyle componentStyle = ( ComponentStyle ) source;
        final Map<String, Object> componentProperties = componentStyle.getComponentProperties ();
        final Map<String, Object> uiProperties = componentStyle.getUIProperties ();
        final List<PainterStyle> painters = componentStyle.getPainters ();

        // Style component type
        final StyleableComponent type = componentStyle.getType ();
        writer.addAttribute ( COMPONENT_TYPE_ATTRIBUTE, type.toString () );

        // Component style ID
        final String styleId = componentStyle.getId ();
        if ( styleId != null && !type.getDefaultStyleId ().getCompleteId ().equals ( styleId ) )
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

    @Override
    public Object unmarshal ( final HierarchicalStreamReader reader, final UnmarshallingContext context )
    {
        // Creating component style and property/styles maps
        // Using LinkedHashMap to keep property orders intact
        final ComponentStyle style = new ComponentStyle ();
        final Map<String, Object> componentProperties = new LinkedHashMap<String, Object> ();
        final Map<String, Object> uiProperties = new LinkedHashMap<String, Object> ();
        final List<PainterStyle> painters = new ArrayList<PainterStyle> ();
        final List<ComponentStyle> styles = new ArrayList<ComponentStyle> ();

        // Reading style component type
        final String sct = reader.getAttribute ( COMPONENT_TYPE_ATTRIBUTE );
        final StyleableComponent type = StyleableComponent.valueOf ( sct );
        if ( type == null )
        {
            throw new StyleException ( "Styleable component type was not specified or cannot be resolved: " + sct );
        }
        style.setType ( type );
        final StyleableComponent oldComponentType = ( StyleableComponent ) context.get ( CONTEXT_COMPONENT_TYPE );
        context.put ( CONTEXT_COMPONENT_TYPE, type );

        // Reading style ID
        final String styleId = reader.getAttribute ( STYLE_ID_ATTRIBUTE );
        style.setId ( styleId != null ? styleId : type.getDefaultStyleId ().getCompleteId () );
        final String oldStyleId = ( String ) context.get ( CONTEXT_STYLE_ID );
        context.put ( CONTEXT_STYLE_ID, styleId );

        // Reading extended style ID
        style.setExtendsId ( reader.getAttribute ( EXTENDS_ID_ATTRIBUTE ) );

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

        // Saving previously set context values
        // This is required to restore them after processing all child elements
        final Object occ = context.get ( CONTEXT_COMPONENT_CLASS );
        final Object ouic = context.get ( CONTEXT_UI_CLASS );
        final Object opc = context.get ( CONTEXT_PAINTER_CLASS );

        // Adding default component and UI class
        // This is required to avoid {@code null} references
        context.put ( CONTEXT_COMPONENT_CLASS, type.getComponentClass () );
        context.put ( CONTEXT_UI_CLASS, type.getUIClass () );

        // Reading component and UI properties, painter styles and child styles
        while ( reader.hasMoreChildren () )
        {
            reader.moveDown ();
            final String nodeName = reader.getNodeName ();
            if ( nodeName.equals ( COMPONENT_NODE ) )
            {
                // Reading component settings
                readComponentProperties ( reader, context, componentProperties, type, styleId );
            }
            else if ( nodeName.equals ( UI_NODE ) )
            {
                // Reading UI settings
                readUIProperties ( reader, context, uiProperties, type, styleId );
            }
            else if ( nodeName.equals ( PAINTER_NODE ) )
            {
                // Reading painter settings
                readPainterStyles ( reader, context, painters, type, styleId );
            }
            else if ( nodeName.equals ( STYLE_NODE ) )
            {
                // Reading child style settings
                readChildStyle ( context, style, styles );
            }
            else
            {
                throw new StyleException ( "Unknown \"" + nodeName + "\" style settings block provided for \"" + styleId + "\" style" );
            }
            reader.moveUp ();
        }

        // Restoring context values
        context.put ( CONTEXT_COMPONENT_CLASS, occ );
        context.put ( CONTEXT_UI_CLASS, ouic );
        context.put ( CONTEXT_PAINTER_CLASS, opc );

        // Resolving base painter
        if ( style.getExtendsId () == null )
        {
            if ( painters.size () == 1 )
            {
                // Marking the only available painter as base
                painters.get ( 0 ).setBase ( true );
            }
            else
            {
                // Searching for base painter mark
                boolean baseSet = false;
                for ( final PainterStyle painter : painters )
                {
                    if ( painter.isBase () )
                    {
                        baseSet = true;
                        break;
                    }
                }

                // Simply assigning first available painter as base if none found
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

        // Cleaning up context
        context.put ( CONTEXT_STYLE_ID, oldStyleId );
        context.put ( CONTEXT_COMPONENT_TYPE, oldComponentType );

        return style;
    }

    /**
     * @param reader     {@link com.thoughtworks.xstream.io.HierarchicalStreamReader}
     * @param context    {@link com.thoughtworks.xstream.converters.UnmarshallingContext}
     * @param properties map to read properties into
     * @param type       styleable component type to read properties for
     * @param styleId    component style ID, might be used to report problems
     */
    protected void readComponentProperties ( final HierarchicalStreamReader reader,
                                                                    final UnmarshallingContext context,
                                                                    final Map<String, Object> properties, final StyleableComponent type,
                                                                    final String styleId )
    {
        Class<? extends JComponent> componentType = type.getComponentClass ();

        // Reading component class property
        // It might be specified explicitly to allow specifying additional parameters from the custom component class
        final String componentClassName = reader.getAttribute ( CLASS_ATTRIBUTE );
        if ( componentClassName != null )
        {
            final Class<? extends JComponent> cc = ReflectUtils.getClassSafely ( componentClassName );
            if ( cc != null )
            {
                if ( componentType.isAssignableFrom ( cc ) )
                {
                    componentType = cc;
                }
                else
                {
                    throw new StyleException ( "Specified custom component class \"" + cc.getCanonicalName () +
                            "\" for style \"" + styleId + "\" is not assignable from base component class \"" +
                            componentType.getCanonicalName () + "\"" );
                }
            }
            else
            {
                throw new StyleException (
                        "Specified custom component class \"" + componentClassName + "\" for style \"" + styleId + "\" cannot be found" );
            }
        }

        // Saving component class into context
        context.put ( CONTEXT_COMPONENT_CLASS, componentType );

        // Reading component properties based on the component class
        StyleConverterUtils.readProperties ( reader, context, properties, componentType, styleId );
    }

    /**
     * @param reader     {@link com.thoughtworks.xstream.io.HierarchicalStreamReader}
     * @param context    {@link com.thoughtworks.xstream.converters.UnmarshallingContext}
     * @param properties map to read properties into
     * @param type       styleable component type to read properties for
     * @param styleId    component style ID, might be used to report problems
     */
    protected void readUIProperties ( final HierarchicalStreamReader reader, final UnmarshallingContext context,
                                                              final Map<String, Object> properties, final StyleableComponent type,
                                                              final String styleId )
    {
        Class<? extends ComponentUI> uiType = type.getUIClass ();

        // Reading UI class property
        // It might be specified explicitly to allow specifying additional parameters from the custom UI class
        final String uiClassName = reader.getAttribute ( CLASS_ATTRIBUTE );
        if ( uiClassName != null )
        {
            final Class<? extends ComponentUI> uic = ReflectUtils.getClassSafely ( uiClassName );
            if ( uic != null )
            {
                if ( uiType.isAssignableFrom ( uic ) )
                {
                    uiType = uic;
                }
                else
                {
                    throw new StyleException ( "Specified custom UI class \"" + uic.getCanonicalName () + "\" for style \"" + styleId +
                            "\" is not assignable from base UI class \"" + uiType.getCanonicalName () + "\"" );
                }
            }
            else
            {
                throw new StyleException (
                        "Specified custom UI class \"" + uiClassName + "\" for style \"" + styleId + "\" cannot be found" );
            }
        }

        // Saving UI class into context
        context.put ( CONTEXT_UI_CLASS, uiType );

        // Reading UI properties based on component UI class
        StyleConverterUtils.readProperties ( reader, context, properties, uiType, styleId );
    }

    /**
     * @param reader   {@link com.thoughtworks.xstream.io.HierarchicalStreamReader}
     * @param context  {@link com.thoughtworks.xstream.converters.UnmarshallingContext}
     * @param painters list to add painter styles into
     * @param type     styleable component type to read painter for
     * @param styleId  component style ID, might be used to report problems
     */
    protected void readPainterStyles ( final HierarchicalStreamReader reader, final UnmarshallingContext context,
                                                           final List<PainterStyle> painters, final StyleableComponent type,
                                                           final String styleId )
    {
        // Collecting style IDs
        // This part is unique to {@link com.alee.managers.style.data.ComponentStyleConverter}
        // {@link com.alee.managers.style.data.PainterConverter} does not do this as it always knows where painter will be used
        // todo Remove this block as deprecated, UIs now only contain single painter but each painter can have many others inside
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

        // Retrieving default painter class based on UI class and default painter field name
        // Basically we are reading this painter as a field of the UI class here
        final Class<? extends ComponentUI> uiClass = ( Class<? extends ComponentUI> ) context.get ( CONTEXT_UI_CLASS );
        final Class<? extends Painter> defaultPainter = StyleConverterUtils.getDefaultPainter ( uiClass, PAINTER_NODE );
        // System.out.println ( defaultPainter + " -> " + ReflectUtils.getClassName ( uiClass ) );

        // Unmarshalling painter class
        final Class<? extends Painter> painterClass =
                PainterStyleConverter.unmarshalPainterClass ( reader, context, defaultPainter, styleId );

        // Providing painter class to subsequent converters
        final Object opc = context.get ( CONTEXT_PAINTER_CLASS );
        context.put ( CONTEXT_PAINTER_CLASS, painterClass );

        // Creating separate painter styles for each style ID
        // This might be the case when the same style scheme applied to more than one painter
        final List<PainterStyle> separateStyles = new ArrayList<PainterStyle> ( indices.size () );
        for ( final String id : indices )
        {
            final PainterStyle painterStyle = new PainterStyle ();
            painterStyle.setId ( id );
            painterStyle.setPainterClass ( painterClass.getCanonicalName () );
            separateStyles.add ( painterStyle );
        }

        // Reading painter style properties
        // Using LinkedHashMap to keep properties order
        final LinkedHashMap<String, Object> painterProperties = new LinkedHashMap<String, Object> ();
        StyleConverterUtils.readProperties ( reader, context, painterProperties, painterClass, styleId );

        // Applying painter properties to each separate painter style
        for ( final PainterStyle painterStyle : separateStyles )
        {
            // Copying properties to avoid issues across different painters
            painterStyle.setProperties ( MapUtils.copyLinkedHashMap ( painterProperties ) );
        }

        // Adding read painter style
        painters.addAll ( separateStyles );

        // Cleaning up context
        context.put ( CONTEXT_PAINTER_CLASS, opc );
    }

    /**
     * @param context {@link com.thoughtworks.xstream.converters.UnmarshallingContext}
     * @param style   parent component style
     * @param styles  list to add component styles into
     */
    protected void readChildStyle ( final UnmarshallingContext context, final ComponentStyle style, final List<ComponentStyle> styles )
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
                    // Cloning child style to avoid issues and updating its ID
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
}