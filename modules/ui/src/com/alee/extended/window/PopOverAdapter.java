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
 * Adapter for PopOverListener listener.
 *
 * @author Mikle Garin
 */

public abstract class PopOverAdapter implements PopOverListener
{
    /**
     * {@inheritDoc}
     *
     * @param popOver
     */
    @Override
    public void opened ( final WebPopOver popOver )
    {
        // Do nothing by default
    }

    /**
     * {@inheritDoc}
     *
     * @param popOver
     */
    @Override
    public void reopened ( final WebPopOver popOver )
    {
        // Do nothing by default
    }

    /**
     * {@inheritDoc}
     *
     * @param popOver
     */
    @Override
    public void detached ( final WebPopOver popOver )
    {
        // Do nothing by default
    }

    /**
     * {@inheritDoc}
     *
     * @param popOver
     */
    @Override
    public void closedWebPopOver ( final WebPopOver popOver )
    {
        // Do nothing by default
    }
}