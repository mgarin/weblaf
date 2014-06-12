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

package com.alee.managers.style.skin.web;

import javax.swing.*;
import java.awt.*;

/**
 * Web-style painter for WebVerticalLabel component.
 * It is used as WebVerticalLabelUI default painter.
 *
 * @author Mikle Garin
 */

public class WebVerticalLabelPainter<E extends JLabel> extends WebLabelPainter<E>
{
    /**
     * Runtime variables.
     */
    protected Rectangle verticalViewR = new Rectangle ();
    protected Rectangle verticalIconR = new Rectangle ();
    protected Rectangle verticalTextR = new Rectangle ();

    /**
     * {@inheritDoc}
     */
    @Override
    protected String layoutCL ( final E label, final FontMetrics fontMetrics, String text, final Icon icon, final Rectangle viewR,
                                final Rectangle iconR, final Rectangle textR )
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

    /**
     * Returns transposed rectangle.
     * If destination rectangle is null it will be created.
     *
     * @param from rectangle to transpose
     * @param to   destination rectangle
     * @return transposed rectangle
     */
    protected Rectangle transposeRectangle ( final Rectangle from, Rectangle to )
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

    /**
     * Returns rectangle copy.
     * If destination rectangle is null it will be created.
     *
     * @param from rectangle to copy
     * @param to   destination rectangle
     * @return rectangle copy
     */
    protected Rectangle copyRectangle ( final Rectangle from, Rectangle to )
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getPreferredSize ( final E label )
    {
        return transposeDimension ( super.getPreferredSize ( label ) );
    }

    /**
     * Returns transposed dimension.
     *
     * @param from dimension to transpose
     * @return transposed dimension
     */
    protected Dimension transposeDimension ( final Dimension from )
    {
        return new Dimension ( from.height, from.width );
    }
}