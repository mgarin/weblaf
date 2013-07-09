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

package com.alee.extended.transition.effects.slide;

import com.alee.extended.transition.ImageTransition;
import com.alee.extended.transition.TransitionUtils;
import com.alee.extended.transition.effects.DefaultTransitionEffect;
import com.alee.extended.transition.effects.Direction;
import com.alee.utils.LafUtils;

import java.awt.*;

/**
 * User: mgarin Date: 09.11.12 Time: 18:19
 */

public class SlideTransitionEffect extends DefaultTransitionEffect
{
    private static final String SLIDE_MINIMUM_SPEED = "SLIDE_MINIMUM_SPEED";
    private static final String SLIDE_SPEED = "SLIDE_SPEED";
    private static final String SLIDE_FADE = "SLIDE_FADE";
    private static final String SLIDE_TYPE = "SLIDE_TYPE";

    private int minimumSpeed;
    private int speed;
    private boolean fade;
    private SlideType type;
    private Direction direction;

    private int slideProgress;

    public SlideTransitionEffect ()
    {
        super ();
    }

    public int getMinimumSpeed ()
    {
        return get ( SLIDE_MINIMUM_SPEED, 1 );
    }

    public void setMinimumSpeed ( int speed )
    {
        put ( SLIDE_MINIMUM_SPEED, speed );
    }

    public int getSpeed ()
    {
        return get ( SLIDE_SPEED, 20 );
    }

    public void setSpeed ( int speed )
    {
        put ( SLIDE_SPEED, speed );
    }

    public boolean isFade ()
    {
        return get ( SLIDE_FADE, true );
    }

    public void setFade ( boolean transparent )
    {
        put ( SLIDE_FADE, transparent );
    }

    public SlideType getType ()
    {
        return get ( SLIDE_TYPE, SlideType.random );
    }

    public void setType ( SlideType type )
    {
        put ( SLIDE_TYPE, type );
    }

    public void prepareAnimation ( ImageTransition imageTransition )
    {
        // Updating settings
        minimumSpeed = getMinimumSpeed ();
        speed = getSpeed ();
        fade = isFade ();
        type = TransitionUtils.getActualValue ( getType () );
        direction = TransitionUtils.getActualValue ( getDirection () );

        // Updating runtime values
        slideProgress = 0;

        // Updating view
        imageTransition.repaint ();
    }

    public boolean performAnimation ( ImageTransition imageTransition )
    {
        // Determining max progress
        int maxProgress = direction.equals ( Direction.left ) || direction.equals ( Direction.right ) ? imageTransition.getWidth () :
                imageTransition.getHeight ();

        // Incrementing grow states
        int resultSpeed = Math.max ( minimumSpeed, Math.round ( speed * ( float ) Math
                .sqrt ( ( float ) Math.abs ( type.equals ( SlideType.moveOld ) ? slideProgress : maxProgress - slideProgress ) /
                        maxProgress ) ) );
        slideProgress = Math.min ( slideProgress + resultSpeed, maxProgress );

        if ( slideProgress < maxProgress )
        {
            imageTransition.repaint ();
            return false;
        }
        else
        {
            type = null;
            return true;
        }
    }

    public void paint ( Graphics2D g2d, ImageTransition imageTransition )
    {
        // Variables
        final int width = imageTransition.getWidth ();
        final int height = imageTransition.getHeight ();
        final float floatProgress = ( float ) slideProgress / ( direction.isHorizontal () ? width : height );

        // Painting depending on transition type
        if ( type.equals ( SlideType.moveNew ) )
        {
            Point np = new Point ( 0, 0 );
            if ( direction.equals ( Direction.left ) )
            {
                np.x = width - slideProgress;
            }
            else if ( direction.equals ( Direction.right ) )
            {
                np.x = -width + slideProgress;
            }
            else if ( direction.equals ( Direction.up ) )
            {
                np.y = height - slideProgress;
            }
            else if ( direction.equals ( Direction.down ) )
            {
                np.y = -height + slideProgress;
            }

            // Old image as background
            g2d.drawImage ( imageTransition.getCurrentImage (), 0, 0, width, height, null );

            // New image sliding in
            Composite old = LafUtils.setupAlphaComposite ( g2d, floatProgress, fade );
            g2d.drawImage ( imageTransition.getOtherImage (), np.x, np.y, width, height, null );
            LafUtils.restoreComposite ( g2d, old, fade );
        }
        else if ( type.equals ( SlideType.moveOld ) )
        {
            Point cp = new Point ( 0, 0 );
            if ( direction.equals ( Direction.left ) )
            {
                cp.x = -slideProgress;
            }
            else if ( direction.equals ( Direction.right ) )
            {
                cp.x = slideProgress;
            }
            else if ( direction.equals ( Direction.up ) )
            {
                cp.y = -slideProgress;
            }
            else if ( direction.equals ( Direction.down ) )
            {
                cp.y = slideProgress;
            }

            // New image as background
            g2d.drawImage ( imageTransition.getOtherImage (), 0, 0, width, height, null );

            // Old image sliding out
            Composite old = LafUtils.setupAlphaComposite ( g2d, 1f - floatProgress, fade );
            g2d.drawImage ( imageTransition.getCurrentImage (), cp.x, cp.y, width, height, null );
            LafUtils.restoreComposite ( g2d, old, fade );
        }
        else if ( type.equals ( SlideType.moveBoth ) )
        {
            Point cp = new Point ( 0, 0 );
            Point np = new Point ( 0, 0 );
            if ( direction.equals ( Direction.left ) )
            {
                cp.x = -slideProgress;
                np.x = width - slideProgress;
            }
            else if ( direction.equals ( Direction.right ) )
            {
                cp.x = slideProgress;
                np.x = -width + slideProgress;
            }
            else if ( direction.equals ( Direction.up ) )
            {
                cp.y = -slideProgress;
                np.y = height - slideProgress;
            }
            else if ( direction.equals ( Direction.down ) )
            {
                cp.y = slideProgress;
                np.y = -height + slideProgress;
            }

            // Old image sliding out
            g2d.drawImage ( imageTransition.getCurrentImage (), cp.x, cp.y, width, height, null );

            // New image sliding in
            g2d.drawImage ( imageTransition.getOtherImage (), np.x, np.y, width, height, null );
        }
    }
}
