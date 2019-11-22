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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.resource.ClassResource;
import com.alee.api.resource.FileResource;
import com.alee.api.resource.Resource;
import com.alee.api.resource.UrlResource;
import com.alee.utils.general.Pair;
import com.alee.utils.swing.Scale;
import com.alee.utils.xml.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import com.thoughtworks.xstream.core.util.CompositeClassLoader;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.*;

/**
 * This class provides a set of utilities to easily serialize and deserialize objects into and from XML.
 * There are is a lot of methods to read specific library objects using various resource files.
 *
 * @author Mikle Garin
 */
public final class XmlUtils
{
    /**
     * todo 1. Instead of utilities class create an extension over XStream
     * todo 2. Structure the way all custom converters/aliases are provided
     */

    /**
     * {@link Resource} attributes.
     */
    public static final String NEAR_CLASS_ATTRIBUTE = "nearClass";
    public static final String BASE_ATTRIBUTE = "base";
    public static final String PATH_ATTRIBUTE = "path";
    public static final String URL_ATTRIBUTE = "url";

    /**
     * {@link Resource}-related context values.
     */
    public static final String CONTEXT_NEAR_CLASS = "Resource.nearClass";
    public static final String CONTEXT_BASE = "Resource.base";

    /**
     * Whether or not should offer better aliases for standard JDK classes like {@link Point} and {@link Rectangle}.
     */
    private static boolean aliasJdkClasses = true;

    /**
     * XStream driver.
     */
    private static HierarchicalStreamDriver hierarchicalStreamDriver;

    /**
     * XStream instance.
     */
    @Nullable
    private static XStream xStream = null;

    /**
     * Private constructor to avoid instantiation.
     */
    private XmlUtils ()
    {
        throw new UtilityException ( "Utility classes are not meant to be instantiated" );
    }

    /**
     * Returns global XStream instance configured with all required aliases and converters.
     *
     * @return XStream
     */
    @NotNull
    public static XStream getXStream ()
    {
        if ( xStream == null )
        {
            initializeXStream ();
        }
        return xStream;
    }

    /**
     * Initializes global XStream instance.
     */
    private static void initializeXStream ()
    {
        try
        {
            // Custom ReflectionProvider to provide pure-java objects instantiation
            // Unlike default SunUnsafeReflectionProvider it has limited objects instantiation options
            // Also it properly initializes object field values specified directly near the fields
            final PureJavaReflectionProvider reflectionProvider = new PureJavaReflectionProvider ();

            // Custom HierarchicalStreamDriver implementation based on StaxDriver
            // It provides us HierarchicalStreamReader to allow XStreamContext usage
            hierarchicalStreamDriver = new XmlDriver ();

            // XStream instance initialization
            xStream = new XStream ( reflectionProvider, hierarchicalStreamDriver );

            // Make sure that XStream ClassLoader finds WebLaF classes in cases where multiple ClassLoaders are used
            // E.g. IntelliJ IDEA uses different ClassLoaders for plugins (e.g. JFormDesigner) and its core (which includes XStream)
            if ( XmlUtils.class.getClassLoader () != xStream.getClass ().getClassLoader () )
            {
                final ClassLoader classLoader = xStream.getClassLoader ();
                if ( classLoader instanceof CompositeClassLoader )
                {
                    ( ( CompositeClassLoader ) classLoader ).add ( XmlUtils.class.getClassLoader () );
                }
                else
                {
                    final CompositeClassLoader compositeClassLoader = new CompositeClassLoader ();
                    compositeClassLoader.add ( XmlUtils.class.getClassLoader () );
                    xStream.setClassLoader ( compositeClassLoader );
                }
            }

            // Standard Java-classes aliases
            if ( aliasJdkClasses )
            {
                // Custom {@link java.awt.Point} mapping
                xStream.alias ( "Point", Point.class );
                xStream.useAttributeFor ( Point.class, "x" );
                xStream.useAttributeFor ( Point.class, "y" );

                // Custom {@link java.awt.geom.Point2D} mapping
                xStream.alias ( "Point2D", Point2D.class );
                xStream.registerConverter ( new Point2DConverter () );

                // Custom {@link java.awt.Dimension} mapping
                xStream.alias ( "Dimension", Dimension.class );
                xStream.registerConverter ( new DimensionConverter () );

                // Custom {@link java.awt.Rectangle} mapping
                xStream.alias ( "Rectangle", Rectangle.class );
                xStream.useAttributeFor ( Rectangle.class, "x" );
                xStream.useAttributeFor ( Rectangle.class, "y" );
                xStream.useAttributeFor ( Rectangle.class, "width" );
                xStream.useAttributeFor ( Rectangle.class, "height" );

                // Custom {@link java.awt.Font} mapping
                xStream.alias ( "Font", Font.class );

                // Custom {@link java.awt.Color} mapping
                xStream.alias ( "Color", Color.class );
                xStream.registerConverter ( new ColorConverter () );

                // Custom {@link java.awt.Insets} mapping
                xStream.alias ( "Insets", Insets.class );
                xStream.registerConverter ( new InsetsConverter () );

                // Custom {@link java.awt.Stroke} mapping
                xStream.alias ( "Stroke", Stroke.class );
                xStream.registerConverter ( new StrokeConverter () );
            }

            // Additional WebLaF data classes aliases
            xStream.processAnnotations ( Pair.class );
            xStream.processAnnotations ( Scale.class );
        }
        catch ( final Exception e )
        {
            // It is a pretty fatal for library if something goes wrong here
            throw new UtilityException ( "Unable to initialize XStream instance", e );
        }
    }

