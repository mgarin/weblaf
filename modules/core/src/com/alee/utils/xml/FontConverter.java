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

package com.alee.utils.xml;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

import java.awt.*;
import java.util.StringTokenizer;

/**
 * Custom XStream converter for {@link Font}.
 * It uses a pretty simple algorithm and doesn't serialize custom font attributes.
 *
 * @author Mikle Garin
 */

public class FontConverter extends AbstractSingleValueConverter
{
    /**
     * Values separator.
     */
    public static final String separator = ";";

    /**
     * Default font style.
     */
    public static final int defaultStyle = Font.PLAIN;

    /**
     * Default font size.
     */
    public static final int defaultSize = 12;

    @Override
    public boolean canConvert ( final Class type )
    {
        return Font.class.isAssignableFrom ( type );
    }

    @Override
    public String toString ( final Object object )
    {
        return fontToString ( ( Font ) object );
    }

    @Override
    public Object fromString ( final String dimension )
    {
        return fontFromString ( dimension );
    }

    /**
     * Returns {@link Font} converted into string.
     *
     * @param font {@link Font} to convert
     * @return {@link Font} converted into string
     */
    public static String fontToString ( final Font font )
    {
        final boolean dst = font.getStyle () == defaultStyle;
        final boolean dsz = font.getStyle () == defaultSize;
        return font.getName () + ( !dst || !dsz ? separator + font.getStyle () + ( !dsz ? separator + font.getSize () : "" ) : "" );
    }

    /**
     * Returns {@link Font} read from string.
     *
     * @param font {@link Font} string
     * @return {@link Font} read from string
     */
    public static Font fontFromString ( final String font )
    {
        try
        {
            final StringTokenizer t = new StringTokenizer ( font, separator, false );
            final String name = t.nextToken ().trim ();
            final int style = t.hasMoreElements () ? Integer.parseInt ( t.nextToken ().trim () ) : defaultStyle;
            final int size = t.hasMoreElements () ? Integer.parseInt ( t.nextToken ().trim () ) : defaultSize;
            return new Font ( name, style, size );
        }
        catch ( final Exception e )
        {
            throw new XmlException ( "Unable to parse Font: " + font, e );
        }
    }
}