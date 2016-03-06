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
import com.alee.managers.plugin.data.PluginDependency;
import com.alee.managers.plugin.data.PluginInformation;
import com.alee.managers.plugin.data.PluginLibrary;
import com.alee.managers.plugin.data.PluginVersion;
import com.alee.utils.collection.ValuesTable;
import com.alee.utils.general.Pair;
import com.alee.utils.xml.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides a set of utilities to easily serialize and deserialize objects into and from XML.
 * There are is a lot of methods to read specific library objects using various resource files.
 *
 * @author Mikle Garin
 */

public final class XmlUtils
{
    /**
     * Whether should offer better aliases for standard Java classes like Point and Rectangle or not.
     */
    public static boolean aliasJdkClasses = true;

    /**
     * Custom converters.
     */
    public static final Point2DConverter point2dConverter = new Point2DConverter ();
    public static final ColorConverter colorConverter = new ColorConverter ();
    public static final DimensionConverter dimensionConverter = new DimensionConverter ();
    public static final InsetsConverter insetsConverter = new InsetsConverter ();
    public static final StrokeConverter strokeConverter = new StrokeConverter ();

    /**
     * Custom password converter that encrypts serialized passwords.
     */
    public static final PasswordConverter passwordConverter = new PasswordConverter ();

    /**
     * XStream instance.
     */
    private static XStream xStream = null;

    /**
     * Returns global XStream instance configured with all required aliases and converters.
     *
     * @return XStream
     */
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
            // XStream instance initialization
            xStream = new XStream ( new DomDriver () );
            // xStream.setMode ( XStream.ID_REFERENCES );

            // Standard Java-classes aliases
            if ( aliasJdkClasses )
            {
                // Custom {@link java.awt.Point} mapping
                xStream.alias ( "Point", Point.class );
                xStream.useAttributeFor ( Point.class, "x" );
                xStream.useAttributeFor ( Point.class, "y" );

                // Custom {@link java.awt.geom.Point2D} mapping
                xStream.alias ( "Point2D", Point2D.class );
                xStream.registerConverter ( point2dConverter );

                // Custom {@link java.awt.Dimension} mapping
                xStream.alias ( "Dimension", Dimension.class );
                xStream.registerConverter ( dimensionConverter );

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
                xStream.registerConverter ( colorConverter );

                // Custom {@link java.awt.Insets} mapping
                xStream.alias ( "Insets", Insets.class );
                xStream.registerConverter ( insetsConverter );

                // Custom {@link java.awt.Stroke} mapping
                xStream.alias ( "Stroke", Stroke.class );
                xStream.registerConverter ( strokeConverter );
            }

            // XML resources aliases
            xStream.processAnnotations ( ResourceLocation.class );
            xStream.processAnnotations ( ResourceFile.class );
            xStream.processAnnotations ( ResourceList.class );
            xStream.processAnnotations ( ResourceMap.class );

            // Additional WebLaF data classes aliases
            xStream.processAnnotations ( ValuesTable.class );
            xStream.processAnnotations ( Pair.class );

