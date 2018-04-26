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
import com.alee.api.merge.Merge;
import com.alee.managers.icon.IconManager;
import com.alee.managers.icon.set.IconSet;
import com.alee.managers.style.*;
import com.alee.utils.CollectionUtils;
import com.alee.utils.TextUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.Serializable;
import java.util.*;

/**
 * Basic information about the skin and its styles.
 * Styles cache is also resolved here when style requested or an extension is added.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see com.alee.managers.style.StyleManager
 * @see com.alee.managers.style.XmlSkin
 * @see com.alee.managers.style.XmlSkinExtension
 */
@XStreamAlias ( "skin" )
@XStreamConverter ( SkinInfoConverter.class )
public final class SkinInfo implements Serializable
{
    /**
     * Unique skin ID.
     * Used to collect and manage skins within StyleManager.
     */
    private String id;

    /**
     * Skin's class canonical name.
     * Mainly used to locate included resources.
     */
    private String skinClass;

    /**
     * List of skins where extension can be applied.
     * This field should only be specified for {@link com.alee.managers.style.SkinExtension} usage cases.
     */
    private List<String> extendedSkins;

    /**
     * List of OS supported by this skin separated by "," character.
     * List of OS IDs constants can be found in SystemUtils class.
     * If skin supports all OS you can simply put "all" here.
     */
    private String supportedSystems;

    /**
     * Skin icon.
     */
    private Icon icon;

    /**
     * Skin title.
     * Might be used to display skin selection and options.
     */
    private String title;

    /**
     * Skin description.
     * You are free to put here any description you like.
     */
    private String description;

    /**
     * Skin author name.
     */
    private String author;

    /**
     * List of icon sets used by this skin.
     * These icon sets will be loaded into {@link com.alee.managers.icon.IconManager} as soon as skin is installed.
     */
    private List<IconSet> iconSets;

    /**
     * List of styles available in the skin.
     * Styles might contain various component, UI and painter settings.
     */
    private List<ComponentStyle> styles;

    /**
     * Skin styles cache map.
     * It is automatically filled-in by the {@link com.alee.managers.style.data.SkinInfoConverter} with compiled styles.
     * It is not serialized and only available and used in runtime for performance reasons.
     */
    private transient Map<String, Map<String, ComponentStyle>> stylesCache;

    /**
     * Extensions already processed by this data.
     */
    private transient Map<String, Boolean> processedExtensions;

    /**
     * Constructs new skin information.
     */
    public SkinInfo ()
    {
        super ();
    }

    /**
     * Returns skin ID.
     *
     * @return skin ID
     */
    public String getId ()
    {
        return id;
    }

    /**
     * Sets skin ID.
     *
     * @param id new skin ID
     */
    public void setId ( final String id )
    {
        this.id = id;
    }

    /**
     * Returns skin class canonical name.
     *
     * @return skin class canonical name
     */
    public String getSkinClass ()
    {
        return skinClass;
    }

    /**
     * Sets skin class canonical name.
     *
     * @param skinClass new skin class canonical name
     */
    public void setSkinClass ( final String skinClass )
    {
        this.skinClass = skinClass;
    }

    /**
     * Returns list of skins where extension can be applied.
     *
     * @return list of skins where extension can be applied
     */
    public List<String> getExtendedSkins ()
    {
        return extendedSkins;
    }

    /**
     * Sets list of skins where extension can be applied.
     *
     * @param extendedSkins list of skins where extension can be applied
     */
    public void setExtendedSkins ( final List<String> extendedSkins )
    {
        this.extendedSkins = extendedSkins;
    }

    /**
     * Returns whether or not skin with the specified ID is supported by this extension.
     *
     * @param skinId ID of the skin to process
     * @return true if skin with the specified ID is supported by this extension, false otherwise
     */
    public boolean isSupported ( final String skinId )
    {
        final List<String> extendedSkins = getExtendedSkins ();
        if ( extendedSkins == null )
        {
            throw new StyleException ( "Extension must specify which skins it extends" );
        }
        return extendedSkins.contains ( skinId );
    }

    /**
     * Returns supported systems.
     *
     * @return supported systems
     */
    public String getSupportedSystems ()
    {
        return supportedSystems;
    }

    /**
     * Sets supported systems.
     *
     * @param supportedSystems supported systems
     */
    public void setSupportedSystems ( final String supportedSystems )
    {
        this.supportedSystems = supportedSystems;
    }

