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

package com.alee.managers.settings;

import com.alee.utils.xml.XMLChar;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Custom XStream converter for {@link SettingsGroup}.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see SettingsManager
 */
public class SettingsConverter extends ReflectionConverter
{
    /**
     * Special value type for null values.
     */
    private static final String NULL_TYPE = "null";

    /**
     * Constructs new {@link SettingsConverter}.
     *
     * @param mapper             {@link Mapper}
     * @param reflectionProvider {@link ReflectionProvider}
     */
    public SettingsConverter ( final Mapper mapper, final ReflectionProvider reflectionProvider )
    {
        super ( mapper, reflectionProvider );
    }

    @Override
    public boolean canConvert ( final Class type )
    {
        return type.equals ( SettingsGroup.class );
    }

    @Override
    public void marshal ( final Object source, final HierarchicalStreamWriter writer, final MarshallingContext context )
    {
        final SettingsGroup settingsGroup = ( SettingsGroup ) source;

        // Converting group information
        writer.addAttribute ( "id", settingsGroup.getId () );
        writer.addAttribute ( "name", settingsGroup.getName () );

        // Converting settings
        for ( final Map.Entry<String, Object> entry : settingsGroup.settings ().entrySet () )
        {
            // If key text is proper for node name it will be used, otherwise it will be separated
            final String key = entry.getKey ();
            final Object value = entry.getValue ();

            // Starting entry node
            final String nodeName;
            if ( XMLChar.isValidName ( key ) )
            {
                nodeName = key;
                writer.startNode ( nodeName );
            }
            else
            {
                nodeName = "entry";
                writer.startNode ( nodeName );
                writer.addAttribute ( "key", key );
            }

            if ( value == null )
            {
                // Adding special null value type
                writer.addAttribute ( "type", NULL_TYPE );
            }
            else
            {
                // Adding type reference if it is not the same as node name
                // This condition added to remove redundant type duplications
                final String serializedType = mapper.serializedClass ( value.getClass () );
                if ( !nodeName.equals ( serializedType ) )
                {
                    writer.addAttribute ( "type", serializedType );
                }

                // Converting value
                context.convertAnother ( value );
            }

            // Closing entry node
            writer.endNode ();
        }
    }

    @Override
    public Object unmarshal ( final HierarchicalStreamReader reader, final UnmarshallingContext context )
    {
        // Creating settings group
        final SettingsGroup settingsGroup = new SettingsGroup ( reader.getAttribute ( "id" ), reader.getAttribute ( "name" ) );

        // Collecting readable settings
        final HashMap<String, Object> settings = new HashMap<String, Object> ();
        while ( reader.hasMoreChildren () )
        {
            // Read next map entry
            reader.moveDown ();
            final String nodeName = reader.getNodeName ();
            if ( nodeName.equals ( "entry" ) && reader.getAttributeCount () == 0 )
            {
                // Old settings style (implicit map)
                // Added for backward compatibility with old settings format

                // Reading key
                reader.moveDown ();
                final String key = reader.getValue ();
                reader.moveUp ();

                // Reading value
                reader.moveDown ();
                final Class type = mapper.realClass ( reader.getNodeName () );
                final Object value = context.convertAnother ( settings, type );
                settings.put ( key, value );
                reader.moveUp ();
            }
            else
            {
                // Reading new settings style
                final String keyAttribute = reader.getAttribute ( "key" );
                final String key = keyAttribute != null ? keyAttribute : nodeName;
                try
                {
                    // Determining data type
                    final String typeAttribute = reader.getAttribute ( "type" );
                    final String actualType = typeAttribute != null ? typeAttribute : nodeName;
                    if ( actualType.equals ( NULL_TYPE ) )
                    {
                        // Adding null value
                        settings.put ( key, null );
                    }
                    else
                    {
                        // Parsing data for the specified type
                        final Class type = mapper.realClass ( actualType );
                        settings.put ( key, context.convertAnother ( settings, type ) );
                    }
                }
                catch ( final Exception e )
                {
                    final String msg = "Unable to load settings entry for group '%s' under key '%s' due to unexpected exception";
                    final String fmsg = String.format ( msg, settingsGroup.getName (), key );
                    LoggerFactory.getLogger ( SettingsConverter.class ).error ( fmsg, e );
                }
            }
            reader.moveUp ();
        }
        settingsGroup.setSettings ( settings );

        return settingsGroup;
    }
}