    /**
     * Returns whether or not aliases for standard JDK classes like {@link Point} and {@link Rectangle} are used.
     *
     * @return {@code true} if should use aliases for standard JDK classes are used, {@code false} otherwise
     */
    public static boolean isAliasJdkClasses ()
    {
        return aliasJdkClasses;
    }

    /**
     * Sets whether or not aliases for standard JDK classes like {@link Point} and {@link Rectangle} should be used.
     *
     * @param alias whether or not aliases for standard JDK classes should be used
     */
    public static void setAliasJdkClasses ( final boolean alias )
    {
        XmlUtils.aliasJdkClasses = alias;
    }

    /**
     * Process the annotations of the given type and configure the XStream.
     * A call of this method will automatically turn the auto-detection mode for annotations off.
     *
     * @param type the type with XStream annotations
     */
    public static void processAnnotations ( @NotNull final Class type )
    {
        getXStream ().processAnnotations ( type );
    }

    /**
     * Process the annotations of the given types and configure the XStream.
     *
     * @param types the types with XStream annotations
     */
    public static void processAnnotations ( @NotNull final Class[] types )
    {
        getXStream ().processAnnotations ( types );
    }

    /**
     * Alias a Class to a shorter name to be used in XML elements.
     *
     * @param name Short name
     * @param type Type to be aliased
     */
    public static void alias ( @NotNull final String name, @NotNull final Class type )
    {
        getXStream ().alias ( name, type );
    }

    /**
     * Prevents field from being serialized.
     *
     * @param type  class owning the field
     * @param field name of the field
     */
    public static void omitField ( @NotNull final Class type, @NotNull final String field )
    {
        getXStream ().omitField ( type, field );
    }

    /**
     * Use an attribute for a field declared in a specific type.
     *
     * @param type  the name of the field
     * @param field the Class containing such field
     */
    public static void useAttributeFor ( @NotNull final Class type, @NotNull final String field )
    {
        getXStream ().useAttributeFor ( type, field );
    }

    /**
     * Adds an implicit array.
     *
     * @param type  class owning the implicit array
     * @param field name of the array field
     */
    public static void addImplicitArray ( @NotNull final Class type, @NotNull final String field )
    {
        getXStream ().addImplicitArray ( type, field );
    }

    /**
     * Adds an implicit array which is used for all items of the given element name defined by itemName.
     *
     * @param type     class owning the implicit array
     * @param field    name of the array field in the ownerType
     * @param itemName alias name of the items
     */
    public static void addImplicitArray ( @NotNull final Class type, @NotNull final String field, @NotNull final String itemName )
    {
        getXStream ().addImplicitArray ( type, field, itemName );
    }

    /**
     * Adds a new converter into converters registry.
     *
     * @param converter the new converter
     */
    public static void registerConverter ( @NotNull final Converter converter )
    {
        getXStream ().registerConverter ( converter );
    }

    /**
     * Adds a new converter into converters registry.
     *
     * @param converter the new converter
     */
    public static void registerConverter ( @NotNull final SingleValueConverter converter )
    {
        getXStream ().registerConverter ( converter );
    }

