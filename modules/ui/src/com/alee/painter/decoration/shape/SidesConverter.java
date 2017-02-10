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

import com.alee.managers.log.Log;
import com.alee.utils.CompareUtils;
import com.alee.utils.TextUtils;
import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import static com.alee.painter.decoration.shape.Sides.*;

/**
 * Custom {@link Sides} object converter.
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
    public boolean canConvert ( final Class type )
    {
        return Sides.class.isAssignableFrom ( type );
    }

    @Override
    public Object fromString ( final String sides )
    {
        return sidesFromString ( sides );
    }

    @Override
    public String toString ( final Object object )
    {
        return sidesToString ( ( Sides ) object );
    }

    /**
     * Returns sides read from string.
     *
     * @param sides sides string
     * @return sides read from string
     */
    public static Sides sidesFromString ( final String sides )
    {
        try
        {
            if ( TextUtils.isEmpty ( sides ) )
            {
                final StringTokenizer tokenizer = new StringTokenizer ( sides, separator, false );
                if ( tokenizer.hasMoreTokens () )
                {
                    final String first = tokenizer.nextToken ().trim ();
                    if ( CompareUtils.equals ( first, TOP, LEFT, BOTTOM, RIGHT ) )
                    {
                        final boolean top = sides.contains ( TOP );
                        final boolean left = sides.contains ( LEFT );
                        final boolean bottom = sides.contains ( BOTTOM );
                        final boolean right = sides.contains ( RIGHT );
                        return new Sides ( top, left, bottom, right );
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
                                    return new Sides ( top, left, bottom, right );
                                }
                                else
                                {
                                    return new Sides ( top, left, bottom );
                                }
                            }
                            else
                            {
                                return new Sides ( top, left );
                            }
                        }
                        else
                        {
                            return new Sides ( top );
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
                                    return new Sides ( top, left, bottom, right );
                                }
                                else
                                {
                                    return new Sides ( top, left, bottom );
                                }
                            }
                            else
                            {
                                return new Sides ( top, left );
                            }
                        }
                        else
                        {
                            return new Sides ( top );
                        }
                    }
                    else
                    {
                        throw new ConversionException ( "Unknown Sides format used: " + sides );
                    }
                }
                else
                {
                    return new Sides ( false );
                }
            }
            else
            {
                return new Sides ( false );
            }
        }
        catch ( final Throwable e )
        {
            Log.get ().error ( "Unable to parse Sides: " + sides, e );
            return new Sides ();
        }
    }

    /**
     * Returns sides converted into string.
     *
     * @param sides sides to convert
     * @return sides converted into string
     */
    public static String sidesToString ( final Sides sides )
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
        return TextUtils.listToString ( list, separator );
    }
}
