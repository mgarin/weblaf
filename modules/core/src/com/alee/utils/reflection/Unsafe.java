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

import com.alee.utils.ReflectUtils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * {@code sun.misc.Unsafe} wrapper.
 *
 * @author Mikle Garin
 * @author Joe Walnes
 */
public final class Unsafe
{
    /**
     * {@link WeakHashMap} that caches serialized data.
     */
    private static Map<Class, byte[]> serializedDataCache;

    /**
     * Returns new {@link Class} instance.
     * Uses {@code sun.misc.Unsafe} whenever it is possible to avoid using {@link Class} constructor.
     * If unable to access {@code sun.misc.Unsafe} - {@link Class} will be instantiated through other means.
     *
     * @param clazz {@link Class} to be instantiated
     * @param <T>   {@link Class} type
     * @return new {@link Class} instance
     */
    public static <T> T allocateInstance ( final Class<T> clazz )
    {
        if ( clazz != null )
        {
            T instance;
            try
            {
                instance = allocateInstanceThroughUnsafe ( clazz );
            }
            catch ( final Exception e )
            {
                try
                {
                    instance = allocateInstanceThroughReflection ( clazz );
                }
                catch ( final Exception ex )
                {
                    if ( Serializable.class.isAssignableFrom ( clazz ) )
                    {
                        try
                        {
                            instance = allocateInstanceThroughSerialization ( clazz );
                        }
                        catch ( final Exception exx )
                        {
                            throw new ReflectionException ( "Unable to instantiate class: " + clazz, ex );
                        }
                    }
                    else
                    {
                        throw new ReflectionException ( "Unable to instantiate class: " + clazz, ex );
                    }
                }
            }
            return instance;
        }
        else
        {
            throw new ReflectionException ( "Class type must not be null" );
        }
    }

    /**
     * Returns new {@link Class} instance created using {@code sun.misc.Unsafe}.
     * This will allow us to skip constructors usage to avoid unwanted behavior.
     *
     * @param clazz {@link Class} to be instantiated
     * @param <T>   {@link Class} type
     * @return new {@link Class} instance created using {@code sun.misc.Unsafe}
     * @throws ClassNotFoundException    if {@code sun.misc.Unsafe} class cannot be found
     * @throws NoSuchFieldException      if {@code sun.misc.Unsafe#theUnsafe} field cannot be found
     * @throws IllegalAccessException    if access was denied for one of the reflection calls
     * @throws NoSuchMethodException     if {@code sun.misc.Unsafe#allocateInstance(Class)} cannot be found
     * @throws InvocationTargetException if {@code sun.misc.Unsafe#allocateInstance(Class)} invocation failed
     */
    private static <T> T allocateInstanceThroughUnsafe ( final Class<T> clazz )
            throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException
    {
        final Class<?> unsafe = ReflectUtils.getClass ( "sun.misc.Unsafe" );
        final Object theUnsafe = ReflectUtils.getStaticFieldValue ( unsafe, "theUnsafe" );
        return ReflectUtils.callMethod ( theUnsafe, "allocateInstance", clazz );
    }

    /**
     * Returns new {@link Class} instance created using default constructor if available.
     * This is not a perfect solution, but might be necessary if {@code sun.misc.Unsafe} is not available.
     *
     * @param clazz {@link Class} to be instantiated
     * @param <T>   {@link Class} type
     * @return new {@link Class} instance created using default constructor if available
     * @throws InstantiationException    if {@link Class} instance creation failed
     * @throws IllegalAccessException    if cannot access {@link Class} or its default constructor
     * @throws InvocationTargetException if calling {@link Class} default constructor failed
     * @throws NoSuchMethodException     if there is no default {@link Class} constructor
     */
    private static <T> T allocateInstanceThroughReflection ( final Class<T> clazz )
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        return ReflectUtils.createInstance ( clazz );
    }

    /**
     * Returns new {@link Class} instance using JDK serialization-deserialization.
     * This might work in a similar way {@code sun.misc.Unsafe} allocation works depending on implementation.
     * This method was borrowed from XStream {@link com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider}.
     *
     * @param clazz {@link Class} to be instantiated
     * @param <T>   {@link Class} type
     * @return new {@link Class} instance using JDK serialization-deserialization
     * @throws IOException            if an IO operation failed
     * @throws ClassNotFoundException if read instance {@link Class} cannot be found
     * @author Joe Walnes
     */
    private synchronized static <T> T allocateInstanceThroughSerialization ( final Class<T> clazz )
            throws IOException, ClassNotFoundException
    {
        // Ensure data cache map is initialized
        if ( serializedDataCache == null )
        {
            serializedDataCache = new WeakHashMap<Class, byte[]> ();
        }

        // Retrieving or creating serialized dummy data
        byte[] data = serializedDataCache.get ( clazz );
        if ( data == null )
        {
            final ByteArrayOutputStream bytes = new ByteArrayOutputStream ();
            final DataOutputStream stream = new DataOutputStream ( bytes );
            stream.writeShort ( ObjectStreamConstants.STREAM_MAGIC );
            stream.writeShort ( ObjectStreamConstants.STREAM_VERSION );
            stream.writeByte ( ObjectStreamConstants.TC_OBJECT );
            stream.writeByte ( ObjectStreamConstants.TC_CLASSDESC );
            stream.writeUTF ( clazz.getName () );
            stream.writeLong ( ObjectStreamClass.lookup ( clazz ).getSerialVersionUID () );
            stream.writeByte ( 2 );  // classDescFlags (2 = Serializable)
            stream.writeShort ( 0 ); // field count
            stream.writeByte ( ObjectStreamConstants.TC_ENDBLOCKDATA );
            stream.writeByte ( ObjectStreamConstants.TC_NULL );
            data = bytes.toByteArray ();
            serializedDataCache.put ( clazz, data );
        }

        // Creating object input stream to read object instance from a dummy data
        final ObjectInputStream in = new ObjectInputStream ( new ByteArrayInputStream ( data ) )
        {
            @Override
            protected Class resolveClass ( final ObjectStreamClass desc ) throws ClassNotFoundException
            {
                return Class.forName ( desc.getName (), false, clazz.getClassLoader () );
            }
        };

        // Reading object instance
        return ( T ) in.readObject ();
    }
}