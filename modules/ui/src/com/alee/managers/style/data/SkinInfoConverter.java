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

import com.alee.managers.icon.set.IconSet;
import com.alee.managers.style.StyleException;
import com.alee.utils.ReflectUtils;
import com.alee.utils.XmlUtils;
import com.alee.utils.xml.Resource;
import com.alee.utils.xml.XStreamContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.mapper.Mapper;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Custom XStream converter for {@link com.alee.managers.style.data.SkinInfo} class.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see com.alee.managers.style.StyleManager
 * @see com.alee.managers.style.data.SkinInfo
 */

public final class SkinInfoConverter extends ReflectionConverter
{
    /**
     * todo 1. Create proper object->xml marshalling strategy
     */

    /**
     * Context variables.
     */
    public static final String SKIN_CLASS = "skin.class";
    public static final String META_DATA_ONLY_KEY = "meta.data.only";

    /**
     * Converter constants.
     */
    public static final String ID_NODE = "id";
    public static final String CLASS_NODE = "class";
    public static final String ICON_NODE = "icon";
    public static final String TITLE_NODE = "title";
    public static final String DESCRIPTION_NODE = "description";
    public static final String AUTHOR_NODE = "author";
    public static final String SUPPORTED_SYSTEMS_NODE = "supportedSystems";
    public static final String EXTENDS_NODE = "extends";
    public static final String INCLUDE_NODE = "include";
    public static final String ICON_SET_NODE = "iconSet";
    public static final String STYLE_NODE = "style";
    public static final String NEAR_CLASS_ATTRIBUTE = "nearClass";

    /**
     * Custom resource map used by StyleEditor to link resources and modified XML files.
     * In other circumstances this map shouldn't be required and will be empty.
     */
    protected static final Map<String, Map<String, String>> resourceMap = new LinkedHashMap<String, Map<String, String>> ();

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
     * Adds custom resource that will be used to change the default resources load strategy.
     * If specified skin XML files will be taken from this map instead of being read from the actual file.
     * This was generally added to support quick styles replacement by the {@link com.alee.extended.style.StyleEditor} application.
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

    @Override
    public boolean canConvert ( final Class type )
    {
        return type.equals ( SkinInfo.class );
    }

    @Override
    public Object unmarshal ( final HierarchicalStreamReader reader, final UnmarshallingContext context )
    {
        // Skin class provided for include skin or extension skin
        final String superSkinClass = ( String ) context.get ( SKIN_CLASS );

        // Have to perform read in try-catch to properly cleanup skin class
        // Cleanup will only be performed if the skin was read by this specific method call
        try
        {
            final SkinInfo skinInfo = new SkinInfo ();

            // Updating skin class with initial value
            // It might get replaced before we get to actual usage of this value
            // That depends on whether or not skin class node is provided
            skinInfo.setSkinClass ( superSkinClass );

            // Checking unmarshalling mode
            final Object mdo = context.get ( META_DATA_ONLY_KEY );
            final boolean metaDataOnly = mdo != null && ( Boolean ) mdo;

            // Creating component style
            final List<IconSet> iconSets = new ArrayList<IconSet> ( 1 );
            final List<ComponentStyle> styles = new ArrayList<ComponentStyle> ( 10 );
            while ( reader.hasMoreChildren () )
            {
                // Read next node
                reader.moveDown ();
                final String nodeName = reader.getNodeName ();
                if ( nodeName.equals ( ID_NODE ) )
                {
                    // Reading skin unique ID
                    skinInfo.setId ( reader.getValue () );
                }
                else if ( nodeName.equals ( CLASS_NODE ) )
                {
                    // Reading skin class canonical name
                    final String skinClass = reader.getValue ();

                    // Saving skin class into skin information
                    skinInfo.setSkinClass ( skinClass );

                    // Adding skin into context, even if this is a subsequent skin
                    // Since it should be defined in the beginning of XML this value will be passed to underlying converters
                    // This way we can provide it into {@link com.alee.managers.style.data.ComponentStyleConverter}
                    context.put ( SKIN_CLASS, skinClass );
                }
                else if ( nodeName.equals ( ICON_NODE ) )
                {
                    // Reading skin icon
                    // It is always located near skin class
                    final Class<?> skinClass = ReflectUtils.getClassSafely ( skinInfo.getSkinClass () );
                    skinInfo.setIcon ( new ImageIcon ( skinClass.getResource ( reader.getValue () ) ) );
                }
                else if ( nodeName.equals ( TITLE_NODE ) )
                {
                    // Reading skin title
                    skinInfo.setTitle ( reader.getValue () );
                }
                else if ( nodeName.equals ( DESCRIPTION_NODE ) )
                {
                    // Reading skin description
                    skinInfo.setDescription ( reader.getValue () );
                }
                else if ( nodeName.equals ( AUTHOR_NODE ) )
                {
                    // Reading skin author
                    skinInfo.setAuthor ( reader.getValue () );
                }
                else if ( nodeName.equals ( SUPPORTED_SYSTEMS_NODE ) )
                {
                    // Reading OS systems supported by this skin
                    skinInfo.setSupportedSystems ( reader.getValue () );
                }
                else if ( nodeName.equals ( EXTENDS_NODE ) )
                {
                    // Reading skins supported by the extension
                    List<String> extendedSkins = skinInfo.getExtendedSkins ();
                    if ( extendedSkins == null )
                    {
                        extendedSkins = new ArrayList<String> ( 1 );
                        skinInfo.setExtendedSkins ( extendedSkins );
                    }
                    extendedSkins.add ( reader.getValue () );
                }
                else if ( nodeName.equals ( STYLE_NODE ) && !metaDataOnly )
                {
                    // Reading component style
                    styles.add ( ( ComponentStyle ) context.convertAnother ( styles, ComponentStyle.class ) );
                }
                else if ( nodeName.equals ( INCLUDE_NODE ) && !metaDataOnly )
                {
                    // Reading included skin file styles
                    final String nearClass = reader.getAttribute ( NEAR_CLASS_ATTRIBUTE );
                    final String file = reader.getValue ();
                    final Resource resource = new Resource ( nearClass, file );
                    styles.addAll ( readInclude ( skinInfo, resource ) );
                }
                else if ( nodeName.equals ( ICON_SET_NODE ) && !metaDataOnly )
                {
                    // Reading included icon set
                    final String className = reader.getValue ();
                    iconSets.add ( readIconSet ( className ) );
                }
                reader.moveUp ();
            }

            // Saving all read icon sets
            skinInfo.setIconSets ( iconSets );

            // Saving all read styles into the skin
            // At this point there might be more than one style with the same ID
            skinInfo.setStyles ( styles );

            return skinInfo;
        }
        finally
        {
            // Restoring previous skin class value
            // This will also reset the skin class back to {@code null} if this is main skin
            context.put ( SKIN_CLASS, superSkinClass );
        }
    }

