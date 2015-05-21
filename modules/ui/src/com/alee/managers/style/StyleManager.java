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

import com.alee.extended.painter.Painter;
import com.alee.managers.style.data.ComponentStyle;
import com.alee.managers.style.data.SkinInfo;
import com.alee.managers.style.skin.WebLafSkin;
import com.alee.managers.style.skin.web.WebSkin;
import com.alee.utils.LafUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.XmlUtils;
import com.alee.utils.laf.Styleable;
import com.alee.utils.ninepatch.NinePatchIcon;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * This class manages WebLaF component styles.
 * It also manages available WebLaF skins and can install/change them in runtime.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see com.alee.managers.style.skin.WebLafSkin
 * @see com.alee.managers.style.data.SkinInfo
 */

public class StyleManager
{
    /**
     * todo 1. Probably add "setCustomUIStyle" to override settings provided by skin
     */

    /**
     * Default painter ID.
     */
    public static final String DEFAULT_PAINTER_ID = "painter";

    /**
     * Static class aliases processing.
     * This allows StyleManager usage without its initialization.
     */
    static
    {
        // Class aliases
        XmlUtils.processAnnotations ( SkinInfo.class );
        XmlUtils.processAnnotations ( ComponentStyle.class );
        XmlUtils.processAnnotations ( NinePatchIcon.class );
    }

    /**
     * Skins applied for each specific skinnable component.
     * Used to determine skinnable components, update them properly and detect their current skin.
     * Map structure: JComponent -> WebLafSkin
     */
    protected static final Map<JComponent, WebLafSkin> appliedSkins = new WeakHashMap<JComponent, WebLafSkin> ();

    /**
     * Custom painter properties.
     * These properties may be specified for each separate component.
     * Map structure: JComponent -> painterId -> propertyName -> propertyValue
     */
    protected static final Map<JComponent, Map<String, Map<String, Object>>> customPainterProperties =
            new WeakHashMap<JComponent, Map<String, Map<String, Object>>> ();

    /**
     * Custom component painters.
     * These are the painters set from the code and they replace default painters provided by skin.
     * Map structure: JComponent -> Painter ID -> Painter
     */
    protected static final Map<JComponent, Map<String, Painter>> customPainters = new WeakHashMap<JComponent, Map<String, Painter>> ();

    /**
     * Default WebLaF skin.
     * Skin used by default when no other skins provided.
     * This skin can be set before WebLaF initialization to avoid unnecessary UI updates afterwards.
     */
    protected static WebLafSkin defaultSkin = null;

    /**
     * Currently used skin.
     * This skin is applied to all newly created components styled by WebLaF except customized ones.
     */
    protected static WebLafSkin currentSkin = null;

    /**
     * Whether strict style checks are enabled or not.
     * <p/>
     * In case strick checks are enabled any incorrect properties or painters getter and setter calls will cause exceptions.
     * These exceptions will not cause UI to halt but they will properly inform about missing styles, incorrect settings etc.
     * <p/>
     * It is highly recommended to keep this property enabled to see and fix all problems right away.
     */
    protected static boolean strictStyleChecks = true;

    /**
     * Manager initialization mark.
     */
    protected static boolean initialized = false;

    /**
     * Initializes StyleManager settings.
     */
    public static synchronized void initialize ()
    {
        if ( !initialized )
        {
            initialized = true;

            // Applying default skin as current skin
            applyDefaultSkin ();
        }
    }

    /**
     * Returns whether strict style checks are enabled or not.
     *
     * @return true if strict style checks are enabled, false otherwise
     */
    public static boolean isStrictStyleChecks ()
    {
        return strictStyleChecks;
    }

    /**
     * Sets whether strict style checks are enabled or not.
     *
     * @param strict whether strict style checks are enabled or not
     */
    public static void setStrictStyleChecks ( final boolean strict )
    {
        StyleManager.strictStyleChecks = strict;
    }

    /**
     * Performs skin support check and throws an exception if skin is not supported.
     *
     * @param skin skin to check
     */
    protected static void checkSupport ( final WebLafSkin skin )
    {
        if ( skin == null )
        {
            throw new StyleException ( "Empty skin provided" );
        }
        if ( !skin.isSupported () )
        {
            throw new StyleException ( "Skin \"" + skin.getName () + "\" is not supported in this system" );
        }
    }

    /**
     * Returns default skin.
     *
     * @return default skin
     */
    public static WebLafSkin getDefaultSkin ()
    {
        if ( defaultSkin == null )
        {
            setDefaultSkin ( new WebSkin () );
        }
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
        return setDefaultSkin ( createSkin ( skinClass ) );
    }

