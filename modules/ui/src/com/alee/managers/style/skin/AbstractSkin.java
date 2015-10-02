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

import com.alee.api.IconSupport;
import com.alee.api.TitleSupport;
import com.alee.extended.painter.Painter;
import com.alee.managers.log.Log;
import com.alee.managers.style.StyleException;
import com.alee.managers.style.StyleManager;
import com.alee.managers.style.StyleableComponent;
import com.alee.managers.style.data.ComponentStyle;
import com.alee.managers.style.data.ComponentStyleConverter;
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

public abstract class AbstractSkin implements IconSupport, TitleSupport
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
     * Returns skin icon.
     *
     * @return skin icon
     */
    @Override
    public abstract Icon getIcon ();

    /**
     * Returns skin title.
     *
     * @return skin title
     */
    @Override
    public abstract String getTitle ();

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
    public abstract ComponentStyle getComponentStyle ( JComponent component, StyleableComponent type );

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
     * Returns whether skin was successfully applied or not.
     *
     * @param component component to apply skin to
     * @return true if skin was applied, false otherwise
     */
    public boolean applySkin ( final JComponent component )
    {
        return applySkin ( component, StyleManager.getCustomPainters ( component ) );
    }

    /**
     * Applies this skin to the specified component.
     * Returns whether skin was successfully applied or not.
     *
     * @param component      component to apply skin to
     * @param customPainters custom painters to apply
     * @return true if skin was applied, false otherwise
     */
    public boolean applySkin ( final JComponent component, final Map<String, Painter> customPainters )
    {
        try
        {
            final StyleableComponent type = StyleableComponent.get ( component );
            final ComponentStyle style = getComponentStyle ( component, type );
            final ComponentUI ui = getComponentUIImpl ( component );

            //            System.out.println ( "Skin applied to: " + style.getCompleteId () + " " + component.hashCode () + " " + component );

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
                        final String msg = "Unable to create painter \"%s\" for component: %s";
                        throw new StyleException ( String.format ( msg, painterStyle.getPainterClass (), component.toString () ) );
                    }

                    // Applying painter properties
                    // These properties are applied only for style-provided painters
                    applyProperties ( painter, painterStyle.getProperties () );
                }

                // Installing painter into the UI
                final String setterMethod = ReflectUtils.getSetterMethodName ( painterId );
                ReflectUtils.callMethod ( ui, setterMethod, painter );
            }

            // Applying UI properties
            // todo Check whether properties should be applied or not somehow? Additional settings?
            applyProperties ( ui, ComponentStyleConverter.appendEmptyUIProperties ( ui, style.getUIProperties () ) );

            // Applying component properties
            // todo Check whether properties should be applied or not somehow? Additional settings?
            applyProperties ( component, style.getComponentProperties () );

            return true;
        }
        catch ( final Throwable e )
        {
            Log.error ( this, e );
            return false;
        }
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
            final ComponentStyle style = getComponentStyle ( component, StyleableComponent.get ( component ) );
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
     * Applies properties to specified object fields.
     *
     * @param object         object instance
     * @param skinProperties skin properties to apply, these properties come from the skin
     */
    protected void applyProperties ( final Object object, final Map<String, Object> skinProperties )
    {
        // Applying merged properties
        if ( skinProperties != null && skinProperties.size () > 0 )
        {
            for ( final Map.Entry<String, Object> entry : skinProperties.entrySet () )
            {
                setFieldValue ( object, entry.getKey (), entry.getValue () );
            }
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
        final ComponentStyle style = getComponentStyle ( component, StyleableComponent.get ( component ) );
        final String pid = painterId != null ? painterId : style.getBasePainter ().getId ();
        final ComponentUI ui = getComponentUIImpl ( component );
        return getFieldValue ( ui, pid );
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

        // todo Still need to check method here? Throw exceptions?
        // Trying to use setter method to apply the specified value
        try
        {
            final String setterMethod = ReflectUtils.getSetterMethodName ( field );
            ReflectUtils.callMethod ( object, setterMethod, value );
            return true;
        }
        catch ( final NoSuchMethodException e )
        {
            // Log.error ( WebLafSkin.class, e );
        }
        catch ( final InvocationTargetException e )
        {
            // Log.error ( WebLafSkin.class, e );
        }
        catch ( final IllegalAccessException e )
        {
            // Log.error ( WebLafSkin.class, e );
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
            Log.error ( AbstractSkin.class, e );
            return false;
        }
        catch ( final IllegalAccessException e )
        {
            Log.error ( AbstractSkin.class, e );
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
            Log.error ( AbstractSkin.class, e );
        }
        catch ( final IllegalAccessException e )
        {
            Log.error ( AbstractSkin.class, e );
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
            Log.error ( AbstractSkin.class, e );
            return null;
        }
        catch ( final IllegalAccessException e )
        {
            Log.error ( AbstractSkin.class, e );
            return null;
        }
    }

    @Override
    public String toString ()
    {
        return getTitle ();
    }
}