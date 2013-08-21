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
 * User: mgarin Date: 15.03.12 Time: 18:09
 */

// todo Requires a lot of additions (hideable sides, switchable background etc)
public class WebStylePainter<E extends JComponent> extends DefaultPainter<E>
{
    private boolean drawBackground = true;
    private int round = StyleConstants.smallRound;
    private boolean drawFocus = false;
    private int shadeWidth = StyleConstants.shadeWidth;
    private boolean fillBackground = true;
    private boolean webColored = true;

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
    }

    public int getRound ()
    {
        return round;
    }

    public void setRound ( int round )
    {
        this.round = round;
    }

    public boolean isDrawFocus ()
    {
        return drawFocus;
    }

    public void setDrawFocus ( boolean drawFocus )
    {
        this.drawFocus = drawFocus;
    }

    public int getShadeWidth ()
    {
        return shadeWidth;
    }

    public void setShadeWidth ( int shadeWidth )
    {
        this.shadeWidth = shadeWidth;
    }

    public boolean isFillBackground ()
    {
        return fillBackground;
    }

    public void setFillBackground ( boolean fillBackground )
    {
        this.fillBackground = fillBackground;
    }

    public boolean isWebColored ()
    {
        return webColored;
    }

    public void setWebColored ( boolean webColored )
    {
        this.webColored = webColored;
    }

    @Override
    public boolean isOpaque ( E c )
    {
        return false;
    }

    @Override
    public Insets getMargin ( E c )
    {
        return new Insets ( shadeWidth + 1, shadeWidth + 1, shadeWidth + 1, shadeWidth + 1 );
    }

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