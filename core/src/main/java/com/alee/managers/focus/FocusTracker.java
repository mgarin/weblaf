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

package com.alee.managers.focus;

import java.awt.*;

/**
 * User: mgarin Date: 21.08.11 Time: 18:10
 */

public interface FocusTracker
{
    /**
     * Should tack the provided component or not (can be used to switch off focus tracking when its not needed to optimize overall
     * performance)
     */
    public boolean isTrackingEnabled ();

    /**
     * Tracked component
     */
    public Component getTrackedComponent ();

    /**
     * Should count component and its childs as a single focus owner (in case this returns true - any focus change between component and
     * any
     * of its childs will be ignored)
     */
    public boolean isUniteWithChilds ();

    /**
     * Listen to any focus change that happens, not only this component focus changes
     */
    public boolean isListenGlobalChange ();

    /**
     * This method will inform about focus changes (only actual focus changes will be fired in case "isListenGlobalChange" returns false
     * and
     * all focus changes otherwise)
     */
    public void focusChanged ( boolean focused );
}
