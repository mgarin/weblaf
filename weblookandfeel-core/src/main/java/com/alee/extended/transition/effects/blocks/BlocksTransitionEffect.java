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

package com.alee.extended.transition.effects.blocks;

import com.alee.extended.transition.ImageTransition;
import com.alee.extended.transition.TransitionUtils;
import com.alee.extended.transition.effects.DefaultTransitionEffect;
import com.alee.extended.transition.effects.Direction;
import com.alee.utils.LafUtils;
import com.alee.utils.MathUtils;
import com.alee.utils.swing.WebTimer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mgarin Date: 09.11.12 Time: 15:15
 */

public class BlocksTransitionEffect extends DefaultTransitionEffect
{
    private static final String BLOCK_TYPE = "BLOCK_TYPE";
    private static final String BLOCK_FADE = "BLOCK_FADE";
    private static final String BLOCK_SIZE = "BLOCK_SIZE";
    private static final String BLOCK_SPEED = "BLOCK_SPEED";
    private static final String BLOCK_AMOUNT = "BLOCK_AMOUNT";

    private int size;
    private int speed;
    private int blocksAmount;
    private boolean fade;
    private BlockType type;
    private Direction direction;

    private int[][] progress;
    private Shape clip;
    private List<Point> allPoints;
    private WebTimer randomizer;

    public BlocksTransitionEffect ()
    {
        super ();
    }

    public BlockType getType ()
    {
        return get ( BLOCK_TYPE, BlockType.random );
    }

    public void setType ( BlockType type )
    {
        put ( BLOCK_TYPE, type );
    }

    public boolean isFade ()
    {
        return get ( BLOCK_FADE, true );
    }

    public void setFade ( boolean transparent )
    {
        put ( BLOCK_FADE, transparent );
    }

    public int getSize ()
    {
        return get ( BLOCK_SIZE, 80 );
    }

    public void setSize ( int size )
    {
        put ( BLOCK_SIZE, size );
    }

    public int getSpeed ()
    {
        return get ( BLOCK_SPEED, 4 );
    }

    public void setSpeed ( int speed )
    {
        put ( BLOCK_SPEED, speed );
    }

    public int getBlocksAmount ()
    {
        return get ( BLOCK_AMOUNT, 5 );
    }

    public void setBlocksAmount ( int blocksAmount )
    {
        put ( BLOCK_AMOUNT, blocksAmount );
    }

    public void prepareAnimation ( final ImageTransition imageTransition )
    {
        // Updating settings
        size = getSize ();
        speed = getSpeed ();
        blocksAmount = getBlocksAmount ();
        fade = isFade ();
        direction = TransitionUtils.getActualValue ( getDirection () );
        type = TransitionUtils.getActualValue ( getType () );

        // Updating runtime values
        int h = imageTransition.getHeight ();
        h = ( h % size == 0 ? h : ( h / size ) * size + size );
        int w = imageTransition.getWidth ();
        w = ( w % size == 0 ? w : ( w / size ) * size + size );
        int cols = w / size;
        int rows = h / size;
        progress = new int[ cols ][ rows ];
        for ( int i = 0; i < cols; i++ )
        {
            for ( int j = 0; j < rows; j++ )
            {
                progress[ i ][ j ] = 0;
            }
        }
        if ( direction.equals ( Direction.right ) || direction.equals ( Direction.down ) )
        {
            progress[ 0 ][ 0 ] = speed;
        }
        else
        {
            progress[ cols - 1 ][ rows - 1 ] = speed;
        }
        if ( !fade )
        {
            clip = getBlocksProgressShape ( progress );
        }
        if ( type.equals ( BlockType.randomize ) )
        {
            // Collecting all blocks array
            allPoints = new ArrayList<Point> ();
            for ( int i = 0; i < cols; i++ )
            {
                for ( int j = 0; j < rows; j++ )
                {
                    allPoints.add ( new Point ( i, j ) );
                }
            }

            // Restarting timer
            if ( randomizer != null )
            {
                randomizer.stop ();
            }
            randomizer = new WebTimer ( "BlocksTransitionEffect.randomizer", 50 );
            randomizer.addActionListener ( new ActionListener ()
            {
                public void actionPerformed ( ActionEvent e )
                {
                    for ( int i = 0; i < blocksAmount; i++ )
                    {
                        if ( allPoints.size () > 0 )
                        {
                            int index = MathUtils.random ( allPoints.size () );
                            Point toAdd = allPoints.remove ( index );
                            progress[ toAdd.x ][ toAdd.y ] += speed;
                            imageTransition.repaint ();
                        }
                        else
                        {
                            randomizer.stop ();
                        }
                    }
                }
            } );
            randomizer.start ();
        }

        // Updating view
        imageTransition.repaint ();
    }

    public boolean performAnimation ( ImageTransition imageTransition )
    {
        // Incrementing grow states
        boolean allMax = true;
        int cols = progress.length;
        int rows = progress[ 0 ].length;
        int filling = 0;
        for ( int i = 0; i < cols; i++ )
        {
            for ( int j = 0; j < rows; j++ )
            {
                if ( progress[ i ][ j ] < size )
                {
                    if ( progress[ i ][ j ] > 0 )
                    {
                        // Was already growing
                        progress[ i ][ j ] += speed;
                        filling++;
                    }
                    else
                    {
                        if ( type.equals ( BlockType.cascade ) )
                        {
                            if ( canStartGrow ( i, j, progress ) )
                            {
                                // Starts growing now
                                progress[ i ][ j ] += speed;
                            }
                        }
                    }

                    // Check if still not max
                    if ( allMax && progress[ i ][ j ] < size )
                    {
                        allMax = false;
                    }
                }
            }
        }

        // Updating blocks clip
        if ( !fade )
        {
            clip = getBlocksProgressShape ( progress );
        }

        if ( !allMax )
        {
            imageTransition.repaint ();
            return false;
        }
        else
        {
            clip = null;
            if ( randomizer != null )
            {
                randomizer.stop ();
            }
            return true;
        }
    }

