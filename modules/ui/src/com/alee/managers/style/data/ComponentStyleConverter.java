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

import com.alee.api.jdk.Objects;
import com.alee.managers.style.ComponentDescriptor;
import com.alee.managers.style.StyleException;
import com.alee.managers.style.StyleManager;
import com.alee.painter.Painter;
import com.alee.utils.ReflectUtils;
import com.alee.utils.swing.InsetsUIResource;
import com.alee.utils.xml.ConverterContext;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Custom XStream converter for {@link ComponentStyle} class.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see StyleManager
 * @see ComponentStyle
 */
public final class ComponentStyleConverter extends ReflectionConverter
{
    /**
     * Converter constants.
     */
    public static final String STYLE_NODE = "style";
    public static final String COMPONENT_TYPE_ATTRIBUTE = "type";
    public static final String STYLE_ID_ATTRIBUTE = "id";
    public static final String EXTENDS_ID_ATTRIBUTE = "extends";
    public static final String MARGIN_ATTRIBUTE = "margin";
    public static final String PADDING_ATTRIBUTE = "padding";
    public static final String CLASS_ATTRIBUTE = "class";
    public static final String COMPONENT_NODE = "component";
    public static final String UI_NODE = "ui";
    public static final String PAINTER_NODE = "painter";
    public static final String OVERWRITE_ATTRIBUTE = "overwrite";

    /**
     * Context variables.
     */
    public static final String CONTEXT_COMPONENT_TYPE = "component.type";
    public static final String CONTEXT_STYLE_ID = "style.id";
    public static final String CONTEXT_COMPONENT_CLASS = "component.class";
    public static final String CONTEXT_UI_CLASS = "ui.class";
    public static final String CONTEXT_PAINTER_CLASS = "painter.class";

