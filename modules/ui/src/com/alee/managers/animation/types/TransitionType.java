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

package com.alee.managers.animation.types;

import com.alee.managers.animation.easing.Easing;

/**
 * Base interface for transition support of specific value types.
 *
 * @param <V> transition value type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-AnimationManager">How to use AnimationManager</a>
 * @see com.alee.managers.animation.AnimationManager
 */
public interface TransitionType<V>
{
    /**
     * Returns provided value adjusted according to transition progress.
     *
     * @param easing   easing algorithm
     * @param start    initial value
     * @param goal     goal value
     * @param progress current transition progress, should always be in [0..total] range
     * @param total    maximum transition progress
     * @return provided value adjusted according to transition progress
     */
    public V value ( Easing easing, V start, V goal, double progress, double total );
}