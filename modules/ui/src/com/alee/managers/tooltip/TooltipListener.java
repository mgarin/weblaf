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

package com.alee.managers.tooltip;

import java.util.EventListener;

/**
 * Custom tooltips listener interface.
 *
 * @author Mikle Garin
 */
public interface TooltipListener extends EventListener
{
    /**
     * Notifies that tooltip started to appear.
     */
    public void tooltipShowing ();

    /**
     * Notifies that tooltip was fully shown.
     */
    public void tooltipShown ();

    /**
     * Notifies that tooltip was hidden.
     */
    public void tooltipHidden ();

    /**
     * Notifies that tooltip was destroyed.
     */
    public void tooltipDestroyed ();
}