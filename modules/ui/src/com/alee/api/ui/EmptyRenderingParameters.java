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

package com.alee.api.ui;

import com.alee.laf.WebLookAndFeel;

/**
 * {@link RenderingParameters} implementation for cases when parameters are irrelevant.
 *
 * @author Mikle Garin
 */
public final class EmptyRenderingParameters implements RenderingParameters
{
    /**
     * Single {@link EmptyRenderingParameters} instance.
     */
    private static EmptyRenderingParameters instance;

    /**
     * Returns single {@link EmptyRenderingParameters} instance.
     * It is necessary to call this on Event Dispatch Thread to avoid possible issues.
     *
     * @return single {@link EmptyRenderingParameters} instance
     */
    public static EmptyRenderingParameters instance ()
    {
        WebLookAndFeel.checkEventDispatchThread ();
        if ( instance == null )
        {
            instance = new EmptyRenderingParameters ();
        }
        return instance;
    }

    /**
     * Constructs new {@link EmptyRenderingParameters}.
     * Made private to avoid extra instances creation.
     */
    private EmptyRenderingParameters ()
    {
        super ();
    }
}