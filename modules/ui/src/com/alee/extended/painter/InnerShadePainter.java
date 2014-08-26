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
import java.awt.*;

/**
 * @author Mikle Garin
 */

public class InnerShadePainter<E extends JComponent> extends NinePatchIconPainter<E>
{
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

    public InnerShadePainter<E> setShadeWidth ( final int shadeWidth )
    {
        this.shadeWidth = shadeWidth;
        return this;
    }

    public int getRound ()
    {
        return round;
    }

    public InnerShadePainter<E> setRound ( final int round )
    {
        this.round = round;
        return this;
    }

    public float getShadeOpacity ()
    {
        return shadeOpacity;
    }

    public InnerShadePainter<E> setShadeOpacity ( final float shadeOpacity )
    {
        this.shadeOpacity = shadeOpacity;
        return this;
    }

    public boolean isDrawTop ()
    {
        return drawTop;
    }

    public InnerShadePainter<E> setDrawTop ( final boolean drawTop )
    {
        this.drawTop = drawTop;
        return this;
    }

    public boolean isDrawLeft ()
    {
        return drawLeft;
    }

    public InnerShadePainter<E> setDrawLeft ( final boolean drawLeft )
    {
        this.drawLeft = drawLeft;
        return this;
    }

    public boolean isDrawBottom ()
    {
        return drawBottom;
    }

    public InnerShadePainter<E> setDrawBottom ( final boolean drawBottom )
    {
        this.drawBottom = drawBottom;
        return this;
    }

    public boolean isDrawRight ()
    {
        return drawRight;
    }

    public InnerShadePainter<E> setDrawRight ( final boolean drawRight )
    {
        this.drawRight = drawRight;
        return this;
    }

    public InnerShadePainter<E> setDrawSides ( final boolean drawTop, final boolean drawLeft, final boolean drawBottom,
                                               final boolean drawRight )
    {
        this.drawTop = drawTop;
        this.drawLeft = drawLeft;
        this.drawBottom = drawBottom;
        this.drawRight = drawRight;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Insets getMargin ( final E c )
    {
        return new Insets ( drawTop ? shadeWidth : 0, drawLeft ? shadeWidth : 0, drawBottom ? shadeWidth : 0, drawRight ? shadeWidth : 0 );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c )
    {
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
}