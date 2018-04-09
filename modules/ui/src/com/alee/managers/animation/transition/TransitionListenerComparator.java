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

package com.alee.managers.animation.transition;

import java.util.Comparator;

/**
 * Simple comparator for {@link java.util.concurrent.ConcurrentSkipListSet} support within {@link AbstractTransition}.
 *
 * @param <V> transition value type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-AnimationManager">How to use AnimationManager</a>
 * @see com.alee.managers.animation.AnimationManager
 */
public final class TransitionListenerComparator<V> implements Comparator<TransitionListener<V>>
{
    @Override
    public int compare ( final TransitionListener<V> listener1, final TransitionListener<V> listener2 )
    {
        return listener1 == listener2 ? 0 : -1;
    }
}