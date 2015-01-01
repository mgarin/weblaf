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

package com.alee.managers.popup;

import com.alee.extended.painter.NinePatchIconPainter;
import com.alee.extended.painter.Painter;

import java.util.HashMap;
import java.util.Map;

/**
 * This enumeration represents predefined available popup styles.
 *
 * @author Mikle Garin
 * @see com.alee.managers.popup.PopupManager
 * @see com.alee.managers.popup.WebPopup
 */

public enum PopupStyle
{
    /**
     * Undecorated popup.
     */
    none,

    /**
     * Simple bordered popup.
     */
    bordered,

    /**
     * Light-colored popup.
     */
    light,

    /**
     * Light-colored popup with small border.
     */
    lightSmall,

    /**
     * Green-colored popup with large border.
     */
    greenLarge,

    /**
     * Popup with bevel border.
     */
    bevel,

    /**
     * Gray-colored popup.
     */
    gray,

    /**
     * Gray-colored popup with small border.
     */
    graySmall,

    /**
     * Gray-colored popup with etched border.
     */
    grayEtched,

    /**
     * Gray-colored tooltip-styled popup.
     */
    grayDownTip,

    /**
     * Dark-colored popup.
     */
    dark;

    /**
     * Style painters cache.
     */
    private static final Map<PopupStyle, Painter> stylePainters = new HashMap<PopupStyle, Painter> ();

    /**
     * Returns popup painter for this popup style.
     *
     * @return popup painter for this popup style
     */
    public synchronized Painter getPainter ()
    {
        Painter painter = stylePainters.get ( this );
        if ( painter == null )
        {
            if ( this == PopupStyle.none )
            {
                painter = null;
            }
            else
            {
                painter = new NinePatchIconPainter ( PopupManager.class.getResource ( "icons/popup/" + this + ".9.png" ) );
            }
            stylePainters.put ( this, painter );
        }
        return painter;
    }
}