    /**
     * Calls static "provideAliases" method on a class that implements AliasProvider.
     * This method is created mostly for internal library usage.
     *
     * @param aliasProvider AliasProvider ancestor class
     * @param <T>           specific class type
     */
    public static <T extends AliasProvider> void alias ( @NotNull final Class<T> aliasProvider )
    {
        ReflectUtils.callStaticMethodSafely ( aliasProvider, AliasProvider.methodName, getXStream () );
    }

    /**
     * Returns Object deserialized from XML content.
     *
     * @param xml XML content
     * @param <T> read object type
     * @return deserialized object
     */
    public static <T> T fromXML ( @NotNull final String xml )
    {
        return ( T ) getXStream ().fromXML ( xml );
    }

    /**
     * Returns Object deserialized from XML content.
     *
     * @param xml  XML content
     * @param root root {@link Object}
     * @param <T>  read object type
     * @return deserialized object
     */
    public static <T> T fromXML ( @NotNull final String xml, @NotNull final Object root )
    {
        return ( T ) getXStream ().fromXML ( xml, root );
    }

    /**
     * Returns Object deserialized from XML content.
     *
     * @param xml     XML content
     * @param context unmarshalling context data
     * @param <T>     read object type
     * @return deserialized object
     */
    public static <T> T fromXML ( @NotNull final String xml, @Nullable final XStreamContext context )
    {
        return ( T ) getXStream ().unmarshal ( hierarchicalStreamDriver.createReader ( new StringReader ( xml ) ), null, context );
    }

    /**
     * Returns Object deserialized from XML text.
     *
     * @param resource {@link Resource}
     * @param <T>      read object type
     * @return deserialized object
     */
    public static <T> T fromXML ( @NotNull final Resource resource )
    {
        return fromXML ( resource.getInputStream () );
    }

    /**
     * Returns Object deserialized from XML text.
     *
     * @param resource {@link Resource}
     * @param root     root {@link Object}
     * @param <T>      read object type
     * @return deserialized object
     */
    public static <T> T fromXML ( @NotNull final Resource resource, @NotNull final Object root )
    {
        return fromXML ( resource.getInputStream (), root );
    }

    /**
     * Returns Object deserialized from XML text.
     *
     * @param resource {@link Resource}
     * @param context  unmarshalling context data
     * @param <T>      read object type
     * @return deserialized object
     */
    public static <T> T fromXML ( @NotNull final Resource resource, @Nullable final XStreamContext context )
    {
        return fromXML ( resource.getInputStream (), context );
    }

    /**
     * Returns Object deserialized from XML content.
     *
     * @param input XML text source
     * @param <T>   read object type
     * @return deserialized object
     */
    public static <T> T fromXML ( @NotNull final InputStream input )
    {
        return ( T ) getXStream ().fromXML ( input );
    }

    /**
     * Returns Object deserialized from XML content.
     *
     * @param input XML text source
     * @param root  root {@link Object}
     * @param <T>   read object type
     * @return deserialized object
     */
    public static <T> T fromXML ( @NotNull final InputStream input, @NotNull final Object root )
    {
        return ( T ) getXStream ().fromXML ( input, root );
    }

    /**
     * Returns Object deserialized from XML content.
     *
     * @param input   XML text source
     * @param context unmarshalling context data
     * @param <T>     read object type
     * @return deserialized object
     */
    public static <T> T fromXML ( @NotNull final InputStream input, @Nullable final XStreamContext context )
    {
        return ( T ) getXStream ().unmarshal ( hierarchicalStreamDriver.createReader ( input ), null, context );
    }

    /**
     * Returns Object deserialized from XML content.
     *
     * @param reader XML text source
     * @param <T>    read object type
     * @return deserialized object
     */
    public static <T> T fromXML ( @NotNull final Reader reader )
    {
        return ( T ) getXStream ().fromXML ( reader );
    }

    /**
     * Returns Object deserialized from XML content.
     *
     * @param reader XML text source
     * @param root   root {@link Object}
     * @param <T>    read object type
     * @return deserialized object
     */
    public static <T> T fromXML ( @NotNull final Reader reader, @NotNull final Object root )
    {
        return ( T ) getXStream ().fromXML ( reader, root );
    }

