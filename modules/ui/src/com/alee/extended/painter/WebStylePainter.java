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

import com.alee.global.StyleConstants;
import com.alee.utils.LafUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Custom web-style painter for basic elements like panels, buttons e.t.c.
 *
 * @param <E> component type
 * @author Mikle Garin
 * @see AbstractPainter
 * @see Painter
 */

public class WebStylePainter<E extends JComponent> extends AbstractPainter<E>
{
    /**
     * todo 1. Implement all required methods (for e.g. - side hiding)
     */

    protected boolean drawBackground = true;
    protected int round = StyleConstants.smallRound;
    protected boolean drawFocus = false;
    protected int shadeWidth = StyleConstants.shadeWidth;
    protected boolean fillBackground = true;
    protected boolean webColored = true;

    public WebStylePainter ()
    {
        super ();
    }

    public boolean isDrawBackground ()
    {
        return drawBackground;
    }

    public void setDrawBackground ( final boolean drawBackground )
    {
        this.drawBackground = drawBackground;
        repaint ();
    }

    public int getRound ()
    {
        return round;
    }

    public void setRound ( final int round )
    {
        this.round = round;
        repaint ();
    }

    public boolean isDrawFocus ()
    {
        return drawFocus;
    }

    public void setDrawFocus ( final boolean drawFocus )
    {
        this.drawFocus = drawFocus;
        repaint ();
    }

    public int getShadeWidth ()
    {
        return shadeWidth;
    }

    public void setShadeWidth ( final int shadeWidth )
    {
        this.shadeWidth = shadeWidth;
        updateAll ();
    }

    public boolean isFillBackground ()
    {
        return fillBackground;
    }

    public void setFillBackground ( final boolean fillBackground )
    {
        this.fillBackground = fillBackground;
        repaint ();
    }

    public boolean isWebColored ()
    {
        return webColored;
    }

    public void setWebColored ( final boolean webColored )
    {
        this.webColored = webColored;
        repaint ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Insets getMargin ( final E c )
    {
        return new Insets ( shadeWidth + 1, shadeWidth + 1, shadeWidth + 1, shadeWidth + 1 );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c )
    {
        if ( drawBackground )
        {
            LafUtils.drawWebStyle ( g2d, c, drawFocus && c.isFocusOwner () ? StyleConstants.fieldFocusColor : StyleConstants.shadeColor,
                    shadeWidth, round, fillBackground, webColored );
        }
    }
}