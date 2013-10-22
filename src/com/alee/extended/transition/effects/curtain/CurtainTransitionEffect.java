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

package com.alee.extended.transition.effects.curtain;

import com.alee.extended.transition.ImageTransition;
import com.alee.extended.transition.TransitionUtils;
import com.alee.extended.transition.effects.DefaultTransitionEffect;
import com.alee.extended.transition.effects.Direction;
import com.alee.utils.LafUtils;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;

/**
 * User: mgarin Date: 09.11.12 Time: 16:35
 */

public class CurtainTransitionEffect extends DefaultTransitionEffect
{
    private static final String CURTAIN_SIZE = "CURTAIN_SIZE";
    private static final String CURTAIN_MINIMUM_SPEED = "CURTAIN_MINIMUM_SPEED";
    private static final String CURTAIN_SPEED = "CURTAIN_SPEED";
    private static final String CURTAIN_FADE = "CURTAIN_FADE";
    private static final String CURTAIN_TYPE = "CURTAIN_TYPE";
    private static final String CURTAIN_SLIDE_DIRECTION = "CURTAIN_SLIDE_DIRECTION";

    private int size;
    private int minimumSpeed;
    private int speed;
    private boolean fade;
    private CurtainType type;
    private Direction direction;
    private CurtainSlideDirection slideDirection;

    private int[] progress;
    private Shape clip;

    public CurtainTransitionEffect ()
    {
        super ();
    }

    public int getSize ()
    {
        return get ( CURTAIN_SIZE, 80 );
    }

    public void setSize ( int size )
    {
        put ( CURTAIN_SIZE, size );
    }

    public int getMinimumSpeed ()
    {
        return get ( CURTAIN_MINIMUM_SPEED, 1 );
    }

    public void setMinimumSpeed ( int speed )
    {
        put ( CURTAIN_MINIMUM_SPEED, speed );
    }

    public int getSpeed ()
    {
        return get ( CURTAIN_SPEED, 4 );
    }

    public void setSpeed ( int speed )
    {
        put ( CURTAIN_SPEED, speed );
    }

    public boolean isFade ()
    {
        return get ( CURTAIN_FADE, true );
    }

    public void setFade ( boolean transparent )
    {
        put ( CURTAIN_FADE, transparent );
    }

    public CurtainType getType ()
    {
        return get ( CURTAIN_TYPE, CurtainType.random );
    }

    public void setType ( CurtainType type )
    {
        put ( CURTAIN_TYPE, type );
    }

    public CurtainSlideDirection getSlideDirection ()
    {
        return get ( CURTAIN_SLIDE_DIRECTION, CurtainSlideDirection.random );
    }

    public void setSlideDirection ( CurtainSlideDirection direction )
    {
        put ( CURTAIN_SLIDE_DIRECTION, direction );
    }

    @Override
    public void prepareAnimation ( ImageTransition imageTransition )
    {
        // Updating settings
        size = getSize ();
        minimumSpeed = getMinimumSpeed ();
        speed = getSpeed ();
        fade = isFade ();
        type = TransitionUtils.getActualValue ( getType () );
        direction = TransitionUtils.getActualValue ( getDirection () );
        slideDirection = TransitionUtils.getActualValue ( getSlideDirection () );

        // Updating runtime values
        int h = direction.isVertical () ? imageTransition.getHeight () : imageTransition.getWidth ();
        int rows = ( h % size == 0 ? h : ( h / size ) * size + size ) / size;
        progress = new int[ rows ];
        for ( int i = 0; i < rows; i++ )
        {
            progress[ i ] = 0;
        }
        if ( direction.equals ( Direction.right ) || direction.equals ( Direction.down ) )
        {
            progress[ 0 ] = increaseProgress ( imageTransition, 0 );
        }
        else
        {
            progress[ rows - 1 ] = increaseProgress ( imageTransition, 0 );
        }
        if ( type.equals ( CurtainType.fill ) )
        {
            clip = getCurtainProgressShape ( progress, imageTransition );
        }

        // Updating view
        imageTransition.repaint ();
    }

