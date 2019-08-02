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

package com.alee.utils.laf;

import javax.swing.plaf.BorderUIResource;
import java.awt.*;

/**
 * Special border used by WebLaF to provide proper component content sides spacing.
 * It usually replaces default/user component border but you can provide a special client property that will disable that behavior.
 *
 * It is not recommended to use this border with WebLaF components as it is a {@link javax.swing.plaf.UIResource} by default and will be
 * replaced by any {@link com.alee.laf.WebUI} implementation as soon as any border changes are made.
 *
 * @author Mikle Garin
 * @see com.alee.laf.WebLookAndFeel#PROPERTY_HONOR_USER_BORDER
 * @see com.alee.laf.WebLookAndFeel#PROPERTY_HONOR_USER_BORDERS
 */
public final class WebBorder extends BorderUIResource.EmptyBorderUIResource
{
    /**
     * Constructs new empty border with the specified border width at each side.
     *
     * @param border border width
     */
    public WebBorder ( final int border )
    {
        super ( border, border, border, border );
    }

    /**
     * Constructs new empty border with the specified border widths at each side.
     *
     * @param top    top border width
     * @param left   left border width
     * @param bottom bottom border width
     * @param right  right border width
     */
    public WebBorder ( final int top, final int left, final int bottom, final int right )
    {
        super ( top, left, bottom, right );
    }

    /**
     * Constructs new empty border with the specified insets.
     *
     * @param borderInsets border insets
     */
    public WebBorder ( final Insets borderInsets )
    {
        super ( borderInsets );
    }
}