    /**
     * Returns supported systems list.
     *
     * @return supported systems list
     */
    public List<String> getSupportedSystemsList ()
    {
        return TextUtils.stringToList ( supportedSystems, "," );
    }

    /**
     * Sets supported systems.
     *
     * @param supportedSystems supported systems
     */
    public void setSupportedSystemsList ( final List<String> supportedSystems )
    {
        this.supportedSystems = TextUtils.listToString ( supportedSystems, "," );
    }

    /**
     * Returns skin {@link Icon}.
     *
     * @return skin {@link Icon}
     */
    public Icon getIcon ()
    {
        return icon;
    }

    /**
     * Sets skin icon.
     *
     * @param icon new skin icon
     */
    public void setIcon ( final Icon icon )
    {
        this.icon = icon;
    }

    /**
     * Returns skin title.
     *
     * @return skin title
     */
    public String getTitle ()
    {
        final String text;
        if ( title != null )
        {
            text = title;
        }
        else
        {
            final String id = getId ();
            if ( id != null )
            {
                text = id;
            }
            else
            {
                text = "Unnamed skin";
            }
        }
        return text;
    }

    /**
     * Sets skin title.
     *
     * @param title new skin title
     */
    public void setTitle ( final String title )
    {
        this.title = title;
    }

    /**
     * Returns skin description.
     *
     * @return skin description
     */
    public String getDescription ()
    {
        return description;
    }

    /**
     * Sets skin description.
     *
     * @param description new skin description
     */
    public void setDescription ( final String description )
    {
        this.description = description;
    }

    /**
     * Returns skin author.
     *
     * @return skin author
     */
    public String getAuthor ()
    {
        return author;
    }

    /**
     * Sets skin author.
     *
     * @param author new skin author
     */
    public void setAuthor ( final String author )
    {
        this.author = author;
    }

    /**
     * Called upon this skin installation as default global skin.
     */
    public void install ()
    {
        // Installing skin extensions
        installExtensions ();

        // Installing skin icon sets
        installIconSets ( getIconSets () );
    }

    /**
     * Applies all existing extensions to this skin.
     * All compliance checks are performed within this method.
     */
    protected void installExtensions ()
    {
        for ( final SkinExtension extension : StyleManager.getExtensions () )
        {
            applyExtension ( extension );
        }
    }

    /**
     * Adds specified icon sets from {@link com.alee.managers.icon.IconManager}.
     *
     * @param iconSets icon sets to add
     */
    private void installIconSets ( final List<IconSet> iconSets )
    {
        if ( CollectionUtils.notEmpty ( iconSets ) )
        {
            for ( final IconSet iconSet : iconSets )
            {
                IconManager.addIconSet ( iconSet );
            }
        }
    }

    /**
     * Called upon this skin uninstallation from being default global skin.
     */
    public void uninstall ()
    {
        // Uninstalling skin icon sets
        uninstallIconSets ( getIconSets () );
    }

    /**
     * Removes specified icon sets from {@link com.alee.managers.icon.IconManager}.
     *
     * @param iconSets icon sets to remove
     */
    private void uninstallIconSets ( final List<IconSet> iconSets )
    {
        if ( CollectionUtils.notEmpty ( iconSets ) )
        {
            for ( final IconSet iconSet : iconSets )
            {
                IconManager.removeIconSet ( iconSet.getId () );
            }
        }
    }

    /**
     * Returns skin icon sets.
     *
     * @return skin icon sets
     */
    public List<IconSet> getIconSets ()
    {
        return iconSets;
    }

    /**
     * Sets skin icon sets.
     *
     * @param iconSets skin icon sets
     */
    public void setIconSets ( final List<IconSet> iconSets )
    {
        this.iconSets = iconSets;
    }

    /**
     * Returns skin styles.
     *
     * @return skin styles
     */
    public List<ComponentStyle> getStyles ()
    {
        return styles;
    }

    /**
     * Sets skin styles.
     *
     * @param styles new skin styles
     */
    public void setStyles ( final List<ComponentStyle> styles )
    {
        this.styles = styles;
    }