    /**
     * Reading and returning included skin file styles.
     *
     * @param skinInfo skin information
     * @param resource included resourse file
     * @return included skin file styles
     */
    protected List<ComponentStyle> readInclude ( final SkinInfo skinInfo, final Resource resource )
    {
        // Replacing null relative class with skin class
        if ( resource.getClassName () == null )
        {
            final String skinClass = skinInfo.getSkinClass ();
            if ( skinClass == null )
            {
                throw new StyleException ( "Included skin file \"" + resource.getPath () +
                        "\" specified but skin \"" + CLASS_NODE + "\" is not set" );
            }
            resource.setClassName ( skinClass );
        }

        // Reading skin part from included file
        final SkinInfo include = loadSkinInfo ( skinInfo, resource );

        // Returning included styles
        return include.getStyles ();
    }

    /**
     * Returns icon set loaded using specified class name.
     *
     * @param className icon set class name
     * @return icon set loaded using specified class name
     */
    protected IconSet readIconSet ( final String className )
    {
        try
        {
            return ReflectUtils.createInstance ( className );
        }
        catch ( final Throwable e )
        {
            throw new StyleException ( "Unable to load icon set: " + className, e );
        }
    }

    /**
     * Loads SkinInfo from the specified resource file.
     * It will use an XML from a predefined resources map if it exists there.
     *
     * @param skinInfo skin information
     * @param resource XML resource file
     * @return loaded SkinInfo
     */
    protected SkinInfo loadSkinInfo ( final SkinInfo skinInfo, final Resource resource )
    {
        try
        {
            final XStreamContext context = new XStreamContext ( SKIN_CLASS, skinInfo.getSkinClass () );
            final Map<String, String> nearClassMap = resourceMap.get ( resource.getClassName () );
            if ( nearClassMap != null )
            {
                final String xml = nearClassMap.get ( resource.getPath () );
                if ( xml != null )
                {
                    return XmlUtils.fromXML ( xml, context );
                }
                else
                {
                    return XmlUtils.fromXML ( resource, context );
                }
            }
            else
            {
                return XmlUtils.fromXML ( resource, context );
            }
        }
        catch ( final Throwable e )
        {
            throw new StyleException ( "Included skin file cannot be read: " + resource.getPath (), e );
        }
    }
}