    /**
     * Returns newly created skin class instance.
     *
     * @param skinClass skin class
     * @return newly created skin class instance
     */
    public static WebLafSkin createSkin ( final Class skinClass )
    {
        try
        {
            return ( WebLafSkin ) ReflectUtils.createInstance ( skinClass );
        }
        catch ( final Throwable e )
        {
            throw new StyleException ( "Unable to initialize skin from its class", e );
        }
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
        return applySkin ( getDefaultSkin () );
    }

    /**
     * Returns currently applied skin.
     *
     * @return currently applied skin
     */
    public static WebLafSkin getCurrentSkin ()
    {
        return currentSkin != null ? currentSkin : getDefaultSkin ();
    }

    /**
     * Applies specified skin to all existing skinnable components.
     * This skin will also be applied to all skinnable components created afterwards.
     *
     * @param skinClassName class name of the skin to be applied
     * @return previously applied skin
     */
    public static WebLafSkin applySkin ( final String skinClassName )
    {
        return applySkin ( ReflectUtils.getClassSafely ( skinClassName ) );
    }

    /**
     * Applies specified skin to all existing skinnable components.
     * This skin will also be applied to all skinnable components created afterwards.
     *
     * @param skinClass class of the skin to be applied
     * @return previously applied skin
     */
    public static WebLafSkin applySkin ( final Class skinClass )
    {
        return applySkin ( createSkin ( skinClass ) );
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
                oldSkin.removeSkin ( component );
            }
            if ( skin != null )
            {
                skin.applySkin ( component );
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
        return applySkin ( component, getCurrentSkin () );
    }

    /**
     * Applies specified skin to the skinnable component.
     * todo This won't change component's styleId, add some fix?
     * todo Do not replace custom applied skins when current skin changed?
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
        skin.applySkin ( component );
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
            skin.removeSkin ( component );
            appliedSkins.remove ( component );
        }
        return skin;
    }

    /**
     * Returns painter property value from the specified component.
     * Specified property is searched only inside the base painter so far.
     *
     * @param component component to retrieve style property from
     * @param key       style property key
     * @param <T>       style property value type
     * @return style property value
     */
    public static <T> T getPainterPropertyValue ( final JComponent component, final String key )
    {
        return getPainterPropertyValue ( component, null, key );
    }

    /**
     * Returns painter property value from the specified component.
     * Specified property is searched only inside the base painter so far.
     *
     * @param component component to retrieve style property from
     * @param painterId painter ID
     * @param key       style property key
     * @param <T>       style property value type
     * @return style property value
     */
    public static <T> T getPainterPropertyValue ( final JComponent component, final String painterId, final String key )
    {
        final WebLafSkin skin = appliedSkins.get ( component );
        return skin != null ? ( T ) skin.getPainterPropertyValue ( component, painterId, key ) : null;
    }

    /**
     * Sets custom value for painter property for the specified component.
     * This tricky method uses component skin to set the specified property into component painter.
     *
     * @param component component to apply custom style property to
     * @param key       custom style property key
     * @param value     custom style property value
     * @param <T>       custom style property value type
     * @return old custom style property value
     */
    public static <T> T setCustomPainterProperty ( final JComponent component, final String key, final T value )
    {
        return setCustomPainterProperty ( component, null, key, value );
    }

    /**
     * Sets custom value for painter property for the specified component.
     * This tricky method uses component skin to set the specified property into component painter.
     *
     * @param component component to apply custom style property to
     * @param painterId painter ID
     * @param key       custom style property key
     * @param value     custom style property value
     * @param <T>       custom style property value type
     * @return old custom style property value
     */
    public static <T> T setCustomPainterProperty ( final JComponent component, final String painterId, final String key, final T value )
    {
        // Retrieving custom properties map
        Map<String, Map<String, Object>> allProperties = customPainterProperties.get ( component );
        if ( allProperties == null )
        {
            allProperties = new HashMap<String, Map<String, Object>> ( 1 );
            customPainterProperties.put ( component, allProperties );
        }

        // Retrieving painter properties map
        Map<String, Object> properties = allProperties.get ( painterId );
        if ( properties == null )
        {
            properties = new HashMap<String, Object> ( 1 );
            allProperties.put ( painterId, properties );
        }

        // Saving custom property
        final T oldValue = ( T ) properties.put ( key, value );

        // Applying custom style property if there is a skin applied to this component
        final WebLafSkin componentSkin = appliedSkins.get ( component );
        if ( componentSkin != null )
        {
            componentSkin.setCustomPainterProperty ( component, key, value );
        }

        return oldValue;
    }

    /**
     * Returns all custom painter properties.
     * Map structure: JComponent -> painterId -> propertyName -> propertyValue
     *
     * @return all custom painter properties
     */
    public static Map<JComponent, Map<String, Map<String, Object>>> getCustomPainterProperties ()
    {
        return customPainterProperties;
    }

