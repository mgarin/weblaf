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

import com.alee.extended.painter.AbstractPainter;
import com.alee.global.StyleConstants;
import com.alee.managers.style.skin.web.WebLabelPainter;
import com.alee.utils.GraphicsUtils;

import java.awt.*;

/**
 * Custom painter for HotkeyTipLabel component.
 *
 * @author Mikle Garin
 * @see AbstractPainter
 * @see com.alee.extended.painter.Painter
 */

public class HotkeyTipPainter<T extends HotkeyTipLabel> extends WebLabelPainter<T>
{
    /**
     * Style constants.
     */
    public static Color bg = new Color ( 255, 255, 255, 178 );
    public static int round = StyleConstants.smallRound;

    /**
     * Constructs new hotkey tip painter.
     */
    public HotkeyTipPainter ()
    {
        super ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final T c )
    {
        final Object aa = GraphicsUtils.setupAntialias ( g2d );
        g2d.setColor ( bg );
        g2d.fillRoundRect ( bounds.x, bounds.y, bounds.width, bounds.height, round * 2, round * 2 );
        GraphicsUtils.restoreAntialias ( g2d, aa );

        super.paint ( g2d, bounds, c );
    }
}