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

package com.alee.managers.proxy;

/**
 * System proxy detection confirm dialog support.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-ProxyManager">How to use ProxyManager</a>
 * @see com.alee.managers.proxy.ProxyManager
 */

public interface SystemProxyConfirmationSupport
{
    /**
     * Returns whether should use system proxy or not.
     *
     * @return true if should use system proxy, false otherwise
     */
    public boolean shouldUseSystemProxy ();

    /**
     * Returns whether should remember the choice or not.
     *
     * @return true if should remember the choice, false otherwise
     */
    public boolean alwaysDoTheSame ();
}