    /**
     * Returns Object deserialized from XML content.
     *
     * @param reader  XML text source
     * @param context unmarshalling context data
     * @param <T>     read object type
     * @return deserialized object
     */
    public static <T> T fromXML ( @NotNull final Reader reader, @Nullable final XStreamContext context )
    {
        return ( T ) getXStream ().unmarshal ( hierarchicalStreamDriver.createReader ( reader ), null, context );
    }

    /**
     * Serializes Object into XML and writes it into specified file.
     *
     * @param obj  object to serialize
     * @param file output file
     */
    public static void toXML ( @NotNull final Object obj, @NotNull final String file )
    {
        toXML ( obj, new File ( file ) );
    }

    /**
     * Serializes Object into XML and writes it into specified file.
     *
     * @param obj  object to serialize
     * @param file output file
     */
    public static void toXML ( @NotNull final Object obj, @NotNull final File file )
    {
        try
        {
            final FileOutputStream fos = new FileOutputStream ( file );
            final OutputStreamWriter osw = new OutputStreamWriter ( fos, "UTF-8" );
            toXML ( obj, osw );
            osw.close ();
            fos.close ();
        }
        catch ( final IOException e )
        {
            final String msg = "Unable to serialize object '%s' into XML and write it into file: %s";
            LoggerFactory.getLogger ( XmlUtils.class ).error ( TextUtils.format ( msg, obj, file ), e );
        }
    }

    /**
     * Returns Object serialized into XML.
     *
     * @param obj object to serialize
     * @return serialized into XML object representation
     */
    @NotNull
    public static String toXML ( @NotNull final Object obj )
    {
        return getXStream ().toXML ( obj );
    }

    /**
     * Serializes Object into XML and writes it using a specified Writer.
     *
     * @param obj object to serialize
     * @param out output writer
     */
    public static void toXML ( @NotNull final Object obj, @NotNull final Writer out )
    {
        getXStream ().toXML ( obj, out );
    }

    /**
     * Serializes Object into XML and writes it using a specified OutputStream.
     *
     * @param obj object to serialize
     * @param out output stream
     */
    public static void toXML ( @NotNull final Object obj, @NotNull final OutputStream out )
    {
        getXStream ().toXML ( obj, out );
    }

    /**
     * Serializes Object into XML and writes it using a specified HierarchicalStreamWriter
     *
     * @param obj    object to serialize
     * @param writer hierarchical stream writer
     */
    public static void toXML ( @NotNull final Object obj, @NotNull final HierarchicalStreamWriter writer )
    {
        getXStream ().marshal ( obj, writer );
    }

    /**
     * Returns {@link Resource} based on the available attributes in the current node.
     *
     * @param reader  {@link HierarchicalStreamReader}
     * @param context {@link UnmarshallingContext}
     * @param mapper  {@link Mapper}
     * @return {@link Resource} based on the available attributes in the current node
     */
    @NotNull
    public static Resource readResource ( @NotNull final HierarchicalStreamReader reader, @NotNull final UnmarshallingContext context,
                                          @NotNull final Mapper mapper )
    {
        final Resource resource;
        final String path = reader.getAttribute ( "path" );
        final String contextBase = ( String ) context.get ( CONTEXT_BASE );
        if ( path != null )
        {
            final String completePath = contextBase != null ? NetUtils.joinUrlPaths ( contextBase, path ) : path;
            final String nearClass = reader.getAttribute ( "nearClass" );
            if ( nearClass != null )
            {
                resource = new ClassResource ( mapper.realClass ( nearClass ), completePath );
            }
            else
            {
                final String contextNearClass = ( String ) context.get ( CONTEXT_NEAR_CLASS );
                if ( contextNearClass != null )
                {
                    resource = new ClassResource ( mapper.realClass ( contextNearClass ), completePath );
                }
                else
                {
                    resource = new FileResource ( completePath );
                }
            }
        }
        else
        {
            final String url = reader.getAttribute ( "url" );
            if ( url != null )
            {
                final String completeURL = contextBase != null ? NetUtils.joinUrlPaths ( contextBase, url ) : url;
                resource = new UrlResource ( completeURL );
            }
            else
            {
                throw new UtilityException ( "Unable to find any Resource type in node attributes: " + reader.getNodeName () );
            }
        }
        return resource;
    }
}