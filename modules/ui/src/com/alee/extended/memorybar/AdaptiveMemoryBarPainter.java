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

package com.alee.extended.memorybar;

import com.alee.api.annotations.NotNull;
import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

/**
 * Simple {@link IMemoryBarPainter} adapter class.
 * It is used to install simple non-specific painters into {@link WMemoryBarUI}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Mikle Garin
 */
public final class AdaptiveMemoryBarPainter<C extends WebMemoryBar, U extends WMemoryBarUI<C>> extends AdaptivePainter<C, U>
        implements IMemoryBarPainter<C, U>
{
    /**
     * Constructs new {@link AdaptiveMemoryBarPainter} for the specified {@link Painter}.
     *
     * @param painter {@link Painter} to adapt
     */
    public AdaptiveMemoryBarPainter ( @NotNull final Painter painter )
    {
        super ( painter );
    }
}