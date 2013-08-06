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

package com.alee.utils.swing;

/**
 * Additional custom timer timing options.
 *
 * @author Mikle Garin
 * @since 1.4
 */

// todo Add those options to WebTimer
public enum WebTimerType
{
    /**
     * Fire event when current minute ends.
     */
    atMinuteEnd,

    /**
     * Fire event when current hour ends.
     */
    atHourEnd,

    /**
     * Fire event when current day ends.
     */
    atDayEnd,

    /**
     * Fire event when current month ends.
     */
    atMonthEnd,

    /**
     * Fire event when current year ends.
     */
    atYearEnd,
}