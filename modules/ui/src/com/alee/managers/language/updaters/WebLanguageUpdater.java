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

package com.alee.managers.language.updaters;

import com.alee.managers.hotkey.HotkeyInfo;

import java.awt.*;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * This class prvides an additional set of methods to simplify language updaters usage.
 * Most of default predefined WebLaF language updaters extend this class.
 *
 * @author Mikle Garin
 */

public abstract class WebLanguageUpdater<E extends Component> extends DefaultLanguageUpdater<E>
{
    /**
     * Hotkeys cache map.
     */
    private static final Map<Component, HotkeyInfo> hotkeysCache = new WeakHashMap<Component, HotkeyInfo> ();

    /**
     * Caches component's hotkey
     *
     * @param component  component
     * @param hotkeyInfo hotkey data
     */
    protected static void cacheHotkey ( final Component component, final HotkeyInfo hotkeyInfo )
    {
        hotkeysCache.put ( component, hotkeyInfo );
    }

    /**
     * Returns wether hotkey is cached or not.
     *
     * @param component hotkey's component
     * @return true if hotkey is cached, false otherwise
     */
    protected static boolean isHotkeyCached ( final Component component )
    {
        return hotkeysCache.containsKey ( component );
    }

    /**
     * Returns cached hotkey data.
     *
     * @param component hotkey's component
     * @return cached hotkey data
     */
    protected static HotkeyInfo getCachedHotkey ( final Component component )
    {
        return hotkeysCache.get ( component );
    }
}