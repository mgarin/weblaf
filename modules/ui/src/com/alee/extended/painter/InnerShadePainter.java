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

package com.alee.extended.painter;

import com.alee.utils.NinePatchUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * @author Mikle Garin
 */

public class InnerShadePainter<E extends JComponent, U extends ComponentUI> extends NinePatchIconPainter<E, U>
{
    protected boolean paintBackground = true;
    protected int shadeWidth = 10;
    protected int round = 0;
    protected float shadeOpacity = 0.75f;
    protected boolean drawTop = true;
    protected boolean drawLeft = true;
    protected boolean drawBottom = true;
    protected boolean drawRight = true;

    protected int cachedShadeWidth = 10;
    protected int cachedRound = 0;
    protected float cachedShadeOpacity = 0.75f;

    public InnerShadePainter ()
    {
        super ();
    }

    public InnerShadePainter ( final int shadeWidth, final int round, final float shadeOpacity )
    {
        super ();
        this.shadeWidth = shadeWidth;
        this.round = round;
        this.shadeOpacity = shadeOpacity;
    }

    public InnerShadePainter ( final boolean drawTop, final boolean drawLeft, final boolean drawBottom, final boolean drawRight )
    {
        super ();
        this.drawTop = drawTop;
        this.drawLeft = drawLeft;
        this.drawBottom = drawBottom;
        this.drawRight = drawRight;
    }

    protected void updateNinePatchIcon ()
    {
        cachedShadeWidth = shadeWidth;
        cachedRound = round;
        cachedShadeOpacity = shadeOpacity;
        setNinePatchIcon ( NinePatchUtils.createInnerShadeIcon ( shadeWidth, round, shadeOpacity ) );
    }

    public int getShadeWidth ()
    {
        return shadeWidth;
    }

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

    public boolean isDrawTop ()
    {
        return drawTop;
    }

    public void setDrawTop ( final boolean drawTop )
    {
        this.drawTop = drawTop;
    }

    public boolean isDrawLeft ()
    {
        return drawLeft;
    }

    public void setDrawLeft ( final boolean drawLeft )
    {
        this.drawLeft = drawLeft;
    }

    public boolean isDrawBottom ()
    {
        return drawBottom;
    }

    public void setDrawBottom ( final boolean drawBottom )
    {
        this.drawBottom = drawBottom;
    }

    public boolean isDrawRight ()
    {
        return drawRight;
    }

    public void setDrawRight ( final boolean drawRight )
    {
        this.drawRight = drawRight;
    }

    public void setDrawSides ( final boolean drawTop, final boolean drawLeft, final boolean drawBottom, final boolean drawRight )
    {
        this.drawTop = drawTop;
        this.drawLeft = drawLeft;
        this.drawBottom = drawBottom;
        this.drawRight = drawRight;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Insets getMargin ()
    {
        return new Insets ( drawTop ? shadeWidth : 0, drawLeft ? shadeWidth : 0, drawBottom ? shadeWidth : 0, drawRight ? shadeWidth : 0 );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        // Background
        if ( paintBackground )
        {
            final RoundRectangle2D.Double s = new RoundRectangle2D.Double ( bounds.x, bounds.y, bounds.width, bounds.height, round, round );
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
            icon.paintIcon ( g2d, bounds.x - ( drawLeft ? 0 : shadeWidth ), bounds.y - ( drawTop ? 0 : shadeWidth ),
                    bounds.width + ( drawLeft ? 0 : shadeWidth ) + ( drawRight ? 0 : shadeWidth ),
                    bounds.height + ( drawTop ? 0 : shadeWidth ) + ( drawBottom ? 0 : shadeWidth ) );
        }
    }

    protected void paintBackground ( final Graphics2D g2d, final Rectangle bounds, final E c, final Shape shape )
    {
        g2d.setPaint ( c.getBackground () );
        g2d.fill ( shape );
    }
}