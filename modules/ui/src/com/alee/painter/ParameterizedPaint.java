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

package com.alee.painter;

import com.alee.api.annotations.NotNull;
import com.alee.managers.style.Bounds;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * Can be implemented by {@link SpecificPainter} to provide methods for paint parameters provision.
 *
 * @param <P> {@link PaintParameters} type
 * @author Mikle Garin
 */
public interface ParameterizedPaint<P extends PaintParameters>
{
    /**
     * Provides {@link PaintParameters} used for paint operation.
     * It is always called right before {@link Painter#paint(Graphics2D, JComponent, ComponentUI, Bounds)}.
     *
     * @param parameters {@link PaintParameters}
     */
    public void prepareToPaint ( @NotNull P parameters );

    /**
     * Cleans up {@link PaintParameters} used for paint operation.
     * It is always called right after {@link Painter#paint(Graphics2D, JComponent, ComponentUI, Bounds)}.
     */
    public void cleanupAfterPaint ();
}