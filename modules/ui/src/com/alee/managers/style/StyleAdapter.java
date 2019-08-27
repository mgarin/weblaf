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

import javax.swing.*;

/**
 * Adapter for {@link StyleListener}.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see StyleListener
 * @see Styleable
 * @see StyleManager
 */
public abstract class StyleAdapter implements StyleListener
{
    @Override
    public void skinChanged ( @NotNull final JComponent component, @Nullable final Skin oldSkin, @NotNull final Skin newSkin )
    {
        /**
         * Do nothing by default.
         */
    }

    @Override
    public void styleChanged ( @NotNull final JComponent component, @Nullable final StyleId oldStyleId, @NotNull final StyleId newStyleId )
    {
        /**
         * Do nothing by default.
         */
    }

    @Override
    public void skinUpdated ( @NotNull final JComponent component, @NotNull final StyleId styleId )
    {
        /**
         * Do nothing by default.
         */
    }
}