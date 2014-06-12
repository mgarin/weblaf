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

package com.alee.managers.notification;

import com.alee.extended.painter.NinePatchIconPainter;
import com.alee.extended.painter.Painter;

import java.util.EnumMap;
import java.util.Map;

/**
 * This enumeration represents available predefined notification popup styles.
 *
 * @author Mikle Garin
 * @see NotificationManager
 */

public enum NotificationStyle
{
    /**
     * WebLaF notification style.
     */
    web,

    /**
     * Mac OS X notification style.
     */
    mac;

    /**
     * Map of cached painters.
     * Painter is created only when used first time, there is no point to load it before that moment - that will be memory waste.
     */
    private static final Map<NotificationStyle, Painter> paintersCache =
            new EnumMap<NotificationStyle, Painter> ( NotificationStyle.class );

    /**
     * Returns cached painter for this notification style.
     *
     * @return cached painter for this notification style
     */
    public Painter getPainter ()
    {
        if ( paintersCache.containsKey ( this ) )
        {
            return paintersCache.get ( this );
        }
        else
        {
            Painter painter = new NinePatchIconPainter ( NotificationStyle.class.getResource ( "icons/styles/" + this + ".9.png" ) );
            paintersCache.put ( this, painter );
            return painter;
        }
    }
}