    /**
     * Returns style for the specified supported component type.
     * Custom style ID can be specified in any Web-component or Web-UI to override default component style.
     * If style for such custom ID is not found in skin descriptor then default style for that component is used.
     *
     * @param component component we are looking style for
     * @return component style
     */
    public ComponentStyle getStyle ( final JComponent component )
    {
        // Lazily initializing style cache
        ensureCacheInitialized ();

        // Searching for appropriate style
        final ComponentDescriptor descriptor = StyleManager.getDescriptor ( component );
        final Map<String, ComponentStyle> componentStyles = stylesCache.get ( descriptor.getId () );
        if ( componentStyles != null )
        {
            final String styleId = StyleId.getCompleteId ( component );
            final ComponentStyle style = componentStyles.get ( styleId );
            if ( style != null )
            {
                // We have found required style
                return style;
            }
            else
            {
                // Required style cannot be found, using default style
                final String warn = "Unable to find style '%s' for component: %s";
                LoggerFactory.getLogger ( SkinInfo.class ).warn ( String.format ( warn, styleId, component ) );

                // Trying to use default component style
                final String defaultStyleId = StyleId.getDefault ( component ).getCompleteId ();
                final ComponentStyle defaultStyle = componentStyles.get ( defaultStyleId );
                if ( defaultStyle != null )
                {
                    return defaultStyle;
                }
                else
                {
                    // Default style cannot be found, using default style
                    final String error = "Unable to find default style for ID '%s' for component: %s";
                    throw new StyleException ( String.format ( error, defaultStyleId, component ) );
                }
            }
        }
        else
        {
            // For some reason type cache doesn't exist
            final String error = "Skin '%s' doesn't have any styles for component type: %s";
            throw new StyleException ( String.format ( error, getTitle (), descriptor ) );
        }
    }

    /**
     * Performs skin cache initialization on demand.
     * This cache will contain all styles compiled into their final forms for actual usage in components.
     * It optimizes runtime routines a lot by just taking a bit more time at skin initialization.
     */
    private void ensureCacheInitialized ()
    {
        if ( stylesCache == null )
        {
            // Creating cache map
            stylesCache = new LinkedHashMap<String, Map<String, ComponentStyle>> ( StyleManager.getDescriptorsCount () );

            // Merging style overrides
            performOverride ( styles, 0 );

            // Building styles which extend some other styles
            // We have to merge these manually once to create complete styles
            buildStyles ( styles, 0 );

            // Generating skin info cache
            // Also merging all styles with the same ID
            gatherStyles ( styles, stylesCache );
        }
    }

    /**
     * Applies specified extension to the skin data.
     *
     * @param extension extension to apply
     * @return true if extension was applied successfully, false otherwise
     */
    public boolean applyExtension ( final SkinExtension extension )
    {
        // Ensure processed extensions list exists
        if ( processedExtensions == null )
        {
            processedExtensions = new HashMap<String, Boolean> ( 1 );
        }

        // Checking whether this extension was already checked before
        if ( !processedExtensions.containsKey ( extension.getId () ) )
        {
            // Checking extension support
            if ( extension.isSupported ( getId () ) )
            {
                // Checking extension type as extension application heavily depends on implementation
                // We only support {@link com.alee.managers.style.XmlSkinExtension} here due to its similar data source
                if ( extension instanceof XmlSkinExtension )
                {
                    // Lazily initializing style cache
                    ensureCacheInitialized ();

                    // Loading extension data
                    final XmlSkinExtension xmlExtension = ( XmlSkinExtension ) extension;
                    final SkinInfo extensionData = xmlExtension.getData ( getSkinClass () );

                    // Updating skin with extension data
                    updateCache ( extensionData );

                    // Saving extension application result
                    processedExtensions.put ( extension.getId (), true );
                    return true;
                }
                else
                {
                    // Saving extension application result
                    processedExtensions.put ( extension.getId (), false );
                    return false;
                }
            }
            else
            {
                // Saving extension application result
                processedExtensions.put ( extension.getId (), false );
                return false;
            }
        }
        else
        {
            // Simply return previously achieved result
            return processedExtensions.get ( extension.getId () );
        }
    }

