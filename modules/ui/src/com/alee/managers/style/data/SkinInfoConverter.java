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
import com.alee.utils.XmlUtils;
import com.alee.utils.xml.ResourceFile;
import com.alee.utils.xml.ResourceLocation;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.mapper.Mapper;

import java.util.*;

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
     * todo 1. Create proper object->xml marshalling strategy
     */

    /**
     * Skin includes identifier mark.
     * It identifies whether or not current skin is a simple include or a standalone skin.
     * These fields used for a dirty workaround, but it works and there is no better way to provide data into subsequent (included) skins.
     */
    private static boolean subsequentSkin = false;
    private static String skinClass = null;
    private static final Object skinLock = new Object ();

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
     * Context variables.
     */
    public static final String SUBSEQUENT_SKIN = "subsequent.skin";
    public static final String SKIN_CLASS = "skin.class";

    /**
     * Custom resource map used by StyleEditor to link resources and modified XML files.
     * In other circumstances this map shouldn't be required and will be empty.
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
        synchronized ( skinLock )
        {
            // Previous skin class
            // It is also used to reset skin class to {@code null} value
            final String superSkinClass = skinClass;

            // Previous subsequent mark value
            final boolean wasSubsequent = subsequentSkin;

            // Have to perform read in try-catch to properly cleanup skin class
            // Cleanup will only be performed if the skin was read by this specific method call
            try
            {
                // Adding context value displaying whether or not this is a subsequent skin
                context.put ( SUBSEQUENT_SKIN, subsequentSkin );

                // Adding context value representing currently processed skin class
                context.put ( SKIN_CLASS, skinClass );

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
                        // Reading skin class canonical name
                        skinInfo.setSkinClass ( reader.getValue () );

                        // Adding skin into context, even if this is a subsequent skin
                        // Since it should be defined in the beginning of XML this value will be passed to underlying converters
                        // This way we can provide it into {@link com.alee.managers.style.data.ComponentStyleConverter}
                        skinClass = skinInfo.getSkinClass ();
                        context.put ( SKIN_CLASS, skinClass );
                    }
                    else if ( nodeName.equals ( STYLE_NODE ) )
                    {
                        // Reading component style
                        styles.add ( ( ComponentStyle ) context.convertAnother ( styles, ComponentStyle.class ) );
                    }
                    else if ( nodeName.equals ( INCLUDE_NODE ) )
                    {
                        // Reading included skin file styles
                        final String nearClass = reader.getAttribute ( NEAR_CLASS_ATTRIBUTE );
                        final String file = reader.getValue ();
                        final ResourceFile resourceFile = new ResourceFile ( ResourceLocation.nearClass, file, nearClass );
                        styles.addAll ( readInclude ( context, wasSubsequent, skinInfo, resourceFile ) );
                    }
                    reader.moveUp ();
                }

                // Saving all read styles into the skin
                // At this point there might be more than one style with the same ID
                skinInfo.setStyles ( styles );

                // Building final skin information if skin is not subsequent
                // This basically gathers all skins into a large cache and merges all settings appropriately
                if ( !subsequentSkin )
                {
                    // Creating cache map
                    final Map<StyleableComponent, Map<String, ComponentStyle>> stylesCache =
                            new LinkedHashMap<StyleableComponent, Map<String, ComponentStyle>> ( StyleableComponent.values ().length );

                    // Merged elements
                    buildOverride(styles);

                    // Building styles which extend some other styles
                    // We have to merge these manually once to create complete styles
                    buildStyles ( styles );

                    // Generating skin info cache
                    // Also merging all styles with the same ID
                    gatherStyles ( styles, stylesCache );

                    // Saving generated cache into skin
                    skinInfo.setStylesCache ( stylesCache );
                }

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
     * Merged styles
     *
     * @param styles styles to merge
     */
    private void buildOverride ( List<ComponentStyle> styles )
    {
        for ( int i = 0; i < styles.size (); i++ )
        {
            buildOverride ( styles, styles, i, "" );
        }
    }

    /**
     * Merged styles
     *
     * @param globalStyles all available global styles
     * @param levelStyles current level styles
     * @param index index of style we are building on current level
     */
    private void buildOverride ( final List<ComponentStyle> globalStyles, final List<ComponentStyle> levelStyles, final int index,
                                 final String tab )
    {
        ComponentStyle style = levelStyles.get ( index );

        final StyleableComponent type = style.getType ();
        final String id = style.getId ();
        final String completeId = style.getCompleteId ();
        final String uniqueId = type + ":" + completeId;

        if ( style.getStylesCount () > 0 )
        {
            for ( int i = 0; i < style.getStylesCount (); i++ )
            {
                buildOverride ( globalStyles, style.getStyles (), i, tab + "   " );
            }
        }

        final String defaultStyleId = type.getDefaultStyleId ().getCompleteId ();
        if ( !defaultStyleId.equals ( completeId ) )
        {
            System.out.println ( tab + "OVERRIDE: " + completeId + " ~ " + type );
            // Style cannot extend itself
            final String extendsId = style.getExtendsId () != null ? style.getExtendsId () : defaultStyleId;
            if ( extendsId.equals ( completeId ) )
            {
                throw new StyleException ( "Component style \"" + uniqueId + "\" extends itself" );
            }

            // Extended style cannot be found
            final ComponentStyle extendedStyle = findStyle ( type, extendsId, id, levelStyles, globalStyles );
            if ( extendedStyle == null )
            {
                throw new StyleException ( "Component style \"" + uniqueId + "\" extends missing style \"" + extendsId + "\"" );
            }

            // Creating a clone of extended style
            final ComponentStyle extendedStyleClone = extendedStyle.clone ();

            // Merging extended style with current one
            extendedStyleClone.merge ( style );

            // Saving resulting style
            style = extendedStyleClone;
            levelStyles.set ( index, style );
        }
    }

    /**
     * Reading and returning included skin file styles.
     *
     * @param context
     * @param wasSubsequent
     * @param skinInfo
     * @param resourceFile
     * @return included skin file styles
     */
    public List<ComponentStyle> readInclude ( final UnmarshallingContext context, final boolean wasSubsequent, final SkinInfo skinInfo,
                                              final ResourceFile resourceFile )
    {
        // We have to perform includes read operation in try-catch to avoid misbehavior on next read
        // This is required to properly reset static "wasSubsequent" field back to {@code false} afterwards
        try
        {
            // Marking all further skins read as subsequent
            subsequentSkin = true;
            context.put ( SUBSEQUENT_SKIN, subsequentSkin );

            // Reading included file

            // Replacing null relative class with skin class
            if ( resourceFile.getClassName () == null )
            {
                final String skinClass = skinInfo.getSkinClass ();
                if ( skinClass == null )
                {
                    throw new StyleException ( "Included skin file \"" + resourceFile.getSource () +
                            "\" specified but skin \"class\" property is not set" );
                }
                resourceFile.setClassName ( skinClass );
            }

            // Reading skin part from included file
            final SkinInfo include = loadSkinInfo ( resourceFile );

            // Returning included styles
            return include.getStyles ();
        }
        finally
        {
            // Restoring include mark
            subsequentSkin = wasSubsequent;
            context.put ( SUBSEQUENT_SKIN, subsequentSkin );
        }
    }

    /**
     * Gathering styles into styles cache map.
     *
     * @param styles      styles available on this level
     * @param stylesCache styles cache map
     */
    protected void gatherStyles ( final List<ComponentStyle> styles,
                                  final Map<StyleableComponent, Map<String, ComponentStyle>> stylesCache )
    {
        if ( styles != null )
        {
            for ( final ComponentStyle style : styles )
            {
                // Retrieving styles map for this component type
                final StyleableComponent type = style.getType ();
                Map<String, ComponentStyle> componentStyles = stylesCache.get ( type );
                if ( componentStyles == null )
                {
                    componentStyles = new LinkedHashMap<String, ComponentStyle> ( 1 );
                    stylesCache.put ( type, componentStyles );
                }

                // Adding this style into cache
                System.out.println ( style.getCompleteId () );
                componentStyles.put ( style.getCompleteId (), style );

                // Adding child styles into cache
                gatherStyles ( style.getStyles (), stylesCache );
            }
        }
    }

    /**
     * Builds specified styles.
     * This will resolve all style dependencies and overrides.
     *
     * @param styles styles to build
     */
    public void buildStyles ( final List<ComponentStyle> styles )
    {
        // Creating built styles IDs map
        final Map<StyleableComponent, List<String>> builtStyles = new HashMap<StyleableComponent, List<String>> ();
        for ( final StyleableComponent type : StyleableComponent.values () )
        {
            builtStyles.put ( type, new ArrayList<String> ( 1 ) );
        }

        // Special list that will keep only styles which are being built
        final List<String> building = new ArrayList<String> ();

        // Building provided styles into a new list
        for ( int i = 0; i < styles.size (); i++ )
        {
            buildStyle ( styles, styles, i, building, builtStyles, "" );
        }
    }

    /**
     * Builds specified style.
     * This will resolve all dependencies and overrides for the specified style.
     *
     * @param globalStyles all available global styles
     * @param levelStyles  all available level styles
     * @param index        index of style we are building on current level
     * @param building     styles which are currently being built, used to determine cyclic references
     * @param builtStyles  IDs of styles which were already built
     */
    private ComponentStyle buildStyle ( final List<ComponentStyle> globalStyles, final List<ComponentStyle> levelStyles, final int index,
                                        final List<String> building, final Map<StyleableComponent, List<String>> builtStyles,
                                        final String tab )
    {
        ComponentStyle style = levelStyles.get ( index );

        final StyleableComponent type = style.getType ();
        final String id = style.getId ();
        final String completeId = style.getCompleteId ();
        final String uniqueId = type + ":" + completeId;

        // Avoiding cyclic references
        if ( building.contains ( uniqueId ) )
        {
            throw new StyleException ( "Style " + uniqueId + " is used within cyclic references" );
        }

        // Check whether this style was already built
        // todo Replace with exact objects compare
        // todo And if IDs are the same objects should be merged into one resulting
        if ( builtStyles.get ( type ).contains ( completeId ) )
        {
            return style;
        }

        // Adding this style into list of styles we are building right now
        building.add ( uniqueId );
        System.out.println ( tab + "BUILDING: " + completeId + " ~ " + type );

        // Resolving nested styles first
        if ( style.getStylesCount () > 0 )
        {
            for ( int i = 0; i < style.getStylesCount (); i++ )
            {
                buildStyle ( globalStyles, style.getStyles (), i, building, builtStyles, tab + "   " );
            }
        }

        // Adding this styles into built list
        builtStyles.get ( type ).add ( completeId );

        // Removing this style from building list upon completion
        building.remove ( uniqueId );

        // Return completed style
        return style;
    }

    /**
     * Returns component style found either on local or global level.
     *
     * @param type        component type
     * @param id          ID of the style to find
     * @param excludeId   style ID that should be excluded on the current level
     * @param levelStyles current level styles
     * @param styles      global styles
     * @return component style found either on local or global level
     */
    private ComponentStyle findStyle ( final StyleableComponent type, final String id, final String excludeId,
                                       final List<ComponentStyle> levelStyles, final List<ComponentStyle> styles )
    {
        // todo Probably look on some other levels later on?
        if ( levelStyles != null && levelStyles != styles )
        {
            final ComponentStyle style = findStyle ( type, id, levelStyles );
            if ( style != null && !CompareUtils.equals ( style.getId (), excludeId ) )
            {
                return style;
            }
        }
        return findStyle ( type, id, styles );
    }

    /**
     * Returns component style found in the specified styles list.
     * This method doesn't perform nested styles search for reason.
     *
     * @param type   component type
     * @param id     ID of the style to find
     * @param styles styles list
     * @return component style found in the specified styles list
     */
    private ComponentStyle findStyle ( final StyleableComponent type, final String id, final List<ComponentStyle> styles )
    {
        for ( final ComponentStyle style : styles )
        {
            if ( style.getType () == type && CompareUtils.equals ( style.getId (), id ) )
            {
                return style;
            }
        }
        return null;
    }

    //    /**
    //     * Builds specified style by collecting missing style settings from extended style.
    //     * This might also cause extended style to be built.
    //     *
    //     * @param id          style ID
    //     * @param style       style
    //     * @param styles      all available styles
    //     * @param builtStyles styles which were already built
    //     */
    //    protected void buildStyle ( final String id, final ComponentStyle style, final Map<String, ComponentStyle> styles,
    //                                final List<String> builtStyles )
    //    {
    //        // Check whether this style was already built
    //        if ( builtStyles.contains ( id ) )
    //        {
    //            return;
    //        }
    //
    //        // No need to process default style
    //        final SupportedComponent sc = style.getType ();
    //        final String defaultStyleId = sc.getDefaultStyleId ().getCompleteId ();
    //        if ( defaultStyleId.equals ( id ) )
    //        {
    //            return;
    //        }
    //
    //        // Retrieving extended style ID
    //        final String extendsId = style.getExtendsId () != null ? style.getExtendsId () : defaultStyleId;
    //
    //        // Style cannot extend itself
    //        if ( extendsId.equals ( id ) )
    //        {
    //            throw new StyleException ( "Component style \"" + style.getType () + ":" + id + "\" extends itself" );
    //        }
    //
    //        // Extended style cannot be found
    //        final ComponentStyle extendedStyle = styles.get ( extendsId );
    //        if ( extendedStyle == null )
    //        {
    //            throw new StyleException (
    //                    "Component style \"" + style.getType () + ":" + id + "\" extends missing style \"" + extendsId + "\"" );
    //        }
    //
    //        // Remember that this style is already built
    //        // Doing this here to avoid cyclic dependencies throw stack overflow
    //        builtStyles.add ( id );
    //
    //        // Building extended style first
    //        // This is called to ensure that extended style is already built and contains all settings in it
    //        buildStyle ( extendedStyle.getId (), extendedStyle, styles, builtStyles );
    //
    //        // Copying settings from extended style
    //        copyProperties ( style.getComponentProperties (), extendedStyle.getComponentProperties () );
    //        copyProperties ( style.getUIProperties (), extendedStyle.getUIProperties () );
    //        copyPainters ( style, extendedStyle );
    //
    //    }

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
}