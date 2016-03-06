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

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

/**
 * Simple StyledLabelPainter adapter class.
 * It is used to install simple non-specific painters into WebStyledLabelUI.
 *
 * @author Mikle Garin
 */

public final class AdaptiveStyledLabelPainter<E extends WebStyledLabel, U extends WebStyledLabelUI> extends AdaptivePainter<E, U>
        implements IStyledLabelPainter<E, U>
{
    /**
     * Constructs new AdaptiveLabelPainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveStyledLabelPainter ( final Painter painter )
    {
        super ( painter );
    }

    @Override
    public void updateTextRanges ()
    {
        // Ignore this method in adaptive class
    }
}