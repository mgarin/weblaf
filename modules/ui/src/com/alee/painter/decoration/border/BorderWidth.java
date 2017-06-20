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

package com.alee.painter.decoration.border;

import com.alee.api.clone.Clone;
import com.alee.api.merge.MergeBehavior;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import java.io.Serializable;

/**
 * Border widths data.
 *
 * @author Mikle Garin
 */

@XStreamConverter ( BorderWidthConverter.class )
public final class BorderWidth implements MergeBehavior<BorderWidth>, Cloneable, Serializable
{
    /**
     * Empty border width constant.
     */
    public static final BorderWidth EMPTY = new BorderWidth ( 0, 0, 0, 0 );

    /**
     * Top border width.
     */
    public int top;

    /**
     * Right border width.
     */
    public int right;

    /**
     * Bottom border width.
     */
    public int bottom;

    /**
     * Left border width.
     */
    public int left;

    /**
     * Constructs border widths data.
     */
    public BorderWidth ()
    {
        super ();
        this.top = 0;
        this.right = 0;
        this.bottom = 0;
        this.left = 0;
    }

    /**
     * Constructs border widths data with the specified values.
     *
     * @param top    top border width
     * @param right  right border width
     * @param bottom bottom border width
     * @param left   left border width
     */
    public BorderWidth ( final int top, final int right, final int bottom, final int left )
    {
        super ();
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    /**
     * Returns whether or not border is empty.
     *
     * @return true if border is empty, false otherwise
     */
    public boolean isEmpty ()
    {
        return top == 0 && right == 0 && bottom == 0 && left == 0;
    }

    @Override
    public String toString ()
    {
        return BorderWidthConverter.borderWidthToString ( this );
    }

    @Override
    public BorderWidth merge ( final BorderWidth merged )
    {
        if ( merged.top != -1 )
        {
            top = merged.top;
        }
        if ( merged.right != -1 )
        {
            right = merged.right;
        }
        if ( merged.bottom != -1 )
        {
            bottom = merged.bottom;
        }
        if ( merged.left != -1 )
        {
            left = merged.left;
        }
        return this;
    }

    @Override
    public BorderWidth clone ()
    {
        return Clone.cloneByFieldsSafely ( this );
    }
}