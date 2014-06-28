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

package com.alee.examples.groups.desktoppane.tetris;

import com.alee.utils.MathUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: mgarin Date: 02.06.11 Time: 17:09
 */

public class Figure
{
    private int rotation = 0;

    private FigureType figureType;
    private Block[] blocks;

    // Figure position on the field
    private Point point;

    // Blocks placement
    private int[][] placement = new int[ 4 ][ 4 ];

    public Figure ()
    {
        super ();
        figureType = FigureType.values ()[ MathUtils.random ( FigureType.values ().length ) ];
        fillWidthRandomBlocks ();
        fillPlacement ();
        fillStartPoint ();
    }

    public Figure ( FigureType figureType )
    {
        super ();
        this.figureType = figureType;
        fillWidthRandomBlocks ();
        fillPlacement ();
        fillStartPoint ();
    }

    public Figure ( FigureType figureType, Block[] blocks )
    {
        super ();
        this.figureType = figureType;
        this.blocks = blocks;
        fillPlacement ();
        fillStartPoint ();
    }

    private void fillWidthRandomBlocks ()
    {
        blocks = new Block[ 4 ];
        blocks[ 0 ] = new Block ();
        blocks[ 1 ] = new Block ();
        blocks[ 2 ] = new Block ();
        blocks[ 3 ] = new Block ();
    }

