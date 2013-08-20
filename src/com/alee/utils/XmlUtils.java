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

import com.alee.extended.colorchooser.GradientColorData;
import com.alee.extended.colorchooser.GradientData;
import com.alee.extended.painter.NinePatchIconPainter;
import com.alee.extended.painter.NinePatchStatePainter;
import com.alee.extended.painter.TexturePainter;
import com.alee.laf.colorchooser.HSBColor;
import com.alee.laf.tree.NodeState;
import com.alee.laf.tree.TreeState;
import com.alee.utils.collection.ValuesTable;
import com.alee.utils.ninepatch.NinePatchIcon;
import com.alee.utils.xml.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.SingleValueConverter;

import javax.swing.*;
import java.awt.*;
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
     * Custom color converter.
     */
    public static final ColorConverter colorConverter = new ColorConverter ();

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
            // XStream instnce initialization
            xStream = new XStream ();

            // Standart Java-classes aliases
            xStream.alias ( "Point", Point.class );
            xStream.useAttributeFor ( Point.class, "x" );
            xStream.useAttributeFor ( Point.class, "y" );
            xStream.alias ( "Dimension", Dimension.class );
            xStream.useAttributeFor ( Dimension.class, "width" );
            xStream.useAttributeFor ( Dimension.class, "height" );
            xStream.alias ( "Rectangle", Rectangle.class );
            xStream.useAttributeFor ( Rectangle.class, "x" );
            xStream.useAttributeFor ( Rectangle.class, "y" );
            xStream.useAttributeFor ( Rectangle.class, "width" );
            xStream.useAttributeFor ( Rectangle.class, "height" );
            xStream.alias ( "Insets", Insets.class );
            xStream.useAttributeFor ( Insets.class, "top" );
            xStream.useAttributeFor ( Insets.class, "left" );
            xStream.useAttributeFor ( Insets.class, "bottom" );
            xStream.useAttributeFor ( Insets.class, "right" );
            xStream.alias ( "Font", Font.class );
            xStream.alias ( "Color", Color.class );
            xStream.registerConverter ( colorConverter );

            // XML resources aliases
            xStream.processAnnotations ( ResourceLocation.class );
            xStream.processAnnotations ( ResourceFile.class );
            xStream.processAnnotations ( ResourceList.class );
            xStream.processAnnotations ( ResourceMap.class );
            xStream.processAnnotations ( ValuesTable.class );
            xStream.processAnnotations ( TreeState.class );
            xStream.processAnnotations ( NodeState.class );

            // Additional WebLaF data classes aliases
            xStream.processAnnotations ( GradientData.class );
            xStream.processAnnotations ( GradientColorData.class );
            xStream.processAnnotations ( HSBColor.class );
        }
        catch ( Throwable e )
        {
            e.printStackTrace ();
        }
    }

    /**
     * Process the annotations of the given type and configure the XStream.
     * A call of this method will automatically turn the auto-detection mode for annotations off.
     *
     * @param type the type with XStream annotations
     */
    public static void processAnnotations ( Class type )
    {
        getXStream ().processAnnotations ( type );
    }

    /**
     * Process the annotations of the given types and configure the XStream.
     *
     * @param types the types with XStream annotations
     */
    public static void processAnnotations ( Class[] types )
    {
        getXStream ().processAnnotations ( types );
    }

    /**
     * Alias a Class to a shorter name to be used in XML elements.
     *
     * @param name Short name
     * @param type Type to be aliased
     */
    public static void alias ( String name, Class type )
    {
        getXStream ().alias ( name, type );
    }

    /**
     * Use an attribute for a field declared in a specific type.
     *
     * @param type  the name of the field
     * @param field the Class containing such field
     */
    public static void useAttributeFor ( Class type, String field )
    {
        getXStream ().useAttributeFor ( type, field );
    }

    /**
     * Adds an implicit array.
     *
     * @param type  class owning the implicit array
     * @param field name of the array field
     */
    public static void addImplicitArray ( Class type, String field )
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
    public static void addImplicitArray ( Class type, String field, String itemName )
    {
        getXStream ().addImplicitArray ( type, field, itemName );
    }

    /**
     * Adds a new converter into converters registry.
     *
     * @param converter the new converter
     */
    public static void registerConverter ( Converter converter )
    {
        getXStream ().registerConverter ( converter );
    }

    /**
     * Adds a new converter into converters registry.
     *
     * @param converter the new converter
     */
    public static void registerConverter ( SingleValueConverter converter )
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
    public static <T extends AliasProvider> void alias ( Class<T> aliasProvider )
    {
        ReflectUtils.callStaticMethodSafely ( aliasProvider, AliasProvider.methodName, getXStream () );
    }

    /**
     * Serializes Object into XML and writes it into specified file.
     *
     * @param obj  object to serialize
     * @param file output file
     */
    public static void toXML ( Object obj, String file )
    {
        toXML ( obj, new File ( file ) );
    }

    /**
     * Serializes Object into XML and writes it into specified file.
     *
     * @param obj  object to serialize
     * @param file output file
     */
    public static void toXML ( Object obj, File file )
    {
        try
        {
            FileOutputStream fos = new FileOutputStream ( file );
            OutputStreamWriter osw = new OutputStreamWriter ( fos, "UTF-8" );
            toXML ( obj, osw );
            osw.close ();
            fos.close ();
        }
        catch ( IOException e )
        {
            e.printStackTrace ();
        }
    }

    /**
     * Returns Object serialized into XML.
     *
     * @param obj object to serialize
     * @return serialized into XML object representation
     */
    public static String toXML ( Object obj )
    {
        return getXStream ().toXML ( obj );
    }

    /**
     * Serializes Object into XML and writes it using a specified Writer.
     *
     * @param obj object to serialize
     * @param out output writer
     */
    public static void toXML ( Object obj, Writer out )
    {
        getXStream ().toXML ( obj, out );
    }

    /**
     * Serializes Object into XML and writes it using a specified OutputStream.
     *
     * @param obj object to serialize
     * @param out output stream
     */
    public static void toXML ( Object obj, OutputStream out )
    {
        getXStream ().toXML ( obj, out );
    }

    /**
     * Returns Object deserialized from XML content.
     *
     * @param reader XML text source
     * @param <T>    read object type
     * @return deserialized object
     */
    public static <T> T fromXML ( Reader reader )
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
    public static <T> T fromXML ( InputStream input )
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
    public static <T> T fromXML ( URL url )
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
    public static <T> T fromXML ( File file )
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
    public static <T> T fromXML ( String xml )
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
    public static <T> T fromXML ( Object source )
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
    public static <T> T fromXML ( ResourceFile resource )
    {
        switch ( resource.getLocation () )
        {
            case url:
            {
                try
                {
                    return XmlUtils.fromXML ( new URL ( resource.getSource () ) );
                }
                catch ( MalformedURLException e )
                {
                    e.printStackTrace ();
                    return null;
                }
            }
            case filePath:
            {
                return XmlUtils.fromXML ( new File ( resource.getSource () ) );
            }
            case nearClass:
            {
                InputStream is = null;
                try
                {
                    is = Class.forName ( resource.getClassName () ).getResourceAsStream ( resource.getSource () );
                    return XmlUtils.fromXML ( is );
                }
                catch ( ClassNotFoundException e )
                {
                    e.printStackTrace ();
                    return null;
                }
                finally
                {
                    try
                    {
                        is.close ();
                    }
                    catch ( Throwable e )
                    {
                        e.printStackTrace ();
                    }
                }
            }
            default:
            {
                return null;
            }
        }
    }

    /**
     * Returns text which is read from the source.
     *
     * @param source one of possible sources: URL, String, File, Reader, InputStream
     * @return text as String
     */
    public static String loadString ( Object source )
    {
        return loadString ( loadResourceFile ( source ) );
    }

    /**
     * Returns text which is read from specified ResourceFile.
     *
     * @param resource file description
     * @return text as String
     */
    public static String loadString ( ResourceFile resource )
    {
        if ( resource.getLocation ().equals ( ResourceLocation.url ) )
        {
            try
            {
                return FileUtils
                        .readToString ( new BufferedReader ( new InputStreamReader ( new URL ( resource.getSource () ).openStream () ) ) );
            }
            catch ( IOException e )
            {
                e.printStackTrace ();
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
            catch ( ClassNotFoundException e )
            {
                e.printStackTrace ();
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
    public static ImageIcon loadImageIcon ( Object source )
    {
        return loadImageIcon ( loadResourceFile ( source ) );
    }

    /**
     * Returns ImageIcon which is read from specified ResourceFile.
     *
     * @param resource file description
     * @return ImageIcon
     */
    public static ImageIcon loadImageIcon ( ResourceFile resource )
    {
        if ( resource.getLocation ().equals ( ResourceLocation.url ) )
        {
            try
            {
                return new ImageIcon ( new URL ( resource.getSource () ) );
            }
            catch ( MalformedURLException e )
            {
                e.printStackTrace ();
                return null;
            }
        }
        if ( resource.getLocation ().equals ( ResourceLocation.filePath ) )
        {
            try
            {
                return new ImageIcon ( new File ( resource.getSource () ).getCanonicalPath () );
            }
            catch ( IOException e )
            {
                e.printStackTrace ();
                return null;
            }
        }
        else if ( resource.getLocation ().equals ( ResourceLocation.nearClass ) )
        {
            try
            {
                return new ImageIcon ( Class.forName ( resource.getClassName () ).getResource ( resource.getSource () ) );
            }
            catch ( ClassNotFoundException e )
            {
                e.printStackTrace ();
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
    public static List<ImageIcon> loadImagesList ( Object source )
    {
        return loadImagesList ( loadResourceList ( source ) );
    }

    /**
     * Returns ImageIcon list which is read from specified ResourceList.
     *
     * @param resourceList ResourceFile list
     * @return ImageIcon list
     */
    public static List<ImageIcon> loadImagesList ( ResourceList resourceList )
    {
        List<ImageIcon> icons = new ArrayList<ImageIcon> ();
        for ( ResourceFile resource : resourceList.getResources () )
        {
            ImageIcon imageIcon = loadImageIcon ( resource );
            if ( imageIcon != null )
            {
                icons.add ( imageIcon );
            }
        }
        return icons;
    }

    /**
     * Returns NinePatchIcon which is read from the source.
     *
     * @param source one of possible sources: URL, String, File, Reader, InputStream
     * @return NinePatchIcon
     */
    public static NinePatchIcon loadNinePatchIcon ( Object source )
    {
        return loadNinePatchIcon ( loadResourceFile ( source ) );
    }

    /**
     * Returns NinePatchIcon which is read from specified ResourceFile.
     *
     * @param resource file description
     * @return NinePatchIcon
     */
    public static NinePatchIcon loadNinePatchIcon ( ResourceFile resource )
    {
        return new NinePatchIcon ( loadImageIcon ( resource ) );
    }

    /**
     * Returns NinePatchStatePainter which is read from the source.
     *
     * @param source one of possible sources: URL, String, File, Reader, InputStream
     * @return NinePatchStatePainter
     */
    public static NinePatchStatePainter loadNinePatchStatePainter ( Object source )
    {
        return loadNinePatchStatePainter ( loadResourceMap ( source ) );
    }

    /**
     * Returns NinePatchStatePainter which is read from specified ResourceMap.
     *
     * @param resourceMap ResourceFile map
     * @return NinePatchStatePainter
     */
    public static NinePatchStatePainter loadNinePatchStatePainter ( ResourceMap resourceMap )
    {
        NinePatchStatePainter sbp = new NinePatchStatePainter ();
        for ( String key : resourceMap.getStates ().keySet () )
        {
            sbp.addStateIcon ( key, loadNinePatchIcon ( resourceMap.getState ( key ) ) );
        }
        return sbp;
    }

    /**
     * Returns NinePatchIconPainter which is read from the source.
     *
     * @param source one of possible sources: URL, String, File, Reader, InputStream
     * @return NinePatchIconPainter
     */
    public static NinePatchIconPainter loadNinePatchIconPainter ( Object source )
    {
        return loadNinePatchIconPainter ( loadResourceFile ( source ) );
    }

    /**
     * Returns NinePatchIconPainter which is read from specified ResourceFile.
     *
     * @param resource file description
     * @return NinePatchIconPainter
     */
    public static NinePatchIconPainter loadNinePatchIconPainter ( ResourceFile resource )
    {
        return new NinePatchIconPainter ( loadNinePatchIcon ( resource ) );
    }

    /**
     * Returns TexturePainter which is read from the source.
     *
     * @param source one of possible sources: URL, String, File, Reader, InputStream
     * @return TexturePainter
     */
    public static TexturePainter loadTexturePainter ( Object source )
    {
        return loadTexturePainter ( loadResourceFile ( source ) );
    }

    /**
     * Returns TexturePainter which is read from specified ResourceFile.
     *
     * @param resource file description
     * @return TexturePainter
     */
    public static TexturePainter loadTexturePainter ( ResourceFile resource )
    {
        return new TexturePainter ( loadImageIcon ( resource ) );
    }

    /**
     * Returns ResourceMap which is read from the source.
     *
     * @param source one of possible sources: URL, String, File, Reader, InputStream
     * @return ResourceMap
     */
    public static ResourceMap loadResourceMap ( Object source )
    {
        return fromXML ( source );
    }

    /**
     * Returns ResourceList which is read from the source.
     *
     * @param source one of possible sources: URL, String, File, Reader, InputStream
     * @return ResourceList
     */
    public static ResourceList loadResourceList ( Object source )
    {
        return fromXML ( source );
    }

    /**
     * Returns ResourceFile which is read from the source.
     *
     * @param source one of possible sources: URL, String, File, Reader, InputStream
     * @return ResourceFile
     */
    public static ResourceFile loadResourceFile ( Object source )
    {
        return fromXML ( source );
    }
}