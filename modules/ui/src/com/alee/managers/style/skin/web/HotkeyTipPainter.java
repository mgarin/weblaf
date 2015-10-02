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
 * Custom painter for HotkeyTipLabel component.
 *
 * @author Mikle Garin
 * @see com.alee.extended.painter.AbstractPainter
 * @see com.alee.extended.painter.Painter
 */

public class HotkeyTipPainter<T extends JLabel, U extends WebLabelUI> extends WebLabelPainter<T, U>
{
    /**
     * Style settings.
     */
    protected int round;

    @Override
    protected void paintBackground ( final Graphics2D g2d, final Rectangle bounds, final T label, final U ui )
    {
        final Object aa = GraphicsUtils.setupAntialias ( g2d );
        g2d.setColor ( label.getBackground () );
        g2d.fillRoundRect ( bounds.x, bounds.y, bounds.width, bounds.height, round * 2, round * 2 );
        GraphicsUtils.restoreAntialias ( g2d, aa );
    }
}