            // Plugin manager classes aliases
            xStream.processAnnotations ( PluginInformation.class );
            xStream.processAnnotations ( PluginVersion.class );
            xStream.processAnnotations ( PluginDependency.class );
            xStream.processAnnotations ( PluginLibrary.class );
        }
        catch ( final Throwable e )
        {
            Log.get ().error ( "Unable to initialize XStream instance", e );
        }
    }

    /**
     * Process the annotations of the given type and configure the XStream.
     * A call of this method will automatically turn the auto-detection mode for annotations off.
     *
     * @param type the type with XStream annotations
     */
    public static void processAnnotations ( final Class type )
    {
        getXStream ().processAnnotations ( type );
    }

    /**
     * Process the annotations of the given types and configure the XStream.
     *
     * @param types the types with XStream annotations
     */
    public static void processAnnotations ( final Class[] types )
    {
        getXStream ().processAnnotations ( types );
    }

    /**
     * Alias a Class to a shorter name to be used in XML elements.
     *
     * @param name Short name
     * @param type Type to be aliased
     */
    public static void alias ( final String name, final Class type )
    {
        getXStream ().alias ( name, type );
    }

    /**
     * Use an attribute for a field declared in a specific type.
     *
     * @param type  the name of the field
     * @param field the Class containing such field
     */
    public static void useAttributeFor ( final Class type, final String field )
    {
        getXStream ().useAttributeFor ( type, field );
    }

    /**
     * Adds an implicit array.
     *
     * @param type  class owning the implicit array
     * @param field name of the array field
     */
    public static void addImplicitArray ( final Class type, final String field )
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
    public static void addImplicitArray ( final Class type, final String field, final String itemName )
    {
        getXStream ().addImplicitArray ( type, field, itemName );
    }

    /**
     * Adds a new converter into converters registry.
     *
     * @param converter the new converter
     */
    public static void registerConverter ( final Converter converter )
    {
        getXStream ().registerConverter ( converter );
    }

    /**
     * Adds a new converter into converters registry.
     *
     * @param converter the new converter
     */
    public static void registerConverter ( final SingleValueConverter converter )
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
    public static <T extends AliasProvider> void alias ( final Class<T> aliasProvider )
    {
        ReflectUtils.callStaticMethodSafely ( aliasProvider, AliasProvider.methodName, getXStream () );
    }

    /**
     * Serializes Object into XML and writes it into specified file.
     *
     * @param obj  object to serialize
     * @param file output file
     */
    public static void toXML ( final Object obj, final String file )
    {
        toXML ( obj, new File ( file ) );
    }

    /**
     * Serializes Object into XML and writes it into specified file.
     *
     * @param obj  object to serialize
     * @param file output file
     */
    public static void toXML ( final Object obj, final File file )
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
            Log.get ().error ( TextUtils.format ( "Unable to serialize object %s into XML and write it into file: %s", obj, file ), e );
        }
    }

    /**
     * Returns Object serialized into XML.
     *
     * @param obj object to serialize
     * @return serialized into XML object representation
     */
    public static String toXML ( final Object obj )
    {
        return getXStream ().toXML ( obj );
    }

    /**
     * Serializes Object into XML and writes it using a specified Writer.
     *
     * @param obj object to serialize
     * @param out output writer
     */
    public static void toXML ( final Object obj, final Writer out )
    {
        getXStream ().toXML ( obj, out );
    }

    /**
     * Serializes Object into XML and writes it using a specified OutputStream.
     *
     * @param obj object to serialize
     * @param out output stream
     */
    public static void toXML ( final Object obj, final OutputStream out )
    {
        getXStream ().toXML ( obj, out );
    }

    /**
     * Serializes Object into XML and writes it using a specified HierarchicalStreamWriter
     *
     * @param obj    object to serialize
     * @param writer hierarchical stream writer
     */
    public static void toXML ( final Object obj, final HierarchicalStreamWriter writer )
    {
        getXStream ().marshal ( obj, writer );
    }

    /**
     * Returns Object deserialized from XML content.
     *
     * @param reader XML text source
     * @param <T>    read object type
     * @return deserialized object
     */
    public static <T> T fromXML ( final Reader reader )
    {
        return ( T ) getXStream ().fromXML ( reader );
    }

    /**
     * Returns Object deserialized from XML content.
     *
     * @param input XML text source
     * @param <T>   read object type
     * @return deserialized object
     */
    public static <T> T fromXML ( final InputStream input )
    {
        return ( T ) getXStream ().fromXML ( input );
    }

    /**
     * Returns Object deserialized from XML text
     *
     * @param url XML text source
     * @param <T> read object type
     * @return deserialized object
     */
    public static <T> T fromXML ( final URL url )
    {
        return ( T ) getXStream ().fromXML ( url );
    }

    /**
     * Returns Object deserialized from XML content.
     *
     * @param file file with XML content
     * @param <T>  read object type
     * @return deserialized object
     */
    public static <T> T fromXML ( final File file )
    {
        return ( T ) getXStream ().fromXML ( file );
    }

    /**
     * Returns Object deserialized from XML content.
     *
     * @param xml XML content
     * @param <T> read object type
     * @return deserialized object
     */
    public static <T> T fromXML ( final String xml )
    {
        return ( T ) getXStream ().fromXML ( xml );
    }

    /**
     * Returns Object deserialized from XML content.
     *
     * @param source XML text source
     * @param <T>    read object type
     * @return deserialized object
     */
    public static <T> T fromXML ( final Object source )
    {
        if ( source instanceof URL )
        {
            return fromXML ( ( URL ) source );
        }
        else if ( source instanceof String )
        {
            return fromXML ( new File ( ( String ) source ) );
        }
        else if ( source instanceof File )
        {
            return fromXML ( ( File ) source );
        }
        else if ( source instanceof Reader )
        {
            return fromXML ( ( Reader ) source );
        }
        else if ( source instanceof InputStream )
        {
            return fromXML ( ( InputStream ) source );
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns Object deserialized from XML text
     *
     * @param resource XML text source description
     * @param <T>      read object type
     * @return deserialized object
     */
    public static <T> T fromXML ( final ResourceFile resource )
    {
        return fromXML ( resource, true );
    }

    /**
     * Returns Object deserialized from XML text
     *
     * @param resource XML text source description
     * @param safely   whether or not should try to retrieve object from XML safely without throwing any exceptions
     * @param <T>      read object type
     * @return deserialized object
     */
    public static <T> T fromXML ( final ResourceFile resource, final boolean safely )
    {
        try
        {
            switch ( resource.getLocation () )
            {
                case url:
                {
                    return XmlUtils.fromXML ( new URL ( resource.getSource () ) );
                }
                case filePath:
                {
                    return XmlUtils.fromXML ( new File ( resource.getSource () ) );
                }
                case nearClass:
                {
                    // todo Replace with try-with-resource when switched to JDK8+
                    InputStream is = null;
                    try
                    {
                        is = Class.forName ( resource.getClassName () ).getResourceAsStream ( resource.getSource () );
                        if ( is == null )
                        {
                            final String src = resource.getSource ();
                            final String cn = resource.getClassName ();
                            throw new RuntimeException ( "Unable to read XML file \"" + src + "\" near class \"" + cn + "\"" );
                        }
                        return XmlUtils.fromXML ( is );
                    }
                    finally
                    {
                        try
                        {
                            if ( is != null )
                            {
                                is.close ();
                            }
                        }
                        catch ( final Throwable e )
                        {
                            // Ignore this exception
                        }
                    }
                }
                default:
                {
                    return null;
                }
            }
        }
        catch ( final Throwable e )
        {
            if ( safely )
            {
                Log.get ().error ( "Unable to deserialize object from XML", e );
                return null;
            }
            else
            {
                throw new RuntimeException ( "Unable to deserialize object from XML", e );
            }
        }
    }

    /**
     * Returns text which is read from the source.
     *
     * @param source one of possible sources: URL, String, File, Reader, InputStream
     * @return text as String
     */
    public static String loadString ( final Object source )
    {
        return loadString ( loadResourceFile ( source ) );
    }

    /**
     * Returns text which is read from specified ResourceFile.
     *
     * @param resource file description
     * @return text as String
     */
    public static String loadString ( final ResourceFile resource )
    {
        if ( resource.getLocation ().equals ( ResourceLocation.url ) )
        {
            try
            {
                return FileUtils.readToString ( new URL ( resource.getSource () ) );
            }
            catch ( final IOException e )
            {
                Log.get ().error ( "Unable to read string from URL: " + resource.getSource (), e );
                return null;
            }
        }
        if ( resource.getLocation ().equals ( ResourceLocation.filePath ) )
        {
            return FileUtils.readToString ( new File ( resource.getSource () ) );
        }
        else if ( resource.getLocation ().equals ( ResourceLocation.nearClass ) )
        {
            try
            {
                return FileUtils.readToString ( Class.forName ( resource.getClassName () ), resource.getSource () );
            }
            catch ( final ClassNotFoundException e )
            {
                final String msg = "Unable to read string from file: %s near class: %s";
                Log.get ().error ( TextUtils.format ( msg, resource.getSource (), resource.getClassName () ), e );
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns ImageIcon which is read from the source.
     *
     * @param source one of possible sources: URL, String, File, Reader, InputStream
     * @return ImageIcon
     */
    public static ImageIcon loadImageIcon ( final Object source )
    {
        return loadImageIcon ( loadResourceFile ( source ) );
    }

    /**
     * Returns ImageIcon which is read from specified ResourceFile.
     *
     * @param resource file description
     * @return ImageIcon
     */
    public static ImageIcon loadImageIcon ( final ResourceFile resource )
    {
        if ( resource.getLocation ().equals ( ResourceLocation.url ) )
        {
            try
            {
                return new ImageIcon ( new URL ( resource.getSource () ) );
            }
            catch ( final MalformedURLException e )
            {
                Log.get ().error ( "Unable to load image from URL: " + resource.getSource (), e );
                return null;
            }
        }
        if ( resource.getLocation ().equals ( ResourceLocation.filePath ) )
        {
            try
            {
                return new ImageIcon ( new File ( resource.getSource () ).getCanonicalPath () );
            }
            catch ( final IOException e )
            {
                Log.get ().error ( "Unable to load image from file: " + resource.getSource (), e );
                return null;
            }
        }
        else if ( resource.getLocation ().equals ( ResourceLocation.nearClass ) )
        {
            try
            {
                return new ImageIcon ( Class.forName ( resource.getClassName () ).getResource ( resource.getSource () ) );
            }
            catch ( final ClassNotFoundException e )
            {
                final String msg = "Unable to load image from file: %s near class: %s";
                Log.get ().error ( TextUtils.format ( msg, resource.getSource (), resource.getClassName () ), e );
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns ImageIcon list which is read from the source.
     *
     * @param source one of possible sources: URL, String, File, Reader, InputStream
     * @return ImageIcon list
     */
    public static List<ImageIcon> loadImagesList ( final Object source )
    {
        return loadImagesList ( loadResourceList ( source ) );
    }

    /**
     * Returns ImageIcon list which is read from specified ResourceList.
     *
     * @param resourceList ResourceFile list
     * @return ImageIcon list
     */
    public static List<ImageIcon> loadImagesList ( final ResourceList resourceList )
    {
        final List<ImageIcon> icons = new ArrayList<ImageIcon> ();
        for ( final ResourceFile resource : resourceList.getResources () )
        {
            final ImageIcon imageIcon = loadImageIcon ( resource );
            if ( imageIcon != null )
            {
                icons.add ( imageIcon );
            }
        }
        return icons;
    }

    /**
     * Returns ResourceMap which is read from the source.
     *
     * @param source one of possible sources: URL, String, File, Reader, InputStream
     * @return ResourceMap
     */
    public static ResourceMap loadResourceMap ( final Object source )
    {
        return fromXML ( source );
    }

    /**
     * Returns ResourceList which is read from the source.
     *
     * @param source one of possible sources: URL, String, File, Reader, InputStream
     * @return ResourceList
     */
    public static ResourceList loadResourceList ( final Object source )
    {
        return fromXML ( source );
    }

    /**
     * Returns ResourceFile which is read from the source.
     *
     * @param source one of possible sources: URL, String, File, Reader, InputStream
     * @return ResourceFile
     */
    public static ResourceFile loadResourceFile ( final Object source )
    {
        return fromXML ( source );
    }
}