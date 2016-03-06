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

import com.alee.laf.label.WebLabelUI;
import com.alee.utils.GraphicsUtils;

import javax.swing.*;
import java.awt.*;


/**
 * Custom tag label painter.
 *
 * @author Mikle Garin
 */

public class TagLabelPainter<E extends JLabel, U extends WebLabelUI> extends WebLabelPainter<E, U>
{
    /**
     * Style settings.
     */
    protected int round;
    protected Color border;
    protected Color background;

    @Override
    protected void paintBackground ( final Graphics2D g2d, final Rectangle bounds, final E label, final U ui )
    {
        final Object aa = GraphicsUtils.setupAntialias ( g2d );
        g2d.setColor ( background );
        g2d.fillRoundRect ( bounds.x, bounds.y, bounds.width, bounds.height, round * 2, round * 2 );
        g2d.setColor (border );
        g2d.drawRoundRect ( bounds.x, bounds.y, bounds.width - 1, bounds.height - 1, round * 2, round * 2 );
        GraphicsUtils.restoreAntialias ( g2d, aa );
    }
}