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

package com.alee.managers.style.skin;

import com.alee.extended.painter.Painter;
import com.alee.managers.log.Log;
import com.alee.managers.style.StyleException;
import com.alee.managers.style.StyleManager;
import com.alee.managers.style.SupportedComponent;
import com.alee.managers.style.data.ComponentStyle;
import com.alee.managers.style.data.IgnoredValue;
import com.alee.managers.style.data.PainterStyle;
import com.alee.utils.LafUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SystemUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This abstract class represents single WebLaF skin.
 * Each skin combines a group of component painters and settings to provide an unique visual style.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see com.alee.managers.style.StyleManager
 * @see com.alee.managers.style.skin.CustomSkin
 */

public abstract class WebLafSkin
{
    /**
     * Constant provided in the skin that supports any kind of systems.
     */
    public static final String ALL_SYSTEMS_SUPPORTED = "all";

    /**
     * Returns unique skin ID.
     * Used to collect and manage skins within StyleManager.
     *
     * @return unique skin ID
     */
    public abstract String getId ();

    /**
     * Returns skin name.
     *
     * @return skin name
     */
    public abstract String getName ();

    /**
     * Returns skin description.
     *
     * @return skin description
     */
    public abstract String getDescription ();

    /**
     * Returns skin author.
     *
     * @return skin author
     */
    public abstract String getAuthor ();

    /**
     * Returns list of supported OS.
     *
     * @return list of supported OS
     */
    public abstract List<String> getSupportedSystems ();

    /**
     * Returns whether this skin is supported or not.
     * This method reflects the default mechanism of checking skin support.
     * You can override it in your own skin to provide any custom checks.
     *
     * @return true if this skin is supported, false otherwise
     */
    public boolean isSupported ()
    {
        final List<String> systems = getSupportedSystems ();
        final boolean supportsAny = systems != null && systems.size () > 0;
        return supportsAny && ( systems.contains ( ALL_SYSTEMS_SUPPORTED ) || systems.contains ( SystemUtils.getShortOsName () ) );
    }

    /**
     * Returns skin base class name.
     *
     * @return skin base class name
     */
    public abstract String getSkinClass ();

    /**
     * Returns style for the specified supported component type.
     * Custom style ID can be specified in any Web-component or Web-UI to override default component style.
     * If style for such custom ID is not found in skin descriptor then default style for that component is used.
     *
     * @param component component instance
     * @param type      component type
     * @return component style
     */
    public abstract ComponentStyle getComponentStyle ( JComponent component, SupportedComponent type );

    /**
     * Returns component type.
     *
     * @param component component instance
     * @return component type
     */
    protected SupportedComponent getSupportedComponentTypeImpl ( final JComponent component )
    {
        final SupportedComponent type = SupportedComponent.getComponentTypeByUIClassID ( component.getUIClassID () );
        if ( type == null )
        {
            throw new StyleException ( "Unknown component UI class ID: " + component.getUIClassID () );
        }
        return type;
    }

    /**
     * Returns component style.
     * This method does a check that style exists in addition to abstract one.
     *
     * @param component component instance
     * @return component style
     */
    protected ComponentStyle getComponentStyleImpl ( final JComponent component )
    {
        return getComponentStyleImpl ( component, getSupportedComponentTypeImpl ( component ) );
    }

    /**
     * Returns component style.
     * This method does a check that style exists in addition to abstract one.
     *
     * @param component component instance
     * @param type      component type
     * @return component style
     */
    protected ComponentStyle getComponentStyleImpl ( final JComponent component, final SupportedComponent type )
    {
        final ComponentStyle style = getComponentStyle ( component, type );
        if ( style == null )
        {
            throw new StyleException ( "Skin doesn't contain style for UI class ID: " + component.getUIClassID () );
        }
        return style;
    }

    /**
     * Returns component UI object.
     *
     * @param component component instance
     * @return component UI object
     */
    protected ComponentUI getComponentUIImpl ( final JComponent component )
    {
        final ComponentUI ui = LafUtils.getUI ( component );
        if ( ui == null )
        {
            throw new StyleException ( "Unable to retrieve UI from component: " + component );
        }
        return ui;
    }

    /**
     * Applies this skin to the specified component.
     * Returns whether sking was successfully applied or not.
     *
     * @param component component to apply skin to
     * @return true if skin was applied, false otherwise
     */
    public boolean applySkin ( final JComponent component )
    {
        return applySkin ( component, StyleManager.getCustomPainterProperties ( component ), StyleManager.getCustomPainters ( component ) );
    }

