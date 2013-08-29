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

package com.alee.extended.label;

import com.alee.laf.label.WebLabelUI;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * User: mgarin Date: 11.10.11 Time: 13:16
 */

public class WebVerticalLabelUI extends WebLabelUI
{
    private boolean clockwise;

    private Rectangle verticalViewR = new Rectangle ();
    private Rectangle verticalIconR = new Rectangle ();
    private Rectangle verticalTextR = new Rectangle ();

    @SuppressWarnings ("UnusedParameters")
    public static ComponentUI createUI ( JComponent c )
    {
        return new WebVerticalLabelUI ();
    }

    public WebVerticalLabelUI ()
    {
        this.clockwise = false;
    }

    public WebVerticalLabelUI ( boolean clockwise )
    {
        this.clockwise = clockwise;
    }

    public boolean isClockwise ()
    {
        return clockwise;
    }

    public void setClockwise ( boolean clockwise )
    {
        this.clockwise = clockwise;
    }

    @Override
    public int getBaseline ( JComponent c, int width, int height )
    {
        super.getBaseline ( c, width, height );
        return -1;
    }

    @Override
    public Component.BaselineResizeBehavior getBaselineResizeBehavior ( JComponent c )
    {
        super.getBaselineResizeBehavior ( c );
        return Component.BaselineResizeBehavior.OTHER;
    }

    @Override
    protected String layoutCL ( JLabel label, FontMetrics fontMetrics, String text, Icon icon, Rectangle viewR, Rectangle iconR,
                                Rectangle textR )
    {
        verticalViewR = transposeRectangle ( viewR, verticalViewR );
        verticalIconR = transposeRectangle ( iconR, verticalIconR );
        verticalTextR = transposeRectangle ( textR, verticalTextR );

        text = super.layoutCL ( label, fontMetrics, text, icon, verticalViewR, verticalIconR, verticalTextR );

        copyRectangle ( verticalViewR, viewR );
        copyRectangle ( verticalIconR, iconR );
        copyRectangle ( verticalTextR, textR );

        return text;
    }

    @Override
    public void paint ( Graphics g, JComponent c )
    {
        Graphics2D g2 = ( Graphics2D ) g.create ();
        if ( c.getComponentOrientation ().isLeftToRight () ? clockwise : !clockwise )
        {
            g2.rotate ( Math.PI / 2, c.getSize ().width / 2, c.getSize ().width / 2 );
        }
        else
        {
            g2.rotate ( -Math.PI / 2, c.getSize ().height / 2, c.getSize ().height / 2 );
        }
        super.paint ( g2, c );
    }

    @Override
    public Dimension getPreferredSize ( JComponent c )
    {
        return transposeDimension ( super.getPreferredSize ( c ) );
    }

    @Override
    public Dimension getMaximumSize ( JComponent c )
    {
        return transposeDimension ( super.getMaximumSize ( c ) );
    }

    @Override
    public Dimension getMinimumSize ( JComponent c )
    {
        return transposeDimension ( super.getMinimumSize ( c ) );
    }

    private Dimension transposeDimension ( Dimension from )
    {
        return new Dimension ( from.height, from.width );
    }

    private Rectangle transposeRectangle ( Rectangle from, Rectangle to )
    {
        if ( to == null )
        {
            to = new Rectangle ();
        }
        to.x = from.y;
        to.y = from.x;
        to.width = from.height;
        to.height = from.width;
        return to;
    }

    private Rectangle copyRectangle ( Rectangle from, Rectangle to )
    {
        if ( to == null )
        {
            to = new Rectangle ();
        }
        to.x = from.x;
        to.y = from.y;
        to.width = from.width;
        to.height = from.height;
        return to;
    }
}