    private Shape getBlocksProgressShape ( int[][] blocksProgress )
    {
        GeneralPath gp = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
        for ( int i = 0; i < blocksProgress.length; i++ )
        {
            for ( int j = 0; j < blocksProgress[ 0 ].length; j++ )
            {
                gp.append ( new Rectangle ( i * size, j * size, blocksProgress[ i ][ j ], blocksProgress[ i ][ j ] ), false );
            }
        }
        return gp;
    }

    private boolean canStartGrow ( int i, int j, int[][] blocksProgress )
    {
        if ( direction.equals ( Direction.right ) || direction.equals ( Direction.down ) )
        {
            return i > 0 && blocksProgress[ i - 1 ][ j ] > ( float ) size / 6 ||
                    j > 0 && blocksProgress[ i ][ j - 1 ] > ( float ) size / 6 ||

                    i > 1 && blocksProgress[ i - 2 ][ j ] > ( float ) size * 2 / 6 ||
                    j > 1 && blocksProgress[ i ][ j - 2 ] > ( float ) size * 2 / 6 ||

                    i > 2 && blocksProgress[ i - 3 ][ j ] > ( float ) size * 3 / 6 ||
                    j > 2 && blocksProgress[ i ][ j - 3 ] > ( float ) size * 3 / 6 ||

                    i > 3 && blocksProgress[ i - 4 ][ j ] > ( float ) size * 4 / 6 ||
                    j > 3 && blocksProgress[ i ][ j - 4 ] > ( float ) size * 4 / 6 ||

                    i > 4 && blocksProgress[ i - 5 ][ j ] > ( float ) size * 5 / 6 ||
                    j > 4 && blocksProgress[ i ][ j - 5 ] > ( float ) size * 5 / 6;
        }
        else
        {
            return i < blocksProgress.length - 1 && blocksProgress[ i + 1 ][ j ] > ( float ) size / 6 ||
                    j < blocksProgress[ 0 ].length - 1 && blocksProgress[ i ][ j + 1 ] > ( float ) size / 6 ||

                    i < blocksProgress.length - 2 && blocksProgress[ i + 2 ][ j ] > ( float ) size * 2 / 6 ||
                    j < blocksProgress[ 0 ].length - 2 && blocksProgress[ i ][ j + 2 ] > ( float ) size * 2 / 6 ||

                    i < blocksProgress.length - 3 && blocksProgress[ i + 3 ][ j ] > ( float ) size * 3 / 6 ||
                    j < blocksProgress[ 0 ].length - 3 && blocksProgress[ i ][ j + 3 ] > ( float ) size * 3 / 6 ||

                    i < blocksProgress.length - 4 && blocksProgress[ i + 4 ][ j ] > ( float ) size * 4 / 6 ||
                    j < blocksProgress[ 0 ].length - 4 && blocksProgress[ i ][ j + 4 ] > ( float ) size * 4 / 6 ||

                    i < blocksProgress.length - 5 && blocksProgress[ i + 5 ][ j ] > ( float ) size * 5 / 6 ||
                    j < blocksProgress[ 0 ].length - 5 && blocksProgress[ i ][ j + 5 ] > ( float ) size * 5 / 6;
        }
    }

    public void paint ( Graphics2D g2d, ImageTransition imageTransition )
    {
        // Variables
        final int width = imageTransition.getWidth ();
        final int height = imageTransition.getHeight ();

        // Old image as background
        g2d.drawImage ( imageTransition.getCurrentImage (), 0, 0, width, height, null );

        // Appearance type
        if ( fade )
        {
            // Drawing separate transparent blocks
            int cols = progress.length;
            int rows = progress[ 0 ].length;
            for ( int i = 0; i < cols; i++ )
            {
                for ( int j = 0; j < rows; j++ )
                {
                    int block = progress[ i ][ j ];
                    if ( block > 0 )
                    {
                        // Block coordinates
                        int dx1 = i * size;
                        int dy1 = j * size;
                        int w = Math.min ( width - dx1, size );
                        int h = Math.min ( height - dy1, size );
                        int dx2 = dx1 + w;
                        int dy2 = dy1 + h;

                        // Single image block with custom transparency
                        Composite old = LafUtils.setupAlphaComposite ( g2d, ( float ) block / size, block < size );
                        g2d.drawImage ( imageTransition.getOtherImage (), dx1, dy1, dx2, dy2, dx1, dy1, dx2, dy2, null );
                        LafUtils.restoreComposite ( g2d, old, block < size );
                    }
                }
            }
        }
        else
        {
            // New image with decreasing clipped area
            Shape old = LafUtils.intersectClip ( g2d, clip );
            g2d.drawImage ( imageTransition.getOtherImage (), 0, 0, width, height, null );
            LafUtils.restoreClip ( g2d, old );
        }
    }
}
