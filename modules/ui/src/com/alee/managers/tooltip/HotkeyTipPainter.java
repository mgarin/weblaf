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

import com.alee.global.StyleConstants;
import com.alee.laf.label.WebLabelUI;
import com.alee.managers.style.skin.web.WebLabelPainter;
import com.alee.utils.GraphicsUtils;

import java.awt.*;

/**
 * Custom painter for HotkeyTipLabel component.
 *
 * @author Mikle Garin
 * @see com.alee.extended.painter.AbstractPainter
 * @see com.alee.extended.painter.Painter
 */

public class HotkeyTipPainter<T extends HotkeyTipLabel, U extends WebLabelUI> extends WebLabelPainter<T, U>
{
    /**
     * Style settings.
     */
    protected int round = StyleConstants.smallRound;

    /**
     * Constructs new hotkey tip painter.
     */
    public HotkeyTipPainter ()
    {
        super ();
    }

    /**
     * Returns decoration round.
     *
     * @return decoration round
     */
    public int getRound ()
    {
        return round;
    }

    /**
     * Sets decoration round.
     *
     * @param round decoration round
     */
    public void setRound ( int round )
    {
        this.round = round;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final T c, final U ui )
    {
        // Painting custom background
        paintBackground ( g2d, bounds, c );

        // Painting label
        super.paint ( g2d, bounds, c, ui );
    }

    /**
     * Paints custom hotkey tip background.
     *
     * @param g2d    graphics context
     * @param bounds label bounds
     * @param c      label component
     */
    protected void paintBackground ( Graphics2D g2d, Rectangle bounds, T c )
    {
        final Object aa = GraphicsUtils.setupAntialias ( g2d );
        g2d.setColor ( c.getBackground () );
        g2d.fillRoundRect ( bounds.x, bounds.y, bounds.width, bounds.height, round * 2, round * 2 );
        GraphicsUtils.restoreAntialias ( g2d, aa );
    }
}