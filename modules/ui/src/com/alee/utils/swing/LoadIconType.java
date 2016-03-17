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

package com.alee.utils.swing;

import com.alee.api.IconSupport;

import javax.swing.*;

/**
 * This enumeration represents various load icons.
 *
 * @author Mikle Garin
 */

public enum LoadIconType implements IconSupport
{
    /**
     * Circle loader.
     */
    circe,

    /**
     * Facebook loader.
     */
    facebook,

    /**
     * Roller loader.
     */
    roller,

    /**
     * Arrows loader.
     */
    arrows,

    /**
     * Indicator loader.
     */
    indicator,

    /**
     * Snake loader.
     */
    snake,

    /**
     * Empty loader.
     */
    none;

    /**
     * Cached load icon.
     */
    private Icon icon;

    @Override
    public Icon getIcon ()
    {
        if ( icon == null )
        {
            icon = new ImageIcon ( LoadIconType.class.getResource ( "icons/load/" + this + ".gif" ) );
        }
        return icon;
    }
}