    private void fillPlacement ()
    {
        clearPlacement ();
        if ( figureType.equals ( FigureType.trident ) )
        {
            if ( rotation == 0 )
            {
                placement[ 0 ][ 1 ] = 0;
                placement[ 1 ][ 0 ] = 1;
                placement[ 1 ][ 1 ] = 2;
                placement[ 1 ][ 2 ] = 3;
            }
            else if ( rotation == 1 )
            {
                placement[ 1 ][ 2 ] = 0;
                placement[ 0 ][ 1 ] = 1;
                placement[ 1 ][ 1 ] = 2;
                placement[ 2 ][ 1 ] = 3;
            }
            else if ( rotation == 2 )
            {
                placement[ 2 ][ 1 ] = 0;
                placement[ 1 ][ 2 ] = 1;
                placement[ 1 ][ 1 ] = 2;
                placement[ 1 ][ 0 ] = 3;
            }
            else if ( rotation == 3 )
            {
                placement[ 1 ][ 0 ] = 0;
                placement[ 0 ][ 1 ] = 3;
                placement[ 1 ][ 1 ] = 2;
                placement[ 2 ][ 1 ] = 1;
            }
        }
        else if ( figureType.equals ( FigureType.line ) )
        {
            if ( rotation == 0 )
            {
                placement[ 1 ][ 0 ] = 0;
                placement[ 1 ][ 1 ] = 1;
                placement[ 1 ][ 2 ] = 2;
                placement[ 1 ][ 3 ] = 3;
            }
            else if ( rotation == 1 )
            {
                placement[ 0 ][ 1 ] = 0;
                placement[ 1 ][ 1 ] = 1;
                placement[ 2 ][ 1 ] = 2;
                placement[ 3 ][ 1 ] = 3;
            }
            else if ( rotation == 2 )
            {
                placement[ 1 ][ 0 ] = 3;
                placement[ 1 ][ 1 ] = 2;
                placement[ 1 ][ 2 ] = 1;
                placement[ 1 ][ 3 ] = 0;
            }
            else if ( rotation == 3 )
            {
                placement[ 0 ][ 1 ] = 3;
                placement[ 1 ][ 1 ] = 2;
                placement[ 2 ][ 1 ] = 1;
                placement[ 3 ][ 1 ] = 0;
            }
        }
        else if ( figureType.equals ( FigureType.lineL ) )
        {
            if ( rotation == 0 )
            {
                placement[ 0 ][ 0 ] = 0;
                placement[ 0 ][ 1 ] = 1;
                placement[ 1 ][ 1 ] = 2;
                placement[ 2 ][ 1 ] = 3;
            }
            else if ( rotation == 1 )
            {
                placement[ 0 ][ 2 ] = 0;
                placement[ 1 ][ 2 ] = 1;
                placement[ 1 ][ 1 ] = 2;
                placement[ 1 ][ 0 ] = 3;
            }
            else if ( rotation == 2 )
            {
                placement[ 2 ][ 2 ] = 0;
                placement[ 2 ][ 1 ] = 1;
                placement[ 1 ][ 1 ] = 2;
                placement[ 0 ][ 1 ] = 3;
            }
            else if ( rotation == 3 )
            {
                placement[ 2 ][ 0 ] = 0;
                placement[ 1 ][ 0 ] = 1;
                placement[ 1 ][ 1 ] = 2;
                placement[ 1 ][ 2 ] = 3;
            }
        }
        else if ( figureType.equals ( FigureType.lineR ) )
        {
            if ( rotation == 0 )
            {
                placement[ 0 ][ 2 ] = 0;
                placement[ 0 ][ 1 ] = 1;
                placement[ 1 ][ 1 ] = 2;
                placement[ 2 ][ 1 ] = 3;
            }
            else if ( rotation == 1 )
            {
                placement[ 2 ][ 2 ] = 0;
                placement[ 1 ][ 2 ] = 1;
                placement[ 1 ][ 1 ] = 2;
                placement[ 1 ][ 0 ] = 3;
            }
            else if ( rotation == 2 )
            {
                placement[ 2 ][ 0 ] = 0;
                placement[ 2 ][ 1 ] = 1;
                placement[ 1 ][ 1 ] = 2;
                placement[ 0 ][ 1 ] = 3;
            }
            else if ( rotation == 3 )
            {
                placement[ 0 ][ 0 ] = 0;
                placement[ 1 ][ 0 ] = 1;
                placement[ 1 ][ 1 ] = 2;
                placement[ 1 ][ 2 ] = 3;
            }
        }
        else if ( figureType.equals ( FigureType.block ) )
        {
            if ( rotation == 0 )
            {
                placement[ 0 ][ 0 ] = 0;
                placement[ 0 ][ 1 ] = 1;
                placement[ 1 ][ 0 ] = 2;
                placement[ 1 ][ 1 ] = 3;
            }
            else if ( rotation == 1 )
            {
                placement[ 0 ][ 0 ] = 2;
                placement[ 0 ][ 1 ] = 0;
                placement[ 1 ][ 0 ] = 3;
                placement[ 1 ][ 1 ] = 1;
            }
            else if ( rotation == 2 )
            {
                placement[ 0 ][ 0 ] = 3;
                placement[ 0 ][ 1 ] = 2;
                placement[ 1 ][ 0 ] = 1;
                placement[ 1 ][ 1 ] = 0;
            }
            else if ( rotation == 3 )
            {
                placement[ 0 ][ 0 ] = 1;
                placement[ 0 ][ 1 ] = 3;
                placement[ 1 ][ 0 ] = 0;
                placement[ 1 ][ 1 ] = 2;
            }
        }
        else if ( figureType.equals ( FigureType.blockL ) )
        {
            if ( rotation == 0 )
            {
                placement[ 1 ][ 0 ] = 0;
                placement[ 1 ][ 1 ] = 1;
                placement[ 2 ][ 1 ] = 2;
                placement[ 2 ][ 2 ] = 3;
            }
            else if ( rotation == 1 )
            {
                placement[ 0 ][ 1 ] = 0;
                placement[ 1 ][ 1 ] = 1;
                placement[ 1 ][ 0 ] = 2;
                placement[ 2 ][ 0 ] = 3;
            }
            else if ( rotation == 2 )
            {
                placement[ 1 ][ 2 ] = 0;
                placement[ 1 ][ 1 ] = 1;
                placement[ 0 ][ 1 ] = 2;
                placement[ 0 ][ 0 ] = 3;
            }
            else if ( rotation == 3 )
            {
                placement[ 2 ][ 1 ] = 0;
                placement[ 1 ][ 1 ] = 1;
                placement[ 1 ][ 2 ] = 2;
                placement[ 0 ][ 2 ] = 3;
            }
        }
        else if ( figureType.equals ( FigureType.blockR ) )
        {
            if ( rotation == 0 )
            {
                placement[ 1 ][ 2 ] = 0;
                placement[ 1 ][ 1 ] = 1;
                placement[ 2 ][ 1 ] = 2;
                placement[ 2 ][ 0 ] = 3;
            }
            else if ( rotation == 1 )
            {
                placement[ 2 ][ 1 ] = 0;
                placement[ 1 ][ 1 ] = 1;
                placement[ 1 ][ 0 ] = 2;
                placement[ 0 ][ 0 ] = 3;
            }
            else if ( rotation == 2 )
            {
                placement[ 1 ][ 0 ] = 0;
                placement[ 1 ][ 1 ] = 1;
                placement[ 0 ][ 1 ] = 2;
                placement[ 0 ][ 2 ] = 3;
            }
            else if ( rotation == 3 )
            {
                placement[ 0 ][ 1 ] = 0;
                placement[ 1 ][ 1 ] = 1;
                placement[ 1 ][ 2 ] = 2;
                placement[ 2 ][ 2 ] = 3;
            }
        }
    }