    @Override
    public boolean performAnimation ( ImageTransition imageTransition )
    {
        int max = getMaxProgress ( imageTransition );

        // Incrementing grow states
        boolean allMax = true;
        int rows = progress.length;
        for ( int i = 0; i < rows; i++ )
        {
            if ( progress[ i ] < max )
            {
                if ( progress[ i ] > 0 )
                {
                    // Was already growing
                    progress[ i ] = increaseProgress ( imageTransition, progress[ i ] );
                }
                else if ( canStartGrow ( i, progress, max ) )
                {
                    // Starts growing now
                    progress[ i ] = increaseProgress ( imageTransition, progress[ i ] );
                }

                // Check if still not max
                if ( allMax && progress[ i ] < max )
                {
                    allMax = false;
                }
            }
        }

        // Updating curtains clip
        if ( type.equals ( CurtainType.fill ) )
        {
            clip = getCurtainProgressShape ( progress, imageTransition );
        }

        if ( !allMax )
        {
            imageTransition.repaint ();
            return false;
        }
        else
        {
            clip = null;
            type = null;
            return true;
        }
    }

    private int increaseProgress ( ImageTransition imageTransition, int progress )
    {
        int max = getMaxProgress ( imageTransition );
        return Math.min ( progress + getCurrentSpeed ( progress, max ), max );
    }

    private int getMaxProgress ( ImageTransition imageTransition )
    {
        if ( type.equals ( CurtainType.slide ) )
        {
            return direction.isHorizontal () ? imageTransition.getHeight () : imageTransition.getWidth ();
        }
        else
        {
            return size;
        }
    }

    private int getCurrentSpeed ( int progress, int maxProgress )
    {
        if ( type.equals ( CurtainType.slide ) )
        {
            return Math.max ( minimumSpeed,
                    Math.round ( speed * ( float ) Math.sqrt ( ( float ) ( maxProgress - progress ) / maxProgress ) ) );
        }
        else
        {
            return speed;
        }
    }

    private boolean canStartGrow ( int i, int[] curtainProgress, int max )
    {
        if ( direction.equals ( Direction.right ) || direction.equals ( Direction.down ) )
        {
            return i > 0 && curtainProgress[ i - 1 ] > max / 4 ||
                    i > 1 && curtainProgress[ i - 2 ] > max * 2 / 4 ||
                    i > 2 && curtainProgress[ i - 3 ] > max * 3 / 4;
        }
        else
        {
            return i < curtainProgress.length - 1 && curtainProgress[ i + 1 ] > max / 4 ||
                    i < curtainProgress.length - 2 && curtainProgress[ i + 2 ] > max * 2 / 4 ||
                    i < curtainProgress.length - 3 && curtainProgress[ i + 3 ] > max * 3 / 4;
        }
    }

    private Shape getCurtainProgressShape ( int[] curtainProgress, ImageTransition imageTransition )
    {
        GeneralPath gp = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
        for ( int i = 0; i < curtainProgress.length; i++ )
        {
            if ( direction.isVertical () )
            {
                gp.append ( new Rectangle ( 0, i * size, imageTransition.getWidth (), curtainProgress[ i ] ), false );
            }
            else
            {
                gp.append ( new Rectangle ( i * size, 0, curtainProgress[ i ], imageTransition.getHeight () ), false );
            }
        }
        return gp;
    }

