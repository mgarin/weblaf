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

package com.alee.managers.icon;

/**
 * Class referencing all icons used within WebLaF library UI elements.
 * These icons are only loaded into memory on demand so it is safe to reference them.
 *
 * @author Mikle Garin
 */

public final class Icons
{
    /**
     * General purpose icons.
     */
    public static final LazyIcon underline = new LazyIcon ( "underline", 16, 16 );
    public static final LazyIcon underlineHover = new LazyIcon ( "underline-hover", 16, 16 );
    public static final LazyIcon pin = new LazyIcon ( "pin", 16, 16 );
    public static final LazyIcon pinHover = new LazyIcon ( "pin-hover", 16, 16 );
    public static final LazyIcon external = new LazyIcon ( "external", 16, 16 );
    public static final LazyIcon externalHover = new LazyIcon ( "external-hover", 16, 16 );
    public static final LazyIcon maximize = new LazyIcon ( "maximize", 16, 16 );
    public static final LazyIcon maximizeHover = new LazyIcon ( "maximize-hover", 16, 16 );
    public static final LazyIcon shrink = new LazyIcon ( "shrink", 16, 16 );
    public static final LazyIcon shrinkHover = new LazyIcon ( "shrink-hover", 16, 16 );
    public static final LazyIcon cross = new LazyIcon ( "cross", 16, 16 );
    public static final LazyIcon crossHover = new LazyIcon ( "cross-hover", 16, 16 );
    public static final LazyIcon crossSmall = new LazyIcon ( "cross-small", 10, 10 );
    public static final LazyIcon crossSmallHover = new LazyIcon ( "cross-small-hover", 10, 10 );
    public static final LazyIcon magnifier = new LazyIcon ( "magnifier", 16, 16 );
    public static final LazyIcon magnifierHover = new LazyIcon ( "magnifier-hover", 16, 16 );
    public static final LazyIcon globe = new LazyIcon ( "globe", 16, 16 );
    public static final LazyIcon globeHover = new LazyIcon ( "globe-hover", 16, 16 );
    public static final LazyIcon email = new LazyIcon ( "email", 16, 16 );
    public static final LazyIcon emailHover = new LazyIcon ( "email-hover", 16, 16 );

    /**
     * Medium arrow icons.
     */
    public static final LazyIcon up = new LazyIcon ( "up", 16, 16 );
    public static final LazyIcon down = new LazyIcon ( "down", 16, 16 );
    public static final LazyIcon left = new LazyIcon ( "left", 16, 16 );
    public static final LazyIcon right = new LazyIcon ( "right", 16, 16 );

    /**
     * Small arrow icons.
     */
    public static final LazyIcon upSmall = new LazyIcon ( "up-small", 8,7 );
    public static final LazyIcon downSmall = new LazyIcon ( "down-small", 8,7 );
    public static final LazyIcon leftSmall = new LazyIcon ( "left-small", 8,7 );
    public static final LazyIcon rightSmall = new LazyIcon ( "right-small", 8,7 );

    /**
     * Large arrow icons.
     */
    public static final LazyIcon upBig = new LazyIcon ( "up-big", 16, 16 );
    public static final LazyIcon downBig = new LazyIcon ( "down-big", 16, 16 );
    public static final LazyIcon leftBig = new LazyIcon ( "left-big", 16, 16 );
    public static final LazyIcon rightBig = new LazyIcon ( "right-big", 16, 16 );

    /**
     * Tree icons.
     */
    public static final LazyIcon squarePlus = new LazyIcon ( "square-plus", 11, 11 );
    public static final LazyIcon squarePlusDisabled = new LazyIcon ( "square-plus-disabled", 11, 11 );
    public static final LazyIcon squareMinus = new LazyIcon ( "square-minus", 11, 11 );
    public static final LazyIcon squareMinusDisabled = new LazyIcon ( "square-minus-disabled", 11, 11 );
}