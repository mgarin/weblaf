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

package com.alee.extended.label;

import com.alee.extended.painter.AbstractPainter;

import javax.swing.*;
import java.awt.*;

/**
 * Custom painter for WebHotkeyLabel component.
 *
 * @author Mikle Garin
 * @see AbstractPainter
 * @see com.alee.extended.painter.Painter
 */

public class HotkeyPainter extends AbstractPainter<JComponent>
{
    /**
     * Used colors.
     */
    public static Color border = new Color ( 204, 204, 204 );
    public static Color bg = new Color ( 247, 247, 247 );
    public static Dimension size = new Dimension ( 6, 7 );
    public static Insets margin = new Insets ( 2, 6, 3, 6 );

    /**
     * Constructs new hotkey painter.
     */
    public HotkeyPainter ()
    {
        super ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        return size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Insets getMargin ( final JComponent c )
    {
        return margin;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final JComponent c )
    {
        // White spacer
        g2d.setPaint ( Color.WHITE );
        g2d.fillRoundRect ( bounds.x, bounds.y, bounds.width, bounds.height - 1, 6, 6 );

        // Background
        g2d.setPaint ( bg );
        g2d.fillRect ( bounds.x + 3, bounds.y + 3, bounds.width - 6, bounds.height - 7 );

        // Border
        g2d.setPaint ( border );
        g2d.drawRoundRect ( bounds.x, bounds.y, bounds.width - 1, bounds.height - 2, 6, 6 );
        g2d.drawRoundRect ( bounds.x, bounds.y, bounds.width - 1, bounds.height - 1, 6, 6 );
    }
}