    private void fillStartPoint ()
    {
        if ( figureType.equals ( FigureType.trident ) )
        {
            point = new Point ( Tetris.BLOCKS_IN_WIDTH / 2 - 2, -2 );
        }
        else if ( figureType.equals ( FigureType.line ) )
        {
            point = new Point ( Tetris.BLOCKS_IN_WIDTH / 2 - 2, -2 );
        }
        else if ( figureType.equals ( FigureType.lineL ) )
        {
            point = new Point ( Tetris.BLOCKS_IN_WIDTH / 2 - 1, -3 );
        }
        else if ( figureType.equals ( FigureType.lineR ) )
        {
            point = new Point ( Tetris.BLOCKS_IN_WIDTH / 2 - 2, -3 );
        }
        else if ( figureType.equals ( FigureType.block ) )
        {
            point = new Point ( Tetris.BLOCKS_IN_WIDTH / 2 - 1, -2 );
        }
        else if ( figureType.equals ( FigureType.blockL ) )
        {
            point = new Point ( Tetris.BLOCKS_IN_WIDTH / 2 - 2, -3 );
        }
        else if ( figureType.equals ( FigureType.blockR ) )
        {
            point = new Point ( Tetris.BLOCKS_IN_WIDTH / 2 - 2, -3 );
        }
    }

    private void clearPlacement ()
    {
        for ( int i = 0; i < placement.length; i++ )
        {
            for ( int j = 0; j < placement[ i ].length; j++ )
            {
                placement[ i ][ j ] = -1;
            }
        }
    }

    public void rotate ( GeneralPath terrain )
    {
        changeRotation ( 1 );
        fillPlacement ();
        if ( intersects ( terrain ) )
        {
            changeRotation ( -1 );
            fillPlacement ();
        }
    }

    private void changeRotation ( int change )
    {
        if ( change == 1 )
        {
            if ( rotation < 3 )
            {
                rotation++;
            }
            else
            {
                rotation = 0;
            }
        }
        else
        {
            if ( rotation > 0 )
            {
                rotation--;
            }
            else
            {
                rotation = 3;
            }
        }
    }

    public void moveLeft ( GeneralPath terrain )
    {
        point.x--;
        if ( intersects ( terrain ) )
        {
            point.x++;
        }
    }

    public void moveRight ( GeneralPath terrain )
    {
        point.x++;
        if ( intersects ( terrain ) )
        {
            point.x--;
        }
    }

    public void moveDown ()
    {
        point.y++;
    }

    public boolean canMoveDown ( GeneralPath terrain )
    {
        point.y++;
        boolean intersects = intersects ( terrain );
        point.y--;
        return !intersects;
    }

    public void paintFigure ( Graphics2D g2d )
    {
        Point start = new Point ( point.x * Tetris.BLOCK_SIDE + point.x, point.y * Tetris.BLOCK_SIDE + point.y );
        for ( int row = 0; row < placement.length; row++ )
        {
            for ( int col = 0; col < placement[ row ].length; col++ )
            {
                if ( placement[ row ][ col ] >= 0 )
                {
                    blocks[ placement[ row ][ col ] ].paintBlock ( g2d,
                            new Rectangle ( start.x + col * Tetris.BLOCK_SIDE + col, start.y + row * Tetris.BLOCK_SIDE + row,
                                    Tetris.BLOCK_SIDE, Tetris.BLOCK_SIDE ), rotation
                    );
                }
            }
        }
    }

