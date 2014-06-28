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

package com.alee.laf.scroll;

import com.alee.extended.painter.Painter;

import javax.swing.*;

/**
 * Base interface for JScrollBar arrow button painters.
 *
 * @author Mikle Garin
 */

public interface ScrollBarButtonPainter<E extends AbstractButton> extends Painter<E>
{
    /**
     * todo 1. Extend ButtonPainter later, when button painter will be implemented
     */

    /**
     * Sets scroll bar button type.
     *
     * @param type scroll bar button type
     */
    public void setButtonType ( ScrollBarButtonType type );

    /**
     * Sets scroll bar which uses this button.
     *
     * @param scrollbar scroll bar which uses this button
     */
    public void setScrollbar ( JScrollBar scrollbar );
}