    /**
     * Applies this skin to the specified component.
     * Returns whether sking was successfully applied or not.
     *
     * @param component               component to apply skin to
     * @param customPainterProperties custom painter properties to apply
     * @param customPainters          custom painters to apply
     * @return true if skin was applied, false otherwise
     */
    public boolean applySkin ( final JComponent component, final Map<String, Map<String, Object>> customPainterProperties,
                               final Map<String, Painter> customPainters )
    {
        try
        {
            final SupportedComponent type = getSupportedComponentTypeImpl ( component );
            final ComponentStyle style = getComponentStyleImpl ( component, type );
            final ComponentUI ui = getComponentUIImpl ( component );

            // Installing painters
            for ( final PainterStyle painterStyle : style.getPainters () )
            {
                // Painter ID
                final String painterId = painterStyle.getId ();

                // Retrieving painter to install into component
                // Custom painter can be null - that will just mean that component should not have painter installed
                final Painter painter;
                if ( customPainters != null && customPainters.containsKey ( painterId ) )
                {
                    // Using provided custom painter
                    // This might be set using Web-component "set...Painter"-like methods
                    painter = customPainters.get ( painterId );
                }
                else
                {
                    // Creating painter instance
                    // Be aware that all painters must have default constructor
                    painter = ReflectUtils.createInstanceSafely ( painterStyle.getPainterClass () );
                    if ( painter == null )
                    {
                        throw new StyleException (
                                "Unable to create painter \"" + painterStyle.getPainterClass () + "\" for component: " + component );
                    }

                    // Applying painter properties
                    // These properties are applied only for style-provided painters
                    // Customly provided painters are not affected by these properties to avoid unexpected changes in them
                    final Map<String, Object> cpp = getCustomPainterProperties ( customPainterProperties, painterStyle, painterId );
                    applyProperties ( painter, painterStyle.getProperties (), cpp );
                }

                // Installing painter into the UI
                final String setterMethod = ReflectUtils.getSetterMethodName ( painterId );
                ReflectUtils.callMethod ( ui, setterMethod, painter );
            }

            // Applying UI properties
            // todo Check whether properties should be applied or not somehow? Additional settings?
            applyProperties ( ui, style.getUIProperties (), null );

            // Applying component properties
            // todo Check whether properties should be applied or not somehow? Additional settings?
            applyProperties ( component, style.getComponentProperties (), null );

            return true;
        }
        catch ( final Throwable e )
        {
            Log.error ( this, e );
            return false;
        }
    }

    /**
     * Returns custom painter properties based on painter ID.
     *
     * @param customPainterProperties all custom painter properties
     * @param painterStyle            painter style
     * @param painterId               painter ID
     * @return specific custom painter properties
     */
    protected Map<String, Object> getCustomPainterProperties ( final Map<String, Map<String, Object>> customPainterProperties,
                                                               final PainterStyle painterStyle, final String painterId )
    {
        return customPainterProperties != null ? customPainterProperties.get ( painterStyle.isBase () ? null : painterId ) : null;
    }

