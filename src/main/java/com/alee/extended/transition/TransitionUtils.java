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
 * User: mgarin Date: 19.11.12 Time: 17:06
 */

public class TransitionUtils
{
    public static Direction getActualValue ( Direction direction )
    {
        if ( direction.equals ( Direction.random ) )
        {
            Direction[] directions = Direction.values ();
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

    public static CurtainSlideDirection getActualValue ( CurtainSlideDirection slideDirection )
    {
        if ( slideDirection.equals ( CurtainSlideDirection.random ) )
        {
            CurtainSlideDirection[] slideDirections = CurtainSlideDirection.values ();
            return slideDirections[ MathUtils.random ( slideDirections.length - 1 ) + 1 ];
        }
        else
        {
            return slideDirection;
        }
    }

    public static BlockType getActualValue ( BlockType blockType )
    {
        if ( blockType.equals ( BlockType.random ) )
        {
            BlockType[] blockTypes = BlockType.values ();
            return blockTypes[ MathUtils.random ( blockTypes.length - 1 ) + 1 ];
        }
        else
        {
            return blockType;
        }
    }

    public static CurtainType getActualValue ( CurtainType curtainType )
    {
        if ( curtainType.equals ( CurtainType.random ) )
        {
            CurtainType[] curtainTypes = CurtainType.values ();
            return curtainTypes[ MathUtils.random ( curtainTypes.length - 1 ) + 1 ];
        }
        else
        {
            return curtainType;
        }
    }

    public static SlideType getActualValue ( SlideType slideType )
    {
        if ( slideType.equals ( SlideType.random ) )
        {
            SlideType[] slideTypes = SlideType.values ();
            return slideTypes[ MathUtils.random ( slideTypes.length - 1 ) + 1 ];
        }
        else
        {
            return slideType;
        }
    }

    public static ZoomType getActualValue ( ZoomType zoomType )
    {
        if ( zoomType.equals ( ZoomType.random ) )
        {
            ZoomType[] zoomTypes = ZoomType.values ();
            return zoomTypes[ MathUtils.random ( zoomTypes.length - 1 ) + 1 ];
        }
        else
        {
            return zoomType;
        }
    }
}