    /**
     * Performs skin cache update with applied extension data.
     * This method doesn't relod all caches, but adds styles provided by extension into the cache.
     * It is only called once per extension ID.
     *
     * @param extension applied extension data
     */
    private void updateCache ( final SkinInfo extension )
    {
        // Merging icon sets from extension
        iconSets = Merge.basic ().merge ( iconSets, extension.getIconSets () );

        // Installing extension icon sets
        installIconSets ( extension.getIconSets () );

        // Saving extension styles starting index
        final int startIndex = styles.size ();

        // Adding all extension styles into the pool
        // Those will be used to generate new caches
        styles.addAll ( extension.styles );

        // Merging style overrides
        performOverride ( styles, startIndex );

        // Building styles which extend some other styles
        // We have to merge these manually once to create complete styles
        buildStyles ( styles, startIndex );

        // Generating skin info cache
        // Also merging all styles with the same ID
        gatherStyles ( styles.subList ( startIndex, styles.size () ), stylesCache );
    }

    /**
     * Performs style override.
     *
     * @param styles     styles to override
     * @param startIndex start index
     */
    private void performOverride ( final List<ComponentStyle> styles, final int startIndex )
    {
        for ( int i = startIndex; i < styles.size (); i++ )
        {
            ComponentStyle currentStyle = styles.get ( i );
            for ( int j = i + 1; j < styles.size (); j++ )
            {
                final ComponentStyle style = styles.get ( j );
                if ( Objects.equals ( style.getType (), currentStyle.getType () ) &&
                        Objects.equals ( style.getId (), currentStyle.getId () ) )
                {
                    currentStyle = currentStyle.clone ().merge ( style );
                    styles.set ( i, currentStyle );
                    styles.remove ( j-- );
                }
            }
        }

        for ( int i = startIndex; i < styles.size (); i++ )
        {
            performOverride ( styles, styles, i, i );
        }
    }

    /**
     * Performs style override.
     *
     * @param globalStyles all available global styles
     * @param levelStyles  current level styles
     * @param index        index of style we are overriding on current level
     * @param globalIndex  global index
     */
    private void performOverride ( final List<ComponentStyle> globalStyles, final List<ComponentStyle> levelStyles, final int index,
                                   final int globalIndex )
    {
        final ComponentStyle style = levelStyles.get ( index );

        // Overriding style children first
        if ( style.getStylesCount () > 0 )
        {
            for ( int i = 0; i < style.getStylesCount (); i++ )
            {
                performOverride ( globalStyles, style.getNestedStyles (), i, globalIndex );
            }
        }

        // Trying to determine style we will extend
        final String type = style.getType ();
        final String completeId = style.getCompleteId ();
        final ComponentDescriptor descriptor = StyleManager.getDescriptor ( type );
        final String defaultStyleId = descriptor.getDefaultStyleId ().getCompleteId ();
        ComponentStyle extendedStyle = null;

        // Searching for extended style
        // This can be a style explicitely specified in style XML as extended one or default one
        if ( !TextUtils.isEmpty ( style.getExtendsId () ) )
        {
            // Style cannot extend itself
            final String extendsId = style.getExtendsId ();
            if ( extendsId.equals ( completeId ) )
            {
                final String msg = "Style '%s' extends itself for type: %s";
                throw new StyleException ( String.format ( msg, completeId, descriptor ) );
            }

            // Extended style must exist in loaded skin
            extendedStyle = findStyle ( type, extendsId, style.getId (), levelStyles, globalStyles, index, globalIndex );
            if ( extendedStyle == null )
            {
                final String msg = "Style '%s' extends missing style '%s' for type: %s";
                throw new StyleException ( String.format ( msg, completeId, extendsId, descriptor ) );
            }
        }

        // Searching for overridden style
        // This allows us to provide default or existing styles overrides
        if ( extendedStyle == null )
        {
            // Retrieving possible style with the same ID
            // In case we find one we will use it as an extended style
            extendedStyle = findOverrideStyle ( globalStyles, style );
        }

        // Searching for default style
        // This is made to provide all initial settings properly without leaving any of those empty
        if ( extendedStyle == null && Objects.notEquals ( completeId, defaultStyleId ) )
        {
            // Default style must exist in loaded skin
            // Any non-default style extends default one by default even if it is not specified
            extendedStyle = findStyle ( type, defaultStyleId, style.getId (), levelStyles, globalStyles, index, globalIndex );
            if ( extendedStyle == null )
            {
                final String msg = "Style '%s' extends missing default style '%s' for type: %s";
                throw new StyleException ( String.format ( msg, completeId, defaultStyleId, descriptor ) );
            }
        }

        // Processing extended style
        // This will be either extended style, overridden style or default style
        // It might also receive {@code null} in case we are working with default style itself
        if ( extendedStyle != null )
        {
            // Creating a clone of extended style and merging it with current style
            // Result of the merge is stored within the styles list on the current level
            levelStyles.set ( index, extendedStyle.clone ().merge ( style ) );
        }
    }

