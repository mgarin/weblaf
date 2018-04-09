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

package com.alee.managers.animation.pipeline;

import com.alee.managers.animation.transition.Transition;

/**
 * Pipeline for simultaneous playback of multiple {@link Transition}s.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-AnimationManager">How to use AnimationManager</a>
 * @see com.alee.managers.animation.AnimationManager
 */
public interface AnimationPipeline
{
    /**
     * Asks pipeline to play transition.
     *
     * @param transition transition to play
     */
    public void play ( Transition transition );

    /**
     * Asks pipeline to stop specified transition.
     *
     * @param transition transition to stop
     */
    public void stop ( Transition transition );

    /**
     * Asks pipeline to stop all running transitions and cleanup all used resources.
     */
    public void shutdown ();
}