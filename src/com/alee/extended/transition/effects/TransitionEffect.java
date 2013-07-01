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
 * User: mgarin Date: 09.11.12 Time: 14:11
 * <p/>
 * This interface provides basic methods for transition effect creation. TransitionEffect can be used to perform transitions between images
 * and components using ImageTransiton, ComponentTransition and MultiComponentTransition components.
 */

public interface TransitionEffect
{
    /**
     * Animation timer delay
     */
    public long getAnimationDelay ();

    /**
     * Returns true if transition is on the way
     */
    public boolean isAnimating ();

    /**
     * Single animation tick actions, should return true when last transition tick performed
     */
    public boolean performAnimationTick ( ImageTransition imageTransition );

    /**
     * Current animation state painting
     */
    public void paint ( Graphics2D g2d, ImageTransition imageTransition );
}
