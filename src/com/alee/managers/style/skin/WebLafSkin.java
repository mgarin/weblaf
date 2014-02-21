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
import com.alee.managers.style.SupportedComponent;
import com.alee.managers.style.data.ComponentStyle;
import com.alee.managers.style.data.PainterStyle;
import com.alee.utils.LafUtils;
import com.alee.utils.ReflectUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This abstract class represents single WebLaF skin.
 * Each skin combines a group of component painters and settings to provide an unique visual style.
 *
 * @author Mikle Garin
 * @see CustomSkin
 */

public abstract class WebLafSkin
{
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
            throw new RuntimeException ( "Unknown component UI class ID: " + component.getUIClassID () );
        }
        return type;
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
            throw new RuntimeException ( "Skin doesn't contain style for UI class ID: " + component.getUIClassID () );
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
            throw new RuntimeException ( "Unable to retrieve UI from component: " + component );
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
    public boolean apply ( final JComponent component )
    {
        return apply ( component, null );
    }

    /**
     * Applies this skin to the specified component.
     * Returns whether sking was successfully applied or not.
     *
     * @param component        component to apply skin to
     * @param customProperties custom properties to apply, these properties are provided directly into the specific component
     * @return true if skin was applied, false otherwise
     */
    public boolean apply ( final JComponent component, final Map<String, Object> customProperties )
    {
        try
        {
            final SupportedComponent type = getSupportedComponentTypeImpl ( component );
            final ComponentStyle style = getComponentStyleImpl ( component, type );
            final ComponentUI ui = getComponentUIImpl ( component );

            // Applying component properties
            // todo Check whether properties should be applied or not somehow
            applyProperties ( component, style.getComponentProperties (), null );

            // Applying UI properties
            // todo Check whether properties should be applied or not somehow
            applyProperties ( ui, style.getUIProperties (), null );

            // Installing painters
            for ( final PainterStyle painterStyle : style.getPainters () )
            {
                // Creating painter instance
                // Be aware that all painters must have default constructor
                final Painter painter = ReflectUtils.createInstanceSafely ( painterStyle.getPainterClass () );
                if ( painter == null )
                {
                    throw new RuntimeException ( "Unable to create painter: " + painterStyle.getPainterClass () );
                }

                // Applying painter properties
                applyProperties ( painter, painterStyle.getProperties (), customProperties );

                // Installing painter into the UI
                final String setterMethod = getSetterMethodName ( painterStyle.getId () );
                ReflectUtils.callMethod ( ui, setterMethod, painter );
            }

            return true;
        }
        catch ( final Throwable e )
        {
            e.printStackTrace ();
            return false;
        }
    }

    public <T> T getStyleValue ( final JComponent component, final String key )
    {
        final SupportedComponent type = getSupportedComponentTypeImpl ( component );
        final ComponentStyle style = getComponentStyleImpl ( component, type );
        final ComponentUI ui = getComponentUIImpl ( component );

        // Retrieving component base painter
        // todo Allow retrieving style properties from non-base painters
        final Painter painter = getFieldValue ( ui, style.getBasePainter ().getId () );
        if ( painter != null )
        {
            // Retrieving painter field value
            return getFieldValue ( painter, key );
        }
        else
        {
            return null;
        }
    }

    /**
     * Applies custom style property to the specified component's painter.
     * This tricky method retrieves component painter throught its UI and skin settings and applies the specified style property.
     *
     * @param component component to apply custom style property to
     * @param key       custom style property key
     * @param value     custom style property value
     * @return true if custom style property was applied, false otherwise
     */
    public boolean applyCustomStyle ( final JComponent component, final String key, final Object value )
    {
        final SupportedComponent type = getSupportedComponentTypeImpl ( component );
        final ComponentStyle style = getComponentStyleImpl ( component, type );
        final ComponentUI ui = getComponentUIImpl ( component );

        // Retrieving component base painter
        // todo Allow applying custom style properties to non-base painters
        final Painter painter = getFieldValue ( ui, style.getBasePainter ().getId () );
        if ( painter != null )
        {
            // Updating painter field with custom style property value
            setFieldValue ( painter, key, value );
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Removes this skin from the specified component.
     *
     * @param component component to remove skin from
     * @return true if skin was successfully removed, false otherwise
     */
    public boolean remove ( final JComponent component )
    {
        try
        {
            final SupportedComponent type = getSupportedComponentTypeImpl ( component );
            final ComponentStyle style = getComponentStyleImpl ( component, type );
            final ComponentUI ui = getComponentUIImpl ( component );

            // Uninstalling skin painters from the UI
            for ( final PainterStyle painterStyle : style.getPainters () )
            {
                final String setterMethod = getSetterMethodName ( painterStyle.getId () );
                ReflectUtils.callMethod ( ui, setterMethod, ( Painter ) null );
            }
            return true;
        }
        catch ( final Throwable e )
        {
            e.printStackTrace ();
            return false;
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
                mergedProperties = new HashMap<String, Object> ( skinProperties.size () );
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
        if ( mergedProperties != null )
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
        final Class<?> objectClass = object.getClass ();

        // Trying to use setter method to apply the specified value
        try
        {
            final String setterMethod = getSetterMethodName ( field );
            ReflectUtils.callMethod ( object, setterMethod, value );
        }
        catch ( final NoSuchMethodException e )
        {
            e.printStackTrace ();
        }
        catch ( final InvocationTargetException e )
        {
            e.printStackTrace ();
        }
        catch ( final IllegalAccessException e )
        {
            e.printStackTrace ();
        }

        // Applying field value directly
        try
        {
            final Field actualField = ReflectUtils.getField ( objectClass, field );
            actualField.setAccessible ( true );
            actualField.set ( object, value );
            return true;
        }
        catch ( final NoSuchFieldException e )
        {
            e.printStackTrace ();
            return false;
        }
        catch ( final IllegalAccessException e )
        {
            e.printStackTrace ();
            return false;
        }
    }

    /**
     * Returns setter method name for the specified field.
     *
     * @param field field name
     * @return setter method name for the specified field
     */
    public static String getSetterMethodName ( final String field )
    {
        return "set" + field.substring ( 0, 1 ).toUpperCase () + field.substring ( 1 );
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
        // Note that this method might work even if there is no field with the specified name but there is fitting getter method
        try
        {
            final String getterMethod = getGetterMethodName ( field );
            return ReflectUtils.callMethod ( object, getterMethod );
        }
        catch ( final NoSuchMethodException e )
        {
            e.printStackTrace ();
        }
        catch ( final InvocationTargetException e )
        {
            e.printStackTrace ();
        }
        catch ( final IllegalAccessException e )
        {
            e.printStackTrace ();
        }

        // Retrieving field value directly
        try
        {
            final Field actualField = ReflectUtils.getField ( objectClass, field );
            actualField.setAccessible ( true );
            return ( T ) actualField.get ( object );
        }
        catch ( final NoSuchFieldException e )
        {
            e.printStackTrace ();
            return null;
        }
        catch ( final IllegalAccessException e )
        {
            e.printStackTrace ();
            return null;
        }
    }

    /**
     * Returns getter method name for the specified field.
     *
     * @param field field name
     * @return getter method name for the specified field
     */
    public static String getGetterMethodName ( final String field )
    {
        return "get" + field.substring ( 0, 1 ).toUpperCase () + field.substring ( 1 );
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