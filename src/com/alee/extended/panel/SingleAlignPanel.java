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

package com.alee.extended.panel;

import com.alee.laf.panel.WebPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Custom panel that aligns contained component to single side.
 *
 * @author Mikle Garin
 */

public class SingleAlignPanel extends WebPanel implements SwingConstants
{
    /**
     * Constructs new single align panel.
     *
     * @param component component to align
     * @param side      side to which component should be aligned
     */
    public SingleAlignPanel ( final Component component, final int side )
    {
        this ( component, translateSide ( side ) );
    }

    /**
     * Constructs new single align panel.
     *
     * @param component component to align
     * @param side      side to which component should be aligned
     */
    public SingleAlignPanel ( final Component component, final String side )
    {
        super ( new BorderLayout ( 0, 0 ) );
        setOpaque ( false );
        add ( component, side );
    }

    /**
     * Returns side translated into BorderLayout constraint.
     *
     * @param side side constraint to translate
     * @return side translated into BorderLayout constraint
     */
    private static String translateSide ( final int side )
    {
        switch ( side )
        {
            case TOP:
            {
                return BorderLayout.NORTH;
            }
            case LEFT:
            case LEADING:
            {
                return BorderLayout.WEST;
            }
            case BOTTOM:
            {
                return BorderLayout.SOUTH;
            }
            case RIGHT:
            case TRAILING:
            {
                return BorderLayout.EAST;
            }
        }
        return BorderLayout.CENTER;
    }
}