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

package com.alee.laf.label;

import com.alee.extended.painter.Painter;
import com.alee.laf.StyleConstants;

import java.awt.*;

/**
 * WebLabel style class.
 *
 * @author Mikle Garin
 * @since 1.4
 */

public final class WebLabelStyle
{
    /**
     * Default label margin.
     */
    public static Insets margin = StyleConstants.margin;

    /**
     * Default label background painter.
     */
    public static Painter painter = StyleConstants.painter;

    /**
     * Default label background color.
     */
    public static Color backgroundColor = StyleConstants.backgroundColor;

    /**
     * Draw shade behind the text.
     */
    public static boolean drawShade = false;

    /**
     * Default label shade color.
     */
    public static Color shadeColor = new Color ( 200, 200, 200 );

    /**
     * Default label transparency.
     */
    public static Float transparency = null;
}