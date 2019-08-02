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

import javax.swing.*;
import java.awt.*;

import com.alee.managers.focus.FocusManager;

/**
 * Implementations of this interface can be used to track {@link JComponent} and its children focus state.
 * There is also a {@link DefaultFocusTracker} implementation with all basic methods and which also contains a few additional features.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-FocusManager">How to use FocusManager</a>
 * @see DefaultFocusTracker
 * @see FocusManager
 */
public interface FocusTracker
{
    /**
     * Returns whether or not tracking is currently enabled.
     *
     * @return {@code true} if tracking is currently enabled, {@code false} otherwise
     */
    public boolean isEnabled ();

    /**
     * Sets whether or not tracking is currently enabled.
     *
     * @param enabled whether or not tracking is currently enabled
     */
    public void setEnabled ( boolean enabled );

    /**
     * Returns whether or not tracked component is currently focused according to this tracker settings.
     *
     * @return {@code true} if tracked component is currently focused according to this tracker settings, {@code false} otherwise
     */
    public boolean isFocused ();

    /**
     * Sets tracked component focused state.
     *
     * @param focused component focused state
     */
    public void setFocused ( boolean focused );

    /**
     * Returns whether specified {@link Component} is involved with this tracked {@link JComponent} or not.
     * It basically says whether or not specified {@link Component} counts towards tracked {@link JComponent} focus changes.
     *
     * @param tracked   tracked {@link JComponent}
     * @param component {@link Component} to check for involvement
     * @return {@code true} if specified {@link Component} is involved with this tracked {@link JComponent}, {@code false} otherwise
     */
    public boolean isInvolved ( JComponent tracked, Component component );

    /**
     * Informs about tracked {@link JComponent} focus changes depending on tracker settings.
     *
     * @param focused whether tracked {@link JComponent} is focused or not
     */
    public void focusChanged ( boolean focused );
}