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

package com.alee.managers.style;

import com.alee.managers.style.data.ComponentStyle;
import com.alee.managers.style.data.SkinInfo;
import com.alee.managers.style.skin.WebLafSkin;
import com.alee.managers.style.skin.web.WebSkin;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SystemUtils;
import com.alee.utils.XmlUtils;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * This class manages WebLaF component styles.
 * It also manages available WebLaF skins and can install/change them in runtime.
 *
 * @author Mikle Garin
 * @see com.alee.managers.style.skin.WebLafSkin
 * @see com.alee.managers.style.data.SkinInfo
 */

public final class StyleManager
{
    /**
     * todo 1. Rename "setCustomStyle" to "setCustomPainterStyle" (and such...)
     * todo 2. Probably add "setCustomUIStyle" to override settings provided by skin
     */

    /**
     * Constant provided in the skin that supports any kind of systems.
     */
    public static final String ALL_SYSTEMS_SUPPORTED = "all";

    /**
     * Skins applied for each specific skinnable component.
     * Used to determine skinnable components, update them properly and detect their current skin.
     */
    private static final Map<JComponent, WebLafSkin> appliedSkins = new WeakHashMap<JComponent, WebLafSkin> ();

    /**
     * Custom component properties.
     * These properties may be specified for each separate component.
     */
    private static final Map<JComponent, Map<String, Object>> customComponentProperties =
            new WeakHashMap<JComponent, Map<String, Object>> ();

    /**
     * Default WebLaF skin.
     */
    private static WebLafSkin defaultSkin = null;

    /**
     * Currently applied skin.
     */
    private static WebLafSkin currentSkin = null;

    /**
     * Manager initialization mark.
     */
    private static boolean initialized = false;

    /**
     * Initializes StyleManager settings.
     */
    public static void initialize ()
    {
        if ( !initialized )
        {
            initialized = true;

            // Class aliases
            XmlUtils.processAnnotations ( SkinInfo.class );
            XmlUtils.processAnnotations ( ComponentStyle.class );

            // Applying default skin as current skin
            applyDefaultSkin ();
        }
    }

    /**
     * Returns whether the specified skin is supported or not.
     *
     * @param skin skin to check
     * @return true if the specified skin is supported, false otherwise
     */
    public static boolean isSupported ( final WebLafSkin skin )
    {
        final List<String> systems = skin != null ? skin.getSupportedSystems () : null;
        final boolean supportsAny = systems != null && systems.size () > 0;
        return supportsAny && ( systems.contains ( ALL_SYSTEMS_SUPPORTED ) || systems.contains ( SystemUtils.getShortOsName () ) );
    }

    /**
     * Performs skin support check and throws an exception if skin is not supported.
     *
     * @param skin skin to check
     */
    private static void checkSupport ( final WebLafSkin skin )
    {
        if ( !isSupported ( skin ) )
        {
            throw new RuntimeException ( "Skin \"" + skin.getName () + "\" is not supported in this system" );
        }
    }

    /**
     * Returns default skin.
     *
     * @return default skin
     */
    public static WebLafSkin getDefaultSkin ()
    {
        return defaultSkin;
    }

    /**
     * Sets default skin.
     *
     * @param skinClassName default skin class name
     * @return previous default skin
     */
    public static WebLafSkin setDefaultSkin ( final String skinClassName )
    {
        return setDefaultSkin ( ReflectUtils.getClassSafely ( skinClassName ) );
    }

    /**
     * Sets default skin.
     *
     * @param skinClass default skin class
     * @return previous default skin
     */
    public static WebLafSkin setDefaultSkin ( final Class skinClass )
    {
        return setDefaultSkin ( ( WebLafSkin ) ReflectUtils.createInstanceSafely ( skinClass ) );
    }

    /**
     * Sets default skin.
     *
     * @param skin default skin
     * @return previous default skin
     */
    public static WebLafSkin setDefaultSkin ( final WebLafSkin skin )
    {
        // Checking skin support
        checkSupport ( skin );

        // Saving new default skin
        final WebLafSkin oldSkin = StyleManager.defaultSkin;
        StyleManager.defaultSkin = skin;
        return oldSkin;
    }

