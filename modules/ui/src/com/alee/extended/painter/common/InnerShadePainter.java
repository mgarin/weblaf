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

package com.alee.extended.painter.common;

import com.alee.extended.painter.PartialDecoration;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.NinePatchUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * @author Mikle Garin
 */

public class InnerShadePainter<E extends JComponent, U extends ComponentUI> extends NinePatchIconPainter<E, U> implements PartialDecoration
{
    protected boolean paintBackground = true;
    protected int shadeWidth = 10;
    protected int round = 0;
    protected float shadeOpacity = 0.75f;
    protected boolean paintTop = true;
    protected boolean paintLeft = true;
    protected boolean paintBottom = true;
    protected boolean paintRight = true;

    protected int cachedShadeWidth = 10;
    protected int cachedRound = 0;
    protected float cachedShadeOpacity = 0.75f;

    public InnerShadePainter ()
    {
        super ();
    }

    protected void updateNinePatchIcon ()
    {
        cachedShadeWidth = shadeWidth;
        cachedRound = round;
        cachedShadeOpacity = shadeOpacity;
        setNinePatchIcon ( NinePatchUtils.createInnerShadeIcon ( shadeWidth, round, shadeOpacity ) );
    }

    @Override
    public int getShadeWidth ()
    {
        return shadeWidth;
    }

    @Override
    public void setShadeWidth ( final int shadeWidth )
    {
        this.shadeWidth = shadeWidth;
    }

    public int getRound ()
    {
        return round;
    }

    public void setRound ( final int round )
    {
        this.round = round;
    }

    public float getShadeOpacity ()
    {
        return shadeOpacity;
    }

    public void setShadeOpacity ( final float shadeOpacity )
    {
        this.shadeOpacity = shadeOpacity;
    }

    @Override
    public boolean isUndecorated ()
    {
        return false;
    }

    @Override
    public void setUndecorated ( final boolean undecorated )
    {
        // This painter doesn't support undecorated state
    }

    public boolean isPaintTop ()
    {
        return paintTop;
    }

    @Override
    public void setPaintTop ( final boolean top )
    {
        this.paintTop = top;
    }

    public boolean isPaintLeft ()
    {
        return paintLeft;
    }

    @Override
    public void setPaintLeft ( final boolean left )
    {
        this.paintLeft = left;
    }

    public boolean isPaintBottom ()
    {
        return paintBottom;
    }

    @Override
    public void setPaintBottom ( final boolean bottom )
    {
        this.paintBottom = bottom;
    }

    public boolean isPaintRight ()
    {
        return paintRight;
    }

    @Override
    public void setPaintRight ( final boolean right )
    {
        this.paintRight = right;
    }

    @Override
    public void setPaintSides ( final boolean top, final boolean left, final boolean bottom, final boolean right )
    {
        setPaintTop ( top );
        setPaintLeft ( left );
        setPaintBottom ( bottom );
        setPaintRight ( right );
    }

    @Override
    public void setPaintTopLine ( final boolean top )
    {
        // This painter doesn't support side lines yet
    }

    @Override
    public void setPaintLeftLine ( final boolean left )
    {
        // This painter doesn't support side lines yet
    }

    @Override
    public void setPaintBottomLine ( final boolean bottom )
    {
        // This painter doesn't support side lines yet
    }

    @Override
    public void setPaintRightLine ( final boolean right )
    {
        // This painter doesn't support side lines yet
    }

    @Override
    public void setPaintSideLines ( final boolean top, final boolean left, final boolean bottom, final boolean right )
    {
        setPaintTopLine ( top );
        setPaintLeftLine ( left );
        setPaintBottomLine ( bottom );
        setPaintRightLine ( right );
    }

    @Override
    public Insets getBorders ()
    {
        return new Insets ( paintTop ? shadeWidth : 0, paintLeft ? shadeWidth : 0, paintBottom ? shadeWidth : 0,
                paintRight ? shadeWidth : 0 );
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        final Object aa = GraphicsUtils.setupAntialias ( g2d );

        // Background
        if ( paintBackground )
        {
            final RoundRectangle2D.Double s =
                    new RoundRectangle2D.Double ( bounds.x, bounds.y, bounds.width, bounds.height, round * 2, round * 2 );
            paintBackground ( g2d, bounds, c, s );
        }

        // Updating icon dynamically only when actually needed
        if ( cachedShadeWidth != shadeWidth || cachedRound != round || cachedShadeOpacity != shadeOpacity || icon == null )
        {
            updateNinePatchIcon ();
        }

        // Painting requested sides
        if ( icon != null )
        {
            icon.setComponent ( c );
            icon.paintIcon ( g2d, bounds.x - ( paintLeft ? 0 : shadeWidth ), bounds.y - ( paintTop ? 0 : shadeWidth ),
                    bounds.width + ( paintLeft ? 0 : shadeWidth ) + ( paintRight ? 0 : shadeWidth ),
                    bounds.height + ( paintTop ? 0 : shadeWidth ) + ( paintBottom ? 0 : shadeWidth ) );
        }

        GraphicsUtils.restoreAntialias ( g2d, aa );
    }

    protected void paintBackground ( final Graphics2D g2d, final Rectangle bounds, final E c, final Shape shape )
    {
        g2d.setPaint ( c.getBackground () );
        g2d.fill ( shape );
    }
}