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

/*
 * ====================================================================
 *
 * The Clearthought Software License, Version 1.0
 *
 * Copyright (c) 2001 Daniel Barbalace.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. The original software may not be altered.  However, the classes
 *    provided may be subclasses as long as the subclasses are not
 *    packaged in the info.clearthought package or any subpackage of
 *    info.clearthought.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR, AFFILATED BUSINESSES,
 * OR ANYONE ELSE BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 */

package com.alee.extended.layout;

/**
 * TableLayoutConstants define the constants used by all the TableLayout classes.
 *
 * @author Daniel E. Barbalace
 * @version 3.0 February 15, 2004
 */
public interface TableLayoutConstants
{
    /**
     * Indicates that the component is left justified in its cell
     */
    public static final int LEFT = 0;

    /**
     * Indicates that the component is top justified in its cell
     */
    public static final int TOP = 0;

    /**
     * Indicates that the component is centered in its cell
     */
    public static final int CENTER = 1;

    /**
     * Indicates that the component is full justified in its cell
     */
    public static final int FULL = 2;

    /**
     * Indicates that the component is bottom justified in its cell
     */
    public static final int BOTTOM = 3;

    /**
     * Indicates that the component is right justified in its cell
     */
    public static final int RIGHT = 3;

    /**
     * Indicates that the component is leading justified in its cell. Leading justification means
     * components are left justified if their container is left-oriented and right justified if
     * their container is right-oriented. Trailing justification is opposite. see
     * java.awt.Component#getComponentOrientation
     */
    public static final int LEADING = 4;

    /**
     * Indicates that the component is trailing justified in its cell. Trailing justification means
     * components are right justified if their container is left-oriented and left justified if
     * their container is right-oriented. Leading justification is opposite. see
     * java.awt.Component#getComponentOrientation
     */
    public static final int TRAILING = 5;

    /**
     * Indicates that the row/column should fill the available space
     */
    public static final double FILL = -1.0;

    /**
     * Indicates that the row/column should be allocated just enough space to accomidate the
     * preferred size of all components contained completely within this row/column.
     */
    public static final double PREFERRED = -2.0;

    /**
     * Indicates that the row/column should be allocated just enough space to accomidate the minimum
     * size of all components contained completely within this row/column.
     */
    public static final double MINIMUM = -3.0;
}