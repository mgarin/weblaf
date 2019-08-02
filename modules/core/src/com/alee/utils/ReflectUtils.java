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

package com.alee.utils;

import com.alee.utils.collection.ImmutableList;
import com.alee.utils.reflection.ModifierType;
import com.alee.utils.reflection.ReflectionException;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.*;

/**
 * This class provides a set of utilities to simplify work with Reflection API.
 *
 * @author Mikle Garin
 */
public final class ReflectUtils
{
    /**
     * todo 1. Rework this utility class into an object that is only instantiated when needed
     * todo 2. Add implemenetation for vararg search
     */

    /**
     * Whether should allow safe methods to log errors or not.
     * By default it is disabled to hide some WebLaF exceptions which occur due to various method checks.
     * You can enable it in case you need a deeper look into whats happening here.
     */
    private static boolean safeMethodsLoggingEnabled = false;

    /**
     * Fields lookup cache.
     */
    private static final Map<Class, Map<String, Field>> fieldsLookupCache = new HashMap<Class, Map<String, Field>> ();

    /**
     * Methods lookup cache.
     */
    private static final Map<Class, Map<String, Method>> methodsLookupCache = new HashMap<Class, Map<String, Method>> ();

    /**
     * Private constructor to avoid instantiation.
     */
    private ReflectUtils ()
    {
        throw new UtilityException ( "Utility classes are not meant to be instantiated" );
    }

    /**
     * Returns whether should allow safe methods to log errors or not.
     *
     * @return {@code true} if should allow safe methods to log errors, {@code false} otherwise
     */
    public static boolean isSafeMethodsLoggingEnabled ()
    {
        return safeMethodsLoggingEnabled;
    }

    /**
     * Sets whether should allow safe methods to log errors or not.
     *
     * @param enabled whether should allow safe methods to log errors or not
     */
    public static void setSafeMethodsLoggingEnabled ( final boolean enabled )
    {
        ReflectUtils.safeMethodsLoggingEnabled = enabled;
    }

    /**
     * Returns class for the specified canonical name.
     *
     * @param canonicalName class canonical name
     * @param <T>           class type
     * @return class for the specified canonical name
     */
    public static <T> Class<T> getClassSafely ( final String canonicalName )
    {
        try
        {
            return getClass ( canonicalName );
        }
        catch ( final Exception e )
        {
            if ( safeMethodsLoggingEnabled )
            {
                final String msg = "ReflectionUtils method failed: getClassSafely ( %s )";
                LoggerFactory.getLogger ( ReflectUtils.class ).error ( String.format ( msg, canonicalName ), e );
            }
            return null;
        }
    }

    /**
     * Returns class for the specified canonical name.
     *
     * @param canonicalName class canonical name
     * @param <T>           class type
     * @return class for the specified canonical name
     * @throws ClassNotFoundException if class was not found
     */
    public static <T> Class<T> getClass ( final String canonicalName ) throws ClassNotFoundException
    {
        return ( Class<T> ) Class.forName ( canonicalName );
    }

    /**
     * Returns inner class with the specified name.
     *
     * @param fromClass      class to look for the inner class
     * @param innerClassName inner class name
     * @param <T>            inner class type
     * @return inner class with the specified name
     */
    public static <T> Class<T> getInnerClassSafely ( final Class fromClass, final String innerClassName )
    {
        return getInnerClassSafely ( fromClass.getCanonicalName (), innerClassName );
    }

    /**
     * Returns inner class with the specified name.
     *
     * @param fromClassName  name of the class to look for the inner class
     * @param innerClassName inner class name
     * @param <T>            inner class type
     * @return inner class with the specified name
     */
    public static <T> Class<T> getInnerClassSafely ( final String fromClassName, final String innerClassName )
    {
        try
        {
            return getInnerClass ( fromClassName, innerClassName );
        }
        catch ( final Exception e )
        {
            if ( safeMethodsLoggingEnabled )
            {
                final String msg = "ReflectionUtils method failed: getInnerClassSafely ( %s, %s )";
                LoggerFactory.getLogger ( ReflectUtils.class ).error ( String.format ( msg, fromClassName, innerClassName ), e );
            }
            return null;
        }
    }

    /**
     * Returns inner class with the specified name.
     *
     * @param fromClass      class to look for the inner class
     * @param innerClassName inner class name
     * @param <T>            inner class type
     * @return inner class with the specified name
     * @throws ClassNotFoundException if inner class was not found
     */
    public static <T> Class<T> getInnerClass ( final Class fromClass, final String innerClassName ) throws ClassNotFoundException
    {
        return getInnerClass ( fromClass.getCanonicalName (), innerClassName );
    }

    /**
     * Returns inner class with the specified name.
     *
     * @param fromClassName  name of the class to look for the inner class
     * @param innerClassName inner class name
     * @param <T>            inner class type
     * @return inner class with the specified name
     * @throws ClassNotFoundException if inner class was not found
     */
    public static <T> Class<T> getInnerClass ( final String fromClassName, final String innerClassName ) throws ClassNotFoundException
    {
        return getClass ( fromClassName + "$" + innerClassName );
    }

    /**
     * Returns method caller class.
     * It is not recommended to use this method anywhere but in debugging.
     *
     * @return method caller class
     */
    public static Class getCallerClass ()
    {
        // We have to add one to depth since this method call is increasing it
        return getCallerClass ( 1 );
    }

    /**
     * Returns method caller class.
     * It is not recommended to use this method anywhere but in debugging.
     *
     * @param additionalDepth additional methods depth
     * @return method caller class
     */
    public static Class getCallerClass ( final int additionalDepth )
    {
        // Depth explanation:
        // 0 - this method class
        // 1 - this method caller class
        // 2 - caller's class caller
        // additionalDepth - in case call goes through additional methods this is required
        final int depth = 2 + additionalDepth;

        try
        {
            // We add additional 3 levels of depth due to reflection calls here
            return callStaticMethod ( "sun.reflect.Reflection", "getCallerClass", depth + 3 );
        }
        catch ( final Exception e )
        {
            try
            {
                // Simply use determined depth
                return getClass ( new Throwable ().getStackTrace ()[ depth ].getClassName () );
            }
            catch ( final ClassNotFoundException ex )
            {
                return null;
            }
        }
    }

    /**
     * Returns all fields in the specified object class and all of its superclasses.
     *
     * @param object object to find fields for
     * @return all fields in the specified object class and all of its superclasses
     */
    public static List<Field> getFields ( final Object object )
    {
        return getFields ( object.getClass () );
    }

    /**
     * Returns all fields in the specified class and all of its superclasses.
     *
     * @param clazz class to find fields for
     * @return all fields in the specified class and all of its superclasses
     */
    public static List<Field> getFields ( final Class clazz )
    {
        return getFields ( clazz, ModifierType.STATIC );
    }

    /**
     * Returns all fields in the specified object class and all of its superclasses.
     *
     * @param object           object to find fields for
     * @param ignoredModifiers modifiers of fields to ignore
     * @return all fields in the specified object class and all of its superclasses
     */
    public static List<Field> getFields ( final Object object, final ModifierType... ignoredModifiers )
    {
        return getFields ( object.getClass (), ignoredModifiers );
    }

    /**
     * Returns all fields in the specified class and all of its superclasses.
     *
     * @param clazz            class to find fields for
     * @param ignoredModifiers modifiers of fields to ignore
     * @return all fields in the specified class and all of its superclasses
     */
    public static List<Field> getFields ( final Class clazz, final ModifierType... ignoredModifiers )
    {
        return getFields ( clazz, new HashSet<String> (), ignoredModifiers );
    }

