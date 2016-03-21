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

package com.alee.laf.tree;

import com.alee.painter.AbstractPainter;

import javax.swing.*;
import java.awt.*;

/**
 * Simple dual-background tree row painter.
 *
 * @author Mikle Garin
 */

public class AlternateTreeRowPainter<E extends JTree, U extends WebTreeUI> extends AbstractPainter<E, U> implements ITreeRowPainter<E, U>
{
    /**
     * Painted row index.
     */
    protected int row;

    /**
     * Odd rows background color.
     */
    protected Color oddColor;

    /**
     * Even rows background color.
     */
    protected Color evenColor;

    @Override
    public void prepareToPaint ( final int row )
    {
        this.row = row;
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        if ( row % 2 == 0 )
        {
            if ( oddColor != null )
            {
                paintRowBackground ( g2d, bounds, oddColor );
            }
        }
        else
        {
            if ( evenColor != null )
            {
                paintRowBackground ( g2d, bounds, evenColor );
            }
        }
    }

    /**
     * Paints singled-colored row background.
     *
     * @param g2d    graphics context
     * @param bounds row bounds
     * @param color  background color
     */
    protected void paintRowBackground ( final Graphics2D g2d, final Rectangle bounds, final Color color )
    {
        g2d.setPaint ( color );
        g2d.fillRect ( bounds.x, bounds.y, bounds.width, bounds.height );
    }
}