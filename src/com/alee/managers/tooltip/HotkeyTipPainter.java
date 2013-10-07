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
import com.alee.laf.StyleConstants;
import com.alee.utils.LafUtils;

import java.awt.*;

/**
 * Custom painter for HotkeyTipLabel component.
 *
 * @author Mikle Garin
 */

public class HotkeyTipPainter extends DefaultPainter<HotkeyTipLabel>
{
    /**
     * Style constants.
     */
    protected static Color bg = new Color ( 255, 255, 255, 178 );
    protected static int round = StyleConstants.smallRound;

    /**
     * Constructs new hotkey tip painter.
     */
    public HotkeyTipPainter ()
    {
        super ();
        setMargin ( new Insets ( 0, 3, 1, 3 ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint ( Graphics2D g2d, Rectangle bounds, HotkeyTipLabel c )
    {
        Object aa = LafUtils.setupAntialias ( g2d );
        g2d.setColor ( bg );
        g2d.fillRoundRect ( bounds.x, bounds.y, bounds.width, bounds.height, round * 2, round * 2 );
        LafUtils.restoreAntialias ( g2d, aa );
    }
}