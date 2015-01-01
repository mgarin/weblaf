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

import com.alee.managers.log.Log;
import com.alee.managers.style.StyleException;
import com.alee.managers.style.StyleManager;
import com.alee.managers.style.SupportedComponent;
import com.alee.utils.MapUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.XmlUtils;
import com.alee.utils.xml.ResourceFile;
import com.alee.utils.xml.ResourceLocation;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.mapper.Mapper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Custom XStream converter for SkinInfo class.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see com.alee.managers.style.StyleManager
 * @see com.alee.managers.style.data.SkinInfo
 */

public class SkinInfoConverter extends ReflectionConverter
{
    /**
     * todo 1. When skins included one by one (not extended) dependencies cannot be matched
     * todo 2. Create proper object->xml marshalling strategy
     */

    /**
     * Converter constants.
     */
    public static final String ID_NODE = "id";
    public static final String NAME_NODE = "name";
    public static final String DESCRIPTION_NODE = "description";
    public static final String AUTHOR_NODE = "author";
    public static final String SUPPORTED_SYSTEMS_NODE = "supportedSystems";
    public static final String CLASS_NODE = "class";
    public static final String INCLUDE_NODE = "include";
    public static final String STYLE_NODE = "style";
    public static final String NEAR_CLASS_ATTRIBUTE = "nearClass";

    /**
     * Custom resource map used by StyleEditor to link resources and modified XML files.
     * In other circumstances this map shouln't be required and will be empty.
     */
    private static final Map<String, Map<String, String>> resourceMap = new LinkedHashMap<String, Map<String, String>> ();

