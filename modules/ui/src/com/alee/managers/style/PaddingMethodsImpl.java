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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.utils.LafUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * Common implementations for {@link PaddingMethods} interface methods.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see PaddingMethods
 * @see StyleManager
 */
public final class PaddingMethodsImpl
{
    /**
     * Returns current padding.
     * Might return {@code null} which is basically the same as an empty [0,0,0,0] padding.
     *
     * @param component component to retrieve padding from
     * @return current padding
     */
    @Nullable
    public static Insets getPadding ( @NotNull final JComponent component )
    {
        final PaddingSupport support = getPaddingSupportUI ( component );
        return support != null ? support.getPadding () : PaddingSupport.EMPTY;
    }

    /**
     * Sets new padding.
     *
     * @param component component to set padding for
     * @param padding   new padding
     */
    public static void setPadding ( @NotNull final JComponent component, final int padding )
    {
        setPadding ( component, padding, padding, padding, padding );
    }

    /**
     * Sets new padding.
     *
     * @param component component to set padding for
     * @param top       new top padding
     * @param left      new left padding
     * @param bottom    new bottom padding
     * @param right     new right padding
     */
    public static void setPadding ( @NotNull final JComponent component, final int top, final int left, final int bottom, final int right )
    {
        setPadding ( component, new Insets ( top, left, bottom, right ) );
    }

    /**
     * Sets new padding.
     * {@code null} can be provided to set an empty [0,0,0,0] padding.
     *
     * @param component component to set padding for
     * @param padding   new padding
     */
    public static void setPadding ( @NotNull final JComponent component, @Nullable final Insets padding )
    {
        final PaddingSupport support = getPaddingSupportUI ( component );
        if ( support != null )
        {
            support.setPadding ( padding );
        }
    }

    /**
     * Returns UI with padding support.
     *
     * @param component component to retrieve UI from
     * @return UI with padding support
     */
    @Nullable
    private static PaddingSupport getPaddingSupportUI ( @NotNull final JComponent component )
    {
        final ComponentUI ui = LafUtils.getUI ( component );
        return ui != null && ui instanceof PaddingSupport ? ( PaddingSupport ) ui : null;
    }
}