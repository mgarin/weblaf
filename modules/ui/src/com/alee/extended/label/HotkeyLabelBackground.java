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

import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.background.AbstractBackground;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.awt.*;

/**
 * Custom background implementation for {@link WebHotkeyLabel}.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> background type
 * @author Mikle Garin
 * @see WebHotkeyLabel
 */
@XStreamAlias ( "HotkeyLabelBackground" )
public class HotkeyLabelBackground<C extends WebHotkeyLabel, D extends IDecoration<C, D>, I extends HotkeyLabelBackground<C, D, I>>
        extends AbstractBackground<C, D, I>
{
    /**
     * todo 1. Replace with proper border/background-based implementations later when different borders are supported
     */

    /**
     * Shape round.
     */
    @XStreamAsAttribute
    protected Integer round;

    /**
     * Border color.
     */
    @XStreamAsAttribute
    protected Color border;

    /**
     * Spacing color.
     */
    @XStreamAsAttribute
    protected Color spacing;

    /**
     * Background color.
     */
    @XStreamAsAttribute
    protected Color color;

    @Override
    public String getId ()
    {
        return id != null ? id : "hotkey-background";
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final C c, final D d, final Shape shape )
    {
        // White spacer
        g2d.setPaint ( spacing );
        g2d.fillRoundRect ( bounds.x, bounds.y, bounds.width, bounds.height - 1, round, round );

        // Background
        g2d.setPaint ( color );
        g2d.fillRect ( bounds.x + 3, bounds.y + 3, bounds.width - 6, bounds.height - 7 );

        // Border
        g2d.setPaint ( border );
        g2d.drawRoundRect ( bounds.x, bounds.y, bounds.width - 1, bounds.height - 2, round, round );
        g2d.drawRoundRect ( bounds.x, bounds.y, bounds.width - 1, bounds.height - 1, round, round );
    }
}