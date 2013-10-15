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

import com.alee.laf.StyleConstants;
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

    public void setDrawBackground ( boolean drawBackground )
    {
        this.drawBackground = drawBackground;
        fireRepaint ();
    }

    public int getRound ()
    {
        return round;
    }

    public void setRound ( int round )
    {
        this.round = round;
        fireRepaint ();
    }

    public boolean isDrawFocus ()
    {
        return drawFocus;
    }

    public void setDrawFocus ( boolean drawFocus )
    {
        this.drawFocus = drawFocus;
        fireRepaint ();
    }

    public int getShadeWidth ()
    {
        return shadeWidth;
    }

    public void setShadeWidth ( int shadeWidth )
    {
        this.shadeWidth = shadeWidth;
        fireUpdate ();
    }

    public boolean isFillBackground ()
    {
        return fillBackground;
    }

    public void setFillBackground ( boolean fillBackground )
    {
        this.fillBackground = fillBackground;
        fireRepaint ();
    }

    public boolean isWebColored ()
    {
        return webColored;
    }

    public void setWebColored ( boolean webColored )
    {
        this.webColored = webColored;
        fireRepaint ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOpaque ( E c )
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Insets getMargin ( E c )
    {
        return new Insets ( shadeWidth + 1, shadeWidth + 1, shadeWidth + 1, shadeWidth + 1 );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint ( Graphics2D g2d, Rectangle bounds, E c )
    {
        if ( drawBackground )
        {
            LafUtils.drawWebStyle ( g2d, c, drawFocus && c.isFocusOwner () ? StyleConstants.fieldFocusColor : StyleConstants.shadeColor,
                    shadeWidth, round, fillBackground, webColored );
        }
    }
}