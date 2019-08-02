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

package com.alee.extended.split;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

/**
 * Simple {@link IMultiSplitPaneDividerPainter} adapter class.
 * It is used to install simple non-specific painters into {@link WMultiSplitPaneDividerUI}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebMultiSplitPane">How to use WebMultiSplitPane</a>
 * @see WebMultiSplitPane
 */
public final class AdaptiveMultiSplitPaneDividerPainter<C extends WebMultiSplitPaneDivider, U extends WMultiSplitPaneDividerUI>
        extends AdaptivePainter<C, U> implements IMultiSplitPaneDividerPainter<C, U>
{
    /**
     * Constructs new {@link AdaptiveMultiSplitPaneDividerPainter} for the specified {@link Painter}.
     *
     * @param painter {@link Painter} to adapt
     */
    public AdaptiveMultiSplitPaneDividerPainter ( final Painter painter )
    {
        super ( painter );
    }
}