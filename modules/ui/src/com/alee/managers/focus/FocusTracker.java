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
 * Advanced interface that allows you to track focus behavior within component and its children.
 * Note that method names are made longer to avoid clashing with other component methods in case you implement tracker interface.
 *
 * @author Mikle Garin
 */

public interface FocusTracker
{
    /**
     * Returns whether tracking is currently enabled or not.
     *
     * @return true if tracking is currently enabled, false otherwise
     */
    public boolean isTrackingEnabled ();

    /**
     * Returns whether specified component is involved with this tracker or not.
     * This basically asks whether this object counts towards this tracker focus changes or not.
     *
     * @param component component to check for involvement
     * @param tracked   tracked component
     * @return true if the specified component is involved with this tracker, false otherwise
     */
    public boolean isInvolved ( Component component, Component tracked );

    /**
     * Informs about component(s) focus changes depending on tracker settings.
     *
     * @param focused whether tracked component(s) is focused or not
     */
    public void focusChanged ( boolean focused );
}