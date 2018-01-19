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

import com.alee.managers.language.LanguageManager;
import com.alee.managers.proxy.ProxyManager;
import com.alee.managers.settings.SettingsManager;

/**
 * Core managers simple initialization class.
 * This might be useful in case you are using WebLaF core separately.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebLaF">How to use WebLaF</a>
 * @see com.alee.managers.language.LanguageManager
 * @see com.alee.managers.settings.SettingsManager
 * @see com.alee.managers.proxy.ProxyManager
 */

public final class CoreManagers
{
    /**
     * Initializes LaF core managers.
     * This method can be performed outside of EDT.
     * Initialization order is important and any changes should be performed with care.
     */
    public static synchronized void initialize ()
    {
        LanguageManager.initialize ();
        SettingsManager.initialize ();
        ProxyManager.initialize ();
    }
}