    /**
     * Returns all fields in the specified class and all of its superclasses.
     *
     * @param clazz            class to find fields for
     * @param found            found field names
     * @param ignoredModifiers modifiers of fields to ignore
     * @return all fields in the specified class and all of its superclasses
     */
    private static List<Field> getFields ( final Class clazz, final Set<String> found, final ModifierType... ignoredModifiers )
    {
        // Find all current-level fields
        final Field[] declared = clazz.getDeclaredFields ();
        final List<Field> fields = new ArrayList<Field> ( declared.length );
        for ( final Field field : declared )
        {
            // Adding fields with unique name that haven't been found yet (on higher hierarchy levels)
            // and that do not contain any modifiers from the ignore list passed into this methos
            if ( !found.contains ( field.getName () ) && ReflectUtils.hasNoneOfModifiers ( field, ignoredModifiers ) )
            {
                // Making field accessible for usage convenience
                field.setAccessible ( true );

                // Collecting field
                fields.add ( field );

                // Marking unique field name as used
                // This is important to avoid overwriting fields with ones from parent classes (with the same name)
                found.add ( field.getName () );
            }
        }

        // Find all superclass fields
        final Class superclass = clazz.getSuperclass ();
        if ( superclass != null )
        {
            fields.addAll ( getFields ( superclass, found, ignoredModifiers ) );
        }

        return fields;
    }

    /**
     * Returns whether or not {@link Class} has any of the specified modifiers.
     *
     * @param clazz     {@link Class} to check modifiers for
     * @param modifiers modifiers to look for
     * @return {@code true} if {@link Class} has any of the specified modifiers, {@code false} otherwise
     */
    public static boolean hasAnyOfModifiers ( final Class clazz, final ModifierType... modifiers )
    {
        return hasAnyOfModifiers ( clazz, new ImmutableList<ModifierType> ( modifiers ) );
    }

    /**
     * Returns whether or not {@link Class} has any of the specified modifiers.
     *
     * @param clazz     {@link Class} to check modifiers for
     * @param modifiers modifiers to look for
     * @return {@code true} if {@link Class} has any of the specified modifiers, {@code false} otherwise
     */
    public static boolean hasAnyOfModifiers ( final Class clazz, final Collection<ModifierType> modifiers )
    {
        boolean contains = false;
        if ( CollectionUtils.notEmpty ( modifiers ) )
        {
            for ( final ModifierType modifier : modifiers )
            {
                if ( modifier.is ( clazz ) )
                {
                    contains = true;
                    break;
                }
            }
        }
        return contains;
    }

    /**
     * Returns whether or not {@link Class} has all of the specified modifiers.
     *
     * @param clazz     {@link Class} to check modifiers for
     * @param modifiers modifiers to look for
     * @return {@code true} if {@link Class} has all of the specified modifiers, {@code false} otherwise
     */
    public static boolean hasAllOfModifiers ( final Class clazz, final ModifierType... modifiers )
    {
        return hasAllOfModifiers ( clazz, new ImmutableList<ModifierType> ( modifiers ) );
    }

    /**
     * Returns whether or not {@link Class} has all of the specified modifiers.
     *
     * @param clazz     {@link Class} to check modifiers for
     * @param modifiers modifiers to look for
     * @return {@code true} if {@link Class} has all of the specified modifiers, {@code false} otherwise
     */
    public static boolean hasAllOfModifiers ( final Class clazz, final Collection<ModifierType> modifiers )
    {
        boolean contains = true;
        if ( ArrayUtils.notEmpty ( modifiers ) )
        {
            for ( final ModifierType modifier : modifiers )
            {
                if ( modifier.not ( clazz ) )
                {
                    contains = false;
                    break;
                }
            }
        }
        return contains;
    }

    /**
     * Returns whether or not {@link Class} has none of the specified modifiers.
     *
     * @param clazz     {@link Class} to check modifiers for
     * @param modifiers modifiers to look for
     * @return {@code true} if {@link Class} has none of the specified modifiers, {@code false} otherwise
     */
    public static boolean hasNoneOfModifiers ( final Class clazz, final ModifierType... modifiers )
    {
        return hasNoneOfModifiers ( clazz, new ImmutableList<ModifierType> ( modifiers ) );
    }

    /**
     * Returns whether or not {@link Class} has none of the specified modifiers.
     *
     * @param clazz     {@link Class} to check modifiers for
     * @param modifiers modifiers to look for
     * @return {@code true} if {@link Class} has none of the specified modifiers, {@code false} otherwise
     */
    public static boolean hasNoneOfModifiers ( final Class clazz, final Collection<ModifierType> modifiers )
    {
        return !hasAnyOfModifiers ( clazz, modifiers );
    }

    /**
     * Returns whether or not {@link Method} has any of the specified modifiers.
     *
     * @param method    {@link Method} to check modifiers for
     * @param modifiers modifiers to look for
     * @return {@code true} if {@link Method} has any of the specified modifiers, {@code false} otherwise
     */
    public static boolean hasAnyOfModifiers ( final Method method, final ModifierType... modifiers )
    {
        return hasAnyOfModifiers ( method, new ImmutableList<ModifierType> ( modifiers ) );
    }

    /**
     * Returns whether or not {@link Method} has any of the specified modifiers.
     *
     * @param method    {@link Method} to check modifiers for
     * @param modifiers modifiers to look for
     * @return {@code true} if {@link Method} has any of the specified modifiers, {@code false} otherwise
     */
    public static boolean hasAnyOfModifiers ( final Method method, final Collection<ModifierType> modifiers )
    {
        boolean contains = false;
        if ( CollectionUtils.notEmpty ( modifiers ) )
        {
            for ( final ModifierType modifier : modifiers )
            {
                if ( modifier.is ( method ) )
                {
                    contains = true;
                    break;
                }
            }
        }
        return contains;
    }

    /**
     * Returns whether or not {@link Method} has all of the specified modifiers.
     *
     * @param method    {@link Method} to check modifiers for
     * @param modifiers modifiers to look for
     * @return {@code true} if {@link Method} has all of the specified modifiers, {@code false} otherwise
     */
    public static boolean hasAllOfModifiers ( final Method method, final ModifierType... modifiers )
    {
        return hasAllOfModifiers ( method, new ImmutableList<ModifierType> ( modifiers ) );
    }

    /**
     * Returns whether or not {@link Method} has all of the specified modifiers.
     *
     * @param method    {@link Method} to check modifiers for
     * @param modifiers modifiers to look for
     * @return {@code true} if {@link Method} has all of the specified modifiers, {@code false} otherwise
     */
    public static boolean hasAllOfModifiers ( final Method method, final Collection<ModifierType> modifiers )
    {
        boolean contains = true;
        if ( ArrayUtils.notEmpty ( modifiers ) )
        {
            for ( final ModifierType modifier : modifiers )
            {
                if ( modifier.not ( method ) )
                {
                    contains = false;
                    break;
                }
            }
        }
        return contains;
    }

    /**
     * Returns whether or not {@link Method} has none of the specified modifiers.
     *
     * @param method    {@link Method} to check modifiers for
     * @param modifiers modifiers to look for
     * @return {@code true} if {@link Method} has none of the specified modifiers, {@code false} otherwise
     */
    public static boolean hasNoneOfModifiers ( final Method method, final ModifierType... modifiers )
    {
        return hasNoneOfModifiers ( method, new ImmutableList<ModifierType> ( modifiers ) );
    }

    /**
     * Returns whether or not {@link Method} has none of the specified modifiers.
     *
     * @param method    {@link Method} to check modifiers for
     * @param modifiers modifiers to look for
     * @return {@code true} if {@link Method} has none of the specified modifiers, {@code false} otherwise
     */
    public static boolean hasNoneOfModifiers ( final Method method, final Collection<ModifierType> modifiers )
    {
        return !hasAnyOfModifiers ( method, modifiers );
    }

