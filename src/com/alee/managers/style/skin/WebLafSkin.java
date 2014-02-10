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
import com.alee.utils.ReflectUtils;
import com.alee.utils.swing.PainterMethods;

import javax.swing.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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
     * Returns style for the specified component type.
     *
     * @param component component instance
     * @param type      component type
     * @return component style
     */
    public abstract ComponentStyle getComponentStyle ( JComponent component, SupportedComponent type );

    /**
     * Applies this skin to the specified component.
     * Returns whether sking was successfully applied or not.
     *
     * @param component component to apply skin to
     * @return true if skin was applied, false otherwise
     */
    public boolean apply ( final JComponent component )
    {
        try
        {
            final SupportedComponent type = SupportedComponent.getComponentTypeByUIClassID ( component.getUIClassID () );
            if ( type == null )
            {
                throw new RuntimeException ( "Unknown component UI class ID: " + component.getUIClassID () );
            }

            final ComponentStyle style = getComponentStyle ( component, type );
            if ( style == null )
            {
                throw new RuntimeException ( "Skin doesn't contain style for UI class ID: " + component.getUIClassID () );
            }

            final Object ui = ReflectUtils.callMethodSafely ( component, "getUI" );
            if ( ui == null )
            {
                throw new RuntimeException ( "Unable to retrieve UI from component: " + component );
            }

            // Applying component properties
            applyProperties ( component, style.getComponentProperties () );

            // Applying UI properties
            applyProperties ( ui, style.getUIProperties () );

            // Installing painters
            if ( ui instanceof PainterMethods )
            {
                final PainterMethods painterMethods = ( PainterMethods ) ui;
                for ( final PainterStyle painterStyle : style.getPainters () )
                {
                    // Creating painter through special method in the UI
                    final Painter painter = painterMethods.createPainter ( painterStyle );

                    // Applying painter properties
                    applyProperties ( painter, painterStyle.getProperties () );

                    // Installing painter into the UI
                    painterMethods.installPainter ( painter, painterStyle );
                }
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
     * Removes this skin from the specified component.
     *
     * @param component component to remove skin from
     * @return true if skin was successfully removed, false otherwise
     */
    public boolean remove ( final JComponent component )
    {
        try
        {
            final Object ui = ReflectUtils.callMethodSafely ( component, "getUI" );
            if ( ui instanceof PainterMethods )
            {
                final PainterMethods painterMethods = ( PainterMethods ) ui;
                painterMethods.uninstallPainters ();
                return true;
            }
            else
            {
                return false;
            }
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
     * @param object     object instance
     * @param properties properties to apply
     */
    protected void applyProperties ( final Object object, final Map<String, Object> properties )
    {
        for ( final Map.Entry<String, Object> entry : properties.entrySet () )
        {
            setFieldValue ( object, entry.getKey (), entry.getValue () );
        }
    }

    /**
     * Applies specified value to object field.
     * This method allows to access and modify even private object fields.
     *
     * @param object object instance
     * @param field  object field
     * @param value  field value
     * @return true if value was applied successfully, false otherwise
     */
    public static boolean setFieldValue ( final Object object, final String field, final Object value )
    {
        final Class<?> objectClass = object.getClass ();

        // Checking that field exists
        final Field actualField;
        try
        {
            actualField = ReflectUtils.getField ( objectClass, field );
        }
        catch ( final NoSuchFieldException e )
        {
            e.printStackTrace ();
            return false;
        }

        // Trying to use setter method
        try
        {
            final String setterMethod = "set" + field.substring ( 0, 1 ).toUpperCase () + field.substring ( 1 );
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
            actualField.setAccessible ( true );
            actualField.set ( object, value );
            return true;
        }
        catch ( final IllegalAccessException e )
        {
            e.printStackTrace ();
            return false;
        }
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