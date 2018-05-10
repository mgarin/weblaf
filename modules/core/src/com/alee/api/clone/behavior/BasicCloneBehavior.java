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

package com.alee.api.clone.behavior;

import com.alee.api.clone.Clone;
import com.alee.api.clone.CloneException;
import com.alee.api.clone.GlobalCloneBehavior;
import com.alee.api.clone.RecursiveClone;
import com.alee.utils.ReflectUtils;

import java.awt.*;
import java.awt.geom.*;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * {@link GlobalCloneBehavior} for various primitive types.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-Clone">How to use Clone</a>
 * @see Clone
 */
public class BasicCloneBehavior implements GlobalCloneBehavior<Object>
{
    @Override
    public boolean supports ( final RecursiveClone clone, final Object object )
    {
        return isBasic ( object );
    }

    @Override
    public Object clone ( final RecursiveClone clone, final Object object, final int depth )
    {
        if ( isSimpleMutable ( object ) )
        {
            final Class<?> clazz = object.getClass ();
            if ( clazz == Insets.class )
            {
                final Insets m = ( Insets ) object;
                return new Insets ( m.top, m.left, m.bottom, m.right );
            }
            else if ( clazz == Dimension.class )
            {
                final Dimension m = ( Dimension ) object;
                return new Dimension ( m.width, m.height );
            }
            else if ( clazz == Point.class )
            {
                final Point m = ( Point ) object;
                return new Point ( m.x, m.y );
            }
            else if ( clazz == Point2D.Float.class )
            {
                final Point2D.Float m = ( Point2D.Float ) object;
                return new Point2D.Float ( m.x, m.y );
            }
            else if ( clazz == Point2D.Double.class )
            {
                final Point2D.Double m = ( Point2D.Double ) object;
                return new Point2D.Double ( m.x, m.y );
            }
            else if ( clazz == Line2D.Float.class )
            {
                final Line2D.Float m = ( Line2D.Float ) object;
                return new Line2D.Float ( m.x1, m.y1, m.x2, m.y2 );
            }
            else if ( clazz == Line2D.Double.class )
            {
                final Line2D.Double m = ( Line2D.Double ) object;
                return new Line2D.Double ( m.x1, m.y1, m.x2, m.y2 );
            }
            else if ( clazz == Rectangle.class )
            {
                final Rectangle m = ( Rectangle ) object;
                return new Rectangle ( m.x, m.y, m.width, m.height );
            }
            else if ( clazz == Rectangle2D.Float.class )
            {
                final Rectangle2D.Float m = ( Rectangle2D.Float ) object;
                return new Rectangle2D.Float ( m.x, m.y, m.width, m.height );
            }
            else if ( clazz == Rectangle2D.Double.class )
            {
                final Rectangle2D.Double m = ( Rectangle2D.Double ) object;
                return new Rectangle2D.Double ( m.x, m.y, m.width, m.height );
            }
            else if ( clazz == Ellipse2D.Float.class )
            {
                final Ellipse2D.Float m = ( Ellipse2D.Float ) object;
                return new Ellipse2D.Float ( m.x, m.y, m.width, m.height );
            }
            else if ( clazz == Ellipse2D.Double.class )
            {
                final Ellipse2D.Double m = ( Ellipse2D.Double ) object;
                return new Ellipse2D.Double ( m.x, m.y, m.width, m.height );
            }
            else if ( clazz == AtomicBoolean.class )
            {
                final AtomicBoolean ab = ( AtomicBoolean ) object;
                return new AtomicBoolean ( ab.get () );
            }
            else if ( clazz == AtomicInteger.class )
            {
                final AtomicInteger ai = ( AtomicInteger ) object;
                return new AtomicInteger ( ai.get () );
            }
            else if ( clazz == AtomicLong.class )
            {
                final AtomicLong al = ( AtomicLong ) object;
                return new AtomicLong ( al.get () );
            }
            else
            {
                throw new CloneException ( "Unsupported mutable type clone requested: " + clazz );
            }
        }
        else
        {
            return object;
        }
    }

    @Override
    public boolean isStorable ()
    {
        return false;
    }

    /**
     * Returns whether or not specified object has basic type.
     *
     * @param object object to check
     * @return {@code true} if specified object has basic type, {@code false} otherwise
     */
    private boolean isBasic ( final Object object )
    {
        return ReflectUtils.isPrimitive ( object ) || isSimpleImmutable ( object ) || isSimpleMutable ( object );
    }

    /**
     * Returns whether or not specified object has simple immutable type.
     * todo Add "java.nio.file.Path" as one of the options
     *
     * @param object object to check
     * @return {@code true} if specified object has simple immutable type, {@code false} otherwise
     */
    private boolean isSimpleImmutable ( final Object object )
    {
        final Class<?> clazz = object.getClass ();
        return clazz.isEnum () ||
                clazz == Class.class ||
                clazz == BigInteger.class ||
                clazz == BigDecimal.class ||
                clazz == String.class ||
                clazz == Date.class ||
                clazz == Color.class ||
                clazz == Font.class ||
                clazz == BasicStroke.class ||
                clazz == File.class;
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
        return clazz == Insets.class ||
                Dimension2D.class.isAssignableFrom ( clazz ) ||
                Point2D.class.isAssignableFrom ( clazz ) ||
                Line2D.class.isAssignableFrom ( clazz ) ||
                Rectangle2D.class.isAssignableFrom ( clazz ) ||
                Ellipse2D.class.isAssignableFrom ( clazz ) ||
                clazz == AtomicBoolean.class ||
                clazz == AtomicInteger.class ||
                clazz == AtomicLong.class;
    }
}