    /**
     * Returns whether or not {@link Field} has any of the specified modifiers.
     *
     * @param field     {@link Field} to check modifiers for
     * @param modifiers modifiers to look for
     * @return {@code true} if {@link Field} has any of the specified modifiers, {@code false} otherwise
     */
    public static boolean hasAnyOfModifiers ( final Field field, final ModifierType... modifiers )
    {
        return hasAnyOfModifiers ( field, new ImmutableList<ModifierType> ( modifiers ) );
    }

    /**
     * Returns whether or not {@link Field} has any of the specified modifiers.
     *
     * @param field     {@link Field} to check modifiers for
     * @param modifiers modifiers to look for
     * @return {@code true} if {@link Field} has any of the specified modifiers, {@code false} otherwise
     */
    public static boolean hasAnyOfModifiers ( final Field field, final Collection<ModifierType> modifiers )
    {
        boolean contains = false;
        if ( CollectionUtils.notEmpty ( modifiers ) )
        {
            for ( final ModifierType modifier : modifiers )
            {
                if ( modifier.is ( field ) )
                {
                    contains = true;
                    break;
                }
            }
        }
        return contains;
    }

    /**
     * Returns whether or not {@link Field} has all of the specified modifiers.
     *
     * @param field     {@link Field} to check modifiers for
     * @param modifiers modifiers to look for
     * @return {@code true} if {@link Field} has all of the specified modifiers, {@code false} otherwise
     */
    public static boolean hasAllOfModifiers ( final Field field, final ModifierType... modifiers )
    {
        return hasAllOfModifiers ( field, new ImmutableList<ModifierType> ( modifiers ) );
    }

    /**
     * Returns whether or not {@link Field} has all of the specified modifiers.
     *
     * @param field     {@link Field} to check modifiers for
     * @param modifiers modifiers to look for
     * @return {@code true} if {@link Field} has all of the specified modifiers, {@code false} otherwise
     */
    public static boolean hasAllOfModifiers ( final Field field, final Collection<ModifierType> modifiers )
    {
        boolean contains = true;
        if ( ArrayUtils.notEmpty ( modifiers ) )
        {
            for ( final ModifierType modifier : modifiers )
            {
                if ( modifier.not ( field ) )
                {
                    contains = false;
                    break;
                }
            }
        }
        return contains;
    }

    /**
     * Returns whether or not {@link Field} has none of the specified modifiers.
     *
     * @param field     {@link Field} to check modifiers for
     * @param modifiers modifiers to look for
     * @return {@code true} if {@link Field} has none of the specified modifiers, {@code false} otherwise
     */
    public static boolean hasNoneOfModifiers ( final Field field, final ModifierType... modifiers )
    {
        return hasNoneOfModifiers ( field, new ImmutableList<ModifierType> ( modifiers ) );
    }

    /**
     * Returns whether or not {@link Field} has none of the specified modifiers.
     *
     * @param field     {@link Field} to check modifiers for
     * @param modifiers modifiers to look for
     * @return {@code true} if {@link Field} has none of the specified modifiers, {@code false} otherwise
     */
    public static boolean hasNoneOfModifiers ( final Field field, final Collection<ModifierType> modifiers )
    {
        return !hasAnyOfModifiers ( field, modifiers );
    }

    /**
     * Returns specified class field.
     * This method will also look for the field in super-classes if any exist.
     *
     * @param classType type of the class where field can be located
     * @param fieldName field name
     * @return specified class field
     */
    public static Field getFieldSafely ( final Class classType, final String fieldName )
    {
        try
        {
            return getField ( classType, fieldName );
        }
        catch ( final NoSuchFieldException e )
        {
            if ( safeMethodsLoggingEnabled )
            {
                final String msg = "ReflectionUtils method failed: getFieldSafely ( %s, %s )";
                LoggerFactory.getLogger ( ReflectUtils.class ).error ( String.format ( msg, classType, fieldName ), e );
            }
            return null;
        }
    }

    /**
     * Returns specified class field.
     * If field is not found in the object class all superclasses will be searched for that field.
     * This method will also find {@code protected}, {@code private} and package local fields.
     *
     * @param classType type of the class where field can be located
     * @param fieldName field name
     * @return specified class field
     * @throws NoSuchFieldException if field was not found
     */
    public static Field getField ( final Class classType, final String fieldName ) throws NoSuchFieldException
    {
        // Field key
        final String canonicalName = classType.getCanonicalName ();
        final String key = canonicalName + "." + fieldName;

        // Checking cache existence
        Field field = null;
        Map<String, Field> classFieldsCache = fieldsLookupCache.get ( classType );
        if ( classFieldsCache != null )
        {
            field = classFieldsCache.get ( key );
        }
        else
        {
            classFieldsCache = new HashMap<String, Field> ( 1 );
            fieldsLookupCache.put ( classType, classFieldsCache );
        }

        // Updating cache
        if ( field == null )
        {
            // Trying to retrieve field from class or one of its superclasses
            field = getFieldImpl ( classType, fieldName );

            // Trying to retrieve static field from interface
            if ( field == null )
            {
                field = getInterfaceFieldImpl ( classType, fieldName );
            }

            // Checking field existence
            if ( field != null )
            {
                field.setAccessible ( true );
            }
            else
            {
                final String msg = "Field '%s' not found in class: %s";
                throw new NoSuchFieldException ( String.format ( msg, fieldName, canonicalName ) );
            }

            // Caching field
            classFieldsCache.put ( key, field );
        }

        return field;
    }

    /**
     * Returns specified class field.
     * This method will also look for the field in super-classes if any exist.
     *
     * @param classType type of the class where field can be located
     * @param fieldName field name
     * @return specified class field
     */
    private static Field getFieldImpl ( final Class classType, final String fieldName )
    {
        Field field;
        try
        {
            field = classType.getDeclaredField ( fieldName );
        }
        catch ( final NoSuchFieldException e )
        {
            final Class superclass = classType.getSuperclass ();
            field = superclass != null ? getFieldImpl ( superclass, fieldName ) : null;
        }
        return field;
    }

    /**
     * Returns specified class interface static field.
     * This method will also look for the field in super-class interfaces if any exist.
     *
     * @param classType type of the interface where field can be located
     * @param fieldName field name
     * @return specified class interface static field
     */
    private static Field getInterfaceFieldImpl ( final Class classType, final String fieldName )
    {
        Field field = null;
        if ( classType.isInterface () )
        {
            final Field[] fields = classType.getDeclaredFields ();
            for ( final Field f : fields )
            {
                if ( f.getName ().equals ( fieldName ) )
                {
                    field = f;
                    break;
                }
            }
        }
        if ( field == null )
        {
            final Class[] interfaces = classType.getInterfaces ();
            for ( final Class iface : interfaces )
            {
                field = getInterfaceFieldImpl ( iface, fieldName );
                if ( field != null )
                {
                    break;
                }
            }
        }
        if ( field == null )
        {
            final Class superclass = classType.getSuperclass ();
            field = superclass != null ? getInterfaceFieldImpl ( superclass, fieldName ) : null;
        }
        return field;
    }

    /**
     * Returns specified class field's type.
     * This method will also look for the field in super-classes if any exist.
     *
     * @param classType type of the class where field can be located
     * @param fieldName field name
     * @return specified class field's type
     */
    public static Class<?> getFieldTypeSafely ( final Class classType, final String fieldName )
    {
        try
        {
            return getFieldType ( classType, fieldName );
        }
        catch ( final NoSuchFieldException e )
        {
            if ( safeMethodsLoggingEnabled )
            {
                final String msg = "ReflectionUtils method failed: getFieldTypeSafely ( %s, %s )";
                LoggerFactory.getLogger ( ReflectUtils.class ).error ( String.format ( msg, classType, fieldName ), e );
            }
            return null;
        }
    }