    /**
     * Returns all custom painter properties for the specified component.
     * Map structure: painterId -> propertyName -> propertyValue
     *
     * @param component component to retrieve custom properties for
     * @return all custom painter properties for the specified component
     */
    public static Map<String, Map<String, Object>> getCustomPainterProperties ( final JComponent component )
    {
        return customPainterProperties.get ( component );
    }

    /**
     * Clears all custom painter properties for the specified component.
     * This is required when painter changes to avoid setting unexisting variables into painter.
     *
     * @param component component to clear custom painter properties for
     */
    public static void clearCustomPainterProperties ( final JComponent component )
    {
        final Map<String, Map<String, Object>> properties = customPainterProperties.get ( component );
        if ( properties != null )
        {
            // Removing all custom painter properties
            properties.clear ();

            // Forcing component skin update
            applySkin ( component );
        }
    }

    /**
     * Returns painter for the specified component.
     *
     * @param component component to retrieve painter from
     * @param <T>       painter type
     * @return current component painter
     */
    public static <T extends Painter> T getPainter ( final JComponent component )
    {
        // todo Change ID to null
        return getPainter ( component, DEFAULT_PAINTER_ID );
    }

    /**
     * Returns painter for the specified component and painter ID.
     *
     * @param component component to retrieve painter from
     * @param painterId painter ID
     * @param <T>       painter type
     * @return current component painter for the specified painter ID
     */
    public static <T extends Painter> T getPainter ( final JComponent component, final String painterId )
    {
        final Map<String, Painter> painters = customPainters.get ( component );
        if ( painters != null && painters.containsKey ( painterId ) )
        {
            // Return custom installed painter
            return ( T ) painters.get ( painterId );
        }
        else
        {
            // Return painter applied by skin
            return getCurrentSkin ().getPainter ( component, painterId );
        }
    }

    /**
     * Sets custom default painter for the specified component.
     * You should call this method when setting painter outside of the UI to avoid
     *
     * @param component component to set painter for
     * @param painter   painter
     * @param <T>       painter type
     * @return old custom painter
     */
    public static <T extends Painter> T setCustomPainter ( final JComponent component, final T painter )
    {
        // todo Change ID to null
        return setCustomPainter ( component, DEFAULT_PAINTER_ID, painter );
    }

    /**
     * Sets custom painter for the specified component.
     * You should call this method when setting painter outside of the UI to avoid
     *
     * @param component component to set painter for
     * @param id        painter ID
     * @param painter   painter
     * @param <T>       painter type
     * @return old custom painter
     */
    public static <T extends Painter> T setCustomPainter ( final JComponent component, final String id, final T painter )
    {
        // Clearing custom properties first
        clearCustomPainterProperties ( component );

        // Saving custom painter
        Map<String, Painter> painters = customPainters.get ( component );
        if ( painters == null )
        {
            painters = new HashMap<String, Painter> ( 1 );
            customPainters.put ( component, painters );
        }
        final T oldValue = ( T ) painters.put ( id, painter );

        // Forcing component update
        applySkin ( component );

        return oldValue;
    }

    /**
     * Returns all custom painters.
     *
     * @return all custom painters
     */
    public static Map<JComponent, Map<String, Painter>> getCustomPainters ()
    {
        return customPainters;
    }

    /**
     * Returns all custom painters for the specified component.
     *
     * @param component component to retrieve custom painters for
     * @return all custom painters for the specified component
     */
    public static Map<String, Painter> getCustomPainters ( final JComponent component )
    {
        return customPainters.get ( component );
    }

    /**
     * Removes all custom painters from the specified component.
     *
     * @param component component to remove custom painters from
     */
    public static void removeCustomPainters ( final JComponent component )
    {
        final Map<String, Painter> painters = customPainters.get ( component );
        if ( painters != null )
        {
            // Removing all custom painters
            painters.clear ();

            // Forcing component skin update
            applySkin ( component );
        }
    }

    /**
     * Sets component style ID if component or its UI is instance of Styleable interface.
     * This might be useful in cases when you cannot be sure about component type but want to provide style if possible.
     *
     * @param component component to apply style ID to
     * @param styleId   style ID
     * @return true if style ID was successfully applied, false otherwise
     */
    public static boolean setStyleId ( final Component component, final String styleId )
    {
        if ( component instanceof Styleable )
        {
            ( ( Styleable ) component ).setStyleId ( styleId );
            return true;
        }
        else
        {
            final ComponentUI ui = LafUtils.getUI ( component );
            if ( ui != null && ui instanceof Styleable )
            {
                ( ( Styleable ) ui ).setStyleId ( styleId );
                return true;
            }
        }
        return false;
    }
}