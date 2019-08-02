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

package com.alee.extended.transition.effects.fade;

import com.alee.extended.transition.ImageTransition;
import com.alee.extended.transition.effects.DefaultTransitionEffect;
import com.alee.utils.GraphicsUtils;

import java.awt.*;

/**
 * @author Mikle Garin
 */
public class FadeTransitionEffect extends DefaultTransitionEffect
{
    private static final String FADE_MINIMUM_SPEED = "FADE_MINIMUM_SPEED";
    private static final String FADE_SPEED = "FADE_SPEED";

    private float minimumSpeed;
    private float speed;

    private float opacity;

    public FadeTransitionEffect ()
    {
        super ();
    }

    public float getMinimumSpeed ()
    {
        return get ( FADE_MINIMUM_SPEED, 0.02f );
    }

    public void setMinimumSpeed ( final float speed )
    {
        put ( FADE_MINIMUM_SPEED, speed );
    }

    public float getSpeed ()
    {
        return get ( FADE_SPEED, 0.1f );
    }

    public void setSpeed ( final float speed )
    {
        put ( FADE_SPEED, speed );
    }

    @Override
    public void prepareAnimation ( final ImageTransition imageTransition )
    {
        // Updating settings
        minimumSpeed = getMinimumSpeed ();
        speed = getSpeed ();

        // Updating runtime values
        opacity = 0f;

        // Updating view
        imageTransition.repaint ();
    }

    @Override
    public boolean performAnimation ( final ImageTransition imageTransition )
    {
        if ( opacity < 1f )
        {
            opacity = Math.min ( opacity + getCurrentSpeed (), 1f );
            imageTransition.repaint ();
            return false;
        }
        else
        {
            return true;
        }
    }

    private float getCurrentSpeed ()
    {
        return Math.max ( minimumSpeed, speed * ( float ) Math.sqrt ( ( 1f - opacity ) / 1f ) );
    }

    @Override
    public void paint ( final Graphics2D g2d, final ImageTransition transition )
    {
        // Fading out old image
        final Composite old = GraphicsUtils.setupAlphaComposite ( g2d, 1f - opacity );
        g2d.drawImage ( transition.getCurrentImage (), 0, 0, transition.getWidth (), transition.getHeight (), null );
        GraphicsUtils.restoreComposite ( g2d, old );

        // Fading in new image
        GraphicsUtils.setupAlphaComposite ( g2d, opacity );
        g2d.drawImage ( transition.getOtherImage (), 0, 0, transition.getWidth (), transition.getHeight (), null );
        GraphicsUtils.restoreComposite ( g2d, old );
    }
}