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
import com.alee.utils.GraphicsUtils;

import java.awt.*;

/**
 * @author Mikle Garin
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

    public void setMinimumSpeed ( final int speed )
    {
        put ( SLIDE_MINIMUM_SPEED, speed );
    }

    public int getSpeed ()
    {
        return get ( SLIDE_SPEED, 20 );
    }

    public void setSpeed ( final int speed )
    {
        put ( SLIDE_SPEED, speed );
    }

    public boolean isFade ()
    {
        return get ( SLIDE_FADE, true );
    }

    public void setFade ( final boolean transparent )
    {
        put ( SLIDE_FADE, transparent );
    }

    public SlideType getType ()
    {
        return get ( SLIDE_TYPE, SlideType.random );
    }

    public void setType ( final SlideType type )
    {
        put ( SLIDE_TYPE, type );
    }

    @Override
    public void prepareAnimation ( final ImageTransition imageTransition )
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

    @Override
    public boolean performAnimation ( final ImageTransition imageTransition )
    {
        // Determining max progress
        final int maxProgress = direction.equals ( Direction.left ) || direction.equals ( Direction.right ) ? imageTransition.getWidth () :
                imageTransition.getHeight ();

        // Incrementing grow states
        final int resultSpeed = Math.max ( minimumSpeed, Math.round ( speed * ( float ) Math
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

    @Override
    public void paint ( final Graphics2D g2d, final ImageTransition transition )
    {
        // Variables
        final int width = transition.getWidth ();
        final int height = transition.getHeight ();
        final float floatProgress = ( float ) slideProgress / ( direction.isHorizontal () ? width : height );

        // Painting depending on transition type
        if ( type.equals ( SlideType.moveNew ) )
        {
            final Point np = new Point ( 0, 0 );
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
            g2d.drawImage ( transition.getCurrentImage (), 0, 0, width, height, null );

            // New image sliding in
            final Composite old = GraphicsUtils.setupAlphaComposite ( g2d, floatProgress, fade );
            g2d.drawImage ( transition.getOtherImage (), np.x, np.y, width, height, null );
            GraphicsUtils.restoreComposite ( g2d, old, fade );
        }
        else if ( type.equals ( SlideType.moveOld ) )
        {
            final Point cp = new Point ( 0, 0 );
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
            g2d.drawImage ( transition.getOtherImage (), 0, 0, width, height, null );

            // Old image sliding out
            final Composite old = GraphicsUtils.setupAlphaComposite ( g2d, 1f - floatProgress, fade );
            g2d.drawImage ( transition.getCurrentImage (), cp.x, cp.y, width, height, null );
            GraphicsUtils.restoreComposite ( g2d, old, fade );
        }
        else if ( type.equals ( SlideType.moveBoth ) )
        {
            final Point cp = new Point ( 0, 0 );
            final Point np = new Point ( 0, 0 );
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
            g2d.drawImage ( transition.getCurrentImage (), cp.x, cp.y, width, height, null );

            // New image sliding in
            g2d.drawImage ( transition.getOtherImage (), np.x, np.y, width, height, null );
        }
    }
}