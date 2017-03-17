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

import com.alee.managers.log.Log;
import com.alee.utils.reflection.ModifierType;

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
     * @return class for the specified canonical name
     */
    public static Class getClassSafely ( final String canonicalName )
    {
        try
        {
            return getClass ( canonicalName );
        }
        catch ( final Throwable e )
        {
            if ( safeMethodsLoggingEnabled )
            {
                Log.warn ( "ReflectionUtils method failed: getClassSafely ( " + canonicalName + " )", e );
            }
            return null;
        }
    }

    /**
     * Returns class for the specified canonical name.
     *
     * @param canonicalName class canonical name
     * @return class for the specified canonical name
     * @throws java.lang.ClassNotFoundException if class was not found
     */
    public static <T> Class<T> getClass ( final String canonicalName ) throws ClassNotFoundException
    {
        return ( Class<T> ) Class.forName ( canonicalName );
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
        catch ( final Throwable e )
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
     * Returns all non-static fields declared in the specified class and all of its superclasses.
     *
     * @param object object or class to find declared non-static fields for
     * @return all non-static fields declared in the specified class and all of its superclasses
     */
    public static List<Field> getFields ( final Object object )
    {
        return getFields ( object, new ArrayList<String> () );
    }

    /**
     * Returns all non-static fields declared in the specified class and all of its superclasses.
     *
     * @param object object or class to find declared non-static fields for
     * @param found  found field names
     * @return all non-static fields declared in the specified class and all of its superclasses
     */
    public static List<Field> getFields ( final Object object, final List<String> found )
    {
        if ( object instanceof Class )
        {
            // Find all current-level fields
            final Class clazz = ( Class ) object;
            final Field[] fields = clazz.getDeclaredFields ();
            final List<Field> filtered = new ArrayList<Field> ( fields.length );
            for ( final Field field : fields )
            {
                final int modifiers = field.getModifiers ();
                if ( !found.contains ( field.getName () ) && !Modifier.isStatic ( modifiers ) )
                {
                    filtered.add ( field );
                    found.add ( field.getName () );
                }
            }

            // Find all superclass fields
            final Class superclass = clazz.getSuperclass ();
            if ( superclass != null )
            {
                filtered.addAll ( getFields ( superclass, found ) );
            }

            return filtered;
        }
        else
        {
            return getFields ( object.getClass (), found );
        }
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
                Log.warn ( "ReflectionUtils method failed: getFieldSafely ( " + classType + ", " + fieldName + " )", e );
            }
            return null;
        }
    }

    /**
     * Returns specified class field.
     * This method will also look for the field in super-classes if any exist.
     *
     * @param classType type of the class where field can be located
     * @param fieldName field name
     * @return specified class field
     * @throws java.lang.NoSuchFieldException if field was not found
     */
    public static Field getField ( final Class classType, final String fieldName ) throws NoSuchFieldException
    {
        // Field key
        final String key = classType.getCanonicalName () + "." + fieldName;

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
                throw new NoSuchFieldException ( "Field \"" + fieldName + "\" not found in class: " + classType.getCanonicalName () );
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
                Log.warn ( "ReflectionUtils method failed: getFieldTypeSafely ( " + classType + ", " + fieldName + " )", e );
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
     * @throws java.lang.NoSuchFieldException if field was not found
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
     * @return true if value was applied successfully, false otherwise
     */
    public static boolean setFieldValueSafely ( final Object object, final String field, final Object value )
    {
        try
        {
            setFieldValue ( object, field, value );
            return true;
        }
        catch ( final Throwable e )
        {
            if ( safeMethodsLoggingEnabled )
            {
                Log.warn ( "ReflectionUtils method failed: setFieldValueSafely", e );
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
     * @throws java.lang.NoSuchFieldException   if field was not found
     * @throws java.lang.IllegalAccessException if field is inaccessible
     */
    public static void setFieldValue ( final Object object, final String fieldName, final Object value )
            throws NoSuchFieldException, IllegalAccessException
    {
        setFieldValue ( object.getClass (), object, fieldName, value );
    }

    /**
     * Applies specified value to static class field.
     * This method allows to access and modify even private fields.
     *
     * @param classType type of the class where static field can be located
     * @param field     object field
     * @param value     field value
     * @return true if value was applied successfully, false otherwise
     */
    public static boolean setStaticFieldValueSafely ( final Class classType, final String field, final Object value )
    {
        try
        {
            setStaticFieldValue ( classType, field, value );
            return true;
        }
        catch ( final Throwable e )
        {
            if ( safeMethodsLoggingEnabled )
            {
                Log.warn ( "ReflectionUtils method failed: setFieldValueSafely", e );
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
     * @throws java.lang.NoSuchFieldException   if field was not found
     * @throws java.lang.IllegalAccessException if field is inaccessible
     */
    public static void setStaticFieldValue ( final Class classType, final String fieldName, final Object value )
            throws NoSuchFieldException, IllegalAccessException
    {
        setFieldValue ( classType, null, fieldName, value );
    }

    /**
     * Applies specified value to object field.
     * This method allows to access and modify even private object fields.
     *
     * @param classType type of the class where field can be located
     * @param object    object instance
     * @param fieldName object field name
     * @param value     field value
     * @throws java.lang.NoSuchFieldException   if field was not found
     * @throws java.lang.IllegalAccessException if field is inaccessible
     */
    private static void setFieldValue ( final Class classType, final Object object, final String fieldName, final Object value )
            throws NoSuchFieldException, IllegalAccessException
    {
        final Field actualField = getField ( classType, fieldName );

        final int oldModifiers = actualField.getModifiers ();
        if ( ModifierType.FINAL.is ( oldModifiers ) )
        {
            final Field modifiers = getField ( Field.class, "modifiers" );
            modifiers.set ( actualField, oldModifiers & ~Modifier.FINAL );
        }

        actualField.set ( object, value );

        if ( ModifierType.FINAL.is ( oldModifiers ) )
        {
            final Field modifiers = getField ( Field.class, "modifiers" );
            modifiers.set ( actualField, oldModifiers );
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
        catch ( final Throwable e )
        {
            if ( safeMethodsLoggingEnabled )
            {
                Log.warn ( "ReflectionUtils method failed: getFieldValueSafely", e );
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
     * @throws java.lang.NoSuchFieldException   if field was not found
     * @throws java.lang.IllegalAccessException if field is inaccessible
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
        catch ( final Throwable e )
        {
            if ( safeMethodsLoggingEnabled )
            {
                Log.warn ( "ReflectionUtils method failed: getStaticFieldValueSafely", e );
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
     * @throws java.lang.NoSuchFieldException   if field was not found
     * @throws java.lang.IllegalAccessException if field is inaccessible
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
     * Returns inner class with the specified name.
     *
     * @param fromClass      class to look for the inner class
     * @param innerClassName inner class name
     * @return inner class with the specified name
     */
    public static Class getInnerClass ( final Class fromClass, final String innerClassName )
    {
        for ( final Class innerClass : fromClass.getDeclaredClasses () )
        {
            if ( getClassName ( innerClass ).equals ( innerClassName ) )
            {
                return innerClass;
            }
        }
        return null;
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
        catch ( final Throwable e )
        {
            if ( safeMethodsLoggingEnabled )
            {
                Log.warn ( "ReflectionUtils method failed: createInstanceSafely", e );
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
     * @throws java.lang.ClassNotFoundException            if class was not found
     * @throws java.lang.reflect.InvocationTargetException if method throws an exception
     * @throws java.lang.IllegalAccessException            if method is inaccessible
     * @throws java.lang.InstantiationException            if the class is abstract
     * @throws java.lang.NoSuchMethodException             if method was not found
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
        catch ( final Throwable e )
        {
            if ( safeMethodsLoggingEnabled )
            {
                Log.warn ( "ReflectionUtils method failed: createInstanceSafely", e );
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
     * @throws java.lang.InstantiationException            if the class is abstract
     * @throws java.lang.NoSuchMethodException             if method was not found
     * @throws java.lang.reflect.InvocationTargetException if method throws an exception
     * @throws java.lang.IllegalAccessException            if method is inaccessible
     */
    public static <T> T createInstance ( final Class theClass, final Object... arguments )
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        // Retrieving argument types
        final Class[] parameterTypes = getClassTypes ( arguments );

        // Retrieving constructor
        final Constructor constructor = getConstructor ( theClass, parameterTypes );

        // Creating new instance
        constructor.setAccessible ( true );
        return ( T ) constructor.newInstance ( arguments );
    }

    /**
     * Returns class constructor for the specified argument types.
     *
     * @param theClass       class to process
     * @param parameterTypes constructor argument types
     * @return class constructor for the specified argument types
     * @throws java.lang.NoSuchMethodException if constructor was not found
     */
    public static Constructor getConstructor ( final Class theClass, final Class... parameterTypes ) throws NoSuchMethodException
    {
        // todo Constructors priority check (by super types)
        // todo For now some constructor with [Object] arg might be used instead of constructor with [String]
        // todo To avoid issues don't call constructors with same amount of arguments and which are cast-able to each other

        // This enhancement was a bad idea and was disabled
        // In case constructor is protected/private it won't be found
        // if ( parameterTypes.length == 0 )
        // {
        //     return theClass.getConstructor ();
        // }

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
        catch ( final Throwable e )
        {
            if ( safeMethodsLoggingEnabled )
            {
                Log.warn ( "ReflectionUtils method failed: callStaticMethodSafely", e );
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
     * @throws java.lang.ClassNotFoundException            if class was not found
     * @throws java.lang.NoSuchMethodException             if method was not found
     * @throws java.lang.reflect.InvocationTargetException if method throws an exception
     * @throws java.lang.IllegalAccessException            if method is inaccessible
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
        catch ( final Throwable e )
        {
            if ( safeMethodsLoggingEnabled )
            {
                Log.warn ( "ReflectionUtils method failed: callStaticMethodSafely", e );
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
     * @throws java.lang.NoSuchMethodException             if method was not found
     * @throws java.lang.reflect.InvocationTargetException if method throws an exception
     * @throws java.lang.IllegalAccessException            if method is inaccessible
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
        catch ( final Throwable e )
        {
            if ( safeMethodsLoggingEnabled )
            {
                Log.warn ( "ReflectionUtils method failed: callMethodsSafely", e );
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
     * @throws java.lang.NoSuchMethodException             if method was not found
     * @throws java.lang.reflect.InvocationTargetException if method throws an exception
     * @throws java.lang.IllegalAccessException            if method is inaccessible
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
        catch ( final Throwable e )
        {
            if ( safeMethodsLoggingEnabled )
            {
                Log.warn ( "ReflectionUtils method failed: callMethodsSafely", e );
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
     * @throws java.lang.NoSuchMethodException             if method was not found
     * @throws java.lang.reflect.InvocationTargetException if method throws an exception
     * @throws java.lang.IllegalAccessException            if method is inaccessible
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
        catch ( final Throwable e )
        {
            if ( safeMethodsLoggingEnabled )
            {
                Log.warn ( "ReflectionUtils method failed: callMethodSafely", e );
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
     * @throws java.lang.NoSuchMethodException             if method was not found
     * @throws java.lang.reflect.InvocationTargetException if method throws an exception
     * @throws java.lang.IllegalAccessException            if method is inaccessible
     * @throws java.lang.ExceptionInInitializerError       if the initialization provoked by this method fails
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
        catch ( final Throwable e )
        {
            if ( safeMethodsLoggingEnabled )
            {
                Log.warn ( "ReflectionUtils method failed: getMethodSafely", e );
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
     * @throws java.lang.NoSuchMethodException             if method was not found
     * @throws java.lang.reflect.InvocationTargetException if method throws an exception
     * @throws java.lang.IllegalAccessException            if method is inaccessible
     */
    public static Method getMethod ( final Object object, final String methodName, final Object... arguments )
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        return getMethod ( object.getClass (), methodName, arguments );
    }

    /**
     * Returns object's method with the specified name and arguments.
     * If method is not found in the object class all superclasses will be searched for that method.
     *
     * @param aClass     object class
     * @param methodName method name
     * @param arguments  method arguments
     * @return object's method with the specified name and arguments
     * @throws java.lang.NoSuchMethodException             if method was not found
     * @throws java.lang.reflect.InvocationTargetException if method throws an exception
     * @throws java.lang.IllegalAccessException            if method is inaccessible
     */
    public static Method getMethod ( final Class aClass, final String methodName, final Object... arguments )
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        // todo Methods priority check (by super types)
        // todo For now some method with [Object] arg might be used instead of method with [String]
        // todo To avoid issues don't call methods with same amount of arguments and which are cast-able to each other

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
     * @throws java.lang.NoSuchMethodException             if method was not found
     * @throws java.lang.reflect.InvocationTargetException if method throws an exception
     * @throws java.lang.IllegalAccessException            if method is inaccessible
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
        final Method method = getMethodImpl ( aClass, aClass, methodName, types );
        method.setAccessible ( true );
        return method;
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
     * @throws java.lang.NoSuchMethodException if method was not found
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
                buf.append ( ( c == null ) ? "null" : c.getCanonicalName () );
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
     * @throws java.lang.NoSuchMethodException             if method was not found
     * @throws java.lang.reflect.InvocationTargetException if method throws an exception
     * @throws java.lang.IllegalAccessException            if method is inaccessible
     */
    public static <T extends Cloneable> T clone ( final T object )
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        if ( object == null )
        {
            return null;
        }
        return ReflectUtils.callMethod ( object, "clone" );
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
        if ( object == null )
        {
            return null;
        }
        return ReflectUtils.callMethodSafely ( object, "clone" );
    }

    /**
     * Returns class loaded for the specified canonical class name.
     *
     * @param canonicalClassName canonical class name
     * @return class loaded for the specified canonical class name
     * @throws java.lang.ClassNotFoundException if class was not found
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
     * @param clazz class type to check
     * @return {@code true} if specified class type is primitive, {@code false} otherwise
     */
    public static boolean isPrimitive ( final Class<?> clazz )
    {
        return clazz.isPrimitive () ||
                Boolean.class.isAssignableFrom ( clazz ) ||
                Integer.class.isAssignableFrom ( clazz ) ||
                Character.class.isAssignableFrom ( clazz ) ||
                Byte.class.isAssignableFrom ( clazz ) ||
                Short.class.isAssignableFrom ( clazz ) ||
                Long.class.isAssignableFrom ( clazz ) ||
                Float.class.isAssignableFrom ( clazz ) ||
                Double.class.isAssignableFrom ( clazz );
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
     * @param class1 first class
     * @param class2 second class
     * @return closest superclass for both of the specified classes
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