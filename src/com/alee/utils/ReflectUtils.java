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

import com.alee.utils.file.FileDownloadListener;
import com.alee.utils.reflection.JarEntry;
import com.alee.utils.reflection.JarEntryType;
import com.alee.utils.reflection.JarStructure;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * This class provides a set of utilities to simplify work with Reflection API.
 * There is also a few methods to retrieve full JAR archive structure.
 *
 * @author Mikle Garin
 */

public final class ReflectUtils
{
    /**
     * Returns class for the specified canonical name.
     *
     * @param canonicalName class canonical name
     * @return class for the specified canonical name
     */
    public static Class getClassSafely ( String canonicalName )
    {
        try
        {
            return Class.forName ( canonicalName );
        }
        catch ( ClassNotFoundException e )
        {
            return null;
        }
    }

    /**
     * Returns class for the specified canonical name.
     *
     * @param canonicalName class canonical name
     * @return class for the specified canonical name
     * @throws ClassNotFoundException
     */
    public static Class getClass ( String canonicalName ) throws ClassNotFoundException
    {
        return Class.forName ( canonicalName );
    }

    /**
     * Returns JAR archive structure.
     *
     * @param jarClass any class within the JAR
     * @return JAR archive structure
     */
    public static JarStructure getJarStructure ( Class jarClass )
    {
        return getJarStructure ( jarClass, null, null );
    }

    /**
     * Returns JAR archive structure.
     *
     * @param jarClass          any class within the JAR
     * @param allowedExtensions list of extension filters
     * @param allowedPackgages  list of allowed packages
     * @return JAR archive structure
     */
    public static JarStructure getJarStructure ( Class jarClass, List<String> allowedExtensions, List<String> allowedPackgages )
    {
        return getJarStructure ( jarClass, allowedExtensions, allowedPackgages, null );
    }

    /**
     * Returns JAR archive structure.
     *
     * @param jarClass          any class within the JAR
     * @param allowedExtensions list of extension filters
     * @param allowedPackgages  list of allowed packages
     * @param listener          jar download listener
     * @return JAR archive structure
     */
    public static JarStructure getJarStructure ( Class jarClass, List<String> allowedExtensions, List<String> allowedPackgages,
                                                 FileDownloadListener listener )
    {
        try
        {
            CodeSource src = jarClass.getProtectionDomain ().getCodeSource ();
            if ( src != null )
            {
                // Creating structure

                // Source url
                URL jarUrl = src.getLocation ();
                URI uri = jarUrl.toURI ();

                // Source file
                File jarFile;
                String scheme = uri.getScheme ();
                if ( scheme != null && scheme.equalsIgnoreCase ( "file" ) )
                {
                    // Local jar-file
                    jarFile = new File ( uri );
                }
                else
                {
                    // Remote jar-file
                    jarFile = FileUtils.downloadFile ( jarUrl.toString (), File.createTempFile ( "jar_file", ".tmp" ), listener );
                }

                // Creating
                JarEntry jarEntry = new JarEntry ( JarEntryType.jarEntry, jarFile.getName () );
                JarStructure jarStructure = new JarStructure ( jarEntry );
                jarStructure.setJarLocation ( jarFile.getAbsolutePath () );

                // Reading all entries and parsing them into structure
                ZipInputStream zip = new ZipInputStream ( jarUrl.openStream () );
                ZipEntry zipEntry;
                while ( ( zipEntry = zip.getNextEntry () ) != null )
                {
                    String entryName = zipEntry.getName ();
                    if ( isAllowedPackage ( entryName, allowedPackgages ) &&
                            ( zipEntry.isDirectory () || isAllowedExtension ( entryName, allowedExtensions ) ) )
                    {
                        parseElement ( jarEntry, entryName, zipEntry );
                    }
                }
                zip.close ();

                return jarStructure;
            }
        }
        catch ( IOException e )
        {
            e.printStackTrace ();
        }
        catch ( URISyntaxException e )
        {
            e.printStackTrace ();
        }
        return null;
    }