    /**
     * Returns specified class field's type.
     * This method will also look for the field in super-classes if any exist.
     *
     * @param classType type of the class where field can be located
     * @param fieldName field name
     * @return specified class field's type
     * @throws NoSuchFieldException if field was not found
     */
    public static Class<?> getFieldType ( final Class classType, final String fieldName ) throws NoSuchFieldException
    {
        return getField ( classType, fieldName ).getType ();
    }

    /**
     * Applies specified value to object field.
     * This method allows to access and modify even private fields.
     *
     * @param object object instance
     * @param field  object field
     * @param value  field value
     * @return {@code true} if value was applied successfully, {@code false} otherwise
     */
    public static boolean setFieldValueSafely ( final Object object, final String field, final Object value )
    {
        try
        {
            setFieldValue ( object, field, value );
            return true;
        }
        catch ( final Exception e )
        {
            if ( safeMethodsLoggingEnabled )
            {
                final String msg = "ReflectionUtils method failed: setFieldValueSafely";
                LoggerFactory.getLogger ( ReflectUtils.class ).error ( msg, e );
            }
            return false;
        }
    }

    /**
     * Applies specified value to object field.
     * This method allows to access and modify even private fields.
     *
     * @param object    object instance
     * @param fieldName object field name
     * @param value     field value
     * @throws NoSuchFieldException   if field was not found
     * @throws IllegalAccessException if field is inaccessible
     */
    public static void setFieldValue ( final Object object, final String fieldName, final Object value )
            throws NoSuchFieldException, IllegalAccessException
    {
        // Retrieving actual field
        final Field actualField = getField ( object.getClass (), fieldName );

        // Applying field value
        setFieldValue ( object, actualField, value );
    }

    /**
     * Applies specified value to static class field.
     * This method allows to access and modify even private fields.
     *
     * @param classType type of the class where static field can be located
     * @param field     object field
     * @param value     field value
     * @return {@code true} if value was applied successfully, {@code false} otherwise
     */
    public static boolean setStaticFieldValueSafely ( final Class classType, final String field, final Object value )
    {
        try
        {
            setStaticFieldValue ( classType, field, value );
            return true;
        }
        catch ( final Exception e )
        {
            if ( safeMethodsLoggingEnabled )
            {
                final String msg = "ReflectionUtils method failed: setFieldValueSafely";
                LoggerFactory.getLogger ( ReflectUtils.class ).error ( msg, e );
            }
            return false;
        }
    }

    /**
     * Applies specified value to static class field.
     * This method allows to access and modify even private fields.
     *
     * @param classType type of the class where static field can be located
     * @param fieldName object field name
     * @param value     field value
     * @throws NoSuchFieldException   if field was not found
     * @throws IllegalAccessException if field is inaccessible
     */
    public static void setStaticFieldValue ( final Class classType, final String fieldName, final Object value )
            throws NoSuchFieldException, IllegalAccessException
    {
        // Retrieving actual field
        final Field actualField = getField ( classType, fieldName );

        // Applying field value
        setFieldValue ( null, actualField, value );
    }

    /**
     * Applies specified value to object field.
     * This method allows to access and modify even private object fields.
     *
     * @param object object instance
     * @param field  object field
     * @param value  field value
     * @return {@code true} if value was applied successfully, {@code false} otherwise
     */
    public static boolean setFieldValueSafely ( final Object object, final Field field, final Object value )
    {
        try
        {
            setFieldValue ( object, field, value );
            return true;
        }
        catch ( final Exception e )
        {
            if ( safeMethodsLoggingEnabled )
            {
                final String msg = "ReflectionUtils method failed: setFieldValueSafely";
                LoggerFactory.getLogger ( ReflectUtils.class ).error ( msg, e );
            }
            return false;
        }
    }

    /**
     * Applies specified value to object field.
     * This method allows to access and modify even private object fields.
     *
     * @param object object instance
     * @param field  object field
     * @param value  field value
     * @throws IllegalAccessException if field is inaccessible
     */
    public static void setFieldValue ( final Object object, final Field field, final Object value )
            throws IllegalAccessException
    {
        // Making field accessible
        if ( !field.isAccessible () )
        {
            field.setAccessible ( true );
        }

        // Removing final modifier if needed
        final int oldModifiers = field.getModifiers ();
        if ( ModifierType.FINAL.is ( oldModifiers ) )
        {
            setFieldModifiers ( field, oldModifiers & ~Modifier.FINAL );
        }

        // Updating field value
        field.set ( object, value );

        // Restoring final modifier if it was removed
        if ( ModifierType.FINAL.is ( oldModifiers ) )
        {
            setFieldModifiers ( field, oldModifiers );
        }
    }

    /**
     * Changes {@link Field} modifiers.
     * Be aware that this is not supported JDK feature and only used in some hacky cases.
     *
     * @param field     {@link Field}
     * @param modifiers new {@link Field} modifiers
     * @throws IllegalAccessException if field is inaccessible
     */
    private static void setFieldModifiers ( final Field field, final int modifiers ) throws IllegalAccessException
    {
        try
        {
            final Field mods = getField ( Field.class, "modifiers" );
            mods.set ( field, modifiers );
        }
        catch ( final NoSuchFieldException e )
        {
            throw new ReflectionException ( "Unable to update field modifiers: " + field + " -> " + modifiers );
        }
    }

    /**
     * Returns object field value.
     * This method allows to access even private object fields.
     *
     * @param object    object instance
     * @param fieldName object field name
     * @param <T>       field value type
     * @return object field value
     */
    public static <T> T getFieldValueSafely ( final Object object, final String fieldName )
    {
        try
        {
            return getFieldValue ( object, fieldName );
        }
        catch ( final Exception e )
        {
            if ( safeMethodsLoggingEnabled )
            {
                final String msg = "ReflectionUtils method failed: getFieldValueSafely";
                LoggerFactory.getLogger ( ReflectUtils.class ).error ( msg, e );
            }
            return null;
        }
    }

    /**
     * Returns object field value.
     * This method allows to access even private object fields.
     *
     * @param object    object instance
     * @param fieldName object field name
     * @param <T>       field value type
     * @return object field value
     * @throws NoSuchFieldException   if field was not found
     * @throws IllegalAccessException if field is inaccessible
     */
    public static <T> T getFieldValue ( final Object object, final String fieldName ) throws NoSuchFieldException, IllegalAccessException
    {
        final Field actualField = getField ( object.getClass (), fieldName );
        ModifierType.STATIC.checkNot ( actualField );
        return ( T ) actualField.get ( object );
    }

    /**
     * Returns static field value from the specified class.
     *
     * @param classType class type
     * @param fieldName class field name
     * @return static field value from the specified class
     */
    public static <T> T getStaticFieldValueSafely ( final Class classType, final String fieldName )
    {
        try
        {
            return getStaticFieldValue ( classType, fieldName );
        }
        catch ( final Exception e )
        {
            if ( safeMethodsLoggingEnabled )
            {
                final String msg = "ReflectionUtils method failed: getStaticFieldValueSafely";
                LoggerFactory.getLogger ( ReflectUtils.class ).error ( msg, e );
            }
            return null;
        }
    }

    /**
     * Returns static field value from the specified class.
     *
     * @param classType class type
     * @param fieldName class field name
     * @return static field value from the specified class
     * @throws NoSuchFieldException   if field was not found
     * @throws IllegalAccessException if field is inaccessible
     */
    public static <T> T getStaticFieldValue ( final Class classType, final String fieldName )
            throws NoSuchFieldException, IllegalAccessException
    {
        final Field actualField = getField ( classType, fieldName );
        ModifierType.STATIC.check ( actualField );
        return ( T ) actualField.get ( null );
    }

