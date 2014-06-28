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
 * Custom Insets class converter.
 *
 * @author Mikle Garin
 */

public class InsetsConverter extends AbstractSingleValueConverter
{
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canConvert ( final Class type )
    {
        return type.equals ( Insets.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object fromString ( final String insets )
    {
        try
        {
            final StringTokenizer tokenizer = new StringTokenizer ( insets, ",", false );
            final int top = Integer.parseInt ( tokenizer.nextToken ().trim () );
            final int left = Integer.parseInt ( tokenizer.nextToken ().trim () );
            final int bottom = Integer.parseInt ( tokenizer.nextToken ().trim () );
            final int right = Integer.parseInt ( tokenizer.nextToken ().trim () );
            return new Insets ( top, left, bottom, right );
        }
        catch ( final Throwable e )
        {
            try
            {
                final int spacing = Integer.parseInt ( insets );
                return new Insets ( spacing, spacing, spacing, spacing );
            }
            catch ( final Throwable ex )
            {
                return new Insets ( 0, 0, 0, 0 );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString ( final Object object )
    {
        final Insets insets = ( Insets ) object;
        return insets.top + "," + insets.left + "," + insets.bottom + "," + insets.right;
    }
}