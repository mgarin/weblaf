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

package com.alee.api.merge.behavior;

import com.alee.api.merge.Merge;
import com.alee.api.merge.MergeBehavior;
import com.alee.api.merge.MergeException;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Date;

/**
 * Merge behavior for various primitive types.
 *
 * @author Mikle Garin
 */

public final class BasicMergeBehavior implements MergeBehavior
{
    @Override
    public boolean supports ( final Merge merge, final Object object, final Object merged )
    {
        return isBasic ( object ) || isBasic ( merged );
    }

    @Override
    public <T> T merge ( final Merge merge, final Object object, final Object merged )
    {
        if ( isSimpleMutable ( merged ) )
        {
            final Class<?> clazz = merged.getClass ();
            if ( clazz == Insets.class )
            {
                final Insets m = ( Insets ) merged;
                return ( T ) new Insets ( m.top, m.left, m.bottom, m.right );
            }
            else if ( clazz == Dimension.class )
            {
                final Dimension m = ( Dimension ) merged;
                return ( T ) new Dimension ( m.width, m.height );
            }
            else if ( clazz == Point.class )
            {
                final Point m = ( Point ) merged;
                return ( T ) new Point ( m.x, m.y );
            }
            else if ( clazz == Point2D.Float.class )
            {
                final Point2D.Float m = ( Point2D.Float ) merged;
                return ( T ) new Point2D.Float ( m.x, m.y );
            }
            else if ( clazz == Point2D.Double.class )
            {
                final Point2D.Double m = ( Point2D.Double ) merged;
                return ( T ) new Point2D.Double ( m.x, m.y );
            }
            else if ( clazz == Rectangle.class )
            {
                final Rectangle m = ( Rectangle ) merged;
                return ( T ) new Rectangle ( m.x, m.y, m.width, m.height );
            }
            else if ( clazz == Rectangle2D.Float.class )
            {
                final Rectangle2D.Float m = ( Rectangle2D.Float ) merged;
                return ( T ) new Rectangle2D.Float ( m.x, m.y, m.width, m.height );
            }
            else if ( clazz == Rectangle2D.Double.class )
            {
                final Rectangle2D.Double m = ( Rectangle2D.Double ) merged;
                return ( T ) new Rectangle2D.Double ( m.x, m.y, m.width, m.height );
            }
            else
            {
                throw new MergeException ( "Unsupported mutable type merge requested: " + clazz );
            }
        }
        else
        {
            return ( T ) merged;
        }
    }

    /**
     * Returns whether or not specified object has basic type.
     *
     * @param object object to check
     * @return {@code true} if specified object has basic type, {@code false} otherwise
     */
    private boolean isBasic ( final Object object )
    {
        return isPrimitive ( object ) || isSimpleImmutable ( object ) || isSimpleMutable ( object );
    }

    /**
     * Returns whether or not specified object has primitive type.
     *
     * @param object object to check
     * @return {@code true} if specified object has primitive type, {@code false} otherwise
     */
    private boolean isPrimitive ( final Object object )
    {
        final Class<?> clazz = object.getClass ();
        return clazz.isPrimitive () ||
                Boolean.class.isAssignableFrom ( clazz ) ||
                Integer.class.isAssignableFrom ( clazz ) ||
                Character.class.isAssignableFrom ( clazz ) ||
                Byte.class.isAssignableFrom ( clazz ) ||
                Short.class.isAssignableFrom ( clazz ) ||
                Long.class.isAssignableFrom ( clazz ) ||
                Float.class.isAssignableFrom ( clazz ) ||
                Double.class.isAssignableFrom ( clazz );
    }

    /**
     * Returns whether or not specified object has simple immutable type.
     *
     * @param object object to check
     * @return {@code true} if specified object has simple immutable type, {@code false} otherwise
     */
    private boolean isSimpleImmutable ( final Object object )
    {
        final Class<?> clazz = object.getClass ();
        return clazz == Class.class || clazz == String.class || clazz == Date.class || clazz == Color.class || clazz == Font.class;
    }

    /**
     * Returns whether or not specified object has simple mutable type.
     *
     * @param object object to check
     * @return {@code true} if specified object has simple mutable type, {@code false} otherwise
     */
    private boolean isSimpleMutable ( final Object object )
    {
        final Class<?> clazz = object.getClass ();
        return clazz == Insets.class || clazz == Dimension.class ||
                Point2D.class.isAssignableFrom ( clazz ) || Rectangle2D.class.isAssignableFrom ( clazz );
    }
}