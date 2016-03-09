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

package com.alee.managers;

import com.alee.managers.drag.DragManager;
import com.alee.managers.focus.FocusManager;
import com.alee.managers.hotkey.HotkeyManager;
import com.alee.managers.icon.IconManager;
import com.alee.managers.language.WebLanguageManager;
import com.alee.managers.log.Log;
import com.alee.managers.proxy.WebProxyManager;
import com.alee.managers.settings.WebSettingsManager;
import com.alee.managers.style.StyleManager;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.managers.version.VersionManager;

/**
 * WebLaF managers simple initialization class.
 * Used by WebLookAndFeel to initialize managers together with or separately from the L&amp;F.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebLaF">How to use WebLaF</a>
 * @see com.alee.managers.log.Log
 * @see com.alee.managers.version.VersionManager
 * @see com.alee.managers.language.WebLanguageManager
 * @see com.alee.managers.settings.WebSettingsManager
 * @see com.alee.managers.hotkey.HotkeyManager
 * @see com.alee.managers.focus.FocusManager
 * @see com.alee.managers.tooltip.TooltipManager
 * @see com.alee.managers.style.StyleManager
 * @see com.alee.managers.proxy.WebProxyManager
 * @see com.alee.managers.drag.DragManager
 */

public class UIManagers
{
    /**
     * Initializes WebLaF managers.
     * Managers initialization order does matter.
     */
    public static synchronized void initialize ()
    {
        Log.initialize ();
        VersionManager.initialize ();
        WebLanguageManager.initialize ();
        WebSettingsManager.initialize ();
        HotkeyManager.initialize ();
        FocusManager.initialize ();
        TooltipManager.initialize ();
        IconManager.initialize ();
        StyleManager.initialize ();
        WebProxyManager.initialize ();
        DragManager.initialize ();
    }
}