    /**
     * Constructs new {@link ComponentStyleConverter} with the specified {@link Mapper} and {@link ReflectionProvider}.
     *
     * @param mapper             {@link Mapper} implementation
     * @param reflectionProvider {@link ReflectionProvider} implementation
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

        // Writing component type
        writer.addAttribute ( COMPONENT_TYPE_ATTRIBUTE, componentStyle.getType () );

        // Writing component style identifier
        final String styleId = componentStyle.getId ();
        final ComponentDescriptor descriptor = StyleManager.getDescriptor ( componentStyle.getType () );
        if ( styleId != null && !descriptor.getDefaultStyleId ().getCompleteId ().equals ( styleId ) )
        {
            writer.addAttribute ( STYLE_ID_ATTRIBUTE, styleId );
        }

        // Writing extended style identifier
        final String extendsId = componentStyle.getExtendsId ();
        if ( extendsId != null )
        {
            writer.addAttribute ( EXTENDS_ID_ATTRIBUTE, extendsId );
        }

        // Writing margin and padding
        final Map<String, Object> uiProperties = componentStyle.getUIProperties ();
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

        // Writing component properties
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

        // Writing UI properties
        if ( uiProperties != null )
        {
            writer.startNode ( UI_NODE );
            for ( final Map.Entry<String, Object> property : uiProperties.entrySet () )
            {
                final String key = property.getKey ();
                if ( Objects.notEquals ( key, MARGIN_ATTRIBUTE, PADDING_ATTRIBUTE ) )
                {
                    writer.startNode ( key );
                    context.convertAnother ( property.getValue () );
                    writer.endNode ();
                }
            }
            writer.endNode ();
        }

        // Writing painter style
        final PainterStyle painterStyle = componentStyle.getPainterStyle ();
        if ( painterStyle != null )
        {
            writer.startNode ( PAINTER_NODE );

            // Writing painter canonical class name
            // That name is shortened if it has the same package as the skin class
            final String skinClassName = ( String ) context.get ( SkinInfoConverter.SKIN_CLASS );
            final Class skinClass = ReflectUtils.getClassSafely ( skinClassName );
            if ( skinClass == null )
            {
                final String msg = "Specified skin class '%s' cannot be found";
                throw new StyleException ( String.format ( msg, skinClassName ) );
            }
            final String painterClassName = painterStyle.getPainterClass ();
            final Class painterClass = ReflectUtils.getClassSafely ( painterClassName );
            if ( painterClass == null )
            {
                final String msg = "Specified painter class '%s' cannot be found";
                throw new StyleException ( String.format ( msg, painterClassName ) );
            }
            final String skinPackage = skinClass.getPackage ().getName ();
            final String painterPackage = painterClass.getPackage ().getName ();
            if ( skinPackage.equals ( painterPackage ) )
            {
                writer.addAttribute ( CLASS_ATTRIBUTE, painterClassName.substring ( skinPackage.length () + 1 ) );
            }
            else
            {
                writer.addAttribute ( CLASS_ATTRIBUTE, painterClassName );
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

    @Override
    public Object unmarshal ( final HierarchicalStreamReader reader, final UnmarshallingContext context )
    {
        // Creating new component style to read settings into
        final ComponentStyle style = new ComponentStyle ();
        final LinkedHashMap<String, Object> componentProperties = new LinkedHashMap<String, Object> ();
        final LinkedHashMap<String, Object> uiProperties = new LinkedHashMap<String, Object> ();
        final List<ComponentStyle> nestedStyles = new ArrayList<ComponentStyle> ();

        // Enabling context UIResource flag
        final ConverterContext converterContext = ConverterContext.get ();
        final boolean oldUIResource = converterContext.isUIResource ();
        converterContext.setUIResource ( true );

        // Reading style component type
        final String type = reader.getAttribute ( COMPONENT_TYPE_ATTRIBUTE );
        final ComponentDescriptor descriptor = StyleManager.getDescriptor ( type );
        style.setType ( type );

        // Saving context component type
        final String oldComponentType = ( String ) context.get ( CONTEXT_COMPONENT_TYPE );
        context.put ( CONTEXT_COMPONENT_TYPE, type );

        // Reading style ID
        final String styleId = reader.getAttribute ( STYLE_ID_ATTRIBUTE );
        style.setId ( styleId != null ? styleId : descriptor.getDefaultStyleId ().getCompleteId () );

        // Saving context style ID
        final String oldStyleId = ( String ) context.get ( CONTEXT_STYLE_ID );
        context.put ( CONTEXT_STYLE_ID, style.getId () );

        // Reading extended style ID
        style.setExtendsId ( reader.getAttribute ( EXTENDS_ID_ATTRIBUTE ) );

        // Reading margin
        final String marginAttribute = reader.getAttribute ( MARGIN_ATTRIBUTE );
        if ( marginAttribute != null )
        {
            final Insets margin = InsetsConverter.insetsFromString ( marginAttribute );
            uiProperties.put ( MARGIN_ATTRIBUTE, new InsetsUIResource ( margin.top, margin.left, margin.bottom, margin.right ) );
        }

        // Reading padding
        final String paddingAttribute = reader.getAttribute ( PADDING_ATTRIBUTE );
        if ( paddingAttribute != null )
        {
            final Insets padding = InsetsConverter.insetsFromString ( paddingAttribute );
            uiProperties.put ( PADDING_ATTRIBUTE, new InsetsUIResource ( padding.top, padding.left, padding.bottom, padding.right ) );
        }

        // Saving previously set context values
        // This is required to restore them after processing all child elements
        final Object occ = context.get ( CONTEXT_COMPONENT_CLASS );
        final Object ouic = context.get ( CONTEXT_UI_CLASS );
        final Object opc = context.get ( CONTEXT_PAINTER_CLASS );

        // Adding default component and UI class
        // This is required to avoid {@code null} references
        context.put ( CONTEXT_COMPONENT_CLASS, descriptor.getComponentClass () );
        context.put ( CONTEXT_UI_CLASS, descriptor.getUIClass () );

        // Reading component and UI properties, painter styles and child styles
        while ( reader.hasMoreChildren () )
        {
            reader.moveDown ();
            final String nodeName = reader.getNodeName ();
            if ( nodeName.equals ( COMPONENT_NODE ) )
            {
                // Reading component settings
                readComponentProperties ( style, componentProperties, reader, context, descriptor );
            }
            else if ( nodeName.equals ( UI_NODE ) )
            {
                // Reading UI settings
                readUIProperties ( style, uiProperties, reader, context, descriptor );
            }
            else if ( nodeName.equals ( PAINTER_NODE ) )
            {
                // Reading painter settings
                readPainterStyle ( style, reader, context );
            }
            else if ( nodeName.equals ( STYLE_NODE ) )
            {
                // Reading child style settings
                readNestedStyles ( style, nestedStyles, context );
            }
            else
            {
                final String msg = "Unknown style settings block '%s' provided for '%s' style";
                throw new StyleException ( String.format ( msg, nodeName, styleId ) );
            }
            reader.moveUp ();
        }
        style.setComponentProperties ( componentProperties );
        style.setUIProperties ( uiProperties );
        style.setNestedStyles ( nestedStyles );

        // Restoring context values
        context.put ( CONTEXT_COMPONENT_CLASS, occ );
        context.put ( CONTEXT_UI_CLASS, ouic );
        context.put ( CONTEXT_PAINTER_CLASS, opc );

        // Cleaning up context
        context.put ( CONTEXT_STYLE_ID, oldStyleId );
        context.put ( CONTEXT_COMPONENT_TYPE, oldComponentType );

        // Restoring context UIResource flag
        converterContext.setUIResource ( oldUIResource );

        return style;
    }

    /**
     * Reads component properties.
     *
     * @param style               {@link ComponentStyle} to read component properties for
     * @param componentProperties {@link Map} to read component properties into
     * @param reader              {@link HierarchicalStreamReader}
     * @param context             {@link UnmarshallingContext}
     * @param descriptor          {@link ComponentDescriptor}
     */
    private void readComponentProperties ( final ComponentStyle style, final Map<String, Object> componentProperties,
                                           final HierarchicalStreamReader reader, final UnmarshallingContext context,
                                           final ComponentDescriptor descriptor )
    {
        Class<? extends JComponent> componentType = descriptor.getComponentClass ();

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
                    final String custom = cc.getCanonicalName ();
                    final String basic = componentType.getCanonicalName ();
                    final String msg = "Specified custom component class '%s' for style '%s' is not assignable from base class '%s'";
                    throw new StyleException ( String.format ( msg, custom, style.getId (), basic ) );
                }
            }
            else
            {
                final String msg = "Specified custom component class %s for style %s cannot be found";
                throw new StyleException ( String.format ( msg, componentClassName, style.getId () ) );
            }
        }

        // Saving component class into context
        context.put ( CONTEXT_COMPONENT_CLASS, componentType );

        // Reading component properties based on the component class
        StyleConverterUtils.readProperties ( reader, context, mapper, componentProperties, componentType, style.getId () );
    }

    /**
     * Reads UI properties.
     *
     * @param style        {@link ComponentStyle} to read UI properties for
     * @param uiProperties {@link Map} to read UI properties into
     * @param reader       {@link HierarchicalStreamReader}
     * @param context      {@link UnmarshallingContext}
     * @param descriptor   {@link ComponentDescriptor}
     */
    private void readUIProperties ( final ComponentStyle style, final Map<String, Object> uiProperties,
                                    final HierarchicalStreamReader reader, final UnmarshallingContext context,
                                    final ComponentDescriptor descriptor )
    {
        Class<? extends ComponentUI> uiType = descriptor.getUIClass ();

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
                    final String custom = uic.getCanonicalName ();
                    final String basic = uiType.getCanonicalName ();
                    final String msg = "Specified custom UI class '%s' for style '%s' is not assignable from base UI class '%s'";
                    throw new StyleException ( String.format ( msg, custom, style.getId (), basic ) );
                }
            }
            else
            {
                final String msg = "Specified custom UI class '%s' for style '%s' cannot be found";
                throw new StyleException ( String.format ( msg, uiClassName, style.getId () ) );
            }
        }

        // Saving UI class into context
        context.put ( CONTEXT_UI_CLASS, uiType );

        // Reading UI properties based on component UI class
        StyleConverterUtils.readProperties ( reader, context, mapper, uiProperties, uiType, style.getId () );
    }

    /**
     * Reads {@link PainterStyle} for the specified {@link ComponentStyle}.
     *
     * @param style   {@link ComponentStyle} to read {@link PainterStyle} for
     * @param reader  {@link HierarchicalStreamReader}
     * @param context {@link UnmarshallingContext}
     */
    private void readPainterStyle ( final ComponentStyle style, final HierarchicalStreamReader reader,
                                    final UnmarshallingContext context )
    {
        // Retrieving overwrite policy
        final String ow = reader.getAttribute ( OVERWRITE_ATTRIBUTE );
        final boolean overwrite = Boolean.parseBoolean ( ow );

        // Retrieving default painter class based on UI class and default painter field name
        // Basically we are reading this painter as a field of the UI class here
        final Class<? extends ComponentUI> uiClass = ( Class<? extends ComponentUI> ) context.get ( CONTEXT_UI_CLASS );
        final Class<? extends Painter> defaultPainter = StyleConverterUtils.getDefaultPainter ( uiClass, PAINTER_NODE );

        // Unmarshalling painter class
        final Class<? extends Painter> painterClass =
                PainterStyleConverter.unmarshalPainterClass ( reader, context, mapper, defaultPainter, style.getId () );

        // Providing painter class to subsequent converters
        final Object opc = context.get ( CONTEXT_PAINTER_CLASS );
        context.put ( CONTEXT_PAINTER_CLASS, painterClass );

        // Creating painter style
        final PainterStyle painterStyle = new PainterStyle ();
        painterStyle.setOverwrite ( overwrite );
        painterStyle.setPainterClass ( painterClass.getCanonicalName () );

        // Reading painter style properties
        // Using LinkedHashMap to keep properties order
        final LinkedHashMap<String, Object> painterProperties = new LinkedHashMap<String, Object> ();
        StyleConverterUtils.readProperties ( reader, context, mapper, painterProperties, painterClass, style.getId () );
        painterStyle.setProperties ( painterProperties );

        // Saving painter style
        style.setPainterStyle ( painterStyle );

        // Cleaning up context
        context.put ( CONTEXT_PAINTER_CLASS, opc );
    }

    /**
     * Reads nested {@link ComponentStyle}s.
     *
     * @param style   parent {@link ComponentStyle}
     * @param styles  {@link List} to read nested {@link ComponentStyle}s into
     * @param context {@link UnmarshallingContext}
     */
    private void readNestedStyles ( final ComponentStyle style, final List<ComponentStyle> styles, final UnmarshallingContext context )
    {
        // Reading nested component style
        final ComponentStyle childStyle = ( ComponentStyle ) context.convertAnother ( style, ComponentStyle.class );

        // Saving parent style reference
        childStyle.setParent ( style );

        // Adding single child style
        styles.add ( childStyle );
    }
}