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

package com.alee.extended.progress;

/**
 * This enumeration represents possible selection modes of WebStepProgress component.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebStepProgress">How to use WebStepProgress</a>
 * @see com.alee.extended.progress.WebStepProgress
 */
public enum StepSelectionMode
{
    /**
     * Steps only selection mode.
     * Allows to select steps only.
     * Used by default.
     */
    step,

    /**
     * Steps and  progress selection mode.
     * Allows to select both steps and progress.
     */
    progress
}