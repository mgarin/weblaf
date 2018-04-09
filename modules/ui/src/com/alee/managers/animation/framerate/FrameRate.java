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

package com.alee.managers.animation.framerate;

/**
 * Basic interface for defining amount of animation frames that should be processed per second.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-AnimationManager">How to use AnimationManager</a>
 * @see com.alee.managers.animation.AnimationManager
 */
public interface FrameRate
{
    /**
     * Returns amount of animation frames that should be processed per second.
     * Common values are {@code 24}, {@code 30}, {@code 40} and {@code 60} - those provide visually distinct results.
     *
     * @return amount of animation frames that should be processed per second
     */
    public double value ();

    /**
     * Returns whether frame rate of this transition is fixed or might vary.
     *
     * @return {@code true} if frame rate of this transition is fixed, {@code false} if it might vary
     */
    public boolean isFixed ();
}