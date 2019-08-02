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

package com.alee.managers.style;

import com.alee.utils.LafUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * Common implementations for {@link MarginMethods} interface methods.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see MarginMethods
 * @see StyleManager
 */
public final class MarginMethodsImpl
{
    /**
     * Returns current margin.
     * Might return {@code null} which is basically the same as an empty [0,0,0,0] margin.
     *
     * @param component component to retrieve margin from
     * @return current margin
     */
    public static Insets getMargin ( final JComponent component )
    {
        final MarginSupport support = getMarginSupportUI ( component );
        return support != null ? support.getMargin () : MarginSupport.EMPTY;
    }

    /**
     * Sets new margin.
     *
     * @param component component to set margin for
     * @param margin   new margin
     */
    public static void setMargin ( final JComponent component, final int margin )
    {
        setMargin ( component, margin, margin, margin, margin );
    }

    /**
     * Sets new margin.
     *
     * @param component component to set margin for
     * @param top       new top margin
     * @param left      new left margin
     * @param bottom    new bottom margin
     * @param right     new right margin
     */
    public static void setMargin ( final JComponent component, final int top, final int left, final int bottom, final int right )
    {
        setMargin ( component, new Insets ( top, left, bottom, right ) );
    }

    /**
     * Sets new margin.
     * {@code null} can be provided to set an empty [0,0,0,0] margin.
     *
     * @param component component to set margin for
     * @param margin   new margin
     */
    public static void setMargin ( final JComponent component, final Insets margin )
    {
        final MarginSupport support = getMarginSupportUI ( component );
        if ( support != null )
        {
            support.setMargin ( margin );
        }
    }

    /**
     * Returns UI with margin support.
     *
     * @param component component to retrieve UI from
     * @return UI with margin support
     */
    private static MarginSupport getMarginSupportUI ( final JComponent component )
    {
        final ComponentUI ui = LafUtils.getUI ( component );
        return ui != null && ui instanceof MarginSupport ? ( MarginSupport ) ui : null;
    }
}