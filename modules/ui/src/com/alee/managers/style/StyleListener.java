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
import java.util.EventListener;

/**
 * Custom listener that can be installed into {@link Styleable} components to listen to style changes.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see StyleAdapter
 * @see Styleable
 * @see StyleManager
 */
public interface StyleListener extends EventListener
{
    /**
     * Informs about component skin change.
     *
     * @param component component which style has changed
     * @param oldSkin   previously used skin
     * @param newSkin   currently used skin
     */
    public void skinChanged ( @NotNull JComponent component, @Nullable Skin oldSkin, @NotNull Skin newSkin );

    /**
     * Informs about component style change.
     *
     * @param component  component which style has changed
     * @param oldStyleId previously used style ID
     * @param newStyleId currently used style ID
     */
    public void styleChanged ( @NotNull JComponent component, @Nullable StyleId oldStyleId, @NotNull StyleId newStyleId );

    /**
     * Informs about component skin visual update.
     * Skin update might occur when component style ID changes or its parent style component style ID changes.
     *
     * @param component component which style have been updated
     * @param styleId   component style ID
     */
    public void skinUpdated ( @NotNull JComponent component, @NotNull StyleId styleId );
}