    /**
     * Removes this skin from the specified component.
     *
     * @param component component to remove skin from
     * @return true if skin was successfully removed, false otherwise
     */
    public boolean removeSkin ( final JComponent component )
    {
        try
        {
            // Uninstalling skin painters from the UI
            final ComponentStyle style = getComponentStyleImpl ( component );
            final ComponentUI ui = getComponentUIImpl ( component );
            for ( final PainterStyle painterStyle : style.getPainters () )
            {
                final String setterMethod = ReflectUtils.getSetterMethodName ( painterStyle.getId () );
                ReflectUtils.callMethod ( ui, setterMethod, ( Painter ) null );
            }
            return true;
        }
        catch ( final Throwable e )
        {
            Log.error ( this, e );
            return false;
        }
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
    public <T> T getPainterPropertyValue ( final JComponent component, final String key )
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
    public <T> T getPainterPropertyValue ( final JComponent component, final String painterId, final String key )
    {
        final Painter painter = getPainter ( component, painterId );
        if ( painter != null )
        {
            // Retrieving painter field value
            return getFieldValue ( painter, key );
        }
        else if ( !StyleManager.isStrictStyleChecks () )
        {
            // Simply return null
            return null;
        }
        else
        {
            // Throw exception if painter was not found
            throw new StyleException ( "Painter with ID \"" + painterId + "\" was not found for component: " + component );
        }
    }

    /**
     * Sets custom value for painter property for the specified component.
     * This tricky method retrieves component painter throught its UI and skin settings and applies the specified style property.
     *
     * @param component component to apply custom style property to
     * @param key       custom style property key
     * @param value     custom style property value
     * @return true if custom style property was applied, false otherwise
     */
    public boolean setCustomPainterProperty ( final JComponent component, final String key, final Object value )
    {
        return setCustomPainterProperty ( component, null, key, value );
    }

    /**
     * Sets custom value for painter property for the specified component.
     * This tricky method retrieves component painter throught its UI and skin settings and applies the specified style property.
     *
     * @param component component to apply custom style property to
     * @param painterId painter ID
     * @param key       custom style property key
     * @param value     custom style property value
     * @return true if custom style property was applied, false otherwise
     */
    public boolean setCustomPainterProperty ( final JComponent component, final String painterId, final String key, final Object value )
    {
        final Painter painter = getPainter ( component, painterId );
        if ( painter != null )
        {
            // Updating painter field with custom style property value
            return setFieldValue ( painter, key, value );
        }
        else if ( !StyleManager.isStrictStyleChecks () )
        {
            // Simply return false
            return false;
        }
        else
        {
            // Throw exception if painter was not found
            throw new StyleException ( "Painter with ID \"" + painterId + "\" was not found for component: " + component );
        }
    }

    /**
     * Applies properties to specified object fields.
     *
     * @param object           object instance
     * @param skinProperties   skin properties to apply, these properties come from the skin
     * @param customProperties custom properties to apply, these properties are provided directly into the specific component
     */
    protected void applyProperties ( final Object object, final Map<String, Object> skinProperties,
                                     final Map<String, Object> customProperties )
    {
        // Merging skin and custom properties
        final Map<String, Object> mergedProperties;
        if ( customProperties != null && customProperties.size () > 0 )
        {
            if ( skinProperties != null && skinProperties.size () > 0 )
            {
                // Custom properties are added after skin properties to replace existing values
                mergedProperties = new HashMap<String, Object> ( Math.max ( skinProperties.size (), customProperties.size () ) );
                mergedProperties.putAll ( skinProperties );
                mergedProperties.putAll ( customProperties );
            }
            else
            {
                mergedProperties = customProperties;
            }
        }
        else
        {
            mergedProperties = skinProperties;
        }

        // Applying merged properties
        if ( mergedProperties != null && mergedProperties.size () > 0 )
        {
            for ( final Map.Entry<String, Object> entry : mergedProperties.entrySet () )
            {
                setFieldValue ( object, entry.getKey (), entry.getValue () );
            }
        }
    }

    /**
     * Applies specified value to object field.
     * This method allows to access and modify even private object fields.
     * Note that this method might also work even if there is no real field with the specified name but there is fitting setter method.
     *
     * @param object object instance
     * @param field  object field
     * @param value  field value
     * @return true if value was applied successfully, false otherwise
     */
    public static boolean setFieldValue ( final Object object, final String field, final Object value )
    {
        // Skip ignored values
        if ( value == IgnoredValue.VALUE )
        {
            return false;
        }

        // Trying to use setter method to apply the specified value
        try
        {
            final String setterMethod = ReflectUtils.getSetterMethodName ( field );
            ReflectUtils.callMethod ( object, setterMethod, value );
            return true;
        }
        catch ( final NoSuchMethodException e )
        {
            Log.error ( WebLafSkin.class, e );
        }
        catch ( final InvocationTargetException e )
        {
            Log.error ( WebLafSkin.class, e );
        }
        catch ( final IllegalAccessException e )
        {
            Log.error ( WebLafSkin.class, e );
        }

        // Applying field value directly
        try
        {
            final Field actualField = ReflectUtils.getField ( object.getClass (), field );
            actualField.setAccessible ( true );
            actualField.set ( object, value );
            return true;
        }
        catch ( final NoSuchFieldException e )
        {
            Log.error ( WebLafSkin.class, e );
            return false;
        }
        catch ( final IllegalAccessException e )
        {
            Log.error ( WebLafSkin.class, e );
            return false;
        }
    }

    /**
     * Returns object field value.
     * This method allows to access even private object fields.
     * Note that this method might also work even if there is no real field with the specified name but there is fitting getter method.
     *
     * @param object object instance
     * @param field  object field
     * @param <T>    value type
     * @return field value for the specified object or null
     */
    public static <T> T getFieldValue ( final Object object, final String field )
    {
        final Class<?> objectClass = object.getClass ();

        // Trying to use getter method to retrieve value
        // Note that this method might work even if there is no real field with the specified name but there is fitting getter method
        // This was made to improve call speed (no real field check) and avoid accessing field directly (in most of cases)
        try
        {
            final Method getter = ReflectUtils.getFieldGetter ( object, field );
            return ( T ) getter.invoke ( object );
        }
        catch ( final InvocationTargetException e )
        {
            Log.error ( WebLafSkin.class, e );
        }
        catch ( final IllegalAccessException e )
        {
            Log.error ( WebLafSkin.class, e );
        }

        // Retrieving field value directly
        // This one is rarely used and in most of times will be called when inappropriate property is set
        try
        {
            final Field actualField = ReflectUtils.getField ( objectClass, field );
            actualField.setAccessible ( true );
            return ( T ) actualField.get ( object );
        }
        catch ( final NoSuchFieldException e )
        {
            Log.error ( WebLafSkin.class, e );
            return null;
        }
        catch ( final IllegalAccessException e )
        {
            Log.error ( WebLafSkin.class, e );
            return null;
        }
    }

    /**
     * Returns component painter for the specified painter ID.
     *
     * @param component component to retrieve painter from
     * @param painterId painter ID
     * @return component painter
     */
    public <T extends Painter> T getPainter ( final JComponent component, final String painterId )
    {
        final String pid = painterId != null ? painterId : getComponentStyleImpl ( component ).getBasePainter ().getId ();
        final ComponentUI ui = getComponentUIImpl ( component );
        return getFieldValue ( ui, pid );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString ()
    {
        return getName ();
    }
}