    @Override
    public void paint ( Graphics2D g2d, ImageTransition transition )
    {
        // Variables
        int width = transition.getWidth ();
        int height = transition.getHeight ();
        int max = getMaxProgress ( transition );

        // Old image as background
        g2d.drawImage ( transition.getCurrentImage (), 0, 0, width, height, null );

        // Separately drawn transparent curtains
        BufferedImage otherImage = transition.getOtherImage ();
        if ( type.equals ( CurtainType.fade ) || type.equals ( CurtainType.fill ) && fade )
        {
            int rows = progress.length;
            for ( int i = 0; i < rows; i++ )
            {
                int curtain = progress[ i ];
                if ( curtain > 0 )
                {
                    if ( direction.isVertical () )
                    {
                        int dx1 = 0;
                        int dy1 = i * size;
                        int h = Math.min ( height - i * size, type.equals ( CurtainType.fill ) && fade ? curtain : size );
                        int dx2 = dx1 + width;
                        int dy2 = dy1 + h;

                        Composite old = LafUtils.setupAlphaComposite ( g2d, ( float ) curtain / size, curtain < size );
                        g2d.drawImage ( otherImage, dx1, dy1, dx2, dy2, dx1, dy1, dx2, dy2, null );
                        LafUtils.restoreComposite ( g2d, old, curtain < size );
                    }
                    else
                    {
                        int dx1 = i * size;
                        int dy1 = 0;
                        int w = Math.min ( width - i * size, type.equals ( CurtainType.fill ) && fade ? curtain : size );
                        int dx2 = dx1 + w;
                        int dy2 = dy1 + height;

                        Composite old = LafUtils.setupAlphaComposite ( g2d, ( float ) curtain / size, curtain < size );
                        g2d.drawImage ( otherImage, dx1, dy1, dx2, dy2, dx1, dy1, dx2, dy2, null );
                        LafUtils.restoreComposite ( g2d, old, curtain < size );
                    }
                }
            }
        }
        else if ( type.equals ( CurtainType.fill ) )
        {
            // New image with decreasing clipped area
            Shape old = LafUtils.intersectClip ( g2d, clip );
            g2d.drawImage ( otherImage, 0, 0, width, height, null );
            LafUtils.restoreClip ( g2d, old );
        }
        else if ( type.equals ( CurtainType.slide ) )
        {
            int rows = progress.length;
            for ( int i = 0; i < rows; i++ )
            {
                int curtain = progress[ i ];
                if ( curtain > 0 )
                {
                    if ( direction.isVertical () )
                    {
                        int xMark;
                        if ( slideDirection.equals ( CurtainSlideDirection.left ) )
                        {
                            xMark = direction.equals ( Direction.up ) ? -1 : 1;
                        }
                        else if ( slideDirection.equals ( CurtainSlideDirection.right ) )
                        {
                            xMark = direction.equals ( Direction.up ) ? 1 : -1;
                        }
                        else
                        {
                            xMark = i % 2 == 0 ? 1 : -1;
                        }

                        int dx1 = xMark * Math.round ( width * ( ( float ) curtain / max ) ) - xMark * width;
                        int dx2 = dx1 + width;

                        int dy1 = i * size;
                        int dy2 = dy1 + Math.min ( height - i * size, size );

                        Composite old = LafUtils.setupAlphaComposite ( g2d, ( float ) curtain / max, fade && curtain < max );
                        g2d.drawImage ( otherImage, dx1, dy1, dx2, dy2, 0, dy1, width, dy2, null );
                        LafUtils.restoreComposite ( g2d, old, fade && curtain < max );
                    }
                    else
                    {
                        int yMark;
                        if ( slideDirection.equals ( CurtainSlideDirection.left ) )
                        {
                            yMark = direction.equals ( Direction.left ) ? -1 : 1;
                        }
                        else if ( slideDirection.equals ( CurtainSlideDirection.right ) )
                        {
                            yMark = direction.equals ( Direction.left ) ? 1 : -1;
                        }
                        else
                        {
                            yMark = i % 2 == 0 ? 1 : -1;
                        }

                        int dx1 = i * size;
                        int dx2 = dx1 + Math.min ( width - i * size, size );

                        int dy1 = yMark * Math.round ( height * ( ( float ) curtain / max ) ) - yMark * height;
                        int dy2 = dy1 + height;

                        Composite old = LafUtils.setupAlphaComposite ( g2d, ( float ) curtain / max, fade && curtain < max );
                        g2d.drawImage ( otherImage, dx1, dy1, dx2, dy2, dx1, 0, dx2, height, null );
                        LafUtils.restoreComposite ( g2d, old, fade && curtain < max );
                    }
                }
            }
        }
    }
}