    /**
     * Returns class name with ".java" extension in the end.
     *
     * @param classObject object of class type
     * @return class name with ".java" extension in the end
     */
    public static String getJavaClassName ( final Object classObject )
    {
        return getJavaClassName ( classObject.getClass () );
    }

    /**
     * Returns class name with ".java" extension in the end.
     *
     * @param classType class type
     * @return class name with ".java" extension in the end
     */
    public static String getJavaClassName ( final Class classType )
    {
        return getClassName ( classType ) + ".java";
    }

    /**
     * Returns class name with ".class" extension in the end.
     *
     * @param classObject object of class type
     * @return class name with ".class" extension in the end
     */
    public static String getClassFileName ( final Object classObject )
    {
        return getClassFileName ( classObject.getClass () );
    }

    /**
     * Returns class name with ".class" extension in the end.
     *
     * @param classType class type
     * @return class name with ".class" extension in the end
     */
    public static String getClassFileName ( final Class classType )
    {
        return ReflectUtils.getClassName ( classType ) + ".class";
    }

    /**
     * Returns class name.
     *
     * @param classObject object of class type
     * @return class name
     */
    public static String getClassName ( final Object classObject )
    {
        return getClassName ( classObject.getClass () );
    }

    /**
     * Returns class name.
     *
     * @param classType class type
     * @return class name
     */
    public static String getClassName ( final Class classType )
    {
        final String canonicalName = classType.getCanonicalName ();
        final String fullName = canonicalName != null ? canonicalName : classType.toString ();
        final int dot = fullName.lastIndexOf ( "." );
        return dot != -1 ? fullName.substring ( dot + 1 ) : fullName;
    }

    /**
     * Returns class packages.
     *
     * @param classObject object of class type
     * @return class packages
     */
    public static String[] getClassPackages ( final Object classObject )
    {
        return getClassPackages ( classObject.getClass () );
    }

    /**
     * Returns class packages.
     *
     * @param classType class type
     * @return class packages
     */
    public static String[] getClassPackages ( final Class classType )
    {
        return getPackages ( classType.getPackage ().getName () );
    }

    /**
     * Returns packages names.
     *
     * @param packageName package name
     * @return packages names
     */
    public static String[] getPackages ( final String packageName )
    {
        return packageName.split ( "\\." );
    }

    /**
     * Returns newly created class instance.
     *
     * @param canonicalClassName canonical class name
     * @param arguments          class constructor arguments
     * @return newly created class instance
     */
    public static <T> T createInstanceSafely ( final String canonicalClassName, final Object... arguments )
    {
        try
        {
            return createInstance ( canonicalClassName, arguments );
        }
        catch ( final Exception e )
        {
            if ( safeMethodsLoggingEnabled )
            {
                final String msg = "ReflectionUtils method failed: createInstanceSafely";
                LoggerFactory.getLogger ( ReflectUtils.class ).error ( msg, e );
            }
            return null;
        }
    }

    /**
     * Returns newly created class instance.
     *
     * @param canonicalClassName canonical class name
     * @param arguments          class constructor arguments
     * @return newly created class instance
     * @throws ClassNotFoundException    if class was not found
     * @throws InvocationTargetException if method throws an exception
     * @throws IllegalAccessException    if method is inaccessible
     * @throws InstantiationException    if the class is abstract
     * @throws NoSuchMethodException     if method was not found
     */
    public static <T> T createInstance ( final String canonicalClassName, final Object... arguments )
            throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException
    {
        return createInstance ( loadClass ( canonicalClassName ), arguments );
    }

    /**
     * Returns newly created class instance.
     *
     * @param theClass  class to process
     * @param arguments class constructor arguments
     * @return newly created class instance
     */
    public static <T> T createInstanceSafely ( final Class theClass, final Object... arguments )
    {
        try
        {
            return createInstance ( theClass, arguments );
        }
        catch ( final Exception e )
        {
            if ( safeMethodsLoggingEnabled )
            {
                final String msg = "ReflectionUtils method failed: createInstanceSafely";
                LoggerFactory.getLogger ( ReflectUtils.class ).error ( msg, e );
            }
            return null;
        }
    }

    /**
     * Returns newly created class instance.
     *
     * @param theClass  class to process
     * @param arguments class constructor arguments
     * @return newly created class instance
     * @throws InstantiationException    if the class is abstract
     * @throws NoSuchMethodException     if method was not found
     * @throws InvocationTargetException if method throws an exception
     * @throws IllegalAccessException    if method is inaccessible
     */
    public static <T> T createInstance ( final Class theClass, final Object... arguments )
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        // Retrieving argument types
        final Class[] parameterTypes = getClassTypes ( arguments );

        // Retrieving constructor
        final Constructor constructor = getConstructor ( theClass, parameterTypes );