    /**
     * Applies default skin to all existing skinnable components.
     * This skin will also be applied to all skinnable components created afterwards.
     *
     * @return previously applied skin
     */
    public static WebLafSkin applyDefaultSkin ()
    {
        if ( defaultSkin == null )
        {
            // Initialize default skin if none specified
            defaultSkin = new WebSkin ();
        }
        return applySkin ( defaultSkin );
    }

    /**
     * Returns currently applied skin.
     *
     * @return currently applied skin
     */
    public static WebLafSkin getCurrentSkin ()
    {
        return currentSkin;
    }

    /**
     * Applies specified skin to all existing skinnable components.
     * This skin will also be applied to all skinnable components created afterwards.
     *
     * @param skin skin to be applied
     * @return previously applied skin
     */
    public static WebLafSkin applySkin ( final WebLafSkin skin )
    {
        // Checking skin support
        checkSupport ( skin );

        // Saving previously applied skin
        final WebLafSkin previousSkin = currentSkin;

        // Updating currently applied skin
        currentSkin = skin;

        // Applying new skin to all existing skinnable components
        for ( final Map.Entry<JComponent, WebLafSkin> entry : appliedSkins.entrySet () )
        {
            final JComponent component = entry.getKey ();
            final WebLafSkin oldSkin = entry.getValue ();
            if ( oldSkin != null )
            {
                oldSkin.remove ( component );
            }
            if ( skin != null )
            {
                skin.apply ( component, customComponentProperties.get ( component ) );
            }
            entry.setValue ( skin );
        }

        return previousSkin;
    }

    /**
     * Applies current skin to the skinnable component.
     *
     * @param component component to apply skin to
     * @return previously applied skin
     */
    public static WebLafSkin applySkin ( final JComponent component )
    {
        return applySkin ( component, currentSkin );
    }

    /**
     * Applies specified skin to the skinnable component.
     *
     * @param component component to apply skin to
     * @param skin      skin to be applied
     * @return previously applied skin
     */
    public static WebLafSkin applySkin ( final JComponent component, final WebLafSkin skin )
    {
        // Checking skin support
        checkSupport ( skin );

        // Removing old skin from the component
        final WebLafSkin previousSkin = removeSkin ( component );

        // Applying new skin
        skin.apply ( component, customComponentProperties.get ( component ) );
        appliedSkins.put ( component, skin );

        return previousSkin;
    }

    /**
     * Removes skin applied to the specified component and returns it.
     *
     * @param component component to remove skin from
     * @return previously applied skin
     */
    public static WebLafSkin removeSkin ( final JComponent component )
    {
        final WebLafSkin skin = appliedSkins.get ( component );
        if ( skin != null )
        {
            skin.remove ( component );
            appliedSkins.remove ( component );
        }
        return skin;
    }

    /**
     * Sets custom style property for the specified component.
     * This tricky method uses component skin to set the specified property into component painter.
     *
     * @param component component to apply custom style property to
     * @param key       custom style property key
     * @param value     custom style property value
     * @param <T>       custom style property value type
     * @return old custom style property value
     */
    public static <T> T setCustomStyle ( final JComponent component, final String key, final T value )
    {
        // Retrieving custom properties map
        Map<String, Object> properties = customComponentProperties.get ( component );
        if ( properties == null )
        {
            properties = new HashMap<String, Object> ( 1 );
            customComponentProperties.put ( component, properties );
        }

        // Saving custom property
        final T oldValue = ( T ) properties.put ( key, value );

        // Applying custom style property if there is a skin applied to this component
        final WebLafSkin componentSkin = appliedSkins.get ( component );
        if ( componentSkin != null )
        {
            componentSkin.applyCustomStyle ( component, key, value );
        }

        return oldValue;
    }

    /**
     * Returns style property from the specified component.
     *
     * @param component component to retrieve style property from
     * @param key       style property key
     * @param <T>       style property value type
     * @return style property value
     */
    public static <T> T getStyleValue ( final JComponent component, final String key )
    {
        final WebLafSkin componentSkin = appliedSkins.get ( component );
        if ( componentSkin != null )
        {
            return componentSkin.getStyleValue ( component, key );
        }
        else
        {
            return null;
        }
    }
}