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

package com.alee.extended.date;

import com.alee.laf.button.WebToggleButton;

import java.util.Date;

/**
 * This interface allows you to customize {@link WebCalendar}'s {@link WebToggleButton}s representing {@link Date}s.
 *
 * @author Mikle Garin
 * @see WebCalendar
 */
public interface DateCustomizer
{
    /**
     * Customizes {@link WebToggleButton} representing single {@link Date}.
     *
     * @param button {@link WebToggleButton} to customize
     * @param date   {@link Date}
     */
    public void customize ( WebToggleButton button, Date date );
}