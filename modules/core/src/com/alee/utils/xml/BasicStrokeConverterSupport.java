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

import com.alee.utils.TextUtils;

import java.awt.*;
import java.util.List;

/**
 * Conversion support for {@link java.awt.BasicStroke}.
 *
 * @author Mikle Garin
 * @see java.awt.BasicStroke
 * @see com.alee.utils.xml.StrokeConverter
 */

public class BasicStrokeConverterSupport implements StrokeConverterSupport<BasicStroke>
{
    /**
     * Converter constants.
     */
    public static final String cap_butt = "butt";
    public static final String cap_round = "round";
    public static final String cap_square = "square";
    public static final String join_miter = "miter";
    public static final String join_round = "round";
    public static final String join_bevel = "bevel";

    @Override
    public String getId ()
    {
        return "basic";
    }

    @Override
    public Class<BasicStroke> getType ()
    {
        return BasicStroke.class;
    }

    @Override
    public BasicStroke fromString ( final String stroke )
    {
        final List<String> settings = TextUtils.stringToList ( stroke, StrokeConverter.separator );
        final Float width = settings.size () > 1 ? Float.valueOf ( settings.get ( 1 ) ) : 1f;
        final int cap = settings.size () > 2 ? unmarshalCap ( settings.get ( 2 ) ) : BasicStroke.CAP_SQUARE;
        final int join = settings.size () > 3 ? unmarshalJoin ( settings.get ( 3 ) ) : BasicStroke.JOIN_MITER;
        final Float miterlimit = settings.size () > 4 ? Float.valueOf ( settings.get ( 4 ) ) : 10f;
        final float[] dash = settings.size () > 5 ? unmarshalDash ( settings.get ( 5 ) ) : null;
        final Float phase = settings.size () > 6 ? Float.valueOf ( settings.get ( 6 ) ) : 0f;
        return new BasicStroke ( width, cap, join, miterlimit, dash, phase );
    }

    /**
     * Returns unmarshalled basic stroke cap.
     *
     * @param value value to unmarshal
     * @return unmarshalled basic stroke cap
     */
    private int unmarshalCap ( final String value )
    {
        if ( value.equals ( cap_butt ) )
        {
            return BasicStroke.CAP_BUTT;
        }
        else if ( value.equals ( cap_round ) )
        {
            return BasicStroke.CAP_ROUND;
        }
        else if ( value.equals ( cap_square ) )
        {
            return BasicStroke.CAP_SQUARE;
        }
        throw new IllegalArgumentException ( "Unknown stroke cap type specified: " + value );
    }

    /**
     * Returns unmarshalled basic stroke join.
     *
     * @param value value to unmarshal
     * @return unmarshalled basic stroke join
     */
    protected int unmarshalJoin ( final String value )
    {
        if ( value.equals ( join_miter ) )
        {
            return BasicStroke.JOIN_MITER;
        }
        else if ( value.equals ( join_round ) )
        {
            return BasicStroke.JOIN_ROUND;
        }
        else if ( value.equals ( join_bevel ) )
        {
            return BasicStroke.JOIN_BEVEL;
        }
        throw new IllegalArgumentException ( "Unknown stroke join type specified: " + value );
    }

    /**
     * Returns unmarshalled basic stroke dash.
     *
     * @param value value to unmarshal
     * @return unmarshalled basic stroke dash
     */
    protected float[] unmarshalDash ( final String value )
    {
        final List<Float> values = TextUtils.stringToFloatList ( value, StrokeConverter.subSeparator );
        final float[] dash = new float[ values.size () ];
        for ( int i = 0; i < values.size (); i++ )
        {
            dash[ i ] = values.get ( i );
        }
        return dash;
    }

    @Override
    public String toString ( final BasicStroke stroke )
    {
        final String s = StrokeConverter.separator;
        final String id = getId ();
        final String width = Float.toString ( stroke.getLineWidth () );
        final String cap = marshalCap ( stroke );
        final String join = marshalJoin ( stroke );
        final String miterlimit = Float.toString ( stroke.getMiterLimit () );
        final String dash = marshalDash ( stroke );
        final String phase = Float.toString ( stroke.getDashPhase () );
        return id + s + width + s + cap + s + join + s + miterlimit + s + dash + s + phase;
    }

    /**
     * Returns marshalled basic stroke cap.
     *
     * @param stroke basic stroke
     * @return marshalled basic stroke cap
     */
    protected String marshalCap ( final BasicStroke stroke )
    {
        switch ( stroke.getEndCap () )
        {
            case BasicStroke.CAP_BUTT:
                return cap_butt;

            case BasicStroke.CAP_ROUND:
                return cap_round;

            case BasicStroke.CAP_SQUARE:
                return cap_square;

            default:
                throw new IllegalArgumentException ( "Unknown stroke cap type specified: " + stroke.getEndCap () );
        }
    }

    /**
     * Returns marshalled basic stroke join.
     *
     * @param stroke basic stroke
     * @return marshalled basic stroke join
     */
    protected String marshalJoin ( final BasicStroke stroke )
    {
        switch ( stroke.getLineJoin () )
        {
            case BasicStroke.JOIN_MITER:
                return join_miter;

            case BasicStroke.JOIN_ROUND:
                return join_round;

            case BasicStroke.JOIN_BEVEL:
                return join_bevel;

            default:
                throw new IllegalArgumentException ( "Unknown stroke line join type specified: " + stroke.getLineJoin () );
        }
    }

    /**
     * Returns marshalled basic stroke dash.
     *
     * @param stroke basic stroke
     * @return marshalled basic stroke dash
     */
    protected String marshalDash ( final BasicStroke stroke )
    {
        final float[] a = stroke.getDashArray ();
        final int iMax = a.length - 1;
        final StringBuilder b = new StringBuilder ();
        for ( int i = 0; ; i++ )
        {
            b.append ( a[ i ] );
            if ( i == iMax )
            {
                return b.toString ();
            }
            b.append ( StrokeConverter.subSeparator );
        }
    }
}