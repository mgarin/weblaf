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
    public static final String STYLE_NODE = "style";
    public static final String NEAR_CLASS_ATTRIBUTE = "nearClass";

    /**
     * Skin read lock to avoid concurrent skin loading.
     * todo This should be removed in future and proper concurrent styles load should be available
     */
    private static final Object skinLock = new Object ();

    /**
     * Custom resource map used by StyleEditor to link resources and modified XML files.
     * In other circumstances this map shouldn't be required and will be empty.
     */
    private static final Map<String, Map<String, String>> resourceMap = new LinkedHashMap<String, Map<String, String>> ();

    /**
     * Skin includes identifier mark.
     * It identifies whether or not current skin is a simple include or a standalone skin.
     * These fields used for a dirty workaround, but it works and there is no better way to provide data into subsequent (included) skins.
     */
    private static String skinClass = null;

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
        synchronized ( skinLock )
        {
            // Previous skin class
            // It is also used to reset skin class to {@code null} value
            final String superSkinClass = skinClass;

            // Have to perform read in try-catch to properly cleanup skin class
            // Cleanup will only be performed if the skin was read by this specific method call
            try
            {
                // Adding context value representing currently processed skin class
                context.put ( SKIN_CLASS, skinClass );

                // Checking unmarshalling mode
                final Object mdo = context.get ( META_DATA_ONLY_KEY );
                final boolean metaDataOnly = mdo != null && ( Boolean ) mdo;

                // Creating component style
                final SkinInfo skinInfo = new SkinInfo ();
                final List<ComponentStyle> styles = new ArrayList<ComponentStyle> ();
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
                        skinInfo.setSkinClass ( reader.getValue () );

                        // Adding skin into context, even if this is a subsequent skin
                        // Since it should be defined in the beginning of XML this value will be passed to underlying converters
                        // This way we can provide it into {@link com.alee.managers.style.data.ComponentStyleConverter}
                        skinClass = skinInfo.getSkinClass ();
                        context.put ( SKIN_CLASS, skinClass );
                    }
                    else if ( nodeName.equals ( ICON_NODE ) )
                    {
                        // todo
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
                        final ResourceFile resourceFile = new ResourceFile ( ResourceLocation.nearClass, file, nearClass );
                        styles.addAll ( readInclude ( skinInfo, resourceFile ) );
                    }
                    reader.moveUp ();
                }

                // Saving all read styles into the skin
                // At this point there might be more than one style with the same ID
                skinInfo.setStyles ( styles );

                return skinInfo;
            }
            finally
            {
                // Restoring previous skin class value
                // This will also reset the skin class back to {@code null} if this is main skin
                skinClass = superSkinClass;
                context.put ( SKIN_CLASS, skinClass );
            }
        }
    }

    /**
     * Reading and returning included skin file styles.
     *
     * @param skinInfo     sking information
     * @param resourceFile included resourse file
     * @return included skin file styles
     */
    private List<ComponentStyle> readInclude ( final SkinInfo skinInfo, final ResourceFile resourceFile )
    {
        // Replacing null relative class with skin class
        if ( resourceFile.getClassName () == null )
        {
            final String skinClass = skinInfo.getSkinClass ();
            if ( skinClass == null )
            {
                throw new StyleException ( "Included skin file \"" + resourceFile.getSource () +
                        "\" specified but skin \"" + CLASS_NODE + "\" is not set" );
            }
            resourceFile.setClassName ( skinClass );
        }

        // Reading skin part from included file
        final SkinInfo include = loadSkinInfo ( resourceFile );

        // Returning included styles
        return include.getStyles ();
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
                    return XmlUtils.fromXML ( resourceFile, false );
                }
            }
            else
            {
                return XmlUtils.fromXML ( resourceFile, false );
            }
        }
        catch ( final Throwable e )
        {
            throw new StyleException ( "Included skin file \"" + resourceFile.getSource () + "\" cannot be read", e );
        }
    }
}