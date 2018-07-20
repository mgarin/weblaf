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
 * Simple {@link StyledLabelPainter} adapter class.
 * It is used to install simple non-specific painters into {@link WStyledLabelUI}.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebStyledLabel">How to use WebStyledLabel</a>
 * @see WebStyledLabel
 */
public final class AdaptiveStyledLabelPainter<C extends WebStyledLabel, U extends WStyledLabelUI<C>>
        extends AdaptivePainter<C, U> implements IStyledLabelPainter<C, U>
{
    /**
     * Constructs new {@link AdaptiveStyledLabelPainter} for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveStyledLabelPainter ( final Painter painter )
    {
        super ( painter );
    }
}