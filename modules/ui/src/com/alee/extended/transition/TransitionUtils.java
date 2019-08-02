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

package com.alee.extended.transition;

import com.alee.extended.transition.effects.Direction;
import com.alee.extended.transition.effects.blocks.BlockType;
import com.alee.extended.transition.effects.curtain.CurtainSlideDirection;
import com.alee.extended.transition.effects.curtain.CurtainType;
import com.alee.extended.transition.effects.slide.SlideType;
import com.alee.extended.transition.effects.zoom.ZoomType;
import com.alee.utils.MathUtils;

/**
 * This class provides a set of utilities for transition components.
 * This is a library utility class and its not intended for use outside of transition components.
 *
 * @author Mikle Garin
 */
public final class TransitionUtils
{
    /**
     * Returns actual direction for the specified direction type.
     *
     * @param direction direction to process
     * @return actual direction for the specified direction type
     */
    public static Direction getActualValue ( final Direction direction )
    {
        if ( direction.equals ( Direction.random ) )
        {
            final Direction[] directions = Direction.values ();
            return directions[ MathUtils.random ( directions.length - 3 ) + 3 ];
        }
        else if ( direction.equals ( Direction.horizontal ) )
        {
            return new Direction[]{ Direction.left, Direction.right }[ MathUtils.random ( 2 ) ];
        }
        else if ( direction.equals ( Direction.vertical ) )
        {
            return new Direction[]{ Direction.up, Direction.down }[ MathUtils.random ( 2 ) ];
        }
        else
        {
            return direction;
        }
    }

    /**
     * Returns actual slide direction for the specified slide direction type.
     *
     * @param slideDirection slide direction to process
     * @return actual slide direction for the specified slide direction type
     */
    public static CurtainSlideDirection getActualValue ( final CurtainSlideDirection slideDirection )
    {
        if ( slideDirection.equals ( CurtainSlideDirection.random ) )
        {
            final CurtainSlideDirection[] slideDirections = CurtainSlideDirection.values ();
            return slideDirections[ MathUtils.random ( slideDirections.length - 1 ) + 1 ];
        }
        else
        {
            return slideDirection;
        }
    }

    /**
     * Returns actual block type for the specified block type.
     *
     * @param blockType block type to process
     * @return actual block type for the specified block type
     */
    public static BlockType getActualValue ( final BlockType blockType )
    {
        if ( blockType.equals ( BlockType.random ) )
        {
            final BlockType[] blockTypes = BlockType.values ();
            return blockTypes[ MathUtils.random ( blockTypes.length - 1 ) + 1 ];
        }
        else
        {
            return blockType;
        }
    }

    /**
     * Returns actual curtain type for the specified curtain type.
     *
     * @param curtainType curtain type to process
     * @return actual curtain type for the specified curtain type
     */
    public static CurtainType getActualValue ( final CurtainType curtainType )
    {
        if ( curtainType.equals ( CurtainType.random ) )
        {
            final CurtainType[] curtainTypes = CurtainType.values ();
            return curtainTypes[ MathUtils.random ( curtainTypes.length - 1 ) + 1 ];
        }
        else
        {
            return curtainType;
        }
    }

    /**
     * Returns actual slide type for the specified slide type.
     *
     * @param slideType slide type to process
     * @return actual slide type for the specified slide type
     */
    public static SlideType getActualValue ( final SlideType slideType )
    {
        if ( slideType.equals ( SlideType.random ) )
        {
            final SlideType[] slideTypes = SlideType.values ();
            return slideTypes[ MathUtils.random ( slideTypes.length - 1 ) + 1 ];
        }
        else
        {
            return slideType;
        }
    }

    /**
     * Returns actual zoom type for the specified zoom type.
     *
     * @param zoomType zoom type to process
     * @return actual zoom type for the specified zoom type
     */
    public static ZoomType getActualValue ( final ZoomType zoomType )
    {
        if ( zoomType.equals ( ZoomType.random ) )
        {
            final ZoomType[] zoomTypes = ZoomType.values ();
            return zoomTypes[ MathUtils.random ( zoomTypes.length - 1 ) + 1 ];
        }
        else
        {
            return zoomType;
        }
    }
}