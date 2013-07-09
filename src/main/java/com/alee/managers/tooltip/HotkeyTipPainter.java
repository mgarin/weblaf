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

package com.alee.managers.tooltip;

import com.alee.extended.painter.DefaultPainter;
import com.alee.utils.LafUtils;

import java.awt.*;

/**
 * User: mgarin Date: 24.09.12 Time: 16:02
 */

public class HotkeyTipPainter extends DefaultPainter<HotkeyTipLabel>
{
    private static Color bg = new Color ( 255, 255, 255, 178 );
    private static int round = 4;
    private static Insets margin = new Insets ( 0, 3, 1, 3 );

    public HotkeyTipPainter ()
    {
        super ();
        setMargin ( margin );
    }

    public void paint ( Graphics2D g2d, Rectangle bounds, HotkeyTipLabel c )
    {
        Object aa = LafUtils.setupAntialias ( g2d );
        g2d.setColor ( bg );
        g2d.fillRoundRect ( bounds.x, bounds.y, bounds.width, bounds.height, round, round );
        LafUtils.restoreAntialias ( g2d, aa );
    }
}