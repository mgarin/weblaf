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

package com.alee.extended.transition.effects;

import com.alee.extended.transition.ImageTransition;

import java.awt.*;

/**
 * This interface provides basic methods for transition effect creation. TransitionEffect can be used to perform transitions between images
 * and components using ImageTransiton, ComponentTransition and MultiComponentTransition components.
 *
 * @author Mikle Garin
 */
public interface TransitionEffect
{
    /**
     * Animation timer delay.
     *
     * @return animation timer delay
     */
    public long getAnimationDelay ();

    /**
     * Returns whether transition is performed or not.
     *
     * @return true if transition is performed, false otherwise
     */
    public boolean isAnimating ();

    /**
     * Called for each animation tick performed.
     * Returns whether this was the last transition tick or not.
     *
     * @param transition image transition component
     * @return true if this was the last transition tick, false otherwise
     */
    public boolean performAnimationTick ( ImageTransition transition );

    /**
     * Paints current transition state.
     *
     * @param g2d        graphics context
     * @param transition image transition component
     */
    public void paint ( Graphics2D g2d, ImageTransition transition );
}