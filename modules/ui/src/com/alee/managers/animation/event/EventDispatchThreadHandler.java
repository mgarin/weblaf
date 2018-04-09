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

package com.alee.managers.animation.event;

import com.alee.utils.CoreSwingUtils;

/**
 * Event handler that sends tasks into Swing Event Dispatch Thread.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-AnimationManager">How to use AnimationManager</a>
 * @see com.alee.managers.animation.AnimationManager
 */
public final class EventDispatchThreadHandler implements EventHandler
{
    /**
     * Globally available handler instance.
     * Useful since there might be no point in spawning additional instances.
     */
    private static EventDispatchThreadHandler instance;

    /**
     * Returns globally available handler instance.
     *
     * @return globally available handler instance
     */
    public static EventDispatchThreadHandler get ()
    {
        if ( instance == null )
        {
            synchronized ( EventDispatchThreadHandler.class )
            {
                if ( instance == null )
                {
                    instance = new EventDispatchThreadHandler ();
                }
            }
        }
        return instance;
    }

    @Override
    public void handle ( final Runnable event )
    {
        CoreSwingUtils.invokeLater ( event );
    }
}