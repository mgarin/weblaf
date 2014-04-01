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

package com.alee.managers.style.skin.ninepatch;

import com.alee.extended.painter.AbstractPainter;
import com.alee.laf.scroll.ScrollBarButtonPainter;
import com.alee.laf.scroll.ScrollBarButtonType;

import javax.swing.*;
import java.awt.*;

/**
 * Base 9-patch painter for JScrollBar arrow buttons.
 *
 * @author Mikle Garin
 */

public class NPScrollBarButtonPainter<E extends AbstractButton> extends AbstractPainter<E> implements ScrollBarButtonPainter<E>
{
    /**
     * todo 1. Implement when ButtonPainter/WebButtonPainter will be added and available
     */

    @Override
    public void setButtonType ( final ScrollBarButtonType type )
    {
        // todo
    }

    @Override
    public void setScrollbar ( final JScrollBar scrollbar )
    {
        // todo
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c )
    {
        // todo
    }
}