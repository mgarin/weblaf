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

package com.alee.extended.transition.effects.zoom;

import com.alee.extended.transition.ImageTransition;
import com.alee.extended.transition.TransitionUtils;
import com.alee.extended.transition.effects.DefaultTransitionEffect;
import com.alee.utils.LafUtils;

import java.awt.*;

/**
 * User: mgarin Date: 09.11.12 Time: 18:56
 */

public class ZoomTransitionEffect extends DefaultTransitionEffect
{
    private static final String ZOOM_MINIMUM_SPEED = "ZOOM_MINIMUM_SPEED";
    private static final String ZOOM_SPEED = "ZOOM_SPEED";
    private static final String ZOOM_FADE = "ZOOM_FADE";
    private static final String ZOOM_TYPE = "ZOOM_TYPE";

    private float minimumSpeed;
    private float speed;
    private ZoomType type;

    private float size;

    public ZoomTransitionEffect ()
    {
        super ();
    }

    public float getMinimumSpeed ()
    {
        return get ( ZOOM_MINIMUM_SPEED, 0.02f );
    }

    public void setMinimumSpeed ( float speed )
    {
        put ( ZOOM_MINIMUM_SPEED, speed );
    }

    public float getSpeed ()
    {
        return get ( ZOOM_SPEED, 0.1f );
    }

    public void setSpeed ( float speed )
    {
        put ( ZOOM_SPEED, speed );
    }

    public boolean isFade ()
    {
        return get ( ZOOM_FADE, true );
    }

    public void setFade ( boolean transparent )
    {
        put ( ZOOM_FADE, transparent );
    }

    public ZoomType getType ()
    {
        return get ( ZOOM_TYPE, ZoomType.random );
    }

    public void setType ( ZoomType type )
    {
        put ( ZOOM_TYPE, type );
    }

    public void prepareAnimation ( ImageTransition imageTransition )
    {
        // Updating settings
        minimumSpeed = getMinimumSpeed ();
        speed = getSpeed ();
        type = TransitionUtils.getActualValue ( getType () );

        // Updating runtime values
        size = 0f;

        // Updating view
        imageTransition.repaint ();
    }

    public boolean performAnimation ( ImageTransition imageTransition )
    {
        if ( size < 1f )
        {
            size = Math.min ( size + getCurrentSpeed (), 1f );
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
        return Math.max ( minimumSpeed, speed * ( float ) Math.sqrt ( ( 1f - size ) / 1f ) );
    }

    public void paint ( Graphics2D g2d, ImageTransition imageTransition )
    {
        int tw = imageTransition.getWidth ();
        int th = imageTransition.getHeight ();
        if ( type.equals ( ZoomType.zoomIn ) )
        {
            // Painting old image behind the new one
            g2d.drawImage ( imageTransition.getCurrentImage (), 0, 0, tw, th, null );

            // Fading in new image
            Composite old = LafUtils.setupAlphaComposite ( g2d, size, isFade () );
            int w = Math.round ( tw * size );
            int h = Math.round ( th * size );
            g2d.drawImage ( imageTransition.getOtherImage (), tw / 2 - w / 2, th / 2 - h / 2, w, h, null );
            LafUtils.restoreComposite ( g2d, old, isFade () );
        }
        else
        {
            // Painting new image behind the old one
            g2d.drawImage ( imageTransition.getOtherImage (), 0, 0, tw, th, null );

            // Fading in new image
            Composite old = LafUtils.setupAlphaComposite ( g2d, 1f - size, isFade () );
            int w = Math.round ( tw * ( 1f - size ) );
            int h = Math.round ( th * ( 1f - size ) );
            g2d.drawImage ( imageTransition.getCurrentImage (), tw / 2 - w / 2, th / 2 - h / 2, w, h, null );
            LafUtils.restoreComposite ( g2d, old, isFade () );
        }
    }
}
