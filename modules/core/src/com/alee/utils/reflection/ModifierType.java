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

package com.alee.utils.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Enumeration representing {@link Class}, {@link Field} and {@link Method} modifier types.
 * This class exists for convenient usage of raw modifiers returned by instances of those objects.
 *
 * @author Mikle Garin
 */
public enum ModifierType
{
    /**
     * {@code public} modifier.
     */
    PUBLIC
            {
                @Override
                public boolean is ( final int modifiers )
                {
                    return Modifier.isPublic ( modifiers );
                }
            },

    /**
     * {@code private} modifier.
     */
    PRIVATE
            {
                @Override
                public boolean is ( final int modifiers )
                {
                    return Modifier.isPrivate ( modifiers );
                }
            },

    /**
     * {@code protected} modifier.
     */
    PROTECTED
            {
                @Override
                public boolean is ( final int modifiers )
                {
                    return Modifier.isProtected ( modifiers );
                }
            },

    /**
     * {@code static} modifier.
     */
    STATIC
            {
                @Override
                public boolean is ( final int modifiers )
                {
                    return Modifier.isStatic ( modifiers );
                }
            },

    /**
     * {@code final} modifier.
     */
    FINAL
            {
                @Override
                public boolean is ( final int modifiers )
                {
                    return Modifier.isFinal ( modifiers );
                }
            },

    /**
     * {@code synchronized} modifier.
     */
    SYNCHRONIZED
            {
                @Override
                public boolean is ( final int modifiers )
                {
                    return Modifier.isSynchronized ( modifiers );
                }
            },

    /**
     * {@code volatile} modifier.
     */
    VOLATILE
            {
                @Override
                public boolean is ( final int modifiers )
                {
                    return Modifier.isVolatile ( modifiers );
                }
            },

    /**
     * {@code transient} modifier.
     */
    TRANSIENT
            {
                @Override
                public boolean is ( final int modifiers )
                {
                    return Modifier.isTransient ( modifiers );
                }
            },

    /**
     * {@code native} modifier.
     */
    NATIVE
            {
                @Override
                public boolean is ( final int modifiers )
                {
                    return Modifier.isNative ( modifiers );
                }
            },

    /**
     * {@code interface} modifier.
     */
    INTERFACE
            {
                @Override
                public boolean is ( final int modifiers )
                {
                    return Modifier.isInterface ( modifiers );
                }
            },

    /**
     * {@code abstract} modifier.
     */
    ABSTRACT
            {
                @Override
                public boolean is ( final int modifiers )
                {
                    return Modifier.isAbstract ( modifiers );
                }
            };

    /**
     * Returns modifier title.
     *
     * @return modifier title
     */
    @Override
    public String toString ()
    {
        return this.name ().toLowerCase ( Locale.ROOT );
    }

    /**
     * Returns whether or not modifiers contain this specific modifier.
     *
     * @param modifiers modifiers to check
     * @return {@code true} if modifiers contain this specific modifier, {@code false} otherwise
     */
    public abstract boolean is ( int modifiers );

    /**
     * Returns whether or not modifiers do not contain this specific modifier.
     *
     * @param modifiers modifiers to check
     * @return {@code true} if modifiers do not contain this specific modifier, {@code false} otherwise
     */
    public boolean not ( final int modifiers )
    {
        return !is ( modifiers );
    }

    /**
     * Returns whether or not class has this specific modifier.
     *
     * @param clazz class to check
     * @return {@code true} if class has this specific modifier, {@code false} otherwise
     */
    public boolean is ( final Class clazz )
    {
        return is ( clazz.getModifiers () );
    }

    /**
     * Returns whether or not class doesn't have this specific modifier.
     *
     * @param clazz class to check
     * @return {@code true} if class doesn't have this specific modifier, {@code false} otherwise
     */
    public boolean not ( final Class clazz )
    {
        return !is ( clazz );
    }