        // Creating new instance
        return ( T ) constructor.newInstance ( arguments );
    }

    /**
     * Returns class constructor for the specified argument types.
     * This method will also find {@code protected}, {@code private} and package local constructors.
     *
     * todo 1. Constructors priority check (by super types)
     * todo    Right now some constructor with [Object] arg might be used instead of constructor with [String]
     * todo    To avoid issues don't call constructors with same amount of arguments and which are cast-able to each other
     * todo 2. Vararg constructors might not be found in many cases
     * todo    Additional checks/workarounds for such constructors should be added to avoid issues
     *
     * @param theClass       class to process
     * @param parameterTypes constructor argument types
     * @return class constructor for the specified argument types
     * @throws NoSuchMethodException if constructor was not found
     */
    public static Constructor getConstructor ( final Class theClass, final Class... parameterTypes ) throws NoSuchMethodException
    {
        // This enhancement is a bad idea since protected/private constructor it won't be found
        /*// Simplified constructor search for empty parameters
        if ( parameterTypes.length == 0 )
        {
            return theClass.getConstructor ();
        }*/

        // This enhancement is a bad idea as it will return appropriate inner class constructor
        // but you will surely be disoriented outside of this call why you have an extra parement
        // and generally you won't be able to properly instantiate it without additional workarounds
        /*// Workaround for simplifying inner classes constructor retrieval
        final Class[] actualParameterTypes;
        if ( theClass.isMemberClass () && ModifierType.STATIC.not ( theClass ) )
        {
            actualParameterTypes = new Class[ parameterTypes.length + 1 ];
            actualParameterTypes[ 0 ] = theClass.getEnclosingClass ();
            System.arraycopy ( parameterTypes, 0, actualParameterTypes, 1, parameterTypes.length );
        }
        else
        {
            actualParameterTypes = parameterTypes;
        }*/

        // Special check for inner classes
        if ( theClass.isMemberClass () && ModifierType.STATIC.not ( theClass ) )
        {
            // Ensure first parameter is a type compatible with class enclosing specified inner class
            if ( parameterTypes.length == 0 )
            {
                // No parameters at all, it seems caller is not aware it is asking to find inner class constructor
                throw new ReflectionException ( "Enclosing class paramter for inner class constructor is missing" );
            }
            else if ( !isAssignable ( theClass.getEnclosingClass (), parameterTypes[ 0 ] ) )
            {
                // Inner's class enclosing class is not assignable from first parameter type
                throw new ReflectionException ( "Incorrect first parameter for inner class constructor" );
            }
        }

        // Constructors can be used only from the topmost class so we don't need to look for them in superclasses
        for ( final Constructor constructor : theClass.getDeclaredConstructors () )
        {
            // Retrieving constructor parameter types
            final Class[] types = constructor.getParameterTypes ();

            // Checking some simple cases first
            if ( types.length != parameterTypes.length )
            {
                // Inappropriate constructor
                continue;
            }
            else if ( types.length == 0 )
            {
                // Constructor with no parameters
                constructor.setAccessible ( true );
                return constructor;
            }

            // Checking parameter types
            boolean fits = true;
            for ( int i = 0; i < types.length; i++ )
            {
                if ( !isAssignable ( types[ i ], parameterTypes[ i ] ) )
                {
                    fits = false;
                    break;
                }
            }
            if ( fits )
            {
                constructor.setAccessible ( true );
                return constructor;
            }
        }

        // Throwing proper exception that constructor was not found
        throw new NoSuchMethodException ( "Constructor was not found: " +
                theClass.getCanonicalName () + argumentTypesToString ( parameterTypes ) );
    }

    /**
     * Returns result of called static method.
     * Will return null in case method is void-type.
     *
     * @param canonicalClassName canonical class name
     * @param methodName         static method name
     * @param arguments          method arguments
     * @return result of called static method
     */
    public static <T> T callStaticMethodSafely ( final String canonicalClassName, final String methodName, final Object... arguments )
    {
        try
        {
            return callStaticMethod ( canonicalClassName, methodName, arguments );
        }
        catch ( final Exception e )
        {
            if ( safeMethodsLoggingEnabled )
            {
                final String msg = "ReflectionUtils method failed: callStaticMethodSafely";
                LoggerFactory.getLogger ( ReflectUtils.class ).error ( msg, e );
            }
            return null;
        }
    }

    /**
     * Returns result of called static method.
     * Will return null in case method is void-type.
     *
     * @param canonicalClassName canonical class name
     * @param methodName         static method name
     * @param arguments          method arguments
     * @return result of called static method
     * @throws ClassNotFoundException    if class was not found
     * @throws NoSuchMethodException     if method was not found
     * @throws InvocationTargetException if method throws an exception
     * @throws IllegalAccessException    if method is inaccessible
     */
    public static <T> T callStaticMethod ( final String canonicalClassName, final String methodName, final Object... arguments )
            throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException
    {
        return callStaticMethod ( getClass ( canonicalClassName ), methodName, arguments );
    }

    /**
     * Returns result of called static method.
     * Will return null in case method is void-type.
     *
     * @param theClass   class to process
     * @param methodName static method name
     * @param arguments  method arguments
     * @return result of called static method
     */
    public static <T> T callStaticMethodSafely ( final Class theClass, final String methodName, final Object... arguments )
    {
        try
        {
            return callStaticMethod ( theClass, methodName, arguments );
        }
        catch ( final Exception e )
        {
            if ( safeMethodsLoggingEnabled )
            {
                final String msg = "ReflectionUtils method failed: callStaticMethodSafely";
                LoggerFactory.getLogger ( ReflectUtils.class ).error ( msg, e );
            }
            return null;
        }
    }

    /**
     * Returns result of called static method.
     * Will return null in case method is void-type.
     *
     * @param theClass   class to process
     * @param methodName static method name
     * @param arguments  static method arguments
     * @return result given by called static method
     * @throws NoSuchMethodException     if method was not found
     * @throws InvocationTargetException if method throws an exception
     * @throws IllegalAccessException    if method is inaccessible
     */
    public static <T> T callStaticMethod ( final Class theClass, final String methodName, final Object... arguments )
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        final Method method = getMethod ( theClass, methodName, arguments );
        return ( T ) method.invoke ( null, arguments );
    }

    /**
     * Returns list of results returned by called methods.
     *
     * @param objects    objects to call methods on
     * @param methodName method name
     * @param arguments  method arguments
     * @return list of results returned by called methods
     */
    public static <T> List<T> callMethodsSafely ( final List objects, final String methodName, final Object... arguments )
    {
        try
        {
            return callMethods ( objects, methodName, arguments );
        }
        catch ( final Exception e )
        {
            if ( safeMethodsLoggingEnabled )
            {
                final String msg = "ReflectionUtils method failed: callMethodsSafely";
                LoggerFactory.getLogger ( ReflectUtils.class ).error ( msg, e );
            }
            return null;
        }
    }

    /**
     * Returns list of results returned by called methods.
     *
     * @param objects    objects to call methods on
     * @param methodName method name
     * @param arguments  method arguments
     * @return list of results returned by called methods
     * @throws NoSuchMethodException     if method was not found
     * @throws InvocationTargetException if method throws an exception
     * @throws IllegalAccessException    if method is inaccessible
     */
    public static <T> List<T> callMethods ( final List objects, final String methodName, final Object... arguments )
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        final List<T> results = new ArrayList<T> ();
        for ( final Object object : objects )
        {
            results.add ( ( T ) callMethod ( object, methodName, arguments ) );
        }
        return results;
    }

    /**
     * Returns an array of results returned by called methods.
     *
     * @param objects    objects to call methods on
     * @param methodName method name
     * @param arguments  method arguments
     * @return an array of results returned by called methods
     */
    public static Object[] callMethodsSafely ( final Object[] objects, final String methodName, final Object... arguments )
    {
        try
        {
            return callMethods ( objects, methodName, arguments );
        }
        catch ( final Exception e )
        {
            if ( safeMethodsLoggingEnabled )
            {
                final String msg = "ReflectionUtils method failed: callMethodsSafely";
                LoggerFactory.getLogger ( ReflectUtils.class ).error ( msg, e );
            }
            return null;
        }
    }

    /**
     * Returns an array of results returned by called methods.
     *
     * @param objects    objects to call methods on
     * @param methodName method name
     * @param arguments  method arguments
     * @return an array of results returned by called methods
     * @throws NoSuchMethodException     if method was not found
     * @throws InvocationTargetException if method throws an exception
     * @throws IllegalAccessException    if method is inaccessible
     */
    public static Object[] callMethods ( final Object[] objects, final String methodName, final Object... arguments )
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        final Object[] results = new Object[ objects.length ];
        for ( int i = 0; i < objects.length; i++ )
        {
            results[ i ] = callMethod ( objects[ i ], methodName, arguments );
        }
        return results;
    }

    /**
     * Returns result given by called method.
     *
     * @param object     object instance
     * @param methodName method name
     * @param arguments  method arguments
     * @return result given by called method
     */
    public static <T> T callMethodSafely ( final Object object, final String methodName, final Object... arguments )
    {
        try
        {
            return callMethod ( object, methodName, arguments );
        }
        catch ( final Exception e )
        {
            if ( safeMethodsLoggingEnabled )
            {
                final String msg = "ReflectionUtils method failed: callMethodSafely";
                LoggerFactory.getLogger ( ReflectUtils.class ).error ( msg, e );
            }
            return null;
        }
    }

    /**
     * Calls object's method with the specified name and arguments.
     * If method is not found in the object class all superclasses will be searched for that method.
     * Returns result given by called method.
     *
     * @param object     object instance
     * @param methodName method name
     * @param arguments  method arguments
     * @return result given by called method
     * @throws NoSuchMethodException       if method was not found
     * @throws InvocationTargetException   if method throws an exception
     * @throws IllegalAccessException      if method is inaccessible
     * @throws ExceptionInInitializerError if the initialization provoked by this method fails
     */
    public static <T> T callMethod ( final Object object, final String methodName, final Object... arguments )
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        final Method method = getMethod ( object.getClass (), methodName, arguments );
        return ( T ) method.invoke ( object, arguments );
    }

    /**
     * Returns field getter method by popular method naming pattern.
     * Basically those are "getFieldName"-like and "isFieldName"-like method names.
     *
     * @param object object
     * @param field  field name
     * @return field getter method by popular method naming pattern
     */
    public static Method getFieldGetter ( final Object object, final String field )
    {
        return getFieldGetter ( object.getClass (), field );
    }

    /**
     * Returns field getter method by popular method naming pattern.
     * Basically those are "getFieldName"-like and "isFieldName"-like method names.
     *
     * @param aClass object class
     * @param field  field name
     * @return field getter method by popular method naming pattern
     */
    public static Method getFieldGetter ( final Class aClass, final String field )
    {
        // Look for "get" method
        final Method get = getMethodSafely ( aClass, getGetterMethodName ( field ) );
        if ( get != null )
        {
            // Return "get" method
            return get;
        }
        else
        {
            // Return "is" method
            return getMethodSafely ( aClass, getIsGetterMethodName ( field ) );
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
        return "set" + field.substring ( 0, 1 ).toUpperCase ( Locale.ROOT ) + field.substring ( 1 );
    }

    /**
     * Returns getter method name for the specified field.
     *
     * @param field field name
     * @return getter method name for the specified field
     */
    public static String getGetterMethodName ( final String field )
    {
        return "get" + field.substring ( 0, 1 ).toUpperCase ( Locale.ROOT ) + field.substring ( 1 );
    }

    /**
     * Returns "is" getter method name for the specified field.
     *
     * @param field field name
     * @return "is" getter method name for the specified field
     */
    public static String getIsGetterMethodName ( final String field )
    {
        return "is" + field.substring ( 0, 1 ).toUpperCase ( Locale.ROOT ) + field.substring ( 1 );
    }

    /**
     * Returns whether method with the specified name and arguments exists in the specified object.
     * If method is not found in the object class all superclasses will be searched for that method.
     *
     * @param object     object
     * @param methodName method name
     * @param arguments  method arguments
     * @return {@code true} if method with the specified name and arguments exists in the specified object, {@code false} otherwise
     */
    public static boolean hasMethod ( final Object object, final String methodName, final Object... arguments )
    {
        return hasMethod ( object.getClass (), methodName, arguments );
    }

    /**
     * Returns whether method with the specified name and arguments exists in the specified class.
     * If method is not found in the object class all superclasses will be searched for that method.
     *
     * @param aClass     object class
     * @param methodName method name
     * @param arguments  method arguments
     * @return {@code true} if method with the specified name and arguments exists in the specified class, {@code false} otherwise
     */
    public static boolean hasMethod ( final Class aClass, final String methodName, final Object... arguments )
    {
        try
        {
            return getMethod ( aClass, methodName, arguments ) != null;
        }
        catch ( final Exception e )
        {
            return false;
        }
    }

    /**
     * Calls object's method with the specified name and arguments.
     * If method is not found in the object class all superclasses will be searched for that method.
     * Returns result given by called method.
     *
     * @param object     object
     * @param methodName method name
     * @param arguments  method arguments
     * @return result given by called method
     */
    public static Method getMethodSafely ( final Object object, final String methodName, final Object... arguments )
    {
        return getMethodSafely ( object.getClass (), methodName, arguments );
    }

    /**
     * Returns object's method with the specified name and arguments.
     * If method is not found in the object class all superclasses will be searched for that method.
     *
     * @param aClass     object class
     * @param methodName method name
     * @param arguments  method arguments
     * @return object's method with the specified name and arguments
     */
    public static Method getMethodSafely ( final Class aClass, final String methodName, final Object... arguments )
    {
        try
        {
            return getMethod ( aClass, methodName, arguments );
        }
        catch ( final Exception e )
        {
            if ( safeMethodsLoggingEnabled )
            {
                final String msg = "ReflectionUtils method failed: getMethodSafely";
                LoggerFactory.getLogger ( ReflectUtils.class ).error ( msg, e );
            }
            return null;
        }
    }

    /**
     * Returns object's method with the specified name and arguments.
     * If method is not found in the object class all superclasses will be searched for that method.
     *
     * @param object     object
     * @param methodName method name
     * @param arguments  method arguments
     * @return object's method with the specified name and arguments
     * @throws NoSuchMethodException     if method was not found
     * @throws InvocationTargetException if method throws an exception
     * @throws IllegalAccessException    if method is inaccessible
     */
    public static Method getMethod ( final Object object, final String methodName, final Object... arguments )
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        return getMethod ( object.getClass (), methodName, arguments );
    }

    /**
     * Returns object's method with the specified name and arguments.
     * If method is not found in the object class all superclasses will be searched for that method.
     * This method will also find {@code protected}, {@code private} and package local methods.
     *
     * todo 1. Methods priority check (by super types)
     * todo    Right now some method with [Object] arg might be used instead of method with [String]
     * todo    To avoid issues don't call methods with same amount of arguments and which are cast-able to each other
     * todo 2. Vararg methods might not be found in many cases
     * todo    Additional checks/workarounds for such methods should be added to avoid issues
     *
     * @param aClass     object class
     * @param methodName method name
     * @param arguments  method arguments
     * @return object's method with the specified name and arguments
     * @throws NoSuchMethodException     if method was not found
     * @throws InvocationTargetException if method throws an exception
     * @throws IllegalAccessException    if method is inaccessible
     */
    public static Method getMethod ( final Class aClass, final String methodName, final Object... arguments )
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        // Method key
        final Class[] classTypes = getClassTypes ( arguments );
        final String key = aClass.getCanonicalName () + "." + methodName + argumentTypesToString ( classTypes );

        // Checking cache
        Method method = null;
        Map<String, Method> classMethodsCache = methodsLookupCache.get ( aClass );
        if ( classMethodsCache != null )
        {
            method = classMethodsCache.get ( key );
        }
        else
        {
            classMethodsCache = new HashMap<String, Method> ( 1 );
            methodsLookupCache.put ( aClass, classMethodsCache );
        }

        // Updating cache
        if ( method == null )
        {
            method = getMethodImpl ( aClass, methodName, arguments );
            classMethodsCache.put ( key, method );
        }

        return method;
    }

    /**
     * Returns object's method with the specified name and arguments.
     * If method is not found in the object class all superclasses will be searched for that method.
     *
     * @param aClass     object class
     * @param methodName method name
     * @param arguments  method arguments
     * @return object's method with the specified name and arguments
     * @throws NoSuchMethodException     if method was not found
     * @throws InvocationTargetException if method throws an exception
     * @throws IllegalAccessException    if method is inaccessible
     */
    private static Method getMethodImpl ( final Class aClass, final String methodName, final Object[] arguments )
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        // This enhancement was a bad idea and was disabled
        // In case method is protected/private or located in one of superclasses it won't be found
        //        if ( arguments.length == 0 )
        //        {
        //            // Searching simple method w/o arguments
        //            method = aClass.getMethod ( methodName );
        //            method.setAccessible ( true );
        //        }

        // Searching for more complex method
        final Class[] types = getClassTypes ( arguments );
        return getMethodImpl ( aClass, aClass, methodName, types );
    }

    /**
     * Returns object's method with the specified name and arguments.
     * If method is not found in the object class all superclasses will be searched for that method.
     *
     * @param topClass     initial object class
     * @param currentClass object class we are looking in for the method
     * @param methodName   method name
     * @param types        method argument types
     * @return object's method with the specified name and arguments
     * @throws NoSuchMethodException if method was not found
     */
    private static Method getMethodImpl ( final Class topClass, final Class currentClass, final String methodName, final Class[] types )
            throws NoSuchMethodException
    {
        // Searching for the specified method in object's class or one of its superclasses
        for ( final Method method : currentClass.getDeclaredMethods () )
        {
            // Checking method name
            if ( method.getName ().equals ( methodName ) )
            {
                // Checking method arguments count
                final Class<?>[] mt = method.getParameterTypes ();
                if ( mt.length == types.length )
                {
                    // Checking that arguments fit
                    boolean fits = true;
                    for ( int i = 0; i < mt.length; i++ )
                    {
                        if ( !isAssignable ( mt[ i ], types[ i ] ) )
                        {
                            fits = false;
                            break;
                        }
                    }
                    if ( fits )
                    {
                        // Returning found method
                        method.setAccessible ( true );
                        return method;
                    }
                }
            }
        }

        // Search object superclass for this method
        final Class superclass = currentClass.getSuperclass ();
        if ( superclass != null )
        {
            return getMethodImpl ( topClass, superclass, methodName, types );
        }

        // Throwing proper method not found exception
        throw new NoSuchMethodException ( "Method was not found: " +
                topClass.getCanonicalName () + "." + methodName + argumentTypesToString ( types ) );
    }

    /**
     * Returns text representation for array of argument types.
     *
     * @param argTypes argument types
     * @return text representation for array of argument types
     */
    private static String argumentTypesToString ( final Class[] argTypes )
    {
        final StringBuilder buf = new StringBuilder ( "(" );
        if ( argTypes != null )
        {
            for ( int i = 0; i < argTypes.length; i++ )
            {
                if ( i > 0 )
                {
                    buf.append ( ", " );
                }
                final Class c = argTypes[ i ];
                buf.append ( c == null ? "null" : c.getCanonicalName () );
            }
        }
        return buf.append ( ")" ).toString ();
    }

    /**
     * Returns cloned object.
     *
     * @param object object to clone
     * @param <T>    cloned object type
     * @return cloned object
     * @throws NoSuchMethodException     if method was not found
     * @throws InvocationTargetException if method throws an exception
     * @throws IllegalAccessException    if method is inaccessible
     */
    public static <T extends Cloneable> T clone ( final T object )
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        return object != null ? ( T ) ReflectUtils.callMethod ( object, "clone" ) : null;
    }

    /**
     * Returns cloned object.
     *
     * @param object object to clone
     * @param <T>    cloned object type
     * @return cloned object
     */
    public static <T extends Cloneable> T cloneSafely ( final T object )
    {
        return object != null ? ( T ) ReflectUtils.callMethodSafely ( object, "clone" ) : null;
    }

    /**
     * Returns class loaded for the specified canonical class name.
     *
     * @param canonicalClassName canonical class name
     * @return class loaded for the specified canonical class name
     * @throws ClassNotFoundException if class was not found
     */
    public static Class loadClass ( final String canonicalClassName ) throws ClassNotFoundException
    {
        return ReflectUtils.class.getClassLoader ().loadClass ( canonicalClassName );
    }

    /**
     * Returns an array of argument class types.
     *
     * @param arguments arguments to process
     * @return an array of argument class types
     */
    public static Class[] getClassTypes ( final Object[] arguments )
    {
        final Class[] parameterTypes = new Class[ arguments.length ];
        for ( int i = 0; i < arguments.length; i++ )
        {
            parameterTypes[ i ] = arguments[ i ] != null ? arguments[ i ].getClass () : null;
        }
        return parameterTypes;
    }

    /**
     * Returns whether first type is assignable from second one or not.
     *
     * @param type checked whether is assignable, always not null
     * @param from checked type, might be null
     * @return {@code true} if first type is assignable from second one, {@code false} otherwise
     */
    public static boolean isAssignable ( final Class type, final Class from )
    {
        if ( from == null )
        {
            return !type.isPrimitive ();
        }
        else if ( type.isAssignableFrom ( from ) )
        {
            return true;
        }
        else if ( type.isPrimitive () )
        {
            if ( type == boolean.class )
            {
                return Boolean.class.isAssignableFrom ( from );
            }
            else if ( type == int.class )
            {
                return Integer.class.isAssignableFrom ( from );
            }
            else if ( type == char.class )
            {
                return Character.class.isAssignableFrom ( from );
            }
            else if ( type == byte.class )
            {
                return Byte.class.isAssignableFrom ( from );
            }
            else if ( type == short.class )
            {
                return Short.class.isAssignableFrom ( from );
            }
            else if ( type == long.class )
            {
                return Long.class.isAssignableFrom ( from );
            }
            else if ( type == float.class )
            {
                return Float.class.isAssignableFrom ( from );
            }
            else if ( type == double.class )
            {
                return Double.class.isAssignableFrom ( from );
            }
            else if ( type == void.class )
            {
                return Void.class.isAssignableFrom ( from );
            }
        }
        return false;
    }

    /**
     * Returns whether or not specified object has primitive type.
     * Specified {@code object} must never be {@code null}.
     *
     * @param object object to check
     * @return {@code true} if specified object has primitive type, {@code false} otherwise
     */
    public static boolean isPrimitive ( final Object object )
    {
        return isPrimitive ( object.getClass () );
    }

    /**
     * Returns whether or not specified class type is primitive.
     * Specified {@code clazz} must never be {@code null}.
     *
     * @param type class type to check
     * @return {@code true} if specified class type is primitive, {@code false} otherwise
     */
    public static boolean isPrimitive ( final Class<?> type )
    {
        return type.isPrimitive () ||
                Boolean.class.isAssignableFrom ( type ) ||
                Character.class.isAssignableFrom ( type ) ||
                Byte.class.isAssignableFrom ( type ) ||
                Short.class.isAssignableFrom ( type ) ||
                Integer.class.isAssignableFrom ( type ) ||
                Long.class.isAssignableFrom ( type ) ||
                Float.class.isAssignableFrom ( type ) ||
                Double.class.isAssignableFrom ( type ) ||
                Void.class.isAssignableFrom ( type );
    }

    /**
     * Returns default primitive type value.
     *
     * @param type primitive class type
     * @return default primitive type value
     */
    public static Object getDefaultPrimitiveValue ( final Class<?> type )
    {
        if ( type.isPrimitive () )
        {
            if ( type == boolean.class )
            {
                return false;
            }
            else if ( type == int.class )
            {
                return 0;
            }
            else if ( type == char.class )
            {
                return '\u0000';
            }
            else if ( type == byte.class )
            {
                return ( byte ) 0;
            }
            else if ( type == short.class )
            {
                return ( short ) 0;
            }
            else if ( type == long.class )
            {
                return 0L;
            }
            else if ( type == float.class )
            {
                return 0.0f;
            }
            else if ( type == double.class )
            {
                return 0.0d;
            }
            else
            {
                throw new IllegalArgumentException ( "Unknown primitive type: " + type );
            }
        }
        else
        {
            throw new IllegalArgumentException ( "Type is not primitive: " + type );
        }
    }

    /**
     * Returns whether one of superclasses contains specified text in its name or not.
     *
     * @param theClass class to process
     * @param text     text to search for
     * @return {@code true} if one of superclasses contains specified text in its name, {@code false} otherwise
     */
    public static boolean containsInClassOrSuperclassName ( final Class theClass, final String text )
    {
        if ( theClass == null )
        {
            return false;
        }
        final String name = theClass.getCanonicalName ();
        if ( name != null )
        {
            return name.contains ( text ) || containsInClassOrSuperclassName ( theClass.getSuperclass (), text );
        }
        else
        {
            return containsInClassOrSuperclassName ( theClass.getSuperclass (), text );
        }
    }

    /**
     * Returns closest superclass for both of the specified classes.
     *
     * @param object1 first object to retrieve {@link Class} of
     * @param object2 second object to retrieve {@link Class} of
     * @return closest superclass for both of the specified classes
     */
    public static Class getClosestSuperclass ( final Object object1, final Object object2 )
    {
        return getClosestSuperclass ( object1.getClass (), object2.getClass () );
    }

    /**
     * Returns closest super {@link Class} for both of the specified {@link Class}es.
     *
     * @param class1 first {@link Class}
     * @param class2 second {@link Class}
     * @return closest super {@link Class} for both of the specified {@link Class}es
     */
    public static Class getClosestSuperclass ( final Class class1, final Class class2 )
    {
        if ( class1.isAssignableFrom ( class2 ) )
        {
            return class1;
        }
        else if ( class2.isAssignableFrom ( class1 ) )
        {
            return class2;
        }
        else
        {
            final Class super1 = class1.getSuperclass ();
            final Class super2 = class2.getSuperclass ();
            return getClosestSuperclass ( super1, super2 );
        }
    }
}