    /**
     * Constructs SkinInfoConverter with the specified mapper and reflection provider.
     *
     * @param mapper             mapper
     * @param reflectionProvider reflection provider
     */
    public SkinInfoConverter ( final Mapper mapper, final ReflectionProvider reflectionProvider )
    {
        super ( mapper, reflectionProvider );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canConvert ( final Class type )
    {
        return type.equals ( SkinInfo.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object unmarshal ( final HierarchicalStreamReader reader, final UnmarshallingContext context )
    {
        // Creating component style
        final SkinInfo skinInfo = new SkinInfo ();
        final List<ComponentStyle> styles = new ArrayList<ComponentStyle> ();
        final List<ResourceFile> includes = new ArrayList<ResourceFile> ();
        while ( reader.hasMoreChildren () )
        {
            // Read next node
            reader.moveDown ();
            final String nodeName = reader.getNodeName ();
            if ( nodeName.equals ( ID_NODE ) )
            {
                skinInfo.setId ( reader.getValue () );
            }
            else if ( nodeName.equals ( NAME_NODE ) )
            {
                skinInfo.setName ( reader.getValue () );
            }
            else if ( nodeName.equals ( DESCRIPTION_NODE ) )
            {
                skinInfo.setDescription ( reader.getValue () );
            }
            else if ( nodeName.equals ( AUTHOR_NODE ) )
            {
                skinInfo.setAuthor ( reader.getValue () );
            }
            else if ( nodeName.equals ( SUPPORTED_SYSTEMS_NODE ) )
            {
                skinInfo.setSupportedSystems ( reader.getValue () );
            }
            else if ( nodeName.equals ( CLASS_NODE ) )
            {
                skinInfo.setSkinClass ( reader.getValue () );
            }
            else if ( nodeName.equals ( CLASS_NODE ) )
            {
                skinInfo.setSkinClass ( reader.getValue () );
            }
            else if ( nodeName.equals ( STYLE_NODE ) )
            {
                // Reading component style
                styles.add ( ( ComponentStyle ) context.convertAnother ( styles, ComponentStyle.class ) );
            }
            else if ( nodeName.equals ( INCLUDE_NODE ) )
            {
                // Reading included file information
                final String nearClass = reader.getAttribute ( NEAR_CLASS_ATTRIBUTE );
                final String file = reader.getValue ();
                includes.add ( new ResourceFile ( ResourceLocation.nearClass, file, nearClass ) );
            }
            reader.moveUp ();
        }

        // Reading all additional included files
        // This operation performed in the end when all required information is read from XML
        for ( int i = 0; i < includes.size (); i++ )
        {
            final ResourceFile resourceFile = includes.get ( i );

            // Replacing null relative class with skin class
            if ( resourceFile.getClassName () == null )
            {
                final String skinClass = skinInfo.getSkinClass ();
                if ( skinClass == null )
                {
                    throw new StyleException (
                            "Included skin file \"" + resourceFile.getSource () + "\" specified but skin \"class\" property is not set!" );
                }
                resourceFile.setClassName ( skinClass );
            }

            // Reading skin part from included file
            final SkinInfo include = loadSkinInfo ( resourceFile );
            if ( include == null )
            {
                throw new StyleException ( "Included skin file \"" + resourceFile.getSource () + "\" cannot be read!" );
            }

            // Adding information from included file
            // Included styles order is preserved to preserve styles override order
            styles.addAll ( i, include.getStyles () );
        }

        // Saving all read styles into the skin
        // At this point there might be more than one style with the same ID
        skinInfo.setStyles ( styles );

        // Generating skin info cache
        // Also merging all styles with the same ID
        final Map<SupportedComponent, Map<String, ComponentStyle>> stylesCache =
                new LinkedHashMap<SupportedComponent, Map<String, ComponentStyle>> ( SupportedComponent.values ().length );
        for ( final ComponentStyle style : styles )
        {
            final SupportedComponent type = style.getType ();
            Map<String, ComponentStyle> componentStyles = stylesCache.get ( type );
            if ( componentStyles == null )
            {
                componentStyles = new LinkedHashMap<String, ComponentStyle> ( 1 );
                stylesCache.put ( type, componentStyles );
            }
            componentStyles.put ( style.getId (), style );
        }
        skinInfo.setStylesCache ( stylesCache );

        // Compiling extending styles
        for ( final Map.Entry<SupportedComponent, Map<String, ComponentStyle>> entry : stylesCache.entrySet () )
        {
            final List<String> compiledStyles = new ArrayList<String> ();
            final Map<String, ComponentStyle> stylesById = entry.getValue ();
            for ( final Map.Entry<String, ComponentStyle> style : stylesById.entrySet () )
            {
                compileStyle ( style.getKey (), style.getValue (), stylesById, compiledStyles );
            }
        }

        return skinInfo;
    }

    /**
     * Loads SkinInfo from the specified resource file.
     * It will use an XML from a predefined resources map if it exists there.
     *
     * @param resourceFile XML resource file
     * @return loaded SkinInfo
     */
    protected SkinInfo loadSkinInfo ( final ResourceFile resourceFile )
    {
        // todo Exceptions are not caught here for some reason (probably XStream blocks this somehow)
        try
        {
            final Map<String, String> nearClassMap = resourceMap.get ( resourceFile.getClassName () );
            if ( nearClassMap != null )
            {
                final String xml = nearClassMap.get ( resourceFile.getSource () );
                if ( xml != null )
                {
                    return XmlUtils.fromXML ( xml );
                }
                else
                {
                    return XmlUtils.fromXML ( resourceFile );
                }
            }
            else
            {
                return XmlUtils.fromXML ( resourceFile );
            }
        }
        catch ( final Throwable e )
        {
            Log.error ( this, e );
            return null;
        }
    }

    /**
     * Compiles specified style by collecting missing style settings from extended style.
     * This might also cause extended style to be compiled.
     *
     * @param id             style ID
     * @param style          style
     * @param styles         all available styles
     * @param compiledStyles already compiled styles
     */
    private void compileStyle ( final String id, final ComponentStyle style, final Map<String, ComponentStyle> styles,
                                final List<String> compiledStyles )
    {
        // Check whether style extends anything
        final String extendsId = style.getExtendsId ();
        if ( extendsId == null )
        {
            return;
        }

        // Check whether this style was already compiled
        if ( compiledStyles.contains ( id ) )
        {
            return;
        }

        // Style cannot extend itself
        if ( extendsId.equals ( id ) )
        {
            throw new StyleException ( "Component style \"" + style.getType () + ":" + id + "\" extends itself!" );
        }

        // Extended style cannot be found
        final ComponentStyle extendedStyle = styles.get ( extendsId );
        if ( extendedStyle == null )
        {
            throw new StyleException (
                    "Component style \"" + style.getType () + ":" + id + "\" extends missing style \"" + extendsId + "\"!" );
        }

        // Remember that this style is already compiled
        // Doing this here to avoid cyclic dependencies throw stack overflow
        compiledStyles.add ( id );

        // Compiling extended style first
        // This is called to ensure that extended style is already compiled and contains all settings in it
        compileStyle ( extendedStyle.getId (), extendedStyle, styles, compiledStyles );

        // Copying settings from extended style
        copyProperties ( style.getComponentProperties (), extendedStyle.getComponentProperties () );
        copyProperties ( style.getUIProperties (), extendedStyle.getUIProperties () );
        copyPainters ( style, extendedStyle );
    }

    /**
     * Peforms settings copy from extended style.
     *
     * @param properties         style properties
     * @param extendedProperties extended style properties
     */
    private void copyProperties ( final Map<String, Object> properties, final Map<String, Object> extendedProperties )
    {
        for ( final Map.Entry<String, Object> property : extendedProperties.entrySet () )
        {
            final String key = property.getKey ();
            if ( !properties.containsKey ( key ) )
            {
                properties.put ( key, property.getValue () );
            }
        }
    }

    /**
     * Performs painter settings copy from extended style.
     *
     * @param style         style
     * @param extendedStyle extended style
     */
    private void copyPainters ( final ComponentStyle style, final ComponentStyle extendedStyle )
    {
        // Converting painters into maps
        final Map<String, PainterStyle> paintersMap = collectPainters ( style, style.getPainters () );
        final Map<String, PainterStyle> extendedPaintersMap = collectPainters ( extendedStyle, extendedStyle.getPainters () );

        // Copying proper painters data
        for ( final Map.Entry<String, PainterStyle> entry : extendedPaintersMap.entrySet () )
        {
            final String painterId = entry.getKey ();
            final PainterStyle extendedPainterStyle = entry.getValue ();
            if ( paintersMap.containsKey ( painterId ) )
            {
                // Copying painter properties if extended painter class type is assignable from current painter class type
                final PainterStyle painterStyle = paintersMap.get ( painterId );
                final Class painterClass = ReflectUtils.getClassSafely ( painterStyle.getPainterClass () );
                final Class extendedPainterClass = ReflectUtils.getClassSafely ( extendedPainterStyle.getPainterClass () );
                if ( painterClass == null || extendedPainterClass == null )
                {
                    final String pc = painterClass == null ? painterStyle.getPainterClass () : extendedPainterStyle.getPainterClass ();
                    final String sid = style.getType () + ":" + style.getId ();
                    throw new StyleException ( "Component style \"" + sid + "\" points to missing painter class: \"" + pc + "\"!" );
                }
                if ( ReflectUtils.isAssignable ( extendedPainterClass, painterClass ) )
                {
                    copyProperties ( painterStyle.getProperties (), extendedPainterStyle.getProperties () );
                }
            }
            else
            {
                // todo Base painters might clash as the result
                // Creating full copy of painter style
                final PainterStyle painterStyle = new PainterStyle ();
                painterStyle.setId ( extendedPainterStyle.getId () );
                painterStyle.setPainterClass ( extendedPainterStyle.getPainterClass () );
                painterStyle.setProperties ( MapUtils.copyMap ( extendedPainterStyle.getProperties () ) );
                paintersMap.put ( painterId, painterStyle );
            }
        }

        // Fixing possible base mark issues
        if ( paintersMap.size () > 0 )
        {
            boolean hasBase = false;
            for ( final Map.Entry<String, PainterStyle> entry : paintersMap.entrySet () )
            {
                final PainterStyle painterStyle = entry.getValue ();
                if ( painterStyle.isBase () )
                {
                    hasBase = true;
                    break;
                }
            }
            if ( !hasBase )
            {
                PainterStyle painterStyle = paintersMap.get ( StyleManager.DEFAULT_PAINTER_ID );
                if ( painterStyle == null )
                {
                    painterStyle = paintersMap.entrySet ().iterator ().next ().getValue ();
                }
                painterStyle.setBase ( true );
            }
        }

        // Updating painters list
        final List<PainterStyle> painters = style.getPainters ();
        painters.clear ();
        painters.addAll ( paintersMap.values () );
    }

    /**
     * Collects all specified painters into map by their IDs.
     *
     * @param style    component style
     * @param painters painters list
     * @return map of painters by their IDs
     */
    private Map<String, PainterStyle> collectPainters ( final ComponentStyle style, final List<PainterStyle> painters )
    {
        final Map<String, PainterStyle> paintersMap = new LinkedHashMap<String, PainterStyle> ( painters.size () );
        for ( final PainterStyle painter : painters )
        {
            final String painterId = painter.getId ();
            if ( paintersMap.containsKey ( painterId ) )
            {
                final String sid = style.getType () + ":" + style.getId ();
                throw new StyleException ( "Component style \"" + sid + "\" has duplicate painters for id \"" + painterId + "\"!" );
            }
            paintersMap.put ( painterId, painter );
        }
        return paintersMap;
    }

    /**
     * Adds custom resource that will be used to change the default resources load strategy.
     * To put it simple - XML will be taken from this map instead of being read from the file.
     *
     * @param nearClass class near which real XML is located
     * @param src       real XML location
     * @param xml       XML to use instead the real one
     */
    public static void addCustomResource ( final String nearClass, final String src, final String xml )
    {
        Map<String, String> nearClassMap = resourceMap.get ( nearClass );
        if ( nearClassMap == null )
        {
            nearClassMap = new LinkedHashMap<String, String> ();
            resourceMap.put ( nearClass, nearClassMap );
        }
        nearClassMap.put ( src, xml );
    }
}