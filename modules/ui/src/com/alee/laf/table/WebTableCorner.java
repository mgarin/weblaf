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

package com.alee.laf.table;

import com.alee.utils.CompareUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 22.08.11 Time: 13:07
 */

public class WebTableCorner extends JComponent
{
    // todo Create ui and painter

    private boolean right;

    public WebTableCorner ( final boolean right )
    {
        super ();
        this.right = right;
        SwingUtils.setOrientation ( this );
    }

    @Override
    protected void paintComponent ( final Graphics g )
    {
        super.paintComponent ( g );

        final Graphics2D g2d = ( Graphics2D ) g;

        // todo Proper painting for RTL
        // boolean ltr = getComponentOrientation ().isLeftToRight ();

        // Highlight
        g2d.setPaint ( WebTableStyle.headerTopLineColor );
        g2d.drawLine ( 0, 0, getWidth () - 1, 0 );

        // Background
        g2d.setPaint ( createBackgroundPaint ( 0, 1, 0, getHeight () - 1 ) );
        g2d.fillRect ( 0, 1, getWidth (), getHeight () - 1 );

        // Bottom line
        g2d.setColor ( WebTableStyle.headerBottomLineColor );
        g2d.drawLine ( 0, getHeight () - 1, getWidth () - 1, getHeight () - 1 );

        // Right line
        if ( right )
        {
            g2d.setColor ( WebTableStyle.gridColor );
            g2d.drawLine ( 0, 2, 0, getHeight () - 4 );
        }
        else
        {
            g2d.setColor ( WebTableStyle.gridColor );
            g2d.drawLine ( getWidth () - 1, 2, getWidth () - 1, getHeight () - 4 );
        }
    }

    protected Paint createBackgroundPaint ( final int x1, final int y1, final int x2, final int y2 )
    {
        final Color topBgColor = WebTableStyle.headerTopBgColor;
        final Color bottomBgColor = WebTableStyle.headerBottomBgColor;

        if ( bottomBgColor == null || CompareUtils.equals ( topBgColor, bottomBgColor ) )
        {
            return topBgColor;
        }
        else
        {
            return new GradientPaint ( x1, y1, topBgColor, x2, y2, bottomBgColor );
        }
    }
}