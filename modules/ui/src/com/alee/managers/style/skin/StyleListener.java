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

package com.alee.managers.style.skin;

import com.alee.managers.style.StyleId;

import javax.swing.*;

/**
 * Custom listener that can be installed into {@link com.alee.managers.style.skin.Skinnable} components to listen to style changes.
 *
 * @author Mikle Garin
 * @see com.alee.managers.style.skin.Skinnable
 * @see com.alee.managers.style.Styleable
 * @see com.alee.managers.style.StyleManager
 */

public interface StyleListener
{
    /**
     * Informs about component skin change.
     *
     * @param component component which style has changed
     * @param oldSkin   previously used skin
     * @param newSkin   currently used skin
     */
    public void skinChanged ( JComponent component, Skin oldSkin, Skin newSkin );

    /**
     * Informs about component style change.
     *
     * @param component  component which style has changed
     * @param oldStyleId previously used style ID
     * @param newStyleId currently used style ID
     */
    public void styleChanged ( JComponent component, StyleId oldStyleId, StyleId newStyleId );

    /**
     * Informs about component skin visual update.
     * Skin update might occur when component style ID changes or its parent style component style ID changes.
     *
     * @param component component which style have been updated
     * @param styleId   component style ID
     */
    public void skinUpdated ( JComponent component, StyleId styleId );
}