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

import com.alee.laf.WebLookAndFeel;
import com.alee.managers.animation.AnimationManager;
import com.alee.managers.drag.DragManager;
import com.alee.managers.focus.FocusManager;
import com.alee.managers.hotkey.HotkeyManager;
import com.alee.managers.icon.IconManager;
import com.alee.managers.language.UILanguageManager;
import com.alee.managers.proxy.UIProxyManager;
import com.alee.managers.settings.UISettingsManager;
import com.alee.managers.style.StyleManager;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.managers.version.VersionManager;
import com.alee.utils.XmlUtils;

/**
 * WebLaF managers simple initialization class.
 * Used by WebLookAndFeel to initialize managers together with or separately from the LaF.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebLaF">How to use WebLaF</a>
 * @see VersionManager
 * @see UILanguageManager
 * @see UISettingsManager
 * @see HotkeyManager
 * @see FocusManager
 * @see TooltipManager
 * @see IconManager
 * @see StyleManager
 * @see AnimationManager
 * @see UIProxyManager
 * @see DragManager
 */
public final class UIManagers
{
    /**
     * Initializes LaF UI managers.
     * This method should be performed on EDT like other Swing UI operations.
     * Initialization order is important and any changes should be performed with care.
     */
    public static synchronized void initialize ()
    {
        // Ensuring that operation is performed on EDT
        WebLookAndFeel.checkEventDispatchThread ();

        // Initializing managers
        XmlUtils.getXStream ();
        VersionManager.initialize ();
        UILanguageManager.initialize ();
        UISettingsManager.initialize ();
        HotkeyManager.initialize ();
        FocusManager.initialize ();
        TooltipManager.initialize ();
        IconManager.initialize ();
        StyleManager.initialize ();
        AnimationManager.initialize ();
        UIProxyManager.initialize ();
        DragManager.initialize ();
    }
}