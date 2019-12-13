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

package com.alee.extended.memorybar;

import com.alee.managers.style.StyleId;

/**
 * Basic descriptor for {@link WebMemoryBar} component.
 * For creating custom {@link WebMemoryBar} descriptor {@link AbstractMemoryBarDescriptor} class can be extended.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see com.alee.managers.style.StyleManager
 * @see com.alee.managers.style.StyleManager#registerComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 * @see com.alee.managers.style.StyleManager#unregisterComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 */
public final class MemoryBarDescriptor extends AbstractMemoryBarDescriptor<WebMemoryBar, WMemoryBarUI, IMemoryBarPainter>
{
    /**
     * Constructs new {@link MemoryBarDescriptor}.
     */
    public MemoryBarDescriptor ()
    {
        super (
                "memorybar",
                WebMemoryBar.class,
                "MemoryBarUI",
                WMemoryBarUI.class,
                WebMemoryBarUI.class,
                IMemoryBarPainter.class,
                MemoryBarPainter.class,
                AdaptiveMemoryBarPainter.class,
                StyleId.memorybar
        );
    }
}