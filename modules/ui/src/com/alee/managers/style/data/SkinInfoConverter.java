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
import com.alee.utils.TextUtils;
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
     * Converter constants.
     */
    public static final String ID_NODE = "id";
    public static final String ICON_NODE = "icon";
    public static final String TITLE_NODE = "title";
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
    private static boolean subsequentSkin = false;
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
                    else if ( nodeName.equals ( ICON_NODE ) )
                    {
                        // todo
                    }
                    else if ( nodeName.equals ( TITLE_NODE ) )
                    {
                        skinInfo.setTitle ( reader.getValue () );
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
                    performOverride ( styles );

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
     * Reading and returning included skin file styles.
     *
     * @param context       unmarshalling context
     * @param wasSubsequent whether or not this skin was a subsequent one
     * @param skinInfo      sking information
     * @param resourceFile  included resourse file
     * @return included skin file styles
     */
    private List<ComponentStyle> readInclude ( final UnmarshallingContext context, final boolean wasSubsequent, final SkinInfo skinInfo,
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
                            "\" specified but skin \"" + CLASS_NODE + "\" is not set" );
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
     * Performs style override.
     *
     * @param styles styles to override
     */
    private void performOverride ( final List<ComponentStyle> styles )
    {
        for ( int i = 0; i < styles.size (); i++ )
        {
            final ComponentStyle currentStyle = styles.get ( i );
            for ( int j = i + 1; j < styles.size (); j++ )
            {
                final ComponentStyle style = styles.get ( j );
                if ( style.getType () == currentStyle.getType () && CompareUtils.equals ( style.getId (), currentStyle.getId () ) )
                {
                    styles.set ( i, currentStyle.clone ().merge ( styles.remove ( j-- ) ) );
                }
            }
        }

        for ( int i = 0; i < styles.size (); i++ )
        {
            performOverride ( styles, styles, i );
        }
    }

    /**
     * Performs style override.
     *
     * @param globalStyles all available global styles
     * @param levelStyles  current level styles
     * @param index        index of style we are overriding on current level
     */
    private void performOverride ( final List<ComponentStyle> globalStyles, final List<ComponentStyle> levelStyles, final int index )
    {
        final ComponentStyle style = levelStyles.get ( index );

        // Overriding style children first
        if ( style.getStylesCount () > 0 )
        {
            for ( int i = 0; i < style.getStylesCount (); i++ )
            {
                performOverride ( globalStyles, style.getStyles (), i );
            }
        }

        // Trying to determine style we will extend
        final StyleableComponent type = style.getType ();
        final String completeId = style.getCompleteId ();
        final String defaultStyleId = type.getDefaultStyleId ().getCompleteId ();
        ComponentStyle extendedStyle = null;

        // Searching for extended style
        // This can be a style explicitely specified in style XML as extended one or default one
        if ( !TextUtils.isEmpty ( style.getExtendsId () ) )
        {
            // Style cannot extend itself
            final String extendsId = style.getExtendsId ();
            if ( extendsId.equals ( completeId ) )
            {
                final String msg = "Component style '%s:%s' extends itself";
                throw new StyleException ( String.format ( msg, type, completeId ) );
            }

            // Extended style must exist in loaded skin
            extendedStyle = findStyle ( type, extendsId, style.getId (), levelStyles, globalStyles, index );
            if ( extendedStyle == null )
            {
                final String msg = "Component style '%s:%s' missing style '%s'";
                throw new StyleException ( String.format ( msg, type, completeId, extendsId ) );
            }
        }

        // Searching for overriden style
        // This allows us to provide default or existing styles overrides
        if ( extendedStyle == null )
        {
            // Retrieving possible style with the same ID
            // In case we find one we will use it as an extended style
            extendedStyle = findOverrideStyle ( globalStyles, style );
        }

        // Searching for default style
        // This is made to provide all initial settings properly without leaving any of those empty
        if ( extendedStyle == null && !CompareUtils.equals ( completeId, defaultStyleId ) )
        {
            // Default style must exist in loaded skin
            // Any non-default style extends default one by default even if it is not specified
            extendedStyle = findStyle ( type, defaultStyleId, style.getId (), levelStyles, globalStyles, index );
            if ( extendedStyle == null )
            {
                final String msg = "Component style '%s:%s' missing default style '%s'";
                throw new StyleException ( String.format ( msg, type, completeId, defaultStyleId ) );
            }
        }

        // Processing extended style
        // This will be either extended style, overriden style or default style
        // It might also receive {@code null} in case we are working with default style itself
        if ( extendedStyle != null )
        {
            // Creating a clone of extended style and merging it with current style
            // Result of the merge is stored within the styles list on the current level
            levelStyles.set ( index, extendedStyle.clone ().merge ( style ) );
        }
    }

    /**
     * Returns overriden style if one exists.
     *
     * @param globalStyles all available global styles
     * @param style        style to look overriden one for
     * @return overriden style if one exists
     */
    private ComponentStyle findOverrideStyle ( final List<ComponentStyle> globalStyles, final ComponentStyle style )
    {
        final List<ComponentStyle> componentStyles = new ArrayList<ComponentStyle> ();
        componentStyles.add ( style );
        while ( componentStyles.get ( 0 ).getParent () != null )
        {
            componentStyles.add ( 0, componentStyles.get ( 0 ).getParent () );
        }

        ComponentStyle oldStyle = null;
        while ( !componentStyles.isEmpty () )
        {
            final ComponentStyle currentStyle = componentStyles.remove ( 0 );
            final List<ComponentStyle> styles = oldStyle == null ? globalStyles : oldStyle.getStyles ();
            final int maxIndex = oldStyle == null ? globalStyles.indexOf ( currentStyle ) : Integer.MAX_VALUE;
            if ( ( oldStyle = findStyle ( currentStyle.getType (), currentStyle.getId (), styles, maxIndex ) ) == null &&
                    ( oldStyle = findStyle ( currentStyle.getType (), currentStyle.getExtendsId (), styles, maxIndex ) ) == null &&
                    ( oldStyle = findStyle ( currentStyle.getType (), currentStyle.getType ().toString (), styles, maxIndex ) ) == null )
            {
                break;
            }
        }

        return oldStyle;
    }

    /**
     * Gathers styles into styles cache map.
     *
     * @param styles      styles available on this level
     * @param stylesCache styles cache map
     */
    private void gatherStyles ( final List<ComponentStyle> styles, final Map<StyleableComponent, Map<String, ComponentStyle>> stylesCache )
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
    private void buildStyles ( final List<ComponentStyle> styles )
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
            buildStyle ( styles, i, building, builtStyles );
        }
    }

    /**
     * Builds style at the specified index on the level.
     * This will resolve all dependencies and overrides for the specified style.
     *
     * @param levelStyles all available level styles
     * @param index       index of style we are building on current level
     * @param building    styles which are currently being built, used to determine cyclic references
     * @param builtStyles IDs of styles which were already built
     * @return build style
     */
    private ComponentStyle buildStyle ( final List<ComponentStyle> levelStyles, final int index, final List<String> building,
                                        final Map<StyleableComponent, List<String>> builtStyles )
    {
        final ComponentStyle style = levelStyles.get ( index );

        final StyleableComponent type = style.getType ();
        final String completeId = style.getCompleteId ();
        final String uniqueId = type + ":" + completeId;

        // Avoiding cyclic references
        if ( building.contains ( uniqueId ) )
        {
            throw new StyleException ( "Style " + uniqueId + " is used within cyclic references" );
        }

        // Check whether this style was already built
        if ( builtStyles.get ( type ).contains ( completeId ) )
        {
            return style;
        }

        // Adding this style into list of styles we are building right now
        building.add ( uniqueId );

        // Resolving nested styles first
        if ( style.getStylesCount () > 0 )
        {
            for ( int i = 0; i < style.getStylesCount (); i++ )
            {
                buildStyle ( style.getStyles (), i, building, builtStyles );
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
     * @param maxIndex    max style index
     * @return component style found either on local or global level
     */
    private ComponentStyle findStyle ( final StyleableComponent type, final String id, final String excludeId,
                                       final List<ComponentStyle> levelStyles, final List<ComponentStyle> styles, final int maxIndex )
    {
        // todo Probably look on some other levels later on?
        if ( levelStyles != null && levelStyles != styles )
        {
            final ComponentStyle style = findStyle ( type, id, levelStyles, maxIndex );
            if ( style != null && !CompareUtils.equals ( style.getId (), excludeId ) )
            {
                return style;
            }
        }
        return findStyle ( type, id, styles, Integer.MAX_VALUE );
    }

    /**
     * Returns component style found in the specified styles list.
     * This method doesn't perform nested styles search for reason.
     *
     * @param type     component type
     * @param id       ID of the style to find
     * @param styles   styles list
     * @param maxIndex max style index
     * @return component style found in the specified styles list
     */
    private ComponentStyle findStyle ( final StyleableComponent type, final String id, final List<ComponentStyle> styles,
                                       final int maxIndex )
    {
        ComponentStyle fstyle = null;
        for ( int i = 0; i < styles.size () && i < maxIndex; i++ )
        {
            final ComponentStyle style = styles.get ( i );
            if ( style.getType () == type && CompareUtils.equals ( style.getId (), id ) )
            {
                fstyle = style;
            }
        }
        return fstyle;
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