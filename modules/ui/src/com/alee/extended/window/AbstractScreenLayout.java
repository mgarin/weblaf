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

package com.alee.extended.window;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Abstract {@link ScreenLayout} that implements addition and removal of {@link Window}s.
 *
 * @param <W> {@link Window} type
 * @param <C> constraints type
 * @author Mikle Garin
 */
public abstract class AbstractScreenLayout<W extends Window, C> implements ScreenLayout<W, C>
{
    /**
     * Windows added into this screen layout.
     */
    protected final List<W> windows = new ArrayList<W> ();

    /**
     * Windows constraints.
     */
    protected final Map<W, C> constraints = new WeakHashMap<W, C> ();

    /**
     * Constraints lock.
     */
    protected final Object lock = new Object ();

    /**
     * Called when window added into this layout.
     *
     * @param window added window
     */
    public void addWindow ( final W window )
    {
        addWindow ( window, null );
    }

    @Override
    public void addWindow ( final W window, final C constraints )
    {
        synchronized ( lock )
        {
            this.windows.add ( window );
            this.constraints.put ( window, constraints );
            layoutScreen ();
        }
    }

    @Override
    public void removeWindow ( final W window )
    {
        synchronized ( lock )
        {
            this.windows.remove ( window );
            this.constraints.remove ( window );
            layoutScreen ();
        }
    }
}