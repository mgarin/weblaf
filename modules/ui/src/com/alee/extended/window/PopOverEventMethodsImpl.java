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

/**
 * Common implementations for {@link com.alee.extended.window.PopOverEventMethods} interface methods.
 *
 * @author Mikle Garin
 * @see com.alee.extended.window.PopOverEventMethods
 */
public final class PopOverEventMethodsImpl
{
    /**
     * Shortcut method for popover open event.
     *
     * @param popOver  popover to handle events for
     * @param runnable popover event runnable
     * @return used popover adapter
     */
    public static PopOverAdapter onOpen ( final WebPopOver popOver, final PopOverEventRunnable runnable )
    {
        final PopOverAdapter popOverAdapter = new PopOverAdapter ()
        {
            @Override
            public void opened ( final WebPopOver popOver )
            {
                runnable.run ( popOver );
            }
        };
        popOver.addPopOverListener ( popOverAdapter );
        return popOverAdapter;
    }

    /**
     * Shortcut method for popover reopen event.
     *
     * @param popOver  popover to handle events for
     * @param runnable popover event runnable
     * @return used popover adapter
     */
    public static PopOverAdapter onReopen ( final WebPopOver popOver, final PopOverEventRunnable runnable )
    {
        final PopOverAdapter popOverAdapter = new PopOverAdapter ()
        {
            @Override
            public void reopened ( final WebPopOver popOver )
            {
                runnable.run ( popOver );
            }
        };
        popOver.addPopOverListener ( popOverAdapter );
        return popOverAdapter;
    }

    /**
     * Shortcut method for popover detach event.
     *
     * @param popOver  popover to handle events for
     * @param runnable popover event runnable
     * @return used popover adapter
     */
    public static PopOverAdapter onDetach ( final WebPopOver popOver, final PopOverEventRunnable runnable )
    {
        final PopOverAdapter popOverAdapter = new PopOverAdapter ()
        {
            @Override
            public void detached ( final WebPopOver popOver )
            {
                runnable.run ( popOver );
            }
        };
        popOver.addPopOverListener ( popOverAdapter );
        return popOverAdapter;
    }

    /**
     * Shortcut method for popover close event.
     *
     * @param popOver  popover to handle events for
     * @param runnable popover event runnable
     * @return used popover adapter
     */
    public static PopOverAdapter onClose ( final WebPopOver popOver, final PopOverEventRunnable runnable )
    {
        final PopOverAdapter popOverAdapter = new PopOverAdapter ()
        {
            @Override
            public void closed ( final WebPopOver popOver )
            {
                runnable.run ( popOver );
            }
        };
        popOver.addPopOverListener ( popOverAdapter );
        return popOverAdapter;
    }
}