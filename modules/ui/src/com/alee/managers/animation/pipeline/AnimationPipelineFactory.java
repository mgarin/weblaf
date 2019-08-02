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
 * Base interface for any factory producing specific {@link AnimationPipeline} implementation.
 * It is made for convenient {@link AnimationPipeline} acquisition in {@link com.alee.managers.animation.AnimationManager}.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-AnimationManager">How to use AnimationManager</a>
 * @see com.alee.managers.animation.AnimationManager
 */
public interface AnimationPipelineFactory
{
    /**
     * Returns {@link AnimationPipeline} suitable for the specified {@link com.alee.managers.animation.transition.Transition}.
     *
     * @param transition {@link com.alee.managers.animation.transition.Transition} to acquire pipeline for
     * @return {@link AnimationPipeline} suitable for the specified transition
     */
    public AnimationPipeline getPipeline ( Transition transition );
}