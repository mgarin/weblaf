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

import com.alee.painter.SpecificPainter;

import javax.swing.*;
import java.awt.*;

/**
 * Base interface for JScrollBar component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Mikle Garin
 */

public interface IScrollBarPainter<E extends JScrollBar, U extends WebScrollBarUI> extends SpecificPainter<E, U>
{
    /**
     * Sets whether scroll bar thumb is being dragged or not.
     * This value is updated by WebScrollBarUI when drag event starts or ends.
     *
     * @param dragged whether scroll bar thumb is being dragged or not
     */
    public void setDragged ( boolean dragged );

    /**
     * Sets scroll bar track bounds.
     * This value is updated by WebScrollBarUI on each paint call to ensure that proper bounds presented.
     *
     * @param bounds new scroll bar track bounds
     */
    public void setTrackBounds ( Rectangle bounds );

    /**
     * Sets scroll bar thumb bounds.
     * This value is updated by WebScrollBarUI on each paint call to ensure that proper bounds presented.
     *
     * @param bounds new scroll bar thumb bounds
     */
    public void setThumbBounds ( Rectangle bounds );
}