    private List<List<Integer>> getPreviewData ()
    {
        List<List<Integer>> data = new ArrayList<List<Integer>> ();
        for ( int row = 0; row < placement.length; row++ )
        {
            data.add ( new ArrayList<Integer> () );
            for ( int col = 0; col < placement[ row ].length; col++ )
            {
                data.get ( row ).add ( placement[ row ][ col ] );
            }
        }

        // Checking rows
        for ( int row = data.size () - 1; row >= 0; row-- )
        {
            List<Integer> rowData = data.get ( row );
            for ( int col = 0; col < rowData.size (); col++ )
            {
                if ( rowData.get ( col ) >= 0 )
                {
                    break;
                }
                else if ( col == rowData.size () - 1 )
                {
                    data.remove ( row );
                }
            }
        }

        // Checking columns
        for ( int col = data.get ( 0 ).size () - 1; col >= 0; col-- )
        {
            for ( int row = 0; row < data.size (); row++ )
            {
                if ( data.get ( row ).get ( col ) >= 0 )
                {
                    break;
                }
                else if ( row == data.size () - 1 )
                {
                    //noinspection ForLoopReplaceableByForEach
                    for ( int r = 0; r < data.size (); r++ )
                    {
                        data.get ( r ).remove ( col );
                    }
                }
            }
        }

        return data;
    }

    public void paintFigurePreview ( Graphics2D g2d, JComponent component )
    {
        List<List<Integer>> previewData = getPreviewData ();
        int rows = previewData.size ();
        int cols = previewData.get ( 0 ).size ();

        int startX = component.getWidth () / 2 - ( cols * Tetris.BLOCK_SIDE + cols ) / 2;
        int startY = component.getHeight () / 2 - ( rows * Tetris.BLOCK_SIDE + rows ) / 2;
        for ( int row = 0; row < rows; row++ )
        {
            for ( int col = 0; col < cols; col++ )
            {
                if ( previewData.get ( row ).get ( col ) >= 0 )
                {
                    blocks[ previewData.get ( row ).get ( col ) ].paintBlock ( g2d,
                            new Rectangle ( startX + col * Tetris.BLOCK_SIDE + col, startY + row * Tetris.BLOCK_SIDE + row,
                                    Tetris.BLOCK_SIDE, Tetris.BLOCK_SIDE ), rotation
                    );
                }
            }
        }
    }

    private boolean intersects ( GeneralPath terrain )
    {
        Point start = new Point ( point.x * Tetris.BLOCK_SIDE + point.x, point.y * Tetris.BLOCK_SIDE + point.y );
        for ( int row = 0; row < placement.length; row++ )
        {
            for ( int col = 0; col < placement[ row ].length; col++ )
            {
                if ( placement[ row ][ col ] >= 0 && terrain.intersects (
                        new Rectangle ( start.x + col * Tetris.BLOCK_SIDE + col, start.y + row * Tetris.BLOCK_SIDE + row, Tetris.BLOCK_SIDE,
                                Tetris.BLOCK_SIDE )
                ) )
                {
                    return true;
                }
            }
        }
        return false;
    }

    public Map<Point, Block> getBlocks ()
    {
        Map<Point, Block> blocksMap = new HashMap<Point, Block> ();
        Point start = new Point ( point.x * Tetris.BLOCK_SIDE + point.x, point.y * Tetris.BLOCK_SIDE + point.y );
        for ( int row = 0; row < placement.length; row++ )
        {
            for ( int col = 0; col < placement[ row ].length; col++ )
            {
                if ( placement[ row ][ col ] >= 0 )
                {
                    blocks[ placement[ row ][ col ] ].setRotation ( rotation );
                    blocks[ placement[ row ][ col ] ].setBlockPoint ( new Point ( point.x + col, point.y + row ) );
                    blocksMap.put ( new Point ( start.x + col * Tetris.BLOCK_SIDE + col, start.y + row * Tetris.BLOCK_SIDE + row ),
                            blocks[ placement[ row ][ col ] ] );
                }
            }
        }
        return blocksMap;
    }
}