    /**
     * Returns whether JAR entry with the specified name is allowed by the extensions list or not.
     *
     * @param entryName         JAR entry name
     * @param allowedExtensions list of allowed extensions
     * @return true if JAR entry with the specified name is allowed by the extensions list, false otherwise
     */
    private static boolean isAllowedExtension ( String entryName, List<String> allowedExtensions )
    {
        if ( allowedExtensions == null || allowedExtensions.size () == 0 )
        {
            return true;
        }
        else
        {
            String entryExt = FileUtils.getFileExtPart ( entryName, true ).toLowerCase ();
            return allowedExtensions.contains ( entryExt );
        }
    }

    /**
     * Returns whether JAR entry with the specified name is allowed by the packages list or not.
     *
     * @param entryName        JAR entry name
     * @param allowedPackgages list of allowed packages
     * @return true if JAR entry with the specified name is allowed by the packages list, false otherwise
     */
    private static boolean isAllowedPackage ( String entryName, List<String> allowedPackgages )
    {
        if ( allowedPackgages == null || allowedPackgages.size () == 0 )
        {
            return true;
        }
        else
        {
            for ( String packageStart : allowedPackgages )
            {
                if ( entryName.startsWith ( packageStart ) )
                {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Parses single JAR entry with the specified name.
     *
     * @param jarEntry  JAR entry
     * @param entryName JAR entry name
     * @param zipEntry  ZIP entry
     */
    private static void parseElement ( JarEntry jarEntry, String entryName, ZipEntry zipEntry )
    {
        String[] path = entryName.split ( "/" );
        JarEntry currentLevel = jarEntry;
        for ( int i = 0; i < path.length; i++ )
        {
            if ( i < path.length - 1 )
            {
                // We are getting deeper into packages
                JarEntry child = currentLevel.getChildByName ( path[ i ] );
                if ( child == null )
                {
                    child = new JarEntry ( JarEntryType.packageEntry, path[ i ], currentLevel );
                    child.setZipEntry ( zipEntry );
                    currentLevel.addChild ( child );
                }
                currentLevel = child;
            }
            else
            {
                // We reached last element
                JarEntry newEntry = new JarEntry ( getJarEntryType ( path[ i ] ), path[ i ], currentLevel );
                newEntry.setZipEntry ( zipEntry );
                currentLevel.addChild ( newEntry );
            }
        }
    }

    /**
     * Returns JAR entry type.
     *
     * @param file file to process
     * @return JAR entry type
     */
    private static JarEntryType getJarEntryType ( String file )
    {
        String ext = FileUtils.getFileExtPart ( file, false );
        if ( ext.equals ( "java" ) )
        {
            return JarEntryType.javaEntry;
        }
        else if ( ext.equals ( "class" ) )
        {
            return JarEntryType.classEntry;
        }
        else if ( !ext.isEmpty () )
        {
            return JarEntryType.fileEntry;
        }
        else
        {
            return JarEntryType.packageEntry;
        }
    }

    /**
     * Returns method caller class.
     * It is not recommended to use this method anywhere but in debugging.
     *
     * @return method caller class
     */
    public static Class getCallerClass ()
    {
        try
        {
            // 0 - this method class
            // 1 - this method caller class
            // 2 - caller's class caller
            return Class.forName ( new Throwable ().getStackTrace ()[ 2 ].getClassName () );
        }
        catch ( Throwable e )
        {
            return null;
        }
    }

    /**
     * Returns class name with ".java" extension in the end.
     *
     * @param classObject object of class type
     * @return class name with ".java" extension in the end
     */
    public static String getJavaClassName ( Object classObject )
    {
        return getJavaClassName ( classObject.getClass () );
    }

    /**
     * Returns class name with ".java" extension in the end.
     *
     * @param classType class type
     * @return class name with ".java" extension in the end
     */
    public static String getJavaClassName ( Class classType )
    {
        return getClassName ( classType ) + ".java";
    }

    /**
     * Returns class name with ".class" extension in the end.
     *
     * @param classObject object of class type
     * @return class name with ".class" extension in the end
     */
    public static String getClassFileName ( Object classObject )
    {
        return getClassFileName ( classObject.getClass () );
    }

    /**
     * Returns class name with ".class" extension in the end.
     *
     * @param classType class type
     * @return class name with ".class" extension in the end
     */
    public static String getClassFileName ( Class classType )
    {
        return ReflectUtils.getClassName ( classType ) + ".class";
    }

    /**
     * Returns class name.
     *
     * @param classObject object of class type
     * @return class name
     */
    public static String getClassName ( Object classObject )
    {
        return getClassName ( classObject.getClass () );
    }

    /**
     * Returns class name.
     *
     * @param classType class type
     * @return class name
     */
    public static String getClassName ( Class classType )
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
    public static String[] getClassPackages ( Object classObject )
    {
        return getClassPackages ( classObject.getClass () );
    }

    /**
     * Returns class packages.
     *
     * @param classType class type
     * @return class packages
     */
    public static String[] getClassPackages ( Class classType )
    {
        return getPackages ( classType.getPackage ().getName () );
    }

    /**
     * Returns packages names.
     *
     * @param packageName package name
     * @return packages names
     */
    public static String[] getPackages ( String packageName )
    {
        return packageName.split ( "\\." );
    }

    /**
     * Returns static field value from the specified class.
     *
     * @param classType class type
     * @param fieldName class field name
     * @return static field value from the specified class
     */
    public static Object getStaticFieldValueSafely ( Class classType, String fieldName )
    {
        try
        {
            return classType.getField ( fieldName ).get ( null );
        }
        catch ( IllegalAccessException e )
        {
            return null;
        }
        catch ( NoSuchFieldException e )
        {
            return null;
        }
    }

    /**
     * Returns static field value from the specified class.
     *
     * @param classType class type
     * @param fieldName class field name
     * @return static field value from the specified class
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static Object getStaticFieldValue ( Class classType, String fieldName ) throws NoSuchFieldException, IllegalAccessException
    {
        return classType.getField ( fieldName ).get ( null );
    }

    /**
     * Returns inner class with the specified name.
     *
     * @param fromClass      class to look for the inner class
     * @param innerClassName inner class name
     * @return inner class with the specified name
     */
    public static Class getInnerClass ( Class fromClass, String innerClassName )
    {
        for ( Class innerClass : fromClass.getDeclaredClasses () )
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
    public static Object createInstanceSafely ( String canonicalClassName, Object... arguments )
    {
        try
        {
            return createInstance ( canonicalClassName, arguments );
        }
        catch ( Throwable e )
        {
            return null;
        }
    }

    /**
     * Returns newly created class instance.
     *
     * @param canonicalClassName canonical class name
     * @param arguments          class constructor arguments
     * @return newly created class instance
     * @throws ClassNotFoundException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     */
    public static Object createInstance ( String canonicalClassName, Object... arguments )
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
    public static Object createInstanceSafely ( Class theClass, Object... arguments )
    {
        try
        {
            return createInstance ( theClass, arguments );
        }
        catch ( Throwable e )
        {
            return null;
        }
    }

    /**
     * Returns newly created class instance.
     *
     * @param theClass  class to process
     * @param arguments class constructor arguments
     * @return newly created class instance
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public static Object createInstance ( Class theClass, Object... arguments )
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        // Retrieving argument types
        Class[] parameterTypes = getClassTypes ( arguments );

        // Retrieving constructor
        Constructor constructor = getConstructor ( theClass, parameterTypes );
        //        Constructor constructor = theClass.getConstructor ( parameterTypes );

        // Creating new instance
        return constructor.newInstance ( arguments );
    }

    /**
     * Returns class constructor for the specified argument types.
     *
     * @param theClass       class to process
     * @param parameterTypes constructor argument types
     * @return class constructor for the specified argument types
     * @throws NoSuchMethodException
     */
    public static Constructor getConstructor ( Class theClass, Class... parameterTypes ) throws NoSuchMethodException
    {
        // todo Constructors priority check (by super types)
        // todo For now some constructor with [Object] arg might be used instead of constructor with [String]
        if ( parameterTypes.length == 0 )
        {
            return theClass.getConstructor ();
        }
        else
        {
            for ( Constructor constructor : theClass.getConstructors () )
            {
                final Class[] types = constructor.getParameterTypes ();

                // Inappropriate constructor
                if ( types.length != parameterTypes.length )
                {
                    continue;
                }

                // Constructor with no parameters
                if ( types.length == parameterTypes.length && types.length == 0 )
                {
                    return constructor;
                }

                // Checking types
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
            throw new NoSuchMethodException ( theClass.getName () + argumentTypesToString ( parameterTypes ) );
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
     */
    public static Object callStaticMethodSafely ( String canonicalClassName, String methodName, Object... arguments )
    {
        try
        {
            return callStaticMethod ( loadClass ( canonicalClassName ), methodName, arguments );
        }
        catch ( Throwable e )
        {
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
     * @throws ClassNotFoundException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     */
    public static Object callStaticMethod ( String canonicalClassName, String methodName, Object... arguments )
            throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException
    {
        return callStaticMethod ( loadClass ( canonicalClassName ), methodName, arguments );
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
    public static Object callStaticMethodSafely ( Class theClass, String methodName, Object... arguments )
    {
        try
        {
            return callStaticMethod ( theClass, methodName, arguments );
        }
        catch ( Throwable e )
        {
            return null;
        }
    }

    /**
     * Returns result of called static method.
     * Will return null in case method is void-type.
     *
     * @param theClass   class to process
     * @param methodName static method name
     * @param arguments  method arguments
     * @return result of called static method
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static Object callStaticMethod ( Class theClass, String methodName, Object... arguments )
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        // todo Methods priority check (by super types)
        // todo For now some method with [Object] arg might be used instead of method with [String]
        if ( arguments.length == 0 )
        {
            // Calling simple method w/o arguments
            return theClass.getMethod ( methodName ).invoke ( null );
        }
        else
        {
            // Searching for more complex method
            Class[] types = getClassTypes ( arguments );
            Method[] methods = theClass.getMethods ();
            for ( Method method : methods )
            {
                // Checking method name
                if ( method.getName ().equals ( methodName ) )
                {
                    // Checking method arguments count
                    Class<?>[] mt = method.getParameterTypes ();
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
                            return method.invoke ( null, arguments );
                        }
                    }
                }
            }
            throw new NoSuchMethodException ( theClass.getName () + "." + methodName + argumentTypesToString ( types ) );
        }
    }

    /**
     * Returns list of results returned by called methods.
     *
     * @param objects    objects to call methods on
     * @param methodName method name
     * @param arguments  method arguments
     * @return list of results returned by called methods
     */
    public static List callMethodsSafely ( List objects, String methodName, Object... arguments )
    {
        try
        {
            return callMethods ( objects, methodName, arguments );
        }
        catch ( Throwable e )
        {
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
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static List callMethods ( List objects, String methodName, Object... arguments )
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        List results = new ArrayList ();
        for ( Object object : objects )
        {
            results.add ( callMethod ( object, methodName, arguments ) );
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
    public static Object[] callMethodsSafely ( Object[] objects, String methodName, Object... arguments )
    {
        try
        {
            return callMethods ( objects, methodName, arguments );
        }
        catch ( Throwable e )
        {
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
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static Object[] callMethods ( Object[] objects, String methodName, Object... arguments )
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        Object[] results = new Object[ objects.length ];
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
    public static Object callMethodSafely ( Object object, String methodName, Object... arguments )
    {
        try
        {
            return callMethod ( object, methodName, arguments );
        }
        catch ( Throwable e )
        {
            return null;
        }
    }

    /**
     * Returns result given by called method.
     *
     * @param object     object instance
     * @param methodName method name
     * @param arguments  method arguments
     * @return result given by called method
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static Object callMethod ( Object object, String methodName, Object... arguments )
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        // todo Methods priority check (by super types)
        // todo For now some method with [Object] arg might be used instead of method with [String]
        if ( arguments.length == 0 )
        {
            // Calling simple method w/o arguments
            return object.getClass ().getMethod ( methodName ).invoke ( object );
        }
        else
        {
            // Searching for more complex method
            Class[] types = getClassTypes ( arguments );
            Method[] methods = object.getClass ().getMethods ();
            for ( Method method : methods )
            {
                // Checking method name
                if ( method.getName ().equals ( methodName ) )
                {
                    // Checking method arguments count
                    Class<?>[] mt = method.getParameterTypes ();
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
                            return method.invoke ( object, arguments );
                        }
                    }
                }
            }
            throw new NoSuchMethodException ( object.getClass ().getName () + "." + methodName + argumentTypesToString ( types ) );
        }
    }

    /**
     * Returns text representation for array of argument types.
     *
     * @param argTypes argument types
     * @return text representation for array of argument types
     */
    private static String argumentTypesToString ( Class[] argTypes )
    {
        StringBuilder buf = new StringBuilder ();
        buf.append ( "(" );
        if ( argTypes != null )
        {
            for ( int i = 0; i < argTypes.length; i++ )
            {
                if ( i > 0 )
                {
                    buf.append ( ", " );
                }
                Class c = argTypes[ i ];
                buf.append ( ( c == null ) ? "null" : c.getName () );
            }
        }
        buf.append ( ")" );
        return buf.toString ();
    }

    /**
     * Returns cloned object.
     *
     * @param object object to clone
     * @param <T>    cloned object type
     * @return cloned object
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static <T extends Cloneable> T clone ( T object ) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        return ( T ) ReflectUtils.callMethod ( object, "clone" );
    }

    /**
     * Returns cloned object.
     *
     * @param object object to clone
     * @param <T>    cloned object type
     * @return cloned object
     */
    public static <T extends Cloneable> T cloneSafely ( T object )
    {
        return ( T ) ReflectUtils.callMethodSafely ( object, "clone" );
    }

    /**
     * Returns class loaded for the specified canonical class name.
     *
     * @param canonicalClassName canonical class name
     * @return class loaded for the specified canonical class name
     * @throws ClassNotFoundException
     */
    public static Class loadClass ( String canonicalClassName ) throws ClassNotFoundException
    {
        return ReflectUtils.class.getClassLoader ().loadClass ( canonicalClassName );
    }

    /**
     * Returns an array of argument class types.
     *
     * @param arguments arguments to process
     * @return an array of argument class types
     */
    public static Class[] getClassTypes ( Object[] arguments )
    {
        Class[] parameterTypes = new Class[ arguments.length ];
        for ( int i = 0; i < arguments.length; i++ )
        {
            parameterTypes[ i ] = arguments[ i ].getClass ();
        }
        return parameterTypes;
    }

    /**
     * Returns whether first type is assignable from second one or not.
     *
     * @param type first type
     * @param from second type
     * @return true if first type is assignable from second one, false otherwise
     */
    public static boolean isAssignable ( Class type, Class from )
    {
        if ( type.isAssignableFrom ( from ) )
        {
            return true;
        }
        else
        {
            if ( type.isPrimitive () )
            {
                if ( type == boolean.class )
                {
                    return Boolean.class.isAssignableFrom ( from );
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
                else if ( type == int.class )
                {
                    return Integer.class.isAssignableFrom ( from );
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
    }

    /**
     * Returns whether one of superclasses contains specified text in its name or not.
     *
     * @param theClass class to process
     * @param text     text to search for
     * @return true if one of superclasses contains specified text in its name, false otherwise
     */
    public static boolean containsInClassOrSuperclassName ( Class theClass, String text )
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
}