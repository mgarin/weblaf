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

/**
 * Abstract {@link AnimationPipeline} implementation.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-AnimationManager">How to use AnimationManager</a>
 * @see com.alee.managers.animation.AnimationManager
 */
public abstract class AbstractAnimationPipeline implements AnimationPipeline
{
    /**
     * Whether or not this pipeline was terminated.
     * This mark is always set to {@code true} upon pipeline shutdown.
     */
    private boolean terminated;

    /**
     * Returns whether or not this pipeline was terminated.
     *
     * @return {@code true} if this pipeline was terminated, {@code false} otherwise
     */
    public boolean isTerminated ()
    {
        return terminated;
    }

    @Override
    public void shutdown ()
    {
        // Marking as terminated
        terminated = true;
    }
}