    /**
     * Returns whether or not field has this specific modifier.
     *
     * @param field field to check
     * @return {@code true} if field has this specific modifier, {@code false} otherwise
     */
    public boolean is ( final Field field )
    {
        return is ( field.getModifiers () );
    }

    /**
     * Returns whether or not field doesn't have this specific modifier.
     *
     * @param field field to check
     * @return {@code true} if field doesn't have this specific modifier, {@code false} otherwise
     */
    public boolean not ( final Field field )
    {
        return !is ( field );
    }

    /**
     * Returns whether or not method has this specific modifier.
     *
     * @param method method to check
     * @return {@code true} if method has this specific modifier, {@code false} otherwise
     */
    public boolean is ( final Method method )
    {
        return is ( method.getModifiers () );
    }

    /**
     * Returns whether or not method doesn't have this specific modifier.
     *
     * @param method method to check
     * @return {@code true} if method doesn't have this specific modifier, {@code false} otherwise
     */
    public boolean not ( final Method method )
    {
        return !is ( method );
    }

    /**
     * Returns checks whether or not class has this specific modifier and throws an exception if it doesn't.
     *
     * @param clazz class to check
     */
    public void check ( final Class clazz )
    {
        if ( !is ( clazz ) )
        {
            throw new ReflectionException ( "Class is not " + this + ": " + clazz );
        }
    }

    /**
     * Returns checks whether or not field has this specific modifier and throws an exception if it doesn't.
     *
     * @param field field to check
     */
    public void check ( final Field field )
    {
        if ( !is ( field ) )
        {
            throw new ReflectionException ( "Field is not " + this + ": " + field );
        }
    }

    /**
     * Returns checks whether or not method has this specific modifier and throws an exception if it doesn't.
     *
     * @param method method to check
     */
    public void check ( final Method method )
    {
        if ( !is ( method ) )
        {
            throw new ReflectionException ( "Method is not " + this + ": " + method );
        }
    }

    /**
     * Returns checks whether or not class has this specific modifier and throws an exception if it doesn't.
     *
     * @param clazz class to check
     */
    public void checkNot ( final Class clazz )
    {
        if ( is ( clazz ) )
        {
            throw new ReflectionException ( "Class is " + this + ": " + clazz );
        }
    }

    /**
     * Returns checks whether or not field has this specific modifier and throws an exception if it doesn't.
     *
     * @param field field to check
     */
    public void checkNot ( final Field field )
    {
        if ( is ( field ) )
        {
            throw new ReflectionException ( "Field is " + this + ": " + field );
        }
    }

    /**
     * Returns checks whether or not method has this specific modifier and throws an exception if it doesn't.
     *
     * @param method method to check
     */
    public void checkNot ( final Method method )
    {
        if ( is ( method ) )
        {
            throw new ReflectionException ( "Method is " + this + ": " + method );
        }
    }

    /**
     * Returns all modifier types.
     *
     * @param modifiers raw modifiers
     * @return all modifier types
     */
    public static List<ModifierType> get ( final int modifiers )
    {
        final List<ModifierType> modifierTypes = new ArrayList<ModifierType> ();
        for ( final ModifierType modifierType : values () )
        {
            if ( modifierType.is ( modifiers ) )
            {
                modifierTypes.add ( modifierType );
            }
        }
        return modifierTypes;
    }

    /**
     * Returns all class modifier types.
     *
     * @param clazz class to retrieve all modifier types for
     * @return all class modifier types
     */
    public static List<ModifierType> get ( final Class clazz )
    {
        return get ( clazz.getModifiers () );
    }

    /**
     * Returns all field modifier types.
     *
     * @param field field to retrieve all modifier types for
     * @return all field modifier types
     */
    public static List<ModifierType> get ( final Field field )
    {
        return get ( field.getModifiers () );
    }

    /**
     * Returns all method modifier types.
     *
     * @param method method to retrieve all modifier types for
     * @return all method modifier types
     */
    public static List<ModifierType> get ( final Method method )
    {
        return get ( method.getModifiers () );
    }
}