    /**
     * Returns overridden style if one exists.
     *
     * @param globalStyles all available global styles
     * @param style        style to look overridden one for
     * @return overridden style if one exists
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
            final List<ComponentStyle> styles = oldStyle == null ? globalStyles : oldStyle.getNestedStyles ();
            final int maxIndex = oldStyle == null ? globalStyles.indexOf ( currentStyle ) : Integer.MAX_VALUE;
            if ( ( oldStyle = findStyle ( currentStyle.getType (), currentStyle.getId (), styles, maxIndex ) ) == null &&
                    ( oldStyle = findStyle ( currentStyle.getType (), currentStyle.getExtendsId (), styles, maxIndex ) ) == null &&
                    ( oldStyle = findStyle ( currentStyle.getType (), currentStyle.getType (), styles, maxIndex ) ) == null )
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
    private void gatherStyles ( final List<ComponentStyle> styles, final Map<String, Map<String, ComponentStyle>> stylesCache )
    {
        if ( styles != null )
        {
            for ( final ComponentStyle style : styles )
            {
                // Retrieving styles map for this component type
                final String type = style.getType ();
                Map<String, ComponentStyle> componentStyles = stylesCache.get ( type );
                if ( componentStyles == null )
                {
                    componentStyles = new LinkedHashMap<String, ComponentStyle> ( 1 );
                    stylesCache.put ( type, componentStyles );
                }

                // Adding this style into cache
                componentStyles.put ( style.getCompleteId (), style );

                // Adding child styles into cache
                gatherStyles ( style.getNestedStyles (), stylesCache );
            }
        }
    }

    /**
     * Builds specified styles.
     * This will resolve all style dependencies and overrides.
     *
     * @param styles     styles to build
     * @param startIndex start index
     */
    private void buildStyles ( final List<ComponentStyle> styles, final int startIndex )
    {
        // Creating built style identifiers map
        final Map<String, List<String>> builtStyles = new HashMap<String, List<String>> ();
        for ( final ComponentDescriptor descriptor : StyleManager.getDescriptors () )
        {
            builtStyles.put ( descriptor.getId (), new ArrayList<String> ( 1 ) );
        }

        // Special list that will keep only styles which are being built
        final List<String> building = new ArrayList<String> ();

        // Building provided styles into a new list
        for ( int i = startIndex; i < styles.size (); i++ )
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
                                        final Map<String, List<String>> builtStyles )
    {
        final ComponentStyle style = levelStyles.get ( index );

        final String type = style.getType ();
        final ComponentDescriptor descriptor = StyleManager.getDescriptor ( type );
        final String completeId = style.getCompleteId ();
        final String uniqueId = type + ":" + completeId;

        // Avoiding cyclic references
        if ( building.contains ( uniqueId ) )
        {
            final String msg = "Style '%s' is used within cyclic references for type: %s";
            throw new StyleException ( String.format ( msg, completeId, descriptor ) );
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
                buildStyle ( style.getNestedStyles (), i, building, builtStyles );
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
     * @param globalIndex global index
     * @return component style found either on local or global level
     */
    private ComponentStyle findStyle ( final String type, final String id, final String excludeId,
                                       final List<ComponentStyle> levelStyles, final List<ComponentStyle> styles, final int maxIndex,
                                       final int globalIndex )
    {
        // todo Probably look on some other levels later on?
        if ( levelStyles != null && levelStyles != styles )
        {
            final ComponentStyle style = findStyle ( type, id, levelStyles, maxIndex );
            if ( style != null && Objects.notEquals ( style.getId (), excludeId ) )
            {
                return style;
            }
        }
        return findStyle ( type, id, styles, globalIndex );
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
    private ComponentStyle findStyle ( final String type, final String id, final List<ComponentStyle> styles, final int maxIndex )
    {
        ComponentStyle fstyle = null;
        for ( int i = 0; i < styles.size () && i < maxIndex; i++ )
        {
            final ComponentStyle style = styles.get ( i );
            if ( Objects.equals ( style.getType (), type ) && Objects.equals ( style.getId (), id ) )
            {
                fstyle = style;
            }
        }
        return fstyle;
    }

    @Override
    public String toString ()
    {
        return getTitle ();
    }
}