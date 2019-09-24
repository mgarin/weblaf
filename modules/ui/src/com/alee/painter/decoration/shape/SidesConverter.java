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

package com.alee.painter.decoration.shape;

import com.alee.api.annotations.NotNull;
import com.alee.api.jdk.Objects;
import com.alee.utils.TextUtils;
import com.alee.utils.xml.XmlException;
import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import static com.alee.painter.decoration.shape.Sides.*;

/**
 * Custom XStream converter for {@link Sides}.
 *
 * @author Mikle Garin
 * @see Sides
 */
public final class SidesConverter extends AbstractSingleValueConverter
{
    /**
     * Values separator.
     */
    public static final String separator = ",";

    @Override
    public boolean canConvert ( @NotNull final Class type )
    {
        return Sides.class.isAssignableFrom ( type );
    }

    @NotNull
    @Override
    public String toString ( @NotNull final Object object )
    {
        return sidesToString ( ( Sides ) object );
    }

    @NotNull
    @Override
    public Object fromString ( @NotNull final String sides )
    {
        return sidesFromString ( sides );
    }

    /**
     * Returns {@link Sides} converted into string.
     *
     * @param sides {@link Sides} to convert
     * @return {@link Sides} converted into string
     */
    @NotNull
    public static String sidesToString ( @NotNull final Sides sides )
    {
        final List<String> list = new ArrayList<String> ( 4 );
        if ( sides.top )
        {
            list.add ( TOP );
        }
        if ( sides.left )
        {
            list.add ( LEFT );
        }
        if ( sides.bottom )
        {
            list.add ( BOTTOM );
        }
        if ( sides.right )
        {
            list.add ( RIGHT );
        }
        if ( list.isEmpty () )
        {
            list.add ( NONE );
        }
        return TextUtils.listToString ( list, separator );
    }

    /**
     * Returns {@link Sides} read from string.
     *
     * @param string {@link Sides} string
     * @return {@link Sides} read from string
     */
    @NotNull
    public static Sides sidesFromString ( final String string )
    {
        try
        {
            final Sides sides;
            if ( TextUtils.notEmpty ( string ) )
            {
                final StringTokenizer tokenizer = new StringTokenizer ( string, separator, false );
                if ( tokenizer.hasMoreTokens () )
                {
                    final String first = tokenizer.nextToken ().trim ();
                    if ( Objects.equals ( first, TOP, LEFT, BOTTOM, RIGHT, NONE ) )
                    {
                        final boolean top = string.contains ( TOP );
                        final boolean left = string.contains ( LEFT );
                        final boolean bottom = string.contains ( BOTTOM );
                        final boolean right = string.contains ( RIGHT );
                        sides = new Sides ( top, left, bottom, right );
                    }
                    else if ( first.length () == 1 && ( first.charAt ( 0 ) == '0' || first.charAt ( 0 ) == '1' ) )
                    {
                        final boolean top = Integer.parseInt ( tokenizer.nextToken () ) == 1;
                        if ( tokenizer.hasMoreTokens () )
                        {
                            final boolean left = Integer.parseInt ( tokenizer.nextToken () ) == 1;
                            if ( tokenizer.hasMoreTokens () )
                            {
                                final boolean bottom = Integer.parseInt ( tokenizer.nextToken () ) == 1;
                                if ( tokenizer.hasMoreTokens () )
                                {
                                    final boolean right = Integer.parseInt ( tokenizer.nextToken () ) == 1;
                                    sides = new Sides ( top, left, bottom, right );
                                }
                                else
                                {
                                    sides = new Sides ( top, left, bottom );
                                }
                            }
                            else
                            {
                                sides = new Sides ( top, left );
                            }
                        }
                        else
                        {
                            sides = new Sides ( top );
                        }
                    }
                    else if ( first.equalsIgnoreCase ( "true" ) || first.equalsIgnoreCase ( "false" ) )
                    {
                        final boolean top = Boolean.parseBoolean ( tokenizer.nextToken () );
                        if ( tokenizer.hasMoreTokens () )
                        {
                            final boolean left = Boolean.parseBoolean ( tokenizer.nextToken () );
                            if ( tokenizer.hasMoreTokens () )
                            {
                                final boolean bottom = Boolean.parseBoolean ( tokenizer.nextToken () );
                                if ( tokenizer.hasMoreTokens () )
                                {
                                    final boolean right = Boolean.parseBoolean ( tokenizer.nextToken () );
                                    sides = new Sides ( top, left, bottom, right );
                                }
                                else
                                {
                                    sides = new Sides ( top, left, bottom );
                                }
                            }
                            else
                            {
                                sides = new Sides ( top, left );
                            }
                        }
                        else
                        {
                            sides = new Sides ( top );
                        }
                    }
                    else
                    {
                        throw new ConversionException ( "Unknown Sides format used: " + string );
                    }
                }
                else
                {
                    sides = new Sides ( false );
                }
            }
            else
            {
                sides = new Sides ( false );
            }
            return sides;
        }
        catch ( final Exception e )
        {
            throw new XmlException ( "Unable to parse Sides: " + string, e );
        }
    }
}