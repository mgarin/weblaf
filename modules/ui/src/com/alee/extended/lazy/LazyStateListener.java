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

package com.alee.extended.lazy;

import com.alee.api.annotations.NotNull;

import java.util.EventListener;

/**
 * {@link LazyContent} state events listener.
 *
 * @author Mikle Garin
 */
public interface LazyStateListener extends EventListener
{
    /**
     * Informs about {@link LazyContent} data {@link LazyState} change.
     *
     * @param oldState old {@link LazyState} for data
     * @param newState new {@link LazyState} for data
     */
    public void dataStateChanged ( @NotNull LazyState oldState, @NotNull LazyState newState );

    /**
     * Informs about {@link LazyContent} UI elements {@link LazyState} change.
     *
     * @param oldState old {@link LazyState} for UI elements
     * @param newState new {@link LazyState} for UI elements
     */
    public void uiStateChanged ( @NotNull LazyState oldState, @NotNull LazyState newState );
}